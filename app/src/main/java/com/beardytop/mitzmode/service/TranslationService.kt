package com.beardytop.mitzmode.service

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.beardytop.mitzmode.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.HttpUrl
import javax.inject.Inject
import javax.inject.Singleton
import dagger.hilt.android.qualifiers.ApplicationContext

@Singleton
class TranslationService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val httpClient = OkHttpClient()
    private val gson = Gson()
    
    // Get API key from local.properties or environment
    private fun getApiKey(): String {
        // Try to read from BuildConfig first
        val apiKey = try {
            com.beardytop.mitzmode.BuildConfig.GOOGLE_TRANSLATE_API_KEY
        } catch (e: Exception) {
            "YOUR_GOOGLE_TRANSLATE_API_KEY_HERE"
        }
        
        // In production, you should store this securely
        // You can also read from environment variables or secure storage
        return apiKey.trim().trim('"')
    }
    
    // Translate text from source language to target language using REST API (POST body — GET + ?q=… breaks long strings)
    suspend fun translateText(
        text: String,
        targetLanguage: String,
        sourceLanguage: String = "auto"
    ): String? = withContext(Dispatchers.IO) {
        try {
            if (text.isBlank()) return@withContext text

            val apiKey = getApiKey()
            if (apiKey.isBlank() ||
                apiKey.equals("YOUR_GOOGLE_TRANSLATE_API_KEY_HERE", ignoreCase = true)) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "No Google Translate API key configured — returning original text")
                }
                return@withContext text
            }

            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Translating (${text.length} chars) to $targetLanguage")
            }

            val url = HttpUrl.Builder()
                .scheme("https")
                .host("translation.googleapis.com")
                .addPathSegment("language")
                .addPathSegment("translate")
                .addPathSegment("v2")
                .addQueryParameter("key", apiKey)
                .build()

            val payload = JsonObject().apply {
                val qArr = JsonArray()
                qArr.add(text)
                add("q", qArr)
                addProperty("target", targetLanguage)
                addProperty("format", "text")
                if (sourceLanguage.isNotBlank() && sourceLanguage.lowercase() != "auto") {
                    addProperty("source", sourceLanguage)
                }
            }
            val json = gson.toJson(payload)
            val body = json.toRequestBody("application/json; charset=utf-8".toMediaType())

            val request = Request.Builder()
                .url(url)
                .post(body)
                .build()
            
            val response = httpClient.newCall(request).execute()
            
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                if (responseBody != null) {
                    val jsonObject = gson.fromJson(responseBody, JsonObject::class.java)
                    val data = jsonObject.getAsJsonObject("data")
                    val translations = data.getAsJsonArray("translations")
                    if (translations.size() > 0) {
                        val translation = translations.get(0).asJsonObject
                        val translatedText = translation.get("translatedText").asString
                        if (BuildConfig.DEBUG) {
                            Log.d(TAG, "Translation successful (${translatedText.length} chars)")
                        }
                        return@withContext translatedText
                    }
                }
            } else {
                Log.w(TAG, "Translation API error: ${response.code} ${response.message}")
                val errorBody = response.body?.string()
                if (errorBody != null && BuildConfig.DEBUG) {
                    Log.d(TAG, "Error details: $errorBody")
                }
            }
            
            // Return original text if translation fails
            text
        } catch (e: Exception) {
            Log.e(TAG, "translateText failed", e)
            // Return original text if translation fails
            text
        }
    }
    
    // Common language codes
    companion object {
        private const val TAG = "TranslationService"
        const val ENGLISH = "en"
        const val SPANISH = "es"
        const val FRENCH = "fr"
        const val GERMAN = "de"
        const val ITALIAN = "it"
        const val PORTUGUESE = "pt"
        const val RUSSIAN = "ru"
        const val CHINESE = "zh"
        const val JAPANESE = "ja"
        const val KOREAN = "ko"
        const val ARABIC = "ar"
        const val HEBREW = "he"
        const val HINDI = "hi"
        const val DUTCH = "nl"
        const val SWEDISH = "sv"
        const val NORWEGIAN = "no"
        const val DANISH = "da"
        const val FINNISH = "fi"
        const val POLISH = "pl"
        const val CZECH = "cs"
        const val HUNGARIAN = "hu"
        const val ROMANIAN = "ro"
        const val GREEK = "el"
        const val TURKISH = "tr"
        const val THAI = "th"
        const val VIETNAMESE = "vi"
        const val INDONESIAN = "id"
        const val MALAY = "ms"
        const val FILIPINO = "tl"
        const val UKRAINIAN = "uk"
        const val BULGARIAN = "bg"
        const val CROATIAN = "hr"
        const val SERBIAN = "sr"
        const val SLOVENIAN = "sl"
        const val SLOVAK = "sk"
        const val LITHUANIAN = "lt"
        const val LATVIAN = "lv"
        const val ESTONIAN = "et"
    }
} 