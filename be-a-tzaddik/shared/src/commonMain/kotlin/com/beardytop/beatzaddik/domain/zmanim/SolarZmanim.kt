package com.beardytop.beatzaddik.domain.zmanim

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin
import kotlin.math.tan

/**
 * KosherJava-compatible NOAA solar calculator (ported from [com.kosherjava.zmanim.util.NOAACalculator]).
 * Used on iOS; Android production still calls KosherJava directly on JVM.
 */
internal object SolarZmanim {

    private const val RAD = PI / 180.0
    private const val JULIAN_DAY_JAN_1_2000 = 2451545.0
    private const val JULIAN_DAYS_PER_CENTURY = 36525.0

    /** Geometric zenith + refraction + solar radius (KosherJava default sunrise/sunset). */
    private const val SUNRISE_ZENITH = 90.833
    private const val TZEIT_ZENITH = 90.0 + 8.5
    private const val ALOT_ZENITH = 90.0 + 16.1
    private const val MISHEYAKIR_ZENITH = 90.0 + 10.2

    private enum class SolarEvent { SUNRISE, SUNSET, NOON }

    fun sunriseMillis(date: LocalDate, latitude: Double, longitude: Double): Long? =
        sunEventMillis(date, latitude, longitude, SUNRISE_ZENITH, SolarEvent.SUNRISE)

    fun sunsetMillis(date: LocalDate, latitude: Double, longitude: Double): Long? =
        sunEventMillis(date, latitude, longitude, SUNRISE_ZENITH, SolarEvent.SUNSET)

    fun solarNoonUtcMillis(date: LocalDate, latitude: Double, longitude: Double): Long? {
        val jd = julianDay(date)
        val noonMin = solarNoonMidnightUtcMinutes(jd, -longitude, SolarEvent.NOON)
        return utcMinutesToEpochMillis(date, noonMin)
    }

    fun tzeitMillis(date: LocalDate, latitude: Double, longitude: Double): Long? =
        sunEventMillis(date, latitude, longitude, TZEIT_ZENITH, SolarEvent.SUNSET)

    fun alotHaShacharMillis(date: LocalDate, latitude: Double, longitude: Double): Long? =
        sunEventMillis(date, latitude, longitude, ALOT_ZENITH, SolarEvent.SUNRISE)

    fun misheyakirMillis(date: LocalDate, latitude: Double, longitude: Double): Long? =
        sunEventMillis(date, latitude, longitude, MISHEYAKIR_ZENITH, SolarEvent.SUNRISE)

    fun proportionalMillis(
        date: LocalDate,
        latitude: Double,
        longitude: Double,
        halachicHour: Double,
    ): Long? {
        val sunrise = sunriseMillis(date, latitude, longitude) ?: return null
        val sunset = sunsetMillis(date, latitude, longitude) ?: return null
        return sunrise + ((sunset - sunrise) * halachicHour / 12.0).toLong()
    }

    fun tomorrow(date: LocalDate): LocalDate = date.plus(1, DateTimeUnit.DAY)

    private fun sunEventMillis(
        date: LocalDate,
        latitude: Double,
        longitude: Double,
        zenith: Double,
        event: SolarEvent,
    ): Long? {
        val utcMinutes = sunRiseSetUtcMinutes(date, latitude, -longitude, zenith, event)
        if (utcMinutes.isNaN()) return null
        return utcMinutesToEpochMillis(date, utcMinutes)
    }

    private fun utcMinutesToEpochMillis(date: LocalDate, utcMinutes: Double): Long {
        var day = date
        var mins = utcMinutes
        while (mins >= 1440.0) {
            mins -= 1440.0
            day = day.plus(1, DateTimeUnit.DAY)
        }
        while (mins < 0) {
            mins += 1440.0
            day = day.plus(-1, DateTimeUnit.DAY)
        }
        val hour = floor(mins / 60.0).toInt()
        val minFrac = mins - hour * 60.0
        val minute = floor(minFrac).toInt()
        val second = floor((minFrac - minute) * 60.0).toInt()
        return LocalDateTime(day.year, day.monthNumber, day.dayOfMonth, hour, minute, second)
            .toInstant(TimeZone.UTC)
            .toEpochMilliseconds()
    }

  private fun julianDay(date: LocalDate): Double {
        var year = date.year
        var month = date.monthNumber
        val day = date.dayOfMonth
        if (month <= 2) {
            year -= 1
            month += 12
        }
        val a = year / 100
        val b = 2 - a + a / 4
        return floor(365.25 * (year + 4716)) + floor(30.6001 * (month + 1)) + day + b - 1524.5
    }

    private fun julianCenturies(julianDay: Double): Double =
        (julianDay - JULIAN_DAY_JAN_1_2000) / JULIAN_DAYS_PER_CENTURY

    private fun sunGeometricMeanLongitude(jc: Double): Double {
        val lon = 280.46646 + jc * (36000.76983 + 0.0003032 * jc)
        return if (lon > 0) lon % 360 else lon % 360 + 360
    }

    private fun sunGeometricMeanAnomaly(jc: Double): Double =
        357.52911 + jc * (35999.05029 - 0.0001537 * jc)

    private fun earthOrbitEccentricity(jc: Double): Double =
        0.016708634 - jc * (0.000042037 + 0.0000001267 * jc)

