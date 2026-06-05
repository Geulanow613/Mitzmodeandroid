package com.beardytop.beatzaddik.domain

import com.beardytop.beatzaddik.data.ChecklistCatalog
import com.beardytop.beatzaddik.data.HolidayOverlayEntry
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class JewishCalendarService(
    private val backend: JewishCalendarBackend = createJewishCalendarBackend(),
    private val holidayOverlay: List<HolidayOverlayEntry> = emptyList()
) {
    fun dayInfoAt(nowEpochMillis: Long, profile: UserProfile): DayInfo =
        backend.dayInfoAt(nowEpochMillis, profile)

    fun electronicsRestPeriod(
        nowEpochMillis: Long = Clock.System.now().toEpochMilliseconds(),
        profile: UserProfile
    ): ElectronicsRestPeriod? = backend.electronicsRestPeriod(nowEpochMillis, profile)

    fun upcomingHolidays(
        from: LocalDate = today(),
        profile: UserProfile = UserProfile()
    ): List<UpcomingHoliday> {
        val fromBackend = backend.upcomingHolidays(from, profile)
        val fromOverlay = holidayOverlay.mapNotNull { entry -> overlayToHoliday(entry, from, profile) }
        return (fromBackend + fromOverlay)
            .distinctBy { "${it.name}-${it.daysAway}" }
            .sortedBy { it.daysAway }
            .take(8)
    }

    private fun overlayToHoliday(entry: HolidayOverlayEntry, from: LocalDate, profile: UserProfile): UpcomingHoliday? {
        val hint = prepHintText(entry.prepMitzvot)
        return when (entry.recurring) {
            "weekly_friday" -> {
                val days = daysUntilFriday(from)
                if (days in 1..14) UpcomingHoliday(entry.name, days, hint) else null
            }
            "monthly" -> {
                val days = daysUntilRoshChodesh(from, profile)
                days?.let { UpcomingHoliday(entry.name, it, hint) }
            }
            else -> {
                val days = daysUntilHebrewDate(from, entry.hebrewMonth, entry.hebrewDay, profile)
                days?.let { UpcomingHoliday(entry.name, it, hint) }
            }
        }
    }

    private fun prepHintText(ids: List<String>): String {
        if (ids.isEmpty()) return ""
        return ids.mapNotNull { id ->
            ChecklistCatalog.titleForId(id) ?: PREP_FALLBACK_LABELS[id]
        }.joinToString(" · ")
    }

    private fun daysUntilFriday(from: LocalDate): Int {
        for (i in 0..7) {
            val d = from.plus(i, DateTimeUnit.DAY)
            if (d.dayOfWeek == kotlinx.datetime.DayOfWeek.FRIDAY) return i
        }
        return 7
    }

    private fun daysUntilRoshChodesh(from: LocalDate, profile: UserProfile): Int? {
        for (i in 0..45) {
            val cal = backend.dayInfoAt(
                from.plus(i, DateTimeUnit.DAY).toEpochMillisAtNoon(profile),
                profile
            )
            if (cal.isRoshChodesh && i > 0) return i
        }
        return null
    }

    private fun daysUntilHebrewDate(
        from: LocalDate,
        hebrewMonth: Int?,
        hebrewDay: Int?,
        profile: UserProfile
    ): Int? {
        if (hebrewMonth == null || hebrewDay == null) return null
        for (i in 0..120) {
            val cal = backend.dayInfoAt(
                from.plus(i, DateTimeUnit.DAY).toEpochMillisAtNoon(profile),
                profile
            )
            if (cal.hebrewMonth == hebrewMonth && cal.hebrewDay == hebrewDay) return i
        }
        return null
    }

    private fun today(): LocalDate =
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    companion object {
        private val PREP_FALLBACK_LABELS = mapOf(
            "shabbat_candles" to "Light Shabbat candles",
            "shabbat_prep" to "Prepare for Shabbat",
            "electronics_shabbat" to "Pause electronics on Shabbat"
        )
    }
}

private fun LocalDate.toEpochMillisAtNoon(profile: UserProfile): Long {
    val tz = TimeZone.of(profile.timezoneId)
    return kotlinx.datetime.LocalDateTime(year, monthNumber, dayOfMonth, 12, 0)
        .toInstant(tz)
        .toEpochMilliseconds()
}
