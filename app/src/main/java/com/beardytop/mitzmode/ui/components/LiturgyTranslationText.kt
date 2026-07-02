package com.beardytop.mitzmode.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.beardytop.beatzaddik.data.BundledTranslationsCatalog
import com.beardytop.mitzmode.ui.LocalTranslationViewModel
import com.beardytop.mitzmode.viewmodel.TranslationViewModel

/**
 * Prayer liturgy translation line: English source from opensiddur-style data, with bundled
 * es/fr/ru overrides from [prayers_liturgy.json]. Uses English when UI language is English
 * or when the global translation toggle is off.
 */
@Composable
fun LiturgyTranslationText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    textAlign: TextAlign? = TextAlign.Start,
    color: Color = MaterialTheme.colorScheme.onSurface,
) {
    val translationViewModel: TranslationViewModel =
        LocalTranslationViewModel.current ?: hiltViewModel()
    val language by translationViewModel.currentLanguage.collectAsState()
    val translationEnabled by translationViewModel.translationEnabled.collectAsState()
    val liturgyLanguage = remember(language, translationEnabled) {
        effectiveLiturgyTranslationLanguage(language, translationEnabled)
    }
    var display by remember(text, liturgyLanguage) {
        mutableStateOf(resolveLiturgyTranslationSync(text, liturgyLanguage))
    }
    LaunchedEffect(text, liturgyLanguage) {
        display = when (liturgyLanguage) {
            "en", "he" -> text
            else -> translationViewModel.translateTextToLanguage(text, liturgyLanguage)
        }
    }
    Text(
        text = display,
        style = style,
        modifier = modifier,
        textAlign = textAlign,
        color = color,
    )
}

internal fun effectiveLiturgyTranslationLanguage(
    currentLanguage: String,
    translationEnabled: Boolean,
): String = when {
    currentLanguage == "he" -> "he"
    currentLanguage == "en" || !translationEnabled -> "en"
    else -> currentLanguage
}

private fun resolveLiturgyTranslationSync(text: String, language: String): String {
    if (language == "en" || language == "he" || text.isBlank()) return text
    return BundledTranslationsCatalog.lookup(text, language) ?: text
}
