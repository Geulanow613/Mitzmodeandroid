import Foundation

/// Builds Bracha Me'ein Shalosh (Al HaMichya) from food + calendar selections — mirrors Android `MeinShaloshTextEngine.kt`.
struct MeinShaloshSelection: Equatable {
    var hasMezonot = false
    var hasWine = false
    var hasFruit = false
    var isRoshChodesh = false
    var isPesach = false
    var isSukkot = false

    var hasAnyFood: Bool { hasMezonot || hasWine || hasFruit }
}

enum MeinShaloshLanguage {
    case hebrew
    case english
}

enum MeinShaloshTextEngine {
    private static let hebrewWinePhrase = "עַל הַגֶּפֶן וְעַל פְּרִי הַגֶּפֶן"
    private static let englishWinePhrase = "the vine and the fruit of the vine"
    private static let hebrewTreePhrase = "עַל הָעֵץ וְעַל פְּרִי הָעֵץ"
    private static let englishTreePhrase = "the tree and the fruit of the tree"

    static func build(_ selection: MeinShaloshSelection, language: MeinShaloshLanguage) -> String {
        guard selection.hasAnyFood else {
            switch language {
            case .hebrew:
                return "בחרו מה אכלתם או שתיתם כדי לבנות את הברכה."
            case .english:
                return "Select what you ate or drank to build your after-blessing."
            }
        }
        switch language {
        case .hebrew: return buildHebrew(selection)
        case .english: return buildEnglish(selection)
        }
    }

    // MARK: - Hebrew

    private static func buildHebrew(_ selection: MeinShaloshSelection) -> String {
        let foodOpening = hebrewFoodParts(selection).joined(separator: " וְ")
        let thanks = "וְנוֹדֶה לְךָ עַל הָאָרֶץ וְ" + hebrewThanksParts(selection).joined(separator: " וְ")
        let closing = "עַל הָאָרֶץ וְ" + hebrewClosingParts(selection).joined(separator: " וְ")
        let calendar = hebrewCalendarInserts(selection)

        var text = "בָּרוּךְ אַתָּה ה' אֱלֹקֵינוּ מֶלֶךְ הָעוֹלָם \(foodOpening) עַל אֶרֶץ חֶמְדָּה טוֹבָה וּרְחָבָה שֶׁרָצִיתָ וְהִנְחַלְתָּ לַאֲבוֹתֵינוּ לֶאֱכוֹל מִפִּרְיָהּ וְלִשְׂבּוֹעַ מִטּוּבָהּ. "
        text += "רַחֵם ה' אֱלֹקֵינוּ עַל יִשְׂרָאֵל עַמֶּךָ וְעַל יְרוּשָׁלַיִם עִירֶךָ וְעַל צִיּוֹן מִשְׁכַּן כְּבוֹדֶךָ וְעַל מִזְבְּחֶךָ וְעַל הֵיכָלֶךָ. "
        if !calendar.isEmpty {
            text += calendar + " "
        }
        text += "וּבְנֵה יְרוּשָׁלַיִם עִיר הַקֹּדֶשׁ בִּמְהֵרָה בְיָמֵינוּ וְהַעֲלֵנוּ לְתוֹכָהּ וְשַׂמְּחֵנוּ בְּבִנְיָנָהּ וְנֹאכַל מִפִּרְיָהּ וְנִשְׂבַּע מִטּוּבָהּ. "
        text += "וּנְבָרֶכְךָ עָלֶיהָ בִּקְדֻשָּׁה וּבְטָהֳרָה כִּי אַתָּה ה' טוֹב וּמֵטִיב לַכֹּל \(thanks). בָּרוּךְ אַתָּה ה' \(closing)."
        return text
    }

    private static func hebrewFoodParts(_ selection: MeinShaloshSelection) -> [String] {
        var parts: [String] = []
        if selection.hasMezonot { parts.append("עַל הַמִּחְיָה וְעַל הַכַּלְכָּלָה") }
        if selection.hasWine { parts.append(hebrewWinePhrase) }
        if selection.hasFruit { parts.append(hebrewTreePhrase) }
        return parts
    }

    private static func hebrewThanksParts(_ selection: MeinShaloshSelection) -> [String] {
        var parts: [String] = []
        if selection.hasMezonot { parts.append("עַל הַמִּחְיָה וְעַל הַכַּלְכָּלָה") }
        if selection.hasWine { parts.append(hebrewWinePhrase) }
        if selection.hasFruit { parts.append(hebrewTreePhrase) }
        return parts
    }

    private static func hebrewClosingParts(_ selection: MeinShaloshSelection) -> [String] {
        var parts: [String] = []
        if selection.hasMezonot { parts.append("עַל הַמִּחְיָה") }
        if selection.hasWine { parts.append(hebrewWinePhrase) }
        if selection.hasFruit { parts.append(hebrewTreePhrase) }
        return parts
    }

    private static func hebrewCalendarInserts(_ selection: MeinShaloshSelection) -> String {
        guard selection.isRoshChodesh || selection.isPesach || selection.isSukkot else { return "" }
        return hebrewYaalehVeyavo(selection)
    }

