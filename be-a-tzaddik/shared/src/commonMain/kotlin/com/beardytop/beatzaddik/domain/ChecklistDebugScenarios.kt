package com.beardytop.beatzaddik.domain

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant

enum class ChecklistDebugPhase { EREV, DAY_OF }

enum class ChecklistDebugTimeSlot(val hour: Int, val label: String) {
    PRE_DAWN(3, "Pre-dawn"),
    MORNING(9, "Morning"),
    AFTERNOON(14, "Afternoon"),
    EVENING(19, "Evening"),
    NIGHT(22, "Night"),
    ;

    /** Checklist section order / period chip while debug sim is active. */
    fun toTimeOfDay(): TimeOfDay = when (this) {
        PRE_DAWN, EVENING, NIGHT -> TimeOfDay.NIGHT
        MORNING -> TimeOfDay.DAY
        AFTERNOON -> TimeOfDay.AFTERNOON
    }
}

data class ChecklistDebugScenario(
    val id: String,
    val group: String,
    val label: String,
    val phase: ChecklistDebugPhase,
    val matcher: (DayInfo, DayInfo) -> Boolean,
    /** Diaspora Simchat Torah prep falls on Shemini Atzeret (Yom Tov) — allow that debug window. */
    val relaxErevWindowCheck: Boolean = false,
)

data class ChecklistDebugOverride(
    val scenarioId: String,
    val label: String,
    val simulatedDate: LocalDate,
    val timeSlot: ChecklistDebugTimeSlot,
    val epochMillis: Long,
)

object ChecklistDebugScenarios {

