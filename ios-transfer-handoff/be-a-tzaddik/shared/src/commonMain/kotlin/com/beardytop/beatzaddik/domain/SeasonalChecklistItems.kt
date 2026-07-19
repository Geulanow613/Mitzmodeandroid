package com.beardytop.beatzaddik.domain

import com.beardytop.beatzaddik.data.ChecklistLoader
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.plus

object SeasonalChecklistItems {

    data class Explainer(
        val id: String,
        val title: String,
        val body: String,
    )

    /**
     * Israeli civil holidays are not treated as mitzvot in the checklist, but we still want their
     * full long explainers available (e.g. from the Upcoming & seasonal header).
     */
    fun israeliCivilHolidayExplainerForName(name: String): Explainer? {
        // ONLY these 4 Israeli civil holidays. We just normalize apostrophes so the
        // same holiday name matches across fonts / OSes.
        val normalized = name
            .replace('\u2019', '\'') // right single quote
            .replace('\u02BC', '\'') // modifier letter apostrophe
            .trim()
        return when (normalized) {
            "Yom HaShoah" -> Explainer(
                id = "yom_hashoah",
                title = "Yom HaShoah — Holocaust Remembrance Day",
                body = YOM_HASHOAH_LONG_EXPLAINER,
            )
            "Yom HaZikaron" -> Explainer(
                id = "yom_hazikaron",
                title = "Yom HaZikaron — Fallen Soldiers Memorial Day",
                body = YOM_HAZIKARON_LONG_EXPLAINER,
            )
            "Yom Ha'atzmaut", "Yom HaAtzmaut" -> Explainer(
                id = "yom_haatzmaut",
                title = "Yom Ha'atzmaut — Israeli Independence Day",
                body = YOM_HAATZMAUT_LONG_EXPLAINER,
            )
            "Yom Yerushalayim" -> Explainer(
                id = "yom_yerushalayim",
                title = "Yom Yerushalayim — Jerusalem Day",
                body = YOM_YERUSHALAYIM_LONG_EXPLAINER,
            )
            else -> null
        }
    }

    fun israeliCivilHolidayExplainerForAnchor(anchor: String): Explainer? = when (anchor) {
        "yom_hashoah" -> Explainer(
            id = "yom_hashoah",
            title = "Yom HaShoah — Holocaust Remembrance Day",
            body = YOM_HASHOAH_LONG_EXPLAINER,
        )
        "yom_hazikaron" -> Explainer(
            id = "yom_hazikaron",
            title = "Yom HaZikaron — Fallen Soldiers Memorial Day",
            body = YOM_HAZIKARON_LONG_EXPLAINER,
        )
        "yom_haatzmaut" -> Explainer(
            id = "yom_haatzmaut",
            title = "Yom Ha'atzmaut — Israeli Independence Day",
            body = YOM_HAATZMAUT_LONG_EXPLAINER,
        )
        "yom_yerushalayim" -> Explainer(
            id = "yom_yerushalayim",
            title = "Yom Yerushalayim — Jerusalem Day",
            body = YOM_YERUSHALAYIM_LONG_EXPLAINER,
        )
        else -> null
    }

    private val YOM_HASHOAH_LONG_EXPLAINER = """Yom HaShoah V'HaGevurah (27 Nisan) is the national day of remembrance for the six million Jews murdered in the Holocaust. It was established by the Israeli Knesset in 1953.

Date adjustment: If 27 Nisan falls on Friday, the day is observed on Thursday (26 Nisan). If it falls on Sunday, it is observed on Monday (28 Nisan), to avoid disruption of Shabbat.

Customs by community:

In Israel: Two-minute siren sounds at 10:00 AM; most Israelis stop and stand in silence. Memorial ceremonies are held at Yad Vashem and throughout the country.

Prayers: Standard weekday davening — Yom HaShoah does not add or remove any siddur insertions. It is a Knesset civil memorial, not a rabbinically instituted liturgical day. Some communities hold memorial learning or ceremonies.

Charedi communities: Many do not observe this date as a religious memorial, preferring 10 Tevet (designated by the Chief Rabbinate in 1949 as Yom Kaddish HaKlali for those whose date of death is unknown) or Tisha B'Av as the appropriate day of mourning for all Jewish tragedies. This is a matter of minhag and communal leadership.

Chabad: No official communal observance is instituted, though the memory of the kedoshim (holy martyrs) is honored."""

    private val YOM_HAZIKARON_LONG_EXPLAINER = """Yom HaZikaron (usually 4 Iyar) is Israel's national day of remembrance for soldiers of the Israel Defense Forces and victims of terrorism who gave their lives for the State of Israel. It was established by the Knesset in 1963 and is observed the day before Yom Ha'atzmaut. When the calendar would clash with Shabbat, both Yom HaZikaron and Yom Ha'atzmaut are postponed.

In Israel: Memorial sirens sound at 8:00 PM (start of the day, at nightfall) and again at 11:00 AM the following morning. Ceremonies are held at military cemeteries across the country. Flags fly at half-mast.

Prayers: Standard weekday davening for most communities. Some Religious Zionist shuls omit Tachanun at Mincha before the transition into Yom Ha'atzmaut; many Charedi and Chabad communities treat the day as an ordinary weekday throughout.

The day ends at nightfall with the transition into Yom Ha'atzmaut celebrations."""

    private val YOM_HAATZMAUT_LONG_EXPLAINER = """Yom Ha'atzmaut (usually 5 Iyar) commemorates Israeli independence in 1948. When 5 Iyar would conflict with Shabbat, Israel postpones Yom Ha'atzmaut (and Yom HaZikaron moves with it).

Customs vary significantly by community:

Religious Zionist / Modern Orthodox: Some communities recite Hallel (instituted by the Chief Rabbinate of Israel) and omit Tachanun; many others treat the day as a regular weekday. Whether Hallel is said with a bracha is disputed — the Chief Rabbinate instructed with a bracha; many Ashkenazi poskim outside Israel say without a bracha. Some communities add special festive prayers (Hallel u'Maariv).

Sephardic (Rav Ovadia Yosef / Yalkut Yosef): Rav Ovadia Yosef ruled that Hallel should not be recited (concern of bracha levatala since the day was not established by Chazal). Tachanun omission is also disputed in these communities.

Chabad: The Rebbe did not institute any special observance. Most Chabad communities do not say Hallel and recite Tachanun as usual.

Charedi communities (Agudah, Litvish): Generally do not observe the day as a religious holiday. Tachanun is said as usual.

The Omer continues to be counted normally.

Ask your rav which custom your community follows."""

    private val YOM_YERUSHALAYIM_LONG_EXPLAINER = """Yom Yerushalayim (28 Iyar) marks the reunification of Jerusalem during the Six-Day War in 1967 (5727).

Customs vary by community:

Religious Zionist / Dati Leumi: Some communities recite Hallel (with or without a bracha, depending on the posek and community) and omit Tachanun; many others treat the day as a regular weekday. Some communities recite special tefillot.

Sephardic (Rav Ovadia Yosef): Hallel is not recited for the same reason as Yom Ha'atzmaut — not established by Chazal. Practice varies.

Chabad and Charedi communities: Generally no special observance. Tachanun is said as usual.

Yom Yerushalayim is observed by fewer communities than Yom Ha'atzmaut, and there is no universally accepted halachic obligation. Ask your rav about your community's custom."""

