package com.beardytop.beatzaddik.domain

enum class HolyDayNoticeKind {
    SHABBAT,
    YOM_TOV,
    /** Banner only — checklist stays usable until [HIDE_BEFORE_SUNSET_MS] before sunset. */
    IMPENDING_HIDE,
}

/** Today screen card when the checklist should not be used on the phone. */
data class HolyDayPhoneNotice(
    val kind: HolyDayNoticeKind,
    val title: String,
    val message: String,
    /** Args for `{holidayName}` etc. placeholders in [message] — fill via rememberAppTranslatedTemplate. */
    val messageArgs: Map<String, String> = emptyMap(),
    val footer: String = "",
)

object HolyDayPhoneRules {

    /** Checklist hides this many ms before sunset on erev Shabbat / Yom Tov / YK (safety buffer). */
    const val HIDE_BEFORE_SUNSET_MS = 5 * 60_000L

    /** Warning banner appears this many ms before sunset. */
    const val WARN_BEFORE_SUNSET_MS = 20 * 60_000L

    /** Debug sims pin to this many minutes before sunset. */
    const val DEBUG_WARN_OFFSET_MINUTES = -21

    /** Civil Saturday (not erev) — for prep/seasonal logic, not phone-blocking windows. */
    fun isShabbatMelachaDay(cal: DayInfo): Boolean =
        cal.isShabbat && !cal.isErevShabbat

    fun hideStartMillis(sunsetMillis: Long): Long = sunsetMillis - HIDE_BEFORE_SUNSET_MS

    fun warnStartMillis(sunsetMillis: Long): Long = sunsetMillis - WARN_BEFORE_SUNSET_MS

    /**
     * Shabbat melacha hours: from Friday (sunset − 5 min) through Saturday tzeit.
     * After tzeit the checklist is usable again for Motzei Shabbat unless Yom Tov
     * continues (handled by [isYomTovMelachaWindow]).
     */
    fun isShabbatMelachaWindow(cal: DayInfo, nowMillis: Long): Boolean {
        if (isShabbatMelachaDay(cal)) {
            return !isPastTzeit(cal, nowMillis)
        }
        if (cal.isErevShabbat) {
            val sunset = cal.zmanim?.sunsetMillis ?: return false
            return nowMillis >= hideStartMillis(sunset)
        }
        return false
    }

    /**
     * Yom Tov melacha hours:
     * - On a Yom Tov day: through that night's tzeit
     * - Weekday erev chag / erev YK: from sunset − 5 min (Friday covered by Shabbat window)
     */
    fun isYomTovMelachaWindow(
        cal: DayInfo,
        nowMillis: Long,
        tomorrowCal: DayInfo? = null,
    ): Boolean {
        if (cal.isYomTovAssurBemelacha) {
            return !isPastTzeit(cal, nowMillis)
        }
        if (!cal.isErevShabbat && isErevYomTovOrYomKippur(cal, tomorrowCal)) {
            val sunset = cal.zmanim?.sunsetMillis ?: return false
            return nowMillis >= hideStartMillis(sunset)
        }
        return false
    }

    fun shouldHideChecklist(
        profile: UserProfile,
        cal: DayInfo,
        nowMillis: Long,
        tomorrowCal: DayInfo? = null,
    ): Boolean =
        isShabbatMelachaWindow(cal, nowMillis) ||
            isYomTovMelachaWindow(cal, nowMillis, tomorrowCal)

    fun phoneNotice(
        profile: UserProfile,
        cal: DayInfo,
        nowMillis: Long,
        tomorrowCal: DayInfo? = null,
    ): HolyDayPhoneNotice? = when {
        isShabbatMelachaWindow(cal, nowMillis) -> shabbatNotice()
        isYomTovMelachaWindow(cal, nowMillis, tomorrowCal) -> {
            val name = cal.yomTovHolidayName
                ?: tomorrowCal?.yomTovHolidayName
                ?: cal.upcomingChagName
                ?: if ("erev_yom_kippur" in cal.activeSeasons) "Yom Kippur" else null
            name?.let(::yomTovNotice)
        }
        else -> null
    }

