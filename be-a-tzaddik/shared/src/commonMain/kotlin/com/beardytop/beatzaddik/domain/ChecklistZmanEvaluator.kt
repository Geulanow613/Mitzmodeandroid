package com.beardytop.beatzaddik.domain

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
        "yaaleh_vyavo_rosh_chodesh_shacharit",
        "yaaleh_vyavo_rosh_chodesh_mincha",
        "yaaleh_vyavo_rosh_chodesh",
        "rosh_chodesh_observances",
        "bedtime_shema_first_paragraph_though_recommended_to_say_enti",
        "bedtime_shema_women",
        "hamapil_blessing_according_to_many_opinions",
        "at_least_one_prayer_daily_typically_morning",
        "ashkenaz_korbanot_before_shacharit",
        "chabad_korbanot_before_shacharit",
        "sefard_korbanot_before_mincha",
        "edot_hamizrach_korbanot_before_mincha",
        "ldovid_hashem_ori",
        "birkat_hachamah",
        "birkat_hailanot",
        "melave_malkah",
        "shabbat_candles",
        "public_fast_day",
        "tefillin_tisha_beav_mincha",
        "motzei_yom_kippur_meal",
        "purim_meshulash_erev_megillah",
        "purim_meshulash_friday_megillah",
        "purim_meshulash_friday_matanot",
    ) + TachanunRules.tachanunItemIds

    fun appliesTo(itemId: String): Boolean =
        itemId in zmanItemIds || itemId.startsWith("chanukah_lighting_day_")

    fun evaluate(
        itemId: String,
        nowMillis: Long,
        zmanim: ZmanimSnapshot?,
        prayerDay: PrayerDayContext
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
        if (itemId.startsWith("chanukah_lighting_day_")) {
            val rawPlag = z.plagHaminchaMillis
                ?: return ItemZmanStatus(hint = "Set location in Settings for Plag HaMincha times.")
            val end = ZmanPeriodLogic.nightObligationWindowEnd(z) ?: return ItemZmanStatus()

            // Chanukah lighting "belongs" to the night that began after sunset.
            // Some backends attach night zmanim to the next civil date; in that case, a 10pm
            // simulation could be before today's (next-day) plag/tzeit. Correct by shifting
            // relevant times back by 24h when we're before dawn.
            val dawn = z.alotHaShacharMillis ?: z.sunriseMillis
            val shiftBack = (dawn != null && nowMillis < dawn)
            val plag = if (shiftBack) rawPlag - 24 * 60 * 60 * 1000L else rawPlag

            val plagClock = ZmanimFormatter.formatAfter(plag, tz) ?: "Plag HaMincha"
            val tzeitForThisNight = z.tzeitMillis?.let { tzeit -> if (shiftBack) tzeit - 24 * 60 * 60 * 1000L else tzeit }
            val burnUntilMillis = tzeitForThisNight?.plus(30 * 60 * 1000L)
            val burnUntilTime = ZmanimFormatter.formatTime(burnUntilMillis, tz)
            val burnNoteAlways = "Candles must have enough oil/wick to burn at least 30 minutes after nightfall."
            val burnNoteWithTime = burnUntilTime?.let {
                "If lighting early, ensure enough oil/candles to burn until $it (30 min after nightfall)."
            } ?: burnNoteAlways
            return when {
                nowMillis < plag -> ItemZmanStatus(
                    availability = ItemZmanAvailability.UPCOMING,
                    windowStartMillis = plag,
                    windowEndMillis = end,
                    hintTemplate = "Available in {time} · at Plag HaMincha",
                    hintArgs = mapOf("time" to plagClock),
                    makeupNote = burnNoteWithTime,
                    availableAtLabel = "Plag HaMincha",
                )
                nowMillis >= end -> ItemZmanStatus(
                    availability = ItemZmanAvailability.EXPIRED,
                )
                else -> ItemZmanStatus(
                    availability = ItemZmanAvailability.ACTIVE,
                    windowStartMillis = plag,
                    windowEndMillis = end,
                    hint = burnNoteAlways,
                    availableAtLabel = "Plag HaMincha",
                )
            }
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
            "ashkenaz_shacharit_tachanun",
            "sefard_shacharit_tachanun",
            "edot_hamizrach_shacharit_tachanun",
            "chabad_shacharit_tachanun" -> shacharitPartsWindow(
                nowMillis, z, tz, label = "Tachanun at Shacharit",
            )
            "ldovid_hashem_ori" -> shacharitPartsWindow(
                nowMillis, z, tz, label = "L'Dovid (Psalm 27)"
            )
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
                if (prayerDay.isShabbatOrYomTov) {
                    return ItemZmanStatus(
                        availability = ItemZmanAvailability.EXPIRED,
                        hint = "Tefillin are not worn on Shabbat or Festivals."
                    )
                }
                if (TefillinSeasonalRules.shouldOmitTefillinOnCholHamoed(prayerDay)) {
                    return ItemZmanStatus(
                        availability = ItemZmanAvailability.EXPIRED,
                        hint = TefillinSeasonalRules.cholHamoedOmittedHint(),
                    )
                }
                if (TishaBeavTefillinRules.isTishaBeav(prayerDay.fastDayIndex) &&
                    TishaBeavTefillinRules.omitsMorningTefillin(prayerDay.nusach)
                ) {
                    return ItemZmanStatus(
                        availability = ItemZmanAvailability.EXPIRED,
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
            "ashkenaz_musaf_tachanun",
            "sefard_mincha_tachanun",
            "edot_hamizrach_mincha_tachanun",
            "chabad_mincha_tachanun",
            -> minchaWindow(nowMillis, z, tz, label = "Tachanun at Mincha", prayerDay = prayerDay)
            "mincha_shemoneh_esrei_tachanun" -> minchaWindow(nowMillis, z, tz, label = "Mincha", prayerDay = prayerDay)
            "evening_shema_with_its_blessings" -> eveningShemaWindow(nowMillis, z, tz)
            "maariv_shemoneh_esrei" -> maarivWindow(nowMillis, z, tz, prayerDay)
            "rosh_chodesh_half_hallel",
            "rosh_chodesh_full_hallel_chanukah" -> shacharitPartsWindow(
                nowMillis, z, tz, label = "Hallel at Shacharit",
            )
            "yaaleh_vyavo_rosh_chodesh_shacharit" -> yaalehVyavoShacharitWindow(nowMillis, z, tz)
            "yaaleh_vyavo_rosh_chodesh_mincha" -> yaalehVyavoMinchaWindow(nowMillis, z, tz, prayerDay)
            "yaaleh_vyavo_rosh_chodesh" -> yaalehVyavoMaarivWindow(nowMillis, z, tz)
            "rosh_chodesh_observances" -> roshChodeshObservancesWindow(nowMillis, z, tz)
            "bedtime_shema_first_paragraph_though_recommended_to_say_enti",
            "bedtime_shema_women" ->
                bedtimeWindow(nowMillis, z, tz, "Bedtime Shema")
            "hamapil_blessing_according_to_many_opinions" ->
                bedtimeWindow(nowMillis, z, tz, "Hamapil blessing")
            "at_least_one_prayer_daily_typically_morning" -> womenDailyPrayerWindow()
            "kiddush_levana" -> kiddushLevanaWindow(nowMillis, tz, prayerDay)
            "birkat_hachamah" -> birkatHachamahWindow(nowMillis, z, tz)
            "birkat_hailanot" -> birkatHaIlanotHint(prayerDay)
            "melave_malkah" -> melaveMalkaWindow(nowMillis, z, tz, prayerDay)
            "shabbat_candles" -> shabbatCandlesWindow(nowMillis, z, tz, prayerDay)
            "public_fast_day" -> publicFastWindow(nowMillis, z, tz, prayerDay.fastDayIndex)
            "motzei_yom_kippur_meal" -> motzeiYomKippurMealWindow(nowMillis, z, tz)
            "purim_meshulash_erev_megillah" -> purimMegillahNightWindow(nowMillis, z, tz)
            "purim_meshulash_friday_megillah" -> purimMegillahDayWindow(nowMillis, z, tz, "Friday daytime Megillah")
            "purim_meshulash_friday_matanot" -> purimMatanotDayWindow(nowMillis, z, tz)
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
                hint = "Musaf is only on Shabbat, Festivals, and Rosh Chodesh — not a regular weekday."
            )
        }
        val label = day.musafDayLabel()
        val start = z.sunriseMillis
        // SA OC 286:1: ideal before end of 7th halachic hour; bedi'eved "its time is all day"
        // (זמנה כל היום) — does not expire until sunset.
        val idealEnd = ZmanimHelpers.endOfHalachicHourMillis(z, 7)
        val absoluteEnd = z.sunsetMillis
        val shabbatNote = if (day.isShabbatOrYomTov) {
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

    private fun roshChodeshObservancesWindow(now: Long, z: ZmanimSnapshot, tz: String): ItemZmanStatus {
        val start = z.alotHaShacharMillis ?: z.sunriseMillis
        val end = z.tzeitMillis ?: z.sunsetMillis
        return windowStatus(
            now, start, end,
            upcoming = "Rosh Chodesh observances — from dawn (after dawn) through nightfall tonight.",
            upcomingTemplate = "Rosh Chodesh observances — from dawn ({time}) through nightfall tonight.",
            upcomingArgs = mapOf("time" to (ZmanimFormatter.formatAfter(start, tz) ?: "after dawn")),
            expired = "Tonight's Rosh Chodesh ended at nightfall (tzeit).",
            makeup = null,
            availableAtLabel = "dawn",
            activeHint = "It is a mitzvah to have a festive meal during the day in honor of Rosh Chodesh.",
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
            upcoming = "Light Shabbat candles by $candlesClock ($lead min before sunset).",
            upcomingTemplate = "Light Shabbat candles by {time} ({lead} min before sunset).",
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

    private fun purimMegillahNightWindow(now: Long, z: ZmanimSnapshot, tz: String): ItemZmanStatus {
        val start = z.tzeitMillis
            ?: return ItemZmanStatus(hint = "Set location in Settings for nightfall (tzeit) times.")
        val end = z.sunriseMillis?.plus(24 * 60 * 60 * 1000L)
            ?: z.alotHaShacharMillis?.plus(24 * 60 * 60 * 1000L)
        val startLabel = ZmanimFormatter.formatTime(start, tz) ?: "nightfall"
        return windowStatus(
            now = now,
            start = start,
            end = end,
            upcoming = "Megillah reading — after nightfall (tzeit).",
            upcomingTemplate = "Megillah reading — after nightfall ({time}).",
            upcomingArgs = mapOf("time" to startLabel),
            expired = "Tonight's Megillah window has passed — hear it tomorrow morning if you missed it.",
            makeup = "If you missed tonight's reading, the Friday morning reading is still required.",
            availableAtLabel = "nightfall",
            activeHint = "Hear the Megillah tonight after Maariv or at your shul's reading time.",
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

    private fun motzeiYomKippurMealWindow(now: Long, z: ZmanimSnapshot, tz: String): ItemZmanStatus {
        val start = z.tzeitMillis
            ?: return ItemZmanStatus(hint = "Break-fast meal — available after nightfall when Yom Kippur ends.")
        return windowStatus(
            now = now,
            start = start,
            end = null,
            upcoming = "Motzei Yom Kippur meal — after nightfall (tzeit).",
            upcomingTemplate = "Motzei Yom Kippur meal — after nightfall ({time}).",
            upcomingArgs = mapOf("time" to (ZmanimFormatter.formatAfter(start, tz) ?: "tzeit")),
            expired = "Tonight's break-fast window has passed.",
            makeup = null,
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
     * Kiddush Levana — Sanctification of the New Moon.
     *
     * Window opens: Ashkenaz/Chabad 72 hours after the molad; Sephardi (Mechaber) 7 days after
     * the molad. Window closes at or before the full moon (~14.75 days from the molad —
     * approximately; opinions vary). Uses molad-based open times and the Hebrew day as a coarse
     * close proxy; check Sof Zman Kiddush Levana for your location.
     *
     * Source: Shulchan Aruch OC 426:3–4; Rama ibid.; Mishnah Berurah 426:20.
     */
    private fun kiddushLevanaWindow(
        now: Long,
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

        if (day > 15) {
            return ItemZmanStatus(
                availability = ItemZmanAvailability.EXPIRED,
                hint = "Window closed for this month — reopens after the next new moon.",
                makeupNote = null,
            )
        }

        val openMillis = if (year != null && month != null) {
            when (prayerDay.nusach) {
                EffectiveNusach.SEFARD, EffectiveNusach.EDOT_HAMIZRACH ->
                    HebrewCalendarEngine.tchilasZmanKidushLevana7DaysMillis(year, month)
                else ->
                    HebrewCalendarEngine.tchilasZmanKidushLevana3DaysMillis(year, month)
            }
        } else null

        if (openMillis != null && now < openMillis) {
            val waitLabel = when (prayerDay.nusach) {
                EffectiveNusach.SEFARD, EffectiveNusach.EDOT_HAMIZRACH -> "7 days after the molad"
                else -> "72 hours after the molad"
            }
            val whenLabel = ZmanimFormatter.formatTimeWithDate(openMillis, tz)
                ?: waitLabel
            return ItemZmanStatus(
                availability = ItemZmanAvailability.UPCOMING,
                windowStartMillis = openMillis,
                hint = "Opens $whenLabel ($waitLabel). Say outdoors after nightfall when the moon is visible.",
                hintTemplate = "Opens {when} ({wait}). Say outdoors after nightfall when the moon is visible.",
                hintArgs = mapOf("when" to whenLabel, "wait" to waitLabel),
                availableAtLabel = whenLabel,
            )
        }

        if (day == 15) {
            return ItemZmanStatus(
                availability = ItemZmanAvailability.ACTIVE,
                hint = "Last chance may be tonight — say if the moon is visible and still within Sof Zman.",
            )
        }

        // Fallback when year/month unavailable: Hebrew-day proxy (3rd / 7th).
        if (openMillis == null) {
            val minDay = when (prayerDay.nusach) {
                EffectiveNusach.SEFARD, EffectiveNusach.EDOT_HAMIZRACH -> 7
                else -> 3
            }
            if (day < minDay) {
                val opens = if (minDay == 7) "the 7th" else "the 3rd"
                return ItemZmanStatus(
                    availability = ItemZmanAvailability.UPCOMING,
                    hint = "Opens around $opens this month.",
                    hintTemplate = "Opens around {day} this month.",
                    hintArgs = mapOf("day" to opens),
                    availableAtLabel = if (minDay == 7) "7th of the month" else "3rd of the month",
                )
            }
        }

        val preferred = kiddushLevanaPreferredTiming(prayerDay)
        return when {
            day >= 14 -> ItemZmanStatus(
                availability = ItemZmanAvailability.ACTIVE,
                hint = "Last chance this month — $preferred",
                hintTemplate = "Last chance this month — {timing}",
                hintArgs = mapOf("timing" to preferred),
            )
            day >= 12 -> ItemZmanStatus(
                availability = ItemZmanAvailability.ACTIVE,
                hint = "$preferred Window closes at the full moon.",
                hintTemplate = "{timing} Window closes at the full moon.",
                hintArgs = mapOf("timing" to preferred),
            )
            else -> ItemZmanStatus(
                availability = ItemZmanAvailability.ACTIVE,
                hint = preferred,
            )
        }
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
        if (prayerDay.isErevShabbat) {
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
