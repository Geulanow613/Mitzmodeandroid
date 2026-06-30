package com.beardytop.beatzaddik.domain

import com.beardytop.beatzaddik.domain.zmanim.SharedZmanimBuilder
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarIdentifierHebrew
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitWeekday
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDate
import platform.Foundation.NSTimeZone

/**
 * iOS Hebrew-calendar backend. Uses [NSCalendar] with [NSCalendarIdentifierHebrew] for accurate
 * Hebrew date resolution, then delegates all halachic calculations to [HebrewCalendarEngine].
 *
 * Zmanim use [SharedZmanimBuilder] (NOAA-style, matches Android KosherJava angles).
 * Hebrew calendar via [HebrewCalendarEngine] + NSCalendar.
 */
internal class NativeJewishCalendarBackend : JewishCalendarBackend {

    // NSCalendar instance is not thread-safe — only use from main thread (KMP apps do this).
    private val hebrewCal: NSCalendar = NSCalendar(NSCalendarIdentifierHebrew)!!

    // ── Data holder ───────────────────────────────────────────────────────────

    private data class HDate(
        val year: Int,
        val month: Int,   // KosherJava-style: NISSAN=1 … TISHREI=7 … ADAR_II=13
        val day: Int,
        val weekday: Int  // Java Calendar: 1=Sun … 7=Sat
    )

    // ── NSCalendar helpers ────────────────────────────────────────────────────

    private fun getHDate(epochMillis: Long, tzId: String): HDate {
        val tz = NSTimeZone.timeZoneWithName(tzId) ?: NSTimeZone.systemTimeZone
        hebrewCal.timeZone = tz
        val nsDate = NSDate.dateWithTimeIntervalSince1970(epochMillis.toDouble() / 1000.0)
        val units = NSCalendarUnitYear or NSCalendarUnitMonth or NSCalendarUnitDay or NSCalendarUnitWeekday
        val c = hebrewCal.components(units, fromDate = nsDate)
        val year = c.year.toInt()
        val iOSMonth = c.month.toInt()
        val isLeap = HebrewCalendarEngine.isJewishLeapYear(year)
        return HDate(
            year    = year,
            month   = iOSToKJ(iOSMonth, isLeap),
            day     = c.day.toInt(),
            weekday = c.weekday.toInt()
        )
    }

    /**
     * Converts an NSCalendar Hebrew month (1=Tishrei-based) to KosherJava month (1=Nisan-based).
     *
     * Non-leap: iOS 1..6  → KJ 7..12 (+6); iOS 7..12 → KJ 1..6  (-6)
     * Leap:     iOS 1..7  → KJ 7..13 (+6); iOS 8..13 → KJ 1..6  (-7)
     */
    private fun iOSToKJ(iOSMonth: Int, isLeap: Boolean): Int {
        val split = if (isLeap) 7 else 6
        return if (iOSMonth <= split) iOSMonth + 6 else iOSMonth - split
    }

    // ── DayInfo ───────────────────────────────────────────────────────────────

