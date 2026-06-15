package com.beardytop.beatzaddik.domain

/** Which Hallel (if any) belongs in Shacharit on Rosh Chodesh. */
object RoshChodeshRules {

    enum class HallelKind {
        /** Rosh Chodesh Tishrei (Rosh Hashanah) — Hallel is not said. */
        NONE,
        /** Typical Rosh Chodesh — Partial (Half) Hallel. */
        HALF,
        /** Rosh Chodesh during Chanukah (e.g. Tevet) — Full Hallel, not Half. */
        FULL_DURING_CHANUKAH,
    }

    fun hallelKind(cal: DayInfo): HallelKind {
        if (!cal.isRoshChodesh) return HallelKind.NONE
        if (cal.hebrewMonth == HebrewCalendarEngine.TISHREI) return HallelKind.NONE
        if (cal.isChanukah) return HallelKind.FULL_DURING_CHANUKAH
        return HallelKind.HALF
    }
}
