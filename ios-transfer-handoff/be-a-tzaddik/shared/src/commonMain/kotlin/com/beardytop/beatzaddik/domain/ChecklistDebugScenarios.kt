package com.beardytop.beatzaddik.domain

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant

enum class ChecklistDebugPhase { EREV, DAY_OF }

enum class ChecklistDebugTimeSlot(val hour: Int, val label: String) {
    MORNING(9, "Morning"),
    AFTERNOON(14, "Afternoon"),
    EVENING(18, "Evening"),
    NIGHT(21, "Night"),
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

        val yomTov = "Yamim Tovim"
        erev(yomTov, "Pesach", "pesach") { cal, _ -> "erev_pesach" in cal.activeSeasons }
        dayOf(yomTov, "Pesach", "pesach") { cal, _ ->
            "pesach" in cal.activeSeasons && cal.isYomTovAssurBemelacha
        }
        erev(yomTov, "Shavuot", "shavuot") { cal, _ ->
            "erev_chag" in cal.activeSeasons && upcomingChag(HebrewCalendarEngine.SHAVUOS)(cal, cal)
        }
        dayOf(yomTov, "Shavuot", "shavuot", season("shavuot"))
        erev(yomTov, "Rosh Hashana", "rosh_hashana") { cal, _ ->
            "erev_chag" in cal.activeSeasons && upcomingChag(HebrewCalendarEngine.ROSH_HASHANA)(cal, cal)
        }
        dayOf(yomTov, "Rosh Hashana", "rosh_hashana", season("rosh_hashana"))
        erev(yomTov, "Sukkot", "sukkot") { cal, _ ->
            "erev_chag" in cal.activeSeasons && upcomingChag(HebrewCalendarEngine.SUCCOS)(cal, cal)
        }
        dayOf(yomTov, "Sukkot", "sukkot") { cal, _ ->
            "sukkot" in cal.activeSeasons && cal.isYomTov
        }
        erev(yomTov, "Shemini Atzeret", "shemini_atzeret") { cal, tomorrow ->
            "shemini_atzeret" !in cal.activeSeasons && "shemini_atzeret" in tomorrow.activeSeasons
        }
        dayOf(yomTov, "Shemini Atzeret", "shemini_atzeret", season("shemini_atzeret"))
        erev(
            yomTov,
            "Simchat Torah",
            "simchat_torah",
            relaxErevWindowCheck = true,
        ) { cal, tomorrow ->
            "shemini_atzeret" in cal.activeSeasons && "simchat_torah" in tomorrow.activeSeasons
        }
        dayOf(yomTov, "Simchat Torah", "simchat_torah", season("simchat_torah"))

        val chol = "Chol HaMoed"
        dayOf(chol, "Pesach", "ch_pesach", season("chol_hamoed_pesach"))
        dayOf(chol, "Sukkot", "ch_sukkot", season("chol_hamoed_sukkot"))

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
            cal.isSefiratHaomer && cal.omerDay != null && cal.omerDay in 10..20
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
        dayOf(seasonal, "Rosh Chodesh", "rosh_chodesh") { cal, _ -> cal.isRoshChodesh }
        erev(seasonal, "Chanukah", "chanukah", match = season("erev_chanukah"))
        dayOf(seasonal, "Chanukah", "chanukah") { cal, _ -> cal.isChanukah }
        erev(seasonal, "Purim", "purim", match = season("erev_purim"))
        dayOf(seasonal, "Purim", "purim") { cal, _ -> cal.isPurim }
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
}

object ChecklistDebugDateFinder {

