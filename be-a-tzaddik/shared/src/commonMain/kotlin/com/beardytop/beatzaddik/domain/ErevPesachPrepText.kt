package com.beardytop.beatzaddik.domain

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.plus

/**
 * Full explainers for Erev Pesach checklist items (14 Nisan).
 */
object ErevPesachPrepText {

    /** Gregorian date of 14 Nisan in the current Hebrew year, when today is on or before Erev Pesach. */
    fun erevPesachCivilDate(cal: DayInfo) = run {
        val month = cal.hebrewMonth ?: return@run null
        val day = cal.hebrewDay ?: return@run null
        if (month != HebrewCalendarEngine.NISSAN || day > 14) return@run null
        cal.date.plus(14 - day, DateTimeUnit.DAY)
    }

    /** 14 Nisan falls on Shabbat — laws move earlier; Seder is Motzei Shabbat. */
    fun isErevPesachOnShabbat(cal: DayInfo): Boolean =
        "erev_pesach" in cal.activeSeasons && cal.isShabbat

    /** 14 Nisan is Friday — first Seder is Friday night; 15 Nisan (first day of Pesach) is Shabbat. */
    fun isErevPesachFridayBeforeShabbatPesach(cal: DayInfo): Boolean =
        "erev_pesach" in cal.activeSeasons && cal.isErevShabbat

    /** This year Pesach intersects Shabbat (either case above, or week-before lead-up). */
    fun isPesachShabbatScheduleYear(cal: DayInfo): Boolean {
        val erev = erevPesachCivilDate(cal) ?: return false
        return erev.dayOfWeek == DayOfWeek.FRIDAY || erev.dayOfWeek == DayOfWeek.SATURDAY
    }

    /** Hebrew day of month in Nissan, when still on or before Erev Pesach. */
    private fun nissanDay(cal: DayInfo): Int? {
        val month = cal.hebrewMonth ?: return null
        val day = cal.hebrewDay ?: return null
        if (month != HebrewCalendarEngine.NISSAN || day > 14) return null
        return day
    }

    private fun appendShabbatSchedule(
        body: String,
        cal: DayInfo,
        profile: UserProfile,
        includePesachSchedule: Boolean = true,
    ): String {
        val pesachBlock = if (includePesachSchedule) erevPesachShabbatScheduleBlock(cal) else null
        val chagName = cal.upcomingChagName ?: "Pesach"
        val yomTovBlock = YomTovShabbatPrepText.scheduleBlock(cal, profile, chagName)
        val blocks = when {
            pesachBlock != null && yomTovBlock != null &&
                isErevPesachFridayBeforeShabbatPesach(cal) -> "$pesachBlock\n\n$yomTovBlock"
            pesachBlock != null && YomTovShabbatPrepText.isShabbatErevChag(cal) ->
                listOfNotNull(pesachBlock, yomTovBlock).joinToString("\n\n")
            else -> listOfNotNull(pesachBlock, yomTovBlock).joinToString("\n\n")
        }
        if (blocks.isEmpty()) return body
        return "$body\n\n$blocks"
    }

    private fun erevPesachOnShabbatFullSchedule(bedikatLeadIn: String): String = """
$bedikatLeadIn

This year, Erev Pesach is on Shabbat (14 Nisan). Per Peninei Halakha (ch. 14), STAR-K, OU, and Ohr Somayach — the schedule moves earlier:

• Taanit Bechorot: Thursday (12 Nisan) — not on Shabbat or Friday. Ashkenazim (Rama) fast or attend a siyum; some Sephardic authorities omit the moved fast — many still attend a siyum (ask your rav).
• Bedikat chametz: Thursday night (night of 13 Nisan, after tzeit) with bracha — not the usual night of 14 Nisan, and not on Shabbat. Recite the first bitul (Kol Chamira) after the search as in a normal year.
• Biur chametz: Friday morning (13 Nisan) — burn the chametz found; preferably by the end of the 5th halachic hour as in other years. Do not recite the final Kol Chamira at the burning — keep chametz for the first two Shabbat meals (STAR-K; OU).
• Mechirat chametz: complete before Shabbat begins Friday evening; seal sold chametz away.
• Shabbat (14 Nisan): Finish eating chametz by the end of the 4th halachic hour on Shabbat morning. Destroy leftover crumbs without burning on Shabbat (e.g. flush down the toilet). Recite the final Kol Chamira before the end of the 5th halachic hour on Shabbat morning (Peninei Halakha; STAR-K).
• Shabbat meals: Use challah or egg matzah for lechem mishneh at the first two meals (many use disposable dishes and KP food for the rest of the meal — see Peninei Halakha). Seudah shlishit: meat, fish, or fruit — not regular matzah on Erev Pesach (Peninei Halakha; STAR-K).
• Seder preparations (charoset, maror, zeroa, etc.): finish on Friday before Shabbat — do not prepare on Shabbat for Motzei Shabbat (Peninei Halakha; OU).
• First Seder: Saturday night after Shabbat (tzeit) — Kiddush with Havdalah (Yaknehaz) in the Haggadah, then the Seder (STAR-K).

Plan with your rav and local zmanim — many communities publish a Pesach-on-Shabbat timetable.
    """.trim()

