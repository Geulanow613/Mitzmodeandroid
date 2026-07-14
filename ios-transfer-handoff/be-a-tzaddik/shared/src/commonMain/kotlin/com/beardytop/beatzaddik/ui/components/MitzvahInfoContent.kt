package com.beardytop.beatzaddik.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beardytop.beatzaddik.domain.ChecklistLink
import com.beardytop.beatzaddik.ui.theme.TzaddikColors
import com.beardytop.beatzaddik.ui.translation.rememberAppTranslatedTemplate
import com.beardytop.beatzaddik.ui.translation.rememberAppTranslatedText

/**
 * Richly renders a structured explanation string.
 *
 * Supported syntax in JSON explanation text:
 *   "Heading:"          — short line ending in colon → bold navy serif header
 *   "• bullet"          — gold diamond bullet point
 *   "1. item"           — numbered list with styled numbers
 *   "\"quoted text\""   — rendered in italic style
 *   blank line (\n\n)   — paragraph break with vertical space
 *   [label](https://…)  — tappable link (verified URLs in JSON)
 *   normal text         — regular body text
 *
 * A Hebrew/Aramaic glossary is checked for any terms in parentheses
 * so definitions surface inline without cluttering the main text.
 */
@Composable
fun MitzvahExplanationContent(
    explanation: String,
    zmanHint: String? = null,
    zmanHintTemplate: String? = null,
    zmanHintArgs: Map<String, String> = emptyMap(),
    zmanMakeupNote: String? = null,
    zmanMakeupTemplate: String? = null,
    zmanMakeupArgs: Map<String, String> = emptyMap(),
    explanationTemplate: String? = null,
    explanationArgs: Map<String, String> = emptyMap(),
    situational: Boolean = false,
    knownLinks: List<ChecklistLink> = emptyList(),
    modifier: Modifier = Modifier
) {
    val translatedExplanation = if (explanationTemplate != null) {
        rememberAppTranslatedTemplate(explanationTemplate, explanationArgs)
    } else {
        rememberAppTranslatedText(explanation)
    }
    val translatedHint = when {
        zmanHintTemplate != null -> rememberAppTranslatedTemplate(zmanHintTemplate, zmanHintArgs)
        zmanHint != null -> rememberAppTranslatedText(zmanHint)
        else -> null
    }
    val translatedMakeup = when {
        zmanMakeupTemplate != null -> rememberAppTranslatedTemplate(zmanMakeupTemplate, zmanMakeupArgs)
        zmanMakeupNote != null -> rememberAppTranslatedText(zmanMakeupNote)
        else -> null
    }
    val situationalBadge = rememberAppTranslatedText(
        "Situational — required when this situation applies, not necessarily every day."
    )
    val makeupHeading = rememberAppTranslatedText("Makeup prayer")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 460.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        // Status badges above explanation
        if (situational) {
            InfoBadge(
                text = situationalBadge,
                containerColor = TzaddikColors.NavyMid.copy(alpha = 0.12f),
                textColor = TzaddikColors.NavyMid
            )
            Spacer(Modifier.height(10.dp))
        }
        translatedHint?.let { hint ->
            InfoBadge(
                text = hint,
                containerColor = TzaddikColors.GoldBorder.copy(alpha = 0.14f),
                textColor = TzaddikColors.GoldDeep
            )
            Spacer(Modifier.height(10.dp))
        }

        // Translate the full explainer body once, then split for layout (not for lookup).
        RichExplanationText(translatedExplanation, knownLinks)

        // Makeup / teshuvah note
        translatedMakeup?.let { makeup ->
            Spacer(Modifier.height(14.dp))
            GoldFlourishDivider(widthFraction = 0.45f)
            Spacer(Modifier.height(10.dp))
            Text(
                text = makeupHeading,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = TzaddikColors.NavyDeep,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = makeup,
                style = MaterialTheme.typography.bodySmall,
                color = TzaddikColors.TextBrown,
            )
        }
    }
}

@Composable
private fun LinkableBodyText(
    text: String,
    style: androidx.compose.ui.text.TextStyle,
    modifier: Modifier = Modifier,
    knownLinks: List<ChecklistLink> = emptyList(),
) {
    HalachicClickableText(
        text = text,
        style = style,
        modifier = modifier,
        color = TzaddikColors.TextBrown,
        knownLinks = knownLinks,
        enableTerms = true,
    )
}

