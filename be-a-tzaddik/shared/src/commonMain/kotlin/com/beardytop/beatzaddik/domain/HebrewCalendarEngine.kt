package com.beardytop.beatzaddik.domain

/**
 * Pure Kotlin port of critical algorithms from KosherJava 2.5.0 (JewishDate + JewishCalendar).
 *
 * Used on iOS where the KosherJava Android library is unavailable.
 * All constant values and formulas match KosherJava exactly.
 */
internal object HebrewCalendarEngine {

    // ── Month constants (Nissan-based, matching KosherJava) ──────────────────
    const val NISSAN   = 1;  const val IYAR    = 2;  const val SIVAN   = 3
    const val TAMMUZ   = 4;  const val AV      = 5;  const val ELUL    = 6
    const val TISHREI  = 7;  const val CHESHVAN = 8; const val KISLEV  = 9
    const val TEVET    = 10; const val SHEVAT  = 11; const val ADAR    = 12
    const val ADAR_II  = 13

    // ── yomTovIndex constants (same integer values as KosherJava) ────────────
    const val EREV_PESACH          =  0
    const val PESACH               =  1
    const val CHOL_HAMOED_PESACH   =  2
    const val PESACH_SHENI         =  3
    const val EREV_SHAVUOS         =  4
    const val SHAVUOS              =  5
    const val SEVENTEEN_OF_TAMMUZ  =  6
    const val TISHA_BEAV           =  7
    const val TU_BEAV              =  8
    const val EREV_ROSH_HASHANA    =  9
    const val ROSH_HASHANA         = 10
    const val FAST_OF_GEDALYAH     = 11
    const val EREV_YOM_KIPPUR      = 12
    const val YOM_KIPPUR           = 13
    const val EREV_SUCCOS          = 14
    const val SUCCOS               = 15
    const val CHOL_HAMOED_SUCCOS   = 16
    const val HOSHANA_RABBA        = 17
    const val SHEMINI_ATZERES      = 18
    const val SIMCHAS_TORAH        = 19
    const val CHANUKAH             = 21
    const val TENTH_OF_TEVES       = 22
    const val TU_BESHVAT           = 23
    const val FAST_OF_ESTHER       = 24
    const val PURIM                = 25
    const val SHUSHAN_PURIM        = 26
    const val PURIM_KATAN          = 27
    const val YOM_HASHOAH          = 29
    const val YOM_HAZIKARON        = 30
    const val YOM_HAATZMAUT        = 31
    const val YOM_YERUSHALAYIM     = 32
    const val LAG_BAOMER           = 33
    const val ISRU_CHAG            = 35

    // ── Day-of-week constants (Java Calendar convention: 1=Sun … 7=Sat) ─────
    const val SUNDAY    = 1
    const val MONDAY    = 2
    const val TUESDAY   = 3
    const val WEDNESDAY = 4
    const val THURSDAY  = 5
    const val FRIDAY    = 6
    const val SATURDAY  = 7

    // ── Molad constants ───────────────────────────────────────────────────────
    private const val CHALAKIM_PER_DAY   = 25920L          // 24 * 1080
    private const val CHALAKIM_PER_MONTH = 765433L         // 29d 12h 793p
    private const val CHALAKIM_MOLAD_TOHU = 31524L         // 1d 5h 204p

    // ── Core calendar math ────────────────────────────────────────────────────

    fun isJewishLeapYear(year: Int): Boolean = (7 * year + 1) % 19 < 7

    fun getLastMonthOfJewishYear(year: Int): Int = if (isJewishLeapYear(year)) 13 else 12

    private fun getJewishMonthOfYear(year: Int, month: Int): Int {
        val leap = isJewishLeapYear(year)
        return (month + (if (leap) 6 else 5)) % (if (leap) 13 else 12) + 1
    }

    private fun getChalakimSinceMoladTohu(year: Int, month: Int): Long {
        val y = year - 1
        val moy = getJewishMonthOfYear(year, month)
        val elapsed = 235L * (y / 19) + 12L * (y % 19) + (7L * (y % 19) + 1) / 19 + (moy - 1)
        return CHALAKIM_MOLAD_TOHU + CHALAKIM_PER_MONTH * elapsed
    }

    private fun addDechiyos(year: Int, moladDay: Int, moladParts: Int): Int {
        var d = moladDay
        if (moladParts >= 19440 ||
            (moladDay % 7 == 2 && moladParts >= 9924 && !isJewishLeapYear(year)) ||
            (moladDay % 7 == 1 && moladParts >= 16789 && isJewishLeapYear(year - 1))
        ) d++
        if (d % 7 == 0 || d % 7 == 3 || d % 7 == 5) d++
        return d
    }

