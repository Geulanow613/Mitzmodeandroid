package com.beardytop.mitzmode.data

/**
 * Builds the full Bracha Me'ein Shalosh (Al HaMichya) text from what was eaten and calendar context.
 * Pure logic — no UI dependencies.
 */
data class MeinShaloshSelection(
    val hasMezonot: Boolean = false,
    val hasWine: Boolean = false,
    val hasFruit: Boolean = false,
    val isRoshChodesh: Boolean = false,
    val isPesach: Boolean = false,
    val isSukkot: Boolean = false
) {
    val hasAnyFood: Boolean get() = hasMezonot || hasWine || hasFruit
}

enum class MeinShaloshLanguage {
    HEBREW,
    ENGLISH
}

object MeinShaloshTextEngine {

    private const val HEBREW_WINE_PHRASE = "עַל הַגֶּפֶן וְעַל פְּרִי הַגֶּפֶן"
    private const val ENGLISH_WINE_PHRASE = "the vine and the fruit of the vine"
    private const val HEBREW_TREE_PHRASE = "עַל הָעֵץ וְעַל פְּרִי הָעֵץ"
    private const val ENGLISH_TREE_PHRASE = "the tree and the fruit of the tree"

    fun build(selection: MeinShaloshSelection, language: MeinShaloshLanguage): String {
        if (!selection.hasAnyFood) {
            return when (language) {
                MeinShaloshLanguage.HEBREW ->
                    "בחרו מה אכלתם או שתיתם כדי לבנות את הברכה."
                MeinShaloshLanguage.ENGLISH ->
                    "Select what you ate or drank to build your after-blessing."
            }
        }
        return when (language) {
            MeinShaloshLanguage.HEBREW -> buildHebrew(selection)
            MeinShaloshLanguage.ENGLISH -> buildEnglish(selection)
        }
    }

    // region Hebrew

    private fun buildHebrew(selection: MeinShaloshSelection): String {
        val foodOpening = hebrewFoodOpeningPhrase(selection)
        val thanks = hebrewThanksPhrase(selection)
        val closing = hebrewClosingPhrase(selection)
        val calendar = hebrewCalendarInserts(selection)

        return buildString {
            append("בָּרוּךְ אַתָּה ה' אֱלֹקֵינוּ מֶלֶךְ הָעוֹלָם ")
            append(foodOpening)
            append(" עַל אֶרֶץ חֶמְדָּה טוֹבָה וּרְחָבָה שֶׁרָצִיתָ וְהִנְחַלְתָּ לַאֲבוֹתֵינוּ לֶאֱכוֹל מִפִּרְיָהּ וְלִשְׂבּוֹעַ מִטּוּבָהּ. ")
            append("רַחֵם ה' אֱלֹקֵינוּ עַל יִשְׂרָאֵל עַמֶּךָ וְעַל יְרוּשָׁלַיִם עִירֶךָ וְעַל צִיּוֹן מִשְׁכַּן כְּבוֹדֶךָ וְעַל מִזְבְּחֶךָ וְעַל הֵיכָלֶךָ. ")
            if (calendar.isNotEmpty()) {
                append(calendar)
                append(' ')
            }
            append("וּבְנֵה יְרוּשָׁלַיִם עִיר הַקֹּדֶשׁ בִּמְהֵרָה בְיָמֵינוּ וְהַעֲלֵנוּ לְתוֹכָהּ וְשַׂמְּחֵנוּ בְּבִנְיָנָהּ וְנֹאכַל מִפִּרְיָהּ וְנִשְׂבַּע מִטּוּבָהּ. ")
            append("וּנְבָרֶכְךָ עָלֶיהָ בִּקְדֻשָּׁה וּבְטָהֳרָה כִּי אַתָּה ה' טוֹב וּמֵטִיב לַכֹּל ")
            append(thanks)
            append(". בָּרוּךְ אַתָּה ה' ")
            append(closing)
            append('.')
        }
    }

    private fun hebrewFoodOpeningPhrase(selection: MeinShaloshSelection): String =
        hebrewFoodParts(selection).joinToString(" וְ")

    private fun hebrewThanksPhrase(selection: MeinShaloshSelection): String {
        val parts = hebrewThanksParts(selection)
        return "וְנוֹדֶה לְךָ עַל הָאָרֶץ וְ${parts.joinToString(" וְ")}"
    }

    private fun hebrewClosingPhrase(selection: MeinShaloshSelection): String {
        val parts = hebrewClosingParts(selection)
        return "עַל הָאָרֶץ וְ${parts.joinToString(" וְ")}"
    }

    private fun hebrewFoodParts(selection: MeinShaloshSelection): List<String> = buildList {
        if (selection.hasMezonot) add("עַל הַמִּחְיָה וְעַל הַכַּלְכָּלָה")
        if (selection.hasWine) add(HEBREW_WINE_PHRASE)
        if (selection.hasFruit) add(HEBREW_TREE_PHRASE)
    }

    private fun hebrewThanksParts(selection: MeinShaloshSelection): List<String> = buildList {
        if (selection.hasMezonot) add("עַל הַמִּחְיָה וְעַל הַכַּלְכָּלָה")
        if (selection.hasWine) add(HEBREW_WINE_PHRASE)
        if (selection.hasFruit) add(HEBREW_TREE_PHRASE)
    }

