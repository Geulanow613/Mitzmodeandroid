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
        val meshulashShabbat = day().copy(activeSeasons = setOf("purim_meshulash_shabbat"), isShabbat = true)
        val meshulashSunday = day().copy(activeSeasons = setOf("purim_meshulash_sunday"))
        val ordinary = day()

        assertTrue(SeasonalChecklistItems.shouldShowZecherMachatzitHaShekel(fast))
        assertTrue(SeasonalChecklistItems.shouldShowZecherMachatzitHaShekel(purim))
        assertTrue(SeasonalChecklistItems.shouldShowZecherMachatzitHaShekel(meshulashFriday))
        assertFalse(SeasonalChecklistItems.shouldShowZecherMachatzitHaShekel(meshulashShabbat))
        assertFalse(SeasonalChecklistItems.shouldShowZecherMachatzitHaShekel(meshulashSunday))
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
        val item = items.single { it.id.startsWith("zecher_machatzit_hashekel") }
        assertTrue(item.title.contains("(custom)"))
        assertFalse(item.title.contains("if not given already"))
        assertFalse(item.required)
        assertTrue(item.persistChecked)
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
        val item = items.single { it.id.startsWith("zecher_machatzit_hashekel") }
        assertTrue(item.title.contains("if not given already on Fast of Esther"))
        assertTrue(item.persistChecked)
    }

    @Test
    fun sameHebrewYear_sharesPersistedIdAcrossFastAndPurim() {
        val fast = day().copy(
            fastDayIndex = HebrewCalendarEngine.FAST_OF_ESTHER,
            activeSeasons = setOf("fast_day"),
            hebrewYear = 5786,
        )
        val purim = day().copy(
            isPurim = true,
            activeSeasons = setOf("purim"),
            hebrewYear = 5786,
            hebrewDay = 14,
        )
        val tomorrow = day()
        val fastItem = SeasonalChecklistItems.forDay(
            fast, UserProfile(), tomorrow, tomorrow, 0L,
        ).single { it.id.startsWith("zecher_machatzit_hashekel") }
        val purimItem = SeasonalChecklistItems.forDay(
            purim, UserProfile(), tomorrow, tomorrow, 0L,
        ).single { it.id.startsWith("zecher_machatzit_hashekel") }
        assertTrue(fastItem.id == purimItem.id)
        assertTrue(fastItem.id == "zecher_machatzit_hashekel_5786")
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
