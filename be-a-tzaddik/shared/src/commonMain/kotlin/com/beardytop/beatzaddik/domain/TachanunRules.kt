package com.beardytop.beatzaddik.domain

/** When Tachanun is recited after weekday Amidah (Shacharit / Mincha). */
object TachanunRules {

    val tachanunItemIds = setOf(
        "ashkenaz_shacharit_tachanun",
        "sefard_shacharit_tachanun",
        "edot_hamizrach_shacharit_tachanun",
        "chabad_shacharit_tachanun",
        "ashkenaz_mincha_tachanun",
        "sefard_mincha_tachanun",
        "edot_hamizrach_mincha_tachanun",
        "chabad_mincha_tachanun",
    )

    /** @deprecated use [tachanunItemIds] */
    val minchaItemIds = tachanunItemIds.filter { it.contains("mincha") }.toSet()

    fun isTachanunOnlyItem(itemId: String): Boolean = itemId in tachanunItemIds

    enum class PrayerSlot { SHACHARIT, MINCHA }

    fun prayerSlotForItem(itemId: String): PrayerSlot = when {
        itemId.contains("mincha") -> PrayerSlot.MINCHA
        else -> PrayerSlot.SHACHARIT
    }

    /** True on ordinary weekdays when Tachanun follows the Amidah for this prayer slot. */
    fun isRecited(cal: DayInfo, profile: UserProfile, itemId: String, tomorrowCal: DayInfo): Boolean =
        isRecited(
            cal,
            profile.isInIsrael,
            prayerSlotForItem(itemId),
            tomorrowCal,
            profile.effectiveNusach(),
        )

    fun isRecited(
        cal: DayInfo,
        isInIsrael: Boolean,
        slot: PrayerSlot = PrayerSlot.SHACHARIT,
        tomorrowCal: DayInfo? = null,
        nusach: EffectiveNusach = EffectiveNusach.ASHKENAZ,
    ): Boolean {
        if (cal.isShabbat) return false
        if (cal.isYomTov) return false
        if (cal.isRoshChodesh) return false
        if (cal.isChanukah || cal.isPurim || JerusalemPurimRules.isMeshulashSeason(cal)) return false
        if (cal.isLagBaomer) return false
        if (cal.hebrewMonth == HebrewCalendarEngine.NISSAN) return false

        val month = cal.hebrewMonth ?: return true
        val day = cal.hebrewDay ?: return true
        val year = cal.hebrewYear

        // Erev Rosh Hashana (29 Elul) and Erev Yom Kippur (9 Tishrei): omitted at Shacharit only.
        if (slot == PrayerSlot.SHACHARIT) {
            if (month == HebrewCalendarEngine.ELUL && day == 29) return false
            if (month == HebrewCalendarEngine.TISHREI && day == 9) return false
        }

        // Erev Shabbat / chagim / Purim / festive days: omit at Mincha (not Erev RH / Erev YK).
        if (slot == PrayerSlot.MINCHA && omitsMinchaTachanunOnErev(cal, tomorrowCal)) {
            return false
        }

        if (isInIsrael && (cal.isYomHaAtzmaut || cal.isYomYerushalayim)) return false
        // Yom HaZikaron (Israel): many omit Tachanun at Mincha before Yom Ha'atzmaut; Shacharit as usual.
        if (isInIsrael && cal.isYomHaZikaron && slot == PrayerSlot.MINCHA) return false

        if (isMinorCommemorativeHoliday(month, day, year)) return false

        if (month == HebrewCalendarEngine.TISHREI && day in 11..14) return false

        if (month == HebrewCalendarEngine.TISHREI) {
            val afterSimchatTorahStart = if (isInIsrael) 23 else 24
            if (day in afterSimchatTorahStart..30) return false
        }

        // Post-Shavuot: Ashkenaz commonly through 12 Sivan (MB 494:11). Sephardi/Edot often
        // resume after Isru Chag — some still omit until the later Ashkenaz date.
        if (month == HebrewCalendarEngine.SIVAN &&
            isInPostShavuotOmissionWindow(month, day, isInIsrael, nusach)
        ) {
            return false
        }

        // Observed Tisha B'Av (9 Av, or deferred Sunday 10 Av).
        if (cal.fastDayIndex == HebrewCalendarEngine.TISHA_BEAV) return false
        if (month == HebrewCalendarEngine.AV && day == 9) return false

        return when (month) {
            HebrewCalendarEngine.SHEVAT -> day != 15
            HebrewCalendarEngine.IYAR -> day != 14 && day != 18
            HebrewCalendarEngine.AV -> day != 15
            else -> true
        }
    }

