import SwiftUI

/// Cycling rainbow fill using clock-driven hue (SwiftUI `withAnimation` on `Color` lerps in RGB, which reads as “stuck on red”).
struct RainbowText: View {
    let text: String
    var fontSize: CGFloat = 20
    /// Seconds for one full hue revolution (0 → 1 → wrap).
    var cycleDuration: Double = 2.4

    var body: some View {
        TimelineView(.periodic(from: .now, by: 1.0 / 45.0)) { context in
            let t = context.date.timeIntervalSinceReferenceDate
            let hue = (t / cycleDuration).truncatingRemainder(dividingBy: 1.0)
            Text(text)
                .font(.system(size: fontSize, weight: .bold, design: .rounded))
                .foregroundColor(Color(hue: hue, saturation: 1, brightness: 1))
                .shadow(color: .black, radius: 2, x: 0, y: 0)
        }
    }
}
