package com.beardytop.beatzaddik.domain

import kotlinx.datetime.DayOfWeek

/**
 * Weekly Torah portion shown in the header and on Shnayim Mikra.
 *
 * Rules:
 * - During Shabbat until tzeit: this Shabbat's reading.
 * - From Motzei Shabbat (tzeit) through the week: the *next* Shabbat's reading.
 * - V'Zot HaBerachah is not a normal Shabbat slot in KosherJava (read on Simchat Torah).
 *   Libraries jump to Bereishit as soon as they skip the NONE Shabbat of Yom Tov.
 *   We keep **V'Zot** until Motzei Simchat Torah at tzeit, then switch to **Bereishit**.
 */
object WeeklyParshaLogic {

    const val BERESHIS = "BERESHIS"
    const val VZOS_HABERACHA = "VZOS_HABERACHA"

    /**
     * KosherJava / engine enum key for the portion to study / display now, or null.
     */
    fun displayParshaKey(
        cal: DayInfo,
        tomorrowCal: DayInfo,
        nowMillis: Long,
        isInIsrael: Boolean,
    ): String? {
        val showNext = shouldShowNextWeeksParsha(cal, tomorrowCal, nowMillis)
        val raw = normalizeKey(
            if (showNext) cal.upcomingShabbatParsha else cal.parsha
        )

        if (isSimchatTorahDay(cal, isInIsrael) && !isPastTodaysTzeit(cal, nowMillis)) {
            return VZOS_HABERACHA
        }

        if (raw == BERESHIS && !simchatTorahCycleEnded(cal, nowMillis, isInIsrael)) {
            return VZOS_HABERACHA
        }

        if (raw == null && isSimchatTorahDay(cal, isInIsrael)) {
            return VZOS_HABERACHA
        }

        return raw
    }

    fun displayParshaLabel(
        cal: DayInfo,
        tomorrowCal: DayInfo,
        nowMillis: Long,
        isInIsrael: Boolean,
    ): String? = ParshaData.displayLabel(
        displayParshaKey(cal, tomorrowCal, nowMillis, isInIsrael)
    )

    /** True once Motzei Shabbat has begun — study next week's portion. */
    fun shouldShowNextWeeksParsha(
        cal: DayInfo,
        tomorrowCal: DayInfo,
        nowMillis: Long,
    ): Boolean {
        if (MotzeiShabbatWindow.isActive(cal, tomorrowCal, nowMillis)) return true

        // Civil Saturday after tzeit: DayInfo may already be rolled to Sunday's Hebrew day
        // (isShabbat=false, startedTonightAtTzeit=true) — still Motzei.
        if (cal.startedTonightAtTzeit && cal.date.dayOfWeek == DayOfWeek.SATURDAY) {
            return true
        }

        if (cal.isShabbat && !cal.isErevShabbat) {
            val tzeit = cal.zmanim?.tzeitMillis
            return tzeit != null && nowMillis >= tzeit
        }

        // Weekdays (and Motzei Sunday after Melave Malka window): next Shabbat's portion.
        return !cal.isShabbat
    }

    fun normalizeKey(raw: String?): String? {
        if (raw.isNullOrBlank() || raw.equals("NONE", ignoreCase = true)) return null
        return raw.trim().replace(' ', '_').uppercase()
    }

    fun isSimchatTorahDay(cal: DayInfo, isInIsrael: Boolean): Boolean {
        val idx = TachanunRules.yomTovIndexFor(cal, isInIsrael) ?: return false
        return if (isInIsrael) {
            idx == HebrewCalendarEngine.SHEMINI_ATZERES
        } else {
            idx == HebrewCalendarEngine.SIMCHAS_TORAH
        }
    }

    /**
     * False from Ha'azinu week through Simchat Torah daytime — Bereishit must wait until
     * Motzei Simchat Torah (tzeit on 22 Tishrei in Israel / 23 in the Diaspora).
     */
    fun simchatTorahCycleEnded(
        cal: DayInfo,
        nowMillis: Long,
        isInIsrael: Boolean,
    ): Boolean {
        val month = cal.hebrewMonth ?: return true
        val day = cal.hebrewDay ?: return true
        val stDay = if (isInIsrael) 22 else 23

        return when {
            month != HebrewCalendarEngine.TISHREI -> true
            day < stDay -> false
            day > stDay -> true
            else -> isPastTodaysTzeit(cal, nowMillis)
        }
    }

    private fun isPastTodaysTzeit(cal: DayInfo, nowMillis: Long): Boolean {
        val tzeit = cal.zmanim?.tzeitMillis ?: return false
        return nowMillis >= tzeit
    }
}
