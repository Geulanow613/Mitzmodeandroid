import SwiftUI

/// Blessings reference — mirrors Android `BrachotDialog.kt`.
struct BrachotView: View {
  @Environment(\.dismiss) private var dismiss

  @State private var showEnglish = false
  @State private var fontScale: CGFloat = 1.0
  @GestureState private var pinchScale: CGFloat = 1.0

  private var showLiturgyTranslation: Bool { false }

  private var scaledFont: CGFloat {
    (16 * fontScale * pinchScale).clamped(to: 10...54)
  }

  private var transliterationFont: CGFloat {
    (scaledFont * 0.82).clamped(to: 9...44)
  }

  var body: some View {
    ZStack {
      LinearGradient(
        colors: [AppColor.parchmentTop, AppColor.parchmentMid, AppColor.parchmentBase],
        startPoint: .top,
        endPoint: .bottom
      )
      .ignoresSafeArea()

      VStack(spacing: 0) {
        MenuSheetHeaderBar(title: "Blessings", onClose: { dismiss() })
        MenuSheetGoldHairline()

        if showLiturgyTranslation {
          HStack {
            TranslatableText(
              source: "Show translation",
              font: .system(size: 15, design: .serif),
              weight: .medium
            )
            Spacer()
            Toggle("", isOn: $showEnglish)
              .labelsHidden()
          }
          .padding(.horizontal, 16)
          .padding(.vertical, 8)
        }

        ScrollView {
          VStack(alignment: .leading, spacing: 0) {
            AlHaMichyaBlessingCard(
              showEnglish: $showEnglish,
              fontScale: fontScale * pinchScale
            )

            Rectangle()
              .fill(AppColor.goldBorder.opacity(0.25))
              .frame(height: 0.8)
              .padding(.vertical, 12)

            ForEach(BrachotData.entries) { entry in
              brachaRow(entry)
            }

            Button(action: { dismiss() }) {
              Text("Close")
                .font(.system(size: 16, weight: .semibold, design: .serif))
                .foregroundColor(AppColor.navyDeep)
                .frame(maxWidth: .infinity)
                .padding(.vertical, 12)
                .background(
                  Capsule()
                    .fill(AppColor.goldBorder.opacity(0.35))
                )
            }
            .buttonStyle(.plain)
            .padding(.top, 8)
            .padding(.bottom, 24)
          }
          .padding(.horizontal, 16)
        }
        .simultaneousGesture(
          MagnificationGesture()
            .updating($pinchScale) { value, state, _ in state = value }
            .onEnded { magnification in
              fontScale = (fontScale * magnification).clamped(to: 0.5...3.0)
            }
        )
      }
    }
  }

  @ViewBuilder
  private func brachaRow(_ entry: BrachaEntry) -> some View {
    VStack(alignment: .leading, spacing: 0) {
      TranslatableText(
        source: entry.name,
        font: .system(size: 17, design: .serif),
        weight: .semibold,
        color: AppColor.goldBorder
      )

      if showBrachaTransliteration {
        BundledLineText(
          key: BrachaEntry.nameTransliterationKey(entry.name),
          fontSize: 13,
          color: AppColor.goldBorder.opacity(0.75),
          italic: true
        )
        .padding(.top, 2)
      }

      TranslatableText(
        source: entry.description,
        font: .system(size: 13, design: .serif),
        color: AppColor.textMuted
      )
      .padding(.top, 4)
      .padding(.bottom, 4)

      if showEnglish && showLiturgyTranslation {
        LiturgyTranslationText(
          source: entry.english,
          fontSize: scaledFont,
          color: AppColor.textPrimary,
          alignment: .center
        )
      } else {
        Text(entry.hebrew)
          .font(.system(size: scaledFont, weight: .regular, design: .serif))
          .foregroundColor(AppColor.textPrimary)
          .multilineTextAlignment(.center)
          .frame(maxWidth: .infinity, alignment: .center)
          .environment(\.layoutDirection, .rightToLeft)
          .padding(.vertical, 8)

        if showBrachaTransliteration {
          BundledLineText(
            key: BrachaEntry.textTransliterationKey(entry.english),
            fontSize: transliterationFont,
            color: AppColor.textMuted,
            alignment: .center,
            italic: true
          )
          .padding(.bottom, 8)
        }
      }

      Rectangle()
        .fill(AppColor.goldBorder.opacity(0.25))
        .frame(height: 0.8)
        .padding(.vertical, 8)
    }
    .padding(.vertical, 6)
  }

  private var showBrachaTransliteration: Bool { false }
}

/// Renders a bundled catalog key (e.g. transliteration) without re-translating the key string.
private struct BundledLineText: View {
  let key: String
  var fontSize: CGFloat = 16
  var color: Color = AppColor.textPrimary
  var alignment: TextAlignment = .leading
  var italic: Bool = false

  var body: some View {
    EmptyView()
  }
}

private extension CGFloat {
  func clamped(to range: ClosedRange<CGFloat>) -> CGFloat {
    Swift.min(Swift.max(self, range.lowerBound), range.upperBound)
  }
}

#if DEBUG
struct BrachotView_Previews: PreviewProvider {
  static var previews: some View {
    BrachotView()
  }
}
#endif
