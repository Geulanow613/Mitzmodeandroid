package com.beardytop.beatzaddik.domain

/**
 * Whether *this evening's* nightfall begins Shabbat or Yom Tov melacha.
 *
 * Distinct from [DayInfo.isErevShabbat] / `erev_chag`, which describe the Hebrew calendar
 * day (prep day) and can leak after tzeit rollover onto that day — see [HalachicDayRollover].
 */
object TonightHolyDayRules {

    /** Friday before tzeit: tonight begins Shabbat. */
    fun tonightBeginsShabbat(cal: DayInfo): Boolean =
        cal.isErevShabbat && !cal.startedTonightAtTzeit

    /** PrayerDayContext helper — same meaning as [tonightBeginsShabbat]. */
    fun tonightBeginsShabbat(prayerDay: PrayerDayContext): Boolean =
        prayerDay.tonightBeginsShabbat

    /**
     * True when Maariv / KL / Motzei-Selichot should treat tonight as a holy-day entry
     * (use [MaarivInAppRules.blockedTonightLabel] — single source of truth).
     */
    fun tonightBeginsHolyDayMelacha(prayerDay: PrayerDayContext): Boolean =
        prayerDay.maarivBlockedTonightLabel != null

    fun holyDayLabelForDeferral(prayerDay: PrayerDayContext): String {
        val maariv = prayerDay.maarivBlockedTonightLabel
        return when {
            prayerDay.tonightBeginsShabbat && maariv != null && maariv != "Shabbat" ->
                "Shabbat / $maariv"
            prayerDay.tonightBeginsShabbat -> "Shabbat"
            else -> maariv ?: "Yom Tov"
        }
    }
}
