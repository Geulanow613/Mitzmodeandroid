package com.beardytop.beatzaddik.domain

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class HalachicDayRolloverTest {

    @Test
    fun sanitize_clearsErevShabbatAndErevChagAfterTzeitRollover() {
        val rolled = DayInfo(
            date = LocalDate(2026, 7, 3),
            civilLabel = "Friday, July 3, 2026",
            hebrewLabel = "18 Tammuz 5786",
            parsha = null,
            statusChips = listOf("Friday", "Erev Shabbat", "Erev Pesach"),
            isShabbat = false,
            isErevShabbat = true,
            isYomTov = false,
            isShabbatOrYomTov = false,
            activeTimeOfDay = TimeOfDay.DAY,
            activePeriodLabel = "Daytime",
            inactivePeriodHint = null,
            activeSeasons = setOf("erev_chag"),
            upcomingChagName = "Pesach",
            upcomingChagYomTovIndex = HebrewCalendarEngine.PESACH,
        )
        val out = HalachicDayRollover.sanitizeAfterTzeitRollover(
            rolled = rolled,
            civilDate = LocalDate(2026, 7, 2),
            civilLabel = "Thursday, July 2, 2026",
            statusChips = listOf("Thursday", "Erev Pesach"),
            activeTimeOfDay = TimeOfDay.NIGHT,
            activePeriodLabel = "Night",
            activePeriodHint = null,
            inactivePeriodHint = null,
        )
        assertTrue(out.startedTonightAtTzeit)
        assertFalse(out.isErevShabbat)
        assertFalse("erev_chag" in out.activeSeasons)
        assertNull(out.upcomingChagName)
        assertNull(out.upcomingChagYomTovIndex)
        assertFalse(out.statusChips.any { it.startsWith("Erev ") && it != "Erev Shabbat" })
        assertFalse(TonightHolyDayRules.tonightBeginsShabbat(out))
        assertNull(MaarivInAppRules.blockedTonightLabel(out, null, false))
    }

    @Test
    fun sanitize_keepsShabbatWhenRollingFridayIntoShabbat() {
        val rolled = DayInfo(
            date = LocalDate(2026, 7, 4),
            civilLabel = "Saturday, July 4, 2026",
            hebrewLabel = "19 Tammuz 5786",
            parsha = "Pinchas",
            statusChips = listOf("Saturday", "Shabbat"),
            isShabbat = true,
            isErevShabbat = false,
            isYomTov = false,
            isShabbatOrYomTov = true,
            activeTimeOfDay = TimeOfDay.DAY,
            activePeriodLabel = "Daytime",
            inactivePeriodHint = null,
        )
        val out = HalachicDayRollover.sanitizeAfterTzeitRollover(
            rolled = rolled,
            civilDate = LocalDate(2026, 7, 3),
            civilLabel = "Friday, July 3, 2026",
            statusChips = listOf("Friday", "Shabbat"),
            activeTimeOfDay = TimeOfDay.NIGHT,
            activePeriodLabel = "Night",
            activePeriodHint = null,
            inactivePeriodHint = null,
        )
        assertTrue(out.isShabbat)
        assertFalse(out.isErevShabbat)
        assertTrue(out.startedTonightAtTzeit)
    }
}
