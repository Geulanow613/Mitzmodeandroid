import SwiftUI
import shared

/// Prayer liturgy line with bundled es/fr/ru overrides — mirrors Android `LiturgyTranslationText.kt`.
struct LiturgyTranslationText: View {
    let source: String
    var fontSize: CGFloat = 16
    var color: Color = AppColor.textPrimary
    var alignment: TextAlignment = .center

    @ObservedObject private var store = AppTranslationStore.shared
    @State private var display = ""

    var body: some View {
        Text(display)
            .font(.system(size: fontSize, weight: .regular, design: .serif))
            .foregroundColor(color)
            .multilineTextAlignment(alignment)
            .frame(maxWidth: .infinity, alignment: alignment == .center ? .center : .leading)
            .padding(.vertical, 8)
            .task(id: taskKey) {
                await refresh()
            }
    }

    private var taskKey: String {
        "\(source)|\(store.currentLanguage)"
    }

    @MainActor
    private func refresh() async {
        let lang = store.currentLanguage
        if lang == "en" || lang == "he" {
            display = source
            return
        }
        display = store.liturgyTranslation(source, languageCode: lang)
    }
}
