package com.beardytop.beatzaddik.ui

import com.beardytop.beatzaddik.domain.TimeOfDay

/** Maps zman period labels to the first checklist section for that part of the day. */
object ChecklistPeriodScroll {

    fun targetBaseSection(period: TimeOfDay): String = when (period) {
        TimeOfDay.DAY, TimeOfDay.ANY -> "Upon waking"
        TimeOfDay.AFTERNOON -> "Afternoon Prayer"
        TimeOfDay.NIGHT -> "Evening Prayer"
    }

    fun periodForBaseSection(baseSection: String): TimeOfDay? = when (baseSection) {
        "Upon waking", "Morning Prayer (Shacharit)" -> TimeOfDay.DAY
        "Afternoon Prayer" -> TimeOfDay.AFTERNOON
        "Evening Prayer" -> TimeOfDay.NIGHT
        else -> null
    }
}
