package com.beardytop.beatzaddik.domain

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Prayer-related checklist rows follow zmanim windows and show friendly gray states
 * when upcoming or past today's ideal time.
 */
object ChecklistZmanEvaluator {

    private val zmanItemIds = setOf(
        "kiddush_levana",
        "morning_blessings_birchot_hashachar",
        "torah_blessings_minimal_torah_study",
        "minimum_pesukei_d_zimra",
        "morning_shema_with_its_blessings",
        "shemoneh_esrei_tachanun",
        "put_on_tefillin_during_morning_prayers_except_shabbat_festiv",
        "musaf_only_on_rosh_chodesh_festivals_and_shabbat",
        "mincha_shemoneh_esrei_tachanun",
        "evening_shema_with_its_blessings",
        "maariv_shemoneh_esrei",
        "rosh_chodesh_half_hallel",
        "rosh_chodesh_full_hallel_chanukah",
        "chanukah_full_hallel",
        "chol_hamoed_full_hallel",
        "chol_hamoed_half_hallel",
        "yaaleh_vyavo_rosh_chodesh_shacharit",
        "yaaleh_vyavo_rosh_chodesh_mincha",
        "yaaleh_vyavo_rosh_chodesh",
        "yaaleh_vyavo_chol_hamoed_shacharit",
        "yaaleh_vyavo_chol_hamoed_mincha",
        "yaaleh_vyavo_chol_hamoed_maariv",
        "rosh_chodesh_observances",
        "bedtime_shema_first_paragraph_though_recommended_to_say_enti",
        "bedtime_shema_women",
        "hamapil_blessing_according_to_many_opinions",
        "at_least_one_prayer_daily_typically_morning",
        "ashkenaz_korbanot_before_shacharit",
        "chabad_korbanot_before_shacharit",
        "sefard_birkat_kohanim",
        "sefard_korbanot_before_mincha",
        "edot_hamizrach_korbanot_before_mincha",
        "ldovid_hashem_ori_shacharit",
        "ldovid_hashem_ori_maariv",
        "erev_pesach_biur_chametz",
        "erev_pesach_mechirat_chametz",
        "bedikat_chametz",
        "birkat_hachamah",
        "birkat_hailanot",
        "havdalah",
        "melave_malkah",
        "shabbat_candles",
        "yom_tov_candles",
        "public_fast_day",
        "tefillin_tisha_beav_mincha",
        "motzei_yom_kippur_meal",
        "purim_megillah",
        "purim_matanot_laevyonim",
        "purim_mishloach_manot",
        "purim_seudah",
        "purim_meshulash_erev_megillah",
        "purim_meshulash_friday_megillah",
        "purim_meshulash_friday_matanot",
        "purim_meshulash_sunday_mishloach",
        "purim_meshulash_sunday_seudah",
        "eruv_tavshilin",
        "selichot_elul_ashkenaz",
        "selichot_elul_chabad",
        "selichot_elul_sefard",
        "selichot_elul_edot_hamizrach",
        "selichot_elul_other",
        "sukkot_arba_minim",
        "hoshana_rabbah_aravot",
        "erev_yom_kippur_eat",
        "erev_tisha_beav_prep",
        "erev_pesach_prepare_seder",
        "erev_chag_prep",
        "yom_tov_shabbat_advance_prep",
        "erev_chanukah_prep",
        "erev_pesach_taanit_bechorot",
    ) + TachanunRules.tachanunItemIds

    fun appliesTo(itemId: String): Boolean =
        itemId in zmanItemIds ||
            itemId.startsWith("chanukah_lighting_day_") ||
            itemId.startsWith("zecher_machatzit_hashekel_")

