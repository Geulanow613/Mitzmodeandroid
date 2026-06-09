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
        val shabbatChagBlock = YomTovShabbatPrepText.scheduleBlock(cal, profile, name)
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
        val (specificTitle, specificBody, links) = holidayBlock(idx, name, profile, cal)
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
        val chagName = cal.upcomingChagName ?: "Yom Tov"
        val sunsetLine = when {
            YomTovShabbatPrepText.isShabbatErevChag(cal) ->
                "• After Shabbat tonight: light $chagName candles from a pre-existing flame; Kiddush includes havdalah (Yaknehaz) — you should have prepared on Friday."
            YomTovShabbatPrepText.isErevChagBeforeShabbatErevChag(cal, tomorrowCal) ->
                "• $chagName begins tomorrow night after Shabbat (Motzei Shabbat), not at tonight's sunset — use the Friday “Tomorrow: Shabbat & erev $chagName” item for full prep."
            else -> ZmanimFormatter.formatTime(sunset, tz)?.let {
                "• Candle lighting & Yom Tov begin at sunset today ($it) — light from a pre-existing flame when possible."
            } ?: "• Candle lighting & Yom Tov begin at sunset — enable location for your local time."
        }

        val simchasBlock = if (HebrewCalendarEngine.isShaloshRegalim(cal.upcomingChagYomTovIndex)) {
            "\n\n${SeasonalMitzvahText.simchasYomTovPrepBlock()}"
        } else {
            ""
        }

        return """${BeginnerHalachaGlossary.erevChagCommon()}

Before chag — every erev Yom Tov:
$sunsetLine
• Finish cooking and reheating food before sunset; set up a blech or hot plate if needed for Yom Tov meals.
• Turn off phones and devices before Yom Tov — this app is for prep, not use on chag.
• Confirm shul times for tonight and tomorrow (candle lighting, Kol Nidre / Maariv / Shacharit / Musaf).$simchasBlock"""
    }

    private fun holidayBlock(
        idx: Int,
        name: String,
        profile: UserProfile,
        cal: DayInfo
    ): Triple<String, String, List<ChecklistLink>> = when (idx) {
        HebrewCalendarEngine.ROSH_HASHANA -> Triple(
            "Erev Rosh Hashana prep",
            BeginnerHalachaGlossary.withKeyTerms(
                BeginnerHalachaGlossary.daveningBasics(),
                """Rosh Hashana — Jewish New Year; Day of Judgment. No melacha from tonight.

If this year Rosh Hashana meets Shabbat, a detailed section follows (eruv tavshilin, Yaknehaz Kiddush–havdalah, etc.) based on Chabad.org, Peninei Halakha, and your Machzor — only in years when the calendar requires it.

Tonight & tomorrow:
• Light Yom Tov candles before sunset; recite Shehecheyanu on the first night.
• Festive meals with Kiddush, challah dipped in honey, and symbolic foods (apple & honey, pomegranate, etc.).
• Hear the shofar blown during daytime services tomorrow (not tonight).
• Add Yaaleh V'yavo in Amidah and bentching; recite full Hallel (per minhag).

Customs:
• Greet others with wishes for a good year (L'shanah tovah).
• Many avoid nuts, vinegar, and sharp foods on Rosh Hashana (minhag).
• Tashlich (casting sins into water) is on the first afternoon when Rosh Hashana is not Shabbat; if the first day is Shabbat, tashlich is postponed to Sunday.

${diasporaSecondDayNote(profile, "Rosh Hashana")}""",
            ),
            roshHashanaLinks(profile)
        )

        HebrewCalendarEngine.YOM_KIPPUR -> Triple(
            "Erev Yom Kippur prep",
            BeginnerHalachaGlossary.withKeyTerms(
                BeginnerHalachaGlossary.block(
                    "Yom Kippur — Day of Atonement; 25-hour fast and intense prayer",
                    BeginnerHalachaGlossary.MELACHA,
                    BeginnerHalachaGlossary.KIDDUSH,
                    BeginnerHalachaGlossary.TZEIT,
                    BeginnerHalachaGlossary.RAV,
                ),
                """Yom Kippur — Day of Atonement. Full 25-hour fast; five afflictions from sunset tonight until nightfall tomorrow.

Today before the fast:
• Eat a festive pre-fast meal (seudah hamafseket) before sunset — finish eating and drinking in time.
• Light Yom Tov candles before Kol Nidre; many use a bracha and a flame lit before Yom Kippur begins.
• Give tzedakah and ask forgiveness from others.
• Kaparot (if your custom) is done before Yom Kippur.

On Yom Kippur (no eating, drinking, washing for pleasure, anointing, leather shoes, or marital relations):
• Spend the day in prayer at shul (Kol Nidre tonight, full day of services tomorrow).
• Many wear white and avoid leather shoes.
• Ne'ilah at the end; after nightfall pray Maariv, make Havdalah, then break the fast.""",
            ),
            yomKippurLinks(profile)
        )

        HebrewCalendarEngine.PESACH -> {
            val sederWhen = when {
                ErevPesachPrepText.isErevPesachFridayBeforeShabbatPesach(cal) ->
                    "First Seder is tonight (Friday night). In the Diaspora, the second Seder is tomorrow night (Saturday night), after Shabbat."
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
            Triple(
                "Erev Pesach prep — Yom Tov & seder",
                BeginnerHalachaGlossary.withKeyTerms(
                    BeginnerHalachaGlossary.pesachPrep(),
                    """$pesachBegins

Chametz:
• All chametz must be gone before Shabbat or Yom Tov begins (sold, burned, or removed). Use today's Erev Pesach checklist items for bedikat chametz, biur, and mechirat chametz if still on your list.

Seder (first night):
• $sederWhen
• Matzah, maror, four cups of wine, reading the Haggadah, reclining, afikoman.
• Kiddush, festive meal, Hallel, Nirtzah.

Tomorrow by day:
• Yom Tov davening with Full Hallel and Musaf; no chametz or kitniyot (per your custom).
• Only eat food prepared for Pesach in kosher-for-Passover utensils.

${diasporaSecondDayNote(profile, "Pesach")}$shabbatBlock""",
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
• Light Yom Tov candles; Shehecheyanu on the first night.
• Many have the custom of dairy meals (cheesecake, blintzes) — also meat meals are permitted.
• All-night Torah learning (Tikkun Leil Shavuot) is a widespread custom tonight.
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
• Eat and spend time in the sukkah if it is built (mitzvah of sukkah begins with the festival).
• Have arba minim ready: lulav, etrog, hadasim, aravot (per your rabbi's kashrut standards).

Tonight & tomorrow:
• Light Yom Tov candles in the sukkah (per custom) or home; Shehecheyanu first night.
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
• Light Yom Tov candles; Shehecheyanu (first night).
• No lulav on Shemini Atzeret — the mitzvah ended with the seventh day of Sukkot.
${if (profile.isInIsrael) """
• Sukkot has ended — do not eat or sleep in the sukkah; festive meals are indoors.
• Simchat Torah in Israel: hakafot, finishing and restarting the Torah cycle, joyous dancing with the Torah.
• Geshem (prayer for rain) in Musaf; Full Hallel.
""" else """
• Shemini Atzeret in the Diaspora: Yizkor is often recited; still a full Yom Tov with no melacha. (Simchat Torah is tomorrow in the Diaspora.)
• Many Ashkenazim still eat in the sukkah on Shemini Atzeret but do not recite leishev basukkah — confirm with your rav.
• Geshem (prayer for rain) begins in Musaf in many communities.
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
• Light Yom Tov candles; Shehecheyanu on the first night.
• Hakafot — dancing with Torah scrolls; finish the annual cycle and begin Bereshit.
• Festive meals and drinking (responsibly) in many communities.
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
            add(ChecklistLink("Chabad — Rosh Hashana", "https://www.chabad.org/holidays/jewishnewyear/default_cdo/jewish/Jewish-New-Year.htm", "chabad"))
        }
        add(ChecklistLink("Peninei Halacha — Rosh Hashana", "https://ph.yhb.org.il/en/category/15/15-03/", "default"))
        add(ChecklistLink("Aish — Rosh Hashana", "https://aish.com/holidays/rosh-hashanah/", "default"))
        add(ChecklistLink("Ohr Somayach — Rosh Hashana", "https://ohr.edu/holidays/rosh_hashana/", "default"))
    }

    private fun yomKippurLinks(profile: UserProfile) = buildList {
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(ChecklistLink("Chabad — Yom Kippur", "https://www.chabad.org/holidays/yomkippur/default_cdo/jewish/Yom-Kippur.htm", "chabad"))
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
            add(ChecklistLink("Chabad — Sukkot", "https://www.chabad.org/holidays/sukkot/default_cdo/jewish/Sukkot.htm", "chabad"))
        }
        add(ChecklistLink("Peninei Halacha — Sukkot", "https://ph.yhb.org.il/en/category/13/13-01/", "default"))
        add(ChecklistLink("Aish — Sukkot", "https://aish.com/holidays/sukkot/", "default"))
    }

    private fun sheminiAtzeretLinks(profile: UserProfile) = buildList {
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(ChecklistLink("Chabad — Shemini Atzeret", "https://www.chabad.org/holidays/sukkot/article_cdo/aid/1771/jewish/Shemini-Atzeret.htm", "chabad"))
        }
        add(ChecklistLink("Peninei Halacha — Sukkot festivals", "https://ph.yhb.org.il/en/category/13/13-01/", "default"))
    }

    private fun simchatTorahLinks(profile: UserProfile) = buildList {
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(ChecklistLink("Chabad — Simchat Torah", "https://www.chabad.org/holidays/sukkot/article_cdo/aid/1772/jewish/Simchat-Torah.htm", "chabad"))
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
