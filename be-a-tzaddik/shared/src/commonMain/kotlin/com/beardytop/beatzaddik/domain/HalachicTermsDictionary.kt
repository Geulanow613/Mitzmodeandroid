package com.beardytop.beatzaddik.domain

/**
 * Central glossary for gold-underlined halachic terms in explanatory text.
 * Longest label wins when multiple terms could match the same span.
 */
object HalachicTermsDictionary {

    private const val TERM_TAG = "halachic_term"

    private fun line(text: String, vararg aliases: String): HalachicTerm? {
        val sep = text.indexOf(" — ")
        if (sep < 0) return null
        val titlePart = text.substring(0, sep).trim().removePrefix("•").trim()
        val def = text.substring(sep + 3).trim()
        val labels = buildList {
            titlePart.split(Regex("""\s*/\s*""")).forEach { add(it.trim()) }
            addAll(aliases)
        }.filter { it.isNotBlank() }.distinctBy { it.lowercase() }
        if (labels.isEmpty()) return null
        val title = labels.first()
        return HalachicTerm(
            id = title.lowercase().replace(Regex("[^a-z0-9']+"), "_").trim('_'),
            title = title,
            definition = def,
            matchLabels = labels,
        )
    }

    private val fromBeginnerGlossary: List<HalachicTerm> = listOf(
        BeginnerHalachaGlossary.MELACHA,
        BeginnerHalachaGlossary.YOM_TOV,
        BeginnerHalachaGlossary.SHABBAT,
        BeginnerHalachaGlossary.EREV,
        BeginnerHalachaGlossary.CHAG,
        BeginnerHalachaGlossary.TZEIT,
        BeginnerHalachaGlossary.RAV,
        BeginnerHalachaGlossary.RABBI,
        BeginnerHalachaGlossary.MINHAG,
        BeginnerHalachaGlossary.BRACHA,
        BeginnerHalachaGlossary.KIDDUSH,
        BeginnerHalachaGlossary.HAVDALAH,
        BeginnerHalachaGlossary.YAKNEHAZ,
        BeginnerHalachaGlossary.ERUV_TAVSHILIN,
        BeginnerHalachaGlossary.BLECH,
        BeginnerHalachaGlossary.SHUL,
        BeginnerHalachaGlossary.DAVEN,
        BeginnerHalachaGlossary.AMIDAH,
        BeginnerHalachaGlossary.MUSAF,
        BeginnerHalachaGlossary.HALLEL,
        BeginnerHalachaGlossary.YAALEH_VYAVO,
        BeginnerHalachaGlossary.BENTCHING,
        BeginnerHalachaGlossary.TACHANUN,
        BeginnerHalachaGlossary.SHEHECHEYANU,
        BeginnerHalachaGlossary.CHAMETZ,
        BeginnerHalachaGlossary.BEDIKAT,
        BeginnerHalachaGlossary.BIUR,
        BeginnerHalachaGlossary.MECHIRAT,
        BeginnerHalachaGlossary.KOL_CHAMIRA,
        BeginnerHalachaGlossary.SIYUM,
        BeginnerHalachaGlossary.SEDER,
        BeginnerHalachaGlossary.KITNIYOT,
        BeginnerHalachaGlossary.CHOL_HAMOED,
        BeginnerHalachaGlossary.REVIIT,
        BeginnerHalachaGlossary.KEZAYIT,
        BeginnerHalachaGlossary.SCHACH,
        BeginnerHalachaGlossary.LULAV_SET,
        BeginnerHalachaGlossary.MEGILLAH,
        BeginnerHalachaGlossary.MISHLOACH,
        BeginnerHalachaGlossary.MATANOT,
        BeginnerHalachaGlossary.SEUDAH,
        BeginnerHalachaGlossary.OMER,
        BeginnerHalachaGlossary.SELICHOT,
        BeginnerHalachaGlossary.PIRSUMEI_NISA,
        BeginnerHalachaGlossary.OCHEL_NEFESH,
        BeginnerHalachaGlossary.CHAMAR_MEDINA,
        "Simchat Yom Tov — rejoicing on the festival (wine, food, and enjoyment)",
    ).mapNotNull { line(it) }

    private val supplemental: List<HalachicTerm> = listOf(
        line("Taanit Bechorot — Fast of the Firstborn on Erev Pesach (many break or nullify the fast with the seudat mitzvah after a siyum)"),
        line("Taanit Esther — fast day before Purim"),
        line("menorah — Chanukah candelabra; candles are lit each night"),
        line("shmurah matzah — matzah guarded from moisture from harvest; required by many for the Seder"),
        line("Simchat Torah — rejoicing with the Torah; in Israel celebrated on the biblical day of Shemini Atzeret; second day in the Diaspora"),
        line("Shemini Atzeret — eighth day festival after Sukkot; biblically Shemini Atzeret — Simchat Torah celebrations fold into it in Israel"),
        line("melachot — the 39 categories of transformative labor forbidden on Shabbat"),
        line("tefillah — prayer; from a root meaning to judge oneself"),
        line("Kinot — elegies read on Tisha B'Av mourning the Temple"),
        line("Tisha B'Av — fast mourning the destruction of the Temple"),
        line("Lag BaOmer — 33rd day of the Omer; mourning eased in many communities"),
        line("Hoshana Raba — seventh day of Sukkot; Minhag Nevi'im — beating aravot five times on the ground"),
        line("Hakafot — dancing while circling around the synagogue with Torah scrolls on Simchat Torah"),
        line("Hagbah — lifting the Torah scroll for the congregation to see; Ashkenazim after reading, Sephardim before"),
        line("Geshem — Rain. Can refer to the prayer for rain inserted on Shemini Atzeret through the winter"),
        line("etrog — citron, one of the Four Species on Sukkot"),
        line("lulav — palm branch waved with the etrog, willows, and myrtle branches on Sukkot"),
        line("hadas — myrtle branch in the Four Species"),
        line("aravah — willow branches used in the Four Species"),
        line("Grace After Meals — Birkat Hamazon after eating bread meals"),
        line("Psalms — Tehillim"),
        line("kehilla — Jewish community or congregation"),
        line("bitul — nullification (e.g. Kol Chamira nullifying chametz)"),
        line("egg matzah — matzah made with eggs and/or fruit juice (matzah ashira); not lechem oni and cannot be used at the Seder. Ashkenazim do not eat it on Pesach except for sick, elderly, or young children (Rema OC 462:4); Sephardim eat it throughout Pesach"),
        line("Kashering — making utensils kosher; kebolo kach polto — hag'alah, libun, or separate Pesach sets per rav"),
        line("seudat mitzvah — festive meal tied to a mitzvah (e.g. brit, siyum, wedding)"),
        line("three stars — colloquial marker for halachic nightfall (tzeit); actual timing varies widely by community and location"),
        line("Havdalah in Kiddush — Yaknehaz order when Shabbat leads into Yom Tov"),
        line("Purim day — 14 Adar (15 in walled cities); Megillah, gifts to friends/poor, and feast"),
        line("Megillah reading — hearing the Book of Esther read on Purim"),
        line("Fast of Gedaliah — fast day after Rosh Hashana"),
        line("Asara B'Tevet — fast on 10 Tevet"),
        line("Ta'anit — a public or personal fast day", "Taanit"),
        line("Shemoneh Esrei — the eighteen (now nineteen) blessings of the Amidah"),
        line("Sefirah — counting period between 2nd day of Pesach and Shavuot; mourning customs and when they end vary by minhag (Ashkenaz, Sephard, Chabad, etc.)", "the Omer"),
        line("Chol HaMoed Sukkot — intermediate Sukkot days with lulav"),
        line("Chol HaMoed Pesach — intermediate days of Pesach"),
        line("Eruv chatzerot — symbolic merging of courtyards allowing carrying on Shabbat"),
        line("Eruv techumin — extends the Shabbat walking boundary in special cases"),
        line("Pesachdik — kosher for Passover standards"),
        line("chametz she'avar — chametz that was in your possession over Pesach (forbidden to benefit from after)"),
        line("matana al menat lehachzir — gift on condition it is returned; commonly used for borrowing a lulav on the first day of Sukkot"),
        line("Gemara — Talmudic commentary and discussion on the Mishnah"),
        line("Shabbat candles — candles lit before sunset to welcome Shabbat"),
        line("nusach Ashkenaz — Ashkenazi prayer wording"),
        line("nusach Sefard — Ashkenazi-Chasidic prayer rite (not the same as Sephardic Nusach Edot HaMizrach)"),
        line("Half Hallel — shortened Hallel psalms on Rosh Chodesh and part of Pesach"),
        line("Full Hallel — complete Hallel psalms on major festivals"),
        line("Davening — praying; any prayer, not only the formal services"),
        line("Chag — festival (everyday term for Yom Tov)"),
        line("Erev — the day before a holy day (Erev Shabbat, Erev Pesach, etc.)", "Erev Shabbat", "Erev Pesach"),
        line("Seudah — festive meal"),
        line("kisui rosh — hair covering for married women in many communities"),
        line("tzniut — modest dress and conduct", "tznius"),
        line("refuah shleimah — complete healing; said when praying for someone ill"),
        line("Borei Minei Besamim — blessing on fragrant herbs, spices, or flowers (especially the spice blessing in Havdalah)"),
        line("Gam zu l'tovah — this too is for the good; trust that hardship can be for a purpose"),
        line("Nachum Ish Gamzu — sage known for finding good in every situation"),
        line("honoring your parents — mitzvah to respect and care for parents"),
        line("loving your fellow Jew — central Torah principle taught by Rabbi Akiva"),
        line("walk in G-d's ways — imitating divine kindness (clothing the needy, visiting the sick)"),
        line("halachot — laws or legal details (plural of halacha)"),
        line("yichud — prohibition against seclusion of a man and woman in private unless they are married or immediate blood relatives"),
        line("hechsher — certification that food was produced under reliable kosher supervision"),
        line("fleishig — meat status; dairy may not be eaten for a waiting period afterward"),
        line("milchig — dairy status; separate utensils and waiting before meat in many homes"),
        line("pareve — neutral foods (neither meat nor dairy) such as fish, eggs, and produce"),
        line("gematria — numerical value of Hebrew letters used for Torah insights (not fortune-telling)"),
        line("Shabbat Shalom — peaceful Sabbath greeting"),
        line("Shalom bayit — peace in the home; a central Jewish value"),
        line("tzedakah box — charity box; collecting coins for those in need is itself a mitzvah"),
        line("Parshat — weekly Torah portion read in synagogue", "parsha"),
        line("Birkat Kohanim — priestly blessing; Sephardim daily worldwide; Diaspora Ashkenazim on festivals"),
        line("Birkat HaMazon — Grace After Meals after eating bread"),
        line("Shmoneh Esrei — the nineteen blessings of the standing Amidah prayer"),
        line("Krias Shema — recitation of the Shema and its blessings"),
        line("siyum — completion of a Torah text, often with a festive meal"),
        line("hakarat hatov — recognizing and thanking someone for a kindness"),
        line("mesorah — tradition faithfully transmitted generation to generation"),
        line("chatzos — halachic midnight or midday depending on context"),
        line("candle lighting — lighting Shabbat or Yom Tov candles before sunset"),
        line("borei me'orei ha'eish — blessing over fire in Yaknehaz and regular Havdalah"),
        line("Zachor — \"remember Shabbat\" from the Exodus version of the Ten Commandments"),
        line("Shamor — \"guard Shabbat\" from the Deuteronomy version of the Ten Commandments"),
        line("Hamapil — blessing entrusting the soul to G-d before sleep, part of bedtime Shema"),
        line("HaNeiros halalu — paragraph sung after lighting Chanukah candles"),
        line("Maoz Tzur — \"Rock of Ages,\" Chanukah hymn sung after lighting"),
        line("Matan Torah — receiving the Torah at Mount Sinai, celebrated on Shavuot"),
        line("shochet — trained kosher slaughterer"),
        line("melicha — salting meat to remove blood after shechita"),
        line("yirat Shamayim — awe of Heaven; living with awareness that G-d is present"),
        line("kippah — skullcap worn as a sign of G-d's presence above", "yarmulke"),
        line("tallit katan — small four-cornered garment with tzitzit worn under clothing"),
        line("tallit gadol — large prayer shawl with tzitzit worn during Shacharit"),
        line("Al Netilat Yadayim — blessing on ritual handwashing"),
        line("shmirat halashon — guarding one's speech from lashon hara and harmful words"),
        line("melaveh malkah — meal escorting the Shabbat queen — like saying goodbye to Shabbat at its departure (Motzei Shabbat through dawn)"),
        line("Nerot — candles, especially Shabbat and Yom Tov lights"),
        line("Shomer Daltot Yisrael — Guardian of the doors of Israel (acronym Shin-Dalet-Yod on mezuzah)"),
        line("beged — garment; tzitzit apply to four-cornered beged"),
        line("tevilah — ritual immersion in a mikveh"),
        line("Eretz Yisrael — the Land of Israel"),
        line("Barchu — call to bless G-d that opens communal prayer with a minyan"),
        line("Aseret Yemei Teshuvah — Ten Days of Repentance from Rosh Hashana to Yom Kippur"),
        line("gabbai — synagogue officer who coordinates aliyot and services"),
        line("kohen — priest descended from Aaron; special laws and blessings"),
        line("assur bemelacha — forbidden to do melacha on Shabbat or Yom Tov"),
        line("karmelit — rabbinic semi-public area with carrying restrictions"),
        line("shevarim — broken shofar notes"),
        line("teruah — staccato shofar notes"),
        line("kneidlach — matzah balls (gebrochts communities may avoid on Pesach)"),
        line("shinui — unusual change of method that sometimes permits melacha leniency"),
        line("Mavir — creating a new flame; forbidden on Shabbat and Yom Tov; transferring an existing flame permitted on Yom Tov for ochel nefesh"),
        line("17 Tammuz — fast marking breaching of Jerusalem's walls"),
        line("ketubah — marriage contract outlining husband's obligations"),
        line("Shabbos — Yiddish for Shabbat"),
        line("ruach — wind or spirit; middle soul level in some teachings"),
        line("Modah — feminine form of Modeh (grateful) in Modeh Ani"),
        line("Sefardim — Jews following Sephardi rites and customs"),
        line("Chasidim — followers of the Baal Shem Tov's spiritual path"),
        line("gemilut chasadim — acts of loving-kindness beyond obligation"),
        line("Zorea — planting melacha"),
        line("Choresh — plowing melacha"),
        line("Gozez — shearing or cutting hair/nails melacha"),
        line("Melaben — laundering melacha"),
        line("Losh — kneading melacha"),
        line("Tochen — grinding melacha"),
        line("Boneh — building melacha"),
        line("Soter — demolishing melacha"),
        line("Mechabeh — extinguishing fire melacha"),
        line("Makeh B'patish — final finishing touch melacha"),
    ).mapNotNull { it }

