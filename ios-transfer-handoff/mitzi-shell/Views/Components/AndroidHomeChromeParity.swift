import SwiftUI
import UIKit

// MARK: - Layout (MitzModeApp.kt)

enum AppChromeLayout {
    /// `isFullScreen = (screenHeightDp > 700)` → 32.dp vs 8.dp.
    static var androidTopPadding: CGFloat {
        UIScreen.main.bounds.height > 700 ? 32 : 8
    }

    /// Pull menu + mitzvot counter closer to the safe-area top (iOS spacing under notch/Dynamic Island).
    static let iosHomeChromeLift: CGFloat = 28

    /// Menu chip / counter row: Android parity minus lift so chips sit slightly higher.
    static var iosHomeChromeTopPadding: CGFloat {
        max(2, androidTopPadding - iosHomeChromeLift)
    }

    static let menuLeading: CGFloat = 14
    static let counterTrailing: CGFloat = 14

    /// Panel top: chip top + 46 + 4 (under glass menu chip).
    static var menuDropdownTopY: CGFloat {
        iosHomeChromeTopPadding + 46 + 4
    }

    static let addMitzvahBottomInset: CGFloat = 76
    static let whatsMitzvahBottomInset: CGFloat = 18
    static let dailyChecklistBottomInset: CGFloat = 124
    static let dailyChecklistOffsetY: CGFloat = -20
}

extension View {
    /// Android `letterSpacing` in sp; `tracking` is iOS 16+ (no spacing API on `View` for iOS 15).
    @ViewBuilder
    func androidLetterSpacing(_ value: CGFloat) -> some View {
        if #available(iOS 16.0, *) {
            self.tracking(value)
        } else {
            self
        }
    }
}

// MARK: - A) Three-dot menu (glass 46×46)

/// Android `MitzModeApp` menu trigger: glass circle, gold rim `#FFD56B` @55%, icon `#FFE082`.
struct AndroidParityMenuChip: View {
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            Image(systemName: "ellipsis")
                .font(.system(size: 17, weight: .bold))
                .foregroundColor(AppColor.androidTextGold)
                .frame(width: 46, height: 46)
                .background(
                    Circle()
                        .fill(
                            RadialGradient(
                                colors: [
                                    Color.white.opacity(0.18),
                                    Color.white.opacity(0.06)
                                ],
                                center: .center,
                                startRadius: 0,
                                endRadius: 46
                            )
                        )
                )
                .overlay(Circle().stroke(AppColor.androidGoldSpot.opacity(0.55), lineWidth: 1))
                .shadow(color: AppColor.androidGoldSpot.opacity(0.9), radius: 8, x: 0, y: 2)
        }
        .buttonStyle(.plain)
    }
}

// MARK: - B) Dropdown panel

struct AndroidParityDropdownPanel: View {
    @Binding var isOpen: Bool
    var onAbout: () -> Void
    var onBirkatHamazon: () -> Void
    var onTefilat: () -> Void
    var onBrachot: () -> Void
    var onSong: () -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            row("About", onAbout)
            row("Birkat HaMazon", onBirkatHamazon)
            row("Traveler's Prayer", onTefilat)
            row("Blessings", onBrachot)
            row("🎵 Official App Song", onSong)
        }
        .background(
            RoundedRectangle(cornerRadius: 14, style: .continuous)
                .fill(AppColor.androidMenuDropdownFill)
                .overlay(
                    RoundedRectangle(cornerRadius: 14, style: .continuous)
                        .stroke(AppColor.goldBorder.opacity(0.55), lineWidth: 1)
                )
        )
        .shadow(color: .black.opacity(0.25), radius: 12, x: 0, y: 6)
        .frame(minWidth: 220)
    }

    private func row(_ title: String, systemImage: String? = nil, _ action: @escaping () -> Void) -> some View {
        Button {
            isOpen = false
            action()
        } label: {
            HStack(spacing: 8) {
                if let systemImage {
                    Image(systemName: systemImage)
                        .font(.system(size: 15, weight: .medium))
                        .foregroundColor(AppColor.textSecondary)
                        .frame(width: 18, alignment: .center)
                }
                TranslatableText(
                    source: title,
                    font: .system(size: 17, weight: .medium),
                    color: AppColor.textPrimary,
                    alignment: .leading
                )
            }
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding(.horizontal, 12)
            .padding(.vertical, 12)
        }
        .buttonStyle(.plain)
    }
}

// MARK: - C) Mitzvah counter pill (top right)

