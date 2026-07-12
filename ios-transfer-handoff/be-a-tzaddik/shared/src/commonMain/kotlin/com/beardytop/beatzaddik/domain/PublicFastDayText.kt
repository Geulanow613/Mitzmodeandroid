package com.beardytop.beatzaddik.domain

object PublicFastDayText {

    // ── Catalog template keys for bundled translation ────────────────────────────

    private const val FAST_DAY_SUBTITLE_TEMPLATE = "{name} · {timing}{endSuffix}"
    // Sub-template placeholders are deliberately left UNRESOLVED when passed as args to a bigger
    // template — see [alotLine] doc for why (a pre-filled clock time can never match a catalog key).
    // Each constant below uses its own placeholder name so several can coexist in one args map
    // without colliding once `fillTranslationTemplate`'s second pass resolves them.
    private const val APPROX_TIME_SUFFIX = " (approx. ${'$'}time)"
    private const val ALOT_LINE_WITH_TIME_TEMPLATE =
        " (approx. ${'$'}time — enable location for your exact zman)"
    private const val ALOT_LINE_NO_LOCATION = " — enable location in Settings for your dawn time"
    private const val TZET_TOMORROW_TIME_TEMPLATE = "approx. ${'$'}tzeitTime tomorrow night"
    private const val TZET_TOMORROW_FALLBACK = "nightfall tomorrow night"
    private const val TZET_LINE_TEMPLATE = " (approx. ${'$'}time tonight)"

    private const val TENTH_OF_TEVES_FRIDAY_NOTE =
        "\n\n10 Tevet on Friday: This is the only public fast that is never postponed. If it falls on Friday, you still fast until nightfall, then break the fast with Shabbat Kiddush and your Friday night meal (Shulchan Arukh O.C. 249:4; Mishnah Berurah)."
    private const val FAST_OF_ESTHER_FRIDAY_NOTE =
        "\n\nFast of Esther note: Under the fixed Hebrew calendar, Taanit Esther does not occur on Friday. When 13 Adar is Shabbat, the fast is moved back to Thursday (Shulchan Arukh O.C. 686)."
    private const val SHABBAT_EREV_TISHA_BEAV_NOTE = """

Shabbat Erev Tisha B'Av (when 9 Av is Shabbat and the fast is moved to Sunday):
• Shabbat is celebrated fully until sunset — no mourning restrictions on Shabbat itself.
• After Shabbat ends (Havdalah), change into non-leather shoes and begin the fast and mourning practices.
• The seudah hamafseket is not eaten on Shabbat — it is observed after Shabbat ends, before the fast begins."""

    /** Raw, unfilled `" (approx. $time)"` template — caller must add `"time" to actualTime` to its args map. */
    private fun approxTimeSuffixTemplate(): String = APPROX_TIME_SUFFIX

    /**
     * Returns the RAW sub-template (its own `$time` placeholder left unresolved) rather than a
     * pre-filled string. The pre-filled form can never match a catalog key once the clock time
     * varies, which left this fragment permanently stuck in English inside otherwise-translated
     * dialogs. Callers must also add `"time" to alotTime` to the outer args map so the two-pass
     * template filler (see [com.beardytop.beatzaddik.ui.translation.fillTranslationTemplate])
     * resolves it after the sub-template itself gets translated as a standalone catalog key.
     */
    private fun alotLine(alotTime: String?): String =
        if (alotTime != null) ALOT_LINE_WITH_TIME_TEMPLATE else ALOT_LINE_NO_LOCATION

    private const val EREV_MINOR_FAST_PREP_TITLE_TEMPLATE =
        "Prepare for tomorrow's fast — ${'$'}fastName"

    fun erevMinorFastPrepTitleTemplate(): String = EREV_MINOR_FAST_PREP_TITLE_TEMPLATE

