package com.beardytop.beatzaddik.domain

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Motzei Shabbat advances the displayed parsha; Bereishit waits until Motzei Simchat Torah.
 */
class WeeklyParshaLogicTest {

    private val jerusalem = UserProfile(
        timezoneId = "Asia/Jerusalem",
        latitude = 31.7683,
        longitude = 35.2137,
        locationLabel = "Jerusalem, Israel",
        manualCityId = "jlm",
    )

    private val backend = createJewishCalendarBackend()
    private val engine = ChecklistEngine(
        calendar = JewishCalendarService(backend),
        catalog = emptyList(),
    )

    @Test
    fun shabbatAfternoon_matosMasei_notDevarim() {
        val cal = dayInfo(LocalDate(2026, 7, 11), 15)
        val tomorrow = dayInfo(LocalDate(2026, 7, 12), 12)
        val now = millis(LocalDate(2026, 7, 11), 15)
        assertFalse(WeeklyParshaLogic.shouldShowNextWeeksParsha(cal, tomorrow, now))
        assertEquals(
            "MATOS_MASEI",
            WeeklyParshaLogic.displayParshaKey(cal, tomorrow, now, isInIsrael = true),
        )
        assertEquals(
            "Matot-Masei",
            WeeklyParshaLogic.displayParshaLabel(cal, tomorrow, now, isInIsrael = true),
        )
    }

    @Test
    fun motzeiSaturdayNight_switchesToDevarim() {
        val cal = dayInfo(LocalDate(2026, 7, 11), 21)
        val tomorrow = dayInfo(LocalDate(2026, 7, 12), 12)
        val now = millis(LocalDate(2026, 7, 11), 21)
        assertTrue(WeeklyParshaLogic.shouldShowNextWeeksParsha(cal, tomorrow, now))
        assertEquals(
            "DEVARIM",
            WeeklyParshaLogic.displayParshaKey(cal, tomorrow, now, isInIsrael = true),
        )
    }

    @Test
    fun sundayMorningMotzei_headerIsDevarimNotMatos() {
        val now = millis(LocalDate(2026, 7, 12), 3)
        val day = engine.resolve(
            profile = jerusalem,
            checkedById = emptyMap(),
            customItems = emptyList(),
            customChecked = emptyMap(),
            nowMillis = now,
        )
        assertEquals("Devarim", day.header.parshaLabel)
        assertTrue("Motzei Shabbat" in day.header.statusChips)
    }

    @Test
    fun beforeSimchatTorah_bereishitUpcoming_staysVzot() {
        // Sunday after Ha'azinu (2026-09-19) — library already jumps to Bereishit.
        val cal = dayInfo(LocalDate(2026, 9, 20), 10)
        val tomorrow = dayInfo(LocalDate(2026, 9, 21), 12)
        val now = millis(LocalDate(2026, 9, 20), 10)
        assertEquals("BERESHIS", WeeklyParshaLogic.normalizeKey(cal.upcomingShabbatParsha))
        assertFalse(WeeklyParshaLogic.simchatTorahCycleEnded(cal, now, isInIsrael = true))
        assertEquals(
            "VZOS_HABERACHA",
            WeeklyParshaLogic.displayParshaKey(cal, tomorrow, now, isInIsrael = true),
        )
        assertEquals(
            "Vezot Habracha",
            WeeklyParshaLogic.displayParshaLabel(cal, tomorrow, now, isInIsrael = true),
        )
    }

    @Test
    fun simchatTorahDayIsrael_beforeTzeit_showsVzotNotBereishit() {
        // Israel 5787: Shemini Atzeret / Simchat Torah = Sat Oct 3, 2026
        val cal = dayInfo(LocalDate(2026, 10, 3), 15)
        val tomorrow = dayInfo(LocalDate(2026, 10, 4), 12)
        val now = millis(LocalDate(2026, 10, 3), 15)
        assertTrue(WeeklyParshaLogic.isSimchatTorahDay(cal, isInIsrael = true))
        assertEquals(
            "VZOS_HABERACHA",
            WeeklyParshaLogic.displayParshaKey(cal, tomorrow, now, isInIsrael = true),
        )
    }

    @Test
    fun motzeiSimchatTorah_switchesToBereishit() {
        val cal = dayInfo(LocalDate(2026, 10, 3), 21)
        val tomorrow = dayInfo(LocalDate(2026, 10, 4), 12)
        val now = millis(LocalDate(2026, 10, 3), 21)
        assertTrue(WeeklyParshaLogic.simchatTorahCycleEnded(cal, now, isInIsrael = true))
        assertEquals(
            "BERESHIS",
            WeeklyParshaLogic.displayParshaKey(cal, tomorrow, now, isInIsrael = true),
        )
    }

    private fun dayInfo(date: LocalDate, hour: Int): DayInfo =
        backend.dayInfoAt(millis(date, hour), jerusalem)

    private fun millis(date: LocalDate, hour: Int): Long =
        LocalDateTime(date.year, date.monthNumber, date.dayOfMonth, hour, 0, 0)
            .toInstant(TimeZone.of("Asia/Jerusalem"))
            .toEpochMilliseconds()
}
