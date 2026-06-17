package com.beardytop.beatzaddik.domain

object PublicFastDayText {

    fun erevMinorFastPrepTitle(fastName: String): String =
        "Prepare for tomorrow's fast — $fastName"

    fun erevMinorFastPrepExplanation(
        cal: DayInfo,
        tomorrowFastIdx: Int,
        profile: UserProfile,
    ): String {
        val fastName = PublicFastDayRules.displayName(tomorrowFastIdx)
        val tz = cal.zmanim?.timezoneId ?: profile.timezoneId
        val alotTomorrow = cal.zmanim?.let { z ->
            // Prep is for tomorrow's dawn — approximate with today's alot label as planning hint.
            ZmanimFormatter.formatTime(z.alotHaShacharMillis, tz)
        }
        val fridayNote = when (tomorrowFastIdx) {
            HebrewCalendarEngine.TENTH_OF_TEVES ->
                "\n\n10 Tevet on Friday: This is the only public fast that is never postponed. If it falls on Friday, you still fast until nightfall, then break the fast with Shabbat Kiddush and your Friday night meal (Shulchan Arukh O.C. 249:4; Mishnah Berurah)."
            HebrewCalendarEngine.FAST_OF_ESTHER ->
                "\n\nFast of Esther on Friday: Many communities still fast until shortly before Shabbat; break in time for Shabbat preparations — ask your rav for local practice."
            else -> ""
        }
        return """
Tomorrow is $fastName — a public fast from dawn (alot hashachar) until nightfall (tzeit).

If you plan to eat before the fast begins:
• Set a mental condition (tanai) the night before: "If I wake up hungry before dawn, I will eat." Without this condition, waking early and eating may prohibit you from eating again until the fast officially begins at dawn (Shulchan Arukh O.C. 564:1).
• If you wake before dawn and want to eat, you may drink water and eat foods that are not normally cooked for a meal — e.g. a piece of cake, fruit, or cereal. A full hot meal is disputed; many avoid a formal cooked meal once they have decided to fast (Mishnah Berurah 564:8–9).
• Stop all eating and drinking at alot hashachar${alotTomorrow?.let { " (approx. $it — enable location for your exact zman)" } ?: " — enable location in Settings for your dawn time"}.

Practical prep tonight:
• Hydrate well and eat a balanced dinner.
• Plan a lighter morning if you will not eat before dawn.
• Know your synagogue schedule if you plan to attend special prayers.

Who must fast: Jewish adults (bar/bat mitzvah age and older) in good health. Children below bar/bat mitzvah are not required to fast — train them gradually per your rav.$fridayNote
        """.trim()
    }

    fun erevYomKippurTitle(): String = "Erev Yom Kippur — eat and prepare"

    fun erevYomKippurExplanation(cal: DayInfo, profile: UserProfile): String {
        val tz = cal.zmanim?.timezoneId ?: profile.timezoneId
        val sunset = ZmanimFormatter.formatTime(cal.zmanim?.sunsetMillis, tz)
        val tzeitTomorrow = ZmanimFormatter.formatTime(cal.zmanim?.tzeitMillis, tz)?.let {
            "approx. $it tomorrow night"
        } ?: "nightfall tomorrow night"
        return """
Today is Erev Yom Kippur — a unique day of eating before the holiest fast of the year.

Mitzvah to eat today:
• It is a mitzvah to eat and drink generously all day Erev Yom Kippur (Shulchan Arukh O.C. 604:1; Mishnah Berurah).
• Have a festive Yom Tov-style meal — many begin with candle lighting and Kiddush as for Yom Tov (ask your rav for your minhag).
• The Talmud compares eating on Erev Yom Kippur to fasting on Yom Kippur itself for merit (Rosh Hashana 9a).

Before the fast begins:
• Yom Kippur starts at sunset tonight${sunset?.let { " (approx. $it)" } ?: ""} and ends at nightfall tomorrow ($tzeitTomorrow).
• Ask forgiveness from those you may have hurt; give tzedakah; immerse in a mikveh if that is your custom.
• Light Yom Kippur candles before sunset (married women traditionally light; others follow community — ask your rav).

Motzei Yom Kippur meal:
• After the fast ends tomorrow night, it is a mitzvah to eat a proper meal — not only a snack. Many have a festive break-fast.
• Chabad tradition (Baal HaTanya) teaches that one's livelihood (parnassa) for the year is especially connected to this post-Yom Kippur meal — eat with joy and intention after davening and Havdalah.

Who fasts tomorrow: Healthy Jewish adults from bar/bat mitzvah age. Those who are ill, pregnant, nursing, or recently gave birth should ask a rav — often they eat in smaller amounts (shiurim) or are exempt.
        """.trim()
    }

