import SwiftUI
import AVFoundation
import MediaPlayer

class AudioPlayerManager: NSObject, ObservableObject {
    static let shared = AudioPlayerManager()
    
    @Published var isPlaying = false
    @Published var currentTime: TimeInterval = 0
    @Published var duration: TimeInterval = 0
    @Published var isRepeating = false
    
    private var audioPlayer: AVAudioPlayer?
    private var timer: Timer?
    
    private override init() {
        super.init()
        setupAudioSession()
        setupRemoteCommandCenter()
    }
    
    private func setupAudioSession() {
        do {
            try AVAudioSession.sharedInstance().setCategory(.playback, mode: .default, options: [.allowAirPlay, .allowBluetoothHFP])
            try AVAudioSession.sharedInstance().setActive(true)
            print("✅ Audio session setup successful")
        } catch {
            print("❌ Failed to setup audio session: \(error)")
        }
    }
    
    private func setupRemoteCommandCenter() {
        let commandCenter = MPRemoteCommandCenter.shared()
        
        commandCenter.playCommand.addTarget { [weak self] _ in
            self?.play()
            return .success
        }
        
        commandCenter.pauseCommand.addTarget { [weak self] _ in
            self?.pause()
            return .success
        }
        
        commandCenter.stopCommand.addTarget { [weak self] _ in
            self?.stop()
            return .success
        }
        
        commandCenter.togglePlayPauseCommand.addTarget { [weak self] _ in
            if self?.isPlaying == true {
                self?.pause()
            } else {
                self?.play()
            }
            return .success
        }
    }
    
    func loadSong() {
        print("🎵 MediaPlayerView: loadSong() called")
        guard let url = Bundle.main.url(forResource: "song", withExtension: "mp3") else {
            print("❌ Could not find song.mp3 in bundle")
            return
        }
        
        print("✅ Found song.mp3 at: \(url)")
        do {
            audioPlayer = try AVAudioPlayer(contentsOf: url)
            audioPlayer?.delegate = self
            audioPlayer?.prepareToPlay()
            duration = audioPlayer?.duration ?? 0
            print("✅ Audio player created successfully, duration: \(duration)")
        } catch {
            print("❌ Error loading song: \(error)")
        }
    }
    
    func play() {
        print("🎵 MediaPlayerView: play() called")
        guard let player = audioPlayer else { 
            print("❌ No audio player available")
            return 
        }
        
        // Ensure audio session is active
        do {
            try AVAudioSession.sharedInstance().setActive(true)
        } catch {
            print("❌ Failed to activate audio session: \(error)")
        }
        
        if !player.isPlaying {
            print("▶️ Starting playback")
            player.play()
            isPlaying = true
            startTimer()
            updateNowPlayingInfo()
        } else {
            print("ℹ️ Already playing")
        }
    }
    
    func pause() {
        audioPlayer?.pause()
        isPlaying = false
        stopTimer()
        updateNowPlayingInfo()
    }
    
    func stop() {
        audioPlayer?.stop()
        audioPlayer?.currentTime = 0
        isPlaying = false
        currentTime = 0
        stopTimer()
        updateNowPlayingInfo()
    }
    
    func restart() {
        audioPlayer?.currentTime = 0
        currentTime = 0
        play()
    }
    
    func toggleRepeat() {
        isRepeating.toggle()
    }
    
    func seek(to time: TimeInterval) {
        audioPlayer?.currentTime = time
        currentTime = time
    }
    
    private func startTimer() {
        timer = Timer.scheduledTimer(withTimeInterval: 0.1, repeats: true) { [weak self] _ in
            guard let self = self, let player = self.audioPlayer else { return }
            self.currentTime = player.currentTime
        }
    }
    
    private func stopTimer() {
        timer?.invalidate()
        timer = nil
    }
    