    /** Richer definitions that always win over shorter duplicate entries. */
    private val enrichedOverrides: List<HalachicTerm> = listOf(
        line(
            "mitzvah — At its core, a mitzvah is a command from Hashem that acts as a direct connection point between you and Him. The word itself is related to a term for \"binding together,\" meaning every time you do a mitzvah, you are plugging yourself directly into the Divine. It covers the big stuff like Torah obligations and formal prayer, but it also includes the everyday moments—like making a blessing over a glass of wine or just having a candid, heart-to-heart talk with Hashem in your own words. Ultimately, a mitzvah is about taking an ordinary, physical moment and turning it into a real relationship with your Creator.",
            "mitzvot",
            "Mitzvah",
            "Mitzvot",
        ),
        line(
            "Minhag — A minhag is a binding community or family custom rooted in Jewish life — not merely \"what people happen to do.\" Minhag can shape prayer text (nusach), mourning practices, Pesach stringencies, and holiday joy. When a minhag is established with rabbinic approval in a community, it often carries nearly the weight of law for that community. If you are new or between communities, ask your rav which minhagim to follow rather than mixing incompatible practices.",
            "minhag",
            "minhagim",
        ),
        line(
            "nusach — Nusach is the prayer \"style\" of a community: which words are said, in what order, and often which melodies are used. Common examples include Nusach Ashkenaz, Nusach Edot HaMizrach (Sephardic), Nusach Sefard (Ashkenazi-Chasidic), and Nusach Ari (Chabad).",
            "Nusach",
        ),
        line(
            "Bracha — A bracha is a structured blessing, usually opening with \"Baruch Atah Hashem…\" It is how we acknowledge G-d as the Source before eating, before mitzvot, and at holy moments. Different foods and mitzvot have specific brachot; saying the wrong one or none when required is a halachic issue. Brachot also train gratitude — pausing to recognize that what we enjoy comes from Above.",
            "bracha",
            "brachot",
            "beracha",
        ),
        line(
            "halacha — Halacha means \"the way of walking\" — the practical path of Jewish life drawn from Torah, Prophets, Writings, Mishnah, Talmud, and codes like the Shulchan Aruch. It covers prayer, Shabbat, diet, family life, business ethics, and festivals. Halacha is decided by qualified poskim; when you are unsure, the mitzvah is to ask your rav rather than guess.",
            "Halacha",
            "halachot",
        ),
        line(
            "teshuvah — Teshuvah means \"return\" — coming back to G-d and to your best self after a mistake. The Rambam teaches that sincere teshuvah includes stopping the sin, regretting it, confessing to G-d, and resolving not to repeat it — with a plan for the future. Yom Kippur is the apex of teshuvah season, but repentance is available every day and can transform past actions into merit.",
            "Teshuvah",
            "teshuva",
        ),
        line(
            "lashon hara — Lashon hara is speaking negatively about another Jew even when the information is true and even in casual conversation. The Torah compares it to murder because it destroys reputations, friendships, and community trust. The Chofetz Chaim systematized its laws: many situations permit speech for protection or constructive need, but gossip for entertainment is never permitted. Guarding speech is one of the fastest ways to grow in holiness.",
            "Lashon hara",
            "lashon hara",
        ),
        line(
            "Tehillim — Tehillim (Psalms) is the prayer-book of the Jewish heart. King David and other authors composed these verses for every mood — joy, fear, gratitude, and exile. Communities recite Tehillim for the sick, for protection, and for comfort. You do not need to understand every word to begin; even one chapter read with sincerity connects you to generations of Jews who poured out their souls in these lines.",
            "tehillim",
            "Psalms",
        ),
        line(
            "Olam HaBa — Olam HaBa is the World to Come — the eternal reality of closeness to G-d after this life (Heaven). Mitzvot, Torah study, and kindness build merits that the sages describe as unfathomable treasures in that world.",
            "olam haba",
            "Olam Haba",
        ),
        line(
            "Mashiach — Mashiach (Moshiach) is the righteous anointed king from David's line who will rebuild the Temple, gather the exiles, and bring universal knowledge of G-d. Belief in his coming is a foundation of Jewish faith. We pray for redemption daily.",
            "Moshiach",
            "moshiach",
        ),
        line(
            "Emunah — Emunah is faith that G-d exists, is one, and guides history and your personal life with purpose. It is not blind denial of difficulty — many psalms and stories show righteous people struggling — but a commitment that there is meaning even when we cannot see it. Strengthening emunah often comes through Torah study, mitzvot, and reflecting on past kindnesses you received.",
            "emunah",
        ),
        line(
            "Kiddush Hashem — Kiddush Hashem means sanctifying G-d's Name — living and speaking in a way that makes observers respect Torah and G-d. Examples include returning lost objects, honest dealings, and gracious behavior in public as a visibly Jewish person. Its opposite, chilul Hashem, is among the most serious sins because it pushes people away from holiness.",
            "kiddush hashem",
        ),
        line(
            "minyan — A minyan is ten Jewish men above bar mitzvah age (13) forming a quorum for public prayer. Certain prayers — including Kaddish, Barchu, and repetition of the Amidah — require a minyan. The Talmud stresses that communal prayer is especially beloved by G-d; joining a minyan is considered the ideal way of prayer.",
            "Minyan",
        ),
        line(
            "Amen — Amen seals a bracha someone else recited, affirming \"so may it be.\" The Talmud praises one who answers Amen even more than the one who made the blessing, when answered with focus. It should not be rushed or mumbled; it is a small but constant mitzvah opportunity in synagogue and at home. Literally it's a acronym for G-d, King who is faithful)",
        ),
        line(
            "Chazal — Chazal (חז\"ל) is an acronym for \"our Sages of blessed memory\" — the rabbis of the Mishnah, Talmud, and midrash who transmitted halacha and values. When a source says \"Chazal teach,\" it refers to this collective tradition rather than a single author.",
            "CHAZAL",
        ),
        line(
            "challah — Challah is the bread often prepared for Shabbat and festival meals — often braided and covered until Kiddush. Two loaves (lechem mishneh) recall the double portion in the desert. On Friday night, if wine or grape juice is completely unavailable, Kiddush can be recited over the challah loaves, provided one washes hands for bread immediately before reciting Kiddush and replaces the wine blessing with Hamotzi.",
            "Challah",
            "challah",
        ),
        line(
            "hafrashat challah — Hafrashat challah is separating a small portion of dough when baking a large amount of bread or similar flour-based foods, then burning or discarding it with a bracha in many customs. It recalls the Temple-era gift to the kohen and reminds us that sustenance comes from G-d. Even if you buy ready-made bread, learning the laws matters when you bake at home.",
            "hafrashat challah",
        ),
        line(
            "brit milah — Brit milah (bris) is the covenant of circumcision on the eighth day for a healthy Jewish boy, usually with festive meal (seudat mitzvah). It marks entry into the Jewish covenant with G-d. It is performed by a trained mohel; postponement for health requires rabbinic guidance.",
            "bris",
            "bris milah",
            "brit",
        ),
        line(
            "shomer negiah — Shomer negiah is the halachic guarding of touch between men and women (and related laws). It preserves the specialness of physical closeness for marriage and reduces situations that lead to serious sin. Details vary by community and circumstance; a rav can guide real-life situations like family, healthcare, etc.",
            "shomer negiah",
            "Shomer negiah",
        ),
        line(
            "yichud — Yichud is the prohibition against the seclusion of a man and a woman together in a private area when they are not married to each other and are not immediate blood relatives (parents, children, siblings, etc.). It prevents situations that could lead to improper intimacy. Exceptions and practical details (open doors, shared workplaces, healthcare) vary — ask your rav for real-life guidance.",
            "yichud",
            "Yichud",
        ),
        line(
            "Rav — Rav (Hebrew for \"great\" or \"master\") is a title for an experienced Torah scholar who rules on halacha — especially in Orthodox and traditional communities. A rav is a decisor (posek) for real-life questions: kashrut, Shabbat, niddah, business, and festivals. Not every rabbi is knowledgeable enough to be considered a rav.",
            "rav",
            "Rav",
        ),
        line(
            "Rabbi — Rabbi is the English word for a Jewish spiritual leader with rabbinical ordination (semicha). From Hebrew Ribbi (\"my master\" or \"my teacher\").",
            "rabbi",
            "Rabbi",
        ),
        line(
            "psak — Psak is a halachic ruling — the answer a qualified posek gives for your real case, not a theoretical debate. Judaism has legitimate range between authorities; your job is to follow your rav's psak consistently rather than shop for leniencies.",
            "Psak",
        ),
        line(
            "tzedakah — Tzedakah is usually translated \"charity,\" but the root means justice — sharing what G-d entrusted to you with those in need. It can be money, time, or influence. The sages list it among the pillars on which the world stands; even a small coin given with a full heart is a complete mitzvah.",
            "Tzedakah",
            "tzedaka",
        ),
        line(
            "Yaknehaz — YaKNeHaZ is a mnemonic for the order when Shabbat flows directly into Yom Tov (for example Saturday night Pesach): Yayin (wine blessing) → Kiddush for the festival → Ner (candle blessing, borei me'orei ha'eish) → Havdalah text ending bein kodesh l'kodesh (between holy and holy, not bein kodesh l'chol) → Zeman (Shehecheyanu when it is the first night). Besamim (spices) are omitted because you transition from Shabbat into a holy festival — the joy of Yom Tov itself comforts the soul, so the fragrant spices normally used when Shabbat departs into a weekday are unnecessary. Havdalah is inside Kiddush, not a separate ceremony. Also called Holiday Havdalah when Shabbat leads into Yom Tov. Use your siddur's festival night Kiddush section.",
            "Yaknehaz",
            "YaKNeHaZ",
            "Holiday Havdalah",
            "Havdalah in Kiddush",
        ),
        line(
            "Eruv tavshilin — Eruv tavshilin is a symbolic meal set aside on Erev Yom Tov when Shabbat follows immediately afterward. You take a baked food (challah or matzah) and a cooked food (often meat, fish, or an unpeeled hard-boiled egg), recite the blessing and declaration from your siddur or machzor in a language you understand, and thereby may continue food preparation on Yom Tov for Shabbat — which would otherwise be forbidden because you may not cook on one holy day for the next. That is why the food is set aside before the festival begins. One eruv per household is enough. It also reminds the home to prepare for Shabbat, not only for the festival.",
            "eruv tavshilin",
            "Eruv Tavshilin",
        ),
        line(
            "Melacha — Melacha is transformative labor forbidden on Shabbat and, with important differences, on Yom Tov — not merely \"hard\" work. The Torah lists 39 categories (melachot) such as writing, cooking, building, and kindling fire. Phones, driving, and most business activity fall under rabbinic extensions of these categories. Yom Tov allows ochel nefesh — food preparation for that day — but not melacha for the next day without eruv tavshilin when Shabbat follows. When unsure, ask your rav before acting.",
            "melacha",
            "Melacha",
            "melachot",
        ),
        line(
            "Yom Tov — Yom Tov (literally \"good day\") is a biblical festival such as Pesach, Shavuot, Sukkot, or Rosh Hashana. It resembles Shabbat in joy and in restricting melacha, but cooking and carrying are generally permitted for festival needs.",
            "Yom Tov",
            "yom tov",
            "Chag",
            "chag",
        ),
        line(
            "Kiddush Levana — Kiddush Levana (קִידּוּשׁ לְבָנָה — Sanctification of the Moon) is a monthly blessing said once each Hebrew month when the new moon is visible at night. It is not worship of the moon — it praises G-d for creation's cycles and Israel's renewal. The Talmud (Sanhedrin 42a) compares one who blesses the moon properly to greeting the Shechinah. Say it outdoors under the open sky when possible, standing, after nightfall. Ashkenazim and Chabad often wait at least three days after the new moon; many Sephardim wait seven. Motzei Shabbat is a widespread preference when people are dressed up and the moon is visible — but if waiting risks a string of cloudy nights and missing the monthly window, say it on the first clear weeknight the mitzvah becomes possible. Do not delay past the ideal early window. Also called Birkat HaLevanah.",
            "Kiddush Levana",
            "Kiddush Levanah",
            "Birkat HaLevanah",
            "Birkat Halevanah",
            "Sanctification of the Moon",
        ),
        line(
            "Kiddush — Kiddush sanctifies Shabbat or Yom Tov over wine (or grape juice) at the meal. It includes biblical verses about the day and the blessing borei pri hagafen. On Yom Tov and Shabbat, Kiddush is ideally recited where you will eat — Kiddush b'Makom Seudah. When Shabbat ends into Yom Tov, Kiddush incorporates havdalah (Yaknehaz). You should drink a meaningful amount after Kiddush; your rav can specify revi'it and maleh lugmov for your situation.",
            "kiddush",
            "Kiddush",
        ),
        line(
            "Havdalah — Havdalah separates holy time from weekday. On a normal Motzei Shabbat it uses wine, a braided candle, and besamim (spices). The blessing praises G-d who distinguishes bein kodesh l'chol — holy from mundane. Until you have heard Havdalah or said Baruch HaMavdil, melacha remains forbidden. When Shabbat leads into Yom Tov, havdalah is folded into Kiddush (Yaknehaz) without besamim — the joy of the festival comforts the soul, so spices are unnecessary. Follow your machzor text rather than improvising.",
            "havdalah",
            "Havdalah",
        ),
        line(
            "Shehecheyanu — Shehecheyanu thanks G-d who \"has kept us alive, sustained us, and brought us to this time.\" It is said on the first night of a festival, when wearing important new clothes, eating a  seasonal fruit for the first time since it came back into season, and many other joyous occasions. The words acknowledge that reaching this moment is itself a gift. On Yaknehaz nights it comes at the end of the combined Kiddush–havdalah sequence for the new Yom Tov.",
            "shehecheyanu",
            "Shehecheyanu",
        ),
        line(
            "bein kodesh l'kodesh — Bein kodesh l'kodesh means \"between holy and holy.\" In havdalah when Shabbat leads into Yom Tov, you declare that one level of holiness (Shabbat) ends as another (the festival) begins — not bein kodesh l'chol (holy to weekday). That wording matches the spiritual reality: Saturday night may be still sacred, but the nature of the day changes. Baruch hamavdil bein kodesh l'kodesh is also said to permit Yom Tov melacha before Kiddush.",
        ),
        line(
            "bein kodesh l'chol — Bein kodesh l'chol means \"between holy and mundane\" — the wording in regular Saturday-night Havdalah when Shabbat ends and weekday begins. People say 'Baruch hamavdil bein kodesh l'chol' before doing melacha if they have not yet heard full Havdalah or recited the maariv amidah with the Havdalah insert. When Shabbat flows into Yom Tov instead, the wording is bein kodesh l'kodesh (holy to holy), not l'chol.",
            "Baruch HaMavdil",
            "Baruch hamavdil",
            "Hamavdil",
        ),
        line(
            "Motzei Shabbat — Motzei Shabbat is Saturday night after Shabbat ends.",
            "Motzei",
            "motzei shabbat",
            "Motzash",
        ),
        line(
            "Vatodi'enu — Vatodi'enu (\"You have made us know\") is an insert in the Maariv Amidah on Saturday night when Yom Tov begins after Shabbat. It acknowledges that Shabbat has ended and the festival has begun. Some communities rely on this instead of saying Baruch hamavdil before Kiddush; others say hamavdil explicitly. Follow your machzor and rav — the app summarizes options but does not replace your community's practice.",
            "Vatodi'enu",
            "vatodienu",
        ),
        line(
            "ochel nefesh — Ochel nefesh (\"food for the soul\") is the Torah-based allowance to perform certain food preparation tasks on Yom Tov (festival days) for consumption on that same day — cooking and baking that would otherwise be forbidden melacha. It does not apply on Chol HaMoed, where food preparation is permitted. It does not permit unnecessary cooking or preparing for a day that is not yet here.",
            "Ochel nefesh",
        ),
        line(
            "Mavir — Mavir (מַבְעִיר) is creating a new flame — striking a match, flipping an electronic switch, or starting a fire from scratch. It is strictly forbidden on both Shabbat and Yom Tov. On Yom Tov, however, you may transfer an existing flame (from a candle lit before Yom Tov) to cook, light Yom Tov candles, or for festival needs (ochel nefesh). That distinction — new fire vs. transferring fire — is central to Yom Tov laws.",
            "Mavir",
            "mavir",
            "kindling fire",
        ),
        line(
            "Birkat Kohanim — Birkat Kohanim (the priestly blessing) is when kohanim bless the congregation with the words of Numbers 6:24–26. Sephardic communities worldwide — in Israel and the Diaspora — recite it every day during Shacharit and often at Musaf. Ashkenazim in Israel also say it daily. In the Diaspora, Ashkenazim generally limit it to festivals. Follow your shul's custom.",
            "Birkat Kohanim",
            "duchenen",
            "duchening",
        ),
        line(
            "Kashering — Kashering makes utensils fit for kosher or Pesach use. The core principle is kebolo kach polto — as a vessel absorbed forbidden taste, so it expels it. Items used in boiling water often need hag'alah (immersion in rolling boiling water); items used directly on fire may need libun (intense heat until they turn red hot). Plastic and glass have highly conflicting rulings between Ashkenazim and Sephardim — and it's less lenient on Pesach. Ask your rav. Ceramic cannot be kashered. Many families keep a separate Pesach set.",
            "Kashering",
            "kashering",
            "hag'alah",
            "hagalah",
            "libun",
        ),
        line(
            "chamar medina — Chamar medina (\"drink of the land\") is a significant local beverage — like beer, coffee, tea, or whiskey — that some authorities allow for Kiddush or Havdalah when wine or grape juice is unavailable or difficult. Ask your rav before relying on it for any mitzvah involving wine.",
            "Chamar medina",
            "chamar medinah",
            "Chamar medinah",
        ),
        line(
            "Simchat Yom Tov — Simchat Yom Tov (שִׂמְחַת יוֹם טוֹב) is the Torah mitzvah \"V'samachta b'chagecha\" — rejoice in your festival (Devarim 16:14). The head of household facilitates joy for each family member in the way that brings them happiness (Pesachim 109a; SA OC 529:2): wife — new clothing or jewelry l'fi mamono (within means); children — treats (today candies and toys; Talmud: grains and nuts); men — festive meat and wine meals. Halacha requires including the poor — joy only among yourselves without helping the needy is \"joy of one's stomach,\" not mitzvah joy. At the least it's recommended to provide the poor with tzedaka so they can enjoy the holiday as well.",
            "Simchat Yom Tov",
            "Simchas Yom Tov",
            "V'samachta b'chagecha",
        ),
        line(
            "Chametz — Chametz is leavened grain product made from wheat, barley, rye, oats, or spelt. Bread, beer, and many processed foods can be chametz. On Pesach, owning, eating, or benefiting from chametz is forbidden.",
            "chametz",
            "Chametz",
        ),
        line(
            "bedikat chametz — Bedikat chametz is the formal search for chametz on the night before Pesach, after nightfall, using candlelight (or flashlight per many poskim). Bread is often hidden in rooms so the bracha on the search isn't in vain if there was no bread; the next morning it is destroyed in biur chametz. Even if the house was cleaned for weeks, halacha still requires this mitzvah night.",
            "Bedikat chametz",
            "bedikat",
        ),
        line(
            "mechirat chametz — Mechirat chametz is selling chametz you must keep (valuable liquor, store inventory) to a non-Jew through your rabbi before Pesach so it is not yours during the holiday. It must be an absolute sale, not a conditional gift (matana al menat lehachzir). Chametz sold should be stored away. Shortly after Pesach, ownership reverts per the contract your rabbi uses.",
            "Mechirat chametz",
            "mechirat",
        ),
        line(
            "matana al menat lehachzir — Matana al menat lehachzir (מַתָּנָה עַל מְנָת לְהַחְזִיר) is a \"gift on condition it is returned.\" The recipient legally owns the item for the moment it is in their hands, then returns it afterward. On the first day of Sukkot, if you do not own a lulav and etrog, someone may gift you their set conditionally so you fulfill the Torah obligation of ownership for the mitzvah, then you return it. This is not used for mechirat chametz — selling chametz requires an absolute sale.",
            "matana al menat lehachzir",
            "matana al menat lehachzir",
        ),
        line(
            "Kol Chamira — Kol Chamira is an Aramaic declaration after bedikat chametz nullifying any chametz still in your possession — seen or unseen. It is not a substitute for physical cleaning or biur; it completes the spiritual renunciation. Say it from a siddur in a language you understand if you can not understand the Aramaic.",
            "Kol Chamira",
        ),
        line(
            "Hallel — Hallel is a set of psalms (113–118, with variations) praising G-d for salvation. Full Hallel is recited on major festivals; Half Hallel on Rosh Chodesh and the last 6 days of Pesach. It is said standing, with joy.",
            "hallel",
            "Hallel",
            "Half Hallel",
            "Full Hallel",
        ),
        line(
            "Musaf — Musaf is the \"additional\" Amidah remembering the extra Temple offerings (korbanot) brought on Shabbat, Rosh Chodesh, and Yom Tov. Its central blessing describes what was offered and prays for the Temple's restoration. Without the Temple, prayer replaces sacrifice — Musaf is how we still connect to that service.",
            "musaf",
            "Musaf",
        ),
        line(
            "Korbanot — Korbanot are the Temple offerings — animal sacrifices, incense, and libations — brought by the Jewish people in the Beit HaMikdash. Since the Temple's destruction, we cannot offer them; prayer recalls and substitutes for those offerings until the Temple is rebuilt.",
            "Korbanot",
            "korbanot",
            "korban",
        ),
        line(
            "Amidah — The Amidah (\"standing\" prayer) is the core of every service — also called Shmoneh Esrei (eighteen, now nineteen blessings as 1 was added). It is recited silently while standing, facing Jerusalem, with feet together.",
            "amidah",
            "Amidah",
            "Shmoneh Esrei",
        ),
        line(
            "Yaaleh V'yavo — Yaaleh V'yavo is the special paragraph inserted in the Amidah and Grace After Meals on Rosh Chodesh and festivals. Forgetting it may require repeating the prayers. Ask a rav for clarification.",
            "Yaaleh V'yavo",
            "Yaaleh V'Yavo",
        ),
        line(
            "Chol HaMoed — Chol HaMoed are the intermediate days sandwiched between the first and last days of Pesach or Sukkot. They are still festival time, but melacha rules are lighter than on Yom Tov or Shabbat: many forms of work and food preparation are permitted, with limits on certain commercial activity and some melachot.",
            "Chol HaMoed",
            "chol hamoed",
        ),
        line(
            "Sefirat HaOmer — Sefirat HaOmer counts forty-nine days from the second night of Pesach until Shavuot — linking freedom to receiving the Torah. Each night, preferably after nightfall, you bless and announce the day and week count. Missing a full day may affect whether you say the blessing the next night — ask your rav. During the Omer, many communities observe mourning customs (no music, weddings, haircuts) until Lag BaOmer or Shavuot, depending on minhag.",
            "Sefirat HaOmer",
            "Counting the Omer",
            "Sefirah",
            "the Omer",
        ),
        line(
            "aveilut — Aveilut is mourning practice after the loss of a close relative. Customs include shiva (seven days), shloshim (thirty days), and for parents, eleven months. During the Omer, aveilut customs overlap with national mourning for Rabbi Akiva's students. During the 3 weeks from 17 Tammuz to 9 Av, we mourn the Temple's destruction.",
            "Aveilut",
            "aveilut",
        ),
        line(
            "Muktzeh — Muktzeh means \"set aside\" — objects not used on Shabbat because they are not needed for Shabbat itself (tools, money, phones). The categories are detailed: some items may not be moved at all; others may be moved for a Shabbat need or to use the place they occupy. Learning basic muktzeh prevents accidental violations when tidying or handling objects on Shabbat.",
            "muktzeh",
            "Muktzeh",
        ),
        line(
            "Blech — A blech is a metal cover on the stove (often with lowered flame) keeping food warm on Shabbat without cooking. It is part of the permitted way to serve hot food when fully cooked before Shabbat. Rules cover covering knobs, flame level, and not stirring or adding liquid. Yom Tov has different cooking rules — a blech is primarily a Shabbat solution.",
            "blech",
            "Blech",
        ),
        line(
            "netilat yadayim — Netilat yadayim is ritual handwashing with a cup (keli). Morning washing removes ruach ra'ah after sleep: pour water three times on each hand in alternating sequence (right, left, right, left, right, left). Before bread, pour two or three times consecutively on each hand to purify for eating. The blessing Al Netilat Yadayim is said per minhag — Ashkenaz often after washing before bread; some Sefardim and Chabad differ on timing. Do not speak between washing and the blessing of Al Netilat Yadayim — that is a hefsek (interruption) for everyone. Many also strictly avoid speaking between the blessing and eating the bread.",
            "Netilat Yadayim",
            "netilat yadayim",
            "Al Netilat Yadayim",
        ),
        line(
            "Al netilat lulav — Al netilat lulav is the blessing before taking the Four Species on Sukkot — \"Who sanctified us with His mitzvot and commanded us to take the lulav.\" Men say it every day of the festival when taking the lulav (except Shabbat); women follow community custom. Shehecheyanu is said on the first day only. Then wave the lulav in the directions your siddur prescribes.",
            "Al netilat lulav",
            "Al Netilat Lulav",
        ),
        line(
            "al hamichya — Al hamichya (Al HaMichya) is the after-blessing on grain foods that are not bread — cake, crackers, pasta, cereal — when you ate a kezayit within k'dei achilat pras. It thanks G-d for food and the Land. If you had a bread meal, Birkat Hamazon covers other foods instead.",
            "al hamichya",
            "Al HaMichya",
            "Al hamichya",
        ),
        line(
            "borei nefashot — Borei nefashot is the short after-blessing on many foods and drinks — fruit (aside from dates, grapes, olives, figs, and pomegranates), vegetables, drinks, meat, cheese, etc.",
            "borei nefashot",
            "Borei nefashot",
        ),
        line(
            "pitom — The pitom is the stem (and sometimes flower) on top of the etrog. Many poskim prefer an etrog whose pitom is intact; a broken pitom can affect kashrut depending on when it broke. Handle the etrog carefully when assembling the lulav set.",
            "pitom",
            "Pitom",
        ),
        line(
            "Keli — A keli is a vessel used for netilat yadayim — cup, bottle, or washing cup with two handles. Water must be poured from a vessel, not dipped from a running tap, for the rabbinic washing before bread and after sleep.",
            "Keli",
            "keli",
        ),
        line(
            "Modeh Ani — Modeh Ani is the first words many Jews say upon waking: thanks to the living King for returning the soul with compassion. It is said before getting out of bed, without washing hands first. Men say Modeh; women often say Modah. It sets the day's tone — gratitude before phone, news, or worry. The soul was \"lent\" for sleep; waking is a small resurrection.",
            "Modeh Ani",
            "Modah Ani",
        ),
        line(
            "Mishloach Manot — Mishloach manot are ready-to-eat food gifts sent to at least one friend on Purim day — minimally two different foods in one package. Esther 9:19 describes sending portions to one another. It builds friendship and communal joy. Deliver personally or through a messenger; label packages before Shabbat in Purim Meshulash years when Sunday is the day for mishloach.",
            "Mishloach Manot",
            "mishloach manot",
        ),
        line(
            "matanot la'evyonim — Matanot la'evyonim are gifts to at least two poor people on Purim day so they can rejoice too. Money is common; food or vouchers work if they can use them that day. Unlike mishloach manot, this mitzvah focuses on need, not friendship. Many shuls collect on Purim morning; verify distribution happens on Purim itself.",
            "matanot la'evyonim",
            "Matanot la'evyonim",
        ),
        line(
            "Purim Meshulash — Purim Meshulash is the rare Jerusalem schedule when Shushan Purim (15 Adar) falls on Shabbat. Megillah and matanot move to Friday; mishloach manot and the seudah move to Sunday. Shabbat has no Purim melacha prep — plan before Shabbat. The app shows a full schedule in advance because the phone is off on Shabbat.",
            "Purim Meshulash",
        ),
        line(
            "gemach — A gemach (gemilut chasadim) is a free-loan fund or kindness society — lending items or money without interest, or channeling charity. On Purim, giving through a trustworthy gemach can fulfill matanot la'evyonim if the poor receive funds that day. Gemachim also run baby gear, wedding dress, and medical equipment loans in many communities.",
            "gemach",
            "Gemach",
        ),
        line(
            "shamash — On Chanukah, the shamash is the helper candle used to light the others; its light is not counted among the mitzvah candles, so you may use it for utility light. In a shul, the shamash often means the caretaker who maintains the building and assists services. Do not use the actual mitzvah flames for reading or work — they are holy and forbidden for personal benefit (bizuy mitzvah).",
            "shamash",
            "Shamash",
        ),
        line(
            "Al HaNissim — Al HaNissim (\"for the miracles\") is added to the Amidah and Birkat Hamazon on Chanukah and Purim. It summarizes the salvation — the Maccabees or Esther — and thanks G-d. If you forgot it, laws differ by day and prayer; ask your rav. Lag BaOmer and ordinary Omer days do not include Al HaNissim.",
            "Al HaNissim",
        ),
        line(
            "Oneg Shabbat — Oneg Shabbat is delighting in Shabbat — good food, rest, Torah study, singing, and time with family or guests. It is a positive mitzvah, not only avoiding melacha. The Talmud criticizes those who fast or deprive themselves on Shabbat without need. Planning meals and atmosphere before Shabbat helps oneg happen without last-minute stress.",
            "Oneg Shabbat",
            "oneg",
        ),
        line(
            "Beit HaMikdash — The Beit HaMikdash (Holy Temple) stood in Jerusalem as the center of sacrifice, pilgrimage, and divine presence. Its destruction on Tisha B'Av is mourned yearly. Musaf prayers still describe the offerings once brought there. Traditional Jews pray daily for rebuilding and for Mashiach. Understanding korbanot in Musaf connects prayer to that history.",
            "Beit HaMikdash",
            "Beit Hamikdash",
        ),
        line(
            "Days of Awe — The Days of Awe (Aseret Yemei Teshuvah) span Rosh Hashana through Yom Kippur — a time of judgment, prayer, and teshuvah. Customs intensify: Selichot, shofar, charity, and asking forgiveness. Even Jews who are less observant year-round often attend services. The tone is solemn but hopeful — repentance can change the decree.",
            "Days of Awe",
        ),
        line(
            "Ruach Ra'ah — Ruach Ra'ah (\"evil spirit\") is a rabbinic term for the impurity resting on hands after sleep. Morning netilat yadayim removes it before touching orifices, food, or prayer — three alternating pours on each hand from a vessel (keli). It is spiritual status, not germs — hygiene is separate. Do not speak between washing and blessing where required.",
            "Ruach Ra'ah",
            "ruach ra'ah",
        ),
        line(
            "tumah — Tumah is ritual impurity — a halachic state affecting what you may touch or eat (Temple-era laws, niddah, corpse contact, etc.). It is not moral guilt and not the same as being \"dirty.\" Many tumah laws apply today in taharat hamishpacha and kohanim rules; others await the Temple. Morning tumah on hands is removed by washing, not by soap alone.",
            "Tumah",
            "tumah",
        ),
        line(
            "shechita — Shechita is kosher slaughter: a trained shochet cuts the throat swiftly with a perfectly smooth knife so the animal dies with minimal suffering and the meat is permitted. Afterwards, blood is removed through salting (melicha) and soaking. Non-kosher meat is called treif. Shechita is one of the most carefully regulated mitzvot in kashrut.",
            "Shechita",
            "shechita",
        ),
        line(
            "rechilut — Rechilut is carrying tales that create hatred — even if true. Example: telling Reuven what Shimon said about him to stir conflict. Lashon hara speaks badly about someone; rechilut spreads words between parties. The Chofetz Chaim devotes extensive chapters to permitted speech for protection or constructive need; casual \"did you hear\" stories are almost never allowed.",
            "Rechilut",
            "rechilut",
        ),
        line(
            "chinuch — Chinuch is training children to do mitzvot before they are bar or bat mitzvah — when obligation becomes personal. Parents teach gradually: brachot, tzitzit, giving charity, Shabbat candles (per minhag), and respectful shul behavior. \"Educate the child according to his way\" (Mishlei) means matching pace to the child's temperament. Chinuch builds habit; understanding deepens with age.",
            "Chinuch",
            "chinuch",
        ),
        line(
            "Shema — The Shema (\"Hear O Israel\") declares G-d's oneness and our duty to love Him with all our heart, soul, and might. It is recited morning and evening with surrounding blessings. Krias Shema has specific times — especially morning Shema before the third hour of the day halachically. It is the Jewish declaration of faith children learn first.",
            "Shema",
            "Shema Yisrael",
            "Krias Shema",
        ),
        line(
            "Tefillin — Tefillin are holy black leather boxes containing parchment with Torah verses, bound on the arm and head during weekdays. Men age thirteen and over are required to wear them. They require kosher scrolls and proper placement. They are not worn on Shabbat or festivals.",
            "tefillin",
            "Tefillin",
        ),
        line(
            "Afikoman — The Afikoman is matzah eaten at the end of the Seder so no other food follows — remembering the Pesach sacrifice. Children often \"steal\" it for a prize, adding joy. You need a kezayit-sized piece eaten before chatzos halachic (midpoint of the night) per many poskim. It is the last taste of matzah the Seder requires.",
            "afikoman",
            "Afikoman",
        ),
        line(
            "posek — A posek is a rabbi qualified to decide difficult halachic questions and write authoritative rulings. Your rav may consult poskim on novel cases. The term differs from a maggid or teacher who inspires but does not pasken. Major poskim include figures behind the Mishnah Berurah, Igrot Moshe, and contemporary halachic works your community follows.",
            "Posek",
            "posek",
        ),
        line(
            "machloket — Machloket is respectful disagreement between Torah authorities — often recorded in the Talmud. Not every machloket is settled; both sides may be valid. The mitzvah is to follow your rav's psak in practice while honoring that another community may follow a different legitimate view. Machloket l'shem shamayim is for truth; argument for ego is destructive.",
            "Machloket",
            "machloket",
        ),
        line(
            "l'chatchila — L'chatchila means the ideal way to perform a mitzvah from the outset — what you should plan to do. Bedieved is after the fact, when something went wrong and halacha may offer a corrective or leniency. Learning both terms helps you read halachic guides: \"l'chatchila use a cup; bedieved if you forgot, some allow…\"",
            "l'chatchila",
            "lechatchila",
        ),
        line(
            "bedieved — Bedieved describes halachic guidance after the ideal was missed — unintentionally or unavoidably. It is not permission to plan poorly; it is how to recover. Example categories: Amidah repeats, chametz found on Pesach, missed Omer count. Your rav applies bedieved rules to your case; articles in the app summarize common patterns from sources like Peninei Halakha and Chabad.org.",
            "bedieved",
            "b'dieved",
        ),
        line(
            "Shabbat — Shabbat is the seventh day — a gift of rest and holiness from Friday sunset until halachic nightfall on Saturday night (tzeit), per your community's zmanim. The Torah forbids melacha — transformative work in thirty-nine categories. We light candles, hear Kiddush, eat three meals, study Torah, pray, and refrain from commerce and devices per your rav. Shabbat is called a taste of the World to Come; keeping it faithfully shapes the entire week.",
            "shabbat",
            "Shabbat",
        ),
        line(
            "Tzeit — Tzeit (tzeit hakochavim) is halachic nightfall — when the day ends for mitzvot like counting the Omer, bedikat chametz, Chanukah candles, Purim, and the end of Shabbat. It is not identical to sunset; communities follow different standards — from three medium stars to fixed minutes after sunset, and in some Diaspora communities much later (e.g. Rabbeinu Tam). \"Three stars\" is a common shorthand, not one universal clock time. The app shows local zmanim; when in doubt on a borderline mitzvah, ask your rav.",
            "tzeit",
            "Tzeit",
            "three stars",
        ),
        line(
            "zmanim — Zmanim are halachic times derived from sunrise, sunset, and seasonal hours — not always clock times. Examples: latest morning Shema, Mincha, Shabbat entry, chatzos, plag hamincha. A \"halachic hour\" divides daylight into twelve parts, so it lengthens in summer. Jewish calendars and apps translate zmanim for your location.",
            "Zmanim",
            "zmanim",
        ),
        line(
            "biur chametz — Biur chametz is destroying chametz the morning after bedikat chametz — usually by burning before the fifth halachic hour of the day. Whatever remains must be gone from your ownership. It completes the removal process together with mechirat chametz for sold items. Do not eat breakfast chametz that morning if your custom is to finish biur first.",
            "Biur chametz",
            "biur",
        ),
        line(
            "kitniyot — Kitniyot are legumes, rice, corn, and similar foods that Ashkenazim traditionally avoid on Pesach though they are not Torah chametz — an Ashkenazi customary stringency, not a Torah prohibition. They are halachically permitted for Sephardim (though some communities have local customs avoiding specific items). The custom began to prevent confusion with chametz grains. Peanut oil, quinoa, and similar items follow widely different psak — follow your rav, not social media debates.",
            "Kitniyot",
            "kitniyot",
        ),
        line(
            "gebrochts — Gebrochts (\"broken\") is matzah that contacted liquid after baking. Some Chasidic communities avoid gebrochts on Pesach to prevent any risk of chametz formation inside the matzah. Others eat matzah balls and kneidlach freely. If you are a guest, follow the host's house rules; at home, follow your rav's psak consistently.",
            "Gebrochts",
            "gebrochts",
        ),
        line(
            "Seder — The Seder is the ordered Pesach night meal — reclining, four cups of wine, matzah, maror, and reading the Haggadah so children ask questions. It reenacts leaving Egypt. Timing matters: matzah afikoman before halachic midnight per many poskim. Prepare the plate, Hagaddah, and guests' needs before Yom Tov begins so the night runs with joy, not panic.",
            "seder",
            "Seder",
            "First Seder",
        ),
        line(
            "Haggadah — The Haggadah (\"telling\") is the book guiding the Seder — questions, answers, plagues, Dayeinu, and Hallel. Its core is making the next generation feel they left Egypt. Many editions add commentaries; the mitzvah is participation and understanding, not speed. Mah Nishtanah is the children's opening question.",
            "Haggadah",
            "haggadah",
        ),
        line(
            "maror — Maror is bitter herb at the Seder — horseradish root or romaine — reminding the bitterness of slavery. A kezayit is required at the Seder. Charoset's sweetness is dipped with maror in Korech (Hillel's sandwich). Prepare enough fresh maror; grated horseradish loses strength quickly.",
            "Maror",
            "maror",
        ),
        line(
            "charoset — Charoset is the sweet paste (apples, wine, nuts) recalling mortar between bricks. It is dipped with maror for Korech. Recipes vary by family — Ashkenazi, Sephardi, and Persian charoset all fulfill the mitzvah when made with intent. It is one of the Seder plate's sensory contrasts: bitter and sweet together.",
            "Charoset",
            "charoset",
        ),
        line(
            "shofar — The shofar is a ram's horn (or other kosher horn) blown on Rosh Hashana as a wake-up call to teshuvah. Tekiah, shevarim, and teruah patterns follow minhag. It is not blown on Shabbat. Hearing shofar in shul with kavannah fulfills the mitzvah for most men; women's obligation follows community psak. Practice times are posted before the chag.",
            "Shofar",
            "shofar",
        ),
        line(
            "tashlich — Tashlich is a Rosh Hashana custom of casting breadcrumbs or symbolic sins into flowing water, reciting Micah 7:19. It is not in the Talmud like shofar, but is widespread. When the first day of Rosh Hashana falls on Shabbat, tashlich is postponed to Sunday — a structural rule across mainstream Ashkenazi and Sephardic practice, not a local preference. The Sages enacted this to prevent carrying machzorim through a public domain without a kosher eruv. It is symbolic teshuvah — the real work is inward change and apology to people you hurt.",
            "Tashlich",
            "tashlich",
        ),
        line(
            "Yizkor — Yizkor is the memorial prayer on Yom Tov (and Yom Kippur) for parents and sometimes other relatives. Many light a yahrzeit candle before the day. Pledging charity in their memory is a beautiful custom. Those with both parents living often leave the shul briefly per minhag. The prayer affirms that merit and memory continue beyond death.",
            "Yizkor",
            "yizkor",
        ),
        line(
            "Kol Nidrei — Kol Nidrei opens Yom Kippur eve — annulling vows so the day can proceed with a clean slate regarding promises to G-d. The haunting melody is famous, but the legal text is serious. It does not cancel interpersonal debts or promises to others — only certain vows to G-d per specific formulas. Arrive early; the shul fills quickly.",
            "Kol Nidrei",
        ),
        line(
            "Ne'ilah — Ne'ilah (\"closing\") is the final prayer service of Yom Kippur as the gates of repentance close. The ark often stays open; the congregation pleads intensely. Many have the custom to say Shema Yisrael together at the end. After Ne'ilah and nightfall (tzeit), one prays Maariv, makes Havdalah, and breaks the fast. It is the spiritual peak of the Ten Days of Teshuvah.",
            "Ne'ilah",
            "Neilah",
        ),
        line(
            "Selichot — Selichot are penitential prayers recited before Rosh Hashana (Ashkenazim often from the Saturday night before, Sephardim from Elul). They include poetic pleas and the Thirteen Attributes of Mercy. Waking early or staying late for Selichot sets a tone of seriousness before the Days of Awe. Nusach and schedule vary — check your shul.",
            "Selichot",
            "selichot",
        ),
        line(
            "Tachanun — Tachanun are supplication prayers after the Amidah on weekdays — omitted on Shabbat, Yom Tov, Rosh Chodesh, and other happy days. Many kneel or lean on the arm. Skipping tachanun on a sad day is a sign the calendar treats the day as joyful. If you are unsure whether tachanun is said, follow the shul or siddur headings.",
            "Tachanun",
            "tachanun",
        ),
        line(
            "Shacharit — Shacharit is the morning service — Pesukei d'Zimra, Shema and its blessings, the Amidah, and on Shabbat and Yom Tov Torah reading and Musaf. The Shema must be recited before the end of the third halachic hour; the Amidah ideally before the end of the fourth. Tefillin are worn on weekdays. Daily Shacharit anchors the day in prayer before the world's noise.",
            "Shacharit",
            "Shacharis",
            "shacharit",
        ),
        line(
            "Mincha — Mincha is the afternoon service, ideally before sunset. On weekdays it is short; on Shabbat and Yom Tov it includes Torah reading (often) and Musaf on festivals. Friday Mincha is special — it enters Shabbat. Missing Mincha requires tashlumin at Maariv when possible. Working people often pray Mincha at shul or a quiet office corner.",
            "Mincha",
            "mincha",
        ),
        line(
            "Maariv — Maariv is the evening service after nightfall — Shema, Amidah, and on Motzei Shabbat Havdalah or festival inserts. Saturday night Maariv may include Vatodi'enu when Yom Tov begins. Weekday Maariv is when many working Jews connect with community after the day. Consistency at Maariv builds a rhythm of ending the day with G-d.",
            "Maariv",
            "maariv",
        ),
        line(
            "Bentching — Bentching is Birkat Hamazon — Grace After Meals after eating a kezayit of bread within a meal. It thanks G-d for food and the Land. On Shabbat and festivals, Psalm 126 (Shir HaMaalot) precedes the second blessing. Yaaleh V'yavo is added on Rosh Chodesh and chag. Zimun invites others when three or more men ate bread together per minhag.",
            "Bentching",
            "bentching",
            "Birkat Hamazon",
            "Birkat HaMazon",
            "Grace After Meals",
        ),
        line(
            "Hamotzi — Hamotzi is the blessing over bread — \"Who brings forth bread from the earth\" — beginning a meal that requires bentching. Wash netilat yadayim before bread meals. Cut bread for others after you bless if you are host. On Pesach, hamotzi is on matzah; on Shabbat, lechem mishneh — two whole loaves.",
            "Hamotzi",
            "hamotzi",
        ),
        line(
            "tevilat keilim — Tevilat keilim immerses new food utensils bought from a non-Jew in a mikveh — recalling the vessels Israel immersed after the Midian war. Metal and glass usually require a bracha; glazed ceramic often requires immersion without a bracha. A Jew's manufacture may exempt. Labels \"tovel before use\" remind you — immersion is quick at a keilim mikveh or community event.",
            "Tevilat keilim",
            "tevilat keilim",
        ),
        line(
            "mezuzah — A mezuzah is the klaf — handwritten parchment with Shema and V'ahavta passages — affixed to doorposts of rooms used for dwelling. The case is protection for the scroll, not the mitzvah itself. Check scrolls by a sofer every few years. Touching the case and kissing the mezuzah when entering reminds you that G-d watches over the home.",
            "mezuzah",
            "Mezuzah",
        ),
        line(
            "mikveh — A mikveh is a kosher ritual pool of natural water meeting halachic size and flow rules. It is used for taharat hamishpacha, conversion, tevilat keilim, and some utensil and status changes. Immersion is a powerful transition — the person or vessel emerges spiritually renewed. Community mikvaot are discreet and staffed by trained attendants.",
            "mikveh",
            "Mikveh",
        ),
        line(
            "tzitzit — Tzitzit are fringes on four-cornered garments reminding us of all 613 mitzvot. The tallit katan is worn daily under the shirt; the tallit gadol at Shacharit. Strings must be kosher and tied correctly. Looking at tzitzit during Shema fulfills \"you shall see them.\" Women in some communities wear tzitzit; ask your rav.",
            "tzitzit",
            "Tzitzit",
        ),
        line(
            "taharat hamishpacha — Taharat hamishpacha (family purity) regulates intimacy through the niddah cycle and mikveh immersion. After menstruation, counting seven clean days and immersing in a mikveh permits reuniting. It is a private mitzvah with detailed laws — classes and rabbis guide couples. Healthy observance is associated with marital renewal and sanctity in Jewish tradition.",
            "taharat hamishpacha",
            "Taharat hamishpacha",
        ),
        line(
            "niddah — Niddah is the state of ritual separation during and after menstruation until mikveh immersion. Husband and wife avoid physical intimacy and certain affectionate contact. Laws of stains and cycles are complex — a kallah teacher or rav supports real-life questions. Niddah is not a punishment; it is rhythm and holiness in marriage.",
            "Niddah",
            "niddah",
        ),
        line(
            "Kaddish — Kaddish sanctifies G-d's Name in Aramaic — exalting G-d publicly. Mourners recite Kaddish for eleven months for parents and thirty days for other close relatives. It requires a minyan. There are half-Kaddish, whole-Kaddish, and rabbinic Kaddish placements in the service. Saying Kaddish connects the living to the deceased's merit.",
            "Kaddish",
            "kaddish",
        ),
        line(
            "neshama yeteira — Neshama yeteira (\"extra soul\") is the tradition that Shabbat brings an added measure of spiritual capacity — more patience, joy, and ability to connect to Torah. It departs at Havdalah, which is why some feel a post-Shabbat dip. Honoring Shabbat meals, rest, and learning helps this soul express itself; rushing and stress diminish it.",
            "neshama yeteira",
        ),
        line(
            "besamim — Besamim are fragrant spices smelled during Havdalah Saturday night — comforting the soul as the extra neshama yeteira departs back into weekday life. The blessing is Borei minei besamim. Cloves, myrtle, or sweet spices are common. Yaknehaz omits besamim when Shabbat flows into Yom Tov — the joy of the festival itself comforts the soul, so spices are halachically unnecessary.",
            "besamim",
            "Besamim",
        ),
        line(
            "lechem mishneh — Lechem mishneh is two whole loaves at Shabbat and Yom Tov meals — remembering double portion of manna before Shabbat in the desert. Cover the loaves, bless hamotzi, break bread for others. On Pesach, two whole matzot serve the role. The loaves should be whole and worthy of hamotzi.",
            "lechem mishneh",
            "Lechem mishneh",
        ),
        line(
            "seudah shlishit — Seudah shlishit is the third Shabbat meal, usually late afternoon before sunset. It is lighter than the first two meals — often fish, salad, and songs (zemiroth). Some insert special Torah ideas. Do not skip it — three meals are a key Shabbat mitzvah. Bentching includes Retzei and may extend toward Havdalah time.",
            "Seudah Shlishit",
            "seudah shlishit",
        ),
        line(
            "Ushpizin — Ushpizin are mystical \"guests\" — Abraham, Isaac, Jacob, and others — welcomed into the sukkah each night of Sukkot in Kabbalistic custom. Some recite an invitation before the meal. The idea is that true hospitality and Torah discussion bring holy presence into the booth. Even without the full kabbalistic text, inviting physical guests fulfills the mitzvah of joy in the sukkah.",
            "Ushpizin",
            "ushpizin",
        ),
        line(
            "leishev baSukkah — Leishev baSukkah is the blessing \"to dwell in the sukkah\" — said when eating bread meals in the sukkah on Sukkot. Dwelling means spending meaningful time there, not only a quick snack. Rain may exempt you per halacha. Decorate the sukkah and make children feel at home in it — the booth recalls clouds of glory in the desert.",
            "leishev baSukkah",
            "leishev basukkah",
        ),
        line(
            "schach — Schach is the plant covering on the sukkah roof — bamboo, branches, or reeds — thick enough that shade exceeds sun, yet sparse enough to see stars. It must be from material that grew from the ground and is not attached to the ground while on the sukkah. Invalid schach invalidates the mitzvah — build with guidance the first time.",
            "schach",
            "Schach",
        ),
        line(
            "Arba Minim — Arba Minim are the Four Species: lulav (palm), etrog (citron), hadas (myrtle), and aravah (willow). They are waved during Hallel on Sukkot (except Shabbat). The etrog must be kosher — not punctured or dried. Many check etrogim before buying. Holding them together unifies the Jewish people's diversity in one mitzvah.",
            "Arba Minim",
            "arba minim",
        ),
        line(
            "revi'it — A revi'it (רביעית) is a standard unit of liquid volume in halacha, defined as one-quarter of a log — commonly translated as the volume of an egg and a half. It is the minimum volume required for ritual mitzvot involving liquids, such as Kiddush. Because physical volumes have shifted and been debated, several contemporary measurements exist depending on the posek followed: Rav Chaim Na'eh: ~86 ml (approx. 2.92 fl oz) — widely used as a lenient baseline, particularly in Sephardic tradition and for rabbinic obligations like netilat yadayim. Rabbi Moshe Feinstein: ~130–150 ml (approx. 4.4–5.1 fl oz) — many recommend this for Torah-level obligations like Shabbat Kiddush. The Chazon Ish: ~150 ml (approx. 5.1 fl oz) — stringent standard in many Ashkenazi and Hasidic communities for Torah-level mitzvot. Besides Kiddush, a revi'it is also required for Havdalah (minimum wine drunk from the cup), netilat yadayim (minimum water poured — may rely on the smaller measure), and the four cups at the Pesach Seder. After Kiddush, many require drinking melo lugmov or the majority of the cup; ask your rav which measure to use.",
            "revi'it",
            "Revi'it",
        ),
        line(
            "kezayit — A kezayit is the olive-sized portion in Jewish law used to measure how much food you must consume for mitzvot — eating matzah at the Seder, maror, bentching after bread, and after-blessings. Because ancient volumes are difficult to calculate precisely, rabbis have established modern equivalents. Halachically, a kezayit is tied to the size of an egg — usually calculated as either half or one-third of an egg's volume depending on the authority. Many major organizations (such as Star-K) equate a kezayit to about 1.2 to 1.3 fluid ounces (approx. 35–40 ml), which visually translates to the size of a golf ball or a roll of quarters. By weight, this generally ranges between 25 and 33 grams — the stringent opinion of the Chazon Ish is approx. 33 g; the more lenient opinion of Rabbi Chayim Na'eh is approx. 27 g. The exact size depends on which authority you follow; ask your rav or use a community chart for the specific food or mitzvah you are measuring. Eating less than a kezayit may mean the mitzvah was not fulfilled.",
            "kezayit",
            "Kezayit",
        ),
        line(
            "Machatzit HaShekel — Machatzit HaShekel recalls the half-shekel each Jew gave for Temple upkeep in Adar. Today many give charity before Purim — often three coins half of a local unit. It is a widespread custom, not one of the four Purim mitzvot, but it connects the community to unified redemption. Give through your shul or reputable charity.",
            "Machatzit HaShekel",
            "Machatzit haShekel",
        ),
        line(
            "bal yera'eh — Bal yera'eh (\"it shall not be seen\") is the Torah prohibition against chametz remaining visible in your domain on Pesach. Together with bal yimatzei (\"it shall not be found\"), it drives bedikat, biur, and mechirat chametz. Even crumbs in your control matter. Sold chametz in a closed sold area must stay inaccessible.",
            "bal yera'eh",
        ),
        line(
            "bal yimatzei — Bal yimatzei forbids chametz being found in your possession on Pesach. Spiritual nullification (Kol Chamira) complements but does not replace cleaning. Chametz you owned over Pesach without sale may be forbidden to benefit from after Pesach — serious questions go to your rav.",
            "bal yimatzei",
        ),
        line(
            "shiva — Shiva is seven days of mourning at home after burial — community visits, low stools, no leather shoes per many minhagim, and Kaddish with a minyan. Work and entertainment pause. Shiva ends after the morning of the seventh day. The purpose is comfort and gradual re-entry to life, not isolation forever.",
            "Shiva",
            "shiva",
        ),
        line(
            "shloshim — Shloshim is thirty days of reduced social celebration after burial (for many relationships). Haircuts and music may be restricted. After shloshim, normal life returns except for parents — Kaddish continues eleven months. Calendar overlaps with the Omer or Three Weeks add extra restrictions — ask your rav.",
            "Shloshim",
            "shloshim",
        ),
        line(
            "yahrzeit — Yahrzeit is the annual Hebrew-calendar date of a relative's death. Many light a 24-hour candle, visit the grave, give charity, and lead services if they are a kohen or know the customs. Parents' yahrzeits are especially observed. The name means \"time of year\" in Yiddish — a day to remember and elevate the soul.",
            "Yahrzeit",
            "yahrzeit",
        ),
        line(
            "Hadlakat Nerot — Hadlakat Nerot is lighting candles before Shabbat or Yom Tov — welcoming holiness into the home. Women traditionally light; men if no woman is present. Ashkenazim often light then bless with eyes covered; Sephardim may bless first. Once you accept Shabbat with the blessing, do not light more flames. Yom Tov candles are lit from a pre-existing flame when following that halacha.",
            "Hadlakat Nerot",
            "Light Yom Tov candles",
        ),
        line(
            "zemiroth — Zemiroth (zemirot) are Shabbat table songs — some biblical phrases, many composed by mystics and poets. Singing zemiroth turns the meal into oneg Shabbat. Common tunes include Menucha v'Simcha and Yah Ribon. No musical instruments on Shabbat per most poskim — voices only. Children learn Shabbat joy through zemiroth.",
            "zemiroth",
            "zemirot",
            "Zemirot",
        ),
        line(
            "kavannah — Kavannah is focused intention — knowing what mitzvah you are doing and for Whom. Prayer without kavannah is still prayer, but the sages urge preparation: understand words, stand with respect, remove distractions. Candle lighting and Amidah are especially suited to personal requests once the formal text is said.",
            "Kavvanah",
            "kavannah",
            "kavana",
        ),
        line(
            "neshama — Neshama is the soul — the divine breath that makes you alive. Modeh Ani thanks G-d for returning it after sleep. Yizkor and Kaddish relate to the soul's journey after death. Jewish thought distinguishes levels of soul (nefesh, ruach, neshama) in mysticism; practically, nurturing the soul means Torah, mitzvot, and character.",
            "neshama",
            "neshamah",
            "Nishmat",
        ),
        line(
            "Chofetz Chaim — Rabbi Yisrael Meir Kagan (Chofetz Chaim) wrote the definitive guide to guarding speech and the Mishnah Berurah on daily halacha. His works teach that gossip destroys communities and that ordinary Jews can master Shabbat, kashrut, and prayer law. Studying his speech laws transforms relationships more than any lecture on morality.",
            "Chofetz Chaim",
            "chofetz chaim",
        ),
        line(
            "yetzer hara — Yetzer hara is the inclination toward selfishness, laziness, or sin — not a devil, but internal pull. Everyone has it; the battle is lifelong. Torah, mitzvot, and good friends strengthen yetzer hatov. The goal is not to destroy desire but to channel it — appetite for holy things, ambition for good deeds.",
            "Yetzer hara",
            "yetzer hara",
        ),
        line(
            "yetzer hatov — Yetzer hatov is the inclination toward good — conscience, love of mitzvot, shame from wrongdoing. It grows when you learn Torah and see righteous examples. Jewish education (chinuch) cultivates yetzer hatov in children through habit before full understanding.",
            "Yetzer hatov",
            "yetzer hatov",
        ),
        line(
            "cheshbon hanefesh — Cheshbon hanefesh is an honest spiritual accounting — reviewing your week: speech, honesty, prayer, family. Many do this before bed or during Elul. Name one strength and one fixable weakness. Without cheshbon, the same mistakes repeat; with it, teshuvah becomes concrete.",
            "Cheshbon hanefesh",
            "cheshbon hanefesh",
        ),
        line(
            "Chilul Hashem — Chilul Hashem is desecrating G-d's Name — behavior that makes Torah or Jews look contemptible, especially in public. Fraud, rage, or hypocrisy by a visibly Jewish person harms the whole people. The opposite is kiddush Hashem — sanctifying G-d's Name through integrity. Non-Jews judging Judaism by your conduct is a serious responsibility.",
            "Chilul Hashem",
            "chilul hashem",
        ),
        line(
            "bar mitzvah — Bar mitzvah is when a boy turns thirteen and becomes obligated in mitzvot — tefillin, fasting, counting the Omer, and the full moral law. The celebration is seudat mitzvah joy, but the essence is responsibility. Parents' chinuch before thirteen prepares him. Aliyah to the Torah often marks the day in shul.",
            "bar mitzvah",
            "Bar Mitzvah",
        ),
        line(
            "bat mitzvah — Bat mitzvah is when a girl reaches twelve and becomes obligated in mitzvot not dependent on the Temple's time — the standard age of obligation in traditional halacha. Customs for celebration vary — speeches, learning projects, family meal. Women's mitzvot include Shabbat candles, kashrut, charity, and Torah study — specifics follow family and rav.",
            "bat mitzvah",
            "Bat Mitzvah",
        ),
        line(
            "Shnayim Mikra v'Echad Targum — Shnayim Mikra v'Echad Targum is completing the weekly Torah portion twice in Hebrew and once in translation (Targum Onkelos or commentary) before Shabbat. It connects you to the synagogue reading. Many use a calendar or Chumash with Rashi. Even one aliyah's worth weekly is a start — consistency matters more than speed.",
            "Shnayim Mikra",
        ),
        line(
            "aliyah — Aliyah means \"going up\" — being called to bless the Torah reading in shul, or immigrating to Israel. In shul, the oleh says blessings before and after the portion; the baal koreh chants. Honoring aliyot goes to kohanim, leviim, a groom, bar mitzvah, and yahrzeit per order on the gabbai's list.",
            "Aliyah",
            "aliyah",
        ),
        line(
            "leining — Leining is chanting the Torah portion from the scroll with precise trop (cantillation). The baal koreh trains for months. Congregants follow in a Chumash. Hearing every word on Shabbat morning fulfills the communal mitzvah of public Torah reading.",
            "Leining",
            "leining",
        ),
        line(
            "Birchot HaShachar — Birchot HaShachar are morning blessings thanking G-d for eyesight, clothing, strength, and being created — said at shul or home. They orient the day toward gratitude before requests. Women and men both say them; some women omit certain blessings per custom. They parallel waking gifts we overlook.",
            "Birchot HaShachar",
        ),
        line(
            "birchat hamapil — Birchat Hamapil (Hamapil) is the blessing before sleep entrusting the soul to G-d. Some omit it if they may know they may not fall asleep for a long time. It pairs with Shema al hamitah for protection. Children benefit from a short bedtime Shema even before full obligation.",
            "birchat hamapil",
            "Hamapil",
            "hamapil",
        ),
        line(
            "Elokei Neshama — Elokei Neshama thanks G-d in the morning for restoring the soul, which was pure and will be returned at death. It follows Birchot HaShachar in many siddurim. The prayer teaches that each day is a new loan of life — use it for mitzvot, not only errands.",
            "Elokei Neshama",
        ),
        line(
            "ona'ah — Ona'ah is wronging someone financially — overcharging when they do not know market price or underpaying a worker. A fifth above or below fair price may trigger Torah ona'ah laws. Honest business is a mitzvah; \"everyone does it\" does not excuse geneivat da'at or ona'ah.",
            "Ona'ah",
            "ona'ah",
        ),
        line(
            "geneivat da'at — Geneivat da'at (\"stealing the mind\") is deception — advertising falsely, hiding defects, or misleading about credentials. It applies to customers, employees, and friends. The Torah demands \"honest scales\" in spirit and letter. Trust lost through geneivat da'at is hard to rebuild.",
            "Geneivat da'at",
        ),
        line(
            "ona'at devarim — Ona'at devarim is verbal wronging — insults, public embarrassment, or pressuring someone to sell. Even \"friendly\" teasing can be forbidden if it hurts. The Talmud lists it among sins that may not be forgiven until the victim is appeased. Guarding speech includes not only lashon hara but ona'at devarim.",
            "Ona'at devarim",
        ),
        line(
            "maaser kesafim — Maaser kesafim is setting aside about ten percent of net income for tzedakah after expenses. It trains that income is entrusted, not owned absolutely. Priorities: local poor, Torah learning, Israel. If you cannot give ten percent without hardship, give what you can and ask your rav.",
            "Maaser kesafim",
            "maaser",
        ),
        line(
            "k'fi daato — K'fi daato means training a child according to what they can understand and do reliably — not demanding adult perfection. Chinuch grows mitzvot stepwise: brachot before full Amidah, short Shabbat attendance before long services. Pressure without da'at backfires; joy in mitzvot lasts longer.",
            "k'fi daato",
            "kfi daato",
        ),
        line(
            "klaf — The klaf is the special kosher parchment on which scribes write Torah scrolls, mezuzots, tefillin scrolls, etc.",
            "Klaf",
            "klaf",
        ),
        line(
            "Shaddai — Shaddai (one of Hashem's names you shouldn't pronouce in vain) on the mezuzah back is a divine name and acronym for Shomer Daltot Yisrael — Guardian of Israel's doors. The letter shin on the case often points outward. It is a reminder of faith that Hashem has the power to guard you at every doorway.",
            "Shaddai",
        ),
        line(
            "Kashrut — Kashrut is the Torah system of permitted food, shechita, separating meat and dairy, and supervising production. It sanctifies eating — turning meals into service of G-d. Learning labels (OU, OK, etc.), hechsherim, and kitchen setup takes time; ask your rav when starting.",
            "Kashrut",
            "kashrut",
        ),
        line(
            "kosher — Kosher means fit for use per halacha — especially food, but also valid scrolls and acceptable conduct (e.g. kosher money for mitzvot). Kosher is not a health label; it is spiritual fitness. When in doubt on a product, call the hechsher company or your rav.",
            "kosher",
            "Kosher",
        ),
        line(
            "treif — Treif means non-kosher — originally torn flesh of improperly slaughtered animals, now any forbidden food. Mixing treif into a kosher kitchen can require kashering. \"Kosher style\" restaurants without supervision are not kosher.",
            "Treif",
            "treif",
        ),
        line(
            "pirsumei nisa — Pirsumei nisa means publicizing the miracle — placing Chanukah menorah where passersby see the lights. The miracle of the oil is proclaimed to the street. Inside homes, many light by the window. Do not use the mitzvah candles for reading — use the shamash.",
            "Pirsumei nisa",
            "Pirsumei Nisa",
        ),
        line(
            "Three Weeks — The Three Weeks from 17 Tammuz to Tisha B'Av mourn the Temple's destruction — no weddings, music, and haircuts per many Ashkenazim. The Nine Days from Rosh Chodesh Av intensify restrictions. Kinot on Tisha B'Av are the climax. Sephardi customs differ in timing — follow your rav.",
            "Three Weeks",
        ),
        line(
            "Nine Days — The Nine Days from Rosh Chodesh Av until Tisha B'Av add mourning customs — no laundry, swimming, or meat/wine until Shabbat cholent rules per minhag. Planning weddings and purchases avoids this period. The goal is to feel the loss of the Temple, not mere inconvenience.",
            "Nine Days",
        ),
        line(
            "Rosh Hashana — Rosh Hashana is the Jewish New Year and Day of Judgment — shofar, festive meals, and solemn prayer. It begins the Ten Days of Teshuvah leading to Yom Kippur. Customs include apples in honey, new fruit, and tashlich. Work is forbidden like Yom Tov.",
            "Rosh Hashana",
            "Rosh Hashanah",
        ),
        line(
            "Yom Kippur — Yom Kippur is the Day of Atonement — fasting, confession, and prayer. Biblically called Shabbat Shabbaton, it carries the exact same strict labor (melacha) prohibitions as Shabbat, meaning cooking and kindling fire are strictly forbidden (unlike Yom Tov, which permits ochel nefesh). Kol Nidrei opens the eve; Ne'ilah closes the day. The day atones with teshuvah, but serious sins between people still require apology and restitution. Prepare easy break-fast before the fast begins.",
            "Yom Kippur",
        ),
        line(
            "Pesach — Pesach commemorates the Exodus — matzah, maror, Seder, and no chametz. It is the festival of freedom and faith. Preparation dominates erev Pesach: bedikat, biur, kashering. The spiritual goal is feeling you left Egypt personally, not only historical memory.",
            "Pesach",
            "Passover",
        ),
        line(
            "Sukkot — Sukkot is seven days dwelling in the sukkah and waving the Four Species — joy after the Days of Awe. It recalls desert clouds of glory. Rain may exempt from sitting. Shemini Atzeret follows immediately — a separate holiday of intimacy with G-d after the public festival.",
            "Sukkot",
        ),
        line(
            "Shemini Atzeret — Shemini Atzeret (\"eighth day of assembly\") is the biblical festival day immediately after the seven days of Sukkot — a day of special closeness with G-d. In Israel, with one day of Yom Tov, the rabbinic celebrations of Simchat Torah are folded into this biblical day. In the Diaspora, Shemini Atzeret is the first day and Simchat Torah is celebrated separately on the second day.",
            "Shemini Atzeret",
            "Shemini Atzeres",
        ),
        line(
            "Simchat Torah — Simchat Torah (\"rejoicing in the Torah\") celebrates completing and restarting the annual Torah cycle with hakafot and joy. Biblically the day is Shemini Atzeret. In Israel, Simchat Torah is celebrated on that single biblical day. In the Diaspora, Shemini Atzeret is the first Yom Tov day and Simchat Torah is the second — separate calendar days with distinct focus.",
            "Simchat Torah",
            "Simchas Torah",
        ),
        line(
            "Shavuot — Shavuot marks Matan Torah — receiving the Torah at Sinai, seven weeks after Pesach. Dairy meals, all-night learning, and reading Ruth are customs. It is one day in Israel, two in the Diaspora. Unlike other pilgrim festivals, the Torah does not name its historical event in the text — tradition supplies it.",
            "Shavuot",
        ),
        line(
            "Purim — Purim celebrates salvation in Shushan — Megillah, matanot la'evyonim, mishloach manot, and seudah. Costumes and joy are mitzvah, not frivolity alone. Hearing every word of the Megillah is essential. Drunkenness per \"until you cannot tell\" is bounded by safety and halacha — ask your rav.",
            "Purim",
        ),
        line(
            "Purim seudah — The Purim seudah is the festive afternoon meal on Purim day — part of the day's joy alongside Megillah, mishloach manot, and matanot la'evyonim. Many begin the meal before sunset and continue into the evening. It is a seudat mitzvah of celebration, not a fast day.",
            "Purim seudah",
        ),
        line(
            "Chanukah — Chanukah is eight nights lighting menorah for the oil miracle and victory over Greek oppression. Al HaNissim is added in prayer. Gifts are American custom, not core mitzvah. Place menorah for pirsumei nisa. Work is permitted; the focus is light and thanks.",
            "Chanukah",
            "Hanukkah",
        ),
        line(
            "Korech — Korech is the \"Hillel sandwich\" at the Seder — matzah and maror together, recalling Temple-era practice. You eat a kezayit of matzah and maror combined after dipping maror in charoset. It is one of the Seder's core tastes: slavery's bitterness held with redemption's bread. Follow your Haggadah's order after the main matzah mitzvah.",
            "korech",
        ),
        line(
            "Mah Nishtanah — Mah Nishtanah (\"Why is this night different?\") is the Four Questions — usually asked by the youngest child at the Seder. The Haggadah answers each question with Torah, story, and mitzvot. The point is engagement: children (and adults) should feel the Exodus personally, not only hear a lecture.",
            "Mah Nishtanah",
        ),
        line(
            "karpas — Karpas is a vegetable (often parsley or potato) dipped in salt water at the Seder start — awakening curiosity before the meal. The salt water recalls tears of slavery. It is not the main maror mitzvah; it opens the night with a question. Prepare enough for every guest.",
            "Karpas",
            "karpas",
        ),
        line(
            "matzah — Matzah is unleavened bread eaten all Pesach and central at the Seder — at least a kezayit when hamotzi is said, again for the afikoman. It recalls haste leaving Egypt. Shmurah matzah is guarded from moisture; many use it at the Seder. Store carefully — broken matzah still counts if kezayit-sized pieces remain.",
            "Matzah",
            "matzah",
        ),
        line(
            "Megillah — Megillah means scroll; on Purim it is Esther's book read twice — night and day. Hearing every word is the mitzvah; noise at Haman's name is custom. Follow along in a printed Megillah or listen closely. If you miss words, ask whether you must hear that section again.",
            "Megillah",
            "megillah",
        ),
        line(
            "Haman — Haman is the Persian official who plotted to destroy the Jews in Esther's time. Mordechai and Esther foiled the plan. When the Megillah reader says Haman's name, many boo, rattle graggers, or stamp — erasing his name symbolically. The mitzvah is hearing the text, not drowning out so much that words are missed.",
            "Haman",
        ),
        line(
            "Shushan Purim — Shushan Purim is 15 Adar in walled cities that had walls in Joshua's time (Jerusalem is primary today). When 15 Adar is Shabbat, Jerusalem observes Purim Meshulash — Megillah and matanot Friday, mishloach and seudah Sunday. Elsewhere Purim is 14 Adar. Know your city's calendar.",
            "Shushan Purim",
        ),
        line(
            "Hoshanot — Hoshanot are prayers of salvation recited while holding the Four Species and circling the synagogue sanctuary platform (Bimah) during Sukkot (except Shabbat). Hoshana Raba (seventh day) has extra circuits. It is the last push of repentance after Yom Kippur — joy and urgency together.",
            "Hoshanot",
            "hoshanot",
        ),
        line(
            "Hoshana Raba — Hoshana Raba is the seventh day of Sukkot — the climax of Hoshanot with extra circuits around the bimah. The beating of aravot (willow branches) against the ground is classified as a Minhag Nevi'im — an ancient custom instituted by the final Prophets (Haggai, Zechariah, and Malachi). Because of its prophetic roots, it follows strict parameters that ordinary customs do not — notably striking the branches against the ground exactly five times to sweeten the divine judgments. See also Hoshanot.",
            "Hoshana Raba",
            "Hoshana Rabbah",
            "hoshana raba",
        ),
        line(
            "Pesukei d'Zimra — Pesukei d'Zimra (\"verses of song\") open Shacharit — psalms and praises warming the heart before Shema. Baruch She'amar and Yishtabach frame the section. Skipping it to \"save time\" loses the service's emotional arc. On Shabbat, Nishmat and expanded psalms add length and joy.",
            "Pesukei d'Zimra",
            "Pesukei DeZimra",
        ),
        line(
            "zimun — Zimun is inviting others to bentch together when three or more men (per common minhag) ate bread as a group. The leader says \"let us bless\" and others answer. It turns private thanks into communal praise. Women's zimun follows separate customs — ask your rav.",
            "Zimun",
            "Zimmun",
            "zimun",
        ),
        line(
            "Retzei — Retzei asks G-d to be pleased with our rest and to restore the Temple and Davidic kingdom — inserted in the third blessing of bentching on Shabbat and Yom Tov. Forgetting Retzei at the first two Shabbat meals usually requires repeating bentching. However, if you forget it at Seudah Shlishit (the third meal), you do not repeat (SA OC 188:8). Ask your rav if unsure.",
            "Retzei",
        ),
        line(
            "baal koreh — The baal koreh (master of reading) chants the Torah from the scroll with correct trop and pronunciation. Training takes months. Congregants follow in Chumash. A mistake that changes meaning may require correction — the oleh listens, not reads aloud from the scroll.",
            "baal koreh",
            "baal kriah",
        ),
        line(
            "Hagbah — Hagbah is lifting the open Torah scroll so the congregation can see the writing and recite \"V'zot HaTorah\" (and related verses). Ashkenazim (and most Chabad communities) perform hagbah after the reading, before gelilah (rolling and dressing the scroll). Sephardim (Nusach Edot HaMizrach) perform hagbah before the reading begins — the scroll is shown, then read. Follow your shul's order so you know when to stand and respond.",
            "Hagbah",
            "hagbah",
            "Hagba",
        ),
        line(
            "oleh — The oleh (one who goes up) recites blessings before and after the Torah reading at an aliyah. Immigrating to Israel is also called aliyah. Honoring aliyot respects kohanim, leviim, yahrzeits, and life-cycle events. Prepare the Hebrew blessings or follow along in transliteration.",
            "oleh",
            "Oleh",
        ),
        line(
            "levi — Levi is the tribe serving in the Temple; today leviim are often called to the Torah after kohanim. Many carry the family name Levi or related traditions. Kohanim and leviim have other halachot (priestly blessing, tumah). If unsure of your status, ask family and your rav.",
            "levi",
            "leviim",
            "Levi",
        ),
        line(
            "tekiah — Tekiah is a long straight shofar blast. The Rosh Hashana sequence combines tekiah with shevarim (broken) and teruah (trembling) sounds per minhag. One hundred blasts are customary Ashkenaz. Practice in shul beforehand so the day is not your first time hearing shofar.",
            "Tekiah",
            "tekiah",
            "shevarim",
            "teruah",
        ),
        line(
            "kaparot — Kaparot is a pre-Yom Kippur custom swinging a chicken (or money substituted) over one's head while reciting verses — charity replaces the chicken in many communities today. It is custom, not Torah law. Handle with sensitivity; follow local rabbinic guidance and kashrut if using chickens.",
            "Kaparot",
            "kaparot",
            "kapparot",
        ),
        line(
            "Borer — Borer (selecting) forbids sorting a mixed pile on Shabbat by removing unwanted from wanted unless three conditions are met: take the good from the bad, by hand (not a dedicated strainer), for immediate use. Example: picking bones from your plate right before eating is OK; sorting a salad bowl for later is not.",
            "Borer",
            "borer",
        ),
        line(
            "Bishul — Bishul is cooking on Shabbat — heating food until yad soledet bo (hand recoils). Fully cooked dry food may be reheated in some cases; liquids are stricter. A blech or slow cooker set before Shabbat keeps food warm. Yom Tov allows cooking for that day — different rules entirely.",
            "Bishul",
            "bishul",
        ),
        line(
            "yad soledet bo — Yad soledet bo (\"when the hand recoils\") is the heat threshold for bishul on Shabbat — roughly 110–113°F (43–45°C). Food already fully cooked may sometimes be reheated dry on Shabbat; liquids are stricter. This is why a blech keeps food warm without reaching cooking temperature.",
            "yad soledet bo",
            "Yad Soledet Bo",
        ),
        line(
            "k'dei achilat pras — K'dei achilat pras is the halachic time limit to consume a specific volume of food so it counts as one act of eating — about 4–7 minutes per many poskim. It applies to eating a kezayit of matzah at the Seder, maror, and eating enough bread or cake within that window to require an after-blessing (Birkat Hamazon or al hamichya). If you ate a kezayit of bread within k'dei achilat pras, Birkat Hamazon covers the whole meal.",
            "k'dei achilat pras",
            "kdei achilat pras",
        ),
        line(
            "bracha acharona — Bracha acharona is the after-blessing said when you ate or drank enough of a food but did not have a bread meal requiring Birkat Hamazon — for example borei nefashot or al hamichya.",
            "bracha acharona",
            "Bracha acharona",
        ),
        line(
            "Mezonot — Mezonot (borei minei mezonot) is the blessing on grain foods that are not bread — cake, crackers, pasta, cereal. It is shorter than hamotzi but still thanks G-d for grain sustenance.",
            "Mezonot",
            "mezonot",
        ),
        line(
            "Hotza'ah — Hotza'ah is carrying from private to public domain (or four amot in public) on Shabbat. Without an eruv, keys in a pin-on pouch, strollers in some areas, and wheeling objects outdoors may be issues. An eruv symbolically encloses a community — check if yours is up this week.",
            "Hotza'ah",
            "hotzaah",
        ),
        line(
            "Koteiv — Koteiv is writing two meaningful letters on Shabbat. Typing, texting, and permanent markers violate this rabbinically and often biblically. Avoid Scrabble with locking tiles, signing checks, and filling forms. Children should have Shabbat-appropriate activities prepared.",
            "Koteiv",
            "koteiv",
        ),
        line(
            "Pikuach nefesh — Pikuach nefesh (saving life) overrides Shabbat and almost all mitzvot. Call emergency services, drive to hospital, and carry medicine when life is at risk. Doctors and nurses have detailed halacha for their shifts — but a layperson should never hesitate to save life. After the danger passes, ask your rav about what you did.",
            "Pikuach nefesh",
            "pikuach nefesh",
        ),
        line(
            "eruv — An eruv (especially eruv chatzerot or city eruv) is a halachic enclosure letting Jews carry within a defined area on Shabbat. It requires kosher boundaries and community upkeep. When the eruv is down, carrying keys and pushing strollers outdoors may be forbidden — shuls often text status Friday. It does not permit driving or phones.",
            "Eruv",
            "eruv",
        ),
        line(
            "Sukkah — A sukkah is a temporary booth with schach roof where we eat (and some sleep) on Sukkot — recalling desert clouds of glory. Walls must stand; schach must shade more than sun. Decorate for joy; invite guests and ushpizin. Rain may exempt you from eating in the sukkah — follow halacha for your situation.",
            "Sukkah",
            "sukkah",
        ),
        line(
            "Rosh Chodesh — Rosh Chodesh begins the Hebrew month — semi-holiday with Hallel (half), Musaf, Yaaleh V'yavo, and often reduced work for women per custom. In ancient times the new moon was proclaimed from testimony. Today the calendar is fixed. It is a monthly reset — plan Torah goals and charity.",
            "Rosh Chodesh",
        ),
        line(
            "Tashlumin — Tashlumin is making up a missed Amidah at the very next service: missed Shacharit at Mincha, missed Mincha at Maariv, missed Maariv at tomorrow's Shacharit. Pray the current Amidah first, pause, then the makeup. This applies only if you missed the prayer by accident or unavoidable delay — if you missed intentionally, there is no tashlumin. Musaf adds complexity; ask your rav.",
            "Tashlumin",
            "tashlumin",
        ),
        line(
            "Tefilat Nedavah — Tefilat Nedavah is a voluntary Amidah when tashlumin is impossible — with mental stipulation and a small novelty in the prayer. It does not replace missed obligation like tashlumin but offers a way to pray after multiple misses. Not for daily casual use.",
            "Tefilat Nedavah",
        ),
        line(
            "Mishnah Berurah — Mishnah Berurah is Rabbi Yisrael Meir Kagan's detailed commentary on Orach Chaim — daily halacha for prayer, Shabbat, and festivals. It explains Shulchan Aruch with practical rulings. Many Ashkenazim rely on it for home practice. It pairs with the Chofetz Chaim's other works on speech and mitzvot.",
            "Mishnah Berurah",
            "Mishna Berura",
            "Mishnah Berura",
            "Mishna Berurah",
        ),
        line(
            "Talmud — The Talmud (Gemara + Mishnah) is the central rabbinic discussion of halacha and aggada — developed in Babylonia and Israel. Daf Yomi learners finish the whole Talmud in about seven years. You can start small; even one line with commentary builds Jewish literacy. It is the backbone of how halacha develops.",
            "Talmud",
            "Gemara",
            "gemara",
        ),
        line(
            "daven — Daven (davening) means to pray. You 'daven' the daily services (Shacharit, Mincha, Maariv). The word is Yiddish-derived Ashkenazi slang but widely understood. People often say \"daven Shacharit\" for a service and \"daven\" for praying in general.",
            "daven",
            "Daven",
            "davening",
        ),
        line(
            "shul — Shul (synagogue) is the community house of prayer, Torah, and chesed. Beyond services, it often hosts classes, youth programs, and charity organizations.",
            "shul",
            "Shul",
        ),
        line(
            "Elul — Elul is the month before Rosh Hashana — daily selichot (in Ashkenaz from before Rosh Hashana, Sephardim all month), blowing shofar after services, and cheshbon hanefesh. It is the runway for teshuvah. Increase charity, repair relationships, and learn one new halacha before the Days of Awe.",
            "Elul",
        ),
        line(
            "simanim — Simanim are symbolic Rosh Hashana foods — apple in honey, pomegranate, fish head, dates — each with a pun or prayer for the new year. They are minhag, not Torah law, but beloved for teaching children. Say the short yehi ratzon prayers from the machzor.",
            "simanim",
        ),
        line(
            "Shema al HaMitah — Shema al HaMitah is the bedtime Shema — declaring faith and entrusting the soul to G-d before sleep. Many add Psalm 91 and other verses. Hamapil is said when actually lying down to sleep. Women are obligated in this protection per halacha. A few minutes of calm prayer ends the day well.",
            "Shema al HaMitah",
            "Kriat Shema al HaMitah",
            "Krias Shema al HaMitah",
            "Bedtime Shema",
            "bedtime Shema",
        ),
        line(
            "kallah — Kallah means bride; kallah classes teach taharat hamishpacha, mitzvot of the home, and wedding laws before marriage. The kallah and chatan prepare for chuppah and building a Jewish family.",
            "kallah",
            "Kallah",
        ),
        line(
            "chuppah — The chuppah is the wedding canopy representing the couple's new home — open on all sides like Abraham's tent. The ceremony includes kiddushin (betrothal) and nisuin under the chuppah. Breaking the glass recalls the destruction of the Temple. It is a seudat mitzvah with dancing and simcha.",
            "chuppah",
            "Chuppah",
        ),
        line(
            "Rashi — Rashi is Rabbi Shlomo Yitzhaki's eleventh-century commentary — the first stop for Chumash and Talmud learners. He explains pshat (plain meaning) in concise Hebrew (or Aramaic in Talmud). Shnayim Mikra often uses Rashi as the \"third\" reading. His commentary shaped Jewish education for centuries.",
            "Rashi",
        ),
        line(
            "Targum Onkelos — Targum Onkelos is the Aramaic translation of the Torah read in Shnayim Mikra v'Echad Targum. It was taught at Ezra's time to understand Torah. Some use Rashi or English instead.",
            "Targum Onkelos",
            "Targum",
        ),
        line(
            "cholent — Cholent (chamin, hamin) is Shabbat stew left on a blech or slow cooker from before Shabbat — hot food without cooking on Shabbat. Every community has recipes: beans, meat, potatoes. It makes oneg Shabbat practical. Start it early Friday so it has time to cook partway before Shabbat.",
            "cholent",
            "Cholent",
        ),
        line(
            "mechitza — A mechitza divides men and women's seating in shul during prayer so focus stays on tefillah per traditional practice. Heights and designs vary by community.",
            "mechitza",
            "Mechitza",
        ),
        line(
            "Kiddush b'Makom Seudah — Kiddush b'Makom Seudah means sanctifying Shabbat or Yom Tov, typically with blessings over wine, in the same place you eat a meal afterwards. You would not fulfill Kiddush if you recited it and left without eating there. On Shabbat night, the meal should include bread (hamotzi) per most poskim. On Yom Tov or daytime Kiddush, other foods may qualify in some cases; ask your rav. If you make Kiddush in one room, the meal should follow there and not in a different space.",
            "Kiddush b'Makom Seudah",
        ),
        line(
            "maleh lugmov — Maleh lugmov is drinking a cheekful from the Kiddush cup (approx 2 fl oz) after the blessing — from at least a total of a revi'it of wine or grape juice in the cup. Grape juice counts like wine for many poskim.",
            "maleh lugmov",
        ),
        line(
            "Shamayim — Shamayim means Heaven or the skies — in yirat Shamayim (awe of Heaven) it means living with awareness that G-d sees all actions.",
            "Shamayim",
            "shamayim",
        ),
        line(
            "nefesh — Nefesh is the 'animal' part of the soul closest to physical life — appetite, hunger, physical desires, etc. Torah and mitzvot attempt to refine the nefesh toward holiness instead of animal instincts.",
            "nefesh",
            "Nefesh",
        ),
        line(
            "Chumash — A Chumash (from chamesh, \"five\") is a printed book of the five books of the Torah — Bereishit, Shemot, Vayikra, Bamidbar, and Devarim. Most Chumashim include commentaries such as Rashi. You use a Chumash for Shnayim Mikra, following along in shul, or learning at home.",
            "chumash",
            "Chumash",
        ),
        line(
            "Torah — Torah means \"teaching\" or \"instruction.\" In the narrow sense it is the five books of the Torah — Genesis through Deuteronomy in English, Bereishit through Devarim in Hebrew — given at Sinai. In shul, the Torah is read from a handwritten Sefer Torah scroll. In study, it can also mean Mishnah, Talmud, halacha, and other works that explain how to live by G-d's will.",
            "torah",
            "Torah",
        ),
        line(
            "parsha — A parsha (parashah) is the weekly Torah portion read in synagogue on Shabbat morning. The Torah is divided into 54 portions so the whole five books are completed each year (with combined portions sometimes depending on the calendar). Shnayim Mikra means studying the same parsha during the week before it is read publicly. The name appears as \"Parshat Bereishit,\" \"Parshat Noach,\" and so on.",
            "parsha",
            "Parsha",
            "Parshat",
            "parashah",
        ),
        line(
            "Sefer Torah — A Sefer Torah is a Torah scroll — the entire five books of the Torah handwritten by a sofer on kosher parchment, rolled on two wooden rollers (atzei chayim). It is read in synagogue with special honor; you usually do not study daily from the scroll the way you use a Chumash. Missing or extra letters can invalidate a scroll, so scribes train for years.",
            "Sefer Torah",
            "sefer torah",
        ),
        line(
            "siddur — A siddur (from seder, \"order\") is the Jewish prayer book with the fixed texts for daily and Shabbat services — blessings, Shema, Amidah, Birkat Hamazon, and more. Editions follow nusach (Ashkenaz, Sefard, Chabad, etc.), so words and order differ slightly. Your siddur is your map for davening.",
            "siddur",
            "Siddur",
        ),
        line(
            "machzor — A machzor is a prayer book for the Jewish festivals and High Holidays — Rosh Hashana, Yom Kippur, Pesach, Sukkot, and Shavuot. It includes texts and prayers not found in the daily siddur. For Rosh Hashana and Yom Kippur, a machzor is required to perform the proper prayers.",
            "machzor",
            "Machzor",
        ),
        line(
            "Mishnah — The Mishnah is the first major written compilation of the Oral Torah, edited around 200 CE. It organizes halacha by topic (Shabbat, blessings, damages, and so on) in concise Hebrew. The Gemara and Talmud Yerushalmi explain and debate the Mishnah. Beginners often start Mishnah before full Talmud.",
            "Mishnah",
            "mishnah",
        ),
        line(
            "Shulchan Aruch — The Shulchan Aruch (\"Set Table\") is the classic 16th-century code of halacha (Jewish Law) by Rabbi Yosef Karo. Ashkenazim often study it with the Rema's glosses; Sephardim generally follow Rabbi Karo, but rabbis have debated about almost everything throughout the years.",
            "Shulchan Aruch",
            "Shulchan Arukh",
        ),
        line(
            "Tanach — Tanach (Tanakh) is the Hebrew Bible in three parts: Torah (the five books), Nevi'im (Prophets), and Ketuvim (Writings). Reading Tanach in Hebrew or translation is Torah study; Psalms (Tehillim) are part of Ketuvim.",
            "Tanach",
            "Tanakh",
        ),
        line(
            "midrash — Midrash is rabbinic teaching that explores Torah stories, ethics, and values — often answering questions the plain text leaves open. Midrash Rabbah and similar collections are not the same as halacha codes; they inspire and teach. Distinguish midrash from pshat (straightforward meaning) when learning Chumash.",
            "Midrash",
            "midrash",
        ),
        line(
            "Rambam — Rambam is Rabbi Moses Maimonides (1138–1204) — physician, philosopher, and halachic giant. His Mishneh Torah codifies halacha in clear Hebrew; his Thirteen Principles summarize Jewish faith. Many beginners meet him through daily halacha summaries or his laws of teshuvah, charity, and Torah study.",
            "Rambam",
            "rambam",
            "Maimonides",
        ),
        line(
            "Pirkei Avot — Pirkei Avot (Ethics of the Fathers) is a tractate of the Mishnah with wisdom sayings about character, community, and love of Torah — often studied on Shabbat afternoon between Pesach and Shavuot. It is not a law manual but a moral compass. Familiar lines include \"The world stands on Torah, service, and acts of kindness.\"",
            "Pirkei Avot",
            "Pirkei Avos",
        ),
        line(
            "Torah study — Torah study (Talmud Torah) means setting aside time to learn G-d's teaching — Chumash, Tanach, Mishnah, Talmud, halacha, or musar. The Mishnah says it outweighs all other mitzvot combined (Peah 1:1). Pirkei Avot teaches that the Shechinah dwells when words of Torah are spoken (Avot 3:2–3); the Talmud lists Torah among the three protectors of life (Sotah 21a). Tradition describes words of Torah ascending as merit and advocacy on high. Men and women have different scopes of obligation, but every Jew who learns bonds with Hashem. Even a few focused minutes daily counts.",
            "Torah study",
            "Talmud Torah",
        ),
        line(
            "sofer — A sofer (scribe) is trained to write STaM — Torah scrolls, tefillin, mezuzot, and megillot — by hand with special ink on parchment. Letters must be formed exactly; mistakes can invalidate the scroll. Soferim also check existing klafim. Never buy tefillin or mezuzot without reliable certification.",
            "Sofer",
            "sofer",
        ),
        line(
            "Ashkenazi — Ashkenazi Jews trace roots to Central and Eastern Europe and follow customs and nusach developed there — Yiddish culture, certain Pesach stringencies (kitniyot), and specific mourning and festival practices. \"Ashkenaz\" in a siddur means that prayer wording. Many American and Israeli shuls are Ashkenazi; ask your rav which sub-tradition applies.",
            "Ashkenazi",
            "Ashkenaz",
        ),
        line(
            "Sephardi — Sephardi Jews trace roots to Iberia and many Mediterranean and Middle Eastern lands, with distinct nusach (Nusach Edot HaMizrach), pronunciation, and customs — rice and legumes are halachically permitted on Pesach where Ashkenazim follow the kitniyot custom. Do not confuse this with \"Nusach Sefard\" on a Chasidic siddur, which is an Ashkenazi rite. Sephardi halacha follows authorities such as the Shulchan Aruch and Rabbi Ovadia Yosef zt\"l.",
            "Sephardi",
            "Sephardic",
            "Sefard",
            "Sefardim",
        ),
        line(
            "Chabad — Chabad is a Chasidic movement founded by the Baal Shem Tov's disciples and led for seven generations by the Lubavitcher Rebbeim. Followers emphasize love of every Jew, joyful mitzvot, and deep study (Chitas: Chumash, Tehillim, Tanya). Prayer follows Nusach Ari. Chabad houses and shluchim welcome beginners without judgment.",
            "Chabad",
            "chabad",
            "Lubavitch",
        ),
        line(
            "chazzan — The chazzan (cantor) leads the congregation in prayer — especially when the service is sung or when a minyan needs a skilled voice for repetition of the Amidah. The role is shaliach tzibur (emissary of the community), not a performer. Many weekday services have no professional chazzan; a member leads instead.",
            "chazzan",
            "Chazzan",
            "cantor",
        ),
        line(
            "shaliach tzibur — A shaliach tzibur is the prayer leader who represents the congregation before G-d — reciting the repetition of the Amidah and guiding pacing. He must be someone the community accepts and who knows the laws of prayer. Women and men have different roles per community in who may lead which parts.",
            "shaliach tzibur",
            "shatz",
        ),
        line(
            "Borei Pri HaGafen — Borei pri hagafen is the blessing on wine and grape juice — \"Who creates the fruit of the vine.\" It is required before Kiddush, the four cups at the Seder, and drinking wine for simcha. After the blessing, drink a meaningful amount (revi'it per many poskim). Ordinary table grapes use a different blessing.",
            "Borei Pri HaGafen",
            "HaGafen",
            "borei pri hagafen",
        ),
        line(
            "Borei Pri HaEtz — Borei pri ha'etz is the blessing on fruit that grows on trees — apples, peaches, grapes (for eating as fruit), and similar. Say it before eating. Fruit from the ground (bananas, strawberries) use borei pri ha'adamah. When in doubt, ask your rav or use a bracha guide.",
            "Borei Pri HaEtz",
            "borei pri ha'etz",
        ),
        line(
            "Borei Pri HaAdamah — Borei pri ha'adamah is the blessing on produce that grows from the earth — potatoes, cucumbers, melons, and many berries. It covers foods that are not tree fruit. Water and plain beverages have their own blessings or none.",
            "Borei Pri HaAdamah",
            "borei pri ha'adamah",
        ),
        line(
            "zeroa — Zeroa (shankbone) is a roasted bone on the Seder plate reminding us of the Pesach sacrifice in the Temple. Today we do not offer the sacrifice; the zeroa is symbolic only — not eaten at the Seder. Many use a chicken neck or a special roasted bone.",
            "Zeroa",
            "zeroa",
        ),
        line(
            "beitzah — Beitzah is a roasted or boiled egg on the Seder plate — another reminder of festival offerings and of mourning for the destroyed Temple. Like the zeroa, it is symbolic; some families eat it during the meal, not as a Seder mitzvah.",
            "Beitzah",
            "beitzah",
        ),
        line(
            "chazeret — Chazeret is the second bitter herb on the Seder plate in some traditions — often romaine lettuce stalk — alongside maror. Not every community uses a separate chazeret; some use only horseradish for maror. Follow your Haggadah and family custom.",
            "Chazeret",
            "chazeret",
        ),
        line(
            "halachic hour — A halachic hour (sha'ah zmanit) is one twelfth of the daylight period from dawn to dusk — so it is longer in summer and shorter in winter, unlike a 60-minute clock hour. Deadlines such as latest morning Shema, chametz sale times, and plag hamincha use these hours. Apps and Jewish calendars convert them for your location.",
            "halachic hour",
            "halachic hours",
            "sha'ah zmanit",
        ),
        line(
            "alot hashachar — Alot hashachar is halachic dawn — when the sky begins to lighten. Many morning laws start here: earliest time for tallit and tefillin, beginning of the day for some fasts, and earliest voluntary morning prayer. It is before sunrise (netz).",
            "alot hashachar",
            "alot haShachar",
        ),
        line(
            "Plag HaMincha — Plag hamincha is one and a quarter halachic hours before nightfall — used for early Mincha, early Shabbat entry in some communities, and certain Pesach and Chanukah times. It is not identical to sunset; check your calendar.",
            "Plag HaMincha",
            "plag hamincha",
            "plag",
        ),
        line(
            "halachic chatzos — Chatzos is halachic midnight or midday — the midpoint of the night or day. Chatzos halayla matters for saying Modeh Ani, starting morning blessings, and some fast practices. Chatzos hayom is the midpoint of daylight. It moves with sunrise and sunset, not with 12:00 on the clock.",
            "halachic chatzos",
            "chatzos",
            "chatzos halayla",
        ),
        line(
            "mayim achronim — Mayim achronim is washing the fingertips after a bread meal and before bentching — a reminder that we eat as servants before G-d. Not every community emphasizes it today, but many siddurim and bentchers include it. It is separate from netilat yadayim before the meal.",
            "Mayim achronim",
            "mayim achronim",
        ),
        line(
            "Gan Eden — Gan Eden (Garden of Eden) in Jewish thought often means the soul's reward after death — closeness to G-d, not a physical vacation. It pairs with Olam HaBa. The original Garden in Bereishit is the starting point of human story; tradition teaches souls return to a heavenly Gan Eden.",
            "Gan Eden",
            "gan eden",
        ),
        line(
            "bitachon — Bitachon is trust in G-d — believing that He provides what you need and that hardship can have purpose even when you cannot see it. It is related to emunah (faith) but stresses calm reliance in daily worry. Mussar and Chasidut both teach practical bitachon.",
            "Bitachon",
            "bitachon",
        ),
        line(
            "Shekhinah — The Shekhinah is G-d's indwelling presence — especially associated with the Temple, with Israel in exile, and with holiness in marriage and Shabbat. Kiddush Levana tradition says blessing the moon is like greeting the Shekhinah. It is not a separate being; it is how we speak of G-d being near.",
            "Shekhinah",
            "Shechina",
            "shekhinah",
        ),
        line(
            "bentcher — A bentcher (birkon) is a small booklet with Birkat Hamazon and sometimes Shabbat songs and benching additions. Keeping one at the table makes bentching after bread meals easy. Many are bilingual; some include zimun instructions and festival inserts.",
            "bentcher",
            "Bentcher",
            "birkon",
        ),
        line(
            "Diaspora — The Diaspora (Galut) means Jewish life outside the Land of Israel. For centuries communities developed local minhagim while staying loyal to Torah. Festival calendars often add an extra day of Yom Tov outside Israel. Aliyah to Israel is a mitzvah for many; Diaspora communities remain fully Jewish and vibrant.",
            "Diaspora",
            "diaspora",
            "Galut",
        ),
        line(
            "chutz la'aretz — Chutz la'aretz means outside the Land of Israel. Halacha differs in some areas — second day of Yom Tov, certain agricultural laws, and some prayer texts. Israelis in chutz la'aretz often follow local practice while visiting; ask your rav for long stays.",
            "chutz la'aretz",
            "chutz laaretz",
        ),
        line(
            "patur — Patur means exempt from a particular mitzvah in a given situation — for example, someone ill on Yom Kippur from fasting, or a child before bar mitzvah. Patur is not \"off the hook\" morally; it means the obligation does not apply to you now. A different mitzvah or leniency may apply instead.",
            "Patur",
            "patur",
        ),
        line(
            "chiyuv — Chiyuv means obligated — the mitzvah applies to you and you are expected to fulfill it. Bar mitzvah creates chiyuv for a boy's mitzvot; other exemptions (illness, danger) can remove chiyuv temporarily. Knowing whether you are chayav or patur is why we ask a rav.",
            "Chiyuv",
            "chiyuv",
            "chayav",
        ),
        line(
            "Aleinu — Aleinu is a closing prayer declaring G-d's sovereignty and our hope for universal recognition of His kingship. It ends daily Shacharit and Maariv and appears near the end of High Holiday services. The bow at \"we bend the knee\" is a moment of submission and awe.",
            "Aleinu",
            "aleinu",
        ),
        line(
            "chesed — Chesed is loving-kindness — going beyond the strict requirement to help another person. Gemilut chasadim (bestowing kindness) includes visiting the sick, burying the dead, and free loans. The world is said to stand on Torah, avodah (service), and gemilut chasadim.",
            "Chesed",
            "chesed",
            "gemilut chasadim",
        ),
        line(
            "Nusach Ari — Nusach Ari is the prayer rite associated with Rabbi Isaac Luria (the Ari) and used by Chabad and some other communities. It blends Ashkenazi and Sephardi elements. Chabad siddurim such as Tehillat Hashem print this nusach. If you daven Nusach Ari, use that siddur consistently for festival inserts.",
            "Nusach Ari",
            "nusach ari",
        ),
        line(
            "Dayeinu — Dayeinu (\"it would have been enough\") is the Seder song listing gifts from the Exodus — each step alone would have justified gratitude. Singing it trains children and adults to notice cumulative kindness from G-d. It appears after the Maggid narrative and before Hallel.",
            "Dayeinu",
            "dayeinu",
        ),
        line(
            "Shir HaMaalot — Shir HaMaalot is Psalm 126 — \"When G-d returns the captivity of Zion\" — sung before bentching on Shabbat and festivals. It expresses joy in redemption and hope for complete return. Many know the tune from Shabbat tables.",
            "Shir HaMaalot",
            "shir ha maalot",
        ),
        line(
            "trop — Trop (ta'amei hamikra) are the cantillation marks above and below Hebrew in a Chumash that tell the baal koreh how to chant Torah — melody and phrase breaks. Learning trop is a skill separate from understanding the words. Congregants follow the chant in their books during the reading.",
            "Trop",
            "trop",
            "ta'amim",
        ),
        line(
            "Tanya — The Tanya is Rabbi Schneur Zalman of Liadi's foundational Chabad work on the Jewish soul, divine service, and practical mysticism. Its full Hebrew name is Likutei Amarim (\"collected sayings\"). Chabad's daily Chitas program includes a portion of Tanya each day alongside Chumash and Tehillim. You can read it in Hebrew or English on Sefaria.",
            "Tanya",
            "tanya",
            "Likutei Amarim",
        ),
        line(
            "Chitas — Chitas (חיט\"ס) is Chabad's daily study cycle: Chumash (weekly Torah portion with Rashi), Tehillim (Psalms divided by day of month), and Tanya. Many complete it after morning prayers, but you may learn today's portions anytime before the next Hebrew day. Hayom Yom is an additional short daily reading.",
            "Chitas",
            "chitas",
            "CHITAS",
        ),
        line(
            "Hayom Yom — Hayom Yom (\"Today is…\") is a Chabad calendar of brief teachings — a saying, story, or law for each day of the year. It is often studied as part of daily Chitas after Chumash, Tehillim, and Tanya. Entries are short and practical, meant for busy schedules.",
            "Hayom Yom",
            "hayom yom",
        ),
        line(
            "Mussar — Mussar is the Jewish discipline of ethical and character development — refining anger, arrogance, laziness, and speech through study and practice. Classic works include Mesillat Yesharim and Ale Shur. It complements halacha (what to do) with work on who you are becoming.",
            "Mussar",
            "mussar",
            "Musar",
        ),
        line(
            "Chassidut — Chassidut (Chasidic philosophy) teaches serving G-d with joy, sincerity, and awareness that divine sparks fill all of life. Founded by the Baal Shem Tov, it spread through courts like Chabad, Breslov, and Satmar. Tanya is a central Chassidut text for daily study.",
            "Chassidut",
            "Chassidus",
            "chassidut",
            "chasidut",
        ),
        line(
            "Kabbalah — Kabbalah is the Jewish mystical tradition exploring how G-d relates to creation, the soul, and mitzvot. Texts include the Zohar and later works like Tanya. Authentic Kabbalah is studied with Torah literacy and a teacher; it is not fortune-telling or magic.",
            "Kabbalah",
            "kabbalah",
            "Kabala",
        ),
        line(
            "Zohar — The Zohar is the classic work of Kabbalah, attributed to Rabbi Shimon bar Yochai, written in Aramaic. It illuminates the Torah's inner dimensions. Many study a daily snippet; full mastery requires Hebrew/Aramaic and guidance.",
            "Zohar",
            "zohar",
        ),
        line(
            "Kitzur Shulchan Arukh — The Kitzur Shulchan Arukh is a condensed summary of daily halacha — prayer, Shabbat, kashrut, festivals, and family life — written by Rabbi Shlomo Ganzfried. It is a practical first halacha book for beginners and a quick reference for experienced Jews.",
            "Kitzur Shulchan Arukh",
            "Kitzur Shulchan Aruch",
            "kitzur",
        ),
        line(
            "Nevi'im — Nevi'im (Prophets) is the middle section of Tanach — Joshua, Kings, Isaiah, Jeremiah, and the other prophetic books. They call Israel to justice and loyalty to Hashem. Reading them is Torah study.",
            "Nevi'im",
            "Neviim",
            "Prophets",
        ),
        line(
            "Ketuvim — Ketuvim (Writings) is the third section of Tanach — Psalms (Tehillim), Proverbs, Esther, Ruth, Daniel, and others. It includes poetry, history, and wisdom literature. Tehillim is the most commonly recited book for comfort and praise.",
            "Ketuvim",
            "Kesuvim",
            "Writings",
        ),
        line(
            "Peninei Halakha — Peninei Halakha is Rabbi Eliezer Melamed's modern Hebrew halacha series (available in English online), organized by topic — Shabbat, festivals, prayer, family, and Israel. It is clear, sourced, and widely used for practical questions.",
            "Peninei Halakha",
            "Peninei Halacha",
        ),
        line(
            "Baal Shem Tov — The Baal Shem Tov (Rabbi Israel ben Eliezer, c. 1700–1760) founded the Chasidic movement, teaching that every Jew can serve G-d with joy and that divine presence fills all creation. Chabad traces its spiritual lineage to his disciples.",
            "Baal Shem Tov",
            "Besht",
        ),
        line(
            "pshat — Pshat is the plain, straightforward meaning of a Torah verse — what the text says on its surface before deeper layers. Rashi on Chumash mainly explains pshat. Learning pshat is the foundation before midrash or Kabbalah.",
            "pshat",
            "Pshat",
        ),
        line(
            "aggadah — Aggadah is the non-legal storytelling and teaching in the Talmud and midrash — ethics, theology, and narrative. It complements halacha (law). Shabbat table stories and many midrashim are aggadah.",
            "Aggadah",
            "aggadah",
        ),
    ).mapNotNull { it }

