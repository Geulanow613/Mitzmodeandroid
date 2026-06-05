package com.beardytop.beatzaddik.ui.translation

import androidx.compose.runtime.compositionLocalOf

/** Google Translate (or other) bridge supplied by the host app on Android. */
fun interface AppTextTranslator {
    suspend fun translate(text: String): String
}

data class AppTranslationState(
    val enabled: Boolean = false,
    val languageCode: String = "en",
    val translator: AppTextTranslator = AppTextTranslator { it },
)

val LocalAppTranslation = compositionLocalOf { AppTranslationState() }

/**
 * Liturgy / Hebrew-only passages should not be sent through machine translation.
 * English UI that cites Hebrew words still contains Latin letters and should translate.
 */
fun shouldSkipMachineTranslation(text: String, targetLanguage: String): Boolean {
    if (targetLanguage != "he" && targetLanguage != "yi") return false
    val hasHebrewScript = text.any { it in '\u0590'..'\u05FF' }
    val hasLatin = text.any { it in 'A'..'Z' || it in 'a'..'z' }
    return hasHebrewScript && !hasLatin
}
