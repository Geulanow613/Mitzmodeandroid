import SwiftUI

/// English-only text for Mitz Mode. Full translation implementation is archived in
/// `archived-translations/TranslatableText.swift` — see `archived-translations/README.md`.
struct TranslatableText: View {
    let source: String
    var font: Font = .body
    var weight: Font.Weight = .regular
    var color: Color = AppColor.textPrimary
    var alignment: TextAlignment = .leading
    var lineLimit: Int?

    var body: some View {
        Text(source)
            .font(font.weight(weight))
            .foregroundColor(color)
            .multilineTextAlignment(alignment)
            .lineLimit(lineLimit)
    }
}

extension View {
    @ViewBuilder
    func appTranslationLayoutDirection() -> some View {
        self
    }
}