    func updateNowPlayingInfo() {
        var nowPlayingInfo = [String: Any]()
        nowPlayingInfo[MPMediaItemPropertyTitle] = "Official App Song"
        nowPlayingInfo[MPMediaItemPropertyArtist] = "Mitz Mode"
        nowPlayingInfo[MPNowPlayingInfoPropertyElapsedPlaybackTime] = currentTime
        nowPlayingInfo[MPMediaItemPropertyPlaybackDuration] = duration
        nowPlayingInfo[MPNowPlayingInfoPropertyPlaybackRate] = isPlaying ? 1.0 : 0.0
        
        MPNowPlayingInfoCenter.default().nowPlayingInfo = nowPlayingInfo
        print("🎵 Updated now playing info - isPlaying: \(isPlaying), currentTime: \(currentTime)")
    }
    
}

extension AudioPlayerManager: AVAudioPlayerDelegate {
    func audioPlayerDidFinishPlaying(_ player: AVAudioPlayer, successfully flag: Bool) {
        if isRepeating {
            restart()
        } else {
            isPlaying = false
            currentTime = 0
            stopTimer()
            updateNowPlayingInfo()
        }
    }
}

struct MediaPlayerView: View {
    @ObservedObject private var audioManager = AudioPlayerManager.shared
    @Binding var isPresented: Bool