    val all: List<ChecklistDebugScenario> = buildList {
        fun erev(
            group: String,
            name: String,
            id: String,
            relaxErevWindowCheck: Boolean = false,
            match: (DayInfo, DayInfo) -> Boolean,
        ) {
            add(
                ChecklistDebugScenario(
                    id = "${id}_erev",
                    group = group,
                    label = name,
                    phase = ChecklistDebugPhase.EREV,
                    matcher = match,
                    relaxErevWindowCheck = relaxErevWindowCheck,
                ),
            )
        }
        fun dayOf(group: String, name: String, id: String, match: (DayInfo, DayInfo) -> Boolean) {
            add(ChecklistDebugScenario("${id}_day", group, name, ChecklistDebugPhase.DAY_OF, match))
        }
        fun season(s: String): (DayInfo, DayInfo) -> Boolean = { cal, _ -> s in cal.activeSeasons }
        fun upcomingChag(idx: Int): (DayInfo, DayInfo) -> Boolean = { cal, _ ->
            cal.upcomingChagYomTovIndex == idx
        }
        fun fastToday(idx: Int): (DayInfo, DayInfo) -> Boolean = { cal, _ ->
            cal.fastDayIndex == idx
        }

        val shabbat = "Shabbat"
        erev(shabbat, "Shabbat", "shabbat") { cal, _ -> cal.isErevShabbat }
        dayOf(shabbat, "Shabbat", "shabbat") { cal, _ ->
            HolyDayPhoneRules.isShabbatMelachaDay(cal)
        }

        val yomTov = "Yamim Tovim"
        erev(yomTov, "Pesach", "pesach") { cal, _ -> "erev_pesach" in cal.activeSeasons }
        dayOf(yomTov, "Pesach", "pesach") { cal, _ ->
            cal.hebrewMonth == HebrewCalendarEngine.NISSAN && cal.hebrewDay == 15
        }
        erev(yomTov, "Shavuot", "shavuot") { cal, _ ->
            "erev_chag" in cal.activeSeasons && upcomingChag(HebrewCalendarEngine.SHAVUOS)(cal, cal)
        }
        dayOf(yomTov, "Shavuot", "shavuot") { cal, _ ->
            cal.hebrewMonth == HebrewCalendarEngine.SIVAN && cal.hebrewDay == 6
        }
        erev(yomTov, "Rosh Hashana", "rosh_hashana") { cal, _ ->
            "erev_chag" in cal.activeSeasons && upcomingChag(HebrewCalendarEngine.ROSH_HASHANA)(cal, cal)
        }
        dayOf(yomTov, "Rosh Hashana", "rosh_hashana") { cal, _ ->
            cal.hebrewMonth == HebrewCalendarEngine.TISHREI && cal.hebrewDay == 1
        }
        erev(yomTov, "Sukkot", "sukkot") { cal, _ ->
            "erev_chag" in cal.activeSeasons && upcomingChag(HebrewCalendarEngine.SUCCOS)(cal, cal)
        }
        dayOf(yomTov, "Sukkot", "sukkot") { cal, _ ->
            cal.hebrewMonth == HebrewCalendarEngine.TISHREI && cal.hebrewDay == 15 &&
                cal.isYomTovAssurBemelacha
        }
        erev(yomTov, "Shemini Atzeret", "shemini_atzeret") { cal, tomorrow ->
            // Hoshana Raba (21 Tishrei) is still chol hamoed — not erev_chag — and in Israel
            // tomorrow may be tagged simchat_torah rather than shemini_atzeret.
            !cal.isYomTovAssurBemelacha && tomorrow.isYomTovAssurBemelacha &&
                (
                    "shemini_atzeret" in tomorrow.activeSeasons ||
                        (cal.hebrewMonth == HebrewCalendarEngine.TISHREI && cal.hebrewDay == 21)
                    )
        }
        dayOf(yomTov, "Shemini Atzeret", "shemini_atzeret") { cal, _ ->
            cal.hebrewMonth == HebrewCalendarEngine.TISHREI && cal.hebrewDay == 22 &&
                ("shemini_atzeret" in cal.activeSeasons || "simchat_torah" in cal.activeSeasons)
        }
        erev(
            yomTov,
            "Simchat Torah",
            "simchat_torah",
            relaxErevWindowCheck = true,
        ) { cal, tomorrow ->
            "shemini_atzeret" in cal.activeSeasons && "simchat_torah" in tomorrow.activeSeasons
        }
        dayOf(yomTov, "Simchat Torah", "simchat_torah") { cal, _ ->
            "simchat_torah" in cal.activeSeasons
        }

        val chol = "Chol HaMoed"
        dayOf(chol, "Pesach", "ch_pesach") { cal, _ ->
            "chol_hamoed_pesach" in cal.activeSeasons &&
                cal.hebrewMonth == HebrewCalendarEngine.NISSAN && cal.hebrewDay == 18
        }
        dayOf(chol, "Sukkot", "ch_sukkot") { cal, _ ->
            "chol_hamoed_sukkot" in cal.activeSeasons &&
                cal.hebrewMonth == HebrewCalendarEngine.TISHREI && cal.hebrewDay == 18
        }

        val fasts = "Fasts"
        erev(fasts, "Gedaliah", "fast_gedaliah") { cal, _ ->
            "erev_minor_fast" in cal.activeSeasons &&
                cal.upcomingFastDayIndex == HebrewCalendarEngine.FAST_OF_GEDALYAH
        }
        dayOf(fasts, "Gedaliah", "fast_gedaliah") { cal, _ ->
            fastToday(HebrewCalendarEngine.FAST_OF_GEDALYAH)(cal, cal)
        }
        erev(fasts, "10 Tevet", "fast_10tev") { cal, _ ->
            "erev_minor_fast" in cal.activeSeasons &&
                cal.upcomingFastDayIndex == HebrewCalendarEngine.TENTH_OF_TEVES
        }
        dayOf(fasts, "10 Tevet", "fast_10tev") { cal, _ ->
            fastToday(HebrewCalendarEngine.TENTH_OF_TEVES)(cal, cal)
        }
        erev(fasts, "Esther", "fast_esther") { cal, _ ->
            "erev_minor_fast" in cal.activeSeasons &&
                cal.upcomingFastDayIndex == HebrewCalendarEngine.FAST_OF_ESTHER
        }
        dayOf(fasts, "Esther", "fast_esther") { cal, _ ->
            fastToday(HebrewCalendarEngine.FAST_OF_ESTHER)(cal, cal)
        }
        erev(fasts, "17 Tammuz", "fast_17tam") { cal, _ ->
            "erev_minor_fast" in cal.activeSeasons &&
                cal.upcomingFastDayIndex == HebrewCalendarEngine.SEVENTEEN_OF_TAMMUZ
        }
        dayOf(fasts, "17 Tammuz", "fast_17tam") { cal, _ ->
            fastToday(HebrewCalendarEngine.SEVENTEEN_OF_TAMMUZ)(cal, cal)
        }
        erev(fasts, "Tisha B'Av", "tisha_beav", match = season("erev_tisha_beav"))
        dayOf(fasts, "Tisha B'Av", "tisha_beav") { cal, _ ->
            fastToday(HebrewCalendarEngine.TISHA_BEAV)(cal, cal)
        }
        erev(fasts, "Yom Kippur", "yom_kippur", match = season("erev_yom_kippur"))
        dayOf(fasts, "Yom Kippur", "yom_kippur") { cal, _ ->
            cal.fastDayIndex == HebrewCalendarEngine.YOM_KIPPUR || "yom_kippur" in cal.activeSeasons
        }

        val israel = "Israel (modern)"
        dayOf(israel, "Yom HaShoah", "yom_hashoah") { cal, _ -> cal.isYomHaShoah }
        dayOf(israel, "Yom HaZikaron", "yom_hazikaron") { cal, _ -> cal.isYomHaZikaron }
        dayOf(israel, "Yom HaAtzmaut", "yom_haatzmaut") { cal, _ -> cal.isYomHaAtzmaut }
        dayOf(israel, "Yom Yerushalayim", "yom_yerushalayim") { cal, _ -> cal.isYomYerushalayim }

        val seasonal = "Seasonal"
        dayOf(seasonal, "Sefirat HaOmer", "sefirah") { cal, _ ->
            cal.isSefiratHaomer && cal.omerDay == 15
        }
        dayOf(seasonal, "Lag BaOmer", "lag_baomer") { cal, _ -> cal.isLagBaomer }
        dayOf(seasonal, "Three Weeks", "three_weeks") { cal, _ ->
            val m = cal.hebrewMonth
            val d = cal.hebrewDay
            m == HebrewCalendarEngine.TAMMUZ && d != null && d >= 17
        }
        dayOf(seasonal, "Nine Days", "nine_days") { cal, _ ->
            cal.hebrewMonth == HebrewCalendarEngine.AV && cal.hebrewDay != null && cal.hebrewDay in 1..8
        }
        dayOf(seasonal, "Rosh Chodesh", "rosh_chodesh") { cal, _ ->
            cal.isRoshChodesh && cal.hebrewDay == 1
        }
        erev(seasonal, "Chanukah", "chanukah", match = season("erev_chanukah"))
        dayOf(seasonal, "Chanukah", "chanukah") { cal, _ ->
            cal.isChanukah && cal.hebrewMonth == HebrewCalendarEngine.KISLEV && cal.hebrewDay == 25
        }
        erev(seasonal, "Purim", "purim", match = season("erev_purim"))
        dayOf(seasonal, "Purim", "purim") { cal, _ ->
            cal.isPurim &&
                (cal.hebrewMonth == HebrewCalendarEngine.ADAR ||
                    cal.hebrewMonth == HebrewCalendarEngine.ADAR_II) &&
                cal.hebrewDay == 14
        }

        val shushan = "Shushan Purim (Jerusalem)"
        dayOf(shushan, "12 Adar — week before", "shushan_12adar") { cal, _ ->
            cal.hebrewDay == 12 &&
                (cal.hebrewMonth == HebrewCalendarEngine.ADAR ||
                    cal.hebrewMonth == HebrewCalendarEngine.ADAR_II) &&
                "erev_purim" !in cal.activeSeasons &&
                !cal.isPurim
        }
        dayOf(shushan, "13 Adar — Taanit Esther", "shushan_fast_esther") { cal, _ ->
            cal.fastDayIndex == HebrewCalendarEngine.FAST_OF_ESTHER
        }
        erev(shushan, "14 Adar — Erev Shushan Purim", "shushan_erev") { cal, _ ->
            "erev_purim" in cal.activeSeasons && cal.hebrewDay == 14
        }
        dayOf(shushan, "15 Adar — Shushan Purim", "shushan_day") { cal, _ ->
            cal.isPurim && cal.hebrewDay == 15
        }
        dayOf(shushan, "Meshulash — Wed advance prep", "shushan_meshulash_adv") { cal, tomorrow ->
            cal.hebrewDay == 12 &&
                "erev_purim" in tomorrow.activeSeasons &&
                "erev_purim" !in cal.activeSeasons
        }
        erev(shushan, "Meshulash — Thu erev (Megillah tonight)", "shushan_meshulash_erev") { cal, tomorrow ->
            "erev_purim" in cal.activeSeasons &&
                "purim_meshulash_friday" in tomorrow.activeSeasons
        }
        dayOf(shushan, "Meshulash — Fri Megillah & matanot", "shushan_meshulash_fri") { cal, _ ->
            "purim_meshulash_friday" in cal.activeSeasons
        }
        dayOf(shushan, "Meshulash — Shabbat (communal)", "shushan_meshulash_shabbat") { cal, _ ->
            "purim_meshulash_shabbat" in cal.activeSeasons
        }
        dayOf(shushan, "Meshulash — Sun mishloach & seudah", "shushan_meshulash_sun") { cal, _ ->
            "purim_meshulash_sunday" in cal.activeSeasons
        }
        dayOf(seasonal, "Month of Nissan", "nissan") { cal, _ ->
            cal.hebrewMonth == HebrewCalendarEngine.NISSAN && cal.hebrewDay == 15
        }
        dayOf(seasonal, "Birkat HaIlanot (Nissan)", "birkat_hailanot") { cal, _ ->
            cal.hebrewMonth == HebrewCalendarEngine.NISSAN && cal.hebrewDay in 1..28
        }
        dayOf(seasonal, "Birkat Hachamah (week before)", "birkat_hachamah_adv") { cal, _ ->
            val occ = BirkatHachamahRules.visibleOccurrence(cal.date)
            occ != null && !BirkatHachamahRules.isRecitationDay(cal.date)
        }
        dayOf(seasonal, "Birkat Hachamah (day)", "birkat_hachamah") { cal, _ ->
            BirkatHachamahRules.isRecitationDay(cal.date)
        }
    }