    override fun dayInfoAt(nowEpochMillis: Long, profile: UserProfile): DayInfo {
        val tz = TimeZone.of(profile.timezoneId)
        val local = Instant.fromEpochMilliseconds(nowEpochMillis).toLocalDateTime(tz)
        val date = local.date

        val hd = getHDate(nowEpochMillis, profile.timezoneId)
        val isLeap = HebrewCalendarEngine.isJewishLeapYear(hd.year)

        val isShabbat     = hd.weekday == HebrewCalendarEngine.SATURDAY
        val isErevShabbat = hd.weekday == HebrewCalendarEngine.FRIDAY

        val idx = HebrewCalendarEngine.getYomTovIndex(
            hd.year, hd.month, hd.day, hd.weekday, isLeap,
            inIsrael = profile.isInIsrael
        )
        val isJerusalemPurim = isJerusalemProfile(profile)
        val tomorrowDate = date.plus(1, DateTimeUnit.DAY)
        val tomorrowMillis = LocalDateTime(
            tomorrowDate.year,
            tomorrowDate.monthNumber,
            tomorrowDate.dayOfMonth,
            12,
            0
        ).toInstant(tz).toEpochMilliseconds()
        val tomorrowHd = getHDate(tomorrowMillis, profile.timezoneId)
        val tomorrowLeap = HebrewCalendarEngine.isJewishLeapYear(tomorrowHd.year)
        val tomorrowIdx = HebrewCalendarEngine.getYomTovIndex(
            tomorrowHd.year,
            tomorrowHd.month,
            tomorrowHd.day,
            tomorrowHd.weekday,
            tomorrowLeap,
            inIsrael = profile.isInIsrael
        )
        val yesterdayDate = date.plus(-1, DateTimeUnit.DAY)
        val yesterdayMillis = LocalDateTime(
            yesterdayDate.year,
            yesterdayDate.monthNumber,
            yesterdayDate.dayOfMonth,
            12,
            0
        ).toInstant(tz).toEpochMilliseconds()
        val yesterdayHd = getHDate(yesterdayMillis, profile.timezoneId)
        val yesterdayLeap = HebrewCalendarEngine.isJewishLeapYear(yesterdayHd.year)
        val yesterdayIdx = HebrewCalendarEngine.getYomTovIndex(
            yesterdayHd.year,
            yesterdayHd.month,
            yesterdayHd.day,
            yesterdayHd.weekday,
            yesterdayLeap,
            inIsrael = profile.isInIsrael
        )

        val isYomTov      = HebrewCalendarEngine.isYomTov(idx)
        val isYomTovAssurBemelacha =
            isYomTov && HebrewCalendarEngine.isYomTovAssurBemelacha(idx)
        val yesterdayWasYomTovAssurBemelacha =
            HebrewCalendarEngine.isYomTov(yesterdayIdx) &&
                HebrewCalendarEngine.isYomTovAssurBemelacha(yesterdayIdx)
        val yomTovHolidayName = if (isYomTovAssurBemelacha) holidayName(idx) else null
        val isChanukah    = HebrewCalendarEngine.getDayOfChanukah(hd.year, hd.month, hd.day) != null
        val chanukahDay   = HebrewCalendarEngine.getDayOfChanukah(hd.year, hd.month, hd.day)
        val isPurimMeshulashFriday = isJerusalemPurim &&
            idx == HebrewCalendarEngine.PURIM &&
            tomorrowIdx == HebrewCalendarEngine.SHUSHAN_PURIM &&
            date.dayOfWeek == DayOfWeek.FRIDAY
        val isPurimMeshulashSunday = isJerusalemPurim &&
            date.dayOfWeek == DayOfWeek.SUNDAY &&
            yesterdayIdx == HebrewCalendarEngine.SHUSHAN_PURIM
        val isPurim = if (isJerusalemPurim) {
            idx == HebrewCalendarEngine.SHUSHAN_PURIM || isPurimMeshulashFriday
        } else {
            idx == HebrewCalendarEngine.PURIM
        }
        val omerDay       = HebrewCalendarEngine.getOmerDay(hd.month, hd.day)
        val isLagBaomer   = idx == HebrewCalendarEngine.LAG_BAOMER
        val isSefirah     = omerDay != null && omerDay in 1..49 && !isLagBaomer
        val isRoshChodesh = (hd.day == 1 && hd.month != HebrewCalendarEngine.TISHREI) || hd.day == 30
        val isTaanis      = isTaanisIndex(idx)
        val fastDayIndex = idx.takeIf { PublicFastDayRules.isPublicFast(it) }
        val fastDayName = fastDayIndex?.let { PublicFastDayRules.displayName(it) }
        val upcomingFastDayIndex = tomorrowIdx.takeIf { PublicFastDayRules.isPublicFast(it) }
        val upcomingFastDayName = upcomingFastDayIndex?.let { PublicFastDayRules.displayName(it) }
        val isErevMinorFast = PublicFastDayRules.isErevMinorFast(idx, tomorrowIdx)
        val isErevYomKippur = PublicFastDayRules.isErevYomKippur(idx)
        val isErevTishaBeav = PublicFastDayRules.isErevTishaBeav(
            idx, tomorrowIdx, date.dayOfWeek == DayOfWeek.SATURDAY, hd.month, hd.day,
        )

        val isYomHaShoah       = idx == HebrewCalendarEngine.YOM_HASHOAH
        val isYomHaZikaron     = idx == HebrewCalendarEngine.YOM_HAZIKARON
        val isYomHaAtzmaut     = idx == HebrewCalendarEngine.YOM_HAATZMAUT
        val isYomYerushalayim  = idx == HebrewCalendarEngine.YOM_YERUSHALAYIM
        val isErevChag = !isYomTov &&
            HebrewCalendarEngine.isYomTov(tomorrowIdx) &&
            HebrewCalendarEngine.isYomTovAssurBemelacha(tomorrowIdx)
        val upcomingChagName = if (isErevChag) holidayName(tomorrowIdx) else null
        val upcomingChagYomTovIndex = if (isErevChag) tomorrowIdx else null
        val isErevPurim = !isPurim && tomorrowIdx == HebrewCalendarEngine.PURIM
        val isErevChanukah = !isChanukah &&
            HebrewCalendarEngine.getDayOfChanukah(tomorrowHd.year, tomorrowHd.month, tomorrowHd.day) != null

        val seasons = buildSet {
            if (isSefirah) add("sefirah")
            if (isChanukah) add("chanukah")
            if (isPurim) add("purim")
            if (isPurimMeshulashFriday) add("purim_meshulash_friday")
            if (isPurimMeshulashSunday) add("purim_meshulash_sunday")
            if (isErevChag) add("erev_chag")
            if (isErevPurim) add("erev_purim")
            if (isErevChanukah) add("erev_chanukah")
            if (isRoshChodesh) add("rosh_chodesh")
            if (isYomHaShoah) add("yom_hashoah")
            if (isYomHaZikaron) add("yom_hazikaron")
            if (isYomHaAtzmaut) add("yom_haatzmaut")
            if (isYomYerushalayim) add("yom_yerushalayim")
            if (idx == HebrewCalendarEngine.EREV_PESACH) add("erev_pesach")
            if (idx == HebrewCalendarEngine.CHOL_HAMOED_PESACH) add("chol_hamoed_pesach")
            if (idx == HebrewCalendarEngine.CHOL_HAMOED_SUCCOS || idx == HebrewCalendarEngine.HOSHANA_RABBA) {
                add("chol_hamoed_sukkot")
            }
            if (idx == HebrewCalendarEngine.SUCCOS || idx == HebrewCalendarEngine.CHOL_HAMOED_SUCCOS || idx == HebrewCalendarEngine.HOSHANA_RABBA) {
                add("sukkot")
            }
            if (idx == HebrewCalendarEngine.SHEMINI_ATZERES) add("shemini_atzeret")
            if (idx == HebrewCalendarEngine.SIMCHAS_TORAH) add("simchat_torah")
            if (idx == HebrewCalendarEngine.PESACH || idx == HebrewCalendarEngine.CHOL_HAMOED_PESACH) add("pesach")
            if (idx == HebrewCalendarEngine.SHAVUOS) add("shavuot")
            if (idx == HebrewCalendarEngine.SUCCOS || idx == HebrewCalendarEngine.CHOL_HAMOED_SUCCOS) add("sukkot")
            if (idx == HebrewCalendarEngine.ROSH_HASHANA) add("rosh_hashana")
            if (idx == HebrewCalendarEngine.YOM_KIPPUR) add("yom_kippur")
            if (isTaanis) add("fast_day")
            if (isErevMinorFast) add("erev_minor_fast")
            if (isErevYomKippur) add("erev_yom_kippur")
            if (isErevTishaBeav) add("erev_tisha_beav")
        }

        val zmanim       = SharedZmanimBuilder.build(nowEpochMillis, profile)
        val period       = ZmanPeriodLogic.activePeriodContext(nowEpochMillis, profile, zmanim)

        val hebrewLabel = "${hd.day} ${monthName(hd.month, isLeap)} ${hd.year}"

        val chips = buildList {
            add(date.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() })
            if (isErevShabbat) add("Erev Shabbat")
            if (isShabbat) add("Shabbat")
            if (isYomTov) add(holidayName(idx))
            if (isRoshChodesh) add("Rosh Chodesh")
            if (isSefirah && omerDay != null) {
                add(OmerCountText.statusChipLabel(omerDay, profile.effectiveNusach()))
            }
            if (isChanukah && chanukahDay != null) add("Chanukah day $chanukahDay")
            if (isPurim) add("Purim")
            if (isYomHaShoah) add("Yom HaShoah")
            if (isYomHaZikaron) add("Yom HaZikaron")
            if (isYomHaAtzmaut) add("Yom Ha'atzmaut")
            if (isYomYerushalayim) add("Yom Yerushalayim")
            if (isTaanis) add("Fast day")
        }

