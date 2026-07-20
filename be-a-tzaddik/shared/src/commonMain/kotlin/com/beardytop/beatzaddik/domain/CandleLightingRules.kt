package com.beardytop.beatzaddik.domain

/** Shabbat and weekday Erev Yom Tov candle lighting — minutes before sunset. */
object CandleLightingRules {

    const val LEAD_MINUTES_DEFAULT = 18
    const val LEAD_MINUTES_JERUSALEM = 40

    fun isJerusalem(profile: UserProfile): Boolean {
        // Align with JerusalemPurimRules: do not infer from freeform GPS labels
        // (false positives outside Jerusalem). Explicit city id or lat/lon box only.
        if (profile.manualCityId in setOf("jlm", "jerusalem")) return true
        val lat = profile.latitude ?: return false
        val lon = profile.longitude ?: return false
        return lat in 31.7..31.85 && lon in 35.15..35.3
    }

    fun leadMinutesBeforeSunset(profile: UserProfile): Int =
        if (isJerusalem(profile)) LEAD_MINUTES_JERUSALEM else LEAD_MINUTES_DEFAULT

    fun candleLightingMillis(sunsetMillis: Long, profile: UserProfile): Long =
        sunsetMillis - leadMinutesBeforeSunset(profile) * 60_000L
}
