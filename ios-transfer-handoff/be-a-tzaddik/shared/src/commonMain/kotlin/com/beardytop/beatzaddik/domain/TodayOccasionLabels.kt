package com.beardytop.beatzaddik.domain

/**
 * Human-readable "what day is today" labels for the calendar header.
 */
object TodayOccasionLabels {

    data class Occasion(
        val label: String,
        val guideAnchor: String? = null,
        val subtitle: String? = null,
    )

    fun primary(cal: DayInfo, nowMillis: Long, tomorrowCal: DayInfo): Occasion? {
        cal.fastDayName?.let { fastName ->
            return Occasion(
                label = fastName,
                guideAnchor = guideAnchorFor(fastName),
                subtitle = PublicFastDayRules.deferredFastSubtitle(cal),
            )
        }
        pesachLabel(cal)?.let { (label, anchor) -> return Occasion(label, anchor) }
        sukkotLabel(cal)?.let { (label, anchor) -> return Occasion(label, anchor) }
        if (cal.isChanukah && cal.chanukahDay != null) {
            return Occasion("Chanukah — day ${cal.chanukahDay}", "chanukah")
        }
        if (cal.isPurim) {
            return Occasion("Purim", "purim")
        }
        if (cal.isLagBaomer) {
            return Occasion("Lag BaOmer", "lag_baomer")
        }
        if (cal.isYomTovAssurBemelacha && cal.yomTovHolidayName != null) {
            val name = cal.yomTovHolidayName
            roshHashanaLabel(cal)?.let { return Occasion(it, "rosh_hashana") }
            return Occasion(name, guideAnchorFor(name))
        }
        if (MotzeiShabbatWindow.isActive(cal, tomorrowCal, nowMillis)) {
            return Occasion("Motzei Shabbat", "havdalah")
        }
        if (cal.isShabbat) {
            return Occasion("Shabbat", "shabbat_overview")
        }
        if (cal.isRoshChodesh) {
            return Occasion("Rosh Chodesh", "rosh_chodesh")
        }
        if (cal.isYomHaShoah) return Occasion("Yom HaShoah", "yom_hashoah")
        if (cal.isYomHaZikaron) return Occasion("Yom HaZikaron", "yom_hazikaron")
        if (cal.isYomHaAtzmaut) return Occasion("Yom Ha'atzmaut", "yom_haatzmaut")
        if (cal.isYomYerushalayim) return Occasion("Yom Yerushalayim", "yom_yerushalayim")
        if (cal.isErevShabbat) {
            return Occasion("Erev Shabbat", "candle_lighting")
        }
        return null
    }

    fun omerTodayLabel(cal: DayInfo): String? {
        val day = cal.omerDay ?: return null
        if (!cal.isSefiratHaomer || cal.isLagBaomer) return null
        return OmerCountText.headerLabel(day)
    }

    private fun pesachLabel(cal: DayInfo): Pair<String, String>? {
        val n = festivalDayNumber(cal, HebrewCalendarEngine.NISSAN, 15..22) ?: return null
        if ("pesach" !in cal.activeSeasons && "chol_hamoed_pesach" !in cal.activeSeasons) return null
        val base = "${ordinal(n)} day of Pesach"
        val isCholHamoed = "chol_hamoed_pesach" in cal.activeSeasons && !cal.isYomTovAssurBemelacha
        val label = if (isCholHamoed) "$base (Chol HaMoed)" else base
        val anchor = if (isCholHamoed) "chol_hamoed_pesach" else "pesach"
        return label to anchor
    }

    private fun sukkotLabel(cal: DayInfo): Pair<String, String>? {
        val n = festivalDayNumber(cal, HebrewCalendarEngine.TISHREI, 15..21) ?: return null
        if ("sukkot" !in cal.activeSeasons && "chol_hamoed_sukkot" !in cal.activeSeasons) return null
        val base = "${ordinal(n)} day of Sukkot"
        val isCholHamoed = "chol_hamoed_sukkot" in cal.activeSeasons && !cal.isYomTovAssurBemelacha
        val label = if (isCholHamoed) "$base (Chol HaMoed)" else base
        val anchor = when {
            n == 7 && isCholHamoed -> "hoshana_raba"
            isCholHamoed -> "chol_hamoed_sukkot"
            else -> "sukkot"
        }
        return label to anchor
    }

    private fun roshHashanaLabel(cal: DayInfo): String? {
        if (cal.hebrewMonth != HebrewCalendarEngine.TISHREI) return null
        val day = cal.hebrewDay ?: return null
        if (day !in 1..2) return null
        if ("rosh_hashana" !in cal.activeSeasons) return null
        return when (day) {
            1 -> "1st day of Rosh Hashana"
            2 -> "2nd day of Rosh Hashana"
            else -> "Rosh Hashana"
        }
    }

    private fun festivalDayNumber(cal: DayInfo, month: Int, range: IntRange): Int? {
        if (cal.hebrewMonth != month) return null
        val day = cal.hebrewDay ?: return null
        if (day !in range) return null
        return day - range.first + 1
    }

    private fun ordinal(n: Int): String = when {
        n % 100 in 11..13 -> "${n}th"
        n % 10 == 1 -> "${n}st"
        n % 10 == 2 -> "${n}nd"
        n % 10 == 3 -> "${n}rd"
        else -> "${n}th"
    }

    private fun guideAnchorFor(name: String): String? = HolidayGuideAnchors.anchorForLabel(name)
}
