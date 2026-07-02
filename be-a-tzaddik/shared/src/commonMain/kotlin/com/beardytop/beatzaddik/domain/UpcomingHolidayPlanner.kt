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

/**
 * Halachic-aware upcoming observance list.
 * Counting starts at alot hashachar; erev days show major night observances as "Tonight" (daysAway = 0).
 */
internal object UpcomingHolidayPlanner {

    fun plan(
        backend: JewishCalendarBackend,
        nowEpochMillis: Long,
        profile: UserProfile,
    ): List<UpcomingHoliday> {
        val nowInfo = backend.dayInfoAt(nowEpochMillis, profile)
        val from = ZmanPeriodLogic.effectivePlanningDate(
            nowMillis = nowEpochMillis,
            civilDate = nowInfo.date,
            zmanim = nowInfo.zmanim,
        )

        val civilToday = Instant.fromEpochMilliseconds(nowEpochMillis)
            .toLocalDateTime(TimeZone.of(profile.timezoneId))
            .date

        var nextShabbat: UpcomingHoliday? = null
        var nextYomTov: UpcomingHoliday? = null
        var nextChanukah: UpcomingHoliday? = null
        var nextPurim: UpcomingHoliday? = null
        var nextRoshChodesh: UpcomingHoliday? = null
        var nextMinorHoliday: UpcomingHoliday? = null

        if (nowInfo.isRoshChodesh) {
            nextRoshChodesh = activeRoshChodeshHoliday(nowInfo, nowEpochMillis, backend, profile)
        }

        for (i in 0..60) {
            val d = from.plus(i, DateTimeUnit.DAY)
            val info = if (i == 0 && d == nowInfo.date) {
                nowInfo
            } else {
                backend.dayInfoAt(d.toEpochMillisAtNoon(profile), profile)
            }
            val tomorrow = backend.dayInfoAt(
                d.plus(1, DateTimeUnit.DAY).toEpochMillisAtNoon(profile),
                profile,
            )

            if (nextShabbat == null && info.isErevShabbat && !nowInfo.isShabbat) {
                nextShabbat = UpcomingHoliday(
                    name = "Shabbat",
                    daysAway = i,
                    hint = "Candles, Kiddush, rest from electronics",
                    beginsTonightWhenImminent = true,
                    timingHint = UpcomingHolidayTiming.timingHintIfThisWeek(
                        civilToday,
                        info.date,
                        UpcomingHolidayTiming.shabbatStartsLabel(info, profile),
                    ),
                )
            }

            if (nextYomTov == null) {
                info.upcomingChagName?.let { name ->
                    val chagIdx = info.upcomingChagYomTovIndex
                    val rawTimingHint = when {
                        name == "Yom Kippur" && chagIdx == HebrewCalendarEngine.YOM_KIPPUR && i == 0 ->
                            UpcomingHolidayTiming.yomKippurErevStartsLabel(info, profile)
                        name == "Yom Kippur" && chagIdx == HebrewCalendarEngine.YOM_KIPPUR ->
                            UpcomingHolidayTiming.sunsetFastStartsLabel(info, profile)
                        else ->
                            UpcomingHolidayTiming.yomTovStartsLabel(info, tomorrow, chagIdx, profile)
                    }
                    nextYomTov = UpcomingHoliday(
                        name = name,
                        daysAway = i,
                        hint = UpcomingHolidayNames.yomTovPrepHint(name),
                        beginsTonightWhenImminent = true,
                        timingHint = UpcomingHolidayTiming.timingHintIfThisWeek(
                            civilToday,
                            info.date,
                            rawTimingHint,
                        ),
                    )
                }
            }

            if (nextChanukah == null && "erev_chanukah" in info.activeSeasons) {
                nextChanukah = UpcomingHoliday(
                    name = "Chanukah",
                    daysAway = i,
                    hint = "Light menorah each night",
                    beginsTonightWhenImminent = true,
                    timingHint = UpcomingHolidayTiming.timingHintIfThisWeek(
                        civilToday,
                        info.date,
                        UpcomingHolidayTiming.chanukahStartsLabel(info, profile),
                    ),
                )
            }

            if (nextPurim == null && "erev_purim" in info.activeSeasons) {
                nextPurim = UpcomingHoliday(
                    name = "Purim",
                    daysAway = i + 1,
                    hint = "Megillah, matanot, mishloach manot",
                    beginsTonightWhenImminent = false,
                    timingHint = UpcomingHolidayTiming.timingHintIfThisWeek(
                        civilToday,
                        info.date,
                        UpcomingHolidayTiming.purimStartsLabel(info, profile),
                    ),
                )
            }

            if (nextRoshChodesh == null && !info.isRoshChodesh && tomorrow.isRoshChodesh && i == 0) {
                // Erev Rosh Chodesh (from alot): observance begins tonight.
                nextRoshChodesh = UpcomingHoliday(
                    name = "Rosh Chodesh",
                    daysAway = 0,
                    hint = "Yaaleh V'yavo, Hallel",
                    beginsTonightWhenImminent = true,
                    timingHint = UpcomingHolidayTiming.timingHintIfThisWeek(
                        civilToday,
                        info.date,
                        UpcomingHolidayTiming.roshChodeshStartsLabel(info, profile),
                    ),
                )
            } else if (nextRoshChodesh == null && info.isRoshChodesh && i > 0) {
                // Count down to the next Rosh Chodesh (not the day already in progress tonight).
                val eveDate = UpcomingHolidayTiming.eveBefore(info.date)
                nextRoshChodesh = UpcomingHoliday(
                    name = "Rosh Chodesh",
                    daysAway = i,
                    hint = "Yaaleh V'yavo, Hallel",
                    beginsTonightWhenImminent = false,
                    timingHint = UpcomingHolidayTiming.timingHintIfThisWeek(
                        civilToday,
                        eveDate,
                        UpcomingHolidayTiming.roshChodeshStartsLabel(
                            UpcomingHolidayTiming.dayInfoOn(backend, eveDate, profile),
                            profile,
                        ),
                    ),
                )
            }

            if (nextMinorHoliday == null) {
                erevFastHolidayOnDay(info, tomorrow, backend, profile, i, civilToday)?.let { holiday ->
                    nextMinorHoliday = holiday
                }
            }

            if (nextMinorHoliday == null) {
                fastHolidayOnDay(info, backend, profile, i, civilToday)?.let { holiday ->
                    nextMinorHoliday = holiday
                }
            }
        }

        return listOfNotNull(
            nextShabbat,
            nextYomTov,
            nextChanukah,
            nextPurim,
            nextRoshChodesh,
            nextMinorHoliday,
        )
            .sortedBy { it.daysAway }
            .take(8)
    }

