package com.beardytop.beatzaddik.domain

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UpcomingHolidayTimingTest {

    private val tz = "America/New_York"
    private val observanceEve = LocalDate(2026, 4, 6) // Monday

    @Test
    fun eveningObservanceTonightOnlyFromMidnightOnErevDay() {
        val zmanim = zmanimFor(observanceEve, dawnHour = 5, duskHour = 20)
        val sundayNight = millisAt(LocalDate(2026, 4, 5), hour = 22)
        val mondayMidnight = millisAt(observanceEve, hour = 0)
        val mondayMorning = millisAt(observanceEve, hour = 9)

        assertFalse(
            UpcomingHolidayTiming.canShowTonightForEveningObservance(
                sundayNight,
                observanceEve,
                zmanim,
                tz,
            ),
        )
        assertTrue(
            UpcomingHolidayTiming.canShowTonightForEveningObservance(
                mondayMidnight,
                observanceEve,
                zmanim,
                tz,
            ),
        )
        assertTrue(
            UpcomingHolidayTiming.canShowTonightForEveningObservance(
                mondayMorning,
                observanceEve,
                zmanim,
                tz,
            ),
        )
    }

    @Test
    fun weekdayEveningLabelUsesObservanceDay() {
        assertEquals("Mon evening", UpcomingHolidayTiming.weekdayEveningLabel(observanceEve))
    }

    private fun millisAt(date: LocalDate, hour: Int): Long =
        LocalDateTime(date.year, date.monthNumber, date.dayOfMonth, hour, 0)
            .toInstant(TimeZone.of(tz))
            .toEpochMilliseconds()

    private fun zmanimFor(date: LocalDate, dawnHour: Int, duskHour: Int): ZmanimSnapshot {
        val dawn = millisAt(date, dawnHour)
        val dusk = millisAt(date, duskHour)
        return ZmanimSnapshot(
            timezoneId = tz,
            alotHaShacharMillis = dawn,
            sunriseMillis = dawn + 30 * 60 * 1000L,
            sunsetMillis = dusk,
            tzeitMillis = dusk + 45 * 60 * 1000L,
            nightObligationsEndMillis = millisAt(date.plus(1, DateTimeUnit.DAY), dawnHour),
        )
    }
}
