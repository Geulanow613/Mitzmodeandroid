package com.beardytop.beatzaddik.domain

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant

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

        var nextShabbat: UpcomingHoliday? = null
        var nextYomTov: UpcomingHoliday? = null
        var nextChanukah: UpcomingHoliday? = null
        var nextPurim: UpcomingHoliday? = null
        var nextRoshChodesh: UpcomingHoliday? = null
        var nextMinorHoliday: UpcomingHoliday? = null

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
                )
            }

            if (nextYomTov == null) {
                info.upcomingChagName?.let { name ->
                    nextYomTov = UpcomingHoliday(
                        name = name,
                        daysAway = i,
                        hint = yomTovPrepHint(name),
                        beginsTonightWhenImminent = true,
                    )
                }
            }

            if (nextChanukah == null && "erev_chanukah" in info.activeSeasons) {
                nextChanukah = UpcomingHoliday(
                    name = "Chanukah",
                    daysAway = i,
                    hint = "Light menorah each night",
                    beginsTonightWhenImminent = true,
                )
            }

            if (nextPurim == null && "erev_purim" in info.activeSeasons) {
                nextPurim = UpcomingHoliday(
                    name = "Purim",
                    daysAway = i + 1,
                    hint = "Megillah, matanot, mishloach manot",
                    beginsTonightWhenImminent = false,
                )
            }

            if (nextRoshChodesh == null && !info.isRoshChodesh && tomorrow.isRoshChodesh && i == 0) {
                // Erev Rosh Chodesh (from alot): observance begins tonight.
                nextRoshChodesh = UpcomingHoliday(
                    name = "Rosh Chodesh",
                    daysAway = 0,
                    hint = "Yaaleh V'yavo, Hallel",
                    beginsTonightWhenImminent = true,
                )
            } else if (nextRoshChodesh == null && info.isRoshChodesh && i > 0) {
                // Count down to the calendar day of Rosh Chodesh (not the erev).
                nextRoshChodesh = UpcomingHoliday(
                    name = "Rosh Chodesh",
                    daysAway = i,
                    hint = "Yaaleh V'yavo, Hallel",
                    beginsTonightWhenImminent = false,
                )
            }

            if (nextMinorHoliday == null) {
                minorHolidayOnDay(info, profile)?.let { (name, hint) ->
                    nextMinorHoliday = UpcomingHoliday(
                        name = name,
                        daysAway = i,
                        hint = hint,
                        beginsTonightWhenImminent = false,
                    )
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

    private fun minorHolidayOnDay(info: DayInfo, profile: UserProfile): Pair<String, String>? {
        val year = info.hebrewYear ?: return null
        val month = info.hebrewMonth ?: return null
        val day = info.hebrewDay ?: return null
        val idx = HebrewCalendarEngine.getYomTovIndex(
            year = year,
            month = month,
            day = day,
            dayOfWeek = info.date.toJavaDayOfWeek(),
            isLeapYear = HebrewCalendarEngine.isJewishLeapYear(year),
            inIsrael = profile.isInIsrael,
            useModernHolidays = true,
        )
        return minorHolidayForIndex(idx)
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

    private fun yomTovPrepHint(name: String): String = when (name) {
        "Rosh Hashana" -> "Yom Tov — Shofar, teshuvah, sweet foods"
        "Yom Kippur" -> "Yom Tov — Fast, prayer, atonement"
        "Sukkot" -> "Yom Tov — Lulav & etrog, sukkah"
        "Shemini Atzeret" -> "Yom Tov — Shemini Atzeret"
        "Simchat Torah" -> "Yom Tov — Simchat Torah, hakafot"
        "Pesach" -> "Yom Tov — Matzah, no chametz, seder"
        "Shavuot" -> "Yom Tov — Torah, dairy foods"
        else -> "Yom Tov — candles, festive meals"
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

