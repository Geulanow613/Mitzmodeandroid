package com.beardytop.mitzmode.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import com.beardytop.beatzaddik.domain.ChecklistLink
import com.beardytop.beatzaddik.ui.components.HalachicClickableText
import com.beardytop.mitzmode.data.MitzvahLink

/** Mitz Mode is English-only; renders source text with halachic term links. */
@Composable
fun TranslatableText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    knownLinks: List<MitzvahLink> = emptyList(),
    enableHalachicTerms: Boolean = true,
) {
    val resolvedStyle = style.copy(
        fontSize = if (fontSize != TextUnit.Unspecified) fontSize else style.fontSize,
        fontWeight = fontWeight ?: style.fontWeight,
        textAlign = textAlign ?: style.textAlign,
    )
    val resolvedColor = if (color != Color.Unspecified) color else style.color
    val checklistLinks = remember(knownLinks) {
        knownLinks.map { ChecklistLink(displayText = it.displayText, url = it.url) }
    }

    HalachicClickableText(
        text = text,
        style = resolvedStyle,
        color = resolvedColor,
        modifier = modifier,
        textAlign = textAlign,
        maxLines = maxLines,
        knownLinks = checklistLinks,
        enableTerms = enableHalachicTerms,
    )
}

@Composable
fun TranslatableTextWithLoading(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    loadingText: String = "Translating...",
    knownLinks: List<MitzvahLink> = emptyList(),
    enableHalachicTerms: Boolean = true,
) {
    TranslatableText(
        text = text,
        modifier = modifier,
        style = style,
        color = color,
        fontSize = fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign,
        maxLines = maxLines,
        knownLinks = knownLinks,
        enableHalachicTerms = enableHalachicTerms,
    )
}
