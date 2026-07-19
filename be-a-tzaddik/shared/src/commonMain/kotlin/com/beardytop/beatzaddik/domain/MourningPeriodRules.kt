package com.beardytop.beatzaddik.domain

/**
 * Summer mourning period (Three Weeks / Nine Days).
 *
 * Normally through chatzos on 10 Av. When Tisha B'Av is deferred to Sunday 10 Av,
 * mourning customs that track the fast stay active through the end of the fast (tzeit).
 */
object MourningPeriodRules {

    val mourningChecklistItemIds = setOf(
        "three_weeks_mourning_customs",
        "nine_days_mourning_customs",
    )

    /** 17 Tammuz through end of mourning on 10 Av (chatzos, or tzeit when 10 Av is the fast). */
    fun isInThreeWeeksPeriod(cal: DayInfo, nowMillis: Long): Boolean {
        val month = cal.hebrewMonth ?: return false
        val day = cal.hebrewDay ?: return false
        return when {
            month == HebrewCalendarEngine.TAMMUZ && day >= 17 -> true
            month == HebrewCalendarEngine.AV && day <= 9 -> true
            month == HebrewCalendarEngine.AV && day == 10 -> isStillMourningOnTenAv(cal, nowMillis)
            else -> false
        }
    }

    /** 1 Av through end of mourning on 10 Av (chatzos, or tzeit when 10 Av is the fast). */
    fun isInNineDaysPeriod(cal: DayInfo, nowMillis: Long): Boolean {
        val month = cal.hebrewMonth ?: return false
        val day = cal.hebrewDay ?: return false
        return when {
            month == HebrewCalendarEngine.AV && day in 1..9 -> true
            month == HebrewCalendarEngine.AV && day == 10 -> isStillMourningOnTenAv(cal, nowMillis)
            else -> false
        }
    }

    /**
     * Ordinary year: 9 Av was the fast → 10 Av mourning ends at chatzos.
     * Deferred year: Sunday 10 Av is Tisha B'Av → keep through tzeit (end of fast).
     */
    private fun isStillMourningOnTenAv(cal: DayInfo, nowMillis: Long): Boolean {
        // Deferred Tisha B'Av: Sunday 10 Av is the fast day — through tzeit, not chatzos.
        if (cal.fastDayIndex == HebrewCalendarEngine.TISHA_BEAV) {
            val tzeit = cal.zmanim?.tzeitMillis
            return tzeit == null || nowMillis < tzeit
        }
        return beforeChatzosOnTenAv(cal, nowMillis)
    }

    private fun beforeChatzosOnTenAv(cal: DayInfo, nowMillis: Long): Boolean {
        val chatzos = cal.zmanim?.chatzosMillis ?: return true
        return nowMillis < chatzos
    }
}
