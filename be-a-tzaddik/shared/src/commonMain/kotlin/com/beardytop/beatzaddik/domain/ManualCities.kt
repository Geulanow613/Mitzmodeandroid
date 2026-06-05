package com.beardytop.beatzaddik.domain

import com.beardytop.beatzaddik.data.ManualCitiesCatalog

/**
 * Preset cities for manual location (onboarding + settings).
 * Loaded from [composeResources/files/manual-cities.json] at app startup.
 */
object ManualCities {
    /** Older app builds used short ids; resolve them for saved profiles. */
    private val legacyIdAliases = mapOf(
        "nyc" to "new_york",
        "chi" to "chicago",
        "lon" to "london",
        "la" to "los_angeles",
        "sf" to "san_francisco",
        "jerusalem" to "jlm",
        "tel_aviv" to "tlv",
    )

    val all: List<ManualCity> get() = ManualCitiesCatalog.all

    fun byId(id: String): ManualCity? {
        val resolved = legacyIdAliases[id] ?: id
        return all.firstOrNull { it.id == resolved }
    }
}
