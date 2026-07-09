package com.beardytop.beatzaddik.domain

/**
 * Human-readable "what day is today" labels for the calendar header.
 */
object TodayOccasionLabels {

    data class Occasion(
        val label: String,
        val labelTemplate: String? = null,
        val labelArgs: Map<String, String> = emptyMap(),
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
        pesachLabel(cal)?.let { return it }
        sukkotLabel(cal)?.let { return it }
        if (cal.isChanukah && cal.chanukahDay != null) {
            return Occasion(
                label = "Chanukah — day ${cal.chanukahDay}",
                guideAnchor = "chanukah",
            )
        }
        if (cal.isPurim) {
            return Occasion(label = "Purim", guideAnchor = "purim")
        }
        minorCommemorativeOccasion(cal)?.let { return it }
        if (cal.isLagBaomer) {
            return Occasion(label = "Lag BaOmer", guideAnchor = "lag_baomer")
        }
        if (cal.isYomTovAssurBemelacha && cal.yomTovHolidayName != null) {
            roshHashanaLabel(cal)?.let { return it }
            val name = cal.yomTovHolidayName
            return Occasion(label = name, guideAnchor = guideAnchorFor(name))
        }
        if (MotzeiShabbatWindow.isActive(cal, tomorrowCal, nowMillis)) {
            return Occasion(label = "Motzei Shabbat", guideAnchor = "havdalah")
        }
        if (cal.isShabbat) {
            return Occasion(label = "Shabbat", guideAnchor = "shabbat_overview")
        }
        erevChagLabel(cal)?.let { return it }
        if (cal.isRoshChodesh) {
            return Occasion(label = "Rosh Chodesh", guideAnchor = "rosh_chodesh")
        }
        if (BirkatHachamahRules.isRecitationDay(cal.date)) {
            return Occasion(label = "Birkat Hachamah", guideAnchor = "birkat_hachamah")
        }
        if (cal.isYomHaShoah) return Occasion(label = "Yom HaShoah", guideAnchor = "yom_hashoah")
        if (cal.isYomHaZikaron) return Occasion(label = "Yom HaZikaron", guideAnchor = "yom_hazikaron")
        if (cal.isYomHaAtzmaut) return Occasion(label = "Yom Ha'atzmaut", guideAnchor = "yom_haatzmaut")
        if (cal.isYomYerushalayim) return Occasion(label = "Yom Yerushalayim", guideAnchor = "yom_yerushalayim")
        if (cal.isErevShabbat) {
            return Occasion(label = "Erev Shabbat", guideAnchor = "candle_lighting")
        }
        return null
    }

    private fun erevChagLabel(cal: DayInfo): Occasion? {
        if ("erev_chag" !in cal.activeSeasons) return null
        val chagName = cal.upcomingChagName ?: return null
        val erevChag = "Erev $chagName"
        val label = if (cal.isErevShabbat) "$erevChag · Erev Shabbat" else erevChag
        return Occasion(
            label = label,
            guideAnchor = guideAnchorFor(chagName) ?: "yom_tov",
        )
    }

    fun omerTodayLabel(cal: DayInfo, nusach: EffectiveNusach): String? {
        val day = cal.omerDay ?: return null
        if (!cal.isSefiratHaomer || cal.isLagBaomer) return null
        return OmerCountText.headerLabel(day, nusach)
    }

    private fun pesachLabel(cal: DayInfo): Occasion? {
        val n = festivalDayNumber(cal, HebrewCalendarEngine.NISSAN, 15..22) ?: return null
        if ("pesach" !in cal.activeSeasons && "chol_hamoed_pesach" !in cal.activeSeasons) return null
        val isCholHamoed = "chol_hamoed_pesach" in cal.activeSeasons && !cal.isYomTovAssurBemelacha
        val template = if (isCholHamoed) {
            "{ordinal} day of Pesach (Chol HaMoed)"
        } else {
            "{ordinal} day of Pesach"
        }
        val args = mapOf("ordinal" to ordinal(n))
        val anchor = if (isCholHamoed) "chol_hamoed_pesach" else "pesach"
        return Occasion(
            label = fillOccasionTemplate(template, args),
            labelTemplate = template,
            labelArgs = args,
            guideAnchor = anchor,
        )
    }

