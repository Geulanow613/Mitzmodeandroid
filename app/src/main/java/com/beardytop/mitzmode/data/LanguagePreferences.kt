package com.beardytop.mitzmode.data

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton
import dagger.hilt.android.qualifiers.ApplicationContext
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

@Singleton
class LanguagePreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences("language_prefs", Context.MODE_PRIVATE)
    
    private val _currentLanguage = MutableStateFlow(getCurrentLanguage())
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()
    
    private val _translationEnabled = MutableStateFlow(isTranslationEnabled())
    val translationEnabled: StateFlow<Boolean> = _translationEnabled.asStateFlow()
    
    fun getCurrentLanguage(): String {
        return try {
            prefs.getString("current_language", "en") ?: "en"
        } catch (e: Exception) {
            android.util.Log.e("LanguagePreferences", "Error getting current language", e)
            "en"
        }
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
                    .putString("current_language", languageCode)
                if (languageCode != "en") {
                    editor.putBoolean("translation_enabled", true)
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
    
    fun isTranslationEnabled(): Boolean {
        return try {
            prefs.getBoolean("translation_enabled", false)
        } catch (e: Exception) {
            android.util.Log.e("LanguagePreferences", "Error checking translation enabled", e)
            false
        }
    }
    
    fun setTranslationEnabled(enabled: Boolean) {
        // Update StateFlow immediately
        _translationEnabled.value = enabled
        
        // Save to SharedPreferences in background thread
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val success = prefs.edit()
                    .putBoolean("translation_enabled", enabled)
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