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

    fun initialize(labels: Map<String, Map<String, String>>, searchAliases: Map<String, List<String>>) {
        labelsByLanguage = labels
        searchAliasesByCityId = searchAliases
    }

    fun label(cityId: String, languageCode: String): String? {
        if (!BundledTranslationLanguages.isBundled(languageCode)) return null
        return labelsByLanguage[languageCode]?.get(cityId)
    }

    fun searchAliases(cityId: String): List<String> = searchAliasesByCityId[cityId].orEmpty()

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
