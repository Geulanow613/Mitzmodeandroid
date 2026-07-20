package com.beardytop.beatzaddik.domain

/** Which Hallel (if any) belongs in Shacharit on Rosh Chodesh. */
object RoshChodeshRules {

    enum class HallelKind {
        /** Rosh Hashana (1–2 Tishrei) — Hallel is not said. */
        NONE,
        /** Typical Rosh Chodesh — Partial (Half) Hallel. */
        HALF,
        /** Rosh Chodesh during Chanukah (e.g. Tevet) — Full Hallel, not Half. */
        FULL_DURING_CHANUKAH,
    }

    fun hallelKind(cal: DayInfo): HallelKind {
        if (!cal.isRoshChodesh) return HallelKind.NONE
        // Rosh Hashana only — not 30 Tishrei (first day of Rosh Chodesh Cheshvan), which has Half Hallel.
        val day = cal.hebrewDay
        if (cal.hebrewMonth == HebrewCalendarEngine.TISHREI && day != null && day in 1..2) {
            return HallelKind.NONE
        }
        if (cal.isChanukah) return HallelKind.FULL_DURING_CHANUKAH
        // Chol HaMoed: Hallel comes from the CHM seasonal rows (Full on Sukkot, Half on Pesach).
        // Returning NONE here avoids a conflicting RC Half alongside CHM Full/Half.
        if ("chol_hamoed_sukkot" in cal.activeSeasons || "chol_hamoed_pesach" in cal.activeSeasons) {
            return HallelKind.NONE
        }
        return HallelKind.HALF
    }

    /**
     * Two-day Rosh Chodesh when the previous month has 30 days: 30th (first day) + 1st (second day).
     * One-day Rosh Chodesh is only the 1st when the previous month has 29 days.
     */
    fun isTwoDayObservance(cal: DayInfo): Boolean {
        if (!cal.isRoshChodesh) return false
        val day = cal.hebrewDay ?: return false
        if (day == 30) return true
        if (day != 1) return false
        val year = cal.hebrewYear ?: return false
        val month = cal.hebrewMonth ?: return false
        val (prevYear, prevMonth) = previousJewishMonth(year, month)
        return HebrewCalendarEngine.getDaysInJewishMonth(prevYear, prevMonth) == 30
    }

    /** True on the first civil/Hebrew segment of a two-day Rosh Chodesh (day 30). */
    fun isFirstDayOfTwoDay(cal: DayInfo): Boolean =
        cal.isRoshChodesh && cal.hebrewDay == 30

    private fun previousJewishMonth(year: Int, month: Int): Pair<Int, Int> = when (month) {
        HebrewCalendarEngine.NISSAN ->
            if (HebrewCalendarEngine.isJewishLeapYear(year)) {
                year to HebrewCalendarEngine.ADAR_II
            } else {
                year to HebrewCalendarEngine.ADAR
            }
        HebrewCalendarEngine.TISHREI -> (year - 1) to HebrewCalendarEngine.ELUL
        HebrewCalendarEngine.ADAR_II -> year to HebrewCalendarEngine.ADAR
        else -> year to (month - 1)
    }
}