    private fun sukkotLabel(cal: DayInfo): Occasion? {
        val n = festivalDayNumber(cal, HebrewCalendarEngine.TISHREI, 15..21) ?: return null
        if ("sukkot" !in cal.activeSeasons && "chol_hamoed_sukkot" !in cal.activeSeasons) return null
        val isCholHamoed = "chol_hamoed_sukkot" in cal.activeSeasons && !cal.isYomTovAssurBemelacha
        val isHoshanaRabbah = n == 7 && isCholHamoed
        val template = when {
            isHoshanaRabbah -> "Hoshana Rabbah"
            isCholHamoed -> "{ordinal} day of Sukkot (Chol HaMoed)"
            else -> "{ordinal} day of Sukkot"
        }
        val args = if (isHoshanaRabbah) {
            emptyMap()
        } else {
            mapOf("ordinal" to ordinal(n))
        }
        val anchor = when {
            isHoshanaRabbah -> "hoshana_raba"
            isCholHamoed -> "chol_hamoed_sukkot"
            else -> "sukkot"
        }
        return Occasion(
            label = if (isHoshanaRabbah) "Hoshana Rabbah" else fillOccasionTemplate(template, args),
            labelTemplate = template.takeIf { !isHoshanaRabbah },
            labelArgs = args,
            guideAnchor = anchor,
            subtitle = if (isHoshanaRabbah) "7th day of Sukkot · Chol HaMoed" else null,
        )
    }

    private fun roshHashanaLabel(cal: DayInfo): Occasion? {
        if (cal.hebrewMonth != HebrewCalendarEngine.TISHREI) return null
        val day = cal.hebrewDay ?: return null
        if (day !in 1..2) return null
        if ("rosh_hashana" !in cal.activeSeasons) return null
        val template = "{ordinal} day of Rosh Hashana"
        val args = mapOf("ordinal" to ordinal(day))
        return Occasion(
            label = fillOccasionTemplate(template, args),
            labelTemplate = template,
            labelArgs = args,
            guideAnchor = "rosh_hashana",
        )
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

    private fun minorCommemorativeOccasion(cal: DayInfo): Occasion? {
        val month = cal.hebrewMonth ?: return null
        val day = cal.hebrewDay ?: return null
        val year = cal.hebrewYear
        if (!TachanunRules.isMinorCommemorativeHoliday(month, day, year)) return null
        return when {
            month == HebrewCalendarEngine.SHEVAT && day == 15 ->
                Occasion(label = "Tu B'Shvat", guideAnchor = "tu_bshvat")
            month == HebrewCalendarEngine.IYAR && day == 14 ->
                Occasion(label = "Pesach Sheni", guideAnchor = "pesach_sheni")
            month == HebrewCalendarEngine.AV && day == 15 ->
                Occasion(label = "Tu B'Av", guideAnchor = "tu_beav")
            month == HebrewCalendarEngine.ADAR && year != null &&
                HebrewCalendarEngine.isJewishLeapYear(year) && day == 14 ->
                Occasion(label = "Purim Katan", guideAnchor = "purim_katan")
            month == HebrewCalendarEngine.ADAR && year != null &&
                HebrewCalendarEngine.isJewishLeapYear(year) && day == 15 ->
                Occasion(label = "Shushan Purim Katan", guideAnchor = "shushan_purim_katan")
            else -> null
        }
    }

    private fun guideAnchorFor(name: String): String? = HolidayGuideAnchors.anchorForLabel(name)

    private fun fillOccasionTemplate(template: String, args: Map<String, String>): String =
        args.entries.fold(template) { acc, (key, value) ->
            acc.replace("{$key}", value)
        }
}
