package com.beardytop.beatzaddik.domain

import kotlinx.datetime.DayOfWeek

/**
 * Summer mourning period (Three Weeks / Nine Days).
 *
 * Normally through chatzos on 10 Av. When Tisha B'Av is deferred to Sunday 10 Av,
 * mourning customs that track the fast stay active through the end of the fast (tzeit),
 * then Ashkenaz meat/wine continue through Monday morning.
 *
 * Sephardi / Edot HaMizrach: the stricter "Nine Days" checklist row follows
 * shavuah she'chal bo (week of Tisha B'Av) through the same 10 Av end — not from 1 Av.
 */
object MourningPeriodRules {

    val mourningChecklistItemIds = setOf(
        "three_weeks_mourning_customs",
        "nine_days_mourning_customs",
        "sefirah_mourning_music",
    )

    /** 17 Tammuz through end of mourning on 10 Av (chatzos, or tzeit when 10 Av is the fast). */
    fun isInThreeWeeksPeriod(cal: DayInfo, nowMillis: Long): Boolean {
        val month = cal.hebrewMonth ?: return false
        val day = cal.hebrewDay ?: return false
        return when {
            month == HebrewCalendarEngine.TAMMUZ && day >= 17 -> true
            month == HebrewCalendarEngine.AV && day <= 9 -> true
            month == HebrewCalendarEngine.AV && day == 10 -> isStillMourningOnTenAv(cal, nowMillis)
            // Day 11 meat/wine tail after deferred TBA is Nine Days only — not general Three Weeks.
            else -> false
        }
    }

    /**
     * Stricter summer-mourning row (Nine Days / shavuah she'chal bo).
     * Ashkenaz, Chabad, Other: from 1 Av through end of mourning on 10 Av.
     * Sephardi / Edot HaMizrach: from Motzei Shabbat of the week of Tisha B'Av only.
     */
    fun isInNineDaysPeriod(
        cal: DayInfo,
        nowMillis: Long,
        nusach: EffectiveNusach = EffectiveNusach.ASHKENAZ,
    ): Boolean {
        if (!isInAshkenazNineDaysWindow(cal, nowMillis)) return false
        return when (nusach) {
            EffectiveNusach.SEFARD, EffectiveNusach.EDOT_HAMIZRACH ->
                isInShavuahSheChalBo(cal, nowMillis)
            EffectiveNusach.ASHKENAZ, EffectiveNusach.CHABAD, EffectiveNusach.OTHER -> true
        }
    }

    /** 1 Av through end of mourning on 10 Av (chatzos, or tzeit when 10 Av is the fast). */
    private fun isInAshkenazNineDaysWindow(cal: DayInfo, nowMillis: Long): Boolean {
        val month = cal.hebrewMonth ?: return false
        val day = cal.hebrewDay ?: return false
        return when {
            month == HebrewCalendarEngine.AV && day in 1..9 -> true
            month == HebrewCalendarEngine.AV && day == 10 -> isStillMourningOnTenAv(cal, nowMillis)
            // Deferred TBA: meat/wine (and related) through Monday morning after Sunday fast.
            month == HebrewCalendarEngine.AV && day == 11 ->
                isPostDeferredTbaUntilMondayMorning(cal, nowMillis)
            else -> false
        }
    }

    /**
     * Shavuah she'chal bo — Motzei Shabbat preceding the observed Tisha B'Av
     * through the same post-fast mourning end as the Nine Days window.
     *
     * When Tisha B'Av is deferred to Sunday, there is no weekday "week of TBA"
     * beforehand — the row begins Motzei Shabbat into the fast.
     */
    fun isInShavuahSheChalBo(cal: DayInfo, nowMillis: Long): Boolean {
        if (!isInAshkenazNineDaysWindow(cal, nowMillis)) return false
        val month = cal.hebrewMonth ?: return false
        val day = cal.hebrewDay ?: return false
        if (month != HebrewCalendarEngine.AV) return false
        // Keep 10 Av / deferred Monday-morning tail for Sephardim too.
        if (day == 10) return isStillMourningOnTenAv(cal, nowMillis)
        if (day == 11) return isPostDeferredTbaUntilMondayMorning(cal, nowMillis)

        val tbaDay = observedTishaBeavHebrewDay(cal) ?: return false
        if (day > tbaDay) return false
        if (day == tbaDay) return true

        val tbaDow = dayOfWeekForHebrewDayInAv(cal, tbaDay) ?: return false
        // Sunday of TBA's week (Motzei previous Shabbat = start of that Sunday night/day).
        val daysFromSundayToTba =
            (tbaDow - HebrewCalendarEngine.SUNDAY + 7) % 7
        val shavuahStartHebrewDay = tbaDay - daysFromSundayToTba
        return day >= shavuahStartHebrewDay
    }

    /** Observed fast: 9 Av, or Sunday 10 Av when 9 Av is Shabbat. */
    private fun observedTishaBeavHebrewDay(cal: DayInfo): Int? {
        val dowOf9 = dayOfWeekForHebrewDayInAv(cal, 9) ?: return null
        return if (dowOf9 == HebrewCalendarEngine.SATURDAY) 10 else 9
    }

    /**
     * Day-of-week (1=Sun … 7=Sat) for a Hebrew day in the same Av as [cal],
     * projected from today's civil DOW and Hebrew day.
     */
    private fun dayOfWeekForHebrewDayInAv(cal: DayInfo, targetHebrewDay: Int): Int? {
        if (cal.hebrewMonth != HebrewCalendarEngine.AV) return null
        val today = cal.hebrewDay ?: return null
        val todayDow = javaDow(cal.date.dayOfWeek)
        val delta = targetHebrewDay - today
        return ((todayDow - 1 + delta) % 7 + 7) % 7 + 1
    }

    private fun javaDow(dow: DayOfWeek): Int = when (dow) {
        DayOfWeek.SUNDAY -> HebrewCalendarEngine.SUNDAY
        DayOfWeek.MONDAY -> HebrewCalendarEngine.MONDAY
        DayOfWeek.TUESDAY -> HebrewCalendarEngine.TUESDAY
        DayOfWeek.WEDNESDAY -> HebrewCalendarEngine.WEDNESDAY
        DayOfWeek.THURSDAY -> HebrewCalendarEngine.THURSDAY
        DayOfWeek.FRIDAY -> HebrewCalendarEngine.FRIDAY
        DayOfWeek.SATURDAY -> HebrewCalendarEngine.SATURDAY
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

    /**
     * After deferred Sunday Tisha B'Av: meat/wine (and related Nine Days tail) until Monday morning.
     * 11 Av begins Motzei Sunday; ends at dawn Monday.
     */
    private fun isPostDeferredTbaUntilMondayMorning(cal: DayInfo, nowMillis: Long): Boolean {
        val dawn = cal.zmanim?.alotHaShacharMillis ?: cal.zmanim?.sunriseMillis
        return when (cal.date.dayOfWeek) {
            DayOfWeek.SUNDAY -> cal.startedTonightAtTzeit
            DayOfWeek.MONDAY ->
                !cal.startedTonightAtTzeit && (dawn == null || nowMillis < dawn)
            else -> false
        }
    }

    private fun beforeChatzosOnTenAv(cal: DayInfo, nowMillis: Long): Boolean {
        val chatzos = cal.zmanim?.chatzosMillis ?: return true
        return nowMillis < chatzos
    }
}
