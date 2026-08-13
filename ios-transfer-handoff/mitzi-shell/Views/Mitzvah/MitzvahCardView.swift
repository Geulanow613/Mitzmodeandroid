import SwiftUI
import UIKit

/// Premium mitzvah dialog aligned with Android `MitzvahDialog` (see `ios_mitzvah_dialog_android_exact_line_reference.txt`).
struct MitzvahCardView: View {
    let mitzvah: Mitzvah
    let onAccept: () -> Void
    let onNext: () -> Void
    let onDismiss: () -> Void
    @Binding var textSize: Double
    @Binding var hasAccepted: Bool
    @ObservedObject var viewModel: DailyMitzvotViewModel

    @State private var showingSparkles = false
    @State private var holyFlashOpacity: CGFloat = 0
    /// Burst hub in unit coords (0–1); animated for sweep flashes, center for legacy burst.
    @State private var holyFlashHubUnit: CGPoint = CGPoint(x: 0.5, y: 0.5)
    /// Last accept flash variant — next pick excludes this so we never repeat twice in a row.
    @State private var lastHolyFlashStyle: HolyFlashSweepStyle?
    @State private var acceptFlashInProgress = false

    private let cardCorner: CGFloat = AppRadius.card
    private let contentHorizontalInset: CGFloat = 28
    private let ruleInset: CGFloat = 24

    var body: some View {
        GeometryReader { geo in
            let scrollSize = Swift.max(Swift.min(geo.size.width * 0.28, 108), 96)
            let torahMetrics = TorahScrollLayout.metrics(for: scrollSize)
            /// Space for the portion of the scroll that sits above the card top (mitzcard-style).
            let cardTopPad = torahMetrics.totalHeight * 0.86
            let maxBodyScrollH = geo.size.height * 0.36
            // Hub for ambient rays: slightly above card top (reference: fan behind scroll)
            let estCardH = Swift.min(geo.size.height * 0.72, 580)
            let cardTopY = geo.size.height * 0.5 - estCardH * 0.5
            let ambientHubFraction = Swift.min(0.44, Swift.max(0.24, (cardTopY + torahMetrics.totalHeight * 0.46) / Swift.max(geo.size.height, 1)))

            ZStack {
                // (1) SCRIM — Android dismiss tap
                AppColor.scrim
                    .ignoresSafeArea()
                    .onTapGesture { onDismiss() }

                // Idle “holy light” behind card (reference mitzcard.png — full-screen fan)
                AmbientHolyLightCanvas(centerYFraction: ambientHubFraction)
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
                    .opacity(Swift.max(CGFloat(0.22), CGFloat(1) - holyFlashOpacity * CGFloat(0.88)))
                    .blendMode(.softLight)
                    .allowsHitTesting(false)

                // (2) Card — shrink-wrap vertically (fixedSize) so Spacers cannot inject infinite height / blank parchment.
                VStack(spacing: 0) {
                    Spacer(minLength: 0)
                    cardBody(
                        scrollSize: scrollSize,
                        torahMetrics: torahMetrics,
                        layoutWidth: geo.size.width,
                        maxBodyScrollHeight: maxBodyScrollH
                    )
                        .fixedSize(horizontal: false, vertical: true)
                        .padding(.horizontal, 18)
                        .padding(.vertical, 4)
                        .overlay(alignment: .top) {
                            scrollWithRays(size: scrollSize)
                                .frame(width: torahMetrics.totalWidth, height: torahMetrics.totalHeight)
                                /// Scroll sits mostly above the card; only the lower band overlaps the top border.
                                .offset(y: -torahMetrics.totalHeight * 0.82 + 2)
                                .allowsHitTesting(false)
                        }
                        .padding(.top, cardTopPad)
                    Spacer(minLength: 0)
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)

                // (3) Accept burst (Section H)
                if holyFlashOpacity > 0.01 {
                    HolyLightFlashCanvas(centerUnit: holyFlashHubUnit)
                        .frame(maxWidth: .infinity, maxHeight: .infinity)
                        .opacity(holyFlashOpacity)
                        .blendMode(.plusLighter)
                        .allowsHitTesting(false)
                }
            }
        }
    }

