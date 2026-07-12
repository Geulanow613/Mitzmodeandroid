import SwiftUI

/// Central palette aligned with Android `MitzvahDialog` / premium parchment theme.
/// Section A (`MitzvahDialog.kt`): use these component values verbatim.
enum AppColor {
    static let parchmentTop = Color(red: 1.0, green: 0.996078431372549, blue: 0.9333333333333333)
    static let parchmentMid = Color(red: 1.0, green: 0.9725490196078431, blue: 0.8823529411764706)
    static let parchmentBase = Color(red: 1.0, green: 0.9411764705882353, blue: 0.8)
    static let goldBorder = Color(red: 0.7215686274509804, green: 0.5254901960784314, blue: 0.043137254901960784)
    static let goldBright = Color(red: 1.0, green: 0.8431372549019608, blue: 0.0)
    static let goldRay = Color(red: 1.0, green: 0.8431372549019608, blue: 0.0)
    /// Parchment grain wash (`MitzvahDialog` C2): `#EDD9A3`.
    static let parchmentGrain = Color(red: 0.9333333333333333, green: 0.8509803921568627, blue: 0.6392156862745098)
    /// Heirloom / link navy `#2B5BA8` (same channel as `navyPrimary`).
    static let navyBlue = Color(red: 0.16862745098039217, green: 0.3568627450980392, blue: 0.6588235294117647)
    /// Heirloom vertical gradient mid `#1F4685`.
    static let navyButtonMid = Color(red: 0.12156862745098039, green: 0.27450980392156865, blue: 0.5215686274509804)
    /// Heirloom vertical gradient deep `#12305A`.
    static let navyButtonDeep = Color(red: 0.07058823529411765, green: 0.18823529411764706, blue: 0.35294117647058826)
    /// Heirloom disabled gradient `#6E7B92`.
    static let disabledNavyFlat = Color(red: 0.43137254901960786, green: 0.4823529411764706, blue: 0.5725490196078431)
    /// Heirloom label cream `#FFF6D8`.
    static let mitzvahHeirloomLabel = Color(red: 1.0, green: 0.9647058823529412, blue: 0.8470588235294118)
    /// Inner hairline gold `#FFD56B` (Android `0xFFFFD56B`).
    static let goldHairline = Color(red: 1.0, green: 0.8352941176470589, blue: 0.4196078431372549)
    /// Android `MitzModeApp` home chrome — gold spot / rim `#FFD56B`.
    static let androidGoldSpot = Color(red: 1.0, green: 0.835, blue: 0.419)
    /// Android menu icon / outlined button label `#FFE082`.
    static let androidTextGold = Color(red: 1.0, green: 0.878, blue: 0.510)
    /// Android `DropdownMenu` parchment `#FFFBEE`.
    static let androidMenuDropdownFill = Color(red: 1.0, green: 0.984, blue: 0.933)
    /// Android “What’s a Mitzvah?” gradient mid `#D4A024`.
    static let androidGoldGradientMid = Color(red: 0.831, green: 0.627, blue: 0.141)
    static let navyPrimary = navyBlue
    static let navyMid = navyButtonMid
    static let navyDeep = navyButtonDeep
    /// Android `TextBrown` `#1A0A00`.
    static let textPrimary = Color(red: 0.10196078431372549, green: 0.0392156862745098, blue: 0.0)
    static let textMuted = Color(red: 0.35, green: 0.28, blue: 0.22)
    static let textSecondary = textMuted
    static let textOnDark = Color(red: 1.0, green: 0.965, blue: 0.847)
    static let scrim = Color.black.opacity(0.55)
    /// Mitzvah dialog: deep navy tint so the background stays rich (not flat gray) under gold rays.
    static let mitzvahDialogScrim = Color(red: 0.039, green: 0.078, blue: 0.157).opacity(0.44)
    static let disabledTone = Color(red: 0.431, green: 0.482, blue: 0.573)
}

enum AppRadius {
    static let card: CGFloat = 20
    static let pill: CGFloat = 22
    static let button: CGFloat = 25
}

enum AppStroke {
    static let hairline: CGFloat = 0.5
    static let thin: CGFloat = 1
    static let medium: CGFloat = 1.5
}

// MARK: - Checklist text size (Android `DailyMitzvotChecklist` A / a controls)

private struct ChecklistTextSizeKey: EnvironmentKey {
    static let defaultValue: CGFloat = 14
}

extension EnvironmentValues {
    var checklistTextSize: CGFloat {
        get { self[ChecklistTextSizeKey.self] }
        set { self[ChecklistTextSizeKey.self] = newValue }
    }
}
