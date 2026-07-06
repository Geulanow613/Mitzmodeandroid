package com.beardytop.beatzaddik.domain

import kotlinx.datetime.DayOfWeek

/**
 * Catalog template keys + args for Erev Pesach checklist explainers.
 */
object ErevPesachExplainerTemplates {

    private val MECHIRAT_TEMPLATE = """
Mechirat chametz (מְכִירַת חָמֵץ) — selling your chametz to a non-Jew through your rabbi before Pesach — lets you keep chametz products locked away without owning them during Pesach (Shulchan Arukh O.C. 448).

Why:
• On Pesach, owning chametz is forbidden (bal yera'eh / bal yimatzei). Selling transfers ownership so sealed chametz in your home does not belong to you during the festival.

How to do it:
• Sign or authorize a sale with your rabbi or community (online forms are common). Deadline warning: While the sale takes effect on Erev Pesach morning, you must authorize your rabbi to sell your chametz several days in advance — most rabbis stop accepting sale forms by the night before Erev Pesach. The rabbi needs time to organize contracts and complete the kinyan (legal transfer) before the halachic deadline.
• Mark cabinets or rooms included in the sale; keep sold chametz separate from what you will burn or discard.
• Do not eat or use sold chametz after the sale takes effect — it belongs to the buyer until buy-back after Pesach.
• Dishes used with chametz: Many people include the chametz residue and absorbed flavor within their year-round dishes and pots in the sale, locking them away securely. The physical dishes themselves are not sold, avoiding the halachic requirement to re-immerse them in a Mikveh (tevilat kelim) after Pesach (Shulchan Arukh Y.D. 120). Rabbis structure the contract accordingly — follow your rabbi's sale form.
• Store the contract or confirmation; many communities sell through a central rabbi (e.g. local kashrut council).

After Pesach: chametz is repurchased per the terms of the sale — follow your rabbi's instructions on when you may use it again.
${'$'}urgency
${'$'}scheduleLeadIn${'$'}scheduleBody${'$'}scheduleYomTov
    """.trimIndent()

    private val SEDER_PREP_TEMPLATE = """
${'$'}intro

${'$'}sederNights
${'$'}secondSederHachana
${'$'}omerTrigger

Set up before Yom Tov:
• Matzah — shmurah matzah for motzi/matza mitzvah (three matzot on the plate per custom)
• Maror — romaine, horseradish, or bitter herbs per your minhag
• Four cups of wine per participant (grape juice is widely used if needed — ask your rav; not the same debate as Chol HaMoed wine)
• Haggadah for each person (or shared)
• Seder plate: zeroa (shankbone), beitzah (egg), karpas, charoset, maror, chazeret
• Seder plate prep: You should ideally roast your zeroa (shankbone) on Erev Pesach day before sunset. Because the shankbone is not eaten on Seder night, roasting it after the holiday begins violates Yom Tov cooking laws. The egg (beitzah), however, is traditionally eaten during the meal, so it may legally be boiled or roasted on Yom Tov night if needed.
• Reclining (hasebha): Recline to the left when drinking the four cups and eating matzah, korech, and afikoman — do not recline while eating maror or chazeret (they symbolize slavery).
• Festive table; candles for Yom Tov

Kitchen:
• Only kosher-for-Passover food and utensils from this point
• Warm food on a blech or pre-set timer for Yom Tov meals

At the Seder: follow your Haggadah step by step — Kiddush, the order of the night, brachot, and the four cups are all laid out there. ${'$'}sederNightCount
${'$'}scheduleLeadIn${'$'}scheduleBody${'$'}scheduleYomTov
    """.trimIndent()

