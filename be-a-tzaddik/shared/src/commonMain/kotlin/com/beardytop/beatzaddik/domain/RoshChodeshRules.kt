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
        return HallelKind.HALF
    }
}
