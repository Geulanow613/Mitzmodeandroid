package com.beardytop.mitzmode.ui.translation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.beardytop.beatzaddik.ui.translation.AppTextTranslator
import com.beardytop.beatzaddik.ui.translation.AppTranslationState
import com.beardytop.beatzaddik.ui.translation.LocalAppTranslation
import com.beardytop.mitzmode.viewmodel.TranslationViewModel

/** Shares MitzMode [TranslationViewModel] with the embedded Be a Tzaddik checklist (KMP UI). */
@Composable
fun ProvideAppTranslation(
    translationViewModel: TranslationViewModel,
    content: @Composable () -> Unit,
) {
    val enabled by translationViewModel.translationEnabled.collectAsState()
    val lang by translationViewModel.currentLanguage.collectAsState()
    val translator = AppTextTranslator { text -> translationViewModel.translateText(text) }
    CompositionLocalProvider(
        LocalAppTranslation provides AppTranslationState(
            enabled = enabled && lang != "en",
            languageCode = lang,
            translator = translator,
        ),
    ) {
        content()
    }
}
