package com.beardytop.beatzaddik.domain

import com.beardytop.beatzaddik.domain.zmanim.SharedZmanimBuilder
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.test.assertEquals

/** Minor/sunset fast zman windows for checklist rows. */
class PublicFastDayZmanTest {

    private val tz = "Asia/Jerusalem"
    private val lat = 31.7683
    private val lon = 35.2137

    private fun fastPrayerDay(fastIdx: Int) = PrayerDayContext(
        isShabbat = false,
        isYomTov = false,
        isRoshChodesh = false,
        isErevShabbat = false,
        fastDayIndex = fastIdx,
    )

    @Test
    fun fastOfEsther_morning_isActive() {
        val now = LocalDateTime(2026, 3, 2, 9, 0)
            .toInstant(TimeZone.of(tz))
            .toEpochMilliseconds()
        val zmanim = SharedZmanimBuilder.buildForLocation(now, tz, lat, lon)
        val status = ChecklistZmanEvaluator.evaluate(
            itemId = "public_fast_day",
            nowMillis = now,
            zmanim = zmanim,
            prayerDay = fastPrayerDay(HebrewCalendarEngine.FAST_OF_ESTHER),
        )
        assertEquals(ItemZmanAvailability.ACTIVE, status.availability)
    }

    @Test
    fun fastOfEsther_beforeDawn_isUpcoming() {
        val zmanim = SharedZmanimBuilder.buildForLocation(
            LocalDateTime(2026, 3, 2, 12, 0).toInstant(TimeZone.of(tz)).toEpochMilliseconds(),
            tz,
            lat,
            lon,
        )
        val dawn = requireNotNull(zmanim.alotHaShacharMillis)
        val status = ChecklistZmanEvaluator.evaluate(
            itemId = "public_fast_day",
            nowMillis = dawn - 60_000L,
            zmanim = zmanim,
            prayerDay = fastPrayerDay(HebrewCalendarEngine.FAST_OF_ESTHER),
        )
        assertEquals(ItemZmanAvailability.UPCOMING, status.availability)
    }

    @Test
    fun fastOfGedaliah_morning_isActive() {
        val now = LocalDateTime(2026, 9, 21, 10, 0)
            .toInstant(TimeZone.of(tz))
            .toEpochMilliseconds()
        val zmanim = SharedZmanimBuilder.buildForLocation(now, tz, lat, lon)
        val status = ChecklistZmanEvaluator.evaluate(
            itemId = "public_fast_day",
            nowMillis = now,
            zmanim = zmanim,
            prayerDay = fastPrayerDay(HebrewCalendarEngine.FAST_OF_GEDALYAH),
        )
        assertEquals(ItemZmanAvailability.ACTIVE, status.availability)
    }

    @Test
    fun tishaBeav_morning_isActive() {
        val now = LocalDateTime(2026, 7, 23, 9, 0)
            .toInstant(TimeZone.of(tz))
            .toEpochMilliseconds()
        val zmanim = SharedZmanimBuilder.buildForLocation(now, tz, lat, lon)
        val status = ChecklistZmanEvaluator.evaluate(
            itemId = "public_fast_day",
            nowMillis = now,
            zmanim = zmanim,
            prayerDay = fastPrayerDay(HebrewCalendarEngine.TISHA_BEAV),
        )
        assertEquals(ItemZmanAvailability.ACTIVE, status.availability)
    }

    @Test
    fun debugClockMismatch_marksActiveFastExpired_withoutFix() {
        val debugMorning = LocalDateTime(2026, 3, 2, 9, 0)
            .toInstant(TimeZone.of(tz))
            .toEpochMilliseconds()
        val zmanim = SharedZmanimBuilder.buildForLocation(debugMorning, tz, lat, lon)
        val resolved = ChecklistZmanEvaluator.evaluate(
            itemId = "public_fast_day",
            nowMillis = debugMorning,
            zmanim = zmanim,
            prayerDay = fastPrayerDay(HebrewCalendarEngine.FAST_OF_ESTHER),
        )
        assertEquals(ItemZmanAvailability.ACTIVE, resolved.availability)
        val wallNow = LocalDateTime(2026, 7, 6, 22, 0)
            .toInstant(TimeZone.of(tz))
            .toEpochMilliseconds()
        val wronglyExpired = ZmanAvailabilityLive.derive(
            availability = resolved.availability,
            windowStartMillis = resolved.windowStartMillis,
            windowEndMillis = resolved.windowEndMillis,
            nowMillis = wallNow,
        )
        assertEquals(ItemZmanAvailability.EXPIRED, wronglyExpired)
        val withDebugClock = ZmanAvailabilityLive.derive(
            availability = resolved.availability,
            windowStartMillis = resolved.windowStartMillis,
            windowEndMillis = resolved.windowEndMillis,
            nowMillis = debugMorning,
        )
        assertEquals(ItemZmanAvailability.ACTIVE, withDebugClock)
    }
}
