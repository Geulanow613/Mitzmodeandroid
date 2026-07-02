package com.beardytop.beatzaddik.domain

import com.beardytop.beatzaddik.domain.zmanim.SharedZmanimBuilder
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class ZmanAvailabilityLiveTest {

    @Test
    fun staleUpcomingAfterWindowStart_becomesActive() {
        val start = 1_000_000L
        val end = 2_000_000L
        assertEquals(
            ItemZmanAvailability.ACTIVE,
            ZmanAvailabilityLive.derive(
                ItemZmanAvailability.UPCOMING,
                windowStartMillis = start,
                windowEndMillis = end,
                nowMillis = start + 1,
            ),
        )
    }

    @Test
    fun staleUpcomingAfterWindowEnd_becomesExpired() {
        val start = 1_000_000L
        val end = 2_000_000L
        assertEquals(
            ItemZmanAvailability.EXPIRED,
            ZmanAvailabilityLive.derive(
                ItemZmanAvailability.UPCOMING,
                windowStartMillis = start,
                windowEndMillis = end,
                nowMillis = end + 1,
            ),
        )
    }
}

class ZmanCountdownPastStartTest {

    @Test
    fun pastWindowStart_doesNotClaimAvailableNow() {
        val now = 5_000_000L
        val dawn = now - 60_000L
        val result = ZmanCountdownFormatter.upcomingSummaryTemplate(
            windowStartMillis = dawn,
            nowMillis = now,
            atLabel = "dawn",
            timezoneId = "Asia/Jerusalem",
            languageCode = "en",
        )
        assertEquals(null, result)
    }

    @Test
    fun jerusalem350pm_eveningShema_showsCountdownNotAvailableNow() {
        val tz = "Asia/Jerusalem"
        val lat = 31.7683
        val lon = 35.2137
        val now = LocalDateTime(2026, 7, 2, 15, 50)
            .toInstant(TimeZone.of(tz))
            .toEpochMilliseconds()
        val zmanim = SharedZmanimBuilder.buildForLocation(now, tz, lat, lon)
        val sunset = requireNotNull(zmanim.sunsetMillis)
        assertTrue(now < sunset, "3:50pm should be before sunset")

        val resolved = ChecklistItemResolver.resolve(
            item = ChecklistItemDef(
                id = "evening_shema_with_its_blessings",
                title = "Shema with its blessings",
                section = "Evening Prayer",
                timeOfDay = TimeOfDay.NIGHT,
                required = true,
                situational = false,
            ),
            profile = UserProfile(
                timezoneId = tz,
                latitude = lat,
                longitude = lon,
                locationLabel = "Jerusalem",
            ),
            checked = false,
            nowMillis = now,
            zmanim = zmanim,
            prayerDay = PrayerDayContext.from(
                DayInfo(
                    date = kotlinx.datetime.LocalDate(2026, 7, 2),
                    civilLabel = "",
                    hebrewLabel = "",
                    parsha = null,
                    statusChips = emptyList(),
                    isShabbat = false,
                    isErevShabbat = false,
                    isYomTov = false,
                    isShabbatOrYomTov = false,
                    activeTimeOfDay = TimeOfDay.DAY,
                    activePeriodLabel = "Day",
                    inactivePeriodHint = null,
                    activeSeasons = emptySet(),
                    hebrewYear = 5786,
                    hebrewMonth = HebrewCalendarEngine.TAMMUZ,
                    hebrewDay = 16,
                    zmanim = zmanim,
                ),
                EffectiveNusach.ASHKENAZ,
                lat,
            ),
        )
        assertEquals(ItemZmanAvailability.UPCOMING, resolved.zmanAvailability)
        val (key, _) = ZmanCountdownFormatter.upcomingSummaryTemplate(
            windowStartMillis = resolved.zmanWindowStartMillis,
            nowMillis = now,
            atLabel = resolved.zmanAvailableAtLabel,
            timezoneId = tz,
            languageCode = "en",
        )!!
        assertEquals("Available in {countdown}{at}", key)
        assertNotEquals("Available now{at}", key)
    }
}
