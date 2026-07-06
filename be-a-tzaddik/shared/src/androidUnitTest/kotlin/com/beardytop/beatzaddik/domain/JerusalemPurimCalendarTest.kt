package com.beardytop.beatzaddik.domain

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Jerusalem uses Shushan Purim (15 Adar). 5786: 12 Adar = Sun Mar 1; 15 Adar = Wed Mar 4.
 */
class JerusalemPurimCalendarTest {

    private val jerusalem = UserProfile(
        timezoneId = "Asia/Jerusalem",
        latitude = 31.7683,
        longitude = 35.2137,
        locationLabel = "Jerusalem, Israel",
        manualCityId = "jlm",
    )

    private val backend = createJewishCalendarBackend()

    @Test
    fun jerusalem_12Adar_noErevPurim_upcomingIsShushanOn15Adar() {
        val info = dayInfo(LocalDate(2026, 3, 1), 9)
        assertEquals(12, info.hebrewDay)
        assertFalse("erev_purim" in info.activeSeasons)

        val holidays = JewishCalendarService(backend).upcomingHolidays(
            nowEpochMillis = millis(LocalDate(2026, 3, 1), 9),
            profile = jerusalem,
        )
        val purim = holidays.firstOrNull { it.name.contains("Purim", ignoreCase = true) }
        assertEquals("Shushan Purim", purim?.name)
        assertEquals(3, purim?.daysAway)
    }

    @Test
    fun jerusalem_14Adar_isErevShushanPurim() {
        val info = dayInfo(LocalDate(2026, 3, 3), 10)
        assertEquals(14, info.hebrewDay)
        assertTrue("erev_purim" in info.activeSeasons)
        assertFalse(info.isPurim)
    }

    @Test
    fun jerusalem_15Adar_isShushanPurimDay() {
        val info = dayInfo(LocalDate(2026, 3, 4), 10)
        assertEquals(15, info.hebrewDay)
        assertTrue(info.isPurim)
        assertTrue("purim" in info.activeSeasons)
    }

    @Test
    fun jerusalem_13Adar_isNotErevPurim() {
        val info = dayInfo(LocalDate(2026, 3, 2), 10)
        assertEquals(13, info.hebrewDay)
        assertFalse("erev_purim" in info.activeSeasons)
    }

    @Test
    fun diaspora_13Adar_isErevPurim() {
        val nyc = UserProfile(
            timezoneId = "America/New_York",
            latitude = 40.7128,
            longitude = -74.0060,
            locationLabel = "New York",
        )
        val info = backend.dayInfoAt(millis(LocalDate(2026, 3, 2), 10), nyc)
        assertTrue("erev_purim" in info.activeSeasons)
    }

    private fun dayInfo(date: LocalDate, hour: Int): DayInfo =
        backend.dayInfoAt(millis(date, hour), jerusalem)

    private fun millis(date: LocalDate, hour: Int): Long =
        LocalDateTime(date.year, date.monthNumber, date.dayOfMonth, hour, 0)
            .toInstant(TimeZone.of("Asia/Jerusalem"))
            .toEpochMilliseconds()
}
