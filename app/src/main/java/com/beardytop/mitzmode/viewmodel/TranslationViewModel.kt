package com.beardytop.mitzmode.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beardytop.mitzmode.data.LanguageInfo
import com.beardytop.mitzmode.data.LanguagePreferencesManager
import com.beardytop.mitzmode.service.TranslationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TranslationViewModel @Inject constructor(
    private val translationService: TranslationService,
    private val languagePreferencesManager: LanguagePreferencesManager
) : ViewModel() {
    
    val currentLanguage = languagePreferencesManager.currentLanguage
    val translationEnabled = languagePreferencesManager.translationEnabled
    
    private val _isTranslating = MutableStateFlow(false)
    val isTranslating: StateFlow<Boolean> = _isTranslating.asStateFlow()
    
    private val _translationCache = mutableMapOf<String, String>()
    
    fun setCurrentLanguage(languageCode: String) {
        languagePreferencesManager.setCurrentLanguage(languageCode)
    }
    
    fun setTranslationEnabled(enabled: Boolean) {
        languagePreferencesManager.setTranslationEnabled(enabled)
    }
    
    fun getSupportedLanguages(): List<LanguageInfo> {
        return languagePreferencesManager.supportedLanguages
    }
    
    private fun isHebrewText(text: String): Boolean {
        // Check if text contains Hebrew characters
        return text.any { char ->
            char in '\u0590'..'\u05FF' // Hebrew Unicode block
        }
    }
    
    suspend fun translateText(text: String): String {
        println("DEBUG: translateText called with '$text', enabled=${translationEnabled.value}, lang=${currentLanguage.value}")
        if (!translationEnabled.value || currentLanguage.value == "en") {
            return text
        }
        
        // If target language is Hebrew and text contains Hebrew characters, don't translate
        if (currentLanguage.value == "he" && isHebrewText(text)) {
            println("DEBUG: Skipping translation for Hebrew text to Hebrew")
            return text
        }
        
        val cacheKey = "${text}_${currentLanguage.value}"
        _translationCache[cacheKey]?.let { return it }
        
        _isTranslating.value = true
        
        val translatedText = try {
            translationService.translateText(
                text = text,
                targetLanguage = currentLanguage.value,
                sourceLanguage = "en"
            ) ?: text
        } catch (e: Exception) {
            text // Return original text if translation fails
        }
        
        _isTranslating.value = false
        _translationCache[cacheKey] = translatedText
        
        return translatedText
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
        if (targetLanguage == "en") {
            return text
        }
        
        // If target language is Hebrew and text contains Hebrew characters, don't translate
        if (targetLanguage == "he" && isHebrewText(text)) {
            return text
        }
        
        val cacheKey = "${text}_${targetLanguage}"
        _translationCache[cacheKey]?.let { return it }
        
        _isTranslating.value = true
        
        val translatedText = try {
            translationService.translateText(
                text = text,
                targetLanguage = targetLanguage,
                sourceLanguage = "en"
            ) ?: text
        } catch (e: Exception) {
            text // Return original text if translation fails
        }
        
        _isTranslating.value = false
        _translationCache[cacheKey] = translatedText
        
        return translatedText
    }
} 