    private val allTerms: List<HalachicTerm> by lazy {
        val merged = fromBeginnerGlossary + supplemental
        val byId = linkedMapOf<String, HalachicTerm>()
        for (term in merged) {
            val existing = byId[term.id]
            if (existing == null) {
                byId[term.id] = term
            } else {
                val labels = (existing.matchLabels + term.matchLabels).distinctBy { it.lowercase() }
                byId[term.id] = existing.copy(
                    matchLabels = labels,
                    definition = if (term.definition.length > existing.definition.length) term.definition else existing.definition,
                )
            }
        }
        for (term in enrichedOverrides) {
            val existing = byId[term.id]
            if (existing == null) {
                byId[term.id] = term
            } else {
                val labels = (existing.matchLabels + term.matchLabels).distinctBy { it.lowercase() }
                byId[term.id] = term.copy(matchLabels = labels)
            }
        }
        byId.values.toList()
    }

    private val byIdMap: Map<String, HalachicTerm> by lazy {
        allTerms.associateBy { it.id }
    }

    private val baseMatchers: List<Pair<String, HalachicTerm>> by lazy {
        buildMatchers(allTerms)
    }

    fun termById(id: String): HalachicTerm? = byIdMap[id]

    fun findMatches(
        text: String,
        excludeRanges: List<IntRange> = emptyList(),
        additionalTerms: List<HalachicTerm> = emptyList(),
    ): List<HalachicTermMatch> {
        if (text.isBlank()) return emptyList()
        val matchers = if (additionalTerms.isEmpty()) {
            baseMatchers
        } else {
            buildMatchers(allTerms + additionalTerms)
        }
        val multiWordLabels = matchers.map { it.first }.filter { ' ' in it }
        val longerPhraseRanges = longerPhraseRangesIn(text, multiWordLabels)
        val candidates = mutableListOf<HalachicTermMatch>()
        for ((label, term) in matchers) {
            var start = 0
            while (start < text.length) {
                val idx = text.indexOf(label, start, ignoreCase = true)
                if (idx < 0) break
                val end = idx + label.length
                if (
                    isWordBoundary(text, idx, end) &&
                    !overlapsAny(idx until end, excludeRanges) &&
                    !isInsideLongerPhrase(idx until end, label, longerPhraseRanges)
                ) {
                    candidates.add(
                        HalachicTermMatch(
                            start = idx,
                            end = end,
                            term = term,
                            matchedText = text.substring(idx, end),
                        )
                    )
                }
                start = idx + 1
            }
        }
        return firstOccurrencePerTerm(selectNonOverlapping(candidates))
    }