    fun erevTishaBeavTitle(): String = "Erev Tisha B'Av — restrictions and seudah hamafseket"

    fun erevTishaBeavExplanation(cal: DayInfo, profile: UserProfile): String {
        val tz = cal.zmanim?.timezoneId ?: profile.timezoneId
        val sunset = ZmanimFormatter.formatTime(cal.zmanim?.sunsetMillis, tz)
        val shabbatNote = if (cal.isShabbat) {
            """
Shabbat Erev Tisha B'Av (when 9 Av is Shabbat and the fast is moved to Sunday):
• Shabbat is celebrated fully until sunset — no mourning restrictions on Shabbat itself.
• After Shabbat ends (Havdalah), change into non-leather shoes and begin the fast and mourning practices.
• The seudah hamafseket is not eaten on Shabbat — it is observed after Shabbat ends, before the fast begins.
            """.trim()
        } else {
            ""
        }
        return """
Erev Tisha B'Av prepares us for mourning the destruction of the Temple.

When the fast begins:
• Tisha B'Av starts at sunset tonight${sunset?.let { " (approx. $it)" } ?: ""} and continues until nightfall tomorrow night — not dawn to dusk like minor fasts.

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
• Additionally: no Torah study except sad topics (Eichah, Job, mourning laws); many sit on the floor until chatzos tomorrow; kinot are recited.$shabbatNote

Ask your rav for details if you are ill, pregnant, or nursing.
        """.trim()
    }

    fun fastDayTitle(fastIdx: Int): String =
        "Fast — ${PublicFastDayRules.displayName(fastIdx)}"

    fun fastDaySubtitle(fastIdx: Int, zmanim: ZmanimSnapshot?, timezoneId: String): String {
        val timing = PublicFastDayRules.fastTimingLabel(fastIdx)
        val tz = zmanim?.timezoneId ?: timezoneId
        val end = ZmanimFormatter.formatTime(zmanim?.tzeitMillis, tz)
        return buildString {
            append(PublicFastDayRules.displayName(fastIdx))
            append(" · ")
            append(timing)
            end?.let { append(" (ends approx. $it)") }
        }
    }

    fun fastDayExplanation(fastIdx: Int, cal: DayInfo, profile: UserProfile): String {
        val common = commonFastLawsBlock()
        val specific = when (fastIdx) {
            HebrewCalendarEngine.FAST_OF_GEDALYAH -> minorFastSpecifics("Fast of Gedaliah", "commemorates the assassination of Gedaliah ben Achikam after the First Temple's destruction.")
            HebrewCalendarEngine.TENTH_OF_TEVES -> minorFastSpecifics("Fast of 10 Tevet", "marks the beginning of the siege of Jerusalem. Never postponed from Friday.")
            HebrewCalendarEngine.FAST_OF_ESTHER -> estherFastSpecifics()
            HebrewCalendarEngine.SEVENTEEN_OF_TAMMUZ -> minorFastSpecifics("Fast of 17 Tammuz", "marks the breaching of Jerusalem's walls and the start of the Three Weeks mourning period.")
            HebrewCalendarEngine.YOM_KIPPUR -> yomKippurFastSpecifics(cal, profile)
            HebrewCalendarEngine.TISHA_BEAV -> tishaBeavFastSpecifics(cal, profile)
            else -> ""
        }
        return "$specific\n\n$common".trim()
    }

    fun motzeiYomKippurMealTitle(): String = "Motzei Yom Kippur — break-fast meal"

    fun motzeiYomKippurMealExplanation(cal: DayInfo, profile: UserProfile): String {
        val tz = cal.zmanim?.timezoneId ?: profile.timezoneId
        val tzeit = ZmanimFormatter.formatTime(cal.zmanim?.tzeitMillis, tz)
        return """
After Yom Kippur ends at nightfall${tzeit?.let { " (approx. $it tonight)" } ?: ""}, it is a mitzvah to eat a proper meal — not only a quick bite.

What to do:
• Complete Maariv and Havdalah (Havdalah includes a candle that was lit throughout Yom Kippur, wine, and no spices).
• Eat a festive break-fast — many prepare food in advance because cooking restrictions on Yom Kippur end at nightfall.
• Chabad tradition (Baal HaTanya) teaches that one's livelihood (parnassa) for the year is especially connected to this meal — eat with joy and spiritual intention.

This item becomes available at nightfall after the fast ends.
        """.trim()
    }

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

