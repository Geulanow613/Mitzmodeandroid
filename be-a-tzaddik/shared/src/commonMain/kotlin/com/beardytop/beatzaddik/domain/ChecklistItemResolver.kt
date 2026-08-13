package com.beardytop.beatzaddik.domain

object ChecklistItemResolver {

    fun resolve(
        item: ChecklistItemDef,
        profile: UserProfile,
        checked: Boolean,
        nowMillis: Long,
        zmanim: ZmanimSnapshot?,
        prayerDay: PrayerDayContext,
        upcomingShabbatParsha: String? = null,
        cal: DayInfo? = null,
        yesterdayCal: DayInfo? = null,
        tomorrowCal: DayInfo? = null,
    ): ResolvedChecklistItem {
        val nusach = profile.effectiveNusach()
        val nusachTag = item.nusachTag ?: nusach.displayName()
        val titleSuffix = if (item.nusachOnly) " · $nusachTag" else ""

        // For weekly mitzvot (Shnayim Mikra), append the parsha name to the title.
        val parshaInfo = if (item.weeklyMitzvah) ParshaData.forKey(upcomingShabbatParsha) else null
        val parshaTitle = parshaInfo?.let { " — Parshat ${it.displayName}" } ?: ""

        val havdalahKind = if (
            item.id == HavdalahRules.CHECKLIST_ITEM_ID &&
            cal != null && yesterdayCal != null && tomorrowCal != null
        ) {
            HavdalahRules.kind(cal, yesterdayCal, tomorrowCal, nowMillis)
        } else {
            null
        }

        val explanation = when {
            havdalahKind != null && cal != null && yesterdayCal != null ->
                HavdalahRules.explanationTemplate(
                    havdalahKind,
                    HavdalahRules.yomKippurWasShabbat(cal, yesterdayCal),
                )
            else -> pickExplanation(item, profile).ifBlank {
                if (item.id.startsWith("custom_")) {
                    "This is a personal goal or reminder you added. Check it off when you've done it."
                } else {
                    "Confirm details with your rabbi."
                }
            }
        }
        // Prepend parsha name to the explanation for weekly items.
        val displayExplanation = if (parshaInfo != null) {
            "This week's parsha: Parshat ${parshaInfo.displayName}\n\n$explanation"
        } else explanation

        // Zman evaluator must run before festival-prep / section bypasses — otherwise
        // deadline items in "Pesach prep" (e.g. mechirat 5th-hour) stay ACTIVE forever.
        val zman = when {
            ChecklistZmanEvaluator.appliesTo(item.id) ->
                ChecklistZmanEvaluator.evaluate(
                    item.id, nowMillis, zmanim, prayerDay,
                    cal = cal,
                    yesterdayCal = yesterdayCal,
                    tomorrowCal = tomorrowCal,
                )
            isFestivalPrepItem(item) -> ItemZmanStatus()

            isMourningPeriodItem(item) -> ItemZmanStatus()
            isCholHamoedItem(item) -> ItemZmanStatus()
            !item.persistChecked && item.timeOfDay != TimeOfDay.ANY ->
                when (item.id) {
                    in daylightTorahStudyIds -> daylightUntilSunsetAvailability(nowMillis, zmanim)
                    else -> timeOfDayAvailability(item.timeOfDay, nowMillis, zmanim)
                }
            else -> ItemZmanStatus()
        }

        val optionalSuffix = ChecklistGenderRules.optionalTitleSuffix(item, profile)
        val catalogTitle = if (profile.gender == Gender.FEMALE) {
            ChecklistGenderRules.displayTitleForWomen(item) ?: item.title
        } else {
            item.title
        }
        val baseTitle = havdalahKind?.let { HavdalahRules.title(it) } ?: catalogTitle
        val section = havdalahKind?.let { HavdalahRules.section(it) } ?: item.section

        val (explanationTemplate, explanationArgs) = ExplainerTemplateResolver
            .resolve(item, profile, cal, yesterdayCal, tomorrowCal, nowMillis)
            .let { it.template to it.args }

        return ResolvedChecklistItem(
            def = item,
            checked = checked,
            displayTitle = baseTitle + optionalSuffix + titleSuffix + parshaTitle,
            titleTranslationKey = baseTitle + optionalSuffix,
            displayExplanation = displayExplanation,
            sectionLabel = sectionWithNusach(section, item, profile),
            zmanAvailability = zman.availability,
            zmanHint = zman.hint,
            zmanHintTemplate = zman.hintTemplate,
            zmanHintArgs = zman.hintArgs,
            zmanMakeupNote = zman.makeupNote,
            zmanMakeupTemplate = zman.makeupTemplate,
            zmanMakeupArgs = zman.makeupArgs,
            zmanCollapsedTemplate = zman.collapsedSummaryTemplate,
            zmanCollapsedArgs = zman.collapsedSummaryArgs,
            zmanWindowStartMillis = zman.windowStartMillis,
            zmanWindowEndMillis = zman.windowEndMillis,
            zmanAvailableAtLabel = zman.availableAtLabel,
            explanationTemplate = explanationTemplate,
            explanationArgs = explanationArgs,
            resourceLinks = resourceLinksFor(item, profile),
        )
    }

