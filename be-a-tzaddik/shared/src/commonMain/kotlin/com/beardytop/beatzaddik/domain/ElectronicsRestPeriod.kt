package com.beardytop.beatzaddik.domain

enum class RestKind {
    SHABBAT,
    YOM_TOV
}

enum class RestPhase {
    /** Ten-minute heads-up before melacha rest begins — user can still open settings. */
    APPROACHING,
    /** Holy day is in effect; the app is paused. */
    ACTIVE,
}

/**
 * When active, the app should not be used (electronics assur bemelacha).
 * Times are computed for the user's saved timezone and location.
 */
data class ElectronicsRestPeriod(
    val kind: RestKind,
    val title: String,
    val message: String,
    val hebrewDateLabel: String? = null,
    val locationLabel: String? = null,
    val phase: RestPhase = RestPhase.ACTIVE,
    /** When the rest period begins (local), for the approaching warning. */
    val startsAtEpochMillis: Long? = null,
    /** When the rest period ends (local), if known — for "available again after …" */
    val endsAtEpochMillis: Long? = null,
)
