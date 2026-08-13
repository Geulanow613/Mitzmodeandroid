import SwiftUI

struct MitzvahPopupView: View {
    let mitzvah: Mitzvah
    let onAccept: () -> Void
    let onNext: () -> Void
    let onDismiss: () -> Void
    @Environment(\.colorScheme) var colorScheme
    @State private var isSparkleAnimating = false
    @State private var acceptButtonFrame: CGRect = .zero
    @AppStorage("mitzvahPopupTextSize") private var textSize: Double = 18
    
    var body: some View {
        ZStack {
            // Semi-transparent background
            Color.black.opacity(0.4)
                .edgesIgnoringSafeArea(.all)
                .onTapGesture {
                    onDismiss()
                }
            
            // Popup content
            VStack(spacing: 20) {
                // Close button
                HStack {
                    // Text size buttons
                    HStack(spacing: 8) {
                        Button(action: { textSize = max(12, textSize - 2) }) {
                            Text("A-")
                                .foregroundColor(.gray)
                                .font(.system(size: 18, weight: .medium))
                                .padding(8)
                        }
                        Button(action: { textSize = min(48, textSize + 2) }) {
                            Text("A+")
                                .foregroundColor(.gray)
                                .font(.system(size: 18, weight: .medium))
                                .padding(8)
                        }
                    }
                    .padding(.leading)
                    
                    Spacer()
                    Button(action: onDismiss) {
                        Image(systemName: "xmark")
                            .foregroundColor(.gray)
                            .font(.system(size: 20, weight: .medium))
                            .padding()
                    }
                }
                .padding(.bottom, 48)
                
                // Mitzvah text
                Text(mitzvah.text)
                    .font(.custom("AvenirNext-Bold", size: textSize))
                    .multilineTextAlignment(.center)
                    .padding(.horizontal, 24)
                    .padding(.bottom, 40)
                
                // Bottom buttons
                HStack {
                    // Accept button
                    Button(action: {
                        withAnimation {
                            isSparkleAnimating = true
                        }
                        // Reset animation after delay
                        DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                            isSparkleAnimating = false
                        }
                        onAccept()
                    }) {
                        Text("Accept Mitzvah")
                            .font(.system(size: 18, weight: .medium))
                            .foregroundColor(.white)
                            .frame(height: 44)
                            .frame(maxWidth: .infinity)
                            .background(Color.blue)
                            .cornerRadius(22)
                            .overlay(
                                GeometryReader { geometry in
                                    Color.clear
                                        .preference(key: ButtonFramePreferenceKey.self,
                                                  value: geometry.frame(in: .global))
                                }
                            )
                    }
                    .padding(.leading, 24)
                    .onPreferenceChange(ButtonFramePreferenceKey.self) { frame in
                        acceptButtonFrame = frame
                    }
                    
                    // Next button
                    Button(action: onNext) {
                        Text("Next")
                            .font(.system(size: 18))
                            .foregroundColor(.blue)
                    }
                    .padding(.horizontal, 24)
                }
                .padding(.bottom, 24)
            }
            .background(colorScheme == .dark ? Color(white: 0.2) : Color(red: 0.9, green: 0.95, blue: 1.0))
            .cornerRadius(20)
            .padding(.horizontal, 20)
            
            // Sparkle effect overlay
            if isSparkleAnimating {
                SparkleEffect(isAnimating: $isSparkleAnimating, frame: acceptButtonFrame)
            }
        }
    }
}

// Preference key to get button frame
struct ButtonFramePreferenceKey: PreferenceKey {
    static var defaultValue: CGRect = .zero
    
    static func reduce(value: inout CGRect, nextValue: () -> CGRect) {
        value = nextValue()
    }
} 