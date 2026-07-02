package com.beardytop.beatzaddik.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.beardytop.beatzaddik.domain.ChecklistLink
import com.beardytop.beatzaddik.domain.HalachicTerm
import com.beardytop.beatzaddik.domain.HalachicTermsDictionary
import com.beardytop.beatzaddik.ui.screens.ShabbatGuideData
import com.beardytop.beatzaddik.ui.screens.ShabbatGuideScreen
import com.beardytop.beatzaddik.ui.theme.TzaddikColors
import com.beardytop.beatzaddik.ui.translation.LocalAppTranslation
import com.beardytop.beatzaddik.ui.translation.resolveBundledTranslationSync
import com.beardytop.beatzaddik.ui.translation.shouldSkipMachineTranslation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

val LocalHalachicTermExtras = compositionLocalOf { emptyList<HalachicTerm>() }

/** When false, suppresses glossary underlines and popup definitions (e.g. guide rows open full pages). */
val LocalHalachicTermsEnabled = compositionLocalOf { true }

/** When set (e.g. on Today), glossary taps for guide-linked terms open the Shabbat guide. */
val LocalOpenShabbatGuide = compositionLocalOf<((String?) -> Unit)?> { null }

/** Tracks term ids already underlined on the current page; first occurrence only. */
val LocalHalachicTermsUsedOnPage = compositionLocalOf<MutableSet<String>?> { null }

/** Term text keeps body color; gold underline is drawn separately in [drawHalachicTermUnderlines]. */
private fun halachicTermSpanStyle(bodyColor: Color): SpanStyle = SpanStyle(
    color = bodyColor,
    fontWeight = FontWeight.Normal,
)

internal fun halachicTermUnderlineColor(bodyColor: Color): Color = when (bodyColor) {
    TzaddikColors.TextBrown, TzaddikColors.TextMuted ->
        TzaddikColors.GoldDeep.copy(alpha = 0.72f)
    TzaddikColors.ParchTop, TzaddikColors.ParchMid ->
        TzaddikColors.GoldBright.copy(alpha = 0.78f)
    else -> TzaddikColors.GoldBorder.copy(alpha = 0.68f)
}

/** Glossary labels are English; underlines are skipped when the UI is translated (see below). */
private fun hasGlossaryTerms(
    text: String,
    enableTerms: Boolean,
    additionalTerms: List<HalachicTerm>,
): Boolean =
    enableTerms && HalachicTermsDictionary.findMatches(text, additionalTerms = additionalTerms).isNotEmpty()

internal const val GUIDE_TERM_TAG = "guide_term"

internal fun DrawScope.drawHalachicTermUnderlines(
    layoutResult: TextLayoutResult,
    annotated: AnnotatedString,
    underlineColor: Color,
    strokeWidthPx: Float,
    underlineOffsetPx: Float,
    annotationTag: String = HalachicTermsDictionary.annotationTag(),
) {
    for (range in annotated.getStringAnnotations(annotationTag, 0, annotated.length)) {
        val start = range.start
        val end = range.end
        if (start >= end) continue

        var lineTop: Float? = null
        var lineLeft = 0f
        var lineRight = 0f
        var lineBottom = 0f

        fun flushLine() {
            if (lineTop == null) return
            val y = lineBottom + underlineOffsetPx
            drawLine(
                color = underlineColor,
                start = Offset(lineLeft, y),
                end = Offset(lineRight, y),
                strokeWidth = strokeWidthPx,
            )
        }

        for (index in start until end) {
            val box = layoutResult.getBoundingBox(index)
            if (lineTop == null || kotlin.math.abs(box.top - lineTop!!) > 0.5f) {
                flushLine()
                lineTop = box.top
                lineLeft = box.left
                lineRight = box.right
                lineBottom = box.bottom
            } else {
                lineLeft = minOf(lineLeft, box.left)
                lineRight = maxOf(lineRight, box.right)
                lineBottom = maxOf(lineBottom, box.bottom)
            }
        }
        flushLine()
    }
}

/**
 * Host for halachic term definition popups — wrap app content inside [TzaddikTheme].
 */
