package com.beardytop.beatzaddik.ui.translation

import com.beardytop.beatzaddik.data.BundledTranslationsCatalog

/**
 * Resolves copy for bundled languages from offline assets first.
 * [apiTranslate] is used only for non-bundled languages (Google Translate on Android).
 */
suspend fun resolveAppTranslation(
    text: String,
    languageCode: String,
    apiTranslate: suspend (String) -> String,
): String {
    if (languageCode == "en" || text.isBlank()) return text
    if (shouldSkipMachineTranslation(text, languageCode)) return text

    BundledTranslationsCatalog.lookup(text, languageCode)?.let { return it }

    if (BundledTranslationLanguages.isBundled(languageCode)) {
        // Bundled language but missing entry — keep English rather than calling API.
        return text
    }

    return apiTranslate(text)
}
