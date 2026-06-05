package com.beardytop.beatzaddik.domain

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

/**
 * Decides when the app is paused for Shabbat or Yom Tov (melacha days).
 *
 * - Shabbat begins **1 minute before sunset** on Friday (not 18-minute candle lighting).
 * - Shabbat ends at **tzeit** Saturday.
 * - Yom Tov is continuous across multi-day festivals and through Shabbat↔Yom Tov transitions
 *   (no gap at Motzei Shabbat into Yom Tov, or between consecutive Yom Tov days).
 */
internal object ElectronicsRestEvaluator {

    private const val SHABBAT_SUNSET_LEAD_MS = 60_000L

    fun evaluate(
        calendar: JewishCalendarService,
        nowMillis: Long,
        profile: UserProfile,
    ): ElectronicsRestPeriod? {
        yomTovRest(calendar, nowMillis, profile)?.let { return it }
        shabbatRest(calendar, nowMillis, profile)?.let { return it }
        return null
    }

    private fun shabbatRest(
        calendar: JewishCalendarService,
        nowMillis: Long,
        profile: UserProfile,
    ): ElectronicsRestPeriod? {
        val today = calendar.dayInfoAt(nowMillis, profile)
        val friday = fridayOnOrBefore(today.date)
        val saturday = friday.plus(1, DateTimeUnit.DAY)

        val fridayInfo = calendar.dayInfoForDate(friday, profile)
        val saturdayInfo = calendar.dayInfoForDate(saturday, profile)

        val start = shabbatEntryMillis(fridayInfo) ?: return null
        val end = dayTzeitMillis(saturday, calendar, profile, saturdayInfo) ?: return null

        if (nowMillis < start || nowMillis >= end) return null

        val isErev = today.date == friday
        return ElectronicsRestPeriod(
            kind = RestKind.SHABBAT,
            title = if (isErev) "Shabbat is beginning" else "Shabbat Shalom",
            message = shabbatMessage(),
            hebrewDateLabel = fridayInfo.hebrewLabel,
            locationLabel = profile.locationLabel,
            endsAtEpochMillis = end,
        )
    }

    private fun yomTovRest(
        calendar: JewishCalendarService,
        nowMillis: Long,
        profile: UserProfile,
    ): ElectronicsRestPeriod? {
        val today = calendar.dayInfoAt(nowMillis, profile)
        val anchor = yomTovMelachaAnchorDay(today, nowMillis, calendar, profile) ?: return null

        val firstDay = yomTovSequenceFirstDay(anchor, calendar, profile)
        val lastDay = yomTovSequenceLastDay(anchor, calendar, profile)

        val start = yomTovSequenceStartMillis(firstDay, calendar, profile) ?: return null
        val end = dayTzeitMillis(lastDay, calendar, profile) ?: return null

        if (nowMillis < start || nowMillis >= end) return null

        val anchorInfo = calendar.dayInfoForDate(anchor, profile)
        val holidayName = anchorInfo.yomTovHolidayName ?: "Yom Tov"

        return ElectronicsRestPeriod(
            kind = RestKind.YOM_TOV,
            title = holidayName,
            message = yomTovMessage(holidayName),
            hebrewDateLabel = anchorInfo.hebrewLabel,
            locationLabel = profile.locationLabel,
            endsAtEpochMillis = end,
        )
    }

    private fun yomTovMelachaAnchorDay(
        today: DayInfo,
        nowMillis: Long,
        calendar: JewishCalendarService,
        profile: UserProfile,
    ): LocalDate? {
        if (today.isYomTovAssurBemelacha) return today.date

        val tomorrow = calendar.dayInfoForDate(today.date.plus(1, DateTimeUnit.DAY), profile)

        // Erev chag: Yom Tov begins at tonight's sunset.
        if (today.upcomingChagName != null && tomorrow.isYomTovAssurBemelacha) {
            val sunset = today.zmanim?.sunsetMillis ?: return null
            if (nowMillis >= sunset) return tomorrow.date
        }

        // Motzei Shabbat → Yom Tov (e.g. Shemini Atzeret / Simchat Torah Saturday night).
        if (today.isShabbat && !today.isErevShabbat && tomorrow.isYomTovAssurBemelacha) {
            val tzeit = today.zmanim?.tzeitMillis ?: return null
            if (nowMillis >= tzeit) return tomorrow.date
        }

        return null
    }

    private fun yomTovSequenceFirstDay(
        anchor: LocalDate,
        calendar: JewishCalendarService,
        profile: UserProfile,
    ): LocalDate {
        var first = anchor
        while (true) {
            val info = calendar.dayInfoForDate(first, profile)
            if (!info.yesterdayWasYomTovAssurBemelacha) break
            first = first.plus(-1, DateTimeUnit.DAY)
        }
        return first
    }

    private fun yomTovSequenceLastDay(
        anchor: LocalDate,
        calendar: JewishCalendarService,
        profile: UserProfile,
    ): LocalDate {
        var last = anchor
        while (true) {
            val next = last.plus(1, DateTimeUnit.DAY)
            val nextInfo = calendar.dayInfoForDate(next, profile)
            if (!nextInfo.isYomTovAssurBemelacha) break
            last = next
        }
        return last
    }

    private fun yomTovSequenceStartMillis(
        firstYomTovDay: LocalDate,
        calendar: JewishCalendarService,
        profile: UserProfile,
    ): Long? {
        val firstInfo = calendar.dayInfoForDate(firstYomTovDay, profile)
        val prev = firstYomTovDay.plus(-1, DateTimeUnit.DAY)
        val prevInfo = calendar.dayInfoForDate(prev, profile)

        if (prevInfo.isShabbat && !prevInfo.isErevShabbat) {
            return dayTzeitMillis(prev, calendar, profile)
        }

        if (firstInfo.yesterdayWasYomTovAssurBemelacha) {
            dayTzeitMillis(prev, calendar, profile)?.let { return it }
        }

        return daySunsetMillis(prev, calendar, profile)
    }

    private fun shabbatEntryMillis(fridayInfo: DayInfo): Long? {
        val sunset = fridayInfo.zmanim?.sunsetMillis ?: return null
        return sunset - SHABBAT_SUNSET_LEAD_MS
    }

    private fun daySunsetMillis(
        date: LocalDate,
        calendar: JewishCalendarService,
        profile: UserProfile,
    ): Long? = calendar.dayInfoForDate(date, profile).zmanim?.sunsetMillis

    private fun dayTzeitMillis(
        date: LocalDate,
        calendar: JewishCalendarService,
        profile: UserProfile,
        cached: DayInfo? = null,
    ): Long? {
        val zmanim = (cached ?: calendar.dayInfoForDate(date, profile)).zmanim
        return zmanim?.tzeitMillis ?: zmanim?.sunsetMillis
    }

    private fun fridayOnOrBefore(date: LocalDate): LocalDate {
        var d = date
        while (d.dayOfWeek != DayOfWeek.FRIDAY) {
            d = d.plus(-1, DateTimeUnit.DAY)
        }
        return d
    }
}
