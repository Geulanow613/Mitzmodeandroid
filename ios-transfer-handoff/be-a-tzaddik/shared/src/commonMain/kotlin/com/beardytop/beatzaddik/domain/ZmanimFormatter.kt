package com.beardytop.beatzaddik.domain

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object ZmanimFormatter {

    fun uses24HourClock(languageCode: String?): Boolean =
        languageCode == "he" || languageCode == "yi"

    fun formatTime(epochMillis: Long?, timezoneId: String, languageCode: String? = null): String? {
        if (epochMillis == null) return null
        return runCatching {
            val local = Instant.fromEpochMilliseconds(epochMillis)
                .toLocalDateTime(TimeZone.of(timezoneId))
            if (uses24HourClock(languageCode)) {
                val hour = local.hour.toString().padStart(2, '0')
                val minute = local.minute.toString().padStart(2, '0')
                "$hour:$minute"
            } else {
                val hour24 = local.hour
                val hour12 = when {
                    hour24 == 0 -> 12
                    hour24 > 12 -> hour24 - 12
                    else -> hour24
                }
                val amPm = if (hour24 < 12) "AM" else "PM"
                val minute = local.minute.toString().padStart(2, '0')
                "$hour12:$minute $amPm"
            }
        }.getOrNull()
    }

    /** Compact clock label — 12h lowercase am/pm (e.g. "7:53pm") or 24h for Hebrew. */
    fun formatTimeInline(epochMillis: Long?, timezoneId: String, languageCode: String? = null): String? =
        formatTime(epochMillis, timezoneId, languageCode)
            ?.let { time ->
                if (uses24HourClock(languageCode)) time
                else time.replace(" AM", "am").replace(" PM", "pm")
            }

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

    fun formatUntil(epochMillis: Long?, timezoneId: String, languageCode: String? = null): String? =
        formatTime(epochMillis, timezoneId, languageCode)?.let { "until $it" }

    fun formatAfter(epochMillis: Long?, timezoneId: String, languageCode: String? = null): String? =
        formatTime(epochMillis, timezoneId, languageCode)?.let { "after $it" }

    /** e.g. "June 27 at 8:43 PM" or "June 27 at 19:43" (Hebrew). */
    fun formatMonthDayTime(epochMillis: Long?, timezoneId: String, languageCode: String? = null): String? {
        if (epochMillis == null) return null
        return runCatching {
            val local = Instant.fromEpochMilliseconds(epochMillis)
                .toLocalDateTime(TimeZone.of(timezoneId))
            val time = formatTime(epochMillis, timezoneId, languageCode) ?: return@runCatching null
            val month = local.month.name.lowercase().replaceFirstChar { it.uppercase() }
            "$month ${local.dayOfMonth} at $time"
        }.getOrNull()
    }

    /** e.g. "6:14 PM on 6/2/2026" */
    fun formatTimeWithDate(epochMillis: Long?, timezoneId: String, languageCode: String? = null): String? {
        val time = formatTime(epochMillis, timezoneId, languageCode) ?: return null
        if (epochMillis == null) return null
        val datePart = runCatching {
            val local = Instant.fromEpochMilliseconds(epochMillis)
                .toLocalDateTime(TimeZone.of(timezoneId))
            "${local.monthNumber}/${local.dayOfMonth}/${local.year}"
        }.getOrNull() ?: return time
        return "$time on $datePart"
    }

    private val CLOCK_12H = Regex("""(\d{1,2}):(\d{2})\s*(am|pm)""", RegexOption.IGNORE_CASE)

    /** Converts embedded 12h strings (e.g. candles hints) to 24h when [languageCode] is Hebrew. */
    fun reformatClockStringForLanguage(clock: String, languageCode: String?): String {
        if (!uses24HourClock(languageCode)) return clock
        if (Regex("""^\d{1,2}:\d{2}$""").matches(clock.trim())) return clock.trim()
        val match = CLOCK_12H.find(clock) ?: return clock
        var hour = match.groupValues[1].toInt()
        val minute = match.groupValues[2]
        val pm = match.groupValues[3].equals("pm", ignoreCase = true)
        if (pm && hour != 12) hour += 12
        if (!pm && hour == 12) hour = 0
        val formatted = "${hour.toString().padStart(2, '0')}:$minute"
        return clock.replaceRange(match.range, formatted)
    }

    /** e.g. "Wednesday, June 26, 2026" */
    fun formatCivilDate(date: LocalDate): String {
        val weekday = date.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
        val month = date.month.name.lowercase().replaceFirstChar { it.uppercase() }
        return "$weekday, $month ${date.dayOfMonth}, ${date.year}"
    }
}
