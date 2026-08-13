package com.beardytop.beatzaddik.domain

/**
 * Term definitions used by [HalachicTermsDictionary] for in-text glossary links.
 * [withKeyTerms] no longer prepends a "Key terms" block — explainers show body text only.
 */
object BeginnerHalachaGlossary {

    /** Prepends nothing — callers may still pass [terms] for API stability; body only is shown. */
    fun withKeyTerms(terms: String, body: String): String = body.trim()

    fun block(vararg lines: String): String =
        "Key terms:\n" + lines.joinToString("\n") { "• $it" }

    // ── Shared term lines ─────────────────────────────────────────────────────

    const val MELACHA =
        "Melacha — transformative labor forbidden on Shabbat (including cooking). On Yom Tov most melachot remain forbidden, but ochel nefesh — food preparation for that day's meals, including cooking from a pre-existing flame — is permitted (Exodus 12:16). Other examples still restricted on Yom Tov: writing, building, kindling a new fire, most phone/electronics per your rav"
    const val YOM_TOV =
        "Yom Tov — a festival day (Pesach, Sukkot, Rosh Hashana, etc.) with work rules similar to Shabbat; some food prep is allowed on the festival itself"
    const val SHABBAT =
        "Shabbat — the weekly Sabbath from Friday sunset until halachic nightfall (tzeit) on Saturday night, per your community's zmanim"
    const val EREV =
        "Erev — the eve before; \"Erev Shabbat\" is Friday, \"Erev Pesach\" is the day before Pesach begins at night"
    const val CHAG =
        "Chag — festival (same idea as Yom Tov in everyday speech)"
    const val TZEIT =
        "Tzeit — halachic nightfall (when three medium stars appear). Standard time for many night mitzvot — Omer count, Purim Megillah, Motzei Shabbat. Bedikat chametz is the night before Pesach begins. Chanukah candles are ideally after tzeit, though many allow from sunset when needed. Note: Shabbat and weekday first-night Yom Tov candles are before sunset (new match OK). Once holy time has begun — second Yom Tov night (incl. Rosh Hashana night 2 in Israel), Motzei Shabbat→Yom Tov after tzeit, Yom Tov→Shabbat before sunset — light only from a pre-existing flame"
    const val RAV =
        "Rav — Hebrew title for a senior Torah scholar who rules on halacha (your community's decisor)"
    const val RABBI =
        "Rabbi — English title for an ordained Jewish spiritual leader (teacher, counselor, or synagogue leader)"
    const val MINHAG =
        "Minhag — community or family custom (not always identical in every synagogue)"
    const val BRACHA =
        "Bracha — blessing said before a mitzvah or food"
    const val KIDDUSH =
        "Kiddush — blessing over wine that sanctifies Shabbat or Yom Tov at the meal"
    const val HAVDALAH =
        "Havdalah — ceremony separating holy time from weekday; men and women obligated. Motzei Shabbat: wine, spices (neshama yeteira), fire (Adam HaRishon / looking at nails). Motzei Yom Tov: wine + Hamavdil only. Motzei Yom Kippur: wine + ner she-shavat; spices only if YK was Shabbat. After Tisha B'Av that began Motzei Shabbat: wine+Hamavdil Sunday night. Missed Saturday night: wine + Hamavdil through Tuesday (no spices or fire). Yaknehaz omits spices when Shabbat leads into Yom Tov"
    const val YAKNEHAZ =
        "Yaknehaz — order when Shabbat leads into Yom Tov: Wine → Kiddush → candle (Ner) → Havdalah text → Shehecheyanu (Zeman); spices are omitted"
    const val ERUV_TAVSHILIN =
        "Eruv tavshilin — symbolic food set aside on Erev Yom Tov so you may continue food preparation on the festival for the Shabbat that follows (only in certain calendar years)"
    const val BLECH =
        "Blech — metal cover on the stove so food stays warm on Shabbat without cooking"
    const val SHUL =
        "Shul — synagogue"
    const val DAVEN =
        "Daven / davening — pray; any prayer (blessings, Shema, Tehillim, personal tefillah, or the services Shacharit, Mincha, Maariv, etc.)"
    const val AMIDAH =
        "Amidah / Shmoneh Esrei — the central standing silent prayer"
    const val MUSAF =
        "Musaf — extra Amidah on Shabbat, Yom Tov, and Rosh Chodesh remembering Temple offerings"
    const val HALLEL =
        "Hallel — psalms of praise added on festivals and Rosh Chodesh"
    const val YAALEH_VYAVO =
        "Yaaleh V'yavo — paragraph added to Amidah and Grace after Meals on Rosh Chodesh and festivals"
    const val BENTCHING =
        "Bentching / Birkat Hamazon — Grace after Meals"
    const val TACHANUN =
        "Tachanun — penitential prayers omitted on happy days"
    const val SHEHECHEYANU =
        "Shehecheyanu — blessing for reaching a new season or mitzvah"
    const val CHAMETZ =
        "Chametz — leavened grain (bread, beer, etc.) forbidden on Pesach"
    const val BEDIKAT =
        "Bedikat chametz — formal search for chametz the night before Pesach"
    const val BIUR =
        "Biur chametz — destroying chametz the next morning"
    const val MECHIRAT =
        "Mechirat chametz — selling chametz to a non-Jew through your rabbi so you do not own it on Pesach"
    const val KOL_CHAMIRA =
        "Kol Chamira — declaration nullifying any chametz still in your possession"
    const val SIYUM =
        "Siyum — festive meal (seudat mitzvah) celebrating finishing a section of Torah study; eating at the meal (not merely hearing the siyum) exempts firstborns from Taanit Bechorot"
    const val SEDER =
        "Seder — Pesach night ritual meal with the Haggadah, matzah, four cups of wine, etc."
    const val KITNIYOT =
        "Kitniyot — legumes/rice; an Ashkenazi customary stringency on Pesach (not Torah chametz); halachically permitted for Sephardim"
    const val CHOL_HAMOED =
        "Chol HaMoed — middle days of Pesach or Sukkot (lighter work rules than full Yom Tov)"
    const val REVIIT =
        "Revi'it — one-quarter of a log; minimum ritual liquid volume for Kiddush, Havdalah, netilat yadayim, and the four cups (86–150 ml depending on poskim; confirm with your rav)"
    const val KEZAYIT =
        "Kezayit — olive-sized portion in halacha; many authorities measure it as about a golf ball (35–40 ml / 25–33 g depending on poskim; confirm with your rav)"
    const val OCHEL_NEFESH =
        "Ochel nefesh — the halachic allowance to perform certain food preparation tasks on Yom Tov (festival days) for consumption on that same day"
    const val CHAMAR_MEDINA =
        "Chamar medina — a prestigious local beverage (such as kosher liquor or coffee, per community standards) used for daytime Kiddush or Havdalah when wine is unavailable — generally not for Friday night Shabbat Kiddush, which typically requires wine, grape juice, or washing for bread; not valid for the Seder's four cups per most poskim; never beer on Pesach (beer is chametz)"
    const val SCHACH =
        "Schach — plant covering on the sukkah roof"
    const val LULAV_SET =
        "Arba Minim — four plants waved on Sukkot: palm (lulav), citron (etrog), myrtle (hadas), willow (aravah)"
    const val MEGILLAH =
        "Megillah — Book of Esther read on Purim"
    const val MISHLOACH =
        "Mishloach manot — sending at least two ready-to-eat foods to one friend on Purim day"
    const val MATANOT =
        "Matanot la'evyonim — gifts to at least two poor people on Purim day"
    const val SEUDAH =
        "Seudah — festive meal (e.g. Purim afternoon feast)"
    const val OMER =
        "Sefirat HaOmer — counting 49 days from Pesach to Shavuot"
    const val SELICHOT =
        "Selichot — extra penitential prayers before Rosh Hashana (timing varies by nusach). You need a special Selichot prayerbook for your nusach — some editions are free online"
    const val PIRSUMEI_NISA =
        "Pirsumei nisa — publicizing the miracle (why the menorah is placed visibly)"

