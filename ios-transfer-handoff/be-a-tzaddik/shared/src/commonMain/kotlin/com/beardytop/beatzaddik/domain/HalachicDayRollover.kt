package com.beardytop.beatzaddik.domain

import kotlinx.datetime.LocalDate

/**
 * The Hebrew calendar day begins at nightfall (tzeit hakochavim), not at civil midnight.
 * Between tonight's tzeit and civil midnight the halachic date is already "tomorrow" —
 * e.g. after tzeit on 17 Tammuz the date is 18 Tammuz and the fast has ended.
 *
 * Both calendar backends (Android KosherJava, iOS NSCalendar) use this rule to advance
 * the Hebrew date, holiday flags, and active seasons at tzeit. When no location (and
 * therefore no tzeit) is available, the civil day boundary is used as a fallback.
 *
 * ## Erev flags after rollover
 *
 * Backends build the next Hebrew day's noon [DayInfo] then stamp today's civil date.
 * That next day may be Friday (`isErevShabbat`) or an `erev_chag` day — meaning the
 * *following* sunset begins Shabbat/Yom Tov. The nightfall that just occurred only
 * *entered* that erev day; it did not begin melacha. [sanitizeAfterTzeitRollover]
 * clears those "next-sunset erev" markers so Maariv, Kiddush Levana, Selichot, phone
 * hide, and prep rows do not treat Motzei as erev night.
 */
object HalachicDayRollover {

    /**
     * Seasons that mean "candle lighting / melacha at the *next* sunset", not at the
     * tzeit that just rolled the Hebrew day forward.
     */
    private val EREV_NEXT_SUNSET_SEASONS = setOf("erev_chag")

    /** True when [nowMillis] is past tonight's tzeit — the next Hebrew day has begun. */
    fun hasRolledOver(nowMillis: Long, tzeitTonightMillis: Long?): Boolean =
        tzeitTonightMillis != null && nowMillis >= tzeitTonightMillis

    /**
     * Apply after copying tomorrow-noon [DayInfo] onto today's civil date at tzeit.
     * Clears next-sunset erev markers; keeps real holy-day state (e.g. Friday→Shabbat).
     */
    fun sanitizeAfterTzeitRollover(
        rolled: DayInfo,
        civilDate: LocalDate,
        civilLabel: String,
        statusChips: List<String>,
        activeTimeOfDay: TimeOfDay,
        activePeriodLabel: String,
        activePeriodHint: String?,
        inactivePeriodHint: String?,
    ): DayInfo {
        val hadErevChag = "erev_chag" in rolled.activeSeasons
        val seasons = rolled.activeSeasons - EREV_NEXT_SUNSET_SEASONS
        val chips = if (hadErevChag) {
            statusChips.filterNot { chip ->
                chip.startsWith("Erev ", ignoreCase = true) &&
                    !chip.equals("Erev Shabbat", ignoreCase = true)
            }
        } else {
            statusChips
        }
        return rolled.copy(
            date = civilDate,
            civilLabel = civilLabel,
            statusChips = chips,
            activeTimeOfDay = activeTimeOfDay,
            activePeriodLabel = activePeriodLabel,
            activePeriodHint = activePeriodHint,
            inactivePeriodHint = inactivePeriodHint,
            startedTonightAtTzeit = true,
            // Entering Friday at Thursday tzeit is not "erev Shabbat night" — Shabbat
            // begins tomorrow evening. Friday tzeit→Shabbat already has isErevShabbat false.
            isErevShabbat = false,
            activeSeasons = seasons,
            upcomingChagName = if (hadErevChag) null else rolled.upcomingChagName,
            upcomingChagYomTovIndex = if (hadErevChag) null else rolled.upcomingChagYomTovIndex,
        )
    }
}
