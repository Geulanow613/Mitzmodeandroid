package com.beardytop.mitzmode.data

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MitzvotRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var mitzvot: List<Mitzvah>? = null
    private val mutex = Mutex()

    // Small prefs: only stores ETag string + legacy combined JSON (for migration)
    private val prefs = context.getSharedPreferences("mitzvot_cache", Context.MODE_PRIVATE)

    // Dedicated file for cloud-only mitzvot (~800 KB for 1000 entries)
    private val cloudCacheFile = File(context.filesDir, "mitzvotcloud_cache.json")

    private val PREF_CLOUD_ETAG = "cloud_etag"
    private val PREF_CLOUD_VERSION = "cloud_version"

    // Bump this whenever a corrected cloud JSON is pushed to GitHub.
    // Any cached file with a lower version will be wiped and re-downloaded.
    private val REQUIRED_MIN_CLOUD_VERSION = 2

    data class MitzvotList(val mitzvot: List<Mitzvah>, val version: Int = 1)

    // ---- Public API ----

    suspend fun getRandomMitzvah(): Mitzvah = withContext(Dispatchers.IO) {
        mutex.withLock {
            if (mitzvot == null) {
                mitzvot = buildCacheFirstList()
            }
            mitzvot!!.random()
        }
    }

    suspend fun preloadMitzvot() = withContext(Dispatchers.IO) {
        mutex.withLock {
            if (mitzvot == null) {
                Log.d("MitzvotRepository", "Preloading mitzvot")
                mitzvot = buildCacheFirstList()
            }
        }
    }

    suspend fun refreshMitzvot() = withContext(Dispatchers.IO) {
        mutex.withLock {
            Log.d("MitzvotRepository", "Force-refreshing mitzvot (ignoring ETag)")
            try {
                val loader = MitzvotLoader(context)
                val local = loader.loadLocalMitzvot()
                if (loader.isNetworkAvailable()) {
                    // Pass null to skip ETag — always re-download
                    val result = loader.loadCloudMitzvotConditional(null)
                    if (result.wasModified && result.mitzvot.isNotEmpty()) {
                        saveCloudCache(result.mitzvot, result.etag, result.version)
                    }
                }
                mitzvot = (local + readCloudCache()).distinctBy { it.id }
                Log.d("MitzvotRepository", "Refreshed: ${mitzvot!!.size} mitzvot")
            } catch (e: Exception) {
                Log.e("MitzvotRepository", "Error during force refresh: ${e.message}", e)
            }
        }
    }

    fun areMitzvotLoaded(): Boolean = mitzvot != null && mitzvot!!.isNotEmpty()

    fun getRandomMitzvahIfAvailable(): Mitzvah? = mitzvot?.randomOrNull()

    // ---- Core loading logic ----

    /**
     * Cache-first strategy:
     *  1. Load local bundled assets immediately (always fast, always works offline)
     *  2. Combine with the cached cloud file (also immediate, also offline)
     *  3. In background: check GitHub with ETag — update the cache and in-memory list
     *     only if the file has actually changed since last time
     */
    private fun buildCacheFirstList(): List<Mitzvah> {
        val loader = MitzvotLoader(context)

        val local = loader.loadLocalMitzvot()
        val cachedCloud = readCloudCache()
        val combined = (local + cachedCloud).distinctBy { it.id }

        Log.d("MitzvotRepository",
            "Cache-first: ${combined.size} mitzvot ready " +
            "(local=${local.size}, cloud=${cachedCloud.size})")

        // Set the in-memory list immediately so the app can start
        mitzvot = combined

        // Background ETag check — runs after mutex is released by caller
        if (loader.isNetworkAvailable()) {
            CoroutineScope(Dispatchers.IO).launch {
                backgroundETagCheck(loader)
            }
        }

        return combined
    }

    private suspend fun backgroundETagCheck(loader: MitzvotLoader) {
        try {
            val storedEtag = prefs.getString(PREF_CLOUD_ETAG, null)
            val result = loader.loadCloudMitzvotConditional(storedEtag)

            if (!result.wasModified) {
                Log.d("MitzvotRepository", "Background check: cloud unchanged (ETag match)")
                return
            }

            if (result.mitzvot.isEmpty()) {
                Log.w("MitzvotRepository", "Background check: wasModified=true but mitzvot empty?")
                return
            }

            Log.d("MitzvotRepository", "Background check: cloud updated to ${result.mitzvot.size} entries (v${result.version})")
            saveCloudCache(result.mitzvot, result.etag, result.version)

            // Merge new cloud into in-memory list under mutex
            mutex.withLock {
                val local = loader.loadLocalMitzvot()
                mitzvot = (local + result.mitzvot).distinctBy { it.id }
                Log.d("MitzvotRepository", "In-memory list silently refreshed: ${mitzvot!!.size} mitzvot")
            }
        } catch (e: Exception) {
            Log.w("MitzvotRepository", "Background ETag check failed: ${e.message}")
        }
    }

    // ---- Cache read/write ----

    /**
     * Reads the cloud-only cache.
     * Priority: new file-based cache → legacy SharedPreferences migration → empty
     */
    private fun readCloudCache(): List<Mitzvah> {
        // Primary: dedicated file (large, binary-safe, fast I/O)
        if (cloudCacheFile.exists()) {
            return try {
                val cached = Gson().fromJson(cloudCacheFile.readText(), MitzvotList::class.java)
                if (cached.version < REQUIRED_MIN_CLOUD_VERSION) {
                    Log.d("MitzvotRepository",
                        "Cloud cache version ${cached.version} < required $REQUIRED_MIN_CLOUD_VERSION — invalidating")
                    cloudCacheFile.delete()
                    prefs.edit().remove(PREF_CLOUD_ETAG).remove(PREF_CLOUD_VERSION).apply()
                    return emptyList()
                }
                Log.d("MitzvotRepository", "Cloud cache file: ${cached.mitzvot.size} entries (v${cached.version})")
                cached.mitzvot
            } catch (e: Exception) {
                Log.e("MitzvotRepository", "Failed to read cloud cache file: ${e.message}", e)
                cloudCacheFile.delete() // corrupt — remove so we re-download
                emptyList()
            }
        }

        // Migration: extract cloud IDs from old SharedPreferences combined cache
        val oldJson = prefs.getString("cached_mitzvot", null) ?: return emptyList()
        return try {
            val all = Gson().fromJson(oldJson, MitzvotList::class.java).mitzvot
            val cloudOnly = all.filter { it.id.startsWith("cloud") }
            if (cloudOnly.isNotEmpty()) {
                Log.d("MitzvotRepository", "Migrating ${cloudOnly.size} cloud entries from old prefs cache")
                saveCloudCache(cloudOnly, null)
                prefs.edit().remove("cached_mitzvot").apply() // free the prefs memory
            }
            cloudOnly
        } catch (e: Exception) {
            Log.e("MitzvotRepository", "Migration from old cache failed: ${e.message}", e)
            emptyList()
        }
    }

    private fun saveCloudCache(cloudMitzvot: List<Mitzvah>, etag: String?, version: Int = REQUIRED_MIN_CLOUD_VERSION) {
        try {
            cloudCacheFile.writeText(Gson().toJson(MitzvotList(cloudMitzvot, version)))
            Log.d("MitzvotRepository", "Saved ${cloudMitzvot.size} cloud entries to cache file (v$version)")
        } catch (e: Exception) {
            Log.e("MitzvotRepository", "Failed to write cloud cache file: ${e.message}", e)
        }
        prefs.edit().apply {
            if (etag != null) putString(PREF_CLOUD_ETAG, etag)
            putInt(PREF_CLOUD_VERSION, version)
            apply()
        }
        Log.d("MitzvotRepository", "Stored version: $version, ETag: $etag")
    }
}
