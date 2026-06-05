package com.beardytop.beatzaddik.domain

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object ZmanimFormatter {
    fun formatTime(epochMillis: Long?, timezoneId: String): String? {
        if (epochMillis == null) return null
        return runCatching {
            val local = Instant.fromEpochMilliseconds(epochMillis)
                .toLocalDateTime(TimeZone.of(timezoneId))
            val hour24 = local.hour
            val hour12 = when {
                hour24 == 0 -> 12
                hour24 > 12 -> hour24 - 12
                else -> hour24
            }
            val amPm = if (hour24 < 12) "AM" else "PM"
            val minute = local.minute.toString().padStart(2, '0')
            "$hour12:$minute $amPm"
        }.getOrNull()
    }

    fun formatUntil(epochMillis: Long?, timezoneId: String): String? =
        formatTime(epochMillis, timezoneId)?.let { "until $it" }

    fun formatAfter(epochMillis: Long?, timezoneId: String): String? =
        formatTime(epochMillis, timezoneId)?.let { "after $it" }

    /** e.g. "6:14 PM on 6/2/2026" */
    fun formatTimeWithDate(epochMillis: Long?, timezoneId: String): String? {
        val time = formatTime(epochMillis, timezoneId) ?: return null
        if (epochMillis == null) return null
        val datePart = runCatching {
            val local = Instant.fromEpochMilliseconds(epochMillis)
                .toLocalDateTime(TimeZone.of(timezoneId))
            "${local.monthNumber}/${local.dayOfMonth}/${local.year}"
        }.getOrNull() ?: return time
        return "$time on $datePart"
    }
}