    fun getJewishCalendarElapsedDays(year: Int): Int {
        val chalakim = getChalakimSinceMoladTohu(year, TISHREI)
        val moladDay   = (chalakim / CHALAKIM_PER_DAY).toInt()
        val moladParts = (chalakim - moladDay * CHALAKIM_PER_DAY).toInt()
        return addDechiyos(year, moladDay, moladParts)
    }

    fun getDaysInJewishYear(year: Int): Int =
        getJewishCalendarElapsedDays(year + 1) - getJewishCalendarElapsedDays(year)

    fun isCheshvanLong(year: Int): Boolean = getDaysInJewishYear(year) % 10 == 5
    fun isKislevShort(year: Int): Boolean  = getDaysInJewishYear(year) % 10 == 3

    fun getDaysInJewishMonth(year: Int, month: Int): Int =
        if (month == IYAR || month == TAMMUZ || month == ELUL ||
            (month == CHESHVAN && !isCheshvanLong(year)) ||
            (month == KISLEV   && isKislevShort(year))   ||
            month == TEVET ||
            (month == ADAR && !isJewishLeapYear(year)) ||
            month == ADAR_II
        ) 29 else 30

    fun getDaysSinceStartOfJewishYear(year: Int, month: Int, day: Int): Int {
        var elapsed = day
        if (month < TISHREI) {
            for (m in TISHREI..getLastMonthOfJewishYear(year)) elapsed += getDaysInJewishMonth(year, m)
            for (m in NISSAN until month)                       elapsed += getDaysInJewishMonth(year, m)
        } else {
            for (m in TISHREI until month)                      elapsed += getDaysInJewishMonth(year, m)
        }
        return elapsed
    }

    // ── Omer day ──────────────────────────────────────────────────────────────

    /** Returns the Omer day (1–49) or null if not in the Omer period. */
    fun getOmerDay(month: Int, day: Int): Int? = when {
        month == NISSAN && day >= 16 -> day - 15
        month == IYAR                -> day + 15
        month == SIVAN && day < 6    -> day + 44
        else                         -> null
    }

    /** Returns the day of Chanukah (1–8) or null. */
    fun getDayOfChanukah(year: Int, month: Int, day: Int): Int? = when {
        month == KISLEV && day >= 25 -> day - 24
        month == TEVET -> when {
            day == 1 || day == 2                           -> if (isKislevShort(year)) day + 5 else day + 6
            day == 3 && isKislevShort(year)                -> 8
            else                                           -> null
        }
        else -> null
    }

    // ── Holiday index (ported from JewishCalendar.getYomTovIndex) ────────────