    private fun sunEquationOfCenter(jc: Double): Double {
        val m = sunGeometricMeanAnomaly(jc)
        val sinm = sinDegrees(m)
        val sin2m = sinDegrees(m + m)
        val sin3m = sinDegrees(m + m + m)
        return sinm * (1.914602 - jc * (0.004817 + 0.000014 * jc)) +
            sin2m * (0.019993 - 0.000101 * jc) +
            sin3m * 0.000289
    }

    private fun sunTrueLongitude(jc: Double): Double =
        sunGeometricMeanLongitude(jc) + sunEquationOfCenter(jc)

    private fun sunApparentLongitude(jc: Double): Double {
        val sunTrue = sunTrueLongitude(jc)
        val omega = 125.04 - 1934.136 * jc
        return sunTrue - 0.00569 - 0.00478 * sinDegrees(omega)
    }

    private fun meanObliquity(jc: Double): Double {
        val seconds = 21.448 - jc * (46.8150 + jc * (0.00059 - jc * 0.001813))
        return 23.0 + (26.0 + seconds / 60.0) / 60.0
    }

    private fun obliquityCorrection(jc: Double): Double {
        val omega = 125.04 - 1934.136 * jc
        return meanObliquity(jc) + 0.00256 * cosDegrees(omega)
    }

    private fun sunDeclination(jc: Double): Double {
        val epsilon = obliquityCorrection(jc)
        val lambda = sunApparentLongitude(jc)
        return asinDegrees(sinDegrees(epsilon) * sinDegrees(lambda))
    }

    private fun equationOfTime(jc: Double): Double {
        val epsilon = obliquityCorrection(jc)
        val geomMeanLong = sunGeometricMeanLongitude(jc)
        val eccentricity = earthOrbitEccentricity(jc)
        val geomMeanAnomaly = sunGeometricMeanAnomaly(jc)
        var y = tanDegrees(epsilon / 2.0)
        y *= y
        val sin2l0 = sinDegrees(2.0 * geomMeanLong)
        val sinm = sinDegrees(geomMeanAnomaly)
        val cos2l0 = cosDegrees(2.0 * geomMeanLong)
        val sin4l0 = sinDegrees(4.0 * geomMeanLong)
        val sin2m = sinDegrees(2.0 * geomMeanAnomaly)
        val eot = y * sin2l0 - 2.0 * eccentricity * sinm +
            4.0 * eccentricity * y * sinm * cos2l0 -
            0.5 * y * y * sin4l0 -
            1.25 * eccentricity * eccentricity * sin2m
        return (eot / RAD) * 4.0
    }

    private fun sunHourAngleRadians(latitude: Double, solarDeclination: Double, zenith: Double, event: SolarEvent): Double {
        val ratio = cosDegrees(zenith) / (cosDegrees(latitude) * cosDegrees(solarDeclination)) -
            tanDegrees(latitude) * tanDegrees(solarDeclination)
        if (ratio < -1.0 || ratio > 1.0) return Double.NaN
        var hourAngle = acos(ratio)
        if (event == SolarEvent.SUNSET) hourAngle = -hourAngle
        return hourAngle
    }

    private fun solarNoonMidnightUtcMinutes(julianDay: Double, longitude: Double, event: SolarEvent): Double {
        var jd = if (event == SolarEvent.NOON) julianDay else julianDay + 0.5
        val tnoon = julianCenturies(jd + longitude / 360.0)
        var equation = equationOfTime(tnoon)
        var solNoon = longitude * 4 - equation
        repeat(2) {
            val newt = julianCenturies(jd + solNoon / 1440.0)
            equation = equationOfTime(newt)
            solNoon = (if (event == SolarEvent.NOON) 720.0 else 1440.0) + longitude * 4 - equation
        }
        return solNoon
    }

    private fun sunRiseSetUtcMinutes(
        date: LocalDate,
        latitude: Double,
        longitude: Double,
        zenith: Double,
        event: SolarEvent,
    ): Double {
        val jd = julianDay(date)
        val noonMin = solarNoonMidnightUtcMinutes(jd, longitude, SolarEvent.NOON)
        val tnoon = julianCenturies(jd + noonMin / 1440.0)
        var eq = equationOfTime(tnoon)
        var decl = sunDeclination(tnoon)
        var ha = sunHourAngleRadians(latitude, decl, zenith, event)
        if (ha.isNaN()) return Double.NaN
        var delta = longitude - (ha / RAD)
        var timeDiff = 4 * delta
        var timeUtc = 720 + timeDiff - eq
        val newt = julianCenturies(jd + timeUtc / 1440.0)
        eq = equationOfTime(newt)
        decl = sunDeclination(newt)
        ha = sunHourAngleRadians(latitude, decl, zenith, event)
        if (ha.isNaN()) return Double.NaN
        delta = longitude - (ha / RAD)
        timeDiff = 4 * delta
        timeUtc = 720 + timeDiff - eq
        return timeUtc
    }

    private fun sinDegrees(d: Double) = sin(d * RAD)
    private fun cosDegrees(d: Double) = cos(d * RAD)
    private fun tanDegrees(d: Double) = tan(d * RAD)
    private fun asinDegrees(v: Double) = asin(v.coerceIn(-1.0, 1.0)) / RAD
}
