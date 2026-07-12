package com.beardytop.beatzaddik.domain

/**
 * Reorders checklist sections by time of day so the current prayer block appears first.
 */
object ChecklistSectionOrder {

    fun baseName(section: String): String {
        val trimmed = section.trim()
        ChecklistEngine.sectionOrder.firstOrNull { orderName ->
            trimmed == orderName || trimmed.startsWith("$orderName (")
        }?.let { return it }
        return trimmed.substringBefore(" (")
    }

    private val prepSectionSortPriority = mapOf(
        "Motzei Yom Kippur" to -70,
        "Motzei Shabbat" to -60,
        "Hoshana Rabbah" to -65,
        "Chol HaMoed" to -55,
        // High so Zecher Machatzit / Birkat Hachamah pin near the top of Today.
        "Seasonal" to -85,
        "Pesach prep" to -40,
        "Prepare for the festival" to -30,
        "Fasts" to -45,
        "Sefirat HaOmer" to -48,
        "Chanukah" to -25,
    )

    fun prioritizedPrepSections(
        cal: DayInfo,
        tomorrowCal: DayInfo,
        profile: UserProfile,
        nowMillis: Long,
    ): Set<String> = buildSet {
        if (BirkatHachamahRules.visibleOccurrence(cal.date) != null ||
            SeasonalChecklistItems.shouldShowZecherMachatzitHaShekel(cal)
        ) {
            add("Seasonal")
        }
        if (ErevPesachPrepText.isPesachPrepWindow(cal)) add("Pesach prep")
        if (SeasonalChecklistItems.isNightAfterYomKippur(cal)) add("Motzei Yom Kippur")
        if (
            "erev_chag" in cal.activeSeasons ||
            cal.festivalWeekPrep() != null ||
            YomTovShabbatPrepText.shouldShowAdvancePrepDay(cal, tomorrowCal, profile)
        ) {
            add("Prepare for the festival")
        }
        if (
            "erev_purim" in cal.activeSeasons ||
            "purim_meshulash_friday" in cal.activeSeasons ||
            "purim_meshulash_shabbat" in cal.activeSeasons ||
            "purim_meshulash_sunday" in cal.activeSeasons
        ) {
            add("Purim")
        }
        // Chanukah: during afternoon/night (until alot), promote the Chanukah section so
        // the lighting mitzvah is easy to find. In the morning, keep normal ordering/collapse.
        if (cal.isChanukah && cal.activeTimeOfDay != TimeOfDay.DAY) add("Chanukah")
        else if ("erev_chanukah" in cal.activeSeasons) add("Chanukah")
        if ("fast_day" in cal.activeSeasons ||
            "erev_minor_fast" in cal.activeSeasons || "erev_yom_kippur" in cal.activeSeasons ||
            "erev_tisha_beav" in cal.activeSeasons
        ) {
            add("Fasts")
        }
        if (MotzeiShabbatWindow.isActive(cal, tomorrowCal, nowMillis)) {
            add("Motzei Shabbat")
        }
        if ("chol_hamoed_pesach" in cal.activeSeasons || "chol_hamoed_sukkot" in cal.activeSeasons) {
            add("Chol HaMoed")
        }
        if (HebrewCalendarEngine.isHoshanaRabbah(cal.hebrewMonth, cal.hebrewDay)) {
            add("Hoshana Rabbah")
        }
        if (OmerCountText.isOmerCountPriority(nowMillis, cal)) {
            add("Sefirat HaOmer")
        }
        // Erev Shabbat: after midday, surface Shabbat prep near the top.
        if (cal.isErevShabbat && cal.activeTimeOfDay != TimeOfDay.DAY) {
            add("Prepare for Shabbat")
        }
    }

    /**
     * Lower index = higher on the Today checklist.
     *
     * - **Day:** Upon waking, then Morning Prayer, then everything else.
     * - **Afternoon:** Mincha, then Upon waking, then rest; Morning Prayer sinks after chatzos.
     * - **Night:** Evening Prayer, then Upon waking, then Monthly, then rest; past prayer sections sink.
     */
    fun sortIndex(
        section: String,
        activePeriod: TimeOfDay,
        prioritizePrepSections: Set<String> = emptySet(),
        sinkMorningPrayerSection: Boolean = false,
    ): Int {
        val base = baseName(section)
        if (base in prioritizePrepSections) {
            // Chanukah should sit right under the current prayer block (not above it).
            if (base == "Chanukah") {
                return when (activePeriod) {
                    TimeOfDay.AFTERNOON -> 1 // right below Mincha section
                    TimeOfDay.NIGHT -> 1 // right below Maariv section
                    else -> prepSectionSortPriority[base] ?: -20
                }
            }
            // Erev Shabbat: keep prep directly under the current prayer block.
            if (base == "Prepare for Shabbat") {
                return when (activePeriod) {
                    TimeOfDay.AFTERNOON -> -1 // very top on Friday afternoon
                    TimeOfDay.NIGHT -> 1 // right below Maariv section
                    else -> prepSectionSortPriority[base] ?: -20
                }
            }
            if (base == "Sefirat HaOmer") {
                return when (activePeriod) {
                    TimeOfDay.NIGHT -> 1 // right below Maariv section
                    else -> prepSectionSortPriority[base] ?: -20
                }
            }
            return prepSectionSortPriority[base] ?: -20
        }
        val defaultIndex = ChecklistEngine.sectionOrder.indexOf(base).takeIf { it >= 0 } ?: 200
        val rest = 1000 + defaultIndex

        if (sinkMorningPrayerSection) {
            when (base) {
                "Morning Prayer (Shacharit)" -> return BOTTOM_SECTION_BASE + 1
                "Afternoon Prayer" -> return when (activePeriod) {
                    TimeOfDay.AFTERNOON -> 0
                    TimeOfDay.NIGHT -> BOTTOM_SECTION_BASE + 2
                    else -> BOTTOM_SECTION_BASE
                }
            }
        }

        return when (activePeriod) {
            TimeOfDay.DAY -> when (base) {
                "Upon waking" -> 0
                "Selichot" -> 1
                "Morning Prayer (Shacharit)" -> 2
                else -> rest
            }
            TimeOfDay.AFTERNOON -> when (base) {
                "Afternoon Prayer" -> 0
                "Upon waking" -> 1
                "Selichot" -> 2
                "Morning Prayer (Shacharit)" -> 3
                else -> rest
            }
            TimeOfDay.NIGHT -> when (base) {
                "Evening Prayer" -> 0
                "Upon waking" -> 1
                "Selichot" -> 2
                "Monthly" -> 3
                else -> rest
            }
            TimeOfDay.ANY -> defaultIndex
        }
    }

    private const val BOTTOM_SECTION_BASE = 10_000

    /** Section to scroll to when the user taps the active zman period label. */
    fun scrollTargetForPeriod(period: TimeOfDay): String = when (period) {
        TimeOfDay.DAY, TimeOfDay.ANY -> "Morning Prayer (Shacharit)"
        TimeOfDay.AFTERNOON -> "Afternoon Prayer"
        TimeOfDay.NIGHT -> "Evening Prayer"
    }
}
