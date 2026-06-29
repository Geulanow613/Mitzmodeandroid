package com.beardytop.beatzaddik.domain

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.serialization.Serializable

/** Device timezone for new profiles before GPS or a manual city is chosen. */
fun deviceTimezoneId(): String =
    runCatching { TimeZone.currentSystemDefault().id }.getOrDefault("America/New_York")

@Serializable
enum class Gender { MALE, FEMALE, PREFER_NOT_TO_SAY }

/** Male-only checklist items; "prefer not to say" uses the same rules without labeling the user male. */
fun Gender.usesMaleChecklistItems(): Boolean =
    this == Gender.MALE || this == Gender.PREFER_NOT_TO_SAY

fun Gender.displayLabel(): String = when (this) {
    Gender.MALE -> "Male"
    Gender.FEMALE -> "Female"
    Gender.PREFER_NOT_TO_SAY -> "Prefer not to say"
}

@Serializable
enum class NusachSelection {
    ASHKENAZ,
    SEFARD,
    /** Middle Eastern & North African communities (Nusach Edot HaMizrach). */
    EDOT_HAMIZRACH,
    CHABAD,
    NOT_SURE;

    companion object {
        /** Choices shown in onboarding and settings (excludes removed legacy values). */
        val selectable: List<NusachSelection> = listOf(
            ASHKENAZ, SEFARD, EDOT_HAMIZRACH, CHABAD, NOT_SURE,
        )
    }
}

@Serializable
enum class EffectiveNusach { ASHKENAZ, SEFARD, EDOT_HAMIZRACH, CHABAD }

fun NusachSelection.toEffective(): EffectiveNusach = when (this) {
    NusachSelection.ASHKENAZ -> EffectiveNusach.ASHKENAZ
    NusachSelection.SEFARD -> EffectiveNusach.SEFARD
    NusachSelection.EDOT_HAMIZRACH -> EffectiveNusach.EDOT_HAMIZRACH
    NusachSelection.CHABAD, NusachSelection.NOT_SURE -> EffectiveNusach.CHABAD
}

fun NusachSelection.displayLabel(): String = when (this) {
    NusachSelection.ASHKENAZ -> "Ashkenaz"
    NusachSelection.SEFARD -> "Sephardi"
    NusachSelection.EDOT_HAMIZRACH -> "Edot HaMizrach"
    NusachSelection.CHABAD -> "Chabad (Nusach Ari)"
    NusachSelection.NOT_SURE -> "I'm not sure"
}

fun EffectiveNusach.displayLabel(): String = when (this) {
    EffectiveNusach.ASHKENAZ -> "Ashkenaz"
    EffectiveNusach.SEFARD -> "Sephardi"
    EffectiveNusach.EDOT_HAMIZRACH -> "Edot HaMizrach"
    EffectiveNusach.CHABAD -> "Nusach Ari / Chabad"
}

/** Lowercase tag used in checklist JSON [ChecklistItemDef.nusach] lists. */
fun EffectiveNusach.jsonTag(): String = when (this) {
    EffectiveNusach.ASHKENAZ -> "ashkenaz"
    EffectiveNusach.SEFARD -> "sefard"
    EffectiveNusach.EDOT_HAMIZRACH -> "edot_hamizrach"
    EffectiveNusach.CHABAD -> "chabad"
}

/** Sephardi and Edot HaMizrach both follow Shulchan Aruch on many lifecycle rules. */
fun EffectiveNusach.followsShulchanAruchMizrachi(): Boolean =
    this == EffectiveNusach.SEFARD || this == EffectiveNusach.EDOT_HAMIZRACH

@Serializable
enum class LocationSource { GPS, MANUAL, NONE }

/** How the app determined that the user is (or isn't) in Israel. */
enum class IsraelDetectionSource { GPS, CITY, MANUAL_FLAG }

@Serializable
enum class TimeOfDay { DAY, AFTERNOON, NIGHT, ANY }

@Serializable
enum class MealCategory { MEAT, DAIRY }

