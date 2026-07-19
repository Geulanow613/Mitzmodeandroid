package com.beardytop.beatzaddik.domain

/**
 * When Psalm 27 (L'Dovid Hashem Ori) is recited — from Elul through Tishrei, end date by minhag.
 */
object LDovidRules {

    fun isRecited(cal: DayInfo, nusach: EffectiveNusach): Boolean {
        val month = cal.hebrewMonth ?: return false
        val day = cal.hebrewDay ?: return false
        return when (month) {
            // Chabad commonly begins on the first day of Rosh Chodesh Elul (30 Av).
            HebrewCalendarEngine.AV ->
                nusach == EffectiveNusach.CHABAD && day == 30
            // Ashkenaz commonly begins on the second day of Rosh Chodesh Elul (1 Elul).
            HebrewCalendarEngine.ELUL -> day >= startDay(nusach)
            HebrewCalendarEngine.TISHREI -> day <= endDay(nusach)
            else -> false
        }
    }

    /** First day of Elul on which L'Dovid is said. */
    fun startDay(nusach: EffectiveNusach): Int = when (nusach) {
        EffectiveNusach.ASHKENAZ,
        EffectiveNusach.CHABAD,
        EffectiveNusach.SEFARD,
        EffectiveNusach.EDOT_HAMIZRACH,
        EffectiveNusach.OTHER -> 1
    }

    /** Last day of Tishrei on which L'Dovid is said (inclusive). */
    fun endDay(nusach: EffectiveNusach): Int = when (nusach) {
        EffectiveNusach.SEFARD -> 10 // Yom Kippur — prevalent Sephardi custom (Ben Ish Chai)
        EffectiveNusach.CHABAD -> 21 // Hoshana Rabbah
        EffectiveNusach.EDOT_HAMIZRACH -> 22 // Shemini Atzeret — many Edot HaMizrach kehillot
        EffectiveNusach.ASHKENAZ -> 22 // Shemini Atzeret — Rama / many Ashkenaz communities
        // Generic window through Shemini Atzeret; end date still varies — follow your kehilla.
        EffectiveNusach.OTHER -> 22
    }
}
