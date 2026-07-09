package com.beardytop.beatzaddik.domain

/**
 * Holiday-specific erev Yom Tov prep copy based on tomorrow's [DayInfo.upcomingChagYomTovIndex].
 */
object ErevChagPrepText {

    data class PrepContent(
        val title: String,
        val explanation: String,
        val links: List<ChecklistLink>
    )

    fun build(cal: DayInfo, profile: UserProfile, tomorrowCal: DayInfo): PrepContent {
        val name = cal.upcomingChagName ?: "Yom Tov"
        val idx = cal.upcomingChagYomTovIndex
        val common = commonErevBlock(cal, profile, tomorrowCal)
        val shabbatChagBlock = YomTovShabbatPrepText.scheduleBlock(cal, profile, name, tomorrowCal = tomorrowCal)
        val intro = when {
            YomTovShabbatPrepText.isShabbatErevChag(cal) ->
                "Today is Shabbat — the checklist is off today. You should have read Friday's “Tomorrow: $name & Shabbat — prepare today” item before Shabbat candles. $name begins tonight at Motzei Shabbat (Havdalah in Kiddush — Yaknehaz)."
            YomTovShabbatPrepText.isErevChagBeforeShabbatErevChag(cal, tomorrowCal) ->
                "Today is Friday before Shabbat. $name does not begin tonight — it begins tomorrow night after Shabbat (Motzei Shabbat). Open the “Tomorrow: $name & Shabbat — prepare today” item now and finish food, flames, and Yaknehaz prep before Shabbat candles."
            YomTovShabbatPrepText.isErevChagYomTovStartsBeforeShabbat(cal) ->
                "Tonight at sunset begins $name, and Shabbat follows tomorrow — a Yom Tov–Shabbat sequence."
            else ->
                "Tomorrow (from tonight at sunset) is $name — a Yom Tov with melacha restrictions."
        }
        val tail = buildString {
            append(common)
            shabbatChagBlock?.let { append("\n\n").append(it) }
        }
        if (idx == null) {
            return PrepContent(
                title = "Erev $name prep",
                explanation = "$intro\n\nSet your location in the app for Hebrew date, zmanim, and tailored prep.\n\n$tail",
                links = defaultChagLinks(profile),
            )
        }
        val (specificTitle, specificBody, links) = holidayBlock(idx, name, profile, cal, tomorrowCal)
        val simchasLinks = if (HebrewCalendarEngine.isShaloshRegalim(idx)) {
            SeasonalMitzvahText.simchasYomTovPrepLinks()
        } else {
            emptyList()
        }
        val allLinks = (links + simchasLinks + YomTovShabbatPrepText.links(cal, profile, name))
            .distinctBy { it.url }
        return PrepContent(
            title = specificTitle,
            explanation = "$intro\n\n$specificBody\n\n$tail",
            links = allLinks
        )
    }

