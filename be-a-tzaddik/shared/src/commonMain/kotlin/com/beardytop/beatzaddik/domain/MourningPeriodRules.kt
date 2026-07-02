package com.beardytop.beatzaddik.domain

/**
 * Summer mourning period (Three Weeks / Nine Days) through chatzos on 10 Av.
 */
object MourningPeriodRules {

    val mourningChecklistItemIds = setOf(
        "three_weeks_mourning_customs",
        "nine_days_mourning_customs",
    )

    /** 17 Tammuz through chatzos on 10 Av. */
    fun isInThreeWeeksPeriod(cal: DayInfo, nowMillis: Long): Boolean {
        val month = cal.hebrewMonth ?: return false
        val day = cal.hebrewDay ?: return false
        return when {
            month == HebrewCalendarEngine.TAMMUZ && day >= 17 -> true
            month == HebrewCalendarEngine.AV && day <= 9 -> true
            month == HebrewCalendarEngine.AV && day == 10 -> beforeChatzosOnTenAv(cal, nowMillis)
            else -> false
        }
    }

    /** 1 Av through chatzos on 10 Av. */
    fun isInNineDaysPeriod(cal: DayInfo, nowMillis: Long): Boolean {
        val month = cal.hebrewMonth ?: return false
        val day = cal.hebrewDay ?: return false
        return when {
            month == HebrewCalendarEngine.AV && day in 1..9 -> true
            month == HebrewCalendarEngine.AV && day == 10 -> beforeChatzosOnTenAv(cal, nowMillis)
            else -> false
        }
    }

    private fun beforeChatzosOnTenAv(cal: DayInfo, nowMillis: Long): Boolean {
        val chatzos = cal.zmanim?.chatzosMillis ?: return true
        return nowMillis < chatzos
    }
}
