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
 * Counting starts at alot hashachar. Evening observances show "Tonight" from alot on the erev day;
 * the previous night shows a weekday-evening label (e.g. "Mon evening").
 */
internal object UpcomingHolidayPlanner {

    fun plan(
        backend: JewishCalendarBackend,
        nowEpochMillis: Long,
        profile: UserProfile,
    ): List<UpcomingHoliday> {
        val nowInfo = backend.dayInfoAt(nowEpochMillis, profile)
        val civilToday = Instant.fromEpochMilliseconds(nowEpochMillis)
            .toLocalDateTime(TimeZone.of(profile.timezoneId))
            .date
        val planningToday = ZmanPeriodLogic.effectivePlanningDate(
            nowMillis = nowEpochMillis,
            civilDate = civilToday,
            zmanim = nowInfo.zmanim,
        )
        // Days-away labels ("tomorrow", "in 2 days") are civil-midnight based.
        // Separate logic (alot-based) is used only for when an evening observance may say "Tonight".
        val from = civilToday

        var nextShabbat: UpcomingHoliday? = null
        var nextYomTov: UpcomingHoliday? = null
        var nextChanukah: UpcomingHoliday? = null
        var nextPurim: UpcomingHoliday? = null
        var nextRoshChodesh: UpcomingHoliday? = null
        val minorHolidays = linkedMapOf<String, UpcomingHoliday>()

        fun addMinorHoliday(holiday: UpcomingHoliday) {
            minorHolidays.putIfAbsent(holiday.name, holiday)
        }

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

            val isCivilErevShabbat = d.dayOfWeek == DayOfWeek.FRIDAY
            if (nextShabbat == null && isCivilErevShabbat && !nowInfo.isShabbat) {
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
                        // Yom Kippur begins the evening before (sunset), so count to the start.
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

            if (nextPurim == null) {
                val isJerusalem = JerusalemPurimRules.isJerusalemProfile(profile)
                when {
                    "erev_purim" in info.activeSeasons -> {
                        val meshulashErev = isJerusalem &&
                            "purim_meshulash_friday" in tomorrow.activeSeasons
                        val onErevFromAlot = UpcomingHolidayTiming.canShowTonightForEveningObservance(
                            nowEpochMillis,
                            info.date,
                            info.zmanim,
                            profile.timezoneId,
                        )
                        val rawTimingHint = if (onErevFromAlot) {
                            UpcomingHolidayTiming.purimStartsLabel(info, profile)
                        } else {
                            UpcomingHolidayTiming.weekdayEveningLabel(info.date)
                        }
                        nextPurim = UpcomingHoliday(
                            name = JerusalemPurimRules.upcomingDisplayName(
                                isJerusalem = isJerusalem,
                                meshulashFridayErev = meshulashErev,
                            ),
                            daysAway = if (onErevFromAlot) 0 else i.coerceAtLeast(1),
                            hint = JerusalemPurimRules.upcomingHint(
                                isJerusalem = isJerusalem,
                                meshulashFridayErev = meshulashErev,
                            ),
                            beginsTonightWhenImminent = onErevFromAlot,
                            timingHint = UpcomingHolidayTiming.timingHintIfThisWeek(
                                civilToday,
                                info.date,
                                rawTimingHint,
                            ),
                        )
                    }
                    isJerusalem && "purim_meshulash_friday" in info.activeSeasons -> {
                        nextPurim = UpcomingHoliday(
                            name = JerusalemPurimRules.upcomingDisplayName(
                                isJerusalem = true,
                                meshulashFriday = true,
                            ),
                            daysAway = i,
                            hint = JerusalemPurimRules.upcomingHint(
                                isJerusalem = true,
                                meshulashFriday = true,
                            ),
                            beginsTonightWhenImminent = false,
                        )
                    }
                    isJerusalem && "purim_meshulash_sunday" in info.activeSeasons -> {
                        nextPurim = UpcomingHoliday(
                            name = JerusalemPurimRules.upcomingDisplayName(
                                isJerusalem = true,
                                meshulashSunday = true,
                            ),
                            daysAway = i,
                            hint = JerusalemPurimRules.upcomingHint(
                                isJerusalem = true,
                                meshulashSunday = true,
                            ),
                            beginsTonightWhenImminent = false,
                        )
                    }
                    isJerusalem && "purim_meshulash_shabbat" in info.activeSeasons -> {
                        nextPurim = UpcomingHoliday(
                            name = JerusalemPurimRules.upcomingDisplayName(
                                isJerusalem = true,
                                meshulashShabbat = true,
                            ),
                            daysAway = daysUntilNextDayOfWeek(info.date, DayOfWeek.SUNDAY),
                            hint = JerusalemPurimRules.upcomingHint(
                                isJerusalem = true,
                                meshulashShabbat = true,
                            ),
                            beginsTonightWhenImminent = false,
                        )
                    }
                }
            }

            if (nextRoshChodesh == null && !info.isRoshChodesh && tomorrow.isRoshChodesh) {
                // Anchor the countdown to when it STARTS (the evening before day 1).
                val eveDate = info.date
                val firstDay = tomorrow.date
                val secondDay = firstDay.plus(1, DateTimeUnit.DAY)
                val isTwoDays = backend.dayInfoAt(secondDay.toEpochMillisAtNoon(profile), profile).isRoshChodesh
                val roshChodeshDays = if (isTwoDays) 2 else 1
                val showFrom = UpcomingHolidayTiming.roshChodeshWhenLabelShouldStartShowing(eveDate)
                val canSayTonight = UpcomingHolidayTiming.canShowTonightForEveningObservance(
                    nowEpochMillis,
                    eveDate,
                    info.zmanim,
                    profile.timezoneId,
                )
                nextRoshChodesh = UpcomingHoliday(
                    name = "Rosh Chodesh",
                    // Count to the START evening (eve date).
                    daysAway = daysBetween(civilToday, eveDate),
                    hint = "Yaaleh V'yavo, Hallel",
                    beginsTonightWhenImminent = canSayTonight,
                    // Always show the weekday-evening label (it's not an exact zman).
                    timingHint = UpcomingHolidayTiming.weekdayEveningLabel(eveDate, days = roshChodeshDays)
                        .takeIf { civilToday >= showFrom },
                )
            }

            eveMinorCommemorativeHolidayOnDay(
                info,
                tomorrow,
                backend,
                profile,
                i,
                civilToday,
                nowEpochMillis,
            )?.let(::addMinorHoliday)

            erevFastHolidayOnDay(info, tomorrow, backend, profile, i, civilToday)?.let(::addMinorHoliday)

            fastHolidayOnDay(info, backend, profile, i, civilToday, nowEpochMillis)?.let(::addMinorHoliday)
        }

        // Birkat Hachamah is extremely rare (once every 28 years) and needs preparation.
        // Show it in "Upcoming & seasonal" during the built-in advance window with exact sunrise time.
        val birkatHachamah = run {
            val occurrence = BirkatHachamahRules.visibleOccurrence(civilToday) ?: return@run null
            if (BirkatHachamahRules.isRecitationDay(civilToday)) return@run null
            val days = BirkatHachamahRules.daysUntilOccurrence(civilToday, occurrence)
            if (days !in 1..BirkatHachamahRules.ADVANCE_DAYS) return@run null

            val occInfo = backend.dayInfoAt(occurrence.toEpochMillisAtNoon(profile), profile)
            val sunrise = occInfo.zmanim?.sunriseMillis
            val tz = occInfo.zmanim?.timezoneId ?: profile.timezoneId
            val timeInline = ZmanimFormatter.formatTimeInline(sunrise, tz)
            val weekday = ZmanimFormatter.formatWeekdayShort(sunrise, tz)
            val cluster = listOfNotNull(timeInline, weekday).joinToString(" ").ifBlank { null }
            val rawTimingHint = if (cluster != null) "Sunrise $cluster" else "Sunrise"
            UpcomingHoliday(
                name = "Birkat Hachamah",
                daysAway = days,
                hint = "Blessing the Sun — once every 28 years",
                beginsTonightWhenImminent = false,
                // Always show the exact time during the 7-day advance window (unlike other holidays).
                timingHint = rawTimingHint,
            )
        }

        return listOfNotNull(
            nextShabbat,
            nextYomTov,
            nextChanukah,
            nextPurim,
            nextRoshChodesh,
        ) + minorHolidays.values + listOfNotNull(birkatHachamah)
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

    private fun eveMinorCommemorativeHolidayOnDay(
        info: DayInfo,
        tomorrow: DayInfo,
        backend: JewishCalendarBackend,
        profile: UserProfile,
        daysAway: Int,
        civilToday: LocalDate,
        nowMillis: Long,
    ): UpcomingHoliday? {
        val tomorrowIdx = yomTovIndexOnDay(tomorrow, profile) ?: return null
        if (!TachanunRules.isEveningStartMinorHoliday(tomorrowIdx)) return null
        val (name, hint) = minorHolidayForIndex(tomorrowIdx) ?: return null
        val eveDate = info.date
        val canSayTonight = UpcomingHolidayTiming.canShowTonightForEveningObservance(
            nowMillis,
            eveDate,
            info.zmanim,
            profile.timezoneId,
        )
        return UpcomingHoliday(
            name = name,
            daysAway = daysBetween(civilToday, eveDate),
            hint = hint,
            beginsTonightWhenImminent = canSayTonight,
            timingHint = UpcomingHolidayTiming.timingHintIfThisWeek(
                civilToday,
                eveDate,
                UpcomingHolidayTiming.weekdayEveningLabel(eveDate),
            ),
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
                    // Tisha B'Av begins at sunset, so count to the start.
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

    private fun daysUntilNextDayOfWeek(from: LocalDate, target: DayOfWeek): Int {
        for (offset in 1..7) {
            if (from.plus(offset, DateTimeUnit.DAY).dayOfWeek == target) return offset
        }
        return 1
    }

    private fun daysBetween(from: LocalDate, to: LocalDate): Int {
        if (from == to) return 0
        var days = 0
        var d = from
        while (d != to && days < 400) {
            d = d.plus(1, DateTimeUnit.DAY)
            days++
        }
        return days
    }

    private fun fastHolidayOnDay(
        info: DayInfo,
        backend: JewishCalendarBackend,
        profile: UserProfile,
        daysAway: Int,
        civilToday: LocalDate,
        nowMillis: Long,
    ): UpcomingHoliday? {
        val idx = yomTovIndexOnDay(info, profile) ?: return null
        val (name, hint) = minorHolidayForIndex(idx) ?: return null
        val startsAtNightfall = TachanunRules.isEveningStartMinorHoliday(idx) ||
            isIsraeliMemorialDay(idx) ||
            (PublicFastDayRules.isPublicFast(idx) && PublicFastDayRules.fastStartsAtSunset(idx))
        val eveDate = if (startsAtNightfall) UpcomingHolidayTiming.eveBefore(info.date) else info.date
        val canSayTonight = startsAtNightfall &&
            UpcomingHolidayTiming.canShowTonightForEveningObservance(
                nowMillis,
                eveDate,
                info.zmanim,
                profile.timezoneId,
            )
        val timingHint = when {
            startsAtNightfall && !canSayTonight && daysAway <= 1 ->
                UpcomingHolidayTiming.timingHintIfThisWeek(
                    civilToday,
                    eveDate,
                    UpcomingHolidayTiming.weekdayEveningLabel(eveDate),
                )
            else -> timingHintForMinorIdx(idx, info, backend, profile, civilToday)
        }
        return UpcomingHoliday(
            name = name,
            // Evening-start observances are anchored to the eve date.
            daysAway = if (startsAtNightfall) (daysAway - 1).coerceAtLeast(0) else daysAway,
            hint = hint,
            beginsTonightWhenImminent = canSayTonight,
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
            TachanunRules.isEveningStartMinorHoliday(idx) -> {
                val eveDate = UpcomingHolidayTiming.eveBefore(info.date)
                eveDate to UpcomingHolidayTiming.weekdayEveningLabel(eveDate)
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
            "Tu B'Av" to "Minor holiday"
        HebrewCalendarEngine.PURIM_KATAN ->
            "Purim Katan" to "Minor holiday — \"little Purim\" in a leap year (Tachanun omitted)"
        HebrewCalendarEngine.SHUSHAN_PURIM_KATAN ->
            "Shushan Purim Katan" to "Walled-city Purim Katan — Tachanun omitted"
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

