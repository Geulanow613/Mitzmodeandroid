package com.beardytop.beatzaddik.domain

/**
 * Resolves explainer bodies that use catalog template keys + placeholder args
 * so bundled translations work (plain [rememberAppTranslatedText] misses runtime interpolation).
 */
object ExplainerTemplateResolver {

    data class Bundle(
        val template: String? = null,
        val args: Map<String, String> = emptyMap(),
    )

    fun resolve(item: ChecklistItemDef, profile: UserProfile, cal: DayInfo?): Bundle {
        if (cal == null) return Bundle()
        val female = prefersFemaleExplanation(item, profile)
        val nusachNote = nusachNote(item, profile)

        return when {
            item.id.startsWith("sefirat_haomer_day_") ->
                Bundle(
                    OmerCountText.explanationTemplate(),
                    OmerCountText.explanationArgs(cal, profile),
                )

            item.id.startsWith("chanukah_lighting_day_") -> {
                val day = item.id.removePrefix("chanukah_lighting_day_").toIntOrNull() ?: return Bundle()
                Bundle(
                    SeasonalMitzvahText.chanukahLightingExplanationTemplate(),
                    SeasonalMitzvahText.chanukahLightingExplanationArgs(day),
                )
            }

            item.id == "birkat_hachamah" -> {
                val occurrence = BirkatHachamahRules.visibleOccurrence(cal.date) ?: return Bundle()
                Bundle(
                    SeasonalMitzvahText.birkatHachamahExplanationTemplate(),
                    SeasonalMitzvahText.birkatHachamahExplanationArgs(occurrence),
                )
            }

            item.id == "birkat_hailanot" ->
                Bundle(
                    SeasonalMitzvahText.birkatHaIlanotExplanationTemplate(),
                    SeasonalMitzvahText.birkatHaIlanotExplanationArgs(profile),
                )

            item.id == "kiddush_levana" ->
                Bundle(
                    SeasonalMitzvahText.kiddushLevanaExplanationTemplate(),
                    SeasonalMitzvahText.kiddushLevanaExplanationArgs(profile),
                )

            item.id == "three_weeks_mourning_customs" ->
                Bundle(template = SeasonalMitzvahText.threeWeeksExplanationTemplate(profile))

            item.id == "nine_days_mourning_customs" ->
                Bundle(template = SeasonalMitzvahText.nineDaysExplanationTemplate(profile))

            item.id == "sukkot_arba_minim" ->
                Bundle(
                    ExplainerTemplateSupport.arbaMinimTemplate(female),
                    ExplainerTemplateSupport.arbaMinimArgs(profile, female),
                )

            item.id == "hoshana_rabbah_aravot" ->
                staticBundle(SeasonalMitzvahText.hoshanaRabbahAravotExplanation())

            item.id == "chol_hamoed_honor" ->
                Bundle(
                    ExplainerTemplateSupport.cholHamoedHonorTemplate(),
                    ExplainerTemplateSupport.cholHamoedHonorArgs(cal, profile),
                )

            item.id == "chol_hamoed_wine_reviit" ->
                Bundle(
                    ExplainerTemplateSupport.cholHamoedWineTemplate(),
                    ExplainerTemplateSupport.cholHamoedWineArgs(cal),
                )

            item.id == "chol_hamoed_sukkah" ->
                Bundle(
                    ExplainerTemplateSupport.cholHamoedSukkahTemplate(
                        prefersFemaleExplanation(item, profile),
                    ),
                    ExplainerTemplateSupport.cholHamoedSukkahArgs(
                        profile,
                        prefersFemaleExplanation(item, profile),
                    ),
                )

            item.id == "chol_hamoed_nicer_clothes" ->
                staticBundle(SeasonalMitzvahText.cholHamoedClothesExplanation())

            item.id == "chol_hamoed_pesach_matzah" ->
                staticBundle(SeasonalMitzvahText.cholHamoedMatzahExplanation())

            item.id == "shemini_atzeret_focus" ->
                staticBundle(SeasonalMitzvahText.sheminiAtzeretExplanation(profile))

            item.id == "simchat_torah_focus" ->
                staticBundle(SeasonalMitzvahText.simchatTorahExplanation(profile))

            item.id == "prepare_for_festival_shavuot" ->
                Bundle(
                    ExplainerTemplateSupport.shavuotWeekPrepTemplate(),
                    ExplainerTemplateSupport.shavuotWeekPrepArgs(profile),
                )

            item.id == "prepare_for_festival_sukkot" ->
                Bundle(
                    ExplainerTemplateSupport.sukkotWeekPrepTemplate(),
                    ExplainerTemplateSupport.sukkotWeekPrepArgs(profile),
                )

            item.id == "prepare_for_festival_pesach" ->
                Bundle(
                    ExplainerTemplateSupport.pesachWeekPrepTemplate(),
                    ExplainerTemplateSupport.pesachWeekPrepArgs(cal, profile),
                )

            item.id == "yaaleh_vyavo_rosh_chodesh_shacharit" ->
                Bundle(
                    SeasonalMitzvahText.yaalehVyavoShacharitTemplate(female),
                    SeasonalMitzvahText.yaalehVyavoShacharitArgs(),
                )

            item.id == "yaaleh_vyavo_rosh_chodesh_mincha" ->
                Bundle(
                    SeasonalMitzvahText.yaalehVyavoMinchaTemplate(female),
                    SeasonalMitzvahText.yaalehVyavoMinchaArgs(),
                )

            item.id == "yaaleh_vyavo_rosh_chodesh" ->
                Bundle(
                    SeasonalMitzvahText.yaalehVyavoMaarivTemplate(female),
                    SeasonalMitzvahText.yaalehVyavoMaarivArgs(),
                )

            item.id == "rosh_chodesh_half_hallel" ->
                nusachBundle(
                    if (female) {
                        SeasonalMitzvahText.roshChodeshHalfHallelExplanationFemale()
                    } else {
                        SeasonalMitzvahText.roshChodeshHalfHallelExplanation()
                    },
                    nusachNote,
                )

            item.id == "rosh_chodesh_full_hallel_chanukah" ->
                nusachBundle(
                    if (female) {
                        SeasonalMitzvahText.roshChodeshFullHallelChanukahExplanationFemale()
                    } else {
                        SeasonalMitzvahText.roshChodeshFullHallelChanukahExplanation()
                    },
                    nusachNote,
                )

            item.id == "rosh_chodesh_observances" ->
                staticBundle(SeasonalMitzvahText.roshChodeshObservancesExplanation())

            item.id == "ldovid_hashem_ori" ->
                nusachBundle(SeasonalMitzvahText.ldovidExplanation(profile.effectiveNusach()), nusachNote)

            item.id == "selichot_elul_chabad" ||
                item.id == "selichot_elul_ashkenaz" ||
                item.id == "selichot_elul_sefard" ||
                item.id == "selichot_elul_edot_hamizrach" ->
                staticBundle(SeasonalMitzvahText.selichotExplanation(profile.effectiveNusach()))

            item.id == "tu_bshvat_seder_optional" ->
                staticBundle(SeasonalMitzvahText.tuBshvatExplanation())

            item.id == "motzei_yk_build_sukkah" ->
                staticBundle(SeasonalMitzvahText.sukkahBuildExplanation())

            item.id == "purim_meshulash_advance_prep" ->
                Bundle(
                    PurimMeshulashText.advancePrepTemplate(),
                    PurimMeshulashText.advancePrepArgs(),
                )

            item.id == "erev_purim_prep" -> {
                val tomorrow = ExplainerTemplateSupport.tomorrowCal(cal, profile)
                if (PurimMeshulashText.isErevBeforeMeshulashFriday(cal, tomorrow)) {
                    Bundle(
                        PurimMeshulashText.erevPrepTemplate(),
                        PurimMeshulashText.erevPrepArgs(),
                    )
                } else {
                    Bundle()
                }
            }

            item.id == "purim_meshulash_friday_megillah" ->
                staticBundle(PurimMeshulashText.fridayMegillahTemplate())

            item.id == "purim_meshulash_friday_matanot" ->
                staticBundle(PurimMeshulashText.fridayMatanotTemplate())

            item.id == "purim_meshulash_sunday_mishloach" ->
                staticBundle(PurimMeshulashText.sundayMishloachTemplate())

            item.id == "purim_meshulash_sunday_seudah" ->
                staticBundle(PurimMeshulashText.sundaySeudahTemplate())

            item.id == "erev_public_fast_prep" -> {
                val idx = cal.upcomingFastDayIndex ?: return Bundle()
                Bundle(
                    PublicFastDayText.erevMinorFastPrepTemplate(),
                    PublicFastDayText.erevMinorFastPrepArgs(cal, idx, profile),
                )
            }

            item.id == "erev_yom_kippur_eat" ->
                Bundle(
                    PublicFastDayText.erevYomKippurTemplate(),
                    PublicFastDayText.erevYomKippurArgs(cal, profile),
                )

            item.id == "erev_tisha_beav_prep" ->
                Bundle(
                    PublicFastDayText.erevTishaBeavTemplate(),
                    PublicFastDayText.erevTishaBeavArgs(cal, profile),
                )

            item.id == "public_fast_day" -> {
                val idx = cal.fastDayIndex ?: return Bundle()
                Bundle(
                    PublicFastDayText.publicFastDayTemplate(idx),
                    PublicFastDayText.publicFastDayArgs(idx, cal, profile),
                )
            }

            item.id == "motzei_yom_kippur_meal" ->
                Bundle(
                    PublicFastDayText.motzeiYomKippurMealTemplate(),
                    PublicFastDayText.motzeiYomKippurMealArgs(cal, profile),
                )

            item.id == "yom_tov_shabbat_advance_prep" -> {
                val tomorrow = ExplainerTemplateSupport.tomorrowCal(cal, profile)
                val block = YomTovShabbatPrepText.advanceBlock(cal, tomorrow, profile)
                if (block.isNullOrBlank()) Bundle() else staticBundle(block)
            }

            item.id == "erev_chag_prep" -> {
                val tomorrow = ExplainerTemplateSupport.tomorrowCal(cal, profile)
                val prep = ErevChagPrepText.build(cal, profile, tomorrow)
                staticBundle(prep.explanation)
            }

            item.id == "eruv_tavshilin" -> {
                val tomorrow = ExplainerTemplateSupport.tomorrowCal(cal, profile)
                staticBundle(YomTovShabbatPrepText.eruvTavshilinExplanation(cal, profile, tomorrow))
            }

            item.id == "erev_pesach_mechirat_chametz" ->
                Bundle(
                    ErevPesachExplainerTemplates.mechiratTemplate(),
                    ErevPesachExplainerTemplates.mechiratArgs(cal, profile),
                )

            item.id == "erev_pesach_prepare_seder" ->
                Bundle(
                    ErevPesachExplainerTemplates.sederPrepTemplate(),
                    ErevPesachExplainerTemplates.sederPrepArgs(cal, profile),
                )

            item.id == "bedikat_chametz" ->
                Bundle(
                    ErevPesachExplainerTemplates.bedikatTemplate(),
                    ErevPesachExplainerTemplates.bedikatArgs(cal, profile),
                )

            item.id == "erev_pesach_biur_chametz" ->
                Bundle(
                    ErevPesachExplainerTemplates.biurTemplate(),
                    ErevPesachExplainerTemplates.biurArgs(cal, profile),
                )

            item.id == "erev_pesach_taanit_bechorot" ->
                Bundle(
                    ErevPesachExplainerTemplates.taanitBechorTemplate(),
                    ErevPesachExplainerTemplates.taanitBechorArgs(cal, profile),
                )

            else -> Bundle()
        }
    }

