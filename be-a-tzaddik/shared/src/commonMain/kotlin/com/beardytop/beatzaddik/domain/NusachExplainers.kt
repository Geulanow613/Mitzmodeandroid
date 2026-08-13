package com.beardytop.beatzaddik.domain

/**
 * Short explainers for Settings nusach choices (tap the ? — not glossary underlines on the chip).
 */
object NusachExplainers {
    fun forSelection(selection: NusachSelection): String? = when (selection) {
        NusachSelection.ASHKENAZ ->
            """Origin: Developed by Jewish communities in Central and Eastern Europe (e.g., Germany, Poland, Russia).

Characteristics: It relies heavily on early European rabbinic traditions and medieval liturgical poems (piyyutim). It generally has distinct variations between Western German (Frankfurt) and Eastern European (Polin) traditions."""

        NusachSelection.SEFARD ->
            """Origin: The liturgy and customs of Iberian (Spanish and Portuguese) Jewry and related Mediterranean communities, often called Sephardi / Bet Yosef.

Characteristics: Halachic guidance in this app for this setting follows the Shulchan Aruch and common Sephardi poskim (such as Rav Ovadia Yosef) where noted — not Chassidic "Nusach Sefard," which is a different Eastern European Hasidic rite."""

        NusachSelection.EDOT_HAMIZRACH ->
            """Origin: The Middle East and North Africa (e.g., Iraq, Yemen, Morocco, Syria, Iran).

Characteristics: Meaning "Communities of the East," this nusach is deeply rooted in the traditions of the Geonim (Babylonian sages) and the rulings of the Shulchan Aruch. In many modern contexts and prayer books, "Sephardi" and "Edot HaMizrach" are grouped together because their textual structures are very similar, heavily influenced by Kabbalah."""

        NusachSelection.CHABAD ->
            """Origin: Formalized by Rabbi Shneur Zalman of Liadi (the Alter Rebbe), the founder of the Chabad-Lubavitch Hasidic movement.

Characteristics: He meticulously reviewed dozens of different prayer book manuscripts to arrange a liturgy that perfectly aligns with both revealed Torah law (Halakha) and the precise Kabbalistic intentions of the Arizal."""

        NusachSelection.OTHER ->
            """Use this if your rite is not Ashkenaz, Sephardi, Edot HaMizrach, or Chabad — for example Yemenite Baladi, Italian, Romaniote, or another local minhag.

The app will show general, widely shared laws and practices, and will not apply the specific custom sets of those four options. Follow your kehilla and rav for prayer text and minhag details."""

        NusachSelection.NOT_SURE ->
            """Choose this if you do not yet know your family's prayer tradition.

The app will show general, widely shared laws and practices — the same approach as "Other" — and will not apply Ashkenaz, Sephardi, Edot HaMizrach, or Chabad-specific custom sets. Update your nusach in Settings when you know which minhag to follow."""
    }
}
