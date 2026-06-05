package com.beardytop.beatzaddik.domain

/** Halachic times for today at the user's location (GRA / common zmanim app defaults on Android). */
data class ZmanimSnapshot(
    val misheyakirMillis: Long? = null,
    val sunriseMillis: Long? = null,
    val sofZmanShemaMillis: Long? = null,
    val sofZmanTefillaMillis: Long? = null,
    val chatzosMillis: Long? = null,
    /** Mincha Gedolah — typically ~30 minutes after chatzos. */
    val minchaGedolaMillis: Long? = null,
    val plagHaminchaMillis: Long? = null,
    val sunsetMillis: Long? = null,
    /** Nightfall (tzeit): sun 8.5° below horizon — matches typical zmanim apps / MyZmanim. */
    val tzeitMillis: Long? = null,
    val alotHaShacharMillis: Long? = null,
    /** End of "tonight" for Maariv / evening Shema / bedtime (next alot hashachar). */
    val nightObligationsEndMillis: Long? = null,
    val timezoneId: String
) {
    val hasLocationTimes: Boolean =
        sunriseMillis != null && sunsetMillis != null
}

enum class ItemZmanAvailability {
    /** In its window — normal checklist row. */
    ACTIVE,
    /** Not yet time (e.g. Mincha before chatzos + 30 min). */
    UPCOMING,
    /** Today's ideal window has passed. */
    EXPIRED
}

data class ItemZmanStatus(
    val availability: ItemZmanAvailability = ItemZmanAvailability.ACTIVE,
    val hint: String? = null,
    val makeupNote: String? = null,
    /** When [availability] is UPCOMING, millis when the window opens. */
    val windowStartMillis: Long? = null,
    val windowEndMillis: Long? = null,
    /** Short label for UI, e.g. "dawn", "nightfall", "sunrise". */
    val availableAtLabel: String? = null
)
