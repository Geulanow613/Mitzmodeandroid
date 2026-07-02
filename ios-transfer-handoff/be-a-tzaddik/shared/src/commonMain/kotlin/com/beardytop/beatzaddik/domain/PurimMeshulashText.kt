package com.beardytop.beatzaddik.domain

/**
 * Purim Meshulash (Jerusalem when Shushan Purim falls on Shabbat): mitzvot split across
 * Thursday night / Friday / Sunday, with Shabbat in between. Users need the full schedule
 * before Shabbat when the phone is off.
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
Purim Meshulash (פורים מְשֻׁלָּשׁ) — Jerusalem when Shushan Purim (15 Adar) falls on Shabbat

Why the calendar splits:
• Your Purim observance (walled city / Jerusalem) would normally be 15 Adar, but Purim mitzvot are not done on Shabbat.
• Megillah and matanot la'evyonim move to Friday (14 Adar). Mishloach manot and the seudah move to Sunday (16 Adar). Shabbat (15 Adar): individual home mitzvot (Megillah, matanot, mishloach, seudah) do not happen today, but Shabbat carries communal Purim obligations — see below.

Read this entire guide before Shabbat — your phone will be off on Shabbat, so you will not see Sunday's checklist until after Havdalah.

The four mitzvot — when they happen this year:
1. Megillah — Thursday night after tzeit (start of Friday / 14 Adar) AND Friday daytime (before sunset). Two readings, like a normal Purim. Confirm times with your shul; write them down or print them.
2. Matanot la'evyonim — Friday daytime only (not Thursday night, not Shabbat). Give at least one gift to each of two different poor people (minimum of two recipients total); money is common. Prepare cash, envelopes, or charity contacts before Friday.
3. Mishloach manot — Sunday (16 Adar) before sunset. At least two ready-to-eat foods to one friend. Prepare and label packages before Shabbat; plan who delivers on Sunday (you, family, or shul list).
4. Purim seudah — Sunday afternoon (16 Adar), before sunset. Festive meal with bread, joy, and Torah words — not on Shabbat.

What to finish before Shabbat candles (Friday):
• Megillah: attend Thursday night and Friday morning readings (or know your shul's schedule).
• Matanot: complete on Friday — have funds ready Friday morning.
• Mishloach: packages packed, labeled, and stored; delivery list written (Sunday only).
• Seudah: Sunday menu and timing planned; invite guests if needed.
• Machatzit haShekel: if your custom, many give before Megillah — handle before or with Thursday/Friday readings.

Shabbat (15 Adar):
• While individual home mitzvot like Megillah, matanot, mishloach, and the seudah do not happen today, Shabbat carries the communal Purim obligations.
• Synagogue: Attend for the special Purim Torah reading (Parashat Vayavo Amalek, Exodus 17:8–16) and the unique Haftarah for Purim.
• Davening & meals: Insert Al HaNissim into all Shabbat prayers (Amidah) and Birkat Hamazon today. Do not say Al HaNissim on Friday (14 Adar) or Sunday (16 Adar), even though other Purim mitzvot happen those days.
• No melacha for Purim prep on Shabbat — everything for Sunday must already be prepared.

Sunday (16 Adar):
• Send mishloach manot and celebrate the Purim seudah. The app will show today's items after Shabbat ends.

Ask your rav about edge cases (travel, illness, minhag).
    """.trimIndent()

    private val ADVANCE_PREP_TEMPLATE = """
Tomorrow is erev Purim — and this year is Purim Meshulash in Jerusalem. Shabbat is in the middle, so you need the full plan now (not only tomorrow).

${'$'}scheduleBlock
    """.trimIndent()

    private val EREV_PREP_TEMPLATE = """
Purim Meshulash starts tonight in Jerusalem. Because Shabbat falls in the middle of the festival, read and save this plan now — you will not be able to rely on the app on Shabbat for Sunday's mitzvot.

${'$'}scheduleBlock

Tonight (Thursday night after tzeit): first Megillah reading. Tomorrow (Friday): second Megillah reading and matanot la'evyonim. Mishloach and seudah wait until Sunday.
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

    fun fridayMegillahTemplate(): String = FRIDAY_MEGILLAH_BODY

    fun fridayMatanotTemplate(): String = FRIDAY_MATANOT_BODY

    fun sundayMishloachTemplate(): String = SUNDAY_MISHLOACH_BODY

    fun sundaySeudahTemplate(): String = SUNDAY_SEUDAH_BODY

    fun fridayMegillahExplanation(): String = BeginnerHalachaGlossary.withKeyTerms(
        BeginnerHalachaGlossary.purimBasics(),
        FRIDAY_MEGILLAH_BODY,
    )

    private val FRIDAY_MEGILLAH_BODY = """
Purim Meshulash — Megillah on Friday (14 Adar)

When this year:
• Thursday night after tzeit — first reading (you should have heard it then).
• Today (Friday) — second reading during the daytime (usually after Shacharit, before sunset) — a universal obligation, not optional.

How (same laws as regular Purim):
• Hear every word from a kosher megillah scroll; men and women are equally obligated.
• Stand for the blessings; customs at Haman's name vary by shul.

${PurimBrachotText.MEGILLAH_BLESSINGS_COMMON}

Reminder: matanot la'evyonim are also today (Friday), not Shabbat. Mishloach manot and the seudah are Sunday — prepare packages before Shabbat if you have not already.
    """.trimIndent()

    fun fridayMatanotExplanation(): String = BeginnerHalachaGlossary.withKeyTerms(
        BeginnerHalachaGlossary.purimBasics(),
        FRIDAY_MATANOT_BODY,
    )

    private val FRIDAY_MATANOT_BODY = """
Purim Meshulash — matanot la'evyonim on Friday only (14 Adar)

Today (Friday daytime) — not on Shabbat:
• Give at least one gift to each of two different poor people (minimum of two recipients total).
• Each gift should enable a modest Purim meal (money is common; amounts vary by community).
• Many give after the daytime Megillah reading.

You may use a trustworthy messenger or organization that distributes today. If you cannot find recipients, ask your rabbi or shul Friday morning.

Sunday is for mishloach manot and the seudah — those should already be prepared before Shabbat.
    """.trimIndent()

    fun sundayMishloachExplanation(): String = BeginnerHalachaGlossary.withKeyTerms(
        BeginnerHalachaGlossary.purimBasics(),
        SUNDAY_MISHLOACH_BODY,
    )

    private val SUNDAY_MISHLOACH_BODY = """
Purim Meshulash — mishloach manot on Sunday (16 Adar)

Deferred from Shabbat because Purim mitzvot are not performed on Shabbat this year.

The mitzvah:
• Send at least two different ready-to-eat foods or drinks to one friend today — one package.
• Deliver before sunset; a messenger is fine.
• Food should be ready to eat without cooking; label sender and recipient.

You should have prepared packages before Shabbat. If not, ask your rav what you may still do today.
    """.trimIndent()

    fun sundaySeudahExplanation(): String = BeginnerHalachaGlossary.withKeyTerms(
        BeginnerHalachaGlossary.purimBasics(),
        SUNDAY_SEUDAH_BODY,
    )

    private val SUNDAY_SEUDAH_BODY = """
Purim Meshulash — Purim seudah on Sunday (16 Adar)

The festive Purim meal is today (not Friday or Shabbat this year).

When:
• Sunday daytime before sunset — many hold the meal in the afternoon after mishloach manot.

How:
• Festive meal with bread, meat, wine, and joy; include words of Torah or thanks to Hashem.
• Drinking wine is a widespread custom; celebrate responsibly.

This completes the four Purim mitzvot for Purim Meshulash in Jerusalem.
    """.trimIndent()
}