    /**
     * Returns a [yomTovIndex] constant (e.g. [PESACH], [CHANUKAH] …) or -1.
     *
     * [dayOfWeek] uses Java Calendar convention: 1=Sunday … 7=Saturday.
     * [isLeapYear] should be [isJewishLeapYear](year).
     */
    fun getYomTovIndex(
        year: Int, month: Int, day: Int,
        dayOfWeek: Int, isLeapYear: Boolean,
        inIsrael: Boolean, useModernHolidays: Boolean = true
    ): Int {
        return when (month) {
            NISSAN -> when {
                day == 14 -> EREV_PESACH
                day == 15 || day == 21 ||
                    (!inIsrael && (day == 16 || day == 22)) -> PESACH
                (day in 17..20) || (day == 16 && inIsrael) -> CHOL_HAMOED_PESACH
                (day == 22 && inIsrael) || (day == 23 && !inIsrael) -> ISRU_CHAG
                useModernHolidays &&
                    ((day == 26 && dayOfWeek == THURSDAY) ||
                     (day == 28 && dayOfWeek == MONDAY)   ||
                     (day == 27 && dayOfWeek != SUNDAY && dayOfWeek != FRIDAY)) -> YOM_HASHOAH
                else -> -1
            }
            IYAR -> when {
                useModernHolidays &&
                    ((day == 4 && dayOfWeek == TUESDAY) ||
                     ((day == 3 || day == 2) && dayOfWeek == WEDNESDAY) ||
                     (day == 5 && dayOfWeek == MONDAY)) -> YOM_HAZIKARON
                useModernHolidays &&
                    ((day == 5 && dayOfWeek == WEDNESDAY) ||
                     ((day == 4 || day == 3) && dayOfWeek == THURSDAY) ||
                     (day == 6 && dayOfWeek == TUESDAY)) -> YOM_HAATZMAUT
                day == 14 -> PESACH_SHENI
                day == 18 -> LAG_BAOMER
                useModernHolidays && day == 28 -> YOM_YERUSHALAYIM
                else -> -1
            }
            SIVAN -> when {
                day == 5 -> EREV_SHAVUOS
                day == 6 || (!inIsrael && day == 7) -> SHAVUOS
                (inIsrael && day == 7) || (!inIsrael && day == 8) -> ISRU_CHAG
                else -> -1
            }
            TAMMUZ -> when {
                (day == 17 && dayOfWeek != SATURDAY) ||
                    (day == 18 && dayOfWeek == SUNDAY) -> SEVENTEEN_OF_TAMMUZ
                else -> -1
            }
            AV -> when {
                (day == 9 && dayOfWeek != SATURDAY) ||
                    (day == 10 && dayOfWeek == SUNDAY) -> TISHA_BEAV
                day == 15 -> TU_BEAV
                else -> -1
            }
            ELUL -> if (day == 29) EREV_ROSH_HASHANA else -1
            TISHREI -> when {
                day == 1 || day == 2 -> ROSH_HASHANA
                (day == 3 && dayOfWeek != SATURDAY) || (day == 4 && dayOfWeek == SUNDAY) -> FAST_OF_GEDALYAH
                day == 9 -> EREV_YOM_KIPPUR
                day == 10 -> YOM_KIPPUR
                day == 14 -> EREV_SUCCOS
                day == 15 || (!inIsrael && day == 16) -> SUCCOS
                (day in 17..20) || (day == 16 && inIsrael) -> CHOL_HAMOED_SUCCOS
                day == 21 -> HOSHANA_RABBA
                day == 22 -> SHEMINI_ATZERES
                day == 23 && !inIsrael -> SIMCHAS_TORAH
                (day == 23 && inIsrael) || (day == 24 && !inIsrael) -> ISRU_CHAG
                else -> -1
            }
            KISLEV -> if (day >= 25) CHANUKAH else -1
            TEVET -> when {
                day == 1 || day == 2 || (day == 3 && isKislevShort(year)) -> CHANUKAH
                day == 10 -> TENTH_OF_TEVES
                else -> -1
            }
            SHEVAT -> if (day == 15) TU_BESHVAT else -1
            ADAR -> when {
                !isLeapYear && (
                    ((day == 11 || day == 12) && dayOfWeek == THURSDAY) ||
                    (day == 13 && dayOfWeek != FRIDAY && dayOfWeek != SATURDAY)
                ) -> FAST_OF_ESTHER
                !isLeapYear && day == 14 -> PURIM
                !isLeapYear && day == 15 -> SHUSHAN_PURIM
                isLeapYear && day == 14 -> PURIM_KATAN
                else -> -1
            }
            ADAR_II -> when {
                ((day == 11 || day == 12) && dayOfWeek == THURSDAY) ||
                    (day == 13 && dayOfWeek != FRIDAY && dayOfWeek != SATURDAY) -> FAST_OF_ESTHER
                day == 14 -> PURIM
                day == 15 -> SHUSHAN_PURIM
                else -> -1
            }
            else -> -1
        }
    }

    /** True if the yomTovIndex represents a day with melacha (work) prohibition. */
    fun isYomTovAssurBemelacha(idx: Int): Boolean = idx in setOf(
        PESACH, SHAVUOS, SUCCOS, SHEMINI_ATZERES, SIMCHAS_TORAH, ROSH_HASHANA, YOM_KIPPUR
    )

    /** Pesach, Shavuot, Sukkot — the three pilgrimage festivals (Shalosh Regalim). */
    fun isShaloshRegalim(idx: Int?): Boolean =
        idx == PESACH || idx == SHAVUOS || idx == SUCCOS

    /** True when today is 14 Nisan — erev first Seder. */
    fun isErevFirstPesachSeder(hebrewMonth: Int?, hebrewDay: Int?): Boolean =
        hebrewMonth == NISSAN && hebrewDay == 14

    /** 21 Tishrei — seventh day of Sukkot; Hoshana Rabbah. */
    fun isHoshanaRabbah(hebrewMonth: Int?, hebrewDay: Int?): Boolean =
        hebrewMonth == TISHREI && hebrewDay == 21

    /** Opening Yom Tov night(s) of Pesach when Shehecheyanu is recited. */
    fun pesachOpeningYomTovGetsShehecheyanu(
        hebrewMonth: Int?,
        hebrewDay: Int?,
        inIsrael: Boolean,
    ): Boolean {
        if (hebrewMonth != NISSAN || hebrewDay == null) return false
        return if (inIsrael) hebrewDay == 15 else hebrewDay == 15 || hebrewDay == 16
    }