    /** One underline per glossary entry per text block — first match in reading order only. */
    private fun firstOccurrencePerTerm(matches: List<HalachicTermMatch>): List<HalachicTermMatch> {
        val seen = mutableSetOf<String>()
        return matches.filter { seen.add(it.term.id) }
    }

    private fun longerPhraseRangesIn(text: String, multiWordLabels: List<String>): List<IntRange> {
        val ranges = mutableListOf<IntRange>()
        for (longer in multiWordLabels) {
            var search = 0
            while (search < text.length) {
                val idx = text.indexOf(longer, search, ignoreCase = true)
                if (idx < 0) break
                val end = idx + longer.length
                if (isWordBoundary(text, idx, end)) {
                    ranges.add(idx until end)
                }
                search = idx + 1
            }
        }
        return ranges
    }

    /**
     * Skip matching a single word when it falls inside a longer glossary phrase in the same text
     * (e.g. "Kiddush" inside "Kiddush Levana" or "Havdalah in Kiddush", "Shabbat" inside
     * "Motzei Shabbat", "mitzvah" inside "bar mitzvah").
     */
    private fun isInsideLongerPhrase(
        range: IntRange,
        label: String,
        longerPhraseRanges: List<IntRange>,
    ): Boolean {
        if (' ' in label) return false
        return longerPhraseRanges.any { longer -> range.first >= longer.first && range.last <= longer.last }
    }

