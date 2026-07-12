package com.beardytop.beatzaddik.domain.liturgy

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
    ENGLISH,
    SPANISH,
    FRENCH,
    RUSSIAN,
}

object MeinShaloshTextEngine {

    const val EMPTY_PROMPT_ENGLISH =
        "Select what you ate or drank to build your after-blessing."

    private const val HEBREW_WINE_PHRASE = "עַל הַגֶּפֶן וְעַל פְּרִי הַגֶּפֶן"
    private const val ENGLISH_WINE_PHRASE = "the vine and the fruit of the vine"
    private const val HEBREW_TREE_PHRASE = "עַל הָעֵץ וְעַל פְּרִי הָעֵץ"
    private const val ENGLISH_TREE_PHRASE = "the tree and the fruit of the tree"

    /** Maps app UI language to a constructed liturgy translation, or null for Hebrew-only UI. */
    fun translationLanguageForApp(
        currentLanguage: String,
        translationEnabled: Boolean,
    ): MeinShaloshLanguage? = when {
        currentLanguage == "he" -> null
        currentLanguage == "en" || !translationEnabled -> MeinShaloshLanguage.ENGLISH
        currentLanguage == "es" -> MeinShaloshLanguage.SPANISH
        currentLanguage == "fr" -> MeinShaloshLanguage.FRENCH
        currentLanguage == "ru" -> MeinShaloshLanguage.RUSSIAN
        else -> MeinShaloshLanguage.ENGLISH
    }

