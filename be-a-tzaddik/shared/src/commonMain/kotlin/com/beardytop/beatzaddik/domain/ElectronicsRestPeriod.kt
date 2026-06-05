package com.beardytop.beatzaddik.domain

enum class RestKind {
    SHABBAT,
    YOM_TOV
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
    /** When the rest period ends (local), if known — for "available again after …" */
    val endsAtEpochMillis: Long? = null
)
