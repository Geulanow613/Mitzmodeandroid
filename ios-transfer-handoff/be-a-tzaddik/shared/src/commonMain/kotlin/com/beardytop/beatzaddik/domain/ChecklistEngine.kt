package com.beardytop.beatzaddik.domain

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

class ChecklistEngine(
    private val calendar: JewishCalendarService,
    private val catalog: List<ChecklistItemDef>
) {
    companion object {
        val sectionOrder: List<String> = listOf(
            "Upon waking",
            "Selichot",
            "Morning Prayer (Shacharit)",
            "Ongoing mitzvot",
            "Torah Study",
            "Eating & Blessings",
            "Married women's mitzvot",
            "Daily Prayer",
            "Afternoon Prayer",
            "Evening Prayer",
            "Monthly",
            "Important Lifestyle Mitzvot",
            "Prepare for Shabbat",
            "Motzei Shabbat",
            "Hoshana Rabbah",
            "Chol HaMoed",
            "Mourning customs",
            "Seasonal",
            "Prepare for the festival",
            "Pesach prep",
            "Fasts",
            "Sefirat HaOmer",
            "Chanukah",
            "Purim",
            "My mitzvot"
        )

    }

    private fun sortDefs(
        defs: List<ChecklistItemDef>,
        activePeriod: TimeOfDay,
        prioritizePrepSections: Set<String> = emptySet(),
        sinkMorningPrayerSection: Boolean = false,
    ): List<ChecklistItemDef> =
        defs.sortedWith(
            compareBy(
                {
                    ChecklistSectionOrder.sortIndex(
                        it.section,
                        activePeriod,
                        prioritizePrepSections,
                        sinkMorningPrayerSection,
                    )
                },
                { it.sortOrder },
                { it.title },
            )
        )
    fun resolve(
        profile: UserProfile,
        checkedById: Map<String, Boolean>,
        customItems: List<CustomChecklistItem>,
        customChecked: Map<String, Boolean>,
        monthlyCheckedMonths: Map<String, String> = emptyMap(),
        weeklyCheckedWeeks: Map<String, String> = emptyMap(),
        tzeitCheckedDays: Map<String, String> = emptyMap(),
        nowMillis: Long = Clock.System.now().toEpochMilliseconds(),
        forcedActivePeriod: TimeOfDay? = null,
    ): DayChecklists {
        val cal = calendar.dayInfoAt(nowMillis, profile)
        val yesterdayCal = calendar.dayInfoForDate(cal.date.plus(-1, DateTimeUnit.DAY), profile)
        val tomorrowCal = calendar.dayInfoForDate(cal.date.plus(1, DateTimeUnit.DAY), profile)
        val dayAfterTomorrowCal = calendar.dayInfoForDate(cal.date.plus(2, DateTimeUnit.DAY), profile)
        // Hebrew month key — monthly items reset each new Hebrew month.
        val currentMonthKey = "${cal.hebrewYear}-${cal.hebrewMonth}"
        // Saturday date key — weekly items reset each Motzei Shabbat.
        // Key = the upcoming Saturday (or today if it is Saturday, though item is hidden on Shabbat).
        val currentWeekKey = run {
            val daysUntilSaturday = (DayOfWeek.SATURDAY.ordinal - cal.date.dayOfWeek.ordinal + 7) % 7
            cal.date.plus(daysUntilSaturday, DateTimeUnit.DAY).toString()
        }
        val currentTzeitDayKey = TzeitDay.currentKey(nowMillis, cal, yesterdayCal)

        val seasonal = SeasonalChecklistItems.forDay(
            cal, profile, tomorrowCal, dayAfterTomorrowCal, nowMillis,
        )
        val customDefs = customItems.map { custom ->
            ChecklistItemDef(
                id = custom.id,
                title = custom.title,
                section = "My mitzvot",
                timeOfDay = custom.timeOfDay,
                required = true,
                situational = false
            )
        }

        val hideChecklist = HolyDayPhoneRules.shouldHideChecklist(profile, cal, nowMillis)
        val prioritizePrepSections = ChecklistSectionOrder.prioritizedPrepSections(
            cal, tomorrowCal, profile, nowMillis,
        )
        val activePeriod = forcedActivePeriod ?: cal.activeTimeOfDay
        val sinkMorningPrayerSection = cal.zmanim?.let { z ->
            !ZmanPeriodLogic.isMorningPrayerSectionPriority(nowMillis, z)
        } ?: false
        val allDefs = if (hideChecklist) {
            emptyList()
        } else {
            sortDefs(
                (catalog + seasonal + customDefs).filter {
                    matches(it, profile, cal, nowMillis, tomorrowCal)
                },
                activePeriod,
                prioritizePrepSections,
                sinkMorningPrayerSection,
            )
        }

        val prayerDay = PrayerDayContext.from(
            cal,
            profile.effectiveNusach(),
            profile.latitude,
            profile.isInIsrael,
            tomorrowCal,
        )
        fun resolveList(defs: List<ChecklistItemDef>) = defs.map { def ->
            val checked = when {
                def.id.startsWith("custom_") -> customChecked[def.id] == true
                def.monthlyMitzvah -> {
                    checkedById[def.id] == true &&
                        monthlyCheckedMonths[def.id] == currentMonthKey
                }
                def.weeklyMitzvah -> {
                    checkedById[def.id] == true &&
                        weeklyCheckedWeeks[def.id] == currentWeekKey
                }
                def.tzeitMitzvah || def.id == TzeitDay.WOMENS_DAILY_PRAYER_ID -> {
                    checkedById[def.id] == true &&
                        currentTzeitDayKey != null &&
                        tzeitCheckedDays[def.id] == currentTzeitDayKey
                }
                else -> checkedById[def.id] == true
            }
            ChecklistItemResolver.resolve(
                def, profile, checked, nowMillis, cal.zmanim, prayerDay,
                upcomingShabbatParsha = WeeklyParshaLogic.displayParshaKey(
                    cal = cal,
                    tomorrowCal = tomorrowCal,
                    nowMillis = nowMillis,
                    isInIsrael = profile.isInIsrael,
                ),
                cal = cal,
            )
        }

        val occasion = TodayOccasionLabels.primary(cal, nowMillis, tomorrowCal)
        val omerLabel = TodayOccasionLabels.omerTodayLabel(cal, profile.effectiveNusach())
        val omerDay = cal.omerDay?.takeIf { cal.isSefiratHaomer && !cal.isLagBaomer }
        val motzeiShabbatActive = MotzeiShabbatWindow.isActive(cal, tomorrowCal, nowMillis)

        val omerBundle = ExplainerTemplateResolver.omerHeaderBundle(cal, profile)
        val periodLabels = if (forcedActivePeriod != null) {
            cal.zmanim?.let {
                ZmanPeriodLabels.forPeriod(forcedActivePeriod, it, profile.effectiveNusach())
            } ?: ZmanPeriodLabels.forPeriodWithoutZmanim(forcedActivePeriod)
        } else {
            ZmanPeriodLabels.Labels(
                activeTitle = cal.activePeriodLabel,
                activeSummary = cal.activePeriodHint,
                laterSummary = cal.inactivePeriodHint,
            )
        }
        return DayChecklists(
            activePeriod = activePeriod,
            activePeriodLabel = periodLabels.activeTitle,
            activePeriodHint = if (profile.hasZmanimLocation()) periodLabels.activeSummary else null,
            inactivePeriodHint = if (profile.hasZmanimLocation()) periodLabels.laterSummary else null,
            items = resolveList(allDefs).filter { item ->
                (item.def.id != "rosh_chodesh_observances" ||
                    item.zmanAvailability != ItemZmanAvailability.EXPIRED) &&
                    (item.def.id != "public_fast_day" ||
                        !PublicFastDayRules.shouldHideEndedFastItem(
                            nowMillis = nowMillis,
                            cal = cal,
                            tomorrowCal = tomorrowCal,
                            availability = item.zmanAvailability,
                        ))
            },
            inactiveItemCount = 0,
            inactiveItems = emptyList(),
            date = cal.date,
            header = CalendarHeader(
                civilDateLabel = cal.civilLabel,
                hebrewDateLabel = cal.hebrewLabel,
                parshaLabel = WeeklyParshaLogic.displayParshaLabel(
                    cal = cal,
                    tomorrowCal = tomorrowCal,
                    nowMillis = nowMillis,
                    isInIsrael = profile.isInIsrael,
                ),
                statusChips = cal.statusChips.filterNot { chip ->
                    chip.startsWith("Omer day") ||
                        (chip.startsWith("Today is") && chip.contains("Omer", ignoreCase = true))
                }.let { chips ->
                    if (!motzeiShabbatActive) chips
                    else chips.filterNot { it == "Shabbat" }.let { filtered ->
                        if ("Motzei Shabbat" in filtered) filtered else filtered + "Motzei Shabbat"
                    }
                },
                timeLabel = if (profile.hasZmanimLocation()) periodLabels.activeTitle else null,
                todayOccasionLabel = occasion?.label,
                todayOccasionTemplate = occasion?.labelTemplate,
                todayOccasionTemplateArgs = occasion?.labelArgs ?: emptyMap(),
                todayOccasionSubtitle = occasion?.subtitle,
                todayOccasionGuideAnchor = occasion?.guideAnchor,
                omerTodayLabel = omerLabel,
                omerDay = omerDay,
                omerExplainerText = omerBundle?.let {
                    OmerCountText.buildExplanation(cal, profile)
                },
                omerExplainerTemplate = omerBundle?.template,
                omerExplainerArgs = omerBundle?.args ?: emptyMap(),
            ),
            nusachLabel = profile.effectiveNusach().displayLabel(),
            holyDayPhoneNotice = HolyDayPhoneRules.phoneNotice(profile, cal, nowMillis),
            prioritizePrepSections = prioritizePrepSections,
            sinkMorningPrayerSection = sinkMorningPrayerSection,
        )
    }

    fun allItems(day: DayChecklists): List<ResolvedChecklistItem> =
        day.items + day.inactiveItems

    fun requiredProgress(day: DayChecklists, profile: UserProfile): Pair<Int, Int> {
        val all = allItems(day)
        val required = all.filter {
            ChecklistGenderRules.isEffectivelyRequired(it.def, profile) &&
                !it.def.situational &&
                !it.def.persistChecked &&
                it.zmanAvailability == ItemZmanAvailability.ACTIVE
            // Note: monthly mitzvot (monthlyMitzvah = true) are included here when ACTIVE;
            // they are not persistChecked, so they naturally enter the progress count.
        }
        val done = required.count { it.checked }
        return done to required.size
    }

    fun allRequiredComplete(day: DayChecklists, profile: UserProfile): Boolean {
        val (done, total) = requiredProgress(day, profile)
        return total > 0 && done == total
    }

    private fun matches(
        item: ChecklistItemDef,
        profile: UserProfile,
        cal: DayInfo,
        nowMillis: Long,
        tomorrowCal: DayInfo,
    ): Boolean {
        if (item.gender == "male" && !profile.gender.usesMaleChecklistItems()) return false
        if (item.gender == "female" && profile.gender != Gender.FEMALE) return false
        if (item.married == true && !profile.married) return false
        if (item.hasChildren == true && !profile.hasChildren) return false
        if (!matchesShabbatAppUse(item, cal)) return false
        if (item.hideOnShabbat && cal.isShabbatOrYomTov) return false
        if (item.shabbatEveOnly && !cal.isErevShabbat) return false
        if (item.motzeiShabbatOnly &&
            !MotzeiShabbatWindow.isActive(cal, tomorrowCal, nowMillis)
        ) {
            return false
        }
        if (item.id == "musaf_only_on_rosh_chodesh_festivals_and_shabbat") {
            if (!cal.isShabbat && !cal.isYomTov && !cal.isRoshChodesh) return false
        } else if (item.id == "rosh_chodesh_half_hallel") {
            if (RoshChodeshRules.hallelKind(cal) != RoshChodeshRules.HallelKind.HALF) return false
        } else if (item.id == "rosh_chodesh_full_hallel_chanukah") {
            if (RoshChodeshRules.hallelKind(cal) != RoshChodeshRules.HallelKind.FULL_DURING_CHANUKAH) {
                return false
            }
        } else if (TachanunRules.isTachanunOnlyItem(item.id) &&
            !TachanunRules.isRecited(cal, profile, item.id, tomorrowCal)
        ) {
            return false
        } else if (item.shabbatOnly && !cal.isShabbatOrYomTov && !cal.isErevShabbat) {
            return false
        }
        if (!matchesNusach(item, profile)) return false
        if (!matchesSeason(item, cal)) return false
        return true
    }

    private val shabbatRelatedSections = setOf("Prepare for Shabbat", "Shabbat & Festivals")

    /**
     * Shabbat mitzvot are not tracked on the phone during Shabbat.
     * Erev Shabbat (Friday): preparation items only.
     */
    private fun matchesShabbatAppUse(item: ChecklistItemDef, cal: DayInfo): Boolean {
        if (item.motzeiShabbatOnly) return true
        if (item.section !in shabbatRelatedSections) return true
        if (cal.isShabbat && !cal.isErevShabbat) return false
        if (cal.isErevShabbat && !cal.isShabbat) {
            return item.shabbatEveOnly ||
                item.id == "prepare_for_and_observe_shabbat_and_festivals"
        }
        return true
    }

    private fun matchesNusach(item: ChecklistItemDef, profile: UserProfile): Boolean {
        val allowed = item.nusach ?: return true
        val eff = profile.effectiveNusach().jsonTag()
        return allowed.any { tag -> tag.equals(eff, ignoreCase = true) }
    }

    private fun matchesSeason(item: ChecklistItemDef, cal: DayInfo): Boolean {
        val seasons = item.seasons ?: return true
        return seasons.any { cal.activeSeasons.contains(it) }
    }
}
