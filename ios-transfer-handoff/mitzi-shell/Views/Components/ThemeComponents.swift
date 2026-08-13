import SwiftUI
import UIKit

// MARK: - Toast

final class ToastCenter: ObservableObject {
    static let shared = ToastCenter()
    @Published var message: String?

    func show(_ text: String, duration: TimeInterval = 1.6) {
        DispatchQueue.main.async {
            self.message = text
            DispatchQueue.main.asyncAfter(deadline: .now() + duration) {
                if self.message == text { self.message = nil }
            }
        }
    }
}

struct ToastOverlay: View {
    @ObservedObject var toast: ToastCenter

    var body: some View {
        ZStack {
            if let m = toast.message {
                Text(m)
                    .font(.subheadline.weight(.semibold))
                    .foregroundColor(AppColor.textOnDark)
                    .padding(.horizontal, 18)
                    .padding(.vertical, 10)
                    .background(
                        Capsule()
                            .fill(AppColor.navyDeep.opacity(0.92))
                            .overlay(Capsule().stroke(AppColor.goldBorder.opacity(0.65), lineWidth: 1))
                    )
                    .shadow(color: .black.opacity(0.25), radius: 8, y: 4)
                    .transition(.move(edge: .bottom).combined(with: .opacity))
                    .padding(.bottom, 48)
                    .frame(maxHeight: .infinity, alignment: .bottom)
            }
        }
        .animation(.spring(response: 0.35, dampingFraction: 0.85), value: toast.message)
        .allowsHitTesting(false)
    }
}

// MARK: - Links (tap open, long-press Open / Copy)

struct AppLinkText: View {
    let displayText: String
    let urlString: String
    var fontSize: CGFloat = 16
    @Environment(\.openURL) private var openURL

    /// Android `LinkText`: line height ≈ `fontSize * 1.5` (extra spacing beyond single line).
    private var linkLineSpacing: CGFloat {
        Swift.max(0, fontSize * 1.5 - UIFont.systemFont(ofSize: fontSize).lineHeight)
    }

    var body: some View {
        Group {
            if #available(iOS 16.0, *) {
                Text(displayText)
                    .font(.system(size: fontSize, weight: .medium, design: .serif))
                    .foregroundColor(AppColor.navyBlue)
                    .lineSpacing(linkLineSpacing)
                    .underline()
                    .multilineTextAlignment(.center)
            } else {
                linkTextIOS15
            }
        }
        .frame(maxWidth: .infinity)
        .contentShape(Rectangle())
            .onTapGesture {
                openIfValid()
            }
            .contextMenu {
                Button("Open URL") { openIfValid() }
                Button("Copy URL") { copyURL() }
            }
    }

    private func openIfValid() {
        guard let u = URL(string: urlString), ["http", "https"].contains(u.scheme?.lowercased() ?? "") else { return }
        openURL(u)
    }

    private func copyURL() {
        UIPasteboard.general.string = urlString
        ToastCenter.shared.show("URL copied")
    }

    private var linkTextIOS15: some View {
        let serif = UIFont(name: "Georgia", size: fontSize) ?? UIFont.systemFont(ofSize: fontSize, weight: .medium)
        let ns = NSMutableAttributedString(string: displayText, attributes: [
            .font: serif,
            .foregroundColor: UIColor(AppColor.navyBlue),
            .underlineStyle: NSUnderlineStyle.single.rawValue
        ])
        let p = NSMutableParagraphStyle()
        p.alignment = .center
        p.lineSpacing = linkLineSpacing
        ns.addAttribute(.paragraphStyle, value: p, range: NSRange(location: 0, length: ns.length))
        return Text(AttributedString(ns))
            .multilineTextAlignment(.center)
    }
}

// MARK: - Heirloom buttons (Android `HeirloomButton` ~558–636)

private enum HeirloomButtonChrome {
    static func labelPointSize(screenWidth: CGFloat = UIScreen.main.bounds.width) -> CGFloat {
        if screenWidth < 360 { return 13 }
        if screenWidth < 400 { return 15 }
        return 16
    }

    static func heirloomFont(size: CGFloat) -> Font {
        .system(size: size, weight: .semibold, design: .default)
    }

    @ViewBuilder
    static func label<Content: View>(enabled: Bool, @ViewBuilder content: () -> Content) -> some View {
        ZStack {
            RoundedRectangle(cornerRadius: AppRadius.button)
                .fill(
                    LinearGradient(
                        colors: enabled
                            ? [AppColor.navyPrimary, AppColor.navyMid, AppColor.navyDeep]
                            : [AppColor.disabledNavyFlat.opacity(0.85), AppColor.disabledNavyFlat],
                        startPoint: .top,
                        endPoint: .bottom
                    )
                )
            RoundedRectangle(cornerRadius: AppRadius.button)
                .stroke(AppColor.goldBorder.opacity(enabled ? 0.85 : 0.45), lineWidth: 1.4)
            RoundedRectangle(cornerRadius: AppRadius.button)
                .stroke(AppColor.goldHairline.opacity(enabled ? 0.55 : 0.30), lineWidth: 0.6)
                .padding(2.5)
        }
        .frame(maxWidth: .infinity)
        .frame(height: 50)
        .overlay {
            content()
                .font(heirloomFont(size: labelPointSize()))
                .androidLetterSpacing(1.2)
                .foregroundColor(AppColor.mitzvahHeirloomLabel)
                .shadow(color: .black.opacity(0.45), radius: 1.5, x: 0, y: 1.2)
                .minimumScaleFactor(0.75)
                .lineLimit(1)
                .multilineTextAlignment(.center)
                .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .center)
        }
        .clipShape(RoundedRectangle(cornerRadius: AppRadius.button))
        .overlay(alignment: .top) {
            GeometryReader { geo in
                HStack(spacing: 0) {
                    Spacer(minLength: geo.size.width * 0.075)
                    RoundedRectangle(cornerRadius: 1)
                        .fill(
                            LinearGradient(
                                colors: [
                                    .clear,
                                    Color.white.opacity(enabled ? 0.18 : 0.06),
                                    .clear
                                ],
                                startPoint: .leading,
                                endPoint: .trailing
                            )
                        )
                        .frame(width: geo.size.width * 0.85, height: 2)
                    Spacer(minLength: geo.size.width * 0.075)
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
                .padding(.top, 1)
            }
            .allowsHitTesting(false)
        }
    }
}