    private fun erevPesachFridayBeforeShabbatFullSchedule(bedikatLeadIn: String): String = """
$bedikatLeadIn

This year, Erev Pesach is on Friday (14 Nisan) and the first day of Pesach is Shabbat (15 Nisan):

• Bedikat chametz: Thursday night (after tzeit) — not Friday night.
• Taanit Bechorot: today (Friday, 14 Nisan) — fast or attend a siyum.
• Biur chametz: this morning (14 Nisan), by the usual 4th/5th halachic hour deadlines, with Kol Chamira after burning as usual.
• Mechirat chametz: must be completed before Shabbat candles tonight.
• First Seder: tonight (Friday night) — Pesach begins at nightfall, not Saturday night.
• Shabbat (15 Nisan): first day of Yom Tov — no chametz; only Pesach food and dishes. In the Diaspora, the second Seder is Saturday night after Shabbat. Prepare what you can today before Shabbat for tomorrow night where halacha allows.

Confirm candle lighting, Yom Tov, and Seder times with your siddur and rav.
    """.trim()

    fun erevPesachShabbatScheduleBlock(cal: DayInfo): String? = when {
        isErevPesachOnShabbat(cal) -> erevPesachOnShabbatFullSchedule(
            "Today is Shabbat — Erev Pesach. The steps below should already be done; use this as a checklist."
        )

        isErevPesachFridayBeforeShabbatPesach(cal) -> erevPesachFridayBeforeShabbatFullSchedule(
            "Today is Erev Pesach (Friday). The calendar below should already be in motion."
        )

        else -> {
            val erev = erevPesachCivilDate(cal) ?: return null
            val day = nissanDay(cal) ?: return null
            when (erev.dayOfWeek) {
                DayOfWeek.SATURDAY -> when (day) {
                    11 -> erevPesachOnShabbatFullSchedule(
                        "Read this before bedikat chametz — tomorrow (Thursday) night after tzeit is the search, not the usual Erev Pesach night."
                    )
                    12 -> erevPesachOnShabbatFullSchedule(
                        "Read this before bedikat chametz — tonight (Thursday night after tzeit) is bedikat, not tomorrow."
                    )
                    13 -> """
This year, Erev Pesach is Shabbat (14 Nisan). Today (Friday, 13 Nisan): biur chametz this morning (no final Kol Chamira at burning — save chametz for Shabbat meals), and finish mechirat chametz before Shabbat candles. Taanit Bechorot was Thursday (12 Nisan). Bedikat was last night (Thursday). Tomorrow is Shabbat — finish chametz by the 4th halachic hour, final bitul by the 5th halachic hour; first Seder is Saturday night (Yaknehaz, then Seder).
                    """.trim()
                    else -> null
                }

                DayOfWeek.FRIDAY -> when (day) {
                    12 -> erevPesachFridayBeforeShabbatFullSchedule(
                        "Read this before bedikat chametz — tomorrow (Thursday) night after tzeit is bedikat, not Friday night."
                    )
                    13 -> erevPesachFridayBeforeShabbatFullSchedule(
                        "Read this before bedikat chametz — tonight (Thursday night after tzeit) is bedikat. Tomorrow is Erev Pesach (Friday): Taanit Bechorot, biur, and mechirat before Shabbat."
                    )
                    else -> null
                }

                else -> null
            }
        }
    }

    /** Bedikat row appears on 14 Nisan; full Pesach–Shabbat timeline is shown earlier on week-before prep. */
    private fun includePesachScheduleOnBedikatItem(cal: DayInfo): Boolean =
        !isErevPesachOnShabbat(cal) && !isErevPesachFridayBeforeShabbatPesach(cal)