    private val SEARCH_START = LocalDate(2018, 1, 1)
    private val SEARCH_END = LocalDate(2042, 12, 31)

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
        append(scenario.id)
        append('|')
        append(timeSlot.name)
        append('|')
        append(profile.isInIsrael)
        append('|')
        append(profile.timezoneId)
        append('|')
        append(profile.manualCityId ?: "")
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
        val canonical = canonicalHebrewDateForScenario(scenario)
        var date = SEARCH_START
        var fallback: ChecklistDebugOverride? = null
        while (date <= SEARCH_END) {
            val cal = calendar.dayInfoAt(date.toNoonMillis(profile), profile)
            val tomorrow = calendar.dayInfoForDate(date.plus(1, DateTimeUnit.DAY), profile)
            if (!scenario.matcher(cal, tomorrow)) {
                date = date.plus(1, DateTimeUnit.DAY)
                continue
            }
            val millis = date.atLocalTime(timeSlot.hour, profile.timezoneId)
            if (scenario.phase == ChecklistDebugPhase.EREV && !isErevDebuggable(cal)) {
                date = date.plus(1, DateTimeUnit.DAY)
                continue
            }
            val override = ChecklistDebugOverride(
                scenarioId = scenario.id,
                label = ChecklistDebugScenarios.displayLabel(scenario),
                simulatedDate = date,
                timeSlot = timeSlot,
                epochMillis = millis,
            )
            if (canonical == null || matchesCanonicalHebrew(cal, canonical)) {
                return override
            }
            if (fallback == null) {
                fallback = override
            }
            date = date.plus(1, DateTimeUnit.DAY)
        }
        return fallback
    }

    /**
     * For "day of" fasts, prefer the actual Hebrew calendar date (e.g. 17 Tammuz on 17 Tammuz)
     * rather than the first Shabbat-deferred observance (e.g. 18 Tammuz in 5778).
     */
    private fun canonicalHebrewDateForScenario(scenario: ChecklistDebugScenario): CanonicalHebrewDate? =
        when {
            scenario.phase == ChecklistDebugPhase.DAY_OF && "fast_17tam" in scenario.id ->
                CanonicalHebrewDate(HebrewCalendarEngine.TAMMUZ, 17)
            scenario.phase == ChecklistDebugPhase.DAY_OF && "tisha_beav" in scenario.id ->
                CanonicalHebrewDate(HebrewCalendarEngine.AV, 9)
            scenario.phase == ChecklistDebugPhase.DAY_OF && "fast_gedaliah" in scenario.id ->
                CanonicalHebrewDate(HebrewCalendarEngine.TISHREI, 3)
            scenario.phase == ChecklistDebugPhase.DAY_OF && "fast_10tev" in scenario.id ->
                CanonicalHebrewDate(HebrewCalendarEngine.TEVET, 10)
            scenario.id == "yom_kippur_erev" ->
                CanonicalHebrewDate(HebrewCalendarEngine.TISHREI, 9)
            scenario.phase == ChecklistDebugPhase.DAY_OF && "yom_kippur" in scenario.id ->
                CanonicalHebrewDate(HebrewCalendarEngine.TISHREI, 10)
            scenario.id == "shemini_atzeret_erev" ->
                CanonicalHebrewDate(HebrewCalendarEngine.TISHREI, 21)
            scenario.id == "simchat_torah_erev" ->
                CanonicalHebrewDate(HebrewCalendarEngine.TISHREI, 22)
            else -> null
        }

    private fun matchesCanonicalHebrew(cal: DayInfo, canonical: CanonicalHebrewDate): Boolean =
        cal.hebrewMonth == canonical.month && cal.hebrewDay == canonical.day

    /** Erev scenarios skip Shabbat (checklist is off). Debug preview ignores electronics-rest windows. */
    private fun isErevDebuggable(cal: DayInfo): Boolean =
        !HolyDayPhoneRules.isShabbatMelachaDay(cal)

    private fun LocalDate.toNoonMillis(profile: UserProfile): Long =
        atLocalTime(12, profile.timezoneId)

    private fun LocalDate.atLocalTime(hour: Int, timezoneId: String): Long {
        val tz = TimeZone.of(timezoneId)
        return LocalDateTime(year, monthNumber, dayOfMonth, hour, 0)
            .toInstant(tz)
            .toEpochMilliseconds()
    }
}
