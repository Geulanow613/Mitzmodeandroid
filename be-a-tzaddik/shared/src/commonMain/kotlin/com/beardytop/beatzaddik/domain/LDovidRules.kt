package com.beardytop.beatzaddik.domain

/**
 * When Psalm 27 (L'Dovid Hashem Ori) is recited — from Elul through Tishrei, end date by minhag.
 */
object LDovidRules {

    fun isRecited(cal: DayInfo, nusach: EffectiveNusach): Boolean {
        val month = cal.hebrewMonth ?: return false
        val day = cal.hebrewDay ?: return false
        return when (month) {
            HebrewCalendarEngine.ELUL -> day >= startDay(nusach)
            HebrewCalendarEngine.TISHREI -> day <= endDay(nusach)
            else -> false
        }
    }

    /** First day of Elul on which L'Dovid is said. */
    fun startDay(nusach: EffectiveNusach): Int = when (nusach) {
        EffectiveNusach.ASHKENAZ, EffectiveNusach.CHABAD -> 2
        EffectiveNusach.SEFARD, EffectiveNusach.EDOT_HAMIZRACH -> 1
    }

    /** Last day of Tishrei on which L'Dovid is said (inclusive). */
    fun endDay(nusach: EffectiveNusach): Int = when (nusach) {
        EffectiveNusach.SEFARD -> 10 // Yom Kippur — prevalent Sephardi custom (Ben Ish Chai)
        EffectiveNusach.CHABAD -> 21 // Hoshana Rabbah
        EffectiveNusach.EDOT_HAMIZRACH -> 22 // Shemini Atzeret — many Edot HaMizrach kehillot
        EffectiveNusach.ASHKENAZ -> 22 // Shemini Atzeret — Rama / many Ashkenaz communities
    }
}
