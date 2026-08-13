import SwiftUI

// MARK: - Tour step and state

enum TourStep: Int, CaseIterable {
    case mitzvahButton = 0
    case menuButton
    case addMitzvahButton
    case levelBadge
}

private let kTourCompletedKey = "tour_completed"
private let kOverlayAlpha: Double = 0.78
private let kSpotlightPadding: CGFloat = 14
private let kCardBottomPadding: CGFloat = 48
private let kGoldColor = Color(red: 1.0, green: 0.843, blue: 0.0) // #FFD700
private let kGlowWhite = Color.white.opacity(0.85)

enum TourCoordinateSpace {
    static let name = "TourRoot"
}

// MARK: - Tour completion persistence

struct TourState {
    static var isCompleted: Bool {
        get { UserDefaults.standard.bool(forKey: kTourCompletedKey) }
        set { UserDefaults.standard.set(newValue, forKey: kTourCompletedKey) }
    }
    
    static func complete() {
        TourState.isCompleted = true
    }
}

// MARK: - Anchor collection (PreferenceKey)

struct TourSpotAnchorKey: PreferenceKey {
    static var defaultValue: [TourStep: Anchor<CGRect>] { [:] }
    static func reduce(value: inout [TourStep: Anchor<CGRect>], nextValue: () -> [TourStep: Anchor<CGRect>]) {
        value.merge(nextValue()) { _, n in n }
    }
}

// MARK: - Frame-based overlay shape (4 rectangles around spotlight)

struct FrameOverlayShape: Shape {
    var holeRect: CGRect
    var boundsSize: CGSize
    
    func path(in rect: CGRect) -> Path {
        let h = holeRect
        let w = boundsSize.width
        let height = boundsSize.height
        var p = Path()
        // Top
        if h.minY > 0 {
            p.addRect(CGRect(x: 0, y: 0, width: w, height: h.minY))
        }
        // Left
        if h.minX > 0 {
            p.addRect(CGRect(x: 0, y: h.minY, width: h.minX, height: h.height))
        }
        // Right
        if h.maxX < w {
            p.addRect(CGRect(x: h.maxX, y: h.minY, width: w - h.maxX, height: h.height))
        }
        // Bottom
        if h.maxY < height {
            p.addRect(CGRect(x: 0, y: h.maxY, width: w, height: height - h.maxY))
        }
        return p
    }
}

private func spotlightCornerRadius(for rect: CGRect) -> CGFloat {
    let minSide = min(rect.width, rect.height)
    if abs(rect.width - rect.height) < 24 {
        return minSide / 2
    }
    return min(minSide / 2, 22)
}

// MARK: - Pulsing glow around the spotlight cutout

private struct SpotlightGlowBorder: View {
    let rect: CGRect
    @State private var pulse = false

    private var isCircular: Bool {
        abs(rect.width - rect.height) < 24
    }

    var body: some View {
        Group {
            if isCircular {
                circleGlow
            } else {
                roundedRectGlow
            }
        }
        .frame(width: rect.width, height: rect.height)
        .offset(x: rect.minX, y: rect.minY)
        .allowsHitTesting(false)
        .onAppear {
            withAnimation(.easeInOut(duration: 0.9).repeatForever(autoreverses: true)) {
                pulse = true
            }
        }
    }

    private var circleGlow: some View {
        ZStack {
            Circle()
                .stroke(kGoldColor.opacity(pulse ? 0.35 : 0.2), lineWidth: pulse ? 14 : 10)
                .blur(radius: pulse ? 10 : 6)

            Circle()
                .stroke(kGlowWhite.opacity(pulse ? 0.55 : 0.35), lineWidth: 1.5)
                .shadow(color: kGlowWhite.opacity(0.45), radius: 4)

            Circle()
                .stroke(kGoldColor.opacity(pulse ? 1.0 : 0.82), lineWidth: pulse ? 3.5 : 2.5)
                .shadow(color: kGoldColor.opacity(pulse ? 0.95 : 0.65), radius: pulse ? 16 : 10)
                .shadow(color: kGoldColor.opacity(pulse ? 0.75 : 0.45), radius: pulse ? 28 : 18)
        }
    }

