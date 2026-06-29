package com.beardytop.beatzaddik.ui.translation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.compositionLocalOf

/** Fills `{name}` placeholders in a translated template string. */
fun fillTranslationTemplate(template: String, args: Map<String, String>): String {
    var out = template
    for ((key, value) in args) {
        out = out.replace("{$key}", value)
    }
    return out
}

suspend fun resolveAppTranslationTemplate(
    templateKey: String,
    args: Map<String, String>,
    languageCode: String,
    apiTranslate: suspend (String) -> String,
): String {
    val translated = resolveAppTranslation(templateKey, languageCode, apiTranslate)
    return fillTranslationTemplate(translated, args)
}

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

/** A single language the app can display in. */
data class LanguageOption(
    val code: String,
    val englishName: String,
    val nativeName: String,
    val isOffline: Boolean = false,
)

/**
 * Encapsulates the language-selection state exposed to the KMP settings screen.
 * Provided by the host Android app via [LocalLanguageSelector].
 */
data class LanguageSelectorState(
    val enabled: Boolean = false,
    val currentLanguageCode: String = "en",
    val availableLanguages: List<LanguageOption> = emptyList(),
    val onLanguageChange: (String) -> Unit = {},
    val onEnabledChange: (Boolean) -> Unit = {},
)

/** Composition local filled by the Android host so the KMP Settings screen can change language. */
val LocalLanguageSelector = compositionLocalOf { LanguageSelectorState() }

/**
 * Translates [source] once as a single catalog key (required for multi-paragraph explainers).
 * Child composables that split text into lines must use the returned string with plain [Text],
 * not [AppText], or each line will miss the bundle and fall back to English.
 */
@Composable
fun rememberAppTranslatedText(source: String): String {
    val appTranslation = LocalAppTranslation.current
    var display by remember(source) { mutableStateOf(source) }
    LaunchedEffect(source, appTranslation.enabled, appTranslation.languageCode) {
        display = when {
            !appTranslation.enabled || appTranslation.languageCode == "en" -> source
            shouldSkipMachineTranslation(source, appTranslation.languageCode) -> source
            else -> appTranslation.translator.translate(source)
        }
    }
    return display
}

/** Translates a catalog template key (with `{placeholder}` tokens), then fills [args]. */
@Composable
fun rememberAppTranslatedTemplate(templateKey: String, args: Map<String, String>): String {
    val appTranslation = LocalAppTranslation.current
    val argsKey = args.entries.sortedBy { it.key }.joinToString("|") { "${it.key}=${it.value}" }
    var display by remember(templateKey, argsKey) {
        mutableStateOf(fillTranslationTemplate(templateKey, args))
    }
    LaunchedEffect(templateKey, argsKey, appTranslation.enabled, appTranslation.languageCode) {
        display = when {
            !appTranslation.enabled || appTranslation.languageCode == "en" ->
                fillTranslationTemplate(templateKey, args)
            shouldSkipMachineTranslation(templateKey, appTranslation.languageCode) ->
                fillTranslationTemplate(templateKey, args)
            else -> {
                val translatedTemplate = appTranslation.translator.translate(templateKey)
                val translatedArgs = args.mapValues { (_, value) ->
                    appTranslation.translator.translate(value)
                }
                fillTranslationTemplate(translatedTemplate, translatedArgs)
            }
        }
    }
    return display
}

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
