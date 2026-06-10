package com.beardytop.beatzaddik.domain

import com.beardytop.beatzaddik.data.ChecklistLoader
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.plus

object SeasonalChecklistItems {

    fun forDay(
        cal: DayInfo,
        profile: UserProfile,
        tomorrowCal: DayInfo,
        dayAfterTomorrowCal: DayInfo,
    ): List<ChecklistItemDef> = buildList {
        if (cal.isSefiratHaomer && cal.omerDay != null && cal.omerDay in 1..49 && !cal.isLagBaomer) {
            add(omerItem(cal, profile))
            add(sefirahMourningMusicItem(profile))
        }
        if (cal.isChanukah && cal.chanukahDay != null) {
            add(chanukahItem(cal.chanukahDay, profile))
        }
        if ("purim_meshulash_friday" in cal.activeSeasons) {
            addAll(purimMeshulashFridayItems(profile))
        } else if ("purim_meshulash_sunday" in cal.activeSeasons) {
            addAll(purimMeshulashSundayItems(profile))
        } else if (cal.isPurim) {
            addAll(purimItems(profile))
        }
        if ("erev_chag" in cal.activeSeasons && !HolyDayPhoneRules.isShabbatMelachaDay(cal)) {
            add(erevChagPrepItem(cal, profile, tomorrowCal))
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
        if ("erev_chanukah" in cal.activeSeasons) {
            add(erevChanukahPrepItem(profile))
        }
        if (cal.isRoshChodesh) {
            add(
                ChecklistItemDef(
                    id = "rosh_chodesh_prayers",
                    title = "Rosh Chodesh — add Yaaleh V'yavo & Hallel",
                    section = "Seasonal",
                    timeOfDay = TimeOfDay.DAY,
                    required = true,
                    situational = false,
                    explanation = SeasonalMitzvahText.roshChodeshExplanation(),
                    links = SeasonalMitzvahText.roshChodeshLinks(profile)
                )
            )
        }
        if (isKiddushLevanaWindow(cal, profile)) {
            add(kiddushLevanaItem(profile))
        }
        if (isTuBshvat(cal)) {
            add(tuBshvatSederItem(profile))
        }
        if (cal.isYomHaShoah) {
            add(yomHaShoahItem(profile))
        }
        if (cal.isYomHaZikaron) {
            add(yomHaZikaronItem(profile))
        }
        if (cal.isYomHaAtzmaut) {
            add(yomHaAtzmautItem(profile))
        }
        if (cal.isYomYerushalayim) {
            add(yomYerushalayimItem(profile))
        }
        if ("erev_pesach" in cal.activeSeasons) {
            addAll(erevPesachItems(cal, profile))
        }
        festivalWeekPrepItem(cal, profile)?.let { add(it) }
        if ("chol_hamoed_pesach" in cal.activeSeasons || "chol_hamoed_sukkot" in cal.activeSeasons) {
            addAll(cholHamoedItems(cal, profile))
        }
        if ("sukkot" in cal.activeSeasons) {
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
        if (isInThreeWeeks(cal)) {
            add(threeWeeksMourningItem(profile))
        }
        if (isInNineDays(cal)) {
            add(nineDaysMourningItem(profile))
        }
        selichotItemForDay(cal, profile)?.let { add(it) }
    }

    private fun omerItem(cal: DayInfo, profile: UserProfile): ChecklistItemDef {
        val day = cal.omerDay!!
        return ChecklistItemDef(
            id = "sefirat_haomer_day_$day",
            title = OmerCountText.buildTitle(day),
            section = "Sefirat HaOmer",
            timeOfDay = TimeOfDay.NIGHT,
            required = true,
            seasons = listOf("sefirah"),
            explanation = OmerCountText.buildExplanation(cal, profile),
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
                """During Sefirat HaOmer we keep customs of mourning (aveilut) because Rabbi Akiva's 24,000 students died in a plague during this period between Pesach and Shavuot (Talmud, Yevamot 62b). Their deaths ceased on Lag BaOmer — which is why many communities ease some restrictions then, while others continue until Shavuot or the morning of the 33rd day of the Omer.

Why we mourn: The Omer is the path from physical freedom (Pesach) to spiritual receiving of Torah (Shavuot). The plague cut short Torah transmission — so we temper joy with restraint until we reach Matan Torah.

Common customs (timing varies — ask your rav):
• No live music (recordings and a cappella rules vary by posek)
• No weddings
• No haircuts for part or all of the Omer

Follow your community's start and end dates for these practices.""",
            ),
            explanationAshkenaz = """Ashkenaz custom: mourning from after Pesach until Lag BaOmer (33rd day of the Omer, 18 Iyar) or until the morning of Lag BaOmer (per your shul). Some continue haircuts/music restrictions until Shavuot or the Three Weeks.

No weddings, no live music, and no haircuts during your community's Sefirah period. Lag BaOmer is a break for many Ashkenazim; ask your rabbi about music and haircuts after that date.""",
            explanationSefard = """Sephardic communities follow two main traditions: mourning from Pesach until the morning of the 34th day of the Omer (Lamed-Dalet Omer), per Shulchan Arukh O.C. 493:2; or from Rosh Chodesh Iyar through Shavuot, per the Arizal. Music, weddings, and haircuts follow your kehilla's psak.

Ask your rav which tradition you follow and when restrictions begin and end.""",
            explanationChabad = """Chabad (Alter Rebbe / Arizal): haircut and shaving restrictions continue the entire 49 days through Erev Shavuot — adults do not take haircuts on Lag BaOmer (the sole exception is upsherin for a 3-year-old boy). Lag BaOmer is a day of intense joy with music, bonfires, and celebration, but haircut restrictions remain until Shavuot.

Music is generally avoided through Shavuot per Chabad practice, with Lag BaOmer as a day without music restrictions. Weddings follow your Chabad rabbi's guidance.

See Chabad.org Sefirah articles for details on your community.""",
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
            explanation = SeasonalMitzvahText.chanukahLightingExplanation(day, profile),
            links = SeasonalMitzvahText.chanukahDayLinks(profile)
        )

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
                """Mikra Megillah (hearing the Book of Esther) is a Torah-level mitzvah (Esther 9:28). Men and women are equally obligated.

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
• Shehecheyanu on the first evening (and on the first daytime reading of the year, per custom)

Machatzit haShekel: A widespread pre-Purim custom (not one of the four Purim mitzvot in the same way); many give before Megillah — follow your community. Confirm local reading times with your shul.

Prayers & meals:
• Insert Al HaNissim into every Amidah and into Birkat Hamazon (bentching) all day long on Purim.""",
            ),
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

The mitzvah (Peninei Halakha 05-16-03; Chabad.org):
• Give at least one gift to each of two different poor people (minimum of two recipients total) during Purim daytime.
• Each gift should enable a modest Purim meal — money is common (Peninei Halakha: roughly enough for about three slices of bread or your community's minimum; amounts vary).

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
• A festive meal with bread (hamotzi — many use two rolls), meat, wine, and joy. Do not use matzah — there is no Purim custom for matzah, and many avoid matzah in the weeks before Pesach so the Seder taste stays distinct (Rema O.C. 471:2).
• Include words of Torah or thanks to Hashem — the meal is a mitzvah, not only a party.
• Drinking wine is a widespread custom but not required to excess; celebrate responsibly.

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
            section = "Seasonal",
            timeOfDay = TimeOfDay.DAY,
            required = false,
            situational = false,
            explanation = explanation,
            links = links
        )
    }

