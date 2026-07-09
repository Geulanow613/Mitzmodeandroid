package com.beardytop.beatzaddik.domain

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SeasonalChecklistItemsTest {

    private val morningMillis = 9L * 60 * 60 * 1000

    @Test
    fun threeWeeksItemStaysActiveAtNight() {
        val items = SeasonalChecklistItems.forDay(
            cal = sampleDayInfo(
                date = LocalDate(2026, 7, 7),
                activeTimeOfDay = TimeOfDay.NIGHT,
                hebrewMonth = HebrewCalendarEngine.TAMMUZ,
                hebrewDay = 21,
            ),
            profile = sampleProfile(),
            tomorrowCal = sampleDayInfo(
                date = LocalDate(2026, 7, 8),
                activeTimeOfDay = TimeOfDay.DAY,
                hebrewMonth = HebrewCalendarEngine.TAMMUZ,
                hebrewDay = 22,
            ),
            dayAfterTomorrowCal = sampleDayInfo(
                date = LocalDate(2026, 7, 9),
                activeTimeOfDay = TimeOfDay.DAY,
                hebrewMonth = HebrewCalendarEngine.TAMMUZ,
                hebrewDay = 23,
            ),
            nowMillis = morningMillis,
        )

        val threeWeeks = items.first { it.id == "three_weeks_mourning_customs" }
        assertEquals(TimeOfDay.ANY, threeWeeks.timeOfDay)
    }

    @Test
    fun nineDaysShowsBothMourningItemsDuringOverlap() {
        val items = SeasonalChecklistItems.forDay(
            cal = sampleDayInfo(
                date = LocalDate(2026, 7, 16),
                activeTimeOfDay = TimeOfDay.NIGHT,
                hebrewMonth = HebrewCalendarEngine.AV,
                hebrewDay = 1,
            ),
            profile = sampleProfile(),
            tomorrowCal = sampleDayInfo(
                date = LocalDate(2026, 7, 17),
                activeTimeOfDay = TimeOfDay.DAY,
                hebrewMonth = HebrewCalendarEngine.AV,
                hebrewDay = 2,
            ),
            dayAfterTomorrowCal = sampleDayInfo(
                date = LocalDate(2026, 7, 18),
                activeTimeOfDay = TimeOfDay.DAY,
                hebrewMonth = HebrewCalendarEngine.AV,
                hebrewDay = 3,
            ),
            nowMillis = morningMillis,
        )

        assertTrue(items.any { it.id == "nine_days_mourning_customs" })
        assertTrue(items.any { it.id == "three_weeks_mourning_customs" })
    }

    @Test
    fun tuBshvatSederAvailableAllDayAndNight() {
        val items = SeasonalChecklistItems.forDay(
            cal = sampleDayInfo(
                date = LocalDate(2026, 2, 3),
                activeTimeOfDay = TimeOfDay.DAY,
                hebrewMonth = HebrewCalendarEngine.SHEVAT,
                hebrewDay = 15,
            ),
            profile = sampleProfile(),
            tomorrowCal = sampleDayInfo(
                date = LocalDate(2026, 2, 4),
                activeTimeOfDay = TimeOfDay.NIGHT,
                hebrewMonth = HebrewCalendarEngine.SHEVAT,
                hebrewDay = 16,
            ),
            dayAfterTomorrowCal = sampleDayInfo(
                date = LocalDate(2026, 2, 5),
                activeTimeOfDay = TimeOfDay.DAY,
                hebrewMonth = HebrewCalendarEngine.SHEVAT,
                hebrewDay = 17,
            ),
            nowMillis = morningMillis,
        )

        val seder = items.first { it.id == "tu_bshvat_seder_optional" }
        assertEquals(TimeOfDay.ANY, seder.timeOfDay)
    }

    @Test
    fun mourningItemsHiddenAfterChatzosOnTenAv() {
        val zmanim = ZmanimSnapshot(
            chatzosMillis = 1_000L,
            timezoneId = "America/New_York",
        )
        val items = SeasonalChecklistItems.forDay(
            cal = sampleDayInfo(
                date = LocalDate(2026, 7, 25),
                activeTimeOfDay = TimeOfDay.DAY,
                hebrewMonth = HebrewCalendarEngine.AV,
                hebrewDay = 10,
                zmanim = zmanim,
            ),
            profile = sampleProfile(),
            tomorrowCal = sampleDayInfo(
                date = LocalDate(2026, 7, 26),
                activeTimeOfDay = TimeOfDay.DAY,
                hebrewMonth = HebrewCalendarEngine.AV,
                hebrewDay = 11,
            ),
            dayAfterTomorrowCal = sampleDayInfo(
                date = LocalDate(2026, 7, 27),
                activeTimeOfDay = TimeOfDay.DAY,
                hebrewMonth = HebrewCalendarEngine.AV,
                hebrewDay = 12,
            ),
            nowMillis = 2_000L,
        )

        assertFalse(items.any { it.id == "nine_days_mourning_customs" })
        assertFalse(items.any { it.id == "three_weeks_mourning_customs" })
    }

    private fun sampleProfile() = UserProfile(
        timezoneId = "America/New_York",
        latitude = 40.7128,
        longitude = -74.0060,
        locationLabel = "New York",
    )

    private fun sampleDayInfo(
        date: LocalDate,
        activeTimeOfDay: TimeOfDay,
        hebrewMonth: Int,
        hebrewDay: Int,
        zmanim: ZmanimSnapshot? = null,
    ) = DayInfo(
        date = date,
        civilLabel = "",
        hebrewLabel = "",
        parsha = null,
        statusChips = emptyList(),
        isShabbat = false,
        isErevShabbat = false,
        isYomTov = false,
        isShabbatOrYomTov = false,
        activeTimeOfDay = activeTimeOfDay,
        activePeriodLabel = activeTimeOfDay.name,
        inactivePeriodHint = null,
        activeSeasons = emptySet(),
        hebrewMonth = hebrewMonth,
        hebrewDay = hebrewDay,
        hebrewYear = 5786,
        zmanim = zmanim,
    )
}