    fun forDay(
        cal: DayInfo,
        profile: UserProfile,
        tomorrowCal: DayInfo,
        dayAfterTomorrowCal: DayInfo,
        nowMillis: Long,
    ): List<ChecklistItemDef> = buildList {
        if (cal.isSefiratHaomer && cal.omerDay != null && cal.omerDay in 1..49) {
            add(omerItem(cal, profile))
            if (SefirahMourningRules.isMourningDay(cal, profile.effectiveNusach(), nowMillis)) {
                add(sefirahMourningMusicItem(profile))
            }
        }
        if (cal.isChanukah && cal.chanukahDay != null) {
            add(chanukahItem(cal.chanukahDay, profile))
        }
        if ("purim_meshulash_friday" in cal.activeSeasons) {
            addAll(purimMeshulashFridayItems(profile))
        } else if ("purim_meshulash_shabbat" in cal.activeSeasons) {
            addAll(purimMeshulashShabbatItems(profile))
        } else if ("purim_meshulash_sunday" in cal.activeSeasons) {
            addAll(purimMeshulashSundayItems(profile))
        } else if (cal.isPurim) {
            addAll(purimItems(profile))
        }
        if (shouldShowZecherMachatzitHaShekel(cal)) {
            add(zecherMachatzitHaShekelItem(cal, profile))
        }
        if ("erev_chag" in cal.activeSeasons && !HolyDayPhoneRules.isShabbatMelachaDay(cal)) {
            add(erevChagPrepItem(cal, profile, tomorrowCal))
            // Timed hadlakat nerot for weekday erev YT (Friday uses Shabbat candles).
            // Motzei Shabbat→YT lighting is after tzeit when the checklist is already hidden —
            // that case stays in Friday advance / erev-chag prep prose.
            if (!cal.isErevShabbat) {
                add(yomTovCandlesItem(cal, profile, tomorrowCal))
            }
        }
        if (YomTovShabbatPrepText.shouldShowAdvancePrepDay(cal, tomorrowCal, profile)) {
            add(yomTovShabbatAdvancePrepItem(cal, tomorrowCal, profile))
        }
        if (PurimMeshulashText.shouldShowAdvancePrep(cal, tomorrowCal, dayAfterTomorrowCal)) {
            add(purimMeshulashAdvancePrepItem())
        }
        if ("erev_purim" in cal.activeSeasons) {
            add(erevPurimPrepItem(cal, tomorrowCal))
        }
        if (PurimMeshulashText.isErevBeforeMeshulashFriday(cal, tomorrowCal)) {
            add(purimMeshulashErevMegillahItem(profile))
        }
        if ("erev_chanukah" in cal.activeSeasons) {
            add(erevChanukahPrepItem(profile))
        }
        if (cal.isRoshChodesh) {
            add(roshChodeshMonthlyItem(profile))
            addAll(yaalehVyavoRoshChodeshItems(profile))
            when (RoshChodeshRules.hallelKind(cal)) {
                RoshChodeshRules.HallelKind.HALF -> add(roshChodeshHalfHallelItem(profile))
                RoshChodeshRules.HallelKind.FULL_DURING_CHANUKAH ->
                    add(roshChodeshFullHallelDuringChanukahItem(profile))
                RoshChodeshRules.HallelKind.NONE -> Unit
            }
        }
        if (isKiddushLevanaWindow(cal, profile, nowMillis) && profile.gender.usesMaleChecklistItems()) {
            add(kiddushLevanaItem(profile))
        }
        if (isTuBshvat(cal)) {
            add(tuBshvatSederItem(profile))
        }
        // Israeli civil holidays (Yom HaShoah / Yom HaZikaron / Yom Ha'atzmaut / Yom Yerushalayim)
        // are intentionally not listed as "mitzvot" in the daily checklist. They still appear in the
        // "Upcoming & seasonal" header block, and their full explainers remain accessible there.
        if (ErevPesachPrepText.isPesachPrepWindow(cal)) {
            addAll(ErevPesachPrepText.pesachPrepItemsForDay(cal, profile))
        }
        if (EruvTavshilinRules.requiresEruvTavshilin(cal, profile, tomorrowCal)) {
            add(eruvTavshilinItem(cal, profile, tomorrowCal))
        }
        festivalWeekPrepItem(cal, profile)?.let { add(it) }
        if ("chol_hamoed_pesach" in cal.activeSeasons || "chol_hamoed_sukkot" in cal.activeSeasons) {
            addAll(cholHamoedItems(cal, profile))
        }
        if ("sukkot" in cal.activeSeasons && "chol_hamoed_sukkot" !in cal.activeSeasons) {
            add(arbaMinimItem(profile))
        }
        if ("shemini_atzeret" in cal.activeSeasons) {
            add(sheminiAtzeretItem(profile))
        }
        if ("simchat_torah" in cal.activeSeasons && !profile.isInIsrael) {
            add(simchatTorahItem(profile))
        }
        if (isNightAfterYomKippur(cal)) {
            add(buildSukkahAfterYomKippurItem(profile))
        }
        if (MourningPeriodRules.isInNineDaysPeriod(cal, nowMillis)) {
            add(nineDaysMourningItem(profile))
        }
        if (MourningPeriodRules.isInThreeWeeksPeriod(cal, nowMillis)) {
            add(threeWeeksMourningItem(profile))
        }
        selichotItemForDay(cal, profile)?.let { add(it) }
        ldovidItemForDay(cal, profile)?.let { add(it) }
        birkatHachamahItemForDay(cal)?.let { add(it) }
        birkatHaIlanotItemForDay(cal, profile)?.let { add(it) }
        if ("erev_minor_fast" in cal.activeSeasons && cal.upcomingFastDayIndex != null) {
            add(erevMinorFastPrepItem(cal, profile))
        }
        if ("erev_yom_kippur" in cal.activeSeasons) {
            add(erevYomKippurEatItem(cal, profile))
        }
        val deferredTbaFriday = PublicFastDayRules.isFridayBeforeDeferredTishaBeav(
            cal, tomorrowCal, dayAfterTomorrowCal,
        )
        if ("erev_tisha_beav" in cal.activeSeasons || deferredTbaFriday) {
            add(erevTishaBeavPrepItem(cal, profile, deferredToSunday = deferredTbaFriday))
        }
        if ("fast_day" in cal.activeSeasons && cal.fastDayIndex != null) {
            add(publicFastDayItem(cal, profile))
            if (cal.fastDayIndex == HebrewCalendarEngine.TISHA_BEAV &&
                TishaBeavTefillinRules.omitsMorningTefillin(profile.effectiveNusach())
            ) {
                add(tefillinTishaBeavMinchaItem())
            }
        }
        // After tzeit, Hebrew day is 11 Tishrei (fast_day is off) — still show the break-fast meal.
        if (isNightAfterYomKippur(cal) ||
            (cal.fastDayIndex == HebrewCalendarEngine.YOM_KIPPUR && "fast_day" in cal.activeSeasons)
        ) {
            add(motzeiYomKippurMealItem(cal, profile))
        }
    }

    private fun omerItem(cal: DayInfo, profile: UserProfile): ChecklistItemDef {
        return ChecklistItemDef(
            id = OmerCountText.CHECKLIST_ITEM_ID,
            title = OmerCountText.buildTitle(cal, profile.effectiveNusach()),
            section = "Sefirat HaOmer",
            timeOfDay = TimeOfDay.ANY,
            required = true,
            situational = false,
            tzeitMitzvah = true,
            sortOrder = -100,
            seasons = listOf("sefirah"),
            explanation = OmerCountText.explanationTemplate(),
            links = omerLinks(profile)
        )
    }

    private fun sefirahMourningMusicItem(profile: UserProfile): ChecklistItemDef =
        ChecklistItemDef(
            id = "sefirah_mourning_music",
            title = "Sefirah: mourning customs (music, weddings, haircuts)",
            section = "Mourning customs",
            timeOfDay = TimeOfDay.DAY,
            required = false,
            situational = false,
            seasons = listOf("sefirah"),
            explanation = BeginnerHalachaGlossary.withKeyTerms(
                BeginnerHalachaGlossary.mourningBasics(),
                """During Sefirat HaOmer we keep customs of mourning (aveilut) because Rabbi Akiva's 24,000 students died in a plague between Pesach and Shavuot (Talmud, Yevamot 62b).

The exact mourning window varies drastically by nusach — start date, end date, and whether Lag BaOmer (day 33) ends the period or only suspends it for one day.

Checklist visibility follows your selected nusach (see below). A personal rav is final.

Common practices during your window: no live music (recordings/a cappella rules vary), no weddings, no haircuts.""",
            ),
            explanationAshkenaz = """Ashkenaz — two widespread customs:

1) First 33 days (most common): mourning on Omer days 1–32. On Lag BaOmer (day 33) the period permanently ends in the morning (miktzat hayom k'kulo — a small part of the day counts as the whole). Days 34–49 have no mourning restrictions.

2) Later 33 / Rema: mourning from 1 Iyar (about day 16) through Erev Shavuot. Lag BaOmer is only a one-day break (weddings, music, haircuts permitted that day); mourning returns on day 34 until about 3 Sivan / Erev Shavuot.

This checklist follows the first-33 window (shows days 1–32). If your family keeps the Rema later custom, resume restrictions after Lag until Erev Shavuot even when this item is hidden — ask your rav.""",
            explanationSefard = """Sephardi / Shulchan Arukh: mourning on Omer days 1–33 inclusive for weddings and haircuts — Lag BaOmer itself remains restricted for those.

Music / joy exception: many contemporary Sephardic poskim permit listening to music and dancing on Lag BaOmer itself out of respect for the Hillula of Rabbi Shimon bar Yochai. As soon as Lag ends and the night of day 34 begins, mourning restrictions snap back until the morning of day 34 (miktzat hayom), when the period permanently ends.

Follow your kehilla (Rav Ovadia Yosef, Yechaveh Daat 3:31; Peninei Halakha).""",
            explanationEdotHamizrach = """Edot HaMizrach often follow Shulchan Arukh: mourning days 1–33 inclusive (Lag still restricted for weddings/haircuts). Many permit music/dancing on Lag for the Hillula of R. Shimon bar Yochai; after Lag ends, night of day 34 snaps restrictions back until morning of day 34, when mourning ends permanently.

Some kehillot follow the Ari through Erev Shavuot (with Lag as a one-day break), and some North African communities end on Lag — ask your rav.""",
            explanationChabad = """Chabad / Arizal: mourning restraint through the Omer (days 1–49) until Erev Shavuot. Lag BaOmer (day 33) is a one-day suspension — celebration and music are permitted; mourning returns on day 34.

Some families ease wedding restrictions during Shloshet Yemei Hagbalah (about days 46–48). Adult haircut practice can be stricter than the general Lag break (upsherin is a common exception) — ask your Chabad rav.""",
            links = sefirahMourningLinks(profile)
        )

    private fun chanukahItem(day: Int, profile: UserProfile): ChecklistItemDef =
        ChecklistItemDef(
            id = "chanukah_lighting_day_$day",
            title = "Light Chanukah candles — Night $day",
            section = "Chanukah",
            timeOfDay = TimeOfDay.NIGHT,
            required = true,
            seasons = listOf("chanukah"),
            explanation = SeasonalMitzvahText.chanukahLightingExplanationTemplate(),
            links = SeasonalMitzvahText.chanukahDayLinks(profile)
        )

    /** Fast of Esther (Mincha), Purim morning, or Purim Meshulash Friday (Megillah day). */
    fun shouldShowZecherMachatzitHaShekel(cal: DayInfo): Boolean =
        cal.fastDayIndex == HebrewCalendarEngine.FAST_OF_ESTHER ||
            cal.isPurim ||
            "purim_meshulash_friday" in cal.activeSeasons

