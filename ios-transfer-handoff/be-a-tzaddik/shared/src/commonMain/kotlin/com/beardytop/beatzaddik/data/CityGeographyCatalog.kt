package com.beardytop.beatzaddik.data

import com.beardytop.beatzaddik.ui.translation.BundledTranslationLanguages
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
private data class CityGeographyFile(
    val version: Int = 1,
    val labels: Map<String, Map<String, String>> = emptyMap(),
    val searchAliases: Map<String, List<String>> = emptyMap(),
)

object CityGeographyCatalog {
    private val json = Json { ignoreUnknownKeys = true }
    private var labelsByLanguage: Map<String, Map<String, String>> = emptyMap()
    private var searchAliasesByCityId: Map<String, List<String>> = emptyMap()
    private var normalizedAliasToCityId: Map<String, String> = emptyMap()

    fun initialize(labels: Map<String, Map<String, String>>, searchAliases: Map<String, List<String>>) {
        labelsByLanguage = labels
        searchAliasesByCityId = searchAliases
        normalizedAliasToCityId = emptyMap()
    }

    fun label(cityId: String, languageCode: String): String? {
        if (!BundledTranslationLanguages.isBundled(languageCode)) return null
        return labelsByLanguage[languageCode]?.get(cityId)
    }

    fun searchAliases(cityId: String): List<String> = searchAliasesByCityId[cityId].orEmpty()

    /**
     * Maps a GPS geocoder string (e.g. "Palo Alto, CA") to a manual-city id using
     * [searchAliases] — the same index that powers city search in Settings.
     */
    fun cityIdForLocationLabel(locationLabel: String): String? {
        if (locationLabel.isBlank() || searchAliasesByCityId.isEmpty()) return null
        ensureAliasIndex()
        val normalized = locationLabel.trim().lowercase()
        val prefix = normalized.substringBefore(',').trim()
        return normalizedAliasToCityId[normalized]
            ?: normalizedAliasToCityId[prefix]
    }

    private fun ensureAliasIndex() {
        if (normalizedAliasToCityId.isNotEmpty()) return
        normalizedAliasToCityId = buildMap {
            for ((cityId, aliases) in searchAliasesByCityId) {
                for (alias in aliases) {
                    val key = alias.trim().lowercase()
                    if (key.isNotBlank()) putIfAbsent(key, cityId)
                }
            }
        }
    }

    suspend fun loadFromAssets() {
        if (labelsByLanguage.isNotEmpty()) return
        val raw = loadCityGeographyJson()
        val file = json.decodeFromString<CityGeographyFile>(raw)
        initialize(file.labels, file.searchAliases)
    }

    fun parseForTest(raw: String) {
        val file = json.decodeFromString<CityGeographyFile>(raw)
        initialize(file.labels, file.searchAliases)
    }
}