    @ViewBuilder
    private func scrollWithRays(size: CGFloat) -> some View {
        TorahScrollArtwork(layoutSpan: size)
            .fixedSize(horizontal: true, vertical: true)
            .shadow(color: Color(red: 1, green: 0.88, blue: 0.45).opacity(0.32), radius: 8, y: 2)
            .shadow(color: Color(red: 1, green: 0.95, blue: 0.75).opacity(0.14), radius: 12, y: 0)
    }

    @ViewBuilder
    private func cardBody(
        scrollSize: CGFloat,
        torahMetrics: TorahScrollLayout.Metrics,
        layoutWidth: CGFloat,
        maxBodyScrollHeight: CGFloat
    ) -> some View {
        ZStack {
            ParchmentSurface()
            cardContent(
                scrollSize: scrollSize,
                torahMetrics: torahMetrics,
                layoutWidth: layoutWidth,
                maxBodyScrollHeight: maxBodyScrollHeight
            )
        }
        .clipShape(RoundedRectangle(cornerRadius: cardCorner))
        .overlay(
            RoundedRectangle(cornerRadius: cardCorner)
                .stroke(AppColor.goldBorder.opacity(0.62), lineWidth: 1.5)
        )
        .overlay(
            RoundedRectangle(cornerRadius: max(8, cardCorner - 4))
                .stroke(AppColor.goldBorder.opacity(0.38), lineWidth: 0.65)
                .padding(3)
        )
        .shadow(color: Color.black.opacity(0.18), radius: 14, x: 0, y: 6)
        .shadow(color: AppColor.goldBorder.opacity(0.09), radius: 18, x: 0, y: 3)
    }

    /// UIKit sizing — avoids SwiftUI ScrollView + GeometryReader lying about height inside flexible layouts.
    private func mitzvahBodyIntrinsicHeight(layoutWidth: CGFloat, bodyLineExtra: CGFloat) -> CGFloat {
        let textColumnWidth = max(
            80,
            layoutWidth - 36 - contentHorizontalInset * 2
        )
        let baseDescriptor = UIFont.systemFont(ofSize: CGFloat(textSize)).fontDescriptor
        let serifDescriptor = baseDescriptor.withDesign(.serif) ?? baseDescriptor
        let font = UIFont(descriptor: serifDescriptor, size: CGFloat(textSize))
        let para = NSMutableParagraphStyle()
        para.lineSpacing = bodyLineExtra
        para.alignment = .center
        let attrs: [NSAttributedString.Key: Any] = [
            .font: font,
            .paragraphStyle: para
        ]
        let textRect = (mitzvah.text as NSString).boundingRect(
            with: CGSize(width: textColumnWidth, height: CGFloat.greatestFiniteMagnitude),
            options: [.usesLineFragmentOrigin, .usesFontLeading],
            attributes: attrs,
            context: nil
        )
        var height = ceil(textRect.height)
        if !mitzvah.links.isEmpty {
            height += 6
        }
        let linkBase = UIFont.systemFont(ofSize: CGFloat(textSize + 1.5)).fontDescriptor
        let linkDesc = linkBase.withDesign(.serif) ?? linkBase
        let linkFont = UIFont(descriptor: linkDesc, size: CGFloat(textSize + 1.5))
        for link in mitzvah.links {
            let lr = (link.displayText as NSString).boundingRect(
                with: CGSize(width: textColumnWidth, height: CGFloat.greatestFiniteMagnitude),
                options: [.usesLineFragmentOrigin, .usesFontLeading],
                attributes: [.font: linkFont],
                context: nil
            )
            height += ceil(lr.height) + 10
        }
        return height + 14
    }

