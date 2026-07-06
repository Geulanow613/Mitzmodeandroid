package com.beardytop.beatzaddik.domain

import kotlinx.datetime.DayOfWeek

/**
 * Walled-city Purim (Jerusalem): observance on 15 Adar (Shushan Purim), not 14 Adar.
 * When 15 Adar is Shabbat, mitzvot split across Purim Meshulash (Friday / Shabbat / Sunday).
 */
object JerusalemPurimRules {

    fun isJerusalemProfile(profile: UserProfile): Boolean {
        if (profile.manualCityId in setOf("jlm", "jerusalem")) return true
        return profile.locationLabel?.contains("jerusalem", ignoreCase = true) == true
    }

    fun isPurimMeshulashFriday(
        isJerusalem: Boolean,
        todayYomTovIndex: Int,
        tomorrowYomTovIndex: Int,
        dayOfWeek: DayOfWeek,
    ): Boolean = isJerusalem &&
        todayYomTovIndex == HebrewCalendarEngine.PURIM &&
        tomorrowYomTovIndex == HebrewCalendarEngine.SHUSHAN_PURIM &&
        dayOfWeek == DayOfWeek.FRIDAY

    fun isPurimMeshulashSunday(
        isJerusalem: Boolean,
        dayOfWeek: DayOfWeek,
        yesterdayYomTovIndex: Int,
    ): Boolean = isJerusalem &&
        dayOfWeek == DayOfWeek.SUNDAY &&
        yesterdayYomTovIndex == HebrewCalendarEngine.SHUSHAN_PURIM

    /** Shushan Purim on Shabbat during Meshulash — communal observance only; no home mitzvot. */
    fun isShushanPurimOnMeshulashShabbat(
        isJerusalem: Boolean,
        todayYomTovIndex: Int,
        yesterdayYomTovIndex: Int,
        dayOfWeek: DayOfWeek,
    ): Boolean = isJerusalem &&
        todayYomTovIndex == HebrewCalendarEngine.SHUSHAN_PURIM &&
        dayOfWeek == DayOfWeek.SATURDAY &&
        yesterdayYomTovIndex == HebrewCalendarEngine.PURIM

    fun isPurimDay(
        isJerusalem: Boolean,
        todayYomTovIndex: Int,
        tomorrowYomTovIndex: Int,
        yesterdayYomTovIndex: Int,
        dayOfWeek: DayOfWeek,
    ): Boolean {
        if (!isJerusalem) {
            return todayYomTovIndex == HebrewCalendarEngine.PURIM
        }
        if (isShushanPurimOnMeshulashShabbat(
                isJerusalem,
                todayYomTovIndex,
                yesterdayYomTovIndex,
                dayOfWeek,
            )
        ) {
            return false
        }
        if (isPurimMeshulashFriday(isJerusalem, todayYomTovIndex, tomorrowYomTovIndex, dayOfWeek)) {
            return true
        }
        return todayYomTovIndex == HebrewCalendarEngine.SHUSHAN_PURIM
    }

    fun isErevPurimDay(
        isJerusalem: Boolean,
        isPurimToday: Boolean,
        tomorrowYomTovIndex: Int,
        dayAfterTomorrowYomTovIndex: Int,
        tomorrowDayOfWeek: DayOfWeek,
    ): Boolean {
        if (isPurimToday) return false
        if (!isJerusalem) {
            return tomorrowYomTovIndex == HebrewCalendarEngine.PURIM
        }
        if (tomorrowYomTovIndex == HebrewCalendarEngine.SHUSHAN_PURIM) {
            return true
        }
        // Meshulash: Thursday erev before Friday megillah / matanot block.
        if (tomorrowYomTovIndex == HebrewCalendarEngine.PURIM &&
            tomorrowDayOfWeek == DayOfWeek.FRIDAY &&
            dayAfterTomorrowYomTovIndex == HebrewCalendarEngine.SHUSHAN_PURIM
        ) {
            return true
        }
        return false
    }

    fun isMeshulashFridayErev(
        tomorrowYomTovIndex: Int,
        dayAfterTomorrowYomTovIndex: Int,
        tomorrowDayOfWeek: DayOfWeek,
    ): Boolean =
        tomorrowYomTovIndex == HebrewCalendarEngine.PURIM &&
            tomorrowDayOfWeek == DayOfWeek.FRIDAY &&
            dayAfterTomorrowYomTovIndex == HebrewCalendarEngine.SHUSHAN_PURIM

    fun upcomingHint(
        isJerusalem: Boolean,
        meshulashSunday: Boolean = false,
        meshulashShabbat: Boolean = false,
        meshulashFriday: Boolean = false,
        meshulashFridayErev: Boolean = false,
    ): String = when {
        meshulashSunday -> "Mishloach manot and Purim seudah"
        meshulashShabbat -> "Al HaNissim & Vayavo Amalek — seudah Sunday"
        meshulashFriday -> "Megillah and matanot la'evyonim — no Al HaNissim or seudah"
        meshulashFridayErev ->
            "Fri: Megillah & gifts to poor; Sun: mishloach & seudah"
        isJerusalem -> "Megillah, matanot, mishloach manot"
        else -> "Megillah, matanot, mishloach manot"
    }

    fun upcomingDisplayName(
        isJerusalem: Boolean,
        meshulashFridayErev: Boolean = false,
        meshulashFriday: Boolean = false,
        meshulashShabbat: Boolean = false,
        meshulashSunday: Boolean = false,
    ): String = when {
        meshulashFridayErev || meshulashFriday || meshulashShabbat || meshulashSunday ->
            "Purim Meshulash"
        !isJerusalem -> "Purim"
        else -> "Shushan Purim"
    }

    fun statusChipLabel(isJerusalem: Boolean): String =
        if (isJerusalem) "Shushan Purim" else "Purim"
}