    private val BEDIKAT_TEMPLATE = """
Bedikat chametz (בְּדִיקַת חָמֵץ) — the formal search for chametz — is a rabbinic mitzvah on the night **before** Erev Pesach day (after tzeit when the Hebrew date becomes 14 Nisan).

${'$'}whenLine

How to search:
• Use a candle (or flashlight per many poskim), a wooden spoon, and a feather (or paper bag) to gather crumbs.
• Search every room where chametz may have been brought during the year — especially kitchen, dining areas, living room, car, office, children's bags, and coat pockets.
• Check under furniture, cushions, car seats, and appliances where crumbs collect.
• Place ten pieces of bread (wrapped) in rooms before searching if your custom includes finding known pieces (optional minhag). If you hide them, write down every location and verify all 10 are recovered — a missed piece means known chametz remains in your home.

After the search:
• Recite the blessing Al bi'ur chametz and the Kol chamira nullification (bitul) — many siddurim print the text.
• Text difference: Use the night version of Kol Chamira from your siddur. It nullifies only chametz you have not seen and do not know about — because you may still legally own chametz for breakfast tomorrow morning.
• When Erev Pesach is Shabbat, this first bitul is Thursday night after bedikat; the final Kol Chamira is on Shabbat morning before the end of the 5th halachic hour — not at Friday's burning.
• Gather found chametz in a bag to destroy the next morning at biur chametz.
• Eating restrictions: You may not eat a meal or start work after nightfall until you complete the search. Once the search is finished, you may eat normally. Tomorrow morning is biur chametz — avoid a heavy meal from midday (chatzos) onward to preserve your appetite for the Seder.

If you are traveling or staying elsewhere, your host or rabbi can guide which rooms you are responsible to search.
${'$'}scheduleLeadIn${'$'}scheduleBody${'$'}scheduleYomTov
    """.trimIndent()

    private val BIUR_TEMPLATE = """
Biur chametz (בִּעוּר חָמֵץ) — destroying chametz — completes the mitzvah of removing leaven before Pesach.

${'$'}morningNote
• Burn or destroy all chametz found last night and any remaining chametz you are not selling.
• Many burn chametz in a safe outdoor fire; flushing crumbs in the toilet or similar is acceptable for small amounts per many poskim — ask your rav.
• ${'$'}zmanNote
• Timeline guardrail: ${'$'}timelineGuardrail
• Text difference: Use the morning version of Kol Chamira from your siddur — it is structurally different from the night text and completely disowns ALL chametz in your possession, whether you have seen it or not and whether you have destroyed it or not.
• Recite the final Kol Chamira immediately after destruction while still before that deadline${'$'}kolChamiraShabbatNote

Mechirat chametz:
• Any chametz included in the rabbi's sale should already be sealed and not eaten — only unsold chametz is burned.

After biur:
• Eat only kosher-for-Passover food until Pesach ends.
• Firstborns: Taanit Bechorot earlier per schedule; Seder ${'$'}sederTiming.
${'$'}scheduleLeadIn${'$'}scheduleBody${'$'}scheduleYomTov
    """.trimIndent()

