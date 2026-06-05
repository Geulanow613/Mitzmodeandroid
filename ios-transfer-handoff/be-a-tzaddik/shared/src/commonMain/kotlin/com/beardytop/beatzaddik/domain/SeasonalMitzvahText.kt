package com.beardytop.beatzaddik.domain

/**
 * Full how-to explainers for seasonal checklist items (Sukkot, Shemini Atzeret, Chol HaMoed, etc.).
 */
object SeasonalMitzvahText {

    // ── Sukkot ───────────────────────────────────────────────────────────────

    fun sukkahBuildExplanation(): String = """
Building a sukkah (סֻכָּה) is a mitzvah — many begin right after Yom Kippur to show we move from teshuvah to simcha.

Basic requirements (Shulchan Arukh O.C. 633–635):
• Walls: At least two full walls plus part of a third (or three full walls). Walls can be wood, fabric on a frame, or existing walls — they must withstand normal wind.
• Size: Large enough to fit a table and for an adult to sit with head and most of body inside (roughly 7×7 handbreadths minimum; build larger for comfort).
• Schach (covering): Must be plant material that grew from the ground and is detached (tree branches, bamboo mats certified for schach, etc.) — not metal, plastic, or a solid roof.
• Shade: More shade than sun on the floor under the schach. You may see some sky through gaps — that is fine.
• Height: Walls at least ~3 ft (10 tefachim); schach can be high, but very high sukkot still need a valid structure below.

Practical steps:
1. Choose a spot — backyard, porch, balcony (check building rules). Avoid placing schach directly under a tree or house roof overhang that blocks the mitzvah.
2. Build the frame and walls first; lay schach last so it stays dry and valid.
3. Arrange schach loosely with gaps — not a solid rainproof roof (use a removable slatted cover on top only if rain is expected, per halachic guidance).
4. Set up table, chairs, and decorations (fruit, lights — electric on Yom Tov/Chol HaMoed per your rav).
5. Before the first night of Sukkot, eat at least a kezayit of bread in the sukkah with the bracha leishev basukkah (first night is Torah-level for men; women per minhag).

First night: Kiddush and Yom Tov meal in the sukkah. Men: leishev basukkah. Throughout Sukkot: eat bread and sleep in sukkah when possible (rain and illness have exemptions — ask your rabbi).
    """.trim()

    fun arbaMinimExplanation(profile: UserProfile): String {
        val nusachWave = when (profile.effectiveNusach()) {
            EffectiveNusach.CHABAD ->
                "Chabad: Hold lulav in right hand, etrog in left (stem/pitom down). Say blessing, then wave: three times forward, three right, three back, three left, three up, three down."
            EffectiveNusach.SEFARD ->
                "Sefard: Hold lulav and etrog together; wave forward, right, back, left, up, down (order per siddur). Hoshanot circuits on Hoshana Raba in many kehillot."
            EffectiveNusach.ASHKENAZ ->
                "Ashkenaz: Hold lulav in right, etrog in left (pitom down). Wave east, south, west, north, up, down — two waves per direction in many siddurim."
        }
        val daysNote = if (profile.isInIsrael) {
            "In Israel: lulav all 7 days; first day is primary Torah obligation for most."
        } else {
            "In the Diaspora: primary obligation on first day(s) of Yom Tov; continue on Chol HaMoed per custom."
        }
        return """
Arba Minim (ארבעה מינים) — the Four Species — are waved each day of Sukkot (except Shabbat).

The four:
• Lulav — closed palm branch (at least 3 tefachim)
• Etrog — citron, beautiful and mostly intact (pitom if present should be intact)
• Hadasim — three myrtle branches
• Aravot — two willow branches

How to observe:
• Assemble the lulav: bind lulav, hadassim, and aravot (per your hoshanah holder / koisan).
• Check kashrut before Yom Tov — especially etrog and that leaves are fresh enough.
• Bracha: "Al netilat lulav" — on the first day (and in Diaspora also on the second day of Yom Tov per minhag) before waving; other days many wave without a bracha (follow your siddur).
• When: Daytime; many do it before Shacharit at home or in shul. Not on Shabbat.
• $nusachWave

$daysNote

If a species is missing or invalid, ask your rabbi — there are limited substitutions in pressing cases.
        """.trim()
    }