    var body: some View {
        ZStack {
            AppColor.scrim
                .ignoresSafeArea()
                .onTapGesture { isPresented = false }

            VStack(spacing: 0) {
                MenuSheetHeaderBar(title: "🎵 Official App Song", onClose: { isPresented = false })
                MenuSheetGoldHairline()

                VStack(spacing: 18) {
                    RoundedRectangle(cornerRadius: 16, style: .continuous)
                        .fill(
                            LinearGradient(
                                colors: [AppColor.parchmentTop, AppColor.parchmentMid, AppColor.parchmentBase],
                                startPoint: .topLeading,
                                endPoint: .bottomTrailing
                            )
                        )
                        .overlay(
                            RoundedRectangle(cornerRadius: 16, style: .continuous)
                                .stroke(AppColor.goldBorder.opacity(0.45), lineWidth: 1.1)
                        )
                        .frame(width: 200, height: 200)
                        .overlay(
                            Image(systemName: "music.note")
                                .font(.system(size: 56))
                                .foregroundColor(AppColor.goldBorder.opacity(0.85))
                        )
                        .shadow(color: .black.opacity(0.12), radius: 8, y: 4)

                    VStack(spacing: 8) {
                        Slider(
                            value: Binding(
                                get: { audioManager.currentTime },
                                set: { audioManager.seek(to: $0) }
                            ),
                            in: 0...max(audioManager.duration, 0.01)
                        )
                        .accentColor(AppColor.navyPrimary)

                        HStack {
                            Text(formatTime(audioManager.currentTime))
                                .font(.system(size: 13, weight: .medium, design: .serif))
                                .foregroundColor(AppColor.textMuted)
                            Spacer()
                            Text(formatTime(audioManager.duration))
                                .font(.system(size: 13, weight: .medium, design: .serif))
                                .foregroundColor(AppColor.textMuted)
                        }
                    }
                    .padding(.horizontal, 4)

                    HStack(spacing: 28) {
                        mediaControlButton(systemName: "backward.fill") {
                            audioManager.restart()
                        }

                        Button(action: {
                            if audioManager.isPlaying {
                                audioManager.pause()
                            } else {
                                audioManager.play()
                            }
                        }) {
                            Image(systemName: audioManager.isPlaying ? "pause.circle.fill" : "play.circle.fill")
                                .font(.system(size: 52))
                                .foregroundColor(AppColor.textOnDark)
                                .background(
                                    Circle()
                                        .fill(
                                            LinearGradient(
                                                colors: [AppColor.navyPrimary, AppColor.navyDeep],
                                                startPoint: .topLeading,
                                                endPoint: .bottomTrailing
                                            )
                                        )
                                        .frame(width: 72, height: 72)
                                        .overlay(Circle().stroke(AppColor.goldBorder.opacity(0.75), lineWidth: 1.2))
                                )
                        }
                        .buttonStyle(.plain)

                        mediaControlButton(systemName: "stop.fill") {
                            audioManager.stop()
                        }
                    }

                    Button(action: { audioManager.toggleRepeat() }) {
                        HStack(spacing: 8) {
                            Image(systemName: "repeat")
                                .font(.system(size: 16, weight: .semibold))
                            TranslatableText(
                                source: "Repeat",
                                font: .system(size: 16, weight: .semibold, design: .serif),
                                weight: .semibold,
                                color: audioManager.isRepeating ? AppColor.navyPrimary : AppColor.textPrimary,
                                alignment: .center
                            )
                        }
                        .foregroundColor(audioManager.isRepeating ? AppColor.navyPrimary : AppColor.textPrimary)
                        .padding(.horizontal, 20)
                        .padding(.vertical, 10)
                        .background(
                            RoundedRectangle(cornerRadius: 12, style: .continuous)
                                .fill(
                                    audioManager.isRepeating
                                        ? AppColor.goldBright.opacity(0.35)
                                        : AppColor.goldBorder.opacity(0.08)
                                )
                        )
                        .overlay(
                            RoundedRectangle(cornerRadius: 12, style: .continuous)
                                .stroke(AppColor.goldBorder.opacity(audioManager.isRepeating ? 0.65 : 0.35), lineWidth: 0.9)
                        )
                    }
                    .buttonStyle(.plain)

                    VStack(spacing: 6) {
                        TranslatableText(
                            source: "Enjoy the official Mitz Mode song!",
                            font: .system(size: 17, weight: .semibold, design: .serif),
                            weight: .semibold,
                            color: AppColor.textPrimary,
                            alignment: .center
                        )
                        Text(verbatim: "Performed by G.E.U.L.A © \(Calendar.current.component(.year, from: Date()))")
                            .font(.system(size: 13, weight: .medium, design: .serif))
                            .foregroundColor(AppColor.textMuted)
                            .multilineTextAlignment(.center)
                    }
                    .padding(.top, 4)

                    Spacer(minLength: 0)
                }
                .padding(18)
                .background(
                    LinearGradient(
                        colors: [AppColor.parchmentTop, AppColor.parchmentMid, AppColor.parchmentBase],
                        startPoint: .top,
                        endPoint: .bottom
                    )
                )
            }
            .clipShape(RoundedRectangle(cornerRadius: 20, style: .continuous))
            .overlay(
                RoundedRectangle(cornerRadius: 20, style: .continuous)
                    .stroke(AppColor.goldBorder.opacity(0.55), lineWidth: 1.2)
            )
            .shadow(color: .black.opacity(0.25), radius: 20, y: 10)
            .frame(maxWidth: min(360, UIScreen.main.bounds.width - 32))
            .frame(minHeight: 440, maxHeight: 520)
        }
        .onAppear {
            print("🎵 MediaPlayerView: onAppear called")
            // Only load if not already loaded
            if audioManager.duration == 0 {
                audioManager.loadSong()
            }
        }
        .onReceive(NotificationCenter.default.publisher(for: UIApplication.didEnterBackgroundNotification)) { _ in
            print("🎵 App entered background - ensuring audio continues")
            // Ensure audio session stays active in background
            do {
                try AVAudioSession.sharedInstance().setActive(true)
            } catch {
                print("❌ Failed to keep audio session active in background: \(error)")
            }
        }
        .onReceive(NotificationCenter.default.publisher(for: UIApplication.didBecomeActiveNotification)) { _ in
            print("🎵 App became active - updating now playing info")
            audioManager.updateNowPlayingInfo()
        }
    }

