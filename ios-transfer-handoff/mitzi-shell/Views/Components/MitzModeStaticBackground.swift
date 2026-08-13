import SwiftUI

/// Android `LowEndDeviceBackground` parity — static gradient, no video.
struct MitzModeStaticBackground: View {
    var body: some View {
        LinearGradient(
            stops: [
                .init(color: Color(red: 5 / 255, green: 11 / 255, blue: 31 / 255), location: 0),
                .init(color: Color(red: 14 / 255, green: 27 / 255, blue: 71 / 255), location: 0.45),
                .init(color: Color(red: 26 / 255, green: 11 / 255, blue: 61 / 255), location: 0.85),
                .init(color: Color(red: 10 / 255, green: 4 / 255, blue: 32 / 255), location: 1),
            ],
            startPoint: .top,
            endPoint: .bottom
        )
        .ignoresSafeArea()
    }
}
