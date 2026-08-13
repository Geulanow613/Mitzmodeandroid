import SwiftUI

/// Interactive Bracha Me'ein Shalosh (Al HaMichya) builder — mirrors Android `AlHaMichyaBlessingCard.kt`.
struct AlHaMichyaBlessingCard: View {
  @Binding var showEnglish: Bool
  var fontScale: CGFloat = 1.0
  var showOwnTranslationToggle: Bool = false

  @State private var hasMezonot = false
  @State private var hasWine = false
  @State private var hasFruit = false
  @State private var isRoshChodesh = false
  @State private var isPesach = false
  @State private var isSukkot = false

  private var selection: MeinShaloshSelection {
    MeinShaloshSelection(
      hasMezonot: hasMezonot,
      hasWine: hasWine,
      hasFruit: hasFruit,
      isRoshChodesh: isRoshChodesh,
      isPesach: isPesach,
      isSukkot: isSukkot
    )
  }

  private var blessingText: String {
    let language: MeinShaloshLanguage = showEnglish ? .english : .hebrew
    return MeinShaloshTextEngine.build(selection, language: language)
  }

  private var scaledFont: CGFloat {
    (23 * fontScale).clamped(to: 12...54)
  }

  private var lineHeight: CGFloat {
    (34 * fontScale).clamped(to: 18...72)
  }

  var body: some View {
    VStack(alignment: .leading, spacing: 10) {
      TranslatableText(
        source: "Bracha Me'ein Shalosh (Al HaMichya)",
        font: .system(size: 17, design: .serif),
        weight: .semibold,
        color: AppColor.goldBorder
      )

      TranslatableText(
        source: "After Mezonot, wine/grape juice, and/or shivat ha-minim tree fruits — select what you had at this sitting.",
        font: .system(size: 13, design: .serif),
        color: AppColor.textMuted
      )
      .padding(.bottom, 4)

      if showOwnTranslationToggle {
        HStack {
          TranslatableText(
            source: "English translation",
            font: .system(size: 15, design: .serif),
            weight: .medium
          )
          Spacer()
          Toggle("", isOn: $showEnglish)
            .labelsHidden()
        }
      }

      TranslatableText(
        source: "What did you have?",
        font: .system(size: 13, design: .serif),
        weight: .medium,
        color: AppColor.textMuted
      )

      LazyVGrid(
        columns: [GridItem(.adaptive(minimum: 140), spacing: 8)],
        alignment: .leading,
        spacing: 6
      ) {
        chipWithHint(
          label: "Mezonot (grain)",
          hint: "Wheat, barley, rye, oats, spelt",
          selected: hasMezonot
        ) { hasMezonot = $0 }

        selectionChip(label: "Wine / grape juice", selected: hasWine) { hasWine = $0 }

        chipWithHint(
          label: "Seven species fruit",
          hint: "Grapes, figs, pomegranates, olives, dates",
          selected: hasFruit
        ) { hasFruit = $0 }
      }

      TranslatableText(
        source: "Today is also:",
        font: .system(size: 13, design: .serif),
        weight: .medium,
        color: AppColor.textMuted
      )
      .padding(.top, 4)

      LazyVGrid(
        columns: [GridItem(.adaptive(minimum: 120), spacing: 8)],
        alignment: .leading,
        spacing: 6
      ) {
        selectionChip(label: "Rosh Chodesh", selected: isRoshChodesh) { isRoshChodesh = $0 }
        selectionChip(label: "Pesach", selected: isPesach) {
          isPesach = $0
          if $0 { isSukkot = false }
        }
        selectionChip(label: "Sukkot", selected: isSukkot) {
          isSukkot = $0
          if $0 { isPesach = false }
        }
      }

      Rectangle()
        .fill(AppColor.goldBorder.opacity(0.25))
        .frame(height: 0.8)
        .padding(.vertical, 8)

      if showEnglish {
        LiturgyTranslationText(
          source: blessingText,
          fontSize: scaledFont,
          color: AppColor.textPrimary,
          alignment: .center
        )
        .lineSpacing(lineHeight - scaledFont)
        .padding(.horizontal, 4)
      } else {
        Text(blessingText)
          .font(.system(size: scaledFont, weight: .regular, design: .serif))
          .foregroundColor(AppColor.textPrimary)
          .multilineTextAlignment(.center)
          .lineSpacing(lineHeight - scaledFont)
          .frame(maxWidth: .infinity, alignment: .center)
          .environment(\.layoutDirection, showEnglish ? .leftToRight : .rightToLeft)
          .padding(.horizontal, 4)
          .padding(.vertical, 8)
      }
    }
    .frame(maxWidth: .infinity, alignment: .leading)
  }

  @ViewBuilder
  private func selectionChip(label: String, selected: Bool, onChange: @escaping (Bool) -> Void) -> some View {
    Button {
      onChange(!selected)
    } label: {
      TranslatableText(
        source: label,
        font: .system(size: 14, design: .serif),
        weight: .medium,
        color: selected ? AppColor.navyDeep : AppColor.textPrimary
      )
      .padding(.horizontal, 12)
      .padding(.vertical, 8)
      .background(
        Capsule()
          .fill(selected ? AppColor.goldBorder.opacity(0.35) : AppColor.goldBorder.opacity(0.1))
      )
      .overlay(
        Capsule()
          .stroke(AppColor.goldBorder.opacity(selected ? 0.8 : 0.35), lineWidth: 1)
      )
    }
    .buttonStyle(.plain)
  }

  @ViewBuilder
  private func chipWithHint(
    label: String,
    hint: String,
    selected: Bool,
    onChange: @escaping (Bool) -> Void
  ) -> some View {
    VStack(alignment: .leading, spacing: 2) {
      selectionChip(label: label, selected: selected, onChange: onChange)
      TranslatableText(
        source: hint,
        font: .system(size: 11, design: .serif),
        color: AppColor.textMuted.opacity(0.9)
      )
    }
    .frame(maxWidth: 200, alignment: .leading)
  }
}

private extension CGFloat {
  func clamped(to range: ClosedRange<CGFloat>) -> CGFloat {
    Swift.min(Swift.max(self, range.lowerBound), range.upperBound)
  }
}
