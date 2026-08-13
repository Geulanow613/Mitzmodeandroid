package com.beardytop.beatzaddik.domain

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.plus

/**
 * When Yom Tov is immediately followed by Shabbat, eruv tavshilin is made on the festival eve.
 *
 * Diaspora (2 civil days of Yom Tov, then Shabbat):
 * • **Wednesday erev** — chag Wed night; Thu & Fri YT; motzei Fri → Shabbat.
 * • **Thursday erev** — chag Thu night; Fri & Sat YT (Sat is also Shabbat).
 * NOT Tue-night start (Wed+Thu YT ending Thu night; erev is Tuesday).
 *
 * Israel:
 * • **Thursday erev** — one day YT Friday (Pesach, Shavuot, Sukkot, etc.); motzei Fri → Shabbat.
 * • **Wednesday erev** — two days Rosh Hashana (Thu & Fri); motzei Fri → Shabbat.
 * • **Thursday erev** — two days Rosh Hashana (Fri & Sat); 2nd day on Shabbat.
 * NOT one-day YT Thursday with Friday chol before Shabbat (erev Wednesday).
 *
 * NOT when Yom Tov begins ON Shabbat (cook on chol/weekday erev as usual). Not Yom Kippur.
 */
object EruvTavshilinRules {

    fun isFestivalEve(cal: DayInfo): Boolean =
        "erev_chag" in cal.activeSeasons ||
            ("erev_pesach" in cal.activeSeasons &&
                HebrewCalendarEngine.isErevFirstPesachSeder(cal.hebrewMonth, cal.hebrewDay))

    fun chagName(cal: DayInfo): String =
        cal.upcomingChagName ?: if ("erev_pesach" in cal.activeSeasons) "Pesach" else "Yom Tov"

    fun chagIndex(cal: DayInfo, tomorrowCal: DayInfo): Int? =
        cal.upcomingChagYomTovIndex
            ?: HebrewCalendarEngine.PESACH.takeIf {
                "erev_pesach" in cal.activeSeasons &&
                    HebrewCalendarEngine.isErevFirstPesachSeder(cal.hebrewMonth, cal.hebrewDay)
            }
            ?: holidayNameToYomTovIndex(tomorrowCal.yomTovHolidayName)

    private fun holidayNameToYomTovIndex(name: String?): Int? = when (name?.trim()?.lowercase()) {
        "pesach", "passover" -> HebrewCalendarEngine.PESACH
        "shavuot", "shavuos" -> HebrewCalendarEngine.SHAVUOS
        "sukkot", "succos", "sukkos" -> HebrewCalendarEngine.SUCCOS
        "rosh hashana", "rosh hashanah" -> HebrewCalendarEngine.ROSH_HASHANA
        "shemini atzeret", "shemini atzeres" -> HebrewCalendarEngine.SHEMINI_ATZERES
        "simchat torah", "simchas torah" -> HebrewCalendarEngine.SIMCHAS_TORAH
        "yom kippur" -> HebrewCalendarEngine.YOM_KIPPUR
        else -> null
    }

    /** Civil Yom Tov days starting tomorrow (first YT civil day = tomorrow on festival eve). */
    fun yomTovCivilDays(profile: UserProfile, chagIdx: Int): Int =
        if (profile.isInIsrael && chagIdx != HebrewCalendarEngine.ROSH_HASHANA) 1 else 2

    /**
     * Civil date of the last Yom Tov day in the opening sequence (chag begins tonight on festival eve).
     */
    fun lastYomTovCivilDate(erev: DayInfo, profile: UserProfile, chagIdx: Int) =
        erev.date.plus(yomTovCivilDays(profile, chagIdx), DateTimeUnit.DAY)

    /**
     * Shabbat immediately follows the Yom Tov sequence — no weekday between the last YT day and Shabbat.
     */
    fun shabbatImmediatelyFollowsYomTov(erev: DayInfo, profile: UserProfile, chagIdx: Int): Boolean {
        val ytDays = yomTovCivilDays(profile, chagIdx)
        return when (lastYomTovCivilDate(erev, profile, chagIdx).dayOfWeek) {
            // Motzei Friday Yom Tov → Shabbat (Wed erev diaspora, Thu erev Israel 1-day, Wed erev Israel RH).
            DayOfWeek.FRIDAY -> true
            // Second YT day on Shabbat (Thu erev diaspora; Thu erev Israel RH Fri–Sat).
            DayOfWeek.SATURDAY -> ytDays >= 2
            else -> false
        }
    }

    /** Civil days from this festival eve until the next Saturday (used by prep copy). */
    fun civilDaysUntilShabbat(cal: DayInfo): Int? {
        if (!isFestivalEve(cal)) return null
        for (i in 1..4) {
            if (cal.date.plus(i, DateTimeUnit.DAY).dayOfWeek == DayOfWeek.SATURDAY) return i
        }
        return null
    }

    fun requiresEruvTavshilin(
        cal: DayInfo,
        profile: UserProfile,
        tomorrowCal: DayInfo,
    ): Boolean {
        if (!isFestivalEve(cal)) return false
        val chagIdx = chagIndex(cal, tomorrowCal) ?: return false
        if (chagIdx == HebrewCalendarEngine.YOM_KIPPUR) return false
        if (YomTovShabbatPrepText.isYomTovFridayLeadsIntoShabbat(cal)) return false
        // Chag begins tonight on Shabbat — today is chol/weekday; cook today for Shabbat normally.
        if (tomorrowCal.isShabbat) return false
        return shabbatImmediatelyFollowsYomTov(cal, profile, chagIdx)
    }

    fun checklistSection(cal: DayInfo): String =
        if ("erev_pesach" in cal.activeSeasons) "Pesach prep" else "Prepare for the festival"
}
