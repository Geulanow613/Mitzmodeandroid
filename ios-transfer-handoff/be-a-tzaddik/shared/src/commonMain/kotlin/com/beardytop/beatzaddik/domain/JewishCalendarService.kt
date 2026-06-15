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

    /** Stable calendar snapshot for a civil date (noon local) — used for tomorrow lookahead. */
    fun dayInfoForDate(date: LocalDate, profile: UserProfile): DayInfo =
        backend.dayInfoAt(date.toEpochMillisAtNoon(profile), profile)

    fun electronicsRestPeriod(
        nowEpochMillis: Long = Clock.System.now().toEpochMilliseconds(),
        profile: UserProfile
    ): ElectronicsRestPeriod? = ElectronicsRestEvaluator.evaluate(this, nowEpochMillis, profile)

    fun upcomingHolidays(
        nowEpochMillis: Long = Clock.System.now().toEpochMilliseconds(),
        profile: UserProfile = UserProfile()
    ): List<UpcomingHoliday> {
        val nowInfo = dayInfoAt(nowEpochMillis, profile)
        val from = ZmanPeriodLogic.effectivePlanningDate(
            nowMillis = nowEpochMillis,
            civilDate = nowInfo.date,
            zmanim = nowInfo.zmanim,
        )
        val fromPlanner = UpcomingHolidayPlanner.plan(backend, nowEpochMillis, profile)
        val fromOverlay = holidayOverlay.mapNotNull { entry ->
            overlayToHoliday(entry, from, profile)
        }
        return (fromPlanner + fromOverlay)
            .distinctBy { it.name }
            .sortedBy { it.daysAway }
            .take(8)
    }

    private fun overlayToHoliday(entry: HolidayOverlayEntry, from: LocalDate, profile: UserProfile): UpcomingHoliday? {
        val hint = prepHintText(entry.prepMitzvot)
        return when (entry.recurring) {
            "weekly_friday" -> {
                val days = daysUntilFriday(from)
                if (days in 0..14) {
                    UpcomingHoliday(
                        name = entry.name,
                        daysAway = days,
                        hint = hint,
                        beginsTonightWhenImminent = true,
                    )
                } else {
                    null
                }
            }
            "monthly" -> {
                val days = daysUntilRoshChodesh(from, profile)
                days?.let {
                    UpcomingHoliday(
                        name = entry.name,
                        daysAway = it,
                        hint = hint,
                        beginsTonightWhenImminent = true,
                    )
                }
            }
            else -> {
                val days = daysUntilHebrewDate(from, entry.hebrewMonth, entry.hebrewDay, profile)
                days?.let {
                    UpcomingHoliday(
                        name = entry.name,
                        daysAway = it,
                        hint = hint,
                        beginsTonightWhenImminent = false,
                    )
                }
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
        val today = backend.dayInfoAt(from.toEpochMillisAtNoon(profile), profile)
        if (today.isRoshChodesh) return null
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
