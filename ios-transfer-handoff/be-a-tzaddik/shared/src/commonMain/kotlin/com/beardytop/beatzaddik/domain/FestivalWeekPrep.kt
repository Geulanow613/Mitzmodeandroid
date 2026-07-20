package com.beardytop.beatzaddik.domain

enum class FestivalWeekPrep { PESACH, SHAVUOT, SUKKOT }

/**
 * ~One week before each pilgrimage festival (Hebrew-date windows ending the day before erev).
 * Nissan 8–13, Iyar 29–30 + Sivan 1–4, Tishrei 8 then 11–13 (skip Erev YK / YK).
 */
fun DayInfo.festivalWeekPrep(): FestivalWeekPrep? {
    val month = hebrewMonth ?: return null
    val day = hebrewDay ?: return null
    return when {
        month == HebrewCalendarEngine.NISSAN && day in 8..13 -> FestivalWeekPrep.PESACH
        (month == HebrewCalendarEngine.IYAR && day >= 29) ||
            (month == HebrewCalendarEngine.SIVAN && day in 1..4) -> FestivalWeekPrep.SHAVUOT
        // Erev Sukkot is 14 Tishrei — skip 9–10 (Erev YK / Yom Kippur).
        month == HebrewCalendarEngine.TISHREI && (day == 8 || day in 11..13) ->
            FestivalWeekPrep.SUKKOT
        else -> null
    }
}