    fun evaluate(
        itemId: String,
        nowMillis: Long,
        zmanim: ZmanimSnapshot?,
        prayerDay: PrayerDayContext,
        cal: DayInfo? = null,
        yesterdayCal: DayInfo? = null,
        tomorrowCal: DayInfo? = null,
    ): ItemZmanStatus {
        if (!appliesTo(itemId)) return ItemZmanStatus()
        if (zmanim == null) return ItemZmanStatus()
        val z = zmanim
        if (!z.hasLocationTimes) {
            return ItemZmanStatus()
        }
        val tz = z.timezoneId
        // Chanukah candles: earliest plag hamincha; ideal after tzeit.
        // We treat it as available from plag so it doesn't look "unavailable" all afternoon.
        // Friday: must light before Shabbat candles — window ends at candle-lighting time.
        if (itemId.startsWith("chanukah_lighting_day_")) {
            val rawPlag = z.plagHaminchaMillis
                ?: return ItemZmanStatus(hint = "Set location in Settings for Plag HaMincha times.")
            val nightEnd = ZmanPeriodLogic.nightObligationWindowEnd(z) ?: return ItemZmanStatus()

            // Chanukah lighting "belongs" to the night that began after sunset.
            // Some backends attach night zmanim to the next civil date; in that case, a 10pm
            // simulation could be before today's (next-day) plag/tzeit. Correct by shifting
            // relevant times back by 24h when we're before dawn.
            val dawn = z.alotHaShacharMillis ?: z.sunriseMillis
            val shiftBack = (dawn != null && nowMillis < dawn)
            val plag = if (shiftBack) rawPlag - 24 * 60 * 60 * 1000L else rawPlag

            val plagClock = ZmanimFormatter.formatAfter(plag, tz) ?: "Plag HaMincha"
            // Shift tzeit back for pre-dawn (Sunday Motzei) or Saturday night after Hebrew rollover.
            val tzeitForThisNight = z.tzeitMillis?.let { tzeit ->
                val motzeiSatNight =
                    cal?.startedTonightAtTzeit == true && cal.date.dayOfWeek == DayOfWeek.SATURDAY
                if (shiftBack || motzeiSatNight) tzeit - 24 * 60 * 60 * 1000L else tzeit
            }
            val burnUntilMillis = tzeitForThisNight?.plus(30 * 60 * 1000L)
            val burnUntilTime = ZmanimFormatter.formatTime(burnUntilMillis, tz)
            val burnNoteAlways = "Candles must have enough oil/wick to burn at least 30 minutes after nightfall."
            val burnNoteWithTime = burnUntilTime?.let {
                "If lighting early, ensure enough oil/candles to burn until $it (30 min after nightfall)."
            } ?: burnNoteAlways
            // Friday erev-Shabbat cutoff applies only to Friday afternoon/evening before tzeit —
            // not to Thursday night when shiftBack is true on civil Friday pre-dawn.
            // Friday Chanukah cutoff: real Friday before tzeit only (tonightBeginsShabbat).
            val fridayCandleEnd = if (prayerDay.tonightBeginsShabbat && !shiftBack) {
                z.sunsetMillis?.let { it - prayerDay.candleLeadMinutes * 60_000L }
            } else {
                null
            }
            // After Friday tzeit rollover, Hebrew day is Shabbat (isErevShabbat false) — keep the
            // missed Friday deadline so the item shows EXPIRED through Shabbat, not ACTIVE.
            val missedFridayCandleEnd = if (
                fridayCandleEnd == null &&
                !shiftBack &&
                cal != null &&
                HolyDayPhoneRules.isShabbatMelachaDay(cal) &&
                !cal.startedTonightAtTzeit
            ) {
                z.sunsetMillis?.let { it - prayerDay.candleLeadMinutes * 60_000L }
                    ?.minus(24 * 60 * 60 * 1000L)
            } else {
                null
            }
            val fridayEnd = fridayCandleEnd ?: missedFridayCandleEnd
            // After tzeit rollover, nightObligationWindowEnd is the *next* dawn — use this night's alot.
            val end = when {
                fridayEnd != null -> fridayEnd
                shiftBack && dawn != null -> dawn
                else -> nightEnd
            }
            val motzeiShabbatLighting = cal != null && cal.isChanukah && (
                (tomorrowCal != null && MotzeiShabbatWindow.isActive(cal, tomorrowCal, nowMillis)) ||
                    (cal.startedTonightAtTzeit && cal.date.dayOfWeek == DayOfWeek.SATURDAY) ||
                    (!cal.startedTonightAtTzeit &&
                        cal.date.dayOfWeek == DayOfWeek.SUNDAY &&
                        shiftBack)
                )
            val shabbatBeforeMotzei = cal != null &&
                HolyDayPhoneRules.isShabbatMelachaDay(cal) &&
                !cal.startedTonightAtTzeit &&
                fridayEnd == null
            val start = when {
                motzeiShabbatLighting && tzeitForThisNight != null -> tzeitForThisNight
                shabbatBeforeMotzei && tzeitForThisNight != null -> tzeitForThisNight
                else -> plag
            }
            val startClock = if (motzeiShabbatLighting || shabbatBeforeMotzei) {
                ZmanimFormatter.formatAfter(start, tz) ?: "nightfall"
            } else {
                plagClock
            }
            val fridayHint = fridayCandleEnd?.let {
                val clock = ZmanimFormatter.formatTime(it, tz) ?: "Shabbat candle lighting"
                "On Friday light Chanukah candles before Shabbat candles ($clock)."
            }
            val motzeiHint = if (motzeiShabbatLighting) {
                "Motzei Shabbat: light after Shabbat ends (tzeit) — many light after Havdalah / Baruch ha'mavdil; only from a pre-existing flame."
            } else {
                null
            }
            val startLabel = when {
                motzeiShabbatLighting || shabbatBeforeMotzei -> "tzeit hakochavim"
                else -> "Plag HaMincha"
            }
            val expiredMakeup = when {
                fridayEnd != null ->
                    "Friday Chanukah lighting must be before Shabbat candles. If you missed Friday's lighting, do not light on Shabbat. Motzei Shabbat after tzeit is Saturday night's candles (from a pre-existing flame) — not a bracha-makeup for Friday; ask your rav about a missed night."
                motzeiShabbatLighting ->
                    "If you missed Motzei Shabbat Chanukah lighting before dawn, ask your rav about lighting later Sunday."
                else ->
                    "If you missed lighting after nightfall, ask your rav whether you may still light tonight, or light tomorrow night without a bracha for the missed night."
            }
            return when {
                nowMillis < start -> ItemZmanStatus(
                    availability = ItemZmanAvailability.UPCOMING,
                    windowStartMillis = start,
                    windowEndMillis = end,
                    hintTemplate = "Available in {time} · at $startLabel",
                    hintArgs = mapOf("time" to startClock),
                    makeupNote = listOfNotNull(burnNoteWithTime, fridayHint, motzeiHint).joinToString(" "),
                    availableAtLabel = startLabel,
                )
                nowMillis >= end -> ItemZmanStatus(
                    availability = ItemZmanAvailability.EXPIRED,
                    makeupNote = expiredMakeup,
                )
                else -> ItemZmanStatus(
                    availability = ItemZmanAvailability.ACTIVE,
                    windowStartMillis = start,
                    windowEndMillis = end,
                    hint = listOfNotNull(burnNoteAlways, fridayHint, motzeiHint).joinToString(" "),
                    availableAtLabel = startLabel,
                )
            }
        }

        // Persist-checked seasonal id includes the Hebrew year suffix.
        if (itemId.startsWith("zecher_machatzit_hashekel_")) {
            return purimDayUntilSunsetWindow(
                nowMillis, z, tz,
                label = "Zecher LeMachatzit HaShekel",
                activeHint = "Give the half-shekel memorial during the daytime, before sunset.",
                expired = "Today's Zecher LeMachatzit HaShekel window has passed (before sunset).",
            )
        }

        return when (itemId) {
            // Birchot HaShachar / Birkat HaTorah — available after chatzos halayla with no hard
            // daytime cutoff; say them when you wake.
            "morning_blessings_birchot_hashachar" -> afterNightChatzosWindow(
                nowMillis, z, tz, label = "Birchot HaShachar"
            )
            "torah_blessings_minimal_torah_study" -> afterNightChatzosWindow(
                nowMillis, z, tz, label = "Birkat HaTorah"
            )
            // Korbanot / Pesukei DeZimra — part of Shacharit; valid until halachic midday (chatzos)
            "ashkenaz_korbanot_before_shacharit",
            "chabad_korbanot_before_shacharit" -> shacharitPartsWindow(
                nowMillis, z, tz, label = "Korbanot"
            )
            "sefard_birkat_kohanim" -> birkatKohanimWindow(nowMillis, z, tz, prayerDay)
            "ashkenaz_shacharit_tachanun",
            "sefard_shacharit_tachanun",
            "edot_hamizrach_shacharit_tachanun",
            "chabad_shacharit_tachanun" -> shacharitPartsWindow(
                nowMillis, z, tz, label = "Tachanun at Shacharit",
            )
            "ldovid_hashem_ori_shacharit" -> ldovidShacharitWindow(nowMillis, z, tz)
            "ldovid_hashem_ori_maariv" -> ldovidMaarivWindow(nowMillis, z, tz)
            "erev_pesach_biur_chametz" -> biurChametzWindow(nowMillis, z, tz)
            "erev_pesach_mechirat_chametz" ->
                mechiratChametzWindow(nowMillis, z, tz, cal, prayerDay)
            "bedikat_chametz" -> bedikatChametzWindow(nowMillis, z, tz)
            "selichot_elul_ashkenaz",
            "selichot_elul_chabad",
            "selichot_elul_sefard",
            "selichot_elul_edot_hamizrach",
            "selichot_elul_other" -> selichotWindow(nowMillis, z, tz, prayerDay)
            "sefard_korbanot_before_mincha",
            "edot_hamizrach_korbanot_before_mincha" -> minchaWindow(
                nowMillis, z, tz, label = "Korbanot before Mincha", prayerDay = prayerDay,
            )
            "minimum_pesukei_d_zimra" -> shacharitPartsWindow(
                nowMillis, z, tz, label = "Pesukei DeZimra"
            )
            // Morning Shema — expires at sof zman Shema (end of 3rd halachic hour), no later
            "morning_shema_with_its_blessings" -> shemaWindow(nowMillis, z, tz)
            // Shacharit Amidah — from dawn; ideal from sunrise; until chatzos bedi'eved
            "shemoneh_esrei_tachanun" -> amidahWindow(nowMillis, z, tz)
            // Tefillin — from misheyakir until sunset (not Shabbat/Yom Tov)
            "put_on_tefillin_during_morning_prayers_except_shabbat_festiv" -> {
                if (prayerDay.isTefillinOmittedCalendarDay) {
                    return ItemZmanStatus(
                        availability = ItemZmanAvailability.EXPIRED,
                        hint = "Tefillin are not worn on Shabbat or Yom Tov (melacha days)."
                    )
                }
                // CHM / TBA morning omit are filtered in ChecklistEngine (hide row).
                if (TefillinSeasonalRules.shouldOmitTefillinOnCholHamoed(prayerDay)) {
                    return ItemZmanStatus(
                        availability = ItemZmanAvailability.UPCOMING,
                        hint = TefillinSeasonalRules.cholHamoedOmittedHint(),
                    )
                }
                if (TishaBeavTefillinRules.isTishaBeav(prayerDay.fastDayIndex) &&
                    TishaBeavTefillinRules.omitsMorningTefillin(prayerDay.nusach)
                ) {
                    return ItemZmanStatus(
                        availability = ItemZmanAvailability.UPCOMING,
                        hint = TishaBeavTefillinRules.morningOmittedHint(),
                    )
                }
                val base = tefillinWindow(nowMillis, z, tz)
                when {
                    prayerDay.isCholHamoed &&
                        prayerDay.nusach == EffectiveNusach.ASHKENAZ &&
                        !prayerDay.isInIsrael &&
                        base.availability == ItemZmanAvailability.ACTIVE ->
                        base.copy(hint = TefillinSeasonalRules.cholHamoedAshkenazActiveHint())
                    prayerDay.isCholHamoed &&
                        prayerDay.nusach == EffectiveNusach.OTHER &&
                        !prayerDay.isInIsrael &&
                        base.availability == ItemZmanAvailability.ACTIVE ->
                        base.copy(hint = TefillinSeasonalRules.cholHamoedOtherActiveHint())
                    TishaBeavTefillinRules.isTishaBeav(prayerDay.fastDayIndex) &&
                        prayerDay.nusach == EffectiveNusach.OTHER &&
                        base.availability == ItemZmanAvailability.ACTIVE ->
                        base.copy(
                            hint = "Tisha B'Av: morning tallit/tefillin customs vary — follow your kehilla. Mincha donning after chatzos is widespread.",
                        )
                    TishaBeavTefillinRules.isTishaBeav(prayerDay.fastDayIndex) &&
                        !TishaBeavTefillinRules.omitsMorningTefillin(prayerDay.nusach) &&
                        base.availability == ItemZmanAvailability.ACTIVE ->
                        base.copy(hint = TishaBeavTefillinRules.sephardiMorningHint())
                    else -> base
                }
            }
            "tefillin_tisha_beav_mincha" -> tefillinTishaBeavMinchaWindow(nowMillis, z, tz)
            "musaf_only_on_rosh_chodesh_festivals_and_shabbat" ->
                musafWindow(nowMillis, z, tz, prayerDay)
            "ashkenaz_mincha_tachanun",
            "sefard_mincha_tachanun",
            "edot_hamizrach_mincha_tachanun",
            "chabad_mincha_tachanun",
            -> minchaWindow(nowMillis, z, tz, label = "Tachanun at Mincha", prayerDay = prayerDay)
            "mincha_shemoneh_esrei_tachanun" -> minchaWindow(nowMillis, z, tz, label = "Mincha", prayerDay = prayerDay)
            "evening_shema_with_its_blessings" -> eveningShemaWindow(nowMillis, z, tz)
            "maariv_shemoneh_esrei" -> maarivWindow(nowMillis, z, tz, prayerDay)
            "rosh_chodesh_half_hallel",
            "rosh_chodesh_full_hallel_chanukah",
            "chanukah_full_hallel",
            "chol_hamoed_full_hallel",
            "chol_hamoed_half_hallel" -> shacharitPartsWindow(
                nowMillis, z, tz, label = "Hallel at Shacharit",
            )
            "yaaleh_vyavo_rosh_chodesh_shacharit",
            "yaaleh_vyavo_chol_hamoed_shacharit" -> yaalehVyavoShacharitWindow(nowMillis, z, tz)
            "yaaleh_vyavo_rosh_chodesh_mincha",
            "yaaleh_vyavo_chol_hamoed_mincha" -> yaalehVyavoMinchaWindow(nowMillis, z, tz, prayerDay)
            "yaaleh_vyavo_rosh_chodesh",
            "yaaleh_vyavo_chol_hamoed_maariv" -> yaalehVyavoMaarivWindow(nowMillis, z, tz)
            "rosh_chodesh_observances" -> roshChodeshObservancesWindow(nowMillis, z, tz, cal)
            "bedtime_shema_first_paragraph_though_recommended_to_say_enti",
            "bedtime_shema_women" ->
                bedtimeWindow(nowMillis, z, tz, "Bedtime Shema")
            "hamapil_blessing_according_to_many_opinions" ->
                bedtimeWindow(nowMillis, z, tz, "Hamapil blessing")
            "at_least_one_prayer_daily_typically_morning" -> womenDailyPrayerWindow()
            "kiddush_levana" -> kiddushLevanaWindow(nowMillis, z, tz, prayerDay)
            "birkat_hachamah" -> birkatHachamahWindow(nowMillis, z, tz)
            "birkat_hailanot" -> birkatHaIlanotHint(prayerDay)
            "havdalah" -> havdalahWindow(
                nowMillis, z, tz, prayerDay, cal, yesterdayCal, tomorrowCal,
            )
            "melave_malkah" -> melaveMalkaWindow(nowMillis, z, tz, prayerDay)
            "shabbat_candles" -> shabbatCandlesWindow(nowMillis, z, tz, prayerDay)
            "yom_tov_candles" -> yomTovCandlesWindow(nowMillis, z, tz, prayerDay)
            "public_fast_day" -> publicFastWindow(nowMillis, z, tz, prayerDay.fastDayIndex)
            "motzei_yom_kippur_meal" -> motzeiYomKippurMealWindow(nowMillis, z, tz, cal)
            "purim_megillah" -> purimMegillahCombinedWindow(nowMillis, z, tz, cal)
            "purim_matanot_laevyonim" -> purimDayUntilSunsetWindow(
                nowMillis, z, tz,
                label = "Matanot la'evyonim",
                activeHint = "Give gifts to the poor during Purim daytime, before sunset.",
                expired = "Today's matanot la'evyonim window has passed (before sunset).",
            )
            "purim_mishloach_manot" -> purimDayUntilSunsetWindow(
                nowMillis, z, tz,
                label = "Mishloach manot",
                activeHint = "Deliver mishloach manot during Purim daytime, before sunset.",
                expired = "Today's mishloach manot window has passed (before sunset).",
            )
            "purim_seudah" -> purimDayUntilSunsetWindow(
                nowMillis, z, tz,
                label = "Purim seudah",
                activeHint = "Hold the Purim seudah during the daytime (often afternoon); finish before sunset.",
                expired = "Today's Purim seudah window has passed (before sunset).",
            )
            "purim_meshulash_erev_megillah" -> purimMegillahNightWindow(nowMillis, z, tz, cal)
            "purim_meshulash_friday_megillah" -> purimMegillahDayWindow(nowMillis, z, tz, "Friday daytime Megillah")
            "purim_meshulash_friday_matanot" -> purimMatanotDayWindow(nowMillis, z, tz)
            "purim_meshulash_sunday_mishloach" -> purimDayUntilSunsetWindow(
                nowMillis, z, tz,
                label = "Mishloach manot",
                activeHint = "Deliver mishloach manot during the daytime, before sunset.",
                expired = "Today's mishloach manot window has passed (before sunset).",
            )
            "purim_meshulash_sunday_seudah" -> purimDayUntilSunsetWindow(
                nowMillis, z, tz,
                label = "Purim seudah",
                activeHint = "Hold the Purim seudah during the daytime; finish before sunset.",
                expired = "Today's Purim seudah window has passed (before sunset).",
            )
            "eruv_tavshilin" -> eruvTavshilinWindow(nowMillis, z, tz, prayerDay)
            "sukkot_arba_minim" -> arbaMinimWindow(nowMillis, z, tz)
            "hoshana_rabbah_aravot" -> hoshanaAravotWindow(nowMillis, z, tz)
            "erev_yom_kippur_eat" -> erevFinishBeforeNightWindow(
                nowMillis, z, tz, prayerDay,
                label = "Erev Yom Kippur meal",
                endAtCandles = true,
                activeHint = "Eat a festive meal today — finish before candle lighting / the fast begins.",
                expired = "The Erev Yom Kippur eating window has closed (past candle lighting).",
                makeup = "If the fast has begun, do not eat — ask your rav only if you are unsure about medical needs.",
            )
            "erev_tisha_beav_prep" -> erevFinishBeforeNightWindow(
                nowMillis, z, tz, prayerDay,
                label = "Erev Tisha B'Av meal",
                // Ordinary erev: finish before sunset. Friday deferred (Shabbat TBA): before candles.
                endAtCandles = prayerDay.tonightBeginsShabbat,
                activeHint = if (prayerDay.tonightBeginsShabbat) {
                    "Finish the seudah hamafseket before Shabbat candles — mourning restrictions begin at sunset."
                } else {
                    "Finish the seudah hamafseket before sunset — the fast begins at nightfall."
                },
                expired = if (prayerDay.tonightBeginsShabbat) {
                    "The Erev Tisha B'Av meal window has closed (past candle lighting)."
                } else {
                    "The Erev Tisha B'Av meal window has closed (past sunset)."
                },
                makeup = "If the fast has begun, do not eat — ask your rav only for medical questions.",
            )
            "erev_pesach_prepare_seder" -> erevFinishBeforeNightWindow(
                nowMillis, z, tz, prayerDay,
                label = "Seder prep",
                endAtCandles = true,
                activeHint = "Finish Seder prep before candle lighting / nightfall.",
                expired = "Today's Seder-prep window has closed (past candle lighting).",
                makeup = "Prepare what you still can after nightfall only where melacha / prep rules allow — ask your rav if unsure.",
            )
            "erev_chag_prep" -> erevFinishBeforeNightWindow(
                nowMillis, z, tz, prayerDay,
                label = "Erev chag prep",
                endAtCandles = true,
                activeHint = "Finish festival prep before candle lighting.",
                expired = "Today's erev-chag prep window has closed (past candle lighting).",
                makeup = "After the festival begins, only do what Yom Tov / Shabbat laws permit.",
            )
            "yom_tov_shabbat_advance_prep" -> erevFinishBeforeNightWindow(
                nowMillis, z, tz, prayerDay,
                label = "Shabbat / erev-chag advance prep",
                endAtCandles = true,
                activeHint = "Read and prepare today before Shabbat candles" +
                    " (Eruv Tavshilin if that row is also listed).",
                expired = "Today's advance-prep window has closed (past candle lighting).",
                makeup = "On Shabbat, follow Shabbat law only — do not use the phone for prep reminders.",
            )
            "erev_chanukah_prep" -> erevChanukahPrepWindow(nowMillis, z, tz, prayerDay)
            "erev_pesach_taanit_bechorot" -> taanitBechorotWindow(nowMillis, z, tz)
            else -> ItemZmanStatus()
        }
    }

    /**
     * Birchot HaShachar / Birkat HaTorah — available from after chatzos halayla.
     * No hard cutoff: say them when you wake, any time of day.
     */
    private fun afterNightChatzosWindow(
        now: Long,
        z: ZmanimSnapshot,
        tz: String,
        label: String
    ): ItemZmanStatus {
        val start = ZmanimHelpers.birchotHashacharWindowStart(now, z)
        return windowStatus(
            now = now,
            start = start,
            end = null,
            upcoming = "Available after halachic midnight (chatzos halayla) — later tonight. Most say $label when they wake, even after a nap.",
            upcomingTemplate = "Available after halachic midnight (chatzos halayla) — {time}. Most say {label} when they wake, even after a nap.",
            upcomingArgs = mapOf(
                "time" to (ZmanimFormatter.formatAfter(start, tz) ?: "later tonight"),
                "label" to label,
            ),
            expired = "$label can be said any time during the day.",
            expiredTemplate = "{label} can be said any time during the day.",
            expiredArgs = mapOf("label" to label),
            makeup = null,
            availableAtLabel = "halachic midnight"
        )
    }