    fun mechiratExplanation(cal: DayInfo, profile: UserProfile): String =
        appendShabbatSchedule(BeginnerHalachaGlossary.withKeyTerms(BeginnerHalachaGlossary.pesachPrep(), """
Mechirat chametz (מְכִירַת חָמֵץ) — selling your chametz to a non-Jew through your rabbi before Pesach — lets you keep chametz products locked away without owning them during Pesach (Shulchan Arukh O.C. 448).

Why:
• On Pesach, owning chametz is forbidden (bal yera'eh / bal yimatzei). Selling transfers ownership so sealed chametz in your home does not belong to you during the festival.

How to do it:
• Sign or authorize a sale with your rabbi or community (online forms are common). Do this before the final time to own chametz on Erev Pesach morning.
• Mark cabinets or rooms included in the sale; keep sold chametz separate from what you will burn or discard.
• Do not eat or use sold chametz after the sale takes effect — it belongs to the buyer until buy-back after Pesach.
• Store the contract or confirmation; many communities sell through a central rabbi (e.g. local kashrut council).

After Pesach: chametz is repurchased per the terms of the sale — follow your rabbi's instructions on when you may use it again.
    """.trim()), cal, profile)

    fun taanitBechorExplanation(cal: DayInfo, profile: UserProfile): String = appendShabbatSchedule(BeginnerHalachaGlossary.withKeyTerms(BeginnerHalachaGlossary.pesachPrep(), """
Taanit Bechorot (תַּעֲנִית בְּכוֹרוֹת) — Fast of the Firstborn — is observed on Erev Pesach day by firstborn males (and some communities include firstborn females — ask your rav).

Why:
• Commemorates the plague of the firstborn in Egypt, when Jewish firstborn were spared.

The fast:
• From dawn until nightfall on Erev Pesach (or until you attend a seudat mitzvah that exempts you).
• When Erev Pesach is Shabbat (14 Nisan), Taanit Bechorot is moved to Thursday (12 Nisan) per Rama, Peninei Halakha, STAR-K, OU, and Ohr Somayach — not Friday or Shabbat. Attend a siyum that day if that is your minhag.
• Many firstborns avoid the fast by attending a siyum (completion of a Talmud tractate or similar Torah work) followed by a festive meal — the siyum itself is the exemption per widespread custom.
• If you fast: no eating or drinking from alot hashachar until after the fast ends (many end at a siyum, at mincha, or at night — follow your rabbi).

Father of a firstborn son under bar mitzvah:
• If you are a man with a firstborn son below bar mitzvah age, the widespread custom is that you fast or attend a siyum yourself on his behalf — the young child does not observe the fast.

Plan ahead: locate a community siyum in advance if that is your minhag, or confirm fasting rules with your rav.
    """.trim()), cal, profile)

    fun sederPrepExplanation(cal: DayInfo, profile: UserProfile): String {
        val sederNights = when {
            isErevPesachFridayBeforeShabbatPesach(cal) ->
                "This year: first Seder is tonight (Friday night). The second Seder in the Diaspora is tomorrow night (Saturday night), after Shabbat."
            isErevPesachOnShabbat(cal) ->
                "This year: first Seder is tomorrow night (Motzei Shabbat / Saturday night) — Havdalah in Kiddush (Yaknehaz), then Seder."
            profile.isInIsrael ->
                "In Israel: one seder tonight (15 Nisan)."
            else ->
                "In the Diaspora: first seder tonight; second seder tomorrow night."
        }
        val intro = when {
            isErevPesachFridayBeforeShabbatPesach(cal) ->
                "Pesach begins tonight — use today (Friday) to prepare so tonight's Seder focuses on mitzvot, not logistics."
            isErevPesachOnShabbat(cal) ->
                "Today is Shabbat — Pesach begins tomorrow night (Motzei Shabbat). Prepare before Shabbat began what you can; finish Seder setup after Havdalah."
            else ->
                "Tonight begins Pesach — prepare so the Seder focuses on mitzvot, not logistics."
        }
        return appendShabbatSchedule(BeginnerHalachaGlossary.withKeyTerms(BeginnerHalachaGlossary.pesachPrep(), """
$intro

$sederNights

Set up before Yom Tov:
• Matzah — shmurah matzah for motzi/matza mitzvah (three matzot on the plate per custom)
• Maror — romaine, horseradish, or bitter herbs per your minhag
• Four cups of wine per participant (grape juice is widely used if needed — ask your rav; not the same debate as Chol HaMoed wine)
• Haggadah for each person (or shared)
• Seder plate: zeroa (shankbone), beitzah (egg), karpas, charoset, maror, chazeret
• Reclining pillows; festive table; candles for Yom Tov

Kitchen:
• Only kosher-for-Passover food and utensils from this point
• Warm food on a blech or pre-set timer for Yom Tov meals

At the Seder: follow your Haggadah step by step — Kiddush, the order of the night, brachot, and the four cups are all laid out there. ${if (profile.isInIsrael) "One" else "Two"} Seder night(s) this Pesach.
        """.trim()), cal, profile)
    }

