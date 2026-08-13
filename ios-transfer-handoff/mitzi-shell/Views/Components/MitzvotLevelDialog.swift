import SwiftUI
import UIKit

struct StarOfDavid: Shape {
    func path(in rect: CGRect) -> Path {
        var path = Path()
        let width = rect.width
        let height = rect.height
        let size = min(width, height)
        let center = CGPoint(x: rect.midX, y: rect.midY)
        let radius = size / 2

        path.move(to: CGPoint(x: center.x, y: center.y - radius))
        path.addLine(to: CGPoint(x: center.x + radius * cos(.pi / 6), y: center.y + radius * sin(.pi / 6)))
        path.addLine(to: CGPoint(x: center.x - radius * cos(.pi / 6), y: center.y + radius * sin(.pi / 6)))
        path.closeSubpath()

        path.move(to: CGPoint(x: center.x, y: center.y + radius))
        path.addLine(to: CGPoint(x: center.x - radius * cos(.pi / 6), y: center.y - radius * sin(.pi / 6)))
        path.addLine(to: CGPoint(x: center.x + radius * cos(.pi / 6), y: center.y - radius * sin(.pi / 6)))
        path.closeSubpath()

        return path
    }
}

struct MitzvotLevel {
    let title: String
    let range: ClosedRange<Int>

    static let levels = [
        MitzvotLevel(title: "Secular", range: 0...0),
        MitzvotLevel(title: "Beginner", range: 1...9),
        MitzvotLevel(title: "Ba'al Teshuva", range: 10...49),
        MitzvotLevel(title: "Master Cholent Chef", range: 50...99),
        MitzvotLevel(title: "Aspiring Kiddush Maker", range: 100...199),
        MitzvotLevel(title: "Assistant Gabbai", range: 200...299),
        MitzvotLevel(title: "Guy who hands out candy at shul", range: 300...399),
        MitzvotLevel(title: "Western Wall Reveler", range: 400...499),
        MitzvotLevel(title: "Sofer", range: 500...599),
        MitzvotLevel(title: "Tzaddik", range: 600...699),
        MitzvotLevel(title: "Living Sefer Torah", range: 700...799),
        MitzvotLevel(title: "Eliyahu HaNavi", range: 800...899),
        MitzvotLevel(title: "King David", range: 900...999),
        MitzvotLevel(title: "Moshiach!!!", range: 1000...1799),
        MitzvotLevel(title: "Mitz Mode!", range: 1800...Int.max)
    ]

    static func getLevel(for count: Int) -> String {
        for level in levels where level.range.contains(count) {
            return level.title
        }
        return "Secular"
    }

    /// Stars on the standard achievement certificate: tier index — Secular = 0 (none), Beginner = 1, …, Moshiach = 13. Mitz Mode (1800+) uses the ultimate certificate with no stars (app icon only).
    static func certificateStarCount(for count: Int) -> Int {
        for (index, level) in levels.enumerated() where level.range.contains(count) {
            return index
        }
        return 0
    }
}

/// Certificate / level dialog aligned with Android `MitzvahLevelDialog` + `ParchmentDialog`.
struct MitzvotLevelDialog: View {
    @Binding var isPresented: Bool
    let mitzvotCount: Int
    var onRequestFinalRewardVideo: (() -> Void)? = nil

    private let cardCorner: CGFloat = 22
    private let innerCornerOuter: CGFloat = 16
    private let innerCornerInner: CGFloat = 12

