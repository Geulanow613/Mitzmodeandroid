package com.beardytop.beatzaddik.domain.zmanim

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
        return buildForLocation(nowMillis, profile.timezoneId, lat, lon)
    }

    fun buildForLocation(
        nowMillis: Long,
        timezoneId: String,
        latitude: Double,
        longitude: Double,
    ): ZmanimSnapshot {
        val tz = TimeZone.of(timezoneId)
        val date = Instant.fromEpochMilliseconds(nowMillis).toLocalDateTime(tz).date
        val tomorrow = SolarZmanim.tomorrow(date)

        val sunrise = SolarZmanim.sunriseMillis(date, latitude, longitude)
        val sunset = SolarZmanim.sunsetMillis(date, latitude, longitude)
        val chatzos = SolarZmanim.solarNoonUtcMillis(date, latitude, longitude)
        val tzeit = SolarZmanim.tzeitMillis(date, latitude, longitude)
        val alot = SolarZmanim.alotHaShacharMillis(date, latitude, longitude)
        val misheyakir = SolarZmanim.misheyakirMillis(date, latitude, longitude)
        val sofShema = SolarZmanim.proportionalMillis(date, latitude, longitude, 3.0)
        val sofTefilla = SolarZmanim.proportionalMillis(date, latitude, longitude, 4.0)
        val minchaGedola = SolarZmanim.proportionalMillis(date, latitude, longitude, 6.5)
        val plag = SolarZmanim.proportionalMillis(date, latitude, longitude, 10.75)
        val nightEnd = SolarZmanim.alotHaShacharMillis(tomorrow, latitude, longitude)

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
