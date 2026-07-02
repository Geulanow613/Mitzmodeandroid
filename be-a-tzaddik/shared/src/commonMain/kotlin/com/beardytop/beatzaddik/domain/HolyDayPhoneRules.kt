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
    /** Args for `{holidayName}` etc. placeholders in [message] — fill via rememberAppTranslatedTemplate. */
    val messageArgs: Map<String, String> = emptyMap(),
    val footer: String
)

object HolyDayPhoneRules {

    /** Civil Saturday (not erev) — for prep/seasonal logic, not phone-blocking windows. */
    fun isShabbatMelachaDay(cal: DayInfo): Boolean =
        cal.isShabbat && !cal.isErevShabbat

    /**
     * Shabbat melacha hours: from Friday sunset through Saturday tzeit.
     * After tzeit the checklist is usable again for Motzei Shabbat unless Yom Tov
     * continues (handled by [isYomTovMelachaWindow]).
     */
    fun isShabbatMelachaWindow(cal: DayInfo, nowMillis: Long): Boolean {
        if (isShabbatMelachaDay(cal)) {
            return !isPastTzeit(cal, nowMillis)
        }
        if (cal.isErevShabbat) {
            val sunset = cal.zmanim?.sunsetMillis ?: return false
            return nowMillis >= sunset
        }
        return false
    }

    /**
     * Any calendar day with full Yom Tov melacha restrictions — through tzeit that night.
     * Diaspora: both days at the start/end of Pesach & Sukkot, both days of Shavuot, etc.
     * Israel: single Yom Tov days only (Chol HaMoed is excluded via [DayInfo.isYomTovAssurBemelacha]).
     */
    fun isYomTovMelachaWindow(cal: DayInfo, nowMillis: Long): Boolean {
        if (!cal.isYomTovAssurBemelacha) return false
        return !isPastTzeit(cal, nowMillis)
    }

    fun shouldHideChecklist(profile: UserProfile, cal: DayInfo, nowMillis: Long): Boolean =
        isShabbatMelachaWindow(cal, nowMillis) ||
            isYomTovMelachaWindow(cal, nowMillis)

    fun phoneNotice(profile: UserProfile, cal: DayInfo, nowMillis: Long): HolyDayPhoneNotice? = when {
        isShabbatMelachaWindow(cal, nowMillis) -> shabbatNotice()
        isYomTovMelachaWindow(cal, nowMillis) ->
            cal.yomTovHolidayName?.let(::yomTovNotice)
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
            "Melacha (forbidden labor) applies on Shabbat, including most phone and device use. " +
            "This app is for weekdays and erev Shabbat preparation, not for use during Shabbat.",
        footer = "Put away your phone and enjoy a peaceful, screen-free Shabbat."
    )

    private fun yomTovNotice(holidayName: String) = HolyDayPhoneNotice(
        kind = HolyDayNoticeKind.YOM_TOV,
        title = holidayName,
        message = yomTovMessageTemplate() +
            " This app is for weekdays and erev chag preparation, not for use on Yom Tov.",
        messageArgs = mapOf("holidayName" to holidayName),
        footer = "Put away your phone and keep the festival day holy."
    )
}
