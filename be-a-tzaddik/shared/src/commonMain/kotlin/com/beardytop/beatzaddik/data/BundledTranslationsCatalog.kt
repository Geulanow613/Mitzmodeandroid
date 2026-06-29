package com.beardytop.beatzaddik.data

import com.beardytop.beatzaddik.ui.translation.BundledTranslationLanguages
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
private data class BundledTranslationsFile(
    val version: Int = 1,
    val language: String = "",
    val entries: Map<String, String> = emptyMap(),
)

object BundledTranslationsCatalog {
    private val json = Json { ignoreUnknownKeys = true }
    private val byLanguage = mutableMapOf<String, Map<String, String>>()

    fun initialize(translationsByLanguage: Map<String, Map<String, String>>) {
        byLanguage.clear()
        byLanguage.putAll(translationsByLanguage)
    }

    fun lookup(text: String, languageCode: String): String? {
        if (!BundledTranslationLanguages.isBundled(languageCode)) return null
        return byLanguage[languageCode]?.get(text)
    }

    fun entryCount(languageCode: String): Int = byLanguage[languageCode]?.size ?: 0

    suspend fun loadFromAssets() {
        if (byLanguage.isNotEmpty()) return
        val loaded = mutableMapOf<String, Map<String, String>>()
        for (code in BundledTranslationLanguages.codes) {
            val raw = loadBundledTranslationsJson(code)
            val file = json.decodeFromString<BundledTranslationsFile>(raw)
            loaded[code] = file.entries
        }
        initialize(loaded)
    }

    fun parseForTest(rawByLanguage: Map<String, String>) {
        val loaded = rawByLanguage.mapValues { (_, raw) ->
            json.decodeFromString<BundledTranslationsFile>(raw).entries
        }
        initialize(loaded)
    }
}
