package com.beardytop.beatzaddik.ui.translation

import com.beardytop.beatzaddik.data.BundledTranslationsCatalog
import com.beardytop.beatzaddik.domain.BirkatHachamahRules
import com.beardytop.beatzaddik.domain.EffectiveNusach
import com.beardytop.beatzaddik.domain.ExplainerTemplateFill
import com.beardytop.beatzaddik.domain.OmerCountText
import com.beardytop.beatzaddik.domain.ZmanCountdownFormatter
import com.beardytop.beatzaddik.domain.ZmanimFormatter
import kotlinx.datetime.LocalDate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

/** Keeps Latin clock times readable when embedded in RTL paragraphs. */
fun embedLtrForRtlMix(text: String): String =
    if (text.isBlank()) text else "\u2066$text\u2069"

/** Plain Hebrew + clock cluster for layout in an explicit RTL Row (no bidi isolates). */
fun formatRtlUpcomingTimingCluster(parts: List<String>): String =
    parts.filter { it.isNotBlank() }.joinToString(" ")

fun isAppRtlLanguage(languageCode: String): Boolean =
    languageCode == "he" || languageCode == "yi"

private fun isRtlLanguage(languageCode: String): Boolean = isAppRtlLanguage(languageCode)

/** Forces LTR for en/es/fr/ru and RTL only for Hebrew (and Yiddish). */
@Composable
fun AppDirectionalLayout(
    languageCode: String,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalLayoutDirection provides if (isAppRtlLanguage(languageCode)) {
            LayoutDirection.Rtl
        } else {
            LayoutDirection.Ltr
        },
    ) {
        content()
    }
}

private fun looksLikeCountdown(value: String): Boolean =
    value == "soon" ||
        value == ZmanCountdownFormatter.UNDER_ONE_MINUTE ||
        value == "now" ||
        Regex("""\d+[dhms](?:\s+\d+[dhms])*""").containsMatchIn(value) ||
        Regex("""\d+ min""").matches(value)

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
    if (!isRtlLanguage(languageCode)) return value
    return when (value) {
        "soon" -> "בקרוב"
        "now" -> "עכשיו"
        ZmanCountdownFormatter.UNDER_ONE_MINUTE -> "דקה"
        else -> embedLtrForRtlMix(value)
    }
}

private fun localizeClockArg(value: String, languageCode: String): String {
    val converted = ZmanimFormatter.reformatClockStringForLanguage(value, languageCode)
    return if (isRtlLanguage(languageCode)) embedLtrForRtlMix(converted) else converted
}

private fun localizeZmanTimePhraseForRtl(value: String, languageCode: String): String {
    if (!isRtlLanguage(languageCode)) return value
    return when {
        value.startsWith("after ") ->
            "אחרי ${localizeClockArg(value.removePrefix("after "), languageCode)}"
        value.startsWith("until ") ->
            "עד ${localizeClockArg(value.removePrefix("until "), languageCode)}"
        value.startsWith("before ") ->
            "לפני ${localizeClockArg(value.removePrefix("before "), languageCode)}"
        else -> localizeClockArg(value, languageCode)
    }
}

private val ENGLISH_GREGORIAN_DATE_LABEL = Regex(
    """^(?:Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday), """ +
        """(?:January|February|March|April|May|June|July|August|September|October|November|December) """ +
        """\d{1,2}, \d{4}$""",
)

private val ENGLISH_MONTH_TO_NUMBER = mapOf(
    "January" to 1, "February" to 2, "March" to 3, "April" to 4,
    "May" to 5, "June" to 6, "July" to 7, "August" to 8,
    "September" to 9, "October" to 10, "November" to 11, "December" to 12,
)

private fun localizeGregorianDateLabel(value: String, languageCode: String): String {
    if (!isRtlLanguage(languageCode)) return value
    val trimmed = value.trim()
    if (!ENGLISH_GREGORIAN_DATE_LABEL.matches(trimmed)) return value
    val parts = trimmed.split(", ")
    if (parts.size != 2) return value
    val monthDayYear = parts[1].split(' ')
    if (monthDayYear.size < 3) return value
    val month = ENGLISH_MONTH_TO_NUMBER[monthDayYear[0]] ?: return value
    val day = monthDayYear[1].trimEnd(',')
    val year = monthDayYear[2]
    val date = runCatching {
        LocalDate(year.toInt(), month, day.toInt())
    }.getOrNull() ?: return value
    return BirkatHachamahRules.formatOccurrenceDateHebrew(date)
}

