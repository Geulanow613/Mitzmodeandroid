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
        """
The week before Shavuot is for practical preparation — Shavuot celebrates Matan Torah (receiving the Torah at Sinai).

Food & home:
• Dairy is a cherished Shavuot minhag (cheesecake, blintzes, lasagna, ice cream) — plan menus and shop early. A festive meat meal with wine remains standard practice to fulfill the primary mitzvah of Simchat Yom Tov (Shulchan Arukh O.C. 529:2); many families have a dairy kiddush or snack, then a full meat Yom Tov meal.
• Some decorate with flowers and greenery (minhag of a garden around Sinai).
• Stock wine, grape juice, and Yom Tov staples for festive meals${if (!profile.isInIsrael) " — in the Diaspora, prepare for two days of Yom Tov" else ""}.

Torah & tefillah:
• Confirm shul times for Maariv, Shacharit, and Musaf; many communities have all-night learning (Tikkun Leil Shavuot) — find a program or plan a home study session.
• Choose texts to learn: Ruth (read on Shavuot), Megillat Rut customs, Pirkei Avot, or a topic your family enjoys.
• Review Akdamut / Yizkor customs if your community observes them on the second day (Diaspora).

Joy & family (simchat Yom Tov):
• Plan festive meat meals with wine — the core Simchat Yom Tov obligation — alongside any dairy minhag.
• Gifts for wife and children (clothes, treats) l'fi mamono — associate the day with joy.

Practical:
• On Erev Shavuot: daytime preparations can continue up until nightfall (tzeit), as the holiday begins later than usual. Reheating and cooking for the night meal may also be done on the holiday itself after tzeit, strictly from a pre-existing flame. Set up a blech or hot plate if needed. You cannot light candles or make Kiddush until tzeit so the Omer count remains temimot (complete).
• Turn off devices before Yom Tov — this app is for prep, not use on chag.
        """.trim(),
    )

    fun sukkotWeekPrepExplanation(profile: UserProfile): String = BeginnerHalachaGlossary.withKeyTerms(
        BeginnerHalachaGlossary.sukkotBasics(),
        """
The week before Sukkot (Tishrei 8–13) is for building joy and getting the mitzvot ready — many begin the sukkah right after Yom Kippur.

Sukkah:
• Build or repair your sukkah — walls, frame, and schach (plant covering, not metal or solid roof).
• Choose a spot with more shade than sun under the schach; avoid blocking by trees or porch roofs.
• Set up table, chairs, and decorations; plan to eat (and sleep, when possible) in the sukkah all seven days.

Arba Minim (Four Species):
• Order or buy a kosher set: lulav, etrog, hadassim (myrtle), aravot (willow).
• Check the etrog (pitom intact if present) and that leaves are fresh — buy early before stores sell out.
• Get a lulav holder (hoshanah holder / koisan) and an etrog box.

Meals & Yom Tov:
• Plan menus for seven days of festive meals in and out of the sukkah.
• Wine, grape juice, challah, and Yom Tov groceries — ${if (profile.isInIsrael) "one day of Yom Tov at the start" else "two days of Yom Tov at the start in the Diaspora"}.
• First night: Kiddush and bread in the sukkah; men say leishev basukkah before eating bread (women per minhag). If it rains on the first night: Ashkenaz — wait up to one hour, then eat a kezayit in the sukkah without leishev basukkah and finish indoors (Rema O.C. 639:5); Sephardic — if rain spoils the meal, eat the full meal indoors from the start (Shulchan Arukh), but many Sephardic decisors (including Yalkut Yosef) hold it is proper at the end of dinner to walk out to the sukkah and eat a kezayit of bread without leishev basukkah.

Joy & family:
• Simchat Yom Tov — plan treats for children, festive clothing, and meals that bring household joy.

Practical:
• Hoshana Raba prep (21 Tishrei): Buy or prepare a dedicated bundle of five fresh willow branches (aravot) for Hoshana Raba morning — separate from your daily lulav set. At the conclusion of synagogue services, the community performs Chagizat Aravah, striking these branches against the ground five times (Minhag Nevi'im).
• Confirm shul times for Hallel and Hoshanot throughout Sukkot, and for hakafot on Hoshana Raba.
• Turn off devices before Yom Tov — this app is for prep, not use on chag.
        """.trim(),
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
• Fabric / canvas sukkahs: Tie fabric walls down tightly so they do not move or flap in a normal wind, or reinforce with horizontal ropes/straps around the frame spaced less than 3 tefachim apart (lavud) to create valid stationary partitions — ask your rav. Pop-up kits that billow are a common beginner pitfall.
• Size: Large enough to fit a table and for an adult to sit with head and most of body inside (roughly 7×7 handbreadths minimum; build larger for comfort).
• Schach (covering): Must be plant material that grew from the ground and is detached (tree branches, bamboo mats certified for schach, etc.) — not metal, plastic, or a solid roof. Schach rules: Material must be detached plant life that has NOT been manufactured into a functional tool, vessel, or finished item (such as old furniture parts or wooden slats from crates), as finished objects are susceptible to ritual impurity (kabalat tumah) and are completely invalid for schach.
• Shade: More shade than sun on the floor under the schach. You may see some sky through gaps — that is fine.
• Height: Walls at least ~3 ft (10 tefachim); schach can be high, but very high sukkot still need a valid structure below.

Practical steps:
1. Choose a spot — backyard, porch, balcony (check building rules). Avoid placing schach directly under a tree or house roof overhang that blocks the mitzvah.
2. Build the frame and walls first; lay schach last so it stays dry and valid.
3. Arrange schach loosely with gaps — not a solid rainproof roof (use a removable slatted cover on top only if rain is expected, per halachic guidance).
4. Set up table, chairs, and decorations (fruit, lights — electric on Yom Tov/Chol HaMoed per your rav).
5. Before the first night of Sukkot, eat at least a kezayit of bread in the sukkah with the bracha leishev basukkah (first night is Torah-level for men; women per minhag).

First night rain protocol (if it rains on the first night):
• Ashkenaz (Rema O.C. 639:5): Wait up to one hour. If rain does not stop, enter the sukkah, make Kiddush, eat a kezayit of bread without leishev basukkah, then finish the rest of the meal indoors.
• Sephardic (Shulchan Arukh): If rain is heavy enough to spoil the meal, you are exempt from eating the full meal in the sukkah — make Kiddush and eat the full meal indoors from the start, without leishev basukkah. Nevertheless, many Sephardic decisors (including Yalkut Yosef) rule that it remains proper and praiseworthy at the very end of the meal to go out to the sukkah and eat a single kezayit of bread without reciting leishev basukkah — honoring the first night's unique obligation in part.

First night: Kiddush and Yom Tov meal in the sukkah. Men: leishev basukkah (sit in the sukkah — a blessing said before eating bread in the sukkah). Throughout Sukkot: eat bread and sleep in sukkah when possible (rain and illness have exemptions — ask your rabbi).
    """.trim(),
    )

    fun arbaMinimSharedBody(profile: UserProfile): String {
        val daysNote = if (profile.isInIsrael) {
            "In Israel: lulav all 7 days; first day is the primary Torah obligation for men."
        } else {
            "In the Diaspora: men's primary Torah obligation is the first day of Sukkot (15 Tishrei); the mitzvah continues rabbinically on the second day of Yom Tov and Chol HaMoed."
        }
        return """
Arba Minim (ארבעה מינים) — the Four Species — are taken each day of Sukkot (except Shabbat).

The four:
• Lulav — closed palm branch (at least 3 tefachim)
• Etrog — citron, beautiful and mostly intact (pitom if present should be intact)
• Hadasim — three myrtle branches
• Aravot — two willow branches

How to observe (everyone):
• Assemble the lulav: Bind the lulav, hadassim, and aravot together (per your hoshanah holder / koisan). Ensure the central spine of the lulav extends upward at least one handbreadth (tefach — around 4 inches) higher than the tops of the myrtle and willow branches (Shulchan Arukh O.C. 650:1).
• Check kashrut before Yom Tov — especially etrog and that leaves are fresh enough.
• When: Daytime; many do it before Shacharit at home or in shul. Not on Shabbat.
• $daysNote

If a species is missing or invalid, ask your rabbi — there are limited substitutions in pressing cases.
        """.trim()
    }

    private fun arbaMinimMenWave(profile: UserProfile): String = when (profile.effectiveNusach()) {
        EffectiveNusach.CHABAD ->
            "Hold lulav in right hand, etrog in left (stem/pitom down). Say Al netilat lulav, then wave three times in each direction: right (south), left (north), forward (east), up, down, back (west) — the Arizal sequence when facing east."
        EffectiveNusach.SEFARD, EffectiveNusach.EDOT_HAMIZRACH ->
            "Hold lulav and etrog together; say bracha, then wave right (south), left (north), forward (east), up, down, back (west) — the Arizal sequence when facing east. Hoshanot on Hoshana Raba in many kehillot."
        EffectiveNusach.ASHKENAZ ->
            "Hold lulav in right, etrog in left (pitom down). Say bracha, then wave east, south, west, north, up, down (often two waves per direction)."
    }

    fun arbaMinimExplanation(profile: UserProfile): String = BeginnerHalachaGlossary.withKeyTerms(
        BeginnerHalachaGlossary.sukkotBasics(),
        """
${arbaMinimSharedBody(profile)}

Men — Torah obligation:
• The first day of Sukkot worldwide (15 Tishrei) is the Torah-level day for men. In the Diaspora, the mitzvah continues rabbinically on the second day of Yom Tov and Chol HaMoed.
• Bracha: Men say Al netilat lulav every day when taking the lulav (except Shabbat). Shehecheyanu is on the first day only.
• ${arbaMinimMenWave(profile)}

The ownership rule (lakhem — u'lekachtem lakhem):
• The strict requirement to fully own your lulav and etrog set applies only on the very first day of Sukkot worldwide (15 Tishrei; Shulchan Arukh O.C. 658:3).
• On that day only: if you do not own a set, ask the owner to give it to you as matana al menat lehachzir (gift on condition of return) before you wave.
• In the Diaspora: on the second day of Yom Tov and throughout Chol HaMoed, you may borrow a shared or synagogue set without a formal gift — the rabbinic extension does not carry the Torah ownership requirement (Peninei Halakha, Laws of Sukkot 13:13).
    """.trim(),
    )

    fun arbaMinimExplanationFemale(profile: UserProfile): String {
        val brachaLine = when (profile.effectiveNusach()) {
            EffectiveNusach.SEFARD, EffectiveNusach.EDOT_HAMIZRACH ->
                "Sephardi / Edot HaMizrach women: Following standard Sephardic authorities, most wave without saying the bracha (still a meaningful mitzvah)."
            EffectiveNusach.ASHKENAZ, EffectiveNusach.CHABAD ->
                "Ashkenazi women (and many Chabad women who follow this custom): Say Al netilat lulav, then wave."
        }
        val waveLine = when (profile.effectiveNusach()) {
            EffectiveNusach.SEFARD, EffectiveNusach.EDOT_HAMIZRACH ->
                "Sephardi / Edot HaMizrach women: Not strictly required to wave in all six directions — a simple shake of the lulav is sufficient to fulfill the recommended practice."
            EffectiveNusach.CHABAD ->
                "Chabad women: Wave right (south), left (north), forward (east), up, down, back (west) — the same Arizal sequence as men."
            EffectiveNusach.ASHKENAZ ->
                "Ashkenazi women: Wave east, south, west, north, up, down — the same as men."
        }
        return BeginnerHalachaGlossary.withKeyTerms(
            BeginnerHalachaGlossary.sukkotBasics(),
            """
${arbaMinimSharedBody(profile)}

Women — recommended mitzvah (not obligatory):
• Women are exempt from this time-bound commandment, but participating is highly popular in both Ashkenazi and Sephardi communities. This checklist marks it as recommended — not a strict daily obligation like men's first-day mitzvah.

Do women need their own set?
• No. Most women use a shared family set (usually owned by a husband or father) or a synagogue set.

The ownership rule (for men on the first day):
• On the first day of Sukkot worldwide only, a man must own the set he waves (see men's explanation for matana al menat lehachzir). In the Diaspora, borrowing without a gift is permitted from the second day of Yom Tov onward.

Bracha:
• $brachaLine

How to wave:
• $waveLine
• Hold etrog in left, lulav (with hadassim and aravot) in right — or together per your siddur.

Each day of Sukkot (except Shabbat) you may fulfill this recommended mitzvah again.
        """.trim(),
        )
    }

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
• Synagogue note: Because drinking often occurs during daytime hakafot, many synagogues move the Priestly Blessing (Birkat Kohanim) up to the early morning Shacharit service instead of keeping it in Musaf, so Kohanim are completely sober for the blessing.
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
• Synagogue note: Because drinking often occurs during daytime hakafot, many synagogues move the Priestly Blessing (Birkat Kohanim) up to the early morning Shacharit service instead of keeping it in Musaf, so Kohanim are completely sober for the blessing.
• Complete the last parsha of Devarim and begin Bereshit — the Torah never ends.
• Aliyot — Kol HaNearim (all children together; an adult or bar mitzvah boy recites the blessing with them), Chatan Torah, Chatan Bereshit, and honors for members.
• Many shuls extend Hakafot to the evening or multiple sessions.

Practical tips:
• Dress festively; arrive early for a good spot.
• If you have a Torah honor, prepare your brachot.
• Celebrate responsibly — the day is about love of Torah, not excess.

Candle lighting last night of Yom Tov; havdalah when Yom Tov ends (often with Sukkot/Yom Tov additions in havdalah — use your siddur).
    """.trim(),
    )

    // ── Chol HaMoed ────────────────────────────────────────────────────────────

    fun cholHamoedHonorExplanation(cal: DayInfo, profile: UserProfile): String {
        val isPesach = "chol_hamoed_pesach" in cal.activeSeasons
        val isSukkot = "chol_hamoed_sukkot" in cal.activeSeasons
        val hallelBlock = buildString {
            appendLine("Davening — Yaaleh V'yavo and Hallel:")
            if (isSukkot) {
                appendLine("• Sukkot (every day of the festival, including Chol HaMoed): Full Hallel.")
            }
            if (isPesach) {
                if (profile.isInIsrael) {
                    appendLine(
                        "• Pesach Chol HaMoed: Half Hallel only (Full Hallel on the first day of Pesach; Half Hallel from the second day onward, including Chol HaMoed and the last days). Hallel blessings: Ashkenazic custom permits a blessing over Partial Hallel; Sephardic custom strictly prohibits it (Shulchan Arukh O.C. 422:2)."
                    )
                } else {
                    appendLine(
                        "• Pesach Chol HaMoed: Half Hallel only (Full Hallel on the first two days of Yom Tov; Half Hallel from Chol HaMoed onward, including the final Yom Tov days). Hallel blessings: Ashkenazic custom permits a blessing over Partial Hallel; Sephardic custom strictly prohibits it (Shulchan Arukh O.C. 422:2)."
                    )
                }
            }
        }.trim()
        val festivalLines = buildString {
            if (isPesach) appendLine("• On Pesach Chol HaMoed: no chametz; matzah and KP food only.")
            if (isSukkot) appendLine("• On Sukkot Chol HaMoed: lulav (not on Shabbat), meals in sukkah when applicable.")
        }.trim()
        val hoshanaRabaBlock = if (
            isSukkot &&
            cal.hebrewMonth == HebrewCalendarEngine.TISHREI &&
            cal.hebrewDay == 20
        ) {
            """

Hoshana Raba prep (tomorrow — 21 Tishrei):
• Buy or prepare a dedicated bundle of five fresh willow branches (aravot) for tomorrow morning — separate from your daily lulav set.
• At the conclusion of synagogue services tomorrow, the community performs Chagizat Aravah — striking these branches against the ground five times (Minhag Nevi'im). Hoshana Raba is the final sealing of judgment from Yom Kippur."""
        } else {
            ""
        }
        return BeginnerHalachaGlossary.withKeyTerms(
            BeginnerHalachaGlossary.cholHamoedBasics(),
            """
Chol HaMoed (חול המועד) — the intermediate festival days — is not full Yom Tov, but it is not ordinary weekdays either.
$hoshanaRabaBlock

Spirit of the day:
• Simchat moed — joy of the festival; nicer meals, family time, Torah learning.
• Melacha — many labors are restricted (not as strict as Yom Tov). Work needed to avoid financial loss may be permitted — ask your rabbi before assuming.
• Ochel nefesh — food preparation is permitted.

What to do:
$hallelBlock
• Avoid treating it like a regular workday — schedule outings, learning, and visits that fit the moed.
$festivalLines

Avoid (without halachic guidance): heavy laundry, major home projects, shaving, haircuts, and activities that diminish the festival atmosphere.
• Grooming restrictions: Shaving and getting a haircut are strictly prohibited on Chol HaMoed (Shulchan Arukh O.C. 531) — unless under highly specific conditions, such as a boy turning three or leaving prison (ask your rav). This applies even if you want to look clean for the remaining days of the festival.
            """.trim(),
        )
    }

    fun cholHamoedClothesExplanation(): String = BeginnerHalachaGlossary.withKeyTerms(
        BeginnerHalachaGlossary.cholHamoedBasics(),
        """
Wearing nicer clothing on Chol HaMoed honors the festival (kavod ha'moed).

Practice:
• Wear clothes you would not wear for dirty chores — clean, pressed, or festive attire.
• Some avoid new clothing that would require a Shehecheyanu on Chol HaMoed (ask your rav).
• Men: many wear a hat and jacket for shul even if weekday dress is casual.

This applies each day of Chol HaMoed — it is a simple daily way to mark the moed apart from ordinary weekdays.
    """.trim(),
    )

    fun cholHamoedWineReviitExplanation(cal: DayInfo): String {
        val isPesach = "chol_hamoed_pesach" in cal.activeSeasons
        val isSukkot = "chol_hamoed_sukkot" in cal.activeSeasons
        val kashrutLine = when {
            isPesach -> "• Use kosher-for-Passover wine only."
            isSukkot -> "• Use wine that meets your year-round kashrut needs."
            else -> "• Use wine that meets your festival kashrut needs."
        }
        return BeginnerHalachaGlossary.withKeyTerms(
            BeginnerHalachaGlossary.cholHamoedBasics(),
            """
Drinking wine on Chol HaMoed is part of simchat ha'moed — rejoicing on the festival (Peninei Halakha 12-10-03; OU Torah). The Talmud teaches that there is no joy without wine; men especially mark simchat Yom Tov with wine, and everyone should enjoy nicer meals than on weekdays.

This is not like the four cups at the Seder or Shabbat-day Kiddush — Torah-level obligations where many communities require a stricter revi'it (~5.1 fl oz / 150 ml per Chazon Ish). For this optional Chol HaMoed custom, many poskim encourage at least a lenient revi'it of wine (~2.9 fl oz / 86 ml per Rav Chaim Na'eh; Peninei Halakha) at a festive Chol HaMoed meal. Confirm with your rav.

Wine vs grape juice (a real debate — ask your rav):
• Strict view: Many authorities (e.g. Rabbeinu Ephraim, Shulchan Aruch HaRav, Rav Elyashiv; Peninei Halakha 12-01-09) hold that simcha requires wine's intoxicating, joy-bringing quality — so grape juice does not fulfill this mitzvah the same way.
• Lenient view: Others (e.g. Rav Shlomo Zalman Auerbach, Rav Yaakov Kamenetsky) treat grape juice like wine for this purpose — drinking a revi'it of grape juice can fulfill the mitzvah per their psak.

If you do not drink wine:
• Chamar medina: If you cannot drink wine or grape juice, many poskim permit using a significant local beverage (such as an appropriate kosher-for-Passover liquor or coffee) depending on your community's standards.
• Some hold that meat and other festival delicacies at Yom Tov meals also help fulfill the broader mitzvah of rejoicing — wine is still the classic way when you can.

How:
• Ideally with bread and enjoyable food at a Chol HaMoed meal — Peninei Halakha encourages two proper meals each day when possible, but bread and extra food are not strict obligations on Chol HaMoed.
$kashrutLine
• Each day of the moed counts separately.

If you avoid alcohol for health or preference, ask your rav which option fits you: grape juice per the lenient view, chamar medina, or emphasizing meat and festive food.
            """.trim(),
        )
    }

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

    private const val CHABAD_ROSH_CHODESH_URL =
        "https://www.chabad.org/library/article_cdo/aid/4909907/jewish/12-Rosh-Chodesh-Facts.htm"

    private const val YAALEH_VYAVO_HALACHA_SOURCE =
        "Shulchan Arukh O.C. 422:1; Peninei Halakha 05-01-10"

    /** Shacharit, Mincha, or Maariv on Chol HaMoed / Yom Tov — full correction rules. */
    private fun yaalehVyavoForgotAmidahCorrection(service: String): String = """
If you forgot:
• Still in Retzei (before concluding the blessing) — insert Yaaleh V'yavo in its place and continue ($YAALEH_VYAVO_HALACHA_SOURCE).
• Finished Retzei but not yet started Modim (e.g. at HaMachazir Shechinato LeZion) — say Yaaleh V'yavo there, then continue with Modim ($YAALEH_VYAVO_HALACHA_SOURCE).
• Already said "Baruch" of Modim or anything after — return to the beginning of Retzei, insert Yaaleh V'yavo, and complete the remaining blessings ($YAALEH_VYAVO_HALACHA_SOURCE).
• Finished the entire Amidah (after the final Yihiyu L'ratzon) — repeat only the $service Amidah (Shemoneh Esrei), never the full service, even if you already davened Musaf, Maariv, or anything else afterward ($YAALEH_VYAVO_HALACHA_SOURCE).
    """.trim()

    /** Shacharit or Mincha on Rosh Chodesh — same in-Amidah rules; repeat Amidah if finished. */
    private fun yaalehVyavoForgotShacharitOrMincha(service: String): String =
        yaalehVyavoForgotAmidahCorrection(service)

    /** Maariv on Rosh Chodesh only — no repeat; limited in-Amidah correction. */
    private fun yaalehVyavoForgotMaarivRoshChodesh(): String = """
If you forgot at Maariv on Rosh Chodesh:
• Still in Retzei before God's name at the conclusion — insert Yaaleh V'yavo there and continue ($YAALEH_VYAVO_HALACHA_SOURCE).
• Once you finished Retzei, or after the entire Amidah — do not go back and do not repeat. Beit Din sanctified the new month by day, not at night (Berachot 30b; $YAALEH_VYAVO_HALACHA_SOURCE). Continue davening.
    """.trim()

    fun yaalehVyavoShacharitExplanation(): String = """
Add Yaaleh V'yavo in the Shacharit Amidah on Rosh Chodesh — in the blessing Retzei (Avodah).

${yaalehVyavoForgotShacharitOrMincha("Shacharit")}

Also add Yaaleh V'yavo in bentching if you eat bread today.
    """.trim()

    private fun yaalehVyavoFemaleAmidahLead(service: String): String =
        "If you recite the $service Amidah on Rosh Chodesh, add Yaaleh V'yavo in the blessing Retzei (Avodah)."

    fun yaalehVyavoShacharitExplanationFemale(): String = """
${yaalehVyavoFemaleAmidahLead("Shacharit")}

${yaalehVyavoForgotShacharitOrMincha("Shacharit")}

If you say Birkat Hamazon when you eat bread today, add Yaaleh V'yavo there too.
    """.trim()

    fun yaalehVyavoMinchaExplanation(): String = """
Add Yaaleh V'yavo in the Mincha Amidah on Rosh Chodesh — in the blessing Retzei (Avodah).

${yaalehVyavoForgotShacharitOrMincha("Mincha")}
    """.trim()

    fun yaalehVyavoMinchaExplanationFemale(): String = """
${yaalehVyavoFemaleAmidahLead("Mincha")}

${yaalehVyavoForgotShacharitOrMincha("Mincha")}

If you say Birkat Hamazon when you eat bread today, add Yaaleh V'yavo there too.
    """.trim()

    fun yaalehVyavoMaarivExplanation(): String = """
Add Yaaleh V'yavo in the Maariv Amidah on Rosh Chodesh — in the blessing Retzei (Avodah).

${yaalehVyavoForgotMaarivRoshChodesh()}

Also add Yaaleh V'yavo in bentching if you eat bread tonight.
    """.trim()

    fun yaalehVyavoMaarivExplanationFemale(): String = """
${yaalehVyavoFemaleAmidahLead("Maariv")}

${yaalehVyavoForgotMaarivRoshChodesh()}

If you say Birkat Hamazon when you eat bread tonight, add Yaaleh V'yavo there too.
    """.trim()

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
• Have the meal during the day. Poskim write this commemorates the feast the Sanhedrin held at Beit Ya'zek for witnesses who came to testify they saw the new moon (Mishnah Rosh Hashanah 2:5; Orchos Chaim and Kol Bo, cited on O.C. 419).
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
• Have the meal during the day. Poskim write this commemorates the feast the Sanhedrin held at Beit Ya'zek for witnesses who came to testify they saw the new moon (Mishnah Rosh Hashanah 2:5; Orchos Chaim and Kol Bo, cited on O.C. 419).
• Money spent on Rosh Chodesh meals — like Shabbat and Yom Tov — is not deducted from the income allotted to you on Rosh Hashanah; if you spend more for these mitzvos, Heaven adds to your allotment (Pesikta de-Rav Kahana, cited in Tur O.C. 419 and Magen Avraham 419:1).

Davening today (listed in your Morning, Afternoon, and Evening Prayer sections):
• If you recite Shacharit, Mincha, or Maariv Amidah — add Yaaleh V'yavo in Retzei. Shacharit/Mincha: correct per timing (insert in Retzei, between Retzei and Modim, return to Retzei after Modim started, or repeat only that Amidah if finished). Maariv on Rosh Chodesh only: do not repeat if forgotten after Retzei (Berachot 30b; SA O.C. 422:1)
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

    fun chanukahLightingExplanation(day: Int, profile: UserProfile): String {
        return BeginnerHalachaGlossary.withKeyTerms(
            BeginnerHalachaGlossary.chanukahBasics() + "\n• Shamash — helper candle used to light the others (not counted in the eight nights)",
            """
Chanukah night $day of 8 — lighting the menorah.

When:
• Light after tzeit (nightfall) — not before sunset. On Friday, light Chanukah candles before Shabbat candles (approx. 20–25 minutes before nightfall).
• Friday candle size warning: Because Chanukah candles must burn for at least 30 minutes after nightfall (tzeit), standard small Chanukah candles cannot be used on Friday afternoon — they will burn out before nightfall and invalidate the mitzvah. Use larger candles (like standard Shabbat candles) or pour enough oil to burn for at least ~90 minutes from lighting (often longer if you light well before sunset) so they survive well past dark.
• On Motzei Shabbat, light Chanukah before or after Havdalah per minhag.
• Motzei Shabbat lighting: If your custom is to light Chanukah candles before formal Havdalah over wine, you must terminate Shabbat first — recite Atah Chonantanu in the Maariv Amidah, or say "Baruch hamavdil bein kodesh l'chol" aloud before striking a match. Melacha remains forbidden until Shabbat has ended.

How:
• Pirsumei nisa — publicize the miracle; place menorah by a window or doorway where passersby can see.
• Placement: Insert candles from right to left in the menorah (universal custom per Shulchan Arukh O.C. 676:5 — Ashkenaz, Sephard, and Chabad).
• Lighting: Light from left to right, always starting with the newest candle (the leftmost one) and moving toward the right. Use the shamash to light each candle.
• Oil or candles must burn at least 30 minutes after nightfall.
• Brachot (first night all three; other nights two): lehadlik ner shel Chanukah, she'asa nissim; Shehecheyanu on first night.
• Do not use the Chanukah lights for work — shamash is for utility light.

Prayers & meals:
• Insert Al HaNissim into every Amidah and into Birkat Hamazon (bentching) all day long.

${if (day == 1) "First night: say all three brachot including Shehecheyanu." else "Tonight: two brachot (no Shehecheyanu unless first time lighting this year)."}

After lighting: sing HaNeiros halalu and Maoz Tzur (custom).
        """.trim(),
        )
    }

    fun pesachWeekPrepExplanation(cal: DayInfo, profile: UserProfile): String {
        val base = BeginnerHalachaGlossary.withKeyTerms(
            BeginnerHalachaGlossary.pesachPrep(),
            """
Pesach begins in about a week — use this time for practical preparation (not spring cleaning every closet, but removing chametz where it matters).

Focus areas:
• Kitchen and dining — where chametz is eaten: counters, stove, microwave, toaster, fridge, pantry.
• Cars, offices, bags, coat pockets — crumbs and snack wrappers.
• Sell or use up chametz food you will not keep; plan Pesach menus and shopping.
• Haggadahs, matzah, wine, seder plate — order before prices rise and stores sell out.

Kashering vessels for Pesach:
• Year-round pots, pans, dishes, and utensils absorb chametz from hot foods and long use. For Pesach you need either kashered vessels or a separate Pesach set.
• Common methods (each item has rules — ask your rabbi): hagalah (immersion in boiling water), irui (pouring boiling water on surfaces), libun (heat for metal that cannot be kashered by hagalah).
• Start early — kashering the whole kitchen takes time. Many families use only their Pesach dishes and keep year-round dishes sealed away for the week.

Pesach dishes vs selling chametz dishes:
• Unpack or buy a dedicated Pesach set; label milchig/fleishig if needed.
• Many people include the chametz residue and absorbed flavor within their year-round dishes and pots in the sale, locking them away securely. The physical dishes themselves are not sold, avoiding the halachic requirement to re-immerse them in a Mikveh (tevilat kelim) after Pesach (Shulchan Arukh Y.D. 120). Rabbis structure the contract accordingly — list items clearly and do not use sealed dishes until buy-back.

Mechirat chametz (plan this week):
• Authorize the sale with your rabbi or community several days before Erev Pesach — most rabbis stop accepting sale forms by the night before Erev Pesach, even though the sale takes effect on Erev Pesach morning. Many accept online forms (local rabbi, kashrut council, or Chabad.org-style sale). The sale must be valid halachically; follow your rabbi's instructions on what to include and when it takes effect.
• Selling transfers ownership so chametz you are not destroying can sit sealed in your home without belonging to you during Pesach.

After Pesach — chametz owned by a Jew during Pesach:
• Chametz that belonged to a Jew over Pesach becomes forbidden (chametz she'avar alav haPesach) — you may not own it, eat it, or otherwise benefit from it even after Pesach ends.
• That is why the sale must be real, and why you must not use sold chametz during Pesach. Ask your rav about repurchase timing and about store-bought chametz after Pesach if you are unsure.

Halachic mindset:
• You need not make the home sterile — remove edible chametz and clean places where chametz was brought.
• Bedikat chametz and biur happen on Erev Pesach — this week sets you up to succeed.

Children & family: assign zones, check school bags, plan chametz finish-up meals before Pesach.
        """.trim(),
        )
        val shabbatSchedule = buildString {
            ErevPesachPrepText.erevPesachShabbatScheduleBlock(cal)?.let { append(it) }
            YomTovShabbatPrepText.scheduleBlock(cal, profile, "Pesach")?.let {
                if (isNotEmpty()) append("\n\n")
                append(it)
            }
        }
        return if (shabbatSchedule.isNotEmpty()) "$base\n\n$shabbatSchedule" else base
    }

    fun birkatHaIlanotExplanation(profile: UserProfile): String {
        val southern = BirkatHaIlanotRules.isSouthernHemisphere(profile.latitude)
        val whenBlock = if (southern) {
            """
When (Southern Hemisphere — your location):
The Shulchan Arukh places this blessing in Nissan, when spring arrives in Israel. Mainstream poskim rule that the blessing follows the natural blossoming of fruit trees in your locale — not the calendar month alone (Yalkut Yosef; Peninei Halakha). In Australia, South America, southern Africa, and similar regions, local spring falls in Elul and Tishrei (typically September–October). Say it once during that window, as soon as you see the first blossoms.
            """.trimIndent()
        } else if (profile.latitude != null) {
            """
When (Northern Hemisphere — your location):
Say it during Nissan, when fruit trees in your area begin to blossom — ideally as early in the month as you first see flowers (Shulchan Arukh O.C. 226:1).
            """.trimIndent()
        } else {
            """
When:
In the Northern Hemisphere, say it during Nissan when fruit trees blossom (Shulchan Arukh O.C. 226:1). In the Southern Hemisphere, say it when local fruit trees blossom in Elul–Tishrei. Set your city or GPS in Settings so the app shows this item in the correct season for you.
            """.trimIndent()
        }
        return """
Birkat Ha'Ilanot (בִּרְכַּת הָאִילָנוֹת — Blessing of the Trees) is recited once each Jewish year upon seeing fruit trees in bloom — thanking Hashem for the beauty and renewal of creation.

This checklist appears during the likely season for your hemisphere — a reminder to go look for blossoms, not an obligation to recite before you see them.

$whenBlock

The blessing (said once per year):
Baruch Atah Ado-nai Eloheinu Melech ha'olam shelo chiser be'olamo kelum u'vara vo beriyot tovot v'ilanot tovim lehanot bahem benei adam.
(בָּרוּךְ אַתָּה ה' אֱלֹקֵינוּ מֶלֶךְ הָעוֹלָם שֶׁלֹּא חִסֵּר בְּעוֹלָמוֹ כְּלוּם וּבָרָא בוֹ בְּרִיּוֹת טוֹבוֹת וְאִילָנוֹת טוֹבִים לֵהָנוֹת בָּהֶם בְּנֵי אָדָם)
"Blessed are You, L-rd our G-d, King of the universe, Who has withheld nothing from His world and created in it good creatures and good trees for people to enjoy."

How to fulfill it:
• Go outdoors to fruit-bearing trees — at least one tree; preferably two or more of the same or different species.
• The trees should show new buds or blossoms — not yet fully formed fruit.
• Recite the blessing once while viewing the blossoms. Many add Tehillim 104 or other verses; follow your siddur or community custom.
• Once a year: if you said it in your local spring, you do not repeat it when traveling to another hemisphere later in the same Jewish year.
• Many communities avoid saying it on Shabbat — go during the week when you first see suitable blossoms.

Spiritual note:
Nissan is the month of redemption; blossoming trees recall that creation itself waits to praise Hashem. Even a brief walk among flowering orchards can be a mitzvah.
        """.trim()
    }

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

    fun birkatHachamahExplanation(occurrence: kotlinx.datetime.LocalDate): String {
        val dateLabel = BirkatHachamahRules.formatOccurrenceDate(occurrence)
        return """
Birkat Hachamah (בִּרְכַּת הַחַמָּה — Blessing of the Sun) is recited once every 28 years, when the sun completes the machzor gadol (מחזור גדול) — the great 28-year solar cycle described in the Talmud (Berakhot 59b).

The cycle:
Jewish tradition calculates the solar year as 365¼ days (Samuel; Eruvin 56a). Each year the vernal equinox shifts forward six hours; after 28 years it returns to the same day of the week and time — Tuesday at sundown, when the sun was created (Bereishit 1:14–19). Because the blessing requires seeing the sun, it is recited the following morning — Wednesday at sunrise.

This occurrence: $dateLabel.

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
        """.trim()
    }

    fun ldovidExplanation(nusach: EffectiveNusach): String = """
Psalm 27 (לְדָוִד ה' אוֹרִי וְיִשְׁעִי) is added to Shacharit during Elul and Tishrei — a season of drawing close to G-d before and through the Days of Awe.

When:
• Said after the morning service (often after Shacharit Amidah, before concluding prayers — follow your siddur).
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

    fun selichotExplanation(nusach: EffectiveNusach): String = when (nusach) {
        EffectiveNusach.SEFARD, EffectiveNusach.EDOT_HAMIZRACH -> BeginnerHalachaGlossary.withKeyTerms(
            BeginnerHalachaGlossary.selichotBasics(),
            """
Selichot (סליחות) — penitential prayers — begin in Elul for Sefardim and Edot HaMizrach (Shulchan Arukh O.C. 581:1; Peninei Halakha, Laws of the Festivals 15-02-05).

How:
• Said at night (often after chatzos layla / before dawn) or early morning per community.
• Not on Rosh Chodesh Elul itself; then daily through Yom Kippur — follow your kehilla's schedule.
• Includes the 13 Attributes of Mercy (Hashem Hashem) with congregation responses.
• Prepare by checking local synagogue calendar — times shift through Elul.
        """.trim(),
        )
        EffectiveNusach.CHABAD -> BeginnerHalachaGlossary.withKeyTerms(
            BeginnerHalachaGlossary.selichotBasics(),
            """
Chabad follows the standard Ashkenazi Selichot calendar — beginning on Motzei Shabbat before Rosh Hashana (or the prior Motzei Shabbat when Rosh Hashana falls early in the week), not on Rosh Chodesh Elul.

During Elul before Selichot begin:
• Blow the shofar daily after Shacharit (except Erev Rosh Hashana and when Rosh Hashana is Shabbat).
• Recite three extra chapters of Tehillim after Shacharit (Baal Shem Tov custom).

When Selichot begin:
• Follow your local Chabad minyan — often late night or before Shacharit.
• Use Nusach Ari for Selichot.
• Increase in Torah, teshuvah, and tzedakah through the month of Elul.
        """.trim(),
        )
        EffectiveNusach.ASHKENAZ -> BeginnerHalachaGlossary.withKeyTerms(
            BeginnerHalachaGlossary.selichotBasics(),
            """
Ashkenaz Selichot begin on Motzei Shabbat before Rosh Hashana, with at least four days of Selichot before the holiday (Peninei Halakha 15-02-05; Rama). If Rosh Hashana falls on Monday or Tuesday, Selichot begin the previous Motzei Shabbat.

How:
• First night is often a large community gathering (Motzei Shabbat).
• Said standing; many wear tallit if already worn for Maariv or at morning Selichot.
• Arrive early — services are long and moving.
• Continue daily until Yom Kippur (including Erev Rosh Hashana, fast day schedules differ).
• Check your shul for exact times (usually late night or early morning).
        """.trim(),
        )
    }

    fun threeWeeksExplanation(profile: UserProfile): String = BeginnerHalachaGlossary.withKeyTerms(
        BeginnerHalachaGlossary.mourningBasics(),
        """
The Three Weeks (בין המצרים) from 17 Tammuz until Tisha B'Av commemorate the destruction of the Temple and Jewish tragedies.

Why we mourn:
• On 17 Tammuz the walls of Jerusalem were breached; on 9 Av the Temple was destroyed (both First and Second Temple, among other calamities).

Common practices (intensify in the Nine Days):
• No weddings, parties, or live music.
• No haircuts or shaving (unless for a mitzvah need — ask your rav).
• Avoid major purchases purely for joy; reduce entertainment.
• Increase Torah study about the Temple and ahavat Yisrael.

Ashkenaz: restrictions apply from 17 Tammuz. Many Sefardim intensify from Rosh Chodesh Av. Chabad follows accepted Chabad psak from 17 Tammuz with stricter Nine Days.

Shabbat during the Three Weeks: no mourning practices on Shabbat itself.
    """.trim(),
    ) + "\n\n" + nusachMourningLine(profile, "Three Weeks")

    fun nineDaysExplanation(profile: UserProfile): String = BeginnerHalachaGlossary.withKeyTerms(
        BeginnerHalachaGlossary.mourningBasics(),
        """
The Nine Days (from Rosh Chodesh Av until after Tisha B'Av) are the strictest part of summer mourning.

Practices (Ashkenaz — many Sefardim follow similar from Rosh Chodesh Av):
• No eating meat or drinking wine (except Shabbat or seudat mitzvah like a siyum — ask your rav).
• No swimming, laundry (except young children's needs per some poskim), and home improvements for pleasure.
• No cutting hair; avoid buying new clothes for pleasure.
• Reduce joyous activities; prepare spiritually for Tisha B'Av.

Erev Tisha B'Av (8 Av afternoon): stop learning Torah (except sad topics); eat the final meal (seudah hamafseket) before the fast. Tallit and tefillin are worn normally all day on Erev Tisha B'Av — the restriction applies to Tisha B'Av day itself.

Tisha B'Av (9 Av): full 25-hour fast, kinot. Morning Shacharit without tallit and tefillin; don them for Mincha after halachic chatzos (midday — use your zmanim app, not clock noon). Sit on low stools until chatzos on Tisha B'Av day itself (9 Av).

The 10th of Av extension: Even though the fast ends at nightfall on the 9th of Av, elements of mourning continue. Ashkenazi custom prohibits eating meat, drinking wine, doing laundry, listening to music, or bathing for pleasure until midday (chatzos) on the 10th of Av. Many Sephardim follow similar restrictions — confirm with your rav before celebrating with meat and wine immediately after the fast.

Shabbat Chazon (Shabbat before 9 Av): Shabbat is observed normally — meat and wine are permitted.

Nine Days Havdalah: When making Havdalah on Motzei Shabbat during the Nine Days, use wine or grape juice normally. Ashkenazi custom (Rema O.C. 551:10): a child (between ages 6–9) should drink the cup if present; if not, the person reciting Havdalah drinks it — the mitzvah of Havdalah overrides the custom of restraint.
    """.trim(),
    ) + "\n\n" + nusachMourningLine(profile, "Nine Days")

    private fun nusachMourningLine(profile: UserProfile, period: String): String =
        when (profile.effectiveNusach()) {
            EffectiveNusach.ASHKENAZ -> "Ashkenaz $period: follow your community calendar for music, haircuts, and laundry."
            EffectiveNusach.SEFARD ->
                "Sephardi $period: many follow Shulchan Arukh O.C. 493 — restrictions often until the morning of the 34th day of the Omer (Peninei Halakha 05-03-03). Follow your kehilla."
            EffectiveNusach.EDOT_HAMIZRACH ->
                "Edot HaMizrach $period: communities differ — some follow Shulchan Arukh until the 34th morning; many who follow the Ari are strict on haircuts until the day before Shavuot (Kaf HaChaim 493:13, cited in Peninei Halakha 05-03-03). Follow your rav."
            EffectiveNusach.CHABAD -> "Chabad $period: follow accepted Chabad psak and your rabbi."
        }

    fun kiddushLevanaExplanation(profile: UserProfile): String {
        val waitLine = when (profile.effectiveNusach()) {
            EffectiveNusach.SEFARD ->
                "Recited once a month when the moon is visible, usually beginning on the 7th night of the Hebrew month (Shulchan Arukh O.C. 426:4; Peninei Halakha 05-01-18)."
            EffectiveNusach.EDOT_HAMIZRACH ->
                "Recited once a month when the moon is visible. The majority of Sefardim wait until the 7th of the month (Shulchan Arukh O.C. 426:4). Moroccan and some other North African kehillot begin after 3 days (Peninei Halakha 05-01-18) — follow your community."
            else ->
                "Recited once a month when the moon is visible, usually beginning on the 3rd night of the Hebrew month (Ashkenaz / Chabad custom; Peninei Halakha 05-01-18)."
        }
        return BeginnerHalachaGlossary.withKeyTerms(
            BeginnerHalachaGlossary.daveningBasics(),
            """
Kiddush Levana (Sanctification of the New Moon) — Birkat HaLevanah.

$waitLine

Deadline: The window ends at the moment of the full moon (roughly 14 days, 18 hours, and 22 minutes from the molad — about 14.75 days into the month). Saying it on the night of the 15th may already be too late depending on the month. This app uses the Hebrew calendar day as a rough guide only — always check Sof Zman Kiddush Levana for your location before the month ends.

When:
• After nightfall (tzeit), standing outdoors under the open sky — not under a roof or porch ceiling.
• Ideally on Motzei Shabbat while still in your Shabbat clothes, when the moon is clearly visible.
• Not on Shabbat or Yom Tov itself — say it on a regular weeknight or Motzei Shabbat/Yom Tov.

How:
• You must be able to see the moon clearly; if it is completely blocked by clouds, do not begin the blessing — wait for a clear night within the window.
• Use your siddur; it is praise of G-d for creation's cycles — not worship of the moon.

This app tracks the monthly window for you — say it on the first clear night; do not delay past the ideal early window. Check Sof Zman Kiddush Levana for your location before the month ends.
            """.trim(),
        )
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
• Eat fruit — especially the seven species of Eretz Yisrael: wheat, barley, grapes, figs, pomegranates, olives, dates.
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
}