    /**
     * Warning from 20 minutes before sunset until hide (5 minutes before sunset).
     * Countdown shows minutes until sunset (20…6); copy says checklist disables in
     * minutes-until-hide (15…1).
     */
    fun impendingHideWarning(
        cal: DayInfo,
        nowMillis: Long,
        tomorrowCal: DayInfo? = null,
    ): HolyDayPhoneNotice? {
        if (isShabbatMelachaWindow(cal, nowMillis) ||
            isYomTovMelachaWindow(cal, nowMillis, tomorrowCal)
        ) {
            return null
        }
        val sunset = cal.zmanim?.sunsetMillis ?: return null
        val warnAt = warnStartMillis(sunset)
        val hideAt = hideStartMillis(sunset)
        if (nowMillis < warnAt || nowMillis >= hideAt) return null

        val label = impendingLabel(cal, tomorrowCal) ?: return null
        val minutesUntilSunset = ceilMinutes(sunset - nowMillis).coerceIn(1, 20)
        val minutesUntilHide = ceilMinutes(hideAt - nowMillis).coerceIn(1, 15)
        val candlesLine = when {
            cal.isErevShabbat && isErevYomTovOrYomKippur(cal, tomorrowCal) ->
                "Light Shabbat / Yom Tov candles now if you have not already."
            cal.isErevShabbat ->
                "Light Shabbat candles now if you have not already."
            "erev_yom_kippur" in cal.activeSeasons ||
                cal.upcomingChagYomTovIndex == HebrewCalendarEngine.YOM_KIPPUR ->
                "Light Yom Kippur candles now if you have not already."
            else ->
                "Light Yom Tov candles now if you have not already."
        }
        return HolyDayPhoneNotice(
            kind = HolyDayNoticeKind.IMPENDING_HIDE,
            title = "Sunset in $minutesUntilSunset min — checklist disables in $minutesUntilHide min",
            message = "This checklist will turn off in {minutesUntilHide} minutes for {holidayName} " +
                "(5 minutes before sunset, as a safety buffer). $candlesLine " +
                "We warmly recommend turning your phone off now and welcoming the holy day.",
            messageArgs = mapOf(
                "holidayName" to label,
                "minutesUntilHide" to minutesUntilHide.toString(),
                "minutesUntilSunset" to minutesUntilSunset.toString(),
            ),
            footer = "Phone checklist hides at 5 minutes before sunset.",
        )
    }

    private fun isErevYomTovOrYomKippur(cal: DayInfo, tomorrowCal: DayInfo?): Boolean {
        if ("erev_yom_kippur" in cal.activeSeasons ||
            cal.upcomingChagYomTovIndex == HebrewCalendarEngine.YOM_KIPPUR
        ) {
            return true
        }
        return tomorrowCal?.isYomTovAssurBemelacha == true &&
            (cal.upcomingChagName != null || "erev_chag" in cal.activeSeasons)
    }

    private fun impendingLabel(cal: DayInfo, tomorrowCal: DayInfo?): String? = when {
        cal.isErevShabbat && isErevYomTovOrYomKippur(cal, tomorrowCal) ->
            cal.upcomingChagName?.let { "Shabbat & $it" } ?: "Shabbat"
        cal.isErevShabbat -> "Shabbat"
        "erev_yom_kippur" in cal.activeSeasons ||
            cal.upcomingChagYomTovIndex == HebrewCalendarEngine.YOM_KIPPUR -> "Yom Kippur"
        tomorrowCal?.isYomTovAssurBemelacha == true ||
            cal.upcomingChagName != null ||
            "erev_chag" in cal.activeSeasons ->
            cal.upcomingChagName ?: tomorrowCal?.yomTovHolidayName ?: "Yom Tov"
        else -> null
    }

    /** Whole minutes remaining, rounding up so 15:01 shows as 16 until the minute flips. */
    private fun ceilMinutes(ms: Long): Int =
        ((ms + 59_999L) / 60_000L).toInt()

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
