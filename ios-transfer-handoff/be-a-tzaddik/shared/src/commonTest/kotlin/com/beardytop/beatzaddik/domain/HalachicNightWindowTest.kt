package com.beardytop.beatzaddik.domain

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class HalachicNightWindowTest {

    /** Dawn on the same civil morning (after-midnight probes). */
    private val morningDawn = 5L * 60 * 60 * 1000
    /** Dawn after an evening tzeit on the prior civil day (rolled DayInfo zmanim). */
    private val nextMorningDawn = 29L * 60 * 60 * 1000
    private val oneAm = 1L * 60 * 60 * 1000
    private val noon = 12L * 60 * 60 * 1000
    private val tenPm = 22L * 60 * 60 * 1000

    @Test
    fun openAfterTzeitUntilDawn_includingAfterMidnight() {
        val night = day(started = true, dawn = nextMorningDawn)
        assertTrue(HalachicNightWindow.isOpen(night, tenPm))

        val afterMidnight = day(started = false, dawn = morningDawn)
        assertTrue(HalachicNightWindow.isOpen(afterMidnight, oneAm))
        assertFalse(HalachicNightWindow.isOpen(afterMidnight, noon))
    }

    @Test
    fun daytimeBeforeTzeitIsClosed() {
        val day = day(started = false, dawn = morningDawn)
        assertFalse(HalachicNightWindow.isOpen(day, noon))
    }

    @Test
    fun bedikatStaysThroughMidnight_biurWaitsForDawn() {
        val afterMidnight = day(
            started = false,
            dawn = morningDawn,
            hebrewDay = 14,
        )
        assertTrue(
            ErevPesachPrepText.isBedikatNight(
                afterMidnight,
                ErevPesachPrepText.PesachErevDow.WEEKDAY,
                oneAm,
            ),
        )
        assertFalse(
            ErevPesachPrepText.isBiurMorning(
                afterMidnight,
                ErevPesachPrepText.PesachErevDow.WEEKDAY,
                oneAm,
            ),
        )
        assertTrue(
            ErevPesachPrepText.isBiurMorning(
                afterMidnight,
                ErevPesachPrepText.PesachErevDow.WEEKDAY,
                noon,
            ),
        )
    }

    @Test
    fun yaalehMaarivUsesTonightRcAfterMidnight() {
        // After midnight on a one-day RC: still RC night until dawn.
        val cal = day(started = false, dawn = morningDawn).copy(isRoshChodesh = true)
        val tomorrow = day(started = false, dawn = morningDawn).copy(isRoshChodesh = false)
        assertTrue(HalachicNightWindow.isOpen(cal, oneAm))
        assertTrue(cal.isRoshChodesh)
        assertFalse(tomorrow.isRoshChodesh)
        // Visibility rule: night open → use today's RC, not tomorrow's.
        val show = if (HalachicNightWindow.isOpen(cal, oneAm)) cal.isRoshChodesh else tomorrow.isRoshChodesh
        assertTrue(show)
    }

    @Test
    fun nightStartShiftsBackBeforeDawn() {
        val tonightTzeit = 20L * 60 * 60 * 1000
        val z = ZmanimSnapshot(
            timezoneId = "UTC",
            tzeitMillis = tonightTzeit,
            alotHaShacharMillis = morningDawn,
            sunriseMillis = morningDawn + 3_600_000,
        )
        val start = HalachicNightWindow.nightStartMillis(oneAm, z)
        assertEquals(tonightTzeit - 24L * 60 * 60 * 1000, start)
    }

    private fun day(
        started: Boolean,
        dawn: Long?,
        hebrewDay: Int = 10,
    ): DayInfo = DayInfo(
        date = LocalDate(2026, 4, 1),
        civilLabel = "test",
        hebrewLabel = "test",
        parsha = null,
        statusChips = emptyList(),
        isShabbat = false,
        isErevShabbat = false,
        isYomTov = false,
        isShabbatOrYomTov = false,
        activeTimeOfDay = if (started) TimeOfDay.NIGHT else TimeOfDay.DAY,
        activePeriodLabel = "test",
        inactivePeriodHint = null,
        hebrewMonth = HebrewCalendarEngine.NISSAN,
        hebrewDay = hebrewDay,
        startedTonightAtTzeit = started,
        zmanim = dawn?.let {
            ZmanimSnapshot(
                timezoneId = "UTC",
                alotHaShacharMillis = it,
                sunriseMillis = it + 3_600_000,
                tzeitMillis = 20L * 60 * 60 * 1000,
            )
        },
    )
}
