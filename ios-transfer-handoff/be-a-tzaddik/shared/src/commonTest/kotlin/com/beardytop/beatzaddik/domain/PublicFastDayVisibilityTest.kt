package com.beardytop.beatzaddik.domain

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PublicFastDayVisibilityTest {

    private val sunset = 1_000L
    private val tzeit = 1_100L
    private val dawn = 2_000L
    // chatzos halayla = sunset + (dawn - sunset) / 2 = 1_500

    private val zmanim = ZmanimSnapshot(
        sunsetMillis = sunset,
        tzeitMillis = tzeit,
        alotHaShacharMillis = dawn,
        nightObligationsEndMillis = dawn,
        timezoneId = "Asia/Jerusalem",
    )

    private fun fastDay(isErevShabbat: Boolean = false) = DayInfo(
        date = LocalDate(2025, 3, 13),
        civilLabel = "Thursday, March 13, 2025",
        hebrewLabel = "13 Adar 5785",
        parsha = null,
        statusChips = emptyList(),
        isShabbat = false,
        isErevShabbat = isErevShabbat,
        isYomTov = false,
        isShabbatOrYomTov = false,
        activeTimeOfDay = TimeOfDay.NIGHT,
        activePeriodLabel = "Evening",
        inactivePeriodHint = null,
        fastDayIndex = HebrewCalendarEngine.FAST_OF_ESTHER,
        zmanim = zmanim,
    )

    @Test
    fun endedFast_staysVisibleBetweenTzeitAndChatzosLayla() {
        val cal = fastDay()
        assertFalse(
            PublicFastDayRules.shouldHideEndedFastItem(
                nowMillis = 1_200L,
                cal = cal,
                tomorrowCal = null,
                availability = ItemZmanAvailability.EXPIRED,
            ),
        )
    }

    @Test
    fun endedFast_hiddenAfterChatzosLayla() {
        val cal = fastDay()
        assertTrue(
            PublicFastDayRules.shouldHideEndedFastItem(
                nowMillis = 1_600L,
                cal = cal,
                tomorrowCal = null,
                availability = ItemZmanAvailability.EXPIRED,
            ),
        )
    }

    @Test
    fun activeFast_notHidden() {
        val cal = fastDay()
        assertFalse(
            PublicFastDayRules.shouldHideEndedFastItem(
                nowMillis = 500L,
                cal = cal,
                tomorrowCal = null,
                availability = ItemZmanAvailability.ACTIVE,
            ),
        )
    }

    @Test
    fun fridayFastIntoShabbat_notAutoHiddenWhenExpired() {
        val cal = fastDay(isErevShabbat = true)
        assertFalse(
            PublicFastDayRules.shouldHideEndedFastItem(
                nowMillis = 1_600L,
                cal = cal,
                tomorrowCal = null,
                availability = ItemZmanAvailability.EXPIRED,
            ),
        )
    }
}