    private fun erevChagPrepItem(cal: DayInfo, profile: UserProfile, tomorrowCal: DayInfo): ChecklistItemDef {
        val prep = ErevChagPrepText.build(cal, profile, tomorrowCal)
        return ChecklistItemDef(
            id = "erev_chag_prep",
            title = prep.title,
            section = "Seasonal",
            timeOfDay = TimeOfDay.DAY,
            required = false,
            situational = false,
            seasons = listOf("erev_chag"),
            explanation = prep.explanation,
            links = prep.links
        )
    }

    private fun erevPurimPrepItem(cal: DayInfo, tomorrowCal: DayInfo) = ChecklistItemDef(
        id = "erev_purim_prep",
        title = if (PurimMeshulashText.isErevBeforeMeshulashFriday(cal, tomorrowCal)) {
            "Purim Meshulash — read full plan before Shabbat (phone off)"
        } else {
            "Erev Purim prep — arrange Megillah and mitzvot"
        },
        section = "Purim",
        timeOfDay = TimeOfDay.DAY,
        required = false,
        situational = false,
        seasons = listOf("erev_purim"),
        explanation = if (PurimMeshulashText.isErevBeforeMeshulashFriday(cal, tomorrowCal)) {
            PurimMeshulashText.erevPrepExplanation()
        } else {
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
        title = "Erev Chanukah prep — set up menorah, candles, and brachot",
        section = "Chanukah",
        timeOfDay = TimeOfDay.DAY,
        required = false,
        situational = false,
        seasons = listOf("erev_chanukah"),
        explanation = "Chanukah starts tomorrow night. Prepare the menorah, enough candles/oil, and [review lighting time and brachot](https://ohr.edu/1304) before tomorrow night.",
        links = erevChanukahPrepLinks()
    )

    private fun erevPesachItems(cal: DayInfo, profile: UserProfile): List<ChecklistItemDef> = listOf(
        ChecklistItemDef(
            id = "erev_pesach_mechirat_chametz",
            title = "Mechirat chametz — sell chametz before Pesach",
            section = "Pesach prep",
            sortOrder = 10,
            timeOfDay = TimeOfDay.DAY,
            required = false,
            situational = false,
            seasons = listOf("erev_pesach"),
            explanation = ErevPesachPrepText.mechiratExplanation(cal, profile),
            links = ErevPesachPrepText.mechiratLinks(profile)
        ),
        ChecklistItemDef(
            id = "erev_pesach_taanit_bechorot",
            title = "Taanit Bechorot — Fast of the Firstborn (or siyum)",
            section = "Pesach prep",
            sortOrder = 20,
            timeOfDay = TimeOfDay.DAY,
            required = false,
            situational = false,
            seasons = listOf("erev_pesach"),
            explanation = ErevPesachPrepText.taanitBechorExplanation(cal, profile),
            links = ErevPesachPrepText.taanitBechorLinks(profile)
        ),
        ChecklistItemDef(
            id = "erev_pesach_prepare_seder",
            title = "Prepare for the Seder (matzah, maror, cups, Haggadah)",
            section = "Pesach prep",
            sortOrder = 30,
            timeOfDay = TimeOfDay.DAY,
            required = false,
            situational = false,
            seasons = listOf("erev_pesach"),
            explanation = ErevPesachPrepText.sederPrepExplanation(cal, profile),
            links = ErevPesachPrepText.sederPrepLinks(profile)
        ),
        ChecklistItemDef(
            id = "bedikat_chametz",
            title = "Bedikat chametz — search tonight",
            section = "Pesach prep",
            sortOrder = 40,
            timeOfDay = TimeOfDay.NIGHT,
            required = true,
            situational = false,
            seasons = listOf("erev_pesach"),
            explanation = ErevPesachPrepText.bedikatExplanation(cal, profile),
            links = ErevPesachPrepText.bedikatLinks(profile)
        ),
        ChecklistItemDef(
            id = "erev_pesach_biur_chametz",
            title = "Biur chametz — burn/remove chametz tomorrow morning",
            section = "Pesach prep",
            sortOrder = 50,
            timeOfDay = TimeOfDay.DAY,
            required = true,
            situational = false,
            seasons = listOf("erev_pesach"),
            explanation = ErevPesachPrepText.biurExplanation(cal, profile),
            links = ErevPesachPrepText.biurLinks(profile)
        )
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
            explanation = SeasonalMitzvahText.festivalWeekPrepExplanation(cal, profile, prep),
            links = SeasonalMitzvahText.festivalWeekPrepLinks(prep, profile)
        )
    }

