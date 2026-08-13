package com.beardytop.beatzaddik.ui.translation

import com.beardytop.beatzaddik.domain.MitzModeFeatures

/** Languages with full offline bundled translations (no Google API needed). */
object BundledTranslationLanguages {
    private val AllBundledCodes: Set<String> = setOf("he", "es", "fr", "ru")

    val codes: Set<String>
        get() = if (MitzModeFeatures.bundledOfflineTranslationsEnabled) AllBundledCodes else emptySet()

    fun isBundled(languageCode: String): Boolean = languageCode in codes
}
