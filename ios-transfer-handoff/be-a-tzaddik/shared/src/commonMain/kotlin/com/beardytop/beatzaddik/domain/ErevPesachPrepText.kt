package com.beardytop.beatzaddik.domain

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.plus

/**
 * Full explainers and checklist timing for Pesach prep (8–14 Nisan).
 */
object ErevPesachPrepText {

    enum class PesachErevDow { WEEKDAY, FRIDAY, SHABBAT }

    /** True during Nissan 8–14 when Pesach-prep checklist items may appear. */
    fun isPesachPrepWindow(cal: DayInfo): Boolean {
        val month = cal.hebrewMonth ?: return false
        val day = cal.hebrewDay ?: return false
        return month == HebrewCalendarEngine.NISSAN && day in 8..14
    }

    fun pesachErevDow(cal: DayInfo): PesachErevDow? {
        val erev = erevPesachCivilDate(cal) ?: return null
        return when (erev.dayOfWeek) {
            DayOfWeek.SATURDAY -> PesachErevDow.SHABBAT
            DayOfWeek.FRIDAY -> PesachErevDow.FRIDAY
            else -> PesachErevDow.WEEKDAY
        }
    }

    /** Hebrew date (Nissan) on which bedikat is performed after tzeit. */
    private fun bedikatNissanDay(dow: PesachErevDow): Int = when (dow) {
        PesachErevDow.SHABBAT -> 12
        else -> 13
    }

    /** Hebrew date (Nissan) on which biur is performed in the morning. */
    private fun biurNissanDay(dow: PesachErevDow): Int = when (dow) {
        PesachErevDow.SHABBAT -> 13
        else -> 14
    }

    /**
     * Bedikat night spans tzeit rollover: show on the search day's evening, and keep showing
     * after nightfall when the Hebrew day has already advanced to the next date.
     */
    fun isBedikatNight(cal: DayInfo, dow: PesachErevDow): Boolean {
        val day = cal.hebrewDay ?: return false
        val target = bedikatNissanDay(dow)
        return when {
            !cal.startedTonightAtTzeit && day == target -> true
            cal.startedTonightAtTzeit && day == target + 1 -> true
            else -> false
        }
    }

    /** Biur is a morning mitzvah — never show on the night the Hebrew day rolls onto biur day. */
    fun isBiurMorning(cal: DayInfo, dow: PesachErevDow): Boolean {
        val day = cal.hebrewDay ?: return false
        return !cal.startedTonightAtTzeit && day == biurNissanDay(dow)
    }

    /** Hebrew date (Nissan) for Taanit Bechorot (moved earlier when Erev Pesach is Shabbat). */
    private fun taanitNissanDay(dow: PesachErevDow): Int = when (dow) {
        PesachErevDow.SHABBAT -> 12
        else -> 14
    }

    /** Last Nissan day to authorize mechirat with your rabbi (sale takes effect Erev Pesach morning). */
    fun mechiratAuthorizeThroughNissanDay(dow: PesachErevDow): Int = when (dow) {
        PesachErevDow.FRIDAY -> 14
        else -> 13
    }

    fun pesachPrepItemsForDay(cal: DayInfo, profile: UserProfile): List<ChecklistItemDef> {
        if (!isPesachPrepWindow(cal)) return emptyList()
        val nissanDay = cal.hebrewDay ?: return emptyList()
        val dow = pesachErevDow(cal) ?: return emptyList()
        return buildList {
            if (nissanDay in 8..mechiratAuthorizeThroughNissanDay(dow)) {
                add(mechiratItem(cal, profile, nissanDay, dow))
            }
            if (isBedikatNight(cal, dow)) {
                add(bedikatItem(cal, profile))
            }
            if (isBiurMorning(cal, dow)) {
                add(biurItem(cal, profile))
            }
            // Taanit is daytime — hide after tzeit rollover onto the fast's Hebrew date.
            if (!cal.startedTonightAtTzeit && nissanDay == taanitNissanDay(dow)) {
                add(taanitItem(cal, profile))
            }
            // The general "Erev Pesach prep — Yom Tov & seder" item (from the erev-chag system)
            // covers seder/Yom Tov setup on the normal erev Pesach day.
            // Keep this dedicated seder-prep row only for the special Shabbat-intersection schedule,
            // where seder prep timing shifts and the erev-chag row is hidden on Shabbat itself.
            if (nissanDay == 13 && dow == PesachErevDow.SHABBAT) {
                add(sederPrepItem(cal, profile))
            }
        }
    }