    /** Final Yom Tov day of Pesach — Shehecheyanu is never recited (7th Israel, 8th Diaspora). */
    fun isFinalYomTovDayOfPesach(hebrewMonth: Int?, hebrewDay: Int?, inIsrael: Boolean): Boolean {
        if (hebrewMonth != NISSAN || hebrewDay == null) return false
        return if (inIsrael) hebrewDay == 21 else hebrewDay == 22
    }

    /** True for days that are "Yom Tov" (not erev/fast days, not Isru Chag). */
    fun isYomTov(idx: Int): Boolean {
        if (idx == -1) return false
        return idx !in setOf(
            EREV_PESACH, EREV_SHAVUOS, EREV_ROSH_HASHANA, EREV_YOM_KIPPUR, EREV_SUCCOS,
            SEVENTEEN_OF_TAMMUZ, TISHA_BEAV, FAST_OF_GEDALYAH, TENTH_OF_TEVES,
            FAST_OF_ESTHER, ISRU_CHAG
        )
    }

    // ── Parsha calculation (ported from JewishCalendar.getParshah) ────────────

    /**
     * Returns the parsha table index (0–16) for the given year, or -1 if unrecognised.
     * Exactly mirrors [JewishCalendar.getParshaYearType].
     */
    fun getParshaYearType(year: Int, inIsrael: Boolean): Int {
        val raw = (getJewishCalendarElapsedDays(year) + 1) % 7
        val dow = if (raw == 0) 7 else raw          // convert 0→7 for Shabbat readability
        val leap = isJewishLeapYear(year)
        val cl = isCheshvanLong(year)
        val ks = isKislevShort(year)
        return if (leap) when (dow) {
            MONDAY    -> if (ks) (if (inIsrael) 14 else 6) else if (cl) (if (inIsrael) 15 else 7) else -1
            TUESDAY   -> if (inIsrael) 15 else 7
            THURSDAY  -> if (ks) 8 else if (cl) 9 else -1
            SATURDAY  -> if (ks) 10 else if (cl) (if (inIsrael) 16 else 11) else -1
            else      -> -1
        } else when (dow) {
            MONDAY    -> if (ks) 0 else if (cl) (if (inIsrael) 12 else 1) else -1
            TUESDAY   -> if (inIsrael) 12 else 1
            THURSDAY  -> if (cl) 3 else if (!ks) (if (inIsrael) 13 else 2) else -1
            SATURDAY  -> if (ks) 4 else if (cl) 5 else -1
            else      -> -1
        }
    }

