package com.beardytop.beatzaddik.ui

import com.beardytop.beatzaddik.domain.ChecklistSectionOrder
import com.beardytop.beatzaddik.domain.TimeOfDay

/** Maps zman period labels to the first checklist section for that part of the day. */
object ChecklistPeriodScroll {

    fun targetBaseSection(period: TimeOfDay): String =
        ChecklistSectionOrder.scrollTargetForPeriod(period)

    fun periodForBaseSection(baseSection: String): TimeOfDay? = when (ChecklistSectionOrder.baseName(baseSection)) {
        "Upon waking", "Morning Prayer (Shacharit)" -> TimeOfDay.DAY
        "Afternoon Prayer" -> TimeOfDay.AFTERNOON
        "Evening Prayer", "Monthly" -> TimeOfDay.NIGHT
        else -> null
    }
}