    private fun resourceLinksFor(item: ChecklistItemDef, profile: UserProfile): List<ChecklistLink> {
        if (item.links.isEmpty()) return emptyList()
        val key = when (profile.effectiveNusach()) {
            EffectiveNusach.CHABAD -> "chabad"
            EffectiveNusach.SEFARD, EffectiveNusach.EDOT_HAMIZRACH -> "sefard"
            EffectiveNusach.ASHKENAZ -> "ashkenaz"
            EffectiveNusach.OTHER -> null
        }
        return item.links.filter { link ->
            link.nusach == null || link.nusach == "default" || (key != null && link.nusach == key)
        }
    }

    private fun pickExplanation(item: ChecklistItemDef, profile: UserProfile): String {
        val nusachSpecific = nusachExplanation(item, profile)
        if (ChecklistGenderRules.usesMaleExplanationForWomen(item, profile)) {
            return joinExplanation(item.explanation, nusachSpecific)
        }
        if (profile.gender == Gender.FEMALE && item.explanationFemale.isNotBlank()) {
            return joinExplanation(item.explanationFemale, nusachSpecific)
        }
        val base = item.explanation
        return joinExplanation(base, nusachSpecific).ifBlank {
            if (nusachSpecific.isNotBlank()) nusachSpecific else ""
        }
    }

    private fun nusachExplanation(item: ChecklistItemDef, profile: UserProfile): String =
        when (profile.effectiveNusach()) {
            EffectiveNusach.ASHKENAZ -> item.explanationAshkenaz
            EffectiveNusach.SEFARD -> item.explanationSefard
            EffectiveNusach.EDOT_HAMIZRACH -> item.explanationEdotHamizrach.ifBlank {
                item.explanationSefard
            }
            EffectiveNusach.CHABAD -> item.explanationChabad
            // Show both Ashkenaz and Sephardi notes when they differ (e.g. Purim Shehecheyanu).
            EffectiveNusach.OTHER -> otherNusachExplanation(item)
        }

    private fun otherNusachExplanation(item: ChecklistItemDef): String {
        val ash = item.explanationAshkenaz.trim()
        val sef = item.explanationSefard.trim()
        return when {
            ash.isNotBlank() && sef.isNotBlank() && ash != sef ->
                "$ash\n\n$sef\n\nYour nusach is Other — follow your kehilla for which practice applies."
            ash.isNotBlank() -> ash
            sef.isNotBlank() -> sef
            else -> item.explanationChabad.trim()
        }
    }

    private fun joinExplanation(base: String, nusachSpecific: String): String {
        if (nusachSpecific.isBlank()) return base
        if (base.isBlank()) return nusachSpecific
        return "$base\n\n$nusachSpecific"
    }

    /** Mourning customs apply day and night for the whole period — not tied to prayer zmanim. */
    private fun isMourningPeriodItem(item: ChecklistItemDef): Boolean =
        item.id in MourningPeriodRules.mourningChecklistItemIds

    /**
     * Chol HaMoed / Hoshana Rabbah observances that are not in [ChecklistZmanEvaluator]
     * stay active past Mincha Gedola. Timed mitzvot (arba minim, aravot) must be in
     * appliesTo so they get a real sunset/chatzos end instead of this bypass.
     */
    private fun isCholHamoedItem(item: ChecklistItemDef): Boolean =
        item.section == "Chol HaMoed" ||
            item.section == "Hoshana Rabbah" ||
            item.id.startsWith("chol_hamoed_")

    /**
     * Soft festival-prep rows without a hard zman stay active for the civil day.
     * Deadline rows (mechirat, seder prep, erev meals, eruv, etc.) must be in
     * [ChecklistZmanEvaluator.appliesTo] so they expire correctly.
     */
    private fun isFestivalPrepItem(item: ChecklistItemDef): Boolean {
        if (item.section in setOf("Prepare for the festival", "Pesach prep")) return true
        if (item.id.startsWith("prepare_for_festival_")) return true
        return item.id in festivalPrepItemIds
    }

