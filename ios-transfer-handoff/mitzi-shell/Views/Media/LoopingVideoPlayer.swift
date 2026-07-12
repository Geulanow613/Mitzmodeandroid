import SwiftUI
import AVKit
import AVFoundation

struct LoopingVideoPlayer: UIViewRepresentable {
    let videoName: String
    let inDirectory: String?
    /// Muxed silent/ambient clips: muting avoids audio-decoder work at loop splices (can hitch video).
    var isMuted: Bool

    init(videoName: String, inDirectory: String? = nil, isMuted: Bool = true) {
        self.videoName = videoName
        self.inDirectory = inDirectory
        self.isMuted = isMuted
    }
    
    class Coordinator: NSObject {
        var player: AVQueuePlayer?
        var playerLooper: AVPlayerLooper?
        var playerLayer: AVPlayerLayer?
        var observers: [NSObjectProtocol] = []

        deinit {
            observers.forEach { NotificationCenter.default.removeObserver($0) }
            playerLooper?.disableLooping()
            player?.pause()
            player = nil
            playerLayer = nil
        }
    }
    
    func makeCoordinator() -> Coordinator {
        Coordinator()
    }
    
    private func shouldUseFallback() -> Bool {
        let device = ProcessInfo.processInfo
        return device.processorCount < 2 || device.physicalMemory < 1_000_000_000 || !ProcessInfo.processInfo.isOperatingSystemAtLeast(OperatingSystemVersion(majorVersion: 15, minorVersion: 0, patchVersion: 0))
    }
    
    func makeUIView(context: Context) -> UIView {
        if shouldUseFallback() {
            return GradientView()
        }
        
        let view = UIView(frame: UIScreen.main.bounds)
        
        let path: String?
        if let directory = inDirectory {
            path = Bundle.main.path(forResource: videoName, ofType: "mp4", inDirectory: directory)
        } else {
            path = Bundle.main.path(forResource: videoName, ofType: "mp4")
        }
        
        guard let videoPath = path else {
            return GradientView()
        }
        
        let asset = AVAsset(url: URL(fileURLWithPath: videoPath))
        let playerItem = AVPlayerItem(asset: asset)

        // Buffer ahead so AVPlayerLooper handoff between queued copies does not stall (~0.5s hitch).
        playerItem.preferredForwardBufferDuration = 8.0
        playerItem.canUseNetworkResourcesForLiveStreamingWhilePaused = false

        let player = AVQueuePlayer(playerItem: playerItem)
        player.automaticallyWaitsToMinimizeStalling = false
        player.isMuted = isMuted
        context.coordinator.player = player

        let playerLooper = AVPlayerLooper(player: player, templateItem: playerItem)
        context.coordinator.playerLooper = playerLooper

        let playerLayer = AVPlayerLayer(player: player)
        playerLayer.frame = view.bounds
        playerLayer.videoGravity = .resizeAspectFill
        let scale: CGFloat = 0.85
        playerLayer.transform = CATransform3DMakeScale(scale, scale, 1.0)
        context.coordinator.playerLayer = playerLayer
        view.layer.addSublayer(playerLayer)
        
        view.backgroundColor = .clear
        view.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        
        player.play()
        player.rate = 1.0
        
        return view
    }
    
    func updateUIView(_ uiView: UIView, context: Context) {
        uiView.frame = UIScreen.main.bounds
        guard let playerLayer = context.coordinator.playerLayer else { return }
        // SwiftUI can drive frequent layout updates; implicit layer animations read as a loop glitch.
        CATransaction.begin()
        CATransaction.setDisableActions(true)
        playerLayer.frame = uiView.bounds
        CATransaction.commit()
    }
    
    static func dismantleUIView(_ uiView: UIView, coordinator: Coordinator) {
        coordinator.observers.forEach { NotificationCenter.default.removeObserver($0) }
        coordinator.playerLooper?.disableLooping()
        coordinator.player?.pause()
        coordinator.player = nil
    }
}

class GradientView: UIView {
    override init(frame: CGRect = .zero) {
        super.init(frame: frame)
        setupBackground()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setupBackground()
    }
    
    private func setupBackground() {
        if let image = UIImage(named: "starbg") {
            let imageView = UIImageView(image: image)
            imageView.frame = bounds
            imageView.contentMode = .scaleAspectFill
            imageView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
            imageView.alpha = 0.95 // Match video opacity
            addSubview(imageView)
        } else {
            // Fallback to gradient if image is not found
            let gradientLayer = CAGradientLayer()
            gradientLayer.frame = bounds
            gradientLayer.colors = [
                UIColor(red: 0.0, green: 0.35, blue: 0.7, alpha: 1.0).cgColor,
                UIColor.white.cgColor,
                UIColor(red: 0.85, green: 0.7, blue: 0.0, alpha: 1.0).cgColor
            ]
            gradientLayer.startPoint = CGPoint(x: 0.0, y: 0.0)
            gradientLayer.endPoint = CGPoint(x: 1.0, y: 1.0)
            gradientLayer.type = .axial
            gradientLayer.locations = [0.0, 0.5, 1.0]
            layer.addSublayer(gradientLayer)
        }
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        for subview in subviews {
            subview.frame = bounds
        }
        if let gradientLayer = layer.sublayers?.first as? CAGradientLayer {
            gradientLayer.frame = bounds
        }
    }
} 