    /** L'Dovid at the end of Shacharit — dawn through chatzos (with Musaf / morning service). */
    private fun ldovidShacharitWindow(now: Long, z: ZmanimSnapshot, tz: String): ItemZmanStatus {
        val start = z.alotHaShacharMillis ?: z.misheyakirMillis ?: z.sunriseMillis
        val end = z.chatzosMillis
        return windowStatus(
            now, start, end,
            upcoming = "L'Dovid (Psalm 27) — say at the end of Shacharit (after Musaf when there is Musaf).",
            upcomingTemplate = "L'Dovid (Psalm 27) — available after dawn ({time}).",
            upcomingArgs = mapOf(
                "time" to (ZmanimFormatter.formatAfter(start, tz) ?: "after dawn"),
            ),
            expired = "This morning's L'Dovid window has closed (after chatzos).",
            makeup = null,
            availableAtLabel = "dawn",
            activeHint = "Say at the end of Shacharit — after Musaf on days with Musaf; follow your siddur.",
        )
    }

    /** L'Dovid at Maariv — many minhagim (especially Ashkenaz) during Elul–Tishrei. */
    private fun ldovidMaarivWindow(now: Long, z: ZmanimSnapshot, tz: String): ItemZmanStatus {
        val start = ZmanPeriodLogic.effectiveEveningStart(now, z)
        val end = ZmanPeriodLogic.effectiveEveningEnd(now, z)
            ?: ZmanPeriodLogic.nightObligationWindowEnd(z)
        return windowStatus(
            now, start, end,
            upcoming = "L'Dovid (Psalm 27) — many communities also say it at Maariv after nightfall.",
            upcomingTemplate = "L'Dovid (Psalm 27) at Maariv — available {time}.",
            upcomingArgs = mapOf(
                "time" to (ZmanimFormatter.formatAfter(start, tz) ?: "after nightfall"),
            ),
            expired = "Tonight's L'Dovid (Maariv) window has closed.",
            makeup = null,
            availableAtLabel = "nightfall",
            activeHint = "Many communities (especially Ashkenaz) recite L'Dovid at Maariv — follow your siddur.",
        )
    }

    /**
     * Biur chametz — from dawn until sof zman biur (end of the 5th halachic hour).
     */
    private fun biurChametzWindow(now: Long, z: ZmanimSnapshot, tz: String): ItemZmanStatus {
        val start = z.alotHaShacharMillis ?: z.sunriseMillis
        val end = ZmanimHelpers.endOfHalachicHourMillis(z, 5) ?: z.chatzosMillis
        val endClock = ZmanimFormatter.formatTime(end, tz) ?: "end of the 5th hour"
        return windowStatus(
            now, start, end,
            upcoming = "Biur chametz is done in the morning after dawn, before the 5th-hour deadline.",
            upcomingTemplate = "Biur chametz — available after dawn ({time}).",
            upcomingArgs = mapOf(
                "time" to (ZmanimFormatter.formatAfter(start, tz) ?: "after dawn"),
            ),
            expired = "Sof zman biur has passed ($endClock). Ask your rav about any remaining chametz.",
            expiredTemplate = "Sof zman biur has passed ({time}). Ask your rav about any remaining chametz.",
            expiredArgs = mapOf("time" to endClock),
            makeup = "If you still have unsold chametz, ask your rav immediately.",
            availableAtLabel = "dawn",
            activeHintTemplate = "Complete biur and Kol Chamira before {time} (end of the 5th halachic hour).",
            activeHintArgs = mapOf("time" to endClock),
        )
    }

    /**
     * Mechirat chametz — must be wired here (not festival-prep bypass) so deadline days
     * actually expire. Early authorize days stay open until sunset; the final day uses the
     * same 5th-hour cutoff as biur (Friday Erev Pesach), candle-lighting (when Erev is Shabbat),
     * or sunset (ordinary last authorize day).
     */
    private fun mechiratChametzWindow(
        now: Long,
        z: ZmanimSnapshot,
        tz: String,
        cal: DayInfo?,
        prayerDay: PrayerDayContext,
    ): ItemZmanStatus {
        val start = z.alotHaShacharMillis ?: z.sunriseMillis
        if (cal == null || !ErevPesachPrepText.isPesachPrepWindow(cal)) {
            return ItemZmanStatus()
        }
        val dow = ErevPesachPrepText.pesachErevDow(cal) ?: return ItemZmanStatus()
        val day = cal.hebrewDay ?: return ItemZmanStatus()
        val last = ErevPesachPrepText.mechiratAuthorizeThroughNissanDay(dow)
        if (day < last) {
            val end = z.sunsetMillis
            return windowStatus(
                now, start, end,
                upcoming = "Mechirat chametz — authorize the sale with your rabbi (before Erev Pesach).",
                upcomingTemplate = "Mechirat chametz — available after dawn ({time}).",
                upcomingArgs = mapOf(
                    "time" to (ZmanimFormatter.formatAfter(start, tz) ?: "after dawn"),
                ),
                expired = "Authorize mechirat chametz with your rabbi before Erev Pesach — do not wait until the last morning.",
                makeup = "Contact your rabbi or a reliable online mechirat form immediately.",
                availableAtLabel = "dawn",
                activeHint = "Authorize mechirat chametz with your rabbi soon — do not wait until Erev Pesach day.",
            )
        }
        return when (dow) {
            ErevPesachPrepText.PesachErevDow.FRIDAY -> {
                // Sale / forms close with sof zman biur (5th hour) on Friday Erev Pesach.
                val end = ZmanimHelpers.endOfHalachicHourMillis(z, 5) ?: z.chatzosMillis
                val endClock = ZmanimFormatter.formatTime(end, tz) ?: "end of the 5th hour"
                windowStatus(
                    now, start, end,
                    upcoming = "Mechirat chametz — complete by the 5th-hour morning deadline.",
                    upcomingTemplate = "Mechirat chametz — available after dawn ({time}); deadline is the 5th hour.",
                    upcomingArgs = mapOf(
                        "time" to (ZmanimFormatter.formatAfter(start, tz) ?: "after dawn"),
                    ),
                    expired = "The mechirat chametz morning deadline has passed ($endClock). Ask your rav immediately.",
                    expiredTemplate = "The mechirat chametz morning deadline has passed ({time}). Ask your rav immediately.",
                    expiredArgs = mapOf("time" to endClock),
                    makeup = "Ask your rav about any unsold chametz right away.",
                    availableAtLabel = "dawn",
                    activeHintTemplate = "Complete mechirat authorization before {time} (end of the 5th halachic hour).",
                    activeHintArgs = mapOf("time" to endClock),
                )
            }
            ErevPesachPrepText.PesachErevDow.SHABBAT -> {
                // Last authorize day is Friday 13 Nisan — finish before Shabbat candles.
                val sunset = z.sunsetMillis
                val end = sunset?.let { it - prayerDay.candleLeadMinutes * 60_000L } ?: sunset
                val endClock = ZmanimFormatter.formatTime(end, tz) ?: "candle lighting"
                windowStatus(
                    now, start, end,
                    upcoming = "Mechirat chametz — complete before Shabbat begins tonight.",
                    upcomingTemplate = "Mechirat chametz — available after dawn ({time}); finish before Shabbat candles.",
                    upcomingArgs = mapOf(
                        "time" to (ZmanimFormatter.formatAfter(start, tz) ?: "after dawn"),
                    ),
                    expired = "Shabbat has begun — mechirat chametz should already be authorized. Ask your rav if unsure.",
                    makeup = "Ask your rav immediately about any unsold chametz.",
                    availableAtLabel = "dawn",
                    activeHintTemplate = "Finish authorizing mechirat chametz before {time} (Shabbat candles).",
                    activeHintArgs = mapOf("time" to endClock),
                )
            }
            ErevPesachPrepText.PesachErevDow.WEEKDAY -> {
                val end = z.sunsetMillis
                val endClock = ZmanimFormatter.formatTime(end, tz) ?: "sunset"
                windowStatus(
                    now, start, end,
                    upcoming = "Mechirat chametz — last day to authorize with your rabbi.",
                    upcomingTemplate = "Mechirat chametz — last day; available after dawn ({time}).",
                    upcomingArgs = mapOf(
                        "time" to (ZmanimFormatter.formatAfter(start, tz) ?: "after dawn"),
                    ),
                    expired = "Today's mechirat authorize window has closed ($endClock). Ask your rav immediately.",
                    expiredTemplate = "Today's mechirat authorize window has closed ({time}). Ask your rav immediately.",
                    expiredArgs = mapOf("time" to endClock),
                    makeup = "Ask your rav immediately about any unsold chametz.",
                    availableAtLabel = "dawn",
                    activeHintTemplate = "Last day — authorize mechirat chametz before {time}.",
                    activeHintArgs = mapOf("time" to endClock),
                )
            }
        }
    }

    /**
     * Birkat Kohanim — Shacharit duchening until chatzos.
     * On public fast days Kohanim also duchen at Mincha → keep open until sunset.
     */
    private fun birkatKohanimWindow(
        now: Long,
        z: ZmanimSnapshot,
        tz: String,
        prayerDay: PrayerDayContext,
    ): ItemZmanStatus {
        val start = z.alotHaShacharMillis ?: z.misheyakirMillis ?: z.sunriseMillis
        val onPublicFast = prayerDay.fastDayIndex != null
        val end = if (onPublicFast) {
            z.sunsetMillis ?: z.chatzosMillis
        } else {
            z.chatzosMillis
        }
        val endLabel = if (onPublicFast) "sunset" else "halachic midday (chatzos)"
        return windowStatus(
            now, start, end,
            upcoming = "Birkat Kohanim — during Shacharit when Kohanim are present.",
            upcomingTemplate = "Birkat Kohanim — available after dawn ({time}) when Kohanim are present.",
            upcomingArgs = mapOf(
                "time" to (ZmanimFormatter.formatAfter(start, tz) ?: "after dawn"),
            ),
            expired = if (onPublicFast) {
                "Today's Birkat Kohanim window has closed (after sunset)."
            } else {
                "The morning Birkat Kohanim window has closed (past $endLabel)."
            },
            makeup = null,
            availableAtLabel = "dawn",
            activeHint = if (onPublicFast) {
                "Public fast day: Birkat Kohanim at Shacharit and often again at Mincha (if Kohanim are present) — until sunset."
            } else {
                "Said during Shacharit when Kohanim are present — not after the morning service."
            },
        )
    }

    /**
     * Bedikat chametz — after nightfall (tzeit) until dawn.
     * Source: SA O.C. 431 — search on the night of 14 Nisan after tzeit.
     */
    private fun bedikatChametzWindow(now: Long, z: ZmanimSnapshot, tz: String): ItemZmanStatus {
        val start = HalachicNightWindow.nightStartMillis(now, z)
        val end = ZmanPeriodLogic.effectiveEveningEnd(now, z)
            ?: z.alotHaShacharMillis
            ?: z.sunriseMillis
        return windowStatus(
            now, start, end,
            upcoming = "Bedikat chametz — search after nightfall (tzeit) tonight.",
            upcomingTemplate = "Bedikat chametz — search after nightfall ({time}).",
            upcomingArgs = mapOf(
                "time" to (ZmanimFormatter.formatAfter(start, tz) ?: "after nightfall (tzeit)"),
            ),
            expired = "Tonight's bedikat window has passed (before dawn). Ask your rav if you still need to search.",
            makeup = "If you missed bedikat, ask your rav — daytime search without a bracha is sometimes required.",
            availableAtLabel = "nightfall",
            activeHint = "Search by candlelight (or flashlight per many poskim); recite the bracha first; say Kol Chamira after.",
        )
    }