@Composable
fun HalachicTermOverlay(
    content: @Composable () -> Unit,
) {
    var selected by remember { mutableStateOf<HalachicTerm?>(null) }
    var showShabbatGuide by remember { mutableStateOf(false) }
    var shabbatGuideAnchor by remember { mutableStateOf<String?>(null) }
    val openShabbatGuide: (String?) -> Unit = { anchor ->
        shabbatGuideAnchor = anchor
        showShabbatGuide = true
    }
    CompositionLocalProvider(
        LocalShowHalachicTerm provides { term -> selected = term },
        LocalOpenShabbatGuide provides openShabbatGuide,
    ) {
        content()
        HalachicTermDefinitionDialog(
            term = selected,
            onDismiss = { selected = null },
        )
        if (showShabbatGuide) {
            Dialog(
                onDismissRequest = { showShabbatGuide = false },
                properties = DialogProperties(usePlatformDefaultWidth = false),
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .navigationBarsPadding()
                ) {
                    ShabbatGuideScreen(
                        initialAnchor = shabbatGuideAnchor,
                        onDismiss = { showShabbatGuide = false },
                    )
                }
            }
        }
    }
}

val LocalShowHalachicTerm = compositionLocalOf<(HalachicTerm) -> Unit> {
    { /* no-op when overlay not mounted */ }
}