    var body: some View {
        ZStack {
            AppColor.scrim
                .ignoresSafeArea()
                .onTapGesture { isPresented = false }

            VStack(spacing: 0) {
                ZStack(alignment: .topTrailing) {
                    // Same idea as `MitzvahCardView`: parchment background only wraps content height (no tall empty slab).
                    VStack(spacing: 0) {
                        ScrollView(.vertical, showsIndicators: false) {
                            certificateBlock
                                .padding(.horizontal, 14)
                                .padding(.top, 36)
                                .padding(.bottom, 8)
                        }
                        .fixedSize(horizontal: false, vertical: true)
                        .frame(maxHeight: UIScreen.main.bounds.height * 0.62)

                        Rectangle()
                            .fill(AppColor.goldBorder.opacity(0.25))
                            .frame(height: 0.8)
                            .padding(.horizontal, 14)

                        HStack {
                            Spacer()
                            Button(action: { isPresented = false }) {
                                TranslatableText(
                                    source: "Close",
                                    font: .system(size: 16, weight: .semibold),
                                    weight: .semibold,
                                    color: .white,
                                    alignment: .center
                                )
                                    .padding(.horizontal, 22)
                                    .padding(.vertical, 10)
                                    .background(
                                        Capsule()
                                            .fill(AppColor.goldBorder)
                                            .overlay(Capsule().stroke(AppColor.goldBright.opacity(0.6), lineWidth: 1))
                                    )
                                    .shadow(color: AppColor.goldBorder.opacity(0.35), radius: 4, y: 2)
                            }
                        }
                        .padding(.horizontal, 18)
                        .padding(.vertical, 12)
                    }
                    .background(
                        LinearGradient(
                            colors: [AppColor.parchmentTop, AppColor.parchmentMid, AppColor.parchmentBase],
                            startPoint: .top,
                            endPoint: .bottom
                        )
                    )

                    Button(action: { isPresented = false }) {
                        Image(systemName: "xmark")
                            .font(.system(size: 14, weight: .semibold))
                            .foregroundColor(AppColor.goldBorder)
                            .padding(10)
                    }
                    .accessibilityLabel("Close")
                    .padding(.top, 6)
                    .padding(.trailing, 6)
                }
                .clipShape(RoundedRectangle(cornerRadius: cardCorner))
                .overlay(
                    RoundedRectangle(cornerRadius: cardCorner)
                        .stroke(AppColor.goldBorder.opacity(0.55), lineWidth: 1.4)
                )
                .shadow(color: AppColor.goldBorder.opacity(0.28), radius: 24, y: 10)
            }
            .frame(maxWidth: min(UIScreen.main.bounds.width * 0.92, 400))
        }
    }

    private var ultimateNeon: LinearGradient {
        LinearGradient(
            colors: [
                Color(red: 0.3, green: 0.88, blue: 1.0),
                Color(red: 0.52, green: 0.38, blue: 1.0),
                Color(red: 1.0, green: 0.48, blue: 0.82)
            ],
            startPoint: .topLeading,
            endPoint: .bottomTrailing
        )
    }

    private var bundledAppIcon: UIImage? {
        guard let url = Bundle.main.url(forResource: "appicon mitz mode", withExtension: "png") else { return nil }
        return UIImage(contentsOfFile: url.path)
    }

    @ViewBuilder
    private var certificateBlock: some View {
        if mitzvotCount >= 1800 {
            ultimateMitzModeCertificate
        } else {
            standardCertificate(levelTitle: MitzvotLevel.getLevel(for: mitzvotCount))
        }
    }

    @ViewBuilder
    private func certificateStarGrid(starCount: Int, gold: Color, lineWidth: CGFloat) -> some View {
        let n = max(0, starCount)
        let cell = starCellSize(for: n)
        certificateStarRowStack(starCount: n, cell: cell) {
            StarOfDavid()
                .stroke(gold.opacity(0.78), lineWidth: lineWidth)
                .frame(width: cell, height: cell)
        }
    }

    /// Rows of 3–4 stars (fewer rows when possible). Avoids one star stranded on a wide line.
    private func certificateStarRowLengths(for n: Int) -> [Int] {
        guard n > 0 else { return [] }
        switch n {
        case 1: return [1]
        case 2: return [2]
        case 3: return [3]
        case 4: return [4]
        case 5: return [3, 2]
        default: break
        }

        let minRows = (n + 3) / 4
        let maxRows = n / 3
        guard minRows <= maxRows else { return [3, n - 3] }

        let rows = minRows
        let fours = n - 3 * rows
        let threes = rows - fours
        var lengths = Array(repeating: 4, count: fours) + Array(repeating: 3, count: threes)
        lengths.sort(by: >)
        return lengths
    }

    private static func partitionedStarIndices(rowLengths: [Int]) -> [[Int]] {
        var start = 0
        return rowLengths.map { len in
            let row = Array(start..<(start + len))
            start += len
            return row
        }
    }

    @ViewBuilder
    private func certificateStarRowStack<Star: View>(starCount: Int, cell: CGFloat, @ViewBuilder star: @escaping () -> Star) -> some View {
        let rows = certificateStarRowLengths(for: starCount)
        if rows.isEmpty {
            EmptyView()
        } else {
            let rowIndices = Self.partitionedStarIndices(rowLengths: rows)
            VStack(spacing: 8) {
                ForEach(0..<rowIndices.count, id: \.self) { ri in
                    HStack(spacing: 8) {
                        ForEach(rowIndices[ri], id: \.self) { _ in
                            star()
                        }
                    }
                }
            }
            .frame(maxWidth: .infinity)
            .padding(.top, 4)
        }
    }

