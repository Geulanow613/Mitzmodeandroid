package com.beardytop.beatzaddik.domain.zmanim

import com.beardytop.beatzaddik.domain.LocationElevation
import com.beardytop.beatzaddik.domain.UserProfile
import com.beardytop.beatzaddik.domain.ZmanimSnapshot
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Location-based zmanim for iOS (and tests). Android production continues to use KosherJava.
 * Returns null when coordinates are missing — no heuristic fallback.
 */
object SharedZmanimBuilder {

    fun build(nowMillis: Long, profile: UserProfile): ZmanimSnapshot? {
        val lat = profile.latitude ?: return null
        val lon = profile.longitude ?: return null
        val elevation = LocationElevation.metersFor(profile)
        return buildForLocation(nowMillis, profile.timezoneId, lat, lon, elevation)
    }

    fun buildForLocation(
        nowMillis: Long,
        timezoneId: String,
        latitude: Double,
        longitude: Double,
        elevationMeters: Double = 0.0,
    ): ZmanimSnapshot {
        val tz = TimeZone.of(timezoneId)
        val date = Instant.fromEpochMilliseconds(nowMillis).toLocalDateTime(tz).date
        val tomorrow = SolarZmanim.tomorrow(date)
        val elev = elevationMeters

        val sunrise = SolarZmanim.sunriseMillis(date, latitude, longitude, elev)
        val sunset = SolarZmanim.sunsetMillis(date, latitude, longitude, elev)
        val chatzos = SolarZmanim.solarNoonUtcMillis(date, latitude, longitude)
        val tzeit = SolarZmanim.tzeitMillis(date, latitude, longitude, elev)
        val alot = SolarZmanim.alotHaShacharMillis(date, latitude, longitude, elev)
        val misheyakir = SolarZmanim.misheyakirMillis(date, latitude, longitude, elev)
        val sofShema = SolarZmanim.proportionalMillis(date, latitude, longitude, 3.0, elev)
        val sofTefilla = SolarZmanim.proportionalMillis(date, latitude, longitude, 4.0, elev)
        val minchaGedola = SolarZmanim.proportionalMillis(date, latitude, longitude, 6.5, elev)
        val plag = SolarZmanim.proportionalMillis(date, latitude, longitude, 10.75, elev)
        val nightEnd = SolarZmanim.alotHaShacharMillis(tomorrow, latitude, longitude, elev)

        return ZmanimSnapshot(
            misheyakirMillis = misheyakir,
            sunriseMillis = sunrise,
            sofZmanShemaMillis = sofShema,
            sofZmanTefillaMillis = sofTefilla,
            chatzosMillis = chatzos,
            minchaGedolaMillis = minchaGedola,
            plagHaminchaMillis = plag,
            sunsetMillis = sunset,
            tzeitMillis = tzeit,
            alotHaShacharMillis = alot,
            nightObligationsEndMillis = nightEnd,
            timezoneId = timezoneId,
        )
    }
}
