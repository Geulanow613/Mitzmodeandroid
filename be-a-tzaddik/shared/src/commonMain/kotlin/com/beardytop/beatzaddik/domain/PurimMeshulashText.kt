package com.beardytop.beatzaddik.domain

/**
 * Purim Meshulash (walled-city Purim when 15 Adar falls on Shabbat): mitzvot split across
 * Thursday night / Friday / Sunday, with Shabbat in between.
 */
object PurimMeshulashText {

    fun isMeshulashFridayDay(cal: DayInfo): Boolean =
        "purim_meshulash_friday" in cal.activeSeasons

    fun isMeshulashSundayDay(cal: DayInfo): Boolean =
        "purim_meshulash_sunday" in cal.activeSeasons

    /** Tomorrow is erev; day after tomorrow is the Friday meshulash block. */
    fun shouldShowAdvancePrep(today: DayInfo, tomorrow: DayInfo, dayAfterTomorrow: DayInfo): Boolean =
        "erev_purim" in tomorrow.activeSeasons &&
            isMeshulashFridayDay(dayAfterTomorrow) &&
            !("erev_purim" in today.activeSeasons)

    fun isErevBeforeMeshulashFriday(cal: DayInfo, tomorrow: DayInfo): Boolean =
        "erev_purim" in cal.activeSeasons && isMeshulashFridayDay(tomorrow)

    fun fullScheduleBlock(): String = FULL_SCHEDULE_BLOCK

    private val FULL_SCHEDULE_BLOCK = """
Purim Meshulash (פורים מְשֻׁלָּשׁ) — Jerusalem (and certain other communities that keep walled‑city Purim) when 15 Adar falls on Shabbat

Why the calendar splits:
• Your Purim observance (walled city / Jerusalem) would normally be 15 Adar, but Purim mitzvot are not done on Shabbat.
• The four mitzvot spread across three days: Friday, Shabbat, and Sunday.

Read this entire guide before Shabbat so you know the full schedule in advance.

Day 1 — Friday (14 Adar) — first day of Meshulash:
• Megillah: Hear the Book of Esther Thursday night after tzeit AND again Friday morning (daytime, before sunset) — both readings are required, like a normal Purim.
• Matanot la'evyonim: Give gifts to the poor Friday daytime only (at least one gift to each of two different poor people).
• Do not say Al HaNissim in davening or bentching today.
• Do not hold the festive Purim seudah today — that waits until Sunday.

Day 2 — Shabbat (15 Adar) — second day:
• Al HaNissim: Insert into every Amidah and into Birkat Hamazon (Grace After Meals) today — not on Friday or Sunday.
• Synagogue: Two Torah scrolls — the weekly portion plus Vayavo Amalek (Exodus 17:8–16); special Haftarah for Purim.
• No Purim seudah today (Shabbat rules). It is customary to elevate your Shabbat meals with extra delicacies.
• No Megillah, matanot, mishloach, or seudah at home today — communal Purim observance at shul only.

Day 3 — Sunday (16 Adar) — third day:
• Mishloach manot: Send at least two ready-to-eat foods to one friend before sunset.
• Purim seudah: Hold the main festive Purim meal today — joy, wine, and words of Torah. Some have the custom for men to drink wine until they cannot distinguish “cursed is Haman” from “blessed is Mordechai”; follow your community’s guidance and celebrate safely and with dignity.

Before Shabbat candles (Friday):
• Complete Megillah (night + morning) and matanot la'evyonim on Friday.
• Plan Sunday mishloach manot delivery and the seudah menu.
• Machatzit haShekel: if your custom, many give before Megillah — handle Thursday night or Friday.

Ask your rav about edge cases (travel, illness, minhag).
    """.trimIndent()

    private val ADVANCE_PREP_TEMPLATE = """
Tomorrow is erev Purim — and this year is Purim Meshulash for Jerusalem (and those observing walled‑city Purim). Shabbat is in the middle, so you need the full plan now (not only tomorrow).

${'$'}scheduleBlock
    """.trimIndent()

    private val EREV_PREP_TEMPLATE = """
Purim Meshulash starts tonight for Jerusalem (and those observing walled‑city Purim). Because Shabbat falls in the middle of the festival, read and save this plan now so you have Sunday's mitzvot ready before Shabbat.

${'$'}scheduleBlock

    """.trimIndent()

    fun advancePrepTemplate(): String = ADVANCE_PREP_TEMPLATE

    fun advancePrepArgs(): Map<String, String> = mapOf("scheduleBlock" to fullScheduleBlock())

    fun erevPrepTemplate(): String = EREV_PREP_TEMPLATE

    fun erevPrepArgs(): Map<String, String> = mapOf("scheduleBlock" to fullScheduleBlock())

    fun advancePrepExplanation(): String = ExplainerTemplateFill.fill(
        ADVANCE_PREP_TEMPLATE,
        advancePrepArgs(),
    )

    fun erevPrepExplanation(): String = ExplainerTemplateFill.fill(
        EREV_PREP_TEMPLATE,
        erevPrepArgs(),
    )

    fun erevMegillahExplanation(): String = BeginnerHalachaGlossary.withKeyTerms(
        BeginnerHalachaGlossary.purimBasics(),
        EREV_MEGILLAH_BODY,
    )

    fun fridayMegillahTemplate(): String = FRIDAY_MEGILLAH_BODY

    fun fridayMatanotTemplate(): String = FRIDAY_MATANOT_BODY

    fun sundayMishloachTemplate(): String = SUNDAY_MISHLOACH_BODY