@Composable
private fun InfoBadge(text: String, containerColor: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(containerColor)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            color = textColor,
        )
    }
}

@Composable
private fun RichExplanationText(
    text: String,
    knownLinks: List<ChecklistLink> = emptyList(),
) {
    // One glossary underline per term across the whole explainer (not once per line).
    HalachicTermsPage(key = text) {
        val paragraphs = text.split("\n\n")
        paragraphs.forEachIndexed { pIdx, para ->
            if (pIdx > 0) Spacer(Modifier.height(10.dp))
            val lines = para.split("\n")
            lines.forEachIndexed { lIdx, rawLine ->
                val line = rawLine.trim()
                if (line.isEmpty()) {
                    Spacer(Modifier.height(4.dp))
                    return@forEachIndexed
                }
                when {
                    isHeading(line) -> {
                        if (lIdx > 0) Spacer(Modifier.height(6.dp))
                        SectionHeading(line.trimEnd(':'))
                        Spacer(Modifier.height(4.dp))
                    }
                    line.startsWith("• ") -> {
                        GoldBulletRow(line.removePrefix("• ").trim(), knownLinks)
                    }
                    lIdx == 0 && lines.size > 1 && !line.startsWith("\"") && !isBulletOrNumber(line) -> {
                        // First line of multi-line paragraph — slight emphasis
                        LinkableBodyText(
                            text = line,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                lineHeight = 22.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            knownLinks = knownLinks,
                        )
                    }
                    isNumberedItem(line) -> {
                        NumberedRow(line)
                    }
                    line.startsWith("\"") -> {
                        Text(
                            text = line.trim('"'),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontStyle = FontStyle.Italic,
                                lineHeight = 22.sp
                            ),
                            color = TzaddikColors.TextMuted,
                        )
                    }
                    else -> {
                        LinkableBodyText(
                            text = line,
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                            knownLinks = knownLinks,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeading(title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .rotate(45f)
                .background(TzaddikColors.GoldBright, RoundedCornerShape(1.dp))
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = TzaddikColors.NavyDeep,
        )
    }
    Box(
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .padding(start = 14.dp, top = 3.dp)
            .height(1.dp)
            .background(
                Brush.horizontalGradient(
                    listOf(TzaddikColors.GoldBorder.copy(alpha = 0.55f), Color.Transparent)
                )
            )
    )
}

@Composable
private fun GoldBulletRow(
    text: String,
    knownLinks: List<ChecklistLink> = emptyList(),
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 7.dp)
                .size(5.dp)
                .rotate(45f)
                .background(TzaddikColors.GoldBorder, RoundedCornerShape(1.dp))
        )
        Spacer(Modifier.width(10.dp))
        LinkableBodyText(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 21.sp),
            modifier = Modifier.weight(1f),
            knownLinks = knownLinks,
        )
    }
}

@Composable
private fun NumberedRow(line: String) {
    val dotIndex = line.indexOf('.')
    if (dotIndex < 0) {
        Text(
            text = line,
            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 21.sp),
            color = TzaddikColors.TextBrown,
        )
        return
    }
    val num = line.substring(0, dotIndex).trim()
    val body = line.substring(dotIndex + 1).trim()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(TzaddikColors.GoldBorder.copy(alpha = 0.18f))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                num,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = TzaddikColors.GoldDeep
            )
        }
        Spacer(Modifier.width(8.dp))
        LinkableBodyText(
            text = body,
            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 21.sp),
            modifier = Modifier.weight(1f),
        )
    }
}

private fun isHeading(line: String): Boolean {
    if (!line.endsWith(":")) return false
    val withoutColon = line.trimEnd(':')
    return withoutColon.length < 60 && !withoutColon.contains(". ")
}

private fun isBulletOrNumber(line: String): Boolean =
    line.startsWith("• ") || isNumberedItem(line)

private fun isNumberedItem(line: String): Boolean =
    line.length > 2 && line[0].isDigit() && line.getOrNull(1) == '.'