    /**
     * Mincha Tachanun is omitted on erev Shabbat, erev most chagim and festive days
     * (Purim, Chanukah, Tisha B'Av, Rosh Chodesh, Tu B'Shvat, Pesach Sheini, Lag BaOmer,
     * Tu B'Av, Purim Katan, etc.).
     *
     * Exceptions: Erev Rosh Hashana and Erev Yom Kippur — Tachanun is said at Mincha
     * (those days omit Tachanun at Shacharit only), even if they fall on Friday.
     */
    private fun omitsMinchaTachanunOnErev(cal: DayInfo, tomorrowCal: DayInfo?): Boolean {
        if (isErevRoshHashanaOrYomKippur(cal)) return false

        if (TonightHolyDayRules.tonightBeginsShabbat(cal)) return true

        if ("erev_tisha_beav" in cal.activeSeasons) return true
        if ("erev_purim" in cal.activeSeasons) return true
        if ("erev_chanukah" in cal.activeSeasons) return true

        // Classic 8 Av when seasons are not set (e.g. unit tests) — includes Friday before
        // deferred Sunday Tisha B'Av (9 Av on Shabbat), which is also covered by erev Shabbat.
        if (cal.hebrewMonth == HebrewCalendarEngine.AV && cal.hebrewDay == 8) return true

        if (tomorrowCal != null) {
            if (tomorrowCal.isYomTovAssurBemelacha || tomorrowCal.isYomTov) return true
            if (tomorrowCal.isPurim) return true
            if (tomorrowCal.isChanukah) return true
            if (tomorrowCal.isRoshChodesh) return true
            if (tomorrowCal.isLagBaomer) return true
            if (tomorrowCal.fastDayIndex == HebrewCalendarEngine.TISHA_BEAV) return true
            // Deferred TBA: tomorrow is Shabbat 9 Av (fast Sunday) — omit Mincha Tachanun Friday.
            if (tomorrowCal.isShabbat &&
                tomorrowCal.hebrewMonth == HebrewCalendarEngine.AV &&
                tomorrowCal.hebrewDay == 9
            ) {
                return true
            }
            if (isMinorCommemorativeHoliday(
                    tomorrowCal.hebrewMonth,
                    tomorrowCal.hebrewDay,
                    tomorrowCal.hebrewYear,
                )
            ) {
                return true
            }
        } else if (cal.upcomingChagName != null || cal.upcomingChagYomTovIndex != null) {
            // Backend marked erev chag without a tomorrow snapshot.
            return true
        }

        return false
    }

    private fun isErevRoshHashanaOrYomKippur(cal: DayInfo): Boolean {
        val month = cal.hebrewMonth ?: return false
        val day = cal.hebrewDay ?: return false
        return (month == HebrewCalendarEngine.ELUL && day == 29) ||
            (month == HebrewCalendarEngine.TISHREI && day == 9)
    }

    /** Minor holidays whose Hebrew day begins at nightfall (eve shows in Upcoming). */
    fun isEveningStartMinorHoliday(idx: Int): Boolean = idx in eveningStartMinorHolidayIndices

    val eveningStartMinorHolidayIndices = setOf(
        HebrewCalendarEngine.TU_BESHVAT,
        HebrewCalendarEngine.PESACH_SHENI,
        HebrewCalendarEngine.LAG_BAOMER,
        HebrewCalendarEngine.TU_BEAV,
        HebrewCalendarEngine.PURIM_KATAN,
        HebrewCalendarEngine.SHUSHAN_PURIM_KATAN,
    )