    /// Keeps many stars readable inside the certificate width (wrap, no overlap).
    private func starCellSize(for count: Int) -> CGFloat {
        switch count {
        case ...4: return 18
        case ...8: return 16
        case ...11: return 14
        default: return 12
        }
    }

    private func standardCertificate(levelTitle: String) -> some View {
        let certificateGold = AppColor.goldBorder
        let stars = MitzvotLevel.certificateStarCount(for: mitzvotCount)
        return ZStack {
            RoundedRectangle(cornerRadius: innerCornerOuter)
                .stroke(certificateGold.opacity(0.70), lineWidth: 1.4)
                .padding(2)

            RoundedRectangle(cornerRadius: innerCornerInner)
                .stroke(certificateGold.opacity(0.38), lineWidth: 0.8)
                .padding(8)

            certificateCornerFlourish(certificateGold, alignment: .topLeading, leading: 11, trailing: 0, top: 9, bottom: 0)
            certificateCornerFlourish(certificateGold, alignment: .topTrailing, leading: 0, trailing: 11, top: 9, bottom: 0)
            certificateCornerFlourish(certificateGold, alignment: .bottomLeading, leading: 11, trailing: 0, top: 0, bottom: 9)
            certificateCornerFlourish(certificateGold, alignment: .bottomTrailing, leading: 0, trailing: 11, top: 0, bottom: 9)

            VStack(spacing: 10) {
                StarOfDavid()
                    .stroke(certificateGold.opacity(0.82), lineWidth: 1.6)
                    .frame(width: 34, height: 34)

                Text("CERTIFICATE OF ACHIEVEMENT")
                    .font(.system(size: 20, weight: .semibold, design: .serif))
                    .multilineTextAlignment(.center)
                    .foregroundColor(certificateGold)
                    .androidLetterSpacing(0.6)

                goldRule

                Text("This document certifies that\nI have completed")
                    .font(.system(size: 15, weight: .regular, design: .serif))
                    .multilineTextAlignment(.center)
                    .foregroundColor(AppColor.textPrimary)
                    .lineSpacing(3)

                Text(mitzvotCount == 1 ? "1 MITZVAH" : "\(mitzvotCount) MITZVOT")
                    .font(.system(size: 30, weight: .bold, design: .serif))
                    .multilineTextAlignment(.center)
                    .foregroundColor(certificateGold)

                goldRule

                Text("Current Level")
                    .font(.system(size: 13, weight: .medium, design: .serif))
                    .foregroundColor(AppColor.textMuted)

                Text(levelTitle.uppercased())
                    .font(.system(size: 17, weight: .semibold, design: .serif))
                    .multilineTextAlignment(.center)
                    .foregroundColor(certificateGold)
                    .padding(.horizontal, 18)
                    .padding(.vertical, 10)
                    .background(
                        Capsule()
                            .fill(Color(red: 1, green: 0.98, blue: 0.92))
                    )
                    .overlay(
                        Capsule()
                            .stroke(certificateGold.opacity(0.75), lineWidth: 1.4)
                    )

                certificateStarGrid(starCount: stars, gold: certificateGold, lineWidth: 1.1)
            }
            .padding(.horizontal, 16)
            .padding(.vertical, 16)
        }
        .padding(.vertical, 4)
    }

