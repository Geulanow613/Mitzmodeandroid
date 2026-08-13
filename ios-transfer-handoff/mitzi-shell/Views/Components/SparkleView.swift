import SwiftUI

struct SparkleParticle: View {
    let position: CGPoint
    @State private var scale: CGFloat = 0
    @State private var opacity: Double = 0
    
    var body: some View {
        Image(systemName: "sparkle")
            .foregroundColor(.yellow)
            .position(position)
            .scaleEffect(scale)
            .opacity(opacity)
            .onAppear {
                withAnimation(.easeOut(duration: 0.4)) {
                    scale = 1
                    opacity = 1
                }
                withAnimation(.easeIn(duration: 0.2).delay(0.2)) {
                    opacity = 0
                }
            }
    }
}

struct SparkleView: View {
    let sparkleCount = 12
    let radius: CGFloat = 50
    
    var body: some View {
        GeometryReader { geometry in
            ForEach(0..<sparkleCount, id: \.self) { index in
                let angle = (2 * .pi * Double(index)) / Double(sparkleCount)
                let x = cos(angle) * radius + geometry.size.width / 2
                let y = sin(angle) * radius + geometry.size.height / 2
                
                SparkleParticle(position: CGPoint(x: x, y: y))
            }
        }
    }
} 