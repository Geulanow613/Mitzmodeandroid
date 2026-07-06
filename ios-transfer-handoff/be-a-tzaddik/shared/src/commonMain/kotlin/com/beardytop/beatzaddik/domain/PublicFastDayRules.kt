package com.beardytop.beatzaddik.domain

/** Public fast days — calendar detection and shared halachic metadata. */
object PublicFastDayRules {

    val MINOR_FAST_INDICES = setOf(
        HebrewCalendarEngine.FAST_OF_GEDALYAH,
        HebrewCalendarEngine.TENTH_OF_TEVES,
        HebrewCalendarEngine.FAST_OF_ESTHER,
        HebrewCalendarEngine.SEVENTEEN_OF_TAMMUZ,
    )

    val ALL_FAST_INDICES = MINOR_FAST_INDICES + setOf(
        HebrewCalendarEngine.YOM_KIPPUR,
        HebrewCalendarEngine.TISHA_BEAV,
    )

    fun isPublicFast(idx: Int): Boolean = idx in ALL_FAST_INDICES

    fun isMinorFast(idx: Int): Boolean = idx in MINOR_FAST_INDICES

    /** Minor fasts: dawn (alot hashachar) to nightfall. Yom Kippur & Tisha B'Av: sunset to nightfall. */
    fun fastStartsAtSunset(idx: Int): Boolean =
        idx == HebrewCalendarEngine.YOM_KIPPUR || idx == HebrewCalendarEngine.TISHA_BEAV

    fun displayName(idx: Int): String = when (idx) {
        HebrewCalendarEngine.FAST_OF_GEDALYAH -> "Fast of Gedaliah"
        HebrewCalendarEngine.TENTH_OF_TEVES -> "Fast of 10 Tevet"
        HebrewCalendarEngine.FAST_OF_ESTHER -> "Fast of Esther (Taanit Esther)"
        HebrewCalendarEngine.SEVENTEEN_OF_TAMMUZ -> "Fast of 17 Tammuz"
        HebrewCalendarEngine.YOM_KIPPUR -> "Yom Kippur"
        HebrewCalendarEngine.TISHA_BEAV -> "Tisha B'Av"
        else -> "Public fast"
    }

    /** Day before a minor dawn-to-nightfall fast — not Yom Kippur or Tisha B'Av (they have their own prep). */
    fun isErevMinorFast(todayIdx: Int, tomorrowIdx: Int): Boolean =
        isMinorFast(tomorrowIdx) && !isPublicFast(todayIdx)

    fun isErevYomKippur(idx: Int): Boolean = idx == HebrewCalendarEngine.EREV_YOM_KIPPUR

    /**
     * Erev Tisha B'Av restrictions (8 Av, or Motzei Shabbat when 9 Av is Shabbat and the fast is Sunday).
     */
    fun isErevTishaBeav(
        todayIdx: Int,
        tomorrowIdx: Int,
        isShabbat: Boolean,
        hebrewMonth: Int?,
        hebrewDay: Int?,
    ): Boolean {
        if (tomorrowIdx == HebrewCalendarEngine.TISHA_BEAV && todayIdx != HebrewCalendarEngine.TISHA_BEAV) {
            return true
        }
        // When 9 Av is Shabbat, the fast moves to Sunday — restrictions begin Motzei Shabbat.
        return isShabbat && hebrewMonth == HebrewCalendarEngine.AV && hebrewDay == 9
    }

    fun fastTimingLabel(idx: Int): String = when {
        fastStartsAtSunset(idx) -> "from sunset until nightfall the following night"
        else -> "from dawn (alot hashachar) until nightfall (tzeit)"
    }

    /**
     * When a sunset fast has already begun on the Hebrew eve (e.g. 8 Av night before 9 Av),
     * [JewishCalendar.isTaanis] is true but [yomTovIndex] may still be the eve day — infer the fast.
     */
    fun resolveFastDayIndex(todayIdx: Int, tomorrowIdx: Int, isTaanis: Boolean): Int? {
        if (isPublicFast(todayIdx)) return todayIdx
        if (isTaanis && isPublicFast(tomorrowIdx) && fastStartsAtSunset(tomorrowIdx)) return tomorrowIdx
        return null
    }

    /**
     * True when a Shabbat-postponed fast is observed on the day after its Hebrew calendar date
     * (e.g. 17 Tammuz on Shabbat → fast on Sunday 18 Tammuz; Hebrew label shows 18 Tammuz).
     */
    fun isDeferredFromShabbat(cal: DayInfo): Boolean {
        val idx = cal.fastDayIndex ?: return false
        val month = cal.hebrewMonth ?: return false
        val day = cal.hebrewDay ?: return false
        return when (idx) {
            HebrewCalendarEngine.SEVENTEEN_OF_TAMMUZ ->
                month == HebrewCalendarEngine.TAMMUZ && day == 18
            HebrewCalendarEngine.TISHA_BEAV ->
                month == HebrewCalendarEngine.AV && day == 10
            HebrewCalendarEngine.FAST_OF_GEDALYAH ->
                month == HebrewCalendarEngine.TISHREI && day == 4
            else -> false
        }
    }

    fun deferredFastSubtitle(cal: DayInfo): String? {
        if (!isDeferredFromShabbat(cal)) return null
        return when (cal.fastDayIndex) {
            HebrewCalendarEngine.SEVENTEEN_OF_TAMMUZ -> "Deferred from 17 Tammuz"
            HebrewCalendarEngine.TISHA_BEAV -> "Deferred from 9 Av"
            HebrewCalendarEngine.FAST_OF_GEDALYAH -> "Deferred from 3 Tishrei"
            else -> "Deferred from Shabbat"
        }
    }

    /**
     * True when tonight's nightfall ends the fast into Shabbat or Yom Tov (e.g. Taanit Esther on
     * Friday, 10 Tevet on Friday). Those days keep the fast row visible with local break-fast copy.
     */
    fun fastEndsIntoShabbatOrYomTov(cal: DayInfo, tomorrowCal: DayInfo?): Boolean {
        if (cal.fastDayIndex == null) return false
        if (cal.isErevShabbat) return true
        if (tomorrowCal?.isYomTovAssurBemelacha == true) return true
        return false
    }

    /**
     * After tzeit the fast is over; show "Fast ended at …" until chatzos halayla, then drop the row.
     * Skipped when [fastEndsIntoShabbatOrYomTov] (Friday / erev chag fasts).
     */
    fun shouldHideEndedFastItem(
        nowMillis: Long,
        cal: DayInfo,
        tomorrowCal: DayInfo?,
        availability: ItemZmanAvailability,
    ): Boolean {
        if (availability != ItemZmanAvailability.EXPIRED) return false
        if (cal.fastDayIndex == null) return true
        if (fastEndsIntoShabbatOrYomTov(cal, tomorrowCal)) return false
        val z = cal.zmanim ?: return true
        val tzeit = z.tzeitMillis ?: return true
        if (nowMillis < tzeit) return false
        val chatzosLayla = ZmanimHelpers.chatzosLaylaMillis(z, nowMillis) ?: return true
        return nowMillis >= chatzosLayla
    }
}