    fun bedikatExplanation(cal: DayInfo, profile: UserProfile): String {
        val tz = cal.zmanim?.timezoneId ?: profile.timezoneId
        val tzeit = cal.zmanim?.tzeitMillis ?: cal.zmanim?.sunsetMillis
        val whenLine = when {
            isErevPesachFridayBeforeShabbatPesach(cal) ->
                "This year bedikat was due last night (Thursday night after tzeit). If you already searched, you are set; if not, ask your rabbi right away."
            isErevPesachOnShabbat(cal) ->
                "This year bedikat was due Thursday night (13 Nisan after tzeit), not tonight."
            else -> ZmanimFormatter.formatTime(tzeit, tz)?.let { time ->
                "Tonight after nightfall — ideally after $time (tzeit). Begin once it is fully dark."
            } ?: "Tonight after nightfall (tzeit). Enable location in the app for your local time."
        }

        return appendShabbatSchedule(BeginnerHalachaGlossary.withKeyTerms(BeginnerHalachaGlossary.pesachPrep(), """
Bedikat chametz (בְּדִיקַת חָמֵץ) — the formal search for chametz — is a rabbinic mitzvah on the night of 14 Nisan (Erev Pesach).

$whenLine

How to search:
• Use a candle (or flashlight per many poskim), a wooden spoon, and a feather (or paper bag) to gather crumbs.
• Search every room where chametz may have been brought during the year — especially kitchen, dining areas, living room, car, office, children's bags, and coat pockets.
• Check under furniture, cushions, car seats, and appliances where crumbs collect.
• Place ten pieces of bread (wrapped) in rooms before searching if your custom includes finding known pieces — so you do not miss anything (optional minhag).

After the search:
• Recite the blessing Al bi'ur chametz and the Kol chamira nullification (bitul) — many siddurim print the text.
• When Erev Pesach is Shabbat, this first bitul is Thursday night after bedikat; the final Kol Chamira is on Shabbat morning before the end of the 5th halachic hour — not at Friday's burning (Peninei Halakha; STAR-K).
• Gather found chametz in a bag to destroy the next morning at biur chametz.
• Do not eat a full meal after this point until the Seder (except as allowed for firstborns who attend a siyum, etc. — ask your rav).

If you are traveling or staying elsewhere, your host or rabbi can guide which rooms you are responsible to search.
        """.trim()), cal, profile, includePesachSchedule = includePesachScheduleOnBedikatItem(cal))
    }

    fun biurExplanation(cal: DayInfo, profile: UserProfile): String {
        val tz = cal.zmanim?.timezoneId ?: profile.timezoneId
        val zmanim = cal.zmanim
        val end4th = zmanim?.let { ZmanimHelpers.endOfHalachicHourMillis(it, 4) }
            ?: zmanim?.sofZmanTefillaMillis
        val end5th = zmanim?.let { ZmanimHelpers.endOfHalachicHourMillis(it, 5) }
        val zmanNote = buildString {
            if (end4th != null) {
                append("Finish eating chametz by end of the 4th halachic hour (approx. ")
                append(ZmanimFormatter.formatTime(end4th, tz) ?: "see zmanim app")
                append(" today). ")
            }
            if (end5th != null) {
                append("Biur (burning) deadline is end of the 5th halachic hour (approx. ")
                append(ZmanimFormatter.formatTime(end5th, tz) ?: "see zmanim app")
                append(" — earlier than solar chatzos midday). ")
            }
            if (isEmpty()) {
                append("Use a zmanim app for Erev Pesach: end of 4th halachic hour (stop eating chametz) and end of 5th halachic hour (burning deadline). ")
            }
        }

        val morningNote = when {
            isErevPesachOnShabbat(cal) ->
                "This year biur was due Friday morning (before Shabbat). On Shabbat you cannot burn chametz — it should already be destroyed or sold."
            isErevPesachFridayBeforeShabbatPesach(cal) ->
                "Morning of Erev Pesach (today, Friday) — complete biur before Shabbat:"
            else -> "Morning of Erev Pesach:"
        }

        return appendShabbatSchedule(BeginnerHalachaGlossary.withKeyTerms(BeginnerHalachaGlossary.pesachPrep(), """
Biur chametz (בִּעוּר חָמֵץ) — destroying chametz — completes the mitzvah of removing leaven before Pesach.

$morningNote
• Burn or destroy all chametz found last night and any remaining chametz you are not selling.
• Many burn chametz in a safe outdoor fire; flushing crumbs in the toilet or similar is acceptable for small amounts per many poskim — ask your rav.
• $zmanNote
• After destruction, recite Kol chamira again (bitul) — nullifying any chametz still in your possession unknowingly.

Mechirat chametz:
• Any chametz included in the rabbi's sale should already be sealed and not eaten — only unsold chametz is burned.

After biur:
• Eat only kosher-for-Passover food until Pesach ends.
• Firstborns: Taanit Bechorot or siyum earlier in the day; Seder ${
            when {
                isErevPesachOnShabbat(cal) -> "after Shabbat (see schedule above)"
                isErevPesachFridayBeforeShabbatPesach(cal) -> "tonight (first Seder); second Seder tomorrow night in the Diaspora"
                else -> "tonight"
            }
        }.
        """.trim()), cal, profile)
    }