    val groups: List<String> = all.map { it.group }.distinct()

    fun byId(id: String): ChecklistDebugScenario? = all.firstOrNull { it.id == id }

    fun displayLabel(scenario: ChecklistDebugScenario): String {
        val phase = when (scenario.phase) {
            ChecklistDebugPhase.EREV -> "Erev"
            ChecklistDebugPhase.DAY_OF -> "Day of"
        }
        return "$phase — ${scenario.label}"
    }

    fun usesJerusalemCalendar(scenario: ChecklistDebugScenario): Boolean =
        scenario.group == "Shushan Purim (Jerusalem)"
}

/** Shushan / Meshulash debug simulates walled-city calendar rules regardless of GPS city. */
fun UserProfile.forDebugCalendar(scenario: ChecklistDebugScenario?): UserProfile {
    if (scenario == null || !ChecklistDebugScenarios.usesJerusalemCalendar(scenario)) return this
    return copy(
        manualCityId = "jlm",
        locationLabel = "Jerusalem, Israel",
        latitude = 31.7683,
        longitude = 35.2137,
        timezoneId = "Asia/Jerusalem",
        useGps = false,
        locationSource = LocationSource.MANUAL,
    )
}

object ChecklistDebugDateFinder {

    private const val CACHE_VERSION = 8