    @ViewBuilder
    private func cardContent(
        scrollSize: CGFloat,
        torahMetrics: TorahScrollLayout.Metrics,
        layoutWidth: CGFloat,
        maxBodyScrollHeight: CGFloat
    ) -> some View {
        let bodyUIFont: UIFont = {
            let d = UIFont.systemFont(ofSize: CGFloat(textSize)).fontDescriptor
            let serif = d.withDesign(.serif) ?? d
            return UIFont(descriptor: serif, size: CGFloat(textSize))
        }()
        let bodyLineExtra = Swift.max(0, CGFloat(textSize) * 1.55 - bodyUIFont.lineHeight)
        let bodyIntrinsic = mitzvahBodyIntrinsicHeight(layoutWidth: layoutWidth, bodyLineExtra: bodyLineExtra)
        let bodyScrollFrameHeight = Swift.min(Swift.max(bodyIntrinsic, 44), maxBodyScrollHeight)

        VStack(spacing: 0) {
            // Clear the Torah scroll overlap before the star (scroll sits high; needs extra gap).
            Spacer().frame(height: max(26, torahMetrics.totalHeight * 0.40))

            StarOfDavid()
                .stroke(AppColor.goldBorder.opacity(0.68), lineWidth: 1.2)
                .frame(width: 18, height: 18)

            Spacer().frame(height: 6)

            HStack(alignment: .center, spacing: 0) {
                HStack(spacing: 0) {
                    Button { textSize = max(12, textSize - 2) } label: {
                        Text("a")
                            .font(.system(size: 12, weight: .medium))
                            .foregroundColor(AppColor.textPrimary.opacity(0.48))
                            .frame(minWidth: 22, maxHeight: 16, alignment: .center)
                            .contentShape(Rectangle())
                    }
                    .buttonStyle(.plain)
                    Button { textSize = min(22, textSize + 2) } label: {
                        Text("A")
                            .font(.system(size: 15, weight: .semibold))
                            .foregroundColor(AppColor.textPrimary.opacity(0.48))
                            .frame(minWidth: 22, maxHeight: 16, alignment: .center)
                            .contentShape(Rectangle())
                    }
                    .buttonStyle(.plain)
                }

                Spacer(minLength: 0)

                Button(action: onDismiss) {
                    Image(systemName: "xmark")
                        .font(.system(size: 12, weight: .semibold))
                        .foregroundColor(AppColor.textPrimary.opacity(0.42))
                        .frame(minWidth: 26, maxHeight: 16, alignment: .center)
                        .contentShape(Rectangle())
                }
                .buttonStyle(.plain)
            }
            .padding(.vertical, 5)
            .padding(.horizontal, ruleInset)

            Spacer().frame(height: 7)

            Rectangle()
                .fill(AppColor.goldBorder.opacity(0.24))
                .frame(height: 0.5)
                .padding(.horizontal, ruleInset)

            Spacer().frame(height: 12)

            ScrollView {
                VStack(alignment: .center, spacing: 6) {
                    TranslatableText(
                        source: mitzvah.text,
                        font: .system(size: textSize, weight: .regular, design: .serif),
                        color: AppColor.textPrimary,
                        alignment: .center
                    )
                    .lineSpacing(bodyLineExtra)
                    .frame(maxWidth: .infinity)

                    ForEach(mitzvah.links, id: \.url) { link in
                        AppLinkText(
                            displayText: link.displayText,
                            urlString: link.url,
                            fontSize: CGFloat(textSize + 1.5)
                        )
                        .padding(.top, 4)
                    }
                }
                .padding(.horizontal, contentHorizontalInset)
                .padding(.vertical, 3)
            }
            .id(mitzvah.id)
            .frame(height: bodyScrollFrameHeight)

            Rectangle()
                .fill(AppColor.goldBorder.opacity(0.24))
                .frame(height: 0.5)
                .padding(.horizontal, ruleInset)
                .padding(.top, 3)
                .padding(.bottom, 2)

            HStack(spacing: 14) {
                ZStack {
                    TranslatableHeirloomButton(
                        source: (hasAccepted || acceptFlashInProgress) ? "Accepted ✓" : "Accept",
                        enabled: !hasAccepted && !acceptFlashInProgress
                    ) {
                        acceptTapped()
                    }
                    if showingSparkles {
                        SparkleView()
                            .allowsHitTesting(false)
                    }
                }
                .frame(maxWidth: .infinity)

                TranslatableHeirloomButton(source: "Next", enabled: true, action: onNext)
                    .frame(maxWidth: .infinity)
            }
            .padding(.horizontal, 22)
            .padding(.top, 6)
            .padding(.bottom, 12)
        }
    }

