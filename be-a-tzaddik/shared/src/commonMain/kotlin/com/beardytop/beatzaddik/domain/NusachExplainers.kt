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
            """Origin: Historically, "Sephardi" refers to the liturgy of Iberian (Spanish and Portuguese) Jews. However, in many modern prayer apps and contexts, Nusach Sefard (with a "d") specifically refers to a hybrid liturgy adopted by Eastern European Hasidim.

Characteristics: It blends the basic structure of Nusach Ashkenaz with the mystical, Kabbalistic concepts of the Arizal (Rabbi Isaac Luria)."""

        NusachSelection.EDOT_HAMIZRACH ->
            """Origin: The Middle East and North Africa (e.g., Iraq, Yemen, Morocco, Syria, Iran).

Characteristics: Meaning "Communities of the East," this nusach is deeply rooted in the traditions of the Geonim (Babylonian sages) and the rulings of the Shulchan Aruch. In many modern contexts and prayer books, "Sephardi" and "Edot HaMizrach" are grouped together because their textual structures are very similar, heavily influenced by Kabbalah."""

        NusachSelection.CHABAD ->
            """Origin: Formalized by Rabbi Shneur Zalman of Liadi (the Alter Rebbe), the founder of the Chabad-Lubavitch Hasidic movement.

Characteristics: He meticulously reviewed dozens of different prayer book manuscripts to arrange a liturgy that perfectly aligns with both revealed Torah law (Halakha) and the precise Kabbalistic intentions of the Arizal."""

        NusachSelection.NOT_SURE -> null
    }
}