struct HeirloomButton: View {
    let title: String
    var enabled: Bool = true
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            HeirloomButtonChrome.label(enabled: enabled) {
                Text(title)
            }
        }
        .disabled(!enabled)
    }
}

struct TranslatableHeirloomButton: View {
    let source: String
    var enabled: Bool = true
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            HeirloomButtonChrome.label(enabled: enabled) {
                TranslatableText(
                    source: source,
                    font: HeirloomButtonChrome.heirloomFont(size: HeirloomButtonChrome.labelPointSize()),
                    weight: .semibold,
                    color: AppColor.mitzvahHeirloomLabel,
                    alignment: .center
                )
            }
        }
        .disabled(!enabled)
    }
}

// MARK: - Parchment surface (card interior — Android `MitzvahDialog` C1–C3)

struct ParchmentSurface: View {
    var body: some View {
        Canvas { context, size in
            let rect = CGRect(origin: .zero, size: size)
            let corner = AppRadius.card
            let clipPath = Path(roundedRect: rect, cornerRadius: corner)
            context.clip(to: clipPath)

            // C1 — vertical gradient stops 0 / 0.3 / 1
            context.fill(
                clipPath,
                with: .linearGradient(
                    Gradient(stops: [
                        .init(color: AppColor.parchmentTop, location: 0),
                        .init(color: AppColor.parchmentMid, location: 0.3),
                        .init(color: AppColor.parchmentBase, location: 1)
                    ]),
                    startPoint: CGPoint(x: size.width * 0.5, y: 0),
                    endPoint: CGPoint(x: size.width * 0.5, y: size.height)
                )
            )

            let mx = Swift.max(size.width, size.height)

            // C2 — paper grain: `#EDD9A3` @ 0.10 → clear, center mid-card
            let c2 = CGPoint(x: size.width * 0.5, y: size.height * 0.5)
            let r2 = mx * 0.6
            context.fill(
                Path(ellipseIn: CGRect(x: c2.x - r2, y: c2.y - r2, width: r2 * 2, height: r2 * 2)),
                with: .radialGradient(
                    Gradient(colors: [AppColor.parchmentGrain.opacity(0.10), .clear]),
                    center: c2,
                    startRadius: 0,
                    endRadius: r2
                )
            )

            // C3 — warm bottom wash: gold @ 0.06, center below card
            let c3 = CGPoint(x: size.width * 0.5, y: size.height * 1.05)
            let r3 = mx * 0.7
            context.fill(
                Path(ellipseIn: CGRect(x: c3.x - r3, y: c3.y - r3, width: r3 * 2, height: r3 * 2)),
                with: .radialGradient(
                    Gradient(colors: [AppColor.goldBorder.opacity(0.06), .clear]),
                    center: c3,
                    startRadius: 0,
                    endRadius: r3
                )
            )
        }
    }
}

// MARK: - Menu sheet chrome (Android checklist / dialog header)

/// Navy bar + gold title + close — use on every full-screen opened from the ⋯ menu.
struct MenuSheetHeaderBar: View {
    let title: String
    let onClose: () -> Void

    var body: some View {
        HStack(spacing: 12) {
            TranslatableText(
                source: title,
                font: .system(size: 20, weight: .semibold, design: .serif),
                weight: .semibold,
                color: AppColor.goldBright,
                alignment: .leading
            )
            .lineLimit(2)
            .minimumScaleFactor(0.65)
            Spacer(minLength: 8)
            Button(action: onClose) {
                Image(systemName: "xmark.circle.fill")
                    .font(.title3)
                    .foregroundColor(AppColor.goldBright.opacity(0.92))
            }
            .buttonStyle(.plain)
            .accessibilityLabel("Close")
        }
        .padding(.horizontal, 14)
        .padding(.vertical, 12)
        .background(
            LinearGradient(
                colors: [AppColor.navyDeep, AppColor.navyMid],
                startPoint: .top,
                endPoint: .bottom
            )
        )
    }
}

struct MenuSheetGoldHairline: View {
    var body: some View {
        Rectangle()
            .fill(
                LinearGradient(
                    colors: [.clear, AppColor.goldBorder.opacity(0.6), .clear],
                    startPoint: .leading,
                    endPoint: .trailing
                )
            )
            .frame(height: 1)
    }
}