    private fun commonFastLawsBlock(): String = """
Who must fast:
Jewish adults from bar/bat mitzvah age who are healthy enough to fast. Children are trained gradually — ask your rav. Women who are pregnant, nursing, or recently gave birth, and anyone who is ill, should ask a posek — many eat in measured amounts (shiurim) or are fully exempt.

If you ate by mistake:
If you forgot and ate or drank unintentionally, you may continue fasting once you remember — the fast remains valid (Shulchan Arukh O.C. 568:1).

If you cannot fast:
Do not endanger your health. Ask a rav about shiurim (small amounts at intervals), postponing the fast, or exemption. Pikuach nefesh overrides fasting.
    """.trim()

    private fun minorFastSpecifics(name: String, meaning: String): String = """
Today is $name — $meaning

Minor fast rules:
• No eating or drinking from dawn until nightfall.
• Washing, music, and showering are generally permitted (unlike Tisha B'Av and Yom Kippur).
• Special selichot and prayers may be recited in synagogue — check your community schedule.
    """.trim()

    private fun estherFastSpecifics(): String = """
Today is the Fast of Esther (Taanit Esther) — commemorating the fasting Esther called before approaching the king (Esther 4:16).

Minor fast rules with Purim context:
• No eating or drinking from dawn until nightfall.
• Music, showering for pleasure, and leather shoes are permitted — this is a minor fast, not like Tisha B'Av.
• Purim mitzvot begin tonight/tomorrow per your calendar (14 Adar, or 15 in walled cities). This fast is the spiritual preparation before the joy of Purim.
    """.trim()

    private fun yomKippurFastSpecifics(cal: DayInfo, profile: UserProfile): String {
        val tz = cal.zmanim?.timezoneId ?: profile.timezoneId
        val tzeit = ZmanimFormatter.formatTime(cal.zmanim?.tzeitMillis, tz)
        return """
Today is Yom Kippur — the Day of Atonement.

The fast:
• Began at sunset last night and ends at nightfall tonight${tzeit?.let { " (approx. $it)" } ?: ""}.
• Five afflictions: no eating/drinking, no leather shoes, no bathing for pleasure, no anointing, no marital relations.
• Many wear white; married men who normally wear a kittel do so; tallit is worn all day.
• The day is spent in prayer — Kol Nidrei last night, Shacharit, Musaf, Mincha, Neilah, then Maariv and Havdalah after nightfall.

After nightfall: eat the Motzei Yom Kippur meal (see your checklist). Chabad tradition connects this meal to parnassa for the year.
        """.trim()
    }

    private fun tishaBeavFastSpecifics(cal: DayInfo, profile: UserProfile): String {
        val tz = cal.zmanim?.timezoneId ?: profile.timezoneId
        val tzeit = ZmanimFormatter.formatTime(cal.zmanim?.tzeitMillis, tz)
        return """
Today is Tisha B'Av — mourning the destruction of both Temples and our exile.

The fast:
• Began at sunset last night and ends at nightfall tonight${tzeit?.let { " (approx. $it)" } ?: ""} — not a dawn-to-dusk fast.

What is forbidden today (in addition to eating and drinking):
• Leather shoes; bathing for pleasure; anointing with cream or cologne; marital relations.
• Torah study except for mournful passages (Eichah, Iyov, parts of Yirmiyahu, halachot of mourning).
• Greeting people with "hello"; idle conversation; work is discouraged (follow your community).
• Many sit on the floor or a low stool until chatzos (halachic midday); kinot (elegies) are recited.

What is permitted (unlike Yom Kippur in some communities):
• Music prohibitions are mourning-related — live joyful music is avoided as part of the Three Weeks/Nine Days spirit, but the fast's core prohibitions are the five afflictions above.

After nightfall: some mourning restrictions of the Nine Days lift — ask your rav about laundry, meat, and music.
        """.trim()
    }
}
