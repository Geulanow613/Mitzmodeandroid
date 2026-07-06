package com.beardytop.beatzaddik.domain

import kotlinx.datetime.DayOfWeek
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class JerusalemPurimRulesTest {

    @Test
    fun galut_erevPurim_is14Adar() {
        assertTrue(
            JerusalemPurimRules.isErevPurimDay(
                isJerusalem = false,
                isPurimToday = false,
                tomorrowYomTovIndex = HebrewCalendarEngine.PURIM,
                dayAfterTomorrowYomTovIndex = HebrewCalendarEngine.SHUSHAN_PURIM,
                tomorrowDayOfWeek = DayOfWeek.FRIDAY,
            ),
        )
    }

    @Test
    fun jerusalem_erevPurim_isEveOf15Adar_not14Adar() {
        assertFalse(
            JerusalemPurimRules.isErevPurimDay(
                isJerusalem = true,
                isPurimToday = false,
                tomorrowYomTovIndex = HebrewCalendarEngine.PURIM,
                dayAfterTomorrowYomTovIndex = HebrewCalendarEngine.SHUSHAN_PURIM,
                tomorrowDayOfWeek = DayOfWeek.TUESDAY,
            ),
        )
        assertTrue(
            JerusalemPurimRules.isErevPurimDay(
                isJerusalem = true,
                isPurimToday = false,
                tomorrowYomTovIndex = HebrewCalendarEngine.SHUSHAN_PURIM,
                dayAfterTomorrowYomTovIndex = -1,
                tomorrowDayOfWeek = DayOfWeek.WEDNESDAY,
            ),
        )
    }

    @Test
    fun jerusalem_meshulash_erev_isThursdayBeforeFridayBlock() {
        assertTrue(
            JerusalemPurimRules.isErevPurimDay(
                isJerusalem = true,
                isPurimToday = false,
                tomorrowYomTovIndex = HebrewCalendarEngine.PURIM,
                dayAfterTomorrowYomTovIndex = HebrewCalendarEngine.SHUSHAN_PURIM,
                tomorrowDayOfWeek = DayOfWeek.FRIDAY,
            ),
        )
    }

    @Test
    fun jerusalem_meshulash_shabbat_isNotHomePurimDay() {
        assertFalse(
            JerusalemPurimRules.isPurimDay(
                isJerusalem = true,
                todayYomTovIndex = HebrewCalendarEngine.SHUSHAN_PURIM,
                tomorrowYomTovIndex = -1,
                yesterdayYomTovIndex = HebrewCalendarEngine.PURIM,
                dayOfWeek = DayOfWeek.SATURDAY,
            ),
        )
        assertTrue(
            JerusalemPurimRules.isShushanPurimOnMeshulashShabbat(
                isJerusalem = true,
                todayYomTovIndex = HebrewCalendarEngine.SHUSHAN_PURIM,
                yesterdayYomTovIndex = HebrewCalendarEngine.PURIM,
                dayOfWeek = DayOfWeek.SATURDAY,
            ),
        )
    }

    @Test
    fun jerusalemProfile_matchesCityIdAndLabel() {
        val byId = UserProfile(manualCityId = "jlm")
        val byLabel = UserProfile(locationLabel = "Jerusalem, Israel")
        assertTrue(JerusalemPurimRules.isJerusalemProfile(byId))
        assertTrue(JerusalemPurimRules.isJerusalemProfile(byLabel))
        assertFalse(JerusalemPurimRules.isJerusalemProfile(UserProfile(locationLabel = "Tel Aviv")))
    }
}