    /**
     * Selichot — Motzei / nightfall through dawn, then morning through sof zman tefilla.
     * Afternoon after sof zman is UPCOMING for tonight — except Friday / Erev Yom Tov,
     * when there is no Motzei Selichot into Shabbat or the holy day.
     */
    private fun selichotWindow(
        now: Long,
        z: ZmanimSnapshot,
        tz: String,
        prayerDay: PrayerDayContext,
    ): ItemZmanStatus {
        val dawn = z.alotHaShacharMillis ?: z.misheyakirMillis ?: z.sunriseMillis
        val eveningStart = ZmanPeriodLogic.effectiveEveningStart(now, z)
        val morningEnd = z.sofZmanTefillaMillis ?: z.chatzosMillis
        val nightEnd = ZmanPeriodLogic.nightObligationWindowEnd(z) ?: dawn
        val hint = "Bring a Selichot booklet for your nusach; many communities begin before dawn."
        // Single source of truth with Maariv — do not OR isErevShabbat (tzeit-rollover leak).
        val noTonightSelichot = TonightHolyDayRules.tonightBeginsHolyDayMelacha(prayerDay)

        // Pre-dawn: still in last night → continue through morning sof zman.
        if (dawn != null && now < dawn) {
            val start = eveningStart ?: (dawn - 6 * 60 * 60 * 1000L)
            return windowStatus(
                now, start, morningEnd,
                upcoming = "Selichot — available from nightfall / early morning.",
                upcomingTemplate = "Selichot — available {time}.",
                upcomingArgs = mapOf(
                    "time" to (ZmanimFormatter.formatAfter(start, tz) ?: "from nightfall / early morning"),
                ),
                expired = "Today's main Selichot window has passed (after sof zman tefilla).",
                makeup = null,
                availableAtLabel = "nightfall / dawn",
                activeHint = hint,
            )
        }

        // Morning through sof zman tefilla.
        if (morningEnd != null && now < morningEnd) {
            return windowStatus(
                now, dawn, morningEnd,
                upcoming = "Selichot — available after dawn.",
                upcomingTemplate = "Selichot — available {time}.",
                upcomingArgs = mapOf(
                    "time" to (ZmanimFormatter.formatAfter(dawn, tz) ?: "after dawn"),
                ),
                expired = "Today's main Selichot window has passed (after sof zman tefilla).",
                makeup = null,
                availableAtLabel = "dawn",
                activeHint = hint,
            )
        }

        // Afternoon: no Motzei Selichot into Shabbat or Yom Tov (Erev RH / Erev YK / Friday).
        if (noTonightSelichot) {
            val holy = TonightHolyDayRules.holyDayLabelForDeferral(prayerDay)
            return ItemZmanStatus(
                availability = ItemZmanAvailability.EXPIRED,
                hint = "Today's Selichot window has passed (after sof zman tefilla). " +
                    "There is no Selichot tonight — $holy begins.",
                hintTemplate = "Today's Selichot window has passed (after sof zman tefilla). " +
                    "There is no Selichot tonight — {holyDay} begins.",
                hintArgs = mapOf("holyDay" to holy),
            )
        }

        // Afternoon: wait for tonight's Motzei / nightfall window.
        if (eveningStart != null && now < eveningStart) {
            val whenLabel = ZmanimFormatter.formatAfter(eveningStart, tz) ?: "tonight after nightfall"
            return ItemZmanStatus(
                availability = ItemZmanAvailability.UPCOMING,
                windowStartMillis = eveningStart,
                windowEndMillis = nightEnd,
                hint = "Next Selichot window $whenLabel (Motzei / before dawn).",
                hintTemplate = "Next Selichot window {time} (Motzei / before dawn).",
                hintArgs = mapOf("time" to whenLabel),
                availableAtLabel = whenLabel,
            )
        }

        // Evening after nightfall → until dawn.
        return windowStatus(
            now, eveningStart, nightEnd,
            upcoming = "Selichot — available after nightfall.",
            upcomingTemplate = "Selichot — available {time}.",
            upcomingArgs = mapOf(
                "time" to (ZmanimFormatter.formatAfter(eveningStart, tz) ?: "after nightfall"),
            ),
            expired = "Tonight's Selichot window has ended (after dawn).",
            makeup = null,
            availableAtLabel = "nightfall",
            activeHint = hint,
        )
    }

    /**
     * Korbanot / Pesukei DeZimra — part of Shacharit.
     * Available from dawn; valid until halachic midday (chatzos) bedi'eved.
     */
    private fun shacharitPartsWindow(
        now: Long,
        z: ZmanimSnapshot,
        tz: String,
        label: String
    ): ItemZmanStatus {
        val start = z.alotHaShacharMillis ?: z.misheyakirMillis ?: z.sunriseMillis
        val idealEnd = z.sofZmanTefillaMillis
        val absoluteEnd = z.chatzosMillis
        val lateTemplate = if (idealEnd != null && now >= idealEnd)
            "The time to fulfill {label} has passed ({time}). You can still pray until midday."
        else null
        val lateArgs = if (idealEnd != null && now >= idealEnd)
            mapOf(
                "label" to label,
                "time" to (ZmanimFormatter.formatTime(idealEnd, tz) ?: "late morning"),
            )
        else emptyMap()
        return windowStatus(
            now, start, absoluteEnd,
            upcoming = "$label is part of the morning Shacharit service, from dawn (after dawn).",
            upcomingTemplate = "{label} is part of the morning Shacharit service, from dawn ({time}).",
            upcomingArgs = mapOf(
                "label" to label,
                "time" to (ZmanimFormatter.formatAfter(start, tz) ?: "after dawn"),
            ),
            expired = "The morning Shacharit window has closed (past halachic midday / chatzos).",
            makeup = null,
            availableAtLabel = "dawn",
            activeHintTemplate = lateTemplate,
            activeHintArgs = lateArgs,
        )
    }

    /**
     * Morning Shema — biblical deadline is sof zman Shema (end of 3rd halachic hour).
     * Bedi'eved it is still recited with its blessings as part of Shacharit until chatzos (midday).
     * The item therefore stays active until midday; after sofZmanShema an explanatory hint is shown.
     */
    private fun shemaWindow(now: Long, z: ZmanimSnapshot, tz: String): ItemZmanStatus {
        val start = z.alotHaShacharMillis ?: z.misheyakirMillis ?: z.sunriseMillis
        val sofZmanShema = z.sofZmanShemaMillis
        val absoluteEnd = z.chatzosMillis  // bedi'eved window extends to midday

        val lateTemplate = if (sofZmanShema != null && now >= sofZmanShema)
            "The time to fulfill the mitzvah of Shema has passed ({time}). Still say Shema with its blessings during Shacharit until midday — even if you missed it, say it during prayer."
        else null
        val lateArgs = if (sofZmanShema != null && now >= sofZmanShema)
            mapOf("time" to (ZmanimFormatter.formatTime(sofZmanShema, tz) ?: "end of 3rd hour"))
        else emptyMap()

        return windowStatus(
            now, start, absoluteEnd,
            upcoming = "Morning Shema opens at dawn (after dawn).",
            upcomingTemplate = "Morning Shema opens at dawn ({time}).",
            upcomingArgs = mapOf("time" to (ZmanimFormatter.formatAfter(start, tz) ?: "after dawn")),
            expired = "Shacharit window closed at halachic midday (chatzos). Evening Shema fulfills a separate obligation tonight.",
            makeup = "Evening Shema is a separate biblical mitzvah — recite it tonight after nightfall.",
            availableAtLabel = "dawn",
            activeHintTemplate = lateTemplate,
            activeHintArgs = lateArgs,
        )
    }

    /**
     * Women's daily prayer — one obligation per halachic day (tzeit to tzeit), any time.
     */
    private fun womenDailyPrayerWindow(): ItemZmanStatus = ItemZmanStatus()

    /**
     * Shacharit Amidah — available from dawn (alot hashachar); ideal from sunrise onward.
     * Ideal deadline: sof zman tefillah; bedi'eved until chatzos.
     */
    private fun amidahWindow(
        now: Long,
        z: ZmanimSnapshot,
        tz: String,
        label: String = "Shacharit Shemoneh Esrei"
    ): ItemZmanStatus {
        val start = z.alotHaShacharMillis ?: z.misheyakirMillis ?: z.sunriseMillis
        val sunrise = z.sunriseMillis
        val idealEnd = z.sofZmanTefillaMillis
        val absoluteEnd = z.chatzosMillis
        val lateTemplate = when {
            idealEnd != null && now >= idealEnd ->
                "The time to fulfill Shacharit has passed ({time}). You can still daven until midday."
            sunrise != null && start != null && now >= start && now < sunrise ->
                "Available from dawn. Ideal at sunrise ({time}) or later — if you're in a hurry you may say it now."
            else -> null
        }
        val lateArgs = when {
            idealEnd != null && now >= idealEnd ->
                mapOf("time" to (ZmanimFormatter.formatTime(idealEnd, tz) ?: "late morning"))
            sunrise != null && start != null && now >= start && now < sunrise ->
                mapOf("time" to (ZmanimFormatter.formatTime(sunrise, tz) ?: "sunrise"))
            else -> emptyMap()
        }
        val upcomingTime = ZmanimFormatter.formatAfter(start, tz) ?: "after dawn"
        val sunriseTime = sunrise?.let { ZmanimFormatter.formatAfter(it, tz) ?: "sunrise" }
        val (upcomingTemplate, upcomingArgs) = if (sunriseTime != null) {
            "{label} is available from dawn ({startTime}). Ideal at sunrise ({sunriseTime}) or later." to
                mapOf("label" to label, "startTime" to upcomingTime, "sunriseTime" to sunriseTime)
        } else {
            "{label} is available from dawn ({startTime}). Ideal from sunrise or later." to
                mapOf("label" to label, "startTime" to upcomingTime)
        }
        return windowStatus(
            now, start, absoluteEnd,
            upcoming = "$label is available from dawn ($upcomingTime).",
            upcomingTemplate = upcomingTemplate,
            upcomingArgs = upcomingArgs,
            expired = "Shacharit window closed at halachic midday (chatzos).",
            makeup = null,
            availableAtLabel = "dawn",
            activeHintTemplate = lateTemplate,
            activeHintArgs = lateArgs,
        )
    }

    /**
     * Tefillin — from misheyakir until sunset. Not worn on Shabbat or Yom Tov.
     * Ideal during Shacharit, but halachically valid all day until sunset.
     */
    private fun tefillinWindow(now: Long, z: ZmanimSnapshot, tz: String): ItemZmanStatus {
        val start = z.misheyakirMillis ?: z.sunriseMillis
        val idealEnd = z.sofZmanTefillaMillis
        val absoluteEnd = z.sunsetMillis
        val lateTemplate = if (idealEnd != null && now >= idealEnd)
            "The time to fulfill the mitzvah of tefillin has passed. Tefillin are still valid to wear until sunset ({time})."
        else null
        val lateArgs = if (idealEnd != null && now >= idealEnd)
            mapOf("time" to (ZmanimFormatter.formatTime(absoluteEnd, tz) ?: "sunset"))
        else emptyMap()
        return windowStatus(
            now, start, absoluteEnd,
            upcoming = "Recite brachos on tallit and tefillin at misheyakir (when daylight permits) — ideally during Shacharit. See explainer if you must leave earlier.",
            upcomingTemplate = "Recite brachos on tallit and tefillin at misheyakir ({time}) — ideally during Shacharit. See explainer if you must leave earlier.",
            upcomingArgs = mapOf("time" to (ZmanimFormatter.formatAfter(start, tz) ?: "when daylight permits")),
            expired = "Tefillin are not worn after sunset.",
            makeup = null,
            availableAtLabel = "misheyakir",
            activeHintTemplate = lateTemplate,
            activeHintArgs = lateArgs,
        )
    }

    private fun musafWindow(
        now: Long,
        z: ZmanimSnapshot,
        tz: String,
        day: PrayerDayContext
    ): ItemZmanStatus {
        if (!day.isMusafDay) {
            return ItemZmanStatus(
                availability = ItemZmanAvailability.EXPIRED,
                hint = "Musaf is only on Shabbat, Rosh Chodesh, Yom Tov, and Chol HaMoed — not Purim, Chanukah, or ordinary weekdays."
            )
        }
        val label = day.musafDayLabel()
        val start = z.sunriseMillis
        // SA OC 286:1: ideal before end of 7th halachic hour; bedi'eved "its time is all day"
        // (זמנה כל היום) — does not expire until sunset.
        val idealEnd = ZmanimHelpers.endOfHalachicHourMillis(z, 7)
        val absoluteEnd = z.sunsetMillis
        val shabbatNote = if (day.isShabbat || day.isYomTovAssurBemelacha) {
            " On Shabbat and Festivals, fulfill this without using your phone."
        } else ""
        val idealEndLabel = ZmanimFormatter.formatTime(idealEnd, tz) ?: "end of the 7th hour"
        val lateHintTemplate = if (idealEnd != null && now >= idealEnd)
            "The time to fulfill Musaf has passed ({time} — end of the 7th halachic hour). You can still fulfill it until sunset — if Mincha time has arrived, pray Mincha first (SA OC 286:4).{shabbatNote}"
        else null
        val lateHintArgs = if (idealEnd != null && now >= idealEnd)
            mapOf(
                "time" to idealEndLabel,
                "shabbatNote" to shabbatNote,
            )
        else emptyMap()
        return windowStatus(
            now, start, absoluteEnd,
            upcoming = "Musaf ($label) — after the morning Amidah, from sunrise.",
            upcomingTemplate = "Musaf ({label}) — after the morning Amidah, from {time}.",
            upcomingArgs = mapOf(
                "label" to label,
                "time" to (ZmanimFormatter.formatAfter(start, tz) ?: "sunrise"),
            ),
            expired = "Musaf window has closed (sunset).$shabbatNote",
            expiredTemplate = "Musaf window has closed (sunset).{shabbatNote}",
            expiredArgs = mapOf("shabbatNote" to shabbatNote),
            makeup = null,
            availableAtLabel = "sunrise",
            activeHintTemplate = lateHintTemplate,
            activeHintArgs = lateHintArgs,
        )
    }