    private val EREV_PESACH_ON_SHABBAT_SCHEDULE_BODY = """

This year, Erev Pesach is on Shabbat (14 Nisan). When Erev Pesach falls on Shabbat, the entire preparatory timeline shifts early (Peninei Halakha ch. 14):

• Taanit Bechorot: Thursday (12 Nisan) — moved early; not on Shabbat or Friday. Ashkenazim fast or attend a siyum. Many Sephardic authorities rule the moved fast is nullified entirely; many still attend a siyum out of custom (ask your rav).
• Bedikat chametz: Thursday night (night of 13 Nisan, after tzeit) with bracha — not the usual night of 14 Nisan, and not on Shabbat. Recite the first bitul (Kol Chamira) immediately after the search.
• Biur chametz: Friday morning (13 Nisan) — burn the chametz found. Do not recite the final Kol Chamira at the burning — you will still eat chametz over Shabbat.
• Mechirat chametz: complete and finalize the sale before Shabbat begins Friday evening.
• Matzah on Friday (13 Nisan): many authorities extend the prohibition of eating regular matzah to Friday as well when Erev Pesach falls on Shabbat, so matzah is eaten with a prime appetite at the Seder (ask your rav).
• Seder prep: all physical cooking, roasting the zeroa (shankbone), checking lettuce/maror, and making charoset must be finished on Friday before Shabbat — you cannot prepare on Shabbat for Motzei Shabbat.
• Shabbat (14 Nisan) — eating deadline: finish eating chametz by the end of the 4th halachic hour on Shabbat morning.
• Shabbat (14 Nisan) — disposal: flush leftover crumbs down the toilet or nullify them chemically (e.g. pour liquid soap over them) before the 5th hour. Do not burn on Shabbat.
• Shabbat (14 Nisan) — final nullification: recite the final Kol Chamira before the end of the 5th halachic hour on Shabbat morning.
• Shabbat meals — lechem mishneh: Sephardim may use egg matzah (matzah ashira). Ashkenazim do not eat egg matzah on Pesach (Rema O.C. 462:4); use small challah rolls with extreme caution over disposable plates, shake out garments completely, and flush all crumbs before the 4th-hour deadline. Seudah shlishit: meat, fish, or fruit — not regular matzah on Erev Pesach.
• First Seder: Saturday night after Shabbat fully ends (tzeit). Kiddush includes the full Yaknehaz sequence (Wine, Yom Tov Kiddush, Ner/candle, Havdalah text, and Shehecheyanu — no spices/besamim).

Plan with your rav and local zmanim — many communities publish a Pesach-on-Shabbat timetable.
    """.trimIndent()

    private val EREV_PESACH_FRIDAY_BEFORE_SHABBAT_SCHEDULE_BODY = """

This year, Erev Pesach is on Friday (14 Nisan) and the first day of Pesach is Shabbat (15 Nisan):

• Bedikat chametz: Thursday night (night of 14 Nisan, after tzeit) — not Friday night. Recite the first bitul (Kol Chamira).
• Taanit Bechorot: Friday daytime (14 Nisan) — fast or attend a siyum.
• Biur chametz: Friday morning (14 Nisan) by the 5th halachic hour deadline. Both physical destruction and the final Kol Chamira nullification must be finished before this time.
• Mechirat chametz: must be entirely completed before Shabbat/Yom Tov candle lighting on Friday evening.
• First Seder: Friday night (commencing 15 Nisan).
• Second Seder (Diaspora only): Saturday night — transitioning directly from Shabbat into the second day of Yom Tov (not a regular weekend Motzei Shabbat). Kiddush includes Yaknehaz (integrated Havdalah: wine, Kiddush, candle, Havdalah text — no Shehecheyanu on the second night of Pesach, and no spices/besamim).
• Preparation warning: you may NOT do any prep work (chopping, cooking, table setting) on Shabbat day for the second Seder. All preparations must wait until Shabbat ends at nightfall. Eruv tavshilin does not apply when Yom Tov falls on Shabbat — it is only for when Yom Tov immediately precedes Shabbat.

Confirm candle lighting, Yom Tov, and Seder times with your siddur and rav.
    """.trimIndent()

    private val EREV_PESACH_FRIDAY_13_NISAN_SCHEDULE = """
This year, Erev Pesach is Shabbat (14 Nisan). Today (Friday, 13 Nisan): biur chametz this morning (no final Kol Chamira at burning — save chametz for Shabbat meals), and finish mechirat chametz before Shabbat candles. Taanit Bechorot was Thursday (12 Nisan). Bedikat was last night (Thursday). Tomorrow is Shabbat — finish chametz by the 4th halachic hour, final bitul by the 5th halachic hour; first Seder is Saturday night (Yaknehaz, then Seder).
    """.trimIndent()

