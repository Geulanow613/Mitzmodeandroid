package com.beardytop.beatzaddik.domain

/**
 * Full how-to explainers for seasonal checklist items (Sukkot, Shemini Atzeret, Chol HaMoed, etc.).
 */
object SeasonalMitzvahText {

    // ── Simchas Yom Tov (erev Pesach / Shavuot / Sukkot prep only) ───────────

    fun simchasYomTovPrepBlock(): String = BeginnerHalachaGlossary.withKeyTerms(
        BeginnerHalachaGlossary.simchasYomTovBasics(),
        """
Simchas Yom Tov (שִׂמְחַת יוֹם טוֹב) — the mitzvah to rejoice on the festival:
The Torah commands "V'samachta b'chagecha" — "And you shall rejoice in your festival" (Devarim 16:14). Joy on Yom Tov is a mitzvah for everyone. Halacha recognizes that happiness is personal — the head of household helps each family member rejoice in the way that naturally brings them joy (Pesachim 109a; Shulchan Arukh O.C. 529:2).

Prepare before chag begins:
• Wife — clothing or jewelry l'fi mamono (within your financial ability): the Shulchan Arukh obligates a husband to buy his wife new clothes or jewelry for Yom Tov. You are not expected to go into debt — an inexpensive but pleasant garment, modest jewelry, or nice shoes fulfills the mitzvah. The goal is her actual joy; if she prefers something else that genuinely makes her happy (a book, houseware, experience), many modern poskim agree that fulfills the spirit of the law.
• Children — treats that bring joy: the Talmud mentions roasted grains and nuts; today candies, chocolates, and age-appropriate toys. The purpose is to associate Yom Tov with excitement and a break from everyday routine — not merely to give sugar.
• Men — meat and wine: in Temple times this meant holiday sacrifices; today festive Yom Tov meals with wine and meat (beef is classic; poultry is often acceptable) fulfill personal simchas Yom Tov.

Rambam's crucial condition (Hilchos Yom Tov 6:18):
When you feed your family, buy your wife clothes, and give your children treats, you must also provide for the poor, the widow, and the orphan. If a household feasts only behind closed doors without helping the needy, the Rambam writes this is not "the joy of a mitzvah" but merely "the joy of one's stomach."
        """.trim(),
    )

    fun simchasYomTovPrepLinks(): List<ChecklistLink> = listOf(
        ChecklistLink(
            "Shulchan Arukh — Simchas Yom Tov",
            "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.529",
            "default"
        ),
        ChecklistLink(
            "Talmud Pesachim 109a",
            "https://www.sefaria.org/Pesachim.109a",
            "default"
        ),
        ChecklistLink(
            "Rambam — Laws of Yom Tov 6:18",
            "https://www.sefaria.org/Mishneh_Torah,_Rest_on_a_Holiday.6.18",
            "default"
        ),
    )

    // ── Week-before festival prep (Pesach, Shavuot, Sukkot) ──────────────────

    fun festivalWeekPrepTitle(prep: FestivalWeekPrep): String = when (prep) {
        FestivalWeekPrep.PESACH -> "Prepare for the festival — Pesach"
        FestivalWeekPrep.SHAVUOT -> "Prepare for the festival — Shavuot"
        FestivalWeekPrep.SUKKOT -> "Prepare for the festival — Sukkot"
    }

    fun festivalWeekPrepExplanation(
        cal: DayInfo,
        profile: UserProfile,
        prep: FestivalWeekPrep,
    ): String = when (prep) {
        FestivalWeekPrep.PESACH -> pesachWeekPrepExplanation(cal, profile)
        FestivalWeekPrep.SHAVUOT -> shavuotWeekPrepExplanation(profile)
        FestivalWeekPrep.SUKKOT -> sukkotWeekPrepExplanation(profile)
    }

    fun festivalWeekPrepLinks(
        prep: FestivalWeekPrep,
        profile: UserProfile,
    ): List<ChecklistLink> = when (prep) {
        FestivalWeekPrep.PESACH -> pesachWeekLinks(profile)
        FestivalWeekPrep.SHAVUOT -> shavuotWeekLinks(profile)
        FestivalWeekPrep.SUKKOT ->
            (sukkahLinks(profile) + arbaMinimLinks(profile)).distinctBy { it.url }
    }

    fun shavuotWeekPrepExplanation(profile: UserProfile): String = BeginnerHalachaGlossary.withKeyTerms(
        BeginnerHalachaGlossary.shavuotBasics(),
        ExplainerTemplateFill.fill(
            ExplainerTemplateSupport.shavuotWeekPrepTemplate(),
            ExplainerTemplateSupport.shavuotWeekPrepArgs(profile),
        ),
    )

    fun sukkotWeekPrepExplanation(profile: UserProfile): String = BeginnerHalachaGlossary.withKeyTerms(
        BeginnerHalachaGlossary.sukkotBasics(),
        ExplainerTemplateFill.fill(
            ExplainerTemplateSupport.sukkotWeekPrepTemplate(),
            ExplainerTemplateSupport.sukkotWeekPrepArgs(profile),
        ),
    )

