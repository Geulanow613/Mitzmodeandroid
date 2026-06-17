package com.beardytop.beatzaddik.domain

/** Gender-specific required/title rules for shared checklist items. */
object ChecklistGenderRules {

    private val womenOptionalHallelIds = setOf(
        "rosh_chodesh_half_hallel",
        "rosh_chodesh_full_hallel_chanukah",
    )

    fun isEffectivelyRequired(item: ChecklistItemDef, profile: UserProfile): Boolean {
        if (!item.required) return false
        if (profile.gender == Gender.FEMALE && item.id in womenOptionalHallelIds) return false
        return true
    }

    fun optionalTitleSuffix(item: ChecklistItemDef, profile: UserProfile): String =
        if (profile.gender == Gender.FEMALE && item.id in womenOptionalHallelIds) " (optional)" else ""
}
