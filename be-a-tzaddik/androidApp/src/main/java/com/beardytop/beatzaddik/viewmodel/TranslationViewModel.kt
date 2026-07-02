package com.beardytop.beatzaddik.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import android.content.Context
import com.beardytop.beatzaddik.BuildConfig
import com.beardytop.beatzaddik.data.LanguageInfo
import com.beardytop.beatzaddik.data.LanguagePreferencesManager
import com.beardytop.beatzaddik.service.TranslationService
import com.beardytop.beatzaddik.data.BundledTranslationsCatalog
import com.beardytop.beatzaddik.ui.translation.resolveAppTranslation
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import android.util.Log

class TranslationViewModel(
    private val translationService: TranslationService,
    private val languagePreferencesManager: LanguagePreferencesManager,
) : ViewModel() {
    
    val currentLanguage = languagePreferencesManager.currentLanguage
    val translationEnabled = languagePreferencesManager.translationEnabled
    
    private val _isTranslating = MutableStateFlow(false)
    val isTranslating: StateFlow<Boolean> = _isTranslating.asStateFlow()
    
    private val _translationCache = mutableMapOf<String, String>()
    /** Avoid bursts of parallel API calls (menu + dialogs → 429 → silent fallback to English). */
    private val translateMutex = Mutex()

    /**
     * Loads all bundled translation JSON files eagerly at ViewModel creation so that the first
     * call to [translateText] never races against an empty catalog.  Without this, any
     * translation requested before [AppDependencies.create] is called (e.g. main-screen labels)
     * would hit an empty [BundledTranslationsCatalog], receive an English fallback, and cache
     * that English value forever — making the UI appear untranslated even after the language is set.
     */
    private val bundleLoadJob: Deferred<Unit> = viewModelScope.async {
        BundledTranslationsCatalog.loadFromAssets()
        // Purge any stale English-fallback values that may have been cached before the bundle
        // finished loading (possible on very fast first composition).
        _translationCache.clear()
    }

    private companion object {
        private const val TAG = "TranslationViewModel"
    }
    
    fun setCurrentLanguage(languageCode: String) {
        languagePreferencesManager.setCurrentLanguage(languageCode)
        _translationCache.clear()
    }
    
    fun setTranslationEnabled(enabled: Boolean) {
        languagePreferencesManager.setTranslationEnabled(enabled)
    }
    
    fun getSupportedLanguages(): List<LanguageInfo> {
        return languagePreferencesManager.supportedLanguages
    }
    
    /**
     * Skip API translation only for passages that are Hebrew-script without Latin letters
     * (e.g. liturgy). English UI that cites Hebrew words still contains A–Z and must translate.
     */
    private fun shouldSkipAsAlreadyHebrewScript(text: String, targetLanguage: String): Boolean {
        if (targetLanguage != "he" && targetLanguage != "yi") return false
        val hasHebrewScript = text.any { it in '\u0590'..'\u05FF' }
        val hasLatin = text.any { it in 'A'..'Z' || it in 'a'..'z' }
        return hasHebrewScript && !hasLatin
    }

    suspend fun translateText(text: String): String {
        // Ensure the bundled catalog is loaded before any lookup; fast no-op after first load.
        bundleLoadJob.await()
        if (BuildConfig.DEBUG) {
            Log.d(
                TAG,
                "translateText '${text.take(80)}…' enabled=${translationEnabled.value} lang=${currentLanguage.value}"
            )
        }
        if (!translationEnabled.value || currentLanguage.value == "en") {
            return text
        }

        if (shouldSkipAsAlreadyHebrewScript(text, currentLanguage.value)) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Skipping translation (Hebrew-script-only → ${currentLanguage.value})")
            }
            return text
        }
        
        val lang = currentLanguage.value
        val cacheKey = "${text}_${lang}"
        _translationCache[cacheKey]?.let { return it }

        return translateMutex.withLock {
            _translationCache[cacheKey]?.let { return@withLock it }

            _isTranslating.value = true
            val translatedText = try {
                resolveAppTranslation(text, lang) { apiText ->
                    translationService.translateText(
                        text = apiText,
                        targetLanguage = lang,
                        sourceLanguage = "auto"
                    ) ?: apiText
                }
            } catch (e: kotlinx.coroutines.CancellationException) {
                throw e
            } catch (e: Exception) {
                text
            } finally {
                _isTranslating.value = false
            }
            _translationCache[cacheKey] = translatedText
            translatedText
        }
    }
    
    fun translateTextAsync(text: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            val result = translateText(text)
            onResult(result)
        }
    }
    
    fun clearTranslationCache() {
        _translationCache.clear()
    }
    
    // Method to translate text to a specific language without changing current language setting
    suspend fun translateTextToLanguage(text: String, targetLanguage: String): String {
        bundleLoadJob.await()
        if (targetLanguage == "en") {
            return text
        }
        
        if (shouldSkipAsAlreadyHebrewScript(text, targetLanguage)) {
            return text
        }
        
        val cacheKey = "${text}_${targetLanguage}"
        _translationCache[cacheKey]?.let { return it }

        return translateMutex.withLock {
            _translationCache[cacheKey]?.let { return@withLock it }

            _isTranslating.value = true
            val translatedText = try {
                resolveAppTranslation(text, targetLanguage) { apiText ->
                    translationService.translateText(
                        text = apiText,
                        targetLanguage = targetLanguage,
                        sourceLanguage = "auto"
                    ) ?: apiText
                }
            } catch (e: kotlinx.coroutines.CancellationException) {
                throw e
            } catch (e: Exception) {
                text
            } finally {
                _isTranslating.value = false
            }
            _translationCache[cacheKey] = translatedText
            translatedText
        }
    }
}

class TranslationViewModelFactory(
    private val context: Context,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TranslationViewModel::class.java)) {
            val prefs = LanguagePreferencesManager(context.applicationContext)
            return TranslationViewModel(TranslationService(), prefs) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}