    private func acceptTapped() {
        guard !hasAccepted, !acceptFlashInProgress else { return }
        acceptFlashInProgress = true
        showingSparkles = true
        onAccept()

        let candidates: [HolyFlashSweepStyle]
        if let last = lastHolyFlashStyle {
            candidates = HolyFlashSweepStyle.allCases.filter { $0 != last }
        } else {
            candidates = Array(HolyFlashSweepStyle.allCases)
        }
        let style = candidates.randomElement() ?? .centered
        lastHolyFlashStyle = style

        holyFlashHubUnit = style.startUnit
        holyFlashOpacity = 0

        let riseDuration: TimeInterval = 0.075
        let sweepDuration: TimeInterval = 0.38
        let fadeStart: TimeInterval = 0.22
        let fadeDuration: TimeInterval = 0.52

        withAnimation(.linear(duration: riseDuration)) {
            holyFlashOpacity = 1
        }
        if style.usesSweep {
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.05) {
                withAnimation(.linear(duration: sweepDuration)) {
                    holyFlashHubUnit = style.endUnit
                }
            }
        } else {
            holyFlashHubUnit = style.endUnit
        }

        DispatchQueue.main.asyncAfter(deadline: .now() + fadeStart) {
            withAnimation(.easeOut(duration: fadeDuration)) {
                holyFlashOpacity = 0
            }
        }

        let flashTotal = fadeStart + fadeDuration + 0.08
        DispatchQueue.main.asyncAfter(deadline: .now() + flashTotal) {
            holyFlashHubUnit = CGPoint(x: 0.5, y: 0.5)
            showingSparkles = false
            acceptFlashInProgress = false
        }
    }
}

// MARK: - Holy light accept flash (luminous spike — radial or sweeping hub)

/// How the accept “holy light” hub moves; same canvas art, hub animates for sweep variants.
private enum HolyFlashSweepStyle: CaseIterable {
    /// Original: full-screen burst from the center of the view.
    case centered
    case sweepLeftToRight
    case sweepBottomToTop
    case sweepRightToLeft
    case sweepTopToBottom

    var startUnit: CGPoint {
        switch self {
        case .centered:
            return CGPoint(x: 0.5, y: 0.5)
        case .sweepLeftToRight:
            return CGPoint(x: -0.12, y: 0.5)
        case .sweepBottomToTop:
            return CGPoint(x: 0.5, y: 1.12)
        case .sweepRightToLeft:
            return CGPoint(x: 1.12, y: 0.5)
        case .sweepTopToBottom:
            return CGPoint(x: 0.5, y: -0.12)
        }
    }

    var endUnit: CGPoint {
        switch self {
        case .centered:
            return CGPoint(x: 0.5, y: 0.5)
        case .sweepLeftToRight:
            return CGPoint(x: 1.12, y: 0.5)
        case .sweepBottomToTop:
            return CGPoint(x: 0.5, y: -0.12)
        case .sweepRightToLeft:
            return CGPoint(x: -0.12, y: 0.5)
        case .sweepTopToBottom:
            return CGPoint(x: 0.5, y: 1.12)
        }
    }

    var usesSweep: Bool { self != .centered }
}

/// Bright core + feathered rays + halo rings + star; parent applies `.opacity` + `.plusLighter`.
private struct HolyLightFlashCanvas: View {
    /// Burst hub in unit coordinates (0–1); values outside 0–1 place the hub off-screen for sweeps.
    var centerUnit: CGPoint

