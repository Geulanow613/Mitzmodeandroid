package com.beardytop.beatzaddik.domain

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant

enum class ChecklistDebugPhase { EREV, DAY_OF, MOTZEI }

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
    /**
     * When set, simulated clock is sunset + this many minutes (e.g. −21 = 21 min before sunset)
     * for phone-hide warning tests. Time-of-day chips are ignored.
     */
    val sunsetOffsetMinutes: Int? = null,
)

data class ChecklistDebugOverride(
    val scenarioId: String,
    val label: String,
    val simulatedDate: LocalDate,
    val timeSlot: ChecklistDebugTimeSlot,
    val epochMillis: Long,
)

object ChecklistDebugScenarios {

    internal const val TWO_DAYS_BEFORE_SUFFIX = "_2d"

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
        fun motzei(group: String, name: String, id: String, match: (DayInfo, DayInfo) -> Boolean) {
            add(ChecklistDebugScenario("${id}_motzei", group, name, ChecklistDebugPhase.MOTZEI, match))
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
        motzei(shabbat, "Shabbat", "shabbat") { cal, _ ->
            HolyDayPhoneRules.isShabbatMelachaDay(cal)
        }

        val yomTov = "Yamim Tovim"
        erev(yomTov, "Pesach", "pesach") { cal, _ -> "erev_pesach" in cal.activeSeasons }
        dayOf(yomTov, "Pesach", "pesach") { cal, _ ->
            cal.hebrewMonth == HebrewCalendarEngine.NISSAN && cal.hebrewDay == 15
        }
        motzei(yomTov, "Pesach", "pesach") { cal, _ ->
            cal.hebrewMonth == HebrewCalendarEngine.NISSAN && cal.hebrewDay == 15
        }
        erev(yomTov, "Shavuot", "shavuot") { cal, _ ->
            "erev_chag" in cal.activeSeasons && upcomingChag(HebrewCalendarEngine.SHAVUOS)(cal, cal)
        }
        dayOf(yomTov, "Shavuot", "shavuot") { cal, _ ->
            cal.hebrewMonth == HebrewCalendarEngine.SIVAN && cal.hebrewDay == 6
        }
        motzei(yomTov, "Shavuot", "shavuot") { cal, _ ->
            cal.hebrewMonth == HebrewCalendarEngine.SIVAN && cal.hebrewDay == 6
        }
        erev(yomTov, "Rosh Hashana", "rosh_hashana") { cal, _ ->
            "erev_chag" in cal.activeSeasons && upcomingChag(HebrewCalendarEngine.ROSH_HASHANA)(cal, cal)
        }
        dayOf(yomTov, "Rosh Hashana", "rosh_hashana") { cal, _ ->
            cal.hebrewMonth == HebrewCalendarEngine.TISHREI && cal.hebrewDay == 1
        }
        motzei(yomTov, "Rosh Hashana", "rosh_hashana") { cal, _ ->
            // Motzei Rosh Hashana = after the second day ends (tzeit of 2 Tishrei).
            // In years when Day 2 is Shabbat, this is Motzei Shabbat (motzash) as well.
            cal.hebrewMonth == HebrewCalendarEngine.TISHREI && cal.hebrewDay == 2
        }
        erev(yomTov, "Sukkot", "sukkot") { cal, _ ->
            "erev_chag" in cal.activeSeasons && upcomingChag(HebrewCalendarEngine.SUCCOS)(cal, cal)
        }
        dayOf(yomTov, "Sukkot", "sukkot") { cal, _ ->
            cal.hebrewMonth == HebrewCalendarEngine.TISHREI && cal.hebrewDay == 15 &&
                cal.isYomTovAssurBemelacha
        }
        motzei(yomTov, "Sukkot", "sukkot") { cal, _ ->
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
        motzei(yomTov, "Shemini Atzeret", "shemini_atzeret") { cal, _ ->
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
        motzei(yomTov, "Simchat Torah", "simchat_torah") { cal, _ ->
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
        motzei(fasts, "Yom Kippur", "yom_kippur") { cal, _ ->
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
        dayOf(seasonal, "Pesach Sheini", "pesach_sheini") { cal, _ ->
            cal.hebrewMonth == HebrewCalendarEngine.IYAR && cal.hebrewDay == 14
        }
        // Same day; multiple label spellings for convenience.
        dayOf(seasonal, "Tu B'Av", "tu_beav") { cal, _ ->
            cal.hebrewMonth == HebrewCalendarEngine.AV && cal.hebrewDay == 15
        }
        dayOf(seasonal, "Tu Bav", "tu_bav") { cal, _ ->
            cal.hebrewMonth == HebrewCalendarEngine.AV && cal.hebrewDay == 15
        }
        dayOf(seasonal, "Tu' B'av", "tu_beav_alt") { cal, _ ->
            cal.hebrewMonth == HebrewCalendarEngine.AV && cal.hebrewDay == 15
        }
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
        // Erev Rosh Chodesh: preview "begins tonight" + upcoming tiny-print label.
        // We use canonical dates so these are stable regardless of the user's real today.
        // 1 day RC: 29 Iyar → 1 Sivan
        erev(seasonal, "Rosh Chodesh (1 day)", "rosh_chodesh") { cal, tomorrow ->
            !cal.isRoshChodesh && tomorrow.isRoshChodesh && tomorrow.hebrewDay == 1
        }
        // 2 day RC: 29 Sivan → 30 Sivan + 1 Tammuz
        erev(seasonal, "Rosh Chodesh (2 days)", "rosh_chodesh_2day") { cal, tomorrow ->
            !cal.isRoshChodesh && tomorrow.isRoshChodesh && tomorrow.hebrewDay == 30
        }
        // Chanukah — one row per day with Erev / Day / Motzei so time-slot chips work.
        // Erev + Motzei land on the eve that begins Night N; Day is daytime of Chanukah day N
        // (afternoon → tonight Night N+1 except day 8; after tzeit via Motzei → Night N).
        val chanukah = "Chanukah"
        for (n in 1..8) {
            val label = "Chanukah day $n (Night $n)"
            val id = "chanukah_d$n"
            erev(chanukah, label, id) { cal, _ ->
                if (n == 1) "erev_chanukah" in cal.activeSeasons
                else cal.isChanukah && cal.chanukahDay == n - 1
            }
            dayOf(chanukah, label, id) { cal, _ ->
                cal.isChanukah && cal.chanukahDay == n
            }
            motzei(chanukah, label, id) { cal, _ ->
                if (n == 1) "erev_chanukah" in cal.activeSeasons
                else cal.isChanukah && cal.chanukahDay == n - 1
            }
        }
        // Friday daytime during Chanukah: helpful for "light before Shabbat candles".
        erev(chanukah, "Erev Shabbat during Chanukah", "chanukah_erev_shabbat") { cal, _ ->
            cal.isChanukah && cal.date.dayOfWeek == kotlinx.datetime.DayOfWeek.FRIDAY
        }
        // Motzei Shabbat during Chanukah: helpful for "light after havdalah" customs.
        motzei(chanukah, "Motzei Shabbat during Chanukah", "chanukah_motzei_shabbat") { cal, _ ->
            cal.isChanukah && cal.date.dayOfWeek == kotlinx.datetime.DayOfWeek.SATURDAY
        }
        erev(seasonal, "Purim", "purim", match = season("erev_purim"))
        dayOf(seasonal, "Purim", "purim") { cal, _ ->
            cal.isPurim &&
                (cal.hebrewMonth == HebrewCalendarEngine.ADAR ||
                    cal.hebrewMonth == HebrewCalendarEngine.ADAR_II) &&
                cal.hebrewDay == 14
        }

        val doubt = "Purim (City of doubt)"
        // Simulate being in a "city of doubt" (e.g. Tiberias). Some users follow regular Purim (14),
        // others follow walled-city Purim (15) and Meshulash when it occurs.
        dayOf(doubt, "Purim — regular (14 Adar)", "doubt_regular_purim") { cal, _ ->
            cal.isPurim &&
                (cal.hebrewMonth == HebrewCalendarEngine.ADAR ||
                    cal.hebrewMonth == HebrewCalendarEngine.ADAR_II) &&
                cal.hebrewDay == 14
        }
        // Walled-mode scenarios (observeWalledCityPurim = true via forDebugCalendar id prefix)
        dayOf(doubt, "Shushan Purim (walled-mode)", "doubt_walled_shushan") { cal, _ ->
            cal.isPurim && cal.hebrewDay == 15
        }
        dayOf(doubt, "Meshulash — Wed advance prep (walled-mode)", "doubt_walled_meshulash_adv") { cal, tomorrow ->
            cal.hebrewDay == 12 &&
                "erev_purim" in tomorrow.activeSeasons &&
                "erev_purim" !in cal.activeSeasons
        }
        erev(doubt, "Meshulash — Thu erev (walled-mode)", "doubt_walled_meshulash_erev") { cal, tomorrow ->
            "erev_purim" in cal.activeSeasons &&
                "purim_meshulash_friday" in tomorrow.activeSeasons
        }
        dayOf(doubt, "Meshulash — Fri Megillah & matanot (walled-mode)", "doubt_walled_meshulash_fri") { cal, _ ->
            "purim_meshulash_friday" in cal.activeSeasons
        }
        dayOf(doubt, "Meshulash — Shabbat communal (walled-mode)", "doubt_walled_meshulash_shabbat") { cal, _ ->
            "purim_meshulash_shabbat" in cal.activeSeasons
        }
        dayOf(doubt, "Meshulash — Sun mishloach & seudah (walled-mode)", "doubt_walled_meshulash_sun") { cal, _ ->
            "purim_meshulash_sunday" in cal.activeSeasons
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

        // Phone checklist warning — pin clock to 21 minutes before sunset.
        val phoneWarn = "Phone hide warning"
        fun phoneWarnErev(name: String, id: String, match: (DayInfo, DayInfo) -> Boolean) {
            add(
                ChecklistDebugScenario(
                    id = "phone_warn_$id",
                    group = phoneWarn,
                    label = "$name (21 min before sunset)",
                    phase = ChecklistDebugPhase.EREV,
                    matcher = match,
                    sunsetOffsetMinutes = HolyDayPhoneRules.DEBUG_WARN_OFFSET_MINUTES,
                ),
            )
        }
        phoneWarnErev("Shabbat", "shabbat") { cal, _ -> cal.isErevShabbat }
        phoneWarnErev("Pesach", "pesach") { cal, _ ->
            cal.upcomingChagYomTovIndex == HebrewCalendarEngine.PESACH ||
                "erev_pesach" in cal.activeSeasons
        }
        phoneWarnErev("Shavuot", "shavuot") { cal, _ ->
            cal.upcomingChagYomTovIndex == HebrewCalendarEngine.SHAVUOS
        }
        phoneWarnErev("Rosh Hashana", "rosh_hashana") { cal, _ ->
            cal.upcomingChagYomTovIndex == HebrewCalendarEngine.ROSH_HASHANA
        }
        phoneWarnErev("Yom Kippur", "yom_kippur") { cal, _ ->
            "erev_yom_kippur" in cal.activeSeasons ||
                cal.upcomingChagYomTovIndex == HebrewCalendarEngine.YOM_KIPPUR
        }
        phoneWarnErev("Sukkot", "sukkot") { cal, _ ->
            cal.upcomingChagYomTovIndex == HebrewCalendarEngine.SUCCOS
        }
    }.let { base ->
        val twoDaysBefore = base.filter { it.sunsetOffsetMinutes == null }.map { s ->
            s.copy(
                id = s.id + TWO_DAYS_BEFORE_SUFFIX,
                label = "${s.label} (2 days before)",
                // Not a real "erev/day/motzei" — it's a shifted date used for testing Upcoming labels.
                phase = ChecklistDebugPhase.DAY_OF,
                // Resolution is handled specially by ChecklistDebugDateFinder; this matcher is unused.
                matcher = { _, _ -> false },
                // Skip any erev-window checks (we are not trying to land on a halachic eve).
                relaxErevWindowCheck = true,
            )
        }
        base + twoDaysBefore
    }

    val groups: List<String> = all.map { it.group }.distinct()

    fun byId(id: String): ChecklistDebugScenario? = all.firstOrNull { it.id == id }

    fun displayLabel(scenario: ChecklistDebugScenario): String {
        if (scenario.sunsetOffsetMinutes != null) {
            return "Warn — ${scenario.label}"
        }
        val phase = when (scenario.phase) {
            ChecklistDebugPhase.EREV -> "Erev"
            ChecklistDebugPhase.DAY_OF -> "Day of"
            ChecklistDebugPhase.MOTZEI -> "Motzei"
        }
        return "$phase — ${scenario.label}"
    }

    /** Keyword filter for the debug menu — matches label, group, id, and phase words. */
    fun matchesSearch(scenario: ChecklistDebugScenario, query: String): Boolean {
        val normalized = query.trim().lowercase()
        if (normalized.isEmpty()) return true
        val haystack = buildString {
            append(scenario.label)
            append(' ')
            append(scenario.group)
            append(' ')
            append(scenario.id.replace('_', ' '))
            append(' ')
            when (scenario.phase) {
                ChecklistDebugPhase.EREV -> append("erev evening before")
                ChecklistDebugPhase.DAY_OF -> append("day daytime")
                ChecklistDebugPhase.MOTZEI -> append("motzei after nightfall")
            }
            if (scenario.id.endsWith(TWO_DAYS_BEFORE_SUFFIX)) {
                append(" 2 days before upcoming")
            }
        }.lowercase()
        return normalized.split(Regex("\\s+"))
            .filter { it.isNotEmpty() }
            .all { term -> haystack.contains(term) }
    }

    fun usesJerusalemCalendar(scenario: ChecklistDebugScenario): Boolean =
        scenario.group == "Shushan Purim (Jerusalem)"

    fun usesPurimDoubtCityCalendar(scenario: ChecklistDebugScenario): Boolean =
        scenario.group == "Purim (City of doubt)"
}

/** Shushan / Meshulash debug simulates walled-city calendar rules regardless of GPS city. */
fun UserProfile.forDebugCalendar(scenario: ChecklistDebugScenario?): UserProfile {
    if (scenario == null) return this
    if (ChecklistDebugScenarios.usesJerusalemCalendar(scenario)) {
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
    if (ChecklistDebugScenarios.usesPurimDoubtCityCalendar(scenario)) {
        // Pick a "city of doubt" and allow toggling walled-city Purim behavior per scenario id.
        val walled = scenario.id.startsWith("doubt_walled_")
        return copy(
            manualCityId = "tiberias",
            locationLabel = "Tiberias, Israel",
            latitude = 32.7936,
            longitude = 35.5328,
            timezoneId = "Asia/Jerusalem",
            useGps = false,
            locationSource = LocationSource.MANUAL,
            observeWalledCityPurim = walled,
        )
    }
    return this
}

object ChecklistDebugDateFinder {

    private const val CACHE_VERSION = 12

    private val CHOL_HAMOED_WEEKDAY_SCENARIOS = setOf("ch_pesach_day", "ch_sukkot_day")

    private val SEARCH_START = LocalDate(2018, 1, 1)
    private val SEARCH_END = LocalDate(2042, 12, 31)
    private const val MAX_SPIRAL_DAYS = 1100

    /**
     * Debug pin: either a fixed Hebrew month/day (with optional deferred alternates),
     * or a Chanukah day number (1–8) that works for both short and long Kislev years.
     */
    private data class CanonicalHebrewDate(
        val options: List<Pair<Int, Int>> = emptyList(),
        val chanukahDay: Int? = null,
    ) {
        constructor(month: Int, day: Int) : this(options = listOf(month to day))
        constructor(vararg monthDays: Pair<Int, Int>) : this(options = monthDays.toList())
    }

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
        // Synthetic "2 days before" debug variants: resolve the base scenario and shift it.
        if (scenario.id.endsWith(ChecklistDebugScenarios.TWO_DAYS_BEFORE_SUFFIX)) {
            val baseId = scenario.id.removeSuffix(ChecklistDebugScenarios.TWO_DAYS_BEFORE_SUFFIX)
            val baseScenario = ChecklistDebugScenarios.byId(baseId) ?: return null
            val baseOverride = resolve(calendar, profile, baseScenario, timeSlot) ?: return null
            val shiftedDate = baseOverride.simulatedDate.plus(-2, DateTimeUnit.DAY)
            if (shiftedDate !in SEARCH_START..SEARCH_END) return null
            val millis = epochMillisAt(shiftedDate, timeSlot, profile.timezoneId)
            return ChecklistDebugOverride(
                scenarioId = scenario.id,
                label = ChecklistDebugScenarios.displayLabel(scenario),
                simulatedDate = shiftedDate,
                timeSlot = timeSlot,
                epochMillis = millis,
            )
        }

        val anchor = calendar.dayInfoAt(
            kotlinx.datetime.Clock.System.now().toEpochMilliseconds(),
            profile,
        ).date
        resolveBirkatHachamah(calendar, scenario, timeSlot, profile, anchor)?.let { return it }
        val canonical = canonicalHebrewDateForScenario(scenario, profile)
        var best: ChecklistDebugOverride? = null
        var bestDistance = Int.MAX_VALUE

        for ((distance, date) in spiralDates(anchor)) {
            if (date !in SEARCH_START..SEARCH_END) continue
            val probeHour = if (scenario.phase == ChecklistDebugPhase.MOTZEI) 12 else timeSlot.hour
            val probeMillis = date.atLocalTime(probeHour, profile.timezoneId)
            val cal = calendar.dayInfoAt(probeMillis, profile)
            val tomorrow = calendar.dayInfoForDate(cal.date.plus(1, DateTimeUnit.DAY), profile)
            if (canonical != null && !matchesCanonicalHebrew(cal, canonical)) continue
            if (!scenario.matcher(cal, tomorrow)) continue
            if (scenario.phase == ChecklistDebugPhase.EREV && !isErevDebuggable(cal)) continue
            val millis = epochMillisFor(
                calendar = calendar,
                profile = profile,
                scenario = scenario,
                simulatedDate = cal.date,
                timeSlot = timeSlot,
            )
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
        calendar: JewishCalendarService,
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
        val millis = epochMillisFor(
            calendar = calendar,
            profile = profile,
            scenario = scenario,
            simulatedDate = simulatedDate,
            timeSlot = timeSlot,
        )
        return ChecklistDebugOverride(
            scenarioId = scenario.id,
            label = ChecklistDebugScenarios.displayLabel(scenario),
            simulatedDate = simulatedDate,
            timeSlot = timeSlot,
            epochMillis = millis,
        )
    }

    private fun epochMillisFor(
        calendar: JewishCalendarService,
        profile: UserProfile,
        scenario: ChecklistDebugScenario,
        simulatedDate: LocalDate,
        timeSlot: ChecklistDebugTimeSlot,
    ): Long {
        scenario.sunsetOffsetMinutes?.let { offsetMin ->
            val noon = simulatedDate.atLocalTime(12, profile.timezoneId)
            val sunset = calendar.dayInfoAt(noon, profile).zmanim?.sunsetMillis
            if (sunset != null) return sunset + offsetMin * 60_000L
        }
        if (scenario.phase == ChecklistDebugPhase.MOTZEI) {
            return motzeiEpochMillis(calendar, profile, simulatedDate, timeSlot)
        }
        return epochMillisAt(simulatedDate, timeSlot, profile.timezoneId)
    }

    /** ~2 hours after tzeit on the holy day — when the weekday checklist returns. */
    private fun motzeiEpochMillis(
        calendar: JewishCalendarService,
        profile: UserProfile,
        date: LocalDate,
        fallbackTimeSlot: ChecklistDebugTimeSlot,
    ): Long {
        val noon = date.atLocalTime(12, profile.timezoneId)
        val dayInfo = calendar.dayInfoAt(noon, profile)
        val tzeit = dayInfo.zmanim?.tzeitMillis
        return if (tzeit != null) {
            tzeit + MOTZEI_HOURS_AFTER_TZEIT_MS
        } else {
            epochMillisAt(date, fallbackTimeSlot, profile.timezoneId)
        }
    }

    private const val MOTZEI_HOURS_AFTER_TZEIT_MS = 2L * 60 * 60 * 1000

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
            "pesach_erev", "phone_warn_pesach" -> CanonicalHebrewDate(HebrewCalendarEngine.NISSAN, 14)
            "pesach_day", "pesach_motzei" -> CanonicalHebrewDate(HebrewCalendarEngine.NISSAN, 15)
            "ch_pesach_day" -> CanonicalHebrewDate(HebrewCalendarEngine.NISSAN, 18)
            "shavuot_erev", "phone_warn_shavuot" -> CanonicalHebrewDate(HebrewCalendarEngine.SIVAN, 5)
            "shavuot_day", "shavuot_motzei" -> CanonicalHebrewDate(HebrewCalendarEngine.SIVAN, 6)
            "rosh_hashana_erev", "phone_warn_rosh_hashana" -> CanonicalHebrewDate(HebrewCalendarEngine.ELUL, 29)
            "rosh_hashana_day" -> CanonicalHebrewDate(HebrewCalendarEngine.TISHREI, 1)
            "rosh_hashana_motzei" -> CanonicalHebrewDate(HebrewCalendarEngine.TISHREI, 2)
            "sukkot_erev", "phone_warn_sukkot" -> CanonicalHebrewDate(HebrewCalendarEngine.TISHREI, 14)
            "sukkot_day", "sukkot_motzei" -> CanonicalHebrewDate(HebrewCalendarEngine.TISHREI, 15)
            "shemini_atzeret_erev" -> CanonicalHebrewDate(HebrewCalendarEngine.TISHREI, 21)
            "shemini_atzeret_day", "shemini_atzeret_motzei" ->
                CanonicalHebrewDate(HebrewCalendarEngine.TISHREI, 22)
            "simchat_torah_erev" -> CanonicalHebrewDate(HebrewCalendarEngine.TISHREI, 22)
            "simchat_torah_day" -> if (profile.isInIsrael) {
                CanonicalHebrewDate(HebrewCalendarEngine.TISHREI, 22)
            } else {
                CanonicalHebrewDate(HebrewCalendarEngine.TISHREI, 23)
            }
            "simchat_torah_motzei" -> if (profile.isInIsrael) {
                CanonicalHebrewDate(HebrewCalendarEngine.TISHREI, 22)
            } else {
                CanonicalHebrewDate(HebrewCalendarEngine.TISHREI, 23)
            }
            "ch_sukkot_day" -> CanonicalHebrewDate(HebrewCalendarEngine.TISHREI, 18)
            "fast_gedaliah_erev" -> CanonicalHebrewDate(
                HebrewCalendarEngine.TISHREI to 2,
                HebrewCalendarEngine.TISHREI to 3, // erev of deferred Sunday 4 Tishrei
            )
            "fast_gedaliah_day" -> CanonicalHebrewDate(
                HebrewCalendarEngine.TISHREI to 3,
                HebrewCalendarEngine.TISHREI to 4, // deferred from Shabbat
            )
            "fast_10tev_erev" -> CanonicalHebrewDate(HebrewCalendarEngine.TEVET, 9)
            "fast_10tev_day" -> CanonicalHebrewDate(HebrewCalendarEngine.TEVET, 10)
            "fast_esther_erev" -> CanonicalHebrewDate(
                HebrewCalendarEngine.ADAR to 12,
                HebrewCalendarEngine.ADAR to 10, // erev of deferred Thursday 11 Adar
                HebrewCalendarEngine.ADAR to 11, // erev of deferred Thursday 12 Adar
            )
            "fast_esther_day" -> CanonicalHebrewDate(
                HebrewCalendarEngine.ADAR to 13,
                HebrewCalendarEngine.ADAR to 11, // deferred Thursday
                HebrewCalendarEngine.ADAR to 12,
            )
            "fast_17tam_erev" -> CanonicalHebrewDate(
                HebrewCalendarEngine.TAMMUZ to 16,
                HebrewCalendarEngine.TAMMUZ to 17, // erev of deferred Sunday 18
            )
            "fast_17tam_day" -> CanonicalHebrewDate(
                HebrewCalendarEngine.TAMMUZ to 17,
                HebrewCalendarEngine.TAMMUZ to 18, // deferred from Shabbat
            )
            // Classic erev and Friday-before-deferred-Sunday are both 8 Av (9 Av = Shabbat that year).
            "tisha_beav_erev" -> CanonicalHebrewDate(HebrewCalendarEngine.AV, 8)
            "tisha_beav_day" -> CanonicalHebrewDate(
                HebrewCalendarEngine.AV to 9,
                HebrewCalendarEngine.AV to 10, // deferred Sunday
            )
            "yom_kippur_erev", "phone_warn_yom_kippur" -> CanonicalHebrewDate(HebrewCalendarEngine.TISHREI, 9)
            "yom_kippur_day", "yom_kippur_motzei" -> CanonicalHebrewDate(HebrewCalendarEngine.TISHREI, 10)
            // Chanukah days 1–5 are always Kislev 25–29; days 6–8 depend on Kislev length.
            "chanukah_d1_erev", "chanukah_d1_motzei" ->
                CanonicalHebrewDate(HebrewCalendarEngine.KISLEV, 24)
            "chanukah_d1_day" -> CanonicalHebrewDate(HebrewCalendarEngine.KISLEV, 25)
            "chanukah_d2_erev", "chanukah_d2_motzei" ->
                CanonicalHebrewDate(HebrewCalendarEngine.KISLEV, 25)
            "chanukah_d2_day" -> CanonicalHebrewDate(HebrewCalendarEngine.KISLEV, 26)
            "chanukah_d3_erev", "chanukah_d3_motzei" ->
                CanonicalHebrewDate(HebrewCalendarEngine.KISLEV, 26)
            "chanukah_d3_day" -> CanonicalHebrewDate(HebrewCalendarEngine.KISLEV, 27)
            "chanukah_d4_erev", "chanukah_d4_motzei" ->
                CanonicalHebrewDate(HebrewCalendarEngine.KISLEV, 27)
            "chanukah_d4_day" -> CanonicalHebrewDate(HebrewCalendarEngine.KISLEV, 28)
            "chanukah_d5_erev", "chanukah_d5_motzei" ->
                CanonicalHebrewDate(HebrewCalendarEngine.KISLEV, 28)
            "chanukah_d5_day" -> CanonicalHebrewDate(HebrewCalendarEngine.KISLEV, 29)
            "chanukah_d6_erev", "chanukah_d6_motzei" ->
                CanonicalHebrewDate(chanukahDay = 5)
            "chanukah_d6_day" -> CanonicalHebrewDate(chanukahDay = 6)
            "chanukah_d7_erev", "chanukah_d7_motzei" ->
                CanonicalHebrewDate(chanukahDay = 6)
            "chanukah_d7_day" -> CanonicalHebrewDate(chanukahDay = 7)
            "chanukah_d8_erev", "chanukah_d8_motzei" ->
                CanonicalHebrewDate(chanukahDay = 7)
            "chanukah_d8_day" -> CanonicalHebrewDate(chanukahDay = 8)
            // Rosh Chodesh debug: force stable "erev" dates.
            // 1-day Rosh Chodesh (month with 29 days): 29 Iyar (erev) → 1 Sivan.
            "rosh_chodesh_erev" -> CanonicalHebrewDate(HebrewCalendarEngine.IYAR, 29)
            // 2-day Rosh Chodesh (month with 30 days): 29 Sivan (erev) → 30 Sivan + 1 Tammuz.
            "rosh_chodesh_2day_erev" -> CanonicalHebrewDate(HebrewCalendarEngine.SIVAN, 29)
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
            "pesach_sheini_day" -> CanonicalHebrewDate(HebrewCalendarEngine.IYAR, 14)
            "tu_beav_day", "tu_bav_day", "tu_beav_alt_day" -> CanonicalHebrewDate(HebrewCalendarEngine.AV, 15)
            "nissan_day" -> CanonicalHebrewDate(HebrewCalendarEngine.NISSAN, 15)
            else -> null
        }

    private fun matchesCanonicalHebrew(cal: DayInfo, canonical: CanonicalHebrewDate): Boolean {
        canonical.chanukahDay?.let { night ->
            return cal.isChanukah && cal.chanukahDay == night
        }
        val month = cal.hebrewMonth ?: return false
        val day = cal.hebrewDay ?: return false
        return canonical.options.any { (pinMonth, pinDay) ->
            day == pinDay && (
                month == pinMonth ||
                    // Leap-year Adar: canonical "Adar" dates match Adar II.
                    (pinMonth == HebrewCalendarEngine.ADAR && month == HebrewCalendarEngine.ADAR_II)
                )
        }
    }

    /** Erev scenarios skip Shabbat (checklist is off). Debug preview ignores electronics-rest windows. */
    private fun isErevDebuggable(cal: DayInfo): Boolean =
        !HolyDayPhoneRules.isShabbatMelachaDay(cal)

    fun overrideEpochMillis(
        calendar: JewishCalendarService,
        profile: UserProfile,
        scenario: ChecklistDebugScenario,
        simulatedDate: LocalDate,
        timeSlot: ChecklistDebugTimeSlot,
    ): Long = epochMillisFor(calendar, profile, scenario, simulatedDate, timeSlot)

    fun epochMillisAt(date: LocalDate, timeSlot: ChecklistDebugTimeSlot, timezoneId: String): Long =
        date.atLocalTime(timeSlot.hour, timezoneId)

    private fun LocalDate.atLocalTime(hour: Int, timezoneId: String): Long {
        val tz = TimeZone.of(timezoneId)
        return LocalDateTime(year, monthNumber, dayOfMonth, hour, 0)
            .toInstant(tz)
            .toEpochMilliseconds()
    }
}
