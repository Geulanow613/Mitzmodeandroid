package com.beardytop.beatzaddik.domain

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

/**
 * Birkat Hachamah — blessing the sun once every 28 years (Berakhot 59b; machzor gadol).
 * Civil dates per the Talmudic 365¼-day solar cycle (Samuel; Eruvin 56a).
 */
object BirkatHachamahRules {

    /** Show the checklist item this many days before the occurrence (inclusive of the day itself). */
    const val ADVANCE_DAYS = 7

    /**
     * Gregorian dates of Birkat Hachamah (Wednesday morning after the Tuesday-evening tekufah).
     * Source: Hebcal, TorahCalc, established 28-year cycle from 1925 onward.
     */
    private val OCCURRENCE_DATES: List<LocalDate> = listOf(
        LocalDate(1925, 4, 8),
        LocalDate(1953, 4, 8),
        LocalDate(1981, 4, 8),
        LocalDate(2009, 4, 8),
        LocalDate(2037, 4, 8),
        LocalDate(2065, 4, 8),
        LocalDate(2093, 4, 8),
        LocalDate(2121, 4, 9),
        LocalDate(2149, 4, 9),
        LocalDate(2177, 4, 9),
        LocalDate(2205, 4, 10),
        LocalDate(2233, 4, 10),
    )

    fun occurrenceOn(date: LocalDate): LocalDate? =
        OCCURRENCE_DATES.firstOrNull { it == date }

    /** Nearest upcoming occurrence visible in the checklist window, if any. */
    fun visibleOccurrence(date: LocalDate): LocalDate? =
        OCCURRENCE_DATES.firstOrNull { occ ->
            val windowStart = occ.plus(-ADVANCE_DAYS, DateTimeUnit.DAY)
            date >= windowStart && date <= occ
        }

    fun isRecitationDay(date: LocalDate): Boolean = occurrenceOn(date) != null

    fun daysUntilOccurrence(date: LocalDate, occurrence: LocalDate): Int =
        occurrence.toEpochDays() - date.toEpochDays()

    fun formatOccurrenceDate(date: LocalDate): String {
        val month = when (date.monthNumber) {
            1 -> "January"; 2 -> "February"; 3 -> "March"; 4 -> "April"
            5 -> "May"; 6 -> "June"; 7 -> "July"; 8 -> "August"
            9 -> "September"; 10 -> "October"; 11 -> "November"; 12 -> "December"
            else -> date.month.name.lowercase().replaceFirstChar { it.uppercase() }
        }
        val weekday = date.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
        return "$weekday, $month ${date.dayOfMonth}, ${date.year}"
    }

    fun formatOccurrenceDateHebrew(date: LocalDate): String {
        val weekday = when (date.dayOfWeek) {
            kotlinx.datetime.DayOfWeek.SUNDAY -> "יום ראשון"
            kotlinx.datetime.DayOfWeek.MONDAY -> "יום שני"
            kotlinx.datetime.DayOfWeek.TUESDAY -> "יום שלישי"
            kotlinx.datetime.DayOfWeek.WEDNESDAY -> "יום רביעי"
            kotlinx.datetime.DayOfWeek.THURSDAY -> "יום חמישי"
            kotlinx.datetime.DayOfWeek.FRIDAY -> "יום שישי"
            kotlinx.datetime.DayOfWeek.SATURDAY -> "שבת"
        }
        val month = when (date.monthNumber) {
            1 -> "ינואר"; 2 -> "פברואר"; 3 -> "מרץ"; 4 -> "אפריל"
            5 -> "מאי"; 6 -> "יוני"; 7 -> "יולי"; 8 -> "אוגוסט"
            9 -> "ספטמבר"; 10 -> "אוקטובר"; 11 -> "נובמבר"; 12 -> "דצמבר"
            else -> date.month.name.lowercase()
        }
        return "$weekday, ${date.dayOfMonth} ב$month ${date.year}"
    }

    /** Closest occurrence to [anchor] — for debug simulator (fixed Gregorian cycle). */
    fun nearestOccurrenceTo(anchor: LocalDate): LocalDate? =
        OCCURRENCE_DATES.minByOrNull { kotlin.math.abs(it.toEpochDays() - anchor.toEpochDays()) }

    /** A civil date in the advance checklist window (not the recitation day itself). */
    fun sampleAdvanceDay(occurrence: LocalDate): LocalDate =
        occurrence.plus(-4, DateTimeUnit.DAY)
}
