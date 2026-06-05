package com.beardytop.beatzaddik.domain

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
        "ashkenaz_musaf_tachanun",
        "evening_shema_with_its_blessings",
        "maariv_shemoneh_esrei",
        "bedtime_shema_first_paragraph_though_recommended_to_say_enti",
        "bedtime_shema_women",
        "hamapil_blessing_according_to_many_opinions",
        "at_least_one_prayer_daily_typically_morning",
        "ashkenaz_korbanot_before_shacharit",
        "chabad_korbanot_before_shacharit"
    )

    fun appliesTo(itemId: String): Boolean = itemId in zmanItemIds

    fun evaluate(
        itemId: String,
        nowMillis: Long,
        zmanim: ZmanimSnapshot?,
        prayerDay: PrayerDayContext
    ): ItemZmanStatus {
        if (!appliesTo(itemId)) return ItemZmanStatus()
        val z = zmanim ?: return ItemZmanStatus(
            hint = "Set location in Settings for zman-based reminders."
        )
        if (!z.hasLocationTimes) {
            return ItemZmanStatus(hint = "Set location in Settings for accurate zmanim.")
        }
        val tz = z.timezoneId
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
                tefillinWindow(nowMillis, z, tz)
            }
            "musaf_only_on_rosh_chodesh_festivals_and_shabbat" ->
                musafWindow(nowMillis, z, tz, prayerDay)
            "ashkenaz_musaf_tachanun" -> minchaWindow(nowMillis, z, tz, label = "Tachanun at Mincha")
            "mincha_shemoneh_esrei_tachanun" -> minchaWindow(nowMillis, z, tz, label = "Mincha")
            "evening_shema_with_its_blessings" -> eveningShemaWindow(nowMillis, z, tz)
            "maariv_shemoneh_esrei" -> maarivWindow(nowMillis, z, tz)
            "bedtime_shema_first_paragraph_though_recommended_to_say_enti",
            "bedtime_shema_women" ->
                bedtimeWindow(nowMillis, z, tz, "Bedtime Shema")
            "hamapil_blessing_according_to_many_opinions" ->
                bedtimeWindow(nowMillis, z, tz, "Hamapil blessing")
            "at_least_one_prayer_daily_typically_morning" -> amidahWindow(
                nowMillis, z, tz, label = "daily prayer"
            )
            "kiddush_levana" -> kiddushLevanaWindow(prayerDay)
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
            end = null,   // No expiry — valid throughout the day once the day begins
            upcoming = "Available after halachic midnight (chatzos halayla) — " +
                "${ZmanimFormatter.formatAfter(start, tz) ?: "later tonight"}. " +
                "Most say $label when they wake, even after a nap.",
            expired = "$label can be said any time during the day.",
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
        val start = z.misheyakirMillis ?: z.sunriseMillis
        val idealEnd = z.sofZmanTefillaMillis
        val absoluteEnd = z.chatzosMillis
        val lateHint = if (idealEnd != null && now >= idealEnd)
            "Ideal time for Shacharit passed (${ZmanimFormatter.formatTime(idealEnd, tz) ?: "late morning"}). You can still daven until midday."
        else null
        return windowStatus(
            now, start, absoluteEnd,
            upcoming = "$label is part of the morning Shacharit service, from dawn (${ZmanimFormatter.formatAfter(start, tz) ?: "after dawn"}).",
            expired = "The morning Shacharit window has closed (past halachic midday / chatzos).",
            makeup = "If you missed Shacharit today, at Mincha pray two Amidot — Mincha first, then tashlumin for Shacharit.",
            availableAtLabel = "dawn",
            activeHint = lateHint
        )
    }

    /**
     * Morning Shema — expires at sof zman Shema (end of 3rd halachic hour).
     * After this time, the morning Shema obligation cannot be fulfilled — universally accepted.
     */
    private fun shemaWindow(now: Long, z: ZmanimSnapshot, tz: String): ItemZmanStatus {
        val start = z.misheyakirMillis ?: z.sunriseMillis
        val end = z.sofZmanShemaMillis   // end of 3rd halachic hour — no obligation can be fulfilled after this

        val expiredNote = "Sof zman Shema has passed (${ZmanimFormatter.formatTime(end, tz) ?: "end of 3rd hour"}). " +
            "The biblical morning Shema obligation cannot be fulfilled today."

        return windowStatus(
            now, start, end,
            upcoming = "Morning Shema opens at dawn (${ZmanimFormatter.formatAfter(start, tz) ?: "after dawn"}).",
            expired = expiredNote,
            makeup = "Bedi'eved: still say Shema with its blessings in Shacharit until chatzos — this does not fulfill the biblical obligation. Evening Shema is separate.",
            availableAtLabel = "dawn"
        )
    }

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
        val lateHint = when {
            idealEnd != null && now >= idealEnd ->
                "Ideal Shacharit time passed (${ZmanimFormatter.formatTime(idealEnd, tz) ?: "late morning"}). Still valid until midday (chatzos)."
            sunrise != null && start != null && now >= start && now < sunrise ->
                "Available from dawn. Ideal at sunrise (${ZmanimFormatter.formatTime(sunrise, tz) ?: "sunrise"}) or later — if you're in a hurry you may say it now."
            else -> null
        }
        val upcomingBase =
            "$label is available from dawn (${ZmanimFormatter.formatAfter(start, tz) ?: "after dawn"})."
        val upcomingIdeal = if (sunrise != null) {
            " Ideal at sunrise (${ZmanimFormatter.formatAfter(sunrise, tz) ?: "sunrise"}) or later."
        } else {
            " Ideal from sunrise or later."
        }
        return windowStatus(
            now, start, absoluteEnd,
            upcoming = upcomingBase + upcomingIdeal,
            expired = "Shacharit window closed at halachic midday (chatzos).",
            makeup = "Missed Shacharit? At Mincha, pray two Amidot — first for Mincha itself, then second as tashlumin for the missed Shacharit.",
            availableAtLabel = "dawn",
            activeHint = lateHint
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
        val lateHint = if (idealEnd != null && now >= idealEnd)
            "Ideal time for putting on tefillin (with morning prayer) has passed. Tefillin are still valid to wear until sunset (${ZmanimFormatter.formatTime(absoluteEnd, tz) ?: "sunset"})."
        else null
        return windowStatus(
            now, start, absoluteEnd,
            upcoming = "Tefillin are worn from dawn/misheyakir (${ZmanimFormatter.formatAfter(start, tz) ?: "after dawn"}) — ideally during Shacharit.",
            expired = "Tefillin are not worn after sunset.",
            makeup = null,
            availableAtLabel = "dawn",
            activeHint = lateHint
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
        // SA OC 286:1: ideal before end of 7th hour (chatzos); bedi'eved "its time is all day"
        // (זמנה כל היום) — does not expire until sunset.
        val idealEnd = z.chatzosMillis
        val absoluteEnd = z.sunsetMillis
        val shabbatNote = if (day.isShabbatOrYomTov) {
            " On Shabbat and Festivals, fulfill this without using your phone."
        } else ""
        val lateHint = if (idealEnd != null && now >= idealEnd)
            "Musaf should ideally be prayed before midday (chatzos). You can still fulfill it — if Mincha time has arrived, pray Mincha first (SA OC 286:4).$shabbatNote"
        else null
        return windowStatus(
            now, start, absoluteEnd,
            upcoming = "Musaf ($label) — after the morning Amidah, from ${ZmanimFormatter.formatAfter(start, tz) ?: "sunrise"}.",
            expired = "Musaf window has closed (sunset).$shabbatNote",
            makeup = "If you missed Musaf entirely, there is no makeup (tashlumin) for it (SA OC 286:1).",
            availableAtLabel = "sunrise",
            activeHint = lateHint
        )
    }

    private fun minchaWindow(now: Long, z: ZmanimSnapshot, tz: String, label: String): ItemZmanStatus {
        val start = z.minchaGedolaMillis ?: z.chatzosMillis?.let { it + 30 * 60 * 1000L }
        val end = z.tzeitMillis ?: z.sunsetMillis ?: z.plagHaminchaMillis
        return windowStatus(
            now, start, end,
            upcoming = "$label appears ${ZmanimFormatter.formatAfter(start, tz) ?: "about 30 min after chatzos (midday)"} — Mincha Gedolah.",
            expired = "Today's $label window ended ${ZmanimFormatter.formatUntil(end, tz) ?: "at nightfall"}.",
            makeup = "Missed Mincha? At Maariv, pray the regular Maariv Amidah first, then one tashlumin Amidah for Mincha. You may make up only the service immediately before Maariv (Mincha) — not an earlier missed service such as Shacharit, which can only be made up at Mincha.",
            availableAtLabel = "midday"
        )
    }

    private fun eveningShemaWindow(now: Long, z: ZmanimSnapshot, tz: String): ItemZmanStatus {
        val start = ZmanPeriodLogic.effectiveEveningStart(now, z)
        val end = ZmanPeriodLogic.effectiveEveningEnd(now, z)
        val tzeitNote = z.tzeitMillis?.let { tzeit ->
            if (now >= tzeit) return@let null
            " Maariv may begin at sunset, but biblical evening Shema begins ${ZmanimFormatter.formatAfter(tzeit, tz) ?: "at nightfall (tzeit)"}. If you daven Maariv early, repeat Shema then."
        }.orEmpty()
        return windowStatus(
            now, start, end,
            upcoming = "Evening Shema — biblical obligation ${ZmanimFormatter.formatAfter(z.tzeitMillis ?: start, tz) ?: "at nightfall (tzeit)"}.$tzeitNote",
            expired = "Tonight's primary window for evening Shema has passed (before alot hashachar).",
            makeup = "If you missed it, ask your rabbi — some fulfill later the same night with guidance; morning Shema is a separate obligation.",
            availableAtLabel = "sunset"
        )
    }

    private fun maarivWindow(now: Long, z: ZmanimSnapshot, tz: String): ItemZmanStatus {
        val start = ZmanPeriodLogic.effectiveEveningStart(now, z)
        val end = ZmanPeriodLogic.effectiveEveningEnd(now, z)
        val tzeitNote = z.tzeitMillis?.let { tzeit ->
            if (now >= tzeit) return@let null
            " Many daven Maariv ideally ${ZmanimFormatter.formatAfter(tzeit, tz) ?: "after nightfall (tzeit)"} — ask your rabbi."
        }.orEmpty()
        return windowStatus(
            now, start, end,
            upcoming = "Maariv — available ${ZmanimFormatter.formatAfter(start, tz) ?: "after sunset"}.$tzeitNote",
            expired = "Tonight's Maariv window has passed (after alot hashachar / dawn).",
            makeup = "Missed Mincha? At Maariv, pray the regular Maariv Amidah first, then one tashlumin Amidah for Mincha only. Shacharit cannot be made up at Maariv — its makeup was only at Mincha.",
            availableAtLabel = "sunset"
        )
    }

    private fun bedtimeWindow(now: Long, z: ZmanimSnapshot, tz: String, label: String): ItemZmanStatus {
        val start = ZmanPeriodLogic.effectiveEveningStart(now, z)
        val end = ZmanPeriodLogic.effectiveEveningEnd(now, z)
        return windowStatus(
            now, start, end,
            upcoming = "$label — when you are ready for sleep (after ${ZmanimFormatter.formatAfter(start, tz) ?: "sunset"}).",
            expired = "Ideal bedtime Shema time is before dawn (${ZmanimFormatter.formatUntil(end, tz) ?: "alot hashachar"}).",
            makeup = null,
            availableAtLabel = "sunset"
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
        activeHint: String? = null
    ): ItemZmanStatus {
        if (start != null && now < start) {
            return ItemZmanStatus(
                availability = ItemZmanAvailability.UPCOMING,
                hint = upcoming,
                makeupNote = makeup,
                windowStartMillis = start,
                windowEndMillis = end,
                availableAtLabel = availableAtLabel
            )
        }
        if (end != null && now >= end) {
            return ItemZmanStatus(
                availability = ItemZmanAvailability.EXPIRED,
                hint = expired,
                makeupNote = makeup,
                windowStartMillis = start,
                windowEndMillis = end,
                availableAtLabel = availableAtLabel
            )
        }
        return ItemZmanStatus(
            hint = activeHint,
            windowStartMillis = start,
            windowEndMillis = end,
            availableAtLabel = availableAtLabel
        )
    }

    /**
     * Kiddush Levana — Sanctification of the New Moon.
     *
     * Window opens: Ashkenaz/Chabad from day 3, Sephardi from day 7 (days after the molad).
     * Window closes: the night of the 15th of the Hebrew month is the latest — day 16+ expired.
     *
     * Source: Shulchan Aruch OC 426:3–4; Rama ibid.; Mishnah Berurah 426:20.
     */
    private fun kiddushLevanaWindow(prayerDay: PrayerDayContext): ItemZmanStatus {
        val day = prayerDay.hebrewDay
            ?: return ItemZmanStatus(
                availability = ItemZmanAvailability.UPCOMING,
                hint = "Set your location so the app knows the Hebrew date and when Kiddush Levana opens this month."
            )

        // Day 16+: window has closed for all opinions
        if (day > 15) {
            return ItemZmanStatus(
                availability = ItemZmanAvailability.EXPIRED,
                hint = "The Kiddush Levana window has closed for this month. " +
                    "It will reopen after the new moon, once enough days have passed (see your nusach).",
                makeupNote = null
            )
        }

        // Day 15: last chance — say it tonight if you haven't yet
        if (day == 15) {
            return ItemZmanStatus(
                availability = ItemZmanAvailability.ACTIVE,
                hint = "Tonight is the last night to say Kiddush Levana this month. " +
                    "Say it as soon as possible — the window closes after tonight."
            )
        }

        // Earliest: 3 days after the molad (Ashkenaz/Chabad); Sephardim wait 7 days (Shulchan Aruch).
        // Hebrew calendar day is a practical proxy (molad is usually near Rosh Chodesh).
        val minDay = when (prayerDay.nusach) {
            EffectiveNusach.SEFARD -> 7
            else -> 3  // Ashkenaz and Chabad
        }
        if (day < minDay) {
            val waitNote = if (prayerDay.nusach == EffectiveNusach.SEFARD)
                "Sephardi custom: wait 7 days after the new moon before saying Kiddush Levana."
            else
                "Ashkenazi / Chabad custom: wait at least 3 days after the new moon before saying Kiddush Levana."
            return ItemZmanStatus(
                availability = ItemZmanAvailability.UPCOMING,
                hint = "Not yet time for Kiddush Levana. $waitNote",
                availableAtLabel = if (prayerDay.nusach == EffectiveNusach.SEFARD) "7 days after the new moon" else "3 days after the new moon"
            )
        }

        // Active window: day minDay..14
        val activeHint = when {
            day == 14 ->
                "Tonight is the last recommended night for Kiddush Levana this month. " +
                "Tomorrow night (the 15th) is the absolute last opportunity — don't miss it."
            day == 13 ->
                "Only 2 nights remaining for Kiddush Levana this month (tonight and the 14th). Best said on Motzei Shabbat."
            day == 12 ->
                "3 nights remaining for Kiddush Levana this month. Best said on Motzei Shabbat."
            else ->
                "Kiddush Levana window is open. Best said on Motzei Shabbat."
        }

        return ItemZmanStatus(
            availability = ItemZmanAvailability.ACTIVE,
            hint = activeHint
        )
    }

    private fun dayOrdinal(day: Int): String = when (day) {
        1 -> "1st"; 2 -> "2nd"; 3 -> "3rd"; 4 -> "4th"; 5 -> "5th"
        6 -> "6th"; 7 -> "7th"; 8 -> "8th"; 9 -> "9th"; 10 -> "10th"
        11 -> "11th"; 12 -> "12th"; 13 -> "13th"; 14 -> "14th"
        else -> "${day}th"
    }
}