    var body: some View {
        Canvas { context, size in
            let center = CGPoint(x: centerUnit.x * size.width, y: centerUnit.y * size.height)
            let maxDim = hypot(size.width, size.height)
            let corners = [
                CGPoint.zero,
                CGPoint(x: size.width, y: 0),
                CGPoint(x: size.width, y: size.height),
                CGPoint(x: 0, y: size.height)
            ]
            let maxCornerDist = corners.map { hypot($0.x - center.x, $0.y - center.y) }.max() ?? maxDim
            let veilEndRadius = Swift.max(maxDim * 0.78, maxCornerDist * 1.04)
            let beamLength = Swift.max(maxDim * 1.02, maxCornerDist * 1.08)

            let midX = size.width * 0.5
            let midY = size.height * 0.5
            /// How far the hub sits past the horizontal/vertical midline (0 = centered, ~1+ when off-screen for sweeps).
            let hubStressH = min(1.35, abs(center.x - midX) / max(size.width * 0.38, 1))
            let hubStressV = min(1.35, abs(center.y - midY) / max(size.height * 0.38, 1))
            /// Wide feathered beams + backward-pointing rays clip against the left/right edges and read as “spider web” streaks; tighten and cull when hub is side-loaded.
            let outerWBase = size.width * 0.22
            let outerW = outerWBase * (1 - 0.42 * hubStressH)

            // 1. Soft luminous veil — radial falloff only (no opaque full-screen fill).
            context.fill(
                Path(CGRect(origin: .zero, size: size)),
                with: .radialGradient(
                    Gradient(stops: [
                        .init(color: Color.white.opacity(0.38), location: 0),
                        .init(color: Color(red: 1, green: 0.98, blue: 0.93).opacity(0.22), location: 0.06),
                        .init(color: Color(red: 1, green: 0.93, blue: 0.62).opacity(0.11), location: 0.18),
                        .init(color: Color(red: 1, green: 0.84, blue: 0.38).opacity(0.04), location: 0.38),
                        .init(color: .clear, location: 1)
                    ]),
                    center: center,
                    startRadius: 0,
                    endRadius: veilEndRadius
                )
            )

            // 2. Twelve feathered beams — airy streaks, not heavy wedges.
            let apexW = Swift.max(12, size.width * 0.016)
            for i in 0..<12 {
                let angle = CGFloat(Double(i) * 30 * .pi / 180)
                let peak: CGFloat = (i % 2 == 0) ? 0.48 : 0.28
                let cosA = cos(angle)
                let sinA = sin(angle)
                var dirAtten: CGFloat = 1
                if hubStressH > 0.38 {
                    let inward = center.x < midX ? 1.0 : -1.0
                    let towardInterior = inward * cosA
                    dirAtten *= holyFlashSmoothAttenuation(towardInterior)
                }
                if hubStressV > 0.38 {
                    let inward = center.y < midY ? 1.0 : -1.0
                    let towardInterior = inward * sinA
                    dirAtten *= holyFlashSmoothAttenuation(towardInterior)
                }
                let peakUse = peak * dirAtten
                guard peakUse > 0.012 else { continue }
                fillFeatheredFanBeam(
                    context: &context,
                    center: center,
                    angle: angle,
                    length: beamLength,
                    apexHalfWidth: apexW,
                    outerHalfWidth: outerW,
                    peakAlpha: peakUse
                )
            }

            // 3. Tight white “spark” at center — sells the flash.
            let sparkR = maxDim * 0.07
            context.fill(
                Path(ellipseIn: CGRect(x: center.x - sparkR, y: center.y - sparkR, width: sparkR * 2, height: sparkR * 2)),
                with: .radialGradient(
                    Gradient(stops: [
                        .init(color: Color.white.opacity(0.92), location: 0),
                        .init(color: Color.white.opacity(0.25), location: 0.45),
                        .init(color: .clear, location: 1)
                    ]),
                    center: center,
                    startRadius: 0,
                    endRadius: sparkR
                )
            )

            // 4. Thin halo rings — partial arcs at the **horizontal** edges read as “webbing”; fade only for side sweeps (leave top/bottom halos unchanged).
            let haloOpacityScale = 1 - 0.55 * hubStressH.clamped(to: 0...1)
            for ring in 1...4 {
                let ringR = maxDim * (0.11 + 0.06 * CGFloat(ring))
                context.stroke(
                    Path(ellipseIn: CGRect(x: center.x - ringR, y: center.y - ringR, width: ringR * 2, height: ringR * 2)),
                    with: .color(Color.white.opacity(0.38 / CGFloat(ring) * haloOpacityScale)),
                    style: StrokeStyle(lineWidth: CGFloat(2 - ring / 3), lineCap: .round)
                )
            }

            // 5. Star of David — gold aura + bright inner stroke (shrink near edges so strokes don’t crawl the clip bounds).
            let starRUncapped = min(76, size.width * 0.19)
            let inset = min(center.x, size.width - center.x, center.y, size.height - center.y)
            let starR = min(starRUncapped, max(28, inset * 0.92))
            let starPath = holyFlashStarOfDavidPath(center: center, radius: starR)
            context.stroke(
                starPath,
                with: .color(AppColor.goldRay.opacity(0.72)),
                style: StrokeStyle(lineWidth: 5, lineCap: .round, lineJoin: .round)
            )
            context.stroke(
                starPath,
                with: .color(Color.white.opacity(0.55)),
                style: StrokeStyle(lineWidth: 1.8, lineCap: .round, lineJoin: .round)
            )
        }
        .ignoresSafeArea()
    }
}