    private func mediaControlButton(systemName: String, action: @escaping () -> Void) -> some View {
        Button(action: action) {
            Image(systemName: systemName)
                .font(.title2)
                .foregroundColor(AppColor.textOnDark)
                .frame(width: 48, height: 48)
                .background(
                    Circle()
                        .fill(
                            LinearGradient(
                                colors: [AppColor.navyPrimary, AppColor.navyDeep],
                                startPoint: .topLeading,
                                endPoint: .bottomTrailing
                            )
                        )
                )
                .overlay(Circle().stroke(AppColor.goldBorder.opacity(0.65), lineWidth: 1))
        }
        .buttonStyle(.plain)
    }

    private func formatTime(_ time: TimeInterval) -> String {
        let minutes = Int(time) / 60
        let seconds = Int(time) % 60
        return String(format: "%d:%02d", minutes, seconds)
    }
}

// MARK: - Mini Player Bar

struct MiniPlayerBar: View {
    @ObservedObject private var audioManager = AudioPlayerManager.shared
    @Binding var showFullPlayer: Bool
    
    var body: some View {
        if audioManager.isPlaying || audioManager.currentTime > 0 {
            HStack(spacing: 12) {
                RoundedRectangle(cornerRadius: 10, style: .continuous)
                    .fill(
                        LinearGradient(
                            colors: [AppColor.parchmentTop, AppColor.parchmentBase],
                            startPoint: .topLeading,
                            endPoint: .bottomTrailing
                        )
                    )
                    .overlay(
                        RoundedRectangle(cornerRadius: 10, style: .continuous)
                            .stroke(AppColor.goldBorder.opacity(0.45), lineWidth: 0.8)
                    )
                    .frame(width: 50, height: 50)
                    .overlay(
                        Image(systemName: "music.note")
                            .font(.system(size: 20))
                            .foregroundColor(AppColor.goldBorder.opacity(0.9))
                    )

                VStack(alignment: .leading, spacing: 2) {
                    TranslatableText(
                        source: "🎵 Official App Song",
                        font: .system(size: 14, weight: .semibold, design: .serif),
                        weight: .semibold,
                        color: AppColor.goldBright,
                        alignment: .leading
                    )
                    .lineLimit(1)
                    TranslatableText(
                        source: "Mitz Mode",
                        font: .system(size: 12, weight: .medium, design: .serif),
                        color: AppColor.textOnDark.opacity(0.82),
                        alignment: .leading
                    )
                }

                Spacer()

                Button(action: {
                    if audioManager.isPlaying {
                        audioManager.pause()
                    } else {
                        audioManager.play()
                    }
                }) {
                    Image(systemName: audioManager.isPlaying ? "pause.fill" : "play.fill")
                        .font(.system(size: 18))
                        .foregroundColor(AppColor.textOnDark)
                        .frame(width: 44, height: 44)
                }
                .buttonStyle(.plain)

                Button(action: { audioManager.stop() }) {
                    Image(systemName: "xmark")
                        .font(.system(size: 15, weight: .semibold))
                        .foregroundColor(AppColor.textOnDark.opacity(0.9))
                        .frame(width: 44, height: 44)
                }
                .buttonStyle(.plain)
            }
            .padding(.horizontal, 12)
            .padding(.vertical, 8)
            .background(
                Capsule()
                    .fill(
                        LinearGradient(
                            colors: [AppColor.navyDeep, AppColor.navyMid],
                            startPoint: .leading,
                            endPoint: .trailing
                        )
                    )
                    .overlay(Capsule().stroke(AppColor.goldBorder.opacity(0.5), lineWidth: 1))
            )
            .shadow(color: .black.opacity(0.28), radius: 10, y: -2)
            .padding(.horizontal, 12)
            .padding(.bottom, 8)
            .onTapGesture {
                showFullPlayer = true
            }
            .transition(.move(edge: .bottom).combined(with: .opacity))
        }
    }
}

#Preview {
    MediaPlayerView(isPresented: .constant(true))
}
