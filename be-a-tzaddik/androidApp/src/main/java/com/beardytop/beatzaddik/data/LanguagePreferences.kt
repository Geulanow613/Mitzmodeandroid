package com.beardytop.beatzaddik.data

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.app.LocaleManager
import androidx.core.os.LocaleListCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class LanguageInfo(
    val code: String,
    val name: String,
    val nativeName: String
)

class LanguagePreferencesManager(
    private val context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /**
     * Must run before [readStoredLanguage] on first launch so phone language (he/es/fr/ru)
     * is persisted before StateFlows are created.
     */
    private val bootstrappedSettings: Pair<String, Boolean> by lazy {
        ensureInitialLanguageFromPhoneSettings()
        readStoredLanguage() to readTranslationEnabled()
    }

    private val _currentLanguage = MutableStateFlow(bootstrappedSettings.first)
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    private val _translationEnabled = MutableStateFlow(bootstrappedSettings.second)
    val translationEnabled: StateFlow<Boolean> = _translationEnabled.asStateFlow()

    fun getCurrentLanguage(): String = readStoredLanguage()

    private fun readStoredLanguage(): String {
        return try {
            prefs.getString(KEY_CURRENT_LANGUAGE, "en") ?: "en"
        } catch (e: Exception) {
            android.util.Log.e("LanguagePreferences", "Error getting current language", e)
            "en"
        }
    }

    private fun readTranslationEnabled(): Boolean {
        return try {
            prefs.getBoolean(KEY_TRANSLATION_ENABLED, false)
        } catch (e: Exception) {
            android.util.Log.e("LanguagePreferences", "Error checking translation enabled", e)
            false
        }
    }

    /**
     * On first install, match the phone's **system language** list (Settings → Languages).
     * Starts natively in he/es/fr/ru when supported; otherwise English.
     */
    private fun ensureInitialLanguageFromPhoneSettings() {
        if (prefs.getBoolean(KEY_LANGUAGE_INITIALIZED, false)) return
        if (prefs.contains(KEY_CURRENT_LANGUAGE)) {
            prefs.edit().putBoolean(KEY_LANGUAGE_INITIALIZED, true).commit()
            return
        }

        val choice = resolveLanguageFromPhoneSettings()
        try {
            prefs.edit()
                .putString(KEY_CURRENT_LANGUAGE, choice.languageCode)
                .putBoolean(KEY_TRANSLATION_ENABLED, choice.translationEnabled)
                .putBoolean(KEY_LANGUAGE_INITIALIZED, true)
                .commit()
        } catch (e: Exception) {
            android.util.Log.e("LanguagePreferences", "Error saving initial language from device", e)
        }
    }

    private fun resolveLanguageFromPhoneSettings(): DeviceAppLanguage.BootstrapChoice {
        val tags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val localeList = context.getSystemService(LocaleManager::class.java).applicationLocales
            (0 until localeList.size()).map { localeList[it].language }
        } else {
            val localeList = LocaleListCompat.getDefault()
            (0 until localeList.size()).mapNotNull { localeList[it]?.language }
        }
        return DeviceAppLanguage.resolveFromDeviceLanguageTags(tags)
    }

    private companion object {
        private const val PREFS_NAME = "tzaddik_language_prefs"
        private const val KEY_CURRENT_LANGUAGE = "current_language"
        private const val KEY_TRANSLATION_ENABLED = "translation_enabled"
        private const val KEY_LANGUAGE_INITIALIZED = "language_initialized_from_device"
    }
    
    fun setCurrentLanguage(languageCode: String) {
        // Update StateFlow immediately
        _currentLanguage.value = languageCode
        if (languageCode != "en") {
            _translationEnabled.value = true
        }

        // Save to SharedPreferences in background thread
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val editor = prefs.edit()
                    .putString(KEY_CURRENT_LANGUAGE, languageCode)
                if (languageCode != "en") {
                    editor.putBoolean(KEY_TRANSLATION_ENABLED, true)
                }
                val success = editor.commit()
                    
                if (!success) {
                    android.util.Log.w("LanguagePreferences", "Failed to save current language")
                }
            } catch (e: Exception) {
                android.util.Log.e("LanguagePreferences", "Error saving current language", e)
            }
        }
    }
    
    fun isTranslationEnabled(): Boolean = readTranslationEnabled()
    
    fun setTranslationEnabled(enabled: Boolean) {
        // Update StateFlow immediately
        _translationEnabled.value = enabled
        
        // Save to SharedPreferences in background thread
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val success = prefs.edit()
                    .putBoolean(KEY_TRANSLATION_ENABLED, enabled)
                    .commit()
                    
                if (!success) {
                    android.util.Log.w("LanguagePreferences", "Failed to save translation enabled")
                }
            } catch (e: Exception) {
                android.util.Log.e("LanguagePreferences", "Error saving translation enabled", e)
            }
        }
    }
    
    // Popular languages for the app
    val supportedLanguages = listOf(
        LanguageInfo("en", "English", "English"),
        LanguageInfo("he", "Hebrew", "עברית"),
        LanguageInfo("yi", "Yiddish", "ייִדיש"),
        LanguageInfo("es", "Spanish", "Español"),
        LanguageInfo("fr", "French", "Français"),
        LanguageInfo("de", "German", "Deutsch"),
        LanguageInfo("ru", "Russian", "Русский"),
        LanguageInfo("ar", "Arabic", "العربية"),
        LanguageInfo("pt", "Portuguese", "Português"),
        LanguageInfo("it", "Italian", "Italiano"),
        LanguageInfo("pl", "Polish", "Polski"),
        LanguageInfo("hu", "Hungarian", "Magyar"),
        LanguageInfo("ro", "Romanian", "Română"),
        LanguageInfo("tr", "Turkish", "Türkçe"),
        LanguageInfo("fa", "Persian", "فارسی"),
        LanguageInfo("nl", "Dutch", "Nederlands"),
        LanguageInfo("sv", "Swedish", "Svenska"),
        LanguageInfo("da", "Danish", "Dansk"),
        LanguageInfo("no", "Norwegian", "Norsk"),
        LanguageInfo("fi", "Finnish", "Suomi"),
        LanguageInfo("cs", "Czech", "Čeština"),
        LanguageInfo("sk", "Slovak", "Slovenčina"),
        LanguageInfo("hr", "Croatian", "Hrvatski"),
        LanguageInfo("sr", "Serbian", "Српски"),
        LanguageInfo("bg", "Bulgarian", "Български"),
        LanguageInfo("uk", "Ukrainian", "Українська"),
        LanguageInfo("lt", "Lithuanian", "Lietuvių"),
        LanguageInfo("lv", "Latvian", "Latviešu"),
        LanguageInfo("et", "Estonian", "Eesti"),
        LanguageInfo("sl", "Slovenian", "Slovenščina"),
        LanguageInfo("el", "Greek", "Ελληνικά"),
        LanguageInfo("ja", "Japanese", "日本語"),
        LanguageInfo("ko", "Korean", "한국어"),
        LanguageInfo("zh", "Chinese", "中文"),
        LanguageInfo("hi", "Hindi", "हिन्दी"),
        LanguageInfo("th", "Thai", "ไทย"),
        LanguageInfo("vi", "Vietnamese", "Tiếng Việt"),
        LanguageInfo("id", "Indonesian", "Bahasa Indonesia"),
        LanguageInfo("ms", "Malay", "Bahasa Melayu"),
        LanguageInfo("tl", "Filipino", "Filipino"),
        LanguageInfo("sw", "Swahili", "Kiswahili"),
        LanguageInfo("am", "Amharic", "አማርኛ"),
        LanguageInfo("bn", "Bengali", "বাংলা"),
        LanguageInfo("ur", "Urdu", "اردو"),
        LanguageInfo("ta", "Tamil", "தமிழ்"),
        LanguageInfo("te", "Telugu", "తెలుగు"),
        LanguageInfo("ml", "Malayalam", "മലയാളം"),
        LanguageInfo("kn", "Kannada", "ಕನ್ನಡ"),
        LanguageInfo("gu", "Gujarati", "ગુજરાતી"),
        LanguageInfo("mr", "Marathi", "मराठी"),
        LanguageInfo("pa", "Punjabi", "ਪੰਜਾਬੀ"),
        LanguageInfo("ne", "Nepali", "नेपाली"),
        LanguageInfo("si", "Sinhala", "සිංහල"),
        LanguageInfo("my", "Myanmar", "မြန်မာ"),
        LanguageInfo("km", "Khmer", "ខ្មែរ"),
        LanguageInfo("lo", "Lao", "ລາວ"),
        LanguageInfo("ka", "Georgian", "ქართული"),
        LanguageInfo("hy", "Armenian", "Հայերեն"),
        LanguageInfo("az", "Azerbaijani", "Azərbaycan"),
        LanguageInfo("kk", "Kazakh", "Қазақ"),
        LanguageInfo("ky", "Kyrgyz", "Кыргыз"),
        LanguageInfo("uz", "Uzbek", "O'zbek"),
        LanguageInfo("tg", "Tajik", "Тоҷикӣ"),
        LanguageInfo("mn", "Mongolian", "Монгол"),
        LanguageInfo("bo", "Tibetan", "བོད་ཡིག"),
        LanguageInfo("dz", "Dzongkha", "རྫོང་ཁ"),
        LanguageInfo("is", "Icelandic", "Íslenska"),
        LanguageInfo("fo", "Faroese", "Føroyskt"),
        LanguageInfo("ga", "Irish", "Gaeilge"),
        LanguageInfo("gd", "Scottish Gaelic", "Gàidhlig"),
        LanguageInfo("cy", "Welsh", "Cymraeg"),
        LanguageInfo("br", "Breton", "Brezhoneg"),
        LanguageInfo("eu", "Basque", "Euskera"),
        LanguageInfo("ca", "Catalan", "Català"),
        LanguageInfo("gl", "Galician", "Galego"),
        LanguageInfo("mt", "Maltese", "Malti"),
        LanguageInfo("sq", "Albanian", "Shqip"),
        LanguageInfo("mk", "Macedonian", "Македонски"),
        LanguageInfo("be", "Belarusian", "Беларуская"),
        LanguageInfo("lv", "Latvian", "Latviešu"),
        LanguageInfo("lt", "Lithuanian", "Lietuvių"),
        LanguageInfo("et", "Estonian", "Eesti")
    )
} 