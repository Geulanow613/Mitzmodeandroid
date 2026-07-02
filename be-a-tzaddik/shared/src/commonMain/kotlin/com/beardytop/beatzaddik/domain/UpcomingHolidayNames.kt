package com.beardytop.beatzaddik.domain

/**
 * Display names and prep hints for upcoming Yom Tov rows when the generic
 * [holidayName] index label is too vague (e.g. final day of Pesach after Chol HaMoed).
 */
object UpcomingHolidayNames {

    const val SHEVII_SHEL_PESACH = "Shevi'i shel Pesach"
    const val ACHARON_SHEL_PESACH = "Acharon shel Pesach"

    /**
     * Name for the observance that begins tonight (tomorrow's assur-be-melacha day).
     */
    fun erevUpcomingDisplayName(
        todayYomTovIndex: Int,
        tomorrowYomTovIndex: Int,
        tomorrowHebrewMonth: Int?,
        tomorrowHebrewDay: Int?,
        inIsrael: Boolean,
        defaultHolidayName: String,
    ): String {
        if (todayYomTovIndex != HebrewCalendarEngine.CHOL_HAMOED_PESACH ||
            tomorrowYomTovIndex != HebrewCalendarEngine.PESACH
        ) {
            return defaultHolidayName
        }
        if (tomorrowHebrewMonth == HebrewCalendarEngine.NISSAN && tomorrowHebrewDay != null) {
            return when {
                tomorrowHebrewDay == 21 -> SHEVII_SHEL_PESACH
                tomorrowHebrewDay == 22 && !inIsrael -> ACHARON_SHEL_PESACH
                else -> defaultHolidayName
            }
        }
        // Chol HaMoed → PESACH is always the seventh day when Hebrew date is unavailable.
        return SHEVII_SHEL_PESACH
    }

    fun yomTovPrepHint(holidayName: String): String = when (holidayName) {
        "Rosh Hashana" -> "Yom Tov — Shofar, teshuvah, sweet foods"
        "Yom Kippur" -> "Yom Tov — Fast, prayer, atonement"
        "Sukkot" -> "Yom Tov — Lulav & etrog, sukkah"
        "Shemini Atzeret" -> "Yom Tov — Shemini Atzeret"
        "Simchat Torah" -> "Yom Tov — Simchat Torah, hakafot"
        "Pesach" -> "Yom Tov — Seder night(s), matzah, no chametz"
        SHEVII_SHEL_PESACH, ACHARON_SHEL_PESACH ->
            "Yom Tov — Yizkor, matzah, festive meals"
        "Shavuot" -> "Yom Tov — Matan Torah, learning, dairy foods"
        else -> "Yom Tov — candles, festive meals"
    }
}