@Serializable
data class UserProfile(
    val onboardingComplete: Boolean = false,
    val gender: Gender = Gender.MALE,
    val married: Boolean = false,
    val hasChildren: Boolean = false,
    val nusachSelection: NusachSelection = NusachSelection.NOT_SURE,
    val locationSource: LocationSource = LocationSource.NONE,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val timezoneId: String = deviceTimezoneId(),
    val locationLabel: String? = null,
    /** Meters above sea level — used for sunrise/sunset (KosherJava elevation adjustment). */
    val elevationMeters: Double? = null,
    val useGps: Boolean = false,
    val manualCityId: String? = null,
    val textScale: Float = 1f,
    val meatAfterDairyHours: Int? = null,
    val dairyAfterMeatHours: Int? = null,
    val tzaddikShownDate: String? = null,
    /** Explicit "I live in Israel" toggle, used as a fallback when location is unavailable. */
    val liveInIsrael: Boolean = false
) {
    fun effectiveNusach(): EffectiveNusach = nusachSelection.toEffective()

    /**
     * True when the user is in Israel — affects parsha cycle (1-day vs 2-day Yom Tov).
     *
     * Priority:
     * 1. GPS / manual-city coordinates: inside Israel's bounding box (lat 29.5–33.4, lon 34.2–35.9).
     * 2. Manual city selected: known Israeli city in [ManualCities].
     * 3. Explicit [liveInIsrael] flag set by the user.
     */
    val isInIsrael: Boolean
        get() {
            if (latitude != null && longitude != null) {
                return latitude in 29.5..33.4 && longitude in 34.2..35.9
            }
            if (manualCityId != null) {
                val city = ManualCities.byId(manualCityId)
                if (city != null) return city.timezoneId == "Asia/Jerusalem"
            }
            return liveInIsrael
        }

    /** Human-readable summary of how Israel status was determined (for UI hint). */
    val isInIsraelSource: IsraelDetectionSource
        get() = when {
            latitude != null && longitude != null -> IsraelDetectionSource.GPS
            manualCityId != null && ManualCities.byId(manualCityId) != null ->
                IsraelDetectionSource.CITY
            else -> IsraelDetectionSource.MANUAL_FLAG
        }

    /**
     * Minutes to wait after dairy before eating meat (custom or nusach default).
     *
     * Shulchan Aruch (Y.D. 89:2): after dairy, only rinsing mouth and hands is required before meat —
     * no timed wait. Most Ashkenaz poskim similarly require no wait. A 30-minute default is used
     * as a conservative universal baseline. Users can adjust in Settings.
     */
    fun dairyToMeatWaitMinutes(): Int =
        KashrutWaitTimes.resolveDairyToMeatMinutes(meatAfterDairyHours) ?: 30

    fun meatToDairyHours(): Int = dairyAfterMeatHours ?: 6

    /** True when GPS or a manual city has provided coordinates for prayer-time calculations. */
    fun hasZmanimLocation(): Boolean = latitude != null && longitude != null
}

@Serializable
data class ChecklistLink(
    val displayText: String,
    val url: String,
    val nusach: String? = null
)

@Serializable
data class ChecklistItemDef(
    val id: String,
    val title: String,
    val section: String,
    val timeOfDay: TimeOfDay = TimeOfDay.ANY,
    val required: Boolean = true,
    /** True when the mitzvah applies only in certain situations (not counted in daily progress). */
    val situational: Boolean = false,
    val gender: String? = null,
    val married: Boolean? = null,
    /** When true, item appears only if [UserProfile.hasChildren] is set. */
    val hasChildren: Boolean? = null,
    val explanation: String = "",
    /** When set, used for female profile instead of [explanation]. */
    val explanationFemale: String = "",
    val explanationAshkenaz: String = "",
    val explanationSefard: String = "",
    val explanationEdotHamizrach: String = "",
    val explanationChabad: String = "",
    val links: List<ChecklistLink> = emptyList(),
    val hideOnShabbat: Boolean = false,
    val shabbatOnly: Boolean = false,
    /** Candles, etc. — show on Erev Shabbat / prep, not on Shabbat day itself */
    val shabbatEveOnly: Boolean = false,
    /** Melave Malka, etc. — show from tzeit Saturday until dawn Sunday */
    val motzeiShabbatOnly: Boolean = false,
    /** Lower = earlier within the same [section]. */
    val sortOrder: Int = 0,
    /** Checked state survives midnight rollover (mezuzot, tevilah, etc.). */
    val persistChecked: Boolean = false,
    /** Lowercase: ashkenaz, sefard, chabad — omit = all nusachim */
    val nusach: List<String>? = null,
    val nusachOnly: Boolean = false,
    val nusachTag: String? = null,
    /** sefirah, chanukah, purim, erev_pesach, etc. — omit = always eligible */
    val seasons: List<String>? = null,
    /**
     * True for mitzvot observed once per Hebrew month (e.g. Kiddush Levana).
     * Checked state is stored with a Hebrew month key so it resets each new month.
     * The item is not in the daily reset bucket; the engine tracks currency via monthlyCheckedMonths.
     */
    val monthlyMitzvah: Boolean = false,
    /**
     * True for mitzvot observed once per civil week (e.g. Shnayim Mikra).
     * Checked state is stored with the upcoming Saturday's date as the key so it resets
     * each week on Motzei Shabbat (when the next parsha week begins).
     */
    val weeklyMitzvah: Boolean = false
)

