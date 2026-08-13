package com.beardytop.beatzaddik.domain

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TachanunRulesTest {

    @Test
    fun sivan_1Through12_omitsTachanun() {
        for (day in 1..12) {
            assertFalse(
                TachanunRules.isRecited(weekday(SIVAN, day), isInIsrael = true),
                "expected omission on Sivan $day",
            )
        }
        assertTrue(TachanunRules.isRecited(weekday(SIVAN, 13), isInIsrael = true))
    }

    @Test
    fun sivan_isruChagThrough12_omitsTachanun() {
        assertTrue(TachanunRules.isInPostShavuotOmissionWindow(HebrewCalendarEngine.SIVAN, 5, isInIsrael = true))
        assertTrue(TachanunRules.isInPostShavuotOmissionWindow(HebrewCalendarEngine.SIVAN, 7, isInIsrael = true))
        assertTrue(TachanunRules.isInPostShavuotOmissionWindow(HebrewCalendarEngine.SIVAN, 12, isInIsrael = true))
        assertFalse(TachanunRules.isInPostShavuotOmissionWindow(HebrewCalendarEngine.SIVAN, 13, isInIsrael = true))
    }

    @Test
    fun sivan_ashkenaz_omitsThrough12_includingDiasporaDay7() {
        assertTrue(TachanunRules.isInPostShavuotOmissionWindow(HebrewCalendarEngine.SIVAN, 7, isInIsrael = false))
        assertTrue(TachanunRules.isInPostShavuotOmissionWindow(HebrewCalendarEngine.SIVAN, 8, isInIsrael = false))
        assertTrue(TachanunRules.isInPostShavuotOmissionWindow(HebrewCalendarEngine.SIVAN, 12, isInIsrael = false))
        assertFalse(TachanunRules.isInPostShavuotOmissionWindow(HebrewCalendarEngine.SIVAN, 13, isInIsrael = false))
    }

    @Test
    fun sivan_sefard_omitsThroughIsruChag_notThrough12() {
        assertTrue(
            TachanunRules.isInPostShavuotOmissionWindow(
                HebrewCalendarEngine.SIVAN, 7, isInIsrael = true, EffectiveNusach.SEFARD,
            ),
        )
        assertFalse(
            TachanunRules.isInPostShavuotOmissionWindow(
                HebrewCalendarEngine.SIVAN, 8, isInIsrael = true, EffectiveNusach.SEFARD,
            ),
        )
        assertTrue(
            TachanunRules.isInPostShavuotOmissionWindow(
                HebrewCalendarEngine.SIVAN, 8, isInIsrael = false, EffectiveNusach.SEFARD,
            ),
        )
        assertFalse(
            TachanunRules.isInPostShavuotOmissionWindow(
                HebrewCalendarEngine.SIVAN, 9, isInIsrael = false, EffectiveNusach.SEFARD,
            ),
        )
    }

    @Test
    fun tishrei_betweenYomKippurAndSukkot_omitsTachanun() {
        for (day in 11..14) {
            assertFalse(TachanunRules.isRecited(weekday(TISHREI, day), isInIsrael = true))
        }
    }

    @Test
    fun tishrei_afterSimchatTorahThroughMonthEnd_omitsTachanun() {
        assertFalse(TachanunRules.isRecited(weekday(TISHREI, 23), isInIsrael = true))
        assertFalse(TachanunRules.isRecited(weekday(TISHREI, 29), isInIsrael = true))
        assertFalse(TachanunRules.isRecited(weekday(TISHREI, 24), isInIsrael = false))
        assertTrue(TachanunRules.isRecited(weekday(TISHREI, 23), isInIsrael = false))
    }

    @Test
    fun erevYomKippur_omitsShacharitOnly() {
        val cal = weekday(TISHREI, 9)
        assertFalse(TachanunRules.isRecited(cal, isInIsrael = true, TachanunRules.PrayerSlot.SHACHARIT))
        assertTrue(TachanunRules.isRecited(cal, isInIsrael = true, TachanunRules.PrayerSlot.MINCHA))
    }

    @Test
    fun erevRoshHashana_omitsShacharitOnly() {
        val cal = weekday(ELUL, 29)
        assertFalse(TachanunRules.isRecited(cal, isInIsrael = true, TachanunRules.PrayerSlot.SHACHARIT))
        assertTrue(TachanunRules.isRecited(cal, isInIsrael = true, TachanunRules.PrayerSlot.MINCHA))
    }

    @Test
    fun israeliIndependenceDays_omitTachanunInIsrael() {
        val atzmaut = weekday(IYAR, 5).copy(isYomHaAtzmaut = true)
        val yerushalayim = weekday(IYAR, 28).copy(isYomYerushalayim = true)
        assertFalse(TachanunRules.isRecited(atzmaut, isInIsrael = true))
        assertFalse(TachanunRules.isRecited(yerushalayim, isInIsrael = true))
        assertTrue(TachanunRules.isRecited(atzmaut, isInIsrael = false))
    }

    @Test
    fun purimKatan_omitsTachanun() {
        val leapYear = 5787
        assertTrue(HebrewCalendarEngine.isJewishLeapYear(leapYear))
        val cal = weekday(ADAR, 14, hebrewYear = leapYear)
        assertFalse(TachanunRules.isRecited(cal, isInIsrael = true))
        val shushan = weekday(ADAR, 15, hebrewYear = leapYear)
        assertFalse(TachanunRules.isRecited(shushan, isInIsrael = true))
    }

    @Test
    fun erevTishaBeav_omitsMinchaOnly() {
        val eve = weekday(AV, 8).copy(activeSeasons = setOf("erev_tisha_beav"))
        val fastDay = weekday(AV, 9).copy(fastDayIndex = HebrewCalendarEngine.TISHA_BEAV)
        assertTrue(
            TachanunRules.isRecited(
                eve,
                isInIsrael = true,
                TachanunRules.PrayerSlot.SHACHARIT,
                tomorrowCal = fastDay,
            ),
        )
        assertFalse(
            TachanunRules.isRecited(
                eve,
                isInIsrael = true,
                TachanunRules.PrayerSlot.MINCHA,
                tomorrowCal = fastDay,
            ),
        )
        assertFalse(
            TachanunRules.isRecited(
                eve,
                UserProfile(),
                "chabad_mincha_tachanun",
                tomorrowCal = fastDay,
            ),
        )
        assertFalse(
            TachanunRules.isRecited(
                eve,
                UserProfile(),
                "ashkenaz_mincha_tachanun",
                tomorrowCal = fastDay,
            ),
        )
    }

    @Test
    fun ashkenazMinchaTachanun_isMinchaSlot() {
        assertEquals(
            TachanunRules.PrayerSlot.MINCHA,
            TachanunRules.prayerSlotForItem("ashkenaz_mincha_tachanun"),
        )
    }

    @Test
    fun erevPurim_omitsMinchaOnly() {
        val eve = weekday(ADAR, 13).copy(activeSeasons = setOf("erev_purim"))
        val purim = weekday(ADAR, 14).copy(isPurim = true)
        assertTrue(
            TachanunRules.isRecited(
                eve,
                isInIsrael = true,
                TachanunRules.PrayerSlot.SHACHARIT,
                tomorrowCal = purim,
            ),
        )
        assertFalse(
            TachanunRules.isRecited(
                eve,
                isInIsrael = true,
                TachanunRules.PrayerSlot.MINCHA,
                tomorrowCal = purim,
            ),
        )
    }

    @Test
    fun erevShabbat_omitsMincha() {
        val friday = weekday(AV, 3).copy(isErevShabbat = true)
        assertTrue(
            TachanunRules.isRecited(friday, isInIsrael = true, TachanunRules.PrayerSlot.SHACHARIT),
        )
        assertFalse(
            TachanunRules.isRecited(friday, isInIsrael = true, TachanunRules.PrayerSlot.MINCHA),
        )
    }

    @Test
    fun erevRoshHashana_onFriday_stillSaysMinchaTachanun() {
        val cal = weekday(ELUL, 29).copy(isErevShabbat = true)
        assertFalse(
            TachanunRules.isRecited(cal, isInIsrael = true, TachanunRules.PrayerSlot.SHACHARIT),
        )
        assertTrue(
            TachanunRules.isRecited(cal, isInIsrael = true, TachanunRules.PrayerSlot.MINCHA),
        )
    }

    @Test
    fun erevChag_omitsMincha() {
        val eve = weekday(TISHREI, 14).copy(upcomingChagName = "Sukkot")
        val chag = weekday(TISHREI, 15).copy(isYomTov = true, isYomTovAssurBemelacha = true)
        assertFalse(
            TachanunRules.isRecited(
                eve,
                isInIsrael = true,
                TachanunRules.PrayerSlot.MINCHA,
                tomorrowCal = chag,
            ),
        )
    }

    @Test
    fun eveOfTuBshvat_omitsMincha() {
        val eve = weekday(SHEVAT, 14)
        val holiday = weekday(SHEVAT, 15)
        assertFalse(
            TachanunRules.isRecited(
                eve,
                isInIsrael = true,
                TachanunRules.PrayerSlot.MINCHA,
                tomorrowCal = holiday,
            ),
        )
    }

    @Test
    fun eveOfPesachSheini_omitsMincha() {
        val eve = weekday(IYAR, 13)
        val holiday = weekday(IYAR, 14)
        assertFalse(
            TachanunRules.isRecited(
                eve,
                isInIsrael = true,
                TachanunRules.PrayerSlot.MINCHA,
                tomorrowCal = holiday,
            ),
        )
    }

    @Test
    fun tachanunItems_hiddenDuringPostShavuotWindow() {
        val cal = weekday(SIVAN, 10)
        assertFalse(TachanunRules.isRecited(cal, isInIsrael = true))
        assertTrue(TachanunRules.isTachanunOnlyItem("ashkenaz_shacharit_tachanun"))
        assertTrue(TachanunRules.isTachanunOnlyItem("sefard_mincha_tachanun"))
    }

    private fun weekday(
        hebrewMonth: Int,
        hebrewDay: Int,
        hebrewYear: Int = 5786,
    ) = DayInfo(
        date = LocalDate(2026, 5, 28),
        civilLabel = "Thursday, May 28, 2026",
        hebrewLabel = "$hebrewDay month $hebrewYear",
        parsha = null,
        statusChips = emptyList(),
        isShabbat = false,
        isErevShabbat = false,
        isYomTov = false,
        isShabbatOrYomTov = false,
        activeTimeOfDay = TimeOfDay.DAY,
        activePeriodLabel = "Daytime",
        inactivePeriodHint = null,
        hebrewMonth = hebrewMonth,
        hebrewDay = hebrewDay,
        hebrewYear = hebrewYear,
    )

    private companion object {
        val SIVAN = HebrewCalendarEngine.SIVAN
        val TISHREI = HebrewCalendarEngine.TISHREI
        val ELUL = HebrewCalendarEngine.ELUL
        val IYAR = HebrewCalendarEngine.IYAR
        val SHEVAT = HebrewCalendarEngine.SHEVAT
        val ADAR = HebrewCalendarEngine.ADAR
        val AV = HebrewCalendarEngine.AV
    }
}
