package com.beardytop.beatzaddik.domain

/** Nusach-specific Mincha / Maariv deadline guidance (matches zman evaluator windows). */
object PrayerZmanRules {

    private const val MINUTES_AFTER_SUNSET_MINCHA = 15L

    fun minchaAbsoluteEndMillis(z: ZmanimSnapshot, nusach: EffectiveNusach): Long? {
        val sunset = z.sunsetMillis ?: return z.tzeitMillis
        return when (nusach) {
            EffectiveNusach.CHABAD -> z.tzeitMillis ?: sunset
            EffectiveNusach.ASHKENAZ,
            EffectiveNusach.SEFARD,
            EffectiveNusach.EDOT_HAMIZRACH,
            EffectiveNusach.OTHER ->
                sunset + MINUTES_AFTER_SUNSET_MINCHA * 60_000L
        }
    }

    fun minchaExpiredMessage(nusach: EffectiveNusach): String = when (nusach) {
        EffectiveNusach.CHABAD ->
            "Today's Mincha window ended at nightfall (tzeit). Chabad custom permits Mincha during twilight until then."
        EffectiveNusach.ASHKENAZ ->
            "Today's Mincha window ended (ideal: Amidah before sunset; many allow until about 15 minutes after sunset per the Gra)."
        EffectiveNusach.SEFARD, EffectiveNusach.EDOT_HAMIZRACH ->
            "Today's Mincha window ended (about 15 minutes after sunset). Sephardi custom often permits starting shortly before sunset even if the Amidah continues after sunset."
        EffectiveNusach.OTHER ->
            "Today's Mincha window ended (about 15 minutes after sunset in many communities; some permit until nightfall). Follow your minhag."
    }

    fun minchaActiveLateHint(nusach: EffectiveNusach): String? = when (nusach) {
        EffectiveNusach.ASHKENAZ ->
            "Ideal: begin the Amidah before sunset. Bedi'eved, many authorities allow until about 15 minutes after sunset."
        EffectiveNusach.SEFARD, EffectiveNusach.EDOT_HAMIZRACH ->
            "Sephardi custom often allows starting Mincha shortly before sunset, even if you finish the Amidah after sunset."
        EffectiveNusach.CHABAD ->
            "Highly optimal before sunset; Chabad custom permits Mincha during twilight (bein hashmashot) until nightfall."
        EffectiveNusach.OTHER ->
            "Aim to begin before sunset; latest time varies by community — follow your minhag."
    }
}

/** Tefillin on Chol HaMoed and Tisha B'Av fast-day copy. */
object TefillinSeasonalRules {

    fun shouldOmitTefillinOnCholHamoed(prayerDay: PrayerDayContext): Boolean {
        if (!prayerDay.isCholHamoed) return false
        if (prayerDay.isInIsrael) return true
        return when (prayerDay.nusach) {
            EffectiveNusach.SEFARD,
            EffectiveNusach.CHABAD,
            EffectiveNusach.EDOT_HAMIZRACH -> true
            EffectiveNusach.ASHKENAZ,
            EffectiveNusach.OTHER -> false
        }
    }

    fun cholHamoedOmittedHint(): String =
        "Chol HaMoed: Sephardi, Chabad, and Edot HaMizrach custom does not wear tefillin. In Israel, the accepted custom for all communities is not to wear tefillin on Chol HaMoed."

    fun cholHamoedAshkenazActiveHint(): String =
        "Chol HaMoed: many Ashkenazim in the Diaspora wear tefillin (often without the bracha, or reciting it quietly). In Israel, tefillin are generally not worn on Chol HaMoed."

    fun cholHamoedOtherActiveHint(): String =
        "Chol HaMoed: tefillin customs vary widely (many omit; some Ashkenazim in the Diaspora wear them). In Israel, the accepted custom is not to wear tefillin. Follow your kehilla."
}
