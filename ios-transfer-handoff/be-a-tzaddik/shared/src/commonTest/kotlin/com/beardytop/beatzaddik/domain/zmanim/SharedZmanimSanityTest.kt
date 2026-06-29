package com.beardytop.beatzaddik.domain.zmanim

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class SharedZmanimSanityTest {

    @Test
    fun summerDayTimesAreOrdered() {
        val date = LocalDate(2026, 6, 15)
        val lat = 32.7157
        val lon = -117.1611
        val tz = "America/Los_Angeles"
        val noon = LocalDateTime(2026, 6, 15, 12, 0).toInstant(TimeZone.of(tz)).toEpochMilliseconds()

        val z = SharedZmanimBuilder.buildForLocation(noon, tz, lat, lon)

        val alot = assertNotNull(z.alotHaShacharMillis)
        val misheyakir = assertNotNull(z.misheyakirMillis)
        val sunrise = assertNotNull(z.sunriseMillis)
        val shema = assertNotNull(z.sofZmanShemaMillis)
        val tefilla = assertNotNull(z.sofZmanTefillaMillis)
        val chatzos = assertNotNull(z.chatzosMillis)
        val mincha = assertNotNull(z.minchaGedolaMillis)
        val plag = assertNotNull(z.plagHaminchaMillis)
        val sunset = assertNotNull(z.sunsetMillis)
        val tzeit = assertNotNull(z.tzeitMillis)

        assertTrue(alot < misheyakir)
        assertTrue(misheyakir < sunrise)
        assertTrue(sunrise < shema)
        assertTrue(shema < tefilla)
        assertTrue(tefilla < chatzos)
        assertTrue(chatzos < mincha)
        assertTrue(mincha < plag)
        assertTrue(plag < sunset)
        assertTrue(sunset < tzeit)
    }

    @Test
    fun missingCoordinatesFallsBackToHeuristic() {
        val profile = com.beardytop.beatzaddik.domain.UserProfile(
            timezoneId = "America/New_York",
            latitude = null,
            longitude = null,
        )
        val z = SharedZmanimBuilder.build(1_700_000_000_000L, profile)
        assertTrue(z.hasLocationTimes)
    }
}