    private fun activeRoshChodeshHoliday(
        nowInfo: DayInfo,
        nowMillis: Long,
        backend: JewishCalendarBackend,
        profile: UserProfile,
    ): UpcomingHoliday {
        val endMillis = roshChodeshPeriodEndMillis(nowInfo, nowMillis, backend, profile)
        val tz = profile.timezoneId
        val whenLabel = buildRoshChodeshActiveWhenLabel(endMillis, tz)
        return UpcomingHoliday(
            name = "Rosh Chodesh",
            daysAway = 0,
            hint = "Yaaleh V'yavo, Hallel",
            beginsTonightWhenImminent = false,
            whenLabelOverride = whenLabel,
        )
    }

    /** Tzeit when the current Rosh Chodesh stint ends (start of the first non–Rosh Chodesh day). */
    private fun roshChodeshPeriodEndMillis(
        nowInfo: DayInfo,
        nowMillis: Long,
        backend: JewishCalendarBackend,
        profile: UserProfile,
    ): Long? {
        var lastRcDate = nowInfo.date
        for (j in 0..2) {
            val nextDate = lastRcDate.plus(1, DateTimeUnit.DAY)
            val nextInfo = backend.dayInfoAt(nextDate.toEpochMillisAtNoon(profile), profile)
            if (nextInfo.isRoshChodesh) {
                lastRcDate = nextDate
            } else {
                break
            }
        }
        fun tzeitOn(date: LocalDate): Long? =
            backend.dayInfoAt(date.toEpochMillisAtNoon(profile), profile)
                .zmanim?.tzeitMillis
                ?: backend.dayInfoAt(date.toEpochMillisAtNoon(profile), profile)
                    .zmanim?.sunsetMillis

        val lastDayTzeit = tzeitOn(lastRcDate)
        if (lastDayTzeit != null && lastDayTzeit > nowMillis) return lastDayTzeit
        return tzeitOn(lastRcDate.plus(1, DateTimeUnit.DAY)) ?: lastDayTzeit
    }

