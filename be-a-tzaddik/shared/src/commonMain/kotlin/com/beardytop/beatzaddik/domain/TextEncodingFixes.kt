package com.beardytop.beatzaddik.domain

/**
 * Repairs common UTF-8 punctuation mojibake that appears when checklist/translation
 * JSON was saved with the wrong encoding.
 *
 * Checklist titles also normalize fancy dashes to ASCII '-' so they cannot resurface
 * as garbage in the UI if a file is re-corrupted.
 */
object TextEncodingFixes {

    // Code-point built strings so this source file cannot itself become mojibake.
    private val REPLACEMENTS: List<Pair<String, String>> = listOf(
        // â + € + ”  -> em dash
        ("\u00E2\u20AC\u201D") to "\u2014",
        // â + € + –  -> en dash (common double-encoded en dash)
        ("\u00E2\u20AC\u2013") to "\u2013",
        // â + € + ¢  -> bullet
        ("\u00E2\u20AC\u00A2") to "\u2022",
        // â + € + ¦  -> ellipsis
        ("\u00E2\u20AC\u00A6") to "\u2026",
        // â + € + ™  -> right single quote
        ("\u00E2\u20AC\u2122") to "\u2019",
    )

    fun repairMojibake(text: String): String {
        if (text.isEmpty()) return text
        var out = text
        for ((bad, good) in REPLACEMENTS) {
            if (out.contains(bad)) out = out.replace(bad, good)
        }
        return out
    }

    /** Repair mojibake, then force em/en dashes to ASCII hyphen (safe for titles). */
    fun sanitizeDisplayTitle(text: String): String {
        var out = repairMojibake(text)
        out = out.replace('\u2014', '-').replace('\u2013', '-')
        return out
    }

    fun repairJsonPayload(json: String): String = repairMojibake(json)
}