    // 17 parsha schedules indexed 0–16, matching KosherJava's parshalist[][].
    // null = NONE (no parsha that Shabbat — Yom Tov / Chol HaMoed).
    private val PARSHA_LIST: Array<Array<String?>> = arrayOf(
        // 0
        arrayOf(null,"VAYEILECH","HAAZINU",null,"BERESHIS","NOACH","LECH_LECHA","VAYERA","CHAYEI_SARA","TOLDOS","VAYETZEI","VAYISHLACH","VAYESHEV","MIKETZ","VAYIGASH","VAYECHI","SHEMOS","VAERA","BO","BESHALACH","YISRO","MISHPATIM","TERUMAH","TETZAVEH","KI_SISA","VAYAKHEL_PEKUDEI","VAYIKRA","TZAV",null,"SHMINI","TAZRIA_METZORA","ACHREI_MOS_KEDOSHIM","EMOR","BEHAR_BECHUKOSAI","BAMIDBAR","NASSO","BEHAALOSCHA","SHLACH","KORACH","CHUKAS","BALAK","PINCHAS","MATOS_MASEI","DEVARIM","VAESCHANAN","EIKEV","REEH","SHOFTIM","KI_SEITZEI","KI_SAVO","NITZAVIM_VAYEILECH"),
        // 1
        arrayOf(null,"VAYEILECH","HAAZINU",null,"BERESHIS","NOACH","LECH_LECHA","VAYERA","CHAYEI_SARA","TOLDOS","VAYETZEI","VAYISHLACH","VAYESHEV","MIKETZ","VAYIGASH","VAYECHI","SHEMOS","VAERA","BO","BESHALACH","YISRO","MISHPATIM","TERUMAH","TETZAVEH","KI_SISA","VAYAKHEL_PEKUDEI","VAYIKRA","TZAV",null,"SHMINI","TAZRIA_METZORA","ACHREI_MOS_KEDOSHIM","EMOR","BEHAR_BECHUKOSAI","BAMIDBAR",null,"NASSO","BEHAALOSCHA","SHLACH","KORACH","CHUKAS_BALAK","PINCHAS","MATOS_MASEI","DEVARIM","VAESCHANAN","EIKEV","REEH","SHOFTIM","KI_SEITZEI","KI_SAVO","NITZAVIM_VAYEILECH"),
        // 2
        arrayOf(null,"HAAZINU",null,null,"BERESHIS","NOACH","LECH_LECHA","VAYERA","CHAYEI_SARA","TOLDOS","VAYETZEI","VAYISHLACH","VAYESHEV","MIKETZ","VAYIGASH","VAYECHI","SHEMOS","VAERA","BO","BESHALACH","YISRO","MISHPATIM","TERUMAH","TETZAVEH","KI_SISA","VAYAKHEL_PEKUDEI","VAYIKRA","TZAV",null,null,"SHMINI","TAZRIA_METZORA","ACHREI_MOS_KEDOSHIM","EMOR","BEHAR_BECHUKOSAI","BAMIDBAR","NASSO","BEHAALOSCHA","SHLACH","KORACH","CHUKAS","BALAK","PINCHAS","MATOS_MASEI","DEVARIM","VAESCHANAN","EIKEV","REEH","SHOFTIM","KI_SEITZEI","KI_SAVO","NITZAVIM"),
        // 3
        arrayOf(null,"HAAZINU",null,null,"BERESHIS","NOACH","LECH_LECHA","VAYERA","CHAYEI_SARA","TOLDOS","VAYETZEI","VAYISHLACH","VAYESHEV","MIKETZ","VAYIGASH","VAYECHI","SHEMOS","VAERA","BO","BESHALACH","YISRO","MISHPATIM","TERUMAH","TETZAVEH","KI_SISA","VAYAKHEL","PEKUDEI","VAYIKRA","TZAV",null,"SHMINI","TAZRIA_METZORA","ACHREI_MOS_KEDOSHIM","EMOR","BEHAR_BECHUKOSAI","BAMIDBAR","NASSO","BEHAALOSCHA","SHLACH","KORACH","CHUKAS","BALAK","PINCHAS","MATOS_MASEI","DEVARIM","VAESCHANAN","EIKEV","REEH","SHOFTIM","KI_SEITZEI","KI_SAVO","NITZAVIM"),
        // 4
        arrayOf(null,null,"HAAZINU",null,null,"BERESHIS","NOACH","LECH_LECHA","VAYERA","CHAYEI_SARA","TOLDOS","VAYETZEI","VAYISHLACH","VAYESHEV","MIKETZ","VAYIGASH","VAYECHI","SHEMOS","VAERA","BO","BESHALACH","YISRO","MISHPATIM","TERUMAH","TETZAVEH","KI_SISA","VAYAKHEL_PEKUDEI","VAYIKRA","TZAV",null,"SHMINI","TAZRIA_METZORA","ACHREI_MOS_KEDOSHIM","EMOR","BEHAR_BECHUKOSAI","BAMIDBAR","NASSO","BEHAALOSCHA","SHLACH","KORACH","CHUKAS","BALAK","PINCHAS","MATOS_MASEI","DEVARIM","VAESCHANAN","EIKEV","REEH","SHOFTIM","KI_SEITZEI","KI_SAVO","NITZAVIM"),
        // 5
        arrayOf(null,null,"HAAZINU",null,null,"BERESHIS","NOACH","LECH_LECHA","VAYERA","CHAYEI_SARA","TOLDOS","VAYETZEI","VAYISHLACH","VAYESHEV","MIKETZ","VAYIGASH","VAYECHI","SHEMOS","VAERA","BO","BESHALACH","YISRO","MISHPATIM","TERUMAH","TETZAVEH","KI_SISA","VAYAKHEL_PEKUDEI","VAYIKRA","TZAV",null,"SHMINI","TAZRIA_METZORA","ACHREI_MOS_KEDOSHIM","EMOR","BEHAR_BECHUKOSAI","BAMIDBAR","NASSO","BEHAALOSCHA","SHLACH","KORACH","CHUKAS","BALAK","PINCHAS","MATOS_MASEI","DEVARIM","VAESCHANAN","EIKEV","REEH","SHOFTIM","KI_SEITZEI","KI_SAVO","NITZAVIM_VAYEILECH"),
        // 6
        arrayOf(null,"VAYEILECH","HAAZINU",null,"BERESHIS","NOACH","LECH_LECHA","VAYERA","CHAYEI_SARA","TOLDOS","VAYETZEI","VAYISHLACH","VAYESHEV","MIKETZ","VAYIGASH","VAYECHI","SHEMOS","VAERA","BO","BESHALACH","YISRO","MISHPATIM","TERUMAH","TETZAVEH","KI_SISA","VAYAKHEL","PEKUDEI","VAYIKRA","TZAV","SHMINI","TAZRIA","METZORA",null,"ACHREI_MOS","KEDOSHIM","EMOR","BEHAR","BECHUKOSAI","BAMIDBAR",null,"NASSO","BEHAALOSCHA","SHLACH","KORACH","CHUKAS_BALAK","PINCHAS","MATOS_MASEI","DEVARIM","VAESCHANAN","EIKEV","REEH","SHOFTIM","KI_SEITZEI","KI_SAVO","NITZAVIM_VAYEILECH"),
        // 7
        arrayOf(null,"VAYEILECH","HAAZINU",null,"BERESHIS","NOACH","LECH_LECHA","VAYERA","CHAYEI_SARA","TOLDOS","VAYETZEI","VAYISHLACH","VAYESHEV","MIKETZ","VAYIGASH","VAYECHI","SHEMOS","VAERA","BO","BESHALACH","YISRO","MISHPATIM","TERUMAH","TETZAVEH","KI_SISA","VAYAKHEL","PEKUDEI","VAYIKRA","TZAV","SHMINI","TAZRIA","METZORA",null,null,"ACHREI_MOS","KEDOSHIM","EMOR","BEHAR","BECHUKOSAI","BAMIDBAR","NASSO","BEHAALOSCHA","SHLACH","KORACH","CHUKAS","BALAK","PINCHAS","MATOS_MASEI","DEVARIM","VAESCHANAN","EIKEV","REEH","SHOFTIM","KI_SEITZEI","KI_SAVO","NITZAVIM"),
        // 8
        arrayOf(null,"HAAZINU",null,null,"BERESHIS","NOACH","LECH_LECHA","VAYERA","CHAYEI_SARA","TOLDOS","VAYETZEI","VAYISHLACH","VAYESHEV","MIKETZ","VAYIGASH","VAYECHI","SHEMOS","VAERA","BO","BESHALACH","YISRO","MISHPATIM","TERUMAH","TETZAVEH","KI_SISA","VAYAKHEL","PEKUDEI","VAYIKRA","TZAV","SHMINI","TAZRIA","METZORA","ACHREI_MOS",null,"KEDOSHIM","EMOR","BEHAR","BECHUKOSAI","BAMIDBAR","NASSO","BEHAALOSCHA","SHLACH","KORACH","CHUKAS","BALAK","PINCHAS","MATOS","MASEI","DEVARIM","VAESCHANAN","EIKEV","REEH","SHOFTIM","KI_SEITZEI","KI_SAVO","NITZAVIM"),
        // 9
        arrayOf(null,"HAAZINU",null,null,"BERESHIS","NOACH","LECH_LECHA","VAYERA","CHAYEI_SARA","TOLDOS","VAYETZEI","VAYISHLACH","VAYESHEV","MIKETZ","VAYIGASH","VAYECHI","SHEMOS","VAERA","BO","BESHALACH","YISRO","MISHPATIM","TERUMAH","TETZAVEH","KI_SISA","VAYAKHEL","PEKUDEI","VAYIKRA","TZAV","SHMINI","TAZRIA","METZORA","ACHREI_MOS",null,"KEDOSHIM","EMOR","BEHAR","BECHUKOSAI","BAMIDBAR","NASSO","BEHAALOSCHA","SHLACH","KORACH","CHUKAS","BALAK","PINCHAS","MATOS","MASEI","DEVARIM","VAESCHANAN","EIKEV","REEH","SHOFTIM","KI_SEITZEI","KI_SAVO","NITZAVIM_VAYEILECH"),
        // 10
        arrayOf(null,null,"HAAZINU",null,null,"BERESHIS","NOACH","LECH_LECHA","VAYERA","CHAYEI_SARA","TOLDOS","VAYETZEI","VAYISHLACH","VAYESHEV","MIKETZ","VAYIGASH","VAYECHI","SHEMOS","VAERA","BO","BESHALACH","YISRO","MISHPATIM","TERUMAH","TETZAVEH","KI_SISA","VAYAKHEL","PEKUDEI","VAYIKRA","TZAV","SHMINI","TAZRIA","METZORA",null,"ACHREI_MOS","KEDOSHIM","EMOR","BEHAR","BECHUKOSAI","BAMIDBAR","NASSO","BEHAALOSCHA","SHLACH","KORACH","CHUKAS","BALAK","PINCHAS","MATOS_MASEI","DEVARIM","VAESCHANAN","EIKEV","REEH","SHOFTIM","KI_SEITZEI","KI_SAVO","NITZAVIM_VAYEILECH"),
        // 11
        arrayOf(null,null,"HAAZINU",null,null,"BERESHIS","NOACH","LECH_LECHA","VAYERA","CHAYEI_SARA","TOLDOS","VAYETZEI","VAYISHLACH","VAYESHEV","MIKETZ","VAYIGASH","VAYECHI","SHEMOS","VAERA","BO","BESHALACH","YISRO","MISHPATIM","TERUMAH","TETZAVEH","KI_SISA","VAYAKHEL","PEKUDEI","VAYIKRA","TZAV","SHMINI","TAZRIA","METZORA",null,"ACHREI_MOS","KEDOSHIM","EMOR","BEHAR","BECHUKOSAI","BAMIDBAR",null,"NASSO","BEHAALOSCHA","SHLACH","KORACH","CHUKAS_BALAK","PINCHAS","MATOS_MASEI","DEVARIM","VAESCHANAN","EIKEV","REEH","SHOFTIM","KI_SEITZEI","KI_SAVO","NITZAVIM_VAYEILECH"),
        // 12
        arrayOf(null,"VAYEILECH","HAAZINU",null,"BERESHIS","NOACH","LECH_LECHA","VAYERA","CHAYEI_SARA","TOLDOS","VAYETZEI","VAYISHLACH","VAYESHEV","MIKETZ","VAYIGASH","VAYECHI","SHEMOS","VAERA","BO","BESHALACH","YISRO","MISHPATIM","TERUMAH","TETZAVEH","KI_SISA","VAYAKHEL_PEKUDEI","VAYIKRA","TZAV",null,"SHMINI","TAZRIA_METZORA","ACHREI_MOS_KEDOSHIM","EMOR","BEHAR_BECHUKOSAI","BAMIDBAR","NASSO","BEHAALOSCHA","SHLACH","KORACH","CHUKAS","BALAK","PINCHAS","MATOS_MASEI","DEVARIM","VAESCHANAN","EIKEV","REEH","SHOFTIM","KI_SEITZEI","KI_SAVO","NITZAVIM_VAYEILECH"),
        // 13
        arrayOf(null,"HAAZINU",null,null,"BERESHIS","NOACH","LECH_LECHA","VAYERA","CHAYEI_SARA","TOLDOS","VAYETZEI","VAYISHLACH","VAYESHEV","MIKETZ","VAYIGASH","VAYECHI","SHEMOS","VAERA","BO","BESHALACH","YISRO","MISHPATIM","TERUMAH","TETZAVEH","KI_SISA","VAYAKHEL_PEKUDEI","VAYIKRA","TZAV",null,"SHMINI","TAZRIA_METZORA","ACHREI_MOS_KEDOSHIM","EMOR","BEHAR","BECHUKOSAI","BAMIDBAR","NASSO","BEHAALOSCHA","SHLACH","KORACH","CHUKAS","BALAK","PINCHAS","MATOS_MASEI","DEVARIM","VAESCHANAN","EIKEV","REEH","SHOFTIM","KI_SEITZEI","KI_SAVO","NITZAVIM"),
        // 14
        arrayOf(null,"VAYEILECH","HAAZINU",null,"BERESHIS","NOACH","LECH_LECHA","VAYERA","CHAYEI_SARA","TOLDOS","VAYETZEI","VAYISHLACH","VAYESHEV","MIKETZ","VAYIGASH","VAYECHI","SHEMOS","VAERA","BO","BESHALACH","YISRO","MISHPATIM","TERUMAH","TETZAVEH","KI_SISA","VAYAKHEL","PEKUDEI","VAYIKRA","TZAV","SHMINI","TAZRIA","METZORA",null,"ACHREI_MOS","KEDOSHIM","EMOR","BEHAR","BECHUKOSAI","BAMIDBAR","NASSO","BEHAALOSCHA","SHLACH","KORACH","CHUKAS","BALAK","PINCHAS","MATOS_MASEI","DEVARIM","VAESCHANAN","EIKEV","REEH","SHOFTIM","KI_SEITZEI","KI_SAVO","NITZAVIM_VAYEILECH"),
        // 15
        arrayOf(null,"VAYEILECH","HAAZINU",null,"BERESHIS","NOACH","LECH_LECHA","VAYERA","CHAYEI_SARA","TOLDOS","VAYETZEI","VAYISHLACH","VAYESHEV","MIKETZ","VAYIGASH","VAYECHI","SHEMOS","VAERA","BO","BESHALACH","YISRO","MISHPATIM","TERUMAH","TETZAVEH","KI_SISA","VAYAKHEL","PEKUDEI","VAYIKRA","TZAV","SHMINI","TAZRIA","METZORA",null,"ACHREI_MOS","KEDOSHIM","EMOR","BEHAR","BECHUKOSAI","BAMIDBAR","NASSO","BEHAALOSCHA","SHLACH","KORACH","CHUKAS","BALAK","PINCHAS","MATOS","MASEI","DEVARIM","VAESCHANAN","EIKEV","REEH","SHOFTIM","KI_SEITZEI","KI_SAVO","NITZAVIM"),
        // 16
        arrayOf(null,null,"HAAZINU",null,null,"BERESHIS","NOACH","LECH_LECHA","VAYERA","CHAYEI_SARA","TOLDOS","VAYETZEI","VAYISHLACH","VAYESHEV","MIKETZ","VAYIGASH","VAYECHI","SHEMOS","VAERA","BO","BESHALACH","YISRO","MISHPATIM","TERUMAH","TETZAVEH","KI_SISA","VAYAKHEL","PEKUDEI","VAYIKRA","TZAV","SHMINI","TAZRIA","METZORA",null,"ACHREI_MOS","KEDOSHIM","EMOR","BEHAR","BECHUKOSAI","BAMIDBAR","NASSO","BEHAALOSCHA","SHLACH","KORACH","CHUKAS","BALAK","PINCHAS","MATOS_MASEI","DEVARIM","VAESCHANAN","EIKEV","REEH","SHOFTIM","KI_SEITZEI","KI_SAVO","NITZAVIM_VAYEILECH")
    )

