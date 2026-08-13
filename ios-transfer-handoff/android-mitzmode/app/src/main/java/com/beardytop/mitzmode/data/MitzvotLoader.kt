package com.beardytop.mitzmode.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.net.URL
import java.io.IOException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.ConnectException

class MitzvotLoader(private val context: Context) {
    data class MitzvotList(val mitzvot: List<Mitzvah>, val version: Int = 1)

    /**
     * Result from a conditional cloud fetch.
     * - wasModified = false → server returned 304 Not Modified; use your existing cache
     * - wasModified = true  → server returned 200 with new content in [mitzvot]; etag is the new value
     */
    data class CloudLoadResult(
        val mitzvot: List<Mitzvah>,
        val etag: String?,
        val wasModified: Boolean,
        val version: Int = 1
    )

    private val githubUrl = "https://raw.githubusercontent.com/Geulanow613/mitzmode/main/mitzvotcloud.json"

    /**
     * Conditionally fetches cloud mitzvot using HTTP ETag / If-None-Match.
     * When [storedEtag] matches the server's ETag, returns a result with wasModified=false
     * and an empty list — caller should keep using its existing cache.
     * Longer timeouts are acceptable here because the caller already has cached data to show.
     */
    suspend fun loadCloudMitzvotConditional(storedEtag: String?): CloudLoadResult =
        withContext(Dispatchers.IO) {
            val connection = URL(githubUrl).openConnection() as HttpURLConnection
            connection.connectTimeout = 4000
            connection.readTimeout = 10000
            connection.setRequestProperty("User-Agent", "MitzMode-Android-App")
            connection.setRequestProperty("Accept", "application/json")
            connection.setRequestProperty("Connection", "close")

            if (storedEtag != null) {
                connection.setRequestProperty("If-None-Match", storedEtag)
                Log.d("MitzvotLoader", "Conditional fetch — ETag: $storedEtag")
            }

            try {
                val responseCode = connection.responseCode
                Log.d("MitzvotLoader", "Cloud response: $responseCode")

                if (responseCode == HttpURLConnection.HTTP_NOT_MODIFIED) {
                    Log.d("MitzvotLoader", "Cloud mitzvot not modified (304) — cache is current")
                    return@withContext CloudLoadResult(emptyList(), storedEtag, wasModified = false)
                }

                if (responseCode != HttpURLConnection.HTTP_OK) {
                    throw IOException("Unexpected HTTP $responseCode")
                }

                val newEtag = connection.getHeaderField("ETag")
                val jsonString = connection.inputStream.bufferedReader().use { it.readText() }
                val parsed = Gson().fromJson<MitzvotList>(
                    jsonString, object : TypeToken<MitzvotList>() {}.type
                )

                Log.d("MitzvotLoader", "Cloud updated: ${parsed.mitzvot.size} entries, v${parsed.version}, ETag: $newEtag")
                CloudLoadResult(parsed.mitzvot, newEtag, wasModified = true, version = parsed.version)
            } catch (e: Exception) {
                Log.e("MitzvotLoader", "Conditional cloud fetch error: ${e.message}")
                throw e
            } finally {
                try { connection.disconnect() } catch (_: Exception) {}
            }
        }

    fun loadLocalMitzvot(): List<Mitzvah> {
        return try {
            val jsonString = context.assets.open("mitzvotlistfull.json").use { it.bufferedReader().readText() }
            Log.d("MitzvotLoader", "Local JSON loaded")
            try {
                Gson().fromJson<MitzvotList>(jsonString, object : TypeToken<MitzvotList>() {}.type).mitzvot
            } catch (e: Exception) {
                Log.e("MitzvotLoader", "Failed to parse local JSON", e)
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("MitzvotLoader", "Failed to load local mitzvot: ${e.message}", e)
            emptyList()
        }
    }

    fun isNetworkAvailable(): Boolean {
        return try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val caps = cm.getNetworkCapabilities(cm.activeNetwork ?: return false) ?: return false
            caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } catch (e: Exception) {
            Log.e("MitzvotLoader", "Error checking network: ${e.message}")
            false
        }
    }

    // ---- Legacy path kept for refreshMitzvot() force-refresh ----

    suspend fun loadMitzvot(): List<Mitzvah> = withContext(Dispatchers.IO) {
        val localMitzvot = loadLocalMitzvot()
        if (localMitzvot.isEmpty()) return@withContext emptyList()

        val cloudMitzvot = if (isNetworkAvailable()) {
            try {
                withTimeoutOrNull(1500) { loadCloudMitzvotWithRetry() } ?: emptyList()
            } catch (e: Exception) {
                Log.w("MitzvotLoader", "loadMitzvot cloud error: ${e.message}")
                emptyList()
            }
        } else emptyList()

        (localMitzvot + cloudMitzvot).distinctBy { it.id }
    }

    private suspend fun loadCloudMitzvotWithRetry(): List<Mitzvah> {
        var lastException: Exception? = null
        repeat(2) { attempt ->
            try {
                Log.d("MitzvotLoader", "Attempt ${attempt + 1}")
                return loadCloudMitzvotLegacy()
            } catch (e: Exception) {
                lastException = e
                if (e is SocketTimeoutException || e is ConnectException) return@repeat
            }
        }
        throw lastException ?: IOException("Unknown cloud load error")
    }

    private fun loadCloudMitzvotLegacy(): List<Mitzvah> {
        val connection = URL(githubUrl).openConnection() as HttpURLConnection
        connection.connectTimeout = 800
        connection.readTimeout = 800
        connection.setRequestProperty("User-Agent", "MitzMode-Android-App")
        connection.setRequestProperty("Accept", "application/json")
        connection.setRequestProperty("Connection", "close")
        return try {
            if (connection.responseCode != HttpURLConnection.HTTP_OK)
                throw IOException("HTTP ${connection.responseCode}")
            val json = connection.inputStream.bufferedReader().use { it.readText() }
            Gson().fromJson<MitzvotList>(json, object : TypeToken<MitzvotList>() {}.type).mitzvot
        } finally {
            try { connection.disconnect() } catch (_: Exception) {}
        }
    }
}
