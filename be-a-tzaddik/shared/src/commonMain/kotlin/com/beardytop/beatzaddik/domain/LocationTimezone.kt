package com.beardytop.beatzaddik.domain

/**
 * Resolves an IANA timezone for GPS coordinates.
 *
 * Uses the bundled city catalog for timezone ids only — zmanim still use the actual GPS
 * latitude/longitude. Device/emulator timezone is a last resort (often UTC on emulators).
 */
object LocationTimezone {
    fun resolve(latitude: Double, longitude: Double, deviceFallback: String): String =
        ManualCities.nearestTimezoneId(latitude, longitude) ?: deviceFallback

    fun resolveForProfile(profile: UserProfile, deviceFallback: String = deviceTimezoneId()): UserProfile {
        if (!profile.useGps) return profile
        val lat = profile.latitude ?: return profile
        val lon = profile.longitude ?: return profile
        val resolved = resolve(lat, lon, deviceFallback)
        return if (resolved != profile.timezoneId) profile.copy(timezoneId = resolved) else profile
    }
}