    private fun zecherMachatzitHaShekelItem(cal: DayInfo, profile: UserProfile): ChecklistItemDef {
        val onFast = cal.fastDayIndex == HebrewCalendarEngine.FAST_OF_ESTHER
        // Year-keyed id + persistChecked: check on Fast of Esther stays checked on Purim;
        // next Hebrew year gets a fresh unchecked item.
        val year = cal.hebrewYear ?: 0
        return ChecklistItemDef(
            id = "zecher_machatzit_hashekel_$year",
            title = if (onFast) {
                "Zecher LeMachatzit HaShekel (custom)"
            } else {
                "Zecher LeMachatzit HaShekel (custom) — if not given already on Fast of Esther"
            },
            section = "Seasonal",
            sortOrder = -200,
            timeOfDay = TimeOfDay.DAY,
            required = false,
            situational = false,
            persistChecked = true,
            seasons = buildList {
                if (onFast) add("fast_day")
                if (cal.isPurim) add("purim")
                if ("purim_meshulash_friday" in cal.activeSeasons) add("purim_meshulash_friday")
            }.takeIf { it.isNotEmpty() },
            explanation = SeasonalMitzvahText.zecherMachatzitHaShekelExplanation(),
            links = SeasonalMitzvahText.zecherMachatzitHaShekelLinks(profile),
        )
    }

    private fun purimItems(profile: UserProfile): List<ChecklistItemDef> = listOf(
        ChecklistItemDef(
            id = "purim_megillah",
            title = "Hear the Megillah (Purim)",
            section = "Purim",
            timeOfDay = TimeOfDay.ANY,
            required = true,
            situational = false,
            seasons = listOf("purim"),
            explanation = BeginnerHalachaGlossary.withKeyTerms(
                BeginnerHalachaGlossary.purimBasics(),
                """Mikra Megillah (hearing the Book of Esther) is a rabbinic mitzvah instituted for Purim (Esther 9:28; Megillah 19a). Men and women are equally obligated.

When to hear it:
• Once on Purim evening — after nightfall (tzeit)
• Once on Purim day — you are halachically obligated to hear the Megillah during the daytime (usually right after morning Shacharit and before sunset). The daytime reading is the primary fulfillment of the mitzvah (mitzvat ha'yom); the night reading was instituted later — both are required.

How to fulfill:
• Hear every word read from a kosher megillah scroll by someone who can discharge your obligation (Shulchan Arukh O.C. 690)
• Stand for the blessings; customs when Haman's name is read vary by shul (many make noise; sitting vs standing — follow your community)
• Listen without talking — missed words may require hearing that passage again; ask your rabbi if unsure

Blessings before reading:
• Al mikra megillah
• She'asa nissim
• Shehecheyanu (recited on the first night; Ashkenazim recite it by day as well)

Confirm local Megillah reading times with your shul. Zecher LeMachatzit HaShekel (the half-shekel remembrance custom) appears as its own checklist item on the Fast of Esther and Purim.

Prayers & meals:
• Insert Al HaNissim into every Amidah and into Birkat Hamazon (bentching) all day long on Purim.""",
            ),
            explanationAshkenaz = PurimBrachotText.SHEHECHEYANU_ASHKENAZ,
            explanationSefard = PurimBrachotText.SHEHECHEYANU_SEPHARDIC,
            explanationEdotHamizrach = PurimBrachotText.SHEHECHEYANU_SEPHARDIC,
            explanationChabad = PurimBrachotText.SHEHECHEYANU_ASHKENAZ,
            links = purimMegillahLinks(profile)
        ),
        ChecklistItemDef(
            id = "purim_matanot_laevyonim",
            title = "Matanot la'evyonim — gifts to the poor",
            section = "Purim",
            timeOfDay = TimeOfDay.DAY,
            required = true,
            situational = false,
            seasons = listOf("purim"),
            explanation = BeginnerHalachaGlossary.withKeyTerms(
                BeginnerHalachaGlossary.purimBasics(),
                """Matanot la'evyonim (מתנות לאביונים) helps every Jew celebrate Purim with food and joy (Esther 9:22).

The mitzvah (Peninei Halakha 05-16-03):
• Give at least one gift to each of two different poor people (minimum of two recipients total) during Purim daytime.
• Each gift should enable a modest Purim meal — money is common.

How to do it:
• Give during Purim daytime only (not at night); many give after the daytime Megillah reading (follow your minhag).
• Ideally before your Purim seudah so recipients can use it for the meal.
• You may give through a trustworthy messenger, gemach, or organization that distributes on Purim day — verify funds reach the poor that day
• If you cannot find recipients, ask your rabbi or shul — many collect on Purim morning

Who qualifies: Someone who lacks resources for Purim — your rav can guide you if unsure.""",
            ),
            links = purimMatanotLinks(profile)
        ),
        ChecklistItemDef(
            id = "purim_mishloach_manot",
            title = "Mishloach manot — food gifts to friends",
            section = "Purim",
            timeOfDay = TimeOfDay.DAY,
            required = true,
            situational = false,
            seasons = listOf("purim"),
            explanation = BeginnerHalachaGlossary.withKeyTerms(
                BeginnerHalachaGlossary.purimBasics(),
                """Mishloach manot (משלוח מנות) — sending portions of food — increases friendship and joy on Purim (Esther 9:19).

The mitzvah (Rambam, Shulchan Arukh 695:4; Aish; Peninei Halakha 05-16-04):
• Send at least two different ready-to-eat foods or drinks to one friend on Purim day — one mishloach manot package.
• Women are equally obligated; many send to a woman friend and men to a man (Rema). Sending to additional friends is praiseworthy.
• Examples: wine and cookies, fruit and pastry — clearly two types, not one combined dish.

How to do it:
• Deliver on Purim day before sunset — by you or a messenger (shul lists, kids, or neighbors are fine)
• Food should be ready to eat without cooking
• Label sender and recipient; mishloach manot should be identifiable

Tips: You need not reciprocate every package you receive the same day. Plan ahead on Erev Purim so deliveries are not rushed at the last minute.""",
            ),
            links = purimMishloachLinks(profile)
        ),
        ChecklistItemDef(
            id = "purim_seudah",
            title = "Purim seudah — festive afternoon meal",
            section = "Purim",
            timeOfDay = TimeOfDay.DAY,
            required = true,
            situational = false,
            seasons = listOf("purim"),
            explanation = BeginnerHalachaGlossary.withKeyTerms(
                BeginnerHalachaGlossary.purimBasics(),
                """The Purim seudah (סעודת פורים) is one of the four Purim mitzvot (Esther 9:22; Peninei Halakha 05-16-02).

When:
• During Purim day — before sunset (many hold the meal in the afternoon after mitzvot are underway).

How:
• A festive meal with bread (hamotzi — many use two rolls), meat, wine, and joy.
• Include words of Torah or thanks to Hashem — the meal is a mitzvah, not only a party.
• Ad d’lo yada (“until he does not know”) is the Purim drinking custom: drink wine toward joyful inebriation until one cannot distinguish “cursed is Haman” from “blessed is Mordechai.” Rema’s common practice is to drink a bit more than usual until drowsy, then sleep; some require actual intoxication; Rambam also favors drink-then-sleep. Do not drink so much that you miss Megillah or tefillah. Exempt if drinking would make you sick, depressed, or lead to sin. Women join the seudah; most hold they should not drink to inebriation.

Plan the menu and timing so matanot la'evyonim and mishloach manot are handled earlier in the day when possible.""",
            ),
            links = purimMegillahLinks(profile)
        )
    )

    private fun yomTovShabbatAdvancePrepItem(cal: DayInfo, tomorrowCal: DayInfo, profile: UserProfile): ChecklistItemDef {
        val tomorrowName = tomorrowCal.upcomingChagName ?: "Yom Tov"
        val explanation = YomTovShabbatPrepText.advanceBlock(cal, tomorrowCal, profile).orEmpty()
        val simchasLinks = if (HebrewCalendarEngine.isShaloshRegalim(tomorrowCal.upcomingChagYomTovIndex)) {
            SeasonalMitzvahText.simchasYomTovPrepLinks()
        } else {
            emptyList()
        }
        val links = (YomTovShabbatPrepText.links(tomorrowCal, profile, tomorrowName) + simchasLinks)
            .distinctBy { it.url }
        val title = if (YomTovShabbatPrepText.isFridayBeforeShabbatErevChag(cal, tomorrowCal)) {
            "Tomorrow: Shabbat & erev $tomorrowName — read before Shabbat"
        } else {
            "Tomorrow: $tomorrowName & Shabbat — prepare today"
        }
        return ChecklistItemDef(
            id = "yom_tov_shabbat_advance_prep",
            title = title,
            section = "Prepare for the festival",
            timeOfDay = TimeOfDay.DAY,
            required = false,
            situational = false,
            sortOrder = 5,
            explanation = explanation,
            links = links
        )
    }

    private fun eruvTavshilinItem(cal: DayInfo, profile: UserProfile, tomorrowCal: DayInfo): ChecklistItemDef {
        val chagName = EruvTavshilinRules.chagName(cal)
        return ChecklistItemDef(
            id = "eruv_tavshilin",
            title = "Eruv tavshilin — cook on Yom Tov for Shabbat",
            section = EruvTavshilinRules.checklistSection(cal),
            sortOrder = 25,
            timeOfDay = TimeOfDay.DAY,
            required = true,
            situational = false,
            seasons = listOfNotNull(
                "erev_chag".takeIf { it in cal.activeSeasons },
                "erev_pesach".takeIf { it in cal.activeSeasons },
            ),
            explanation = YomTovShabbatPrepText.eruvTavshilinExplanation(cal, profile, tomorrowCal),
            links = YomTovShabbatPrepText.links(cal, profile, chagName),
        )
    }