    private val TAANIT_BECHOR_TEMPLATE = """
Taanit Bechorot (תַּעֲנִית בְּכוֹרוֹת) — Fast of the Firstborn — is observed on Erev Pesach day by firstborn males (and some communities include firstborn females — ask your rav).

Why:
• Commemorates the plague of the firstborn in Egypt, when Jewish firstborn were spared.

The fast:
• When Erev Pesach is Shabbat (14 Nisan), Taanit Bechorot is moved to Thursday (12 Nisan) per Rama and Peninei Halakha — not Friday or Shabbat. Attend a siyum that day if that is your minhag.
• Many firstborns avoid fasting entirely by attending a siyum (completion of a Talmud tractate or similar Torah work) followed by a seudat mitzvah (festive meal). The exemption: To be exempt from the fast, you must not only hear the conclusion of the siyum but also participate in the seudat mitzvah by eating at least a kezayit (approx. 1 oz) of bread or cake. Leaving after the siyum without eating does not exempt you — you must still fast.
• If you fast: the fast runs from dawn (alot hashachar) until full nightfall (tzeit) — no eating or drinking in between.

Father of a firstborn son under bar mitzvah:
• If you are a man with a firstborn son below bar mitzvah age, the widespread custom is that you fast or attend a siyum yourself on his behalf — the young child does not observe the fast.

Plan ahead: locate a community siyum in advance if that is your minhag, or confirm fasting rules with your rav.
${'$'}scheduleLeadIn${'$'}scheduleBody${'$'}scheduleYomTov
    """.trimIndent()

    fun mechiratTemplate(): String = MECHIRAT_TEMPLATE

    fun taanitBechorTemplate(): String = TAANIT_BECHOR_TEMPLATE

    fun taanitBechorArgs(cal: DayInfo, profile: UserProfile): Map<String, String> =
        scheduleArgs(cal, profile, includePesachSchedule = true)

    fun mechiratArgs(cal: DayInfo, profile: UserProfile): Map<String, String> {
        val nissanDay = cal.hebrewDay ?: 14
        val dow = ErevPesachPrepText.pesachErevDow(cal) ?: ErevPesachPrepText.PesachErevDow.WEEKDAY
        val urgency = when {
            nissanDay >= ErevPesachPrepText.mechiratAuthorizeThroughNissanDay(dow) ->
                "\n\nToday is the deadline to authorize mechirat chametz with your rabbi. The sale takes effect on Erev Pesach morning, but rabbis need your form in advance — most stop accepting by the night before Erev Pesach at the latest."
            nissanDay >= 11 ->
                "\n\nAuthorize mechirat chametz with your rabbi soon — do not wait until Erev Pesach day."
            else -> ""
        }
        return mapOf(
            "urgency" to urgency,
        ) + scheduleArgs(cal, profile, includePesachSchedule = true)
    }

    fun sederPrepTemplate(): String = SEDER_PREP_TEMPLATE

