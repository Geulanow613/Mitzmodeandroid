import Foundation

/// Mirrors Android `BirkatCategory` (for documentation / future filtering).
enum BirkatCategory {
    case zimun
    case mainBlessings
    case additions
}

/// One step of zimun: English who/when, then Hebrew, then optional English translation of that Hebrew.
struct BirkatZimmunLineGroup: Equatable, Hashable {
    let englishGuide: String
    let hebrew: String
    let englishLine: String
}

struct BirkatHamazonSection {
    let title: String
    /// Optional Hebrew note shown first (e.g. when zimun applies).
    let hebrewIntro: String?
    let hebrew: String
    let english: String?
    /// English note shown after `hebrewIntro` and before `hebrew`, regardless of “Show English” (e.g. zimun context).
    let englishExplanatoryIntro: String?
    /// When set, the zimmun section renders as preamble + guided line groups instead of the flat intro + hebrew + english stack.
    let zimmunPreambleEnglish: String?
    let zimmunLineGroups: [BirkatZimmunLineGroup]?
    let category: BirkatCategory?
    /// When set, Hebrew and English show this one-line hint until the user expands the full text.
    let collapsedSummaryHebrew: String?
    let collapsedSummaryEnglish: String?

    var isCollapsible: Bool {
        collapsedSummaryHebrew != nil || collapsedSummaryEnglish != nil
    }

    init(
        title: String,
        hebrewIntro: String? = nil,
        hebrew: String,
        english: String? = nil,
        englishExplanatoryIntro: String? = nil,
        zimmunPreambleEnglish: String? = nil,
        zimmunLineGroups: [BirkatZimmunLineGroup]? = nil,
        category: BirkatCategory? = nil,
        collapsedSummaryHebrew: String? = nil,
        collapsedSummaryEnglish: String? = nil
    ) {
        self.title = title
        self.hebrewIntro = hebrewIntro
        self.hebrew = hebrew
        self.english = english
        self.englishExplanatoryIntro = englishExplanatoryIntro
        self.zimmunPreambleEnglish = zimmunPreambleEnglish
        self.zimmunLineGroups = zimmunLineGroups
        self.category = category
        self.collapsedSummaryHebrew = collapsedSummaryHebrew
        self.collapsedSummaryEnglish = collapsedSummaryEnglish
    }
}

/// Full Grace After Meals text — same sections as Android `BirkatHamazonText.kt`.
/// Hebrew is vocalized (nikkud); liturgical wording largely follows common Ashkenaz sources (e.g. Sefaria, “Birkat Hamazon — Ashkenaz”).
enum BirkatHamazonText {
    /// על נהרות בבל — cut from main `sections`; shown only after user picks “days with Tachanun”.
    static let prefatoryAlNaharotBavel: BirkatHamazonSection = BirkatHamazonSection(
        title: "על נהרות בבל",
        hebrew: """
עַל־נַהֲרוֹת בָּבֶל שָׁם יָשַׁבְנוּ גַּם־בָּכִינוּ בְּזׇכְרֵנוּ אֶת־צִיּֽוֹן עַֽל־עֲרָבִים בְּתוֹכָהּ תָּלִינוּ כִּנֹּרוֹתֵֽינוּ כִּי שָׁם שְֽׁאֵלוּנוּ שׁוֹבֵינוּ דִּבְרֵי־שִׁיר וְתוֹלָלֵינוּ שִׂמְחָה שִׁירוּ לָנוּ מִשִּׁיר צִיּֽוֹן אֵיךְ נָשִׁיר אֶת־שִׁיר־יְהוָה עַל אַדְמַת נֵכָֽר אִֽם־אֶשְׁכָּחֵךְ יְֽרוּשָׁלִָם תִּשְׁכַּח יְמִינִֽי תִּדְבַּֽק־לְשׁוֹנִי לְחִכִּי אִם־לֹא אֶזְכְּרֵכִי אִם־לֹא אַעֲלֶה אֶת־יְרוּשָׁלִַם עַל רֹאשׁ שִׂמְחָתִֽי זְכֹר יְהוָה לִבְנֵי אֱדוֹם אֵת יוֹם יְֽרוּשָׁלִָם הָאֹמְרִים עָרוּ עָרוּ עַד הַיְסוֹד בָּֽהּ בַּת־בָּבֶל הַשְּׁדוּדָה אַשְׁרֵי שֶׁיְשַׁלֶּם־לָךְ אֶת־גְּמוּלֵךְ שֶׁגָּמַלְתְּ לָֽנוּ אַשְׁרֵי שֶׁיֹּאחֵז וְנִפֵּץ אֶֽת־עֹלָלַיִךְ אֶל־הַסָּֽלַע
""",
        english: """
By the rivers of Babylon, there we sat and wept when we remembered Zion. Upon the willows in its midst we hung our harps. For there our captors asked us for words of song, and our tormentors [asked of us] mirth: 'Sing for us from Zion's song.' How shall we sing the Lord's song on alien soil? If I forget you, O Jerusalem, let my right hand forget its skill. Let my tongue cleave to my palate if I do not remember you, if I do not bring up Jerusalem at the beginning of my joy. Remember, O Lord, against the Edomites the day of Jerusalem, who said, 'Raze it, raze it to its very foundation!' O daughter of Babylon, who is destined to be plundered, praiseworthy is he who repays you your recompense for what you did to us. Praiseworthy is he who will take and dash your infants against the rock.
""",
        category: .additions
    )