private val ENGLISH_WEEKDAY_TO_HE = mapOf(
    "Sunday" to "יום ראשון",
    "Monday" to "יום שני",
    "Tuesday" to "יום שלישי",
    "Wednesday" to "יום רביעי",
    "Thursday" to "יום חמישי",
    "Friday" to "יום שישי",
    "Saturday" to "שבת",
)

private fun localizeEnglishWeekday(value: String, languageCode: String): String {
    if (!isRtlLanguage(languageCode)) return value
    return ENGLISH_WEEKDAY_TO_HE[value] ?: value
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
            key == "todaySummary" && args.containsKey("day") && args.containsKey("nusach") -> {
                val day = args["day"]?.toIntOrNull() ?: return@mapValues value
                val nusach = runCatching {
                    EffectiveNusach.valueOf(args["nusach"] ?: return@mapValues value)
                }.getOrNull() ?: return@mapValues value
                OmerCountText.omerDaySummaryHe(day, nusach)
            }
            key == "tonightSummary" && args.containsKey("day") && args.containsKey("nusach") -> {
                val day = args["day"]?.toIntOrNull()?.plus(1) ?: return@mapValues value
                if (day > 49) return@mapValues value
                val nusach = runCatching {
                    EffectiveNusach.valueOf(args["nusach"] ?: return@mapValues value)
                }.getOrNull() ?: return@mapValues value
                OmerCountText.omerDaySummaryHe(day, nusach)
            }
            key == "speechPhrase" && args.containsKey("day") && args.containsKey("nusach") -> {
                val day = args["day"]?.toIntOrNull()?.plus(1) ?: return@mapValues value
                if (day > 49) return@mapValues value
                val nusach = runCatching {
                    EffectiveNusach.valueOf(args["nusach"] ?: return@mapValues value)
                }.getOrNull() ?: return@mapValues value
                "היום ${OmerCountText.omerDaySummaryHe(day, nusach)}."
            }
            key == "tonight" || key == "tomorrowNight" ->
                localizeEnglishWeekday(value, languageCode)
            key == "month" ->
                BundledTranslationsCatalog.lookup(value, languageCode) ?: value
            key == "time" || looksLikeClockOrLatinTime(value) ->
                localizeClockArg(value, languageCode)
            key == "dateLabel" || key == "date" ->
                localizeGregorianDateLabel(value, languageCode)
            key == "countdown" || looksLikeCountdown(value) ->
                localizeCountdownForRtl(value, languageCode)
            looksLikeZmanTimePhrase(value) ->
                localizeZmanTimePhraseForRtl(value, languageCode)
            else -> value
        }
    }
}

fun fillLocalizedTranslationTemplate(
    template: String,
    args: Map<String, String>,
    languageCode: String,
): String = fillTranslationTemplate(template, localizeTemplateArgsForRtl(args, languageCode))

/**
 * Fills `{name}` and legacy `$name` placeholders in a translated template string.
 * Runs two passes so an arg value that itself carries another `{otherArg}` placeholder
 * (e.g. a translated sub-phrase like " (approx. {time} — enable location…)" substituted
 * in as the value of a different arg) still gets fully resolved in a single call.
 * Longer keys are substituted first so `$nusach` does not corrupt `$nusachWhen`.
 */
fun fillTranslationTemplate(template: String, args: Map<String, String>): String =
    ExplainerTemplateFill.fill(template, args, passes = 2)

private val TEMPLATE_PLACEHOLDER_REGEX = Regex("""(\{[^}]+\}|\$[A-Za-z_][A-Za-z0-9_]*)""")

/** Translates literal segments only so `{key}` / `$key` placeholders stay machine-readable. */
internal fun translatePreservingPlaceholdersSync(
    template: String,
    translate: (String) -> String,
): String {
    if (!TEMPLATE_PLACEHOLDER_REGEX.containsMatchIn(template)) {
        return translate(template)
    }
    val out = StringBuilder()
    var lastIndex = 0
    TEMPLATE_PLACEHOLDER_REGEX.findAll(template).forEach { match ->
        if (match.range.first > lastIndex) {
            val literal = template.substring(lastIndex, match.range.first)
            if (literal.isNotEmpty()) {
                out.append(translate(literal))
            }
        }
        out.append(match.value)
        lastIndex = match.range.last + 1
    }
    if (lastIndex < template.length) {
        out.append(translate(template.substring(lastIndex)))
    }
    return out.toString()
}