    @ViewBuilder
    private var ultimateMitzModeCertificate: some View {
        let levelTitle = MitzvotLevel.getLevel(for: mitzvotCount)

        ZStack {
            RoundedRectangle(cornerRadius: innerCornerOuter)
                .stroke(ultimateNeon, lineWidth: 3)
                .padding(1)

            RoundedRectangle(cornerRadius: innerCornerInner)
                .stroke(ultimateNeon.opacity(0.55), lineWidth: 1.5)
                .padding(8)

            certificateCornerFlourishUltimate(alignment: .topLeading, leading: 11, trailing: 0, top: 9, bottom: 0)
            certificateCornerFlourishUltimate(alignment: .topTrailing, leading: 0, trailing: 11, top: 9, bottom: 0)
            certificateCornerFlourishUltimate(alignment: .bottomLeading, leading: 11, trailing: 0, top: 0, bottom: 9)
            certificateCornerFlourishUltimate(alignment: .bottomTrailing, leading: 0, trailing: 11, top: 0, bottom: 9)

            VStack(spacing: 12) {
                Text("CERTIFICATE OF ACHIEVEMENT")
                    .font(.system(size: 20, weight: .semibold, design: .serif))
                    .multilineTextAlignment(.center)
                    .foregroundStyle(ultimateNeon)
                    .androidLetterSpacing(0.6)

                neonGoldRule

                Text("This document certifies that\nI have completed")
                    .font(.system(size: 15, weight: .regular, design: .serif))
                    .multilineTextAlignment(.center)
                    .foregroundColor(AppColor.textPrimary)
                    .lineSpacing(3)

                Text(mitzvotCount == 1 ? "1 MITZVAH" : "\(mitzvotCount) MITZVOT")
                    .font(.system(size: 30, weight: .bold, design: .serif))
                    .multilineTextAlignment(.center)
                    .foregroundStyle(ultimateNeon)

                neonGoldRule

                Text("Current Level")
                    .font(.system(size: 13, weight: .medium, design: .serif))
                    .foregroundColor(AppColor.textMuted)

                Text(levelTitle.uppercased())
                    .font(.system(size: 17, weight: .bold, design: .serif))
                    .multilineTextAlignment(.center)
                    .foregroundStyle(ultimateNeon)
                    .padding(.horizontal, 18)
                    .padding(.vertical, 10)
                    .background(
                        Capsule()
                            .fill(Color(red: 0.12, green: 0.08, blue: 0.22).opacity(0.35))
                    )
                    .overlay(
                        Capsule()
                            .stroke(ultimateNeon, lineWidth: 2)
                    )

                VStack(spacing: 5) {
                    Text("Legendary Tier - thank you")
                    Text("for lighting up the world.")
                    Text("Keep going!")
                }
                .font(.system(size: 14, weight: .medium, design: .serif))
                .multilineTextAlignment(.center)
                .foregroundColor(AppColor.textPrimary.opacity(0.92))
                .padding(.top, 2)

                Button(action: { onRequestFinalRewardVideo?() }) {
                    Group {
                        if let ui = bundledAppIcon {
                            Image(uiImage: ui)
                                .resizable()
                                .interpolation(.high)
                                .scaledToFit()
                                .padding(6)
                        } else {
                            Image(systemName: "app.fill")
                                .font(.system(size: 40))
                                .foregroundStyle(ultimateNeon)
                                .frame(maxWidth: .infinity, maxHeight: .infinity)
                        }
                    }
                    .frame(width: 76, height: 76)
                    .clipShape(RoundedRectangle(cornerRadius: 16, style: .continuous))
                    .overlay(
                        RoundedRectangle(cornerRadius: 16, style: .continuous)
                            .stroke(ultimateNeon, lineWidth: 2.5)
                    )
                }
                .buttonStyle(.plain)
                .padding(.top, 8)
                .accessibilityLabel("Mitz Mode")
            }
            .padding(.horizontal, 16)
            .padding(.vertical, 16)
        }
        .padding(.vertical, 4)
    }

    private func certificateCornerFlourishUltimate(
        alignment: Alignment,
        leading: CGFloat,
        trailing: CGFloat,
        top: CGFloat,
        bottom: CGFloat
    ) -> some View {
        StarOfDavid()
            .stroke(
                LinearGradient(
                    colors: [Color.cyan.opacity(0.5), Color.purple.opacity(0.5)],
                    startPoint: .top,
                    endPoint: .bottom
                ),
                lineWidth: 0.75
            )
            .frame(width: 11, height: 11)
            .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: alignment)
            .padding(.leading, leading)
            .padding(.trailing, trailing)
            .padding(.top, top)
            .padding(.bottom, bottom)
    }

    private var neonGoldRule: some View {
        HStack {
            Spacer()
            Rectangle()
                .fill(
                    LinearGradient(
                        colors: [.clear, Color.cyan.opacity(0.85), Color.purple.opacity(0.85), .clear],
                        startPoint: .leading,
                        endPoint: .trailing
                    )
                )
                .frame(width: 220, height: 2)
            Spacer()
        }
    }

    private func certificateCornerFlourish(
        _ gold: Color,
        alignment: Alignment,
        leading: CGFloat,
        trailing: CGFloat,
        top: CGFloat,
        bottom: CGFloat
    ) -> some View {
        StarOfDavid()
            .stroke(gold.opacity(0.38), lineWidth: 0.75)
            .frame(width: 11, height: 11)
            .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: alignment)
            .padding(.leading, leading)
            .padding(.trailing, trailing)
            .padding(.top, top)
            .padding(.bottom, bottom)
    }

    private var goldRule: some View {
        HStack {
            Spacer()
            Rectangle()
                .fill(
                    LinearGradient(
                        colors: [.clear, AppColor.goldBorder, .clear],
                        startPoint: .leading,
                        endPoint: .trailing
                    )
                )
                .frame(width: 200, height: 2)
            Spacer()
        }
    }
}

