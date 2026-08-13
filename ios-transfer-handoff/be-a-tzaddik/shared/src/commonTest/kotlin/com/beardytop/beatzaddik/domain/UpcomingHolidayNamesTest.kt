package com.beardytop.beatzaddik.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class UpcomingHolidayNamesTest {

    @Test
    fun cholHamoedPesachErevShowsSheviiShelPesach() {
        val name = UpcomingHolidayNames.erevUpcomingDisplayName(
            todayYomTovIndex = HebrewCalendarEngine.CHOL_HAMOED_PESACH,
            tomorrowYomTovIndex = HebrewCalendarEngine.PESACH,
            tomorrowHebrewMonth = HebrewCalendarEngine.NISSAN,
            tomorrowHebrewDay = 21,
            inIsrael = true,
            defaultHolidayName = "Pesach",
        )
        assertEquals(UpcomingHolidayNames.SHEVII_SHEL_PESACH, name)
    }

    @Test
    fun cholHamoedPesachErevDiasporaDay21ShowsSheviiShelPesach() {
        val name = UpcomingHolidayNames.erevUpcomingDisplayName(
            todayYomTovIndex = HebrewCalendarEngine.CHOL_HAMOED_PESACH,
            tomorrowYomTovIndex = HebrewCalendarEngine.PESACH,
            tomorrowHebrewMonth = HebrewCalendarEngine.NISSAN,
            tomorrowHebrewDay = 21,
            inIsrael = false,
            defaultHolidayName = "Pesach",
        )
        assertEquals(UpcomingHolidayNames.SHEVII_SHEL_PESACH, name)
    }

    @Test
    fun firstPesachErevStillShowsPesach() {
        val name = UpcomingHolidayNames.erevUpcomingDisplayName(
            todayYomTovIndex = HebrewCalendarEngine.EREV_PESACH,
            tomorrowYomTovIndex = HebrewCalendarEngine.PESACH,
            tomorrowHebrewMonth = HebrewCalendarEngine.NISSAN,
            tomorrowHebrewDay = 15,
            inIsrael = true,
            defaultHolidayName = "Pesach",
        )
        assertEquals("Pesach", name)
    }

    @Test
    fun sheviiShelPesachPrepHintMentionsYizkor() {
        assertEquals(
            "Yom Tov — Yizkor, matzah, festive meals",
            UpcomingHolidayNames.yomTovPrepHint(UpcomingHolidayNames.SHEVII_SHEL_PESACH),
        )
    }
}