    fun sederPrepArgs(cal: DayInfo, profile: UserProfile): Map<String, String> {
        val secondSederHachana = if (
            ErevPesachPrepText.isErevPesachFridayBeforeShabbatPesach(cal) && !profile.isInIsrael
        ) {
            """

Second Seder prep (Diaspora this year):
• Saturday daytime is the first day of Yom Tov (Shabbat). Saturday night begins the second day of Yom Tov — not a regular Motzei Shabbat weekend.
• Kiddush at the second Seder includes Yaknehaz (integrated Havdalah: wine, Kiddush, candle, Havdalah text — no Shehecheyanu on the second night of Pesach, and no spices).
• You may NOT cook, chop, or prepare on Shabbat day for the second Seder. Hachana from weekday across Shabbat to a later Yom Tov is forbidden. Eruv tavshilin does not apply when Yom Tov falls on Shabbat — it is only when Yom Tov immediately precedes Shabbat. All food prep, cooking, and table setting for the second night must wait until Shabbat fully ends Saturday night.
            """.trim()
        } else {
            ""
        }
        val sederNights = when {
            ErevPesachPrepText.isErevPesachFridayBeforeShabbatPesach(cal) ->
                "This year: first Seder is tonight (Friday night, commencing 15 Nisan). In the Diaspora, the second Seder is Saturday night — transitioning from Shabbat directly into the second day of Yom Tov, with Yaknehaz in Kiddush."
            ErevPesachPrepText.isErevPesachOnShabbat(cal) ->
                "This year: first Seder is tomorrow night (Motzei Shabbat / Saturday night) — Havdalah in Kiddush (Yaknehaz), then Seder."
            cal.hebrewDay == 13 &&
                ErevPesachPrepText.pesachErevDow(cal) == ErevPesachPrepText.PesachErevDow.SHABBAT ->
                "This year: finish all Seder setup today (Friday) before Shabbat. First Seder is Saturday night after Havdalah."
            profile.isInIsrael ->
                "In Israel: one seder tonight (15 Nisan)."
            else ->
                "In the Diaspora: first seder tonight; second seder tomorrow night."
        }
        val omerTrigger = if (profile.isInIsrael) {
            """
• Omer count trigger: The night after the first Seder (16 Nisan) officially begins Sefirat HaOmer. Before leaving the table on that night, count Day 1 of the Omer — if you miss a full day-and-night cycle, ask your rav about continuing with a bracha (see the Omer checklist item)."""
        } else {
            """
• Omer count trigger: The second night of Pesach (second Seder) officially begins Sefirat HaOmer. Before leaving the Seder table on the second night, count Day 1 of the Omer — if you miss a full day-and-night cycle, ask your rav about continuing with a bracha (see the Omer checklist item)."""
        }
        val intro = when {
            ErevPesachPrepText.isErevPesachFridayBeforeShabbatPesach(cal) ->
                "Pesach begins at the next nightfall — use today (Friday) to prepare so the first Seder focuses on mitzvot, not logistics."
            ErevPesachPrepText.isErevPesachOnShabbat(cal) ->
                "Today is Shabbat — Pesach begins after Shabbat ends (Motzei Shabbat). Finish what you can before Shabbat; complete Seder setup after Havdalah."
            cal.hebrewDay == 13 &&
                ErevPesachPrepText.pesachErevDow(cal) == ErevPesachPrepText.PesachErevDow.SHABBAT ->
                "Erev Pesach is tomorrow (Shabbat). Finish Seder preparations today before Shabbat candles — you cannot prepare on Shabbat for Motzei Shabbat."
            else ->
                "Use today to prepare so the Seder focuses on mitzvot, not logistics — phones will be off once Yom Tov begins."
        }
        return mapOf(
            "intro" to intro,
            "sederNights" to sederNights,
            "secondSederHachana" to secondSederHachana,
            "omerTrigger" to omerTrigger,
            "sederNightCount" to if (profile.isInIsrael) "One" else "Two",
        ) + scheduleArgs(cal, profile)
    }

    fun bedikatTemplate(): String = BEDIKAT_TEMPLATE

    fun bedikatArgs(cal: DayInfo, profile: UserProfile): Map<String, String> {
        val tz = cal.zmanim?.timezoneId ?: profile.timezoneId
        val tzeit = cal.zmanim?.tzeitMillis ?: cal.zmanim?.sunsetMillis
        val dow = ErevPesachPrepText.pesachErevDow(cal)
        val whenLine = when (dow) {
            ErevPesachPrepText.PesachErevDow.SHABBAT ->
                "Tonight after nightfall (Thursday night, beginning 13 Nisan) — not on Shabbat and not the usual Erev Pesach night."
            ErevPesachPrepText.PesachErevDow.FRIDAY ->
                "Tonight after nightfall (Thursday night, beginning 14 Nisan) — not Friday night."
            else -> ZmanimFormatter.formatTime(tzeit, tz)?.let { time ->
                "Tonight after nightfall (beginning 14 Nisan) — ideally after $time (tzeit)."
            } ?: "Tonight after nightfall (beginning 14 Nisan). Enable location in the app for your local time."
        }
        return mapOf(
            "whenLine" to whenLine,
        ) + scheduleArgs(cal, profile, includePesachSchedule = false)
    }

    fun biurTemplate(): String = BIUR_TEMPLATE

