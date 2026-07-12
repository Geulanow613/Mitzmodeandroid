package com.beardytop.beatzaddik.data

import com.beardytop.beatzaddik.domain.TextEncodingFixes
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
    private val normalizedByLanguage = mutableMapOf<String, Map<String, String>>()

    fun initialize(translationsByLanguage: Map<String, Map<String, String>>) {
        byLanguage.clear()
        normalizedByLanguage.clear()
        for ((languageCode, entries) in translationsByLanguage) {
            val cleaned = LinkedHashMap<String, String>(entries.size)
            for ((key, value) in entries) {
                val k = TextEncodingFixes.sanitizeDisplayTitle(key)
                val v = TextEncodingFixes.repairMojibake(value).let { repaired ->
                    if (k.startsWith("Talmud Torah -")) {
                        TextEncodingFixes.sanitizeDisplayTitle(repaired)
                    } else {
                        repaired
                    }
                }
                cleaned.putIfAbsent(k, v)
            }
            byLanguage[languageCode] = cleaned
            val normalized = LinkedHashMap<String, String>(cleaned.size)
            for ((key, value) in cleaned) {
                normalized.putIfAbsent(normalizeLookupKey(key), value)
            }
            normalizedByLanguage[languageCode] = normalized
        }
    }

    fun lookup(text: String, languageCode: String): String? {
        if (!BundledTranslationLanguages.isBundled(languageCode)) return null
        val sanitized = TextEncodingFixes.sanitizeDisplayTitle(text)
        byLanguage[languageCode]?.get(sanitized)?.let { return it }
        byLanguage[languageCode]?.get(text)?.let { return TextEncodingFixes.repairMojibake(it) }
        return normalizedByLanguage[languageCode]?.get(normalizeLookupKey(sanitized))
            ?: normalizedByLanguage[languageCode]?.get(normalizeLookupKey(text))
    }

    /** Collapse prayer/UI key variants (indent, trailing period, case) for offline liturgy lookup. */
    internal fun normalizeLookupKey(text: String): String =
        TextEncodingFixes.sanitizeDisplayTitle(text)
            .lines()
            .map { line -> line.trim().trimEnd('.') }
            .filter { it.isNotEmpty() }
            .joinToString("\n")
            .lowercase()

    fun entryCount(languageCode: String): Int = byLanguage[languageCode]?.size ?: 0

    suspend fun loadFromAssets() {
        if (byLanguage.isNotEmpty()) return
        val loaded = mutableMapOf<String, Map<String, String>>()
        for (code in BundledTranslationLanguages.codes) {
            val raw = TextEncodingFixes.repairJsonPayload(loadBundledTranslationsJson(code))
            val file = json.decodeFromString<BundledTranslationsFile>(raw)
            loaded[code] = file.entries
        }
        initialize(loaded)
    }

    fun parseForTest(rawByLanguage: Map<String, String>) {
        val loaded = rawByLanguage.mapValues { (_, raw) ->
            json.decodeFromString<BundledTranslationsFile>(
                TextEncodingFixes.repairJsonPayload(raw),
            ).entries
        }
        initialize(loaded)
    }
}
