package com.beardytop.beatzaddik.ui.translation

import com.beardytop.beatzaddik.data.BundledTranslationsCatalog

/**
 * Resolves copy for bundled languages from offline assets first.
 * [apiTranslate] is used only for non-bundled languages (Google Translate on Android).
 *
 * For bundled languages (he/es/fr/ru), strings not found in the offline catalog are
 * returned as-is (English). Dynamic runtime strings — countdown timers, location labels,
 * assembled zman hints with embedded clock times — cannot be pre-bundled and intentionally
 * stay in English rather than making live API calls that would cost money and drain battery.
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
        // Static strings are all in the bundle. Anything that reaches here is a dynamic
        // runtime string (embedded times, countdown values, location names). Return English
        // rather than firing an API call — doing so for every checklist tick would cost
        // real API money and cause rapid-fire network requests.
        return text
    }

    return apiTranslate(text)
}