/// Fades rays that point back off-screen when the flash hub sits on a side (reduces clipped “web” streaks).
private func holyFlashSmoothAttenuation(_ towardInterior: CGFloat) -> CGFloat {
    smoothstep(0.02, 0.28, towardInterior)
}

private func smoothstep(_ edge0: CGFloat, _ edge1: CGFloat, _ x: CGFloat) -> CGFloat {
    let t = ((x - edge0) / (edge1 - edge0)).clamped(to: 0...1)
    return t * t * (3 - 2 * t)
}

// MARK: - Full-screen ambient holy light (idle — soft feathered beams like mitzcard)

/// Trapezoid beam with lateral linear gradient so edges fade smoothly (no hard wedge outlines).
private func fillFeatheredFanBeam(
    context: inout GraphicsContext,
    center: CGPoint,
    angle: CGFloat,
    length: CGFloat,
    apexHalfWidth: CGFloat,
    outerHalfWidth: CGFloat,
    peakAlpha: CGFloat
) {
    guard peakAlpha > 0.004 else { return }
    let c = cos(angle)
    let s = sin(angle)
    let px = -s
    let py = c
    let tip = CGPoint(x: center.x + c * length, y: center.y + s * length)

    var path = Path()
    path.move(to: CGPoint(x: center.x + px * (-apexHalfWidth), y: center.y + py * (-apexHalfWidth)))
    path.addLine(to: CGPoint(x: center.x + px * apexHalfWidth, y: center.y + py * apexHalfWidth))
    path.addLine(to: CGPoint(x: tip.x + px * outerHalfWidth, y: tip.y + py * outerHalfWidth))
    path.addLine(to: CGPoint(x: tip.x - px * outerHalfWidth, y: tip.y - py * outerHalfWidth))
    path.closeSubpath()

    let extend = outerHalfWidth * 2.35
    let g0 = CGPoint(x: tip.x - px * extend, y: tip.y - py * extend)
    let g1 = CGPoint(x: tip.x + px * extend, y: tip.y + py * extend)

    let edge = Color(red: 1, green: 0.94, blue: 0.72)
    let core = Color(red: 1, green: 0.86, blue: 0.38)

    context.fill(
        path,
        with: .linearGradient(
            Gradient(stops: [
                .init(color: edge.opacity(0), location: 0),
                .init(color: core.opacity(peakAlpha * 0.75), location: 0.38),
                .init(color: core.opacity(peakAlpha), location: 0.5),
                .init(color: core.opacity(peakAlpha * 0.72), location: 0.62),
                .init(color: edge.opacity(0), location: 1)
            ]),
            startPoint: g0,
            endPoint: g1
        )
    )
}

private struct AmbientHolyLightCanvas: View {
    var centerYFraction: CGFloat

