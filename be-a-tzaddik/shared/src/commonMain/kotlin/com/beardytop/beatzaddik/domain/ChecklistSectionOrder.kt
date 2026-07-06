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
        "Motzei Shabbat" to -60,
        "Hoshana Rabbah" to -65,
        "Chol HaMoed" to -55,
        "Seasonal" to -50,
        "Pesach prep" to -40,
        "Prepare for the festival" to -30,
        "Fasts" to -45,
        "Chanukah" to -25,
    )

    fun prioritizedPrepSections(
        cal: DayInfo,
        tomorrowCal: DayInfo,
        profile: UserProfile,
        nowMillis: Long,
    ): Set<String> = buildSet {
        if (BirkatHachamahRules.visibleOccurrence(cal.date) != null) add("Seasonal")
        if (ErevPesachPrepText.isPesachPrepWindow(cal)) add("Pesach prep")
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
        if ("erev_chanukah" in cal.activeSeasons) add("Chanukah")
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
                "Morning Prayer (Shacharit)" -> 1
                else -> rest
            }
            TimeOfDay.AFTERNOON -> when (base) {
                "Afternoon Prayer" -> 0
                "Upon waking" -> 1
                "Morning Prayer (Shacharit)" -> 2
                else -> rest
            }
            TimeOfDay.NIGHT -> when (base) {
                "Evening Prayer" -> 0
                "Upon waking" -> 1
                "Monthly" -> 2
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