        val parsha = if (isShabbat)
            HebrewCalendarEngine.getParshaKey(hd.year, hd.month, hd.day, hd.weekday, profile.isInIsrael)
                ?.let { key -> ParshaData.forKey(key)?.displayName ?: key.replace('_', ' ') }
        else null

        val upcomingShabbatParsha = HebrewCalendarEngine.getUpcomingParshaKey(
            hd.year, hd.month, hd.day, hd.weekday, profile.isInIsrael
        )

        return DayInfo(
            date                  = date,
            civilLabel            = ZmanimFormatter.formatCivilDate(date),
            hebrewLabel           = hebrewLabel,
            parsha                = parsha,
            statusChips           = chips,
            isShabbat             = isShabbat,
            isErevShabbat         = isErevShabbat,
            isYomTov              = isYomTov,
            isYomTovAssurBemelacha = isYomTovAssurBemelacha,
            yesterdayWasYomTovAssurBemelacha = yesterdayWasYomTovAssurBemelacha,
            yomTovHolidayName     = yomTovHolidayName,
            isShabbatOrYomTov     = isShabbat || isYomTov,
            activeTimeOfDay       = period.timeOfDay,
            activePeriodLabel     = period.activeTitle,
            activePeriodHint      = period.activeSummary,
            inactivePeriodHint    = period.laterSummary,
            omerDay               = omerDay,
            isSefiratHaomer       = isSefirah,
            isLagBaomer           = isLagBaomer,
            isChanukah            = isChanukah,
            chanukahDay           = chanukahDay,
            isPurim               = isPurim,
            isRoshChodesh         = isRoshChodesh,
            isYomHaShoah          = isYomHaShoah,
            isYomHaZikaron        = isYomHaZikaron,
            isYomHaAtzmaut        = isYomHaAtzmaut,
            isYomYerushalayim     = isYomYerushalayim,
            activeSeasons         = seasons,
            hebrewMonth           = hd.month,
            hebrewDay             = hd.day,
            hebrewYear            = hd.year,
            zmanim                = zmanim,
            upcomingShabbatParsha = upcomingShabbatParsha,
            upcomingChagName      = upcomingChagName,
            upcomingChagYomTovIndex = upcomingChagYomTovIndex,
            fastDayIndex          = fastDayIndex,
            fastDayName           = fastDayName,
            upcomingFastDayIndex  = upcomingFastDayIndex,
            upcomingFastDayName   = upcomingFastDayName,
        )
    }

    // ── Upcoming holidays ────────────────────────────────────────────────────

    override fun upcomingHolidays(from: LocalDate, profile: UserProfile): List<UpcomingHoliday> {
        val results  = mutableListOf<UpcomingHoliday>()
        var nextShabbat:      UpcomingHoliday? = null
        var nextYomTov:       UpcomingHoliday? = null
        var nextChanukah:     UpcomingHoliday? = null
        var nextPurim:        UpcomingHoliday? = null
        var nextRoshChodesh:  UpcomingHoliday? = null
        var nextMinorHoliday: UpcomingHoliday? = null

        // We iterate by adding days to the civil date to get epochMillis, then query NSCalendar.
        // Use noon local time to avoid day-boundary issues with Hebrew date transitions.
        val baseMillis = LocalDateTime(from.year, from.monthNumber, from.dayOfMonth, 12, 0)
            .toInstant(TimeZone.of(profile.timezoneId))
            .toEpochMilliseconds()

        for (i in 0..60) {
            if (nextShabbat != null && nextYomTov != null && nextChanukah != null &&
                nextPurim != null && nextRoshChodesh != null && nextMinorHoliday != null) break

            val dayMillis = baseMillis + i * 86_400_000L
            val hd = getHDate(dayMillis, profile.timezoneId)
            val d = from.plus(i, DateTimeUnit.DAY)
            val isLeap = HebrewCalendarEngine.isJewishLeapYear(hd.year)

            if (nextShabbat == null && d.dayOfWeek == DayOfWeek.FRIDAY) {
                nextShabbat = UpcomingHoliday("Shabbat", i, "Candles, Kiddush, rest from electronics")
            }

            val idx = HebrewCalendarEngine.getYomTovIndex(
                hd.year, hd.month, hd.day, hd.weekday, isLeap, profile.isInIsrael
            )

            if (nextYomTov == null && HebrewCalendarEngine.isYomTov(idx) &&
                HebrewCalendarEngine.isYomTovAssurBemelacha(idx) && i > 0) {
                nextYomTov = UpcomingHoliday(holidayName(idx), i, prepHint(idx))
            }

            val isChanukah = HebrewCalendarEngine.getDayOfChanukah(hd.year, hd.month, hd.day) != null
            if (nextChanukah == null && isChanukah && i > 0) {
                nextChanukah = UpcomingHoliday("Chanukah", i, "Light menorah each night")
            }

            if (nextPurim == null && idx == HebrewCalendarEngine.PURIM && i > 0) {
                nextPurim = UpcomingHoliday("Purim", i, "Megillah, matanot, mishloach manot")
            }

            val isRoshChodesh = (hd.day == 1 && hd.month != HebrewCalendarEngine.TISHREI) || hd.day == 30
            if (nextRoshChodesh == null && isRoshChodesh && i > 0) {
                nextRoshChodesh = UpcomingHoliday("Rosh Chodesh", i, "Yaaleh V'yavo, Hallel")
            }

            if (nextMinorHoliday == null && i > 0) {
                nextMinorHoliday = when (idx) {
                    HebrewCalendarEngine.TU_BESHVAT         -> UpcomingHoliday("Tu B'Shvat", i, "Minor holiday — New Year for Trees")
                    HebrewCalendarEngine.PESACH_SHENI        -> UpcomingHoliday("Pesach Sheni", i, "Minor holiday — Second Passover")
                    HebrewCalendarEngine.LAG_BAOMER          -> UpcomingHoliday("Lag BaOmer", i, "Minor holiday — 33rd day of the Omer")
                    HebrewCalendarEngine.TU_BEAV             -> UpcomingHoliday("Tu B'Av", i, "Minor holiday — celebration of joy")
                    HebrewCalendarEngine.TISHA_BEAV          -> UpcomingHoliday("Tisha B'Av", i, "Fast day — mourning the Temple")
                    HebrewCalendarEngine.FAST_OF_GEDALYAH    -> UpcomingHoliday("Fast of Gedaliah", i, "Fast day")
                    HebrewCalendarEngine.TENTH_OF_TEVES      -> UpcomingHoliday("Fast of 10 Tevet", i, "Fast day")
                    HebrewCalendarEngine.FAST_OF_ESTHER      -> UpcomingHoliday("Fast of Esther", i, "Fast day — before Purim")
                    HebrewCalendarEngine.SEVENTEEN_OF_TAMMUZ -> UpcomingHoliday("Fast of 17 Tammuz", i, "Fast day")
                    HebrewCalendarEngine.YOM_HASHOAH         -> UpcomingHoliday("Yom HaShoah", i, "Holocaust Remembrance Day")
                    HebrewCalendarEngine.YOM_HAZIKARON       -> UpcomingHoliday("Yom HaZikaron", i, "Israeli Fallen Soldiers Memorial Day")
                    HebrewCalendarEngine.YOM_HAATZMAUT       -> UpcomingHoliday("Yom Ha'atzmaut", i, "Israeli Independence Day — customs vary by community")
                    HebrewCalendarEngine.YOM_YERUSHALAYIM    -> UpcomingHoliday("Yom Yerushalayim", i, "Jerusalem Day — customs vary by community")
                    else                                     -> null
                }
            }
        }

        listOfNotNull(nextShabbat, nextYomTov, nextChanukah, nextPurim, nextRoshChodesh, nextMinorHoliday)
            .forEach { results += it }
        return results.sortedBy { it.daysAway }.take(8)
    }

    // ── Electronics rest ─────────────────────────────────────────────────────

    override fun electronicsRestPeriod(nowEpochMillis: Long, profile: UserProfile): ElectronicsRestPeriod? {
        val tz = TimeZone.of(profile.timezoneId)
        val local = Instant.fromEpochMilliseconds(nowEpochMillis).toLocalDateTime(tz)
        val today = dayInfoAt(nowEpochMillis, profile)

        if (today.isYomTovAssurBemelacha) {
            val yesterday = today.date.plus(-1, DateTimeUnit.DAY)
            val yZmanim = SharedZmanimBuilder.build(noonMillis(yesterday, tz), profile) ?: return null
            val start = yZmanim.sunsetMillis ?: today.zmanim?.sunriseMillis ?: return null
            val end = today.zmanim?.tzeitMillis ?: today.zmanim?.sunsetMillis ?: return null
            if (nowEpochMillis < start || nowEpochMillis >= end) return null
            return ElectronicsRestPeriod(
                kind = RestKind.YOM_TOV,
                title = today.yomTovHolidayName ?: "Yom Tov",
                message = yomTovMessage(today.yomTovHolidayName ?: "Yom Tov"),
                locationLabel = profile.locationLabel,
                endsAtEpochMillis = end,
            )
        }

        var friday = today.date
        while (friday.dayOfWeek != DayOfWeek.FRIDAY) {
            friday = friday.plus(-1, DateTimeUnit.DAY)
        }
        val saturday = friday.plus(1, DateTimeUnit.DAY)
        val friZmanim = SharedZmanimBuilder.build(noonMillis(friday, tz), profile) ?: return null
        val satZmanim = SharedZmanimBuilder.build(noonMillis(saturday, tz), profile) ?: return null
        val start = (friZmanim.sunsetMillis ?: return null) - SHABBAT_SUNSET_LEAD_MS
        val end = satZmanim.tzeitMillis ?: satZmanim.sunsetMillis ?: return null
        if (nowEpochMillis < start || nowEpochMillis >= end) return null

        val isErev = local.dayOfWeek == DayOfWeek.FRIDAY
        return shabbatRest(profile, isErev = isErev, endsAt = end)
    }

    private fun noonMillis(date: LocalDate, tz: TimeZone): Long =
        LocalDateTime(date.year, date.monthNumber, date.dayOfMonth, 12, 0)
            .toInstant(tz)
            .toEpochMilliseconds()

    private companion object {
        const val SHABBAT_SUNSET_LEAD_MS = 60_000L
    }

    private fun shabbatRest(profile: UserProfile, isErev: Boolean, endsAt: Long): ElectronicsRestPeriod =
        ElectronicsRestPeriod(
            kind               = RestKind.SHABBAT,
            title              = if (isErev) "Shabbat is beginning" else "Shabbat Shalom",
            message            = shabbatMessage(),
            locationLabel      = profile.locationLabel,
            endsAtEpochMillis  = endsAt
        )

    // ── Helper / display functions ────────────────────────────────────────────

    private fun isTaanisIndex(idx: Int): Boolean = idx in setOf(
        HebrewCalendarEngine.SEVENTEEN_OF_TAMMUZ,
        HebrewCalendarEngine.TISHA_BEAV,
        HebrewCalendarEngine.YOM_KIPPUR,
        HebrewCalendarEngine.FAST_OF_GEDALYAH,
        HebrewCalendarEngine.TENTH_OF_TEVES,
        HebrewCalendarEngine.FAST_OF_ESTHER
    )

    private fun monthName(kjMonth: Int, isLeap: Boolean): String = when (kjMonth) {
        HebrewCalendarEngine.NISSAN   -> "Nissan"
        HebrewCalendarEngine.IYAR     -> "Iyar"
        HebrewCalendarEngine.SIVAN    -> "Sivan"
        HebrewCalendarEngine.TAMMUZ   -> "Tammuz"
        HebrewCalendarEngine.AV       -> "Av"
        HebrewCalendarEngine.ELUL     -> "Elul"
        HebrewCalendarEngine.TISHREI  -> "Tishrei"
        HebrewCalendarEngine.CHESHVAN -> "Cheshvan"
        HebrewCalendarEngine.KISLEV   -> "Kislev"
        HebrewCalendarEngine.TEVET    -> "Tevet"
        HebrewCalendarEngine.SHEVAT   -> "Shevat"
        HebrewCalendarEngine.ADAR     -> if (isLeap) "Adar I" else "Adar"
        HebrewCalendarEngine.ADAR_II  -> "Adar II"
        else                          -> "Month $kjMonth"
    }

    private fun holidayName(idx: Int): String = when (idx) {
        HebrewCalendarEngine.EREV_PESACH         -> "Erev Pesach"
        HebrewCalendarEngine.PESACH              -> "Pesach"
        HebrewCalendarEngine.CHOL_HAMOED_PESACH  -> "Chol HaMoed Pesach"
        HebrewCalendarEngine.SHAVUOS             -> "Shavuot"
        HebrewCalendarEngine.ROSH_HASHANA        -> "Rosh Hashana"
        HebrewCalendarEngine.FAST_OF_GEDALYAH    -> "Fast of Gedaliah"
        HebrewCalendarEngine.YOM_KIPPUR          -> "Yom Kippur"
        HebrewCalendarEngine.SUCCOS              -> "Sukkot"
        HebrewCalendarEngine.CHOL_HAMOED_SUCCOS  -> "Chol HaMoed Sukkot"
        HebrewCalendarEngine.HOSHANA_RABBA       -> "Hoshana Raba"
        HebrewCalendarEngine.SHEMINI_ATZERES     -> "Shemini Atzeret"
        HebrewCalendarEngine.SIMCHAS_TORAH       -> "Simchat Torah"
        HebrewCalendarEngine.CHANUKAH            -> "Chanukah"
        HebrewCalendarEngine.TENTH_OF_TEVES      -> "Fast of 10 Tevet"
        HebrewCalendarEngine.TU_BESHVAT          -> "Tu B'Shvat"
        HebrewCalendarEngine.FAST_OF_ESTHER      -> "Fast of Esther"
        HebrewCalendarEngine.PURIM               -> "Purim"
        HebrewCalendarEngine.PURIM_KATAN         -> "Purim Katan"
        HebrewCalendarEngine.PESACH_SHENI        -> "Pesach Sheni"
        HebrewCalendarEngine.LAG_BAOMER          -> "Lag BaOmer"
        HebrewCalendarEngine.SEVENTEEN_OF_TAMMUZ -> "Fast of 17 Tammuz"
        HebrewCalendarEngine.TISHA_BEAV          -> "Tisha B'Av"
        HebrewCalendarEngine.TU_BEAV             -> "Tu B'Av"
        HebrewCalendarEngine.YOM_HASHOAH         -> "Yom HaShoah"
        HebrewCalendarEngine.YOM_HAZIKARON       -> "Yom HaZikaron"
        HebrewCalendarEngine.YOM_HAATZMAUT       -> "Yom Ha'atzmaut"
        HebrewCalendarEngine.YOM_YERUSHALAYIM    -> "Yom Yerushalayim"
        else                                     -> "Yom Tov"
    }

    private fun prepHint(idx: Int): String = when (idx) {
        HebrewCalendarEngine.ROSH_HASHANA       -> "Yom Tov — Shofar, teshuvah, sweet foods"
        HebrewCalendarEngine.YOM_KIPPUR         -> "Yom Tov — Fast, prayer, atonement"
        HebrewCalendarEngine.SUCCOS             -> "Yom Tov — Lulav & etrog, sukkah"
        HebrewCalendarEngine.SHEMINI_ATZERES    -> "Yom Tov — Shemini Atzeret"
        HebrewCalendarEngine.SIMCHAS_TORAH      -> "Yom Tov — Simchat Torah, hakafot"
        HebrewCalendarEngine.PESACH             -> "Yom Tov — Seder night(s), matzah, no chametz"
        HebrewCalendarEngine.SHAVUOS            -> "Yom Tov — Matan Torah, learning, dairy foods"
        HebrewCalendarEngine.CHOL_HAMOED_PESACH -> "Chol HaMoed Pesach — intermediate festival day"
        HebrewCalendarEngine.CHOL_HAMOED_SUCCOS -> "Chol HaMoed Sukkot — intermediate festival day"
        HebrewCalendarEngine.HOSHANA_RABBA      -> "Chol HaMoed Sukkot — last intermediate day"
        else                                    -> "Special day"
    }

    private fun formatCivil(date: LocalDate): String = ZmanimFormatter.formatCivilDate(date)
}

private fun isJerusalemProfile(profile: UserProfile): Boolean {
    if (profile.manualCityId in setOf("jlm", "jerusalem")) return true
    return profile.locationLabel?.contains("jerusalem", ignoreCase = true) == true
}