@Serializable
data class CustomChecklistItem(
    val id: String,
    val title: String,
    val timeOfDay: TimeOfDay = TimeOfDay.ANY
)

data class ResolvedChecklistItem(
    val def: ChecklistItemDef,
    val checked: Boolean,
    val displayTitle: String = def.title,
    val displayExplanation: String = def.explanation,
    val learnMoreUrl: String? = null,
    val learnMoreLabel: String? = null,
    /** All verified resource links from the item (tap to open in browser). */
    val resourceLinks: List<ChecklistLink> = emptyList(),
    val sectionLabel: String = def.section,
    val zmanAvailability: ItemZmanAvailability = ItemZmanAvailability.ACTIVE,
    val zmanHint: String? = null,
    val zmanMakeupNote: String? = null,
    val zmanWindowStartMillis: Long? = null,
    val zmanWindowEndMillis: Long? = null,
    val zmanAvailableAtLabel: String? = null
)

data class DayChecklists(
    val activePeriod: TimeOfDay,
    val activePeriodLabel: String,
    val activePeriodHint: String? = null,
    val inactivePeriodHint: String?,
    val items: List<ResolvedChecklistItem>,
    val inactiveItemCount: Int,
    val inactiveItems: List<ResolvedChecklistItem> = emptyList(),
    val date: LocalDate,
    val header: CalendarHeader,
    val nusachLabel: String,
    /** Shabbat or (chutz) 2nd-day Yom Tov: show holy-day notice instead of checklist. */
    val holyDayPhoneNotice: HolyDayPhoneNotice? = null,
    /** Prep sections pinned to the top of the Today checklist (erev chag, festival week, etc.). */
    val prioritizePrepSections: Set<String> = emptySet(),
    /** After chatzos until chatzos halayla — Morning Prayer section moves to the bottom. */
    val sinkMorningPrayerSection: Boolean = false,
)

data class CalendarHeader(
    val civilDateLabel: String,
    val hebrewDateLabel: String,
    val parshaLabel: String?,
    val statusChips: List<String>,
    val timeLabel: String? = null,
    /** e.g. "3rd day of Pesach", "Fast of 17 Tammuz" — shown below the clock. */
    val todayOccasionLabel: String? = null,
    val todayOccasionSubtitle: String? = null,
    val todayOccasionGuideAnchor: String? = null,
    /** e.g. "Today is 14 days, which is 2 weeks of the Omer." */
    val omerTodayLabel: String? = null,
    val omerExplainerText: String? = null,
)

data class KashrutWait(
    val category: MealCategory,
    val startedAtEpochMillis: Long,
    val endsAtEpochMillis: Long
)

data class UpcomingHoliday(
    val name: String,
    val daysAway: Int,
    val hint: String = "",
    /** When [daysAway] is 0: true → label "Tonight" (begins at sunset); false → "today". */
    val beginsTonightWhenImminent: Boolean = true,
    /** When set, Today screen uses this instead of [daysAway] / [beginsTonightWhenImminent]. */
    val whenLabelOverride: String? = null,
    /** Smaller secondary line under [hint] (e.g. location-specific fast start time). */
    val timingHint: String? = null,
)