    fun sheminiAtzeretExplanation(profile: UserProfile): String =
        if (profile.isInIsrael) {
            """
Shemini Atzeret / Simchat Torah (22 Tishrei in Israel) — one day combining both.

Yom Tov:
• Full Yom Tov — no melacha; festive meals with Kiddush and Shehecheyanu (if not said already).
• No sukkah in Israel on this day — we leave the sukkah and move the joy indoors.

Davening highlights:
• Tefillat Geshem — prayer for rain inserted in Musaf (we begin mentioning mashiv haruach umorid hagashem in Shmoneh Esrei from this point per minhag).
• Yizkor — memorial prayer in many Ashkenaz communities.
• Hakafot — dancing with Torah scrolls; finish the annual Torah reading and begin Bereshit again.
• Full Hallel and Musaf; Yom Tov Amidah.

Simchat Torah joy:
• Everyone receives an aliyah in many shuls; Kol HaNearim (children's aliyah) with flags.
• Singing, dancing — honor the day with Torah celebration.

Evening: Candle lighting and Yom Tov; morning services are long — plan accordingly.
            """.trim()
        } else {
            """
Shemini Atzeret (22 Tishrei in the Diaspora) — eighth day of Sukkot.

Yom Tov:
• Full Yom Tov — no melacha; Kiddush and festive meals.
• Sukkah: In the Diaspora, many still eat in the sukkah on Shemini Atzeret (with leishev basukkah without bracha per Ashkenaz custom) — confirm with your rabbi.

Davening:
• Tefillat Geshem in Musaf — begin the winter insert for rain (mashiv haruach) per community calendar.
• Yizkor — Ashkenaz communities recite memorial prayers.
• Hallel, Musaf, Yom Tov Amidah.

Simchat Torah is **tomorrow** (23 Tishrei) in the Diaspora — see that day's checklist item for hakafot and Torah joy.

Tonight: light Yom Tov candles; no lulav on Shemini Atzeret.
            """.trim()
        }

    fun simchatTorahExplanation(profile: UserProfile): String = """
Simchat Torah (23 Tishrei in the Diaspora) — rejoicing with the Torah.

Yom Tov:
• Full Yom Tov — no melacha; Kiddush, festive meals, joy.
• Sukkah: Most Ashkenazim do not eat in the sukkah on Simchat Torah (it is no longer Sukkot) — Sefardic practice varies; follow your rav.

The mitzvah of the day — simcha and Torah:
• Hakafot — circling the bimah with Torah scrolls, singing and dancing (often many circuits).
• Complete the last parsha of Devarim and begin Bereshit — the Torah never ends.
• **Aliyot** — Kol HaNearim (all children together), Chatan Torah, Chatan Bereshit, and honors for members.
• Many shuls extend Hakafot to the evening or multiple sessions.

Practical tips:
• Dress festively; arrive early for a good spot.
• If you have a Torah honor, prepare your brachot.
• Celebrate responsibly — the day is about love of Torah, not excess.

Candle lighting last night of Yom Tov; havdalah when Yom Tov ends (often with Sukkot/Yom Tov additions in havdalah — use your siddur).
    """.trim()

    // ── Chol HaMoed ────────────────────────────────────────────────────────────

    fun cholHamoedHonorExplanation(): String = """
Chol HaMoed (חול המועד) — the intermediate festival days — is not full Yom Tov, but it is not ordinary weekdays either.

Spirit of the day:
• Simchat moed — joy of the festival; nicer meals, family time, Torah learning.
• Melacha — many labors are restricted (not as strict as Yom Tov). Work needed to avoid financial loss may be permitted — ask your rabbi before assuming.
• Ochel nefesh — food preparation is permitted.

What to do:
• Daven with Yaaleh V'yavo and festive additions; Half Hallel on Pesach Chol HaMoed (after first days).
• Avoid treating it like a regular workday — schedule outings, learning, and visits that fit the moed.
• On Pesach Chol HaMoed: no chametz; matzah and KP food only.
• On Sukkot Chol HaMoed: lulav (not on Shabbat), meals in sukkah when applicable.

Avoid (without halachic guidance): heavy laundry, major home projects, and activities that diminish the festival atmosphere.
    """.trim()

    fun cholHamoedClothesExplanation(): String = """
Wearing nicer clothing on Chol HaMoed honors the festival (kavod ha'moed).

Practice:
• Wear clothes you would not wear for dirty chores — clean, pressed, or festive attire.
• Some avoid new clothing that would require a Shehecheyanu on Chol HaMoed (ask your rav).
• Men: many wear a hat and jacket for shul even if weekday dress is casual.

This applies each day of Chol HaMoed — it is a simple daily way to mark the moed apart from ordinary weekdays.
    """.trim()

