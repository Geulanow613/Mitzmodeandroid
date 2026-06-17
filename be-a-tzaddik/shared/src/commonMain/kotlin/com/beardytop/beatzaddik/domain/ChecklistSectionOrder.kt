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
        "Pesach prep" to -40,
        "Prepare for the festival" to -30,
        "Fasts" to -35,
        "Chanukah" to -25,
    )

    fun prioritizedPrepSections(
        cal: DayInfo,
        tomorrowCal: DayInfo,
        profile: UserProfile,
    ): Set<String> = buildSet {
        if (ErevPesachPrepText.isPesachPrepWindow(cal)) add("Pesach prep")
        if (
            "erev_chag" in cal.activeSeasons ||
            cal.festivalWeekPrep() != null ||
            YomTovShabbatPrepText.shouldShowAdvancePrepDay(cal, tomorrowCal, profile)
        ) {
            add("Prepare for the festival")
        }
        if ("erev_purim" in cal.activeSeasons) add("Purim")
        if ("erev_chanukah" in cal.activeSeasons) add("Chanukah")
        if ("erev_minor_fast" in cal.activeSeasons || "erev_yom_kippur" in cal.activeSeasons ||
            "erev_tisha_beav" in cal.activeSeasons
        ) {
            add("Fasts")
        }
    }

    /**
     * Lower index = higher on the Today checklist.
     *
     * - **Day:** Upon waking, then Morning Prayer, then everything else.
     * - **Afternoon:** Mincha, then Upon waking, then Morning Prayer (if still open), then rest.
     * - **Night:** Evening Prayer, then Upon waking, then Monthly (Kiddush Levana / RC), then rest.
     */
    fun sortIndex(
        section: String,
        activePeriod: TimeOfDay,
        prioritizePrepSections: Set<String> = emptySet(),
    ): Int {
        val base = baseName(section)
        if (base in prioritizePrepSections) {
            return prepSectionSortPriority[base] ?: -20
        }
        val defaultIndex = ChecklistEngine.sectionOrder.indexOf(base).takeIf { it >= 0 } ?: 200
        val rest = 1000 + defaultIndex

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

    /** Section to scroll to when the user taps the active zman period label. */
    fun scrollTargetForPeriod(period: TimeOfDay): String = when (period) {
        TimeOfDay.DAY, TimeOfDay.ANY -> "Morning Prayer (Shacharit)"
        TimeOfDay.AFTERNOON -> "Afternoon Prayer"
        TimeOfDay.NIGHT -> "Evening Prayer"
    }
}
