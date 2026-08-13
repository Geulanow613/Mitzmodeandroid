package com.beardytop.beatzaddik.domain

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SefirahMourningRulesTest {

    @Test
    fun chabad_excludesLagAndErevShavuot() {
        assertFalse(SefirahMourningRules.isMourningDay(omer(33), EffectiveNusach.CHABAD, 0L))
        assertFalse(SefirahMourningRules.isMourningDay(omer(49), EffectiveNusach.CHABAD, 0L))
        assertTrue(SefirahMourningRules.isMourningDay(omer(34), EffectiveNusach.CHABAD, 0L))
        assertTrue(SefirahMourningRules.isMourningDay(omer(48), EffectiveNusach.CHABAD, 0L))
    }

    private fun omer(day: Int) = DayInfo(
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
        omerDay = day,
        isSefiratHaomer = true,
    )
}
