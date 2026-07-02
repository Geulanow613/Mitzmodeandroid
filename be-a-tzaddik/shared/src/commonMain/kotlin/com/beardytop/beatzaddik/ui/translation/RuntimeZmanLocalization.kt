package com.beardytop.beatzaddik.ui.translation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.beardytop.beatzaddik.data.BundledTranslationsCatalog
import com.beardytop.beatzaddik.domain.ZmanimFormatter

/**
 * Parses domain-layer English zman summaries (with embedded clock times) into catalog
 * template keys so bundled translations can localize them offline.
 */
object RuntimeZmanLocalization {

    data class TemplatePart(val template: String, val args: Map<String, String>)

    private val segmentPatterns: List<Pair<Regex, (MatchResult) -> TemplatePart>> = listOf(
        Regex("""^Afternoon prayer \(Mincha\) (.+)$""") to { match ->
            TemplatePart("Afternoon prayer (Mincha) {when}", mapOf("when" to match.groupValues[1]))
        },
        Regex("""^Until shortly after sunset: (.+)$""") to { match ->
            TemplatePart("Until shortly after sunset: {time}", mapOf("time" to match.groupValues[1]))
        },
        Regex("""^Until nightfall: (.+)$""") to { match ->
            TemplatePart("Until nightfall: {time}", mapOf("time" to match.groupValues[1]))
        },
        Regex("""^Until nightfall$""") to { _ ->
            TemplatePart("Until nightfall", emptyMap())
        },
        Regex("""^Evening prayers available after (.+)$""") to { match ->
            TemplatePart("Evening prayers available after {time}", mapOf("time" to match.groupValues[1]))
        },
        Regex("""^Evening prayers available after sunset$""") to { _ ->
            TemplatePart("Evening prayers available after sunset", emptyMap())
        },
        Regex("""^Latest time for morning Shema: (.+)$""") to { match ->
            TemplatePart("Latest time for morning Shema: {time}", mapOf("time" to match.groupValues[1]))
        },
    )

    private val inlineZmanHint = Regex(
        """^(\d{1,2}:\d{2})(am|pm)(?:\s+(\w{3}))?$""",
        RegexOption.IGNORE_CASE,
    )

    fun parsePeriodSummary(summary: String): List<TemplatePart> =
        summary.split(" · ").map { segment ->
            val trimmed = segment.trim()
            segmentPatterns.firstNotNullOfOrNull { (regex, builder) ->
                regex.find(trimmed)?.let(builder)
            } ?: TemplatePart(trimmed, emptyMap())
        }

    fun localizeInlineZmanHint(raw: String, languageCode: String): String {
        val trimmed = raw.trim()
        if (trimmed.isBlank()) return trimmed
        inlineZmanHint.find(trimmed)?.let { match ->
            return formatInlineZmanClockWeekday(match, languageCode, embedForParagraph = true)
        }
        return localizeDotJoinedHint(trimmed, languageCode)
    }

    /** Clock + weekday only — for explicit RTL Row layout (no bidi isolates). */
    fun localizeInlineZmanHintForRow(raw: String, languageCode: String): String {
        val trimmed = raw.trim()
        if (trimmed.isBlank()) return trimmed
        inlineZmanHint.find(trimmed)?.let { match ->
            return formatInlineZmanClockWeekday(match, languageCode, embedForParagraph = false)
        }
        return localizeDotJoinedHint(trimmed, languageCode)
    }

    /** Full-string lookup, then segment-wise translation for overlay hints joined with " · ". */
    fun localizeDotJoinedHint(raw: String, languageCode: String): String {
        if (languageCode == "en" || raw.isBlank()) return raw
        BundledTranslationsCatalog.lookup(raw, languageCode)?.let { return it }
        if (" · " !in raw) {
            return BundledTranslationsCatalog.lookup(raw, languageCode) ?: raw
        }
        return raw.split(" · ").joinToString(" · ") { segment ->
            val trimmed = segment.trim()
            BundledTranslationsCatalog.lookup(trimmed, languageCode) ?: trimmed
        }
    }

