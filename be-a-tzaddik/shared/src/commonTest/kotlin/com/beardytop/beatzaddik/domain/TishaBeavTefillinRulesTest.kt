package com.beardytop.beatzaddik.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TishaBeavTefillinRulesTest {

    @Test
    fun omitsMorningTefillinForAshkenazAndChabad() {
        assertTrue(TishaBeavTefillinRules.omitsMorningTefillin(EffectiveNusach.ASHKENAZ))
        assertTrue(TishaBeavTefillinRules.omitsMorningTefillin(EffectiveNusach.CHABAD))
    }

    @Test
    fun keepsMorningTefillinForSephardiCommunities() {
        assertFalse(TishaBeavTefillinRules.omitsMorningTefillin(EffectiveNusach.SEFARD))
        assertFalse(TishaBeavTefillinRules.omitsMorningTefillin(EffectiveNusach.EDOT_HAMIZRACH))
    }

    @Test
    fun detectsTishaBeavFastIndex() {
        assertTrue(TishaBeavTefillinRules.isTishaBeav(HebrewCalendarEngine.TISHA_BEAV))
        assertFalse(TishaBeavTefillinRules.isTishaBeav(HebrewCalendarEngine.YOM_KIPPUR))
        assertFalse(TishaBeavTefillinRules.isTishaBeav(null))
    }

    @Test
    fun morningTefillinExpiredOnTishaBeavAshkenaz() {
        val z = zmanim()
        val now = z.misheyakirMillis!! + 60_000
        val status = ChecklistZmanEvaluator.evaluate(
            itemId = "put_on_tefillin_during_morning_prayers_except_shabbat_festiv",
            nowMillis = now,
            zmanim = z,
            prayerDay = PrayerDayContext(
                isShabbat = false,
                isYomTov = false,
                isRoshChodesh = false,
                isErevShabbat = false,
                nusach = EffectiveNusach.ASHKENAZ,
                fastDayIndex = HebrewCalendarEngine.TISHA_BEAV,
            ),
        )
        assertEquals(ItemZmanAvailability.EXPIRED, status.availability)
        assertTrue(status.hint?.contains("Tisha B'Av") == true)
    }

    @Test
    fun minchaTefillinItemOpensAfterChatzosOnTishaBeav() {
        val z = zmanim()
        val chatzos = z.chatzosMillis!!
        val beforeChatzos = chatzos - 60_000
        val afterChatzos = chatzos + 60_000
        val prayerDay = PrayerDayContext(
            isShabbat = false,
            isYomTov = false,
            isRoshChodesh = false,
            isErevShabbat = false,
            fastDayIndex = HebrewCalendarEngine.TISHA_BEAV,
        )
        val upcoming = ChecklistZmanEvaluator.evaluate(
            "tefillin_tisha_beav_mincha", beforeChatzos, z, prayerDay,
        )
        val active = ChecklistZmanEvaluator.evaluate(
            "tefillin_tisha_beav_mincha", afterChatzos, z, prayerDay,
        )
        assertEquals(ItemZmanAvailability.UPCOMING, upcoming.availability)
        assertEquals(ItemZmanAvailability.ACTIVE, active.availability)
    }

    private fun zmanim(): ZmanimSnapshot {
        val base = 1_700_000_000_000L
        val hour = 3_600_000L
        return ZmanimSnapshot(
            timezoneId = "Asia/Jerusalem",
            alotHaShacharMillis = base,
            misheyakirMillis = base + hour / 2,
            sunriseMillis = base + hour,
            chatzosMillis = base + 6 * hour,
            minchaGedolaMillis = base + 6 * hour + 30 * 60_000L,
            sunsetMillis = base + 12 * hour,
            tzeitMillis = base + 12 * hour + 30 * 60_000L,
        )
    }
}