    fun cholHamoedMatzahExplanation(): String = """
Eating matzah on Pesach after the Seder nights is a mitzvah many observe, though not with the same strict obligation as the first Seder night.

Levels:
• First Seder night: Torah obligation (k'zayit) for men; women per minhag.
• **Rest of Pesach including Chol HaMoed:** Rabbinic/optional mitzvah to eat matzah — especially at Yom Tov and Shabbat meals.

How:
• Use shmurah matzah if that is your custom for mitzvah eating.
• A kezayit at the meal is sufficient; you need not eat matzah at every meal if it is difficult.
• No chametz or kitniyot (per your custom) the entire Pesach.
    """.trim()

    // ── Rosh Chodesh, Chanukah, Pesach week, Selichot, mourning, Tu B'Shvat ───

    fun roshChodeshExplanation(): String = """
Rosh Chodesh (ראש חודש) — the new Hebrew month — is a minor festival.

Davening:
• Yaaleh V'yavo in Shmoneh Esrei (all Amidahs of the day) and in Bentching.
• Hallel — partial Hallel on Rosh Chodesh (per Ashkenaz/Sefard siddur).
• Musaf — special Rosh Chodesh Musaf with korban musaf paragraph.
• Tachanun is omitted.

Customs:
• Some eat a festive meal; many women have a custom not to do melacha they avoid on Rosh Chodesh (heavy sewing, etc.) — per family minhag.
• Work is permitted; it is not Yom Tov.

If you forget Yaaleh V'yavo: rules differ for Rosh Chodesh vs Yom Tov — at Maariv on RC, Ashkenaz often does not repeat; other omissions may require repeating — ask your rabbi.
    """.trim()

    fun chanukahLightingExplanation(day: Int, profile: UserProfile): String {
        val nusachLight = when (profile.effectiveNusach()) {
            EffectiveNusach.CHABAD -> "Chabad: light the shamash, then candles right-to-left (newest candle first each night)."
            EffectiveNusach.SEFARD -> "Sefard: light left-to-right (newest first) per many communities."
            EffectiveNusach.ASHKENAZ -> "Ashkenaz: light right-to-left — newest candle on the right each night."
        }
        return """
Chanukah night $day of 8 — lighting the menorah.

When:
• Light after tzeit (nightfall) — not before sunset. On Friday, light Chanukah candles **before** Shabbat candles. On Motzei Shabbat, light Chanukah before or after Havdalah per minhag.

How:
• Pirsumei nisa — publicize the miracle; place menorah by a window or doorway where passersby can see.
• $nusachLight
• Oil or candles must burn at least 30 minutes after nightfall.
• Brachot (first night all three; other nights two): lehadlik ner shel Chanukah, she'asa nissim; Shehecheyanu on first night.
• Do not use the Chanukah lights for work — shamash is for utility light.

${if (day == 1) "First night: say all three brachot including Shehecheyanu." else "Tonight: two brachot (no Shehecheyanu unless first time lighting this year)."}

After lighting: sing HaNeiros halalu and Maoz Tzur (custom).
        """.trim()
    }

    fun pesachWeekPrepExplanation(): String = """
The week before Pesach (Nissan 8–13) is for practical preparation — not spring cleaning every closet, but removing chametz where it matters.

Focus areas:
• **Kitchen & dining** — where chametz is eaten: counters, stove, microwave, toaster, fridge, pantry.
• **Cars, offices, bags, coat pockets** — crumbs and snack wrappers.
• **Sell or use up** chametz you will not keep; plan Pesach menus and shopping.
• **Pesach dishes** — unpack or acquire; label milchig/fleishig if needed.
• **Haggadahs, matzah, wine, seder plate** — order before prices rise and stores sell out.

Halachic mindset:
• You need not make the home sterile — remove edible chametz and clean places where chametz was brought.
• Mechirat chametz and bedikat/biur happen on Erev Pesach — this week sets you up to succeed.

Children & family: assign zones, check school bags, plan chametz finish-up meals before Pesach.
    """.trim()

