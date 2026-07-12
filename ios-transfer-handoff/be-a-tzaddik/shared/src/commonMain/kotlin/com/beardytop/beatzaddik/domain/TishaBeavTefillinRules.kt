package com.beardytop.beatzaddik.domain

/**
 * Tisha B'Av tefillin customs — morning omission (Ashkenaz/Chabad) and universal Mincha donning.
 * Applies whenever the fast is observed (9 Av or deferred 10 Av when 9 Av is Shabbat).
 */
object TishaBeavTefillinRules {

    fun isTishaBeav(fastDayIndex: Int?): Boolean =
        fastDayIndex == HebrewCalendarEngine.TISHA_BEAV

    /** Ashkenazi and Chabad omit tallit and tefillin at Shacharit on Tisha B'Av. */
    fun omitsMorningTefillin(nusach: EffectiveNusach): Boolean = when (nusach) {
        EffectiveNusach.ASHKENAZ, EffectiveNusach.CHABAD -> true
        EffectiveNusach.SEFARD, EffectiveNusach.EDOT_HAMIZRACH -> false
    }

    fun morningOmittedHint(): String =
        "Tisha B'Av: tallit and tefillin are not worn at Shacharit — they are a sign of glory, omitted in deep mourning. Put them on at Mincha after halachic chatzos (see the afternoon item below)."

    fun sephardiMorningHint(): String =
        "Tisha B'Av: Sephardi morning custom varies — many omit tefillin at Shacharit; Jerusalem custom often wears them. All communities wear tallit and tefillin at Mincha after chatzos. If your minyan's custom differs from yours, ask your rav (lo titgodedu)."

    fun minchaItemTitle(): String =
        "Tefillin & tallit at Mincha (Tisha B'Av)"

    fun minchaItemExplanation(): String = """
Today is Tisha B'Av — on this day Ashkenazi and Chabad custom does not wear tallit or tefillin at Shacharit.

Why:
Tefillin are called an עֵדוּת (testimony / glory). On the day we mourn the Temple's destruction, morning tefillin are omitted — like the day of burial — to express deepest aveilut. After halachic chatzos (midday), we don tallit and tefillin at Mincha as a sign that consolation will eventually come.

What to do:
• Do not put on tallit or tefillin at Shacharit today — even if you daven at home before a later minyan.
• After chatzos, at Mincha, recite the brachos on tallit and tefillin and pray Mincha while wearing them.

Community note:
Sephardi and Edot HaMizrach morning customs vary (many omit tefillin at Shacharit; some Jerusalem communities wear them in the morning). Afternoon donning at Mincha is universal. If you pray where the congregation's custom differs from yours, avoid public division — ask your rav.
    """.trimIndent()

    fun minchaUpcomingHint(timeLabel: String): String =
        "Tisha B'Av: tallit and tefillin at Mincha after chatzos — available $timeLabel."

    fun minchaExpiredHint(): String =
        "Today's Tisha B'Av Mincha tefillin window ended at nightfall."

    /** Bullet for fast-day explainers (public_fast_day, Nine Days). */
    fun fastDayTefillinNote(nusach: EffectiveNusach): String = when {
        omitsMorningTefillin(nusach) ->
            "Ashkenazi/Chabad: tallit and tefillin are not worn at Shacharit (like the day of burial); wear them at Mincha after halachic chatzos."
        else ->
            "Morning tefillin: Sephardi custom varies (many omit at Shacharit; some Jerusalem communities wear them). All communities wear tallit and tefillin at Mincha after chatzos."
    }

    const val FAST_DAY_TRAVEL_NOTE =
        "Non-essential travel is discouraged on Tisha B'Av; when travel is truly needed, ask your rav."
}