    private fun commonErevBlock(cal: DayInfo, profile: UserProfile, tomorrowCal: DayInfo): String {
        val tz = cal.zmanim?.timezoneId ?: profile.timezoneId
        val sunset = cal.zmanim?.sunsetMillis
        val tzeit = cal.zmanim?.tzeitMillis
        val chagName = cal.upcomingChagName ?: "Yom Tov"
        val isShavuos = cal.upcomingChagYomTovIndex == HebrewCalendarEngine.SHAVUOS
        val sunsetLine = when {
            YomTovShabbatPrepText.isShabbatErevChag(cal) ->
                "• After Shabbat tonight: light $chagName candles from a pre-existing flame; Kiddush includes havdalah (Yaknehaz) — you should have prepared on Friday."
            YomTovShabbatPrepText.isErevChagBeforeShabbatErevChag(cal, tomorrowCal) ->
                "• $chagName begins tomorrow night after Shabbat (Motzei Shabbat), not at tonight's sunset — use the Friday “Tomorrow: Shabbat & erev $chagName” item for full prep."
            isShavuos -> ZmanimFormatter.formatTime(tzeit, tz)?.let {
                "• Crucial timing note for Shavuot: Unlike other festivals, you cannot light candles, start prayers, or make Kiddush until full nightfall (tzeit — approx. $it today)."
            } ?: "• Crucial timing note for Shavuot: Unlike other festivals, you cannot light candles, start prayers, or make Kiddush until full nightfall (tzeit)."
            else -> ZmanimFormatter.formatTime(sunset, tz)?.let {
                "• Candle lighting & Yom Tov begin at sunset today ($it) — before sunset you may strike a new match normally to light Yom Tov candles and to kindle a 24- or 48-hour transfer candle for use after the holiday begins. Once Yom Tov has started, light candles only from a pre-existing flame (not a new match or lighter)."
            } ?: "• Candle lighting & Yom Tov begin at sunset — enable location for your local time."
        }

        val cookingLine = if (isShavuos) {
            "• On Erev Shavuot: daytime preparations can continue up until nightfall (tzeit), as the holiday begins later than usual. Reheating and cooking for the night meal may also be done on the holiday itself after tzeit, strictly from a pre-existing flame. Set up a blech or hot plate if needed."
        } else {
            "• Finish cooking and reheating food before sunset; set up a blech or hot plate if needed for Yom Tov meals."
        }

        val simchasBlock = if (HebrewCalendarEngine.isShaloshRegalim(cal.upcomingChagYomTovIndex)) {
            "\n\n${SeasonalMitzvahText.simchasYomTovPrepBlock()}"
        } else {
            ""
        }

        val isYomKippur = cal.upcomingChagYomTovIndex == HebrewCalendarEngine.YOM_KIPPUR
        val shulServicesLine = if (isYomKippur) {
            "• Confirm shul times for tonight and tomorrow (Kol Nidre / Maariv / Shacharit / Musaf)."
        } else {
            "• Confirm shul times for tonight and tomorrow (Maariv / Shacharit / Musaf)."
        }

        return """Before chag — every erev Yom Tov:
$sunsetLine
$cookingLine
• Turn off phones and devices before Yom Tov — this app is for prep, not use on chag.
$shulServicesLine$simchasBlock"""
    }

    private fun shehecheyanuErevLines(idx: Int?, tomorrowCal: DayInfo, profile: UserProfile): String =
        when (idx) {
            HebrewCalendarEngine.PESACH -> {
                if (HebrewCalendarEngine.pesachOpeningYomTovGetsShehecheyanu(
                        tomorrowCal.hebrewMonth,
                        tomorrowCal.hebrewDay,
                        profile.isInIsrael,
                    )
                ) {
                    "• Recite Shehecheyanu on the first night."
                } else {
                    val reason = if (HebrewCalendarEngine.isFinalYomTovDayOfPesach(
                            tomorrowCal.hebrewMonth,
                            tomorrowCal.hebrewDay,
                            profile.isInIsrael,
                        )
                    ) {
                        " — the final day of Pesach extends the original festival, not a new holiday."
                    } else {
                        " — only the opening night(s) of Pesach include this blessing."
                    }
                    "• Do NOT recite Shehecheyanu at candle lighting or Kiddush$reason"
                }
            }
            HebrewCalendarEngine.ROSH_HASHANA ->
                "• Recite Shehecheyanu at Kiddush on both nights."
            else -> "• Recite Shehecheyanu on the first night."
        }

    private fun diasporaFinalPesachAdvanceNote(tomorrowCal: DayInfo, profile: UserProfile): String {
        if (profile.isInIsrael) return ""
        if (tomorrowCal.hebrewMonth != HebrewCalendarEngine.NISSAN || tomorrowCal.hebrewDay != 21) return ""
        return "\n• When the 8th (final) day begins after tomorrow's Yom Tov ends, do NOT recite Shehecheyanu at candle lighting or Kiddush — plan for that before phones go off on Yom Tov."
    }

