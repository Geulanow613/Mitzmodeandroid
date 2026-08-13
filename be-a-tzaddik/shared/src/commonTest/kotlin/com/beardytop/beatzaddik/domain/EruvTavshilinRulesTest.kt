package com.beardytop.beatzaddik.domain

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EruvTavshilinRulesTest {

    private val diaspora = UserProfile(
        timezoneId = "America/New_York",
        latitude = 40.7128,
        longitude = -74.0060,
        locationLabel = "New York",
    )

    private val israel = diaspora.copy(
        timezoneId = "Asia/Jerusalem",
        latitude = 31.7683,
        longitude = 35.2137,
        locationLabel = "Jerusalem",
        manualCityId = "jlm",
    )

    @Test
    fun diasporaWednesdayErevPesach_requiresEruv() {
        val cal = erevPesachDay(LocalDate(2026, 4, 1), DayOfWeek.WEDNESDAY)
        val tomorrow = pesachDay(LocalDate(2026, 4, 2))
        assertTrue(EruvTavshilinRules.requiresEruvTavshilin(cal, diaspora, tomorrow))
        assertEquals("Pesach prep", EruvTavshilinRules.checklistSection(cal))
    }

    @Test
    fun diasporaThursdayErevTwoDayYomTovBeforeShabbat_requiresEruv() {
        // Thu erev: chag Thu night; Fri–Sat YT; Saturday is Shabbat.
        val cal = erevChagDay(LocalDate(2026, 10, 8), DayOfWeek.THURSDAY, HebrewCalendarEngine.SUCCOS)
        val tomorrow = yomTovDay(LocalDate(2026, 10, 9), HebrewCalendarEngine.SUCCOS)
        assertTrue(EruvTavshilinRules.requiresEruvTavshilin(cal, diaspora, tomorrow))
    }

    @Test
    fun diasporaTuesdayErevWedThuYomTovEndingThursdayNight_noEruv() {
        // Tue-night start: Wed+Thu YT, done motzei Thu; Fri is weekday — erev is Tuesday, not Wed/Thu.
        val cal = erevChagDay(LocalDate(2026, 10, 6), DayOfWeek.TUESDAY, HebrewCalendarEngine.SUCCOS)
        val tomorrow = yomTovDay(LocalDate(2026, 10, 7), HebrewCalendarEngine.SUCCOS)
        assertFalse(EruvTavshilinRules.requiresEruvTavshilin(cal, diaspora, tomorrow))
    }

    @Test
    fun israelWednesdayErevOneDayYomTovWithCholBeforeShabbat_noEruv() {
        // Israel 1-day YT Thursday only; Friday chol; Shabbat Saturday — erev Wednesday.
        val cal = erevChagDay(LocalDate(2026, 5, 20), DayOfWeek.WEDNESDAY, HebrewCalendarEngine.SHAVUOS)
        val tomorrow = yomTovDay(LocalDate(2026, 5, 21), HebrewCalendarEngine.SHAVUOS)
        assertFalse(EruvTavshilinRules.requiresEruvTavshilin(cal, israel, tomorrow))
    }

    @Test
    fun israelWednesdayErevRoshHashanaTwoDaysBeforeShabbat_requiresEruv() {
        // Israel RH: erev Wed; Thu–Fri RH; motzei Fri → Shabbat.
        val cal = erevChagDay(LocalDate(2026, 9, 16), DayOfWeek.WEDNESDAY, HebrewCalendarEngine.ROSH_HASHANA)
        val tomorrow = yomTovDay(LocalDate(2026, 9, 17), HebrewCalendarEngine.ROSH_HASHANA)
        assertTrue(EruvTavshilinRules.requiresEruvTavshilin(cal, israel, tomorrow))
    }

    @Test
    fun israelThursdayErevOneDayYomTovBeforeShabbat_requiresEruv() {
        val cal = erevChagDay(LocalDate(2026, 4, 2), DayOfWeek.THURSDAY, HebrewCalendarEngine.PESACH)
        val tomorrow = yomTovDay(LocalDate(2026, 4, 3), HebrewCalendarEngine.PESACH)
        assertTrue(EruvTavshilinRules.requiresEruvTavshilin(cal, israel, tomorrow))
    }

    @Test
    fun erevYomKippur_excluded() {
        val cal = erevChagDay(LocalDate(2029, 9, 21), DayOfWeek.FRIDAY, HebrewCalendarEngine.YOM_KIPPUR)
        val tomorrow = yomTovDay(LocalDate(2029, 9, 22), HebrewCalendarEngine.YOM_KIPPUR)
        assertFalse(EruvTavshilinRules.requiresEruvTavshilin(cal, diaspora, tomorrow))
    }

    @Test
    fun fridayErevPesachWhenPesachBeginsOnShabbat_noEruv() {
        val cal = erevPesachDay(LocalDate(2025, 4, 11), DayOfWeek.FRIDAY)
        val tomorrow = yomTovDay(LocalDate(2025, 4, 12), HebrewCalendarEngine.PESACH).copy(isShabbat = true)
        assertFalse(EruvTavshilinRules.requiresEruvTavshilin(cal, diaspora, tomorrow))
    }

    @Test
    fun diasporaTuesdayErevPesach_noEruv() {
        val cal = erevPesachDay(LocalDate(2024, 4, 22), DayOfWeek.MONDAY)
        val tomorrow = pesachDay(LocalDate(2024, 4, 23))
        assertFalse(EruvTavshilinRules.requiresEruvTavshilin(cal, diaspora, tomorrow))
    }

    private fun erevPesachDay(date: LocalDate, dow: DayOfWeek) = DayInfo(
        date = date,
        civilLabel = "",
        hebrewLabel = "",
        parsha = null,
        statusChips = emptyList(),
        isShabbat = dow == DayOfWeek.SATURDAY,
        isErevShabbat = dow == DayOfWeek.FRIDAY,
        isYomTov = false,
        isShabbatOrYomTov = false,
        activeTimeOfDay = TimeOfDay.DAY,
        activePeriodLabel = "",
        inactivePeriodHint = null,
        activeSeasons = setOf("erev_pesach", "erev_chag"),
        hebrewMonth = HebrewCalendarEngine.NISSAN,
        hebrewDay = 14,
        hebrewYear = 5786,
        upcomingChagName = "Pesach",
        upcomingChagYomTovIndex = HebrewCalendarEngine.PESACH,
    )

    private fun erevChagDay(date: LocalDate, dow: DayOfWeek, chagIdx: Int) = DayInfo(
        date = date,
        civilLabel = "",
        hebrewLabel = "",
        parsha = null,
        statusChips = emptyList(),
        isShabbat = dow == DayOfWeek.SATURDAY,
        isErevShabbat = dow == DayOfWeek.FRIDAY,
        isYomTov = false,
        isShabbatOrYomTov = false,
        activeTimeOfDay = TimeOfDay.DAY,
        activePeriodLabel = "",
        inactivePeriodHint = null,
        activeSeasons = setOf("erev_chag"),
        hebrewMonth = HebrewCalendarEngine.TISHREI,
        hebrewDay = 1,
        hebrewYear = 5786,
        upcomingChagName = "Sukkot",
        upcomingChagYomTovIndex = chagIdx,
    )

    private fun pesachDay(date: LocalDate) = yomTovDay(date, HebrewCalendarEngine.PESACH)

    private fun yomTovDay(date: LocalDate, chagIdx: Int) = DayInfo(
        date = date,
        civilLabel = "",
        hebrewLabel = "",
        parsha = null,
        statusChips = emptyList(),
        isShabbat = false,
        isErevShabbat = false,
        isYomTov = true,
        isShabbatOrYomTov = true,
        activeTimeOfDay = TimeOfDay.DAY,
        activePeriodLabel = "",
        inactivePeriodHint = null,
        activeSeasons = emptySet(),
        hebrewMonth = HebrewCalendarEngine.NISSAN,
        hebrewDay = 15,
        hebrewYear = 5786,
        upcomingChagYomTovIndex = chagIdx,
    )
}
