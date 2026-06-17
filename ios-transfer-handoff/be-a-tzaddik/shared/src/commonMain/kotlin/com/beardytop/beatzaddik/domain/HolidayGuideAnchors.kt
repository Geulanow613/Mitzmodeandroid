package com.beardytop.beatzaddik.domain

/** Maps display labels to Shabbat guide topic ids (shared by header and upcoming holidays). */
object HolidayGuideAnchors {

    /** Exact display names from calendar backends and [UpcomingHolidayPlanner]. */
    private val exactTopicIds = mapOf(
        "Shabbat" to "shabbat_overview",
        "Rosh Chodesh" to "rosh_chodesh",
        "Chanukah" to "chanukah",
        "Purim" to "purim",
        "Purim Katan" to "purim_katan",
        "Pesach" to "pesach",
        "Erev Pesach" to "erev_pesach",
        "Chol HaMoed Pesach" to "chol_hamoed_pesach",
        "Shavuot" to "shavuot",
        "Sukkot" to "sukkot",
        "Chol HaMoed Sukkot" to "chol_hamoed_sukkot",
        "Hoshana Raba" to "hoshana_raba",
        "Shemini Atzeret" to "shemini_atzeret",
        "Simchat Torah" to "simchat_torah",
        "Rosh Hashana" to "rosh_hashana",
        "Yom Kippur" to "yom_kippur",
        "Yom Tov" to "yom_tov",
        "Tu B'Shvat" to "tu_bshvat",
        "Pesach Sheni" to "pesach_sheni",
        "Lag BaOmer" to "lag_baomer",
        "Tu B'Av" to "tu_beav",
        "Tisha B'Av" to "tisha_beav",
        "Fast of Gedaliah" to "fast_gedaliah",
        "Fast of 10 Tevet" to "fast_10_tevet",
        "Fast of Esther" to "fast_esther",
        "Fast of 17 Tammuz" to "fast_17_tammuz",
        "Yom HaShoah" to "yom_hashoah",
        "Yom HaZikaron" to "yom_hazikaron",
        "Yom Ha'atzmaut" to "yom_haatzmaut",
        "Yom Yerushalayim" to "yom_yerushalayim",
    )

    private val bySubstring = listOf(
        "Chol HaMoed Sukkot" to "chol_hamoed_sukkot",
        "Chol HaMoed Pesach" to "chol_hamoed_pesach",
        "Chol HaMoed" to "pesach",
        "Fast of Esther (Taanit Esther)" to "fast_esther",
        "Fast of Gedaliah" to "fast_gedaliah",
        "Fast of 10 Tevet" to "fast_10_tevet",
        "Fast of 17 Tammuz" to "fast_17_tammuz",
        "Fast of Esther" to "fast_esther",
        "Tisha B'Av" to "tisha_beav",
        "Yom Kippur" to "yom_kippur",
        "Rosh Hashana" to "rosh_hashana",
        "Sukkot" to "sukkot",
        "Shemini Atzeret" to "shemini_atzeret",
        "Simchat Torah" to "simchat_torah",
        "Chanukah" to "chanukah",
        "Purim Katan" to "purim_katan",
        "Purim" to "purim",
        "Erev Pesach" to "erev_pesach",
        "Pesach" to "pesach",
        "Passover" to "pesach",
        "Shavuot" to "shavuot",
        "Hoshana Raba" to "hoshana_raba",
        "Tu B'Shvat" to "tu_bshvat",
        "Pesach Sheni" to "pesach_sheni",
        "Tu B'Av" to "tu_beav",
        "Lag BaOmer" to "lag_baomer",
        "Shabbat" to "shabbat_overview",
        "Rosh Chodesh" to "rosh_chodesh",
        "Yom HaShoah" to "yom_hashoah",
        "Yom HaZikaron" to "yom_hazikaron",
        "Yom Ha'atzmaut" to "yom_haatzmaut",
        "Yom Yerushalayim" to "yom_yerushalayim",
    ).sortedByDescending { it.first.length }

    private fun normalize(label: String): String =
        label.replace('\u2019', '\'').trim()

    fun anchorForLabel(label: String): String? {
        val normalized = normalize(label)
        exactTopicIds[normalized]?.let { return it }
        return bySubstring.firstOrNull { (term, _) ->
            normalized.contains(term, ignoreCase = true)
        }?.second
    }
}