    private fun yomTovCandlesItem(
        cal: DayInfo,
        profile: UserProfile,
        tomorrowCal: DayInfo,
    ): ChecklistItemDef {
        val name = cal.upcomingChagName
            ?: tomorrowCal.yomTovHolidayName
            ?: "Yom Tov"
        val lead = CandleLightingRules.leadMinutesBeforeSunset(profile)
        return ChecklistItemDef(
            id = "yom_tov_candles",
            title = "Light $name candles",
            section = "Prepare for the festival",
            sortOrder = 15,
            timeOfDay = TimeOfDay.DAY,
            required = true,
            situational = false,
            seasons = listOf("erev_chag"),
            explanation = BeginnerHalachaGlossary.withKeyTerms(
                BeginnerHalachaGlossary.erevChagCommon(),
                """Light Yom Tov candles before sunset (about $lead minutes before — Jerusalem custom is often 40). On a weekday first night you may strike a new match.

Once Yom Tov has begun, light only from a pre-existing flame (second night of Yom Tov, including Rosh Hashana night 2 in Israel; Motzei Shabbat→Yom Tov after tzeit).

Women traditionally light; a man lights if no woman is present. Ask your rav for bracha wording (neirot shel Yom Tov / Shehecheyanu when applicable).""",
            ),
            links = listOf(
                ChecklistLink(
                    "Chabad — Yom Tov candle lighting",
                    "https://www.chabad.org/library/article_cdo/aid/4689/jewish/Candle-Lighting.htm",
                    if (profile.effectiveNusach() == EffectiveNusach.CHABAD) "chabad" else "default",
                ),
            ),
        )
    }

    private fun erevChagPrepItem(cal: DayInfo, profile: UserProfile, tomorrowCal: DayInfo): ChecklistItemDef {
        val prep = ErevChagPrepText.build(cal, profile, tomorrowCal)
        val isErevPesachCombinedPrep =
            "erev_pesach" in cal.activeSeasons && cal.upcomingChagYomTovIndex == HebrewCalendarEngine.PESACH
        return ChecklistItemDef(
            id = "erev_chag_prep",
            title = prep.title,
            section = if (isErevPesachCombinedPrep) "Pesach prep" else "Prepare for the festival",
            timeOfDay = TimeOfDay.DAY,
            required = false,
            situational = false,
            sortOrder = if (isErevPesachCombinedPrep) 30 else 0,
            seasons = listOfNotNull(
                "erev_chag".takeIf { "erev_chag" in cal.activeSeasons },
                "erev_pesach".takeIf { isErevPesachCombinedPrep },
            ),
            explanation = prep.explanation,
            links = prep.links
        )
    }

    private fun erevPurimPrepItem(cal: DayInfo, tomorrowCal: DayInfo) = ChecklistItemDef(
        id = "erev_purim_prep",
        title = when {
            PurimMeshulashText.isErevBeforeMeshulashFriday(cal, tomorrowCal) ->
                "Purim Meshulash — read full plan before Shabbat"
            tomorrowCal.hebrewDay == 15 ->
                "Erev Shushan Purim prep — arrange Megillah and mitzvot"
            else -> "Erev Purim prep — arrange Megillah and mitzvot"
        },
        section = "Purim",
        timeOfDay = TimeOfDay.DAY,
        required = false,
        situational = false,
        seasons = listOf("erev_purim"),
        explanation = when {
            PurimMeshulashText.isErevBeforeMeshulashFriday(cal, tomorrowCal) ->
                PurimMeshulashText.erevPrepExplanation()
            tomorrowCal.hebrewDay == 15 ->
                """Shushan Purim is tomorrow (15 Adar). Plan all four mitzvot: Megillah (night and morning), matanot la'evyonim, mishloach manot, and tomorrow's festive seudah (afternoon meal). Confirm reading times with your shul."""
            else ->
                """Purim is tomorrow. Plan all four mitzvot: Megillah (night and morning), matanot la'evyonim, mishloach manot, and tomorrow's festive seudah (afternoon meal). Confirm reading times with your shul."""
        },
        links = purimPrepLinks(),
    )

    private fun purimMeshulashAdvancePrepItem() = ChecklistItemDef(
        id = "purim_meshulash_advance_prep",
        title = "Purim Meshulash — read full plan before Shabbat",
        section = "Purim",
        timeOfDay = TimeOfDay.DAY,
        required = false,
        situational = false,
        seasons = emptyList(),
        explanation = PurimMeshulashText.advancePrepExplanation(),
        links = purimPrepLinks(),
    )

    private fun purimPrepLinks() = listOf(
        ChecklistLink("Chabad — Purim checklist", "https://www.chabad.org/holidays/purim/default_cdo/jewish/Purim.htm", "chabad"),
        ChecklistLink("Peninei Halacha — Purim", "https://ph.yhb.org.il/en/category/zemanim/05-15/", "default"),
        ChecklistLink("Ohr Somayach — Purim", "https://ohr.edu/1508", "default"),
    )

    private fun erevChanukahPrepItem(profile: UserProfile) = ChecklistItemDef(
        id = "erev_chanukah_prep",
        title = "Erev Chanukah prep — set up menorah, candles, etc.",
        section = "Chanukah",
        timeOfDay = TimeOfDay.DAY,
        required = false,
        situational = false,
        seasons = listOf("erev_chanukah"),
        explanation = """
Chanukah starts tomorrow night. Set up now so lighting is calm and on time.

Before the first night:
• Place the menorah where it will be safe and visible (common: by a window/doorway for pirsumei nisa; avoid drafts and fire hazards).
• Make sure you have enough candles/oil for all 8 nights (and a shamash each night).
• Review the lighting order and what to say:
  - First night: three brachot (lehadlik, she'asa nissim, Shehecheyanu).
  - Other nights: two brachot (no Shehecheyanu unless first time lighting this year).

Timing notes:
• Ideally light after tzeit (nightfall).
• Friday: light Chanukah before Shabbat candles; use enough oil/large enough candles to last 30 minutes after nightfall.
• Motzei Shabbat: light before or after Havdalah per minhag.

Quick reference: https://ohr.edu/1304
        """.trimIndent(),
        links = erevChanukahPrepLinks()
    )

    private fun festivalWeekPrepItem(cal: DayInfo, profile: UserProfile): ChecklistItemDef? {
        val prep = cal.festivalWeekPrep() ?: return null
        return ChecklistItemDef(
            id = "prepare_for_festival_${prep.name.lowercase()}",
            title = SeasonalMitzvahText.festivalWeekPrepTitle(prep),
            section = "Prepare for the festival",
            timeOfDay = TimeOfDay.DAY,
            required = false,
            situational = false,
            sortOrder = 10,
            explanation = SeasonalMitzvahText.festivalWeekPrepExplanation(cal, profile, prep),
            links = SeasonalMitzvahText.festivalWeekPrepLinks(prep, profile)
        )
    }

    private fun cholHamoedItems(cal: DayInfo, profile: UserProfile): List<ChecklistItemDef> {
        val list = mutableListOf<ChecklistItemDef>()
        if ("chol_hamoed_sukkot" in cal.activeSeasons) {
            list += arbaMinimItem(profile).copy(
                section = if (HebrewCalendarEngine.isHoshanaRabbah(cal.hebrewMonth, cal.hebrewDay)) {
                    "Hoshana Rabbah"
                } else {
                    "Chol HaMoed"
                },
                sortOrder = -30,
                seasons = listOf("chol_hamoed_sukkot", "sukkot"),
            )
            if (HebrewCalendarEngine.isHoshanaRabbah(cal.hebrewMonth, cal.hebrewDay)) {
                list += hoshanaRabbahAravotItem(profile)
            }
            list += cholHamoedSukkahItem(profile)
        }
        list += listOf(
            ChecklistItemDef(
                id = "chol_hamoed_wine_reviit",
                title = "Revi'it of wine — daily mitzvah on Chol HaMoed (recommended)",
                section = "Chol HaMoed",
                timeOfDay = TimeOfDay.ANY,
                required = false,
                situational = false,
                sortOrder = -20,
                seasons = listOf("chol_hamoed_pesach", "chol_hamoed_sukkot"),
                explanation = SeasonalMitzvahText.cholHamoedWineReviitExplanation(cal),
                links = cholHamoedWineLinks(cal, profile)
            ),
            ChecklistItemDef(
                id = "chol_hamoed_honor",
                title = "Honor Chol HaMoed",
                section = "Chol HaMoed",
                timeOfDay = TimeOfDay.ANY,
                required = false,
                situational = false,
                seasons = listOf("chol_hamoed_pesach", "chol_hamoed_sukkot"),
                explanation = SeasonalMitzvahText.cholHamoedHonorExplanation(cal, profile),
                links = SeasonalMitzvahText.cholHamoedLinks(cal, profile)
            ),
            ChecklistItemDef(
                id = "chol_hamoed_nicer_clothes",
                title = "Wear Nicer Clothes in Honor of the Moed",
                section = "Chol HaMoed",
                timeOfDay = TimeOfDay.ANY,
                required = false,
                situational = false,
                seasons = listOf("chol_hamoed_pesach", "chol_hamoed_sukkot"),
                explanation = SeasonalMitzvahText.cholHamoedClothesExplanation(),
                links = SeasonalMitzvahText.cholHamoedLinks(cal, profile)
            ),
        )
        if ("chol_hamoed_pesach" in cal.activeSeasons) {
            list += ChecklistItemDef(
                id = "chol_hamoed_pesach_matzah",
                title = "Eat matzah on Pesach (optional on Chol HaMoed)",
                section = "Chol HaMoed",
                timeOfDay = TimeOfDay.ANY,
                required = false,
                situational = false,
                sortOrder = -25,
                seasons = listOf("chol_hamoed_pesach"),
                explanation = SeasonalMitzvahText.cholHamoedMatzahExplanation(),
                links = SeasonalMitzvahText.pesachWeekLinks(profile)
            )
        }
        return list
    }

    private fun cholHamoedSukkahItem(profile: UserProfile): ChecklistItemDef {
        val isFemale = profile.gender == Gender.FEMALE
        return ChecklistItemDef(
            id = "chol_hamoed_sukkah",
            title = if (isFemale) {
                "Eat Meals in the Sukkah (Optional for Women)"
            } else {
                "Eat Bread Meals in the Sukkah"
            },
            section = "Chol HaMoed",
            timeOfDay = TimeOfDay.ANY,
            required = false,
            situational = false,
            sortOrder = -25,
            seasons = listOf("chol_hamoed_sukkot", "sukkot"),
            explanation = SeasonalMitzvahText.cholHamoedSukkahExplanation(profile),
            explanationFemale = SeasonalMitzvahText.cholHamoedSukkahExplanationFemale(profile),
            links = SeasonalMitzvahText.sukkahLinks(profile),
        )
    }

