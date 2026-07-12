import SwiftUI

/// Short level-up toast: animated rainbow copy (`RainbowText`) on parchment — same idea as before the certificate redesign.
/// Placement matches historic `ContentView` overlay (lower-third anchor via `.position`).
struct LevelAchievedView: View {
    let level: String

    var body: some View {
        VStack(spacing: 14) {
            RainbowText(text: "You've reached a new level!", fontSize: 22)
                .multilineTextAlignment(.center)
                .fixedSize(horizontal: false, vertical: true)

            RainbowText(text: level.uppercased(), fontSize: 17)
                .multilineTextAlignment(.center)
                .fixedSize(horizontal: false, vertical: true)
        }
        .padding(.horizontal, 28)
        .padding(.vertical, 22)
        .background(
            RoundedRectangle(cornerRadius: 22, style: .continuous)
                .fill(
                    LinearGradient(
                        colors: [
                            AppColor.parchmentTop.opacity(0.96),
                            AppColor.parchmentMid,
                            AppColor.parchmentBase.opacity(0.92)
                        ],
                        startPoint: .top,
                        endPoint: .bottom
                    )
                )
        )
        .overlay(
            RoundedRectangle(cornerRadius: 22, style: .continuous)
                .stroke(AppColor.goldBorder.opacity(0.45), lineWidth: 1.2)
        )
        .shadow(color: Color.black.opacity(0.35), radius: 14, x: 0, y: 6)
        .allowsHitTesting(false)
    }
}
