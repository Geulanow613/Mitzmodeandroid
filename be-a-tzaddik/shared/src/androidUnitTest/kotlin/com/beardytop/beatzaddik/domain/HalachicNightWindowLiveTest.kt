package com.beardytop.beatzaddik.domain

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Live backend probes: civil midnight must not break night mitzvot that run until dawn.
 */
class HalachicNightWindowLiveTest {
    private val jlm = UserProfile(
        timezoneId = "Asia/Jerusalem",
        latitude = 31.7683,
        longitude = 35.2137,
        locationLabel = "Jerusalem",
        manualCityId = "jlm",
    )
    private val backend = createJewishCalendarBackend()

    private fun at(y: Int, m: Int, d: Int, h: Int, min: Int = 0): DayInfo {
        val ms = LocalDateTime(y, m, d, h, min)
            .toInstant(TimeZone.of("Asia/Jerusalem"))
            .toEpochMilliseconds()
        return backend.dayInfoAt(ms, jlm)
    }

    private fun millis(y: Int, m: Int, d: Int, h: Int, min: Int = 0): Long =
        LocalDateTime(y, m, d, h, min)
            .toInstant(TimeZone.of("Asia/Jerusalem"))
            .toEpochMilliseconds()

    @Test
    fun chanukah_night_stable_after_midnight() {
        val eve = at(2025, 12, 18, 22)
        val amMs = millis(2025, 12, 19, 1)
        val am = backend.dayInfoAt(amMs, jlm)
        val eveNight = SeasonalChecklistItems.chanukahLightingNight(eve, millis(2025, 12, 18, 22))
        val amNight = SeasonalChecklistItems.chanukahLightingNight(am, amMs)
        assertEquals(eve.chanukahDay, am.chanukahDay)
        assertEquals(eveNight, amNight)
        assertTrue(HalachicNightWindow.isOpen(am, amMs))
    }

    @Test
    fun bedikat_remains_biur_hidden_after_midnight() {
        val amMs = millis(2026, 4, 1, 1)
        val am = backend.dayInfoAt(amMs, jlm)
        val dow = ErevPesachPrepText.pesachErevDow(am)!!
        assertTrue(ErevPesachPrepText.isBedikatNight(am, dow, amMs))
        assertFalse(ErevPesachPrepText.isBiurMorning(am, dow, amMs))
    }

    @Test
    fun omer_priority_after_midnight() {
        // Pick a day in the Omer: 15 Iyar 5786 ≈ May 3 2026
        val amMs = millis(2026, 5, 3, 1)
        val am = backend.dayInfoAt(amMs, jlm)
        if (am.isSefiratHaomer && am.omerDay != null) {
            assertTrue(HalachicNightWindow.isOpen(am, amMs))
            assertTrue(OmerCountText.isOmerCountPriority(amMs, am))
        }
    }
}