    private fun hoshanaRabbahAravotItem(profile: UserProfile) = ChecklistItemDef(
        id = "hoshana_rabbah_aravot",
        title = "Hoshana Rabbah — beat the aravot (Minhag Nevi'im)",
        section = "Hoshana Rabbah",
        timeOfDay = TimeOfDay.DAY,
        required = false,
        situational = false,
        sortOrder = -35,
        seasons = listOf("chol_hamoed_sukkot", "sukkot"),
        explanation = SeasonalMitzvahText.hoshanaRabbahAravotExplanation(),
        links = SeasonalMitzvahText.hoshanaRabbahLinks(),
    )

    private fun arbaMinimItem(profile: UserProfile): ChecklistItemDef {
        val isFemale = profile.gender == Gender.FEMALE
        return ChecklistItemDef(
            id = "sukkot_arba_minim",
            title = if (isFemale) {
                "Wave Arba Minim — optional for women"
            } else {
                "Wave Arba Minim (lulav, etrog, hadassim, aravot)"
            },
            section = "Seasonal",
            timeOfDay = TimeOfDay.DAY,
            required = false,
            situational = false,
            seasons = listOf("sukkot"),
            explanation = SeasonalMitzvahText.arbaMinimExplanation(profile),
            explanationFemale = SeasonalMitzvahText.arbaMinimExplanationFemale(profile),
            links = SeasonalMitzvahText.arbaMinimLinks(profile)
        )
    }

    private fun sheminiAtzeretItem(profile: UserProfile) = ChecklistItemDef(
        id = "shemini_atzeret_focus",
        title = if (profile.isInIsrael) {
            "Shemini Atzeret & Simchat Torah — Geshem, hakafot, Yom Tov"
        } else {
            "Shemini Atzeret — Geshem, Yizkor, Yom Tov"
        },
        section = "Seasonal",
        timeOfDay = TimeOfDay.DAY,
        required = false,
        situational = false,
        seasons = listOf("shemini_atzeret"),
        explanation = SeasonalMitzvahText.sheminiAtzeretExplanation(profile),
        links = SeasonalMitzvahText.sheminiAtzeretLinks(profile)
    )

    private fun simchatTorahItem(profile: UserProfile) = ChecklistItemDef(
        id = "simchat_torah_focus",
        title = "Simchat Torah — hakafot & Torah joy",
        section = "Seasonal",
        timeOfDay = TimeOfDay.DAY,
        required = false,
        situational = false,
        seasons = listOf("simchat_torah"),
        explanation = SeasonalMitzvahText.simchatTorahExplanation(profile),
        links = SeasonalMitzvahText.simchatTorahLinks(profile)
    )

    private fun buildSukkahAfterYomKippurItem(profile: UserProfile) = ChecklistItemDef(
        id = "motzei_yk_build_sukkah",
        title = "Motzei Yom Kippur: begin building the sukkah (men)",
        section = "Motzei Yom Kippur",
        timeOfDay = TimeOfDay.NIGHT,
        required = false,
        situational = true,
        gender = "male",
        explanation = SeasonalMitzvahText.sukkahBuildExplanation(),
        links = SeasonalMitzvahText.sukkahLinks(profile)
    )

    private fun threeWeeksMourningItem(profile: UserProfile) = ChecklistItemDef(
        id = "three_weeks_mourning_customs",
        title = "Observe Three Weeks mourning customs",
        section = "Mourning customs",
        timeOfDay = TimeOfDay.ANY,
        required = false,
        situational = false,
        explanation = "",
        explanationAshkenaz = SeasonalMitzvahText.threeWeeksAshkenazExplanation(),
        explanationSefard = SeasonalMitzvahText.threeWeeksSephardicExplanation(),
        explanationEdotHamizrach = SeasonalMitzvahText.threeWeeksEdotHamizrachExplanation(),
        explanationChabad = SeasonalMitzvahText.threeWeeksChabadExplanation(),
        links = mourningLinks(profile)
    )

    private fun nineDaysMourningItem(profile: UserProfile) = ChecklistItemDef(
        id = "nine_days_mourning_customs",
        title = "Nine Days: observe stricter mourning customs",
        section = "Mourning customs",
        timeOfDay = TimeOfDay.ANY,
        required = false,
        situational = false,
        explanation = "",
        explanationAshkenaz = SeasonalMitzvahText.nineDaysAshkenazExplanation(),
        explanationSefard = SeasonalMitzvahText.nineDaysSephardicExplanation(),
        explanationEdotHamizrach = SeasonalMitzvahText.nineDaysEdotHamizrachExplanation(),
        explanationChabad = SeasonalMitzvahText.nineDaysChabadExplanation(),
        links = mourningLinks(profile)
    )

    private fun birkatHaIlanotItemForDay(cal: DayInfo, profile: UserProfile): ChecklistItemDef? {
        if (!BirkatHaIlanotRules.isInWindow(cal, profile.latitude)) return null
        return ChecklistItemDef(
            id = "birkat_hailanot",
            title = "Birkat Ha'Ilanot — Blessing on Fruit Trees",
            section = "Seasonal",
            timeOfDay = TimeOfDay.ANY,
            required = false,
            situational = false,
            persistChecked = true,
            hideOnShabbat = false,
            sortOrder = 8,
            explanation = SeasonalMitzvahText.birkatHaIlanotExplanationTemplate(),
            explanationChabad = SeasonalMitzvahText.birkatHaIlanotChabadNote(profile),
            links = SeasonalMitzvahText.birkatHaIlanotLinks(),
        )
    }

    private fun birkatHachamahItemForDay(cal: DayInfo): ChecklistItemDef? {
        val occurrence = BirkatHachamahRules.visibleOccurrence(cal.date) ?: return null
        val isRecitationDay = BirkatHachamahRules.isRecitationDay(cal.date)
        return ChecklistItemDef(
            id = "birkat_hachamah",
            title = "Birkat Hachamah — Blessing the Sun",
            section = "Seasonal",
            timeOfDay = TimeOfDay.DAY,
            required = isRecitationDay,
            situational = !isRecitationDay,
            persistChecked = true,
            sortOrder = 5,
            explanation = SeasonalMitzvahText.birkatHachamahExplanationTemplate(),
            links = listOf(
                ChecklistLink(
                    displayText = "Berakhot 59b (Sefaria)",
                    url = "https://www.sefaria.org/Berakhot.59b",
                ),
                ChecklistLink(
                    displayText = "Peninei Halakha — Blessing the Sun",
                    url = "https://ph.yhb.org.il/en/category/14/14-01/",
                ),
                ChecklistLink(
                    displayText = "Chabad.org — Birkat Hachama",
                    url = "https://www.chabad.org/library/article_cdo/aid/3731/jewish/Birkat-Hachama.htm",
                    nusach = "chabad",
                ),
            ),
        )
    }

    private fun ldovidItemForDay(cal: DayInfo, profile: UserProfile): ChecklistItemDef? {
        val nusach = profile.effectiveNusach()
        if (!LDovidRules.isRecited(cal, nusach)) return null
        return ChecklistItemDef(
            id = "ldovid_hashem_ori",
            title = "Psalm 27 — L'Dovid Hashem Ori",
            section = "Morning Prayer (Shacharit)",
            timeOfDay = TimeOfDay.DAY,
            required = false,
            situational = false,
            sortOrder = 65,
            explanation = SeasonalMitzvahText.ldovidExplanation(nusach),
            explanationAshkenaz = SeasonalMitzvahText.ldovidAshkenazNote(),
            explanationSefard = SeasonalMitzvahText.ldovidSephardNote(),
            explanationEdotHamizrach = SeasonalMitzvahText.ldovidEdotHamizrachNote(),
            explanationChabad = SeasonalMitzvahText.ldovidChabadNote(),
            links = listOf(
                ChecklistLink(
                    displayText = "Psalm 27 (Sefaria)",
                    url = "https://www.sefaria.org/Psalms.27",
                ),
            ),
        )
    }

    private fun selichotItemForDay(cal: DayInfo, profile: UserProfile): ChecklistItemDef? {
        val month = cal.hebrewMonth ?: return null
        val day = cal.hebrewDay ?: return null
        // Selichot are weekday services — not on Shabbat.
        if (HolyDayPhoneRules.isShabbatMelachaDay(cal)) return null
        // Show Selichot:
        // - Sefard/Edot: from Elul 2 through Erev Yom Kippur (9 Tishrei)
        // - Ashkenaz/Chabad: Motzei Shabbat before RH (min. 4 days) through Erev YK
        val inSelichotWindow = when (month) {
            HebrewCalendarEngine.ELUL -> day >= 2
            HebrewCalendarEngine.TISHREI -> day in 2..9 // through Erev Yom Kippur (9 Tishrei)
            else -> false
        }
        if (!inSelichotWindow) return null
        return when (profile.effectiveNusach()) {
            EffectiveNusach.SEFARD, EffectiveNusach.EDOT_HAMIZRACH -> {
                ChecklistItemDef(
                id = if (profile.effectiveNusach() == EffectiveNusach.EDOT_HAMIZRACH) {
                    "selichot_elul_edot_hamizrach"
                } else {
                    "selichot_elul_sefard"
                },
                title = "Say Selichot today",
                section = "Selichot",
                timeOfDay = TimeOfDay.ANY,
                required = false,
                situational = false,
                tzeitMitzvah = true,
                explanation = SeasonalMitzvahText.selichotExplanation(profile.effectiveNusach()),
                links = selichotLinks(profile)
                )
            }
            EffectiveNusach.CHABAD -> {
                if (!isAshkenazStyleSelichotDay(cal)) return null
                ChecklistItemDef(
                    id = "selichot_elul_chabad",
                    title = "Say Selichot today",
                    section = "Selichot",
                    timeOfDay = TimeOfDay.ANY,
                    required = false,
                    situational = false,
                    tzeitMitzvah = true,
                    explanation = SeasonalMitzvahText.selichotExplanation(EffectiveNusach.CHABAD),
                    links = selichotLinks(profile)
                )
            }
            EffectiveNusach.ASHKENAZ -> {
                if (!isAshkenazStyleSelichotDay(cal)) return null
                ChecklistItemDef(
                    id = "selichot_elul_ashkenaz",
                    title = "Say Selichot today",
                    section = "Selichot",
                    timeOfDay = TimeOfDay.ANY,
                    required = false,
                    situational = false,
                    tzeitMitzvah = true,
                    explanation = SeasonalMitzvahText.selichotExplanation(EffectiveNusach.ASHKENAZ),
                    links = selichotLinks(profile)
                )
            }
            EffectiveNusach.OTHER -> ChecklistItemDef(
                id = "selichot_elul_other",
                title = "Say Selichot today (per your custom)",
                section = "Selichot",
                timeOfDay = TimeOfDay.ANY,
                required = false,
                situational = false,
                tzeitMitzvah = true,
                explanation = SeasonalMitzvahText.selichotExplanation(EffectiveNusach.OTHER),
                links = selichotLinks(profile)
            )
        }
    }