    private fun hebrewClosingParts(selection: MeinShaloshSelection): List<String> = buildList {
        if (selection.hasMezonot) add("עַל הַמִּחְיָה")
        if (selection.hasWine) add(HEBREW_WINE_PHRASE)
        if (selection.hasFruit) add(HEBREW_TREE_PHRASE)
    }

    private fun hebrewCalendarInserts(selection: MeinShaloshSelection): String = buildString {
        if (selection.isRoshChodesh || selection.isPesach || selection.isSukkot) {
            append(hebrewYaalehVeyavo(selection))
        }
    }

    private fun hebrewYaalehVeyavo(selection: MeinShaloshSelection): String {
        val dayPhrase = when {
            selection.isPesach -> "בְּיוֹם חַג הַמַּצּוֹת הַזֶּה"
            selection.isSukkot -> "בְּיוֹם חַג הַסֻּכּוֹת הַזֶּה"
            selection.isRoshChodesh -> "בְּיוֹם רֹאשׁ חֹדֶשׁ הַזֶּה"
            else -> "הַיּוֹם"
        }
        return "יַעֲלֶה וְיָבֹא וְיַגִּיעַ וְיֵרָאֶה וְיֵרָצֶה וְיִשָּׁמַע וְיִפָּקֵד וְיִזָּכֵר " +
            "זִכְרוֹנֵנוּ וְזִכְרוֹן אֲבוֹתֵינוּ, זִכְרוֹן מָשִׁיחַ בֶּן דָּוִד עַבְדֶּךָ, " +
            "זִכְרוֹן יְרוּשָׁלַיִם עִיר קָדְשֶׁךָ וּזְכִירַת כָּל עַמְּךָ בֵּית יִשְׂרָאֵל " +
            "לְחַיִּים טוֹבִים וּלְשָׁלוֹם $dayPhrase."
    }

    // endregion

    // region English

    private fun buildEnglish(selection: MeinShaloshSelection): String {
        val foodFor = englishFoodForPhrase(selection)
        val thanks = englishThanksPhrase(selection)
        val closing = englishClosingPhrase(selection)
        val calendar = englishCalendarInserts(selection)

        return buildString {
            append("Blessed are You, Lord our God, King of the universe, for ")
            append(foodFor)
            append(", on the good and spacious land which You desired and caused our forefathers to inherit, ")
            append("that they might eat of its fruit and be satisfied with its goodness. ")
            append("Have mercy, Lord our God, on Israel Your people, on Jerusalem Your city, ")
            append("on Zion the abode of Your glory, on Your altar, and on Your Temple. ")
            if (calendar.isNotEmpty()) {
                append(calendar)
                append(' ')
            }
            append(
                "Rebuild Jerusalem the holy city speedily in our days, bring us up into it, " +
                    "and rejoice us in its rebuilding, that we may eat of its fruit and be satisfied with its goodness. "
            )
            append(
                "We will bless You upon it in holiness and purity, for You, Lord, are good " +
                    "and do good to all. "
            )
            append(thanks)
            append(". Blessed are You, Lord, ")
            append(closing)
            append('.')
        }
    }

    private fun englishFoodForPhrase(selection: MeinShaloshSelection): String {
        val phrases = buildList {
            if (selection.hasMezonot) add("the sustenance and the nourishment")
            if (selection.hasWine) add(ENGLISH_WINE_PHRASE)
            if (selection.hasFruit) add(ENGLISH_TREE_PHRASE)
        }
        return joinEnglishList(phrases)
    }

    private fun englishThanksPhrase(selection: MeinShaloshSelection): String {
        val parts = buildList {
            if (selection.hasMezonot) add("the sustenance and the nourishment")
            if (selection.hasWine) add(ENGLISH_WINE_PHRASE)
            if (selection.hasFruit) add(ENGLISH_TREE_PHRASE)
        }
        return "and we thank You for the land and for ${joinEnglishList(parts)}"
    }

    private fun englishClosingPhrase(selection: MeinShaloshSelection): String {
        val parts = buildList {
            if (selection.hasMezonot) add("the sustenance")
            if (selection.hasWine) add(ENGLISH_WINE_PHRASE)
            if (selection.hasFruit) add(ENGLISH_TREE_PHRASE)
        }
        return "for the land and for ${joinEnglishList(parts)}"
    }

    private fun englishCalendarInserts(selection: MeinShaloshSelection): String = buildString {
        if (selection.isRoshChodesh || selection.isPesach || selection.isSukkot) {
            append(englishYaalehVeyavo(selection))
        }
    }

    private fun englishYaalehVeyavo(selection: MeinShaloshSelection): String {
        val dayPhrase = when {
            selection.isPesach -> "on this Festival of Matzot"
            selection.isSukkot -> "on this Festival of Sukkot"
            selection.isRoshChodesh -> "on this Rosh Chodesh"
            else -> "this day"
        }
        return "May our remembrance and the remembrance of our fathers, the remembrance of Messiah son of David Your servant, " +
            "the remembrance of Jerusalem Your holy city, and the remembrance of all Your people the house of Israel " +
            "rise and come, reach and be seen, be favored and heard, be visited and be remembered for good life and peace $dayPhrase. "
    }

    /** Oxford-style list: "a", "a and b", "a, b, and c". */
    internal fun joinEnglishList(items: List<String>): String = when (items.size) {
        0 -> ""
        1 -> items[0]
        2 -> "${items[0]} and ${items[1]}"
        else -> items.dropLast(1).joinToString(", ") + ", and " + items.last()
    }

    // endregion
}
