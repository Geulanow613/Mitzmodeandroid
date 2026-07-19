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
            isShavuos -> ZmanimFormatter.formatTime(sunset, tz)?.let { sunsetTime ->
                val tzeitNote = ZmanimFormatter.formatTime(tzeit, tz)?.let { " (approx. $it today)" } ?: ""
                "• Shavuot candles: light before sunset today ($sunsetTime) like other Yom Tov — strike a new match while it is still weekday. After sunset you may not create a new flame. Crucial Omer note: delay Maariv and Kiddush until full nightfall (tzeit$tzeitNote) so all 49 days of the Omer are complete before the festive meal. (When Shavuot begins Motzei Shabbat instead, light only from a pre-existing flame after Shabbat ends — see that prep item.)"
            } ?: "• Shavuot candles: light before sunset like other Yom Tov (new match OK while still weekday). Delay Maariv and Kiddush until full nightfall (tzeit) so all 49 days of the Omer are complete. When Shavuot begins Motzei Shabbat, light only from a pre-existing flame after Shabbat ends."
            else -> ZmanimFormatter.formatTime(sunset, tz)?.let {
                "• Candle lighting & Yom Tov begin at sunset today ($it) — before sunset you may strike a new match normally to light Yom Tov candles and to kindle a 24- or 48-hour transfer candle for use after the holiday begins. Once Yom Tov has started, light candles only from a pre-existing flame (not a new match or lighter)."
            } ?: "• Candle lighting & Yom Tov begin at sunset — enable location for your local time."
        }

        val cookingLine = if (isShavuos) {
            "• Finish weekday cooking/setup before sunset; after sunset, festival ochel-nefesh cooking rules apply (pre-existing flame). Hold the night meal / Kiddush until after tzeit for the Omer reason above."
        } else {
            "• Finish weekday cooking/setup before sunset; after Yom Tov begins, see ochel-nefesh rules below."
        }

        val isYomKippur = cal.upcomingChagYomTovIndex == HebrewCalendarEngine.YOM_KIPPUR
        val lawsBlock = if (isYomKippur) "" else "\n\n${festivalYomTovLawsBlock()}"
        val simchasBlock = if (HebrewCalendarEngine.isShaloshRegalim(cal.upcomingChagYomTovIndex)) {
            "\n\n${SeasonalMitzvahText.simchasYomTovPrepBlock()}"
        } else {
            ""
        }

        val shulServicesLine = if (isYomKippur) {
            "• Confirm shul times for tonight and tomorrow (Kol Nidre / Maariv / Shacharit / Musaf)."
        } else {
            "• Confirm shul times for tonight and tomorrow (Maariv / Shacharit / Musaf)."
        }

        return """Before chag — every erev Yom Tov:
$sunsetLine
$cookingLine
• Turn off phones and devices before Yom Tov — this app is for prep, not use on chag.
$shulServicesLine$lawsBlock$simchasBlock"""
    }

    /**
     * Core festival Yom Tov vs Shabbat laws for erev explainers.
     * Skips eruv how-to (see [YomTovShabbatPrepText] when the calendar requires it),
     * simchas gifts/meat/wine ([SeasonalMitzvahText.simchasYomTovPrepBlock]), and
     * Shehecheyanu (holiday-specific lines above).
     */
    private fun festivalYomTovLawsBlock(): String = """
Yom Tov vs Shabbat — ochel nefesh:
Preparing for a biblical festival (Pesach, Shavuot, Sukkot, Rosh Hashana, and similar) is similar to preparing for Shabbat, but with critical differences. Shabbat is absolute rest; Yom Tov is physical, communal celebration. The Torah therefore permits certain food-related melacha on Yom Tov — ochel nefesh ("food for sustenance") — that Shabbat forbids (Exodus 12:16).

Fire and cooking:
• Shabbat: no cooking, baking, boiling, or transferring fire — food must be fully cooked before Friday sunset.
• Yom Tov: you may cook, bake, and boil fresh food for that day's meals from a pre-existing flame only. Do not create a new fire (match, lighter, electric stove igniter) once Yom Tov has begun.
• Before Yom Tov begins: light a 24- or 48-hour transfer candle (often called a yahrzeit / holiday candle). On the festival, transfer flame with a splint or toothpick to light a stove burner or second-night candles.
• You may raise an existing gas flame to cook; lowering or extinguishing a flame is generally forbidden (narrow grama / indirect methods only per your rav).

Kitchen / plata on Yom Tov:
• You may reheat fully cooked cold liquids (soup, etc.) and dry food on a plata for that day's meals — the common Ashkenazi Shabbat restriction against warming cold liquids does not apply the same way, because reheating is part of simchat Yom Tov.
• Meechin (preparing for the next day): cook and wash only for that Yom Tov day — not for Day 2 of Yom Tov or for the weekday after. If you cook a large pot intending to eat some of it today and leftovers remain, that is fine; do not cook on Day 1 solely for Day 2.

Carrying:
• Shabbat: no carrying between private and public domains without a kosher eruv.
• Yom Tov: carrying is permitted even without an eruv when the item is useful for the holiday (siddur, keys, stroller, food to a friend's meal, etc.).

When Yom Tov meets Shabbat:
• Yom Tov on Friday leading into Shabbat: make eruv tavshilin before Yom Tov begins (blessing + declaration over cooked food and a baked item — see the Eruv Tavshilin checklist item / section when this year requires it). That lets you finish Shabbat food prep on Friday Yom Tov.
• Yom Tov on Saturday: Shabbat laws fully override Yom Tov cooking and carrying leniencies — treat the day as Shabbat, with festival prayers and Torah readings added.
""".trim()

    private fun shofarErevLine(tomorrowCal: DayInfo): String =
        if (tomorrowCal.isShabbat) {
            "Hear the shofar on Sunday (the second day) — shofar is not blown when the first day of Rosh Hashana is Shabbat."
        } else {
            "Hear the shofar blown during daytime services tomorrow (not tonight)."
        }

    private fun lulavErevLine(profile: UserProfile, tomorrowCal: DayInfo): String = when {
        tomorrowCal.isShabbat && profile.isInIsrael ->
            "No lulav on Shabbat — first taking with bracha (and Shehecheyanu if you deferred it) is Sunday."
        tomorrowCal.isShabbat && !profile.isInIsrael ->
            "No lulav on Shabbat (first day). Shake with bracha on Sunday (second day of Yom Tov in the Diaspora); continue on Chol HaMoed per custom."
        profile.isInIsrael ->
            "Shake lulav and etrog with bracha tomorrow (first day of Sukkot)."
        else ->
            "Shake lulav and etrog with bracha on the first and second days of Yom Tov (Diaspora); continue on Chol HaMoed per custom."
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
                    when {
                        profile.isInIsrael ->
                            "• Recite Shehecheyanu tonight (the opening night)."
                        tomorrowCal.hebrewDay == 15 ->
                            "• Recite Shehecheyanu at Kiddush on both opening nights (tonight and tomorrow night)."
                        else ->
                            "• Recite Shehecheyanu tonight (the second opening night in the Diaspora)."
                    }
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
            HebrewCalendarEngine.SUCCOS, HebrewCalendarEngine.SHAVUOS ->
                if (profile.isInIsrael) {
                    "• Recite Shehecheyanu tonight (the first night)."
                } else {
                    "• Recite Shehecheyanu at Kiddush on both nights of Yom Tov."
                }
            else -> "• Recite Shehecheyanu tonight."
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
• ${shofarErevLine(tomorrowCal)}
• Daven from a Machzor: Rosh Hashana uses a special festival Amidah (and a unique Musaf) — not the regular prayer with a small insert. Tachanun is omitted.

Customs:
• Greet others with wishes for a good year (L'shanah tovah).
• Many avoid nuts, vinegar, and sharp foods on Rosh Hashana (minhag).
• Tashlich: on the first afternoon when Rosh Hashana is not Shabbat. When the first day is Shabbat: Ashkenazim customarily postpone to Sunday; many Sephardic communities recite Tashlich on the first day (where a kosher eruv permits carrying a machzor). Tashlich is prayers at the water — not feeding fish (breadcrumbs are forbidden on Shabbat and Yom Tov). If you missed it, many communities do it later — commonly any day until Hoshana Rabbah; follow your minhag.

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
                    "Pesach begins tonight at sunset — light Yom Tov/Shabbat candles before sunset (new match OK). Tonight begins 15 Nisan (first Seder); tomorrow daytime is still the first day of Pesach and is Shabbat. Torah-level Yom Tov — no melacha from sunset."
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
                    "• Yom Tov davening with Full Hallel and Musaf; no chametz (and no kitniyot if that is your custom)."
                EffectiveNusach.SEFARD, EffectiveNusach.EDOT_HAMIZRACH ->
                    "• Yom Tov davening with Full Hallel and Musaf; no chametz."
                EffectiveNusach.OTHER ->
                    "• Yom Tov davening with Full Hallel and Musaf; no chametz. Kitniyot customs vary — follow your minhag."
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
• Reclining (hasebha): Men recline to the left when drinking the four cups and eating matzah, korech, and afikoman — not while eating maror or chazeret (they symbolize slavery). Ashkenaz women often do not recline; many Sephardi women do — follow your minhag.
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
                    "• Yom Tov davening with Half Hallel and Musaf; no chametz (and no kitniyot if that is your custom)."
                EffectiveNusach.SEFARD, EffectiveNusach.EDOT_HAMIZRACH ->
                    "• Yom Tov davening with Half Hallel and Musaf; no chametz."
                EffectiveNusach.OTHER ->
                    "• Yom Tov davening with Half Hallel and Musaf; no chametz. Kitniyot customs vary — follow your minhag."
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
• Light Yom Tov candles before sunset (new match OK) — see the timing note above. After sunset you cannot create a new flame.
• Delay Maariv and Kiddush until after nightfall (tzeit) so the full 49 days of the Omer are complete before the festive meal.
• If tonight is Motzei Shabbat into Shavuot: do not strike a new match — light candles only from a pre-existing flame after Shabbat ends, then Kiddush with Yaknehaz.
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
• ${lulavErevLine(profile, tomorrowCal)}
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
• Liturgical shift: During Musaf tomorrow (on Shemini Atzeret), communities begin the winter rain liturgy in the second blessing of the Amidah — Ashkenaz often inserts "Mashiv HaRuach U'Morid HaGeshem"; many Sephardim say "Morid HaGeshem" (exact wording follows your siddur). Tefillat Geshem (the formal prayer for rain) is recited in that Musaf; Full Hallel.
""" else """
• Shemini Atzeret in the Diaspora: Yizkor is often recited; still a full Yom Tov with no melacha. (Simchat Torah is tomorrow in the Diaspora.)
• Sukkah in the Diaspora: Due to safek dyoma (halachic doubt which day is which), Diaspora Ashkenazim and Chabad eat major meals in the sukkah on Shemini Atzeret but omit leishev basukkah. Sephardim generally eat indoors (no sukkah) on Shemini Atzeret — follow your rav.
• Liturgical shift: During Musaf tomorrow (on Shemini Atzeret), communities begin the winter rain liturgy in the second blessing of the Amidah — Ashkenaz often inserts "Mashiv HaRuach U'Morid HaGeshem"; many Sephardim say "Morid HaGeshem" (exact wording follows your siddur). Tefillat Geshem is recited in that Musaf.
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
