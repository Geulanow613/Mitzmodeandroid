package com.beardytop.beatzaddik.domain

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

/**
 * Day vs afternoon vs night for checklist sections and zman windows.
 * Uses sunset (not only tzeit) so evening is active after sundown even when
 * KosherJava returns the next calendar day's tzeit.
 *
 * Afternoon runs from earliest Mincha (Mincha Gedola) until nightfall (sunset).
 */
object ZmanPeriodLogic {

    fun activeTimeOfDay(nowMillis: Long, zmanim: ZmanimSnapshot): TimeOfDay {
        val sunrise = zmanim.sunriseMillis
        if (sunrise != null && nowMillis < sunrise) return TimeOfDay.NIGHT

        val sunset = zmanim.sunsetMillis
        if (sunset != null && nowMillis >= sunset) return TimeOfDay.NIGHT

        val tzeit = zmanim.tzeitMillis
        if (tzeit != null && nowMillis >= tzeit) return TimeOfDay.NIGHT

        val minchaStart = afternoonStartMillis(zmanim)
        if (minchaStart != null && nowMillis >= minchaStart) return TimeOfDay.AFTERNOON

        return TimeOfDay.DAY
    }

    fun afternoonStartMillis(zmanim: ZmanimSnapshot): Long? =
        zmanim.minchaGedolaMillis
            ?: zmanim.chatzosMillis?.let { it + 30 * 60 * 1000L }

    /**
     * When evening checklist items become available — sunset (shkiah), not tzeit.
     * Tzeit is only a fallback if sunset is unavailable from zmanim.
     */
    fun eveningMitzvahStart(zmanim: ZmanimSnapshot): Long? =
        zmanim.sunsetMillis ?: zmanim.tzeitMillis

    /**
     * When the current evening obligation window began (or will begin).
     *
     * - After today's sunset: tonight's sunset.
     * - Between dawn and sunset (e.g. 2pm): tonight's sunset (items show as UPCOMING).
     * - After midnight but before dawn: yesterday's sunset (still finishing last night).
     */
    fun effectiveEveningStart(nowMillis: Long, zmanim: ZmanimSnapshot): Long? {
        val todayStart = eveningMitzvahStart(zmanim) ?: return null
        if (nowMillis >= todayStart) return todayStart

        val dawn = zmanim.alotHaShacharMillis ?: zmanim.sunriseMillis
        if (dawn != null && nowMillis < dawn) {
            return todayStart - 24 * 60 * 60 * 1000L
        }
        return todayStart
    }

    /**
     * End of the active evening window for [nowMillis].
     * Pairs with [effectiveEveningStart] so daytime hours are not treated as "inside" last night.
     */
    fun effectiveEveningEnd(nowMillis: Long, zmanim: ZmanimSnapshot): Long? {
        val todayDawn = zmanim.alotHaShacharMillis ?: zmanim.sunriseMillis
        val todayStart = eveningMitzvahStart(zmanim) ?: return nightObligationWindowEnd(zmanim)
        if (nowMillis >= todayStart) {
            return nightObligationWindowEnd(zmanim)
        }
        val dawn = todayDawn
        if (dawn != null && nowMillis < dawn) {
            return dawn
        }
        return nightObligationWindowEnd(zmanim)
    }

    /** Next dawn (alot hashachar) — end of obligations that began at the previous sunset. */
    fun nightObligationWindowEnd(zmanim: ZmanimSnapshot): Long? =
        zmanim.nightObligationsEndMillis ?: zmanim.alotHaShacharMillis

    /**
     * Civil date for counting days until upcoming observances.
     * Before alot hashachar we still count as the previous morning-day
     * (e.g. Friday 4am → Shabbat is tomorrow, not tonight).
     */
    fun effectivePlanningDate(
        nowMillis: Long,
        civilDate: LocalDate,
        zmanim: ZmanimSnapshot?,
    ): LocalDate {
        val dawn = zmanim?.alotHaShacharMillis ?: zmanim?.sunriseMillis
        if (dawn != null && nowMillis < dawn) {
            return civilDate.plus(-1, DateTimeUnit.DAY)
        }
        return civilDate
    }
}
