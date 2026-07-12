import SwiftUI
import AVFoundation
import UIKit

// MARK: - Milestone → bundled clip (mirrors Android FINAL_REWARD_IOS_PORT_NOTES)

enum RewardVideoCatalog {
    /// Sentinel: play `finalreward.mp4`, not `mitzmodenew14`.
    static let finalRewardSentinel = 100
    private static let milestoneTotals = [1, 10, 50, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 1800]

    /// `1...13` → `mitzmodenew{n}.mp4`; `finalRewardSentinel` → `finalreward.mp4`; `nil` → no reward clip.
    static func clipId(forTotalAcceptedCount count: Int) -> Int? {
        guard let index = milestoneTotals.firstIndex(of: count) else { return nil }
        if count == 1800 { return finalRewardSentinel }
        return index + 1
    }
}

// MARK: - Full-bleed video (no system `VideoPlayer` chrome)

private final class PlayerLayoutView: UIView {
    var hostedLayer: AVPlayerLayer?

    override func layoutSubviews() {
        super.layoutSubviews()
        hostedLayer?.frame = bounds
    }
}

private struct RewardVideoLayerView: UIViewRepresentable {
    let player: AVPlayer

    func makeUIView(context: Context) -> PlayerLayoutView {
        let view = PlayerLayoutView()
        let layer = AVPlayerLayer(player: player)
        layer.videoGravity = .resizeAspect
        view.layer.addSublayer(layer)
        view.hostedLayer = layer
        return view
    }

    func updateUIView(_ uiView: PlayerLayoutView, context: Context) {
        uiView.hostedLayer?.player = player
    }
}

struct RewardVideoPlayer: View {
    let videoNumber: Int
    @Binding var isPresented: Bool
    @State private var player: AVPlayer?
    @State private var endObserver: NSObjectProtocol?
    @State private var isMuted = false

    var body: some View {
        ZStack {
            Color.black
                .ignoresSafeArea()

            if let player {
                RewardVideoLayerView(player: player)
                    .ignoresSafeArea()
            }

            VStack {
                HStack {
                    Spacer()
                    Button {
                        isMuted.toggle()
                    } label: {
                        Image(systemName: isMuted ? "speaker.slash.fill" : "speaker.wave.2.fill")
                            .font(.system(size: 18, weight: .semibold))
                            .foregroundStyle(.white)
                            .padding(12)
                            .background(.ultraThinMaterial, in: Circle())
                    }
                    .accessibilityLabel(isMuted ? "Unmute" : "Mute")

                    Button {
                        dismissPlayer()
                    } label: {
                        Image(systemName: "xmark")
                            .font(.system(size: 16, weight: .bold))
                            .foregroundStyle(.white)
                            .padding(12)
                            .background(.ultraThinMaterial, in: Circle())
                    }
                    .accessibilityLabel("Close")
                }
                .padding(.top, 8)
                .padding(.trailing, 12)

                Spacer()

                Button {
                    dismissPlayer()
                } label: {
                    Text("Close")
                        .font(.system(size: 16, weight: .semibold))
                        .foregroundColor(.white)
                        .padding(.horizontal, 28)
                        .padding(.vertical, 11)
                        .background(
                            Capsule()
                                .fill(AppColor.goldBorder)
                                .overlay(Capsule().stroke(AppColor.goldBright.opacity(0.55), lineWidth: 1))
                        )
                }
                .padding(.bottom, 28)
            }
        }
        .onAppear {
            setupPlayer()
        }
        .onDisappear {
            tearDownPlayer()
        }
        .onChange(of: isMuted) { muted in
            player?.volume = muted ? 0 : 1
        }
    }

    private func dismissPlayer() {
        tearDownPlayer()
        isPresented = false
    }

    private func setupPlayer() {
        let url: URL?
        if videoNumber == RewardVideoCatalog.finalRewardSentinel {
            url = Bundle.main.url(forResource: "finalreward", withExtension: "mp4")
        } else if let path = Bundle.main.path(forResource: "mitzmodenew\(videoNumber)", ofType: "mp4") {
            url = URL(fileURLWithPath: path)
        } else {
            url = nil
        }

        guard let url else {
            print("Reward video not found for id \(videoNumber)")
            isPresented = false
            return
        }

        let newPlayer = AVPlayer(url: url)
        newPlayer.volume = isMuted ? 0 : 1
        player = newPlayer
        newPlayer.play()

        endObserver = NotificationCenter.default.addObserver(
            forName: .AVPlayerItemDidPlayToEndTime,
            object: newPlayer.currentItem,
            queue: .main
        ) { _ in
            dismissPlayer()
        }
    }

    private func tearDownPlayer() {
        if let endObserver {
            NotificationCenter.default.removeObserver(endObserver)
            self.endObserver = nil
        }
        player?.pause()
        player?.replaceCurrentItem(with: nil)
        player = nil
    }
}
