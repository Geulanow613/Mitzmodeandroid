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
            "Morning Prayer (Shacharit)",
            "Ongoing mitzvot",
            "Torah Study",
            "Eating & Blessings",
            "Married women's mitzvot",
            "Daily Prayer",
            "Afternoon Prayer",
            "Evening Prayer",
            "Important Lifestyle Mitzvot",
            "Prepare for Shabbat",
            "Motzei Shabbat",
            "Chol HaMoed",
            "Mourning customs",
            "Seasonal",
            "Prepare for the festival",
            "Pesach prep",
            "Sefirat HaOmer",
            "Chanukah",
            "Purim",
            "My mitzvot"
        )

        private fun sectionIndex(section: String): Int {
            val base = section.substringBefore(" (")
            return sectionOrder.indexOf(base).takeIf { it >= 0 }
                ?: sectionOrder.indexOf(section).takeIf { it >= 0 }
                ?: 200
        }
    }

    private fun sortDefs(defs: List<ChecklistItemDef>): List<ChecklistItemDef> =
        defs.sortedWith(
            compareBy({ sectionIndex(it.section) }, { it.sortOrder }, { it.title })
        )
    fun resolve(
        profile: UserProfile,
        checkedById: Map<String, Boolean>,
        customItems: List<CustomChecklistItem>,
        customChecked: Map<String, Boolean>,
        monthlyCheckedMonths: Map<String, String> = emptyMap(),
        weeklyCheckedWeeks: Map<String, String> = emptyMap()
    ): DayChecklists {
        val now = Clock.System.now()
        val nowMillis = now.toEpochMilliseconds()
        val cal = calendar.dayInfoAt(nowMillis, profile)
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

        val seasonal = SeasonalChecklistItems.forDay(cal, profile, tomorrowCal, dayAfterTomorrowCal)
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
        val allDefs = if (hideChecklist) {
            emptyList()
        } else {
            sortDefs((catalog + seasonal + customDefs).filter {
                matches(it, profile, cal, nowMillis, tomorrowCal)
            })
        }

        val prayerDay = PrayerDayContext.from(cal, profile.effectiveNusach())
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
                else -> checkedById[def.id] == true
            }
            ChecklistItemResolver.resolve(
                def, profile, checked, nowMillis, cal.zmanim, prayerDay,
                upcomingShabbatParsha = cal.upcomingShabbatParsha
            )
        }

        return DayChecklists(
            activePeriod = cal.activeTimeOfDay,
            activePeriodLabel = cal.activePeriodLabel,
            activePeriodHint = cal.activePeriodHint,
            inactivePeriodHint = cal.inactivePeriodHint,
            items = resolveList(allDefs),
            inactiveItemCount = 0,
            inactiveItems = emptyList(),
            date = cal.date,
            header = CalendarHeader(
                civilDateLabel = cal.civilLabel,
                hebrewDateLabel = cal.hebrewLabel,
                parshaLabel = cal.parsha,
                statusChips = cal.statusChips,
                timeLabel = cal.activePeriodLabel
            ),
            nusachLabel = profile.effectiveNusach().let {
                when (it) {
                    EffectiveNusach.ASHKENAZ -> "Ashkenaz"
                    EffectiveNusach.SEFARD -> "Sefard"
                    EffectiveNusach.CHABAD -> "Nusach Ari / Chabad"
                }
            },
            holyDayPhoneNotice = HolyDayPhoneRules.phoneNotice(profile, cal, nowMillis)
        )
    }

    fun allItems(day: DayChecklists): List<ResolvedChecklistItem> =
        day.items + day.inactiveItems

    fun requiredProgress(day: DayChecklists): Pair<Int, Int> {
        val all = allItems(day)
        val required = all.filter {
            it.def.required &&
                !it.def.situational &&
                !it.def.persistChecked &&
                it.zmanAvailability == ItemZmanAvailability.ACTIVE
            // Note: monthly mitzvot (monthlyMitzvah = true) are included here when ACTIVE;
            // they are not persistChecked, so they naturally enter the progress count.
        }
        val done = required.count { it.checked }
        return done to required.size
    }

    fun allRequiredComplete(day: DayChecklists): Boolean {
        val (done, total) = requiredProgress(day)
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
        val eff = profile.effectiveNusach().name.lowercase()
        return allowed.any { tag ->
            tag.equals(eff, ignoreCase = true) ||
                (tag == "chabad" && eff == "chabad") ||
                (tag == "ashkenaz" && eff == "ashkenaz") ||
                (tag == "sefard" && eff == "sefard")
        }
    }

    private fun matchesSeason(item: ChecklistItemDef, cal: DayInfo): Boolean {
        val seasons = item.seasons ?: return true
        return seasons.any { cal.activeSeasons.contains(it) }
    }
}