    fun build(selection: MeinShaloshSelection, language: MeinShaloshLanguage): String {
        if (!selection.hasAnyFood) {
            return when (language) {
                MeinShaloshLanguage.HEBREW ->
                    "בחרו מה אכלתם או שתיתם כדי לבנות את הברכה."
                MeinShaloshLanguage.ENGLISH ->
                    EMPTY_PROMPT_ENGLISH
                MeinShaloshLanguage.SPANISH ->
                    "Seleccione lo que comió o bebió para armar su bendición posterior."
                MeinShaloshLanguage.FRENCH ->
                    "Sélectionnez ce que vous avez mangé ou bu pour composer votre bénédiction après le repas."
                MeinShaloshLanguage.RUSSIAN ->
                    "Выберите, что вы ели или пили, чтобы составить благословение после еды."
            }
        }
        return when (language) {
            MeinShaloshLanguage.HEBREW -> buildHebrew(selection)
            MeinShaloshLanguage.ENGLISH -> buildLocalized(selection, LocaleStrings.ENGLISH)
            MeinShaloshLanguage.SPANISH -> buildLocalized(selection, LocaleStrings.SPANISH)
            MeinShaloshLanguage.FRENCH -> buildLocalized(selection, LocaleStrings.FRENCH)
            MeinShaloshLanguage.RUSSIAN -> buildLocalized(selection, LocaleStrings.RUSSIAN)
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

    // region Localized (en / es / fr / ru)

    private data class LocaleStrings(
        val opening: String,
        val foodForPrefix: String,
        val landMiddle: String,
        val mercy: String,
        val rebuild: String,
        val blessYouUpon: String,
        val thanksPrefix: String,
        val closingPrefix: String,
        val blessedClosing: String,
        val mezonotFood: String,
        val mezonotThanks: String,
        val mezonotClosing: String,
        val winePhrase: String,
        val treePhrase: String,
        val yaalehPesach: String,
        val yaalehSukkot: String,
        val yaalehRoshChodesh: String,
        val yaalehIntro: String,
        val joinList: (List<String>) -> String,
    ) {
        companion object {
            val ENGLISH = LocaleStrings(
                opening = "Blessed are You, Lord our God, King of the universe, ",
                foodForPrefix = "for ",
                landMiddle = ", on the good and spacious land which You desired and caused our forefathers to inherit, " +
                    "that they might eat of its fruit and be satisfied with its goodness. ",
                mercy = "Have mercy, Lord our God, on Israel Your people, on Jerusalem Your city, " +
                    "on Zion the abode of Your glory, on Your altar, and on Your Temple. ",
                rebuild = "Rebuild Jerusalem the holy city speedily in our days, bring us up into it, " +
                    "and rejoice us in its rebuilding, that we may eat of its fruit and be satisfied with its goodness. ",
                blessYouUpon = "We will bless You upon it in holiness and purity, for You, Lord, are good " +
                    "and do good to all. ",
                thanksPrefix = "and we thank You for the land and for ",
                closingPrefix = "for the land and for ",
                blessedClosing = "Blessed are You, Lord, ",
                mezonotFood = "the sustenance and the nourishment",
                mezonotThanks = "the sustenance and the nourishment",
                mezonotClosing = "the sustenance",
                winePhrase = ENGLISH_WINE_PHRASE,
                treePhrase = ENGLISH_TREE_PHRASE,
                yaalehPesach = "on this Festival of Matzot",
                yaalehSukkot = "on this Festival of Sukkot",
                yaalehRoshChodesh = "on this Rosh Chodesh",
                yaalehIntro = "May our remembrance and the remembrance of our fathers, the remembrance of Messiah son of David Your servant, " +
                    "the remembrance of Jerusalem Your holy city, and the remembrance of all Your people the house of Israel " +
                    "rise and come, reach and be seen, be favored and heard, be visited and be remembered for good life and peace ",
                joinList = ::joinEnglishList,
            )

            val SPANISH = LocaleStrings(
                opening = "Bendito eres Tú, Hashem, Dios nuestro, Rey del universo, ",
                foodForPrefix = "por ",
                landMiddle = ", sobre la buena y espaciosa tierra que deseaste e hiciste heredar a nuestros padres, " +
                    "para que comieran de su fruto y se saciaran de su bondad. ",
                mercy = "Ten misericordia, Hashem, Dios nuestro, de Israel Tu pueblo, de Jerusalén Tu ciudad, " +
                    "de Sion morada de Tu gloria, de Tu altar y de Tu Templo. ",
                rebuild = "Reconstruye Jerusalén la ciudad santa pronto en nuestros días, llévanos a ella, " +
                    "y alégranos en su reconstrucción, para que comamos de su fruto y nos saciemos de su bondad. ",
                blessYouUpon = "Te bendeciremos sobre ella en santidad y pureza, porque Tú, Hashem, eres bueno " +
                    "y haces bien a todos. ",
                thanksPrefix = "y te damos gracias por la tierra y por ",
                closingPrefix = "por la tierra y por ",
                blessedClosing = "Bendito eres Tú, Hashem, ",
                mezonotFood = "la subsistencia y el sustento",
                mezonotThanks = "la subsistencia y el sustento",
                mezonotClosing = "la subsistencia",
                winePhrase = "la vid y el fruto de la vid",
                treePhrase = "el árbol y el fruto del árbol",
                yaalehPesach = "en este Festival de Matzot",
                yaalehSukkot = "en este Festival de Sucot",
                yaalehRoshChodesh = "en este Rosh Jodesh",
                yaalehIntro = "Que nuestro recuerdo y el recuerdo de nuestros padres, el recuerdo del Mesías hijo de David Tu siervo, " +
                    "el recuerdo de Jerusalén Tu ciudad santa y el recuerdo de todo Tu pueblo la casa de Israel " +
                    "suban y lleguen, aparezcan y sean aceptados, sean escuchados y recordados para buena vida y paz ",
                joinList = ::joinSpanishList,
            )

            val FRENCH = LocaleStrings(
                opening = "Béni sois-Tu, Hashem, notre Dieu, Roi de l'univers, ",
                foodForPrefix = "pour ",
                landMiddle = ", sur la bonne et spacieuse terre que Tu as désirée et fait hériter à nos pères, " +
                    "afin qu'ils en mangent le fruit et soient rassasiés de sa bonté. ",
                mercy = "Aie pitié, Hashem, notre Dieu, d'Israël Ton peuple, de Jérusalem Ta ville, " +
                    "de Sion demeure de Ta gloire, de Ton autel et de Ton Temple. ",
                rebuild = "Rebâtis Jérusalem la ville sainte bientôt en nos jours, élève-nous en elle, " +
                    "et réjouis-nous dans sa reconstruction, afin que nous mangions de son fruit et soyons rassasiés de sa bonté. ",
                blessYouUpon = "Nous Te bénirons sur elle en sainteté et en pureté, car Toi, Hashem, Tu es bon " +
                    "et Tu fais du bien à tous. ",
                thanksPrefix = "et nous Te remercions pour la terre et pour ",
                closingPrefix = "pour la terre et pour ",
                blessedClosing = "Béni sois-Tu, Hashem, ",
                mezonotFood = "la subsistance et la nourriture",
                mezonotThanks = "la subsistance et la nourriture",
                mezonotClosing = "la subsistance",
                winePhrase = "la vigne et le fruit de la vigne",
                treePhrase = "l'arbre et le fruit de l'arbre",
                yaalehPesach = "en ce Festival des Matzot",
                yaalehSukkot = "en ce Festival de Souccot",
                yaalehRoshChodesh = "en ce Roch 'Hodesh",
                yaalehIntro = "Que notre souvenir et le souvenir de nos pères, le souvenir du Messie fils de David Ton serviteur, " +
                    "le souvenir de Jérusalem Ta ville sainte et le souvenir de tout Ton peuple la maison d'Israël " +
                    "montent et arrivent, apparaissent et soient agréés, soient entendus et rappelés pour une bonne vie et la paix ",
                joinList = ::joinFrenchList,
            )

            val RUSSIAN = LocaleStrings(
                opening = "Благословен Ты, Господь, Бог наш, Царь вселенной, ",
                foodForPrefix = "за ",
                landMiddle = ", на добрую и просторную землю, которую Ты пожелал и в которую ввёл наших отцов, " +
                    "чтобы они ели от её плодов и насыщались её благом. ",
                mercy = "Помилуй, Господь, Бог наш, Израиль — народ Твой, Иерусалим — город Твой, " +
                    "Сион — обитель славы Твоей, жертвенник Твой и Храм Твой. ",
                rebuild = "Воздвигни Иерусалим, город святой, скоро в наши дни, введи нас в него " +
                    "и возвесели нас в его восстановлении, чтобы мы ели от его плодов и насыщались его благом. ",
                blessYouUpon = "Мы будем благословлять Тебя на нём в святости и чистоте, ибо Ты, Господь, благ " +
                    "и творишь добро всем. ",
                thanksPrefix = "и благодарим Тебя за землю и за ",
                closingPrefix = "за землю и за ",
                blessedClosing = "Благословен Ты, Господь, ",
                mezonotFood = "пропитание и пищу",
                mezonotThanks = "пропитание и пищу",
                mezonotClosing = "пропитание",
                winePhrase = "лозу и плод лозы",
                treePhrase = "дерево и плод дерева",
                yaalehPesach = "в этот праздник Мацот",
                yaalehSukkot = "в этот праздник Суккот",
                yaalehRoshChodesh = "в этот Рош Ходеш",
                yaalehIntro = "Да взойдёт и придёт, да явится и будет принято, услышано и вспомнено " +
                    "наше поминовение и поминовение отцов наших, поминовение Машиаха, сына Давида, раба Твоего, " +
                    "поминовение Иерусалима, города святого Твоего, и поминовение всего народа Твоего, дома Израиля, " +
                    "на добрую жизнь и мир ",
                joinList = ::joinRussianList,
            )
        }
    }

    private fun buildLocalized(selection: MeinShaloshSelection, locale: LocaleStrings): String {
        val foodFor = localizedFoodForPhrase(selection, locale)
        val thanks = localizedThanksPhrase(selection, locale)
        val closing = localizedClosingPhrase(selection, locale)
        val calendar = localizedCalendarInserts(selection, locale)

        return buildString {
            append(locale.opening)
            append(locale.foodForPrefix)
            append(foodFor)
            append(locale.landMiddle)
            append(locale.mercy)
            if (calendar.isNotEmpty()) {
                append(calendar)
                append(' ')
            }
            append(locale.rebuild)
            append(locale.blessYouUpon)
            append(thanks)
            append(". ")
            append(locale.blessedClosing)
            append(closing)
            append('.')
        }
    }

    private fun localizedFoodParts(selection: MeinShaloshSelection, locale: LocaleStrings): List<String> =
        buildList {
            if (selection.hasMezonot) add(locale.mezonotFood)
            if (selection.hasWine) add(locale.winePhrase)
            if (selection.hasFruit) add(locale.treePhrase)
        }

    private fun localizedThanksParts(selection: MeinShaloshSelection, locale: LocaleStrings): List<String> =
        buildList {
            if (selection.hasMezonot) add(locale.mezonotThanks)
            if (selection.hasWine) add(locale.winePhrase)
            if (selection.hasFruit) add(locale.treePhrase)
        }

    private fun localizedClosingParts(selection: MeinShaloshSelection, locale: LocaleStrings): List<String> =
        buildList {
            if (selection.hasMezonot) add(locale.mezonotClosing)
            if (selection.hasWine) add(locale.winePhrase)
            if (selection.hasFruit) add(locale.treePhrase)
        }

    private fun localizedFoodForPhrase(selection: MeinShaloshSelection, locale: LocaleStrings): String =
        locale.joinList(localizedFoodParts(selection, locale))

    private fun localizedThanksPhrase(selection: MeinShaloshSelection, locale: LocaleStrings): String =
        locale.thanksPrefix + locale.joinList(localizedThanksParts(selection, locale))

    private fun localizedClosingPhrase(selection: MeinShaloshSelection, locale: LocaleStrings): String =
        locale.closingPrefix + locale.joinList(localizedClosingParts(selection, locale))

    private fun localizedCalendarInserts(selection: MeinShaloshSelection, locale: LocaleStrings): String =
        buildString {
            if (selection.isRoshChodesh || selection.isPesach || selection.isSukkot) {
                append(localizedYaalehVeyavo(selection, locale))
            }
        }

    private fun localizedYaalehVeyavo(selection: MeinShaloshSelection, locale: LocaleStrings): String {
        val dayPhrase = when {
            selection.isPesach -> locale.yaalehPesach
            selection.isSukkot -> locale.yaalehSukkot
            selection.isRoshChodesh -> locale.yaalehRoshChodesh
            else -> ""
        }
        return locale.yaalehIntro + dayPhrase + ". "
    }

    /** Oxford-style list: "a", "a and b", "a, b, and c". */
    internal fun joinEnglishList(items: List<String>): String = when (items.size) {
        0 -> ""
        1 -> items[0]
        2 -> "${items[0]} and ${items[1]}"
        else -> items.dropLast(1).joinToString(", ") + ", and " + items.last()
    }

    private fun joinSpanishList(items: List<String>): String = when (items.size) {
        0 -> ""
        1 -> items[0]
        2 -> "${items[0]} y ${items[1]}"
        else -> items.dropLast(1).joinToString(", ") + " y " + items.last()
    }

    private fun joinFrenchList(items: List<String>): String = when (items.size) {
        0 -> ""
        1 -> items[0]
        2 -> "${items[0]} et ${items[1]}"
        else -> items.dropLast(1).joinToString(", ") + " et " + items.last()
    }

    private fun joinRussianList(items: List<String>): String = when (items.size) {
        0 -> ""
        1 -> items[0]
        2 -> "${items[0]} и ${items[1]}"
        else -> items.dropLast(1).joinToString(", ") + " и " + items.last()
    }

    // endregion
}