    private fun formatInlineZmanClockWeekday(
        match: MatchResult,
        languageCode: String,
        embedForParagraph: Boolean,
    ): String {
        val clockRaw = "${match.groupValues[1]}${match.groupValues[2]}"
        val clock = ZmanimFormatter.reformatClockStringForLanguage(clockRaw, languageCode)
        val weekdayKey = match.groupValues.getOrNull(3)?.takeIf { it.isNotBlank() }
        val weekday = weekdayKey?.let { key ->
            BundledTranslationsCatalog.lookup(key, languageCode) ?: key
        }.orEmpty()
        val display = if (weekday.isNotBlank()) "$clock $weekday" else clock
        return if (embedForParagraph && ZmanimFormatter.uses24HourClock(languageCode)) {
            embedLtrForRtlMix(display)
        } else {
            display
        }
    }

    fun localizePeriodSummarySync(summary: String, languageCode: String): String {
        if (languageCode == "en" || summary.isBlank()) return summary
        return parsePeriodSummary(summary).joinToString(" · ") { part ->
            if (part.args.isEmpty()) {
                resolveBundledTranslationSync(part.template, languageCode)
            } else {
                resolveBundledTemplateSync(part.template, part.args, languageCode)
            }
        }
    }
}

fun resolveBundledTranslationSync(text: String, languageCode: String): String {
    if (languageCode == "en" || text.isBlank()) return text
    if (shouldSkipMachineTranslation(text, languageCode)) return text
    return BundledTranslationsCatalog.lookup(text, languageCode) ?: text
}

fun resolveBundledTemplateSync(
    templateKey: String,
    args: Map<String, String>,
    languageCode: String,
): String {
    if (languageCode == "en") return fillTranslationTemplate(templateKey, localizeExplainerTemplateArgs(args, languageCode))
    val template = BundledTranslationsCatalog.lookup(templateKey, languageCode) ?: templateKey
    return fillLocalizedTranslationTemplate(template, localizeExplainerTemplateArgs(args, languageCode), languageCode)
}

/** Resolves nested catalog keys in template args, then RTL-specific arg formatting. */
fun localizeExplainerTemplateArgs(
    args: Map<String, String>,
    languageCode: String,
): Map<String, String> {
    val bundled = args.mapValues { (_, value) ->
        if (languageCode == "en") value
        else BundledTranslationsCatalog.lookup(value, languageCode) ?: value
    }
    val nested = bundled.mapValues { (_, value) ->
        if ('$' in value) {
            fillLocalizedTranslationTemplate(value, bundled, languageCode)
        } else {
            value
        }
    }
    return localizeTemplateArgsForRtl(nested, languageCode)
}

@Composable
fun rememberLocalizedZmanPeriodHint(summary: String): String {
    val appTranslation = LocalAppTranslation.current
    val displayLang = appTranslation.displayLanguageCode
    var display by remember(summary, appTranslation.enabled, appTranslation.languageCode) {
        mutableStateOf(
            if (displayLang == "en") summary
            else RuntimeZmanLocalization.localizePeriodSummarySync(summary, displayLang),
        )
    }
    LaunchedEffect(summary, appTranslation.enabled, appTranslation.languageCode) {
        display = when {
            !appTranslation.enabled || appTranslation.languageCode == "en" -> summary
            else -> {
                val localized = RuntimeZmanLocalization.localizePeriodSummarySync(
                    summary,
                    appTranslation.displayLanguageCode,
                )
                if (localized != summary) {
                    localized
                } else {
                    appTranslation.translator.translate(summary)
                }
            }
        }
    }
    return display
}

@Composable
fun rememberLocalizedZmanInlineHint(rawHint: String): String {
    val appTranslation = LocalAppTranslation.current
    val displayLang = appTranslation.displayLanguageCode
    var display by remember(rawHint, appTranslation.enabled, appTranslation.languageCode) {
        mutableStateOf(
            if (displayLang == "en") rawHint
            else RuntimeZmanLocalization.localizeInlineZmanHint(rawHint, displayLang),
        )
    }
    LaunchedEffect(rawHint, appTranslation.enabled, appTranslation.languageCode) {
        display = when {
            !appTranslation.enabled || appTranslation.languageCode == "en" -> rawHint
            else -> RuntimeZmanLocalization.localizeInlineZmanHint(
                rawHint,
                appTranslation.displayLanguageCode,
            )
        }
    }
    return display
}
