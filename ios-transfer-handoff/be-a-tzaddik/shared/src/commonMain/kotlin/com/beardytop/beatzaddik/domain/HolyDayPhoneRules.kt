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

    fun isShabbatMelachaDay(cal: DayInfo): Boolean =
        cal.isShabbat && !cal.isErevShabbat

    /**
     * Outside Israel, the second day of a two-day Yom Tov (e.g. 2nd day Rosh Hashana,
     * 8th day Pesach in the Diaspora).
     */
    fun isChutzLaaretzSecondYomTovDay(profile: UserProfile, cal: DayInfo): Boolean {
        if (profile.isInIsrael) return false
        if (!cal.isYomTovAssurBemelacha) return false
        return cal.yesterdayWasYomTovAssurBemelacha
    }

    fun shouldHideChecklist(profile: UserProfile, cal: DayInfo): Boolean =
        isShabbatMelachaDay(cal) || isChutzLaaretzSecondYomTovDay(profile, cal)

    fun phoneNotice(profile: UserProfile, cal: DayInfo): HolyDayPhoneNotice? = when {
        isChutzLaaretzSecondYomTovDay(profile, cal) ->
            cal.yomTovHolidayName?.let(::yomTovNotice)
        isShabbatMelachaDay(cal) -> shabbatNotice()
        else -> null
    }

    private fun shabbatNotice() = HolyDayPhoneNotice(
        kind = HolyDayNoticeKind.SHABBAT,
        title = "Shabbat Shalom",
        message = "Today is Shabbat. Please put away your phone and keep the day holy — " +
            "pray, learn Torah, enjoy Shabbat meals, and rest. " +
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
