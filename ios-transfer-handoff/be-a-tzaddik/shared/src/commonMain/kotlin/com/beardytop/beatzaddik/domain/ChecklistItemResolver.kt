package com.beardytop.beatzaddik.domain

object ChecklistItemResolver {

    fun resolve(
        item: ChecklistItemDef,
        profile: UserProfile,
        checked: Boolean,
        nowMillis: Long,
        zmanim: ZmanimSnapshot?,
        prayerDay: PrayerDayContext,
        upcomingShabbatParsha: String? = null
    ): ResolvedChecklistItem {
        val nusach = profile.effectiveNusach()
        val nusachTag = item.nusachTag ?: nusach.displayName()
        val titleSuffix = if (item.nusachOnly) " · $nusachTag" else ""

        // For weekly mitzvot (Shnayim Mikra), append the parsha name to the title.
        val parshaInfo = if (item.weeklyMitzvah) ParshaData.forKey(upcomingShabbatParsha) else null
        val parshaTitle = parshaInfo?.let { " — Parshat ${it.displayName}" } ?: ""

        val explanation = pickExplanation(item, profile).ifBlank {
            if (item.id.startsWith("custom_")) {
                "This is a personal goal or reminder you added. Check it off when you've done it."
            } else {
                "Confirm details with your rabbi."
            }
        }
        // Prepend parsha name to the explanation for weekly items.
        val displayExplanation = if (parshaInfo != null) {
            "This week's parsha: Parshat ${parshaInfo.displayName}\n\n$explanation"
        } else explanation

        val zman = when {
            ChecklistZmanEvaluator.appliesTo(item.id) ->
                ChecklistZmanEvaluator.evaluate(item.id, nowMillis, zmanim, prayerDay)
            isFestivalPrepItem(item) -> ItemZmanStatus()
            !item.persistChecked && item.timeOfDay != TimeOfDay.ANY ->
                timeOfDayAvailability(item.timeOfDay, nowMillis, zmanim)
            else -> ItemZmanStatus()
        }

        val optionalSuffix = ChecklistGenderRules.optionalTitleSuffix(item, profile)
        val baseTitle = if (profile.gender == Gender.FEMALE) {
            ChecklistGenderRules.displayTitleForWomen(item) ?: item.title
        } else {
            item.title
        }

        return ResolvedChecklistItem(
            def = item,
            checked = checked,
            displayTitle = baseTitle + optionalSuffix + titleSuffix + parshaTitle,
            displayExplanation = displayExplanation,
            sectionLabel = sectionWithNusach(item.section, item, profile),
            zmanAvailability = zman.availability,
            zmanHint = zman.hint,
            zmanMakeupNote = zman.makeupNote,
            zmanWindowStartMillis = zman.windowStartMillis,
            zmanWindowEndMillis = zman.windowEndMillis,
            zmanAvailableAtLabel = zman.availableAtLabel
        )
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
        }

    private fun joinExplanation(base: String, nusachSpecific: String): String {
        if (nusachSpecific.isBlank()) return base
        if (base.isBlank()) return nusachSpecific
        return "$base\n\n$nusachSpecific"
    }

    /** Festival prep items stay active for the whole civil day they appear (including afternoon/evening). */
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
    )

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