    private fun buildRoshChodeshActiveWhenLabel(endMillis: Long?, timezoneId: String): String {
        if (endMillis == null) return "Now"
        val time = ZmanimFormatter.formatTime(endMillis, timezoneId)
        val night = weekdayNightLabel(endMillis, timezoneId)
        return if (time != null) "Now — ends $time $night" else "Now — ends $night"
    }

    private fun weekdayNightLabel(epochMillis: Long, timezoneId: String): String {
        val dayName = runCatching {
            Instant.fromEpochMilliseconds(epochMillis)
                .toLocalDateTime(TimeZone.of(timezoneId))
                .dayOfWeek
                .name
                .lowercase()
                .replaceFirstChar { it.uppercase() }
        }.getOrNull()
        return if (dayName != null) "$dayName night" else "at nightfall"
    }

    private fun yomTovIndexOnDay(info: DayInfo, profile: UserProfile): Int? {
        val year = info.hebrewYear ?: return null
        val month = info.hebrewMonth ?: return null
        val day = info.hebrewDay ?: return null
        return HebrewCalendarEngine.getYomTovIndex(
            year = year,
            month = month,
            day = day,
            dayOfWeek = info.date.toJavaDayOfWeek(),
            isLeapYear = HebrewCalendarEngine.isJewishLeapYear(year),
            inIsrael = profile.isInIsrael,
            useModernHolidays = true,
        )
    }

    private fun erevFastHolidayOnDay(
        info: DayInfo,
        tomorrow: DayInfo,
        backend: JewishCalendarBackend,
        profile: UserProfile,
        daysAway: Int,
        civilToday: LocalDate,
    ): UpcomingHoliday? {
        when {
            "erev_tisha_beav" in info.activeSeasons -> {
                return UpcomingHoliday(
                    name = "Tisha B'Av",
                    daysAway = daysAway,
                    hint = "Fast day — mourning the Temple",
                    beginsTonightWhenImminent = true,
                    timingHint = UpcomingHolidayTiming.timingHintIfThisWeek(
                        civilToday,
                        info.date,
                        UpcomingHolidayTiming.sunsetFastStartsLabel(info, profile),
                    ),
                )
            }
            "erev_minor_fast" in info.activeSeasons -> {
                val fastIdx = info.upcomingFastDayIndex ?: return null
                val hint = minorHolidayForIndex(fastIdx)?.second ?: "Fast day"
                return UpcomingHoliday(
                    name = PublicFastDayRules.displayName(fastIdx),
                    daysAway = daysAway + 1,
                    hint = hint,
                    beginsTonightWhenImminent = false,
                    timingHint = UpcomingHolidayTiming.timingHintIfThisWeek(
                        civilToday,
                        tomorrow.date,
                        UpcomingHolidayTiming.minorFastStartsLabel(tomorrow, profile),
                    ),
                )
            }
        }
        return null
    }

    private fun fastHolidayOnDay(
        info: DayInfo,
        backend: JewishCalendarBackend,
        profile: UserProfile,
        daysAway: Int,
        civilToday: LocalDate,
    ): UpcomingHoliday? {
        val idx = yomTovIndexOnDay(info, profile) ?: return null
        val (name, hint) = minorHolidayForIndex(idx) ?: return null
        val timingHint = timingHintForMinorIdx(idx, info, backend, profile, civilToday)
        return UpcomingHoliday(
            name = name,
            daysAway = daysAway,
            hint = hint,
            beginsTonightWhenImminent = false,
            timingHint = timingHint,
        )
    }

    private fun timingHintForMinorIdx(
        idx: Int,
        info: DayInfo,
        backend: JewishCalendarBackend,
        profile: UserProfile,
        civilToday: LocalDate,
    ): String? {
        val (anchorDate, hint) = when {
            PublicFastDayRules.isPublicFast(idx) -> {
                if (PublicFastDayRules.fastStartsAtSunset(idx)) {
                    val erev = fastStartDayInfo(idx, info, backend, profile)
                    erev.date to UpcomingHolidayTiming.sunsetFastStartsLabel(erev, profile)
                } else {
                    info.date to UpcomingHolidayTiming.minorFastStartsLabel(info, profile)
                }
            }
            isIsraeliMemorialDay(idx) -> {
                val eveDate = UpcomingHolidayTiming.eveBefore(info.date)
                val eve = UpcomingHolidayTiming.dayInfoOn(backend, eveDate, profile)
                eveDate to UpcomingHolidayTiming.israeliMemorialStartsLabel(eve, profile)
            }
            else -> return null
        }
        return UpcomingHolidayTiming.timingHintIfThisWeek(civilToday, anchorDate, hint)
    }