    private fun mechiratItem(
        cal: DayInfo,
        profile: UserProfile,
        nissanDay: Int,
        dow: PesachErevDow,
    ) = ChecklistItemDef(
        id = "erev_pesach_mechirat_chametz",
        title = mechiratTitle(nissanDay, dow),
        section = "Pesach prep",
        sortOrder = 10,
        timeOfDay = TimeOfDay.DAY,
        required = false,
        situational = false,
        explanation = mechiratExplanation(cal, profile, nissanDay, dow),
        links = mechiratLinks(profile),
    )

    private fun taanitItem(cal: DayInfo, profile: UserProfile) = ChecklistItemDef(
        id = "erev_pesach_taanit_bechorot",
        title = taanitTitle(cal),
        section = "Pesach prep",
        sortOrder = 20,
        timeOfDay = TimeOfDay.DAY,
        required = false,
        situational = false,
        explanation = taanitBechorExplanation(cal, profile),
        links = taanitBechorLinks(profile),
    )

    private fun sederPrepItem(cal: DayInfo, profile: UserProfile) = ChecklistItemDef(
        id = "erev_pesach_prepare_seder",
        title = sederPrepTitle(cal),
        section = "Pesach prep",
        sortOrder = 30,
        timeOfDay = TimeOfDay.DAY,
        required = false,
        situational = false,
        explanation = sederPrepExplanation(cal, profile),
        links = sederPrepLinks(profile),
    )

    private fun bedikatItem(cal: DayInfo, profile: UserProfile) = ChecklistItemDef(
        id = "bedikat_chametz",
        title = "Bedikat chametz — search tonight after nightfall",
        section = "Pesach prep",
        sortOrder = 40,
        timeOfDay = TimeOfDay.NIGHT,
        required = true,
        situational = false,
        explanation = bedikatExplanation(cal, profile),
        links = bedikatLinks(profile),
    )

    private fun biurItem(cal: DayInfo, profile: UserProfile) = ChecklistItemDef(
        id = "erev_pesach_biur_chametz",
        title = biurTitle(cal),
        section = "Pesach prep",
        sortOrder = 50,
        timeOfDay = TimeOfDay.DAY,
        required = true,
        situational = false,
        explanation = biurExplanation(cal, profile),
        links = biurLinks(profile),
    )

    private fun mechiratTitle(nissanDay: Int, dow: PesachErevDow): String {
        val last = mechiratAuthorizeThroughNissanDay(dow)
        return when {
            nissanDay < last ->
                "Mechirat chametz — authorize sale with your rabbi"
            dow == PesachErevDow.FRIDAY ->
                "Mechirat chametz — complete by the 5th-hour morning deadline"
            dow == PesachErevDow.SHABBAT ->
                "Mechirat chametz — complete before Shabbat begins tonight"
            else ->
                "Mechirat chametz — last day to authorize with your rabbi"
        }
    }

    private fun biurTitle(cal: DayInfo): String =
        if (pesachErevDow(cal) == PesachErevDow.SHABBAT) {
            "Biur chametz — burn/remove chametz this morning (before Shabbat)"
        } else {
            "Biur chametz — burn/remove chametz this morning"
        }

    private fun taanitTitle(cal: DayInfo): String =
        if (pesachErevDow(cal) == PesachErevDow.SHABBAT && cal.hebrewDay == 12) {
            "Taanit Bechorot — Fast of the Firstborn (moved to Thursday)"
        } else {
            "Taanit Bechorot — Fast of the Firstborn (or siyum)"
        }

    private fun sederPrepTitle(cal: DayInfo): String = when {
        isErevPesachOnShabbat(cal) ->
            "Prepare for the Seder after Shabbat (Motzei Shabbat)"
        isErevPesachFridayBeforeShabbatPesach(cal) ->
            "Prepare for the Seder (matzah, maror, cups, Haggadah)"
        cal.hebrewDay == 13 && pesachErevDow(cal) == PesachErevDow.SHABBAT ->
            "Prepare for the Seder — finish before Shabbat begins"
        else ->
            "Prepare for the Seder (matzah, maror, cups, Haggadah)"
    }