    /// שיר המעלות בשוב ה' — cut from main `sections`; shown only after user picks “days without Tachanun”.
    static let prefatoryShirHamaalosBeshuv: BirkatHamazonSection = BirkatHamazonSection(
        title: "שיר המעלות בשוב ה׳",
        hebrew: """
שִׁיר הַֽמַּעֲלוֹת בְּשׁוּב יְהוָה אֶת־שִׁיבַת צִיּוֹן הָיִינוּ כְּחֹלְמִֽים אָז יִמָּלֵא שְׂחוֹק פִּינוּ וּלְשׁוֹנֵנוּ רִנָּה אָז יֹאמְרוּ בַגּוֹיִם הִגְדִּיל יְהוָה לַעֲשׂוֹת עִם־אֵֽלֶּה הִגְדִּיל יְהוָה לַעֲשׂוֹת עִמָּנוּ הָיִינוּ שְׂמֵחִֽים שׁוּבָה יְהוָה אֶת־שְׁבוּתֵנוּ כַּאֲפִיקִים בַּנֶּגֶב הַזֹּרְעִים בְּדִמְעָה בְּרִנָּה יִקְצֹֽרוּ הָלוֹךְ יֵלֵךְ וּבָכֹה נֹשֵׂא מֶשֶׁךְ־הַזָּרַע בֹּא־יָבֹא בְרִנָּה נֹשֵׂא אֲלֻמֹּתָֽיו
""",
        english: """
A song of ascents. When the Lord brought back those who returned to Zion, we felt like we were dreaming. Our mouths filled with laughter and our tongues with joy. The nations said, “The Lord has done great things for them!” The Lord has done great things for us, and we were glad. Lord, bring our people home again like streams in the Negev. Those who plant in tears will harvest with joy. Whoever walks out weeping, carrying bags of seed, will come home cheering, carrying bundles of grain.
""",
        category: .additions
    )

