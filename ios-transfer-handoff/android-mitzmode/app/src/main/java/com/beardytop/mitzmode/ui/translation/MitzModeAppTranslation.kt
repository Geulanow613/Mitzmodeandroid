package com.beardytop.mitzmode.ui.translation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.beardytop.beatzaddik.ui.translation.AppTextTranslator
import com.beardytop.beatzaddik.ui.translation.AppTranslationState
import com.beardytop.beatzaddik.ui.translation.LanguageSelectorState
import com.beardytop.beatzaddik.ui.translation.LocalAppTranslation
import com.beardytop.beatzaddik.ui.translation.LocalLanguageSelector

/**
 * English-only bridge for the embedded Be a Tzaddik checklist.
 * Multilingual UI lives in the standalone Be a Tzaddik app (see `app/archived-translations/`).
 */
@Composable
fun ProvideAppTranslation(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalAppTranslation provides AppTranslationState(
            enabled = false,
            languageCode = "en",
            translator = AppTextTranslator { text -> text },
        ),
        LocalLanguageSelector provides LanguageSelectorState(
            enabled = false,
            currentLanguageCode = "en",
            availableLanguages = emptyList(),
            onLanguageChange = {},
            onEnabledChange = {},
        ),
    ) {
        content()
    }
}