    /** Gregorian date of 14 Nisan in the current Hebrew year, when today is on or before Erev Pesach. */
    fun erevPesachCivilDate(cal: DayInfo) = run {
        val month = cal.hebrewMonth ?: return@run null
        val day = cal.hebrewDay ?: return@run null
        if (month != HebrewCalendarEngine.NISSAN || day > 14) return@run null
        cal.date.plus(14 - day, DateTimeUnit.DAY)
    }

    /** 14 Nisan falls on Shabbat — laws move earlier; Seder is Motzei Shabbat. */
    fun isErevPesachOnShabbat(cal: DayInfo): Boolean =
        "erev_pesach" in cal.activeSeasons && cal.isShabbat

    /** 14 Nisan is Friday — first Seder is Friday night; 15 Nisan (first day of Pesach) is Shabbat. */
    fun isErevPesachFridayBeforeShabbatPesach(cal: DayInfo): Boolean =
        "erev_pesach" in cal.activeSeasons && cal.isErevShabbat

    /** This year Pesach intersects Shabbat (either case above, or week-before lead-up). */
    fun isPesachShabbatScheduleYear(cal: DayInfo): Boolean {
        val erev = erevPesachCivilDate(cal) ?: return false
        return erev.dayOfWeek == DayOfWeek.FRIDAY || erev.dayOfWeek == DayOfWeek.SATURDAY
    }

    /** Hebrew day of month in Nissan, when still on or before Erev Pesach. */
    private fun nissanDay(cal: DayInfo): Int? {
        val month = cal.hebrewMonth ?: return null
        val day = cal.hebrewDay ?: return null
        if (month != HebrewCalendarEngine.NISSAN || day > 14) return null
        return day
    }

    private fun appendShabbatSchedule(
        body: String,
        cal: DayInfo,
        profile: UserProfile,
        includePesachSchedule: Boolean = true,
    ): String {
        val pesachBlock = if (includePesachSchedule) erevPesachShabbatScheduleBlock(cal) else null
        val chagName = cal.upcomingChagName ?: "Pesach"
        val yomTovBlock = YomTovShabbatPrepText.scheduleBlock(cal, profile, chagName)
        val blocks = when {
            pesachBlock != null && yomTovBlock != null &&
                isErevPesachFridayBeforeShabbatPesach(cal) -> pesachBlock
            pesachBlock != null && YomTovShabbatPrepText.isShabbatErevChag(cal) ->
                listOfNotNull(pesachBlock, yomTovBlock).joinToString("\n\n")
            else -> listOfNotNull(pesachBlock, yomTovBlock).joinToString("\n\n")
        }
        if (blocks.isEmpty()) return body
        return "$body\n\n$blocks"
    }

    fun erevPesachShabbatScheduleBlock(cal: DayInfo): String? =
        ErevPesachExplainerTemplates.pesachScheduleBlock(cal)

    /** Bedikat row on the night before Erev Pesach day; Shabbat-year schedule in week-before prep. */
    private fun includePesachScheduleOnBedikatItem(cal: DayInfo): Boolean = false

    fun mechiratExplanation(
        cal: DayInfo,
        profile: UserProfile,
        nissanDay: Int = cal.hebrewDay ?: 14,
        dow: PesachErevDow = pesachErevDow(cal) ?: PesachErevDow.WEEKDAY,
    ): String = BeginnerHalachaGlossary.withKeyTerms(
        BeginnerHalachaGlossary.pesachPrep(),
        ExplainerTemplateFill.fill(
            ErevPesachExplainerTemplates.mechiratTemplate(),
            ErevPesachExplainerTemplates.mechiratArgs(cal, profile),
        ),
    )
    fun taanitBechorExplanation(cal: DayInfo, profile: UserProfile): String =
        BeginnerHalachaGlossary.withKeyTerms(
            BeginnerHalachaGlossary.pesachPrep(),
            ExplainerTemplateFill.fill(
                ErevPesachExplainerTemplates.taanitBechorTemplate(),
                ErevPesachExplainerTemplates.taanitBechorArgs(cal, profile),
            ),
        )