    fun erevMinorFastPrepTitle(fastName: String): String =
        ExplainerTemplateFill.fill(erevMinorFastPrepTitleTemplate(), mapOf("fastName" to fastName))

    fun erevMinorFastPrepExplanation(
        cal: DayInfo,
        tomorrowFastIdx: Int,
        profile: UserProfile,
    ): String = ExplainerTemplateFill.fill(
        erevMinorFastPrepTemplate(),
        erevMinorFastPrepArgs(cal, tomorrowFastIdx, profile),
    )

    fun erevYomKippurTitle(): String = "Erev Yom Kippur — eat and prepare"

    fun erevYomKippurExplanation(cal: DayInfo, profile: UserProfile): String =
        ExplainerTemplateFill.fill(erevYomKippurTemplate(), erevYomKippurArgs(cal, profile))

    fun erevTishaBeavTitle(): String = "Erev Tisha B'Av — restrictions and seudah hamafseket"

    fun erevTishaBeavExplanation(cal: DayInfo, profile: UserProfile): String =
        ExplainerTemplateFill.fill(erevTishaBeavTemplate(), erevTishaBeavArgs(cal, profile))

    private const val FAST_DAY_TITLE_TEMPLATE = "Fast — ${'$'}name"

    fun fastDayTitleTemplate(): String = FAST_DAY_TITLE_TEMPLATE

    fun fastDayTitle(fastIdx: Int): String =
        ExplainerTemplateFill.fill(
            fastDayTitleTemplate(),
            mapOf("name" to PublicFastDayRules.displayName(fastIdx)),
        )

    fun fastDaySubtitleTemplate(): String = FAST_DAY_SUBTITLE_TEMPLATE

    fun fastDaySubtitleArgs(fastIdx: Int, zmanim: ZmanimSnapshot?, timezoneId: String): Map<String, String> {
        val tz = zmanim?.timezoneId ?: timezoneId
        val end = ZmanimFormatter.formatTime(zmanim?.tzeitMillis, tz)
        return mapOf(
            "name" to PublicFastDayRules.displayName(fastIdx),
            "timing" to PublicFastDayRules.fastTimingLabel(fastIdx),
            "endSuffix" to (end?.let { approxTimeSuffixTemplate() } ?: ""),
            "time" to (end ?: ""),
        )
    }

    fun fastDaySubtitle(fastIdx: Int, zmanim: ZmanimSnapshot?, timezoneId: String): String =
        ExplainerTemplateFill.fill(fastDaySubtitleTemplate(), fastDaySubtitleArgs(fastIdx, zmanim, timezoneId))

    fun fastDayExplanation(fastIdx: Int, cal: DayInfo, profile: UserProfile): String =
        ExplainerTemplateFill.fill(
            publicFastDayTemplate(fastIdx),
            publicFastDayArgs(fastIdx, cal, profile),
        )

    fun motzeiYomKippurMealTitle(): String = "Motzei Yom Kippur — break-fast meal"

    fun motzeiYomKippurMealExplanation(cal: DayInfo, profile: UserProfile): String =
        ExplainerTemplateFill.fill(
            motzeiYomKippurMealTemplate(),
            motzeiYomKippurMealArgs(cal, profile),
        )

    fun erevMinorFastLinks() = listOf(
        ChecklistLink(
            "Sefaria — Tanai before a fast (OC 564)",
            "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.564",
        ),
        ChecklistLink(
            "Peninei Halacha — Public fast days",
            "https://ph.yhb.org.il/en/05-01-01/",
        ),
    )

