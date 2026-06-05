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
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beardytop.beatzaddik.domain.ChecklistLink
import com.beardytop.beatzaddik.ui.theme.TzaddikColors

private val MarkdownLinkRegex = Regex("""\[([^\]]+)\]\(([^)]+)\)""")

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
    zmanMakeupNote: String? = null,
    situational: Boolean = false,
    learnMoreUrl: String? = null,
    learnMoreLabel: String? = null,
    resourceLinks: List<ChecklistLink> = emptyList(),
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current

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
                text = "Situational — required when this situation applies, not necessarily every day.",
                containerColor = TzaddikColors.NavyMid.copy(alpha = 0.12f),
                textColor = TzaddikColors.NavyMid
            )
            Spacer(Modifier.height(10.dp))
        }
        zmanHint?.let { hint ->
            InfoBadge(
                text = hint,
                containerColor = TzaddikColors.GoldBorder.copy(alpha = 0.14f),
                textColor = TzaddikColors.GoldDeep
            )
            Spacer(Modifier.height(10.dp))
        }

        // Main explanation — structured rendering (markdown + fallback link labels)
        RichExplanationText(explanation, knownLinks = resourceLinks)

        // Makeup / teshuvah note
        zmanMakeupNote?.let { makeup ->
            Spacer(Modifier.height(14.dp))
            GoldFlourishDivider(widthFraction = 0.45f)
            Spacer(Modifier.height(10.dp))
            Text(
                "Makeup prayer",
                style = MaterialTheme.typography.titleSmall,
                color = TzaddikColors.NavyDeep,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                makeup,
                color = TzaddikColors.TextBrown,
                style = MaterialTheme.typography.bodySmall
            )
        }

        val linkedInExplanation = MarkdownLinkRegex.findAll(explanation)
            .map { it.groupValues[2].trim().lowercase() }
            .toSet()
        val extraResourceLinks = resourceLinks.filter { link ->
            val urlKey = link.url.trim().lowercase()
            urlKey !in linkedInExplanation &&
                (learnMoreUrl == null || !link.url.equals(learnMoreUrl, ignoreCase = true))
        }
        if (extraResourceLinks.isNotEmpty()) {
            Spacer(Modifier.height(14.dp))
            SectionHeading("Resources")
            Spacer(Modifier.height(6.dp))
            extraResourceLinks.forEach { link ->
                TappableLinkLine(label = link.displayText, url = link.url, uriHandler = uriHandler)
                Spacer(Modifier.height(4.dp))
            }
        }

        learnMoreUrl?.let { url ->
            if (extraResourceLinks.isEmpty()) {
                Spacer(Modifier.height(14.dp))
            }
            TappableLinkLine(
                label = learnMoreLabel ?: "Further reading",
                url = url,
                uriHandler = uriHandler,
                prefix = "↗ "
            )
        }
    }
}

@Composable
private fun TappableLinkLine(
    label: String,
    url: String,
    uriHandler: androidx.compose.ui.platform.UriHandler,
    prefix: String = ""
) {
    val linkText = buildAnnotatedString {
        pushStringAnnotation(tag = "URL", annotation = url)
        withStyle(
            SpanStyle(
                color = TzaddikColors.NavyMid,
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.Medium
            )
        ) {
            append("$prefix$label")
        }
        pop()
    }
    ClickableText(
        text = linkText,
        style = MaterialTheme.typography.bodySmall,
        onClick = { offset ->
            linkText.getStringAnnotations("URL", offset, offset).firstOrNull()
                ?.let { uriHandler.openUri(it.item) }
        }
    )
}

@Composable
private fun LinkableBodyText(
    text: String,
    style: androidx.compose.ui.text.TextStyle,
    modifier: Modifier = Modifier,
    knownLinks: List<ChecklistLink> = emptyList()
) {
    val uriHandler = LocalUriHandler.current
    val annotated = when {
        text.contains("](") -> buildMarkdownLinkAnnotatedString(text)
        else -> buildPlainTextLinkAnnotatedString(text, knownLinks)
    }
    if (annotated.getStringAnnotations("URL", 0, annotated.length).isEmpty()) {
        Text(text, style = style, color = TzaddikColors.TextBrown, modifier = modifier)
        return
    }
    ClickableText(
        text = annotated,
        style = style.copy(color = TzaddikColors.TextBrown),
        modifier = modifier,
        onClick = { offset ->
            annotated.getStringAnnotations("URL", offset, offset).firstOrNull()
                ?.let { uriHandler.openUri(it.item) }
        }
    )
}

