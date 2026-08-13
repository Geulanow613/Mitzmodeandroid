package com.beardytop.beatzaddik.domain

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Motzei Shabbat / Melave Malka: Saturday nightfall → Sunday dawn only.
 * Regression: after Sunday tzeit, DayInfo keeps civil Sunday but Monday zmanim —
 * that must not keep Motzei Shabbat active all Sunday night.
 */
class MotzeiShabbatWindowTest {

    private val jerusalem = UserProfile(
        timezoneId = "Asia/Jerusalem",
        latitude = 31.7683,
        longitude = 35.2137,
        locationLabel = "Jerusalem, Israel",
        manualCityId = "jlm",
    )

    private val backend = createJewishCalendarBackend()

    /** Sunday night July 12, 2026 — user-reported false Motzei Shabbat. */
    @Test
    fun sundayNightAfterTzeit_isNotMotzeiShabbat() {
        val now = millis(LocalDate(2026, 7, 12), hour = 22, minute = 51)
        val cal = backend.dayInfoAt(now, jerusalem)
        val tomorrow = backend.dayInfoAt(now + 86_400_000L, jerusalem)
        assertTrue(cal.startedTonightAtTzeit, "expected post-tzeit Sunday night rollover")
        assertFalse(MotzeiShabbatWindow.isActive(cal, tomorrow, now))
        assertTrue(TodayOccasionLabels.primary(cal, now, tomorrow)?.label != "Motzei Shabbat")
    }

    @Test
    fun saturdayNightAfterTzeit_isMotzeiShabbat() {
        val now = millis(LocalDate(2026, 7, 11), hour = 22, minute = 30)
        val cal = backend.dayInfoAt(now, jerusalem)
        val tomorrow = backend.dayInfoAt(now + 86_400_000L, jerusalem)
        assertTrue(cal.startedTonightAtTzeit)
        assertTrue(MotzeiShabbatWindow.isActive(cal, tomorrow, now))
    }

    @Test
    fun sundayMorningAfterDawn_isNotMotzeiShabbat() {
        val now = millis(LocalDate(2026, 7, 12), hour = 10, minute = 0)
        val cal = backend.dayInfoAt(now, jerusalem)
        val tomorrow = backend.dayInfoAt(now + 86_400_000L, jerusalem)
        assertFalse(cal.startedTonightAtTzeit)
        assertFalse(MotzeiShabbatWindow.isActive(cal, tomorrow, now))
    }

    private fun millis(date: LocalDate, hour: Int, minute: Int = 0): Long {
        val tz = TimeZone.of("Asia/Jerusalem")
        return LocalDateTime(date.year, date.monthNumber, date.dayOfMonth, hour, minute)
            .toInstant(tz)
            .toEpochMilliseconds()
    }
}
