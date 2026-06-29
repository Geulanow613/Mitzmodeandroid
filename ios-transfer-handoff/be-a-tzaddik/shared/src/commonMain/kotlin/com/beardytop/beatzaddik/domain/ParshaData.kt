package com.beardytop.beatzaddik.domain

/**
 * Maps KosherJava Parsha enum names to display names and verified reader URLs.
 * Chabad links use current default_cdo parsha pages (not deprecated article_cdo aids).
 * Sefaria links use parsha title refs that resolve to the weekly Torah portion text.
 */
object ParshaData {

    data class ParshaInfo(
        val displayName: String,
        val chabadUrl: String,
        /** Sefaria ref segment (e.g. "Beha'alotcha" or "Deuteronomy.33.1-34.12"). */
        val sefariaRef: String,
    ) {
        val sefariaUrl: String
            get() = "https://www.sefaria.org/${sefariaRef.replace(" ", "%20").replace("'", "%27")}"
    }

    private val map: Map<String, ParshaInfo> = mapOf(
        "BERESHIS"            to ParshaInfo("Bereishit",          "https://www.chabad.org/parshah/default_cdo/aid/7781/jewish/Bereshit.htm",           "Bereishit"),
        "NOACH"               to ParshaInfo("Noach",              "https://www.chabad.org/parshah/default_cdo/aid/9168/jewish/Noach.htm",               "Noach"),
        "LECH_LECHA"          to ParshaInfo("Lech Lecha",         "https://www.chabad.org/parshah/default_cdo/aid/9169/jewish/Lech-Lecha.htm",          "Lech Lecha"),
        "VAYERA"              to ParshaInfo("Vayera",             "https://www.chabad.org/parshah/default_cdo/aid/9170/jewish/Vayera.htm",              "Vayera"),
        "CHAYEI_SARA"         to ParshaInfo("Chayei Sara",        "https://www.chabad.org/parshah/default_cdo/aid/9171/jewish/Chayei-Sarah.htm",        "Chayei Sara"),
        "TOLDOS"              to ParshaInfo("Toldot",             "https://www.chabad.org/parshah/default_cdo/aid/9172/jewish/Toldot.htm",              "Toldot"),
        "VAYETZEI"            to ParshaInfo("Vayetze",            "https://www.chabad.org/parshah/default_cdo/aid/9173/jewish/Vayetze.htm",              "Vayetze"),
        "VAYISHLACH"          to ParshaInfo("Vayishlach",         "https://www.chabad.org/parshah/default_cdo/aid/15554/jewish/Vayishlach.htm",         "Vayishlach"),
        "VAYESHEV"            to ParshaInfo("Vayeshev",           "https://www.chabad.org/parshah/default_cdo/aid/15555/jewish/Vayeshev.htm",           "Vayeshev"),
        "MIKETZ"              to ParshaInfo("Miketz",             "https://www.chabad.org/parshah/default_cdo/aid/15556/jewish/Miketz.htm",              "Miketz"),
        "VAYIGASH"            to ParshaInfo("Vayigash",           "https://www.chabad.org/parshah/default_cdo/aid/15557/jewish/Vayigash.htm",           "Vayigash"),
        "VAYECHI"             to ParshaInfo("Vayechi",            "https://www.chabad.org/parshah/default_cdo/aid/15558/jewish/Vayechi.htm",              "Vayechi"),
        "SHEMOS"              to ParshaInfo("Shemot",             "https://www.chabad.org/parshah/default_cdo/aid/15559/jewish/Shemot.htm",              "Shemot"),
        "VAERA"               to ParshaInfo("Va'era",             "https://www.chabad.org/parshah/default_cdo/aid/15560/jewish/Vaera.htm",              "Vaera"),
        "BO"                  to ParshaInfo("Bo",                 "https://www.chabad.org/parshah/default_cdo/aid/15561/jewish/Bo.htm",                  "Bo"),
        "BESHALACH"           to ParshaInfo("Beshalach",          "https://www.chabad.org/parshah/default_cdo/aid/15562/jewish/Beshalach.htm",           "Beshalach"),
        "YISRO"               to ParshaInfo("Yitro",              "https://www.chabad.org/parshah/default_cdo/aid/15563/jewish/Yitro.htm",              "Yitro"),
        "MISHPATIM"           to ParshaInfo("Mishpatim",          "https://www.chabad.org/parshah/default_cdo/aid/15564/jewish/Mishpatim.htm",          "Mishpatim"),
        "TERUMAH"             to ParshaInfo("Terumah",            "https://www.chabad.org/parshah/default_cdo/aid/15565/jewish/Terumah.htm",            "Terumah"),
        "TETZAVEH"            to ParshaInfo("Tetzaveh",           "https://www.chabad.org/parshah/default_cdo/aid/15566/jewish/Tetzaveh.htm",           "Tetzaveh"),
        "KI_SISA"             to ParshaInfo("Ki Tisa",            "https://www.chabad.org/parshah/default_cdo/aid/15567/jewish/Ki-Tisa.htm",            "Ki Tisa"),
        "VAYAKHEL"            to ParshaInfo("Vayakhel",           "https://www.chabad.org/parshah/default_cdo/aid/15568/jewish/Vayakhel.htm",           "Vayakhel"),
        "PEKUDEI"             to ParshaInfo("Pekudei",            "https://www.chabad.org/parshah/default_cdo/aid/15570/jewish/Pekudei.htm",            "Pekudei"),
        "VAYAKHEL_PEKUDEI"    to ParshaInfo("Vayakhel-Pekudei",   "https://www.chabad.org/parshah/default_cdo/aid/15569/jewish/Vayakhel-Pekudei.htm",   "Vayakhel-Pekudei"),
        "VAYIKRA"             to ParshaInfo("Vayikra",            "https://www.chabad.org/parshah/default_cdo/aid/15574/jewish/Vayikra.htm",              "Vayikra"),
        "TZAV"                to ParshaInfo("Tzav",               "https://www.chabad.org/parshah/default_cdo/aid/15575/jewish/Tzav.htm",               "Tzav"),
        "SHMINI"              to ParshaInfo("Shemini",            "https://www.chabad.org/parshah/default_cdo/aid/15576/jewish/Shemini.htm",            "Shemini"),
        "TAZRIA"              to ParshaInfo("Tazria",             "https://www.chabad.org/parshah/default_cdo/aid/15577/jewish/Tazria.htm",              "Tazria"),
        "METZORA"             to ParshaInfo("Metzora",            "https://www.chabad.org/parshah/default_cdo/aid/15579/jewish/Metzora.htm",             "Metzora"),
        "TAZRIA_METZORA"      to ParshaInfo("Tazria-Metzora",     "https://www.chabad.org/parshah/default_cdo/aid/15578/jewish/Tazria-Metzora.htm",     "Tazria-Metzora"),
        "ACHREI_MOS"          to ParshaInfo("Achrei Mot",         "https://www.chabad.org/parshah/default_cdo/aid/15580/jewish/Acharei-Mot.htm",        "Achrei Mot"),
        "KEDOSHIM"            to ParshaInfo("Kedoshim",           "https://www.chabad.org/parshah/default_cdo/aid/15582/jewish/Kedoshim.htm",           "Kedoshim"),
        "ACHREI_MOS_KEDOSHIM" to ParshaInfo("Achrei Mot-Kedoshim","https://www.chabad.org/parshah/default_cdo/aid/15581/jewish/Acharei-Kedoshim.htm",  "Achrei Mot-Kedoshim"),
        "EMOR"                to ParshaInfo("Emor",               "https://www.chabad.org/parshah/default_cdo/aid/15583/jewish/Emor.htm",               "Emor"),
        "BEHAR"               to ParshaInfo("Behar",              "https://www.chabad.org/parshah/default_cdo/aid/15584/jewish/Behar.htm",              "Behar"),
        "BECHUKOSAI"          to ParshaInfo("Bechukotai",         "https://www.chabad.org/parshah/default_cdo/aid/15586/jewish/Bechukotai.htm",         "Bechukotai"),
        "BEHAR_BECHUKOSAI"    to ParshaInfo("Behar-Bechukotai",   "https://www.chabad.org/parshah/default_cdo/aid/15585/jewish/Behar-Bechukotai.htm",   "Behar-Bechukotai"),
        "BAMIDBAR"            to ParshaInfo("Bamidbar",           "https://www.chabad.org/parshah/default_cdo/aid/36466/jewish/Bamidbar.htm",           "Bamidbar"),
        "NASSO"               to ParshaInfo("Nasso",              "https://www.chabad.org/parshah/default_cdo/aid/39589/jewish/Naso.htm",               "Nasso"),
        "BEHAALOSCHA"         to ParshaInfo("Beha'alotcha",       "https://www.chabad.org/parshah/default_cdo/aid/36744/jewish/Behaalotecha.htm",       "Beha'alotcha"),
        "SHLACH"              to ParshaInfo("Shelach",            "https://www.chabad.org/parshah/default_cdo/aid/45586/jewish/Shelach.htm",             "Shlach"),
        "KORACH"              to ParshaInfo("Korach",             "https://www.chabad.org/parshah/default_cdo/aid/45591/jewish/Korach.htm",             "Korach"),
        "CHUKAS"              to ParshaInfo("Chukat",             "https://www.chabad.org/parshah/default_cdo/aid/45612/jewish/Chukat.htm",             "Chukat"),
        "BALAK"               to ParshaInfo("Balak",              "https://www.chabad.org/parshah/default_cdo/aid/45614/jewish/Balak.htm",              "Balak"),
        "CHUKAS_BALAK"        to ParshaInfo("Chukat-Balak",       "https://www.chabad.org/parshah/default_cdo/aid/45613/jewish/Chukat-Balak.htm",       "Chukat-Balak"),
        "PINCHAS"             to ParshaInfo("Pinchas",            "https://www.chabad.org/parshah/default_cdo/aid/45615/jewish/Pinchas.htm",            "Pinchas"),
        "MATOS"               to ParshaInfo("Matot",              "https://www.chabad.org/parshah/default_cdo/aid/52598/jewish/Matot.htm",              "Matot"),
        "MASEI"               to ParshaInfo("Masei",              "https://www.chabad.org/parshah/default_cdo/aid/52600/jewish/Masei.htm",              "Masei"),
        "MATOS_MASEI"         to ParshaInfo("Matot-Masei",        "https://www.chabad.org/parshah/default_cdo/aid/52599/jewish/Matot-Masei.htm",        "Matot-Masei"),
        "DEVARIM"             to ParshaInfo("Devarim",            "https://www.chabad.org/parshah/default_cdo/aid/36232/jewish/Devarim.htm",              "Devarim"),
        "VAESCHANAN"          to ParshaInfo("Va'etchanan",        "https://www.chabad.org/parshah/default_cdo/aid/36233/jewish/Vaetchanan.htm",         "Vaetchanan"),
        "EIKEV"               to ParshaInfo("Eikev",              "https://www.chabad.org/parshah/default_cdo/aid/36234/jewish/Eikev.htm",              "Eikev"),
        "REEH"                to ParshaInfo("Re'eh",              "https://www.chabad.org/parshah/default_cdo/aid/36235/jewish/Reeh.htm",               "Re'eh"),
        "SHOFTIM"             to ParshaInfo("Shoftim",            "https://www.chabad.org/parshah/default_cdo/aid/36236/jewish/Shoftim.htm",           "Deuteronomy.16.18-21.9"),
        "KI_SEITZEI"          to ParshaInfo("Ki Teitzei",         "https://www.chabad.org/parshah/default_cdo/aid/36237/jewish/Ki-Teitzei.htm",         "Ki Teitzei"),
        "KI_SAVO"             to ParshaInfo("Ki Tavo",            "https://www.chabad.org/parshah/default_cdo/aid/36238/jewish/Ki-Tavo.htm",            "Ki Tavo"),
        "NITZAVIM"            to ParshaInfo("Nitzavim",           "https://www.chabad.org/parshah/default_cdo/aid/36239/jewish/Nitzavim.htm",           "Nitzavim"),
        "VAYEILECH"           to ParshaInfo("Vayelech",           "https://www.chabad.org/parshah/default_cdo/aid/36240/jewish/Vayelech.htm",           "Vayeilech"),
        "NITZAVIM_VAYEILECH"  to ParshaInfo("Nitzavim-Vayelech",  "https://www.chabad.org/parshah/default_cdo/aid/53151/jewish/Nitzavim-Vayelech.htm",  "Nitzavim-Vayelech"),
        "HAAZINU"             to ParshaInfo("Ha'azinu",           "https://www.chabad.org/parshah/default_cdo/aid/36241/jewish/Haazinu.htm",            "Haazinu"),
        "VZOS_HABERACHA"      to ParshaInfo("Vezot Habracha",     "https://www.chabad.org/parshah/default_cdo/aid/36242/jewish/VZot-HaBerachah.htm",    "Deuteronomy.33.1-34.12"),
    )

    /** Returns display info for a KosherJava parsha enum name, or null if unknown/no parsha. */
    fun forKey(kosherJavaName: String?): ParshaInfo? =
        if (kosherJavaName == null || kosherJavaName == "NONE") null
        else map[kosherJavaName]

    fun displayLabel(kosherJavaName: String?): String? =
        forKey(kosherJavaName)?.displayName
            ?: kosherJavaName?.takeIf { it.isNotBlank() && it != "NONE" }
                ?.replace('_', ' ')
                ?.trim()
}