    private fun tefillinTishaBeavMinchaWindow(now: Long, z: ZmanimSnapshot, tz: String): ItemZmanStatus {
        val start = z.chatzosMillis ?: z.minchaGedolaMillis
        val end = z.tzeitMillis ?: z.sunsetMillis ?: z.plagHaminchaMillis
        return windowStatus(
            now, start, end,
            upcoming = "Tisha B'Av: tallit and tefillin at Mincha after halachic chatzos (midday).",
            upcomingTemplate = TishaBeavTefillinRules.minchaUpcomingHint("{time}"),
            upcomingArgs = mapOf(
                "time" to (ZmanimFormatter.formatAfter(start, tz) ?: "after chatzos (midday)"),
            ),
            expired = TishaBeavTefillinRules.minchaExpiredHint(),
            makeup = null,
            availableAtLabel = "chatzos",
        )
    }

    private fun minchaWindow(
        now: Long,
        z: ZmanimSnapshot,
        tz: String,
        label: String,
        prayerDay: PrayerDayContext,
    ): ItemZmanStatus {
        val nusach = prayerDay.nusach
        val start = z.minchaGedolaMillis ?: z.chatzosMillis?.let { it + 30 * 60 * 1000L }
        val end = PrayerZmanRules.minchaAbsoluteEndMillis(z, nusach)
            ?: z.tzeitMillis
            ?: z.sunsetMillis
            ?: z.plagHaminchaMillis
        val sunset = z.sunsetMillis
        val lateHint = if (sunset != null && now >= sunset && end != null && now < end) {
            PrayerZmanRules.minchaActiveLateHint(nusach)
        } else {
            null
        }
        return windowStatus(
            now, start, end,
            upcoming = "$label appears about 30 min after chatzos (midday) — Mincha Gedolah.",
            upcomingTemplate = "{label} appears {time} — Mincha Gedolah.",
            upcomingArgs = mapOf(
                "label" to label,
                "time" to (ZmanimFormatter.formatAfter(start, tz) ?: "about 30 min after chatzos (midday)"),
            ),
            expired = PrayerZmanRules.minchaExpiredMessage(nusach),
            expiredTemplate = "Today's {label} window ended {time}.",
            expiredArgs = mapOf(
                "label" to label,
                "time" to (ZmanimFormatter.formatUntil(end, tz) ?: "at nightfall"),
            ),
            makeup = null,
            availableAtLabel = "Mincha Gedola",
            activeHint = lateHint,
        )
    }

    private fun eveningShemaWindow(now: Long, z: ZmanimSnapshot, tz: String): ItemZmanStatus {
        val start = ZmanPeriodLogic.effectiveEveningStart(now, z)
        val end = ZmanPeriodLogic.effectiveEveningEnd(now, z)
        val obligationTime = ZmanimFormatter.formatAfter(z.tzeitMillis ?: start, tz) ?: "at nightfall (tzeit)"
        val tzeitTime = z.tzeitMillis?.let { tzeit ->
            if (now >= tzeit) null else ZmanimFormatter.formatAfter(tzeit, tz) ?: "at nightfall (tzeit)"
        }
        val (upcomingTemplate, upcomingArgs) = if (tzeitTime != null) {
            "Evening Shema — biblical obligation {time}. Maariv may begin at sunset, but biblical evening Shema begins {tzeitTime}. If you daven Maariv early, repeat Shema then." to
                mapOf("time" to obligationTime, "tzeitTime" to tzeitTime)
        } else {
            "Evening Shema — biblical obligation {time}." to mapOf("time" to obligationTime)
        }
        return windowStatus(
            now, start, end,
            upcoming = "Evening Shema — biblical obligation $obligationTime.",
            upcomingTemplate = upcomingTemplate,
            upcomingArgs = upcomingArgs,
            expired = "Tonight's primary window for evening Shema has passed (before alot hashachar).",
            makeup = "If you missed it, ask your rabbi — some fulfill later the same night with guidance; morning Shema is a separate obligation.",
            availableAtLabel = "sunset"
        )
    }

    private fun yaalehVyavoShacharitWindow(now: Long, z: ZmanimSnapshot, tz: String): ItemZmanStatus =
        amidahWindow(now, z, tz).copy(
            makeupNote = "Forgot Yaaleh V'yavo? In Retzei — insert there; after concluding Retzei — return to beginning of Retzei; after Yihiyu L'ratzon — repeat only Shacharit Amidah (SA O.C. 422:1).",
        )

    private fun yaalehVyavoMinchaWindow(now: Long, z: ZmanimSnapshot, tz: String, prayerDay: PrayerDayContext): ItemZmanStatus =
        minchaWindow(now, z, tz, label = "Yaaleh V'yavo at Mincha", prayerDay = prayerDay).copy(
            makeupNote = "Forgot Yaaleh V'yavo? In Retzei — insert there; after concluding Retzei — return to beginning of Retzei; after Yihiyu L'ratzon — repeat only Mincha Amidah (SA O.C. 422:1).",
        )

    private fun yaalehVyavoMaarivWindow(now: Long, z: ZmanimSnapshot, tz: String): ItemZmanStatus {
        val start = ZmanPeriodLogic.effectiveEveningStart(now, z)
        val end = ZmanPeriodLogic.effectiveEveningEnd(now, z)
        val tzeitNote = z.tzeitMillis?.let { tzeit ->
            if (now >= tzeit) return@let ""
            " Many daven Maariv ideally ${ZmanimFormatter.formatAfter(tzeit, tz) ?: "after nightfall (tzeit)"}."
        }.orEmpty()
        val tzeitTime = z.tzeitMillis?.let { tzeit ->
            if (now >= tzeit) null else ZmanimFormatter.formatAfter(tzeit, tz) ?: "after nightfall (tzeit)"
        }
        val (upcomingTemplate, upcomingArgs) = if (tzeitTime != null) {
            "Yaaleh V'yavo at Maariv — available {time}. Many daven Maariv ideally {tzeitTime}." to
                mapOf("time" to (ZmanimFormatter.formatAfter(start, tz) ?: "after sunset"), "tzeitTime" to tzeitTime)
        } else {
            "Yaaleh V'yavo at Maariv — available {time}." to
                mapOf("time" to (ZmanimFormatter.formatAfter(start, tz) ?: "after sunset"))
        }
        return windowStatus(
            now, start, end,
            upcoming = "Yaaleh V'yavo at Maariv — available after sunset.$tzeitNote",
            upcomingTemplate = upcomingTemplate,
            upcomingArgs = upcomingArgs,
            expired = "Tonight's Maariv window has passed (after alot hashachar / dawn).",
            makeup = "Forgot Yaaleh V'yavo at Maariv on Rosh Chodesh? After Retzei or after the Amidah — do not repeat (Berachot 30b; SA O.C. 422:1). Still in Retzei before God's name — insert there.",
            availableAtLabel = "sunset",
        )
    }

    private fun maarivWindow(now: Long, z: ZmanimSnapshot, tz: String, prayerDay: PrayerDayContext): ItemZmanStatus {
        MaarivInAppRules.blockedMaarivStatusIfApplicable(prayerDay, now, z, tz)?.let { return it }
        val start = ZmanPeriodLogic.effectiveEveningStart(now, z)
        val end = ZmanPeriodLogic.effectiveEveningEnd(now, z)
        val plagTime = z.plagHaminchaMillis?.let { plag ->
            ZmanimFormatter.formatAfter(plag, tz)
        }
        val tzeitTime = z.tzeitMillis?.let { tzeit ->
            if (now >= tzeit) null else ZmanimFormatter.formatAfter(tzeit, tz) ?: "after nightfall (tzeit)"
        }
        val startTime = ZmanimFormatter.formatAfter(start, tz) ?: "after sunset"
        val shemaRepeatNote = if (tzeitTime != null) {
            " If Maariv is before nightfall, repeat Shema (without blessings) at tzeit."
        } else {
            ""
        }
        val plagNote = if (plagTime != null) {
            " Some communities may daven Maariv after plag haMincha — follow your shul and rav."
        } else ""
        val (upcomingTemplate, upcomingArgs) = if (tzeitTime != null) {
            "Maariv — earliest {time} (sunset). Ideal for many: {tzeitTime}.{plagNote}{shemaNote}" to
                mapOf(
                    "time" to startTime,
                    "tzeitTime" to tzeitTime,
                    "plagNote" to plagNote,
                    "shemaNote" to shemaRepeatNote,
                )
        } else {
            "Maariv — available from {time} (sunset).{plagNote}" to
                mapOf("time" to startTime, "plagNote" to plagNote)
        }
        return windowStatus(
            now, start, end,
            upcoming = "Maariv — earliest $startTime (sunset).$plagNote$shemaRepeatNote",
            upcomingTemplate = upcomingTemplate,
            upcomingArgs = upcomingArgs,
            expired = "Tonight's Maariv window has passed (after alot hashachar / dawn).",
            makeup = null,
            availableAtLabel = "sunset"
        )
    }

    private fun havdalahWindow(
        now: Long,
        z: ZmanimSnapshot,
        tz: String,
        prayerDay: PrayerDayContext,
        cal: DayInfo?,
        yesterdayCal: DayInfo?,
        tomorrowCal: DayInfo?,
    ): ItemZmanStatus {
        val tzeit = z.tzeitMillis
            ?: return ItemZmanStatus(hint = "Set location in Settings for tzeit hakochavim times.")
        val kind = if (cal != null && yesterdayCal != null && tomorrowCal != null) {
            HavdalahRules.kind(cal, yesterdayCal, tomorrowCal, now)
        } else {
            null
        }
        val end = when {
            kind == HavdalahRules.Kind.MOTZEI_SHABBAT ||
                kind == HavdalahRules.Kind.MOTZEI_SHABBAT_INTO_TISHA_BEAV ->
                MotzeiShabbatWindow.melaveMalkaEndMillis(z, !prayerDay.isShabbat)
            else ->
                HavdalahRules.windowEndMillis(z)
                    ?: MotzeiShabbatWindow.melaveMalkaEndMillis(z, !prayerDay.isShabbat)
        } ?: return ItemZmanStatus(hint = "Set location in Settings for dawn (alot hashachar) times.")
        val start = when {
            cal != null -> HavdalahRules.windowStartMillis(cal) ?: tzeit
            prayerDay.isShabbat -> tzeit
            else -> tzeit - 24 * 60 * 60 * 1000L
        }
        val tzeitLabel = ZmanimFormatter.formatAfter(tzeit, tz) ?: "tzeit hakochavim"
        val (expired, makeup) = when (kind) {
            HavdalahRules.Kind.MOTZEI_YOM_TOV ->
                "Tonight's Motzei Yom Tov Havdalah window ended at dawn." to
                    "If you missed Motzei Yom Tov Havdalah, ask your rav; many allow wine + Hamavdil into the following days (no spices or fire)."
            HavdalahRules.Kind.MOTZEI_YOM_KIPPUR ->
                "Tonight's Motzei Yom Kippur Havdalah window ended at dawn." to
                    "Make Havdalah as soon as you can after the fast — wine, fire (ner she-shavat), and Hamavdil."
            HavdalahRules.Kind.DELAYED_AFTER_TISHA_BEAV ->
                "Tonight's post-fast Havdalah window ended at dawn." to
                    "Recite wine + Hamavdil as soon as possible after the fast (no spices or fire)."
            HavdalahRules.Kind.MOTZEI_SHABBAT_INTO_TISHA_BEAV ->
                "Tonight's Motzei Shabbat → Tisha B'Av candle / Baruch ha'mavdil window ended at dawn." to
                    "If you missed the fire blessing Motzei Shabbat, ask your rav — Sunday night is wine + Hamavdil only."
            else ->
                "Tonight's primary Havdalah window ended at dawn — if you still have not said it, you may recite wine + Hamavdil through Tuesday (no spices or fire)." to
                    "Missed Saturday night? Say Havdalah over wine through Tuesday without besamim or the fire blessing."
        }
        return windowStatus(
            now = now,
            start = start,
            end = end,
            upcoming = "Havdalah — after nightfall (tzeit hakochavim).",
            upcomingTemplate = "Havdalah — after nightfall ({time}).",
            upcomingArgs = mapOf("time" to tzeitLabel),
            expired = expired,
            makeup = makeup,
            availableAtLabel = "tzeit hakochavim"
        )
    }