    private fun cholHamoedItems(cal: DayInfo, profile: UserProfile): List<ChecklistItemDef> {
        val list = mutableListOf(
            ChecklistItemDef(
                id = "chol_hamoed_honor",
                title = "Honor Chol HaMoed with a festive day",
                section = "Chol HaMoed",
                timeOfDay = TimeOfDay.DAY,
                required = false,
                situational = false,
                seasons = listOf("chol_hamoed_pesach", "chol_hamoed_sukkot"),
                explanation = SeasonalMitzvahText.cholHamoedHonorExplanation(cal, profile),
                links = SeasonalMitzvahText.cholHamoedLinks(cal, profile)
            ),
            ChecklistItemDef(
                id = "chol_hamoed_wine_reviit",
                title = "Revi'it of wine — daily mitzvah on Chol HaMoed (recommended)",
                section = "Chol HaMoed",
                timeOfDay = TimeOfDay.DAY,
                required = false,
                situational = false,
                seasons = listOf("chol_hamoed_pesach", "chol_hamoed_sukkot"),
                explanation = SeasonalMitzvahText.cholHamoedWineReviitExplanation(cal),
                links = cholHamoedWineLinks(cal, profile)
            ),
            ChecklistItemDef(
                id = "chol_hamoed_nicer_clothes",
                title = "Wear nicer clothes in honor of the moed",
                section = "Chol HaMoed",
                timeOfDay = TimeOfDay.DAY,
                required = false,
                situational = false,
                seasons = listOf("chol_hamoed_pesach", "chol_hamoed_sukkot"),
                explanation = SeasonalMitzvahText.cholHamoedClothesExplanation(),
                links = SeasonalMitzvahText.cholHamoedLinks(cal, profile)
            )
        )
        if ("chol_hamoed_pesach" in cal.activeSeasons) {
            list += ChecklistItemDef(
                id = "chol_hamoed_pesach_matzah",
                title = "Eat matzah on Pesach (optional on Chol HaMoed)",
                section = "Chol HaMoed",
                timeOfDay = TimeOfDay.DAY,
                required = false,
                situational = false,
                seasons = listOf("chol_hamoed_pesach"),
                explanation = SeasonalMitzvahText.cholHamoedMatzahExplanation(),
                links = SeasonalMitzvahText.pesachWeekLinks(profile)
            )
        }
        return list
    }