    private fun isIsraeliMemorialDay(idx: Int): Boolean = idx in setOf(
        HebrewCalendarEngine.YOM_HASHOAH,
        HebrewCalendarEngine.YOM_HAZIKARON,
        HebrewCalendarEngine.YOM_HAATZMAUT,
        HebrewCalendarEngine.YOM_YERUSHALAYIM,
    )

    /** Civil day whose zmanim mark when the fast begins (dawn day or erev sunset day). */
    private fun fastStartDayInfo(
        fastIdx: Int,
        fastDayInfo: DayInfo,
        backend: JewishCalendarBackend,
        profile: UserProfile,
    ): DayInfo {
        if (!PublicFastDayRules.fastStartsAtSunset(fastIdx)) return fastDayInfo
        val erevDate = fastDayInfo.date.plus(-1, DateTimeUnit.DAY)
        return backend.dayInfoAt(erevDate.toEpochMillisAtNoon(profile), profile)
    }

    private fun minorHolidayForIndex(idx: Int): Pair<String, String>? = when (idx) {
        HebrewCalendarEngine.TU_BESHVAT ->
            "Tu B'Shvat" to "Minor holiday — New Year for Trees"
        HebrewCalendarEngine.PESACH_SHENI ->
            "Pesach Sheni" to "Minor holiday — Second Passover"
        HebrewCalendarEngine.LAG_BAOMER ->
            "Lag BaOmer" to "Minor holiday — 33rd day of the Omer"
        HebrewCalendarEngine.TU_BEAV ->
            "Tu B'Av" to "Minor holiday — celebration of joy"
        HebrewCalendarEngine.TISHA_BEAV ->
            "Tisha B'Av" to "Fast day — mourning the Temple"
        HebrewCalendarEngine.FAST_OF_GEDALYAH ->
            "Fast of Gedaliah" to "Fast day"
        HebrewCalendarEngine.TENTH_OF_TEVES ->
            "Fast of 10 Tevet" to "Fast day"
        HebrewCalendarEngine.FAST_OF_ESTHER ->
            "Fast of Esther" to "Fast day — before Purim"
        HebrewCalendarEngine.SEVENTEEN_OF_TAMMUZ ->
            "Fast of 17 Tammuz" to "Fast day"
        HebrewCalendarEngine.YOM_HASHOAH ->
            "Yom HaShoah" to "Holocaust Remembrance Day"
        HebrewCalendarEngine.YOM_HAZIKARON ->
            "Yom HaZikaron" to "Israeli Fallen Soldiers Memorial Day"
        HebrewCalendarEngine.YOM_HAATZMAUT ->
            "Yom Ha'atzmaut" to "Israeli Independence Day — customs vary by community"
        HebrewCalendarEngine.YOM_YERUSHALAYIM ->
            "Yom Yerushalayim" to "Jerusalem Day — customs vary by community"
        else -> null
    }

    private fun LocalDate.toJavaDayOfWeek(): Int = when (dayOfWeek) {
        DayOfWeek.SUNDAY -> HebrewCalendarEngine.SUNDAY
        DayOfWeek.MONDAY -> HebrewCalendarEngine.MONDAY
        DayOfWeek.TUESDAY -> HebrewCalendarEngine.TUESDAY
        DayOfWeek.WEDNESDAY -> HebrewCalendarEngine.WEDNESDAY
        DayOfWeek.THURSDAY -> HebrewCalendarEngine.THURSDAY
        DayOfWeek.FRIDAY -> HebrewCalendarEngine.FRIDAY
        DayOfWeek.SATURDAY -> HebrewCalendarEngine.SATURDAY
    }
}

private fun LocalDate.toEpochMillisAtNoon(profile: UserProfile): Long {
    val tz = TimeZone.of(profile.timezoneId)
    return LocalDateTime(year, monthNumber, dayOfMonth, 12, 0)
        .toInstant(tz)
        .toEpochMilliseconds()
}