    /**
     * Returns the parsha key (e.g. "BERESHIS", "VAYAKHEL_PEKUDEI") for a given Shabbat,
     * or null if that Shabbat has no parsha (Yom Tov / Chol HaMoed).
     *
     * [dayOfWeek] must be 7 (Saturday). Returns null for non-Shabbat dates.
     */
    fun getParshaKey(year: Int, month: Int, day: Int, dayOfWeek: Int, inIsrael: Boolean): String? {
        if (dayOfWeek != SATURDAY) return null
        val yearType = getParshaYearType(year, inIsrael)
        if (yearType < 0) return null
        val rhDow = getJewishCalendarElapsedDays(year) % 7
        val dayIndex = rhDow + getDaysSinceStartOfJewishYear(year, month, day)
        val schedule = PARSHA_LIST[yearType]
        val slot = dayIndex / 7
        return if (slot in schedule.indices) schedule[slot] else null
    }

    /**
     * Returns the parsha key for the upcoming Shabbat, skipping Shabbatot with no parsha
     * (Yom Tov / Chol HaMoed), matching [JewishCalendar.getUpcomingParshah].
     *
     * [currentDayOfWeek]: 1=Sun … 7=Sat; if Saturday, returns *next* Shabbat's parsha.
     */
    fun getUpcomingParshaKey(
        year: Int, month: Int, day: Int,
        currentDayOfWeek: Int, inIsrael: Boolean
    ): String? {
        // Advance to the upcoming Saturday (next Saturday if today is already Saturday)
        val daysForward = if (currentDayOfWeek == SATURDAY) 7 else (SATURDAY - currentDayOfWeek + 7) % 7

        var sy = year; var sm = month; var sd = day
        repeat(daysForward) {
            val next = incrementDay(sy, sm, sd)
            sy = next.first; sm = next.second; sd = next.third
        }

        // Skip Shabbatot with NONE (e.g. Chol HaMoed, Yom Kippur on Shabbat) — max 8 attempts
        for (attempt in 0..8) {
            val key = getParshaKey(sy, sm, sd, SATURDAY, inIsrael)
            if (key != null) return key
            // Advance 7 days to the next Shabbat
            repeat(7) {
                val next = incrementDay(sy, sm, sd)
                sy = next.first; sm = next.second; sd = next.third
            }
        }
        return null
    }

