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
    private val shuffleDeck = mutableListOf<Mitzvah>()
    private var shuffleIndex = 0
    private var lastIssuedId: String? = null
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

    data class MitzvotList(val mitzvot: List<Mitzvah> = emptyList(), val version: Int = 1)

    // ---- Public API ----

    suspend fun getRandomMitzvah(): Mitzvah = withContext(Dispatchers.IO) {
        mutex.withLock {
            ensurePoolLoadedLocked()
            nextShuffledMitzvahLocked()
        }
    }

    suspend fun preloadMitzvot() = withContext(Dispatchers.IO) {
        mutex.withLock {
            ensurePoolLoadedLocked()
        }
    }

    suspend fun refreshMitzvot() = withContext(Dispatchers.IO) {
        mutex.withLock {
            Log.d("MitzvotRepository", "Force-refreshing mitzvot (ignoring ETag)")
            try {
                val loader = MitzvotLoader(context)
                val local = sanitizeMitzvot(loader.loadLocalMitzvot())
                if (loader.isNetworkAvailable()) {
                    // Pass null to skip ETag — always re-download
                    val result = loader.loadCloudMitzvotConditional(null)
                    if (result.wasModified && result.mitzvot.isNotEmpty()) {
                        saveCloudCache(sanitizeMitzvot(result.mitzvot), result.etag, result.version)
                    }
                }
                val combined = (local + sanitizeMitzvot(readCloudCache())).distinctBy { it.id }
                if (combined.isNotEmpty()) {
                    mitzvot = combined
                    resetShuffleQueue(combined)
                    Log.d("MitzvotRepository", "Refreshed: ${combined.size} mitzvot")
                } else {
                    Log.e("MitzvotRepository", "Force refresh produced empty pool; keeping previous list")
                    ensurePoolLoadedLocked()
                }
            } catch (e: Exception) {
                Log.e("MitzvotRepository", "Error during force refresh: ${e.message}", e)
            }
        }
    }

    fun areMitzvotLoaded(): Boolean = mitzvot != null && mitzvot!!.isNotEmpty()

    /**
     * Returns the next mitzvah from a shuffled deck (local + cloud), without replacement
     * until the deck is exhausted, then reshuffles. Avoids showing the same mitzvah twice
     * in a row when the pool has more than one entry.
     */
    suspend fun getRandomMitzvahIfAvailable(): Mitzvah? = withContext(Dispatchers.IO) {
        mutex.withLock {
            val pool = mitzvot
            if (pool.isNullOrEmpty()) return@withLock null
            nextShuffledMitzvahLocked()
        }
    }

    // ---- Core loading logic ----

    /**
     * Ensures [mitzvot] is a non-empty in-memory list.
     * Reloads when null OR empty (empty used to crash because only null was treated as unloaded).
     */
    private fun ensurePoolLoadedLocked() {
        if (!mitzvot.isNullOrEmpty()) return
        val loaded = buildCacheFirstList()
        if (loaded.isNotEmpty()) return
        Log.e("MitzvotRepository", "Cache-first load returned empty; installing emergency fallback")
        val fallback = listOf(emergencyFallbackMitzvah())
        mitzvot = fallback
        resetShuffleQueue(fallback)
    }

    /**
     * Cache-first strategy:
     *  1. Load local bundled assets immediately (always fast, always works offline)
     *  2. Combine with the cached cloud file (also immediate, also offline)
     *  3. In background: check GitHub with ETag — update the cache and in-memory list
     *     only if the file has actually changed since last time
     */
    private fun buildCacheFirstList(): List<Mitzvah> {
        val loader = MitzvotLoader(context)

        val local = sanitizeMitzvot(loader.loadLocalMitzvot())
        val cachedCloud = sanitizeMitzvot(readCloudCache())
        val combined = (local + cachedCloud).distinctBy { it.id }

        Log.d(
            "MitzvotRepository",
            "Cache-first: ${combined.size} mitzvot ready " +
                "(local=${local.size}, cloud=${cachedCloud.size})",
        )

        // Keep previous non-empty pool if this load somehow came up empty
        // (e.g. asset open race / transient parse failure).
        if (combined.isEmpty()) {
            val previous = mitzvot
            if (!previous.isNullOrEmpty()) {
                Log.w("MitzvotRepository", "Load empty; retaining previous pool size=${previous.size}")
                return previous
            }
            mitzvot = emptyList()
            resetShuffleQueue(emptyList())
            return emptyList()
        }

        mitzvot = combined
        resetShuffleQueue(combined)

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

            Log.d(
                "MitzvotRepository",
                "Background check: cloud updated to ${result.mitzvot.size} entries (v${result.version})",
            )
            val cloud = sanitizeMitzvot(result.mitzvot)
            if (cloud.isEmpty()) {
                Log.w("MitzvotRepository", "Background check: cloud payload sanitized to empty; skipped")
                return
            }
            saveCloudCache(cloud, result.etag, result.version)

            // Merge new cloud into in-memory list under mutex
            mutex.withLock {
                val local = sanitizeMitzvot(loader.loadLocalMitzvot())
                val combined = (local + cloud).distinctBy { it.id }
                if (combined.isEmpty()) {
                    Log.w("MitzvotRepository", "Background refresh would empty pool; skipped")
                    return@withLock
                }
                mitzvot = combined
                resetShuffleQueue(combined)
                Log.d("MitzvotRepository", "In-memory list silently refreshed: ${combined.size} mitzvot")
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
                    ?: return emptyList()
                if (cached.version < REQUIRED_MIN_CLOUD_VERSION) {
                    Log.d(
                        "MitzvotRepository",
                        "Cloud cache version ${cached.version} < required $REQUIRED_MIN_CLOUD_VERSION — invalidating",
                    )
                    cloudCacheFile.delete()
                    prefs.edit().remove(PREF_CLOUD_ETAG).remove(PREF_CLOUD_VERSION).apply()
                    return emptyList()
                }
                val list = cached.mitzvot
                Log.d("MitzvotRepository", "Cloud cache file: ${list.size} entries (v${cached.version})")
                list
            } catch (e: Exception) {
                Log.e("MitzvotRepository", "Failed to read cloud cache file: ${e.message}", e)
                cloudCacheFile.delete() // corrupt — remove so we re-download
                emptyList()
            }
        }

        // Migration: extract cloud IDs from old SharedPreferences combined cache
        val oldJson = prefs.getString("cached_mitzvot", null) ?: return emptyList()
        return try {
            val parsed = Gson().fromJson(oldJson, MitzvotList::class.java) ?: return emptyList()
            val cloudOnly = parsed.mitzvot.filter { it.id.startsWith("cloud") }
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

    private fun resetShuffleQueue(pool: List<Mitzvah>) {
        shuffleDeck.clear()
        shuffleIndex = 0
        if (pool.isEmpty()) return
        shuffleDeck.addAll(pool)
        shuffleDeck.shuffle()
        avoidImmediateRepeatLocked()
    }

    /** If the first card matches the last one shown, swap it with a different card (pool size > 1). */
    private fun avoidImmediateRepeatLocked() {
        val last = lastIssuedId ?: return
        if (shuffleDeck.size <= 1) return
        if (shuffleDeck[0].id != last) return
        val swapIdx = shuffleDeck.indexOfFirst { it.id != last }
        if (swapIdx <= 0) return
        val tmp = shuffleDeck[0]
        shuffleDeck[0] = shuffleDeck[swapIdx]
        shuffleDeck[swapIdx] = tmp
    }

    /** Call only while holding [mutex]. Never throws for empty pool. */
    private fun nextShuffledMitzvahLocked(): Mitzvah {
        ensurePoolLoadedLocked()
        val pool = mitzvot
        if (pool.isNullOrEmpty()) {
            // Should be unreachable after ensurePoolLoadedLocked(); keep a hard safety net.
            return emergencyFallbackMitzvah()
        }
        if (shuffleDeck.isEmpty() || shuffleIndex >= shuffleDeck.size) {
            resetShuffleQueue(pool)
        }
        if (shuffleDeck.isEmpty() || shuffleIndex >= shuffleDeck.size) {
            return pool.random()
        }
        val m = shuffleDeck[shuffleIndex++]
        lastIssuedId = m.id
        return m
    }

    private fun emergencyFallbackMitzvah(): Mitzvah =
        Mitzvah(
            id = "emergency_fallback",
            text = "Say Modeh Ani / thank G-d for this moment. Even when the list takes a second to load, gratitude always works!",
            links = emptyList(),
        )

    /** Drop Gson-null / blank entries so UI composition never trips on null fields. */
    @Suppress("UNNECESSARY_SAFE_CALL", "USELESS_ELVIS")
    private fun sanitizeMitzvot(raw: List<Mitzvah>): List<Mitzvah> =
        raw.mapNotNull { m ->
            // Gson may still produce nulls into non-null Kotlin fields via reflection.
            val id = (m.id as String?)?.takeIf { it.isNotBlank() } ?: return@mapNotNull null
            val text = (m.text as String?)?.takeIf { it.isNotBlank() } ?: return@mapNotNull null
            val links = (m.links as List<MitzvahLink>?)
                .orEmpty()
                .mapNotNull { link ->
                    val display = (link.displayText as String?)?.takeIf { it.isNotBlank() }
                        ?: return@mapNotNull null
                    val url = (link.url as String?)?.takeIf { it.isNotBlank() } ?: return@mapNotNull null
                    MitzvahLink(displayText = display, url = url)
                }
            Mitzvah(id = id, text = text, links = links)
        }
}