    fun selichotExplanation(nusach: EffectiveNusach): String = when (nusach) {
        EffectiveNusach.SEFARD -> """
Selichot (סליחות) — penitential prayers — begin in Elul for Sefardim.

How:
• Said at night (often after chatzos layla / before dawn) or early morning per community.
• Best with a minyan; follow your kehilla's schedule from Rosh Chodesh Elul through Yom Kippur.
• Includes the 13 Attributes of Mercy (Hashem Hashem) with congregation responses.
• Prepare by checking local synagogue calendar — times shift through Elul.
        """.trim()
        EffectiveNusach.CHABAD -> """
Selichot in Chabad custom begin from Rosh Chodesh Elul (some communities start earlier).

How:
• Follow your local Chabad minyan — often late night or before Shacharit.
• Use the Chabad Selichot nusach (Nusach Ari).
• Increase in Torah, teshuvah, and tzedakah through the month of Elul.
        """.trim()
        EffectiveNusach.ASHKENAZ -> """
Ashkenaz Selichot begin the Saturday night before Rosh Hashana (with at least four days of Selichot before RH).

How:
• First night is often a large community gathering (Motzei Shabbat).
• Said standing; many wear tallit if already worn for Maariv or at morning Selichot.
• Arrive early — services are long and moving.
• Continue daily until Yom Kippur (including Erev Rosh Hashana, fast day schedules differ).
• Check your shul for exact times (usually late night or early morning).
        """.trim()
    }

    fun threeWeeksExplanation(profile: UserProfile): String = """
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
    """.trim() + "\n\n" + nusachMourningLine(profile, "Three Weeks")

    fun nineDaysExplanation(profile: UserProfile): String = """
The Nine Days (from Rosh Chodesh Av until after Tisha B'Av) are the strictest part of summer mourning.

Practices (Ashkenaz — many Sefardim follow similar from Rosh Chodesh Av):
• No eating meat or drinking wine (except Shabbat or seudat mitzvah like a siyum — ask your rav).
• No swimming, laundry (except young children's needs per some poskim), and home improvements for pleasure.
• No cutting hair; avoid buying new clothes for pleasure.
• Reduce joyous activities; prepare spiritually for Tisha B'Av.

Erev Tisha B'Av (8 Av afternoon): stop learning Torah (except sad topics), no tallit tefillin after chatzos, final meal before the fast.

Tisha B'Av: full 25-hour fast, kinot, sitting on low stools until chatzos on 10 Av.

Shabbat Chazon (Shabbat before 9 Av): Shabbat is observed normally — meat and wine are permitted.
    """.trim() + "\n\n" + nusachMourningLine(profile, "Nine Days")

    private fun nusachMourningLine(profile: UserProfile, period: String): String =
        when (profile.effectiveNusach()) {
            EffectiveNusach.ASHKENAZ -> "Ashkenaz $period: follow your community calendar for music, haircuts, and laundry."
            EffectiveNusach.SEFARD -> "Sefard $period: timing may start Rosh Chodesh Av — follow your kehilla."
            EffectiveNusach.CHABAD -> "Chabad $period: follow accepted Chabad psak and your rabbi."
        }

    fun tuBshvatExplanation(): String = """
Tu B'Shvat (15 Shevat) — New Year for Trees — is a day to appreciate Hashem's fruit and Land of Israel.

Customs:
• Eat fruit — especially the seven species of Eretz Yisrael: wheat, barley, grapes, figs, pomegranates, olives, dates.
• Say brachot and after-brachot carefully; consider Shehecheyanu on new fruits you have not eaten this season.
• Some hold a Tu B'Shvat Seder with four cups of wine (white to red) and themed fruit — follow a guide if hosting.
• No fasting; work is permitted; it is not Yom Tov.

Spiritual focus: gratitude for creation, connection to Eretz Yisrael, and growth (trees blossom in Israel around this season).
    """.trim()

    // ── Links ─────────────────────────────────────────────────────────────────

