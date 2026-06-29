package com.beardytop.beatzaddik.domain

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

/** Civil Sunday–Saturday week (from midnight Sunday). */
internal object CivilWeek {

    fun sundayStart(date: LocalDate): LocalDate {
        val daysBack = when (date.dayOfWeek) {
            DayOfWeek.SUNDAY -> 0
            DayOfWeek.MONDAY -> 1
            DayOfWeek.TUESDAY -> 2
            DayOfWeek.WEDNESDAY -> 3
            DayOfWeek.THURSDAY -> 4
            DayOfWeek.FRIDAY -> 5
            DayOfWeek.SATURDAY -> 6
        }
        return date.plus(-daysBack, DateTimeUnit.DAY)
    }

    fun saturdayEnd(date: LocalDate): LocalDate =
        sundayStart(date).plus(6, DateTimeUnit.DAY)

    /** True when [eventDate] falls in the same Sun–Sat week as [weekOf]. */
    fun contains(weekOf: LocalDate, eventDate: LocalDate): Boolean {
        val start = sundayStart(weekOf)
        val end = saturdayEnd(weekOf)
        return eventDate in start..end
    }
}