    private val CHOL_HAMOED_WEEKDAY_SCENARIOS = setOf("ch_pesach_day", "ch_sukkot_day")

    private val SEARCH_START = LocalDate(2018, 1, 1)
    private val SEARCH_END = LocalDate(2042, 12, 31)
    private const val MAX_SPIRAL_DAYS = 1100

    private data class CanonicalHebrewDate(val month: Int, val day: Int)

    private val cache = mutableMapOf<String, ChecklistDebugOverride?>()

    fun clearCache() {
        cache.clear()
    }

    private fun cacheKey(
        scenario: ChecklistDebugScenario,
        timeSlot: ChecklistDebugTimeSlot,
        profile: UserProfile,
    ): String = buildString {
        append(CACHE_VERSION)
        append('|')
        append(scenario.id)
        append('|')
        append(timeSlot.name)
        append('|')
        append(profile.isInIsrael)
        append('|')
        append(profile.timezoneId)
        append('|')
        append(profile.manualCityId ?: "")
        append('|')
        append(profile.locationLabel ?: "")
    }

    fun resolve(
        calendar: JewishCalendarService,
        profile: UserProfile,
        scenario: ChecklistDebugScenario,
        timeSlot: ChecklistDebugTimeSlot,
    ): ChecklistDebugOverride? {
        val key = cacheKey(scenario, timeSlot, profile)
        if (key in cache) return cache[key]
        val result = resolveUncached(calendar, profile, scenario, timeSlot)
        cache[key] = result
        return result
    }

