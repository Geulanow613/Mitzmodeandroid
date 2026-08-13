package com.beardytop.beatzaddik.data

import com.beardytop.beatzaddik.domain.GeneratorMitzvah
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import platform.Foundation.NSUserDefaults

actual class MitzvotCountStore actual constructor(platformContext: Any?) {
    private val defaults = NSUserDefaults.standardUserDefaults
    private val json = Json { ignoreUnknownKeys = true }

    actual suspend fun loadCompleted(): List<GeneratorMitzvah> = withContext(Dispatchers.Default) {
        val arr = defaults.arrayForKey(KEY_COMPLETED) as? List<*> ?: return@withContext emptyList()
        arr.mapNotNull { entry ->
            val raw = entry as? String ?: return@mapNotNull null
            runCatching { json.decodeFromString<GeneratorMitzvah>(raw) }.getOrNull()
        }
    }

    actual suspend fun saveCompleted(mitzvot: List<GeneratorMitzvah>) = withContext(Dispatchers.Default) {
        val encoded = mitzvot.map { json.encodeToString(it) }
        defaults.setObject(encoded, KEY_COMPLETED)
        defaults.synchronize()
    }

    companion object {
        private const val KEY_COMPLETED = "completed_mitzvot"
    }
}