    private val festivalPrepItemIds = setOf(
        "erev_chag_prep",
        "yom_tov_shabbat_advance_prep",
        "purim_meshulash_advance_prep",
        "erev_purim_prep",
        "erev_chanukah_prep",
        "erev_public_fast_prep",
        "erev_yom_kippur_eat",
        "erev_tisha_beav_prep",
        "eruv_tavshilin",
    )

    /** Daytime Torah study stays active until sunset, not only until Mincha Gedola. */
    private val daylightTorahStudyIds = setOf("torah_study_day")

    private fun daylightUntilSunsetAvailability(
        nowMillis: Long,
        zmanim: ZmanimSnapshot?
    ): ItemZmanStatus {
        val z = zmanim ?: return ItemZmanStatus()
        if (!z.hasLocationTimes) return ItemZmanStatus()
        val start = z.alotHaShacharMillis ?: z.sunriseMillis ?: return ItemZmanStatus()
        val end = z.sunsetMillis ?: return ItemZmanStatus()
        return when {
            nowMillis < start -> ItemZmanStatus(
                availability = ItemZmanAvailability.UPCOMING,
                windowStartMillis = start,
                availableAtLabel = "dawn"
            )
            nowMillis >= end -> ItemZmanStatus(
                availability = ItemZmanAvailability.EXPIRED
            )
            else -> ItemZmanStatus()
        }
    }

    /**
     * For non-prayer items, derive availability from [timeOfDay] and zmanim so that
     * daily items are locked (EXPIRED) once their window closes and show as dimmed-but-readable.
     */
    private fun timeOfDayAvailability(
        timeOfDay: TimeOfDay,
        nowMillis: Long,
        zmanim: ZmanimSnapshot?
    ): ItemZmanStatus {
        val z = zmanim ?: return ItemZmanStatus()
        if (!z.hasLocationTimes) return ItemZmanStatus()
        return when (timeOfDay) {
            TimeOfDay.DAY -> {
                val start = z.alotHaShacharMillis ?: z.sunriseMillis ?: return ItemZmanStatus()
                val end = ZmanPeriodLogic.afternoonStartMillis(z) ?: z.sunsetMillis ?: return ItemZmanStatus()
                when {
                    nowMillis < start -> ItemZmanStatus(
                        availability = ItemZmanAvailability.UPCOMING,
                        windowStartMillis = start,
                        availableAtLabel = "dawn"
                    )
                    nowMillis >= end -> ItemZmanStatus(
                        availability = ItemZmanAvailability.EXPIRED
                    )
                    else -> ItemZmanStatus()
                }
            }
            TimeOfDay.AFTERNOON -> {
                val start = ZmanPeriodLogic.afternoonStartMillis(z) ?: return ItemZmanStatus()
                val end = z.sunsetMillis ?: return ItemZmanStatus()
                when {
                    nowMillis < start -> ItemZmanStatus(
                        availability = ItemZmanAvailability.UPCOMING,
                        windowStartMillis = start,
                        availableAtLabel = "Mincha Gedola"
                    )
                    nowMillis >= end -> ItemZmanStatus(
                        availability = ItemZmanAvailability.EXPIRED
                    )
                    else -> ItemZmanStatus()
                }
            }
            TimeOfDay.NIGHT -> {
                val start = ZmanPeriodLogic.effectiveEveningStart(nowMillis, z)
                    ?: return ItemZmanStatus()
                val end = ZmanPeriodLogic.effectiveEveningEnd(nowMillis, z)
                    ?: return ItemZmanStatus()
                when {
                    nowMillis < start -> ItemZmanStatus(
                        availability = ItemZmanAvailability.UPCOMING,
                        windowStartMillis = start,
                        availableAtLabel = "nightfall"
                    )
                    nowMillis >= end -> ItemZmanStatus(
                        availability = ItemZmanAvailability.EXPIRED
                    )
                    else -> ItemZmanStatus()
                }
            }
            else -> ItemZmanStatus()
        }
    }

    private fun sectionWithNusach(section: String, item: ChecklistItemDef, profile: UserProfile): String {
        if (item.nusachOnly) return normalizeSection(section)
        if (section.startsWith("Prayer")) {
            return "Prayer (${profile.effectiveNusach().displayName()})"
        }
        return normalizeSection(section)
    }

    /** Legacy JSON used "Shabbat" for anything mentioning Shabbat in the title. */
    private fun normalizeSection(section: String): String =
        if (section == "Shabbat") "Shabbat & Festivals" else section

    private fun EffectiveNusach.displayName() = displayLabel()
}
