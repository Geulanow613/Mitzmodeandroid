package com.beardytop.mitzmode.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.hilt.navigation.compose.hiltViewModel
import com.beardytop.mitzmode.viewmodel.TranslationViewModel

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
    translationViewModel: TranslationViewModel = hiltViewModel()
) {
    val translationEnabled by translationViewModel.translationEnabled.collectAsState()
    val currentLanguage by translationViewModel.currentLanguage.collectAsState()
    
    var translatedText by remember(text, currentLanguage) { mutableStateOf(text) }
    
    LaunchedEffect(text, currentLanguage, translationEnabled) {
        if (translationEnabled && currentLanguage != "en") {
            translatedText = translationViewModel.translateText(text)
        } else {
            translatedText = text
        }
    }
    
    Text(
        text = translatedText,
        modifier = modifier,
        style = style,
        color = color,
        fontSize = fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign,
        maxLines = maxLines
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
    translationViewModel: TranslationViewModel = hiltViewModel()
) {
    val translationEnabled by translationViewModel.translationEnabled.collectAsState()
    val currentLanguage by translationViewModel.currentLanguage.collectAsState()
    val isTranslating by translationViewModel.isTranslating.collectAsState()
    
    var translatedText by remember(text, currentLanguage) { mutableStateOf(text) }
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
    
    Text(
        text = if (isLoading) loadingText else translatedText,
        modifier = modifier,
        style = style,
        color = color,
        fontSize = fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign,
        maxLines = maxLines
    )
} 