    fun yomTovAndShabbat(): String = block(
        MELACHA,
        YOM_TOV,
        OCHEL_NEFESH,
        SHABBAT,
        TZEIT,
        KIDDUSH,
        HAVDALAH,
        BLECH,
        ERUV_TAVSHILIN,
        YAKNEHAZ,
        RAV,
    )

    fun erevChagCommon(): String = block(
        MELACHA,
        YOM_TOV,
        OCHEL_NEFESH,
        ERUV_TAVSHILIN,
        TZEIT,
        KIDDUSH,
        BLECH,
        MUSAF,
        SHUL,
    )

    fun pesachPrep(): String = block(
        CHAMETZ,
        BEDIKAT,
        BIUR,
        MECHIRAT,
        KOL_CHAMIRA,
        SIYUM,
        SEDER,
        YOM_TOV,
        TZEIT,
        RAV,
    )

    fun daveningBasics(): String = block(
        DAVEN,
        AMIDAH,
        MUSAF,
        HALLEL,
        YAALEH_VYAVO,
        BENTCHING,
        TACHANUN,
        BRACHA,
        SHUL,
    )

    fun shavuotBasics(): String = block(
        YOM_TOV,
        "Shavuot — festival marking Matan Torah (receiving the Torah at Sinai)",
        "Tikkun Leil Shavuot — all-night Torah study on the first night",
        MUSAF,
        HALLEL,
        YAALEH_VYAVO,
        MINHAG,
        RAV,
    )

    fun simchasYomTovBasics(): String = block(
        YOM_TOV,
        "Simchat Yom Tov — Torah mitzvah to rejoice on the festival (V'samachta b'chagecha)",
        REVIIT,
        CHAMAR_MEDINA,
        MINHAG,
        RAV,
    )

    fun cholHamoedBasics(): String = block(
        CHOL_HAMOED,
        YOM_TOV,
        MELACHA,
        CHAMAR_MEDINA,
        "Simchat Yom Tov — rejoicing on the festival (wine, food, and enjoyment)",
        REVIIT,
        MINHAG,
        RAV,
    )

    fun sukkotBasics(): String = block(
        SCHACH,
        LULAV_SET,
        YOM_TOV,
        MELACHA,
        BRACHA,
        KEZAYIT,
        RAV,
    )

    fun purimBasics(): String = block(
        MEGILLAH,
        MATANOT,
        MISHLOACH,
        SEUDAH,
        TZEIT,
        BRACHA,
        RAV,
    )

    fun chanukahBasics(): String = block(
        PIRSUMEI_NISA,
        TZEIT,
        SHABBAT,
        BRACHA,
        SHEHECHEYANU,
        RAV,
    )

    fun omerBasics(): String = block(
        OMER,
        BRACHA,
        TZEIT,
        SHUL,
        RAV,
    )

    fun mourningBasics(): String = block(
        MINHAG,
        SHUL,
        RAV,
    )

    fun selichotBasics(): String = block(
        SELICHOT,
        DAVEN,
        SHUL,
        MINHAG,
        RAV,
    )
}
