package com.beardytop.beatzaddik.domain

/**
 * When Sefirah mourning customs (music / haircuts / weddings) apply during the Omer.
 *
 * Windows follow common published minhagim — communities differ; a personal rav is final.
 */
object SefirahMourningRules {

    /**
     * @return true when the mourning checklist item should appear for this day/nusach.
     */
    fun isMourningDay(cal: DayInfo, nusach: EffectiveNusach, nowMillis: Long): Boolean {
        if (!cal.isSefiratHaomer) return false
        val day = cal.omerDay ?: return false
        if (day !in 1..49) return false
        return when (nusach) {
            // Standard Ashkenaz "first 33": days 1–32. Ends at nightfall when Lag begins
            // (tzeit after day 32 = night of day 33). Days 33–49: no mourning for this custom.
            // Rema "later 33" (from Rosh Chodesh Iyar / day 16 through Erev Shavuot,
            // with Lag as a one-day break) is described in the explainer — checklist
            // follows the more common first-33 window for Ashkenaz.
            EffectiveNusach.ASHKENAZ,
            EffectiveNusach.OTHER -> day in 1..32

            // Shulchan Aruch / Edot HaMizrach: mourning through day 33 inclusive
            // (Lag still restricted for weddings/haircuts). After Lag ends, night of
            // day 34 snaps restrictions back until morning of day 34 (miktzat hayom).
            EffectiveNusach.SEFARD,
            EffectiveNusach.EDOT_HAMIZRACH -> when {
                day in 1..33 -> true
                day == 34 -> beforeMorningOfDay34(cal, nowMillis)
                else -> false
            }

            // Arizal / Chabad: restraint through the Omer until Erev Shavuot;
            // Lag BaOmer (33) is a one-day suspension; day 49 (Erev Shavuot) is already lifted.
            EffectiveNusach.CHABAD -> day != 33 && day != 49
        }
    }

    /** Night of day 34 through dawn — then mourning ends permanently. */
    private fun beforeMorningOfDay34(cal: DayInfo, nowMillis: Long): Boolean {
        val dawn = cal.zmanim?.alotHaShacharMillis ?: cal.zmanim?.sunriseMillis
        return dawn == null || nowMillis < dawn
    }
}