private fun AnnotatedString.Builder.appendLinkedSpan(label: String, url: String) {
    pushStringAnnotation(tag = "URL", annotation = url)
    withStyle(
        SpanStyle(
            color = TzaddikColors.NavyMid,
            textDecoration = TextDecoration.Underline,
            fontWeight = FontWeight.Medium
        )
    ) {
        append(label)
    }
    pop()
}

private fun buildMarkdownLinkAnnotatedString(text: String) = buildAnnotatedString {
    var cursor = 0
    MarkdownLinkRegex.findAll(text).forEach { match ->
        if (match.range.first > cursor) {
            append(text.substring(cursor, match.range.first))
        }
        appendLinkedSpan(match.groupValues[1], match.groupValues[2].trim())
        cursor = match.range.last + 1
    }
    if (cursor < text.length) {
        append(text.substring(cursor))
    }
}

/** When JSON still has plain site names, match labels from the item's verified links array. */
private fun buildPlainTextLinkAnnotatedString(
    text: String,
    knownLinks: List<ChecklistLink>
): androidx.compose.ui.text.AnnotatedString {
    val patterns = knownLinks
        .flatMap { link ->
            val label = link.displayText.trim()
            val host = runCatching {
                val withoutScheme = link.url.removePrefix("https://").removePrefix("http://")
                withoutScheme.substringBefore('/').removePrefix("www.")
            }.getOrNull()
            buildList {
                if (label.isNotEmpty()) add(label to link.url)
                if (!host.isNullOrBlank()) {
                    add(host to link.url)
                    add("www.$host" to link.url)
                }
            }
        }
        .distinctBy { it.first.lowercase() }
        .sortedByDescending { it.first.length }
    if (patterns.isEmpty()) {
        return buildAnnotatedString { append(text) }
    }
    return buildAnnotatedString {
        var cursor = 0
        while (cursor < text.length) {
            val match = patterns
                .asSequence()
                .mapNotNull { (label, url) ->
                    val idx = text.indexOf(label, cursor, ignoreCase = true)
                    if (idx < 0) null else Triple(idx, label, url)
                }
                .minByOrNull { it.first }
            if (match == null) {
                append(text.substring(cursor))
                break
            }
            val (start, label, url) = match
            if (start > cursor) append(text.substring(cursor, start))
            appendLinkedSpan(text.substring(start, start + label.length), url)
            cursor = start + label.length
        }
    }
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
            text,
            style = MaterialTheme.typography.bodySmall,
            color = textColor,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun RichExplanationText(
    text: String,
    knownLinks: List<ChecklistLink> = emptyList()
) {
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
                    GoldBulletRow(line.removePrefix("• ").trim(), knownLinks = knownLinks)
                }
                lIdx == 0 && lines.size > 1 && !line.startsWith("\"") && !isBulletOrNumber(line) -> {
                    // First line of multi-line paragraph — slight emphasis
                    LinkableBodyText(
                        text = line,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            lineHeight = 22.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        knownLinks = knownLinks
                    )
                }
                isNumberedItem(line) -> {
                    NumberedRow(line, knownLinks = knownLinks)
                }
                line.startsWith("\"") -> {
                    Text(
                        line.trim('"'),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontStyle = FontStyle.Italic,
                            lineHeight = 22.sp
                        ),
                        color = TzaddikColors.TextMuted
                    )
                }
                else -> {
                    LinkableBodyText(
                        text = line,
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                        knownLinks = knownLinks
                    )
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
            title,
            style = MaterialTheme.typography.titleSmall,
            color = TzaddikColors.NavyDeep,
            fontWeight = FontWeight.Bold
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
private fun GoldBulletRow(text: String, knownLinks: List<ChecklistLink> = emptyList()) {
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
            knownLinks = knownLinks
        )
    }
}

@Composable
private fun NumberedRow(line: String, knownLinks: List<ChecklistLink> = emptyList()) {
    val dotIndex = line.indexOf('.')
    if (dotIndex < 0) {
        Text(
            line,
            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 21.sp),
            color = TzaddikColors.TextBrown
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
                style = MaterialTheme.typography.labelSmall,
                color = TzaddikColors.GoldDeep,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.width(8.dp))
        LinkableBodyText(
            text = body,
            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 21.sp),
            modifier = Modifier.weight(1f),
            knownLinks = knownLinks
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