    fun biurArgs(cal: DayInfo, profile: UserProfile): Map<String, String> {
        val tz = cal.zmanim?.timezoneId ?: profile.timezoneId
        val zmanim = cal.zmanim
        val dow = ErevPesachPrepText.pesachErevDow(cal) ?: ErevPesachPrepText.PesachErevDow.WEEKDAY
        val end4th = zmanim?.let { ZmanimHelpers.endOfHalachicHourMillis(it, 4) }
            ?: zmanim?.sofZmanTefillaMillis
        val end5th = zmanim?.let { ZmanimHelpers.endOfHalachicHourMillis(it, 5) }
        val zmanNote = buildString {
            if (end4th != null) {
                append("Finish eating chametz by end of the 4th halachic hour (approx. ")
                append(ZmanimFormatter.formatTime(end4th, tz) ?: "see zmanim app")
                append(" today). ")
            }
            if (end5th != null && dow != ErevPesachPrepText.PesachErevDow.SHABBAT) {
                append("Both biur (destruction) and the final Kol Chamira must be completed before end of the 5th halachic hour (approx. ")
                append(ZmanimFormatter.formatTime(end5th, tz) ?: "see zmanim app")
                append(" — earlier than solar chatzos midday). ")
            } else if (end5th != null && dow == ErevPesachPrepText.PesachErevDow.SHABBAT) {
                append("Destroy chametz by the end of the 5th halachic hour (approx. ")
                append(ZmanimFormatter.formatTime(end5th, tz) ?: "see zmanim app")
                append(") — but do not recite the final Kol Chamira here; that is on Shabbat morning. ")
            }
            if (isEmpty()) {
                append("Use a zmanim app for Erev Pesach: end of 4th halachic hour (stop eating chametz) and end of 5th halachic hour (biur and Kol Chamira deadline). ")
            }
        }
        val morningNote = when (dow) {
            ErevPesachPrepText.PesachErevDow.SHABBAT ->
                "Friday morning of 13 Nisan (before Shabbat) — complete biur. Do not recite the final Kol Chamira at the burning — keep chametz for Shabbat meals."
            ErevPesachPrepText.PesachErevDow.FRIDAY ->
                "Morning of Erev Pesach (today, Friday, 14 Nisan) — complete biur before Shabbat:"
            else -> "Morning of Erev Pesach (14 Nisan) — complete biur today:"
        }
        val timelineGuardrail = when (dow) {
            ErevPesachPrepText.PesachErevDow.SHABBAT ->
                "On this year's schedule, do not recite the final Kol Chamira at today's burning — keep chametz for Shabbat meals. The final Kol Chamira is recited on Shabbat morning before the end of the 5th halachic hour."
            else ->
                "Both the physical destruction of chametz and the final recitation of Kol Chamira must be fully completed before the end of the 5th halachic hour. Once the 5th hour ends, chametz becomes assur b'hana'ah — you can no longer nullify ownership, and a late Kol Chamira is invalid."
        }
        val kolChamiraShabbatNote = if (dow == ErevPesachPrepText.PesachErevDow.SHABBAT) {
            " (on Shabbat morning, not at today's burning)"
        } else {
            ""
        }
        val sederTiming = when (dow) {
            ErevPesachPrepText.PesachErevDow.SHABBAT -> "after Shabbat (Motzei Shabbat)"
            ErevPesachPrepText.PesachErevDow.FRIDAY ->
                "tonight (first Seder); second Seder Saturday night in the Diaspora (Yaknehaz — Shabbat into second day of Yom Tov)"
            else -> "tonight"
        }
        return mapOf(
            "morningNote" to morningNote,
            "zmanNote" to zmanNote,
            "timelineGuardrail" to timelineGuardrail,
            "kolChamiraShabbatNote" to kolChamiraShabbatNote,
            "sederTiming" to sederTiming,
        ) + scheduleArgs(cal, profile)
    }