    private fun selectNonOverlapping(candidates: List<HalachicTermMatch>): List<HalachicTermMatch> {
        val sorted = candidates.sortedWith(
            compareByDescending<HalachicTermMatch> { it.end - it.start }.thenBy { it.start }
        )
        val picked = mutableListOf<HalachicTermMatch>()
        for (candidate in sorted) {
            if (picked.none { overlaps(it.start until it.end, candidate.start until candidate.end) }) {
                picked.add(candidate)
            }
        }
        return picked.sortedBy { it.start }
    }

    private fun overlaps(a: IntRange, b: IntRange): Boolean =
        a.first <= b.last && b.first <= a.last

    private fun overlapsAny(range: IntRange, others: List<IntRange>): Boolean =
        others.any { overlaps(range, it) }

    private fun isWordBoundary(text: String, start: Int, end: Int): Boolean {
        val before = text.getOrNull(start - 1)
        val after = text.getOrNull(end)
        return !before.isTermInteriorChar(text, start - 1) &&
            !after.isTermInteriorChar(text, end)
    }

    /**
     * Apostrophe is interior only inside a word (b'Makom, ha'eish), not when quoting
     * a phrase ('yad soledet bo').
     */
    private fun Char?.isTermInteriorChar(text: String, index: Int): Boolean {
        if (this == null) return false
        if (isLetterOrDigit()) return true
        if (this == '\'') {
            val prev = text.getOrNull(index - 1)
            val next = text.getOrNull(index + 1)
            return prev?.isLetterOrDigit() == true && next?.isLetterOrDigit() == true
        }
        return false
    }

