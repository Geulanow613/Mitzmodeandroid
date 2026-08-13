import SwiftUI
import AVKit

struct TzaddikVideoView: View {
    @Binding var isPresented: Bool
    @State private var player: AVPlayer?
    @State private var colorIndex = 0
    @State private var scale: CGFloat = 1.0
    
    // Rainbow colors array
    private let colors: [Color] = [.red, .orange, .yellow, .green, .blue, .purple]
    
    var body: some View {
        ZStack {
            Color.black
                .edgesIgnoringSafeArea(.all)
            
            if let player = player {
                VideoPlayer(player: player)
                    .edgesIgnoringSafeArea(.all)
                    .onAppear {
                        // Add observer for video end
                        NotificationCenter.default.addObserver(
                            forName: .AVPlayerItemDidPlayToEndTime,
                            object: player.currentItem,
                            queue: .main
                        ) { _ in
                            withAnimation {
                                isPresented = false
                            }
                        }
                        player.play()
                    }
                    .onDisappear {
                        player.pause()
                        player.seek(to: .zero)
                    }
            }
            
            VStack {
                Text("BLESSED!!")
                    .font(.system(size: 60))
                    .fontWeight(.heavy)
                    .foregroundStyle(colors[colorIndex])
                    .shadow(color: .black, radius: 2, x: 2, y: 2)
                    .scaleEffect(scale)
                    .onAppear {
                        // Start color animation
                        withAnimation(.easeInOut(duration: 0.5).repeatForever()) {
                            scale = 1.2
                        }
                        
                        Timer.scheduledTimer(withTimeInterval: 0.2, repeats: true) { _ in
                            withAnimation(.easeInOut(duration: 0.2)) {
                                colorIndex = (colorIndex + 1) % colors.count
                            }
                        }
                    }
                
                Button(action: {
                    withAnimation {
                        isPresented = false
                    }
                }) {
                    Text("Close")
                        .font(.title2)
                        .foregroundColor(.white)
                        .padding()
                        .background(Color.black.opacity(0.5))
                        .cornerRadius(10)
                }
                .padding(.top, 40)
            }
        }
        .onAppear {
            if let path = Bundle.main.path(forResource: "tzaddik", ofType: "mp4") {
                player = AVPlayer(url: URL(fileURLWithPath: path))
            }
        }
    }
}

#if DEBUG
struct TzaddikVideoView_Previews: PreviewProvider {
    static var previews: some View {
        TzaddikVideoView(isPresented: .constant(true))
    }
}
#endif 