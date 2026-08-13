package com.beardytop.mitzmode.data

/**
 * Maps the phone's configured system language (Settings → Languages) to an offline app language.
 * Country/region is ignored — only the language tag matters (e.g. es-MX → es).
 */
object DeviceAppLanguage {

    /** Bundled native UI languages; anything else starts in English. */
    val NATIVE_START_LANGUAGES = setOf("he", "es", "fr", "ru")

    data class BootstrapChoice(
        val languageCode: String,
        val translationEnabled: Boolean,
    )

    /**
     * Walks the phone's language preference list in order and picks the first supported match.
     * Falls back to English when no he/es/fr/ru (or explicit en) is found.
     */
    fun resolveFromDeviceLanguageTags(tags: List<String>): BootstrapChoice {
        for (tag in tags) {
            matchBundledLanguage(tag)?.let { code ->
                return BootstrapChoice(
                    languageCode = code,
                    translationEnabled = code != "en",
                )
            }
            if (tag.equals("en", ignoreCase = true)) {
                return BootstrapChoice(languageCode = "en", translationEnabled = false)
            }
        }
        return BootstrapChoice(languageCode = "en", translationEnabled = false)
    }

    /** Maps a device language tag to a bundled app language, or null if unsupported. */
    fun matchBundledLanguage(deviceLanguage: String?): String? {
        if (deviceLanguage.isNullOrBlank()) return null
        val language = deviceLanguage.lowercase().substringBefore('-').substringBefore('_')
        return when (language) {
            "he", "iw" -> "he" // "iw" = legacy Hebrew code on some Android builds
            "es" -> "es"
            "fr" -> "fr"
            "ru" -> "ru"
            else -> null
        }
    }
}
