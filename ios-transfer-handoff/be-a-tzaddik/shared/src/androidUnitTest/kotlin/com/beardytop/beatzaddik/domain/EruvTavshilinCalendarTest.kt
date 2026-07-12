package com.beardytop.beatzaddik.domain

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EruvTavshilinCalendarTest {

    private val nyc = UserProfile(
        timezoneId = "America/New_York",
        latitude = 40.7128,
        longitude = -74.0060,
        locationLabel = "New York, NY",
    )

    private val jerusalem = nyc.copy(
        timezoneId = "Asia/Jerusalem",
        latitude = 31.7683,
        longitude = 35.2137,
        locationLabel = "Jerusalem, Israel",
        manualCityId = "jlm",
    )

    private val calendar = JewishCalendarService(createJewishCalendarBackend())

    @Test
    fun erevPesach5786WednesdayNyc_showsEruvInPesachPrep() {
        val cal = dayInfo(LocalDate(2026, 4, 1), hour = 2, nyc)
        assertTrue("erev_pesach" in cal.activeSeasons)
        assertTrue("erev_chag" in cal.activeSeasons)
        assertEquals(14, cal.hebrewDay)
        assertEquals(HebrewCalendarEngine.PESACH, cal.upcomingChagYomTovIndex)

        val tomorrow = calendar.dayInfoForDate(cal.date.plus(1, DateTimeUnit.DAY), nyc)
        val dayAfter = calendar.dayInfoForDate(cal.date.plus(2, DateTimeUnit.DAY), nyc)
        assertTrue(EruvTavshilinRules.requiresEruvTavshilin(cal, nyc, tomorrow))

        val items = SeasonalChecklistItems.forDay(
            cal = cal,
            profile = nyc,
            tomorrowCal = tomorrow,
            dayAfterTomorrowCal = dayAfter,
            nowMillis = millis(LocalDate(2026, 4, 1), 2, nyc),
        )
        val eruv = items.first { it.id == "eruv_tavshilin" }
        assertEquals("Pesach prep", eruv.section)
        assertTrue(eruv.required)
    }

    @Test
    fun israelThursdayErevOneDayYomTovBeforeShabbat_calendarBacked() {
        // Shavuot 5786: erev Thu 5 Sivan; one day Yom Tov Fri; Shabbat Sat.
        val cal = dayInfo(LocalDate(2026, 5, 21), hour = 10, jerusalem)
        assertTrue("erev_chag" in cal.activeSeasons)
        val tomorrow = calendar.dayInfoForDate(cal.date.plus(1, DateTimeUnit.DAY), jerusalem)
        assertTrue(EruvTavshilinRules.requiresEruvTavshilin(cal, jerusalem, tomorrow))

        val dayAfter = calendar.dayInfoForDate(cal.date.plus(2, DateTimeUnit.DAY), jerusalem)
        val items = SeasonalChecklistItems.forDay(
            cal = cal,
            profile = jerusalem,
            tomorrowCal = tomorrow,
            dayAfterTomorrowCal = dayAfter,
            nowMillis = millis(LocalDate(2026, 5, 21), 10, jerusalem),
        )
        assertTrue(items.any { it.id == "eruv_tavshilin" })
    }

    @Test
    fun hoshanaRabbah5787_showsAravotItemAndLabel() {
        val cal = dayInfo(LocalDate(2026, 10, 2), hour = 9, nyc)
        assertEquals(21, cal.hebrewDay)
        val occasion = TodayOccasionLabels.primary(
            cal,
            millis(LocalDate(2026, 10, 2), 9, nyc),
            calendar.dayInfoForDate(cal.date.plus(1, DateTimeUnit.DAY), nyc),
        )
        assertEquals("Hoshana Rabbah", occasion?.label)

        val tomorrow = calendar.dayInfoForDate(cal.date.plus(1, DateTimeUnit.DAY), nyc)
        val dayAfter = calendar.dayInfoForDate(cal.date.plus(2, DateTimeUnit.DAY), nyc)
        val items = SeasonalChecklistItems.forDay(
            cal = cal,
            profile = nyc,
            tomorrowCal = tomorrow,
            dayAfterTomorrowCal = dayAfter,
            nowMillis = millis(LocalDate(2026, 10, 2), 9, nyc),
        )
        val aravot = items.first { it.id == "hoshana_rabbah_aravot" }
        assertEquals("Hoshana Rabbah", aravot.section)
        assertTrue(aravot.explanation.contains("Minhag Nevi'im"))
        assertTrue(aravot.explanation.contains("five"))
        assertTrue(
            "Hoshana Rabbah" in ChecklistSectionOrder.prioritizedPrepSections(
                cal,
                tomorrow,
                nyc,
                millis(LocalDate(2026, 10, 2), 9, nyc),
            ),
        )
    }

    @Test
    fun cholHamoedFridayWhenSheminiAtzeretOnShabbat_noEruv() {
        val cal = dayInfo(LocalDate(2026, 10, 2), hour = 9, nyc)
        assertTrue("chol_hamoed_sukkot" in cal.activeSeasons)
        assertTrue("erev_chag" in cal.activeSeasons)
        val tomorrow = calendar.dayInfoForDate(cal.date.plus(1, DateTimeUnit.DAY), nyc)
        assertTrue(tomorrow.isShabbat)
        assertFalse(EruvTavshilinRules.requiresEruvTavshilin(cal, nyc, tomorrow))

        val dayAfter = calendar.dayInfoForDate(cal.date.plus(2, DateTimeUnit.DAY), nyc)
        val items = SeasonalChecklistItems.forDay(
            cal = cal,
            profile = nyc,
            tomorrowCal = tomorrow,
            dayAfterTomorrowCal = dayAfter,
            nowMillis = millis(LocalDate(2026, 10, 2), 9, nyc),
        )
        assertFalse(items.any { it.id == "eruv_tavshilin" })
    }

    @Test
    fun israelCholHamoedFridayWhenSheminiAtzeretOnShabbat_noEruv() {
        val cal = dayInfo(LocalDate(2026, 10, 2), hour = 9, jerusalem)
        assertTrue("chol_hamoed_sukkot" in cal.activeSeasons)
        val tomorrow = calendar.dayInfoForDate(cal.date.plus(1, DateTimeUnit.DAY), jerusalem)
        assertTrue(tomorrow.isShabbat)
        assertFalse(EruvTavshilinRules.requiresEruvTavshilin(cal, jerusalem, tomorrow))
    }

    @Test
    fun regularErevPesachWithoutShabbatSequence_noEruv() {
        val cal = dayInfo(LocalDate(2024, 4, 22), hour = 10, nyc)
        assertTrue("erev_pesach" in cal.activeSeasons)
        val tomorrow = calendar.dayInfoForDate(cal.date.plus(1, DateTimeUnit.DAY), nyc)
        assertFalse(EruvTavshilinRules.requiresEruvTavshilin(cal, nyc, tomorrow))
    }

    private fun dayInfo(date: LocalDate, hour: Int, profile: UserProfile): DayInfo =
        calendar.dayInfoAt(millis(date, hour, profile), profile)

    private fun millis(date: LocalDate, hour: Int, profile: UserProfile): Long =
        LocalDateTime(date.year, date.monthNumber, date.dayOfMonth, hour, 0)
            .toInstant(TimeZone.of(profile.timezoneId))
            .toEpochMilliseconds()
}
