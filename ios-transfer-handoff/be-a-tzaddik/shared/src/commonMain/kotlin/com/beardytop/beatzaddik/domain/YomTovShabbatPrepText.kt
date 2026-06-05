package com.beardytop.beatzaddik.domain

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.plus

/**
 * Calendar-driven copy when Yom Tov and Shabbat meet (eruv tavshilin, havdalah in Kiddush).
 * Wording follows Chabad.org, Peninei Halakha, Aish, and Ohr Somayach — not invented psak.
 * Shown only in years when detection helpers return true.
 */
object YomTovShabbatPrepText {

    private fun dayAfter(cal: DayInfo, days: Int = 1) = cal.date.plus(days, DateTimeUnit.DAY)

    fun isRoshHashanaChag(cal: DayInfo, chagName: String): Boolean =
        cal.upcomingChagYomTovIndex == HebrewCalendarEngine.ROSH_HASHANA ||
            chagName.contains("Rosh Hashana", ignoreCase = true)

    /** Civil Shabbat that follows this erev chag (1 = tomorrow is Shabbat, 3 = two Yom Tov days then Shabbat). */
    private fun shabbatDaysAfterErevChag(cal: DayInfo): Int? {
        if ("erev_chag" !in cal.activeSeasons) return null
        for (i in 1..4) {
            if (dayAfter(cal, i).dayOfWeek == DayOfWeek.SATURDAY) return i
        }
        return null
    }

    fun isShabbatErevChag(cal: DayInfo): Boolean =
        "erev_chag" in cal.activeSeasons && cal.isShabbat

    /** Friday before civil Shabbat that is erev chag (Motzei Shabbat → Yom Tov). */
    fun isFridayBeforeShabbatErevChag(today: DayInfo, tomorrow: DayInfo): Boolean =
        today.isErevShabbat && !today.isShabbat && isShabbatErevChag(tomorrow)

    /**
     * Today is erev chag on the calendar, but the festival actually begins only after
     * tomorrow's Shabbat (e.g. Friday marked erev when RH starts Saturday night).
     */
    fun isErevChagBeforeShabbatErevChag(today: DayInfo, tomorrow: DayInfo): Boolean =
        "erev_chag" in today.activeSeasons && isFridayBeforeShabbatErevChag(today, tomorrow)

    /** Yom Tov begins tonight; one day of Yom Tov then Shabbat (erev is Friday). */
    fun isErevChagYomTovStartsBeforeShabbat(cal: DayInfo): Boolean {
        if ("erev_chag" !in cal.activeSeasons || !cal.isErevShabbat) return false
        if (ErevPesachPrepText.isErevPesachFridayBeforeShabbatPesach(cal)) return false
        return shabbatDaysAfterErevChag(cal) == 2 &&
            cal.upcomingChagYomTovIndex != null &&
            HebrewCalendarEngine.isYomTovAssurBemelacha(cal.upcomingChagYomTovIndex!!)
    }

    /** Diaspora: two days of Yom Tov starting tomorrow, then Shabbat (erev is Wed or Thu). */
    fun isErevChagTwoDayYomTovBeforeShabbat(cal: DayInfo, profile: UserProfile): Boolean {
        if (profile.isInIsrael) return false
        if ("erev_chag" !in cal.activeSeasons) return false
        if (ErevPesachPrepText.isErevPesachFridayBeforeShabbatPesach(cal)) return false
        return shabbatDaysAfterErevChag(cal) == 3 &&
            cal.upcomingChagYomTovIndex != null &&
            HebrewCalendarEngine.isYomTovAssurBemelacha(cal.upcomingChagYomTovIndex!!)
    }

    fun isYomTovFridayLeadsIntoShabbat(cal: DayInfo): Boolean =
        cal.isYomTovAssurBemelacha && cal.date.dayOfWeek == DayOfWeek.FRIDAY

    fun requiresEruvTavshilin(cal: DayInfo, profile: UserProfile): Boolean =
        isErevChagYomTovStartsBeforeShabbat(cal) ||
            isErevChagTwoDayYomTovBeforeShabbat(cal, profile) ||
            ErevPesachPrepText.isErevPesachFridayBeforeShabbatPesach(cal) ||
            isYomTovFridayLeadsIntoShabbat(cal)

