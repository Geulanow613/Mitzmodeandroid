package com.beardytop.beatzaddik.domain

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ZecherMachatzitHaShekelTest {

    @Test
    fun showsOnFastOfEstherAndPurim() {
        val fast = day().copy(fastDayIndex = HebrewCalendarEngine.FAST_OF_ESTHER, activeSeasons = setOf("fast_day"))
        val purim = day().copy(isPurim = true, activeSeasons = setOf("purim"))
        val meshulashFriday = day().copy(activeSeasons = setOf("purim_meshulash_friday"))
        val ordinary = day()

        assertTrue(SeasonalChecklistItems.shouldShowZecherMachatzitHaShekel(fast))
        assertTrue(SeasonalChecklistItems.shouldShowZecherMachatzitHaShekel(purim))
        assertTrue(SeasonalChecklistItems.shouldShowZecherMachatzitHaShekel(meshulashFriday))
        assertFalse(SeasonalChecklistItems.shouldShowZecherMachatzitHaShekel(ordinary))
    }

    @Test
    fun forDay_includesCustomLabeledItem() {
        val cal = day().copy(
            fastDayIndex = HebrewCalendarEngine.FAST_OF_ESTHER,
            activeSeasons = setOf("fast_day"),
        )
        val tomorrow = day()
        val items = SeasonalChecklistItems.forDay(
            cal = cal,
            profile = UserProfile(),
            tomorrowCal = tomorrow,
            dayAfterTomorrowCal = tomorrow,
            nowMillis = 0L,
        )
        val item = items.single { it.id == "zecher_machatzit_hashekel" }
        assertTrue(item.title.contains("(custom)"))
        assertFalse(item.title.contains("if not given already"))
        assertFalse(item.required)
        assertTrue(item.section == "Seasonal")
        assertTrue(item.sortOrder < 0)
    }

    @Test
    fun purimTitle_notesIfNotGivenOnFast() {
        val cal = day().copy(isPurim = true, activeSeasons = setOf("purim"))
        val tomorrow = day()
        val items = SeasonalChecklistItems.forDay(
            cal = cal,
            profile = UserProfile(),
            tomorrowCal = tomorrow,
            dayAfterTomorrowCal = tomorrow,
            nowMillis = 0L,
        )
        val item = items.single { it.id == "zecher_machatzit_hashekel" }
        assertTrue(item.title.contains("if not given already on Fast of Esther"))
    }

    private fun day() = DayInfo(
        date = LocalDate(2026, 3, 2),
        civilLabel = "Monday, March 2, 2026",
        hebrewLabel = "13 Adar 5786",
        parsha = null,
        statusChips = emptyList(),
        isShabbat = false,
        isErevShabbat = false,
        isYomTov = false,
        isShabbatOrYomTov = false,
        activeTimeOfDay = TimeOfDay.DAY,
        activePeriodLabel = "Daytime",
        inactivePeriodHint = null,
        hebrewMonth = HebrewCalendarEngine.ADAR,
        hebrewDay = 13,
        hebrewYear = 5786,
    )
}
