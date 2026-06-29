package com.beardytop.beatzaddik.domain

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
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

    /** Like [formatTime] but lowercase am/pm with no space — e.g. "7:53pm", "4:10am". */
    fun formatTimeInline(epochMillis: Long?, timezoneId: String): String? =
        formatTime(epochMillis, timezoneId)
            ?.replace(" AM", "am")
            ?.replace(" PM", "pm")

    /** e.g. "Fri", "Thu" */
    fun formatWeekdayShort(epochMillis: Long?, timezoneId: String): String? {
        if (epochMillis == null) return null
        return runCatching {
            val day = Instant.fromEpochMilliseconds(epochMillis)
                .toLocalDateTime(TimeZone.of(timezoneId))
                .dayOfWeek
            when (day) {
                kotlinx.datetime.DayOfWeek.MONDAY -> "Mon"
                kotlinx.datetime.DayOfWeek.TUESDAY -> "Tue"
                kotlinx.datetime.DayOfWeek.WEDNESDAY -> "Wed"
                kotlinx.datetime.DayOfWeek.THURSDAY -> "Thu"
                kotlinx.datetime.DayOfWeek.FRIDAY -> "Fri"
                kotlinx.datetime.DayOfWeek.SATURDAY -> "Sat"
                kotlinx.datetime.DayOfWeek.SUNDAY -> "Sun"
            }
        }.getOrNull()
    }

    fun formatUntil(epochMillis: Long?, timezoneId: String): String? =
        formatTime(epochMillis, timezoneId)?.let { "until $it" }

    fun formatAfter(epochMillis: Long?, timezoneId: String): String? =
        formatTime(epochMillis, timezoneId)?.let { "after $it" }

    /** e.g. "June 27 at 8:43 PM" in the given timezone (for Shabbat/Yom Tov rest end times). */
    fun formatMonthDayTime(epochMillis: Long?, timezoneId: String): String? {
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
            val month = local.month.name.lowercase().replaceFirstChar { it.uppercase() }
            "$month ${local.dayOfMonth} at $hour12:$minute $amPm"
        }.getOrNull()
    }

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

    /** e.g. "Wednesday, June 26, 2026" */
    fun formatCivilDate(date: LocalDate): String {
        val weekday = date.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
        val month = date.month.name.lowercase().replaceFirstChar { it.uppercase() }
        return "$weekday, $month ${date.dayOfMonth}, ${date.year}"
    }
}
