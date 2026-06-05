package com.beardytop.beatzaddik.domain

/**
 * Full explainers for Erev Pesach checklist items (14 Nisan).
 */
object ErevPesachPrepText {

    fun mechiratExplanation(profile: UserProfile): String = """
Mechirat chametz (מְכִירַת חָמֵץ) — selling your chametz to a non-Jew through your rabbi before Pesach — lets you keep chametz products locked away without owning them during Pesach (Shulchan Arukh O.C. 448).

Why:
• On Pesach, owning chametz is forbidden (bal yera'eh / bal yimatzei). Selling transfers ownership so sealed chametz in your home does not belong to you during the festival.

How to do it:
• Sign or authorize a sale with your rabbi or community (online forms are common). Do this before the final time to own chametz on Erev Pesach morning.
• Mark cabinets or rooms included in the sale; keep sold chametz separate from what you will burn or discard.
• Do not eat or use sold chametz after the sale takes effect — it belongs to the buyer until buy-back after Pesach.
• Store the contract or confirmation; many communities sell through a central rabbi (e.g. local kashrut council).

After Pesach: chametz is repurchased per the terms of the sale — follow your rabbi's instructions on when you may use it again.
    """.trim()

    fun taanitBechorExplanation(): String = """
Taanit Bechorot (תַּעֲנִית בְּכוֹרוֹת) — Fast of the Firstborn — is observed on Erev Pesach day by firstborn males (and some communities include firstborn females — ask your rav).

Why:
• Commemorates the plague of the firstborn in Egypt, when Jewish firstborn were spared.

The fast:
• From dawn until nightfall on Erev Pesach (or until you attend a seudat mitzvah that exempts you).
• Many firstborns avoid the fast by attending a siyum (completion of a Talmud tractate or similar Torah work) followed by a festive meal — the siyum itself is the exemption per widespread custom.
• If you fast: no eating or drinking from alot hashachar until after the fast ends (many end at a siyum, at mincha, or at night — follow your rabbi).

Plan ahead: locate a community siyum in advance if that is your minhag, or confirm fasting rules with your rav.
    """.trim()

    fun sederPrepExplanation(profile: UserProfile): String {
        val sederNights = if (profile.isInIsrael) {
            "In Israel: one seder tonight (15 Nisan)."
        } else {
            "In the Diaspora: first seder tonight; second seder tomorrow night."
        }
        return """
Tonight begins Pesach — prepare so the Seder focuses on mitzvot, not logistics.

$sederNights

Set up before Yom Tov:
• Matzah — shmurah matzah for motzi/matza mitzvah (three matzot on the plate per custom)
• Maror — romaine, horseradish, or bitter herbs per your minhag
• Four cups of wine (or grape juice) per participant — enough for each cup
• Haggadah for each person (or shared)
• Seder plate: zeroa (shankbone), beitzah (egg), karpas, charoset, maror, chazeret
• Reclining pillows; festive table; candles for Yom Tov

Kitchen:
• Only kosher-for-Passover food and utensils from this point
• Warm food on a blech or pre-set timer for Yom Tov meals

Mitzvot of the night: Kiddush, maggid (telling the story), four cups, matzah, maror, korech, afikoman, Hallel, Nirtzah — ${if (profile.isInIsrael) "one" else "two"} night(s) of seder.
        """.trim()
    }

    fun bedikatExplanation(cal: DayInfo, profile: UserProfile): String {
        val tz = cal.zmanim?.timezoneId ?: profile.timezoneId
        val tzeit = cal.zmanim?.tzeitMillis ?: cal.zmanim?.sunsetMillis
        val whenLine = ZmanimFormatter.formatTime(tzeit, tz)?.let { time ->
            "Tonight after nightfall — ideally after $time (tzeit). Begin once it is fully dark."
        } ?: "Tonight after nightfall (tzeit). Enable location in the app for your local time."

        return """
Bedikat chametz (בְּדִיקַת חָמֵץ) — the formal search for chametz — is a rabbinic mitzvah on the night of 14 Nisan (Erev Pesach).

$whenLine

How to search:
• Use a candle (or flashlight per many poskim), a wooden spoon, and a feather (or paper bag) to gather crumbs.
• Search every room where chametz may have been brought during the year — especially kitchen, dining areas, living room, car, office, children's bags, and coat pockets.
• Check under furniture, cushions, car seats, and appliances where crumbs collect.
• Place ten pieces of bread (wrapped) in rooms before searching if your custom includes finding known pieces — so you do not miss anything (optional minhag).

After the search:
• Recite the blessing Al bi'ur chametz and the Kol chamira nullification (bitul) — many siddurim print the text.
• Gather found chametz in a bag to destroy the next morning at biur chametz.
• Do not eat a full meal after this point until the Seder (except as allowed for firstborns who attend a siyum, etc. — ask your rav).

If you are traveling or staying elsewhere, your host or rabbi can guide which rooms you are responsible to search.
        """.trim()
    }

    fun biurExplanation(cal: DayInfo, profile: UserProfile): String {
        val tz = cal.zmanim?.timezoneId ?: profile.timezoneId
        val chatzos = cal.zmanim?.chatzosMillis
        val sofZmanTefilla = cal.zmanim?.sofZmanTefillaMillis
        val zmanNote = buildString {
            if (sofZmanTefilla != null) {
                append("Many communities: finish eating chametz by end of the 4th halachic hour (approx. ")
                append(ZmanimFormatter.formatTime(sofZmanTefilla, tz) ?: "sof zman tefillah")
                append(" today). ")
            }
            if (chatzos != null) {
                append("Biur (burning) deadline is end of the 5th hour (midday/chatzos region — approx. ")
                append(ZmanimFormatter.formatTime(chatzos, tz) ?: "chatzos")
                append("). ")
            }
            if (isEmpty()) {
                append("Use a zmanim app for Erev Pesach: end of 4th hour (stop eating chametz) and end of 5th hour (burning deadline). ")
            }
        }

        return """
Biur chametz (בִּעוּר חָמֵץ) — destroying chametz — completes the mitzvah of removing leaven before Pesach.

Morning of Erev Pesach:
• Burn or destroy all chametz found last night and any remaining chametz you are not selling.
• Many burn chametz in a safe outdoor fire; flushing crumbs in the toilet or similar is acceptable for small amounts per many poskim — ask your rav.
• $zmanNote
• After destruction, recite Kol chamira again (bitul) — nullifying any chametz still in your possession unknowingly.

Mechirat chametz:
• Any chametz included in the rabbi's sale should already be sealed and not eaten — only unsold chametz is burned.

After biur:
• Eat only kosher-for-Passover food until Pesach ends.
• Firstborns: Taanit Bechorot or siyum earlier in the day; Seder tonight.
        """.trim()
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
        add(ChecklistLink("Peninei Halacha — Pesach", "https://ph.yhb.org.il/en/category/moadim/04-pesach/", "default"))
        add(ChecklistLink("Ohr Somayach — Pesach", "https://ohr.edu/yhiy/pesach/", "default"))
    }
}
