package com.beardytop.beatzaddik.domain

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PurimMeshulashTextTest {

    @Test
    fun nightMegillah_afterMidnightBeforeDawn_stillShows() {
        val dawn = 1_000_000L
        val cal = meshulashFriday().copy(
            date = LocalDate(2026, 3, 6), // Friday
            startedTonightAtTzeit = false,
            zmanim = ZmanimSnapshot(
                timezoneId = "Asia/Jerusalem",
                alotHaShacharMillis = dawn,
                sunriseMillis = dawn + 60_000L,
            ),
        )
        assertTrue(
            PurimMeshulashText.isMeshulashFridayNightMegillahWindow(cal, nowMillis = dawn - 60_000L),
        )
        assertFalse(
            PurimMeshulashText.isMeshulashFridayNightMegillahWindow(cal, nowMillis = dawn + 1L),
        )
    }

    @Test
    fun nightMegillah_afterTzeitRollover_showsUntilDawn() {
        val dawn = 2_000_000L
        val cal = meshulashFriday().copy(
            date = LocalDate(2026, 3, 5), // Thursday civil after tzeit
            startedTonightAtTzeit = true,
            zmanim = ZmanimSnapshot(
                timezoneId = "Asia/Jerusalem",
                alotHaShacharMillis = dawn,
            ),
        )
        assertTrue(
            PurimMeshulashText.isMeshulashFridayNightMegillahWindow(cal, nowMillis = dawn - 1L),
        )
    }

    private fun meshulashFriday() = DayInfo(
        date = LocalDate(2026, 3, 6),
        civilLabel = "Friday",
        hebrewLabel = "14 Adar",
        parsha = null,
        statusChips = emptyList(),
        isShabbat = false,
        isErevShabbat = true,
        isYomTov = false,
        isShabbatOrYomTov = false,
        activeTimeOfDay = TimeOfDay.NIGHT,
        activePeriodLabel = "Night",
        inactivePeriodHint = null,
        activeSeasons = setOf("purim_meshulash_friday"),
    )
}
