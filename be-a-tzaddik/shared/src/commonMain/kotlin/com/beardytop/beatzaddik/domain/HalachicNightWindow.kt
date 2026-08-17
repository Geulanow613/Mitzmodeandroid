package com.beardytop.beatzaddik.domain

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Night-mitzvah window for the **current Hebrew day**.
 *
 * [DayInfo.startedTonightAtTzeit] is only true from tzeit until civil midnight (the backend
 * rollover path). After midnight the Hebrew day is still the one that began at last night's
 * tzeit, but that flag flips off — so any night row / label / zman that must stay correct
 * through dawn must use this helper instead of raw `startedTonightAtTzeit`.
 *
 * **Rule for new code:** for "still tonight until dawn," call [isOpen]. Do not invent
 * one-off midnight branches.
 */
object HalachicNightWindow {

    /**
     * True from the tzeit that began [cal]'s Hebrew day through dawn.
     * False during daytime (after dawn, before tonight's next tzeit).
     */
    fun isOpen(cal: DayInfo, nowMillis: Long): Boolean {
        if (cal.startedTonightAtTzeit) {
            val dawn = dawnMillis(cal)
            return dawn == null || nowMillis < dawn
        }
        // After civil midnight: Hebrew day already advanced at last night's tzeit;
        // still "tonight" until dawn.
        val dawn = dawnMillis(cal)
        if (dawn != null) return nowMillis < dawn
        // No zmanim/location on file -- fall back to the civil clock. Only the
        // post-midnight sliver (00:00-05:00) of civil NIGHT counts as "still tonight";
        // 20:00-midnight NIGHT is tomorrow's evening, not a continuation of last night.
        if (cal.activeTimeOfDay != TimeOfDay.NIGHT) return false
        val hour = localHour(nowMillis, cal.zmanim?.timezoneId) ?: return false
        return hour < 5
    }

    private fun localHour(nowMillis: Long, timezoneId: String?): Int? {
        if (timezoneId.isNullOrBlank()) return null
        return runCatching {
            Instant.fromEpochMilliseconds(nowMillis)
                .toLocalDateTime(TimeZone.of(timezoneId))
                .hour
        }.getOrNull()
    }

    fun dawnMillis(cal: DayInfo): Long? =
        cal.zmanim?.alotHaShacharMillis ?: cal.zmanim?.sunriseMillis

    /**
     * Start of the active evening mitzvah window for [nowMillis].
     * Prefers [ZmanPeriodLogic.effectiveEveningStart] so post-tzeit (tomorrow-noon) zmanim
     * and midnight→dawn do not point at the *next* nightfall.
     */
    fun nightStartMillis(nowMillis: Long, z: ZmanimSnapshot): Long? =
        ZmanPeriodLogic.effectiveNightfallStart(nowMillis, z) ?: z.tzeitMillis
}
