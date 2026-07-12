package com.beardytop.beatzaddik.data

import android.content.Context
import com.beardytop.beatzaddik.domain.GeneratorMitzvah
import com.beardytop.beatzaddik.domain.GeneratorMitzvahLink
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.json.JSONArray
import org.json.JSONObject

actual class MitzvotCountStore actual constructor(platformContext: Any?) {
    private val context = platformContext as? Context
        ?: error("MitzvotCountStore requires Android Context")
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val json = Json { ignoreUnknownKeys = true }

    actual suspend fun loadCompleted(): List<GeneratorMitzvah> = withContext(Dispatchers.IO) {
        val saved = prefs.getStringSet(KEY_COMPLETED, emptySet()) ?: emptySet()
        saved.mapNotNull { raw ->
            runCatching {
                // Prefer kotlinx; fall back to org.json for legacy Gson-shaped entries.
                runCatching { json.decodeFromString<GeneratorMitzvah>(raw) }.getOrElse {
                    parseLegacyGson(raw)
                }
            }.getOrNull()
        }
    }

    actual suspend fun saveCompleted(mitzvot: List<GeneratorMitzvah>) = withContext(Dispatchers.IO) {
        val set = mitzvot.map { json.encodeToString(GeneratorMitzvah.serializer(), it) }.toSet()
        prefs.edit().putStringSet(KEY_COMPLETED, set).apply()
    }

    private fun parseLegacyGson(raw: String): GeneratorMitzvah? {
        val obj = JSONObject(raw)
        val id = obj.optString("id", "").ifBlank { return null }
        val text = obj.optString("text", "")
        val linksArr = obj.optJSONArray("links") ?: JSONArray()
        val links = buildList {
            for (i in 0 until linksArr.length()) {
                val link = linksArr.optJSONObject(i) ?: continue
                add(
                    GeneratorMitzvahLink(
                        displayText = link.optString("displayText", ""),
                        url = link.optString("url", ""),
                    )
                )
            }
        }
        return GeneratorMitzvah(id = id, text = text, links = links)
    }

    companion object {
        private const val PREFS_NAME = "mitzvot_prefs"
        private const val KEY_COMPLETED = "completed_mitzvot"
    }
}
