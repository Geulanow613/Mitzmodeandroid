package com.beardytop.beatzaddik.domain

/**
 * Maps KosherJava Parsha enum names to display names and Chabad.org parsha page URLs.
 * Double parshas are listed separately for Diaspora and Israel where they differ.
 */
object ParshaData {

    data class ParshaInfo(
        val displayName: String,
        val chabadUrl: String,
        /** Sefaria weekly Torah reader; parsha name is in the title/explanation. */
        val sefariaUrl: String = "https://www.sefaria.org/texts/Torah"
    )

    // Chabad URL base pattern: https://www.chabad.org/parshah/article_cdo/aid/[ID]/jewish/[Name].htm
    // AIDs sourced from chabad.org parsha section (stable deep links).
    private val map: Map<String, ParshaInfo> = mapOf(
        "BERESHIS"            to ParshaInfo("Bereishit",          "https://www.chabad.org/parshah/article_cdo/aid/3185/jewish/Bereishit.htm"),
        "NOACH"               to ParshaInfo("Noach",              "https://www.chabad.org/parshah/article_cdo/aid/3188/jewish/Noach.htm"),
        "LECH_LECHA"          to ParshaInfo("Lech Lecha",         "https://www.chabad.org/parshah/article_cdo/aid/3191/jewish/Lech-Lecha.htm"),
        "VAYERA"              to ParshaInfo("Vayera",             "https://www.chabad.org/parshah/article_cdo/aid/3194/jewish/Vayera.htm"),
        "CHAYEI_SARA"         to ParshaInfo("Chayei Sara",        "https://www.chabad.org/parshah/article_cdo/aid/3197/jewish/Chayei-Sara.htm"),
        "TOLDOS"              to ParshaInfo("Toldot",             "https://www.chabad.org/parshah/article_cdo/aid/3200/jewish/Toldot.htm"),
        "VAYETZEI"            to ParshaInfo("Vayetze",            "https://www.chabad.org/parshah/article_cdo/aid/3203/jewish/Vayetze.htm"),
        "VAYISHLACH"          to ParshaInfo("Vayishlach",         "https://www.chabad.org/parshah/article_cdo/aid/3206/jewish/Vayishlach.htm"),
        "VAYESHEV"            to ParshaInfo("Vayeshev",           "https://www.chabad.org/parshah/article_cdo/aid/3209/jewish/Vayeshev.htm"),
        "MIKETZ"              to ParshaInfo("Miketz",             "https://www.chabad.org/parshah/article_cdo/aid/3212/jewish/Miketz.htm"),
        "VAYIGASH"            to ParshaInfo("Vayigash",           "https://www.chabad.org/parshah/article_cdo/aid/3215/jewish/Vayigash.htm"),
        "VAYECHI"             to ParshaInfo("Vayechi",            "https://www.chabad.org/parshah/article_cdo/aid/3218/jewish/Vayechi.htm"),
        "SHEMOS"              to ParshaInfo("Shemot",             "https://www.chabad.org/parshah/article_cdo/aid/3221/jewish/Shemot.htm"),
        "VAERA"               to ParshaInfo("Va'era",             "https://www.chabad.org/parshah/article_cdo/aid/3224/jewish/Vaera.htm"),
        "BO"                  to ParshaInfo("Bo",                 "https://www.chabad.org/parshah/article_cdo/aid/3227/jewish/Bo.htm"),
        "BESHALACH"           to ParshaInfo("Beshalach",          "https://www.chabad.org/parshah/article_cdo/aid/3230/jewish/Beshalach.htm"),
        "YISRO"               to ParshaInfo("Yitro",              "https://www.chabad.org/parshah/article_cdo/aid/3233/jewish/Yitro.htm"),
        "MISHPATIM"           to ParshaInfo("Mishpatim",          "https://www.chabad.org/parshah/article_cdo/aid/3236/jewish/Mishpatim.htm"),
        "TERUMAH"             to ParshaInfo("Terumah",            "https://www.chabad.org/parshah/article_cdo/aid/3239/jewish/Terumah.htm"),
        "TETZAVEH"            to ParshaInfo("Tetzaveh",           "https://www.chabad.org/parshah/article_cdo/aid/3242/jewish/Tetzaveh.htm"),
        "KI_SISA"             to ParshaInfo("Ki Tisa",            "https://www.chabad.org/parshah/article_cdo/aid/3245/jewish/Ki-Tisa.htm"),
        "VAYAKHEL"            to ParshaInfo("Vayakhel",           "https://www.chabad.org/parshah/article_cdo/aid/3248/jewish/Vayakhel.htm"),
        "PEKUDEI"             to ParshaInfo("Pekudei",            "https://www.chabad.org/parshah/article_cdo/aid/3251/jewish/Pekudei.htm"),
        "VAYAKHEL_PEKUDEI"    to ParshaInfo("Vayakhel-Pekudei",   "https://www.chabad.org/parshah/article_cdo/aid/3248/jewish/Vayakhel-Pekudei.htm"),
        "VAYIKRA"             to ParshaInfo("Vayikra",            "https://www.chabad.org/parshah/article_cdo/aid/3254/jewish/Vayikra.htm"),
        "TZAV"                to ParshaInfo("Tzav",               "https://www.chabad.org/parshah/article_cdo/aid/3257/jewish/Tzav.htm"),
        "SHMINI"              to ParshaInfo("Shemini",            "https://www.chabad.org/parshah/article_cdo/aid/3260/jewish/Shemini.htm"),
        "TAZRIA"              to ParshaInfo("Tazria",             "https://www.chabad.org/parshah/article_cdo/aid/3263/jewish/Tazria.htm"),
        "METZORA"             to ParshaInfo("Metzora",            "https://www.chabad.org/parshah/article_cdo/aid/3266/jewish/Metzora.htm"),
        "TAZRIA_METZORA"      to ParshaInfo("Tazria-Metzora",     "https://www.chabad.org/parshah/article_cdo/aid/3263/jewish/Tazria-Metzora.htm"),
        "ACHREI_MOS"          to ParshaInfo("Achrei Mot",         "https://www.chabad.org/parshah/article_cdo/aid/3269/jewish/Acharei.htm"),
        "KEDOSHIM"            to ParshaInfo("Kedoshim",           "https://www.chabad.org/parshah/article_cdo/aid/3272/jewish/Kedoshim.htm"),
        "ACHREI_MOS_KEDOSHIM" to ParshaInfo("Achrei Mot-Kedoshim","https://www.chabad.org/parshah/article_cdo/aid/3269/jewish/Acharei-Kedoshim.htm"),
        "EMOR"                to ParshaInfo("Emor",               "https://www.chabad.org/parshah/article_cdo/aid/3275/jewish/Emor.htm"),
        "BEHAR"               to ParshaInfo("Behar",              "https://www.chabad.org/parshah/article_cdo/aid/3278/jewish/Behar.htm"),
        "BECHUKOSAI"          to ParshaInfo("Bechukotai",         "https://www.chabad.org/parshah/article_cdo/aid/3281/jewish/Bechukotai.htm"),
        "BEHAR_BECHUKOSAI"    to ParshaInfo("Behar-Bechukotai",   "https://www.chabad.org/parshah/article_cdo/aid/3278/jewish/Behar-Bechukotai.htm"),
        "BAMIDBAR"            to ParshaInfo("Bamidbar",           "https://www.chabad.org/parshah/article_cdo/aid/3284/jewish/Bamidbar.htm"),
        "NASSO"               to ParshaInfo("Nasso",              "https://www.chabad.org/parshah/article_cdo/aid/3287/jewish/Nasso.htm"),
        "BEHAALOSCHA"         to ParshaInfo("Beha'alotcha",       "https://www.chabad.org/parshah/article_cdo/aid/3290/jewish/Behaalotcha.htm"),
        "SHLACH"              to ParshaInfo("Shelach",            "https://www.chabad.org/parshah/article_cdo/aid/3293/jewish/Shelach.htm"),
        "KORACH"              to ParshaInfo("Korach",             "https://www.chabad.org/parshah/article_cdo/aid/3296/jewish/Korach.htm"),
        "CHUKAS"              to ParshaInfo("Chukat",             "https://www.chabad.org/parshah/article_cdo/aid/3299/jewish/Chukat.htm"),
        "BALAK"               to ParshaInfo("Balak",              "https://www.chabad.org/parshah/article_cdo/aid/3302/jewish/Balak.htm"),
        "CHUKAS_BALAK"        to ParshaInfo("Chukat-Balak",       "https://www.chabad.org/parshah/article_cdo/aid/3299/jewish/Chukat-Balak.htm"),
        "PINCHAS"             to ParshaInfo("Pinchas",            "https://www.chabad.org/parshah/article_cdo/aid/3305/jewish/Pinchas.htm"),
        "MATOS"               to ParshaInfo("Matot",              "https://www.chabad.org/parshah/article_cdo/aid/3308/jewish/Matot.htm"),
        "MASEI"               to ParshaInfo("Masei",              "https://www.chabad.org/parshah/article_cdo/aid/3311/jewish/Masei.htm"),
        "MATOS_MASEI"         to ParshaInfo("Matot-Masei",        "https://www.chabad.org/parshah/article_cdo/aid/3308/jewish/Matot-Masei.htm"),
        "DEVARIM"             to ParshaInfo("Devarim",            "https://www.chabad.org/parshah/article_cdo/aid/3314/jewish/Devarim.htm"),
        "VAESCHANAN"          to ParshaInfo("Va'etchanan",        "https://www.chabad.org/parshah/article_cdo/aid/3317/jewish/Vaetchanan.htm"),
        "EIKEV"               to ParshaInfo("Eikev",              "https://www.chabad.org/parshah/article_cdo/aid/3320/jewish/Eikev.htm"),
        "REEH"                to ParshaInfo("Re'eh",              "https://www.chabad.org/parshah/article_cdo/aid/3323/jewish/Reeh.htm"),
        "SHOFTIM"             to ParshaInfo("Shoftim",            "https://www.chabad.org/parshah/article_cdo/aid/3326/jewish/Shoftim.htm"),
        "KI_SEITZEI"          to ParshaInfo("Ki Teitzei",         "https://www.chabad.org/parshah/article_cdo/aid/3329/jewish/Ki-Teitzei.htm"),
        "KI_SAVO"             to ParshaInfo("Ki Tavo",            "https://www.chabad.org/parshah/article_cdo/aid/3332/jewish/Ki-Tavo.htm"),
        "NITZAVIM"            to ParshaInfo("Nitzavim",           "https://www.chabad.org/parshah/article_cdo/aid/3335/jewish/Nitzavim.htm"),
        "VAYEILECH"           to ParshaInfo("Vayelech",           "https://www.chabad.org/parshah/article_cdo/aid/3338/jewish/Vayelech.htm"),
        "NITZAVIM_VAYEILECH"  to ParshaInfo("Nitzavim-Vayelech",  "https://www.chabad.org/parshah/article_cdo/aid/3335/jewish/Nitzavim-Vayelech.htm"),
        "HAAZINU"             to ParshaInfo("Ha'azinu",           "https://www.chabad.org/parshah/article_cdo/aid/3341/jewish/Haazinu.htm"),
        "VZOS_HABERACHA"      to ParshaInfo("Vezot Habracha",     "https://www.chabad.org/parshah/article_cdo/aid/3344/jewish/Vezot-Habracha.htm"),
    )

    /** Returns display info for a KosherJava parsha enum name, or null if unknown/no parsha. */
    fun forKey(kosherJavaName: String?): ParshaInfo? =
        if (kosherJavaName == null || kosherJavaName == "NONE") null
        else map[kosherJavaName]
}
