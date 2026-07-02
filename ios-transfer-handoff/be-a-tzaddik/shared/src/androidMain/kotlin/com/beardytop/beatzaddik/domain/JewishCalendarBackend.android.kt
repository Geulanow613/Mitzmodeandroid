package com.beardytop.beatzaddik.domain

import com.kosherjava.zmanim.ComplexZmanimCalendar
import com.kosherjava.zmanim.hebrewcalendar.JewishCalendar
import com.kosherjava.zmanim.hebrewcalendar.JewishDate
import com.kosherjava.zmanim.util.GeoLocation
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDate as JavaLocalDate
import java.util.Calendar
import java.util.TimeZone as JavaTimeZone

actual fun createJewishCalendarBackend(): JewishCalendarBackend = ZmanimJewishCalendarBackend()

private class ZmanimJewishCalendarBackend : JewishCalendarBackend {

    override fun dayInfoAt(nowEpochMillis: Long, profile: UserProfile): DayInfo {
        val geo = geoFor(profile)
        val tz = JavaTimeZone.getTimeZone(profile.timezoneId)
        val now = Calendar.getInstance(tz).apply { timeInMillis = nowEpochMillis }
        val date = LocalDate(
            now.get(Calendar.YEAR),
            now.get(Calendar.MONTH) + 1,
            now.get(Calendar.DAY_OF_MONTH)
        )
        val jc = JewishCalendar(now.time).apply {
            inIsrael = profile.isInIsrael
            isUseModernHolidays = true
        }
        val zc = geo?.let { zmanimCalendar(it, now) }

        val isShabbat = date.dayOfWeek == DayOfWeek.SATURDAY
        val isErevShabbat = date.dayOfWeek == DayOfWeek.FRIDAY
        val isYomTov = jc.isYomTov
        val omerDay = jc.dayOfOmer.takeIf { it > 0 }
        val isLagBaomer = jc.yomTovIndex == JewishCalendar.LAG_BAOMER
        val isChanukah = jc.isChanukah
        val chanukahDay = jc.dayOfChanukah.takeIf { it > 0 }
        val isJerusalemPurim = isJerusalemProfile(profile)
        val jcTomorrow = JewishCalendar((now.clone() as Calendar).apply {
            add(Calendar.DAY_OF_MONTH, 1)
        }.time).apply {
            inIsrael = profile.isInIsrael
            isUseModernHolidays = true
        }
        val jcYesterday = JewishCalendar((now.clone() as Calendar).apply {
            add(Calendar.DAY_OF_MONTH, -1)
        }.time).apply {
            inIsrael = profile.isInIsrael
            isUseModernHolidays = true
        }
        val isYomTovAssurBemelacha =
            jc.isYomTov && HebrewCalendarEngine.isYomTovAssurBemelacha(jc.yomTovIndex)
        val yesterdayWasYomTovAssurBemelacha =
            jcYesterday.isYomTov &&
                HebrewCalendarEngine.isYomTovAssurBemelacha(jcYesterday.yomTovIndex)
        val yomTovHolidayName = if (isYomTovAssurBemelacha) holidayName(jc) else null
        val isPurimMeshulashFriday = isJerusalemPurim &&
            jc.yomTovIndex == JewishCalendar.PURIM &&
            jcTomorrow.yomTovIndex == JewishCalendar.SHUSHAN_PURIM &&
            date.dayOfWeek == DayOfWeek.FRIDAY
        val isPurimMeshulashSunday = isJerusalemPurim &&
            date.dayOfWeek == DayOfWeek.SUNDAY &&
            jcYesterday.yomTovIndex == JewishCalendar.SHUSHAN_PURIM
        val isPurim = if (isJerusalemPurim) {
            jc.yomTovIndex == JewishCalendar.SHUSHAN_PURIM || isPurimMeshulashFriday
        } else {
            jc.yomTovIndex == JewishCalendar.PURIM
        }
        val isRoshChodesh = jc.isRoshChodesh
        val isYomHaShoah = jc.yomTovIndex == JewishCalendar.YOM_HASHOAH
        val isYomHaZikaron = jc.yomTovIndex == JewishCalendar.YOM_HAZIKARON
        val isYomHaAtzmaut = jc.yomTovIndex == JewishCalendar.YOM_HAATZMAUT
        val isYomYerushalayim = jc.yomTovIndex == JewishCalendar.YOM_YERUSHALAYIM
        val isSefirah = omerDay != null && omerDay in 1..49 && !isLagBaomer
        val isErevChag = !isYomTovAssurBemelacha && !jc.isYomTov &&
            jcTomorrow.isYomTov &&
            HebrewCalendarEngine.isYomTovAssurBemelacha(jcTomorrow.yomTovIndex)
        val todayIdx = jc.yomTovIndex
        val tomorrowIdx = jcTomorrow.yomTovIndex
        val upcomingChagName = if (isErevChag) {
            UpcomingHolidayNames.erevUpcomingDisplayName(
                todayYomTovIndex = todayIdx,
                tomorrowYomTovIndex = tomorrowIdx,
                tomorrowHebrewMonth = jcTomorrow.jewishMonth,
                tomorrowHebrewDay = jcTomorrow.jewishDayOfMonth,
                inIsrael = profile.isInIsrael,
                defaultHolidayName = holidayName(jcTomorrow),
            )
        } else {
            null
        }
        val upcomingChagYomTovIndex = if (isErevChag) jcTomorrow.yomTovIndex else null
        val isErevPurim = !isPurim && jcTomorrow.isPurim
        val isErevChanukah = !isChanukah && jcTomorrow.isChanukah
        val fastDayIndex = PublicFastDayRules.resolveFastDayIndex(
            todayIdx = todayIdx,
            tomorrowIdx = tomorrowIdx,
            isTaanis = jc.isTaanis,
        )
        val fastDayName = fastDayIndex?.let { PublicFastDayRules.displayName(it) }
        val upcomingFastDayIndex = tomorrowIdx.takeIf { PublicFastDayRules.isPublicFast(it) }
        val upcomingFastDayName = upcomingFastDayIndex?.let { PublicFastDayRules.displayName(it) }
        val isErevMinorFast = PublicFastDayRules.isErevMinorFast(todayIdx, tomorrowIdx)
        val isErevYomKippur = PublicFastDayRules.isErevYomKippur(todayIdx)
        val isErevTishaBeav = PublicFastDayRules.isErevTishaBeav(
            todayIdx,
            tomorrowIdx,
            isShabbat,
            jc.jewishMonth,
            jc.jewishDayOfMonth,
        )

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
            if (jc.yomTovIndex == JewishCalendar.EREV_PESACH) add("erev_pesach")
            if (jc.yomTovIndex == JewishCalendar.CHOL_HAMOED_PESACH) add("chol_hamoed_pesach")
            if (jc.yomTovIndex == JewishCalendar.CHOL_HAMOED_SUCCOS || jc.yomTovIndex == JewishCalendar.HOSHANA_RABBA) {
                add("chol_hamoed_sukkot")
            }
            if (jc.yomTovIndex == JewishCalendar.SUCCOS || jc.yomTovIndex == JewishCalendar.CHOL_HAMOED_SUCCOS || jc.yomTovIndex == JewishCalendar.HOSHANA_RABBA) {
                add("sukkot")
            }
            if (jc.yomTovIndex == JewishCalendar.SHEMINI_ATZERES) add("shemini_atzeret")
            if (jc.yomTovIndex == JewishCalendar.SIMCHAS_TORAH) add("simchat_torah")
            if (jc.yomTovIndex == JewishCalendar.PESACH || jc.yomTovIndex == JewishCalendar.CHOL_HAMOED_PESACH) {
                add("pesach")
            }
            if (jc.yomTovIndex == JewishCalendar.SHAVUOS) add("shavuot")
            if (jc.yomTovIndex == JewishCalendar.SUCCOS || jc.yomTovIndex == JewishCalendar.CHOL_HAMOED_SUCCOS) {
                add("sukkot")
            }
            if (jc.yomTovIndex == JewishCalendar.ROSH_HASHANA) add("rosh_hashana")
            if (jc.yomTovIndex == JewishCalendar.YOM_KIPPUR) add("yom_kippur")
            if (jc.isTaanis) add("fast_day")
            if (isErevMinorFast) add("erev_minor_fast")
            if (isErevYomKippur) add("erev_yom_kippur")
            if (isErevTishaBeav) add("erev_tisha_beav")
        }