    private fun holidayBlock(
        idx: Int,
        name: String,
        profile: UserProfile,
        cal: DayInfo,
        tomorrowCal: DayInfo,
    ): Triple<String, String, List<ChecklistLink>> = when (idx) {
        HebrewCalendarEngine.ROSH_HASHANA -> Triple(
            "Erev Rosh Hashana prep",
            BeginnerHalachaGlossary.withKeyTerms(
                BeginnerHalachaGlossary.daveningBasics(),
                """Rosh Hashana — Jewish New Year; Day of Judgment. No melacha from tonight.

If this year Rosh Hashana meets Shabbat, a detailed section follows (eruv tavshilin, Yaknehaz Kiddush–havdalah, etc.) — only in years when the calendar requires it. Use your Machzor for the exact wording.

Tonight & tomorrow:
• Light Yom Tov candles before sunset.
${shehecheyanuErevLines(HebrewCalendarEngine.ROSH_HASHANA, tomorrowCal, profile)}
• Festive meals with Kiddush, challah dipped in honey, and symbolic foods (apple & honey, pomegranate, etc.).
• Hear the shofar blown during daytime services tomorrow (not tonight).
• Daven from a Machzor: Rosh Hashana uses a special festival Amidah (and a unique Musaf) — not the regular prayer with a small insert. Tachanun is omitted.

Customs:
• Greet others with wishes for a good year (L'shanah tovah).
• Many avoid nuts, vinegar, and sharp foods on Rosh Hashana (minhag).
• Tashlich: on the first afternoon when Rosh Hashana is not Shabbat. When the first day is Shabbat: Ashkenazim postpone to Sunday (a universal Rabbinic gezeirah against carrying in public, even with an eruv); many Sephardic communities recite Tashlich on the first day where carrying a machzor is permitted. Tashlich is prayers at the water — not feeding fish (breadcrumbs are forbidden on Shabbat and Yom Tov). If you missed it, many communities do it later — commonly any day until Hoshana Rabbah; follow your minhag.

• A second day of Rosh Hashana follows tomorrow night — in Israel and the Diaspora — so prepare candles and meals for two days of Yom Tov.""",
            ),
            roshHashanaLinks(profile)
        )

        HebrewCalendarEngine.YOM_KIPPUR -> Triple(
            "Erev Yom Kippur prep",
            BeginnerHalachaGlossary.withKeyTerms(
                BeginnerHalachaGlossary.block(
                    "Yom Kippur — Day of Atonement; 25-hour fast and intense prayer",
                    "Yom Kippur melacha — Shabbat Shabbaton: same strict labor rules as Shabbat (cooking and transferring flame forbidden — not like festival Yom Tov)",
                    BeginnerHalachaGlossary.KIDDUSH,
                    BeginnerHalachaGlossary.TZEIT,
                    BeginnerHalachaGlossary.RAV,
                ),
                """Yom Kippur — Day of Atonement. Full 25-hour fast; five afflictions from sunset tonight until nightfall tomorrow.

Today before the fast:
• The mitzvah of eating: There is a unique halachic obligation to eat and drink throughout the day on Erev Yom Kippur (Berachot 8b). Halacha considers eating today and fasting tomorrow as two halves of the same complete mitzvah — eat regular meals during the day, not only the final pre-fast meal.
• Eat a festive pre-fast meal (seudah hamafseket) before sunset — finish eating and drinking in time.
• Light candles before Kol Nidre with the bracha (neir shel Yom Hakippurim per your siddur) — all flames must be lit before sunset. Yom Kippur has the same strict fire restrictions as Shabbat: unlike regular Yom Tov, you cannot transfer a flame once the fast begins.
• Candle-lighting and travel: Once you light candles and say the blessing, Yom Kippur has fully begun for you — you cannot drive or ride in a vehicle after that point. Plan to walk when possible, or arrive at synagogue early and light candles there before Kol Nidre. Driving on Yom Kippur is treated with exceptional stringency across communities, and vehicle use raises serious melacha concerns independent of candle-lighting. Some advanced poskim discuss a rare disputed workaround (a mental halachic condition / tnai before lighting at home) only in narrow cases — not a DIY option. Ask your rav only if you genuinely have no other option; do not rely on it without expert guidance.
• Give tzedakah and ask forgiveness from others.
• Kaparot (if your custom) is done before Yom Kippur.

On Yom Kippur (no eating, drinking, washing for pleasure, anointing, leather shoes, or marital relations):
• Daven from a Machzor: Kol Nidre tonight, then a full day of Yom Kippur services tomorrow (special liturgy throughout).
• Clothing & shoes: There is a widespread custom to wear white clothing to look like angels. Separately, it is a strict halachic prohibition for everyone to wear leather shoes or leather footwear of any kind on Yom Kippur (one of the five mandatory inuyim).
• Ne'ilah at the end; after nightfall pray Maariv, then Havdalah over wine and a ner she-shavat (a flame that burned throughout Yom Kippur, such as a 48-hour candle lit before the fast). Then break the fast.""",
            ),
            yomKippurLinks(profile)
        )

        HebrewCalendarEngine.PESACH -> if (HebrewCalendarEngine.isErevFirstPesachSeder(cal.hebrewMonth, cal.hebrewDay)) {
            val sederWhen = when {
                ErevPesachPrepText.isErevPesachFridayBeforeShabbatPesach(cal) ->
                    "First Seder is tonight (Friday night, commencing 15 Nisan). In the Diaspora, the second Seder is Saturday night — transitioning from Shabbat directly into the second day of Yom Tov, with Yaknehaz in Kiddush."
                ErevPesachPrepText.isErevPesachOnShabbat(cal) ->
                    "First Seder is tomorrow night (Motzei Shabbat / Saturday night) — Havdalah in Kiddush (Yaknehaz), then Seder."
                profile.isInIsrael -> "In Israel: one seder tonight."
                else -> "In the Diaspora: first seder tonight; second seder tomorrow night."
            }
            val pesachBegins = when {
                ErevPesachPrepText.isErevPesachFridayBeforeShabbatPesach(cal) ->
                    "Pesach begins tonight at nightfall. Torah-level Yom Tov — no melacha from then. Tomorrow (15 Nisan) is Shabbat, the first day of Pesach."
                ErevPesachPrepText.isErevPesachOnShabbat(cal) ->
                    "Pesach begins tomorrow night after Shabbat ends (Saturday night). Today is Shabbat — no bedikat or biur today."
                else -> "Pesach begins tonight. Torah-level Yom Tov — no melacha."
            }
            val shabbatBlock = ErevPesachPrepText.erevPesachShabbatScheduleBlock(cal)
                ?.let { if (YomTovShabbatPrepText.isShabbatErevChag(cal)) null else "\n\n$it" }
                ?: ""
            val chametzNote = when {
                ErevPesachPrepText.isErevPesachOnShabbat(cal) ->
                    """Chametz:
• Biur was Friday morning (13 Nisan). On Shabbat morning, finish eating chametz by the end of the 4th halachic hour and recite the final Kol Chamira before the end of the 5th halachic hour — do not burn on Shabbat. Bedikat was Thursday night; mechirat chametz should already be authorized."""
                else ->
                    """Chametz:
• All chametz must be completely gone, destroyed, or sold, and the final Kol Chamira recited, before the end of the 5th halachic hour this morning (midday threshold) — NOT sunset. Stop eating chametz by the end of the 4th halachic hour. Bedikat chametz was last night; mechirat chametz should already be authorized with your rabbi — use today's biur chametz checklist item if still on your list."""
            }
            val pesachChametzKitniyotLine = when (profile.effectiveNusach()) {
                EffectiveNusach.ASHKENAZ ->
                    "• Yom Tov davening with Full Hallel and Musaf; no chametz or kitniyot."
                EffectiveNusach.CHABAD ->
                    "• Yom Tov davening with Full Hallel and Musaf; no chametz or kitniyot (per your custom)."
                EffectiveNusach.SEFARD, EffectiveNusach.EDOT_HAMIZRACH ->
                    "• Yom Tov davening with Full Hallel and Musaf; no chametz."
            }
            Triple(
                "Erev Pesach prep — Yom Tov & seder",
                BeginnerHalachaGlossary.withKeyTerms(
                    BeginnerHalachaGlossary.pesachPrep(),
                    """$pesachBegins

$chametzNote

Seder (first night):
• $sederWhen
• Matzah, maror, four cups of wine, reading the Haggadah, afikoman.
• Seder plate setup: zeroa (shankbone), beitzah (egg), maror, chazeret (per custom), charoset, karpas, salt water.
• Reclining (hasebha): Recline to the left when drinking the four cups and eating matzah, korech, and afikoman — do not recline while eating maror or chazeret (they symbolize slavery).
• Kiddush, festive meal, Hallel, Nirtzah.
• Follow your Haggadah step by step for the exact order and details.

Tomorrow by day:
$pesachChametzKitniyotLine
• Only eat food prepared for Pesach in kosher-for-Passover utensils.

${diasporaSecondDayNote(profile, "Pesach")}$shabbatBlock""",
                ),
                pesachChagLinks(profile)
            )
        } else {
            val isFinal = HebrewCalendarEngine.isFinalYomTovDayOfPesach(
                tomorrowCal.hebrewMonth,
                tomorrowCal.hebrewDay,
                profile.isInIsrael,
            )
            val pesachChametzKitniyotLine = when (profile.effectiveNusach()) {
                EffectiveNusach.ASHKENAZ ->
                    "• Yom Tov davening with Half Hallel and Musaf; no chametz or kitniyot."
                EffectiveNusach.CHABAD ->
                    "• Yom Tov davening with Half Hallel and Musaf; no chametz or kitniyot (per your custom)."
                EffectiveNusach.SEFARD, EffectiveNusach.EDOT_HAMIZRACH ->
                    "• Yom Tov davening with Half Hallel and Musaf; no chametz."
            }
            Triple(
                if (isFinal) "Erev final Pesach Yom Tov prep" else "Erev Pesach Yom Tov prep",
                BeginnerHalachaGlossary.withKeyTerms(
                    BeginnerHalachaGlossary.pesachPrep(),
                    """Another Yom Tov day of Pesach begins after sunset today — no melacha from then.

Tomorrow:
$pesachChametzKitniyotLine
• Festive meals in kosher-for-Passover utensils only.
• Light Yom Tov candles before sunset.
${shehecheyanuErevLines(HebrewCalendarEngine.PESACH, tomorrowCal, profile)}${diasporaFinalPesachAdvanceNote(tomorrowCal, profile)}""",
                ),
                pesachChagLinks(profile)
            )
        }

        HebrewCalendarEngine.SHAVUOS -> Triple(
            "Erev Shavuot prep",
            BeginnerHalachaGlossary.withKeyTerms(
                BeginnerHalachaGlossary.yomTovAndShabbat(),
                """Shavuot — receiving the Torah at Sinai. Yom Tov from tonight.

Tonight & tomorrow:
• Do not light candles, begin Maariv, or make Kiddush until full nightfall (tzeit) — see the timing note above.
• Light Yom Tov candles at tzeit.
${shehecheyanuErevLines(HebrewCalendarEngine.SHAVUOS, tomorrowCal, profile)}
• Dairy is a cherished Shavuot minhag (cheesecake, blintzes). A festive meat meal with wine fulfills the primary mitzvah of Simchat Yom Tov (O.C. 529:2); many families have dairy first, then a full meat Yom Tov meal.
• All-night Torah learning (Tikkun Leil Shavuot) is a widespread custom tonight.
• Staying up all night: If you remain awake the entire night studying, standard Ashkenazi practice is to hear the morning blessings and Torah blessings from someone who slept, to avoid halachic doubts. However, according to Chabad custom (and several Sephardic authorities), you personally recite the entire sequence of morning and Torah blessings yourself after dawn (alot hashachar), even with zero sleep.
• Read Megillat Rut in many communities (tomorrow).
• Full Yom Tov davening with Full Hallel and Musaf; Akdamut/Megillat Rut per minhag.

No melacha; treat meals and prayer with joy and Torah focus.""",
            ),
            shavuotLinks(profile)
        )

        HebrewCalendarEngine.SUCCOS -> Triple(
            "Erev Sukkot prep — Yom Tov tonight",
            BeginnerHalachaGlossary.withKeyTerms(
                BeginnerHalachaGlossary.sukkotBasics(),
                """Sukkot (first day) begins tonight — Zman Simchateinu.

Before sunset:
• Have arba minim ready: lulav, etrog, hadasim, aravot (per your rabbi's kashrut standards). The etrog is held separately — never bound with the others.
• Bind the lulav with hadasim and aravot before sunset. Tying a secure double knot is prohibited on Yom Tov, so standard custom is to bind them erev with a double knot or koishelach (woven leaf holder). Pre-made holders may be slipped on during Yom Tov, but do not tie new knots. If you forgot: wrap a lulav leaf around the species and tuck the end in — no knot.

Tonight & tomorrow:
• Light Yom Tov candles in the sukkah (per custom) or home.
${shehecheyanuErevLines(HebrewCalendarEngine.SUCCOS, tomorrowCal, profile)}
• ${if (profile.isInIsrael) "Shake lulav and etrog with bracha tomorrow (first day of Sukkot)." else "Shake lulav and etrog with bracha on the first and second days of Yom Tov (Diaspora); continue on Chol HaMoed per custom."}
• Festive meals in the sukkah; Ushpizin welcome guests.
• No melacha; Full Hallel and Musaf in davening.

${diasporaSecondDayNote(profile, "Sukkot")}""",
            ),
            sukkotLinks(profile)
        )

        HebrewCalendarEngine.SHEMINI_ATZERES -> Triple(
            "Erev Shemini Atzeret prep",
            BeginnerHalachaGlossary.withKeyTerms(
                BeginnerHalachaGlossary.yomTovAndShabbat(),
                """Shemini Atzeret begins tonight${if (profile.isInIsrael) " — in Israel this is also Simchat Torah." else "."}

Tonight & tomorrow:
• Light Yom Tov candles.
${shehecheyanuErevLines(HebrewCalendarEngine.SHEMINI_ATZERES, tomorrowCal, profile)}
• No lulav on Shemini Atzeret — the mitzvah ended with the seventh day of Sukkot.
${if (profile.isInIsrael) """
• Sukkot has ended — do not eat or sleep in the sukkah; festive meals are indoors.
• Simchat Torah in Israel: hakafot, finishing and restarting the Torah cycle, joyous dancing with the Torah.
• Liturgical shift: During Musaf today, the entire Jewish world officially transitions to the winter prayer cycle, universally inserting "Mashiv HaRuach U'Morid HaGeshem" into the second blessing of the Amidah. Tefillat Geshem (the formal prayer for rain) is recited in Musaf; Full Hallel.
""" else """
• Shemini Atzeret in the Diaspora: Yizkor is often recited; still a full Yom Tov with no melacha. (Simchat Torah is tomorrow in the Diaspora.)
• Sukkah in the Diaspora: Due to safek dyoma (halachic doubt which day is which), Diaspora Ashkenazim are required to eat all major meals in the sukkah on Shemini Atzeret, though leishev basukkah is omitted entirely. Sephardic and Chabad customs vary — confirm with your rav.
• Liturgical shift: During Musaf today, the entire Jewish world officially transitions to the winter prayer cycle, universally inserting "Mashiv HaRuach U'Morid HaGeshem" into the second blessing of the Amidah. Tefillat Geshem is recited in Musaf.
"""}
• Festive Yom Tov meals.""",
            ),
            sheminiAtzeretLinks(profile)
        )

        HebrewCalendarEngine.SIMCHAS_TORAH -> Triple(
            "Erev Simchat Torah prep",
            BeginnerHalachaGlossary.withKeyTerms(
                BeginnerHalachaGlossary.yomTovAndShabbat(),
                """Simchat Torah begins tonight (Diaspora) — rejoicing with the Torah.

Tonight & tomorrow:
• Light Yom Tov candles.
${shehecheyanuErevLines(HebrewCalendarEngine.SIMCHAS_TORAH, tomorrowCal, profile)}
• Hakafot — dancing with Torah scrolls; finish the annual cycle and begin Bereshit.
• Festive meals and drinking (responsibly) in many communities.
• Minhag in some Ashkenaz Diaspora shuls (not universal): because drinking often accompanies daytime hakafot, Birkat Kohanim is sometimes moved from Musaf to early Shacharit so Kohanim are sober. This is generally irrelevant in Israel (where Birkat Kohanim is said daily at Shacharit) and in many Sephardic communities with different timing — follow your synagogue's practice.
• Full Yom Tov — no melacha; Full Hallel and Musaf.
• In Israel, Simchat Torah coincides with Shemini Atzeret (one day).""",
            ),
            simchatTorahLinks(profile)
        )

        else -> Triple(
            "Erev $name prep",
            BeginnerHalachaGlossary.withKeyTerms(
                BeginnerHalachaGlossary.yomTovAndShabbat(),
                "Tomorrow is $name. Observe Yom Tov laws: no melacha, festive meals, special davening. Ask your rabbi for details specific to this day.",
            ),
            defaultChagLinks(profile)
        )
    }

