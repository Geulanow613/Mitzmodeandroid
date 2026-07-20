package com.beardytop.beatzaddik.domain

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MourningPeriodRulesTest {

    @Test
    fun notSure_mapsToGenericOtherNotChabad() {
        assertEquals(EffectiveNusach.OTHER, NusachSelection.NOT_SURE.toEffective())
        assertEquals(EffectiveNusach.OTHER, NusachSelection.OTHER.toEffective())
        assertEquals(EffectiveNusach.CHABAD, NusachSelection.CHABAD.toEffective())
    }

    @Test
    fun sephardi_nineDays_onlyInShavuahNotFromRoshChodeshAv() {
        // 2026: 1 Av = Thu Jul 16; 9 Av = Fri Jul 24 → shavuah starts Motzei Sat Jul 18 = Sun 4 Av.
        val roshChodeshAv = day(LocalDate(2026, 7, 16), hebrewDay = 1)
        assertEquals(DayOfWeek.THURSDAY, roshChodeshAv.date.dayOfWeek)
        assertTrue(
            MourningPeriodRules.isInNineDaysPeriod(roshChodeshAv, 0L, EffectiveNusach.ASHKENAZ),
        )
        assertFalse(
            MourningPeriodRules.isInNineDaysPeriod(roshChodeshAv, 0L, EffectiveNusach.SEFARD),
        )
        assertFalse(
            MourningPeriodRules.isInNineDaysPeriod(roshChodeshAv, 0L, EffectiveNusach.EDOT_HAMIZRACH),
        )

        val inShavuah = day(LocalDate(2026, 7, 20), hebrewDay = 5)
        assertEquals(DayOfWeek.MONDAY, inShavuah.date.dayOfWeek)
        assertTrue(
            MourningPeriodRules.isInNineDaysPeriod(inShavuah, 0L, EffectiveNusach.SEFARD),
        )
        assertTrue(
            MourningPeriodRules.isInNineDaysPeriod(inShavuah, 0L, EffectiveNusach.EDOT_HAMIZRACH),
        )
    }

    private fun day(date: LocalDate, hebrewDay: Int) = DayInfo(
        date = date,
        civilLabel = "",
        hebrewLabel = "",
        parsha = null,
        statusChips = emptyList(),
        isShabbat = false,
        isErevShabbat = false,
        isYomTov = false,
        isShabbatOrYomTov = false,
        activeTimeOfDay = TimeOfDay.DAY,
        activePeriodLabel = "DAY",
        inactivePeriodHint = null,
        hebrewMonth = HebrewCalendarEngine.AV,
        hebrewDay = hebrewDay,
        hebrewYear = 5786,
    )
}
