import SwiftUI

struct TefilatHaderechView: View {
    @Environment(\.dismiss) private var dismiss
    @State private var fontSize: CGFloat = 18
    @State private var showEnglish = true
    @GestureState private var scale: CGFloat = 1.0

    private let hebrewText = """
    יְהִי רָצוֹן מִלְּפָנֶיךָ ה׳ אֱלֹהֵינוּ וֵאלֹהֵי אֲבוֹתֵינוּ שֶׁתּוֹלִיכֵנוּ לְשָׁלוֹם וְתַצְעִידֵנוּ לְשָׁלוֹם וְתַדְרִיכֵנוּ לְשָׁלוֹם׃
    וְתִסְמְכֵנוּ לְשָׁלוֹם וְתַגִּיעֵנוּ לִמְחוֹז חֶפְצֵנוּ לְחַיִּים וּלְשִׂמְחָה וּלְשָׁלוֹם׃
    וְתַצִּילֵנוּ מִכַּף כָּל אוֹיֵב וְאוֹרֵב וְלִסְטִים וְחַיּוֹת רָעוֹת בַּדֶּרֶךְ וּמִכָּל מִינֵי פֻּרְעָנִיּוֹת הַמִּתְרַגְּשׁוֹת לָבוֹא לָעוֹלָם׃
    וְתִשְׁלַח בְּרָכָה בְּכָל מַעֲשֵׂה יָדֵינוּ וְתִתְּנֵנוּ לְחֵן וּלְחֶסֶד וּלְרַחֲמִים בְּעֵינֶיךָ וּבְעֵינֵי כָל רוֹאֵינוּ׃
    וְתִשְׁמַע קוֹל תַּחֲנוּנֵינוּ כִּי אֵל שׁוֹמֵעַ תְּפִלָּה וְתַחֲנוּן אָתָּה׃
    בָּרוּךְ אַתָּה ה׳ שׁוֹמֵעַ תְּפִלָּה׃
    """

    private let englishText = """
May it be Your will, Lord our God and God of our ancestors, that You lead us toward peace, guide our footsteps toward peace, and make us reach our desired destination for life, gladness, and peace.
May You rescue us from the hand of every foe, ambush along the way, and from all manner of punishments that assemble to come to earth.
May You send blessing in our handiwork, and grant us grace, kindness, and mercy in Your eyes and in the eyes of all who see us.
May You hear the voice of our supplication because You are God Who hears prayer and supplication.
Blessed are You, Lord, Who hears prayer.
"""

    var body: some View {
        ZStack {
            LinearGradient(
                colors: [AppColor.parchmentTop, AppColor.parchmentMid, AppColor.parchmentBase],
                startPoint: .top,
                endPoint: .bottom
            )
            .ignoresSafeArea()

            VStack(spacing: 0) {
                MenuSheetHeaderBar(title: "תפילת הדרך", onClose: { dismiss() })
                MenuSheetGoldHairline()

                HStack {
                    Button(action: { fontSize = max(12, fontSize - 2) }) {
                        Label("Decrease text size", systemImage: "textformat.size.smaller")
                            .labelStyle(.iconOnly)
                            .foregroundColor(AppColor.navyPrimary)
                    }
                    .accessibilityLabel("Decrease text size")

                    Button(action: { fontSize = min(40, fontSize + 2) }) {
                        Label("Increase text size", systemImage: "textformat.size.larger")
                            .labelStyle(.iconOnly)
                            .foregroundColor(AppColor.navyPrimary)
                    }
                    .accessibilityLabel("Increase text size")

                    Spacer()

                    Button(action: { showEnglish.toggle() }) {
                        Text(showEnglish ? "Hide English" : "Show English")
                            .font(.system(size: 15, weight: .medium, design: .serif))
                            .foregroundColor(AppColor.navyPrimary)
                    }
                    .accessibilityLabel(showEnglish ? "Hide English translation" : "Show English translation")
                }
                .padding(.horizontal, 16)
                .padding(.vertical, 10)

                ScrollView {
                    VStack(spacing: 20) {
                        Text(hebrewText)
                            .font(.system(size: fontSize * scale))
                            .foregroundColor(AppColor.textPrimary)
                            .multilineTextAlignment(.trailing)
                            .frame(maxWidth: .infinity, alignment: .trailing)
                            .padding(.horizontal, 8)
                            .accessibilityLabel("Hebrew text of Tefilat Haderech")

                        if showEnglish {
                            Text(englishText)
                                .font(.system(size: fontSize * scale))
                                .foregroundColor(AppColor.textPrimary)
                                .multilineTextAlignment(.leading)
                                .frame(maxWidth: .infinity, alignment: .leading)
                                .padding(.horizontal, 8)
                                .accessibilityLabel("English translation of Tefilat Haderech")
                        }
                    }
                    .padding(.vertical, 12)
                    .gesture(
                        MagnificationGesture()
                            .updating($scale) { currentState, gestureState, _ in
                                gestureState = currentState
                            }
                    )
                }
            }
        }
    }
}