    fun sukkahLinks(profile: UserProfile) = buildList {
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(ChecklistLink("Chabad — Build a sukkah", "https://www.chabad.org/library/article_cdo/aid/420822/jewish/How-to-Build-a-Sukkah.htm", "chabad"))
            add(ChecklistLink("Chabad — Dwelling in the sukkah", "https://www.chabad.org/library/article_cdo/aid/420823/jewish/Eating-in-the-Sukkah.htm", "chabad"))
        }
        add(ChecklistLink("Peninei Halacha — Sukkah", "https://ph.yhb.org.il/en/category/moadim/13-sukkot/", "default"))
        add(ChecklistLink("Aish — Sukkot", "https://aish.com/holidays/sukkot/", "default"))
        add(ChecklistLink("Ohr Somayach — Sukkot", "https://ohr.edu/yhiy/sukkot/", "default"))
    }

    fun arbaMinimLinks(profile: UserProfile) = buildList {
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(ChecklistLink("Chabad — The four species", "https://www.chabad.org/library/article_cdo/aid/420824/jewish/The-Four-Species.htm", "chabad"))
        }
        add(ChecklistLink("Peninei Halacha — Lulav", "https://ph.yhb.org.il/en/13-05-00/", "default"))
        add(ChecklistLink("Aish — Lulav & Etrog", "https://aish.com/holidays/sukkot/", "default"))
    }

    fun sheminiAtzeretLinks(profile: UserProfile) = buildList {
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(ChecklistLink("Chabad — Shemini Atzeret", "https://www.chabad.org/holidays/sukkot/article_cdo/aid/1771/jewish/Shemini-Atzeret.htm", "chabad"))
        }
        add(ChecklistLink("Peninei Halacha — Festivals", "https://ph.yhb.org.il/en/13-08-00/", "default"))
        add(ChecklistLink("Aish — Shemini Atzeret", "https://aish.com/holidays/sukkot/", "default"))
    }

    fun simchatTorahLinks(profile: UserProfile) = buildList {
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(ChecklistLink("Chabad — Simchat Torah", "https://www.chabad.org/holidays/sukkot/article_cdo/aid/1772/jewish/Simchat-Torah.htm", "chabad"))
        }
        add(ChecklistLink("Peninei Halacha — Simchat Torah", "https://ph.yhb.org.il/en/13-09-00/", "default"))
        add(ChecklistLink("Aish — Simchat Torah", "https://aish.com/holidays/sukkot/", "default"))
        add(ChecklistLink("Ohr Somayach — Simchat Torah", "https://ohr.edu/yhiy/simchat-torah/", "default"))
    }

    fun cholHamoedLinks(profile: UserProfile) = buildList {
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(ChecklistLink("Chabad — Chol HaMoed", "https://www.chabad.org/library/article_cdo/aid/5279/jewish/Chol-Hamoed.htm", "chabad"))
        }
        add(ChecklistLink("Peninei Halacha — Chol HaMoed", "https://ph.yhb.org.il/en/04-09-00/", "default"))
        add(ChecklistLink("Aish — Chol HaMoed", "https://aish.com/holidays/pesach/", "default"))
    }

    fun roshChodeshLinks(profile: UserProfile) = buildList {
        add(ChecklistLink("Chabad — Rosh Chodesh", "https://www.chabad.org/library/article_cdo/aid/4770/jewish/Rosh-Chodesh.htm", "chabad"))
        add(ChecklistLink("Peninei Halacha — Rosh Chodesh", "https://ph.yhb.org.il/en/05-01-00/", "default"))
        add(ChecklistLink("Aish — Rosh Chodesh", "https://aish.com/roshchodesh/", "default"))
    }

    fun chanukahDayLinks(profile: UserProfile) = buildList {
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(ChecklistLink("Chabad — How to light", "https://www.chabad.org/holidays/chanukah/article_cdo/aid/103868/jewish/How-to-Light-Chanukah-Candles.htm", "chabad"))
        }
        add(ChecklistLink("Peninei Halacha — Chanukah", "https://ph.yhb.org.il/en/12-00-00/", "default"))
        add(ChecklistLink("Ohr Somayach — Chanukah", "https://ohr.edu/1304", "default"))
        add(ChecklistLink("Aish — Chanukah", "https://aish.com/holidays/chanukah/", "default"))
    }

    fun pesachWeekLinks(profile: UserProfile) = pesachPrepLinksShared(profile)

    fun pesachPrepLinksShared(profile: UserProfile) = buildList {
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(ChecklistLink("Chabad — Passover prep", "https://www.chabad.org/holidays/passover/default_cdo/jewish/Passover.htm", "chabad"))
        }
        add(ChecklistLink("Peninei Halacha — Pesach", "https://ph.yhb.org.il/en/category/moadim/04-pesach/", "default"))
        add(ChecklistLink("Aish — Passover", "https://aish.com/holidays/pesach/", "default"))
    }

    fun tuBshvatLinks() = listOf(
        ChecklistLink("Chabad — Tu B'Shvat", "https://www.chabad.org/holidays/jewishnewyear/article_cdo/aid/1185/jewish/Tu-BShvat.htm", "chabad"),
        ChecklistLink("Aish — Tu B'Shvat", "https://aish.com/48965616/", "default"),
        ChecklistLink("Peninei Halacha — Tu B'Shvat", "https://ph.yhb.org.il/en/06-11-00/", "default")
    )
}