    static let sections: [BirkatHamazonSection] = [
        BirkatHamazonSection(
            title: "זימון",
            hebrewIntro: nil,
            hebrew: """
רַבּוֹתַי, נְבָרֵךְ!
יְהִי שֵׁם יְיָ מְבֹרָךְ מֵעַתָּה וְעַד עוֹלָם.
בִּרְשׁוּת מָרָנָן וְרַבָּנָן וְרַבּוֹתַי, נְבָרֵךְ (אֱלֹהֵינוּ) שֶׁאָכַלְנוּ מִשֶּׁלּוֹ.
בָּרוּךְ (אֱלֹהֵינוּ) שֶׁאָכַלְנוּ מִשֶּׁלּוֹ וּבְטוּבּוֹ חָיִינוּ.
בָּרוּךְ (אֱלֹהֵינוּ) שֶׁאָכַלְנוּ מִשֶּׁלּוֹ וּבְטוּבּוֹ חָיִינוּ.
""",
            english: nil,
            englishExplanatoryIntro: nil,
            zimmunPreambleEnglish: """
Zimun applies when at least three Jewish men who have reached bar mitzvah ate bread as part of the same meal and will recite Birkat Hamazon together. One of them leads as the mezamen (מזמן). When at least ten such men are present, include “Eloheinu” (our God) wherever it appears in parentheses in the lines below (Ashkenazi custom).
""",
            zimmunLineGroups: [
                BirkatZimmunLineGroup(
                    englishGuide: "The mezamen opens:",
                    hebrew: "רַבּוֹתַי, נְבָרֵךְ!",
                    englishLine: "My masters, let us bless!"
                ),
                BirkatZimmunLineGroup(
                    englishGuide: "Everyone who ate together responds:",
                    hebrew: "יְהִי שֵׁם יְיָ מְבֹרָךְ מֵעַתָּה וְעַד עוֹלָם.",
                    englishLine: "May the name of the Lord be blessed from now and forever."
                ),
                BirkatZimmunLineGroup(
                    englishGuide: "The mezamen says the invitation to bless. With ten men, add “Eloheinu” in parentheses before “she’achalnu”:",
                    hebrew: "בִּרְשׁוּת מָרָנָן וְרַבָּנָן וְרַבּוֹתַי, נְבָרֵךְ (אֱלֹהֵינוּ) שֶׁאָכַלְנוּ מִשֶּׁלּוֹ.",
                    englishLine: "With the permission of our masters and teachers, let us bless (our God) He of whose food we have eaten."
                ),
                BirkatZimmunLineGroup(
                    englishGuide: "Everyone responds (with ten, include “Eloheinu” after “Baruch”):",
                    hebrew: "בָּרוּךְ (אֱלֹהֵינוּ) שֶׁאָכַלְנוּ מִשֶּׁלּוֹ וּבְטוּבּוֹ חָיִינוּ.",
                    englishLine: "Blessed be (our God) He of whose food we have eaten and through whose goodness we live."
                ),
                BirkatZimmunLineGroup(
                    englishGuide: "The mezamen repeats aloud the same words the group just said (again with “Eloheinu” when ten are present). This completes zimun before bentching—it is halachically required repetition, not a duplicate line by mistake.",
                    hebrew: "בָּרוּךְ (אֱלֹהֵינוּ) שֶׁאָכַלְנוּ מִשֶּׁלּוֹ וּבְטוּבּוֹ חָיִינוּ.",
                    englishLine: "Blessed be (our God) He of whose food we have eaten and through whose goodness we live."
                )
            ],
            category: .zimun
        ),
        BirkatHamazonSection(
            title: "ברכת הזן",
            hebrew: """
בָּרוּךְ אַתָּה יְהוָה אֱלֹהֵינוּ מֶלֶךְ הָעוֹלָם הַזָּן אֶת הָעוֹלָם כֻּלּוֹ בְּטוּבוֹ בְּחֵן בְּחֶסֶד וּבְרַחֲמִים, הוּא נֹתֵן לֶחֶם לְכָל־בָּשָׂר כִּי לְעוֹלָם חַסְדּוֹ וּבְטוּבוֹ הַגָּדוֹל תָּמִיד לֹא חָסַר לָנוּ וְאַל יֶחְסַר לָנוּ מָזוֹן (תָּמִיד) לְעוֹלָם וָעֶד בַּעֲבוּר שְׁמוֹ הַגָּדוֹל כִּי הוּא אֵל זָן וּמְפַרְנֵס לַכֹּל וּמֵטִיב לַכֹּל וּמֵכִין מָזוֹן לְכָל־בְּרִיּוֹתָיו אֲשֶׁר בָּרָא בָּרוּךְ אַתָּה יְיָ הַזָּן אֶת הַכֹּל.
""",
            english: """
Blessed are You, Lord our God, King of the universe, who nourishes the entire world with His goodness, with grace, with kindness and with mercy. He gives bread to all flesh, for His kindness endures forever. And through His great goodness, we have never lacked, and may we never lack nourishment, for all eternity, for the sake of His great name. For He is God who nourishes and sustains all, and does good to all, and prepares food for all His creatures which He created. Blessed are You, Lord, who nourishes all.
""",
            category: .mainBlessings
        ),
        BirkatHamazonSection(
            title: "ברכת הארץ",
            hebrew: """
נוֹדֶה לְּךָ יְהוָה אֱלֹהֵינוּ עַל שֶׁהִנְחַלְתָּ לַאֲבוֹתֵינוּ אֶרֶץ חֶמְדָּה טוֹבָה וּרְחָבָה וְעַל שֶׁהוֹצֵאתָנוּ יְיָ אֱלֹהֵינוּ מֵאֶרֶץ מִצְרַיִם וּפְדִיתָנוּ מִבֵּית עֲבָדִים וְעַל בְּרִיתְךָ שֶׁחָתַמְתָּ בִּבְשָׂרֵנוּ וְעַל תּוֹרָתְךָ שֶׁלִּמַּדְתָּנוּ וְעַל חֻקֶּיךָ שֶׁהוֹדַעְתָּנוּ וְעַל חַיִּים חֵן וָחֶסֶד שֶׁחוֹנַנְתָּנוּ, וְעַל אֲכִילַת מָזוֹן שָׁאַתָּה זָן וּמְפַרְנֵס אוֹתָנוּ תָּמִיד, בְּכָל יוֹם וּבְכָל עֵת וּבְכָל שָׁעָה.

וְעַל הַכֹּל יְיָ אֱלֹהֵינוּ אֲנַחְנוּ מוֹדִים לָךְ וּמְבָרְכִים אוֹתָךְ, יִתְבָּרַךְ שִׁמְךָ בְּפִי כָּל חַי תָּמִיד לְעוֹלָם וָעֶד, כַּכָּתוּב: וְאָכַלְתָּ וְשָׂבַעְתָּ, וּבֵרַכְתָּ אֶת יְיָ אֱלֹהֶיךָ עַל הָאָרֶץ הַטּוֹבָה אֲשֶּׁר נָתַן לָךְ. בָּרוּךְ אַתָּה יְיָ, עַל הָאָרֶץ וְעַל הַמָּזוֹן.
""",
            english: """
We thank You, Lord our God, for having given as a heritage to our fathers a desirable, good and spacious land. And for having brought us out, Lord our God, from the land of Egypt and redeemed us from the house of bondage. And for Your covenant which You sealed in our flesh. And for Your Torah which You taught us. And for Your statutes which You made known to us. And for the life, grace and kindness which You graciously bestowed upon us. And for the eating of food with which You constantly nourish and sustain us every day, every moment and every hour.

For all this, Lord our God, we thank You and bless You. May Your name be blessed by the mouth of every living being, constantly and forever, as it is written: 'When you have eaten and are satisfied, you shall bless the Lord your God for the good land which He has given you.' Blessed are You, Lord, for the land and for the food.
""",
            category: .mainBlessings
        ),
        BirkatHamazonSection(
            title: "על הניסים לחנוכה",
            hebrew: """
בַּחֲנֻכָּה וּבִפּוּרִים אוֹמְרִים כָּאן עַל הַנִּסִּים
עַל הַנִּסִּים וְעַל הַפֻּרְקָן וְעַל הַגְּבוּרוֹת וְעַל הַתְּשׁוּעוֹת וְעַל הַמִּלְחָמוֹת שֶׁעָשִׂיתָ לַאֲבוֹתֵינוּ בַּיָּמִים הָהֵם בַּזְּמַן הַזֶּה.

בִּימֵי מַתִּתְיָהו בֶן יוֹחָנָן כֹּהֵן גָּדוֹל חַשְׁמוֹנָאִי וּבָנָיו כְּשֶׁעָמְדָה מַלְכוּת יָוָן הָרְשָׁעָה עַל עַמְּךָ יִשְׂרָאֵל לְהַשְׁכִּיחָם מִתּוֹרָתָךְ וּלְהַעֲבִירָם מֵחֻקֵּי רְצוֹנָךְ וְאַתָּה בְּרַחֲמֶיךָ הָרַבִּים עָמַדְתָּ לָהֶם בְּעֵת צָרָתָם רַבְתָּ אֶת רִיבָם דַּנְתָּ אֶת דִּינָם נָקַמְתָּ אֶת נִקְמָתָם מָסַרְתָּ גִבּוֹרִים בְּיַד חַלָּשִׁים וְרַבִּים בְּיַד מְעַטִּים וּטְמֵאִים בְּיַד טְהוֹרִים וּרְשָׁעִים בְּיַד צַדִּיקִים וְזֵדִים בְּיַד עוֹסְקֵי תוֹרָתֶךָ וּלְךָ עָשִׂיתָ שֵׁם גָּדוֹל וְקָדוֹשׁ בְּעוֹלָמָךְ וּלְעַמְּךָ יִשְׂרָאֵל עָשִׂיתָ תְּשׁוּעָה גְדוֹלָה וּפֻרְקָן כְּהַיּוֹם הַזֶּה וְאַחַר כָּךְ בָּאוּ בָנֶיךָ לִדְבִיר בֵּיתֶךָ וּפִנּוּ אֶת הֵיכָלֶךָ וְטִהֲרוּ אֶת־מִקְדָּשֶׁךָ וְהִדְלִיקוּ נֵרוֹת בְּחַצְרוֹת קָדְשֶׁךָ וְקָבְעוּ שְׁמוֹנַת יְמֵי חֲנֻכָּה אֵלּוּ לְהוֹדוֹת וּלְהַלֵּל לְשִׁמְךָ הַגָּדוֹל.
""",
            english: """
For the miracles, and for the salvation, and for the mighty deeds, and for the victories, and for the battles which You performed for our forefathers in those days, at this time. In the days of Mattathias, the son of Johanan the High Priest, the Hasmonean and his sons, when the wicked Greek kingdom rose up against Your people Israel to make them forget Your Torah and compel them to stray from the statutes of Your will. But You in Your great mercy stood up for them in the time of their distress. You took up their grievance, judged their claim, and avenged their wrong. You delivered the strong into the hands of the weak, the many into the hands of the few, the impure into the hands of the pure, the wicked into the hands of the righteous, and the wanton into the hands of the diligent students of Your Torah. For Yourself You made a great and holy name in Your world, and for Your people Israel you worked a great victory and salvation as this very day. Thereafter, Your children came to the Holy of Holies of Your House, cleansed Your Temple, purified Your Sanctuary, kindled lights in Your holy courtyards, and instituted these eight days of Chanukah to give thanks and praise to Your great name.
""",
            category: .additions,
            collapsedSummaryHebrew: "עַל הַנִּסִּים לַחֲנֻכָּה — נֶאֱמַר בַּחֲנֻכָּה (וּבִמְקוֹם הַמַּתְאִים בִּבְרָכַת הַמָּזוֹן). הַקִּישׁוּ לְהַרְחִיב.",
            collapsedSummaryEnglish: "Al haNissim for Chanukah — recited on Chanukah when appropriate in Birkat Hamazon. Tap to show full text."
        ),
        BirkatHamazonSection(
            title: "על הניסים לפורים",
            hebrew: """
בִּימֵי מָרְדְּכַי וְאֶסְתֵּר בְּשׁוּשַׁן הַבִּירָה כְּשֶׁעָמַד עֲלֵיהֶם הָמָן הָרָשָׁע בִּקֵּשׁ לְהַשְׁמִיד לַהֲרוֹג וּלְאַבֵּד אֶת־כָּל־הַיְּהוּדִים מִנַּעַר וְעַד זָקֵן טַף וְנָשִׁים בְּיוֹם אֶחָד בִּשְׁלֹשָׁה עָשָׂר לְחֹדֶשׁ שְׁנֵים עָשָׂר הוּא חֹדֶשׁ אֲדָר וּשְׁלָלָם לָבוֹז וְאַתָּה בְּרַחֲמֶיךָ הָרַבִּים הֵפַרְתָּ אֶת עֲצָתוֹ וְקִלְקַלְתָּ אֶת מַחֲשַׁבְתּוֹ וַהֲשֵׁבוֹתָ־לוֹ גְמוּלוֹ בְרֹאשׁוֹ וְתָלוּ אוֹתוֹ וְאֶת בָּנָיו עַל הָעֵץ וְעָשִׂיתָ עִמָּהֶם נִסִּים וְנִפְלָאוֹת וְנוֹדֶה לְשִׁמְךָ הַגָּדוֹל סֶלָה.
""",
            english: """
In the days of Mordechai and Esther, in Shushan the capital, when the wicked Haman rose up against them and sought to destroy, to slay, and to exterminate all the Jews, young and old, infants and women, on a single day, on the thirteenth day of the twelfth month, which is the month of Adar, and to take their spoils for plunder. But You, in Your abundant mercy, nullified his counsel and frustrated his intention and caused his design to return upon his own head, and they hanged him and his sons on the gallows. For all these miracles we thank Your great name.
""",
            category: .additions,
            collapsedSummaryHebrew: "עַל הַנִּסִּים לְפוּרִים — נֶאֱמַר בְּפוּרִים. הַקִּישׁוּ לְהַרְחִיב.",
            collapsedSummaryEnglish: "For Purim — recited on Purim in Birkat Hamazon. Tap to show full text."
        ),
        BirkatHamazonSection(
            title: "בונה ירושלים",
            hebrew: """
רַחֶם נָא יְיָ אֱלֹהֵינוּ עַל יִשְׂרָאֵל עַמֶּךָ, וְעַל יְרוּשָׁלַיִם עִירֶךָ, וְעַל צִיּוֹן מִשְׁכַּן כְּבוֹדֶךָ, וְעַל מַלְכוּת בֵּית דָּוִד מְשִׁיחֶךָ, וְעַל הַבַּיִת הַגָדוֹל וְהַקָדוֹשׁ שֶׁנִּקְרָא שִׁמְךָ עָלָיו. אֱלֹהֵינוּ, אָבִינוּ, רְעֵנוּ, זוּנֵנוּ, פַרְנְסֵנוּ וְכַלְכְּלֵנוּ וְהַרְוִיחֵנוּ, וְהַרְוַח לָנוּ יְיָ אֱלֹהֵינוּ מְהֵרָה מִכָּל צָרוֹתֵינוּ. וְנָא אַל תַּצְרִיכֵנוּ יְיָ אֱלֹהֵינוּ, לֹא לִידֵי מַתְּנַת בָּשָׂר וָדָם וְלֹא לִידֵי הַלְוָאָתָם, כִּי אִם לְיָדְךָ הַמְּלֵאָה הַפְּתוּחָה הַקְּדוֹשָׁה וְהָרְחָבָה, שֶׁלֹא נֵבוֹשׁ וְלֹא נִכָּלֵם לְעוֹלָם וָעֶד.
""",
            english: """
Have mercy, Lord our God, on Israel Your people, on Jerusalem Your city, on Zion the abode of Your glory, on the kingdom of the house of David Your anointed, and on the great and holy Temple that bears Your name. Our God, our Father, tend us, feed us, sustain us, support us, relieve us, and grant us speedy relief, Lord our God, from all our troubles. Please do not make us dependent, Lord our God, on the gifts or loans of human beings, but only on Your full, open, holy and generous hand, so that we may never be shamed or humiliated.
""",
            category: .mainBlessings
        ),
        BirkatHamazonSection(
            title: "יעלה ויבוא",
            hebrew: """
אֱלֹהֵינוּ וֵאלֹהֵי אֲבוֹתֵינוּ, יַעֲלֶה וְיָבֹא יַגִּיעַ יֵרָאֶה וְיֵרָצֶה יִשָּׁמַע יִפָּקֵד וְיִזָּכֵר זִכְרוֹנֵנוּ וּפִקְדוֹנֵנוּ וְזִכְרוֹן אֲבוֹתֵינוּ, וְזִכְרוֹן מָשִׁיחַ בֶּן דָּוִד עַבְדֶּךָ וְזִכְרוֹן יְרוּשָׁלַיִם עִיר קָדְשֶׁךָ וְזִכְרוֹן כָּל עַמְּךָ בֵּית יִשְׂרָאֵל לְפָנֶיךָ, לִפְלֵטָה לְטוֹבָה, לְחֵן לְחֶסֶד וּלְרַחֲמִים לְחַיִּים וּלְשָׁלוֹם בְּיוֹם
בְּרֹאשׁ חֹדֶשׁ: רֹאשׁ הַחֹדֶשׁ
בַּפֶּסַח: חַג הַמַּצּוֹת
בַּסֻּכּוֹת: חַג הַסֻּכּוֹת
זָכְרֵנוּ יְהוָה אֱלֹהֵינוּ בּוֹ לְטוֹבָה, וּפָקְדֵנוּ בוֹ לִבְרָכָה, וְהוֹשִׁיעֵנוּ בוֹ לְחַיִּים, וּבִדְבַר יְשׁוּעָה וְרַחֲמִים חוּס וְחָנֵּנוּ, וְרַחֵם עָלֵינוּ, וְהוֹשִׁיעֵנוּ כִּי אֵלֶיךָ עֵינֵינוּ, כִּי אֵל מֶלֶךְ חַנּוּן וְרַחוּם אָתָּה.
""",
            english: """
Our God and God of our fathers, may there rise, come, reach, appear, be favored, be heard, be regarded, and be remembered before You our remembrance and consideration, and the remembrance of our fathers, and the remembrance of Messiah son of David Your servant, and the remembrance of Jerusalem Your holy city, and the remembrance of all Your people the house of Israel, for deliverance, for goodness, for grace, for kindness, for mercy, for life, and for peace on this day of:
[On Rosh Chodesh: the New Moon]
[On Passover: the Festival of Matzot]
[On Sukkot: the Festival of Sukkot]
Remember us, Lord our God, on this day for good; consider us on this day for blessing; save us on this day for life. With a word of salvation and mercy spare us and be gracious to us; have mercy on us and save us, for our eyes are turned to You, because You are God, the gracious and merciful King.
""",
            category: .additions,
            collapsedSummaryHebrew: "יַעֲלֶה וְיָבֹא — נֶאֱמַר בְּרֹאשׁ חֹדֶשׁ, בַּפֶּסַח, בַּסֻּכּוֹת וּבִשְׁאָר מוֹעֲדִים. הַקִּישׁוּ לְהַרְחִיב.",
            collapsedSummaryEnglish: "Yaaleh VeYavo — Rosh Chodesh, Pesach, Sukkot, and other holy days. Tap to expand."
        ),
        BirkatHamazonSection(
            title: "וּבְנֵה יְרוּשָׁלַיִם",
            hebrew: """
וּבְנֵה יְרוּשָׁלַיִם עִיר הַקֹּדֶשׁ בִּמְהֵרָה בְיָמֵינוּ. בָּרוּךְ אַתָּה יְיָ, בּוֹנֵה בְרַחֲמָיו יְרוּשָׁלַיִם. אָמֵן.
""",
            english: """
And rebuild Jerusalem the holy city speedily in our days. Blessed are You, Lord, who in His mercy rebuilds Jerusalem. Amen.
""",
            category: .mainBlessings
        ),
        BirkatHamazonSection(
            title: "הטוב והמטיב",
            hebrew: """
בָּרוּךְ אַתָּה יְיָ אֱלֹהֵינוּ, מֶלֶךְ הָעוֹלָם, הָאֵל אָבִינוּ, מַלְכֵּנוּ, אַדִירֵנוּ, בּוֹרְאֵנוּ, גֹּאֲלֵנוּ, יוֹצְרֵנוּ, קְדוֹשֵׁנוּ קְדוֹשׁ יַעֲקֹב, רוֹעֵנוּ רוֹעֵה יִשְׂרָאֵל, הַמֶּלֶךְ הַטּוֹב וְהַמֵּיטִיב לַכֹּל, שֶׁבְּכָל יוֹם וָיוֹם הוּא הֵיטִיב, הוּא מֵיטִיב, הוּא יֵיטִיב לָנוּ, הוּא גְמָלָנוּ, הוּא גוֹמְלֵנוּ, הוּא יִגְמְלֵנוּ לָעַד, לְחֵן וּלְחֶסֶד וּלְרַחֲמִים וּלְרֶוַח הַצָּלָה וְהַצְלָחָה, בְּרָכָה וִישׁוּעָה, נֶחָמָה פַּרְנָסָה וְכַלְכָּלָה וְרַחֲמִים וְחַיִּים וְשָׁלוֹם, וְכָל טוֹב; וּמִכָּל טוּב לְעוֹלָם אַל יְחַסְּרֵנוּ.
""",
            english: """
Blessed are You, Lord our God, King of the universe, God our Father, our King, our Mighty one, our Creator, our Redeemer, our Maker, our Holy One, the Holy One of Jacob, our Shepherd, the Shepherd of Israel, the King who is good and does good to all. For each and every day He has done good, He does good, and He will do good to us. He has bestowed, He bestows, and He will forever bestow upon us grace, kindness and mercy, relief, salvation and success, blessing and help, consolation, sustenance and nourishment, compassion, life, peace and all good; and may He never let us lack any good.
""",
            category: .mainBlessings
        ),
        BirkatHamazonSection(
            title: "הרחמן",
            hebrew: """
הָרַחֲמָן הוּא יִמְלוֹךְ עָלֵינוּ לְעוֹלָם וָעֶד. הָרַחֲמָן הוּא יִתְבָּרַךְ בַּשָּׁמַיִם וּבָאָרֶץ. הָרַחֲמָן הוּא יִשְׁתַּבַּח לְדוֹר דּוֹרִים, וְיִתְפָּאַר בָּנוּ לָעַד וּלְנֵצַח נְצָחִים, וְיִתְהַדַּר בָּנוּ לָעַד וּלְעוֹלְמֵי עוֹלָמִים.
הָרַחֲמָן הוּא יְפַרְנְסֵנוּ בְּכָבוֹד. הָרַחֲמָן הוּא יִשְׁבּוֹר עֻלֵּנוּ מֵעַל צַּוָּארֵנוּ, וְהוּא יוֹלִיכֵנוּ קוֹמְמִיוּת לְאַרְצֵנוּ. הָרַחֲמָן הוּא יִשְׁלַח לָנוּ בְּרָכָה מְרֻבָּה בַּבַּיִת הַזֶּה, וְעַל שֻׁלְחָן זֶה שֶׁאָכַלְנוּ עָלָיו. הָרַחֲמָן הוּא יִשְׁלַח לָנוּ אֶת אֵלִיָּהוּ הַנָּבִיא זָכוּר לַטּוֹב, וִיבַשֵּׂר לָנוּ בְּשׂוֹרוֹת טוֹבוֹת יְשׁוּעוֹת וְנֶחָמוֹת.

בְּבֵית אָבִיו אוֹמֵר: הָרַחֲמָן הוּא יְבָרֵךְ אֶת אָבִי מוֹרִי בַּעַל הַבַּיִת הַזֶּה, וְאֶת אִמִּי מוֹרָתִי בַּעֲלַת הַבַּיִת הַזֶּה.
נָשׂוּי אוֹמֵר: הָרַחֲמָן הוּא יְבָרֵךְ אוֹתִי, (אִם אָבִיו וְאִמּוֹ בַּחַיִּים: וְאֶת אָבִי מוֹרִי, וְאֶת אִמִּי מוֹרָתִי,) וְאֶת אִשְׁתִּי, וְאֶת זַרְעִי, וְאֶת כָּל אֲשֶׁר לִי.
נְשׂוּאָה אוֹמֶרֶת: הָרַחֲמָן הוּא יְבָרֵךְ אוֹתִי, (אִם אָבִיהָ וְאִמָּהּ בַּחַיִּים: וְאֶת אָבִי מוֹרִי, וְאֶת אִמִּי מוֹרָתִי,) וְאֶת בַּעְלִי, וְאֶת זַרְעִי, וְאֶת כָּל אֲשֶׁר לִי.
אוֹרֵחַ אוֹמֵר: הָרַחֲמָן הוּא יְבָרֵךְ אֶת בַּעַל הַבַּיִת הַזֶּה וְאֶת בַּעֲלַת הַבַּיִת הַזֶּה, אוֹתָם וְאֶת בֵּיתָם וְאֶת זַרְעָם וְאֶת כָּל אֲשֶׁר לָהֶם.
""",
            english: """
May the Merciful One reign over us forever and ever. May the Merciful One be blessed in heaven and on earth. May the Merciful One be praised for all generations, and be glorified through us forever and all eternity, and honored through us forever and ever. May the Merciful One grant us an honorable livelihood. May the Merciful One break the yoke from our neck and lead us upright to our land. May the Merciful One send abundant blessing into this house and upon this table at which we have eaten. May the Merciful One send us Elijah the Prophet - may he be remembered for good - who will bring us good tidings of salvation and comfort.

[In father's house:] May the Merciful One bless my father, my teacher, the master of this house, and my mother, my teacher, the mistress of this house.
[If married man:] May the Merciful One bless me (and my father and my mother) and my wife and my children and all that is mine.
[If married woman:] May the Merciful One bless me (and my father and my mother) and my husband and my children and all that is mine.
[If guest:] May the Merciful One bless the master and mistress of this house, them, and their household, and their children, and all that is theirs.
""",
            category: .additions
        ),
        BirkatHamazonSection(
            title: "הרחמן — ראש חודש וסוכות",
            hebrew: """
בְּרֹאשׁ חֹדֶשׁ: הָרַחֲמָן הוּא יְחַדֵּשׁ עָלֵינוּ אֶת הַחֹדֶשׁ הַזֶּה לְטוֹבָה וְלִבְרָכָה.
בַּסֻּכּוֹת: הָרַחֲמָן הוּא יָקִים לָנוּ אֶת סֻכַּת דָּוִד הַנּוֹפֶלֶת.
""",
            english: """
[On Rosh Chodesh:] May the Merciful One renew for us this month for good and for blessing.
[On Sukkot:] May the Merciful One restore for us the fallen sukkah of David.
""",
            category: .additions,
            collapsedSummaryHebrew: "רֹאשׁ חֹדֶשׁ וְסֻכּוֹת — שְׁנֵי פְּסוּקֵי ״הָרַחֲמָן״ נוֹסָפִים (לֹא בִּשְׁאָר יָמִים). הַקִּישׁוּ לְהַרְחִיב.",
            collapsedSummaryEnglish: "Rosh Chodesh & Sukkot — two extra Harachaman lines (not on ordinary days). Tap to show full text."
        ),
        BirkatHamazonSection(
            title: "הרחמן — סיום",
            hebrew: """
הָרַחֲמָן הוּא יְזַכֵּנוּ לִימוֹת הַמָּשִׁיחַ וּלְחַיֵּי הָעוֹלָם הַבָּא. מַגְדִּיל יְשׁוּעוֹת מַלְכּוֹ, וְעֹשֶׂה חֶסֶד לִמְשִׁיחוֹ, לְדָוִד וּלְזַרְעוֹ עַד עוֹלָם. עֹשֶׂה שָׁלוֹם בִּמְרוֹמָיו, הוּא יַעֲשֶׂה שָׁלוֹם עָלֵינוּ וְעַל כָּל יִשְׂרָאֵל. וְאָמְרוּ: אָמֵן.
""",
            english: """
May the Merciful One grant us the privilege of reaching the days of the Messiah and the life of the World to Come. He who makes great salvation for His king and shows kindness to His anointed one, to David and his descendants forever. He who makes peace in His heights, may He make peace for us and for all Israel. And say: Amen.
""",
            category: .additions
        ),
        BirkatHamazonSection(
            title: "פסוקים אחרונים",
            hebrew: """
יִרְאוּ אֶת יְיָ קְדֹשָׁיו, כִּי אֵין מַחְסוֹר לִירֵאָיו. כְּפִירִים רָשׁוּ וְרָעֵבוּ, וְדֹרְשֵׁי יְיָ לֹא יַחְסְרוּ כָל טוֹב. הוֹדוּ לַיְיָ כִּי טוֹב, כִּי לְעוֹלָם חַסְדּוֹ. פּוֹתֵחַ אֶת יָדֶךָ, וּמַשְׂבִּיעַ לְכָל חַי רָצוֹן. בָּרוּךְ הַגֶּבֶר אֲשֶׁר יִבְטַח בַּיְיָ, וְהָיָה יְיָ מִבְטַחוֹ. נַעַר הָיִיתִי גַּם זָקַנְתִּי, וְלֹא רָאִיתִי צַדִּיק נֶעֱזָב, וְזַרְעוֹ מְבַקֶּשׁ לָחֶם. יְיָ עֹז לְעַמּוֹ יִתֵּן, יְיָ יְבָרֵךְ אֶת עַמּוֹ בַשָּׁלוֹם.
""",
            english: """
Fear the Lord, you His holy ones, for those who fear Him suffer no want. Young lions may want and hunger, but those who seek the Lord shall not lack any good thing. Thank the Lord for He is good, His kindness endures forever. You open Your hand and satisfy the desire of every living thing. Blessed is the man who trusts in the Lord, and the Lord will be his security. I was young and now I am old, yet I have never seen a righteous man forsaken, nor his children begging for bread. The Lord will give strength to His people; The Lord will bless His people with peace.
""",
            category: .additions
        )
    ]
}