struct AndroidParityMitzvahCountPill: View {
    let count: Int

    private var active: Bool { count > 0 }
    private var gold: Color { AppColor.androidGoldSpot }

    var body: some View {
        HStack(spacing: 8) {
            Image(systemName: "checkmark.circle.fill")
                .font(.system(size: 26))
                .foregroundColor(active ? gold : gold.opacity(0.5))
            Text("\(count)")
                .font(.system(size: 22, weight: .bold))
                .androidLetterSpacing(0.3)
                .foregroundColor(active ? gold : gold.opacity(0.55))
                .shadow(color: .black.opacity(0.45), radius: 1.5, x: 0, y: 1)
        }
        .padding(.horizontal, 14)
        .padding(.vertical, 8)
        .background(
            Capsule()
                .fill(
                    LinearGradient(
                        colors: [
                            Color.white.opacity(active ? 0.20 : 0.10),
                            Color.white.opacity(active ? 0.08 : 0.04)
                        ],
                        startPoint: .leading,
                        endPoint: .trailing
                    )
                )
        )
        .overlay(
            Capsule()
                .stroke(
                    LinearGradient(
                        colors: [
                            gold.opacity(active ? 0.85 : 0.4),
                            gold.opacity(active ? 0.5 : 0.2)
                        ],
                        startPoint: .leading,
                        endPoint: .trailing
                    ),
                    lineWidth: 1
                )
        )
        .shadow(color: gold.opacity(0.85), radius: 8, x: 0, y: 2)
    }
}

// MARK: - D) Add a Mitzvah (frosted outline)

struct AndroidParityAddMitzvahButton: View {
    let action: () -> Void

    private var strokeGold: Color { AppColor.androidGoldSpot.opacity(0.8) }

    var body: some View {
        Button(action: action) {
            TranslatableText(
                source: "Add a Mitzvah",
                font: .system(size: 15, weight: .semibold),
                color: AppColor.androidTextGold,
                alignment: .center
            )
            .androidLetterSpacing(0.6)
                .padding(.horizontal, 22)
                .padding(.vertical, 10)
                .background(
                    Capsule()
                        .fill(Color.white.opacity(0.08))
                )
                .overlay(
                    Capsule()
                        .stroke(strokeGold, lineWidth: 1.4)
                )
        }
        .buttonStyle(.plain)
    }
}

// MARK: - F) Holy Light Checklist (full-width gold pill on home)

private struct HolyLightSparkleSpec: Identifiable {
    let id: Int
    let x: CGFloat
    let y: CGFloat
    let delay: Double
    let size: CGFloat
}

private struct StarOfDavidSparkle: View {
    let size: CGFloat
    let bright: Bool

    var body: some View {
        Text("✡")
            .font(.system(size: size, weight: .bold))
            .foregroundStyle(
                LinearGradient(
                    colors: [
                        Color.white.opacity(bright ? 0.95 : 0.15),
                        Color(red: 1.0, green: 0.835, blue: 0.420).opacity(bright ? 0.9 : 0.12)
                    ],
                    startPoint: .topLeading,
                    endPoint: .bottomTrailing
                )
            )
    }
}

private struct HolyLightSparkleOverlay: View {
    private let specs: [HolyLightSparkleSpec] = [
        HolyLightSparkleSpec(id: 0, x: 0.08, y: 0.28, delay: 0.0, size: 9),
        HolyLightSparkleSpec(id: 1, x: 0.18, y: 0.72, delay: 1.05, size: 7),
        HolyLightSparkleSpec(id: 2, x: 0.86, y: 0.24, delay: 0.45, size: 10),
        HolyLightSparkleSpec(id: 3, x: 0.92, y: 0.68, delay: 1.65, size: 8),
        HolyLightSparkleSpec(id: 4, x: 0.42, y: 0.14, delay: 0.75, size: 7),
        HolyLightSparkleSpec(id: 5, x: 0.58, y: 0.84, delay: 1.35, size: 9),
        HolyLightSparkleSpec(id: 6, x: 0.30, y: 0.50, delay: 1.95, size: 6),
        HolyLightSparkleSpec(id: 7, x: 0.72, y: 0.48, delay: 2.55, size: 6),
    ]

    @State private var twinkle = false

