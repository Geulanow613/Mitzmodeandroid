package com.beardytop.beatzaddik.domain

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Halachic day boundaries for mitzvot counted from tzeit to tzeit (nightfall to nightfall).
 */
object TzeitDay {

    const val WOMENS_DAILY_PRAYER_ID = "at_least_one_prayer_daily_typically_morning"

    /**
     * Epoch millis of the tzeit that started the current halachic day, as a string key for storage.
     * Before today's tzeit, the day began at yesterday's tzeit.
     */
    fun currentKey(nowMillis: Long, today: DayInfo, yesterday: DayInfo): String? {
        val todayTzeit = today.zmanim?.tzeitMillis ?: today.zmanim?.sunsetMillis
        val yesterdayTzeit = yesterday.zmanim?.tzeitMillis ?: yesterday.zmanim?.sunsetMillis
        val dayStart = when {
            todayTzeit != null && nowMillis >= todayTzeit -> todayTzeit
            yesterdayTzeit != null -> yesterdayTzeit
            else -> todayTzeit
        }
        return dayStart?.toString()
    }

    /**
     * Stable day key for prefs that must not flicker when zmanim are missing.
     * Prefers tzeit→tzeit; falls back to the civil calendar date in [timezoneId].
     */
    fun currentKeyOrCivilDate(
        nowMillis: Long,
        today: DayInfo,
        yesterday: DayInfo,
        timezoneId: String,
    ): String {
        currentKey(nowMillis, today, yesterday)?.let { return it }
        return runCatching {
            Instant.fromEpochMilliseconds(nowMillis)
                .toLocalDateTime(TimeZone.of(timezoneId))
                .date
                .toString()
        }.getOrElse { "civil-$nowMillis" }
    }
}
