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

    /** Catalog template key + args for "Available in X · at midday" style lines. */
    fun upcomingSummaryTemplate(
        windowStartMillis: Long?,
        nowMillis: Long,
        atLabel: String?,
    ): Pair<String, Map<String, String>>? {
        val start = windowStartMillis ?: return onlyAvailableWhenTemplate(atLabel, upcoming = true)
        val countdown = formatDuration(start, nowMillis)
        val atSuffix = atSuffixForLabel(atLabel)
        return "Available in {countdown}{at}" to mapOf(
            "countdown" to countdown,
            "at" to atSuffix,
        )
    }

    fun upcomingSummary(
        windowStartMillis: Long?,
        nowMillis: Long,
        atLabel: String?
    ): String? {
        val (key, args) = upcomingSummaryTemplate(windowStartMillis, nowMillis, atLabel) ?: return null
        return fillTemplate(key, args)
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

    fun unavailableCollapsedSummaryTemplate(
        availability: ItemZmanAvailability,
        windowStartMillis: Long?,
        nowMillis: Long,
        atLabel: String?,
    ): Pair<String, Map<String, String>>? = when (availability) {
        ItemZmanAvailability.UPCOMING ->
            upcomingSummaryTemplate(windowStartMillis, nowMillis, atLabel)
        ItemZmanAvailability.EXPIRED ->
            onlyAvailableWhenTemplate(atLabel, upcoming = false)
        ItemZmanAvailability.ACTIVE -> null
    }

    private fun atSuffixForLabel(atLabel: String?): String = when (atLabel) {
        "dawn", "sunrise" -> " · at dawn"
        "midday" -> " · at midday"
        "sunset" -> " · at sunset"
        "nightfall" -> " · at nightfall"
        "halachic midnight" -> " · at halachic midnight"
        "misheyakir" -> " · at misheyakir"
        "Mincha Gedola" -> " · at Mincha Gedola"
        else -> atLabel?.let { " · at $it" }.orEmpty()
    }

    private fun onlyAvailableWhenTemplate(atLabel: String?, upcoming: Boolean): Pair<String, Map<String, String>>? {
        val whenKey = when (atLabel) {
            "dawn", "sunrise" -> if (upcoming) "Available during the day · tap to read"
            else "Only available during the day · tap to read"
            "midday" -> if (upcoming) "Available from midday on · tap to read"
            else "Only available from midday on · tap to read"
            "sunset" -> if (upcoming) "Available after sunset · tap to read"
            else "Only available after sunset · tap to read"
            "nightfall" -> if (upcoming) "Available at night · tap to read"
            else "Only available at night · tap to read"
            "halachic midnight" -> if (upcoming) "Available after halachic midnight (chatzos) · tap to read"
            else "Only available after halachic midnight (chatzos) · tap to read"
            else -> null
        }
        return whenKey?.let { it to emptyMap() }
    }

    private fun onlyAvailableWhen(atLabel: String?, upcoming: Boolean): String {
        val template = onlyAvailableWhenTemplate(atLabel, upcoming)
        if (template != null) return template.first
        return if (upcoming) "Coming up later · tap to read"
        else "Tap to read when it's time"
    }

    private fun fillTemplate(key: String, args: Map<String, String>): String {
        var out = key
        for ((k, v) in args) out = out.replace("{$k}", v)
        return out
    }
}
