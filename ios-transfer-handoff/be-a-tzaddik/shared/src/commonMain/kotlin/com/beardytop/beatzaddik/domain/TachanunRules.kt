package com.beardytop.beatzaddik.domain

/** When Tachanun is recited after weekday Amidah (Shacharit / Mincha). */
object TachanunRules {

    private val tachanunOnlyItemIds = setOf(
        "ashkenaz_musaf_tachanun",
    )

    fun isTachanunOnlyItem(itemId: String): Boolean = itemId in tachanunOnlyItemIds

    /** True on ordinary weekdays when Tachanun follows the Amidah. */
    fun isRecited(cal: DayInfo): Boolean {
        if (cal.isShabbat) return false
        if (cal.isYomTov) return false
        if (cal.isRoshChodesh) return false
        if (cal.isChanukah || cal.isPurim) return false
        if (cal.isLagBaomer) return false
        if (cal.hebrewMonth == HebrewCalendarEngine.NISSAN) return false

        val month = cal.hebrewMonth ?: return true
        val day = cal.hebrewDay ?: return true

        // Elul 29 = Erev Rosh Hashana — Tachanun omitted (MB 581:14)
        if (month == HebrewCalendarEngine.ELUL && day == 29) return false

        // Tishrei: Erev Yom Kippur (9), Erev Sukkot (14) — Tachanun omitted
        if (month == HebrewCalendarEngine.TISHREI && (day == 9 || day == 14)) return false

        // Sivan: Erev Shavuot (5) and extended post-Shavuot period through 12 Sivan (Ashkenaz, MB 494:11)
        if (month == HebrewCalendarEngine.SIVAN && day in 5..12) return false

        return when (month) {
            HebrewCalendarEngine.SHEVAT -> day != 15 // Tu B'Shvat
            HebrewCalendarEngine.IYAR -> day != 14 // Pesach Sheni
            HebrewCalendarEngine.AV -> day != 15 // Tu B'Av
            else -> true
        }
    }
}
