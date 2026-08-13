package com.beardytop.beatzaddik.domain

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RoshChodeshRulesTest {

    @Test
    fun hallelKind_onCholHamoedSukkot_isNoneSoOnlyFullHallelShows() {
        val cal = day(
            hebrewMonth = HebrewCalendarEngine.TISHREI,
            hebrewDay = 17,
            isRoshChodesh = true,
        ).copy(activeSeasons = setOf("chol_hamoed_sukkot", "rosh_chodesh"))
        assertEquals(RoshChodeshRules.HallelKind.NONE, RoshChodeshRules.hallelKind(cal))
    }

    @Test
    fun hallelKind_onCholHamoedPesach_isNoneSoOnlyChmHalfShows() {
        val cal = day(
            hebrewMonth = HebrewCalendarEngine.NISSAN,
            hebrewDay = 17,
            isRoshChodesh = true,
        ).copy(activeSeasons = setOf("chol_hamoed_pesach", "rosh_chodesh"))
        assertEquals(RoshChodeshRules.HallelKind.NONE, RoshChodeshRules.hallelKind(cal))
    }

    @Test
    fun hallelKind_ordinaryRoshChodesh_isHalf() {
        val cal = day(
            hebrewMonth = HebrewCalendarEngine.SIVAN,
            hebrewDay = 1,
            isRoshChodesh = true,
        )
        assertEquals(RoshChodeshRules.HallelKind.HALF, RoshChodeshRules.hallelKind(cal))
    }

    @Test
    fun twoDay_whenHebrewDayIs30() {
        val cal = day(
            hebrewMonth = HebrewCalendarEngine.SIVAN,
            hebrewDay = 30,
            isRoshChodesh = true,
        )
        assertTrue(RoshChodeshRules.isTwoDayObservance(cal))
        assertTrue(RoshChodeshRules.isFirstDayOfTwoDay(cal))
    }

    @Test
    fun twoDay_onFirstOfMonth_whenPreviousMonthHas30Days() {
        // Elul always has 29 days → 1 Tishrei is NOT ordinary RC (it's RH).
        // Iyar has 29 → 1 Sivan is one-day RC.
        val oneDay = day(
            hebrewMonth = HebrewCalendarEngine.SIVAN,
            hebrewDay = 1,
            hebrewYear = 5786,
            isRoshChodesh = true,
        )
        assertFalse(RoshChodeshRules.isTwoDayObservance(oneDay))

        // Nissan has 30 days → 1 Iyar is second day of two-day RC.
        val secondDay = day(
            hebrewMonth = HebrewCalendarEngine.IYAR,
            hebrewDay = 1,
            hebrewYear = 5786,
            isRoshChodesh = true,
        )
        assertTrue(RoshChodeshRules.isTwoDayObservance(secondDay))
        assertFalse(RoshChodeshRules.isFirstDayOfTwoDay(secondDay))
    }

    private fun day(
        hebrewMonth: Int,
        hebrewDay: Int,
        hebrewYear: Int = 5786,
        isRoshChodesh: Boolean,
    ) = DayInfo(
        date = LocalDate(2026, 5, 1),
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
        hebrewMonth = hebrewMonth,
        hebrewDay = hebrewDay,
        hebrewYear = hebrewYear,
        isRoshChodesh = isRoshChodesh,
    )
}