    fun scheduleBlock(
        cal: DayInfo,
        profile: UserProfile,
        chagName: String,
        forFridayAdvance: Boolean = false,
    ): String? {
        val parts = mutableListOf<String>()
        shabbatErevChagHavdalahBlock(cal, profile, chagName, forFridayAdvance)?.let { parts.add(it) }
        eruvTavshilinBlock(cal, profile, chagName)?.let { parts.add(it) }
        yomTovFridayCookingBlock(cal, profile, chagName)?.let { parts.add(it) }
        return parts.takeIf { it.isNotEmpty() }?.joinToString("\n\n")
    }

    /**
     * Show on a readable day before prep is needed — especially **Friday before Shabbat–erev–chag**
     * (checklist is off on Shabbat). Also Thu/Wed before other Yom Tov–Shabbat sequences.
     */
    fun shouldShowAdvancePrepDay(today: DayInfo, tomorrow: DayInfo, profile: UserProfile): Boolean {
        if (HolyDayPhoneRules.isShabbatMelachaDay(today)) return false
        if (isFridayBeforeShabbatErevChag(today, tomorrow)) return true
        if ("erev_chag" in today.activeSeasons) return false
        return advanceBlock(today, tomorrow, profile) != null
    }

    fun advanceBlock(today: DayInfo, tomorrow: DayInfo, profile: UserProfile): String? {
        val parts = mutableListOf<String>()
        val tomorrowChag = tomorrow.upcomingChagName
            ?: if ("erev_pesach" in tomorrow.activeSeasons) "Pesach" else null

        if (tomorrowChag != null) {
            scheduleBlock(tomorrow, profile, tomorrowChag, forFridayAdvance = true)?.let { body ->
                val intro = if (isShabbatErevChag(tomorrow)) {
                    """
Read this today (Friday) before Shabbat candles — the app is not for use on Shabbat.

Tomorrow is Shabbat and erev $tomorrowChag. $tomorrowChag begins tomorrow night at nightfall (Motzei Shabbat), not tonight. Finish Yaknehaz prep, Yom Tov candles from a pre-existing flame, wine, and festive food before Shabbat ends.
                    """.trim()
                } else {
                    """
Tomorrow is erev $tomorrowChag — the Yom Tov–Shabbat rules below apply starting then. Use today (during the day) to prepare so you are not caught tonight or tomorrow without eruv tavshilin, flames, or food in place.
                    """.trim()
                }
                parts.add("$intro\n\n$body")
            }
        }

        if ("erev_pesach" !in today.activeSeasons) {
            ErevPesachPrepText.erevPesachShabbatScheduleBlock(today)?.let { body ->
                parts.add(
                    """
Pesach meets Shabbat this year — some steps happen today or tonight (before tomorrow's erev Pesach checklist):

$body
                    """.trim()
                )
            }
        }

        return parts.takeIf { it.isNotEmpty() }?.joinToString("\n\n\n")
    }