    fun sederPrepExplanation(cal: DayInfo, profile: UserProfile): String =
        BeginnerHalachaGlossary.withKeyTerms(
            BeginnerHalachaGlossary.pesachPrep(),
            ExplainerTemplateFill.fill(
                ErevPesachExplainerTemplates.sederPrepTemplate(),
                ErevPesachExplainerTemplates.sederPrepArgs(cal, profile),
            ),
        )

    fun bedikatExplanation(cal: DayInfo, profile: UserProfile): String =
        BeginnerHalachaGlossary.withKeyTerms(
            BeginnerHalachaGlossary.pesachPrep(),
            ExplainerTemplateFill.fill(
                ErevPesachExplainerTemplates.bedikatTemplate(),
                ErevPesachExplainerTemplates.bedikatArgs(cal, profile),
            ),
        )

    fun biurExplanation(cal: DayInfo, profile: UserProfile): String =
        BeginnerHalachaGlossary.withKeyTerms(
            BeginnerHalachaGlossary.pesachPrep(),
            ExplainerTemplateFill.fill(
                ErevPesachExplainerTemplates.biurTemplate(),
                ErevPesachExplainerTemplates.biurArgs(cal, profile),
            ),
        )

    fun mechiratLinks(profile: UserProfile) = pesachLinks(profile, "mechirat")
    fun taanitBechorLinks(profile: UserProfile) = pesachLinks(profile, "taanit")
    fun sederPrepLinks(profile: UserProfile) = pesachLinks(profile, "seder")
    fun bedikatLinks(profile: UserProfile) = pesachLinks(profile, "bedikat")
    fun biurLinks(profile: UserProfile) = pesachLinks(profile, "biur")

    private fun pesachLinks(profile: UserProfile, topic: String): List<ChecklistLink> = buildList {
        when (topic) {
            "bedikat" -> {
                if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
                    add(
                        ChecklistLink(
                            "Chabad — Bedikat chametz",
                            "https://www.chabad.org/holidays/passover/pesach_cdo/aid/1721/jewish/Bedikat-Chametz.htm",
                            "chabad"
                        )
                    )
                }
                add(ChecklistLink("Peninei Halacha — Bedikat chametz", "https://ph.yhb.org.il/en/04-03-01/", "default"))
            }
            "mechirat" -> {
                if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
                    add(
                        ChecklistLink(
                            "Chabad — Sale of chametz",
                            "https://www.chabad.org/holidays/passover/pesach_cdo/aid/1720/jewish/Sale-of-Chametz.htm",
                            "chabad"
                        )
                    )
                }
                add(ChecklistLink("Peninei Halacha — Mechirat chametz", "https://ph.yhb.org.il/en/04-03-02/", "default"))
            }
            "biur" -> {
                if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
                    add(
                        ChecklistLink(
                            "Chabad — Destroying chametz",
                            "https://www.chabad.org/holidays/passover/pesach_cdo/aid/1723/jewish/Destroying-Chametz.htm",
                            "chabad"
                        )
                    )
                }
                add(ChecklistLink("Peninei Halacha — Biur chametz", "https://ph.yhb.org.il/en/04-03-03/", "default"))
            }
            "taanit" -> {
                if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
                    add(
                        ChecklistLink(
                            "Chabad — Fast of the Firstborn",
                            "https://www.chabad.org/holidays/passover/pesach_cdo/aid/1722/jewish/Fast-of-the-Firstborn.htm",
                            "chabad"
                        )
                    )
                }
                add(ChecklistLink("Peninei Halacha — Taanit Bechorot", "https://ph.yhb.org.il/en/04-03-04/", "default"))
            }
            else -> {
                if (profile.effectiveNusach() == EffectiveNusach.CHABAD) {
                    add(
                        ChecklistLink(
                            "Chabad — Erev Pesach",
                            "https://www.chabad.org/holidays/passover/pesach_cdo/aid/1719/jewish/Erev-Pesach.htm",
                            "chabad"
                        )
                    )
                }
            }
        }
        add(ChecklistLink("Aish — Passover", "https://aish.com/holidays/pesach/", "default"))
        add(ChecklistLink("Peninei Halacha — Pesach", "https://ph.yhb.org.il/en/04-03-01/", "default"))
        add(ChecklistLink("Ohr Somayach — Pesach", "https://ohr.edu/holidays/pesach/", "default"))
    }
}