    fun isMinorCommemorativeHoliday(month: Int?, day: Int?, year: Int?): Boolean {
        if (month == null || day == null) return false
        return when (month) {
            HebrewCalendarEngine.SHEVAT -> day == 15
            HebrewCalendarEngine.IYAR -> day == 14 || day == 18
            HebrewCalendarEngine.AV -> day == 15
            HebrewCalendarEngine.ADAR ->
                year != null &&
                    HebrewCalendarEngine.isJewishLeapYear(year) &&
                    (day == 14 || day == 15)
            else -> false
        }
    }

    fun yomTovIndexFor(cal: DayInfo, isInIsrael: Boolean): Int? {
        val year = cal.hebrewYear ?: return null
        val month = cal.hebrewMonth ?: return null
        val day = cal.hebrewDay ?: return null
        return HebrewCalendarEngine.getYomTovIndex(
            year = year,
            month = month,
            day = day,
            dayOfWeek = cal.date.dayOfWeek.toHebrewEngineDay(),
            isLeapYear = HebrewCalendarEngine.isJewishLeapYear(year),
            inIsrael = isInIsrael,
            useModernHolidays = true,
        ).takeIf { it >= 0 }
    }

    /** @deprecated use [isRecited] with item id — retained for tests */
    fun isRecited(cal: DayInfo, isInIsrael: Boolean): Boolean =
        isRecited(cal, isInIsrael, PrayerSlot.SHACHARIT, tomorrowCal = null)

    /**
     * Post-Shavuot Tachanun omission.
     * Ashkenaz / Chabad / Other: through 12 Sivan (MB 494:11).
     * Sephardi / Edot: through Isru Chag (7 Sivan Israel / 8 diaspora); some communities
     * continue omitting until 12 Sivan — follow your minhag.
     */
    fun isInPostShavuotOmissionWindow(
        hebrewMonth: Int,
        hebrewDay: Int,
        isInIsrael: Boolean,
        nusach: EffectiveNusach = EffectiveNusach.ASHKENAZ,
    ): Boolean {
        if (hebrewMonth != HebrewCalendarEngine.SIVAN) return false
        val isruChagDay = if (isInIsrael) 7 else 8
        val throughDay = when (nusach) {
            EffectiveNusach.SEFARD, EffectiveNusach.EDOT_HAMIZRACH -> isruChagDay
            else -> 12
        }
        return hebrewDay in 1..throughDay
    }

    fun isInPostShavuotOmissionWindow(
        cal: DayInfo,
        isInIsrael: Boolean,
        nusach: EffectiveNusach = EffectiveNusach.ASHKENAZ,
    ): Boolean {
        val month = cal.hebrewMonth ?: return false
        val day = cal.hebrewDay ?: return false
        return isInPostShavuotOmissionWindow(month, day, isInIsrael, nusach)
    }

    const val POST_SHAVUOT_OMISSION_NOTE =
        "From Rosh Chodesh Sivan, Ashkenaz communities commonly omit Tachanun through 12 Sivan. " +
            "Many Sephardi communities resume after Isru Chag, though some continue omitting until the later date — follow your minhag."
}

private fun kotlinx.datetime.DayOfWeek.toHebrewEngineDay(): Int = when (this) {
    kotlinx.datetime.DayOfWeek.SUNDAY -> HebrewCalendarEngine.SUNDAY
    kotlinx.datetime.DayOfWeek.MONDAY -> HebrewCalendarEngine.MONDAY
    kotlinx.datetime.DayOfWeek.TUESDAY -> HebrewCalendarEngine.TUESDAY
    kotlinx.datetime.DayOfWeek.WEDNESDAY -> HebrewCalendarEngine.WEDNESDAY
    kotlinx.datetime.DayOfWeek.THURSDAY -> HebrewCalendarEngine.THURSDAY
    kotlinx.datetime.DayOfWeek.FRIDAY -> HebrewCalendarEngine.FRIDAY
    kotlinx.datetime.DayOfWeek.SATURDAY -> HebrewCalendarEngine.SATURDAY
}