    /** Ashkenaz / Chabad: from Motzei of the start Saturday through Erev Yom Kippur. */
    private fun isAshkenazStyleSelichotDay(cal: DayInfo): Boolean {
        val month = cal.hebrewMonth ?: return false
        if (month == HebrewCalendarEngine.TISHREI) return true
        val startSat = ashkenazSelichotStartDate(cal)
        return when {
            cal.date > startSat -> true
            cal.date == startSat -> cal.startedTonightAtTzeit
            else -> false
        }
    }

    private fun ashkenazSelichotStartDate(cal: DayInfo) =
        ((cal.date.plus((30 - (cal.hebrewDay ?: 29)), DateTimeUnit.DAY)).let { rhDate ->
            var candidate = rhDate
            while (candidate.dayOfWeek != DayOfWeek.SATURDAY) {
                candidate = candidate.plus(-1, DateTimeUnit.DAY)
            }
            val diff = rhDate.toEpochDays() - candidate.toEpochDays()
            if (diff < 4) candidate.plus(-7, DateTimeUnit.DAY) else candidate
        })

    fun isNightAfterYomKippur(cal: DayInfo): Boolean {
        val month = cal.hebrewMonth ?: return false
        val day = cal.hebrewDay ?: return false
        return month == HebrewCalendarEngine.TISHREI && day == 11
    }

    private fun isTuBshvat(cal: DayInfo): Boolean {
        val month = cal.hebrewMonth ?: return false
        val day = cal.hebrewDay ?: return false
        return month == HebrewCalendarEngine.SHEVAT && day == 15
    }

    private fun yomHaShoahItem(profile: UserProfile) = ChecklistItemDef(
        id = "yom_hashoah",
        title = "Yom HaShoah — Holocaust Remembrance Day",
        section = "Seasonal",
        timeOfDay = TimeOfDay.ANY,
        required = false,
        situational = false,
        seasons = listOf("yom_hashoah"),
        explanation = """Yom HaShoah V'HaGevurah (27 Nisan) is the national day of remembrance for the six million Jews murdered in the Holocaust. It was established by the Israeli Knesset in 1953.

Date adjustment: If 27 Nisan falls on Friday, the day is observed on Thursday (26 Nisan). If it falls on Sunday, it is observed on Monday (28 Nisan), to avoid disruption of Shabbat.

Customs by community:

In Israel: Two-minute siren sounds at 10:00 AM; most Israelis stop and stand in silence. Memorial ceremonies are held at Yad Vashem and throughout the country.

Prayers: Standard weekday davening — Yom HaShoah does not add or remove any siddur insertions. It is a Knesset civil memorial, not a rabbinically instituted liturgical day. Some communities hold memorial learning or ceremonies.

Charedi communities: Many do not observe this date as a religious memorial, preferring 10 Tevet (designated by the Chief Rabbinate in 1949 as Yom Kaddish HaKlali for those whose date of death is unknown) or Tisha B'Av as the appropriate day of mourning for all Jewish tragedies. This is a matter of minhag and communal leadership.

Chabad: No official communal observance is instituted, though the memory of the kedoshim (holy martyrs) is honored.""",
        links = listOf(
            ChecklistLink(
                "Yad Vashem — Holocaust Martyrs' and Heroes' Remembrance Authority",
                "https://www.yadvashem.org/remembrance/remembrance-day.html",
                "default"
            )
        )
    )

    private fun yomHaZikaronItem(profile: UserProfile) = ChecklistItemDef(
        id = "yom_hazikaron",
        title = "Yom HaZikaron — Fallen Soldiers Memorial Day",
        section = "Seasonal",
        timeOfDay = TimeOfDay.ANY,
        required = false,
        situational = false,
        seasons = listOf("yom_hazikaron"),
        explanation = YOM_HAZIKARON_LONG_EXPLAINER,
        links = listOf(
            ChecklistLink(
                "About Yom HaZikaron",
                "https://www.chabad.org/library/article_cdo/aid/528529/jewish/Yom-Hazikaron.htm",
                "chabad"
            )
        )
    )

    private fun yomHaAtzmautItem(profile: UserProfile) = ChecklistItemDef(
        id = "yom_haatzmaut",
        title = "Yom Ha'atzmaut — Israeli Independence Day",
        section = "Seasonal",
        timeOfDay = TimeOfDay.ANY,
        required = false,
        situational = false,
        seasons = listOf("yom_haatzmaut"),
        explanation = YOM_HAATZMAUT_LONG_EXPLAINER,
        links = listOf(
            ChecklistLink(
                "Chief Rabbinate — Yom Ha'atzmaut siddur",
                "https://www.chabad.org/library/article_cdo/aid/107801/jewish/Yom-HaAtzmaut.htm",
                "chabad"
            )
        )
    )

    private fun yomYerushalayimItem(profile: UserProfile) = ChecklistItemDef(
        id = "yom_yerushalayim",
        title = "Yom Yerushalayim — Jerusalem Day",
        section = "Seasonal",
        timeOfDay = TimeOfDay.ANY,
        required = false,
        situational = false,
        seasons = listOf("yom_yerushalayim"),
        explanation = """Yom Yerushalayim (28 Iyar) marks the reunification of Jerusalem during the Six-Day War in 1967 (5727).

Customs vary by community:

Religious Zionist / Dati Leumi: Some communities recite Hallel (with or without a bracha, depending on the posek and community) and omit Tachanun; many others treat the day as a regular weekday. Some communities recite special tefillot.

Sephardic (Rav Ovadia Yosef): Hallel is not recited for the same reason as Yom Ha'atzmaut — not established by Chazal. Practice varies.

Chabad and Charedi communities: Generally no special observance. Tachanun is said as usual.

Yom Yerushalayim is observed by fewer communities than Yom Ha'atzmaut, and there is no universally accepted halachic obligation. Ask your rav about your community's custom.""",
        links = listOf(
            ChecklistLink(
                "About Yom Yerushalayim",
                "https://www.chabad.org/library/article_cdo/aid/528538/jewish/Yom-Yerushalayim.htm",
                "chabad"
            )
        )
    )

    private fun omerLinks(profile: UserProfile) = when (profile.effectiveNusach()) {
        EffectiveNusach.CHABAD -> listOf(
            ChecklistLink("Chabad — Sefirat HaOmer", "https://www.chabad.org/library/article_cdo/aid/130631/jewish/Sefirat-HaOmer.htm", "chabad"),
            ChecklistLink("Peninei Halacha — Sefirat HaOmer", "https://ph.yhb.org.il/en/category/zemanim/05-02/", "default"),
            ChecklistLink("Sefaria", "https://www.sefaria.org/Numbers.28.26", "default")
        )
        else -> listOf(
            ChecklistLink("Aish — Sefirat HaOmer", "https://aish.com/holidays/pesach/", "default"),
            ChecklistLink("Peninei Halacha — Sefirat HaOmer", "https://ph.yhb.org.il/en/category/zemanim/05-02/", "default"),
            ChecklistLink("Sefaria — Omer", "https://www.sefaria.org/Numbers.28.26", "default"),
            ChecklistLink("OU — Sefirat HaOmer", "https://ou.org/judaism/omer/", "default")
        )
    }

    private fun sefirahMourningLinks(profile: UserProfile) = when (profile.effectiveNusach()) {
        EffectiveNusach.CHABAD -> listOf(
            ChecklistLink("Chabad — Sefirah customs", "https://www.chabad.org/library/article_cdo/aid/130631/jewish/Sefirat-HaOmer.htm", "chabad"),
            ChecklistLink("Peninei Halacha — Sefirat HaOmer period", "https://ph.yhb.org.il/en/category/zemanim/05-02/", "default")
        )
        else -> listOf(
            ChecklistLink("Aish — Sefirat HaOmer period", "https://aish.com/holidays/pesach/", "default"),
            ChecklistLink("Chabad — Sefirah overview", "https://www.chabad.org/library/article_cdo/aid/130631/jewish/Sefirat-HaOmer.htm", "default"),
            ChecklistLink("Peninei Halacha — Sefirat HaOmer period", "https://ph.yhb.org.il/en/category/zemanim/05-02/", "default"),
            ChecklistLink("Ohr Somayach — Sefirah", "https://ohr.edu/this_week/prayer_essentials/7338", "default")
        )
    }

    private fun erevChanukahPrepLinks() = listOf(
        ChecklistLink(
            "Ohr Somayach — The Laws of Chanukah",
            "https://ohr.edu/1304",
            "default"
        )
    )

