package com.beardytop.beatzaddik.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

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
}