    fun sundaySeudahTemplate(): String = SUNDAY_SEUDAH_BODY

    fun fridayMegillahExplanation(): String = BeginnerHalachaGlossary.withKeyTerms(
        BeginnerHalachaGlossary.purimBasics(),
        FRIDAY_MEGILLAH_BODY,
    )

    fun shabbatAlHaNissimExplanation(): String = SHABBAT_AL_HANISSIM_BODY

    fun shabbatTorahExplanation(): String = SHABBAT_TORAH_BODY

    private val EREV_MEGILLAH_BODY = """
Purim Meshulash — Thursday night (13 Adar): first Megillah reading

Tonight after nightfall (tzeit) is the first of two required Megillah readings this year. Friday morning is the second reading.

How:
• Hear every word from a kosher megillah scroll; men and women are equally obligated.
• Stand for the blessings; customs at Haman's name vary by shul.

${PurimBrachotText.MEGILLAH_BLESSINGS_COMMON}

Tomorrow (Friday): second Megillah reading and matanot la'evyonim — but no Al HaNissim and no Purim seudah on Friday.
    """.trimIndent()

    private val FRIDAY_MEGILLAH_BODY = """
Purim Meshulash — Day 1 (Friday / 14 Adar): Megillah

When this year:
• Thursday night after tzeit — first reading (you should have heard it then).
• Today (Friday) — second reading during the daytime (usually after Shacharit, before sunset) — a universal obligation, not optional.

How (same laws as regular Purim):
• Hear every word from a kosher megillah scroll; men and women are equally obligated.
• Stand for the blessings; customs at Haman's name vary by shul.

${PurimBrachotText.MEGILLAH_BLESSINGS_COMMON}

Friday only — also today:
• Matanot la'evyonim (gifts to the poor) — see today's other checklist item.
• Do not say Al HaNissim in davening or bentching today (that is Shabbat only this year).
• Do not hold the Purim seudah today — Sunday is for the festive meal. Mishloach manot is also Sunday.
    """.trimIndent()

    fun fridayMatanotExplanation(): String = BeginnerHalachaGlossary.withKeyTerms(
        BeginnerHalachaGlossary.purimBasics(),
        FRIDAY_MATANOT_BODY,
    )

    private val FRIDAY_MATANOT_BODY = """
Purim Meshulash — Day 1 (Friday / 14 Adar): matanot la'evyonim

Today (Friday daytime) — not on Shabbat:
• Give at least one gift to each of two different poor people (minimum of two recipients total).
• Each gift should enable a modest Purim meal (money is common; amounts vary by community).
• Many give after the daytime Megillah reading.

You may use a trustworthy messenger or organization that distributes today. If you cannot find recipients, ask your rabbi or shul Friday morning.

Do not say Al HaNissim today. Mishloach manot and the Purim seudah are Sunday.
    """.trimIndent()

    fun sundayMishloachExplanation(): String = BeginnerHalachaGlossary.withKeyTerms(
        BeginnerHalachaGlossary.purimBasics(),
        SUNDAY_MISHLOACH_BODY,
    )

    private val SUNDAY_MISHLOACH_BODY = """
Purim Meshulash — Day 3 (Sunday / 16 Adar): mishloach manot

Deferred from Shabbat because Purim mitzvot are not performed on Shabbat this year.

The mitzvah:
• Send at least two different ready-to-eat foods or drinks to one friend today — one package.
• Deliver before sunset; a messenger is fine.
• Food should be ready to eat without cooking; label sender and recipient.

Do not say Al HaNissim today — that was Shabbat only.
    """.trimIndent()

    fun sundaySeudahExplanation(): String = BeginnerHalachaGlossary.withKeyTerms(
        BeginnerHalachaGlossary.purimBasics(),
        SUNDAY_SEUDAH_BODY,
    )

    private val SUNDAY_SEUDAH_BODY = """
Purim Meshulash — Day 3 (Sunday / 16 Adar): Purim seudah

The main festive Purim meal is today — not Friday or Shabbat this year.

When:
• Sunday daytime before sunset — many hold the meal in the afternoon after mishloach manot.

How:
• Festive meal with bread, meat, wine, and joy; include words of Torah or thanks to Hashem.
• Drinking wine is a widespread custom. Some have the custom for men to drink until they cannot distinguish “cursed is Haman” from “blessed is Mordechai”; follow your community’s guidance and celebrate safely and with dignity.

This completes the four Purim mitzvot for Purim Meshulash.
    """.trimIndent()

    private val SHABBAT_AL_HANISSIM_BODY = """
Purim Meshulash — Day 2 (Shabbat / 15 Adar): Al HaNissim

Today only (not Friday or Sunday):
• Insert Al HaNissim into every Amidah of Shabbat davening.
• Insert Al HaNissim into Birkat Hamazon (Grace After Meals) at every Shabbat meal today.

There is no Purim seudah today — elevate your Shabbat meals with extra delicacies if you wish, but the formal Purim feast is Sunday.
    """.trimIndent()

    private val SHABBAT_TORAH_BODY = """
Purim Meshulash — Day 2 (Shabbat / 15 Adar): synagogue readings

Attend shul for the communal Purim observance on Shabbat:
• Two Torah scrolls: the weekly parsha plus the special reading of Vayavo Amalek (Exodus 17:8–16).
• The unique Haftarah for Purim when 15 Adar falls on Shabbat.

No Megillah, matanot la'evyonim, mishloach manot, or Purim seudah at home today — those were Friday or wait until Sunday.
    """.trimIndent()
}