    private fun pesachScheduleParts(cal: DayInfo): PesachScheduleParts? {
        return when {
        ErevPesachPrepText.isErevPesachOnShabbat(cal) -> PesachScheduleParts(
            "Today is Shabbat — Erev Pesach. The steps below should already be done; use this as a checklist.",
            EREV_PESACH_ON_SHABBAT_SCHEDULE_BODY,
        )
        ErevPesachPrepText.isErevPesachFridayBeforeShabbatPesach(cal) -> PesachScheduleParts(
            "Today is Erev Pesach (Friday). The calendar below should already be in motion.",
            EREV_PESACH_FRIDAY_BEFORE_SHABBAT_SCHEDULE_BODY,
        )
        else -> {
            val erev = ErevPesachPrepText.erevPesachCivilDate(cal) ?: return null
            val day = cal.hebrewDay ?: return null
            if (cal.hebrewMonth != HebrewCalendarEngine.NISSAN || day > 14) return null
            when (erev.dayOfWeek) {
                DayOfWeek.SATURDAY -> when (day) {
                    11 -> PesachScheduleParts(
                        "Read this before bedikat chametz — tomorrow (Thursday) night after tzeit is the search, not the usual Erev Pesach night.",
                        EREV_PESACH_ON_SHABBAT_SCHEDULE_BODY,
                    )
                    12 -> PesachScheduleParts(
                        "Read this before bedikat chametz — tonight (Thursday night after tzeit) is bedikat, not tomorrow.",
                        EREV_PESACH_ON_SHABBAT_SCHEDULE_BODY,
                    )
                    13 -> PesachScheduleParts("", EREV_PESACH_FRIDAY_13_NISAN_SCHEDULE)
                    else -> null
                }
                DayOfWeek.FRIDAY -> when (day) {
                    12 -> PesachScheduleParts(
                        "Read this before bedikat chametz — tomorrow (Thursday) night after tzeit is bedikat, not Friday night.",
                        EREV_PESACH_FRIDAY_BEFORE_SHABBAT_SCHEDULE_BODY,
                    )
                    13 -> PesachScheduleParts(
                        "Read this before bedikat chametz — tonight (Thursday night after tzeit) is bedikat. Tomorrow is Erev Pesach (Friday): Taanit Bechorot, biur, and mechirat before Shabbat.",
                        EREV_PESACH_FRIDAY_BEFORE_SHABBAT_SCHEDULE_BODY,
                    )
                    else -> null
                }
                else -> null
            }
        }
        }
    }

    /** Combined lead-in + schedule body for legacy append paths (taanit bechor, Yom Tov prep). */
    fun pesachScheduleBlock(cal: DayInfo): String? {
        val parts = pesachScheduleParts(cal) ?: return null
        return buildString {
            if (parts.leadIn.isNotEmpty()) {
                append(parts.leadIn.trim())
                append("\n\n")
            }
            append(parts.body.trim())
        }.trim().ifEmpty { null }
    }

    private data class PesachScheduleParts(val leadIn: String, val body: String)

    private fun scheduleArgs(
        cal: DayInfo,
        profile: UserProfile,
        includePesachSchedule: Boolean = true,
    ): Map<String, String> {
        val pesach = if (includePesachSchedule) pesachScheduleParts(cal) else null
        val chagName = cal.upcomingChagName ?: "Pesach"
        val yomTovBlock = YomTovShabbatPrepText.scheduleBlock(cal, profile, chagName)
        val yomTov = when {
            pesach != null && yomTovBlock != null &&
                ErevPesachPrepText.isErevPesachFridayBeforeShabbatPesach(cal) -> ""
            pesach != null && yomTovBlock != null &&
                YomTovShabbatPrepText.isShabbatErevChag(cal) -> yomTovBlock
            pesach == null -> yomTovBlock.orEmpty()
            else -> ""
        }
        val leadIn = pesach?.leadIn?.takeIf { it.isNotEmpty() }?.let { "\n\n$it" }.orEmpty()
        val body = pesach?.body.orEmpty()
        val yomTovSuffix = yomTov.takeIf { it.isNotEmpty() }?.let { "\n\n$it" }.orEmpty()
        return mapOf(
            "scheduleLeadIn" to leadIn,
            "scheduleBody" to body,
            "scheduleYomTov" to yomTovSuffix,
        )
    }
}