    private fun resolveUncached(
        calendar: JewishCalendarService,
        profile: UserProfile,
        scenario: ChecklistDebugScenario,
        timeSlot: ChecklistDebugTimeSlot,
    ): ChecklistDebugOverride? {
        val anchor = calendar.dayInfoAt(
            kotlinx.datetime.Clock.System.now().toEpochMilliseconds(),
            profile,
        ).date
        resolveBirkatHachamah(scenario, timeSlot, profile, anchor)?.let { return it }
        val canonical = canonicalHebrewDateForScenario(scenario, profile)
        var best: ChecklistDebugOverride? = null
        var bestDistance = Int.MAX_VALUE

        for ((distance, date) in spiralDates(anchor)) {
            if (date !in SEARCH_START..SEARCH_END) continue
            val millis = date.atLocalTime(timeSlot.hour, profile.timezoneId)
            val cal = calendar.dayInfoAt(millis, profile)
            val tomorrow = calendar.dayInfoForDate(cal.date.plus(1, DateTimeUnit.DAY), profile)
            if (canonical != null && !matchesCanonicalHebrew(cal, canonical)) continue
            if (!scenario.matcher(cal, tomorrow)) continue
            if (scenario.phase == ChecklistDebugPhase.EREV && !isErevDebuggable(cal)) continue
            val override = ChecklistDebugOverride(
                scenarioId = scenario.id,
                label = ChecklistDebugScenarios.displayLabel(scenario),
                simulatedDate = cal.date,
                timeSlot = timeSlot,
                epochMillis = millis,
            )
            if (canonical != null) {
                if (scenario.id in CHOL_HAMOED_WEEKDAY_SCENARIOS &&
                    HolyDayPhoneRules.isShabbatMelachaDay(cal)
                ) {
                    continue
                }
                return override
            }
            if (distance < bestDistance) {
                best = override
                bestDistance = distance
            }
        }
        return best
    }

    /**
     * Birkat Hachamah uses fixed Gregorian dates (2037, 2065, …) — too far for the spiral
     * search window, so jump directly to the nearest occurrence.
     */
    private fun resolveBirkatHachamah(
        scenario: ChecklistDebugScenario,
        timeSlot: ChecklistDebugTimeSlot,
        profile: UserProfile,
        anchor: LocalDate,
    ): ChecklistDebugOverride? {
        val occurrence = BirkatHachamahRules.nearestOccurrenceTo(anchor) ?: return null
        val simulatedDate = when (scenario.id) {
            "birkat_hachamah_day" -> occurrence
            "birkat_hachamah_adv_day" -> BirkatHachamahRules.sampleAdvanceDay(occurrence)
            else -> return null
        }
        if (simulatedDate !in SEARCH_START..SEARCH_END) return null
        val millis = simulatedDate.atLocalTime(timeSlot.hour, profile.timezoneId)
        return ChecklistDebugOverride(
            scenarioId = scenario.id,
            label = ChecklistDebugScenarios.displayLabel(scenario),
            simulatedDate = simulatedDate,
            timeSlot = timeSlot,
            epochMillis = millis,
        )
    }