@Composable
fun HalachicTermDefinitionDialog(
    term: HalachicTerm?,
    onDismiss: () -> Unit,
) {
    if (term == null) return
    val openShabbatGuide = LocalOpenShabbatGuide.current
    val guideAnchor = ShabbatGuideData.anchorForTerm(term)
    val appTranslation = LocalAppTranslation.current
    var dialogTitle by remember(term.id) { mutableStateOf(term.title) }
    var dialogDefinition by remember(term.id) { mutableStateOf(term.definition) }
    var dialogLiteral by remember(term.id) { mutableStateOf(term.literal) }

    LaunchedEffect(term, appTranslation.enabled, appTranslation.languageCode) {
        dialogTitle = term.title
        dialogDefinition = term.definition
        dialogLiteral = term.literal
        if (!appTranslation.enabled || appTranslation.languageCode == "en") return@LaunchedEffect
        if (!shouldSkipMachineTranslation(term.title, appTranslation.languageCode)) {
            dialogTitle = appTranslation.translator.translate(term.title)
        }
        if (!shouldSkipMachineTranslation(term.definition, appTranslation.languageCode)) {
            dialogDefinition = appTranslation.translator.translate(term.definition)
        }
        term.literal?.let { lit ->
            if (!shouldSkipMachineTranslation(lit, appTranslation.languageCode)) {
                dialogLiteral = appTranslation.translator.translate(lit)
            }
        }
    }

    ParchmentDialog(
        onDismiss = onDismiss,
        title = dialogTitle,
    ) {
        dialogLiteral?.let { lit ->
            AppText(
                text = lit,
                style = MaterialTheme.typography.labelMedium,
                color = TzaddikColors.TextMuted,
                enableTerms = false,
                modifier = Modifier.padding(bottom = 6.dp),
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 320.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            AppText(
                text = dialogDefinition,
                style = MaterialTheme.typography.bodyMedium,
                enableTerms = false,
            )
        }
        if (guideAnchor != null && openShabbatGuide != null) {
            TextButton(
                onClick = {
                    onDismiss()
                    openShabbatGuide(guideAnchor)
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                AppText(
                    "More in Shabbat Guide",
                    color = TzaddikColors.NavyMid,
                    enableTerms = false,
                )
            }
        }
        TextButton(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth(),
        ) {
            AppText("Close", color = TzaddikColors.GoldDeep, enableTerms = false)
        }
    }
}

/**
 * User-facing copy: optional Google Translate ([LocalAppTranslation]) plus halachic term underline.
 */
@Composable
fun AppText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = TzaddikColors.TextBrown,
    knownLinks: List<ChecklistLink> = emptyList(),
    enableTerms: Boolean = true,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
) {
    HalachicClickableText(
        text = text,
        style = style,
        modifier = modifier,
        color = color,
        knownLinks = knownLinks,
        enableTerms = enableTerms,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow,
    )
}

@Composable
private fun SimpleTranslatedText(
    text: String,
    style: TextStyle,
    color: Color,
    modifier: Modifier,
    maxLines: Int,
    overflow: TextOverflow = TextOverflow.Clip,
) {
    val appTranslation = LocalAppTranslation.current
    var displayText by remember(text, appTranslation.enabled, appTranslation.languageCode) {
        mutableStateOf(
            when {
                !appTranslation.enabled || appTranslation.languageCode == "en" -> text
                else -> resolveBundledTranslationSync(text, appTranslation.languageCode)
            },
        )
    }
    LaunchedEffect(text, appTranslation.enabled, appTranslation.languageCode) {
        displayText = when {
            !appTranslation.enabled || appTranslation.languageCode == "en" -> text
            shouldSkipMachineTranslation(text, appTranslation.languageCode) -> text
            else -> appTranslation.translator.translate(text)
        }
    }
    Text(
        text = displayText,
        style = style,
        color = color,
        modifier = modifier,
        maxLines = maxLines,
        overflow = overflow,
    )
}

@Composable
fun HalachicClickableText(
    text: String,
    style: TextStyle,
    modifier: Modifier = Modifier,
    color: Color = TzaddikColors.TextBrown,
    knownLinks: List<ChecklistLink> = emptyList(),
    enableTerms: Boolean = true,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
) {
    val showTerm = LocalShowHalachicTerm.current
    val extras = LocalHalachicTermExtras.current
    val usedOnPage = LocalHalachicTermsUsedOnPage.current
    val appTranslation = LocalAppTranslation.current
    val uriHandler = LocalUriHandler.current
    val termsEnabled = enableTerms && LocalHalachicTermsEnabled.current
    val resolvedStyle = if (textAlign != null) style.copy(textAlign = textAlign) else style

    if (appTranslation.enabled && appTranslation.languageCode != "en") {
        SimpleTranslatedText(
            text = text,
            style = resolvedStyle,
            color = color,
            modifier = modifier,
            maxLines = maxLines,
            overflow = overflow,
        )
        return
    }

    if (!termsEnabled && knownLinks.isEmpty() && !text.contains("](")) {
        SimpleTranslatedText(
            text = text,
            style = resolvedStyle,
            color = color,
            modifier = modifier,
            maxLines = maxLines,
            overflow = overflow,
        )
        return
    }

    if (!termsEnabled) {
        val annotated = remember(text, knownLinks, color) {
            buildBodyAnnotatedString(
                text = text,
                knownLinks = knownLinks,
                enableTerms = false,
                additionalTerms = extras,
                bodyColor = color,
            )
        }
        val hasUrls = annotated.getStringAnnotations("URL", 0, annotated.length).isNotEmpty()
        if (!hasUrls) {
            SimpleTranslatedText(
                text = text,
                style = resolvedStyle,
                color = color,
                modifier = modifier,
                maxLines = maxLines,
                overflow = overflow,
            )
            return
        }
        ClickableText(
            text = annotated,
            style = resolvedStyle.copy(color = color),
            modifier = modifier,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
            onClick = { offset ->
                annotated.getStringAnnotations("URL", offset, offset)
                    .firstOrNull()
                    ?.item
                    ?.let { uriHandler.openUri(it) }
            },
        )
        return
    }

    var displayText by remember(text) { mutableStateOf(text) }
    var annotated by remember(text, knownLinks, termsEnabled, extras, color) {
        mutableStateOf<AnnotatedString?>(null)
    }

    LaunchedEffect(text, knownLinks, termsEnabled, extras, color, usedOnPage) {
        HalachicAnnotationCache.get(text, knownLinks, termsEnabled, extras, color)?.let {
            annotated = it
            return@LaunchedEffect
        }
        val localUsed = usedOnPage?.toMutableSet()
        val built = withContext(Dispatchers.Default) {
            buildBodyAnnotatedString(
                text = text,
                knownLinks = knownLinks,
                enableTerms = termsEnabled,
                additionalTerms = extras,
                bodyColor = color,
                usedOnPage = localUsed,
            )
        }
        HalachicAnnotationCache.put(text, knownLinks, termsEnabled, extras, color, built)
        if (usedOnPage != null && localUsed != null) {
            usedOnPage.addAll(localUsed)
        }
        annotated = built
    }

    LaunchedEffect(text, appTranslation.enabled, appTranslation.languageCode) {
        displayText = when {
            !appTranslation.enabled || appTranslation.languageCode == "en" -> text
            shouldSkipMachineTranslation(text, appTranslation.languageCode) -> text
            else -> appTranslation.translator.translate(text)
        }
    }

    // Annotate from English source so glossary labels match on every device/locale.
    val resolvedAnnotated = annotated
    if (resolvedAnnotated == null) {
        Text(
            text = displayText,
            style = resolvedStyle,
            color = color,
            modifier = modifier,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
        )
        return
    }
    val hasTerms = resolvedAnnotated.getStringAnnotations(HalachicTermsDictionary.annotationTag(), 0, resolvedAnnotated.length).isNotEmpty()
    val hasUrls = resolvedAnnotated.getStringAnnotations("URL", 0, resolvedAnnotated.length).isNotEmpty()
    val density = LocalDensity.current
    val underlineColor = halachicTermUnderlineColor(color)
    val underlineStrokePx = with(density) { 0.75.dp.toPx() }
    val underlineOffsetPx = with(density) { 2.dp.toPx() }
    var textLayout by remember(resolvedAnnotated) { mutableStateOf<TextLayoutResult?>(null) }

    if (!hasTerms && !hasUrls) {
        Text(
            text = displayText,
            style = resolvedStyle,
            color = color,
            modifier = modifier,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
        )
        return
    }

    // When the UI is translated the annotation offsets (built from English source) no longer
    // correspond to the translated text, so we show the translated plain text and skip
    // term-underlines and URL taps for this render pass.
    if (appTranslation.enabled && appTranslation.languageCode != "en") {
        Text(
            text = displayText,
            style = resolvedStyle,
            color = color,
            modifier = modifier,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
        )
        return
    }

    val visibleText = if (hasTerms || hasUrls) resolvedAnnotated else AnnotatedString(displayText)

    fun handleTap(offset: Int) {
        val term = resolvedAnnotated.getStringAnnotations(HalachicTermsDictionary.annotationTag(), offset, offset)
            .firstOrNull()
            ?.item
            ?.let { id -> HalachicTermsDictionary.termById(id) ?: HalachicGuideTerms.termById(id) }
        if (term != null) {
            showTerm(term)
            return
        }
        resolvedAnnotated.getStringAnnotations("URL", offset, offset)
            .firstOrNull()
            ?.item
            ?.let { uriHandler.openUri(it) }
    }

    // ClickableText handles taps reliably inside vertically scrolling parents (e.g. mitzvah info).
    ClickableText(
        text = visibleText,
        style = resolvedStyle.copy(color = color),
        modifier = modifier.drawWithContent {
            drawContent()
            if (hasTerms) {
                textLayout?.let { layout ->
                    drawHalachicTermUnderlines(
                        layoutResult = layout,
                        annotated = resolvedAnnotated,
                        underlineColor = underlineColor,
                        strokeWidthPx = underlineStrokePx,
                        underlineOffsetPx = underlineOffsetPx,
                    )
                }
            }
        },
        onTextLayout = { textLayout = it },
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        onClick = ::handleTap,
    )
}

internal fun buildBodyAnnotatedString(
    text: String,
    knownLinks: List<ChecklistLink> = emptyList(),
    enableTerms: Boolean = true,
    additionalTerms: List<HalachicTerm> = emptyList(),
    bodyColor: Color = TzaddikColors.TextBrown,
    usedOnPage: MutableSet<String>? = null,
): AnnotatedString {
    if (text.contains("](")) {
        return buildMarkdownWithTerms(text, enableTerms, additionalTerms, bodyColor, usedOnPage)
    }
    return buildPlainBodyAnnotatedString(text, knownLinks, enableTerms, additionalTerms, bodyColor, usedOnPage)
}

private fun buildMarkdownWithTerms(
    text: String,
    enableTerms: Boolean,
    additionalTerms: List<HalachicTerm>,
    bodyColor: Color,
    usedOnPage: MutableSet<String>?,
): AnnotatedString {
    val markdownLinkRegex = Regex("""\[([^\]]+)\]\(([^)]+)\)""")
    val protected = mutableListOf<IntRange>()
    return buildAnnotatedString {
        var cursor = 0
        markdownLinkRegex.findAll(text).forEach { match ->
            val plain = text.substring(cursor, match.range.first)
            appendPlainWithTerms(plain, enableTerms, additionalTerms, protected, bodyColor, usedOnPage)
            protected.add(match.range)
            pushStringAnnotation(tag = "URL", annotation = match.groupValues[2].trim())
            withStyle(linkSpanStyle(bodyColor)) {
                append(match.groupValues[1])
            }
            pop()
            cursor = match.range.last + 1
        }
        if (cursor < text.length) {
            appendPlainWithTerms(text.substring(cursor), enableTerms, additionalTerms, protected, bodyColor, usedOnPage)
        }
    }
}

private fun linkSpanStyle(bodyColor: Color): SpanStyle = SpanStyle(
    color = bodyColor,
    textDecoration = TextDecoration.Underline,
    fontWeight = FontWeight.Medium,
)

private fun buildPlainBodyAnnotatedString(
    text: String,
    knownLinks: List<ChecklistLink>,
    enableTerms: Boolean,
    additionalTerms: List<HalachicTerm>,
    bodyColor: Color,
    usedOnPage: MutableSet<String>?,
): AnnotatedString {
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
        return buildAnnotatedString {
            appendPlainWithTerms(text, enableTerms, additionalTerms, emptyList(), bodyColor, usedOnPage)
        }
    }
    return buildAnnotatedString {
        var cursor = 0
        val protected = mutableListOf<IntRange>()
        while (cursor < text.length) {
            val match = patterns
                .asSequence()
                .mapNotNull { (label, url) ->
                    val idx = text.indexOf(label, cursor, ignoreCase = true)
                    if (idx < 0) null else Triple(idx, label, url)
                }
                .minByOrNull { it.first }
            if (match == null) {
                appendPlainWithTerms(text.substring(cursor), enableTerms, additionalTerms, protected, bodyColor, usedOnPage)
                break
            }
            val (start, label, url) = match
            if (start > cursor) {
                appendPlainWithTerms(text.substring(cursor, start), enableTerms, additionalTerms, protected, bodyColor, usedOnPage)
            }
            val end = start + label.length
            protected.add(start until end)
            pushStringAnnotation(tag = "URL", annotation = url)
            withStyle(linkSpanStyle(bodyColor)) {
                append(text.substring(start, end))
            }
            pop()
            cursor = end
        }
    }
}

