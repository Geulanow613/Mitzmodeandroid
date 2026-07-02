package com.beardytop.beatzaddik.domain

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class HeuristicJewishCalendarBackend : JewishCalendarBackend {

    override fun dayInfoAt(nowEpochMillis: Long, profile: UserProfile): DayInfo {
        val tz = TimeZone.of(profile.timezoneId)
        val local = Instant.fromEpochMilliseconds(nowEpochMillis).toLocalDateTime(tz)
        val date = local.date
        val isFriday = date.dayOfWeek == DayOfWeek.FRIDAY
        val isSaturday = date.dayOfWeek == DayOfWeek.SATURDAY

        val zmanim = null
        val period = ZmanPeriodLogic.activePeriodContext(nowEpochMillis, profile, zmanim)

        return DayInfo(
            date = date,
            civilLabel = ZmanimFormatter.formatCivilDate(date),
            hebrewLabel = "Set location for Hebrew date & zmanim",
            parsha = if (isSaturday || isFriday) "Weekly Parsha" else null,
            statusChips = buildList {
                add(date.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() })
                if (isFriday) add("Erev Shabbat")
                if (isSaturday) add("Shabbat")
            },
            isShabbat = isSaturday,
            isErevShabbat = isFriday,
            isYomTov = false,
            isShabbatOrYomTov = isSaturday,
            activeTimeOfDay = period.timeOfDay,
            activePeriodLabel = period.activeTitle,
            activePeriodHint = period.activeSummary,
            inactivePeriodHint = period.laterSummary,
            zmanim = zmanim
        )
    }

    override fun upcomingHolidays(from: LocalDate, profile: UserProfile): List<UpcomingHoliday> {
        val results = mutableListOf<UpcomingHoliday>()
        for (i in 0..21) {
            val d = from.plus(i, DateTimeUnit.DAY)
            if (d.dayOfWeek == DayOfWeek.FRIDAY) {
                results += UpcomingHoliday("Shabbat", i, "Weekly Shabbat")
            }
        }
        return results.distinctBy { it.name }.take(6)
    }

    override fun electronicsRestPeriod(nowEpochMillis: Long, profile: UserProfile): ElectronicsRestPeriod? {
        val tz = TimeZone.of(profile.timezoneId)
        val local = Instant.fromEpochMilliseconds(nowEpochMillis).toLocalDateTime(tz)
        return when {
            local.dayOfWeek == DayOfWeek.SATURDAY && local.hour < 20 ->
                // Pass Friday's date so saturdayHavdalahMillis correctly adds 1 day → Saturday 20:30
                shabbatRest(profile, isErev = false, endsAtEpochMillis = saturdayHavdalahMillis(local.date.plus(-1, DateTimeUnit.DAY), tz))
            local.dayOfWeek == DayOfWeek.FRIDAY && local.hour >= 18 ->
                shabbatRest(profile, isErev = true, endsAtEpochMillis = saturdayHavdalahMillis(local.date, tz))
            else -> null
        }
    }

    /** Returns heuristic Havdalah time: 20:30 on the day after [fridayDate]. */
    private fun saturdayHavdalahMillis(fridayDate: LocalDate, tz: TimeZone): Long {
        val saturday = fridayDate.plus(1, DateTimeUnit.DAY)
        return LocalDateTime(saturday.year, saturday.monthNumber, saturday.dayOfMonth, 20, 30)
            .toInstant(tz)
            .toEpochMilliseconds()
    }

    private fun shabbatRest(
        profile: UserProfile,
        isErev: Boolean,
        endsAtEpochMillis: Long?
    ): ElectronicsRestPeriod {
        return ElectronicsRestPeriod(
            kind = RestKind.SHABBAT,
            title = if (isErev) "Shabbat is beginning" else "Shabbat Shalom",
            message = shabbatMessage(),
            locationLabel = profile.locationLabel,
            endsAtEpochMillis = endsAtEpochMillis
        )
    }

    private fun formatCivil(date: LocalDate): String = ZmanimFormatter.formatCivilDate(date)
}
