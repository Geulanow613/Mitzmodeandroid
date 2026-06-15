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

    /**
     * Lower index = higher on the Today checklist.
     *
     * - **Day:** Upon waking, then Morning Prayer, then everything else.
     * - **Afternoon:** Mincha, then Upon waking, then Morning Prayer (if still open), then rest.
     * - **Night:** Evening Prayer, then Upon waking, then Monthly (Kiddush Levana / RC), then rest.
     */
    fun sortIndex(section: String, activePeriod: TimeOfDay): Int {
        val base = baseName(section)
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