    private var roundedRectGlow: some View {
        let radius = spotlightCornerRadius(for: rect)
        return ZStack {
            RoundedRectangle(cornerRadius: radius, style: .continuous)
                .stroke(kGoldColor.opacity(pulse ? 0.35 : 0.2), lineWidth: pulse ? 14 : 10)
                .blur(radius: pulse ? 10 : 6)

            RoundedRectangle(cornerRadius: radius, style: .continuous)
                .stroke(kGlowWhite.opacity(pulse ? 0.55 : 0.35), lineWidth: 1.5)
                .shadow(color: kGlowWhite.opacity(0.45), radius: 4)

            RoundedRectangle(cornerRadius: radius, style: .continuous)
                .stroke(kGoldColor.opacity(pulse ? 1.0 : 0.82), lineWidth: pulse ? 3.5 : 2.5)
                .shadow(color: kGoldColor.opacity(pulse ? 0.95 : 0.65), radius: pulse ? 16 : 10)
                .shadow(color: kGoldColor.opacity(pulse ? 0.75 : 0.45), radius: pulse ? 28 : 18)
        }
    }
}

// MARK: - Tour card content per step

private func tourMessage(for step: TourStep) -> String {
    switch step {
    case .mitzvahButton:
        return "Tap the Mitzvah Button any time for a random mitzvah suggestion!"
    case .menuButton:
        return "Tap the menu button (three dots) for blessings, traveler's prayer, the Official App Song, and more. The Holy Light Checklist is on the home screen below."
    case .addMitzvahButton:
        return "Can you think of any more mitzvot we should add to the list? Submit them here!"
    case .levelBadge:
        return "You can check how many mitzvot you've completed here, and your current level."
    }
}

/// Determines optimal card position based on highlighted element location
private func cardPosition(for step: TourStep, spotFrame: CGRect?, screenHeight: CGFloat) -> CardPosition {
    guard let frame = spotFrame else { return .bottom }
    
    let elementMidY = frame.midY
    let screenMidY = screenHeight / 2
    
    switch step {
    case .mitzvahButton:
        // Center button - card at bottom
        return .bottom
    case .menuButton:
        // Top-right menu - card at bottom to avoid notch
        return .bottom
    case .addMitzvahButton:
        // Bottom button - card at middle/top to avoid overlap
        return elementMidY > screenMidY ? .middle : .bottom
    case .levelBadge:
        // Top-right badge - card at middle/bottom to avoid notch and badge
        return .middle
    }
}

private enum CardPosition {
    case top, middle, bottom
    
    var topPadding: CGFloat {
        switch self {
        case .top: return 60
        case .middle: return 0
        case .bottom: return 0
        }
    }
    
    var bottomPadding: CGFloat {
        switch self {
        case .top: return 0
        case .middle: return 0
        case .bottom: return kCardBottomPadding
        }
    }
}

// MARK: - FirstTimeTourOverlay

struct FirstTimeTourOverlay: View {
    @Binding var currentStep: Int
    let spotFrames: [TourStep: CGRect]
    let onComplete: () -> Void
    
    private var step: TourStep {
        TourStep(rawValue: currentStep) ?? .mitzvahButton
    }
    
    private var isLastStep: Bool {
        step.rawValue == TourStep.allCases.count - 1
    }
    
    private func spotlightRect(for raw: CGRect) -> CGRect {
        raw.insetBy(dx: -kSpotlightPadding, dy: -kSpotlightPadding)
    }
    