    private fun arbaMinimItem(profile: UserProfile): ChecklistItemDef {
        val isFemale = profile.gender == Gender.FEMALE
        return ChecklistItemDef(
            id = "sukkot_arba_minim",
            title = if (isFemale) {
                "Wave Arba Minim — recommended mitzvah (women)"
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
        section = "Seasonal",
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
        timeOfDay = TimeOfDay.DAY,
        required = false,
        situational = false,
        explanation = SeasonalMitzvahText.threeWeeksExplanation(profile),
        explanationAshkenaz = SeasonalMitzvahText.threeWeeksExplanation(profile),
        explanationSefard = SeasonalMitzvahText.threeWeeksExplanation(profile),
        explanationChabad = SeasonalMitzvahText.threeWeeksExplanation(profile),
        links = mourningLinks(profile)
    )

    private fun nineDaysMourningItem(profile: UserProfile) = ChecklistItemDef(
        id = "nine_days_mourning_customs",
        title = "Nine Days: observe stricter mourning customs",
        section = "Mourning customs",
        timeOfDay = TimeOfDay.DAY,
        required = false,
        situational = false,
        explanation = SeasonalMitzvahText.nineDaysExplanation(profile),
        explanationAshkenaz = SeasonalMitzvahText.nineDaysExplanation(profile),
        explanationSefard = SeasonalMitzvahText.nineDaysExplanation(profile),
        explanationChabad = SeasonalMitzvahText.nineDaysExplanation(profile),
        links = mourningLinks(profile)
    )

    private fun selichotItemForDay(cal: DayInfo, profile: UserProfile): ChecklistItemDef? {
        val month = cal.hebrewMonth ?: return null
        val day = cal.hebrewDay ?: return null
        if (month != HebrewCalendarEngine.ELUL) return null
        return when (profile.effectiveNusach()) {
            EffectiveNusach.SEFARD -> {
                if (day == 1) return null
                ChecklistItemDef(
                id = "selichot_elul_sefard",
                title = "Say Selichot (Sefard custom)",
                section = "Seasonal",
                timeOfDay = TimeOfDay.NIGHT,
                required = false,
                situational = false,
                explanation = SeasonalMitzvahText.selichotExplanation(EffectiveNusach.SEFARD),
                links = selichotLinks(profile)
                )
            }
            EffectiveNusach.CHABAD -> ChecklistItemDef(
                id = "selichot_elul_chabad",
                title = "Say Selichot (Chabad — Nusach Ari)",
                section = "Seasonal",
                timeOfDay = TimeOfDay.NIGHT,
                required = false,
                situational = false,
                explanation = SeasonalMitzvahText.selichotExplanation(EffectiveNusach.CHABAD),
                links = selichotLinks(profile)
            )
            EffectiveNusach.ASHKENAZ -> {
                if (cal.date >= ashkenazSelichotStartDate(cal)) {
                    ChecklistItemDef(
                        id = "selichot_elul_ashkenaz",
                        title = "Say Selichot (Ashkenaz timing)",
                        section = "Seasonal",
                        timeOfDay = TimeOfDay.NIGHT,
                        required = false,
                        situational = false,
                        explanation = SeasonalMitzvahText.selichotExplanation(EffectiveNusach.ASHKENAZ),
                        links = selichotLinks(profile)
                    )
                } else null
            }
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

    private fun isNightAfterYomKippur(cal: DayInfo): Boolean {
        val month = cal.hebrewMonth ?: return false
        val day = cal.hebrewDay ?: return false
        return month == HebrewCalendarEngine.TISHREI && day == 11
    }

    private fun isInThreeWeeks(cal: DayInfo): Boolean {
        val month = cal.hebrewMonth ?: return false
        val day = cal.hebrewDay ?: return false
        return (month == HebrewCalendarEngine.TAMMUZ && day >= 17) ||
            (month == HebrewCalendarEngine.AV && day <= 9)
    }

    private fun isInNineDays(cal: DayInfo): Boolean {
        val month = cal.hebrewMonth ?: return false
        val day = cal.hebrewDay ?: return false
        return month == HebrewCalendarEngine.AV && day in 1..9
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

This is not a Yom Tov — melacha is fully permitted. It is a national civil observance, not a halachically mandated fast or prayer day.

Date adjustment: If 27 Nisan falls on Friday, the day is observed on Thursday (26 Nisan). If it falls on Sunday, it is observed on Monday (28 Nisan), to avoid disruption of Shabbat.

Customs by community:

In Israel: Two-minute siren sounds at 10:00 AM; most Israelis stop and stand in silence. Memorial ceremonies are held at Yad Vashem and throughout the country.

Prayers: Standard weekday davening — Yom HaShoah does not add or remove any siddur insertions. It is a Knesset civil memorial, not a rabbinically instituted liturgical day; Religious Zionist / Dati Leumi communities do not omit Tachanun specifically because of Yom HaShoah. (27 Nisan falls in Nisan — many Ashkenazim omit Tachanun throughout Nisan per Shulchan Arukh O.C. 429:2 anyway; that is a separate rule of the joyous month, not this observance.) Some communities hold memorial learning or ceremonies.

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
        explanation = """Yom HaZikaron (4 Iyar) is Israel's national day of remembrance for soldiers of the Israel Defense Forces and victims of terrorism who gave their lives for the State of Israel. It was established by the Knesset in 1963 and always falls the day before Yom Ha'atzmaut.

This is not a Yom Tov — melacha is fully permitted. It is a national civil observance.

Date adjustment: KosherJava adjusts the date when 4 Iyar falls near Shabbat, to keep Yom HaZikaron and Yom Ha'atzmaut together on consecutive days and not adjacent to Shabbat.

In Israel: Memorial sirens sound at 8:00 PM (start of the day, at nightfall) and again at 11:00 AM the following morning. Ceremonies are held at military cemeteries across the country. Flags fly at half-mast.

Prayers (Religious Zionist / Dati Leumi): Tachanun is recited fully at Shacharit — the day is one of solemn national memory. Tachanun is omitted only at Mincha as the calendar transitions into Yom Ha'atzmaut celebrations (Peninei Halakha 5:4:11; Koren Yom Ha'atzmaut mahzor — Mincha ketana before nightfall). Communities that do not treat Yom Ha'atzmaut as a religious day say Tachanun at Mincha too.

Most Charedi and Chabad communities: Do not observe as a religious day; regular weekday davening with Tachanun throughout the day.

The day ends at nightfall with the transition into Yom Ha'atzmaut celebrations.""",
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
        explanation = """Yom Ha'atzmaut (5 Iyar) commemorates Israeli independence in 1948. It is not a Biblical or Rabbinic Yom Tov — melacha (work) is fully permitted.

Customs vary significantly by community:

Religious Zionist / Modern Orthodox: Hallel is recited (instituted by the Chief Rabbinate of Israel). Whether Hallel is said with a bracha is disputed — the Chief Rabbinate instructed with a bracha; many Ashkenazi poskim outside Israel say without a bracha. Tachanun is omitted. Some communities add special festive prayers (Hallel u'Maariv).

Sephardic (Rav Ovadia Yosef / Yalkut Yosef): Rav Ovadia Yosef ruled that Hallel should not be recited (concern of bracha levatala since the day was not established by Chazal). Tachanun omission is also disputed in these communities.

Chabad: The Rebbe did not institute any special observance. Most Chabad communities do not say Hallel and recite Tachanun as usual.

Charedi communities (Agudah, Litvish): Generally do not observe the day as a religious holiday. Tachanun is said as usual.

The Omer continues to be counted normally. There is no Al HaNissim addition to davening.

Ask your rav which custom your community follows.""",
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
        explanation = """Yom Yerushalayim (28 Iyar) marks the reunification of Jerusalem during the Six-Day War in 1967 (5727). It is not a Yom Tov — melacha is fully permitted.

Customs vary by community:

Religious Zionist / Dati Leumi: Hallel is recited (with or without a bracha, depending on the posek and community). Tachanun is omitted. Some communities recite special tefillot.

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
                    "https://www.chabad.org/holidays/purim/article_cdo/aid/1362/jewish/The-Megillah.htm",
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
                    "https://www.chabad.org/library/article_cdo/aid/1363/jewish/Gifts-to-the-Poor.htm",
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
                    "https://www.chabad.org/library/article_cdo/aid/1364/jewish/Food-Gifts.htm",
                    "chabad"
                )
            )
        }
        add(ChecklistLink("Peninei Halacha — Mishloach manot", "https://ph.yhb.org.il/en/category/zemanim/05-15/", "default"))
        add(ChecklistLink("Aish — Purim mitzvot", "https://aish.com/holidays/purim/", "default"))
    }

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
            required = false,
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
                    "https://www.chabad.org/library/article_cdo/aid/5280/jewish/Drinking-on-Chol-Hamoed.htm",
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
        ChecklistLink("Chabad — The Three Weeks", "https://www.chabad.org/library/article_cdo/aid/144568/jewish/The-Three-Weeks.htm", "chabad"),
        ChecklistLink("Aish — Nine Days overview", "https://aish.com/48943916/", "default"),
        ChecklistLink("Peninei Halacha — Three Weeks and Nine Days", "https://ph.yhb.org.il/en/category/zemanim/05-08/", "default")
    )

    private fun selichotLinks(profile: UserProfile) = listOf(
        ChecklistLink("Chabad — Selichot", "https://www.chabad.org/library/article_cdo/aid/4744/jewish/Selichot.htm", "chabad"),
        ChecklistLink("Aish — Selichot overview", "https://aish.com/slichot_and_the_13_attributes/", "default"),
        ChecklistLink("Peninei Halacha — Selichot customs", "https://ph.yhb.org.il/en/category/15/15-01/", "default")
    )

    private fun tuBshvatSederItem(profile: UserProfile) = ChecklistItemDef(
        id = "tu_bshvat_seder_optional",
        title = "Tu B'Shvat Seder (optional)",
        section = "Seasonal",
        timeOfDay = TimeOfDay.NIGHT,
        required = false,
        situational = false,
        explanation = SeasonalMitzvahText.tuBshvatExplanation(),
        links = SeasonalMitzvahText.tuBshvatLinks()
    )

    private fun isKiddushLevanaWindow(cal: DayInfo, profile: UserProfile): Boolean {
        val day = cal.hebrewDay ?: return false
        if (day > 15) return false
        val minDay = when (profile.effectiveNusach()) {
            EffectiveNusach.SEFARD -> 7
            else -> 3
        }
        return day >= minDay
    }

    private fun kiddushLevanaItem(profile: UserProfile) = ChecklistItemDef(
        id = "kiddush_levana",
        title = "Kiddush Levana — Sanctification of the Moon (once per Hebrew month)",
        section = "Seasonal",
        timeOfDay = TimeOfDay.NIGHT,
        required = true,
        situational = false,
        monthlyMitzvah = true,
        explanation = SeasonalMitzvahText.kiddushLevanaExplanation(profile),
        links = SeasonalMitzvahText.kiddushLevanaLinks(profile)
    )

    private fun defaultLinks(profile: UserProfile) =
        listOf(ChecklistLink("Learn more", ChecklistLoader.defaultUrl(profile), null))
}
