package com.beardytop.beatzaddik.domain

/**
 * Checklist rows contribute to the mitzvah count at most once per halachic day (tzeit→tzeit).
 * Re-checking after uncheck must not increment again until the day key changes.
 */
object ChecklistMitzvahCounting {
    fun entryId(itemId: String, dayKey: String): String =
        "checklist_count_${itemId}_$dayKey"

    fun alreadyCounted(
        completedIds: Iterable<String>,
        itemId: String,
        dayKey: String,
    ): Boolean {
        val target = entryId(itemId, dayKey)
        return completedIds.any { it == target }
    }
}
