package com.beardytop.beatzaddik.domain

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

/**
 * Compact inline zman labels for Upcoming & seasonal.
 * Shabbat / weekday Yom Tov: "Candles 7:35pm Fri" (18 or 40 min before sunset).
 * Shabbat→Yom Tov: "Nightfall …" at tzeit (Yaknehaz). Everything else: zman at "7:36pm Wed".
 */
internal object UpcomingHolidayTiming {

    /**
     * Compact, no-exact-time label for upcoming-night starts (used for Yom Tov + Rosh Chodesh).
     * Examples:
     * - "Tue evening"
     * - "Tue + Wed evening (2 days)"
     */
    fun weekdayEveningLabel(eveDate: LocalDate, days: Int = 1): String {
        val first = weekdayShortKey(eveDate.dayOfWeek)
        return when (days) {
            2 -> {
                "$first evening (2 days)"
            }
            else -> "$first evening"
        }
    }

    /**
     * To avoid showing "Tue evening" too early (e.g. two weeks out), we only begin showing the
     * weekday-evening label starting from the Wednesday of the previous civil week.
     *
     * Example: if the eve is a Tuesday, this returns the prior Wednesday (6 days earlier).
     */
    fun roshChodeshWhenLabelShouldStartShowing(eveDate: LocalDate): LocalDate {
        // Show starting 6 days before the evening in question (i.e., the previous civil week window).
        return eveDate.plus(-6, DateTimeUnit.DAY)
    }

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
        // Shabbat→YT: festival begins at tzeit (Yaknehaz) — label as nightfall, not candles.
        if (erevInfo.isShabbat && tomorrowInfo.isYomTovAssurBemelacha) {
            return zmanInlineLabel(z.tzeitMillis, erevInfo, profile)?.let { "Nightfall $it" }
        }
        val epochMillis = z.sunsetMillis?.let {
            CandleLightingRules.candleLightingMillis(it, profile)
        } ?: return null
        return candlesInlineLabel(epochMillis, erevInfo, profile)
    }

    fun yomKippurErevStartsLabel(erevInfo: DayInfo, profile: UserProfile): String? =
        zmanInlineLabel(erevInfo.zmanim?.sunsetMillis, erevInfo, profile)

    fun chanukahStartsLabel(erevChanukahInfo: DayInfo, profile: UserProfile): String? {
        val z = erevChanukahInfo.zmanim ?: return null
        // Friday erev: first lighting is from plag (before Shabbat candles), not tzeit.
        if (erevChanukahInfo.isErevShabbat) {
            return zmanInlineLabel(z.plagHaminchaMillis, erevChanukahInfo, profile)
                ?.let { "From plag $it" }
        }
        return zmanInlineLabel(z.tzeitMillis, erevChanukahInfo, profile)
    }

    fun purimStartsLabel(erevPurimInfo: DayInfo, profile: UserProfile): String? =
        zmanInlineLabel(erevPurimInfo.zmanim?.tzeitMillis, erevPurimInfo, profile)

    fun roshChodeshStartsLabel(erevInfo: DayInfo, profile: UserProfile): String? =
        weekdayEveningLabel(erevInfo.date, days = 1)

    fun israeliMemorialStartsLabel(eveBeforeDayInfo: DayInfo, profile: UserProfile): String? =
        zmanInlineLabel(eveBeforeDayInfo.zmanim?.tzeitMillis, eveBeforeDayInfo, profile)

    fun minorFastStartsLabel(fastDayInfo: DayInfo, profile: UserProfile): String? =
        zmanInlineLabel(fastDayInfo.zmanim?.alotHaShacharMillis, fastDayInfo, profile)

    fun sunsetFastStartsLabel(erevInfo: DayInfo, profile: UserProfile): String? =
        zmanInlineLabel(erevInfo.zmanim?.sunsetMillis, erevInfo, profile)?.let { "Sunset $it" }

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

    /**
     * Evening observances (Purim Megillah, Israeli civil days, etc.) may show "Tonight" only
     * from civil midnight on the erev day — not on the previous calendar night.
     */
    fun canShowTonightForEveningObservance(
        nowMillis: Long,
        observanceEveDate: LocalDate,
        zmanim: ZmanimSnapshot?,
        timezoneId: String,
    ): Boolean {
        val civilToday = Instant.fromEpochMilliseconds(nowMillis)
            .toLocalDateTime(TimeZone.of(timezoneId))
            .date
        return civilToday == observanceEveDate
    }

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

private fun weekdayShortKey(dow: kotlinx.datetime.DayOfWeek): String = when (dow) {
    kotlinx.datetime.DayOfWeek.MONDAY -> "Mon"
    kotlinx.datetime.DayOfWeek.TUESDAY -> "Tue"
    kotlinx.datetime.DayOfWeek.WEDNESDAY -> "Wed"
    kotlinx.datetime.DayOfWeek.THURSDAY -> "Thu"
    kotlinx.datetime.DayOfWeek.FRIDAY -> "Fri"
    kotlinx.datetime.DayOfWeek.SATURDAY -> "Sat"
    kotlinx.datetime.DayOfWeek.SUNDAY -> "Sun"
}

private fun LocalDate.toEpochMillisAtNoon(profile: UserProfile): Long {
    val tz = TimeZone.of(profile.timezoneId)
    return LocalDateTime(year, monthNumber, dayOfMonth, 12, 0)
        .toInstant(tz)
        .toEpochMilliseconds()
}