    // ── Date arithmetic helpers ───────────────────────────────────────────────

    /**
     * Advance one day in the Hebrew calendar; handles month/year boundaries.
     * Returns a Triple(year, month, day).
     *
     * Calendar-order for KosherJava months:
     * TISHREI(7)→CHESHVAN(8)→KISLEV(9)→TEVET(10)→SHEVAT(11)→ADAR(12)
     *   →[ADAR_II(13) in leap]→NISSAN(1)→IYAR(2)→SIVAN(3)→TAMMUZ(4)→AV(5)→ELUL(6)
     *   →TISHREI(7) of the next year
     */
    fun incrementDay(year: Int, month: Int, day: Int): Triple<Int, Int, Int> {
        val maxDay = getDaysInJewishMonth(year, month)
        return if (day < maxDay) {
            Triple(year, month, day + 1)
        } else {
            val nextMonth = when (month) {
                ELUL  -> TISHREI  // 6 → 7, new year
                ADAR  -> if (isJewishLeapYear(year)) ADAR_II else NISSAN  // 12 → 13 or 1
                ADAR_II -> NISSAN // 13 → 1
                else  -> month + 1
            }
            val nextYear = if (month == ELUL) year + 1 else year
            Triple(nextYear, nextMonth, 1)
        }
    }
}
