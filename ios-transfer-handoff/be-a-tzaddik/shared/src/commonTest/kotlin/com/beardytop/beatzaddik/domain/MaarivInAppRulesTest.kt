package com.beardytop.beatzaddik.domain

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MaarivInAppRulesTest {

    private fun day(
        isErevShabbat: Boolean = false,
        isYomTovAssurBemelacha: Boolean = false,
        activeSeasons: Set<String> = emptySet(),
        hebrewMonth: Int? = null,
        hebrewDay: Int? = null,
    ) = DayInfo(
        date = LocalDate(2026, 7, 3),
        civilLabel = "Friday, July 3, 2026",
        hebrewLabel = "18 Tammuz 5786",
        parsha = "Pinchas",
        statusChips = emptyList(),
        isShabbat = false,
        isErevShabbat = isErevShabbat,
        isYomTov = false,
        isYomTovAssurBemelacha = isYomTovAssurBemelacha,
        isShabbatOrYomTov = false,
        activeTimeOfDay = TimeOfDay.DAY,
        activePeriodLabel = "Daytime",
        inactivePeriodHint = null,
        activeSeasons = activeSeasons,
        hebrewMonth = hebrewMonth,
        hebrewDay = hebrewDay,
    )

    @Test
    fun erevShabbatBlocksMaariv() {
        assertEquals("Shabbat", MaarivInAppRules.blockedTonightLabel(day(isErevShabbat = true), null, false))
    }

    @Test
    fun erevChagBlocksMaariv() {
        assertEquals(
            "Yom Tov",
            MaarivInAppRules.blockedTonightLabel(day(activeSeasons = setOf("erev_chag")), null, false),
        )
    }

    @Test
    fun hoshanaRabaErevSheminiAtzeretBlocksMaariv() {
        val today = day(
            hebrewMonth = HebrewCalendarEngine.TISHREI,
            hebrewDay = 21,
            activeSeasons = setOf("chol_hamoed_sukkot"),
        )
        val tomorrow = day(
            isYomTovAssurBemelacha = true,
            hebrewMonth = HebrewCalendarEngine.TISHREI,
            hebrewDay = 22,
            activeSeasons = setOf("shemini_atzeret"),
        )
        assertEquals("Yom Tov", MaarivInAppRules.blockedTonightLabel(today, tomorrow, inIsrael = true))
    }

    @Test
    fun erevFinalPesachDayIsraelBlocksMaariv() {
        val today = day(
            hebrewMonth = HebrewCalendarEngine.NISSAN,
            hebrewDay = 20,
            activeSeasons = setOf("chol_hamoed_pesach"),
        )
        val tomorrow = day(
            isYomTovAssurBemelacha = true,
            hebrewMonth = HebrewCalendarEngine.NISSAN,
            hebrewDay = 21,
            activeSeasons = setOf("pesach"),
        )
        assertEquals("Yom Tov", MaarivInAppRules.blockedTonightLabel(today, tomorrow, inIsrael = true))
    }

    @Test
    fun plainWeekdayDoesNotBlockMaariv() {
        assertNull(MaarivInAppRules.blockedTonightLabel(day(), null, false))
    }

    @Test
    fun erevShabbatBeforeDawnDoesNotBlockYet() {
        val z = ZmanimSnapshot(
            timezoneId = "America/New_York",
            alotHaShacharMillis = 1_000_000L,
            sunriseMillis = 2_000_000L,
            sunsetMillis = 3_000_000L,
        )
        assertEquals(
            null,
            MaarivInAppRules.blockedMaarivStatusIfApplicable(
                prayerDay = PrayerDayContext(
                    isShabbat = false,
                    isYomTov = false,
                    isRoshChodesh = false,
                    isErevShabbat = true,
                    maarivBlockedTonightLabel = "Shabbat",
                ),
                nowMillis = 500_000L,
                zmanim = z,
                timezoneId = "America/New_York",
            ),
        )
    }

    @Test
    fun erevShabbatAfterDawnBlocksMaariv() {
        val z = ZmanimSnapshot(
            timezoneId = "America/New_York",
            alotHaShacharMillis = 1_000_000L,
            sunriseMillis = 2_000_000L,
            sunsetMillis = 3_000_000L,
        )
        val status = MaarivInAppRules.blockedMaarivStatusIfApplicable(
            prayerDay = PrayerDayContext(
                isShabbat = false,
                isYomTov = false,
                isRoshChodesh = false,
                isErevShabbat = true,
                maarivBlockedTonightLabel = "Shabbat",
            ),
            nowMillis = 2_000_000L,
            zmanim = z,
            timezoneId = "America/New_York",
        )
        assertEquals("Not available in-app tonight — {holyDay}", status?.collapsedSummaryTemplate)
        assertEquals("Shabbat", status?.collapsedSummaryArgs?.get("holyDay"))
    }
}