    var body: some View {
        GeometryReader { proxy in
            let bounds = proxy.size
            let rawSpot = spotFrames[step]
            let hole = rawSpot.map { spotlightRect(for: $0) }
            let position = cardPosition(
                for: step,
                spotFrame: rawSpot,
                screenHeight: bounds.height
            )

            ZStack(alignment: .topLeading) {
                if let hole {
                    FrameOverlayShape(holeRect: hole, boundsSize: bounds)
                        .fill(Color.black.opacity(kOverlayAlpha))
                        .frame(width: bounds.width, height: bounds.height)

                    SpotlightGlowBorder(rect: hole)
                }

                VStack {
                    if position == .middle || position == .bottom {
                        Spacer(minLength: 0)
                    }

                    tourCard
                        .frame(maxWidth: .infinity)
                        .padding(.horizontal, 20)
                        .padding(.top, position.topPadding)
                        .padding(.bottom, position.bottomPadding)

                    if position == .top || position == .middle {
                        Spacer(minLength: 0)
                    }
                }
                .frame(width: bounds.width, height: bounds.height)
            }
            .animation(nil, value: currentStep)
        }
        .transaction { transaction in
            transaction.disablesAnimations = true
        }
        .allowsHitTesting(true)
    }
    
    private var tourCard: some View {
        VStack(alignment: .center, spacing: 0) {
            TranslatableText(
                source: tourMessage(for: step),
                font: .custom("AvenirNext-Medium", size: 16),
                weight: .medium,
                color: .primary,
                alignment: .center
            )
            .frame(maxWidth: .infinity, alignment: .center)
            .multilineTextAlignment(.center)
            .fixedSize(horizontal: false, vertical: true)
            .padding(.top, 20)
            .padding(.horizontal, 20)
            
            // Buttons row: Skip (left), Next/Done + Close (right)
            HStack(alignment: .center) {
                Button(action: {
                    TourState.complete()
                    onComplete()
                }) {
                    TranslatableText(
                        source: "Skip",
                        font: .custom("AvenirNext-DemiBold", size: 16),
                        weight: .semibold,
                        color: .gray,
                        alignment: .center
                    )
                }
                
                Spacer()
                
                HStack(spacing: 8) {
                    Button(action: {
                        if isLastStep {
                            TourState.complete()
                            onComplete()
                        } else {
                            var transaction = Transaction()
                            transaction.disablesAnimations = true
                            withTransaction(transaction) {
                                currentStep += 1
                            }
                        }
                    }) {
                        TranslatableText(
                            source: isLastStep ? "Done" : "Next",
                            font: .custom("AvenirNext-DemiBold", size: 16),
                            weight: .semibold,
                            color: .white,
                            alignment: .center
                        )
                            .padding(.vertical, 10)
                            .padding(.horizontal, 24)
                            .background(kGoldColor)
                            .cornerRadius(20)
                    }

                    Button(action: {
                        TourState.complete()
                        onComplete()
                    }) {
                        Image(systemName: "xmark")
                            .font(.system(size: 15, weight: .semibold))
                            .foregroundColor(.gray)
                            .frame(width: 32, height: 32)
                    }
                }
            }
            .padding(.horizontal, 16)
            .padding(.top, 16)
            .padding(.bottom, 16)
        }
        .background(Color.white)
        .cornerRadius(16)
        .shadow(color: .black.opacity(0.2), radius: 12, x: 0, y: 4)
    }
}

// MARK: - View modifier to report frame for tour

struct TourSpotModifier: ViewModifier {
    let spot: TourStep
    
    func body(content: Content) -> some View {
        content.anchorPreference(key: TourSpotAnchorKey.self, value: .bounds) { anchor in
            [spot: anchor]
        }
    }
}

extension View {
    func tourSpot(_ spot: TourStep) -> some View {
        modifier(TourSpotModifier(spot: spot))
    }
}

#if DEBUG
struct FirstTimeTourOverlay_Previews: PreviewProvider {
    static var previews: some View {
        FirstTimeTourOverlay(
            currentStep: .constant(0),
            spotFrames: [.mitzvahButton: CGRect(x: 87, y: 300, width: 200, height: 200)],
            onComplete: {}
        )
    }
}
#endif
