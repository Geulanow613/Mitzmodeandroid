package com.beardytop.beatzaddik.data

/**
 * Maps the phone's configured system language (Settings → Languages) to an offline app language.
 */
object DeviceAppLanguage {

    val NATIVE_START_LANGUAGES = setOf("he", "es", "fr", "ru")

    data class BootstrapChoice(
        val languageCode: String,
        val translationEnabled: Boolean,
    )

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

    fun matchBundledLanguage(deviceLanguage: String?): String? {
        if (deviceLanguage.isNullOrBlank()) return null
        val language = deviceLanguage.lowercase().substringBefore('-').substringBefore('_')
        return when (language) {
            "he", "iw" -> "he"
            "es" -> "es"
            "fr" -> "fr"
            "ru" -> "ru"
            else -> null
        }
    }
}