internal suspend fun translatePreservingPlaceholders(
    template: String,
    translate: suspend (String) -> String,
): String {
    if (!TEMPLATE_PLACEHOLDER_REGEX.containsMatchIn(template)) {
        return translate(template)
    }
    val out = StringBuilder()
    var lastIndex = 0
    TEMPLATE_PLACEHOLDER_REGEX.findAll(template).forEach { match ->
        if (match.range.first > lastIndex) {
            val literal = template.substring(lastIndex, match.range.first)
            if (literal.isNotEmpty()) {
                out.append(translate(literal))
            }
        }
        out.append(match.value)
        lastIndex = match.range.last + 1
    }
    if (lastIndex < template.length) {
        out.append(translate(template.substring(lastIndex)))
    }
    return out.toString()
}

suspend fun resolveAppTranslationTemplate(
    templateKey: String,
    args: Map<String, String>,
    languageCode: String,
    apiTranslate: suspend (String) -> String,
): String {
    if (languageCode == "en" || templateKey.isBlank()) {
        return fillLocalizedTranslationTemplate(templateKey, args, languageCode)
    }
    if (shouldSkipMachineTranslation(templateKey, languageCode)) {
        return fillLocalizedTranslationTemplate(templateKey, args, languageCode)
    }
    val bundledTemplate = BundledTranslationsCatalog.lookup(templateKey, languageCode)
    val translatedTemplate = when {
        bundledTemplate != null -> bundledTemplate
        BundledTranslationLanguages.isBundled(languageCode) -> templateKey
        else -> translatePreservingPlaceholders(templateKey, apiTranslate)
    }
    val translatedArgs = if (BundledTranslationLanguages.isBundled(languageCode)) {
        args
    } else {
        args.mapValues { (_, value) ->
            if (shouldSkipTemplateArgTranslation(value)) value
            else apiTranslate(value)
        }
    }
    return fillLocalizedTranslationTemplate(translatedTemplate, translatedArgs, languageCode)
}

/** Google Translate (or other) bridge supplied by the host app on Android. */
fun interface AppTextTranslator {
    suspend fun translate(text: String): String
}

data class AppTranslationState(
    val enabled: Boolean = false,
    val languageCode: String = "en",
    val translator: AppTextTranslator = AppTextTranslator { it },
) {
    /** Layout direction, clocks, and bundled strings follow what is on screen (not the stored language when English UI). */
    val displayLanguageCode: String
        get() = if (!enabled || languageCode == "en") "en" else languageCode
}

private fun initialBundledText(source: String, appTranslation: AppTranslationState): String =
    when {
        !appTranslation.enabled || appTranslation.languageCode == "en" -> source
        else -> resolveBundledTranslationSync(source, appTranslation.languageCode)
    }

private fun initialBundledTemplate(
    templateKey: String,
    args: Map<String, String>,
    appTranslation: AppTranslationState,
): String =
    when {
        !appTranslation.enabled || appTranslation.languageCode == "en" ->
            fillTranslationTemplate(templateKey, args)
        else -> resolveBundledTemplateSync(templateKey, args, appTranslation.languageCode)
    }

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
    var display by remember(source, appTranslation.enabled, appTranslation.languageCode) {
        mutableStateOf(initialBundledText(source, appTranslation))
    }
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
    var display by remember(templateKey, argsKey, appTranslation.enabled, appTranslation.languageCode) {
        mutableStateOf(initialBundledTemplate(templateKey, args, appTranslation))
    }
    LaunchedEffect(templateKey, argsKey, appTranslation.enabled, appTranslation.languageCode) {
        display = when {
            !appTranslation.enabled || appTranslation.languageCode == "en" ->
                fillTranslationTemplate(templateKey, args)
            shouldSkipMachineTranslation(templateKey, appTranslation.languageCode) ->
                fillLocalizedTranslationTemplate(templateKey, args, appTranslation.languageCode)
            else -> {
                val translatedTemplate = translatePreservingPlaceholders(templateKey) { segment ->
                    appTranslation.translator.translate(segment)
                }
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
