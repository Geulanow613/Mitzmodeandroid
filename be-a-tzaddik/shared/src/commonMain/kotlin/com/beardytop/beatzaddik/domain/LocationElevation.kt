package com.beardytop.beatzaddik.domain

import com.beardytop.beatzaddik.platform.fetchElevationMeters
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

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

    fun nearestBundledCityElevation(latitude: Double, longitude: Double): Double? {
        var bestElev: Double? = null
        var bestDistKm = Double.MAX_VALUE
        for (city in ManualCities.all) {
            val elev = city.elevationMeters ?: continue
            if (elev <= 0.0) continue
            val distKm = haversineKm(latitude, longitude, city.latitude, city.longitude)
            if (distKm < bestDistKm) {
                bestDistKm = distKm
                bestElev = elev
            }
        }
        if (bestElev == null || bestDistKm > MAX_BUNDLED_CITY_KM) return null
        return bestElev
    }

    private fun storedMeters(profile: UserProfile): Double? =
        profile.elevationMeters?.takeIf { it > 0.0 }
            ?: profile.manualCityId
                ?.let { ManualCities.byId(it)?.elevationMeters?.takeIf { e -> e > 0.0 } }

    private fun haversineKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6_371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2.0) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(dLon / 2).pow(2.0)
        return r * 2 * atan2(sqrt(a), sqrt(1 - a))
    }
}
