package com.beardytop.beatzaddik.domain

import kotlinx.datetime.DayOfWeek

/**
 * Walled-city Purim (Jerusalem): observance on 15 Adar (Shushan Purim), not 14 Adar.
 * When 15 Adar is Shabbat, mitzvot split across Purim Meshulash (Friday / Shabbat / Sunday).
 */
object JerusalemPurimRules {

    fun isJerusalemProfile(profile: UserProfile): Boolean {
        // Same city-id / lat-lon box as CandleLightingRules (40-min candles) — not freeform labels.
        if (CandleLightingRules.isJerusalem(profile)) return true
        if (profile.manualCityId in doubtfulWalledCityIds) return profile.observeWalledCityPurim
        return false
    }

    fun isPurimDoubtCity(manualCityId: String?): Boolean =
        manualCityId != null && manualCityId in doubtfulWalledCityIds

    /**
     * Cities with disputed “walled in Yehoshua’s time” status (or diaspora communities that
     * historically observed 15 Adar). Users there may choose walled-city Purim customs.
     */
    val doubtfulWalledCityIds: Set<String> = setOf(
        // Israel / Land of Israel (often cited)
        "acre_il",
        "ashdod",
        "ashkelon",
        "beeri_il",
        "beersheba",
        "beit_shean_il",
        "beit_shemesh_il",
        "ein_zeitim_il",
        "gaza_city",
        "gush_chalav_il",
        "haifa",
        "hebron_il",
        "jaffa_il",
        "jericho_il",
        "lod_il",
        "nablus_ps",
        "ramallah",
        "ramla_il",
        "safed",
        "shechem_il",
        "tiberias",
        // Outside Israel (community / lineage dependent)
        "baghdad",
        "damascus",
        "prague",
        "thessaloniki",
        "izmir",
        "istanbul",
    )

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
            // Communal Shabbat only — Tachanun already off for Shabbat; not a home Purim day.
            return false
        }
        if (isPurimMeshulashFriday(isJerusalem, todayYomTovIndex, tomorrowYomTovIndex, dayOfWeek)) {
            return true
        }
        // Sunday mishloach / seudah — omit Tachanun; checklist uses meshulash_sunday season.
        if (isPurimMeshulashSunday(isJerusalem, dayOfWeek, yesterdayYomTovIndex)) {
            return true
        }
        return todayYomTovIndex == HebrewCalendarEngine.SHUSHAN_PURIM
    }

    fun isMeshulashSeason(cal: DayInfo): Boolean =
        "purim_meshulash_friday" in cal.activeSeasons ||
            "purim_meshulash_sunday" in cal.activeSeasons ||
            "purim_meshulash_shabbat" in cal.activeSeasons

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

    fun statusChipLabel(
        isJerusalem: Boolean,
        meshulashFriday: Boolean = false,
        meshulashSunday: Boolean = false,
        meshulashShabbat: Boolean = false,
    ): String = when {
        meshulashFriday || meshulashSunday || meshulashShabbat -> "Purim Meshulash"
        isJerusalem -> "Shushan Purim"
        else -> "Purim"
    }

    fun occasionLabel(
        isJerusalem: Boolean,
        meshulashFriday: Boolean = false,
        meshulashSunday: Boolean = false,
        meshulashShabbat: Boolean = false,
    ): String = statusChipLabel(
        isJerusalem = isJerusalem,
        meshulashFriday = meshulashFriday,
        meshulashSunday = meshulashSunday,
        meshulashShabbat = meshulashShabbat,
    )
}