    fun shavuotWeekLinks(profile: UserProfile) = buildList {
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(ChecklistLink("Chabad — Shavuot", "https://www.chabad.org/holidays/shavuot/default_cdo/jewish/Shavuot.htm", "chabad"))
        }
        add(ChecklistLink("Peninei Halacha — Shavuot", "https://ph.yhb.org.il/en/category/12/12-13/", "default"))
        add(ChecklistLink("Aish — Shavuot", "https://aish.com/holidays/shavuot/", "default"))
        add(ChecklistLink("Ohr Somayach — Shavuot", "https://ohr.edu/holidays/shavuot/", "default"))
    }

    // ── Sukkot ───────────────────────────────────────────────────────────────

    fun sukkahBuildExplanation(): String = BeginnerHalachaGlossary.withKeyTerms(
        BeginnerHalachaGlossary.sukkotBasics(),
        """
Building a sukkah (סֻכָּה) is a mitzvah — many begin right after Yom Kippur to show we move from teshuvah to simcha.

Basic requirements (Shulchan Arukh O.C. 633–635):
• Walls: At least two full walls plus part of a third (or three full walls). Walls can be wood, fabric on a frame, or existing walls — they must be stationary and not flap in a normal breeze (Shulchan Arukh O.C. 630:10).
• Fabric / canvas sukkahs: Tie fabric walls down tightly so they do not move or flap in a normal wind — loose canvas that billows inside the frame does not become valid merely by wrapping ropes around it. Lavud (gaps under 3 tefachim) can create a halachic wall from ropes alone in some cases, but cannot fix billowing fabric. Pop-up kits that flap are a common beginner pitfall — ask your rav.
• Size: Large enough to fit a table and for an adult to sit with head and most of body inside (roughly 7×7 handbreadths minimum; build larger for comfort).
• Schach (covering): Must be plant material that grew from the ground and is detached (tree branches, bamboo mats certified for schach, etc.) — not metal, plastic, or a solid roof. Schach rules: Material must be detached plant life that has NOT been manufactured into a functional tool, vessel, or finished item (such as old furniture parts or wooden slats from crates), as finished objects are susceptible to ritual impurity (kabalat tumah) and are completely invalid for schach.
• Shade: More shade than sun on the floor under the schach. You may see some sky through gaps — that is fine.
• Height: Walls at least ~3 ft (10 tefachim); schach can be high, but very high sukkot still need a valid structure below.

Practical steps:
1. Choose a spot — backyard, porch, balcony (check building rules). Avoid placing schach directly under a tree or house roof overhang that blocks the mitzvah.
2. Build the frame and walls first; lay schach last so it stays dry and valid.
3. Arrange schach loosely with gaps — not a solid rainproof roof (use a removable slatted cover on top only if rain is expected, per halachic guidance).
4. Set up table, chairs, and decorations (fruit, lights — electric on Yom Tov/Chol HaMoed per your rav).
5. Before the first night of Sukkot, eat at least a kezayit of bread in the sukkah with the bracha leishev basukkah (first night is Torah-level for men; Ashkenazi women who eat there say leishev baSukkah; Sephardic women generally do not).

First night rain protocol (if it rains on the first night):
• Ashkenaz (Rema O.C. 639:5): Wait up to one hour. If rain does not stop, enter the sukkah, make Kiddush, eat a kezayit of bread without leishev basukkah, then finish the rest of the meal indoors.
• Sephardic (Shulchan Arukh): If rain is heavy enough to spoil the meal, you are exempt from eating the full meal in the sukkah — make Kiddush and eat the full meal indoors from the start, without leishev basukkah. Nevertheless, many Sephardic decisors (including Yalkut Yosef) rule that it remains proper and praiseworthy at the very end of the meal to go out to the sukkah and eat a single kezayit of bread without reciting leishev basukkah — honoring the first night's unique obligation in part.

First night: Kiddush and Yom Tov meal in the sukkah. Men: leishev basukkah (sit in the sukkah — a blessing said before eating bread in the sukkah). Throughout Sukkot: eat bread and sleep in sukkah when possible (rain and illness have exemptions — ask your rabbi).
    """.trim(),
    )

    fun arbaMinimExplanation(profile: UserProfile): String = BeginnerHalachaGlossary.withKeyTerms(
        BeginnerHalachaGlossary.sukkotBasics(),
        ExplainerTemplateFill.fill(
            ExplainerTemplateSupport.arbaMinimTemplate(female = false),
            ExplainerTemplateSupport.arbaMinimArgs(profile, female = false),
        ),
    )

    fun arbaMinimExplanationFemale(profile: UserProfile): String =
        BeginnerHalachaGlossary.withKeyTerms(
            BeginnerHalachaGlossary.sukkotBasics(),
            ExplainerTemplateFill.fill(
                ExplainerTemplateSupport.arbaMinimTemplate(female = true),
                ExplainerTemplateSupport.arbaMinimArgs(profile, female = true),
            ),
        )

    fun hoshanaRabbahAravotExplanation(): String = BeginnerHalachaGlossary.withKeyTerms(
        BeginnerHalachaGlossary.sukkotBasics(),
        """
Hoshana Rabbah (הושענא רבה) is the 21st of Tishrei — the seventh and final day of Sukkot. It is Chol HaMoed, but the day has its own identity and heightened teshuvah: many treat it as the final sealing of the divine judgment from Yom Kippur.

Morning — lulav and Hoshanot:
• Take the arba minim as on other Chol HaMoed days (not on Shabbat).
• In many kehillot there are extra hakafot (circuits) around the bimah with Hoshanot prayers — follow your siddur and shul.

Beating the aravot (Minhag Nevi'im):
• Separate from your daily lulav set, prepare a bundle of five fresh willow branches (aravot).
• At the conclusion of synagogue services (after Hoshanot / before or after Musaf per minhag), many communities beat these branches against the ground (Chagizat Aravah).
• The Talmud (Sukkah 44a) treats this rite as a Yesod Nevi'im or Minhag Nevi'im — an institution of the Prophets, not merely a local custom. The number of times and exact form vary by kehilla — often about five strikes in Ashkenaz and Chabad custom. Follow your siddur.
• The beaten branches are not used for the lulav mitzvah; many discard them respectfully after the rite.

Nusach notes:
• Ashkenaz: extended Hoshanot with seven hakafot; aravot beaten on the ground after circuits.
• Sephardi / Edot HaMizrach: Hoshanot customs vary by kehilla — follow your siddur.
• Chabad: hakafot with Hoshanot; aravot beaten five times on the ground (not the lulav itself) — see your Chabad machzor.

Confirm times with your shul — especially if Hoshana Rabbah is Erev Shabbat / Erev Shemini Atzeret.
        """.trim(),
    )

    fun hoshanaRabbahLinks() = listOf(
        ChecklistLink(
            "Chabad — Hoshana Rabbah",
            "https://www.chabad.org/library/article_cdo/aid/757453/jewish/Hoshana-Rabbah.htm",
            "chabad",
        ),
        ChecklistLink(
            "Peninei Halakha — Sukkot",
            "https://ph.yhb.org.il/en/category/13/13-05/",
            "default",
        ),
        ChecklistLink(
            "Ohr Somayach — Sukkot",
            "https://ohr.edu/holidays/succos/",
            "default",
        ),
    )

    fun sheminiAtzeretExplanation(profile: UserProfile): String = BeginnerHalachaGlossary.withKeyTerms(
        BeginnerHalachaGlossary.block(
            BeginnerHalachaGlossary.YOM_TOV,
            BeginnerHalachaGlossary.MELACHA,
            BeginnerHalachaGlossary.MUSAF,
            BeginnerHalachaGlossary.HALLEL,
            BeginnerHalachaGlossary.DAVEN,
            BeginnerHalachaGlossary.SHUL,
            BeginnerHalachaGlossary.RAV,
        ),
        if (profile.isInIsrael) {
            """
Shemini Atzeret / Simchat Torah (22 Tishrei in Israel) — one day combining both.

Yom Tov:
• Full Yom Tov — no melacha; festive meals with Kiddush and Shehecheyanu (if not said already).
• Sukkot has ended — in Israel, do not eat or sleep in the sukkah today; festive meals are indoors (not in the sukkah).

Davening highlights:
• Liturgical shift: During Musaf today, the entire Jewish world officially transitions to the winter prayer cycle, universally inserting "Mashiv HaRuach U'Morid HaGeshem" into the second blessing of the Amidah. Tefillat Geshem (the formal prayer for rain) is recited in Musaf.
• Yizkor — memorial prayer in many Ashkenaz communities.
• Hakafot — dancing with Torah scrolls; finish the annual Torah reading and begin Bereshit again.
• Full Hallel and Musaf; Yom Tov Amidah.

Simchat Torah joy:
• Everyone receives an aliyah in many shuls; Kol HaNearim (children's aliyah) with flags — an adult (or a boy over bar mitzvah) stands with the group and recites the Torah blessing aloud so the aliya is halachically valid.
• Singing, dancing — honor the day with Torah celebration.

Evening: Candle lighting and Yom Tov; morning services are long — plan accordingly.
            """.trim()
        } else {
            """
Shemini Atzeret (22 Tishrei in the Diaspora) — eighth day of Sukkot.

Yom Tov:
• Full Yom Tov — no melacha; Kiddush and festive meals.
• Sukkah in the Diaspora: Due to safek dyoma (halachic doubt which day is which), Diaspora Ashkenazim are required to eat all major meals in the sukkah on Shemini Atzeret, though leishev basukkah is omitted entirely. Sephardic and Chabad customs vary — confirm with your rav.

Davening:
• Liturgical shift: During Musaf today, the entire Jewish world officially transitions to the winter prayer cycle, universally inserting "Mashiv HaRuach U'Morid HaGeshem" into the second blessing of the Amidah. Tefillat Geshem is recited in Musaf.
• Yizkor — Ashkenaz communities recite memorial prayers.
• Full Hallel and Musaf; Yom Tov Amidah.

Simchat Torah is tomorrow (23 Tishrei) in the Diaspora — see that day's checklist item for hakafot and Torah joy.

Tonight: light Yom Tov candles; no lulav on Shemini Atzeret.
            """.trim()
        },
    )

    fun simchatTorahExplanation(profile: UserProfile): String = BeginnerHalachaGlossary.withKeyTerms(
        BeginnerHalachaGlossary.block(
            BeginnerHalachaGlossary.YOM_TOV,
            BeginnerHalachaGlossary.MELACHA,
            BeginnerHalachaGlossary.MUSAF,
            BeginnerHalachaGlossary.HALLEL,
            BeginnerHalachaGlossary.RAV,
        ),
        """
Simchat Torah (23 Tishrei in the Diaspora) — rejoicing with the Torah.

Yom Tov:
• Full Yom Tov — no melacha; Kiddush, festive meals, joy.
• Sukkah: Most Ashkenazim do not eat in the sukkah on Simchat Torah (it is no longer Sukkot) — Sefardic practice varies; follow your rav.
• Davening: Full Hallel and Musaf (like other Yom Tov days).

The mitzvah of the day — simcha and Torah:
• Hakafot — circling the bimah with Torah scrolls, singing and dancing (often many circuits).
• Minhag in some Ashkenaz Diaspora shuls (not universal): because drinking often accompanies daytime hakafot, Birkat Kohanim is sometimes moved from Musaf to early Shacharit so Kohanim are sober. This is generally irrelevant in Israel (where Birkat Kohanim is said daily at Shacharit) and in many Sephardic communities with different timing — follow your synagogue's practice.
• Complete the last parsha of Devarim and begin Bereshit — the Torah never ends.
• Aliyot — Kol HaNearim (all children together; an adult or bar mitzvah boy recites the blessing with them), Chatan Torah, Chatan Bereshit, and honors for members.
• Many shuls extend Hakafot to the evening or multiple sessions.

Practical tips:
• Dress festively; arrive early for a good spot.
• If you have a Torah honor, prepare your brachot.
• Celebrate responsibly — the day is about love of Torah, not excess.
    """.trim(),
    )

    // ── Chol HaMoed ────────────────────────────────────────────────────────────

    fun cholHamoedHonorExplanation(cal: DayInfo, profile: UserProfile): String =
        BeginnerHalachaGlossary.withKeyTerms(
            BeginnerHalachaGlossary.cholHamoedBasics(),
            ExplainerTemplateFill.fill(
                ExplainerTemplateSupport.cholHamoedHonorTemplate(),
                ExplainerTemplateSupport.cholHamoedHonorArgs(cal, profile),
            ),
        )

    fun cholHamoedClothesExplanation(): String = BeginnerHalachaGlossary.withKeyTerms(
        BeginnerHalachaGlossary.cholHamoedBasics(),
        """
Wearing nicer clothing on Chol HaMoed honors the festival (kavod ha'moed).

Practice:
• Wear clothes you would not wear for dirty chores — clean, pressed, or festive attire.
• Men: many wear a hat and jacket for shul even if weekday dress is casual.

This applies each day of Chol HaMoed — it is a simple daily way to mark the moed apart from ordinary weekdays.
    """.trim(),
    )

    fun cholHamoedWineReviitExplanation(cal: DayInfo): String =
        BeginnerHalachaGlossary.withKeyTerms(
            BeginnerHalachaGlossary.cholHamoedBasics(),
            ExplainerTemplateFill.fill(
                ExplainerTemplateSupport.cholHamoedWineTemplate(),
                ExplainerTemplateSupport.cholHamoedWineArgs(cal),
            ),
        )

    fun cholHamoedMatzahExplanation(): String = BeginnerHalachaGlossary.withKeyTerms(
        BeginnerHalachaGlossary.block(
            BeginnerHalachaGlossary.CHAMETZ,
            BeginnerHalachaGlossary.KEZAYIT,
            BeginnerHalachaGlossary.SEDER,
            BeginnerHalachaGlossary.CHOL_HAMOED,
            BeginnerHalachaGlossary.RAV,
        ),
        """
Eating matzah on Pesach after the Seder night(s) is a mitzvah many observe, though not with the same strict obligation as the first Seder night(s).

Levels:
• First Seder night: Torah obligation (k'zayit) for men and women.
• Rest of Pesach including Chol HaMoed: Rabbinic command (if you have a second seder) and optional mitzvah to eat matzah every day of Pesach. Most will eat matzah at all Yom Tov and Shabbat meals.

How:
• Use shmurah matzah for the seder if available.
• A kezayit per meal is sufficient.
• No chametz or kitniyot (per your custom) the entire Pesach.
    """.trim(),
    )

    fun cholHamoedSukkahExplanation(profile: UserProfile): String =
        BeginnerHalachaGlossary.withKeyTerms(
            BeginnerHalachaGlossary.block(
                BeginnerHalachaGlossary.CHOL_HAMOED,
                BeginnerHalachaGlossary.SCHACH,
                BeginnerHalachaGlossary.LULAV_SET,
                BeginnerHalachaGlossary.RAV,
                BeginnerHalachaGlossary.MINHAG,
            ),
            ExplainerTemplateSupport.cholHamoedSukkahTemplate(female = false),
        )

    fun cholHamoedSukkahExplanationFemale(profile: UserProfile): String =
        BeginnerHalachaGlossary.withKeyTerms(
            BeginnerHalachaGlossary.block(
                BeginnerHalachaGlossary.CHOL_HAMOED,
                BeginnerHalachaGlossary.SCHACH,
                BeginnerHalachaGlossary.RAV,
                BeginnerHalachaGlossary.MINHAG,
            ),
            ExplainerTemplateFill.fill(
                ExplainerTemplateSupport.cholHamoedSukkahTemplate(female = true),
                ExplainerTemplateSupport.cholHamoedSukkahArgs(profile, female = true),
            ),
        )

    private const val CHABAD_ROSH_CHODESH_URL =
        "https://www.chabad.org/library/article_cdo/aid/4909907/jewish/12-Rosh-Chodesh-Facts.htm"

    private const val YAALEH_VYAVO_HALACHA_SOURCE =
        "Shulchan Arukh O.C. 422:1; Peninei Halakha 05-01-10"

    /** Catalog keys for forgot blocks — must match [tools/_sync_explainer_catalog_keys.py]. */
    const val YAALEH_FORGOT_AMIDAH_SHACHARIT = """If you forgot:
• Still in Retzei (before concluding the blessing) — insert Yaaleh V'yavo in its place and continue (Shulchan Arukh O.C. 422:1; Peninei Halakha 05-01-10).
• After concluding Retzei — return to the beginning of Retzei, insert Yaaleh V'yavo, and complete the remaining blessings (Shulchan Arukh O.C. 422:1; Peninei Halakha 05-01-10).
• Finished the entire Amidah (after the final Yihiyu L'ratzon) — repeat only the Shacharit Amidah (Shemoneh Esrei), never the full service, even if you already davened Musaf, Maariv, or anything else afterward (Shulchan Arukh O.C. 422:1; Peninei Halakha 05-01-10)."""

    const val YAALEH_FORGOT_AMIDAH_MINCHA = """If you forgot:
• Still in Retzei (before concluding the blessing) — insert Yaaleh V'yavo in its place and continue (Shulchan Arukh O.C. 422:1; Peninei Halakha 05-01-10).
• After concluding Retzei — return to the beginning of Retzei, insert Yaaleh V'yavo, and complete the remaining blessings (Shulchan Arukh O.C. 422:1; Peninei Halakha 05-01-10).
• Finished the entire Amidah (after the final Yihiyu L'ratzon) — repeat only the Mincha Amidah (Shemoneh Esrei), never the full service, even if you already davened Musaf, Maariv, or anything else afterward (Shulchan Arukh O.C. 422:1; Peninei Halakha 05-01-10)."""

    const val YAALEH_FORGOT_MAARIV = """If you forgot at Maariv on Rosh Chodesh:
• Still in Retzei before God's name at the conclusion — insert Yaaleh V'yavo there and continue (Shulchan Arukh O.C. 422:1; Peninei Halakha 05-01-10).
• Once you finished Retzei, or after the entire Amidah — do not go back and do not repeat. Beit Din sanctified the new month by day, not at night (Berachot 30b; Shulchan Arukh O.C. 422:1; Peninei Halakha 05-01-10). Continue davening."""

    private val YAALEH_SHACHARIT_TEMPLATE = """
Add Yaaleh V'yavo in the Shacharit Amidah on Rosh Chodesh — in the blessing Retzei (Avodah).

${'$'}forgotBlock

Also add Yaaleh V'yavo in bentching if you eat bread today.
    """.trimIndent()

    private val YAALEH_SHACHARIT_FEMALE_TEMPLATE = """
If you recite the Shacharit Amidah on Rosh Chodesh, add Yaaleh V'yavo in the blessing Retzei (Avodah).

${'$'}forgotBlock

If you say Birkat Hamazon when you eat bread today, add Yaaleh V'yavo there too.
    """.trimIndent()

    private val YAALEH_MINCHA_TEMPLATE = """
Add Yaaleh V'yavo in the Mincha Amidah on Rosh Chodesh — in the blessing Retzei (Avodah).

${'$'}forgotBlock

Also add Yaaleh V'yavo in bentching if you eat bread today.
    """.trimIndent()

    private val YAALEH_MINCHA_FEMALE_TEMPLATE = """
If you recite the Mincha Amidah on Rosh Chodesh, add Yaaleh V'yavo in the blessing Retzei (Avodah).

${'$'}forgotBlock

If you say Birkat Hamazon when you eat bread today, add Yaaleh V'yavo there too.
    """.trimIndent()

    private val YAALEH_MAARIV_TEMPLATE = """
Add Yaaleh V'yavo in the Maariv Amidah on Rosh Chodesh — in the blessing Retzei (Avodah).

$YAALEH_FORGOT_MAARIV

Also add Yaaleh V'yavo in bentching if you eat bread tonight.
    """.trimIndent()

    private val YAALEH_MAARIV_FEMALE_TEMPLATE = """
If you recite the Maariv Amidah on Rosh Chodesh, add Yaaleh V'yavo in the blessing Retzei (Avodah).

$YAALEH_FORGOT_MAARIV

If you say Birkat Hamazon when you eat bread tonight, add Yaaleh V'yavo there too.
    """.trimIndent()

    fun yaalehVyavoShacharitTemplate(female: Boolean = false): String =
        if (female) YAALEH_SHACHARIT_FEMALE_TEMPLATE else YAALEH_SHACHARIT_TEMPLATE

    fun yaalehVyavoShacharitArgs(): Map<String, String> =
        mapOf("forgotBlock" to YAALEH_FORGOT_AMIDAH_SHACHARIT)

    fun yaalehVyavoMinchaTemplate(female: Boolean = false): String =
        if (female) YAALEH_MINCHA_FEMALE_TEMPLATE else YAALEH_MINCHA_TEMPLATE

    fun yaalehVyavoMinchaArgs(): Map<String, String> =
        mapOf("forgotBlock" to YAALEH_FORGOT_AMIDAH_MINCHA)

    fun yaalehVyavoMaarivTemplate(female: Boolean = false): String =
        if (female) YAALEH_MAARIV_FEMALE_TEMPLATE else YAALEH_MAARIV_TEMPLATE

    fun yaalehVyavoMaarivArgs(): Map<String, String> = emptyMap()

    fun yaalehVyavoShacharitExplanation(): String =
        ExplainerTemplateFill.fill(yaalehVyavoShacharitTemplate(), yaalehVyavoShacharitArgs())

    fun yaalehVyavoShacharitExplanationFemale(): String =
        ExplainerTemplateFill.fill(yaalehVyavoShacharitTemplate(female = true), yaalehVyavoShacharitArgs())

    fun yaalehVyavoMinchaExplanation(): String =
        ExplainerTemplateFill.fill(yaalehVyavoMinchaTemplate(), yaalehVyavoMinchaArgs())

    fun yaalehVyavoMinchaExplanationFemale(): String =
        ExplainerTemplateFill.fill(yaalehVyavoMinchaTemplate(female = true), yaalehVyavoMinchaArgs())

    fun yaalehVyavoMaarivExplanation(): String =
        ExplainerTemplateFill.fill(yaalehVyavoMaarivTemplate(), yaalehVyavoMaarivArgs())

    fun yaalehVyavoMaarivExplanationFemale(): String =
        ExplainerTemplateFill.fill(yaalehVyavoMaarivTemplate(female = true), yaalehVyavoMaarivArgs())

    fun roshChodeshHalfHallelExplanation(): String = """
Recite Half Hallel after the Shacharit Amidah on Rosh Chodesh (a cherished custom; not a Torah obligation — Peninei Halakha 05-01-12).

Before Musaf:
Remove tefillin before Musaf — do not wear tefillin during the Musaf Amidah.

Tachanun is omitted on Rosh Chodesh.
    """.trim()

    fun roshChodeshHalfHallelExplanationFemale(): String = """
Recite Half Hallel after Shacharit on Rosh Chodesh if you say Hallel — a cherished custom, not obligatory for women (Peninei Halakha 05-01-12). Many Ashkenazi women omit Hallel; many Sephardi women recite it. Follow your community.

Tachanun is omitted on Rosh Chodesh.
    """.trim()

    fun roshChodeshFullHallelChanukahExplanation(): String = """
Rosh Chodesh falls during Chanukah — recite Full Hallel at Shacharit (the Chanukah obligation), not the usual Half Hallel of Rosh Chodesh (Peninei Halakha 12-02-07).

Before Musaf:
Remove tefillin before Musaf — do not wear tefillin during the Musaf Amidah.

Tachanun is omitted.
    """.trim()

    fun roshChodeshFullHallelChanukahExplanationFemale(): String = """
Rosh Chodesh during Chanukah — if you recite Hallel, say Full Hallel at Shacharit (the Chanukah practice), not Half Hallel (Peninei Halakha 12-02-07). Optional for women — follow your minhag.

Tachanun is omitted.
    """.trim()

    fun roshChodeshHalfHallelAshkenazNote(): String = """
Ashkenaz — Half Hallel:
Recite the blessings before Half Hallel on Rosh Chodesh (Mishnah Berurah 422:8).
    """.trim()

    fun roshChodeshHalfHallelSephardNote(): String = """
Sephardi — Half Hallel:
Do not recite a blessing over Partial (Half) Hallel — say the psalms without a bracha (Shulchan Arukh O.C. 422:2; Peninei Halakha 05-01-12).
    """.trim()

    fun roshChodeshHalfHallelChabadNote(): String = """
Chabad — Half Hallel:
Follow Ashkenazic custom — recite the blessings before Half Hallel (Alter Rebbe's Siddur; ask your rav if unsure).
    """.trim()

    fun roshChodeshFullHallelAshkenazNote(): String = """
Ashkenaz — Full Hallel (Chanukah):
Recite the blessings before Full Hallel as on Chanukah (Mishnah Berurah 422:8).
    """.trim()

    fun roshChodeshFullHallelSephardNote(): String = """
Sephardi — Full Hallel (Chanukah):
Recite the blessings before Full Hallel as on Chanukah (Shulchan Arukh O.C. 422:2).
    """.trim()

    fun roshChodeshFullHallelChabadNote(): String = """
Chabad — Full Hallel (Chanukah):
Follow Ashkenazic custom — recite the blessings before Full Hallel (Alter Rebbe's Siddur).
    """.trim()

    fun roshChodeshObservancesExplanation(): String = """
Rosh Chodesh (ראש חודש) — the New Month — is a semi-holiday with extra prayer and customs.

Festive meal (mitzvah):
• It is a mitzvah to increase your meal on Rosh Chodesh — at minimum add an extra dish or special food in honor of the day (Shulchan Arukh O.C. 419:1).
• Have the meal during the day. Poskim write this commemorates the lavish feast the Sanhedrin held in the courtyard of Beit Ya'azek (בֵּית יַעְזֵק) in Jerusalem for witnesses who came to testify that they had seen the new moon — so they would be incentivized to make the trip in the future (Mishnah Rosh Hashanah 2:5; Orchos Chaim and Kol Bo, cited on O.C. 419).
• Money spent on Rosh Chodesh meals — like Shabbat and Yom Tov — is not deducted from the income allotted to you on Rosh Hashanah; if you spend more for these mitzvos, Heaven adds to your allotment (Pesikta de-Rav Kahana, cited in Tur O.C. 419 and Magen Avraham 419:1).

Davening today (listed in your Morning, Afternoon, and Evening Prayer sections):
• Yaaleh V'yavo in Shacharit, Mincha, and Maariv Amidah — and in bentching when you eat bread
• Half Hallel at Shacharit (Full Hallel if Rosh Chodesh falls during Chanukah; no Hallel on Rosh Chodesh Tishrei / Rosh Hashanah)
• Musaf after Shacharit — remove tefillin before Musaf
• Tachanun is omitted all day

Other customs:
• Fasting and eulogies are generally not done on Rosh Chodesh
• Widespread custom: married women refrain from certain melacha (needlework, laundry, etc.) as an extra mark of honor — ask your rav for details

When Rosh Chodesh spans two days (30th of the previous month and 1st), observances apply to both days.
    """.trim()

    fun roshChodeshObservancesExplanationFemale(): String = """
Rosh Chodesh (ראש חודש) — the New Month — is a semi-holiday with extra prayer and customs.

Festive meal (mitzvah):
• It is a mitzvah to increase your meal on Rosh Chodesh — at minimum add an extra dish or special food in honor of the day (Shulchan Arukh O.C. 419:1).
• Have the meal during the day. Poskim write this commemorates the lavish feast the Sanhedrin held in the courtyard of Beit Ya'azek (בֵּית יַעְזֵק) in Jerusalem for witnesses who came to testify that they had seen the new moon — so they would be incentivized to make the trip in the future (Mishnah Rosh Hashanah 2:5; Orchos Chaim and Kol Bo, cited on O.C. 419).
• Money spent on Rosh Chodesh meals — like Shabbat and Yom Tov — is not deducted from the income allotted to you on Rosh Hashanah; if you spend more for these mitzvos, Heaven adds to your allotment (Pesikta de-Rav Kahana, cited in Tur O.C. 419 and Magen Avraham 419:1).

Davening today (listed in your Morning, Afternoon, and Evening Prayer sections):
• If you recite Shacharit, Mincha, or Maariv Amidah — add Yaaleh V'yavo in Retzei. Shacharit/Mincha: correct per timing (insert in Retzei, return to the beginning of Retzei if already concluded, or repeat only that Amidah if finished). Maariv on Rosh Chodesh only: do not repeat if forgotten after Retzei (Berachot 30b; SA O.C. 422:1)
• If you say Birkat Hamazon when you eat bread — add Yaaleh V'yavo there too
• Ashkenazi custom — most authorities obligate Shacharit and Mincha Amidah on these days
• Sephardic custom — many women fulfill the daily obligation with one prayer; if you daven an extra Amidah and forget Yaaleh V'yavo, ask your rabbi about a voluntary (nedavah) stipulation
• Half Hallel at Shacharit if you say Hallel (optional — follow your minhag; Full Hallel if Rosh Chodesh falls during Chanukah)
• Tachanun is omitted all day

Other customs:
• Fasting and eulogies are generally not done on Rosh Chodesh
• Widespread custom: married women refrain from certain melacha (needlework, laundry, etc.) as an extra mark of honor — ask your rav for details

When Rosh Chodesh spans two days (30th of the previous month and 1st), observances apply to both days.
    """.trim()

    /** @deprecated Replaced by [roshChodeshObservancesExplanation]. */
    fun roshChodeshMonthlyExplanation(): String = roshChodeshObservancesExplanation()

    /** @deprecated Replaced by [roshChodeshHalfHallelAshkenazNote]. */
    fun roshChodeshMonthlyAshkenazNote(): String = roshChodeshHalfHallelAshkenazNote()

    /** @deprecated Replaced by [roshChodeshHalfHallelSephardNote]. */
    fun roshChodeshMonthlySephardNote(): String = roshChodeshHalfHallelSephardNote()

    /** @deprecated Replaced by [roshChodeshHalfHallelChabadNote]. */
    fun roshChodeshMonthlyChabadNote(): String = roshChodeshHalfHallelChabadNote()

    fun yaalehVyavoLinks(profile: UserProfile) = buildList {
        add(
            ChecklistLink(
                "Sefaria — Shulchan Arukh O.C. 422:1",
                "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.422.1",
                "default",
            )
        )
        add(ChecklistLink("Peninei Halacha — Rosh Chodesh", "https://ph.yhb.org.il/en/05-01-01/", "default"))
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(ChecklistLink("Chabad — 12 Rosh Chodesh Facts", CHABAD_ROSH_CHODESH_URL, "chabad"))
        }
    }

    /** @deprecated Split into [yaalehVyavoMaarivExplanation] and [roshChodeshHalfHallelExplanation]. */
    fun roshChodeshExplanation(): String = roshChodeshHalfHallelExplanation()

    fun chanukahLightingExplanationTemplate(): String = CHANUKAH_LIGHTING_EXPLANATION_TEMPLATE

    fun chanukahLightingExplanationArgs(day: Int): Map<String, String> = mapOf(
        "day" to day.toString(),
        "brachotNote" to if (day == 1) {
            "First night: say all three brachot including Shehecheyanu."
        } else {
            "Tonight: two brachot (no Shehecheyanu unless first time lighting this year)."
        },
    )

    fun chanukahLightingExplanation(day: Int, profile: UserProfile): String {
        val args = chanukahLightingExplanationArgs(day)
        val out = ExplainerTemplateFill.fill(CHANUKAH_LIGHTING_EXPLANATION_TEMPLATE, args)
        return BeginnerHalachaGlossary.withKeyTerms(
            BeginnerHalachaGlossary.chanukahBasics() + "\n• Shamash — helper candle used to light the others (not counted in the eight nights)",
            out,
        )
    }

    private val CHANUKAH_LIGHTING_EXPLANATION_TEMPLATE = """
Chanukah night ${'$'}day of 8 — lighting the menorah.

When:
• Ideal: after tzeit (nightfall).
• Earliest time (when necessary): from Plag HaMincha (1¼ halachic hours before sunset — often ~60–75 minutes before sunset, season-dependent).
• Friday: light Chanukah before Shabbat candles; use enough oil/large enough candles to last 30 minutes after nightfall.
• Motzei Shabbat: light before or after Havdalah per minhag (but do not do melacha before Shabbat ends).

Late lighting (bedieved):
If you were unable to light at the ideal time (around sunset or nightfall), the absolute final cutoff is dawn (alot hashachar).

Whether you can still say the blessings depends on pirsumei nisa (publicizing the miracle):
• If household members are awake, or people are still on the street outside, you may light and recite the usual blessings — pirsumei nisa is still achieved.
• If everyone in the home is asleep and the streets are empty, many hold you should still light until dawn but omit the blessings — though mainstream poskim (Mishnah Berurah 672:11) and Chabad custom hold that if you can wake even one or two family members to create a household presence, you may still recite the blessings. Ask your rav when unsure.

Oil/candle requirement:
Make sure there is enough oil / a large enough candle to burn for at least 30 minutes. If you light early (from plag), it should still burn at least 30 minutes after nightfall (tzeit). Once dawn passes, that night’s mitzvah is missed.

How:
• Pirsumei nisa — publicize the miracle; place menorah by a window or doorway where passersby can see.
• Candles: insert right→left; light left→right; use the shamash.
• Do not use the Chanukah lights for work (use the shamash for utility light).

Prayers & meals:
• Insert Al HaNissim into every Amidah and into Birkat Hamazon (bentching) all day long.

${'$'}brachotNote

After lighting: sing HaNeiros halalu and Maoz Tzur (custom).
    """.trimIndent()

    fun pesachWeekPrepExplanation(cal: DayInfo, profile: UserProfile): String {
        val base = BeginnerHalachaGlossary.withKeyTerms(
            BeginnerHalachaGlossary.pesachPrep(),
            ExplainerTemplateFill.fill(
                ExplainerTemplateSupport.pesachWeekPrepTemplate(),
                ExplainerTemplateSupport.pesachWeekPrepArgs(cal, profile),
            ),
        )
        return base
    }

    fun birkatHaIlanotExplanationTemplate(): String = BIRKAT_HAILANOT_EXPLANATION_TEMPLATE

    fun birkatHaIlanotExplanationArgs(profile: UserProfile): Map<String, String> =
        mapOf("whenBlock" to birkatHaIlanotWhenBlock(profile))

    fun birkatHaIlanotWhenBlock(profile: UserProfile): String = when {
        BirkatHaIlanotRules.isSouthernHemisphere(profile.latitude) -> BIRKAT_HAILANOT_WHEN_SOUTHERN
        profile.latitude != null -> BIRKAT_HAILANOT_WHEN_NORTHERN
        else -> BIRKAT_HAILANOT_WHEN_UNKNOWN
    }

    fun birkatHaIlanotExplanation(profile: UserProfile): String {
        val args = birkatHaIlanotExplanationArgs(profile)
        return ExplainerTemplateFill.fill(BIRKAT_HAILANOT_EXPLANATION_TEMPLATE, args)
    }

    private val BIRKAT_HAILANOT_WHEN_SOUTHERN = """
When (Southern Hemisphere — your location):
The Shulchan Arukh places this blessing in Nissan, when spring arrives in Israel. Mainstream poskim rule that the blessing follows the natural blossoming of fruit trees in your locale — not the calendar month alone (Yalkut Yosef; Peninei Halakha). In Australia, South America, southern Africa, and similar regions, local spring falls in Elul and Tishrei (typically September–October). Say it once during that window, as soon as you see the first blossoms.
    """.trimIndent()

    private val BIRKAT_HAILANOT_WHEN_NORTHERN = """
When (Northern Hemisphere — your location):
Say it during Nissan, when fruit trees in your area begin to blossom — ideally as early in the month as you first see flowers (Shulchan Arukh O.C. 226:1).
    """.trimIndent()

    private val BIRKAT_HAILANOT_WHEN_UNKNOWN = """
When:
In the Northern Hemisphere, say it during Nissan when fruit trees blossom (Shulchan Arukh O.C. 226:1). In the Southern Hemisphere, say it when local fruit trees blossom in Elul–Tishrei. Set your city or GPS in Settings so the app shows this item in the correct season for you.
    """.trimIndent()

    private val BIRKAT_HAILANOT_EXPLANATION_TEMPLATE = """
Birkat Ha'Ilanot (בִּרְכַּת הָאִילָנוֹת — Blessing of the Trees) is recited once each Jewish year upon seeing fruit trees in bloom — thanking Hashem for the beauty and renewal of creation.

This checklist appears during the likely season for your hemisphere — a reminder to go look for blossoms, not an obligation to recite before you see them.

${'$'}whenBlock

The blessing (said once per year):
Baruch Atah Ado-nai Eloheinu Melech ha'olam shelo chiser be'olamo kelum u'vara vo beriyot tovot v'ilanot tovim lehanot bahem benei adam.
(בָּרוּךְ אַתָּה ה' אֱלֹקֵינוּ מֶלֶךְ הָעוֹלָם שֶׁלֹּא חִסֵּר בְּעוֹלָמוֹ כְּלוּם וּבָרָא בוֹ בְּרִיּוֹת טוֹבוֹת וְאִילָנוֹת טוֹבִים לֵהָנוֹת בָּהֶם בְּנֵי אָדָם)
"Blessed are You, L-rd our G-d, King of the universe, Who has withheld nothing from His world and created in it good creatures and good trees for people to enjoy."

How to fulfill it:
• Go outdoors to fruit-bearing trees — at least one tree; preferably two or more of the same or different species.
• Recite the blessing once while viewing the blossoms. Many add Tehillim 104 or other verses; follow your siddur or community custom.
• Once a year: if you said it in your local spring, you do not repeat it when traveling to another hemisphere later in the same Jewish year.
• Many communities avoid saying it on Shabbat — go during the week when you first see suitable blossoms.

What stage must the tree be in (melav'lave — מְלַבְלְבֵי)?
Melav'lave literally means when trees put forth shoots or blossoms (Shulchan Arukh O.C. 226:1, from the Talmud). The mainstream view requires that you see the flower emerge — ideally open, visible blossoms you can recognize as flowers.

L'chatchilah (ideal): the tree has moved past the leaf-only stage and shows open flowers, but the petals have not yet fallen and fruit has not begun to form.

What does not work:
• Green leaf buds alone — invalid; do not recite the blessing on leaves without recognizable flowers.
• After petals have dropped and fruit is forming — too late for this year's blessing.

Other views: several major poskim (including the Chazon Ish) rule that if flower buds are distinctly recognizable as flower buds — even if the petals have not yet opened — you may recite the blessing. Ask your rav when unsure.

Spiritual note:
Nissan is the month of redemption; blossoming trees recall that creation itself waits to praise Hashem. Even a brief walk among flowering orchards can be a mitzvah.
    """.trimIndent()

    fun birkatHaIlanotChabadNote(profile: UserProfile): String {
        if (!BirkatHaIlanotRules.isSouthernHemisphere(profile.latitude)) return ""
        return """
Chabad in the Southern Hemisphere:
Some Chabad communities look for rare late-blooming fruit trees during Nissan (local autumn) to align with the traditional month. If no suitable blossoms are found in Nissan, rely on the general rule and recite the blessing in Elul–Tishrei when local trees flower — as above.
        """.trim()
    }

    fun birkatHaIlanotLinks() = listOf(
        ChecklistLink(
            displayText = "Shulchan Arukh O.C. 226 (Sefaria)",
            url = "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.226",
        ),
        ChecklistLink(
            displayText = "Peninei Halakha — Blessing on Trees",
            url = "https://ph.yhb.org.il/en/14-11-01/",
        ),
        ChecklistLink(
            displayText = "Chabad.org — Blessing on Fruit Trees",
            url = "https://www.chabad.org/library/article_cdo/aid/4731/jewish/Blessing-on-Fruit-Trees.htm",
            nusach = "chabad",
        ),
    )

    fun birkatHachamahExplanationTemplate(): String = BIRKAT_HACHAMAH_EXPLANATION_TEMPLATE

    fun birkatHachamahExplanationArgs(occurrence: kotlinx.datetime.LocalDate): Map<String, String> =
        mapOf("dateLabel" to BirkatHachamahRules.formatOccurrenceDate(occurrence))

    fun birkatHachamahExplanation(occurrence: kotlinx.datetime.LocalDate): String {
        val dateLabel = BirkatHachamahRules.formatOccurrenceDate(occurrence)
        return BIRKAT_HACHAMAH_EXPLANATION_TEMPLATE
            .replace("\$dateLabel", dateLabel)
            .replace("{dateLabel}", dateLabel)
    }

    private val BIRKAT_HACHAMAH_EXPLANATION_TEMPLATE = """
Birkat Hachamah (בִּרְכַּת הַחַמָּה — Blessing of the Sun) is recited once every 28 years, when the sun completes the machzor gadol (מחזור גדול) — the great 28-year solar cycle described in the Talmud (Berakhot 59b).

The cycle:
Jewish tradition calculates the solar year as 365¼ days (Samuel; Eruvin 56a). Each year the vernal equinox shifts forward six hours; after 28 years it returns to the same day of the week and time — Tuesday at nightfall, when the sun was created (Bereishit 1:14–19). Because the blessing requires seeing the sun, recite it on Wednesday morning at sunrise.

This occurrence: ${'$'}dateLabel.

The blessing:
Baruch Atah Ado-nai Eloheinu Melech ha'olam oseh ma'aseh bereishit.
(בָּרוּךְ אַתָּה ה' אֱלֹקֵינוּ מֶלֶךְ הָעוֹלָם עוֹשֵׂה מַעֲשֵׂה בְרֵאשִׁית)
"Blessed are You, L-rd our G-d, King of the universe, Who makes the works of creation."

How to fulfill it:
• Go outdoors where the sun is visible — not through a window, per most poskim.
• Recite at sunrise on the morning of the occurrence, when you can see the sun. Many communities gather for a public recitation with additional Tehillim and passages — follow your kehilla.
• Ideal time: from sunrise through the third halachic hour of the day (sof zman tefillah). According to some opinions, you may still recite it until chatzos (halachic midday).
• If the sky is completely overcast and the sun cannot be seen, many hold the blessing is not recited without visible sunlight; some communities still gather when clouds hide the sun.

Notes:
• This mitzvah comes only once every 28 years.
• Check your local synagogue or Chabad center — large communal gatherings are common.
    """.trimIndent()

    fun ldovidExplanation(nusach: EffectiveNusach): String = """
Psalm 27 (לְדָוִד ה' אוֹרִי וְיִשְׁעִי) is added to Shacharit during Elul and Tishrei — a season of drawing close to G-d before and through the Days of Awe.

When:
• Said at the end of Shacharit — after Musaf on days when Musaf is recited; otherwise after the main morning service (follow your siddur).
• Start and end dates vary by minhag — this item follows your nusach setting.

Why:
• "The L-rd is my light and my salvation — whom shall I fear?" (Tehillim 27:1) — trust in divine protection through judgment season.
• A widespread custom in Elul–Tishrei; widely adopted across Ashkenaz, Sephard, and Edot HaMizrach with different calendars.
    """.trim()

    fun ldovidAshkenazNote(): String =
        "Ashkenaz minhag: from 2 Elul through Shemini Atzeret (22 Tishrei). Some communities end on Hoshana Rabbah (21 Tishrei) — follow your kehilla (Mishna Berurah 581:2; Rama)."

    fun ldovidSephardNote(): String =
        "Sephardi minhag: from Rosh Chodesh Elul through Yom Kippur (10 Tishrei) — prevalent custom (Ben Ish Chai). Some kehillot continue through Sukkot or Shemini Atzeret; ask your rav."

    fun ldovidEdotHamizrachNote(): String =
        "Edot HaMizrach minhag: from Rosh Chodesh Elul through Shemini Atzeret (22 Tishrei) in many communities; some end on Yom Kippur or Hoshana Rabbah — follow your kehilla."

    fun ldovidChabadNote(): String =
        "Chabad minhag: from 2 Elul through Hoshana Rabbah (21 Tishrei). Some continue through Shemini Atzeret — follow your Chabad rabbi."

    private val SELICHOT_EXPLANATION = """
Selichot (literally "Forgivenesses") is a formal order of liturgical prayer — penitential piyutim and biblical verses — recited in the lead-up to Rosh Hashana and Yom Kippur. It is not merely informal preparation before the High Holidays: it is a distinct service built around pleas for forgiveness and the Thirteen Attributes of Mercy (Shlosh-Esrei Midot Harachamim).

What you need:
• A special Selichot prayerbook (machzor / kuntres of Selichot) matched to your nusach — a regular weekday siddur usually does not include the full order.
• Bring your own, borrow from shul, or use a free online edition (many nuschaos are available digitally). Match Ashkenaz, Sephard, Edot HaMizrach, or Nusach Ari (Chabad) to your community.

The Core: The 13 Attributes of Mercy

The absolute centerpiece of every Selichot service is the repeated recitation of the Thirteen Attributes of Divine Mercy (Shlosh-Esrei Midot Harachamim).

According to the Talmud (Rosh Hashanah 17b), after the Israelites sinned with the Golden Calf, God wrapped Himself in a prayer shawl (tallit) like a prayer leader and demonstrated this exact sequence of verses to Moses. God made a covenant: "Whenever Israel sins, let them recite this service before Me in this order, and I will forgive them." The verses (Exodus 34:6-7) emphasize God's infinite patience, compassion, and willingness to extend clean slates to those who genuinely seek change.

The Great Timing Divide: Sephardic vs. Ashkenazic

While the emotional intent is identical, the Jewish world splits significantly on when the Selichot prayers begin, rooted in different legal and cultural traditions:

• The Sephardic Custom (An Entire Month): Sephardic communities begin reciting Selichot on the second day of the Hebrew month of Elul and continue every single day until Yom Kippur—a full 40-day marathon. This timeline directly mirrors the 40 days Moses spent on Mount Sinai pleading for forgiveness after the Golden Calf incident, ending on Yom Kippur.

• The Ashkenazic Custom (The Final Countdown): Ashkenazic communities begin reciting Selichot much later—traditionally on the Saturday night/Sunday morning before Rosh Hashana (ensuring a minimum of four days of prayers before the New Year hits). The first night of Ashkenazic Selichot is a major communal event, packed with high-attendance services that often begin at midnight or pre-dawn hours, but can occur throughout the day.

Mystical and psychological texts note that these hours carry a unique quality of quiet stillness. The distractions of the day have faded, the world is asleep, and the mind is stripped of its usual defenses. Pleading for forgiveness in a dimly lit synagogue while the rest of the neighborhood is dark creates an intensely intimate, raw conversation between the individual and God.
    """.trim()

    private val SELICHOT_CHABAD_ADDENDUM = """

Chabad follows the Ashkenazic Selichot calendar described above (not from Rosh Chodesh Elul).

During Elul before Selichot begin:
• Blow the shofar daily after Shacharit (except Erev Rosh Hashana and when Rosh Hashana is Shabbat).
• Recite three extra chapters of Tehillim after Shacharit (Baal Shem Tov custom).

When Selichot begin:
• Follow your local Chabad minyan — often late night or before Shacharit.
• Use a Nusach Ari Selichot book (or a free online Nusach Ari edition) — not a generic Ashkenaz Selichot booklet.
• Increase in Torah, teshuvah, and tzedakah through the month of Elul.
    """.trim()

    fun selichotExplanation(nusach: EffectiveNusach): String = when (nusach) {
        EffectiveNusach.CHABAD -> BeginnerHalachaGlossary.withKeyTerms(
            BeginnerHalachaGlossary.selichotBasics(),
            "$SELICHOT_EXPLANATION\n\n$SELICHOT_CHABAD_ADDENDUM",
        )
        EffectiveNusach.SEFARD,
        EffectiveNusach.EDOT_HAMIZRACH,
        EffectiveNusach.ASHKENAZ -> BeginnerHalachaGlossary.withKeyTerms(
            BeginnerHalachaGlossary.selichotBasics(),
            SELICHOT_EXPLANATION,
        )
    }

    fun kiddushLevanaExplanationTemplate(): String = KIDDUSH_LEVANA_EXPLANATION_TEMPLATE

    fun kiddushLevanaExplanationArgs(profile: UserProfile): Map<String, String> =
        mapOf("waitLine" to kiddushLevanaWaitLine(profile))

    private fun kiddushLevanaWaitLine(profile: UserProfile): String = when (profile.effectiveNusach()) {
        EffectiveNusach.SEFARD -> KIDDUSH_LEVANA_WAIT_SEFARD
        EffectiveNusach.EDOT_HAMIZRACH -> KIDDUSH_LEVANA_WAIT_EDOT
        else -> KIDDUSH_LEVANA_WAIT_ASHKENAZ_CHABAD
    }

    fun kiddushLevanaExplanation(profile: UserProfile): String {
        val args = kiddushLevanaExplanationArgs(profile)
        val out = ExplainerTemplateFill.fill(KIDDUSH_LEVANA_EXPLANATION_TEMPLATE, args)
        return BeginnerHalachaGlossary.withKeyTerms(BeginnerHalachaGlossary.daveningBasics(), out)
    }

    private val KIDDUSH_LEVANA_WAIT_SEFARD =
        "Recited once a month when the moon is visible, usually beginning 7 days after the molad (Shulchan Arukh O.C. 426:4; Peninei Halakha 05-01-18)."

    private val KIDDUSH_LEVANA_WAIT_EDOT =
        "Recited once a month when the moon is visible. The majority of Sefardim wait until 7 days after the molad (Shulchan Arukh O.C. 426:4). Moroccan and some other North African kehillot begin after 3 days / 72 hours (Peninei Halakha 05-01-18) — follow your community."

    private val KIDDUSH_LEVANA_WAIT_ASHKENAZ_CHABAD =
        "Recited once a month when the moon is visible, usually beginning 72 hours (3 days) after the molad (Ashkenaz / Chabad custom; Peninei Halakha 05-01-18)."

    private val KIDDUSH_LEVANA_EXPLANATION_TEMPLATE = """
Kiddush Levana (Sanctification of the New Moon) — Birkat HaLevanah. Men are obligated in this time-bound positive mitzvah; women are exempt and the widespread custom is that women do not recite it at all (see Deracheha link below).

${'$'}waitLine

Deadline: The window ends at or before the full moon. Many authorities calculate this as approximately half the mean lunar month from the molad — often cited as roughly 14 days, 18 hours, and 22 minutes (~14.75 days) — though poskim disagree whether the cutoff follows that calculation or when the moon is visibly waning. Saying it on the night of the 15th may already be too late depending on the month and opinion. Always check Sof Zman Kiddush Levana for your location before the month ends.

When:
• After nightfall (tzeit), standing outdoors under the open sky.
• Ideally on Motzei Shabbat while still in nice clothes — a widespread custom because you are already dressed up.
• In Av, most wait until after Tisha B'Av; in Tishrei, many wait until after Yom Kippur — unless concerned the moon may not be visible later.
• Not on Shabbat or Yom Tov itself.

How:
• The moon must be clearly visible — if clouds block it, wait for a clear night within the window.
• Use your siddur; it praises G-d for creation — not worship of the moon.

Window closes at or before the full moon (approximately — opinions vary; often ~14.75 days from the molad). Check Sof Zman Kiddush Levana for your location.
    """.trimIndent()

    private val THREE_WEEKS_INTRO = """
The Three Weeks (בין המצרים) from 17 Tammuz until Tisha B'Av commemorate the destruction of the Temple and Jewish tragedies.

Why we mourn:
• On 17 Tammuz the walls of Jerusalem were breached; on 9 Av both Temples were destroyed, among other national calamities.

Three stages of mourning (similar to mourning for a parent):
• The Three Weeks (17 Tammuz–Tisha B'Av): the least severe stage
• The Nine Days (from Rosh Chodesh Av in Ashkenazi custom, or stricter from the week of Tisha B'Av for many Sephardim): stricter mourning
• The week of Tisha B'Av (shavuah she'chal bo, from the Shabbat before 9 Av): the strictest stage

Clothing:
• During the Three Weeks, you may buy and wear garments on which you would not recite Shehecheyanu — e.g. socks, underwear, or plain replacements without special joy (Kaf HaChaim O.C. 551:88). If you would not make a bracha on wearing the item, many authorities permit wearing it as well.
• From Rosh Chodesh Av, do not buy new clothes even without Shehecheyanu, even if you will wear them only after Tisha B'Av (Rama O.C. 551:7). See the Nine Days checklist for laundry and wearing freshly laundered clothing.

Other Three Weeks practices:
• Moving into a new home is permitted during the Three Weeks; ideally avoid moving during the Nine Days, but it is allowed if necessary.
• Swimming for pleasure is permitted during the Three Weeks but not during the Nine Days. Exercise swimming may be permitted even then — ask your rav.
• Dangerous or unnecessarily risky activities are discouraged (non-urgent surgery should ideally wait). Travel is not forbidden.
• Social outings before Rosh Chodesh Av are not forbidden by mourning law itself (other halachot may still apply).

Shabbat during the Three Weeks: mourning practices do not apply on Shabbat itself — observe Shabbat fully.
    """.trimIndent()

    private val THREE_WEEKS_ASHKENAZ_EXPLANATION = (THREE_WEEKS_INTRO + """

Ashkenazi custom observes a longer, stricter mourning throughout the Three Weeks, intensifying during the Nine Days.

From 17 Tammuz (general Three Weeks):
• Haircuts & shaving: prohibited for the entire Three Weeks.
• Music: instrumental music is not listened to throughout the period.
• Weddings: not held.
• Shehecheyanu: traditionally not recited on new clothes or new fruits; permitted on Shabbat.

From 1 Av (Nine Days): restrictions intensify — see the Nine Days checklist item for meat, wine, laundry, bathing, and home practices.
""").trim()

    private val THREE_WEEKS_SEPHARDIC_EXPLANATION = (THREE_WEEKS_INTRO + """

Sephardic and Edot HaMizrach communities, following Shulchan Arukh, generally take a more lenient approach than Ashkenazim during the early Three Weeks.

From 17 Tammuz (general Three Weeks):
• Haircuts & shaving: permitted during most of the Three Weeks; shaving is usually prohibited only during the week in which Tisha B'Av falls (shavuah she'chal bo).
• Music: live or recorded music is avoided.
• Weddings: some communities avoid weddings from 17 Tammuz; others are lenient and avoid them only from Rosh Chodesh Av — follow your kehilla.
• Shehecheyanu: avoided on new items for the duration of the period.

From Rosh Chodesh Av or the week of Tisha B'Av: additional restrictions apply — see the Nine Days checklist item. Some communities (e.g. Syrian, Mashadi) are stricter on meat and wine from Rosh Chodesh Av.
""").trim()

    private val THREE_WEEKS_CHABAD_EXPLANATION = (THREE_WEEKS_INTRO + """

Chabad follows strict Ashkenazi mourning customs, with specific emphasis from the Lubavitcher Rebbe on spiritual growth during this period.

From 17 Tammuz (general Three Weeks):
• Haircuts, music & weddings: prohibited throughout the entire Three Weeks.
• Shehecheyanu: avoided entirely, except on Shabbat or when required for a mitzvah (e.g. brit milah).
• Torah & charity: increase Torah study — especially subjects about the Holy Temple's layout and construction — and give extra tzedakah during this time.

From Rosh Chodesh Av (Nine Days): restrictions intensify — see the Nine Days checklist item for meat, wine, laundry, bathing, and home practices.
""").trim()

    fun threeWeeksExplanationTemplate(profile: UserProfile): String = when (profile.effectiveNusach()) {
        EffectiveNusach.ASHKENAZ -> THREE_WEEKS_ASHKENAZ_EXPLANATION
        EffectiveNusach.SEFARD -> THREE_WEEKS_SEPHARDIC_EXPLANATION
        EffectiveNusach.EDOT_HAMIZRACH -> THREE_WEEKS_SEPHARDIC_EXPLANATION
        EffectiveNusach.CHABAD -> THREE_WEEKS_CHABAD_EXPLANATION
    }

    fun threeWeeksAshkenazExplanation(): String = BeginnerHalachaGlossary.withKeyTerms(
        BeginnerHalachaGlossary.mourningBasics(),
        THREE_WEEKS_ASHKENAZ_EXPLANATION,
    )

    fun threeWeeksSephardicExplanation(): String = BeginnerHalachaGlossary.withKeyTerms(
        BeginnerHalachaGlossary.mourningBasics(),
        THREE_WEEKS_SEPHARDIC_EXPLANATION,
    )

    fun threeWeeksEdotHamizrachExplanation(): String = threeWeeksSephardicExplanation()

    fun threeWeeksChabadExplanation(): String = BeginnerHalachaGlossary.withKeyTerms(
        BeginnerHalachaGlossary.mourningBasics(),
        THREE_WEEKS_CHABAD_EXPLANATION,
    )

    fun threeWeeksExplanation(profile: UserProfile): String = when (profile.effectiveNusach()) {
        EffectiveNusach.ASHKENAZ -> threeWeeksAshkenazExplanation()
        EffectiveNusach.SEFARD -> threeWeeksSephardicExplanation()
        EffectiveNusach.EDOT_HAMIZRACH -> threeWeeksEdotHamizrachExplanation()
        EffectiveNusach.CHABAD -> threeWeeksChabadExplanation()
    }

    private fun nineDaysSharedHalacha(nusach: EffectiveNusach) = """
Erev Tisha B'Av (8 Av afternoon): stop learning Torah except sad topics; eat the final meal (seudah hamafseket) before the fast. Tallit and tefillin are worn at Shacharit on Erev Tisha B'Av — the restriction applies on Tisha B'Av day itself. At the seudah hamafseket, do not bentch with a mezuman or at a minyan meal — only the simple final meal before the fast.

Tisha B'Av (9 Av) — the fast and the day:
• Full 25-hour fast from sunset to nightfall; kinot at Shacharit without tallit and tefillin (Ashkenazi/Chabad omit morning tefillin; Sephardi custom varies).
• To follow along with the kinot (elegies), you need a Kinnot / Kinot prayerbook (or a free online Kinnot edition for your nusach) — a regular siddur does not include them.
• ${TishaBeavTefillinRules.fastDayTefillinNote(nusach)}
• Until chatzos on 9 Av: maintain a mournful mindset; sit on the floor or a low stool (no seat higher than about 12 inches / 30 cm).
• After chatzos on 9 Av: the five afflictions still apply until nightfall when the fast ends — but do not greet others during the fast day (until nightfall when the fast ends).
• Hand washing: only to remove ritual impurity (tum'a) — wash only to the knuckle where the fingers join the hand; or to remove actual dirt where it is on the hand.
• Do not brush teeth with water; a dry toothbrush is permitted. Flossing is permitted.
• ${TishaBeavTefillinRules.FAST_DAY_TRAVEL_NOTE}

When 9 Av begins on Motzei Shabbat (Saturday night fast):
• Recite Baruch ha'mavdil when Shabbat ends; say the candle blessing after Maariv.
• Do not say the rest of Havdalah that night. After the fast day is over, after nightfall Sunday night, recite Havdalah over wine with the hamavdil paragraph only — no spices that week.

Shabbat Chazon (the Shabbat before 9 Av): Shabbat is observed normally — meat and wine are permitted.

When 9 Av falls on Shabbat and the fast is observed Sunday (10 Av):
• Because you are already fasting on 10 Av, the usual daytime mourning restrictions for the 10th are heavily altered.
• Ashkenazim: laundry, bathing, and haircuts are permitted immediately Motzei fast Sunday night — you do not wait until Monday chatzos. Meat and wine remain prohibited Sunday night; resume Monday morning. Havdalah is delayed from Saturday night until Sunday night after the fast over wine. Music: Ashkenazi custom often continues until Monday morning, though some lenient views permit it Sunday night.
• Sephardim: when the fast is observed Sunday, laundry, haircuts, and bathing are permitted immediately Motzei fast Sunday night; only meat and wine remain restricted until Monday morning. When 10 Av is Friday, many resume bathing and laundry immediately Thursday night after the fast; follow your rav for meat restrictions.

When the fast falls on Thursday and 10 Av is Friday (Shabbat prep — kvod Shabbat):
• Haircuts, shaving, laundry, and bathing are permitted Friday morning right away so you can prepare for Shabbat — you do not wait until Friday chatzos for these.
• Ashkenazim still refrain from meat and wine until chatzos (midday) Friday, unless tasting food being cooked specifically for Shabbat.
• Sephardim generally permit bathing and laundry immediately Thursday night after the fast, but keep the meat restriction until Friday sunset.
• After the fast ends Thursday night, some melacha related to personal pleasure may resume in specific cases — ask your rav before resuming music, laundry, or bathing for pleasure.
    """.trimIndent()

    private val NINE_DAYS_ASHKENAZ_EXPLANATION = ("""
The Nine Days (from 1 Av until after Tisha B'Av) are the strictest part of summer mourning in Ashkenazi custom.

From 1 Av:
• Meat & wine: forbidden, except on Shabbat (not on Rosh Chodesh Av or erev Shabbat), at a brit milah, siyum, or pidyon ha'ben, or wine for Havdalah (ideally a child ages 6–10 drinks the cup).
• Laundry: washing, ironing, and wearing freshly laundered outer clothing are prohibited. Clean socks and underwear are permitted (ideally toss them on the floor first).
• Bathing: bathing or showering for pleasure is prohibited; washing a dirty, sweaty, or smelly body is permitted (except on Tisha B'Av).
• Clothing & shopping: do not buy new clothes even without Shehecheyanu, even if you will wear them only after the fast (Rama O.C. 551:7). Major home improvements, painting, and renovations are avoided.
• Music & celebrations: live or recorded music remains prohibited; weddings are not held.
• Shehecheyanu: do not recite on new clothes or fruits during the Nine Days, except on Shabbat — but if you do eat a new fruit or buy something new, you must still say Shehecheyanu.
• Court cases: try not to be involved in a court case opposing a non-Jew during the Nine Days (not forbidden if unavoidable).
• Kiddush Levana: if you will not likely see the moon on any day from 10–14 Av, you may say kiddush levana during the Nine Days.
• Investments & projects: do not start new projects or investments if they can be delayed without loss.

""" + nineDaysSharedHalacha(EffectiveNusach.ASHKENAZ) + """

After the fast day is over, after nightfall when the fast ends: some Nine Days restrictions begin to lift — but Ashkenazi custom continues meat, wine, music, laundry, and bathing for pleasure until chatzos (halachic midday) on 10 Av (not at nightfall of 9 Av itself). When 9 Av was Shabbat and the fast was Sunday, or when 10 Av is Friday, see the special-calendar notes above. Ask your rav before resuming.

Nine Days Havdalah: On Motzei Shabbat during the Nine Days, use wine or grape juice for Havdalah. Ashkenazi custom (Rema O.C. 551:10): ideally a child (ages 6–9) drinks the cup; if none is present, the one reciting Havdalah drinks it — the mitzvah of Havdalah overrides the custom of restraint.
""").trim()

    private val NINE_DAYS_SEPHARDIC_EXPLANATION = ("""
The Nine Days and the week of Tisha B'Av (shavuah she'chal bo) are the strictest part of summer mourning for Sephardic communities following Shulchan Arukh — generally more lenient than Ashkenazim until the week of Tisha B'Av.

From Rosh Chodesh Av (1 Av):
• Haircuts: usually prohibited from Rosh Chodesh Av, or only during the actual week of Tisha B'Av — follow your rav.
• Weddings & music: many communities stop weddings and avoid music from Rosh Chodesh Av; some are lenient until the week of Tisha B'Av.
• Meat & wine: some kehillot already avoid them from Rosh Chodesh Av, while others wait for shavuah she'chal bo.
• Clothing: from Rosh Chodesh Av, do not buy new clothes even without Shehecheyanu, even if you will wear them only after Tisha B'Av (Rama O.C. 551:7).

From the week in which Tisha B'Av falls (shavuah she'chal bo):
• Meat & wine: prohibited from the start of that week (not necessarily the full Nine Days). Some communities (e.g. Syrian, Mashadi) are strict from Rosh Chodesh Av. Wine on Shabbat and at seudot mitzvah (brit, siyum, pidyon ha'ben) is permitted.
• Laundry & bathing: restrictions on washing clothes and bathing for pleasure apply during the week of Tisha B'Av; washing a dirty body is permitted (except on Tisha B'Av).
• Fresh clothing & home projects: many avoid freshly laundered garments, renovations, painting, and major purchases during that stricter week.
• Shehecheyanu: avoided on new items except on Shabbat — but if you do eat a new fruit, you must still say Shehecheyanu.

""" + nineDaysSharedHalacha(EffectiveNusach.SEFARD) + """

After the fast day is over, after nightfall when the fast ends: many Sephardim resume most restrictions at that point, while others continue meat, wine, laundry, and bathing for pleasure until chatzos on 10 Av — confirm your community's psak before resuming.
""").trim()

    private val NINE_DAYS_CHABAD_EXPLANATION = ("""
The Nine Days (from Rosh Chodesh Av until after Tisha B'Av) follow strict Ashkenazi mourning in Chabad practice.

From Rosh Chodesh Av:
• Meat & wine: prohibited entirely, except on Shabbat or at a seudat mitzvah (e.g. brit milah, siyum, pidyon ha'ben).
• Laundry, bathing & home: traditional Ashkenazi prohibitions against laundering, bathing for pleasure, home improvements, painting, and buying new garments are observed.
• Clothing: do not buy new clothes even without Shehecheyanu, even if you will wear them only after the fast.
• Music, weddings & haircuts: Three Weeks restrictions continue in full through the Nine Days.
• Temple focus: increase study about the Beit HaMikdash and add tzedakah, following the Rebbe's emphasis for these days.

""" + nineDaysSharedHalacha(EffectiveNusach.CHABAD) + """

After the fast day is over, after nightfall when the fast ends: follow accepted Chabad psak — Ashkenazi custom often continues meat, wine, music, laundry, and bathing for pleasure until chatzos on 10 Av (not at nightfall of 9 Av itself). Ask your rav if unsure.

Nine Days Havdalah: use wine or grape juice; Ashkenazi-style custom often gives the cup to a child (ages 6–9) when possible.
""").trim()

    fun nineDaysExplanationTemplate(profile: UserProfile): String = when (profile.effectiveNusach()) {
        EffectiveNusach.ASHKENAZ -> NINE_DAYS_ASHKENAZ_EXPLANATION
        EffectiveNusach.SEFARD -> NINE_DAYS_SEPHARDIC_EXPLANATION
        EffectiveNusach.EDOT_HAMIZRACH -> NINE_DAYS_SEPHARDIC_EXPLANATION
        EffectiveNusach.CHABAD -> NINE_DAYS_CHABAD_EXPLANATION
    }

    fun nineDaysAshkenazExplanation(): String = BeginnerHalachaGlossary.withKeyTerms(
        BeginnerHalachaGlossary.mourningBasics(),
        NINE_DAYS_ASHKENAZ_EXPLANATION,
    )

    fun nineDaysSephardicExplanation(): String = BeginnerHalachaGlossary.withKeyTerms(
        BeginnerHalachaGlossary.mourningBasics(),
        NINE_DAYS_SEPHARDIC_EXPLANATION,
    )

    fun nineDaysEdotHamizrachExplanation(): String = nineDaysSephardicExplanation()

    fun nineDaysChabadExplanation(): String = BeginnerHalachaGlossary.withKeyTerms(
        BeginnerHalachaGlossary.mourningBasics(),
        NINE_DAYS_CHABAD_EXPLANATION,
    )

    fun nineDaysExplanation(profile: UserProfile): String = when (profile.effectiveNusach()) {
        EffectiveNusach.ASHKENAZ -> nineDaysAshkenazExplanation()
        EffectiveNusach.SEFARD -> nineDaysSephardicExplanation()
        EffectiveNusach.EDOT_HAMIZRACH -> nineDaysEdotHamizrachExplanation()
        EffectiveNusach.CHABAD -> nineDaysChabadExplanation()
    }

    fun tuBshvatExplanation(): String = BeginnerHalachaGlossary.withKeyTerms(
        BeginnerHalachaGlossary.block(
            BeginnerHalachaGlossary.BRACHA,
            BeginnerHalachaGlossary.SHEHECHEYANU,
            BeginnerHalachaGlossary.RAV,
        ),
        """
Tu B'Shvat (15 Shevat) — New Year for Trees — is a day to appreciate Hashem's fruit and Land of Israel.

Liturgical note:
• Tachanun is completely omitted from standard weekday prayers today — as well as during yesterday afternoon's Mincha service (14 Shevat).

Customs:
• Eat fruit — especially tree fruit from the seven species: grapes, figs, pomegranates, olives, and dates. Wheat and barley are grains in the Shivat Haminim — honor them with bread or grain dishes separately, not under the tree-fruit bracha (Borei Pri Ha'etz).
• Say brachot and after-brachot carefully.
• Shehecheyanu on fruit: You may say Shehecheyanu only on a seasonal fruit that is genuinely new to the market this season (like fresh pomegranates or figs). Do not say this blessing on fruits grown and sold year-round (like bananas, pineapples, or apples), even if you personally have not eaten one recently.
• Some hold a Tu B'Shvat Seder with four cups of wine (white to red) and themed fruit — follow a guide if hosting.
• No fasting; work is permitted; it is not Yom Tov.

Spiritual focus: gratitude for creation, connection to Eretz Yisrael, and growth (trees blossom in Israel around this season).
    """.trim(),
    )

    // ── Links ─────────────────────────────────────────────────────────────────

    fun sukkahLinks(profile: UserProfile) = buildList {
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(ChecklistLink("Chabad — Build a sukkah", "https://www.chabad.org/library/article_cdo/aid/420822/jewish/How-to-Build-a-Sukkah.htm", "chabad"))
            add(ChecklistLink("Chabad — Dwelling in the sukkah", "https://www.chabad.org/library/article_cdo/aid/420823/jewish/Eating-in-the-Sukkah.htm", "chabad"))
        }
        add(ChecklistLink("Peninei Halacha — Sukkah", "https://ph.yhb.org.il/en/category/13/13-01/", "default"))
        add(ChecklistLink("Aish — Sukkot", "https://aish.com/holidays/sukkot/", "default"))
        add(ChecklistLink("Ohr Somayach — Sukkot", "https://ohr.edu/holidays/succos/", "default"))
    }

    fun arbaMinimLinks(profile: UserProfile) = buildList {
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(ChecklistLink("Chabad — The four species", "https://www.chabad.org/library/article_cdo/aid/420824/jewish/The-Four-Species.htm", "chabad"))
            add(ChecklistLink("Chabad — Women & lulav", "https://www.chabad.org/library/article_cdo/aid/420825/jewish/Women-and-Lulav.htm", "chabad"))
        }
        add(ChecklistLink("Peninei Halacha — Lulav", "https://ph.yhb.org.il/en/13-05-01/", "default"))
        add(ChecklistLink("Aish — Lulav & Etrog", "https://aish.com/holidays/sukkot/", "default"))
        add(ChecklistLink("Ohr Somayach — Sukkot", "https://ohr.edu/holidays/succos/", "default"))
    }

    fun sheminiAtzeretLinks(profile: UserProfile) = buildList {
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(ChecklistLink("Chabad — Shemini Atzeret & Simchat Torah", "https://www.chabad.org/library/article_cdo/aid/4464/jewish/What-Is-Shemini-Atzeret-Simchat-Torah.htm", "chabad"))
        }
        add(ChecklistLink("Peninei Halacha — Festivals", "https://ph.yhb.org.il/en/category/13/13-07/", "default"))
        add(ChecklistLink("Aish — Shemini Atzeret", "https://aish.com/holidays/sukkot/", "default"))
    }

    fun simchatTorahLinks(profile: UserProfile) = buildList {
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(ChecklistLink("Chabad — Simchat Torah", "https://www.chabad.org/library/article_cdo/aid/4464/jewish/What-Is-Shemini-Atzeret-Simchat-Torah.htm", "chabad"))
        }
        add(ChecklistLink("Peninei Halacha — Simchat Torah", "https://ph.yhb.org.il/en/category/13/13-07/", "default"))
        add(ChecklistLink("Aish — Simchat Torah", "https://aish.com/holidays/sukkot/", "default"))
        add(ChecklistLink("Ohr Somayach — Simchat Torah", "https://ohr.edu/holidays/simchat_torah/", "default"))
    }

    fun cholHamoedLinks(cal: DayInfo, profile: UserProfile) = buildList {
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(ChecklistLink("Chabad — Chol HaMoed", "https://www.chabad.org/library/article_cdo/aid/5279/jewish/Chol-Hamoed.htm", "chabad"))
        }
        add(ChecklistLink("Peninei Halacha — Chol HaMoed", "https://ph.yhb.org.il/en/12-11-01/", "default"))
        val aishUrl = if ("chol_hamoed_sukkot" in cal.activeSeasons && "chol_hamoed_pesach" !in cal.activeSeasons) {
            "https://aish.com/holidays/sukkot/"
        } else {
            "https://aish.com/holidays/pesach/"
        }
        add(ChecklistLink("Aish — Chol HaMoed", aishUrl, "default"))
    }

    fun kiddushLevanaLinks(profile: UserProfile) = buildList {
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(ChecklistLink("Chabad — Kiddush Levana", "https://www.chabad.org/library/article_cdo/aid/1904288/jewish/Kiddush-Levana-Sanctification-of-the-Moon.htm", "chabad"))
        }
        add(ChecklistLink("Peninei Halacha — Blessing the New Moon", "https://ph.yhb.org.il/en/05-01-17/", "default"))
        add(ChecklistLink("Deracheha — Women & Kiddush Levana", "https://www.deracheha.org/answers/kiddush-levana/", "default"))
        add(ChecklistLink("Shulchan Arukh OC 426 (Sefaria)", "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.426", "default"))
    }

    fun roshChodeshLinks(profile: UserProfile) = buildList {
        add(ChecklistLink("Shulchan Arukh O.C. 419 (Sefaria)", "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.419", "default"))
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(ChecklistLink("Chabad — 12 Rosh Chodesh Facts", CHABAD_ROSH_CHODESH_URL, "chabad"))
        }
        add(ChecklistLink("Peninei Halacha — Rosh Chodesh", "https://ph.yhb.org.il/en/05-01-01/", "default"))
        add(ChecklistLink("Aish — Rosh Chodesh", "https://aish.com/43-rosh-chodesh-2/", "default"))
    }

    fun chanukahDayLinks(profile: UserProfile) = buildList {
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(ChecklistLink("Chabad — How to light", "https://www.chabad.org/holidays/chanukah/article_cdo/aid/103868/jewish/How-to-Light-Chanukah-Candles.htm", "chabad"))
        }
        add(ChecklistLink("Peninei Halacha — Chanukah", "https://ph.yhb.org.il/en/05-12-01/", "default"))
        add(ChecklistLink("Ohr Somayach — Chanukah", "https://ohr.edu/1304", "default"))
        add(ChecklistLink("Aish — Chanukah", "https://aish.com/holidays/chanukah/", "default"))
    }

    fun pesachWeekLinks(profile: UserProfile) = pesachPrepLinksShared(profile)

    fun pesachPrepLinksShared(profile: UserProfile) = buildList {
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(ChecklistLink("Chabad — Passover prep", "https://www.chabad.org/holidays/passover/default_cdo/jewish/Passover.htm", "chabad"))
        }
        add(ChecklistLink("Peninei Halacha — Pesach", "https://ph.yhb.org.il/en/04-03-01/", "default"))
        add(ChecklistLink("Aish — Passover", "https://aish.com/holidays/pesach/", "default"))
    }

    fun tuBshvatLinks() = listOf(
        ChecklistLink("Chabad — Tu B'Shvat", "https://www.chabad.org/library/article_cdo/aid/468738/jewish/Tu-BiShvat-What-and-How.htm", "chabad"),
        ChecklistLink("Aish — Tu B'Shvat", "https://aish.com/48965616/", "default"),
        ChecklistLink("Peninei Halacha — Tu B'Shvat", "https://ph.yhb.org.il/tu-beshvat/", "default")
    )

    fun zecherMachatzitHaShekelExplanation(): String = BeginnerHalachaGlossary.withKeyTerms(
        BeginnerHalachaGlossary.purimBasics(),
        """Zecher LeMachatzit HaShekel (the remembrance of the half-shekel) is a custom observed today to commemorate the historic biblical tax. In Torah times, every adult Jewish male was required to contribute a yearly half-shekel to the Holy Temple (Beit HaMikdash) to fund the public communal sacrifices and maintenance of the sanctuary.

Because the Temple is no longer standing, the absolute requirement no longer applies. Instead, we give a symbolic donation to charity to keep the memory of this mitzvah alive.

Why is it performed?

Historical commemoration: It serves as a physical reminder of the communal unity and shared responsibility of the Temple era.

A spiritual shield: According to the Talmud, Haman offered 10,000 silver talents to King Achashverosh to justify destroying the Jewish people. God anticipated this, and the merit of the Jewish people's annual half-shekel donations countered and nullified Haman's wicked decree. By giving this donation today, we tap into that same spiritual protection.

Tzedakah (charity): The money given is distributed to poor families, Torah institutions, or other charitable causes, fulfilling a vital role in supporting the community.

When and how is it performed?

The timing: The custom is traditionally performed on the Fast of Esther, during the afternoon Mincha service. Giving charity on a fast day is considered especially auspicious.

If someone is unable to give it during Mincha, or if the fast falls early (like on a Thursday before a Sunday Purim), it can be given on Purim morning before the reading of the Megillah.

The practice: It is customary to give three coins that represent the "half" unit of the local currency (for example, three half-dollar coins in the US, or three half-shekel coins in Israel). Three coins are used because the word terumah (contribution) is mentioned three times in the Torah portion discussing the commandment.

Many synagogues place three silver coins out for the congregation to use. You lift the coins to acquire them, place your own donation into the charity box to "buy" them, and then drop the silver coins back into the box to fulfill the custom.

This is a widespread custom (minhag), not one of the four Purim mitzvot. Follow your shul or rav.""",
    )

    fun zecherMachatzitHaShekelLinks(profile: UserProfile) = buildList {
        add(
            ChecklistLink(
                "Chabad — Machatzit HaShekel",
                "https://www.chabad.org/holidays/purim/article_cdo/aid/394172/jewish/Machatzit-HaShekel.htm",
                if (profile.effectiveNusach() == EffectiveNusach.CHABAD) "chabad" else "default",
            ),
        )
        add(
            ChecklistLink(
                "Sefaria — Shulchan Arukh O.C. 694",
                "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.694",
                "default",
            ),
        )
        add(ChecklistLink("Aish — Purim", "https://aish.com/holidays/purim/", "default"))
    }
}
