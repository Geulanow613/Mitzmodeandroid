package com.beardytop.beatzaddik.ui.translation

/** Languages with full offline bundled translations (no Google API needed). */
object BundledTranslationLanguages {
    val codes: Set<String> = setOf("he", "es", "fr", "ru")

    fun isBundled(languageCode: String): Boolean = languageCode in codes
}