    fun omerHeaderBundle(cal: DayInfo, profile: UserProfile): Bundle? {
        if (cal.omerDay == null || !cal.isSefiratHaomer || cal.isLagBaomer) return null
        return Bundle(
            OmerCountText.explanationTemplate(),
            OmerCountText.explanationArgs(cal, profile),
        )
    }

    private fun prefersFemaleExplanation(item: ChecklistItemDef, profile: UserProfile): Boolean =
        profile.gender == Gender.FEMALE &&
            item.explanationFemale.isNotBlank() &&
            !ChecklistGenderRules.usesMaleExplanationForWomen(item, profile)

    private fun nusachNote(item: ChecklistItemDef, profile: UserProfile): String =
        when (profile.effectiveNusach()) {
            EffectiveNusach.ASHKENAZ -> item.explanationAshkenaz
            EffectiveNusach.SEFARD -> item.explanationSefard
            EffectiveNusach.EDOT_HAMIZRACH -> item.explanationEdotHamizrach.ifBlank { item.explanationSefard }
            EffectiveNusach.CHABAD -> item.explanationChabad
        }

    private fun staticBundle(template: String): Bundle =
        if (template.isBlank()) Bundle() else Bundle(template = template)

    private fun nusachBundle(baseTemplate: String, nusachNote: String): Bundle {
        if (baseTemplate.isBlank()) return Bundle()
        val template = ExplainerTemplateSupport.withNusachNote(baseTemplate, nusachNote)
        return Bundle(template, ExplainerTemplateSupport.nusachNoteArgs(nusachNote))
    }
}
