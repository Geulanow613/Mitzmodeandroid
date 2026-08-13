package com.beardytop.beatzaddik.domain

import com.beardytop.beatzaddik.platform.fetchElevationMeters

/** Resolves elevation in meters for KosherJava sunrise/sunset adjustments. */
object LocationElevation {
    /** Max distance to trust a bundled city's elevation for GPS (km). */
    private const val MAX_BUNDLED_CITY_KM = 120.0

    fun metersFor(profile: UserProfile): Double =
        storedMeters(profile)
            ?: profile.latitude?.let { lat ->
                profile.longitude?.let { lon -> nearestBundledCityElevation(lat, lon) }
            }
            ?: 0.0

    /** GPS altitude is often noisy; ignore absurd values. */
    fun fromGpsAltitude(altitudeMeters: Double?, hasAltitude: Boolean): Double? {
        if (!hasAltitude || altitudeMeters == null) return null
        if (!altitudeMeters.isFinite() || altitudeMeters <= 0.0 || altitudeMeters > 9_000.0) return null
        return altitudeMeters
    }

    /**
     * Resolve elevation after a GPS fix: device altitude, then nearest bundled city,
     * then a one-shot network lookup (Android).
     */
    suspend fun resolveForGps(
        latitude: Double,
        longitude: Double,
        gpsAltitudeMeters: Double?,
        hasAltitudeReading: Boolean,
    ): Double? {
        fromGpsAltitude(gpsAltitudeMeters, hasAltitudeReading)?.let { return it }
        nearestBundledCityElevation(latitude, longitude)?.let { return it }
        fetchElevationMeters(latitude, longitude)?.takeIf { it > 0.0 }?.let { return it }
        return null
    }

    /** Backfill when profile has coordinates but no stored elevation. */
    suspend fun backfillForProfile(profile: UserProfile): Double? {
        if (storedMeters(profile) != null) return null
        val lat = profile.latitude ?: return null
        val lon = profile.longitude ?: return null
        return resolveForGps(lat, lon, gpsAltitudeMeters = null, hasAltitudeReading = false)
            ?.takeIf { it > 0.0 }
    }

    fun nearestBundledCityElevation(latitude: Double, longitude: Double): Double? =
        ManualCities.nearestCityElevation(latitude, longitude, MAX_BUNDLED_CITY_KM)

    private fun storedMeters(profile: UserProfile): Double? =
        profile.elevationMeters?.takeIf { it > 0.0 }
            ?: profile.manualCityId
                ?.let { ManualCities.byId(it)?.elevationMeters?.takeIf { e -> e > 0.0 } }
}
