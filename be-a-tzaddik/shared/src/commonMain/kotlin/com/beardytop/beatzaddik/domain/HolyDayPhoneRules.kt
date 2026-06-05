package com.beardytop.beatzaddik.domain

enum class HolyDayNoticeKind {
    SHABBAT,
    YOM_TOV
}

/** Today screen card when the checklist should not be used on the phone. */
data class HolyDayPhoneNotice(
    val kind: HolyDayNoticeKind,
    val title: String,
    val message: String,
    val footer: String
)

object HolyDayPhoneRules {

    /** Civil Saturday (not erev) — for prep/seasonal logic, not phone-blocking windows. */
    fun isShabbatMelachaDay(cal: DayInfo): Boolean =
        cal.isShabbat && !cal.isErevShabbat

    /**
     * Shabbat melacha hours: from 1 minute before Friday sunset until Saturday tzeit.
     * After tzeit the checklist and app are usable again for Motzei Shabbat
     * (unless Yom Tov begins that night — then [ElectronicsRestEvaluator] keeps the pause).
     */
    fun isShabbatMelachaWindow(cal: DayInfo, nowMillis: Long): Boolean {
        if (!isShabbatMelachaDay(cal)) return false
        return !isPastTzeit(cal, nowMillis)
    }

    /**
     * Outside Israel, the second day of a two-day Yom Tov (e.g. 2nd day Rosh Hashana,
     * 8th day Pesach in the Diaspora).
     */
    fun isChutzLaaretzSecondYomTovDay(profile: UserProfile, cal: DayInfo): Boolean {
        if (profile.isInIsrael) return false
        if (!cal.isYomTovAssurBemelacha) return false
        return cal.yesterdayWasYomTovAssurBemelacha
    }

    fun isChutzLaaretzSecondYomTovWindow(
        profile: UserProfile,
        cal: DayInfo,
        nowMillis: Long,
    ): Boolean {
        if (!isChutzLaaretzSecondYomTovDay(profile, cal)) return false
        return !isPastTzeit(cal, nowMillis)
    }

    fun shouldHideChecklist(profile: UserProfile, cal: DayInfo, nowMillis: Long): Boolean =
        isShabbatMelachaWindow(cal, nowMillis) ||
            isChutzLaaretzSecondYomTovWindow(profile, cal, nowMillis)

    fun phoneNotice(profile: UserProfile, cal: DayInfo, nowMillis: Long): HolyDayPhoneNotice? = when {
        isChutzLaaretzSecondYomTovWindow(profile, cal, nowMillis) ->
            cal.yomTovHolidayName?.let(::yomTovNotice)
        isShabbatMelachaWindow(cal, nowMillis) -> shabbatNotice()
        else -> null
    }

    private fun isPastTzeit(cal: DayInfo, nowMillis: Long): Boolean {
        val tzeit = cal.zmanim?.tzeitMillis ?: return false
        return nowMillis >= tzeit
    }

    private fun shabbatNotice() = HolyDayPhoneNotice(
        kind = HolyDayNoticeKind.SHABBAT,
        title = "Shabbat Shalom",
        message = "Today is Shabbat. Please put away your phone and keep the day holy — " +
            "pray, learn Torah, enjoy Shabbat meals, and rest. " +
            "Melacha (forbidden work on Shabbat) includes most phone and device use — ask your rav if unsure. " +
            "This app is for weekdays and erev Shabbat preparation, not for use during Shabbat.",
        footer = "Close this app and enjoy a peaceful, screen-free Shabbat."
    )

    private fun yomTovNotice(holidayName: String) = HolyDayPhoneNotice(
        kind = HolyDayNoticeKind.YOM_TOV,
        title = holidayName,
        message = yomTovMessage(holidayName) +
            " This app is for weekdays and erev chag preparation, not for use on Yom Tov.",
        footer = "Close this app and keep the festival day holy."
    )
}
