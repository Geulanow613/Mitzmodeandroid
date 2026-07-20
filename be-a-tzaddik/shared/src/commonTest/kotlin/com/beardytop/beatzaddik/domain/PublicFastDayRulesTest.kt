package com.beardytop.beatzaddik.domain

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PublicFastDayRulesTest {

    @Test
    fun resolveFastDayIndexUsesTomorrowWhenSunsetFastAlreadyStarted() {
        val idx = PublicFastDayRules.resolveFastDayIndex(
            todayIdx = 0,
            tomorrowIdx = HebrewCalendarEngine.TISHA_BEAV,
            isTaanis = true,
        )
        assertEquals(HebrewCalendarEngine.TISHA_BEAV, idx)
    }

    @Test
    fun resolveFastDayIndexReturnsNullWhenNotFasting() {
        assertNull(
            PublicFastDayRules.resolveFastDayIndex(
                todayIdx = 0,
                tomorrowIdx = HebrewCalendarEngine.TISHA_BEAV,
                isTaanis = false,
            ),
        )
    }

    @Test
    fun deferredSunday17Tammuz_detectedForMotzeiPrep() {
        // Sunday 18 Tammuz = deferred Fast of 17 Tammuz.
        val sunday = DayInfo(
            date = LocalDate(2024, 7, 21), // a Sunday
            civilLabel = "Sunday",
            hebrewLabel = "18 Tammuz",
            parsha = null,
            statusChips = emptyList(),
            isShabbat = false,
            isErevShabbat = false,
            isYomTov = false,
            isShabbatOrYomTov = false,
            activeTimeOfDay = TimeOfDay.NIGHT,
            activePeriodLabel = "Night",
            inactivePeriodHint = null,
            hebrewMonth = HebrewCalendarEngine.TAMMUZ,
            hebrewDay = 18,
            fastDayIndex = HebrewCalendarEngine.SEVENTEEN_OF_TAMMUZ,
            startedTonightAtTzeit = true,
        )
        assertEquals(DayOfWeek.SUNDAY, sunday.date.dayOfWeek)
        assertTrue(PublicFastDayRules.isDeferredFromShabbat(sunday))
        assertEquals(
            HebrewCalendarEngine.SEVENTEEN_OF_TAMMUZ,
            PublicFastDayRules.deferredSundayMinorFastIndexForMotzeiPrep(sunday, sunday),
        )
    }
}
