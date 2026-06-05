package com.beardytop.beatzaddik.domain

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

/** Approximate zmanim when KosherJava zmanim is unavailable (iOS / fallback). */
object HeuristicZmanim {
    fun build(nowMillis: Long, profile: UserProfile): ZmanimSnapshot {
        val tz = TimeZone.of(profile.timezoneId)
        val local = Instant.fromEpochMilliseconds(nowMillis).toLocalDateTime(tz)
        val date = local.date

        val sunrise = localDateTimeMillis(date, 6, 30, tz)
        val sunset = localDateTimeMillis(date, 18, 30, tz)
        val dayLength = sunset - sunrise
        // GRA-style proportional hours (sunrise→sunset ÷ 12), not clock noon
        fun endOfHour(h: Int) = sunrise + dayLength * h / 12
        val sofShema = endOfHour(3)
        val sofTefilla = endOfHour(4)
        val chatzos = endOfHour(6)
        val minchaGedola = sunrise + dayLength * 13 / 24 // 6.5 halachic hours after sunrise
        val misheyakir = sunrise - 60 * 60 * 1000L
        val plag = sunset - (75 * 60 * 1000L)
        // ~8.5° below horizon (~33 min after sunset at mid-latitudes; aligns with MyZmanim-style nightfall)
        val tzeit = sunset + (33 * 60 * 1000L)
        val alot = sunrise - (72 * 60 * 1000L)
        val nextAlot = localDateTimeMillis(date.plus(1, DateTimeUnit.DAY), 5, 18, tz)

        return ZmanimSnapshot(
            misheyakirMillis = misheyakir,
            sunriseMillis = sunrise,
            sofZmanShemaMillis = sofShema,
            sofZmanTefillaMillis = sofTefilla,
            chatzosMillis = chatzos,
            minchaGedolaMillis = minchaGedola,
            plagHaminchaMillis = plag,
            sunsetMillis = sunset,
            tzeitMillis = tzeit,
            alotHaShacharMillis = alot,
            nightObligationsEndMillis = nextAlot,
            timezoneId = profile.timezoneId
        )
    }

    private fun localDateTimeMillis(date: LocalDate, hour: Int, minute: Int, tz: TimeZone): Long =
        LocalDateTime(date.year, date.monthNumber, date.dayOfMonth, hour, minute)
            .toInstant(tz)
            .toEpochMilliseconds()
}
