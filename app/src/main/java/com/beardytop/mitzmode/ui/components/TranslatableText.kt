package com.beardytop.mitzmode.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.hilt.navigation.compose.hiltViewModel
import com.beardytop.beatzaddik.domain.ChecklistLink
import com.beardytop.beatzaddik.ui.components.HalachicClickableText
import com.beardytop.beatzaddik.ui.translation.resolveBundledTranslationSync
import com.beardytop.mitzmode.data.MitzvahLink
import com.beardytop.mitzmode.ui.LocalTranslationViewModel
import com.beardytop.mitzmode.viewmodel.TranslationViewModel

private fun initialBundledUiText(text: String, language: String, enabled: Boolean): String {
    if (!enabled || language == "en") return text
    return resolveBundledTranslationSync(text, language)
}

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
    translationViewModel: TranslationViewModel =
        LocalTranslationViewModel.current ?: hiltViewModel(),
) {
    val translationEnabled by translationViewModel.translationEnabled.collectAsState()
    val currentLanguage by translationViewModel.currentLanguage.collectAsState()

    var translatedText by remember(text, currentLanguage, translationEnabled) {
        mutableStateOf(initialBundledUiText(text, currentLanguage, translationEnabled))
    }

    LaunchedEffect(text, currentLanguage, translationEnabled) {
        translatedText = if (translationEnabled && currentLanguage != "en") {
            translationViewModel.translateText(text)
        } else {
            text
        }
    }

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
        text = translatedText,
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
    translationViewModel: TranslationViewModel =
        LocalTranslationViewModel.current ?: hiltViewModel(),
) {
    val translationEnabled by translationViewModel.translationEnabled.collectAsState()
    val currentLanguage by translationViewModel.currentLanguage.collectAsState()

    var translatedText by remember(text, currentLanguage, translationEnabled) {
        mutableStateOf(initialBundledUiText(text, currentLanguage, translationEnabled))
    }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(text, currentLanguage, translationEnabled) {
        if (translationEnabled && currentLanguage != "en") {
            isLoading = true
            translatedText = translationViewModel.translateText(text)
            isLoading = false
        } else {
            translatedText = text
            isLoading = false
        }
    }

    val display = if (isLoading) loadingText else translatedText
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
        text = display,
        style = resolvedStyle,
        color = resolvedColor,
        modifier = modifier,
        textAlign = textAlign,
        maxLines = maxLines,
        knownLinks = if (isLoading) emptyList() else checklistLinks,
        enableTerms = enableHalachicTerms && !isLoading,
    )
}