    var body: some View {
        GeometryReader { geo in
            ZStack {
                ForEach(specs) { spec in
                    StarOfDavidSparkle(size: spec.size, bright: twinkle)
                        .scaleEffect(twinkle ? 1.0 : 0.55)
                        .position(x: geo.size.width * spec.x, y: geo.size.height * spec.y)
                        .animation(
                            .easeInOut(duration: 3.45)
                                .repeatForever(autoreverses: true)
                                .delay(spec.delay),
                            value: twinkle
                        )
                }
            }
        }
        .allowsHitTesting(false)
        .onAppear { twinkle = true }
    }
}

struct AndroidParityDailyChecklistButton: View {
    let action: () -> Void

    @State private var shimmer = false

    private let navyText = Color(red: 26 / 255, green: 61 / 255, blue: 114 / 255)
    private let goldGlow = Color(red: 1.0, green: 0.835, blue: 0.420)

    var body: some View {
        Button(action: action) {
            ZStack {
                Capsule()
                    .fill(
                        LinearGradient(
                            colors: [
                                Color(red: 1.0, green: 0.953, blue: 0.710),
                                Color(red: 1.0, green: 0.878, blue: 0.510),
                                Color(red: 1.0, green: 0.835, blue: 0.420),
                                Color(red: 0.878, green: 0.671, blue: 0.184)
                            ],
                            startPoint: .leading,
                            endPoint: .trailing
                        )
                    )

                Capsule()
                    .fill(
                        LinearGradient(
                            colors: [
                                Color.white.opacity(0.0),
                                Color.white.opacity(0.55),
                                Color.white.opacity(0.0)
                            ],
                            startPoint: shimmer ? .trailing : .leading,
                            endPoint: shimmer ? .leading : .trailing
                        )
                    )
                    .blendMode(.screen)

                HolyLightSparkleOverlay()
                    .clipShape(Capsule())

                TranslatableText(
                    source: "Holy Light Checklist",
                    font: .system(size: 17, weight: .bold),
                    color: navyText,
                    alignment: .center
                )
                .androidLetterSpacing(0.55)
                .shadow(color: .black.opacity(0.32), radius: 1.4, x: 0, y: 1.8)
            }
            .frame(maxWidth: .infinity)
            .padding(.horizontal, 24)
            .padding(.vertical, 15)
            .overlay(
                Capsule()
                    .stroke(Color(red: 1.0, green: 0.973, blue: 0.839), lineWidth: 1.6)
            )
            .shadow(color: goldGlow, radius: 16, x: 0, y: 4)
        }
        .buttonStyle(.plain)
        .padding(.horizontal, 20)
        .offset(y: AppChromeLayout.dailyChecklistOffsetY)
        .onAppear {
            withAnimation(.linear(duration: 9.6).repeatForever(autoreverses: false)) {
                shimmer = true
            }
        }
    }
}

struct AndroidParityWhatsAMitzvahButton: View {
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            TranslatableText(
                source: "What's a Mitzvah?",
                font: .system(size: 18, weight: .semibold),
                color: .white,
                alignment: .center
            )
            .androidLetterSpacing(0.6)
            .shadow(color: .black.opacity(0.35), radius: 1.5, x: 0, y: 1.5)
                .padding(.horizontal, 28)
                .padding(.vertical, 12)
                .background(
                    Capsule()
                        .fill(
                            LinearGradient(
                                colors: [
                                    AppColor.androidGoldSpot,
                                    AppColor.androidGoldGradientMid,
                                    AppColor.goldBorder
                                ],
                                startPoint: .leading,
                                endPoint: .trailing
                            )
                        )
                )
                .overlay(
                    Capsule()
                        .stroke(AppColor.androidTextGold.opacity(0.8), lineWidth: 1)
                )
                .shadow(color: AppColor.androidGoldSpot, radius: 12, x: 0, y: 4)
        }
        .buttonStyle(.plain)
    }
}

// MARK: - G) Mitzvah button (asset: mitzvahbutton)

struct AndroidParityMitzvahButton: View {
    let action: () -> Void

    private let buttonSize: CGFloat = 180

    var body: some View {
        Button(action: action) {
            Image("mitzvahbutton", bundle: .main)
                .resizable()
                .scaledToFit()
                .frame(width: buttonSize, height: buttonSize)
                .shadow(color: .black.opacity(0.25), radius: 12, y: 6)
        }
        .buttonStyle(MitzvahMePressStyle())
    }
}

private struct MitzvahMePressStyle: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .scaleEffect(configuration.isPressed ? 0.9 : 1.0)
            .animation(.spring(response: 0.3, dampingFraction: 0.6), value: configuration.isPressed)
    }
}