    private fun melaveMalkaWindow(
        now: Long,
        z: ZmanimSnapshot,
        tz: String,
        prayerDay: PrayerDayContext,
    ): ItemZmanStatus {
        val tzeit = z.tzeitMillis
            ?: return ItemZmanStatus(hint = "Set location in Settings for tzeit hakochavim times.")
        val end = MotzeiShabbatWindow.melaveMalkaEndMillis(z, !prayerDay.isShabbat)
            ?: return ItemZmanStatus(hint = "Set location in Settings for dawn (alot hashachar) times.")
        val start = if (prayerDay.isShabbat) {
            tzeit
        } else {
            tzeit - 24 * 60 * 60 * 1000L
        }
        return windowStatus(
            now = now,
            start = start,
            end = end,
            upcoming = "Melave Malka — after Havdalah, from tzeit hakochavim through dawn.",
            upcomingTemplate = "Melave Malka — after Havdalah, from {time} through dawn.",
            upcomingArgs = mapOf("time" to (ZmanimFormatter.formatAfter(tzeit, tz) ?: "tzeit hakochavim")),
            expired = "Tonight's Melave Malka window ended at dawn (alot hashachar).",
            makeup = "If you were too full to eat after Shabbat, you are not required to force yourself.",
            availableAtLabel = "tzeit hakochavim"
        )
    }

    private fun bedtimeWindow(now: Long, z: ZmanimSnapshot, tz: String, label: String): ItemZmanStatus {
        val start = ZmanPeriodLogic.effectiveEveningStart(now, z)
        val end = ZmanPeriodLogic.effectiveEveningEnd(now, z)
        return windowStatus(
            now, start, end,
            upcoming = "$label — when you are ready for sleep (after sunset).",
            upcomingTemplate = "{label} — when you are ready for sleep ({time}).",
            upcomingArgs = mapOf(
                "label" to label,
                "time" to (ZmanimFormatter.formatAfter(start, tz) ?: "after sunset"),
            ),
            expired = "Ideal bedtime Shema time is before dawn (alot hashachar).",
            expiredTemplate = "Ideal bedtime Shema time is before dawn ({time}).",
            expiredArgs = mapOf("time" to (ZmanimFormatter.formatUntil(end, tz) ?: "alot hashachar")),
            makeup = null,
            availableAtLabel = "sunset"
        )
    }

    /**
     * Full Rosh Chodesh period: from the tzeit that begins RC until the tzeit that ends the
     * last RC day (one-day: ~24h; two-day: ~48h — 30th + 1st).
     */
    private fun roshChodeshObservancesWindow(
        now: Long,
        z: ZmanimSnapshot,
        tz: String,
        cal: DayInfo?,
    ): ItemZmanStatus {
        val rawTzeit = z.tzeitMillis ?: z.sunsetMillis
        if (rawTzeit == null || cal == null || !cal.isRoshChodesh) {
            return ItemZmanStatus()
        }
        val dayMs = 24 * 60 * 60 * 1000L
        // Next nightfall boundary from this snapshot (handles already-passed tonight's tzeit).
        val nextTzeit = if (now >= rawTzeit) rawTzeit + dayMs else rawTzeit
        // Tzeit that began the current Hebrew day.
        val startOfCurrentHebrewDay = nextTzeit - dayMs
        val twoDay = RoshChodeshRules.isTwoDayObservance(cal)
        val (start, end) = when {
            twoDay && RoshChodeshRules.isFirstDayOfTwoDay(cal) ->
                startOfCurrentHebrewDay to (startOfCurrentHebrewDay + 2 * dayMs)
            twoDay ->
                // Second day (1st of the month): RC began the previous nightfall.
                (startOfCurrentHebrewDay - dayMs) to (startOfCurrentHebrewDay + dayMs)
            else ->
                startOfCurrentHebrewDay to (startOfCurrentHebrewDay + dayMs)
        }
        val endLabel = ZmanimFormatter.formatTimeWithDate(end, tz)
            ?: ZmanimFormatter.formatTime(end, tz)
            ?: "nightfall at the end of Rosh Chodesh"
        val lengthHint = if (twoDay) {
            "Two-day Rosh Chodesh (30th + 1st) — from the opening tzeit through the tzeit after the 1st."
        } else {
            "One-day Rosh Chodesh — from the opening tzeit through the following tzeit."
        }
        return windowStatus(
            now, start, end,
            upcoming = "Rosh Chodesh begins at nightfall (tzeit).",
            upcomingTemplate = "Rosh Chodesh begins at nightfall ({time}).",
            upcomingArgs = mapOf(
                "time" to (ZmanimFormatter.formatAfter(start, tz) ?: "tzeit"),
            ),
            expired = "Rosh Chodesh ended at nightfall ($endLabel).",
            expiredTemplate = "Rosh Chodesh ended at nightfall ({time}).",
            expiredArgs = mapOf("time" to endLabel),
            makeup = null,
            availableAtLabel = "tzeit",
            activeHint = "$lengthHint Festive meal is during the daytime; Yaaleh V'yavo, Hallel, and Musaf follow your prayer checklist.",
        )
    }

    /**
     * Hadlakat Nerot — light by candle-lighting time (18 / Jerusalem 40 min before sunset).
     * Available from Plag HaMincha; ideal deadline is candle lighting; expires at sunset.
     */
    private fun shabbatCandlesWindow(
        now: Long,
        z: ZmanimSnapshot,
        tz: String,
        day: PrayerDayContext,
    ): ItemZmanStatus = hadlakatNerotWindow(
        now = now,
        z = z,
        tz = tz,
        day = day,
        label = "Shabbat candles",
    )

    /** Weekday erev Yom Tov — same plag→sunset window as Shabbat candles. */
    private fun yomTovCandlesWindow(
        now: Long,
        z: ZmanimSnapshot,
        tz: String,
        day: PrayerDayContext,
    ): ItemZmanStatus = hadlakatNerotWindow(
        now = now,
        z = z,
        tz = tz,
        day = day,
        label = "Yom Tov candles",
    )

    private fun hadlakatNerotWindow(
        now: Long,
        z: ZmanimSnapshot,
        tz: String,
        day: PrayerDayContext,
        label: String,
    ): ItemZmanStatus {
        val sunset = z.sunsetMillis
            ?: return ItemZmanStatus(hint = "Set location in Settings for candle-lighting times.")
        val candles = sunset - day.candleLeadMinutes * 60_000L
        val start = z.plagHaminchaMillis ?: (candles - 90 * 60_000L)
        val candlesClock = ZmanimFormatter.formatTime(candles, tz) ?: "candle lighting"
        val lead = day.candleLeadMinutes
        return windowStatus(
            now = now,
            start = start,
            end = sunset,
            upcoming = "Light $label by $candlesClock ($lead min before sunset).",
            upcomingTemplate = "Light $label by {time} ({lead} min before sunset).",
            upcomingArgs = mapOf(
                "time" to candlesClock,
                "lead" to lead.toString(),
            ),
            expired = "Candle-lighting time has passed (after sunset).",
            makeup = null,
            availableAtLabel = "Plag HaMincha",
            activeHint = "Light by $candlesClock ($lead min before sunset).",
            activeHintTemplate = "Light by {time} ({lead} min before sunset).",
            activeHintArgs = mapOf(
                "time" to candlesClock,
                "lead" to lead.toString(),
            ),
        )
    }

    private fun publicFastWindow(
        now: Long,
        z: ZmanimSnapshot,
        tz: String,
        fastIdx: Int?,
    ): ItemZmanStatus {
        val idx = fastIdx ?: return ItemZmanStatus()
        val subtitle = PublicFastDayText.fastDaySubtitle(idx, z, tz)
        val end = z.tzeitMillis
            ?: return ItemZmanStatus(hint = subtitle)
        val endLabel = ZmanimFormatter.formatTime(end, tz) ?: "nightfall"
        if (PublicFastDayRules.fastStartsAtSunset(idx)) {
            val sunsetToday = z.sunsetMillis
            val sunsetYesterday = sunsetToday?.minus(24 * 60 * 60 * 1000L)
            val dawnToday = z.alotHaShacharMillis ?: z.sunriseMillis
            // Once this fast day is active (from tzeit rollover the evening before through its
            // own tzeit), the fast began at the previous sunset — during the night portion the
            // window must already be ACTIVE, not "begins at sunset".
            val start = sunsetYesterday ?: dawnToday ?: sunsetToday ?: 0L
            val upcomingLabel = if (sunsetToday != null && now < sunsetToday) "sunset" else "dawn"
            return windowStatus(
                now = now,
                start = start,
                end = end,
                upcoming = "$subtitle — fast begins at sunset.",
                upcomingTemplate = "{subtitle} — fast begins at sunset.",
                upcomingArgs = mapOf("subtitle" to subtitle),
                expired = "The fast ended at nightfall ($endLabel).",
                expiredTemplate = "The fast ended at nightfall ({time}).",
                expiredArgs = mapOf("time" to endLabel),
                makeup = null,
                availableAtLabel = upcomingLabel,
                activeHint = subtitle,
            )
        }
        val start = z.alotHaShacharMillis ?: z.sunriseMillis
            ?: return ItemZmanStatus(hint = subtitle)
        return windowStatus(
            now = now,
            start = start,
            end = end,
            upcoming = "$subtitle — begins at dawn (alot hashachar).",
            upcomingTemplate = "{subtitle} — begins at dawn ({time}).",
            upcomingArgs = mapOf(
                "subtitle" to subtitle,
                "time" to (ZmanimFormatter.formatAfter(start, tz) ?: "alot hashachar"),
            ),
            expired = "Today's fast ended at nightfall ($endLabel).",
            expiredTemplate = "Today's fast ended at nightfall ({time}).",
            expiredArgs = mapOf("time" to endLabel),
            makeup = null,
            availableAtLabel = "dawn",
            activeHint = subtitle,
        )
    }

    /**
     * Ordinary Purim: one checklist row covers night (tzeit→dawn) and day (dawn→sunset).
     * ACTIVE in either window; EXPIRED after sunset on Purim day.
     */
    private fun purimMegillahCombinedWindow(
        now: Long,
        z: ZmanimSnapshot,
        tz: String,
        cal: DayInfo?,
    ): ItemZmanStatus {
        val tzeit = z.tzeitMillis
            ?: return ItemZmanStatus(hint = "Set location in Settings for nightfall (tzeit) times.")
        val dawn = z.alotHaShacharMillis ?: z.sunriseMillis
        val sunset = z.sunsetMillis
            ?: return ItemZmanStatus(hint = "Set location in Settings for sunset times.")
        val nightOpen = cal != null && HalachicNightWindow.isOpen(cal, now)
        val beforeDawn = dawn != null && now < dawn

        // Night reading: Purim night after tzeit through dawn (incl. after civil midnight).
        if (nightOpen || beforeDawn) {
            val nightStart = HalachicNightWindow.nightStartMillis(now, z) ?: (tzeit - 24 * 60 * 60 * 1000L)
            val startLabel = ZmanimFormatter.formatTime(nightStart, tz) ?: "nightfall"
            if (now < nightStart) {
                return ItemZmanStatus(
                    availability = ItemZmanAvailability.UPCOMING,
                    windowStartMillis = nightStart,
                    windowEndMillis = dawn,
                    hintTemplate = "Megillah — night reading after nightfall ({time}); day reading until sunset.",
                    hintArgs = mapOf("time" to startLabel),
                    availableAtLabel = "nightfall",
                )
            }
            if (dawn == null || now < dawn) {
                return ItemZmanStatus(
                    availability = ItemZmanAvailability.ACTIVE,
                    windowStartMillis = nightStart,
                    windowEndMillis = dawn,
                    hint = "Hear the Megillah tonight after nightfall (and again daytime before sunset).",
                    availableAtLabel = "nightfall",
                )
            }
        }

        if (now < sunset) {
            return ItemZmanStatus(
                availability = ItemZmanAvailability.ACTIVE,
                windowStartMillis = dawn,
                windowEndMillis = sunset,
                hint = "Hear the daytime Megillah before sunset — the primary daytime fulfillment (night reading was also required).",
                availableAtLabel = "dawn",
            )
        }
        return ItemZmanStatus(
            availability = ItemZmanAvailability.EXPIRED,
            hint = "Today's Megillah windows have passed (night after tzeit; day before sunset). Ask your rav about a makeup reading.",
            makeupNote = "If you missed a reading, ask your rav — some allow a makeup the same day; do not delay.",
        )
    }

    private fun purimMegillahNightWindow(
        now: Long,
        z: ZmanimSnapshot,
        tz: String,
        cal: DayInfo?,
    ): ItemZmanStatus {
        z.tzeitMillis
            ?: return ItemZmanStatus(hint = "Set location in Settings for nightfall (tzeit) times.")
        // After Hebrew-day rollover / after midnight, shift start back to tonight's tzeit.
        val start = HalachicNightWindow.nightStartMillis(now, z)
        // Night reading ends at dawn (alot), not ~24h after sunrise.
        val dawnToday = z.alotHaShacharMillis ?: z.sunriseMillis
        val nightOpen = cal != null && HalachicNightWindow.isOpen(cal, now)
        val end = when {
            nightOpen -> dawnToday
            dawnToday != null && now < dawnToday -> dawnToday
            dawnToday != null -> dawnToday + 24 * 60 * 60 * 1000L
            else -> null
        }
        val startLabel = ZmanimFormatter.formatTime(start, tz) ?: "nightfall"
        return windowStatus(
            now = now,
            start = start,
            end = end,
            upcoming = "Megillah reading — after nightfall (tzeit) until dawn.",
            upcomingTemplate = "Megillah reading — after nightfall ({time}) until dawn.",
            upcomingArgs = mapOf("time" to startLabel),
            expired = "Tonight's Megillah window has passed — hear it tomorrow morning if you missed it.",
            makeup = "If you missed tonight's reading, the Friday morning reading is still required.",
            availableAtLabel = "nightfall",
            activeHint = "Hear the Megillah tonight after Maariv or at your shul's reading time.",
        )
    }

