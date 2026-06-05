package com.beardytop.beatzaddik.domain

import kotlinx.datetime.LocalDate

interface JewishCalendarBackend {
    fun dayInfoAt(nowEpochMillis: Long, profile: UserProfile): DayInfo
    fun upcomingHolidays(from: LocalDate, profile: UserProfile): List<UpcomingHoliday>
    fun electronicsRestPeriod(nowEpochMillis: Long, profile: UserProfile): ElectronicsRestPeriod?
}

expect fun createJewishCalendarBackend(): JewishCalendarBackend