    private fun diasporaSecondDayNote(profile: UserProfile, holiday: String): String =
        if (profile.isInIsrael) {
            ""
        } else {
            "\n• Diaspora: A second day of $holiday follows tomorrow night — prepare candles and meals for two days of Yom Tov."
        }

    private fun roshHashanaLinks(profile: UserProfile) = buildList {
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(ChecklistLink("Chabad — Rosh Hashana", "https://www.chabad.org/library/article_cdo/aid/4762/jewish/What-Is-Rosh-Hashanah.htm", "chabad"))
        }
        add(ChecklistLink("Peninei Halacha — Rosh Hashana", "https://ph.yhb.org.il/en/category/15/15-03/", "default"))
        add(ChecklistLink("Aish — Rosh Hashana", "https://aish.com/holidays/rosh-hashanah/", "default"))
        add(ChecklistLink("Ohr Somayach — Rosh Hashana", "https://ohr.edu/holidays/rosh_hashana/", "default"))
    }

    private fun yomKippurLinks(profile: UserProfile) = buildList {
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(ChecklistLink("Chabad — Yom Kippur", "https://www.chabad.org/library/article_cdo/aid/4688/jewish/What-Is-Yom-Kippur.htm", "chabad"))
        }
        add(ChecklistLink("Peninei Halacha — Yom Kippur", "https://ph.yhb.org.il/en/category/15/15-06/", "default"))
        add(ChecklistLink("Aish — Yom Kippur", "https://aish.com/holidays/yom-kippur/", "default"))
        add(ChecklistLink("Ohr Somayach — Yom Kippur", "https://ohr.edu/holidays/yom_kippur/", "default"))
    }

    private fun pesachChagLinks(profile: UserProfile) = buildList {
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(ChecklistLink("Chabad — Passover", "https://www.chabad.org/holidays/passover/default_cdo/jewish/Passover.htm", "chabad"))
        }
        add(ChecklistLink("Peninei Halacha — Pesach", "https://ph.yhb.org.il/en/04-03-01/", "default"))
        add(ChecklistLink("Aish — Passover", "https://aish.com/holidays/pesach/", "default"))
        add(ChecklistLink("Ohr Somayach — Pesach", "https://ohr.edu/holidays/pesach/", "default"))
    }

    private fun shavuotLinks(profile: UserProfile) = buildList {
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(ChecklistLink("Chabad — Shavuot", "https://www.chabad.org/holidays/shavuot/default_cdo/jewish/Shavuot.htm", "chabad"))
        }
        add(ChecklistLink("Peninei Halacha — Shavuot", "https://ph.yhb.org.il/en/category/12/12-13/", "default"))
        add(ChecklistLink("Aish — Shavuot", "https://aish.com/holidays/shavuot/", "default"))
    }

    private fun sukkotLinks(profile: UserProfile) = buildList {
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(ChecklistLink("Chabad — Sukkot", "https://www.chabad.org/library/article_cdo/aid/4784/jewish/What-Is-Sukkot.htm", "chabad"))
        }
        add(ChecklistLink("Peninei Halacha — Sukkot", "https://ph.yhb.org.il/en/category/13/13-01/", "default"))
        add(ChecklistLink("Aish — Sukkot", "https://aish.com/holidays/sukkot/", "default"))
    }

    private fun sheminiAtzeretLinks(profile: UserProfile) = buildList {
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(ChecklistLink("Chabad — Shemini Atzeret", "https://www.chabad.org/library/article_cdo/aid/4464/jewish/What-Is-Shemini-Atzeret-Simchat-Torah.htm", "chabad"))
        }
        add(ChecklistLink("Peninei Halacha — Sukkot festivals", "https://ph.yhb.org.il/en/category/13/13-01/", "default"))
    }

    private fun simchatTorahLinks(profile: UserProfile) = buildList {
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(ChecklistLink("Chabad — Simchat Torah", "https://www.chabad.org/library/article_cdo/aid/4464/jewish/What-Is-Shemini-Atzeret-Simchat-Torah.htm", "chabad"))
        }
        add(ChecklistLink("Peninei Halacha — Simchat Torah", "https://ph.yhb.org.il/en/category/13/13-01/", "default"))
        add(ChecklistLink("Aish — Simchat Torah", "https://aish.com/holidays/sukkot/", "default"))
    }

    private fun defaultChagLinks(profile: UserProfile) = buildList {
        add(ChecklistLink("Peninei Halacha — Festivals", "https://ph.yhb.org.il/en/category/zemanim/", "default"))
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(ChecklistLink("Chabad — Holidays", "https://www.chabad.org/holidays/default_cdo/jewish/Jewish-Holidays.htm", "chabad"))
        }
    }
}