    fun mechiratLinks(profile: UserProfile) = pesachLinks(profile, "mechirat")
    fun taanitBechorLinks(profile: UserProfile) = pesachLinks(profile, "taanit")
    fun sederPrepLinks(profile: UserProfile) = pesachLinks(profile, "seder")
    fun bedikatLinks(profile: UserProfile) = pesachLinks(profile, "bedikat")
    fun biurLinks(profile: UserProfile) = pesachLinks(profile, "biur")

    private fun pesachLinks(profile: UserProfile, topic: String): List<ChecklistLink> = buildList {
        when (topic) {
            "bedikat" -> {
                if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
                    add(
                        ChecklistLink(
                            "Chabad — Bedikat chametz",
                            "https://www.chabad.org/holidays/passover/pesach_cdo/aid/1721/jewish/Bedikat-Chametz.htm",
                            "chabad"
                        )
                    )
                }
                add(ChecklistLink("Peninei Halacha — Bedikat chametz", "https://ph.yhb.org.il/en/04-03-01/", "default"))
            }
            "mechirat" -> {
                if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
                    add(
                        ChecklistLink(
                            "Chabad — Sale of chametz",
                            "https://www.chabad.org/holidays/passover/pesach_cdo/aid/1720/jewish/Sale-of-Chametz.htm",
                            "chabad"
                        )
                    )
                }
                add(ChecklistLink("Peninei Halacha — Mechirat chametz", "https://ph.yhb.org.il/en/04-03-02/", "default"))
            }
            "biur" -> {
                if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
                    add(
                        ChecklistLink(
                            "Chabad — Destroying chametz",
                            "https://www.chabad.org/holidays/passover/pesach_cdo/aid/1723/jewish/Destroying-Chametz.htm",
                            "chabad"
                        )
                    )
                }
                add(ChecklistLink("Peninei Halacha — Biur chametz", "https://ph.yhb.org.il/en/04-03-03/", "default"))
            }
            "taanit" -> {
                if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
                    add(
                        ChecklistLink(
                            "Chabad — Fast of the Firstborn",
                            "https://www.chabad.org/holidays/passover/pesach_cdo/aid/1722/jewish/Fast-of-the-Firstborn.htm",
                            "chabad"
                        )
                    )
                }
                add(ChecklistLink("Peninei Halacha — Taanit Bechorot", "https://ph.yhb.org.il/en/04-03-04/", "default"))
            }
            else -> {
                if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
                    add(
                        ChecklistLink(
                            "Chabad — Erev Pesach",
                            "https://www.chabad.org/holidays/passover/pesach_cdo/aid/1719/jewish/Erev-Pesach.htm",
                            "chabad"
                        )
                    )
                }
            }
        }
        add(ChecklistLink("Aish — Passover", "https://aish.com/holidays/pesach/", "default"))
        add(ChecklistLink("Peninei Halacha — Pesach", "https://ph.yhb.org.il/en/04-03-01/", "default"))
        add(ChecklistLink("Ohr Somayach — Pesach", "https://ohr.edu/holidays/pesach/", "default"))
    }
}
