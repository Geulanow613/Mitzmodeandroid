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
        val day = cal.hebrewDay ?: return true
        return when (cal.hebrewMonth) {
            HebrewCalendarEngine.SHEVAT -> day != 15 // Tu B'Shvat
            HebrewCalendarEngine.IYAR -> day != 14 // Pesach Sheni
            HebrewCalendarEngine.AV -> day != 15 // Tu B'Av
            else -> true
        }
    }
}