    /** Today first, then alternating future/past — nearest observance to real today. */
    private fun spiralDates(anchor: LocalDate): Sequence<Pair<Int, LocalDate>> = sequence {
        yield(0 to anchor)
        for (offset in 1..MAX_SPIRAL_DAYS) {
            yield(offset to anchor.plus(offset, DateTimeUnit.DAY))
            yield(offset to anchor.plus(-offset, DateTimeUnit.DAY))
        }
    }

    /**
     * Pin each debug scenario to a specific Hebrew date when possible so "Day of Pesach"
     * is always 15 Nisan (not 8th day Yom Tov), etc.
     */
    private fun canonicalHebrewDateForScenario(
        scenario: ChecklistDebugScenario,
        profile: UserProfile,
    ): CanonicalHebrewDate? =
        when (scenario.id) {
            "pesach_erev" -> CanonicalHebrewDate(HebrewCalendarEngine.NISSAN, 14)
            "pesach_day" -> CanonicalHebrewDate(HebrewCalendarEngine.NISSAN, 15)
            "ch_pesach_day" -> CanonicalHebrewDate(HebrewCalendarEngine.NISSAN, 18)
            "shavuot_erev" -> CanonicalHebrewDate(HebrewCalendarEngine.SIVAN, 5)
            "shavuot_day" -> CanonicalHebrewDate(HebrewCalendarEngine.SIVAN, 6)
            "rosh_hashana_erev" -> CanonicalHebrewDate(HebrewCalendarEngine.ELUL, 29)
            "rosh_hashana_day" -> CanonicalHebrewDate(HebrewCalendarEngine.TISHREI, 1)
            "sukkot_erev" -> CanonicalHebrewDate(HebrewCalendarEngine.TISHREI, 14)
            "sukkot_day" -> CanonicalHebrewDate(HebrewCalendarEngine.TISHREI, 15)
            "shemini_atzeret_erev" -> CanonicalHebrewDate(HebrewCalendarEngine.TISHREI, 21)
            "shemini_atzeret_day" -> CanonicalHebrewDate(HebrewCalendarEngine.TISHREI, 22)
            "simchat_torah_erev" -> CanonicalHebrewDate(HebrewCalendarEngine.TISHREI, 22)
            "simchat_torah_day" -> if (profile.isInIsrael) {
                CanonicalHebrewDate(HebrewCalendarEngine.TISHREI, 22)
            } else {
                CanonicalHebrewDate(HebrewCalendarEngine.TISHREI, 23)
            }
            "ch_sukkot_day" -> CanonicalHebrewDate(HebrewCalendarEngine.TISHREI, 18)
            "fast_gedaliah_erev" -> CanonicalHebrewDate(HebrewCalendarEngine.TISHREI, 2)
            "fast_gedaliah_day" -> CanonicalHebrewDate(HebrewCalendarEngine.TISHREI, 3)
            "fast_10tev_erev" -> CanonicalHebrewDate(HebrewCalendarEngine.TEVET, 9)
            "fast_10tev_day" -> CanonicalHebrewDate(HebrewCalendarEngine.TEVET, 10)
            "fast_esther_erev" -> CanonicalHebrewDate(HebrewCalendarEngine.ADAR, 12)
            "fast_esther_day" -> CanonicalHebrewDate(HebrewCalendarEngine.ADAR, 13)
            "fast_17tam_erev" -> CanonicalHebrewDate(HebrewCalendarEngine.TAMMUZ, 16)
            "fast_17tam_day" -> CanonicalHebrewDate(HebrewCalendarEngine.TAMMUZ, 17)
            "tisha_beav_erev" -> CanonicalHebrewDate(HebrewCalendarEngine.AV, 8)
            "tisha_beav_day" -> CanonicalHebrewDate(HebrewCalendarEngine.AV, 9)
            "yom_kippur_erev" -> CanonicalHebrewDate(HebrewCalendarEngine.TISHREI, 9)
            "yom_kippur_day" -> CanonicalHebrewDate(HebrewCalendarEngine.TISHREI, 10)
            "chanukah_erev" -> CanonicalHebrewDate(HebrewCalendarEngine.KISLEV, 24)
            "chanukah_day" -> CanonicalHebrewDate(HebrewCalendarEngine.KISLEV, 25)
            "purim_erev" -> if (JerusalemPurimRules.isJerusalemProfile(profile)) {
                CanonicalHebrewDate(HebrewCalendarEngine.ADAR, 14)
            } else {
                CanonicalHebrewDate(HebrewCalendarEngine.ADAR, 13)
            }
            "purim_day" -> if (JerusalemPurimRules.isJerusalemProfile(profile)) {
                CanonicalHebrewDate(HebrewCalendarEngine.ADAR, 15)
            } else {
                CanonicalHebrewDate(HebrewCalendarEngine.ADAR, 14)
            }
            "shushan_12adar_day" -> CanonicalHebrewDate(HebrewCalendarEngine.ADAR, 12)
            "shushan_fast_esther_day" -> CanonicalHebrewDate(HebrewCalendarEngine.ADAR, 13)
            "shushan_erev_erev" -> CanonicalHebrewDate(HebrewCalendarEngine.ADAR, 14)
            "shushan_day_day" -> CanonicalHebrewDate(HebrewCalendarEngine.ADAR, 15)
            "shushan_meshulash_adv_day" -> CanonicalHebrewDate(HebrewCalendarEngine.ADAR, 12)
            "shushan_meshulash_erev_erev" -> CanonicalHebrewDate(HebrewCalendarEngine.ADAR, 13)
            "shushan_meshulash_fri_day" -> CanonicalHebrewDate(HebrewCalendarEngine.ADAR, 14)
            "shushan_meshulash_shabbat_day" -> CanonicalHebrewDate(HebrewCalendarEngine.ADAR, 15)
            "shushan_meshulash_sun_day" -> CanonicalHebrewDate(HebrewCalendarEngine.ADAR, 16)
            "lag_baomer_day" -> CanonicalHebrewDate(HebrewCalendarEngine.IYAR, 18)
            "nissan_day" -> CanonicalHebrewDate(HebrewCalendarEngine.NISSAN, 15)
            else -> null
        }

    private fun matchesCanonicalHebrew(cal: DayInfo, canonical: CanonicalHebrewDate): Boolean {
        if (cal.hebrewDay != canonical.day) return false
        if (cal.hebrewMonth == canonical.month) return true
        // Leap-year Adar: canonical "Adar" dates match Adar II when that is the active festival month.
        return canonical.month == HebrewCalendarEngine.ADAR &&
            cal.hebrewMonth == HebrewCalendarEngine.ADAR_II
    }

    /** Erev scenarios skip Shabbat (checklist is off). Debug preview ignores electronics-rest windows. */
    private fun isErevDebuggable(cal: DayInfo): Boolean =
        !HolyDayPhoneRules.isShabbatMelachaDay(cal)

    fun epochMillisAt(date: LocalDate, timeSlot: ChecklistDebugTimeSlot, timezoneId: String): Long =
        date.atLocalTime(timeSlot.hour, timezoneId)

    private fun LocalDate.atLocalTime(hour: Int, timezoneId: String): Long {
        val tz = TimeZone.of(timezoneId)
        return LocalDateTime(year, monthNumber, dayOfMonth, hour, 0)
            .toInstant(tz)
            .toEpochMilliseconds()
    }
}
