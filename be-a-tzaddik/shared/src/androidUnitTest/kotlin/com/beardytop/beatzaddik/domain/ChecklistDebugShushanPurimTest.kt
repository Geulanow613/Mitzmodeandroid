package com.beardytop.beatzaddik.domain

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ChecklistDebugShushanPurimTest {

    private val jerusalem = UserProfile(
        timezoneId = "Asia/Jerusalem",
        latitude = 31.7683,
        longitude = 35.2137,
        locationLabel = "Jerusalem, Israel",
        manualCityId = "jlm",
    )

    private val calendar = JewishCalendarService(createJewishCalendarBackend())

    @Test
    fun shushan5786_12Adar_resolvesToMarch1() {
        val scenario = ChecklistDebugScenarios.byId("shushan_12adar_day")!!
        val override = ChecklistDebugDateFinder.resolve(
            calendar,
            jerusalem,
            scenario,
            ChecklistDebugTimeSlot.MORNING,
        )
        assertNotNull(override)
        assertEquals(12, calendar.dayInfoAt(override.epochMillis, jerusalem).hebrewDay)
        assertEquals(2026, override.simulatedDate.year)
        assertEquals(3, override.simulatedDate.monthNumber)
        assertEquals(1, override.simulatedDate.dayOfMonth)
    }

    @Test
    fun shushan5786_15Adar_resolvesWithPurimSeason() {
        val scenario = ChecklistDebugScenarios.byId("shushan_day_day")!!
        val override = ChecklistDebugDateFinder.resolve(
            calendar,
            jerusalem,
            scenario,
            ChecklistDebugTimeSlot.MORNING,
        )
        assertNotNull(override)
        val info = calendar.dayInfoAt(override.epochMillis, jerusalem)
        assertEquals(15, info.hebrewDay)
        assertEquals(true, info.isPurim)
    }

    @Test
    fun meshulashFriday_findsPurimMeshulashSeason() {
        ChecklistDebugDateFinder.clearCache()
        val scenario = ChecklistDebugScenarios.byId("shushan_meshulash_fri_day")!!
        val override = ChecklistDebugDateFinder.resolve(
            calendar,
            jerusalem,
            scenario,
            ChecklistDebugTimeSlot.MORNING,
        )
        assertNotNull(override)
        val info = calendar.dayInfoAt(override.epochMillis, jerusalem)
        assertTrue("purim_meshulash_friday" in info.activeSeasons)
    }

    @Test
    fun meshulashFriday_resolvesForNonJerusalemProfileWhenDebugCalendarApplied() {
        val nyc = UserProfile(
            timezoneId = "America/New_York",
            latitude = 40.7128,
            longitude = -74.0060,
            locationLabel = "New York",
        )
        ChecklistDebugDateFinder.clearCache()
        val scenario = ChecklistDebugScenarios.byId("shushan_meshulash_fri_day")!!
        val override = ChecklistDebugDateFinder.resolve(
            calendar,
            nyc.forDebugCalendar(scenario),
            scenario,
            ChecklistDebugTimeSlot.MORNING,
        )
        assertNotNull(override)
        val info = calendar.dayInfoAt(override.epochMillis, nyc.forDebugCalendar(scenario))
        assertTrue("purim_meshulash_friday" in info.activeSeasons)
    }

    @Test
    fun shushanScenarios_requireJerusalemProfile() {
        val nyc = UserProfile(
            timezoneId = "America/New_York",
            latitude = 40.7128,
            longitude = -74.0060,
            locationLabel = "New York",
        )
        ChecklistDebugDateFinder.clearCache()
        val scenario = ChecklistDebugScenarios.byId("shushan_day_day")!!
        val override = ChecklistDebugDateFinder.resolve(
            calendar,
            nyc,
            scenario,
            ChecklistDebugTimeSlot.MORNING,
        )
        // 15 Adar in NYC is not Purim day (14 Adar is).
        val info = override?.let { calendar.dayInfoAt(it.epochMillis, nyc) }
        if (info != null) {
            assertEquals(false, info.isPurim)
        } else {
            assertNull(override)
        }
    }
}