    var body: some View {
        Canvas { context, size in
            let cx = size.width * 0.5
            let cy = size.height * centerYFraction
            let maxR = hypot(size.width, size.height) * 1.18

            let hubW = size.width * 0.52
            let hubH = Swift.max(size.height * 0.088, 74)
            let hubRect = CGRect(x: cx - hubW / 2, y: cy - hubH / 2, width: hubW, height: hubH)
            context.fill(
                Path(ellipseIn: hubRect),
                with: .radialGradient(
                    Gradient(stops: [
                        .init(color: Color(red: 1, green: 0.97, blue: 0.84).opacity(0.88), location: 0),
                        .init(color: Color(red: 1, green: 0.91, blue: 0.56).opacity(0.48), location: 0.36),
                        .init(color: Color(red: 1, green: 0.82, blue: 0.38).opacity(0.14), location: 0.76),
                        .init(color: .clear, location: 1)
                    ]),
                    center: CGPoint(x: cx, y: cy),
                    startRadius: 0,
                    endRadius: Swift.max(hubW, hubH) * 0.52
                )
            )

            let apexW = Swift.max(12, size.width * 0.022)
            let outerW = size.width * 0.27

            for i in 0..<8 {
                let angle = CGFloat(Double(i) * 45 * .pi / 180)
                let sinA = sin(angle)
                let upFactor = CGFloat((1 - sinA) / 2).clamped(to: 0...1)
                let baseA: CGFloat = (i % 2 == 0) ? 0.26 : 0.17
                let beamAlpha = baseA * (0.34 + 0.66 * upFactor)
                if beamAlpha < 0.008 { continue }

                fillFeatheredFanBeam(
                    context: &context,
                    center: CGPoint(x: cx, y: cy),
                    angle: angle,
                    length: maxR * 0.92,
                    apexHalfWidth: apexW,
                    outerHalfWidth: outerW,
                    peakAlpha: beamAlpha
                )
            }

            let haloR = maxR * 0.55
            context.fill(
                Path(ellipseIn: CGRect(x: cx - haloR, y: cy - haloR * 0.8, width: haloR * 2, height: haloR * 1.62)),
                with: .radialGradient(
                    Gradient(stops: [
                        .init(color: Color(red: 1, green: 0.99, blue: 0.91).opacity(0.38), location: 0),
                        .init(color: Color(red: 1, green: 0.93, blue: 0.56).opacity(0.22), location: 0.44),
                        .init(color: Color(red: 1, green: 0.85, blue: 0.4).opacity(0.065), location: 0.78),
                        .init(color: .clear, location: 1)
                    ]),
                    center: CGPoint(x: cx, y: cy),
                    startRadius: 0,
                    endRadius: haloR
                )
            )
        }
    }
}

private func holyFlashStarOfDavidPath(center: CGPoint, radius: CGFloat) -> Path {
    let cos30 = CGFloat(cos(30.0 * .pi / 180.0))
    let sin30 = CGFloat(sin(30.0 * .pi / 180.0))
    var path = Path()
    path.move(to: CGPoint(x: center.x, y: center.y - radius))
    path.addLine(to: CGPoint(x: center.x + radius * cos30, y: center.y + radius * sin30))
    path.addLine(to: CGPoint(x: center.x - radius * cos30, y: center.y + radius * sin30))
    path.closeSubpath()
    path.move(to: CGPoint(x: center.x, y: center.y + radius))
    path.addLine(to: CGPoint(x: center.x + radius * cos30, y: center.y - radius * sin30))
    path.addLine(to: CGPoint(x: center.x - radius * cos30, y: center.y - radius * sin30))
    path.closeSubpath()
    return path
}

private extension Comparable {
    func clamped(to range: ClosedRange<Self>) -> Self {
        min(max(self, range.lowerBound), range.upperBound)
    }
}

// MARK: - Torah scroll (taller strip, wider parchment + rollers)

private enum TorahScrollLayout {
    struct Metrics: Equatable {
        let parchmentW: CGFloat
        let parchmentH: CGFloat
        let rollerW: CGFloat
        let rollerBarW: CGFloat
        var totalWidth: CGFloat { rollerW * 2 + parchmentW + 2 }
        var totalHeight: CGFloat { parchmentH }
    }

    /// Wider cream strip and stouter rollers; `layoutSpan` follows card `scrollSize`.
    static func metrics(for layoutSpan: CGFloat) -> Metrics {
        let parchmentW = min(62, max(46, layoutSpan * 0.54))
        let parchmentH = min(58, max(50, layoutSpan * 0.60))
        return Metrics(
            parchmentW: parchmentW,
            parchmentH: parchmentH,
            rollerW: 9,
            rollerBarW: 8
        )
    }
}

