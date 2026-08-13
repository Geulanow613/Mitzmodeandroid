package com.beardytop.beatzaddik.domain

/**
 * Tisha B'Av tefillin customs — morning omission for Ashkenaz/Chabad; many Sephardim wear in the morning; Mincha donning widespread.
 * Applies whenever the fast is observed (9 Av or deferred 10 Av when 9 Av is Shabbat).
 */
object TishaBeavTefillinRules {

    fun isTishaBeav(fastDayIndex: Int?): Boolean =
        fastDayIndex == HebrewCalendarEngine.TISHA_BEAV

    /** Ashkenazi and Chabad omit tallit and tefillin at Shacharit on Tisha B'Av. */
    fun omitsMorningTefillin(nusach: EffectiveNusach): Boolean = when (nusach) {
        EffectiveNusach.ASHKENAZ, EffectiveNusach.CHABAD -> true
        EffectiveNusach.SEFARD, EffectiveNusach.EDOT_HAMIZRACH, EffectiveNusach.OTHER -> false
    }

    fun morningOmittedHint(): String =
        "Tisha B'Av: tallit and tefillin are not worn at Shacharit — they are a sign of glory, omitted in deep mourning. Put them on at Mincha after halachic chatzos (see the afternoon item below)."

    fun sephardiMorningHint(): String =
        "Tisha B'Av: many Sephardim wear tallit and tefillin at Shacharit; some communities omit them in the morning. All communities wear them at Mincha after chatzos. If your minyan's custom differs from yours, ask your rav (lo titgodedu)."

    fun minchaItemTitle(): String =
        "Tefillin & tallit at Mincha (Tisha B'Av)"

    fun minchaItemExplanation(): String = """
Today is Tisha B'Av — tallit and tefillin at Mincha after chatzos.

By minhag:
• Ashkenaz / Chabad: do not wear tallit or tefillin at Shacharit (like the day of burial — they are a sign of glory, omitted in deepest mourning). Put them on at Mincha after halachic chatzos.
• Sephardi / Edot HaMizrach: many wear tallit and tefillin at Shacharit as usual; some communities omit them in the morning. Afternoon donning at Mincha after chatzos is still the widespread practice.

Why Mincha:
After midday, putting on tallit and tefillin expresses that consolation will eventually come.

What to do at Mincha:
• After chatzos, recite the brachos on tallit and tefillin and pray Mincha while wearing them.
• If you pray where the congregation's custom differs from yours, avoid public division — ask your rav.
    """.trimIndent()

    fun minchaUpcomingHint(timeLabel: String): String =
        "Tisha B'Av: tallit and tefillin at Mincha after chatzos — available $timeLabel."

    fun minchaExpiredHint(): String =
        "Today's Tisha B'Av Mincha tefillin window ended at nightfall."

    /** Bullet for fast-day explainers (public_fast_day, Nine Days). */
    fun fastDayTefillinNote(nusach: EffectiveNusach): String = when (nusach) {
        EffectiveNusach.OTHER ->
            "Morning tallit/tefillin on Tisha B'Av varies by community; wearing them at Mincha after chatzos is widespread. Follow your kehilla."
        EffectiveNusach.ASHKENAZ, EffectiveNusach.CHABAD ->
            "Ashkenazi/Chabad: tallit and tefillin are not worn at Shacharit (like the day of burial); wear them at Mincha after halachic chatzos."
        EffectiveNusach.SEFARD, EffectiveNusach.EDOT_HAMIZRACH ->
            "Morning tefillin: many Sephardim wear tallit and tefillin at Shacharit; some communities omit them. All communities wear them at Mincha after chatzos."
    }

    const val FAST_DAY_TRAVEL_NOTE =
        "Non-essential travel is discouraged on Tisha B'Av; when travel is truly needed, ask your rav."
}
