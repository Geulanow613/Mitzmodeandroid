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

    fun build(cal: DayInfo, profile: UserProfile): PrepContent {
        val name = cal.upcomingChagName ?: "Yom Tov"
        val idx = cal.upcomingChagYomTovIndex
        val common = commonErevBlock(cal, profile)
        if (idx == null) {
            return PrepContent(
                title = "Erev $name prep",
                explanation = "Tomorrow is $name. Set your location in the app for Hebrew date, zmanim, and tailored prep.\n\n$common",
                links = defaultChagLinks(profile)
            )
        }
        val (specificTitle, specificBody, links) = holidayBlock(idx, name, profile, cal)
        return PrepContent(
            title = specificTitle,
            explanation = "Tomorrow (from tonight at sunset) is $name — a Yom Tov with melacha restrictions.\n\n$specificBody\n\n$common",
            links = links
        )
    }

    private fun commonErevBlock(cal: DayInfo, profile: UserProfile): String {
        val tz = cal.zmanim?.timezoneId ?: profile.timezoneId
        val sunset = cal.zmanim?.sunsetMillis
        val sunsetLine = ZmanimFormatter.formatTime(sunset, tz)?.let {
            "• Candle lighting & Yom Tov begin at sunset today ($it) — light from a pre-existing flame when possible."
        } ?: "• Candle lighting & Yom Tov begin at sunset — enable location for your local time."

        val fridayNote = if (cal.isErevShabbat) {
            "\n• Erev Shabbat + Yom Tov: Make an eruv tavshilin so you may cook on Yom Tov for Shabbat (ask your rabbi for the text and procedure)."
        } else ""

        return """Before chag — every erev Yom Tov:
$sunsetLine
• Finish cooking and reheating food before sunset; set up a blech or hot plate if needed for Yom Tov meals.
• Turn off phones and devices before Yom Tov — this app is for prep, not use on chag.
• Confirm shul times for tonight and tomorrow (candle lighting, Kol Nidre / Maariv / Shacharit / Musaf).
$fridayNote"""
    }

    private fun holidayBlock(
        idx: Int,
        name: String,
        profile: UserProfile,
        cal: DayInfo
    ): Triple<String, String, List<ChecklistLink>> = when (idx) {
        HebrewCalendarEngine.ROSH_HASHANA -> Triple(
            "Erev Rosh Hashana prep",
            """Rosh Hashana — Jewish New Year; Day of Judgment. No melacha from tonight.

Tonight & tomorrow:
• Light Yom Tov candles before sunset; recite Shehecheyanu on the first night.
• Festive meals with Kiddush, challah dipped in honey, and symbolic foods (apple & honey, pomegranate, etc.).
• Hear the shofar blown during daytime services tomorrow (not tonight).
• Add Yaaleh V'yavo in Amidah and bentching; recite full Hallel (per minhag).

Customs:
• Greet others with wishes for a good year (L'shanah tovah).
• Many avoid nuts, vinegar, and sharp foods on Rosh Hashana (minhag).
• Tashlich (casting sins into water) is often on the first afternoon — follow your community.

${diasporaSecondDayNote(profile, "Rosh Hashana")}""",
            roshHashanaLinks(profile)
        )

        HebrewCalendarEngine.YOM_KIPPUR -> Triple(
            "Erev Yom Kippur prep",
            """Yom Kippur — Day of Atonement. Full 25-hour fast; five afflictions from sunset tonight until nightfall tomorrow.

Today before the fast:
• Eat a festive pre-fast meal (seudah hamafseket) before sunset — finish eating and drinking in time.
• Light Yom Tov candles before Kol Nidre; many use a bracha and a flame lit before Yom Kippur begins.
• Give tzedakah and ask forgiveness from others.
• Kaparot (if your custom) is done before Yom Kippur.

On Yom Kippur (no eating, drinking, washing for pleasure, anointing, leather shoes, or marital relations):
• Spend the day in prayer at shul (Kol Nidre tonight, full day of services tomorrow).
• Many wear white and avoid leather shoes.
• Ne'ilah and shofar at the end; break fast after nightfall tomorrow.""",
            yomKippurLinks(profile)
        )

        HebrewCalendarEngine.PESACH -> Triple(
            "Erev Pesach prep — Yom Tov & seder tonight",
            """Pesach begins tonight. Torah-level Yom Tov — no melacha.

Chametz:
• All chametz must be gone before sunset (sold, burned, or removed). Use today's Erev Pesach checklist items for bedikat chametz, biur, and mechirat chametz if still on your list.

Tonight — seder (first night):
• ${if (profile.isInIsrael) "In Israel: one seder tonight." else "In the Diaspora: first seder tonight; second seder tomorrow night."}
• Matzah, maror, four cups of wine, reading the Haggadah, reclining, afikoman.
• Kiddush, festive meal, Hallel, Nirtzah.

Tomorrow by day:
• Yom Tov davening with Hallel and Musaf; no chametz or kitniyot (per your custom).
• Only eat food prepared for Pesach in kosher-for-Passover utensils.

${diasporaSecondDayNote(profile, "Pesach")}""",
            pesachChagLinks(profile)
        )

        HebrewCalendarEngine.SHAVUOS -> Triple(
            "Erev Shavuot prep",
            """Shavuot — receiving the Torah at Sinai. Yom Tov from tonight.

Tonight & tomorrow:
• Light Yom Tov candles; Shehecheyanu on the first night.
• Many have the custom of dairy meals (cheesecake, blintzes) — also meat meals are permitted.
• All-night Torah learning (Tikkun Leil Shavuot) is a widespread custom tonight.
• Read Megillat Rut in many communities (tomorrow).
• Full Yom Tov davening with Hallel and Musaf; Akdamut/Megillat Rut per minhag.

No melacha; treat meals and prayer with joy and Torah focus.""",
            shavuotLinks(profile)
        )

        HebrewCalendarEngine.SUCCOS -> Triple(
            "Erev Sukkot prep — Yom Tov tonight",
            """Sukkot (first day) begins tonight — Zman Simchateinu.

Before sunset:
• Eat and spend time in the sukkah if it is built (mitzvah of sukkah begins with the festival).
• Have arba minim ready: lulav, etrog, hadasim, aravot (per your rabbi's kashrut standards).

Tonight & tomorrow:
• Light Yom Tov candles in the sukkah (per custom) or home; Shehecheyanu first night.
• Shake lulav and etrog with brachot tomorrow (first day of Sukkot).
• Festive meals in the sukkah; Ushpizin welcome guests.
• No melacha; Hallel and Musaf in davening.

${diasporaSecondDayNote(profile, "Sukkot")}""",
            sukkotLinks(profile)
        )

        HebrewCalendarEngine.SHEMINI_ATZERES -> Triple(
            "Erev Shemini Atzeret prep",
            """Shemini Atzeret begins tonight${if (profile.isInIsrael) " — in Israel this is also Simchat Torah." else "."}

Tonight & tomorrow:
• Light Yom Tov candles; Shehecheyanu (first night).
• ${if (profile.isInIsrael) "Simchat Torah in Israel: hakafot, finishing and restarting the Torah cycle, joyous dancing with the Torah." else "Shemini Atzeret in the Diaspora: Yizkor is often recited; still a Yom Tov with no melacha. (Simchat Torah is tomorrow in the Diaspora.)"}
• No lulav on Shemini Atzeret (outside Israel some still shake without bracha on first day only — follow your rav).
• Geshem (prayer for rain) begins in Musaf in Israel and many communities.
• Festive meals; no sukkah mitzvah in Israel on Shemini Atzeret.""",
            sheminiAtzeretLinks(profile)
        )

        HebrewCalendarEngine.SIMCHAS_TORAH -> Triple(
            "Erev Simchat Torah prep",
            """Simchat Torah begins tonight (Diaspora) — rejoicing with the Torah.

Tonight & tomorrow:
• Light Yom Tov candles; Shehecheyanu on the first night.
• Hakafot — dancing with Torah scrolls; finish the annual cycle and begin Bereshit.
• Festive meals and drinking (responsibly) in many communities.
• Full Yom Tov — no melacha; Hallel and Musaf.
• In Israel, Simchat Torah coincides with Shemini Atzeret (one day).""",
            simchatTorahLinks(profile)
        )

        else -> Triple(
            "Erev $name prep",
            "Tomorrow is $name. Observe Yom Tov laws: no melacha, festive meals, special davening. Ask your rabbi for details specific to this day.",
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
        add(ChecklistLink("Peninei Halacha — Rosh Hashana", "https://ph.yhb.org.il/en/03-05-00/", "default"))
        add(ChecklistLink("Aish — Rosh Hashana", "https://aish.com/holidays/rosh-hashanah/", "default"))
        add(ChecklistLink("Ohr Somayach — Rosh Hashana", "https://ohr.edu/yhiy/rosh-hashana/", "default"))
    }

    private fun yomKippurLinks(profile: UserProfile) = buildList {
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(ChecklistLink("Chabad — Yom Kippur", "https://www.chabad.org/holidays/yomkippur/default_cdo/jewish/Yom-Kippur.htm", "chabad"))
        }
        add(ChecklistLink("Peninei Halacha — Yom Kippur", "https://ph.yhb.org.il/en/03-09-00/", "default"))
        add(ChecklistLink("Aish — Yom Kippur", "https://aish.com/holidays/yom-kippur/", "default"))
        add(ChecklistLink("Ohr Somayach — Yom Kippur", "https://ohr.edu/yhiy/yom-kippur/", "default"))
    }

    private fun pesachChagLinks(profile: UserProfile) = buildList {
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(ChecklistLink("Chabad — Passover", "https://www.chabad.org/holidays/passover/default_cdo/jewish/Passover.htm", "chabad"))
        }
        add(ChecklistLink("Peninei Halacha — Pesach", "https://ph.yhb.org.il/en/category/moadim/04-pesach/", "default"))
        add(ChecklistLink("Aish — Passover", "https://aish.com/holidays/pesach/", "default"))
        add(ChecklistLink("Ohr Somayach — Pesach", "https://ohr.edu/yhiy/pesach/", "default"))
    }

    private fun shavuotLinks(profile: UserProfile) = buildList {
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(ChecklistLink("Chabad — Shavuot", "https://www.chabad.org/holidays/shavuot/default_cdo/jewish/Shavuot.htm", "chabad"))
        }
        add(ChecklistLink("Peninei Halacha — Shavuot", "https://ph.yhb.org.il/en/03-07-00/", "default"))
        add(ChecklistLink("Aish — Shavuot", "https://aish.com/holidays/shavuot/", "default"))
    }

    private fun sukkotLinks(profile: UserProfile) = buildList {
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(ChecklistLink("Chabad — Sukkot", "https://www.chabad.org/holidays/sukkot/default_cdo/jewish/Sukkot.htm", "chabad"))
        }
        add(ChecklistLink("Peninei Halacha — Sukkot", "https://ph.yhb.org.il/en/category/moadim/13-sukkot/", "default"))
        add(ChecklistLink("Aish — Sukkot", "https://aish.com/holidays/sukkot/", "default"))
    }

    private fun sheminiAtzeretLinks(profile: UserProfile) = buildList {
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(ChecklistLink("Chabad — Shemini Atzeret", "https://www.chabad.org/holidays/sukkot/article_cdo/aid/1771/jewish/Shemini-Atzeret.htm", "chabad"))
        }
        add(ChecklistLink("Peninei Halacha — Sukkot festivals", "https://ph.yhb.org.il/en/category/moadim/13-sukkot/", "default"))
    }

    private fun simchatTorahLinks(profile: UserProfile) = buildList {
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(ChecklistLink("Chabad — Simchat Torah", "https://www.chabad.org/holidays/sukkot/article_cdo/aid/1772/jewish/Simchat-Torah.htm", "chabad"))
        }
        add(ChecklistLink("Peninei Halacha — Simchat Torah", "https://ph.yhb.org.il/en/category/moadim/13-sukkot/", "default"))
        add(ChecklistLink("Aish — Simchat Torah", "https://aish.com/holidays/sukkot/", "default"))
    }

    private fun defaultChagLinks(profile: UserProfile) = buildList {
        add(ChecklistLink("Peninei Halacha — Festivals", "https://ph.yhb.org.il/en/category/moadim/", "default"))
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(ChecklistLink("Chabad — Holidays", "https://www.chabad.org/holidays/default_cdo/jewish/Jewish-Holidays.htm", "chabad"))
        }
    }
}
