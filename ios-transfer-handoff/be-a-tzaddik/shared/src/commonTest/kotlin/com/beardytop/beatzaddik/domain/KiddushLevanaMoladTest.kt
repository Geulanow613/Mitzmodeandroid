package com.beardytop.beatzaddik.domain

import com.beardytop.beatzaddik.domain.zmanim.SharedZmanimBuilder
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Kiddush Levana opens from the molad (72h Ashkenaz / 7 days Sefard), not merely Hebrew day ≥ 3/7.
 * Av 5786 molad ≈ Jul 14 2026 evening; +72h ≈ Jul 17 ~20:10 Israel summer time.
 */
class KiddushLevanaMoladTest {

    private val tz = "Asia/Jerusalem"
    private val lat = 31.7683
    private val lon = 35.2137

    private fun avDay(day: Int, nusach: EffectiveNusach = EffectiveNusach.ASHKENAZ) =
        PrayerDayContext(
            isShabbat = false,
            isYomTov = false,
            isRoshChodesh = false,
            isErevShabbat = false,
            hebrewDay = day,
            hebrewMonth = HebrewCalendarEngine.AV,
            hebrewYear = 5786,
            nusach = nusach,
        )

    @Test
    fun moladPlus72Hours_av5786_isAfterNightOfThird() {
        val open = HebrewCalendarEngine.tchilasZmanKidushLevana3DaysMillis(5786, HebrewCalendarEngine.AV)
        // Night of 3 Av in Jerusalem (civil Jul 16 evening) is still before 72h from molad.
        val nightOf3Av = LocalDateTime(2026, 7, 16, 22, 0)
            .toInstant(TimeZone.of(tz))
            .toEpochMilliseconds()
        assertTrue(nightOf3Av < open, "expected open=$open after nightOf3Av=$nightOf3Av")
        // Next evening (Jul 17 ~22:00) is after the ~20:10 open.
        val nightOf4Av = LocalDateTime(2026, 7, 17, 22, 0)
            .toInstant(TimeZone.of(tz))
            .toEpochMilliseconds()
        assertTrue(nightOf4Av >= open)
    }

    @Test
    fun nightOf3Av_before72Hours_isUpcoming() {
        val now = LocalDateTime(2026, 7, 16, 22, 0)
            .toInstant(TimeZone.of(tz))
            .toEpochMilliseconds()
        val zmanim = SharedZmanimBuilder.buildForLocation(now, tz, lat, lon)
        val status = ChecklistZmanEvaluator.evaluate(
            itemId = "kiddush_levana",
            nowMillis = now,
            zmanim = zmanim,
            prayerDay = avDay(3),
        )
        assertEquals(ItemZmanAvailability.UPCOMING, status.availability)
        assertTrue(status.hint?.contains("72 hours") == true || status.hintArgs?.get("wait")?.contains("72") == true)
    }

    @Test
    fun after72Hours_on4Av_isActive_withAvWaitHint() {
        val now = LocalDateTime(2026, 7, 17, 22, 0)
            .toInstant(TimeZone.of(tz))
            .toEpochMilliseconds()
        val zmanim = SharedZmanimBuilder.buildForLocation(now, tz, lat, lon)
        val status = ChecklistZmanEvaluator.evaluate(
            itemId = "kiddush_levana",
            nowMillis = now,
            zmanim = zmanim,
            prayerDay = avDay(4),
        )
        assertEquals(ItemZmanAvailability.ACTIVE, status.availability)
        assertTrue(status.hint?.contains("Tisha B'Av") == true)
        assertTrue(status.hint?.contains("unless concerned") == true)
        assertTrue(status.hint?.contains("moon is clear") != true)
    }

    @Test
    fun sefard_stillUpcoming_after72Hours_before7Days() {
        val now = LocalDateTime(2026, 7, 17, 22, 0)
            .toInstant(TimeZone.of(tz))
            .toEpochMilliseconds()
        val zmanim = SharedZmanimBuilder.buildForLocation(now, tz, lat, lon)
        val status = ChecklistZmanEvaluator.evaluate(
            itemId = "kiddush_levana",
            nowMillis = now,
            zmanim = zmanim,
            prayerDay = avDay(4, EffectiveNusach.SEFARD),
        )
        assertEquals(ItemZmanAvailability.UPCOMING, status.availability)
        assertTrue(status.hint?.contains("7 days") == true || status.hintArgs?.get("wait")?.contains("7 days") == true)
    }
}
