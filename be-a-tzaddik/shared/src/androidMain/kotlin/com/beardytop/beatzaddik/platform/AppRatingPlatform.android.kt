package com.beardytop.beatzaddik.platform

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.beardytop.beatzaddik.domain.AppRatingPromptPolicy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.time.Instant

private var ratingAppContext: Context? = null

fun initAppRatingPlatform(context: Context) {
    ratingAppContext = context.applicationContext
}

actual fun appRatingPlatformLabel(): String = "android"

actual fun appStoreWriteReviewUrl(): String = AppRatingPromptPolicy.PLAY_STORE_LISTING_URL

actual fun openAppStoreListing() {
    val context = ratingAppContext ?: return
    val uri = Uri.parse(AppRatingPromptPolicy.PLAY_STORE_LISTING_URL)
    val intent = Intent(Intent.ACTION_VIEW, uri).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    runCatching { context.startActivity(intent) }
}

actual suspend fun submitAppRatingFeedback(rating: Int, message: String): Boolean =
    withContext(Dispatchers.IO) {
        runCatching {
            val connection =
                (URL(AppRatingPromptPolicy.FORMSPREE_ENDPOINT).openConnection() as HttpURLConnection).apply {
                    requestMethod = "POST"
                    connectTimeout = 8000
                    readTimeout = 12000
                    doOutput = true
                    setRequestProperty("Content-Type", "application/json")
                    setRequestProperty("Accept", "application/json")
                    setRequestProperty("User-Agent", "MitzMode-Android-App")
                }
            try {
                val body = JSONObject()
                    .put("name", "App rating feedback")
                    .put("rating", rating)
                    .put("message", message.ifBlank { "(no message)" })
                    .put("platform", "android")
                    .put("timestamp", Instant.now().toString())
                    .toString()
                connection.outputStream.bufferedWriter().use { it.write(body) }
                connection.responseCode in 200..299
            } finally {
                connection.disconnect()
            }
        }.getOrDefault(false)
    }
