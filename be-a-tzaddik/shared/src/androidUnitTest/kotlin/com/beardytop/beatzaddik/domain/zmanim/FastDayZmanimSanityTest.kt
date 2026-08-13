package com.beardytop.beatzaddik.domain.zmanim

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.junit.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Wall-clock sanity for the 17 Tammuz 5786 fast in Jerusalem (Thursday July 2, 2026).
 * Regression guard for the iOS bug where the fast start displayed as an afternoon time
 * (2:12 PM) instead of dawn (~4:10 AM).
 */
class FastDayZmanimSanityTest {

    private val tz = "Asia/Jerusalem"
    private val fastDay = LocalDate(2026, 7, 2)

    @Test
    fun seventeenTammuzJerusalem_fastStartsAtDawnEarlyMorning() {
        val z = snapshot()
        val alot = assertNotNull(z.alotHaShacharMillis, "alot hashachar missing")
        val local = localTime(alot)
        assertTrue(local.hour in 3..5, "alot should be early morning, was $local")
    }

    @Test
    fun seventeenTammuzJerusalem_fastEndsAtNightfallEvening() {
        val z = snapshot()
        val tzeit = assertNotNull(z.tzeitMillis, "tzeit missing")
        val local = localTime(tzeit)
        assertTrue(local.hour in 19..21, "tzeit should be evening, was $local")
        val sunset = assertNotNull(z.sunsetMillis)
        assertTrue(tzeit > sunset, "tzeit must be after sunset")
    }

    private fun snapshot() = SharedZmanimBuilder.buildForLocation(
        nowMillis = LocalDateTime(fastDay.year, fastDay.monthNumber, fastDay.dayOfMonth, 12, 0)
            .toInstant(TimeZone.of(tz))
            .toEpochMilliseconds(),
        timezoneId = tz,
        latitude = 31.7683,
        longitude = 35.2137,
    )

    private fun localTime(epochMillis: Long): LocalDateTime =
        Instant.fromEpochMilliseconds(epochMillis).toLocalDateTime(TimeZone.of(tz))
}
