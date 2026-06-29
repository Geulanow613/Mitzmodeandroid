package com.beardytop.beatzaddik.domain

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant

/**
 * Compact inline zman labels for Upcoming & seasonal.
 * Shabbat / weekday Yom Tov: "Candles 7:35pm Fri" (18 or 40 min before sunset; tzeit when Motzei Shabbat / Shavuot).
 * Everything else: relevant zman at "7:36pm Wed" (sunset, tzeit, or dawn for minor fasts only).
 */
internal object UpcomingHolidayTiming {

    fun shabbatStartsLabel(erevFridayInfo: DayInfo, profile: UserProfile): String? {
        val sunset = erevFridayInfo.zmanim?.sunsetMillis ?: return null
        val candles = CandleLightingRules.candleLightingMillis(sunset, profile)
        return candlesInlineLabel(candles, erevFridayInfo, profile)
    }

    fun yomTovStartsLabel(
        erevInfo: DayInfo,
        tomorrowInfo: DayInfo,
        chagIdx: Int?,
        profile: UserProfile,
    ): String? {
        val z = erevInfo.zmanim ?: return null
        val epochMillis = when {
            chagIdx == HebrewCalendarEngine.SHAVUOS -> z.tzeitMillis
            erevInfo.isShabbat && tomorrowInfo.isYomTovAssurBemelacha -> z.tzeitMillis
            else -> z.sunsetMillis?.let { CandleLightingRules.candleLightingMillis(it, profile) }
        } ?: return null
        return candlesInlineLabel(epochMillis, erevInfo, profile)
    }

    fun yomKippurErevStartsLabel(erevInfo: DayInfo, profile: UserProfile): String? =
        zmanInlineLabel(erevInfo.zmanim?.sunsetMillis, erevInfo, profile)

    fun chanukahStartsLabel(erevChanukahInfo: DayInfo, profile: UserProfile): String? =
        zmanInlineLabel(erevChanukahInfo.zmanim?.tzeitMillis, erevChanukahInfo, profile)

    fun purimStartsLabel(erevPurimInfo: DayInfo, profile: UserProfile): String? =
        zmanInlineLabel(erevPurimInfo.zmanim?.tzeitMillis, erevPurimInfo, profile)

    fun roshChodeshStartsLabel(erevInfo: DayInfo, profile: UserProfile): String? =
        zmanInlineLabel(erevInfo.zmanim?.tzeitMillis, erevInfo, profile)

    fun israeliMemorialStartsLabel(eveBeforeDayInfo: DayInfo, profile: UserProfile): String? =
        zmanInlineLabel(eveBeforeDayInfo.zmanim?.tzeitMillis, eveBeforeDayInfo, profile)

    fun minorFastStartsLabel(fastDayInfo: DayInfo, profile: UserProfile): String? =
        zmanInlineLabel(fastDayInfo.zmanim?.alotHaShacharMillis, fastDayInfo, profile)

    fun sunsetFastStartsLabel(erevInfo: DayInfo, profile: UserProfile): String? =
        zmanInlineLabel(erevInfo.zmanim?.sunsetMillis, erevInfo, profile)

    /**
     * Show compact zman subtitles only when [timingAnchorDate] is in the current civil week
     * (Sunday 12:00 am through Saturday) relative to [civilToday] — avoids "Tue 7:30" on "in 18 days".
     */
    fun timingHintIfThisWeek(
        civilToday: LocalDate,
        timingAnchorDate: LocalDate,
        hint: String?,
    ): String? {
        if (hint.isNullOrBlank()) return null
        if (!CivilWeek.contains(civilToday, timingAnchorDate)) return null
        return hint
    }

    fun eveBefore(observanceDate: LocalDate): LocalDate =
        observanceDate.plus(-1, DateTimeUnit.DAY)

    fun dayInfoOn(
        backend: JewishCalendarBackend,
        date: LocalDate,
        profile: UserProfile,
    ): DayInfo = backend.dayInfoAt(date.toEpochMillisAtNoon(profile), profile)

    /** e.g. "Candles 7:35pm Fri". */
    private fun candlesInlineLabel(
        epochMillis: Long?,
        anchorDayInfo: DayInfo,
        profile: UserProfile,
    ): String? = zmanInlineLabel(epochMillis, anchorDayInfo, profile)?.let { "Candles $it" }

    /** e.g. "7:36pm Wed" or "4:10am Thu". */
    private fun zmanInlineLabel(
        epochMillis: Long?,
        anchorDayInfo: DayInfo,
        profile: UserProfile,
    ): String? {
        val tz = anchorDayInfo.zmanim?.timezoneId ?: profile.timezoneId
        val time = ZmanimFormatter.formatTimeInline(epochMillis, tz) ?: return null
        val weekday = ZmanimFormatter.formatWeekdayShort(epochMillis, tz)
        return if (weekday != null) "$time $weekday" else time
    }
}

private fun LocalDate.toEpochMillisAtNoon(profile: UserProfile): Long {
    val tz = TimeZone.of(profile.timezoneId)
    return LocalDateTime(year, monthNumber, dayOfMonth, 12, 0)
        .toInstant(tz)
        .toEpochMilliseconds()
}
