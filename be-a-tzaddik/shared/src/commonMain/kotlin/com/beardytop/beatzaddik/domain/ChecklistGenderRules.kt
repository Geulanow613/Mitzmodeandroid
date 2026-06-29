package com.beardytop.beatzaddik.domain

/** Gender-specific required/title rules for shared checklist items. */
object ChecklistGenderRules {

    private val womenOptionalAmidahIds = setOf(
        "shemoneh_esrei_tachanun",
        "mincha_shemoneh_esrei_tachanun",
        "maariv_shemoneh_esrei",
        "musaf_only_on_rosh_chodesh_festivals_and_shabbat",
    )

    private val womenOptionalHallelIds = setOf(
        "rosh_chodesh_half_hallel",
        "rosh_chodesh_full_hallel_chanukah",
    )

    val womenOptionalPrayerIds: Set<String> = womenOptionalAmidahIds + womenOptionalHallelIds

    fun isEffectivelyRequired(item: ChecklistItemDef, profile: UserProfile): Boolean {
        if (!item.required) return false
        if (profile.gender == Gender.FEMALE && item.id in womenOptionalPrayerIds) return false
        return true
    }

    fun optionalTitleSuffix(item: ChecklistItemDef, profile: UserProfile): String =
        if (profile.gender == Gender.FEMALE && item.id in womenOptionalPrayerIds) {
            " (optional for women)"
        } else {
            ""
        }

    /** Amidah-only titles for women — not the full service name (e.g. no Tachanun in the label). */
    fun displayTitleForWomen(item: ChecklistItemDef): String? = when (item.id) {
        "shemoneh_esrei_tachanun" -> "Amidah / Shemoneh Esrei (Shacharit)"
        "mincha_shemoneh_esrei_tachanun" -> "Amidah / Shemoneh Esrei (Mincha)"
        "maariv_shemoneh_esrei" -> "Amidah / Shemoneh Esrei (Maariv)"
        "musaf_only_on_rosh_chodesh_festivals_and_shabbat" -> "Musaf Amidah"
        else -> null
    }

    fun usesMaleExplanationForWomen(item: ChecklistItemDef, profile: UserProfile): Boolean =
        profile.gender == Gender.FEMALE && item.id in womenOptionalPrayerIds
}