    fun erevYomKippurLinks(profile: UserProfile) = buildList {
        add(
            ChecklistLink(
                "Sefaria — Erev Yom Kippur (OC 604)",
                "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.604",
            )
        )
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(
                ChecklistLink(
                    "Chabad — Erev Yom Kippur",
                    "https://www.chabad.org/library/article_cdo/aid/177886/jewish/Erev-Yom-Kippur.htm",
                )
            )
        }
    }

    fun erevTishaBeavLinks() = listOf(
        ChecklistLink(
            "Sefaria — Erev Tisha B'Av (OC 552–553)",
            "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.552",
        ),
        ChecklistLink(
            "Peninei Halacha — Erev Tisha B'Av",
            "https://ph.yhb.org.il/en/09-08-01/",
        ),
    )

    fun fastDayLinks(fastIdx: Int, profile: UserProfile) = buildList {
        add(
            ChecklistLink(
                "Sefaria — Fasting (OC 549–550)",
                "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.549",
            )
        )
        when (fastIdx) {
            HebrewCalendarEngine.TISHA_BEAV -> add(
                ChecklistLink(
                    "Peninei Halacha — Tisha B'Av",
                    "https://ph.yhb.org.il/en/09-09-01/",
                )
            )
            HebrewCalendarEngine.YOM_KIPPUR -> if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
                add(
                    ChecklistLink(
                        "Chabad — Yom Kippur",
                        "https://www.chabad.org/holidays/JewishNewYear/template_cdo/aid/177886/jewish/Yom-Kippur.htm",
                    )
                )
            }
            HebrewCalendarEngine.FAST_OF_ESTHER -> add(
                ChecklistLink(
                    "Sefaria — Taanit Esther (OC 686)",
                    "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.686",
                )
            )
            else -> Unit
        }
    }

    /** Catalog keys — must match [tools/_write_seasonal_arg_explainers.py]. */
    private const val FAST_GEDALIAH_MEANING =
        "commemorates the assassination of Gedaliah ben Achikam after the First Temple's destruction."
    private const val FAST_TENTH_TEVES_MEANING =
        "marks the beginning of the siege of Jerusalem. Never postponed from Friday."
    private const val FAST_SEVENTEEN_TAMMUZ_MEANING =
        "marks the breaching of Jerusalem's walls and the start of the Three Weeks mourning period."

    /** Catalog key — must match [tools/_public_fast_template_data.py] COMMON_FAST_LAWS. */
    private const val COMMON_FAST_LAWS = """Who must fast:
Jewish adults from bar/bat mitzvah age who are healthy enough to fast. Children are trained gradually — ask your rav. Women who are pregnant, nursing, or recently gave birth, and anyone who is ill, should ask a posek — many eat in measured amounts (shiurim) or are fully exempt.

If you ate by mistake:
If you forgot and ate or drank unintentionally, you may continue fasting once you remember — the fast remains valid (Shulchan Arukh O.C. 568:1).

If you cannot fast:
Do not endanger your health. Ask a rav about shiurim (small amounts at intervals), postponing the fast, or exemption. Pikuach nefesh overrides fasting."""

    private fun commonFastLawsBlock(): String = COMMON_FAST_LAWS

    // ── Template keys for bundled translation ────────────────────────────────────

    private val EREV_MINOR_FAST_TEMPLATE = """
Tomorrow is ${'$'}fastName — a public fast from dawn (alot hashachar) until nightfall (tzeit).

If you plan to eat before the fast begins:
• Set a mental condition (tanai) the night before: "If I wake up hungry before dawn, I will eat." Without this condition, waking early and eating may prohibit you from eating again until the fast officially begins at dawn (Shulchan Arukh O.C. 564:1).
• If you wake before dawn and want to eat before the fast begins, stick to water, coffee, fruit, or a very small snack. Establishing a formal meal or eating a significant amount of baked goods or bread is restricted if you wake up early to eat.
• Stop all eating and drinking at alot hashachar${'$'}alotLine.

Practical prep tonight:
• Hydrate well and eat a balanced dinner.
• Plan a lighter morning if you will not eat before dawn.
• Know your synagogue schedule if you plan to attend special prayers.

Who must fast: Jewish adults (bar/bat mitzvah age and older) in good health. Children below bar/bat mitzvah are not required to fast — train them gradually per your rav.${'$'}fridayNote
    """.trimIndent()

    fun erevMinorFastPrepTemplate(): String = EREV_MINOR_FAST_TEMPLATE

    fun erevMinorFastPrepArgs(
        cal: DayInfo,
        tomorrowFastIdx: Int,
        profile: UserProfile,
    ): Map<String, String> {
        val fastName = PublicFastDayRules.displayName(tomorrowFastIdx)
        val tz = cal.zmanim?.timezoneId ?: profile.timezoneId
        // The fast starts at TOMORROW's dawn — nightObligationsEndMillis is tomorrow's alot.
        val alotTomorrow = cal.zmanim?.let { z ->
            ZmanimFormatter.formatTime(
                z.nightObligationsEndMillis ?: z.alotHaShacharMillis,
                tz,
            )
        }
        val alotLine = alotLine(alotTomorrow)
        val fridayNote = when (tomorrowFastIdx) {
            HebrewCalendarEngine.TENTH_OF_TEVES -> TENTH_OF_TEVES_FRIDAY_NOTE
            HebrewCalendarEngine.FAST_OF_ESTHER -> FAST_OF_ESTHER_FRIDAY_NOTE
            else -> ""
        }
        return mapOf(
            "fastName" to fastName,
            "alotLine" to alotLine,
            "time" to (alotTomorrow ?: ""),
            "fridayNote" to fridayNote,
        )
    }

    private val EREV_YOM_KIPPUR_TEMPLATE = """
Today is Erev Yom Kippur — a unique day of eating before the holiest fast of the year.

Mitzvah to eat today:
• It is a mitzvah to eat and drink generously all day Erev Yom Kippur (Shulchan Arukh O.C. 604:1; Mishnah Berurah).
• Have a festive Yom Tov-style meal — many begin with candle lighting and Kiddush as for Yom Tov (ask your rav for your minhag).
• The Talmud compares eating on Erev Yom Kippur to fasting on Yom Kippur itself for merit (Rosh Hashana 9a).

Before the fast begins:
• Halachic fast time: Yom Kippur runs from sunset tonight${'$'}sunsetLine until nightfall tomorrow (${'$'}tzeitTomorrow).
• Tosefet (adding from the weekday): Many communities begin abstaining from food and drink somewhat before sunset — accepting the fast early, as we "add from the weekday onto the holy day" (Yoma 81b; Shulchan Arukh O.C. 608:1). How many minutes early varies by community; ask your rav. The sunset time in this app is when the fast is definitely in effect, not necessarily when your synagogue begins Kol Nidrei.
• Kol Nidrei before sunset: Kol Nidrei is the annulment of vows (hatarat nedarim) before the Day of Atonement. A beit din cannot convene on Yom Kippur itself, so Kol Nidrei must be completed before sunset while it is still weekday (Rosh Hashana 9b; Shulchan Arukh O.C. 619:1). That is why services often start well before sunset — so the congregation can finish Kol Nidrei and begin the fast with tosefet while it is still erev.
• Ask forgiveness from those you may have hurt; give tzedakah; immerse in a mikveh if that is your custom.
• Light Yom Kippur candles before sunset (married women traditionally light; others follow community — ask your rav).

Motzei Yom Kippur meal:
• After the fast ends tomorrow night, it is a mitzvah to eat a proper meal — not only a snack. Many have a festive break-fast.
• Chabad tradition (Baal HaTanya) teaches that one's livelihood (parnassa) for the year is especially connected to this post-Yom Kippur meal — eat with joy and intention after davening and Havdalah.

Who fasts tomorrow: Healthy Jewish adults from bar/bat mitzvah age. Those who are ill, pregnant, nursing, or recently gave birth should ask a rav — often they eat in smaller amounts (shiurim) or are exempt.
    """.trimIndent()

    fun erevYomKippurTemplate(): String = EREV_YOM_KIPPUR_TEMPLATE

    fun erevYomKippurArgs(cal: DayInfo, profile: UserProfile): Map<String, String> {
        val tz = cal.zmanim?.timezoneId ?: profile.timezoneId
        val sunset = ZmanimFormatter.formatTime(cal.zmanim?.sunsetMillis, tz)
        val tzeitTime = ZmanimFormatter.formatTime(cal.zmanim?.tzeitMillis, tz)
        val tzeitTomorrow = if (tzeitTime != null) TZET_TOMORROW_TIME_TEMPLATE else TZET_TOMORROW_FALLBACK
        return mapOf(
            "sunsetLine" to (sunset?.let { approxTimeSuffixTemplate() } ?: ""),
            "time" to (sunset ?: ""),
            "tzeitTomorrow" to tzeitTomorrow,
            "tzeitTime" to (tzeitTime ?: ""),
        )
    }

    private val EREV_TISHA_BEAV_TEMPLATE = """
Erev Tisha B'Av prepares us for mourning the destruction of the Temple.

When the fast begins:
• Tisha B'Av starts at sunset tonight${'$'}sunsetLine and continues until nightfall tomorrow night — not dawn to dusk like minor fasts.

Restrictions from chatzos (halachic midday) today:
• From chatzos onward: do not eat meat or drink wine (some extend other restrictions — follow your minhag).
• Reduce pleasurable activities; avoid laundry, haircuts, and swimming per mourning customs.

Seudah hamafseket — the final meal before the fast:
• Eat a simple meal alone, sitting on the floor or a low stool, with only one cooked dish (e.g. a hard-boiled egg dipped in ashes, or bread with cold water).
• Do not eat two cooked dishes together; do not recline; do not greet others with "hello."
• Finish before sunset; afterward, only water is permitted until the fast begins.
• Many recite Birkat HaMazon and then change into non-leather shoes before sunset.

After sunset tonight until nightfall tomorrow:
• Five afflictions apply (like Yom Kippur): no eating/drinking, no leather shoes, no bathing for pleasure, no anointing, no marital relations.
• Additionally: no Torah study except sad topics (Eichah, Job, mourning laws); many sit on the floor until chatzos tomorrow; kinot are recited.${'$'}shabbatNote

Ask your rav for details if you are ill, pregnant, or nursing.
    """.trimIndent()

    fun erevTishaBeavTemplate(): String = EREV_TISHA_BEAV_TEMPLATE

    fun erevTishaBeavArgs(cal: DayInfo, profile: UserProfile): Map<String, String> {
        val tz = cal.zmanim?.timezoneId ?: profile.timezoneId
        val sunset = ZmanimFormatter.formatTime(cal.zmanim?.sunsetMillis, tz)
        val shabbatNote = if (cal.isShabbat) SHABBAT_EREV_TISHA_BEAV_NOTE else ""
        return mapOf(
            "sunsetLine" to (sunset?.let { approxTimeSuffixTemplate() } ?: ""),
            "time" to (sunset ?: ""),
            "shabbatNote" to shabbatNote,
        )
    }

    fun fastDayExplanationTemplate(fastIdx: Int): String = publicFastDayTemplate(fastIdx)

    fun publicFastDayTemplate(fastIdx: Int): String = when (fastIdx) {
        HebrewCalendarEngine.FAST_OF_GEDALYAH,
        HebrewCalendarEngine.TENTH_OF_TEVES,
        HebrewCalendarEngine.SEVENTEEN_OF_TAMMUZ -> MINOR_FAST_DAY_TEMPLATE
        HebrewCalendarEngine.FAST_OF_ESTHER -> ESTHER_FAST_DAY_TEMPLATE
        HebrewCalendarEngine.YOM_KIPPUR -> YOM_KIPPUR_FAST_DAY_TEMPLATE
        HebrewCalendarEngine.TISHA_BEAV -> TISHA_FAST_DAY_TEMPLATE
        else -> ""
    }

    fun fastDayExplanationArgs(fastIdx: Int, cal: DayInfo, profile: UserProfile): Map<String, String> =
        publicFastDayArgs(fastIdx, cal, profile)

    fun publicFastDayArgs(fastIdx: Int, cal: DayInfo, profile: UserProfile): Map<String, String> {
        val common = commonFastLawsBlock()
        val tz = cal.zmanim?.timezoneId ?: profile.timezoneId
        val tzeitTime = ZmanimFormatter.formatTime(cal.zmanim?.tzeitMillis, tz)
        val tzeitSuffix = if (tzeitTime != null) approxTimeSuffixTemplate() else ""
        return when (fastIdx) {
            HebrewCalendarEngine.FAST_OF_GEDALYAH -> mapOf(
                "name" to PublicFastDayRules.displayName(fastIdx),
                "meaning" to FAST_GEDALIAH_MEANING,
                "common" to common,
            )
            HebrewCalendarEngine.TENTH_OF_TEVES -> mapOf(
                "name" to PublicFastDayRules.displayName(fastIdx),
                "meaning" to FAST_TENTH_TEVES_MEANING,
                "common" to common,
            )
            HebrewCalendarEngine.FAST_OF_ESTHER -> mapOf("common" to common)
            HebrewCalendarEngine.SEVENTEEN_OF_TAMMUZ -> mapOf(
                "name" to PublicFastDayRules.displayName(fastIdx),
                "meaning" to FAST_SEVENTEEN_TAMMUZ_MEANING,
                "common" to common,
            )
            HebrewCalendarEngine.YOM_KIPPUR -> mapOf(
                "tzeitSuffix" to tzeitSuffix,
                "time" to (tzeitTime ?: ""),
                "common" to common,
            )
            HebrewCalendarEngine.TISHA_BEAV -> mapOf(
                "tzeitSuffix" to tzeitSuffix,
                "time" to (tzeitTime ?: ""),
                "tefillinNote" to TishaBeavTefillinRules.fastDayTefillinNote(profile.effectiveNusach()),
                "travelNote" to TishaBeavTefillinRules.FAST_DAY_TRAVEL_NOTE,
                "common" to common,
            )
            else -> emptyMap()
        }
    }

    private val MINOR_FAST_DAY_TEMPLATE = """
Today is ${'$'}name — ${'$'}meaning

Minor fast rules:
• No eating or drinking from dawn until nightfall.
• Washing, music, and showering are generally permitted (unlike Tisha B'Av and Yom Kippur).
• Special selichot and prayers may be recited in synagogue — check your community schedule.

${'$'}common
    """.trimIndent()

    private val ESTHER_FAST_DAY_TEMPLATE = """
Today is the Fast of Esther (Taanit Esther) — commemorating the fasting Esther called before approaching the king (Esther 4:16).

Minor fast rules with Purim context:
• No eating or drinking from dawn until nightfall.
• Music, showering for pleasure, and leather shoes are permitted — this is a minor fast, not like Tisha B'Av.
• Purim mitzvot begin tonight/tomorrow per your calendar (14 Adar, or 15 in walled cities). This fast is the spiritual preparation before the joy of Purim.

${'$'}common
    """.trimIndent()

    private val YOM_KIPPUR_FAST_DAY_TEMPLATE = """
Today is Yom Kippur — the Day of Atonement.

The fast:
• Began at sunset last night (many communities accepted it earlier with tosefet — adding time from erev onto Yom Kippur) and ends at nightfall tonight${'$'}tzeitSuffix.
• Five afflictions: no eating/drinking, no leather shoes, no bathing for pleasure, no anointing, no marital relations.
• Many wear white; married men who normally wear a kittel do so; tallit is worn all day.
• The day is spent in prayer — Kol Nidrei was last night before sunset (hatarat nedarim cannot be done on Yom Kippur itself), then Shacharit, Musaf, Mincha, Neilah, then Maariv and Havdalah after nightfall.

After nightfall: eat the Motzei Yom Kippur meal (see your checklist). Chabad tradition connects this meal to parnassa for the year.

${'$'}common
    """.trimIndent()

    private val TISHA_FAST_DAY_TEMPLATE = """
Today is Tisha B'Av — mourning the destruction of both Temples and our exile.

The fast:
• Began at sunset last night and ends at nightfall tonight${'$'}tzeitSuffix — not a dawn-to-dusk fast.

What is forbidden today (in addition to eating and drinking):
• Leather shoes; bathing for pleasure; anointing with cream or cologne; marital relations.
• Torah study except for mournful passages (Eichah, Iyov, parts of Yirmiyahu, halachot of mourning).
• Greeting people with "hello" during the fast day — even after chatzos, do not greet or reply to greetings until nightfall when the fast ends.
• Idle conversation; work is discouraged (follow your community).
• ${'$'}tefillinNote
• Until chatzos (halachic midday): maintain a mournful mindset; sit on the floor or a low stool (no seat higher than about 12 inches / 30 cm); kinot (elegies) are recited at Shacharit.
• Hand washing: only to remove ritual impurity — wash only to the knuckle where fingers join the hand; or to remove actual dirt on the hand.
• Do not brush teeth with water; a dry toothbrush is permitted. Flossing is permitted.
• ${'$'}travelNote

What is permitted (unlike Yom Kippur in some communities):
• Music prohibitions are mourning-related — live joyful music is avoided as part of the Three Weeks/Nine Days spirit, but the fast's core prohibitions are the five afflictions above.

After the fast day is over, after nightfall when the fast ends: some mourning restrictions of the Nine Days begin to lift — but Ashkenazi custom continues meat, wine, music, laundry, and bathing for pleasure until chatzos (halachic midday) on 10 Av (not at nightfall of 9 Av itself). When 9 Av was Shabbat and the fast was Sunday, laundry, bathing, and haircuts may be permitted Motzei fast for Ashkenazim; meat and wine often wait until Monday morning. Note for Sephardic users: when the fast is pushed to Sunday, restrictions on laundry, haircuts, and bathing lift immediately on Sunday night; only meat and wine are restricted until Monday morning. When 10 Av is Friday, haircuts, laundry, and bathing may be permitted Friday morning for Shabbat prep; meat and wine for Ashkenazim often until Friday chatzos. Ask your rav before resuming.

${'$'}common
    """.trimIndent()

    private val MOTZEI_YOM_KIPPUR_MEAL_TEMPLATE = """
After Yom Kippur ends at nightfall${'$'}tzeitLine, it is a mitzvah to eat a proper meal — not only a quick bite.

What to do:
• Complete Maariv and Havdalah (Havdalah includes a candle that was lit throughout Yom Kippur, wine, and no spices).
• Eat a festive break-fast — many prepare food in advance because cooking restrictions on Yom Kippur end at nightfall.
• Chabad tradition (Baal HaTanya) teaches that one's livelihood (parnassa) for the year is especially connected to this meal — eat with joy and spiritual intention.

This item becomes available at nightfall after the fast ends.
    """.trimIndent()

    fun motzeiYomKippurMealTemplate(): String = MOTZEI_YOM_KIPPUR_MEAL_TEMPLATE

    fun motzeiYomKippurMealArgs(cal: DayInfo, profile: UserProfile): Map<String, String> {
        val tz = cal.zmanim?.timezoneId ?: profile.timezoneId
        val tzeit = ZmanimFormatter.formatTime(cal.zmanim?.tzeitMillis, tz)
        return mapOf(
            "tzeitLine" to (tzeit?.let { TZET_LINE_TEMPLATE } ?: ""),
            "time" to (tzeit ?: ""),
        )
    }
}