private struct TorahScrollArtwork: View {
    var layoutSpan: CGFloat

    private let woodDark = Color(red: 0.28, green: 0.17, blue: 0.09)
    private let woodMid = Color(red: 0.42, green: 0.28, blue: 0.16)
    private let woodLight = Color(red: 0.52, green: 0.38, blue: 0.24)
    private let parchment = Color(red: 0.99, green: 0.96, blue: 0.88)
    private let parchmentShadow = Color(red: 0.94, green: 0.88, blue: 0.72)

    private var m: TorahScrollLayout.Metrics { TorahScrollLayout.metrics(for: layoutSpan) }
    private var rollerH: CGFloat { m.parchmentH }

    var body: some View {
        HStack(alignment: .center, spacing: 0) {
            rollerEnd(isLeading: true)
            parchmentStrip
            rollerEnd(isLeading: false)
        }
        .padding(.horizontal, 1)
    }

    private func rollerEnd(isLeading: Bool) -> some View {
        ZStack {
            RoundedRectangle(cornerRadius: 5, style: .continuous)
                .fill(
                    LinearGradient(
                        colors: isLeading
                            ? [woodLight, woodMid, woodDark]
                            : [woodDark, woodMid, woodLight],
                        startPoint: .leading,
                        endPoint: .trailing
                    )
                )
                .frame(width: m.rollerBarW, height: rollerH)
                .overlay(
                    RoundedRectangle(cornerRadius: 5, style: .continuous)
                        .stroke(
                            LinearGradient(
                                colors: [
                                    AppColor.goldBorder.opacity(0.55),
                                    AppColor.androidGoldSpot.opacity(0.75),
                                    AppColor.goldBorder.opacity(0.45)
                                ],
                                startPoint: .top,
                                endPoint: .bottom
                            ),
                            lineWidth: 1
                        )
                )
            VStack(spacing: Swift.max(6, rollerH * 0.12)) {
                Rectangle()
                    .fill(Color.black.opacity(0.22))
                    .frame(height: 1)
                Rectangle()
                    .fill(Color.white.opacity(0.15))
                    .frame(height: 1)
            }
            .frame(height: rollerH * 0.52)
            Capsule()
                .fill(
                    LinearGradient(
                        colors: [
                            AppColor.androidGoldSpot.opacity(0.95),
                            AppColor.goldBorder.opacity(0.88)
                        ],
                        startPoint: .top,
                        endPoint: .bottom
                    )
                )
                .frame(width: 3, height: rollerH * 0.88)
                .overlay(Capsule().stroke(Color.white.opacity(0.35), lineWidth: 0.5))
        }
        .frame(width: m.rollerW, height: rollerH)
    }

    private var parchmentStrip: some View {
        ZStack {
            RoundedRectangle(cornerRadius: 5, style: .continuous)
                .fill(
                    LinearGradient(
                        colors: [parchment, parchmentShadow],
                        startPoint: .topLeading,
                        endPoint: .bottomTrailing
                    )
                )
                .overlay(
                    RoundedRectangle(cornerRadius: 5, style: .continuous)
                        .stroke(
                            LinearGradient(
                                colors: [
                                    AppColor.goldBorder.opacity(0.65),
                                    AppColor.androidGoldSpot.opacity(0.45)
                                ],
                                startPoint: .top,
                                endPoint: .bottom
                            ),
                            lineWidth: 1
                        )
                )
                .overlay(
                    RoundedRectangle(cornerRadius: 5, style: .continuous)
                        .stroke(Color.white.opacity(0.35), lineWidth: 0.5)
                        .padding(1)
                        .allowsHitTesting(false)
                )
            VStack(spacing: 2.2) {
                ForEach(0..<5, id: \.self) { i in
                    Rectangle()
                        .fill(AppColor.textPrimary.opacity(0.07 + CGFloat(i % 2) * 0.03))
                        .frame(height: 1)
                }
            }
            .padding(.horizontal, 6)
            .padding(.vertical, 8)
        }
        .frame(width: m.parchmentW, height: m.parchmentH)
    }
}