    private fun purimDayUntilSunsetWindow(
        now: Long,
        z: ZmanimSnapshot,
        tz: String,
        label: String,
        activeHint: String,
        expired: String,
    ): ItemZmanStatus {
        val start = z.alotHaShacharMillis ?: z.sunriseMillis
            ?: return ItemZmanStatus(hint = "$label — set location in Settings for dawn times.")
        val end = z.sunsetMillis
            ?: return ItemZmanStatus(hint = "$label — set location in Settings for sunset times.")
        return windowStatus(
            now = now,
            start = start,
            end = end,
            upcoming = "$label — from dawn until sunset.",
            upcomingTemplate = "{label} — from dawn ({time}) until sunset.",
            upcomingArgs = mapOf(
                "label" to label,
                "time" to (ZmanimFormatter.formatAfter(start, tz) ?: "dawn"),
            ),
            expired = expired,
            makeup = null,
            availableAtLabel = "dawn",
            activeHint = activeHint,
        )
    }

    private fun purimMegillahDayWindow(
        now: Long,
        z: ZmanimSnapshot,
        tz: String,
        label: String,
    ): ItemZmanStatus {
        val start = z.alotHaShacharMillis ?: z.sunriseMillis
            ?: return ItemZmanStatus(hint = "$label — set location in Settings for dawn times.")
        val end = z.sunsetMillis
            ?: return ItemZmanStatus(hint = "$label — set location in Settings for sunset times.")
        return windowStatus(
            now = now,
            start = start,
            end = end,
            upcoming = "$label — from dawn until sunset.",
            upcomingTemplate = "{label} — from dawn ({time}) until sunset.",
            upcomingArgs = mapOf(
                "label" to label,
                "time" to (ZmanimFormatter.formatAfter(start, tz) ?: "dawn"),
            ),
            expired = "Today's daytime Megillah window has passed (before sunset).",
            makeup = "Ask your rav if you still need to hear the daytime reading.",
            availableAtLabel = "dawn",
            activeHint = "Hear the Megillah during the daytime, usually after Shacharit.",
        )
    }

    private fun purimMatanotDayWindow(now: Long, z: ZmanimSnapshot, tz: String): ItemZmanStatus {
        val start = z.alotHaShacharMillis ?: z.sunriseMillis
            ?: return ItemZmanStatus(hint = "Matanot la'evyonim — set location for dawn times.")
        val end = z.sunsetMillis
            ?: return ItemZmanStatus(hint = "Matanot la'evyonim — set location for sunset times.")
        return windowStatus(
            now = now,
            start = start,
            end = end,
            upcoming = "Matanot la'evyonim — from dawn until sunset.",
            upcomingTemplate = "Matanot la'evyonim — from dawn ({time}) until sunset.",
            upcomingArgs = mapOf("time" to (ZmanimFormatter.formatAfter(start, tz) ?: "dawn")),
            expired = "Today's matanot la'evyonim window has passed.",
            makeup = null,
            availableAtLabel = "dawn",
            activeHint = "Give gifts to the poor during the daytime only.",
        )
    }

    private fun motzeiYomKippurMealWindow(
        now: Long,
        z: ZmanimSnapshot,
        tz: String,
        cal: DayInfo?,
    ): ItemZmanStatus {
        val tzeit = z.tzeitMillis
            ?: return ItemZmanStatus(hint = "Break-fast meal — available after nightfall when Yom Kippur ends.")
        val end = HavdalahRules.windowEndMillis(z)
        val start = when {
            cal != null -> HavdalahRules.windowStartMillis(cal) ?: (tzeit - 24 * 60 * 60 * 1000L)
            else -> tzeit - 24 * 60 * 60 * 1000L
        }
        return windowStatus(
            now = now,
            start = start,
            end = end,
            upcoming = "Motzei Yom Kippur meal — after nightfall (tzeit).",
            upcomingTemplate = "Motzei Yom Kippur meal — after nightfall ({time}).",
            upcomingArgs = mapOf("time" to (ZmanimFormatter.formatAfter(start, tz) ?: "tzeit")),
            expired = "Tonight's break-fast window has passed.",
            makeup = "Eat as soon as you can after the fast if you have not broken it yet.",
            availableAtLabel = "nightfall",
            activeHint = "Eat a proper meal after Maariv and Havdalah.",
        )
    }

    private fun windowStatus(
        now: Long,
        start: Long?,
        end: Long?,
        upcoming: String,
        expired: String,
        makeup: String?,
        availableAtLabel: String? = null,
        activeHint: String? = null,
        activeHintTemplate: String? = null,
        activeHintArgs: Map<String, String> = emptyMap(),
        upcomingTemplate: String? = null,
        upcomingArgs: Map<String, String> = emptyMap(),
        expiredTemplate: String? = null,
        expiredArgs: Map<String, String> = emptyMap(),
        makeupTemplate: String? = null,
        makeupArgs: Map<String, String> = emptyMap(),
    ): ItemZmanStatus {
        if (start != null && now < start) {
            return ItemZmanStatus(
                availability = ItemZmanAvailability.UPCOMING,
                hint = upcoming,
                hintTemplate = upcomingTemplate,
                hintArgs = upcomingArgs,
                makeupNote = makeup,
                makeupTemplate = makeupTemplate,
                makeupArgs = makeupArgs,
                windowStartMillis = start,
                windowEndMillis = end,
                availableAtLabel = availableAtLabel
            )
        }
        if (end != null && now >= end) {
            return ItemZmanStatus(
                availability = ItemZmanAvailability.EXPIRED,
                hint = expired,
                hintTemplate = expiredTemplate,
                hintArgs = expiredArgs,
                makeupNote = makeup,
                makeupTemplate = makeupTemplate,
                makeupArgs = makeupArgs,
                windowStartMillis = start,
                windowEndMillis = end,
                availableAtLabel = availableAtLabel
            )
        }
        return ItemZmanStatus(
            hint = activeHint,
            hintTemplate = activeHintTemplate,
            hintArgs = activeHintArgs,
            makeupNote = makeup,
            makeupTemplate = makeupTemplate,
            makeupArgs = makeupArgs,
            windowStartMillis = start,
            windowEndMillis = end,
            availableAtLabel = availableAtLabel
        )
    }

    private fun birkatHaIlanotHint(prayerDay: PrayerDayContext): ItemZmanStatus =
        ItemZmanStatus(hint = BirkatHaIlanotRules.listSubtextHint(prayerDay.latitude))

    private fun birkatHachamahWindow(now: Long, z: ZmanimSnapshot, tz: String): ItemZmanStatus {
        val today = Instant.fromEpochMilliseconds(now).toLocalDateTime(TimeZone.of(tz)).date
        val occurrence = BirkatHachamahRules.visibleOccurrence(today) ?: return ItemZmanStatus()
        val dateLabel = BirkatHachamahRules.formatOccurrenceDate(occurrence)
        if (!BirkatHachamahRules.isRecitationDay(today)) {
            val days = BirkatHachamahRules.daysUntilOccurrence(today, occurrence)
            val (whenTemplate, whenArgs) = when (days) {
                1 -> "Birkat Hachamah tomorrow — {date}. Plan to recite outdoors at sunrise." to
                    mapOf("date" to dateLabel)
                else -> "Birkat Hachamah in {count} days — {date}. Plan to recite outdoors at sunrise." to
                    mapOf("count" to days.toString(), "date" to dateLabel)
            }
            return ItemZmanStatus(
                availability = ItemZmanAvailability.UPCOMING,
                hint = "Birkat Hachamah in $days days — $dateLabel. Plan to recite outdoors at sunrise.",
                hintTemplate = whenTemplate,
                hintArgs = whenArgs,
                availableAtLabel = dateLabel,
            )
        }
        val start = z.sunriseMillis ?: z.alotHaShacharMillis
        val idealEnd = z.sofZmanTefillaMillis
        val absoluteEnd = z.chatzosMillis
        val lateHintTemplate = if (idealEnd != null && now >= idealEnd)
            "The time to fulfill Birkat Hachamah has passed ({time}). According to some opinions, you may still recite it until chatzos (halachic midday)."
        else null
        val lateHintArgs = if (idealEnd != null && now >= idealEnd)
            mapOf("time" to (ZmanimFormatter.formatTime(idealEnd, tz) ?: "third hour"))
        else emptyMap()
        return windowStatus(
            now = now,
            start = start,
            end = absoluteEnd,
            upcoming = "Today — Birkat Hachamah at sunrise ($dateLabel). Recite outdoors while viewing the sun.",
            upcomingTemplate = "Today — Birkat Hachamah at sunrise ({date}). Recite outdoors while viewing the sun.",
            upcomingArgs = mapOf("date" to dateLabel),
            expired = "Today's window for Birkat Hachamah has passed. This opportunity comes only once every 28 years.",
            makeup = null,
            availableAtLabel = ZmanimFormatter.formatTime(start, tz) ?: "sunrise",
            activeHint = if (lateHintTemplate == null) "Recite Birkat Hachamah outdoors at sunrise while viewing the sun." else null,
            activeHintTemplate = lateHintTemplate,
            activeHintArgs = lateHintArgs,
        )
    }

    /**
     * Kiddush Levana — open 72h / 7d after molad; close at sof zman (molad + 14d 18h 22m).
     * Only ACTIVE after nightfall (tzeit) until dawn each night — not all day.
     * Source: Shulchan Aruch O.C. 426:3–4.
     */
    private fun kiddushLevanaWindow(
        now: Long,
        z: ZmanimSnapshot,
        tz: String,
        prayerDay: PrayerDayContext,
    ): ItemZmanStatus {
        val day = prayerDay.hebrewDay
            ?: return ItemZmanStatus(
                availability = ItemZmanAvailability.UPCOMING,
                hint = "Set location for Hebrew date and timing this month.",
            )
        val year = prayerDay.hebrewYear
        val month = prayerDay.hebrewMonth

        val openMillis = if (year != null && month != null) {
            when (prayerDay.nusach) {
                EffectiveNusach.SEFARD, EffectiveNusach.EDOT_HAMIZRACH ->
                    HebrewCalendarEngine.tchilasZmanKidushLevana7DaysMillis(year, month)
                else ->
                    HebrewCalendarEngine.tchilasZmanKidushLevana3DaysMillis(year, month)
            }
        } else {
            null
        }
        val closeMillis = if (year != null && month != null) {
            HebrewCalendarEngine.sofZmanKidushLevanaMillis(year, month)
        } else {
            null
        }

        if (closeMillis != null && now >= closeMillis) {
            val closeLabel = ZmanimFormatter.formatTimeWithDate(closeMillis, tz) ?: "sof zman"
            return ItemZmanStatus(
                availability = ItemZmanAvailability.EXPIRED,
                hint = "Sof Zman Kiddush Levana passed ($closeLabel). Window reopens after the next molad.",
                hintTemplate = "Sof Zman Kiddush Levana passed ({time}). Window reopens after the next molad.",
                hintArgs = mapOf("time" to closeLabel),
            )
        }

        if (openMillis != null && now < openMillis) {
            val waitLabel = when (prayerDay.nusach) {
                EffectiveNusach.SEFARD, EffectiveNusach.EDOT_HAMIZRACH -> "7 days after the molad"
                else -> "72 hours after the molad"
            }
            val whenLabel = ZmanimFormatter.formatTimeWithDate(openMillis, tz) ?: waitLabel
            return ItemZmanStatus(
                availability = ItemZmanAvailability.UPCOMING,
                windowStartMillis = openMillis,
                windowEndMillis = closeMillis,
                hint = "Opens $whenLabel ($waitLabel). Say outdoors after nightfall when the moon is visible.",
                hintTemplate = "Opens {when} ({wait}). Say outdoors after nightfall when the moon is visible.",
                hintArgs = mapOf("when" to whenLabel, "wait" to waitLabel),
                availableAtLabel = whenLabel,
            )
        }

        if (openMillis == null || closeMillis == null) {
            return ItemZmanStatus(
                availability = ItemZmanAvailability.UPCOMING,
                hint = "Set location for molad-based Sof Zman Kiddush Levana (14d 18h 22m after the molad).",
            )
        }

        // Night gate: recite outdoors after tzeit until dawn (shifted after rollover / midnight).
        val nightStart = HalachicNightWindow.nightStartMillis(now, z)
        val nightEnd = ZmanPeriodLogic.effectiveEveningEnd(now, z)
            ?: z.alotHaShacharMillis
            ?: z.sunriseMillis
        val preferred = kiddushLevanaPreferredTiming(prayerDay)
        val closeHint = closeMillis.let {
            val label = ZmanimFormatter.formatTimeWithDate(it, tz) ?: "sof zman (molad + 14d 18h 22m)"
            " Sof Zman ends $label."
        }
        val lateWindow = closeMillis - now <= 36 * 60 * 60 * 1000L
        val nightHint = when {
            lateWindow || day >= 14 -> "Last chance this month — $preferred.$closeHint"
            day >= 12 -> "$preferred Window closes at sof zman (molad + 14d 18h 22m).$closeHint"
            else -> preferred + closeHint
        }

        // Do not point UPCOMING/ACTIVE at tonight's tzeit when tonight is Shabbat or Yom Tov.
        // Use Maariv's holy-night label only — never raw isErevShabbat (tzeit-rollover leak).
        if (TonightHolyDayRules.tonightBeginsHolyDayMelacha(prayerDay)) {
            val holy = TonightHolyDayRules.holyDayLabelForDeferral(prayerDay)
            return ItemZmanStatus(
                availability = ItemZmanAvailability.UPCOMING,
                windowStartMillis = null,
                windowEndMillis = closeMillis,
                hint = "Not tonight — $holy begins at nightfall. $nightHint",
                hintTemplate = "Not tonight — {holyDay} begins at nightfall. {timing}",
                hintArgs = mapOf("holyDay" to holy, "timing" to nightHint),
                availableAtLabel = "after $holy",
            )
        }

        if (nightStart != null && now < nightStart) {
            val tonightLabel = ZmanimFormatter.formatAfter(nightStart, tz) ?: "after nightfall (tzeit)"
            return ItemZmanStatus(
                availability = ItemZmanAvailability.UPCOMING,
                windowStartMillis = nightStart,
                windowEndMillis = closeMillis,
                hint = "Say outdoors $tonightLabel when the moon is visible. $nightHint",
                hintTemplate = "Say outdoors {tonight} when the moon is visible. {timing}",
                hintArgs = mapOf("tonight" to tonightLabel, "timing" to nightHint),
                availableAtLabel = tonightLabel,
            )
        }
        if (nightEnd != null && now >= nightEnd) {
            return ItemZmanStatus(
                availability = ItemZmanAvailability.EXPIRED,
                windowEndMillis = closeMillis,
                hint = "Tonight's Kiddush Levana window ended at dawn. Try again after nightfall while the monthly window is open.$closeHint",
                availableAtLabel = "nightfall",
            )
        }

        return ItemZmanStatus(
            availability = ItemZmanAvailability.ACTIVE,
            windowStartMillis = nightStart,
            windowEndMillis = nightEnd ?: closeMillis,
            hint = nightHint,
        )
    }