    private fun purimMegillahLinks(profile: UserProfile) = buildList {
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(
                ChecklistLink(
                    "Chabad — The Megillah",
                    "https://www.chabad.org/holidays/purim/article_cdo/aid/644322/jewish/Laws-and-Customs.htm",
                    "chabad"
                )
            )
        }
        add(ChecklistLink("Aish — Megillah & Purim", "https://aish.com/holidays/purim/", "default"))
        add(ChecklistLink("Peninei Halacha — Megillah", "https://ph.yhb.org.il/en/category/zemanim/05-15/", "default"))
        add(ChecklistLink("Ohr Somayach — Purim", "https://ohr.edu/1508", "default"))
        add(ChecklistLink("Sefaria — Megillah", "https://www.sefaria.org/Esther", "default"))
    }

    private fun purimMatanotLinks(profile: UserProfile) = buildList {
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(
                ChecklistLink(
                    "Chabad — Gifts to the poor",
                    "https://www.chabad.org/holidays/purim/article_cdo/aid/5846239/jewish/Matanot-Laevyonim-FAQs.htm",
                    "chabad"
                )
            )
        }
        add(ChecklistLink("Peninei Halacha — Matanot la'evyonim", "https://ph.yhb.org.il/en/category/zemanim/05-15/", "default"))
        add(ChecklistLink("Aish — Purim mitzvot", "https://aish.com/holidays/purim/", "default"))
    }

    private fun purimMishloachLinks(profile: UserProfile) = buildList {
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(
                ChecklistLink(
                    "Chabad — Mishloach manot",
                    "https://www.chabad.org/holidays/purim/article_cdo/aid/261101/jewish/Mishloach-Manot-Sending-Food-Gifts-on-Purim.htm",
                    "chabad"
                )
            )
        }
        add(ChecklistLink("Peninei Halacha — Mishloach manot", "https://ph.yhb.org.il/en/category/zemanim/05-15/", "default"))
        add(ChecklistLink("Aish — Purim mitzvot", "https://aish.com/holidays/purim/", "default"))
    }

    private fun purimMeshulashErevMegillahItem(profile: UserProfile) = ChecklistItemDef(
        id = "purim_meshulash_erev_megillah",
        title = "Purim Meshulash (Thursday night): Hear the Megillah",
        section = "Purim",
        timeOfDay = TimeOfDay.NIGHT,
        required = true,
        situational = false,
        seasons = emptyList(),
        explanation = PurimMeshulashText.erevMegillahExplanation(),
        explanationAshkenaz = PurimBrachotText.SHEHECHEYANU_ASHKENAZ,
        explanationSefard = PurimBrachotText.SHEHECHEYANU_SEPHARDIC,
        explanationEdotHamizrach = PurimBrachotText.SHEHECHEYANU_SEPHARDIC,
        explanationChabad = PurimBrachotText.SHEHECHEYANU_ASHKENAZ,
        links = purimMegillahLinks(profile),
    )

    private fun purimMeshulashFridayItems(profile: UserProfile): List<ChecklistItemDef> = listOf(
        ChecklistItemDef(
            id = "purim_meshulash_friday_megillah",
            title = "Purim Meshulash (Friday): Hear the Megillah",
            section = "Purim",
            timeOfDay = TimeOfDay.ANY,
            required = true,
            situational = false,
            seasons = listOf("purim_meshulash_friday"),
            explanation = PurimMeshulashText.fridayMegillahExplanation(),
            explanationAshkenaz = PurimBrachotText.SHEHECHEYANU_ASHKENAZ,
            explanationSefard = PurimBrachotText.SHEHECHEYANU_SEPHARDIC,
            explanationEdotHamizrach = PurimBrachotText.SHEHECHEYANU_SEPHARDIC,
            explanationChabad = PurimBrachotText.SHEHECHEYANU_ASHKENAZ,
            links = purimMegillahLinks(profile)
        ),
        ChecklistItemDef(
            id = "purim_meshulash_friday_matanot",
            title = "Purim Meshulash (Friday): Matanot la'evyonim",
            section = "Purim",
            timeOfDay = TimeOfDay.DAY,
            required = true,
            situational = false,
            seasons = listOf("purim_meshulash_friday"),
            explanation = PurimMeshulashText.fridayMatanotExplanation(),
            links = purimMatanotLinks(profile)
        )
    )

    private fun purimMeshulashShabbatItems(profile: UserProfile): List<ChecklistItemDef> = listOf(
        ChecklistItemDef(
            id = "purim_meshulash_shabbat_al_hanissim",
            title = "Purim Meshulash (Shabbat): Al HaNissim in davening & bentching",
            section = "Purim",
            timeOfDay = TimeOfDay.ANY,
            required = true,
            situational = false,
            seasons = listOf("purim_meshulash_shabbat"),
            explanation = PurimMeshulashText.shabbatAlHaNissimExplanation(),
            links = purimPrepLinks(),
        ),
        ChecklistItemDef(
            id = "purim_meshulash_shabbat_torah",
            title = "Purim Meshulash (Shabbat): Vayavo Amalek at shul",
            section = "Purim",
            timeOfDay = TimeOfDay.DAY,
            required = false,
            situational = false,
            seasons = listOf("purim_meshulash_shabbat"),
            explanation = PurimMeshulashText.shabbatTorahExplanation(),
            links = purimPrepLinks(),
        ),
    )

    private fun purimMeshulashSundayItems(profile: UserProfile): List<ChecklistItemDef> = listOf(
        ChecklistItemDef(
            id = "purim_meshulash_sunday_mishloach",
            title = "Purim Meshulash (Sunday): Mishloach manot",
            section = "Purim",
            timeOfDay = TimeOfDay.DAY,
            required = true,
            situational = false,
            seasons = listOf("purim_meshulash_sunday"),
            explanation = PurimMeshulashText.sundayMishloachExplanation(),
            links = purimMishloachLinks(profile)
        ),
        ChecklistItemDef(
            id = "purim_meshulash_sunday_seudah",
            title = "Purim Meshulash (Sunday): Purim seudah",
            section = "Purim",
            timeOfDay = TimeOfDay.DAY,
            required = true,
            situational = false,
            seasons = listOf("purim_meshulash_sunday"),
            explanation = PurimMeshulashText.sundaySeudahExplanation(),
            links = purimMegillahLinks(profile)
        )
    )

    private fun cholHamoedWineLinks(cal: DayInfo, profile: UserProfile) = buildList {
        if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
            add(
                ChecklistLink(
                    "Chabad — Wine on Chol HaMoed",
                    "https://www.chabad.org/library/article_cdo/aid/5279/jewish/Chol-Hamoed.htm",
                    "chabad"
                )
            )
        }
        add(ChecklistLink("Peninei Halacha — Chol HaMoed", "https://ph.yhb.org.il/en/12-11-01/", "default"))
        val aishUrl = if ("chol_hamoed_sukkot" in cal.activeSeasons && "chol_hamoed_pesach" !in cal.activeSeasons) {
            "https://aish.com/holidays/sukkot/"
        } else {
            "https://aish.com/holidays/pesach/"
        }
        add(ChecklistLink("Aish — Chol HaMoed", aishUrl, "default"))
    }

    private fun pesachPrepLinks(profile: UserProfile) = listOf(
        ChecklistLink("Chabad — Erev Pesach guide", "https://www.chabad.org/holidays/passover/pesach_cdo/aid/1719/jewish/Erev-Pesach.htm", "chabad"),
        ChecklistLink("Aish — Preparing for Passover", "https://aish.com/holidays/pesach/", "default"),
        ChecklistLink("Peninei Halacha — Pesach preparations", "https://ph.yhb.org.il/en/04-03-01/", "default")
    )

    private fun mourningLinks(profile: UserProfile) = listOf(
        ChecklistLink("Chabad — The Three Weeks", "https://www.chabad.org/library/article_cdo/aid/144558/jewish/Tisha-BAv-and-the-3-Weeks.htm", "chabad"),
        ChecklistLink("Aish — Nine Days overview", "https://aish.com/48943916/", "default"),
        ChecklistLink("Peninei Halacha — Three Weeks and Nine Days", "https://ph.yhb.org.il/en/category/zemanim/05-08/", "default")
    )

    private fun selichotLinks(profile: UserProfile) = listOf(
        ChecklistLink("Chabad — Selichot", "https://www.chabad.org/library/article_cdo/aid/4744/jewish/Selichot.htm", "chabad"),
        ChecklistLink("Sefaria — Selichot texts", "https://www.sefaria.org/topics/selichot", "default"),
        ChecklistLink("Aish — Selichot overview", "https://aish.com/slichot_and_the_13_attributes/", "default"),
        ChecklistLink("Peninei Halacha — Selichot customs", "https://ph.yhb.org.il/en/category/15/15-01/", "default")
    )

    private fun tuBshvatSederItem(profile: UserProfile) = ChecklistItemDef(
        id = "tu_bshvat_seder_optional",
        title = "Tu B'Shvat Seder (optional)",
        section = "Seasonal",
        timeOfDay = TimeOfDay.ANY,
        required = false,
        situational = false,
        explanation = SeasonalMitzvahText.tuBshvatExplanation(),
        links = SeasonalMitzvahText.tuBshvatLinks()
    )

    private fun yaalehVyavoRoshChodeshItems(profile: UserProfile) = listOf(
        ChecklistItemDef(
            id = "yaaleh_vyavo_rosh_chodesh_shacharit",
            title = "Yaaleh V'yavo — Rosh Chodesh (in Shacharit Amidah)",
            section = "Morning Prayer (Shacharit)",
            sortOrder = 52,
            timeOfDay = TimeOfDay.ANY,
            required = true,
            explanation = SeasonalMitzvahText.yaalehVyavoShacharitExplanation(),
            explanationFemale = SeasonalMitzvahText.yaalehVyavoShacharitExplanationFemale(),
            links = SeasonalMitzvahText.yaalehVyavoLinks(profile),
        ),
        ChecklistItemDef(
            id = "yaaleh_vyavo_rosh_chodesh_mincha",
            title = "Yaaleh V'yavo — Rosh Chodesh (in Mincha Amidah)",
            section = "Afternoon Prayer",
            sortOrder = 15,
            timeOfDay = TimeOfDay.DAY,
            required = true,
            explanation = SeasonalMitzvahText.yaalehVyavoMinchaExplanation(),
            explanationFemale = SeasonalMitzvahText.yaalehVyavoMinchaExplanationFemale(),
            links = SeasonalMitzvahText.yaalehVyavoLinks(profile),
        ),
        ChecklistItemDef(
            id = "yaaleh_vyavo_rosh_chodesh",
            title = "Yaaleh V'yavo — Rosh Chodesh (in Maariv Amidah)",
            section = "Evening Prayer",
            sortOrder = 25,
            timeOfDay = TimeOfDay.NIGHT,
            required = true,
            explanation = SeasonalMitzvahText.yaalehVyavoMaarivExplanation(),
            explanationFemale = SeasonalMitzvahText.yaalehVyavoMaarivExplanationFemale(),
            links = SeasonalMitzvahText.yaalehVyavoLinks(profile),
        ),
    )

    private fun roshChodeshHalfHallelItem(profile: UserProfile) = ChecklistItemDef(
        id = "rosh_chodesh_half_hallel",
        title = "Half Hallel — Rosh Chodesh",
        section = "Morning Prayer (Shacharit)",
        sortOrder = 54,
        timeOfDay = TimeOfDay.DAY,
        required = true,
        explanation = SeasonalMitzvahText.roshChodeshHalfHallelExplanation(),
        explanationFemale = SeasonalMitzvahText.roshChodeshHalfHallelExplanationFemale(),
        explanationAshkenaz = SeasonalMitzvahText.roshChodeshHalfHallelAshkenazNote(),
        explanationSefard = SeasonalMitzvahText.roshChodeshHalfHallelSephardNote(),
        explanationEdotHamizrach = SeasonalMitzvahText.roshChodeshHalfHallelSephardNote(),
        explanationChabad = SeasonalMitzvahText.roshChodeshHalfHallelChabadNote(),
        links = SeasonalMitzvahText.roshChodeshLinks(profile),
    )

    private fun roshChodeshFullHallelDuringChanukahItem(profile: UserProfile) = ChecklistItemDef(
        id = "rosh_chodesh_full_hallel_chanukah",
        title = "Full Hallel — Rosh Chodesh during Chanukah",
        section = "Morning Prayer (Shacharit)",
        sortOrder = 54,
        timeOfDay = TimeOfDay.DAY,
        required = true,
        explanation = SeasonalMitzvahText.roshChodeshFullHallelChanukahExplanation(),
        explanationFemale = SeasonalMitzvahText.roshChodeshFullHallelChanukahExplanationFemale(),
        explanationAshkenaz = SeasonalMitzvahText.roshChodeshFullHallelAshkenazNote(),
        explanationSefard = SeasonalMitzvahText.roshChodeshFullHallelSephardNote(),
        explanationEdotHamizrach = SeasonalMitzvahText.roshChodeshFullHallelSephardNote(),
        explanationChabad = SeasonalMitzvahText.roshChodeshFullHallelChabadNote(),
        links = SeasonalMitzvahText.roshChodeshLinks(profile),
    )

    /** Visible only after molad+72h (Ashkenaz/Chabad) or +7 days (Sefard), through day 15. */
    private fun isKiddushLevanaWindow(cal: DayInfo, profile: UserProfile, nowMillis: Long): Boolean {
        val day = cal.hebrewDay ?: return false
        if (day !in 1..15) return false
        val year = cal.hebrewYear
        val month = cal.hebrewMonth
        if (year != null && month != null) {
            val openMillis = when (profile.effectiveNusach()) {
                EffectiveNusach.SEFARD, EffectiveNusach.EDOT_HAMIZRACH ->
                    HebrewCalendarEngine.tchilasZmanKidushLevana7DaysMillis(year, month)
                else ->
                    HebrewCalendarEngine.tchilasZmanKidushLevana3DaysMillis(year, month)
            }
            return nowMillis >= openMillis
        }
        val minDay = when (profile.effectiveNusach()) {
            EffectiveNusach.SEFARD, EffectiveNusach.EDOT_HAMIZRACH -> 7
            else -> 3
        }
        return day >= minDay
    }

    private fun roshChodeshMonthlyItem(profile: UserProfile) = ChecklistItemDef(
        id = "rosh_chodesh_observances",
        title = "Rosh Chodesh — the New Month",
        section = "Monthly",
        sortOrder = 10,
        timeOfDay = TimeOfDay.ANY,
        required = false,
        situational = false,
        explanation = SeasonalMitzvahText.roshChodeshObservancesExplanation(),
        explanationFemale = SeasonalMitzvahText.roshChodeshObservancesExplanationFemale(),
        links = SeasonalMitzvahText.roshChodeshLinks(profile),
    )

    private fun kiddushLevanaItem(profile: UserProfile) = ChecklistItemDef(
        id = "kiddush_levana",
        title = "Kiddush Levana — Sanctification of the Moon (once per Hebrew month)",
        section = "Monthly",
        sortOrder = 20,
        timeOfDay = TimeOfDay.NIGHT,
        required = true,
        situational = false,
        gender = "male",
        monthlyMitzvah = true,
        explanation = SeasonalMitzvahText.kiddushLevanaExplanationTemplate(),
        links = SeasonalMitzvahText.kiddushLevanaLinks(profile)
    )

    private fun erevMinorFastPrepItem(cal: DayInfo, profile: UserProfile): ChecklistItemDef {
        val idx = cal.upcomingFastDayIndex!!
        return ChecklistItemDef(
            id = "erev_public_fast_prep",
            title = PublicFastDayText.erevMinorFastPrepTitle(PublicFastDayRules.displayName(idx)),
            section = "Fasts",
            sortOrder = 5,
            timeOfDay = TimeOfDay.DAY,
            required = true,
            seasons = listOf("erev_minor_fast"),
            explanation = PublicFastDayText.erevMinorFastPrepExplanation(cal, idx, profile),
            links = PublicFastDayText.erevMinorFastLinks(),
        )
    }

    private fun erevYomKippurEatItem(cal: DayInfo, profile: UserProfile) = ChecklistItemDef(
        id = "erev_yom_kippur_eat",
        title = PublicFastDayText.erevYomKippurTitle(),
        section = "Fasts",
        sortOrder = 5,
        timeOfDay = TimeOfDay.DAY,
        required = true,
        seasons = listOf("erev_yom_kippur"),
        explanation = PublicFastDayText.erevYomKippurExplanation(cal, profile),
        links = PublicFastDayText.erevYomKippurLinks(profile),
    )

    private fun erevTishaBeavPrepItem(
        cal: DayInfo,
        profile: UserProfile,
        deferredToSunday: Boolean = false,
    ) = ChecklistItemDef(
        id = "erev_tisha_beav_prep",
        title = PublicFastDayText.erevTishaBeavTitle(deferredToSunday),
        section = "Fasts",
        sortOrder = 5,
        timeOfDay = TimeOfDay.DAY,
        required = true,
        seasons = if (deferredToSunday) emptyList() else listOf("erev_tisha_beav"),
        explanation = PublicFastDayText.erevTishaBeavExplanation(cal, profile, deferredToSunday),
        links = PublicFastDayText.erevTishaBeavLinks(),
    )

    private fun publicFastDayItem(cal: DayInfo, profile: UserProfile): ChecklistItemDef {
        val idx = cal.fastDayIndex!!
        return ChecklistItemDef(
            id = "public_fast_day",
            title = PublicFastDayText.fastDayTitle(idx),
            section = "Fasts",
            sortOrder = -20,
            timeOfDay = TimeOfDay.ANY,
            required = true,
            seasons = listOf("fast_day"),
            explanation = PublicFastDayText.fastDayExplanation(idx, cal, profile),
            links = PublicFastDayText.fastDayLinks(idx, profile),
        )
    }

    private fun tefillinTishaBeavMinchaItem() = ChecklistItemDef(
        id = "tefillin_tisha_beav_mincha",
        title = TishaBeavTefillinRules.minchaItemTitle(),
        section = "Afternoon Prayer",
        timeOfDay = TimeOfDay.DAY,
        required = true,
        gender = "male",
        sortOrder = 8,
        seasons = listOf("fast_day"),
        explanation = TishaBeavTefillinRules.minchaItemExplanation(),
        links = listOf(
            ChecklistLink(
                "Shulchan Arukh — Tisha B'Av",
                "https://www.sefaria.org/Shulchan_Arukh,_Orach_Chayim.555",
            ),
            ChecklistLink(
                "Rambam — mourning practices",
                "https://www.sefaria.org/Mishneh_Torah,_Fasts.5",
            ),
        ),
    )

    private fun motzeiYomKippurMealItem(cal: DayInfo, profile: UserProfile) = ChecklistItemDef(
        id = "motzei_yom_kippur_meal",
        title = PublicFastDayText.motzeiYomKippurMealTitle(),
        section = "Motzei Yom Kippur",
        sortOrder = 20,
        timeOfDay = TimeOfDay.NIGHT,
        required = true,
        // Visible Motzei even after Hebrew rollover to 11 Tishrei (no "fast_day" season then).
        seasons = null,
        explanation = PublicFastDayText.motzeiYomKippurMealExplanation(cal, profile),
        links = PublicFastDayText.erevYomKippurLinks(profile),
    )

    private fun defaultLinks(profile: UserProfile) =
        listOf(ChecklistLink("Learn more", ChecklistLoader.defaultUrl(profile), null))
}