    /** Annotation tag stored in [androidx.compose.ui.text.AnnotatedString]. */
    fun annotationTag(): String = TERM_TAG

    /**
     * Single-word aliases of a multi-word term (e.g. "challah" on "hafrashat challah") only match
     * when that word is the term's primary title — otherwise the full phrase must appear together.
     */
    private fun effectiveMatchLabels(term: HalachicTerm): List<String> {
        val labels = term.matchLabels
        val multiWordLabels = labels.filter { it.contains(' ') }
        if (multiWordLabels.isEmpty()) return labels
        return labels.filter { label ->
            when {
                label.contains(' ') -> true
                label.equals(term.title, ignoreCase = true) -> true
                else -> multiWordLabels.none { multi -> containsWholeWord(multi, label) }
            }
        }
    }

    private fun containsWholeWord(text: String, word: String): Boolean {
        if (word.isBlank()) return false
        val pattern = """(?i)(?<![A-Za-z0-9'])${Regex.escape(word)}(?![A-Za-z0-9'])"""
        return Regex(pattern).containsMatchIn(text)
    }

    private fun buildMatchers(terms: List<HalachicTerm>): List<Pair<String, HalachicTerm>> =
        terms
            .flatMap { term -> effectiveMatchLabels(term).map { label -> label to term } }
            .sortedByDescending { it.first.length }
}