    /** Eruv tavshilin must be completed before Yom Tov candle lighting / sunset. */
    private fun eruvTavshilinWindow(
        now: Long,
        z: ZmanimSnapshot,
        tz: String,
        prayerDay: PrayerDayContext,
    ): ItemZmanStatus {
        val sunset = z.sunsetMillis
            ?: return ItemZmanStatus(hint = "Eruv tavshilin — complete before Yom Tov begins (set location for sunset).")
        val end = sunset - prayerDay.candleLeadMinutes * 60_000L
        val start = z.alotHaShacharMillis ?: z.sunriseMillis ?: (end - 12 * 60 * 60 * 1000L)
        val endLabel = ZmanimFormatter.formatTime(end, tz) ?: "candle lighting"
        return windowStatus(
            now = now,
            start = start,
            end = end,
            upcoming = "Eruv tavshilin — make today before candle lighting ($endLabel).",
            upcomingTemplate = "Eruv tavshilin — make today before candle lighting ({time}).",
            upcomingArgs = mapOf("time" to endLabel),
            expired = "Eruv tavshilin must be made before Yom Tov begins — ask your rav if you missed the deadline.",
            makeup = "If Yom Tov has already begun, do not make an eruv; ask your rav about cooking for Shabbat.",
            availableAtLabel = "dawn",
            activeHint = "Make the eruv before candle lighting ($endLabel).",
            activeHintTemplate = "Make the eruv before candle lighting ({time}).",
            activeHintArgs = mapOf("time" to endLabel),
        )
    }

    /**
     * Arba Minim — daytime mitzvah until sunset (not Mincha Gedola).
     * Must be in appliesTo so Chol HaMoed / Hoshana Rabbah section bypass cannot swallow it.
     */
    private fun arbaMinimWindow(now: Long, z: ZmanimSnapshot, tz: String): ItemZmanStatus {
        val start = z.alotHaShacharMillis ?: z.sunriseMillis
            ?: return ItemZmanStatus(hint = "Arba Minim — set location in Settings for dawn times.")
        val end = z.sunsetMillis
            ?: return ItemZmanStatus(hint = "Arba Minim — set location in Settings for sunset times.")
        val endClock = ZmanimFormatter.formatTime(end, tz) ?: "sunset"
        return windowStatus(
            now = now,
            start = start,
            end = end,
            upcoming = "Arba Minim — take the Four Species after dawn (morning preferred).",
            upcomingTemplate = "Arba Minim — available after dawn ({time}).",
            upcomingArgs = mapOf(
                "time" to (ZmanimFormatter.formatAfter(start, tz) ?: "after dawn"),
            ),
            expired = "Today's Arba Minim window has closed (past sunset).",
            makeup = "If you missed the daytime mitzvah, ask your rav — it does not carry into the night.",
            availableAtLabel = "dawn",
            activeHint = "Take the Four Species today before sunset ($endClock) — morning is preferred.",
            activeHintTemplate = "Take the Four Species today before sunset ({time}) — morning is preferred.",
            activeHintArgs = mapOf("time" to endClock),
        )
    }

    /** Hoshana Rabbah aravot — morning minhag; expire at chatzos (not all day / night). */
    private fun hoshanaAravotWindow(now: Long, z: ZmanimSnapshot, tz: String): ItemZmanStatus {
        val start = z.alotHaShacharMillis ?: z.sunriseMillis
            ?: return ItemZmanStatus(hint = "Aravot — set location in Settings for dawn times.")
        val end = z.chatzosMillis
            ?: return ItemZmanStatus(hint = "Aravot — set location in Settings for midday times.")
        val endClock = ZmanimFormatter.formatTime(end, tz) ?: "halachic midday"
        return windowStatus(
            now = now,
            start = start,
            end = end,
            upcoming = "Hoshana Rabbah aravot — usually during / after Shacharit.",
            upcomingTemplate = "Hoshana Rabbah aravot — available after dawn ({time}).",
            upcomingArgs = mapOf(
                "time" to (ZmanimFormatter.formatAfter(start, tz) ?: "after dawn"),
            ),
            expired = "The morning aravot window has closed (past $endClock).",
            expiredTemplate = "The morning aravot window has closed (past {time}).",
            expiredArgs = mapOf("time" to endClock),
            makeup = "If your kehilla still beats aravot later, follow the shul — otherwise ask your rav.",
            availableAtLabel = "dawn",
            activeHint = "Beat the aravot (Minhag Nevi'im) this morning — typically with Hoshana Rabbah.",
        )
    }

    /**
     * Erev Chanukah setup reminder.
     * Friday: hard deadline at Shabbat candles (Night 1 must be lit earlier).
     * Other weekdays: daytime prep until sunset (lighting row takes over after tzeit).
     */
    private fun erevChanukahPrepWindow(
        now: Long,
        z: ZmanimSnapshot,
        tz: String,
        prayerDay: PrayerDayContext,
    ): ItemZmanStatus {
        val friday = prayerDay.tonightBeginsShabbat
        return erevFinishBeforeNightWindow(
            now = now,
            z = z,
            tz = tz,
            prayerDay = prayerDay,
            label = "Erev Chanukah prep",
            endAtCandles = friday,
            activeHint = if (friday) {
                "Finish menorah setup before Shabbat candles — light Night 1 from plag if needed."
            } else {
                "Set up the menorah today — light Night 1 after nightfall (tzeit)."
            },
            expired = if (friday) {
                "Erev Chanukah prep window has closed (past candle lighting)."
            } else {
                "Erev Chanukah daytime prep window has closed (past sunset)."
            },
            makeup = if (friday) {
                "If you missed Friday setup, do not light on Shabbat — Motzei after tzeit is Saturday night's candles."
            } else {
                "If the menorah is not ready, set it up now and light after tzeit if still night."
            },
        )
    }

    /** Taanit Bechorot — dawn (alot) through nightfall (tzeit), like other daytime fasts. */
    private fun taanitBechorotWindow(now: Long, z: ZmanimSnapshot, tz: String): ItemZmanStatus {
        val start = z.alotHaShacharMillis ?: z.sunriseMillis
            ?: return ItemZmanStatus(hint = "Taanit Bechorot — set location in Settings for dawn times.")
        val end = z.tzeitMillis ?: z.sunsetMillis
            ?: return ItemZmanStatus(hint = "Taanit Bechorot — set location in Settings for nightfall times.")
        val endClock = ZmanimFormatter.formatTime(end, tz) ?: "nightfall"
        return windowStatus(
            now = now,
            start = start,
            end = end,
            upcoming = "Taanit Bechorot — from dawn until nightfall (or attend a siyum).",
            upcomingTemplate = "Taanit Bechorot — begins at dawn ({time}).",
            upcomingArgs = mapOf(
                "time" to (ZmanimFormatter.formatAfter(start, tz) ?: "dawn"),
            ),
            expired = "Taanit Bechorot has ended (past nightfall — $endClock).",
            expiredTemplate = "Taanit Bechorot has ended (past nightfall — {time}).",
            expiredArgs = mapOf("time" to endClock),
            makeup = "If you are a firstborn who neither fasted nor heard a siyum, ask your rav.",
            availableAtLabel = "dawn",
            activeHint = "Fast from dawn until nightfall, or attend a siyum if that is your minhag.",
            activeHintTemplate = "Fast until nightfall ({time}), or attend a siyum if that is your minhag.",
            activeHintArgs = mapOf("time" to endClock),
        )
    }

    /**
     * Erev-day prep / last meals that must finish before night (candles or sunset).
     * Wired here so festival-prep / Fasts bypass cannot leave them ACTIVE forever.
     */
    private fun erevFinishBeforeNightWindow(
        now: Long,
        z: ZmanimSnapshot,
        tz: String,
        prayerDay: PrayerDayContext,
        label: String,
        endAtCandles: Boolean,
        activeHint: String,
        expired: String,
        makeup: String?,
    ): ItemZmanStatus {
        val sunset = z.sunsetMillis
            ?: return ItemZmanStatus(hint = "$label — set location in Settings for sunset times.")
        val end = if (endAtCandles) {
            sunset - prayerDay.candleLeadMinutes * 60_000L
        } else {
            sunset
        }
        val start = z.alotHaShacharMillis ?: z.sunriseMillis ?: (end - 12 * 60 * 60 * 1000L)
        val endLabel = if (endAtCandles) {
            ZmanimFormatter.formatTime(end, tz) ?: "candle lighting"
        } else {
            ZmanimFormatter.formatTime(end, tz) ?: "sunset"
        }
        val endWord = if (endAtCandles) "candle lighting" else "sunset"
        return windowStatus(
            now = now,
            start = start,
            end = end,
            upcoming = "$label — finish today before $endWord ($endLabel).",
            upcomingTemplate = "$label — finish today before $endWord ({time}).",
            upcomingArgs = mapOf("time" to endLabel),
            expired = expired,
            makeup = makeup,
            availableAtLabel = "dawn",
            activeHint = activeHint,
            activeHintTemplate = "$activeHint (deadline {time}).",
            activeHintArgs = mapOf("time" to endLabel),
        )
    }

    private fun kiddushLevanaPreferredTiming(prayerDay: PrayerDayContext): String {
        val month = prayerDay.hebrewMonth
        val day = prayerDay.hebrewDay
        when (month) {
            HebrewCalendarEngine.AV -> if (day != null && day < 10) {
                return "Most wait until after Tisha B'Av, unless concerned the moon may not be visible later."
            }
            HebrewCalendarEngine.TISHREI -> if (day != null && day < 11) {
                return "Many wait until after Yom Kippur, unless concerned the moon may not be visible later."
            }
        }
        if (prayerDay.tonightBeginsShabbat) {
            return "Tonight after Shabbat — ideally still in Shabbat clothes."
        }
        return "Ideally Motzei Shabbat in nice clothes; say when the moon is visible."
    }

    private fun dayOrdinal(day: Int): String = when (day) {
        1 -> "1st"; 2 -> "2nd"; 3 -> "3rd"; 4 -> "4th"; 5 -> "5th"
        6 -> "6th"; 7 -> "7th"; 8 -> "8th"; 9 -> "9th"; 10 -> "10th"
        11 -> "11th"; 12 -> "12th"; 13 -> "13th"; 14 -> "14th"
        else -> "${day}th"
    }
}
