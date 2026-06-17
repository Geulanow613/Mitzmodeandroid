package com.beardytop.beatzaddik.domain

/**
 * When Birkat Ha'Ilanot is recited — follows local blossoming season by hemisphere.
 * Northern Hemisphere: Nissan (Shulchan Arukh O.C. 226:1).
 * Southern Hemisphere: Elul–Tishrei when local spring blooms (Yalkut Yosef and mainstream poskim).
 */
object BirkatHaIlanotRules {

    fun isSouthernHemisphere(latitude: Double?): Boolean =
        latitude != null && latitude < 0.0

    fun isInWindow(cal: DayInfo, latitude: Double?): Boolean {
        val month = cal.hebrewMonth ?: return false
        return if (isSouthernHemisphere(latitude)) {
            month == HebrewCalendarEngine.ELUL || month == HebrewCalendarEngine.TISHREI
        } else {
            month == HebrewCalendarEngine.NISSAN
        }
    }

    fun windowLabel(latitude: Double?): String =
        if (isSouthernHemisphere(latitude)) {
            "when fruit trees blossom in your local spring (Elul–Tishrei)"
        } else {
            "when fruit trees blossom this Nissan"
        }

    fun seasonName(latitude: Double?): String =
        if (isSouthernHemisphere(latitude)) "Elul–Tishrei" else "Nissan"

    /** Short subtext under the checklist row when the item is visible. */
    fun listSubtextHint(latitude: Double?): String =
        when {
            latitude == null ->
                "Set your location in Settings — the app shows the likely season by hemisphere; recite only when you see fruit trees in bloom."
            isSouthernHemisphere(latitude) ->
                "Likely season in the Southern Hemisphere (Elul–Tishrei) — say the blessing when you actually see fruit trees blossoming."
            else ->
                "Likely season in the Northern Hemisphere (Nissan) — say the blessing when you actually see fruit trees blossoming."
        }
}
