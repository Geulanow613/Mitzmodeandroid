import SwiftUI

struct SparkleEffect: View {
    @Binding var isAnimating: Bool
    let frame: CGRect
    
    var body: some View {
        GeometryReader { geometry in
            ForEach(0..<20) { index in
                SparkleParticleEffect(
                    startPoint: CGPoint(
                        x: frame.midX,
                        y: frame.midY
                    ),
                    angle: .degrees(Double(index) * 18),
                    distance: CGFloat.random(in: 50...100)
                )
                .opacity(isAnimating ? 0 : 1)
                .animation(
                    .easeOut(duration: 0.5)
                    .delay(Double.random(in: 0...0.2)),
                    value: isAnimating
                )
            }
        }
    }
}

struct SparkleParticleEffect: View {
    let startPoint: CGPoint
    let angle: Angle
    let distance: CGFloat
    
    var body: some View {
        Circle()
            .fill(Color.yellow)
            .frame(width: 4, height: 4)
            .position(
                x: startPoint.x + cos(angle.radians) * distance,
                y: startPoint.y + sin(angle.radians) * distance
            )
            .shadow(color: .yellow.opacity(0.5), radius: 2)
    }
} 