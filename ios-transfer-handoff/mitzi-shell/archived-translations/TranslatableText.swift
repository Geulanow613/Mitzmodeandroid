import SwiftUI
import shared

/// SwiftUI equivalent of Android `TranslatableText` — uses bundled JSON first, then online translation.
struct TranslatableText: View {
    let source: String
    var font: Font = .body
    var weight: Font.Weight = .regular
    var color: Color = AppColor.textPrimary
    var alignment: TextAlignment = .leading
    var lineLimit: Int?

    @ObservedObject private var store = AppTranslationStore.shared
    @State private var display: String = ""

    var body: some View {
        Text(display)
            .font(font.weight(weight))
            .foregroundColor(color)
            .multilineTextAlignment(alignment)
            .lineLimit(lineLimit)
            .task(id: taskKey) {
                await refresh()
            }
    }

    private var taskKey: String {
        "\(source)|\(store.currentLanguage)|\(store.translationEnabled)|\(store.catalogGeneration)"
    }

    @MainActor
    private func refresh() async {
        display = store.bundledText(source)
        if store.isActive {
            display = await store.translate(source)
        }
    }
}

extension View {
  @ViewBuilder
  func appTranslationLayoutDirection() -> some View {
    let store = AppTranslationStore.shared
    environment(\.layoutDirection, store.isRtl ? .rightToLeft : .leftToRight)
  }
}