        val zmanim = if (geo != null && zc != null) {
            buildZmanimSnapshot(zc, geo, now, profile.timezoneId)
        } else {
            null
        }
        val period = ZmanPeriodLogic.activePeriodContext(now.timeInMillis, profile, zmanim)

        val hebrew = "${jc.jewishDayOfMonth} ${monthName(jc.jewishMonth)} ${jc.jewishYear}"
        val chips = buildList {
            add(date.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() })
            if (isErevShabbat) add("Erev Shabbat")
            if (isShabbat) add("Shabbat")
            if (isYomTov) add(holidayName(jc))
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
            if (jc.isTaanis) add("Fast day")
        }

        val parsha = runCatching {
            jc.parshah?.name?.replace('_', ' ')?.trim()
        }.getOrNull()?.takeIf { it.isNotBlank() && it != "NONE" }

        // Find the upcoming Shabbat and get its parsha (accounts for Israel/Diaspora divergence).
        val upcomingShabbatParsha = runCatching {
            val dowToday = now.get(Calendar.DAY_OF_WEEK) // Sunday=1 … Saturday=7
            val daysUntilSaturday = (Calendar.SATURDAY - dowToday + 7) % 7
            // If today is Saturday (daysUntilSaturday == 0) we want next Shabbat in 7 days,
            // because the app shows the Shabbat screen today anyway.
            val daysForward = if (daysUntilSaturday == 0) 7 else daysUntilSaturday
            val shabbatCal = (now.clone() as Calendar).apply { add(Calendar.DAY_OF_MONTH, daysForward) }
            val jcShabbat = JewishCalendar(shabbatCal.time).apply {
                inIsrael = profile.isInIsrael
                isUseModernHolidays = true
            }
            val rawName = jcShabbat.parshah?.name
            if (rawName == null || rawName == "NONE") null else rawName
        }.getOrNull()

        return DayInfo(
            date = date,
            civilLabel = ZmanimFormatter.formatCivilDate(date),
            hebrewLabel = hebrew,
            parsha = parsha,
            statusChips = chips,
            isShabbat = isShabbat,
            isErevShabbat = isErevShabbat,
            isYomTov = isYomTov,
            isYomTovAssurBemelacha = isYomTovAssurBemelacha,
            yesterdayWasYomTovAssurBemelacha = yesterdayWasYomTovAssurBemelacha,
            yomTovHolidayName = yomTovHolidayName,
            isShabbatOrYomTov = isShabbat || isYomTov,
            activeTimeOfDay = period.timeOfDay,
            activePeriodLabel = period.activeTitle,
            activePeriodHint = period.activeSummary,
            inactivePeriodHint = period.laterSummary,
            omerDay = omerDay,
            isSefiratHaomer = isSefirah,
            isLagBaomer = isLagBaomer,
            isChanukah = isChanukah,
            chanukahDay = chanukahDay,
            isPurim = isPurim,
            isRoshChodesh = isRoshChodesh,
            isYomHaShoah = isYomHaShoah,
            isYomHaZikaron = isYomHaZikaron,
            isYomHaAtzmaut = isYomHaAtzmaut,
            isYomYerushalayim = isYomYerushalayim,
            activeSeasons = seasons,
            hebrewMonth = jc.jewishMonth,
            hebrewDay = jc.jewishDayOfMonth,
            hebrewYear = jc.jewishYear,
            zmanim = zmanim,
            upcomingShabbatParsha = upcomingShabbatParsha,
            upcomingChagName = upcomingChagName,
            upcomingChagYomTovIndex = upcomingChagYomTovIndex,
            fastDayIndex = fastDayIndex,
            fastDayName = fastDayName,
            upcomingFastDayIndex = upcomingFastDayIndex,
            upcomingFastDayName = upcomingFastDayName,
        )
    }

    private fun buildZmanimSnapshot(
        zc: ComplexZmanimCalendar,
        geo: GeoLocation,
        now: Calendar,
        timezoneId: String
    ): ZmanimSnapshot {
        val chatzos = zc.chatzos?.time ?: zc.getChatzos()?.time
        val tomorrow = (now.clone() as Calendar).apply { add(Calendar.DAY_OF_MONTH, 1) }
        val zcTomorrow = zmanimCalendar(geo, tomorrow)
        // Alot hashachar = 16.1° below horizon (standard MyZmanim / zmanim-app default).
        // Do not use getAlos72() here — 72 minutes before sunrise is a separate zman (MGA day start).
        val nightEnd = zcTomorrow.alosHashachar?.time ?: zcTomorrow.getAlosHashachar()?.time
            ?: zcTomorrow.getAlos16Point1Degrees()?.time
        return ZmanimSnapshot(
            misheyakirMillis = zc.misheyakir10Point2Degrees?.time ?: zc.getMisheyakir10Point2Degrees()?.time,
            sunriseMillis = zc.sunrise?.time,
            sofZmanShemaMillis = zc.sofZmanShmaGRA?.time ?: zc.getSofZmanShmaGRA()?.time,
            sofZmanTefillaMillis = zc.sofZmanTfilaGRA?.time ?: zc.getSofZmanTfilaGRA()?.time,
            chatzosMillis = chatzos,
            minchaGedolaMillis = zc.minchaGedola?.time ?: zc.getMinchaGedola()?.time
                ?: chatzos?.let { it + 30 * 60 * 1000L },
            plagHaminchaMillis = zc.plagHamincha?.time ?: zc.getPlagHamincha()?.time,
            sunsetMillis = zc.sunset?.time,
            tzeitMillis = zc.nightfallMillis(),
            alotHaShacharMillis = zc.alosHashachar?.time ?: zc.getAlosHashachar()?.time
                ?: zc.getAlos16Point1Degrees()?.time,
            nightObligationsEndMillis = nightEnd,
            timezoneId = timezoneId
        )
    }

    private fun formatTime(epochMs: Long, tz: JavaTimeZone): String {
        val cal = Calendar.getInstance(tz).apply { timeInMillis = epochMs }
        val h = cal.get(Calendar.HOUR)
        val hour = if (h == 0) 12 else h
        val amPm = if (cal.get(Calendar.AM_PM) == Calendar.AM) "AM" else "PM"
        return "$hour:${cal.get(Calendar.MINUTE).toString().padStart(2, '0')} $amPm"
    }

    override fun electronicsRestPeriod(nowEpochMillis: Long, profile: UserProfile): ElectronicsRestPeriod? {
        val geo = geoFor(profile) ?: return null
        val tz = JavaTimeZone.getTimeZone(profile.timezoneId)
        val now = Calendar.getInstance(tz).apply { timeInMillis = nowEpochMillis }

        yomTovRestIfActive(now, geo, profile, nowEpochMillis)?.let { return it }
        shabbatRestIfActive(now, geo, profile, nowEpochMillis)?.let { return it }
        return null
    }

    private fun shabbatRestIfActive(
        now: Calendar,
        geo: GeoLocation,
        profile: UserProfile,
        nowEpochMillis: Long
    ): ElectronicsRestPeriod? {
        val friday = (now.clone() as Calendar).apply {
            while (get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
                add(Calendar.DAY_OF_MONTH, -1)
            }
        }
        val zcFriday = zmanimCalendar(geo, friday)
        val candleLighting = zcFriday.candleLighting?.time ?: return null

        val saturday = (friday.clone() as Calendar).apply { add(Calendar.DAY_OF_MONTH, 1) }
        val zcSaturday = zmanimCalendar(geo, saturday)
        val havdalah = zcSaturday.nightfallMillis() ?: return null

        if (nowEpochMillis < candleLighting || nowEpochMillis >= havdalah) return null

        val jc = calendarFor(
            LocalDate(friday.get(Calendar.YEAR), friday.get(Calendar.MONTH) + 1, friday.get(Calendar.DAY_OF_MONTH))
        )
        val hebrew = "${jc.jewishDayOfMonth} ${monthName(jc.jewishMonth)} ${jc.jewishYear}"
        val isErev = now.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY

        return ElectronicsRestPeriod(
            kind = RestKind.SHABBAT,
            title = if (isErev) "Shabbat is beginning" else "Shabbat Shalom",
            message = shabbatMessage(),
            hebrewDateLabel = hebrew,
            locationLabel = profile.locationLabel ?: geo.locationName,
            endsAtEpochMillis = havdalah
        )
    }

    private fun yomTovRestIfActive(
        now: Calendar,
        geo: GeoLocation,
        profile: UserProfile,
        nowEpochMillis: Long
    ): ElectronicsRestPeriod? {
        val jc = JewishCalendar(now.time)
        if (!jc.isYomTov || !jc.isAssurBemelacha) return null

        val zc = zmanimCalendar(geo, now)
        val previousDay = (now.clone() as Calendar).apply { add(Calendar.DAY_OF_MONTH, -1) }
        val zcPrev = zmanimCalendar(geo, previousDay)
        val start = zcPrev.sunset?.time ?: zc.sunrise?.time ?: return null
        val end = zc.nightfallMillis() ?: zc.sunset?.time ?: return null

        if (nowEpochMillis < start || nowEpochMillis >= end) return null

        val name = holidayName(jc)
        val hebrew = "${jc.jewishDayOfMonth} ${monthName(jc.jewishMonth)} ${jc.jewishYear}"

        return ElectronicsRestPeriod(
            kind = RestKind.YOM_TOV,
            title = name,
            message = yomTovMessageTemplate(),
            args = mapOf("holidayName" to name),
            hebrewDateLabel = hebrew,
            locationLabel = profile.locationLabel ?: geo.locationName,
            endsAtEpochMillis = end
        )
    }

    override fun upcomingHolidays(from: LocalDate, profile: UserProfile): List<UpcomingHoliday> {
        val results = mutableListOf<UpcomingHoliday>()
        var nextShabbat: UpcomingHoliday? = null
        var nextYomTov: UpcomingHoliday? = null
        var nextChanukah: UpcomingHoliday? = null
        var nextPurim: UpcomingHoliday? = null
        var nextRoshChodesh: UpcomingHoliday? = null
        var nextMinorHoliday: UpcomingHoliday? = null

        for (i in 0..60) {
            val d = from.plus(i, DateTimeUnit.DAY)
            val jc = calendarFor(d)
            if (nextShabbat == null && d.dayOfWeek == DayOfWeek.FRIDAY) {
                nextShabbat = UpcomingHoliday("Shabbat", i, "Candles, Kiddush, rest from electronics")
            }
            if (nextYomTov == null && jc.isYomTov && i > 0) {
                nextYomTov = UpcomingHoliday(holidayName(jc), i, prepHint(jc))
            }
            if (nextChanukah == null && jc.isChanukah && i > 0) {
                nextChanukah = UpcomingHoliday("Chanukah", i, "Light menorah each night")
            }
            if (nextPurim == null && jc.isPurim && i > 0) {
                nextPurim = UpcomingHoliday("Purim", i, "Megillah, matanot, mishloach manot")
            }
            if (nextRoshChodesh == null && jc.isRoshChodesh && i > 0) {
                nextRoshChodesh = UpcomingHoliday("Rosh Chodesh", i, "Yaaleh V'yavo, Hallel")
            }
            if (nextMinorHoliday == null && i > 0) {
                val idx = jc.yomTovIndex
                when (idx) {
                    JewishCalendar.TU_BESHVAT -> nextMinorHoliday =
                        UpcomingHoliday("Tu B'Shvat", i, "Minor holiday — New Year for Trees")
                    JewishCalendar.PESACH_SHENI -> nextMinorHoliday =
                        UpcomingHoliday("Pesach Sheni", i, "Minor holiday — Second Passover")
                    JewishCalendar.LAG_BAOMER -> nextMinorHoliday =
                        UpcomingHoliday("Lag BaOmer", i, "Minor holiday — 33rd day of the Omer")
                    JewishCalendar.TU_BEAV -> nextMinorHoliday =
                        UpcomingHoliday("Tu B'Av", i, "Minor holiday — celebration of joy")
                    JewishCalendar.TISHA_BEAV -> nextMinorHoliday =
                        UpcomingHoliday("Tisha B'Av", i, "Fast day — mourning the Temple")
                    JewishCalendar.FAST_OF_GEDALYAH -> nextMinorHoliday =
                        UpcomingHoliday("Fast of Gedaliah", i, "Fast day")
                    JewishCalendar.TENTH_OF_TEVES -> nextMinorHoliday =
                        UpcomingHoliday("Fast of 10 Tevet", i, "Fast day")
                    JewishCalendar.FAST_OF_ESTHER -> nextMinorHoliday =
                        UpcomingHoliday("Fast of Esther", i, "Fast day — before Purim")
                    JewishCalendar.SEVENTEEN_OF_TAMMUZ -> nextMinorHoliday =
                        UpcomingHoliday("Fast of 17 Tammuz", i, "Fast day")
                    JewishCalendar.YOM_HASHOAH -> nextMinorHoliday =
                        UpcomingHoliday("Yom HaShoah", i, "Holocaust Remembrance Day")
                    JewishCalendar.YOM_HAZIKARON -> nextMinorHoliday =
                        UpcomingHoliday("Yom HaZikaron", i, "Israeli Fallen Soldiers Memorial Day")
                    JewishCalendar.YOM_HAATZMAUT -> nextMinorHoliday =
                        UpcomingHoliday("Yom Ha'atzmaut", i, "Israeli Independence Day — customs vary by community")
                    JewishCalendar.YOM_YERUSHALAYIM -> nextMinorHoliday =
                        UpcomingHoliday("Yom Yerushalayim", i, "Jerusalem Day — customs vary by community")
                }
            }
        }
        listOfNotNull(nextShabbat, nextYomTov, nextChanukah, nextPurim, nextRoshChodesh, nextMinorHoliday).forEach {
            results += it
        }
        return results.sortedBy { it.daysAway }.take(8)
    }

    private fun prepHint(jc: JewishCalendar): String = when (jc.yomTovIndex) {
        // Major Yom Tov (d'oraita — Torah-level festival days)
        JewishCalendar.ROSH_HASHANA -> "Yom Tov — Shofar, teshuvah, sweet foods"
        JewishCalendar.YOM_KIPPUR -> "Yom Tov — Fast, prayer, atonement"
        JewishCalendar.SUCCOS -> "Yom Tov — Lulav & etrog, sukkah"
        JewishCalendar.SHEMINI_ATZERES -> "Yom Tov — Shemini Atzeret"
        JewishCalendar.SIMCHAS_TORAH -> "Yom Tov — Simchat Torah, hakafot"
        JewishCalendar.PESACH -> "Yom Tov — Seder night(s), matzah, no chametz"
        JewishCalendar.SHAVUOS -> "Yom Tov — Matan Torah, learning, dairy foods"
        // Chol HaMoed (intermediate festival days — work somewhat restricted)
        JewishCalendar.CHOL_HAMOED_PESACH -> "Chol HaMoed Pesach — intermediate festival day"
        JewishCalendar.CHOL_HAMOED_SUCCOS -> "Chol HaMoed Sukkot — intermediate festival day"
        JewishCalendar.HOSHANA_RABBA -> "Chol HaMoed Sukkot — last intermediate day"
        // Erev Yom Tov
        JewishCalendar.EREV_PESACH -> "Erev Pesach — fast of firstborn, search for chametz"
        // Rabbinic holidays (no Yom Tov restrictions, but special mitzvot)
        JewishCalendar.CHANUKAH -> "Chanukah — light the menorah each night"
        JewishCalendar.PURIM -> "Purim — Megillah, mishloach manot, matanot l'evyonim"
        JewishCalendar.PURIM_KATAN -> "Minor holiday — Purim Katan (in a leap year)"
        // Minor holidays / celebrations (no restrictions)
        JewishCalendar.TU_BESHVAT -> "Minor holiday — New Year for Trees"
        JewishCalendar.PESACH_SHENI -> "Minor holiday — Second Passover"
        JewishCalendar.LAG_BAOMER -> "Minor holiday — 33rd day of the Omer"
        JewishCalendar.TU_BEAV -> "Minor holiday — celebration of joy"
        // Fast days
        JewishCalendar.FAST_OF_GEDALYAH -> "Fast day — Fast of Gedaliah"
        JewishCalendar.TENTH_OF_TEVES -> "Fast day — Fast of 10 Tevet"
        JewishCalendar.FAST_OF_ESTHER -> "Fast day — before Purim"
        JewishCalendar.SEVENTEEN_OF_TAMMUZ -> "Fast day — Fast of 17 Tammuz"
        JewishCalendar.TISHA_BEAV -> "Fast day — Tisha B'Av, mourning the Temple"
        else -> "Special day"
    }

    /** 8.5° tzeit (common app default); not Rabbeinu Tam 72-minute tzeit. */
    private fun ComplexZmanimCalendar.nightfallMillis(): Long? =
        getTzaisGeonim8Point5Degrees()?.time ?: getTzais72()?.time

    private fun geoFor(profile: UserProfile): GeoLocation? {
        val lat = profile.latitude ?: return null
        val lon = profile.longitude ?: return null
        val tz = JavaTimeZone.getTimeZone(profile.timezoneId)
        val elevation = LocationElevation.metersFor(profile)
        return GeoLocation(profile.locationLabel ?: "User", lat, lon, elevation, tz)
    }

    /** Elevation adjusts sunrise/sunset only (KosherJava / MyZmanim-style). */
    private fun zmanimCalendar(geo: GeoLocation, day: Calendar): ComplexZmanimCalendar =
        ComplexZmanimCalendar(geo).apply {
            calendar = day
            isUseElevation = geo.elevation > 0.0
        }

    private fun calendarFor(date: LocalDate): JewishCalendar {
        val jld = JavaLocalDate.of(date.year, date.monthNumber, date.dayOfMonth)
        return JewishCalendar(jld).apply { isUseModernHolidays = true }
    }

    private fun monthName(month: Int): String = when (month) {
        JewishDate.NISSAN -> "Nissan"
        JewishDate.IYAR -> "Iyar"
        JewishDate.SIVAN -> "Sivan"
        JewishDate.TAMMUZ -> "Tammuz"
        JewishDate.AV -> "Av"
        JewishDate.ELUL -> "Elul"
        JewishDate.TISHREI -> "Tishrei"
        JewishDate.CHESHVAN -> "Cheshvan"
        JewishDate.KISLEV -> "Kislev"
        JewishDate.TEVES -> "Teves"
        JewishDate.SHEVAT -> "Shevat"
        JewishDate.ADAR -> "Adar"
        JewishDate.ADAR_II -> "Adar II"
        else -> "Month $month"
    }

    private fun holidayName(jc: JewishCalendar): String = when (jc.yomTovIndex) {
        JewishCalendar.EREV_PESACH -> "Erev Pesach"
        JewishCalendar.PESACH -> "Pesach"
        JewishCalendar.CHOL_HAMOED_PESACH -> "Chol HaMoed Pesach"
        JewishCalendar.SHAVUOS -> "Shavuot"
        JewishCalendar.ROSH_HASHANA -> "Rosh Hashana"
        JewishCalendar.FAST_OF_GEDALYAH -> "Fast of Gedaliah"
        JewishCalendar.YOM_KIPPUR -> "Yom Kippur"
        JewishCalendar.SUCCOS -> "Sukkot"
        JewishCalendar.CHOL_HAMOED_SUCCOS -> "Chol HaMoed Sukkot"
        JewishCalendar.HOSHANA_RABBA -> "Hoshana Raba"
        JewishCalendar.SHEMINI_ATZERES -> "Shemini Atzeret"
        JewishCalendar.SIMCHAS_TORAH -> "Simchat Torah"
        JewishCalendar.CHANUKAH -> "Chanukah"
        JewishCalendar.TENTH_OF_TEVES -> "Fast of 10 Tevet"
        JewishCalendar.TU_BESHVAT -> "Tu B'Shvat"
        JewishCalendar.FAST_OF_ESTHER -> "Fast of Esther"
        JewishCalendar.PURIM -> "Purim"
        JewishCalendar.PURIM_KATAN -> "Purim Katan"
        JewishCalendar.PESACH_SHENI -> "Pesach Sheni"
        JewishCalendar.LAG_BAOMER -> "Lag BaOmer"
        JewishCalendar.SEVENTEEN_OF_TAMMUZ -> "Fast of 17 Tammuz"
        JewishCalendar.TISHA_BEAV -> "Tisha B'Av"
        JewishCalendar.TU_BEAV -> "Tu B'Av"
        JewishCalendar.YOM_HASHOAH -> "Yom HaShoah"
        JewishCalendar.YOM_HAZIKARON -> "Yom HaZikaron"
        JewishCalendar.YOM_HAATZMAUT -> "Yom Ha'atzmaut"
        JewishCalendar.YOM_YERUSHALAYIM -> "Yom Yerushalayim"
        else -> "Yom Tov"
    }

    private fun formatCivil(date: LocalDate): String = ZmanimFormatter.formatCivilDate(date)
}

private fun isJerusalemProfile(profile: UserProfile): Boolean {
    if (profile.manualCityId in setOf("jlm", "jerusalem")) return true
    return profile.locationLabel?.contains("jerusalem", ignoreCase = true) == true
}