private fun AnnotatedString.Builder.appendPlainWithTerms(
    plain: String,
    enableTerms: Boolean,
    additionalTerms: List<HalachicTerm>,
    protected: List<IntRange>,
    bodyColor: Color,
    usedOnPage: MutableSet<String>? = null,
) {
    if (plain.isEmpty()) return
    if (!enableTerms) {
        append(plain)
        return
    }
    var matches = HalachicTermsDictionary.findMatches(plain, protected, additionalTerms)
    if (usedOnPage != null) {
        matches = matches.filter { match ->
            if (match.term.id in usedOnPage) false
            else {
                usedOnPage.add(match.term.id)
                true
            }
        }
    }
    var pos = 0
    for (match in matches) {
        if (match.start > pos) append(plain.substring(pos, match.start))
        pushStringAnnotation(HalachicTermsDictionary.annotationTag(), match.term.id)
        withStyle(halachicTermSpanStyle(bodyColor)) {
            append(plain.substring(match.start, match.end))
        }
        pop()
        pos = match.end
    }
    if (pos < plain.length) append(plain.substring(pos))
}

private object HalachicAnnotationCache {
    private const val MAX_ENTRIES = 64
    private val cache = object : LinkedHashMap<String, AnnotatedString>(MAX_ENTRIES, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, AnnotatedString>?) =
            size > MAX_ENTRIES
    }

    private fun key(
        text: String,
        knownLinks: List<ChecklistLink>,
        enableTerms: Boolean,
        extras: List<HalachicTerm>,
        bodyColor: Color,
    ): String = buildString {
        append(enableTerms)
        append('|')
        append(bodyColor.value)
        append('|')
        append(extras.joinToString { it.id })
        append('|')
        append(knownLinks.joinToString { it.url })
        append('|')
        append(text)
    }

    fun get(
        text: String,
        knownLinks: List<ChecklistLink>,
        enableTerms: Boolean,
        extras: List<HalachicTerm>,
        bodyColor: Color,
    ): AnnotatedString? = cache[key(text, knownLinks, enableTerms, extras, bodyColor)]

    fun put(
        text: String,
        knownLinks: List<ChecklistLink>,
        enableTerms: Boolean,
        extras: List<HalachicTerm>,
        bodyColor: Color,
        annotated: AnnotatedString,
    ) {
        cache[key(text, knownLinks, enableTerms, extras, bodyColor)] = annotated
    }
}
