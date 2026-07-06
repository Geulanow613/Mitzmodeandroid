package com.beardytop.beatzaddik.domain

import kotlinx.datetime.LocalDate

data class DayInfo(
    val date: LocalDate,
    val civilLabel: String,
    val hebrewLabel: String,
    val parsha: String?,
    val statusChips: List<String>,
    val isShabbat: Boolean,
    val isErevShabbat: Boolean,
    val isYomTov: Boolean,
    /** Full Yom Tov with melacha restrictions (not Chol HaMoed, Purim, Chanukah, etc.). */
    val isYomTovAssurBemelacha: Boolean = false,
    /** Previous civil day was also Yom Tov assur bemelacha (for Diaspora 2nd-day detection). */
    val yesterdayWasYomTovAssurBemelacha: Boolean = false,
    /** Display name when [isYomTovAssurBemelacha] (e.g. "Pesach", "Rosh Hashana"). */
    val yomTovHolidayName: String? = null,
    val isShabbatOrYomTov: Boolean,
    /** What to show right now based on zmanim / local clock */
    val activeTimeOfDay: TimeOfDay,
    val activePeriodLabel: String,
    /** Plain-English summary for the current (active) part of the day. */
    val activePeriodHint: String? = null,
    /** Plain-English summary for mitzvot in the collapsed "later" section. */
    val inactivePeriodHint: String?,
    val omerDay: Int? = null,
    val isSefiratHaomer: Boolean = false,
    val isLagBaomer: Boolean = false,
    val isChanukah: Boolean = false,
    val chanukahDay: Int? = null,
    val isPurim: Boolean = false,
    val isRoshChodesh: Boolean = false,
    val isYomHaShoah: Boolean = false,
    val isYomHaZikaron: Boolean = false,
    val isYomHaAtzmaut: Boolean = false,
    val isYomYerushalayim: Boolean = false,
    val activeSeasons: Set<String> = emptySet(),
    val hebrewMonth: Int? = null,
    val hebrewDay: Int? = null,
    val hebrewYear: Int? = null,
    val zmanim: ZmanimSnapshot? = null,
    /**
     * The parsha (Torah portion) to be read on the upcoming Shabbat, formatted for display.
     * Accounts for Israel vs Diaspora divergence (Yom Tov deferral).
     * Null when no calendar library is available (heuristic backend).
     */
    val upcomingShabbatParsha: String? = null,
    /** Display name of tomorrow's Yom Tov when today is erev chag (e.g. "Pesach", "Rosh Hashana"). */
    val upcomingChagName: String? = null,
    /** KosherJava [yomTovIndex] for tomorrow when today is erev chag; drives erev prep copy. */
    val upcomingChagYomTovIndex: Int? = null,
    /** Today's public fast day index ([HebrewCalendarEngine] yomTovIndex), if any. */
    val fastDayIndex: Int? = null,
    val fastDayName: String? = null,
    /** Tomorrow's public fast day index when today is erev prep. */
    val upcomingFastDayIndex: Int? = null,
    val upcomingFastDayName: String? = null,
    /**
     * True from tzeit until civil midnight: this DayInfo describes the Hebrew day that began
     * tonight at nightfall, while the civil (Gregorian) date is still the previous day.
     * [date] and [zmanim] refer to this Hebrew day's upcoming civil daytime;
     * [civilLabel] keeps the real civil date for display. See [HalachicDayRollover].
     */
    val startedTonightAtTzeit: Boolean = false,
)
