package com.beardytop.beatzaddik.domain

object ZmanCountdownFormatter {

    fun formatDuration(untilMillis: Long, nowMillis: Long): String {
        val diffMs = (untilMillis - nowMillis).coerceAtLeast(0)
        val totalMinutes = diffMs / 60_000
        if (totalMinutes < 1) return "soon"
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return when {
            hours >= 48 -> {
                val days = hours / 24
                val remHours = hours % 24
                if (remHours > 0) "${days}d ${remHours}h" else "${days}d"
            }
            hours > 0 -> if (minutes > 0) "${hours}h ${minutes}m" else "${hours}h"
            else -> "${minutes}m"
        }
    }

    fun upcomingSummary(
        windowStartMillis: Long?,
        nowMillis: Long,
        atLabel: String?
    ): String? {
        val start = windowStartMillis ?: return onlyAvailableWhen(atLabel, upcoming = true)
        val countdown = formatDuration(start, nowMillis)
        val at = atLabel?.let { " · at $it" }.orEmpty()
        return "Available in $countdown$at"
    }

    /** Short line for collapsed rows when outside the item's zman window. */
    fun unavailableCollapsedSummary(
        availability: ItemZmanAvailability,
        windowStartMillis: Long?,
        nowMillis: Long,
        atLabel: String?
    ): String? = when (availability) {
        ItemZmanAvailability.UPCOMING ->
            upcomingSummary(windowStartMillis, nowMillis, atLabel)
        ItemZmanAvailability.EXPIRED ->
            onlyAvailableWhen(atLabel, upcoming = false)
        ItemZmanAvailability.ACTIVE -> null
    }

    private fun onlyAvailableWhen(atLabel: String?, upcoming: Boolean): String {
        val whenPhrase = when (atLabel) {
            "dawn", "sunrise" -> "during the day"
            "midday" -> "from midday on"
            "sunset" -> "after sunset"
            "nightfall" -> "at night"
            "halachic midnight" -> "after halachic midnight (chatzos)"
            else -> null
        }
        return if (whenPhrase != null) {
            val lead = if (upcoming) "Available" else "Only available"
            "$lead $whenPhrase · tap to read"
        } else {
            if (upcoming) "Coming up later · tap to read"
            else "Tap to read when it's time"
        }
    }
}
