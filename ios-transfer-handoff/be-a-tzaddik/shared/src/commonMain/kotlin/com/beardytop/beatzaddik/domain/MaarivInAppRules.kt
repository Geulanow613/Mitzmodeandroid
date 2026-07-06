package com.beardytop.beatzaddik.domain

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Maariv is not tracked in-app when tonight begins Shabbat or Yom Tov melacha —
 * the checklist is for weekdays and erev preparation, not holy-day evenings.
 *
 * Blocking starts at dawn (alot hashachar) on the erev day, not at civil midnight.
 */
object MaarivInAppRules {

    /**
     * Calendar erev label for tonight's Maariv, if this civil day is erev Shabbat or erev Yom Tov.
     * Apply only after [isErevDayDawnReached] — before dawn on erev, Thursday night's Maariv stays normal.
     */
    fun blockedTonightLabel(
        cal: DayInfo,
        tomorrowCal: DayInfo?,
        inIsrael: Boolean,
    ): String? {
        if (cal.isErevShabbat) return "Shabbat"
        if ("erev_chag" in cal.activeSeasons) return "Yom Tov"
        val tomorrow = tomorrowCal ?: return null
        if (!cal.isYomTovAssurBemelacha && tomorrow.isYomTovAssurBemelacha) {
            return "Yom Tov"
        }
        if (HebrewCalendarEngine.isFinalYomTovDayOfPesach(
                tomorrow.hebrewMonth,
                tomorrow.hebrewDay,
                inIsrael,
            )
        ) {
            return "Yom Tov"
        }
        if (tomorrow.hebrewMonth == HebrewCalendarEngine.TISHREI &&
            tomorrow.hebrewDay == 22 &&
            ("shemini_atzeret" in tomorrow.activeSeasons || "simchat_torah" in tomorrow.activeSeasons)
        ) {
            return "Yom Tov"
        }
        if (!inIsrael &&
            tomorrow.hebrewMonth == HebrewCalendarEngine.TISHREI &&
            tomorrow.hebrewDay == 23 &&
            "simchat_torah" in tomorrow.activeSeasons
        ) {
            return "Yom Tov"
        }
        return null
    }

    /** True once today's dawn (alot hashachar) has passed on the erev calendar day. */
    fun isErevDayDawnReached(
        nowMillis: Long,
        zmanim: ZmanimSnapshot?,
        timezoneId: String,
    ): Boolean {
        val dawn = zmanim?.alotHaShacharMillis ?: zmanim?.sunriseMillis
        if (dawn != null) return nowMillis >= dawn
        val hour = runCatching {
            Instant.fromEpochMilliseconds(nowMillis)
                .toLocalDateTime(TimeZone.of(timezoneId))
                .hour
        }.getOrElse { 12 }
        return hour >= 5
    }

    fun blockedMaarivStatusIfApplicable(
        prayerDay: PrayerDayContext,
        nowMillis: Long,
        zmanim: ZmanimSnapshot?,
        timezoneId: String,
    ): ItemZmanStatus? {
        val label = prayerDay.maarivBlockedTonightLabel ?: return null
        if (!isErevDayDawnReached(nowMillis, zmanim, timezoneId)) return null
        return blockedStatus(label)
    }

    fun blockedStatus(holyDayLabel: String): ItemZmanStatus = ItemZmanStatus(
        availability = ItemZmanAvailability.UPCOMING,
        hint = "Not available in-app tonight — $holyDayLabel. Put away your phone when $holyDayLabel begins.",
        hintTemplate = "Not available in-app tonight — {holyDay}. Put away your phone when {holyDay} begins.",
        hintArgs = mapOf("holyDay" to holyDayLabel),
        collapsedSummaryTemplate = "Not available in-app tonight — {holyDay}",
        collapsedSummaryArgs = mapOf("holyDay" to holyDayLabel),
        windowStartMillis = null,
        availableAtLabel = null,
    )
}
