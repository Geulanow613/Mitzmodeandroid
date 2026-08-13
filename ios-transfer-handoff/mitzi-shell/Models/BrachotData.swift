import Foundation

/// Static blessing entries — content and order match Android `BrachotDialog.kt`.
struct BrachaEntry: Identifiable {
    let id: String
    let name: String
    let description: String
    let hebrew: String
    let english: String

    static func nameTransliterationKey(_ name: String) -> String { "translit::name::\(name)" }
    static func textTransliterationKey(_ english: String) -> String { "translit::text::\(english)" }
}

enum BrachotData {
    static let entries: [BrachaEntry] = [
        BrachaEntry(
            id: "asher_yatzar",
            name: "Asher Yatzar",
            description: "After using the bathroom — each time you finish. If you are ill (e.g. diarrhea) and may need to go again right away, wait until you are reasonably sure you have finished for that round before saying it.",
            hebrew: "בָּרוּךְ אַתָּה ה' אֱלֹהֵינוּ מֶלֶךְ הָעוֹלָם אֲשֶׁר יָצַר אֶת הָאָדָם בְּחָכְמָה וּבָרָא בוֹ נְקָבִים נְקָבִים חֲלוּלִים חֲלוּלִים גָּלוּי וְיָדוּעַ לִפְנֵי כִסֵּא כְבוֹדֶךָ שֶׁאִם יִפָּתֵחַ אֶחָד מֵהֶם אוֹ יִסָּתֵם אֶחָד מֵהֶם אִי אֶפְשַׁר לְהִתְקַיֵּם וְלַעֲמוֹד לְפָנֶיךָ אֲפִילוּ שָׁעָה אֶחָת בָּרוּךְ אַתָּה ה' רוֹפֵא כָל בָּשָׂר וּמַפְלִיא לַעֲשׂוֹת",
            english: "Blessed are You, Lord our God, King of the universe, who formed man with wisdom and created within him many openings and many hollow spaces. It is obvious and known before Your Throne of Glory that if even one of them would be opened, or if even one of them would be sealed, it would be impossible to survive and to stand before You even for one hour. Blessed are You, Lord, who heals all flesh and acts wondrously."
        ),
        BrachaEntry(
            id: "hamotzi",
            name: "Hamotzi",
            description: "For bread",
            hebrew: "בָּרוּךְ אַתָּה ה' אֱלֹהֵינוּ מֶלֶךְ הָעוֹלָם הַמּוֹצִיא לֶחֶם מִן הָאָרֶץ",
            english: "Blessed are You, Lord our God, King of the universe, who brings forth bread from the earth"
        ),
        BrachaEntry(
            id: "mezonot",
            name: "Mezonot",
            description: "Before eating: grain foods that are not bread — cake, cookies, crackers, pasta, cereal (from wheat, barley, spelt, oat, or rye)",
            hebrew: "בָּרוּךְ אַתָּה ה' אֱלֹהֵינוּ מֶלֶךְ הָעוֹלָם בּוֹרֵא מִינֵי מְזוֹנוֹת",
            english: "Blessed are You, Lord our God, King of the universe, who creates various kinds of sustenance"
        ),
        BrachaEntry(
            id: "hagafen",
            name: "Hagafen",
            description: "For wine and grape juice",
            hebrew: "בָּרוּךְ אַתָּה ה' אֱלֹהֵינוּ מֶלֶךְ הָעוֹלָם בּוֹרֵא פְּרִי הַגָּפֶן",
            english: "Blessed are You, Lord our God, King of the universe, who creates the fruit of the vine"
        ),
        BrachaEntry(
            id: "haetz",
            name: "Ha'etz",
            description: "Before eating: tree fruits (not shivat ha-minim — use Ha'adama for ground produce)",
            hebrew: "בָּרוּךְ אַתָּה ה' אֱלֹהֵינוּ מֶלֶךְ הָעוֹלָם בּוֹרֵא פְּרִי הָעֵץ",
            english: "Blessed are You, Lord our God, King of the universe, who creates the fruit of the tree"
        ),
        BrachaEntry(
            id: "haadama",
            name: "Ha'adama",
            description: "For vegetables and ground fruits",
            hebrew: "בָּרוּךְ אַתָּה ה' אֱלֹהֵינוּ מֶלֶךְ הָעוֹלָם בּוֹרֵא פְּרִי הָאֲדָמָה",
            english: "Blessed are You, Lord our God, King of the universe, who creates the fruit of the earth"
        ),
        BrachaEntry(
            id: "shehakol",
            name: "Shehakol",
            description: "For everything else (meat, dairy, drinks)",
            hebrew: "בָּרוּךְ אַתָּה ה' אֱלֹהֵינוּ מֶלֶךְ הָעוֹלָם שֶׁהַכֹּל נִהְיָה בִּדְבָרוֹ",
            english: "Blessed are You, Lord our God, King of the universe, by whose word all things came to be"
        ),
        BrachaEntry(
            id: "borei_nefashot",
            name: "Borei Nefashot",
            description: "After-blessing for foods/drinks that don't require Birkat Hamazon or Bracha Me'ein Shalosh",
            hebrew: "בָּרוּךְ אַתָּה ה' אֱלֹהֵינוּ מֶלֶךְ הָעוֹלָם בּוֹרֵא נְפָשׁוֹת רַבּוֹת וְחֶסְרוֹנָן עַל כָּל מַה שֶּׁבָּרָאתָ לְהַחֲיוֹת בָּהֶם נֶפֶשׁ כָּל חָי בָּרוּךְ חֵי הָעוֹלָמִים",
            english: "Blessed are You, Lord our God, King of the universe, Who creates numerous living things with their deficiencies, for all that You have created with which to sustain the life of every being. Blessed is He who is the Life of the worlds"
        ),
        BrachaEntry(
            id: "shehecheyanu",
            name: "Shehecheyanu",
            description: "For new experiences, seasonal fruits, holidays",
            hebrew: "בָּרוּךְ אַתָּה ה' אֱלֹהֵינוּ מֶלֶךְ הָעוֹלָם שֶׁהֶחֱיָנוּ וְקִיְּמָנוּ וְהִגִּיעָנוּ לַזְּמַן הַזֶּה",
            english: "Blessed are You, Lord our God, King of the universe, who has granted us life, sustained us, and enabled us to reach this occasion"
        ),
        BrachaEntry(
            id: "oseh_maaseh",
            name: "Oseh Ma'aseh Bereishit",
            description: "On lightning, shooting stars, earthquakes, strong winds",
            hebrew: "בָּרוּךְ אַתָּה ה' אֱלֹהֵינוּ מֶלֶךְ הָעוֹלָם עוֹשֶׂה מַעֲשֵׂה בְרֵאשִׁית",
            english: "Blessed are You, Lord our God, King of the universe, who does the work of creation"
        ),
        BrachaEntry(
            id: "birkat_hailanot",
            name: "Birkat Ha'Ilanot",
            description: "Blessing on blossoming fruit trees during the month of Nissan",
            hebrew: "בָּרוּךְ אַתָּה ה' אֱלֹקֵינוּ מֶלֶךְ הָעוֹלָם שֶׁלֹּא חִסַּר בְּעוֹלָמוֹ כְּלוּם וּבָרָא בוֹ בְּרִיּוֹת טוֹבוֹת וְאִילָנוֹת טוֹבוֹת לֵהָנוֹת בָּהֶם בְּנֵי אָדָם",
            english: "Blessed are You, Lord our God, King of the Universe, who has made nothing lacking in His universe, and created within it good creatures and good trees to give pleasure to mankind"
        ),
    ]
}