    fun links(cal: DayInfo, profile: UserProfile, chagName: String): List<ChecklistLink> = buildList {
        add(
            ChecklistLink(
                "Chabad — Eruv tavshilin",
                "https://www.chabad.org/library/article_cdo/aid/2327/jewish/Eruv-Tavshilin.htm",
                "chabad"
            )
        )
        add(
            ChecklistLink(
                "Chabad — Holiday Havdalah (Yaknehaz)",
                "https://www.chabad.org/library/article_cdo/aid/611296/jewish/Holiday-Havdalah.htm",
                "chabad"
            )
        )
        add(
            ChecklistLink(
                "Chabad — What you need to know about eruv tavshilin",
                "https://www.chabad.org/library/article_cdo/aid/7292713/jewish/What-You-Need-to-Know-About-Eruv-Tavshilin.htm",
                "chabad"
            )
        )
        add(ChecklistLink("Peninei Halacha — Eruv tavshilin", "https://ph.yhb.org.il/en/12-08-02/", "default"))
        add(ChecklistLink("Peninei Halacha — Festivals & Shabbat", "https://ph.yhb.org.il/en/category/12/12-08/", "default"))
        add(ChecklistLink("Aish — Holidays & Shabbat", "https://aish.com/holidays/", "default"))
        add(ChecklistLink("Ohr Somayach — Yom Tov", "https://ohr.edu/holidays/yom_tov/", "default"))
        if (isRoshHashanaChag(cal, chagName)) {
            if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
                add(
                    ChecklistLink(
                        "Chabad — Rosh Hashana",
                        "https://www.chabad.org/holidays/jewishnewyear/default_cdo/jewish/Jewish-New-Year.htm",
                        "chabad"
                    )
                )
            }
            add(ChecklistLink("Peninei Halacha — Rosh Hashana", "https://ph.yhb.org.il/en/category/15/15-03/", "default"))
            add(ChecklistLink("Aish — Rosh Hashana", "https://aish.com/holidays/rosh-hashanah/", "default"))
            add(ChecklistLink("Ohr Somayach — Rosh Hashana", "https://ohr.edu/holidays/rosh_hashana/", "default"))
        }
    }

    private fun shabbatErevChagHavdalahBlock(
        cal: DayInfo,
        profile: UserProfile,
        chagName: String,
        forFridayAdvance: Boolean,
    ): String? {
        if (!isShabbatErevChag(cal)) return null
        if (!forFridayAdvance && HolyDayPhoneRules.isShabbatMelachaDay(cal)) return null
        return if (isRoshHashanaChag(cal, chagName)) {
            roshHashanaHavdalahInKiddushBlock(chagName, forFridayAdvance)
        } else {
            genericHavdalahInKiddushBlock(chagName, forFridayAdvance)
        }
    }

    private fun genericHavdalahInKiddushBlock(chagName: String, forFridayAdvance: Boolean): String {
        val opener = if (forFridayAdvance) {
            """
This year, tomorrow (Shabbat) is Erev $chagName — $chagName begins tomorrow night at nightfall (Motzei Shabbat). This doesn't happen every year.
            """.trim()
        } else {
            """
This year, Shabbat is Erev $chagName — $chagName begins tonight at nightfall (Motzei Shabbat). This doesn't happen every year.
            """.trim()
        }
        val prepWhen = if (forFridayAdvance) {
            "Prepare today (Friday) before Shabbat candles"
        } else {
            "Prepare before Shabbat ends"
        }
        return BeginnerHalachaGlossary.withKeyTerms(
        BeginnerHalachaGlossary.yomTovAndShabbat(),
        """
$opener

Per Chabad.org (Holiday Havdalah) and widespread halachic practice:
• Havdalah is recited when entering a day of lesser holiness. Shabbat is holier than Yom Tov, so when Shabbat leads into a festival, havdalah is included in that night's Kiddush — not as a full separate havdalah with spices before Kiddush.
• Order (mnemonic YaKNeHaZ per many Ashkenaz poskim, cited by STAR-K and Torah Mitzion): Yayin (borei pri hagafen) → Kiddush for Yom Tov → Ner (borei me'orei ha'eish on the Yom Tov candles) → Havdalah (holiday text ending bein kodesh l'kodesh, not bein kodesh l'chol) → Zeman (Shehecheyanu on the first festival night when applicable).
• Spices (besamim) are omitted for this transition (Chabad.org — Holiday Havdalah).
• Before Kiddush, melacha permitted on Yom Tov but not on Shabbat: many say Baruch hamavdil bein kodesh l'kodesh, or rely on the Vatodi'enu insert in Maariv — follow your Machzor (Chabad.org).

$prepWhen: Yom Tov candles from a pre-existing flame; wine; festive meal ready; 48-hour candle or pilot light per your rav.
    """.trim(),
    )
    }

    private fun roshHashanaHavdalahInKiddushBlock(chagName: String, forFridayAdvance: Boolean): String {
        val opener = if (forFridayAdvance) {
            """
This year, tomorrow (Shabbat) is Erev Rosh Hashana — Rosh Hashana begins tomorrow night at nightfall (Motzei Shabbat). This doesn't happen every year.
            """.trim()
        } else {
            """
This year, Shabbat is Erev Rosh Hashana — Rosh Hashana begins tonight at nightfall (Motzei Shabbat). This doesn't happen every year.
            """.trim()
        }
        val prepLead = if (forFridayAdvance) {
            "Rosh Hashana–specific prep today (Friday) before Shabbat"
        } else {
            "Rosh Hashana–specific prep before Shabbat ends"
        }
        return BeginnerHalachaGlossary.withKeyTerms(
        BeginnerHalachaGlossary.yomTovAndShabbat(),
        """
$opener

Sources: Chabad.org — Holiday Havdalah; Chabad.org — Rosh Hashana guides; Peninei Halakha — Festivals.

Havdalah inside Kiddush (Yaknehaz):
• Chabad.org states that when a biblical holiday begins Saturday night (including Rosh Hashana), that night's Kiddush incorporates havdalah for Shabbat. The order is: (1) wine blessing, (2) holiday Kiddush, (3) candle blessing, (4) havdalah blessing — with the Yom Tov text concluding bein kodesh l'kodesh (not bein kodesh l'chol), (5) Shehecheyanu for the festival.
• This is the YaKNeHaZ order (Yayin, Kiddush, Ner, Havdalah, Zeman) described in Shulchan Arukh and summarized by STAR-K and Torah Mitzion for Motzei Shabbat into Yom Tov.
• Spices are not used (Chabad.org — Holiday Havdalah).
• Use the Machzor or siddur nusach for Rosh Hashana — do not rely on memory for the long havdalah text.

Before or at Maariv:
• Chabad.org: you may say Baruch hamavdil bein kodesh l'kodesh to begin Yom Tov-permitted activities before Kiddush, or rely on Vatodi'enu in the Amidav of Maariv — follow your community.

$prepLead:
• Have round challah, honey, apples, and symbolic foods ready for the Yom Tov meals after Shabbat (minhag, per Rosh Hashana guides).
• Confirm shofar and Musaf times for the first day(s) of Rosh Hashana after Shabbat — shofar is not blown on Shabbat itself.
• Tashlich, when observed, is scheduled per your community calendar (often not on Shabbat).

Candles: after Shabbat ends, light Yom Tov candles from a flame lit before Shabbat began (Chabad.org — from a pre-existing flame).
    """.trim(),
    )
    }

    private fun eruvTavshilinBlock(cal: DayInfo, profile: UserProfile, chagName: String): String? {
        if (!requiresEruvTavshilin(cal, profile)) return null
        if (isYomTovFridayLeadsIntoShabbat(cal)) return null

        return if (isRoshHashanaChag(cal, chagName)) {
            roshHashanaEruvBlock(cal, profile)
        } else {
            genericEruvBlock(cal, profile, chagName)
        }
    }

    private fun genericEruvBlock(cal: DayInfo, profile: UserProfile, chagName: String): String {
        val whenLine = eruvWhenLine(cal, profile, chagName)
        return BeginnerHalachaGlossary.withKeyTerms(
            BeginnerHalachaGlossary.yomTovAndShabbat(),
            """
This year, $chagName meets Shabbat on the calendar — you need eruv tavshilin. This doesn't happen every year.

Why (Peninei Halakha 12:8; Chabad.org — Eruv Tavshilin):
• On Yom Tov, cooking is generally allowed only for that calendar day of Yom Tov, not for the next day.
• When Yom Tov is immediately followed by Shabbat, the Sages required eruv tavshilin so you may cook on Yom Tov for Shabbat, and so Shabbat is not forgotten amid festival preparations.

$whenLine

How (Chabad.org; Peninei Halakha 12:8:2):
• Set aside a baked food (challah or matzah) and a cooked food (meat, fish, or unpeeled hard-boiled egg are common examples).
• Recite the blessing and declaration from your Machzor or siddur in any language you understand — use the printed wording; do not paraphrase from memory.
• One eruv per household is sufficient (Chabad.org).
• Peninei Halakha: food must be appropriate to eat with bread; at least a kezayit of cooked food should remain until Shabbat prep is done.

Limits (Chabad.org):
• Permits cooking and food prep on Yom Tov for Shabbat only — not cooking on one day of Yom Tov for the next festival day.
• Food should be ready early enough Friday afternoon that it could theoretically be eaten before Shabbat (Chabad.org — Eruv Tavshilin).
• Does not permit non-food Shabbat prep (e.g. certain laundry) — ask your rav.
        """.trim(),
        )
    }

    private fun roshHashanaEruvBlock(cal: DayInfo, profile: UserProfile): String {
        val whenLine = eruvWhenLine(cal, profile, "Rosh Hashana")
        return BeginnerHalachaGlossary.withKeyTerms(
            BeginnerHalachaGlossary.yomTovAndShabbat(),
            """
This year, Rosh Hashana leads into Shabbat on the calendar — you need eruv tavshilin. This doesn't happen every year.

Why (Chabad.org — What You Need to Know About Eruv Tavshilin; Peninei Halakha 12:8):
• When Rosh Hashana (or its second day in the Diaspora) falls on Friday, Shabbat follows immediately. Without an eruv, you may not cook on Yom Tov for Shabbat.
• The eruv reminds the household to prepare for Shabbat, not only for the Days of Awe.

$whenLine

How to make eruv tavshilin (Chabad.org — Eruv Tavshilin; Peninei Halakha 12:8:2):
• Foods: challah (or matzah) plus cooked food — commonly fish, meat, or an unpeeled hard-boiled egg.
• Blessing: Asher kid'shanu b'mitzvotav v'tzivanu al mitzvat eruv (Peninei Halakha) — use your siddur text.
• Declaration: recite the eruv declaration from your Machzor or siddur in any language you understand (traditionally Aramaic; many editions include translation) — use the printed wording; it permits baking, cooking, lighting, and food prep on Yom Tov for Shabbat.
• Store the eruv foods safely; many eat them at a Shabbat meal (Peninei Halakha — lechem mishneh / oneg Shabbat).

Rosh Hashana notes:
• Eruv allows Shabbat **food** prep on Friday Yom Tov — honey cake, challah, fish, soup, etc. — not melacha forbidden on Yom Tov itself.
• If the first day(s) were Thursday–Friday in the Diaspora, only **Friday** Yom Tov cooking for Shabbat uses this eruv (Chabad.org: eruv does not permit Thursday cooking for Friday Yom Tov).
• Confirm communal eruv from your rabbi does not replace your household eruv for personal cooking — many poskim require each home to make its own (ask your rav).

Shofar & davening: eruv does not change shofar rules — shofar is blown on Yom Tov days of Rosh Hashana when not Shabbat, per your minhag and calendar.
        """.trim(),
        )
    }

    private fun eruvWhenLine(cal: DayInfo, profile: UserProfile, chagName: String): String = when {
        ErevPesachPrepText.isErevPesachFridayBeforeShabbatPesach(cal) ->
            "When: Today (Friday) before Shabbat and Yom Tov candle lighting — Pesach begins tonight; tomorrow is Shabbat and the first day of Yom Tov. Make eruv before candles so you may cook on that Yom Tov for Shabbat meals (Peninei Halakha 12:8; Chabad.org)."
        isErevChagTwoDayYomTovBeforeShabbat(cal, profile) ->
            "When: Today, before the first day of $chagName tomorrow — in the Diaspora the next two days are Yom Tov, then Shabbat (Chabad.org: set eruv on the day before the festival begins)."
        isErevChagYomTovStartsBeforeShabbat(cal) ->
            "When: Today (Friday) before Yom Tov candle lighting — $chagName begins tonight and Shabbat is tomorrow (Chabad.org)."
        else ->
            "When: Before Yom Tov begins — follow Chabad.org and your Machzor for the exact time."
    }

    private fun yomTovFridayCookingBlock(cal: DayInfo, profile: UserProfile, chagName: String): String? {
        if (!isYomTovFridayLeadsIntoShabbat(cal)) return null
        return if (isRoshHashanaChag(cal, chagName)) {
            BeginnerHalachaGlossary.withKeyTerms(
                BeginnerHalachaGlossary.yomTovAndShabbat(),
                """
This year, today is Rosh Hashana on Friday and Shabbat begins tonight — the Yom Tov–Shabbat sequence is in progress (Chabad.org; Peninei Halakha).

If you made eruv tavshilin before Rosh Hashana began:
• You may cook and otherwise prepare food on Yom Tov today for Shabbat meals, within the limits above (food ready with time before Shabbat; no cooking for weekday or for the previous Yom Tov day).
• Many prepare honey challah, fish, soup, and other Shabbat/Rosh Hashana dishes today.

Shofar: blown today (if today is a Yom Tov day of Rosh Hashana and not Shabbat) per shul schedule — not on Shabbat itself.

If you did not make eruv tavshilin: ask your rabbi immediately what you may still prepare for Shabbat.

Shabbat tonight: when Yom Tov ends Friday before Shabbat, light Shabbat candles from a pre-existing flame and follow your siddur for the transition — this is not the same as Motzei Shabbat into Yom Tov (no Yaknehaz tonight).
            """.trim(),
            )
        } else {
            BeginnerHalachaGlossary.withKeyTerms(
                BeginnerHalachaGlossary.yomTovAndShabbat(),
                """
This year, today is $chagName on Friday and Shabbat begins tonight — the Yom Tov–Shabbat sequence is in progress.

Today on Yom Tov (if you made eruv tavshilin before the festival began, per Chabad.org and Peninei Halakha):
• You may cook and prepare for Shabbat meals today — within halachic limits (food ready with time to spare before Shabbat).
• Finish preparations before Shabbat; set timers/blech as guided by your rav.

If you have not made eruv tavshilin, ask your rabbi immediately.

Shabbat tonight: light candles from a pre-existing flame after Yom Tov ends; follow your siddur for when Yom Tov ends and Shabbat begins.
            """.trim(),
            )
        }
    }
}
