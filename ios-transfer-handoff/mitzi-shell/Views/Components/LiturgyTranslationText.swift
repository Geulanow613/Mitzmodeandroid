import SwiftUI

/// English-only liturgy display for Mitz Mode. Archived implementation:
/// `archived-translations/LiturgyTranslationText.swift`
struct LiturgyTranslationText: View {
    let source: String
    var fontSize: CGFloat = 16
    var color: Color = AppColor.textPrimary
    var alignment: TextAlignment = .center

    var body: some View {
        Text(source)
            .font(.system(size: fontSize, weight: .regular, design: .serif))
            .foregroundColor(color)
            .multilineTextAlignment(alignment)
            .frame(maxWidth: .infinity, alignment: alignment == .center ? .center : .leading)
            .padding(.vertical, 8)
    }
}
