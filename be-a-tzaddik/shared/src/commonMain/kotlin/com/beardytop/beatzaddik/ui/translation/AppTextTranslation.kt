package com.beardytop.beatzaddik.ui.translation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.compositionLocalOf

/** Keeps Latin clock times readable when embedded in RTL paragraphs. */
fun embedLtrForRtlMix(text: String): String =
    if (text.isBlank()) text else "\u2066$text\u2069"

private fun isRtlLanguage(languageCode: String): Boolean =
    languageCode == "he" || languageCode == "yi"

private fun looksLikeCountdown(value: String): Boolean =
    value == "soon" || Regex("""\d+[dhms](?:\s+\d+[dhms])*""").containsMatchIn(value)

private fun looksLikeZmanTimePhrase(value: String): Boolean =
    value.startsWith("after ") || value.startsWith("until ") || value.startsWith("before ")

private fun looksLikeClockOrLatinTime(value: String): Boolean {
    if (value.any { it.isDigit() } &&
        (value.contains("am", ignoreCase = true) || value.contains("pm", ignoreCase = true) ||
            value.contains(':') || value.contains('h') || value.contains('m') || value.contains('d'))
    ) {
        return true
    }
    return Regex("""\d{1,2}:\d{2}""").containsMatchIn(value)
}

private fun shouldSkipTemplateArgTranslation(value: String): Boolean {
    if (value.contains('\u2066')) return true
    return looksLikeCountdown(value) || looksLikeZmanTimePhrase(value) || looksLikeClockOrLatinTime(value)
}

private fun localizeCountdownForRtl(value: String, languageCode: String): String {
    if (!isRtlLanguage(languageCode)) return embedLtrForRtlMix(value)
    if (value == "soon") return "בקרוב"
    Regex("""(\d+)h (\d+)m""").matchEntire(value)?.let { m ->
        return embedLtrForRtlMix("${m.groupValues[1]} שע׳ ${m.groupValues[2]} דק׳")
    }
    Regex("""(\d+)h""").matchEntire(value)?.let { m ->
        return embedLtrForRtlMix("${m.groupValues[1]} שע׳")
    }
    Regex("""(\d+)m""").matchEntire(value)?.let { m ->
        return embedLtrForRtlMix("${m.groupValues[1]} דק׳")
    }
    Regex("""(\d+)d (\d+)h""").matchEntire(value)?.let { m ->
        return embedLtrForRtlMix("${m.groupValues[1]} ימ׳ ${m.groupValues[2]} שע׳")
    }
    Regex("""(\d+)d""").matchEntire(value)?.let { m ->
        return embedLtrForRtlMix("${m.groupValues[1]} ימ׳")
    }
    return embedLtrForRtlMix(value)
}

private fun localizeZmanTimePhraseForRtl(value: String, languageCode: String): String {
    if (!isRtlLanguage(languageCode)) return embedLtrForRtlMix(value)
    return when {
        value.startsWith("after ") ->
            "אחרי ${embedLtrForRtlMix(value.removePrefix("after "))}"
        value.startsWith("until ") ->
            "עד ${embedLtrForRtlMix(value.removePrefix("until "))}"
        value.startsWith("before ") ->
            "לפני ${embedLtrForRtlMix(value.removePrefix("before "))}"
        else -> embedLtrForRtlMix(value)
    }
}

/** Localizes countdowns and clock phrases for Hebrew/Yiddish template args. */
fun localizeTemplateArgsForRtl(
    args: Map<String, String>,
    languageCode: String,
): Map<String, String> {
    if (!isRtlLanguage(languageCode)) return args
    return args.mapValues { (key, value) ->
        if (value.contains('\u2066')) return@mapValues value
        when {
            key == "countdown" || looksLikeCountdown(value) ->
                localizeCountdownForRtl(value, languageCode)
            looksLikeZmanTimePhrase(value) ->
                localizeZmanTimePhraseForRtl(value, languageCode)
            looksLikeClockOrLatinTime(value) ->
                embedLtrForRtlMix(value)
            else -> value
        }
    }
}

fun fillLocalizedTranslationTemplate(
    template: String,
    args: Map<String, String>,
    languageCode: String,
): String = fillTranslationTemplate(template, localizeTemplateArgsForRtl(args, languageCode))

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
    return fillLocalizedTranslationTemplate(translated, args, languageCode)
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
        mutableStateOf(fillLocalizedTranslationTemplate(templateKey, args, appTranslation.languageCode))
    }
    LaunchedEffect(templateKey, argsKey, appTranslation.enabled, appTranslation.languageCode) {
        display = when {
            !appTranslation.enabled || appTranslation.languageCode == "en" ->
                fillTranslationTemplate(templateKey, args)
            shouldSkipMachineTranslation(templateKey, appTranslation.languageCode) ->
                fillLocalizedTranslationTemplate(templateKey, args, appTranslation.languageCode)
            else -> {
                val translatedTemplate = appTranslation.translator.translate(templateKey)
                val translatedArgs = args.mapValues { (_, value) ->
                    if (shouldSkipTemplateArgTranslation(value)) value
                    else appTranslation.translator.translate(value)
                }
                fillLocalizedTranslationTemplate(translatedTemplate, translatedArgs, appTranslation.languageCode)
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
