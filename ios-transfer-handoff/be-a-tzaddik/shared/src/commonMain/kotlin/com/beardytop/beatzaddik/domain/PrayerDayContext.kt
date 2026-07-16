package com.beardytop.beatzaddik.domain

/** Calendar context for Musaf, Hallel, and similar day-specific prayer rows. */
data class PrayerDayContext(
    val isShabbat: Boolean,
    val isYomTov: Boolean,
    val isRoshChodesh: Boolean,
    val isErevShabbat: Boolean,
    /** Current day of the Hebrew month (1–30). Null when no calendar library is available. */
    val hebrewDay: Int? = null,
    /** Current Hebrew month number (1 = Nissan … 7 = Tishrei, etc., per KosherJava convention). */
    val hebrewMonth: Int? = null,
    /** Current Hebrew year (e.g. 5786). Needed for molad-based monthly windows. */
    val hebrewYear: Int? = null,
    /** User latitude when known — used for hemisphere-specific seasonal mitzvot. */
    val latitude: Double? = null,
    val nusach: EffectiveNusach = EffectiveNusach.ASHKENAZ,
    val fastDayIndex: Int? = null,
    val isCholHamoed: Boolean = false,
    val isInIsrael: Boolean = false,
    /** When set, Maariv is not tracked tonight (Erev Shabbat / Erev Yom Tov). */
    val maarivBlockedTonightLabel: String? = null,
    /** Minutes before sunset for Shabbat / weekday Erev Yom Tov candle lighting. */
    val candleLeadMinutes: Int = CandleLightingRules.LEAD_MINUTES_DEFAULT,
) {
    val isShabbatOrYomTov: Boolean = isShabbat || isYomTov

    /** Musaf is said on Shabbat, Yom Tov, and Rosh Chodesh (not plain Erev Shabbat). */
    val isMusafDay: Boolean = isShabbat || isYomTov || isRoshChodesh

    fun musafDayLabel(): String = when {
        isShabbat && isRoshChodesh -> "Shabbat & Rosh Chodesh"
        isShabbat -> "Shabbat"
        isYomTov && isRoshChodesh -> "Festival & Rosh Chodesh"
        isYomTov -> "Festival"
        isRoshChodesh -> "Rosh Chodesh"
        else -> "today"
    }

    companion object {
        fun from(
            cal: DayInfo,
            nusach: EffectiveNusach = EffectiveNusach.ASHKENAZ,
            latitude: Double? = null,
            inIsrael: Boolean = false,
            tomorrowCal: DayInfo? = null,
            candleLeadMinutes: Int = CandleLightingRules.LEAD_MINUTES_DEFAULT,
        ) = PrayerDayContext(
            isShabbat = cal.isShabbat,
            isYomTov = cal.isYomTov,
            isRoshChodesh = cal.isRoshChodesh,
            isErevShabbat = cal.isErevShabbat,
            hebrewDay = cal.hebrewDay,
            hebrewMonth = cal.hebrewMonth,
            hebrewYear = cal.hebrewYear,
            latitude = latitude,
            nusach = nusach,
            fastDayIndex = cal.fastDayIndex,
            isCholHamoed = "chol_hamoed_pesach" in cal.activeSeasons ||
                "chol_hamoed_sukkot" in cal.activeSeasons,
            isInIsrael = inIsrael,
            maarivBlockedTonightLabel = MaarivInAppRules.blockedTonightLabel(cal, tomorrowCal, inIsrael),
            candleLeadMinutes = candleLeadMinutes,
        )
    }
}