    private static func hebrewYaalehVeyavo(_ selection: MeinShaloshSelection) -> String {
        let dayPhrase: String
        if selection.isPesach {
            dayPhrase = "בְּיוֹם חַג הַמַּצּוֹת הַזֶּה"
        } else if selection.isSukkot {
            dayPhrase = "בְּיוֹם חַג הַסֻּכּוֹת הַזֶּה"
        } else if selection.isRoshChodesh {
            dayPhrase = "בְּיוֹם רֹאשׁ חֹדֶשׁ הַזֶּה"
        } else {
            dayPhrase = "הַיּוֹם"
        }
        return "יַעֲלֶה וְיָבֹא וְיַגִּיעַ וְיֵרָאֶה וְיֵרָצֶה וְיִשָּׁמַע וְיִפָּקֵד וְיִזָּכֵר " +
            "זִכְרוֹנֵנוּ וְזִכְרוֹן אֲבוֹתֵינוּ, זִכְרוֹן מָשִׁיחַ בֶּן דָּוִד עַבְדֶּךָ, " +
            "זִכְרוֹן יְרוּשָׁלַיִם עִיר קָדְשֶׁךָ וּזְכִירַת כָּל עַמְּךָ בֵּית יִשְׂרָאֵל " +
            "לְחַיִּים טוֹבִים וּלְשָׁלוֹם \(dayPhrase)."
    }

    // MARK: - English

    private static func buildEnglish(_ selection: MeinShaloshSelection) -> String {
        let foodFor = joinEnglishList(englishFoodForParts(selection))
        let thanks = "and we thank You for the land and for " + joinEnglishList(englishThanksParts(selection))
        let closing = "for the land and for " + joinEnglishList(englishClosingParts(selection))
        let calendar = englishCalendarInserts(selection)

        var text = "Blessed are You, Lord our God, King of the universe, for \(foodFor), on the good and spacious land which You desired and caused our forefathers to inherit, that they might eat of its fruit and be satisfied with its goodness. "
        text += "Have mercy, Lord our God, on Israel Your people, on Jerusalem Your city, on Zion the abode of Your glory, on Your altar, and on Your Temple. "
        if !calendar.isEmpty {
            text += calendar + " "
        }
        text += "Rebuild Jerusalem the holy city speedily in our days, bring us up into it, and rejoice us in its rebuilding, that we may eat of its fruit and be satisfied with its goodness. "
        text += "We will bless You upon it in holiness and purity, for You, Lord, are good and do good to all. \(thanks). Blessed are You, Lord, \(closing)."
        return text
    }

    private static func englishFoodForParts(_ selection: MeinShaloshSelection) -> [String] {
        var parts: [String] = []
        if selection.hasMezonot { parts.append("the sustenance and the nourishment") }
        if selection.hasWine { parts.append(englishWinePhrase) }
        if selection.hasFruit { parts.append(englishTreePhrase) }
        return parts
    }

    private static func englishThanksParts(_ selection: MeinShaloshSelection) -> [String] {
        var parts: [String] = []
        if selection.hasMezonot { parts.append("the sustenance and the nourishment") }
        if selection.hasWine { parts.append(englishWinePhrase) }
        if selection.hasFruit { parts.append(englishTreePhrase) }
        return parts
    }

    private static func englishClosingParts(_ selection: MeinShaloshSelection) -> [String] {
        var parts: [String] = []
        if selection.hasMezonot { parts.append("the sustenance") }
        if selection.hasWine { parts.append(englishWinePhrase) }
        if selection.hasFruit { parts.append(englishTreePhrase) }
        return parts
    }

    private static func englishCalendarInserts(_ selection: MeinShaloshSelection) -> String {
        guard selection.isRoshChodesh || selection.isPesach || selection.isSukkot else { return "" }
        return englishYaalehVeyavo(selection)
    }

    private static func englishYaalehVeyavo(_ selection: MeinShaloshSelection) -> String {
        let dayPhrase: String
        if selection.isPesach {
            dayPhrase = "on this Festival of Matzot"
        } else if selection.isSukkot {
            dayPhrase = "on this Festival of Sukkot"
        } else if selection.isRoshChodesh {
            dayPhrase = "on this Rosh Chodesh"
        } else {
            dayPhrase = "this day"
        }
        return "May our remembrance and the remembrance of our fathers, the remembrance of Messiah son of David Your servant, " +
            "the remembrance of Jerusalem Your holy city, and the remembrance of all Your people the house of Israel " +
            "rise and come, reach and be seen, be favored and heard, be visited and be remembered for good life and peace \(dayPhrase). "
    }

    static func joinEnglishList(_ items: [String]) -> String {
        switch items.count {
        case 0: return ""
        case 1: return items[0]
        case 2: return "\(items[0]) and \(items[1])"
        default:
            let head = items.dropLast().joined(separator: ", ")
            return "\(head), and \(items.last!)"
        }
    }
}
