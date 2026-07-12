import SwiftUI
import AVKit
import CoreHaptics
import Sentry

struct LegacyMitzModeHomeView: View {
    @StateObject private var viewModel = DailyMitzvotViewModel()
    @State private var engine: CHHapticEngine?
    @State private var showingMitzvahDefinition = false
    @State private var showingMenu = false
    @State private var showingAbout = false
    @State private var showingChecklist = false
    @State private var showingSubmission = false
    @State private var showingBirkatHamazon = false
    @State private var showingTefilatHaderech = false
    @State private var showingBrachot = false
    @State private var showingMediaPlayer = false
    @State private var showingMitzvotLevel = false

    @State private var showingTour = false
    @State private var tourStep = 0

    private func setTourVisible(_ visible: Bool) {
        var transaction = Transaction()
        transaction.disablesAnimations = true
        withTransaction(transaction) {
            showingTour = visible
        }
    }

    var body: some View {
        ZStack {
            MitzModeStaticBackground()

            VStack {
                HStack(alignment: .center) {
                    AndroidParityMenuChip {
                        guard !showingTour else { return }
                        showingMenu.toggle()
                    }
                    .padding(.top, AppChromeLayout.iosHomeChromeTopPadding)
                    .padding(.leading, AppChromeLayout.menuLeading)
                    .tourSpot(.menuButton)

                    Spacer(minLength: 0)

                    Button(action: {
                        guard !showingTour else { return }
                        showingMitzvotLevel = true
                    }) {
                        AndroidParityMitzvahCountPill(count: viewModel.acceptedMitzvotCount)
                    }
                    .buttonStyle(.plain)
                    .padding(.top, AppChromeLayout.iosHomeChromeTopPadding)
                    .padding(.trailing, AppChromeLayout.counterTrailing)
                    .tourSpot(.levelBadge)
                    .disabled(showingTour)
                }
                Spacer()
            }
            .frame(maxHeight: .infinity, alignment: .top)

            TranslatableText(
                source: "Tap the \"Mitzvah Me\"\nbutton for a mitzvah!",
                font: .system(size: 17, weight: .semibold, design: .rounded),
                weight: .semibold,
                color: AppColor.mitzvahHeirloomLabel,
                alignment: .center
            )
            .androidLetterSpacing(0.28)
            .lineSpacing(3)
                .shadow(color: .black.opacity(0.55), radius: 6, x: 0, y: 2)
                .shadow(color: AppColor.androidGoldSpot.opacity(0.4), radius: 12, x: 0, y: 0)
                .padding(.horizontal, 24)
                .padding(.vertical, 12)
                .offset(y: -UIScreen.main.bounds.height * 0.3425)

            AndroidParityMitzvahButton {
                viewModel.buttonPressed()
                playHaptic()
            }
            .padding(.bottom, 72)
            .tourSpot(.mitzvahButton)

            ZStack(alignment: .bottom) {
                AndroidParityDailyChecklistButton {
                    showingChecklist = true
                }
                .padding(.bottom, AppChromeLayout.dailyChecklistBottomInset)
                .frame(maxWidth: .infinity)

                AndroidParityWhatsAMitzvahButton {
                    showingMitzvahDefinition.toggle()
                }
                .padding(.bottom, AppChromeLayout.whatsMitzvahBottomInset)
                .frame(maxWidth: .infinity)

                AndroidParityAddMitzvahButton {
                    showingSubmission = true
                }
                .padding(.bottom, AppChromeLayout.addMitzvahBottomInset)
                .tourSpot(.addMitzvahButton)
            }
            .frame(maxHeight: .infinity, alignment: .bottom)

            if viewModel.showingMitzvah {
                MitzvahCardView(
                    mitzvah: viewModel.currentMitzvah ?? Mitzvah(id: "", text: "", links: []),
                    onAccept: viewModel.acceptMitzvah,
                    onNext: viewModel.nextMitzvah,
                    onDismiss: { withAnimation { viewModel.showingMitzvah = false } },
                    textSize: Binding(
                        get: { Double(viewModel.textSize) },
                        set: { viewModel.textSize = CGFloat($0) }
                    ),
                    hasAccepted: Binding(
                        get: { viewModel.hasAccepted },
                        set: { viewModel.hasAccepted = $0 }
                    ),
                    viewModel: viewModel
                )
                .transition(.opacity.combined(with: .scale(scale: 0.96)))
            }

            if showingMitzvahDefinition {
                MitzvahDefinitionView(isPresented: $showingMitzvahDefinition)
            }

            if showingMenu {
                MenuOverlay(
                    isPresented: $showingMenu,
                    showingAbout: $showingAbout,
                    showingBirkatHamazon: $showingBirkatHamazon,
                    showingTefilatHaderech: $showingTefilatHaderech,
                    showingBrachot: $showingBrachot,
                    showingMediaPlayer: $showingMediaPlayer
                )
            }

            if showingMediaPlayer {
                MediaPlayerView(isPresented: $showingMediaPlayer)
            }

            if viewModel.showingRewardVideo {
                RewardVideoPlayer(
                    videoNumber: viewModel.currentRewardVideo,
                    isPresented: $viewModel.showingRewardVideo
                )
            }

            if viewModel.showingLevelAchieved {
                LevelAchievedView(level: viewModel.achievedLevel)
                .position(
                    x: UIScreen.main.bounds.width / 2,
                    y: UIScreen.main.bounds.height * 0.7
                )
                .transition(.scale.combined(with: .opacity))
            }

            if showingMitzvotLevel {
                MitzvotLevelDialog(
                    isPresented: $showingMitzvotLevel,
                    mitzvotCount: viewModel.acceptedMitzvotCount,
                    onRequestFinalRewardVideo: {
                        showingMitzvotLevel = false
                        viewModel.requestFinalRewardReplay()
                    }
                )
            }

            if !showingMediaPlayer {
                VStack {
                    Spacer()
                    MiniPlayerBar(showFullPlayer: $showingMediaPlayer)
                        .animation(.spring(), value: AudioPlayerManager.shared.isPlaying)
                }
                .allowsHitTesting(true)
            }
        }
        .coordinateSpace(name: TourCoordinateSpace.name)
        .overlayPreferenceValue(TourSpotAnchorKey.self) { anchors in
            GeometryReader { geo in
                if showingTour {
                    FirstTimeTourOverlay(
                        currentStep: $tourStep,
                        spotFrames: anchors.mapValues { geo[$0] },
                        onComplete: { setTourVisible(false) }
                    )
                }
            }
        }
        .animation(nil, value: showingTour)
        .onAppear {
            prepareHaptics()
            if !TourState.isCompleted {
                var transaction = Transaction()
                transaction.disablesAnimations = true
                withTransaction(transaction) {
                    showingMenu = false
                    showingTour = true
                    tourStep = 0
                }
            }
        }
        .sheet(isPresented: $showingAbout) {
            AboutView()
        }
        .fullScreenCover(isPresented: $showingChecklist) {
            DailyChecklistHostView { showingChecklist = false }
            .ignoresSafeArea()
        }
        .sheet(isPresented: $showingBirkatHamazon) {
            BirkatHamazonView()
        }
        .sheet(isPresented: $showingTefilatHaderech) {
            TefilatHaderechView()
        }
        .sheet(isPresented: $showingBrachot) {
            BrachotView()
        }
        .sheet(isPresented: $showingSubmission) {
            MitzvahSubmissionView(isPresented: $showingSubmission)
        }
        .appTranslationLayoutDirection()
    }

    private func prepareHaptics() {
        guard CHHapticEngine.capabilitiesForHardware().supportsHaptics else { return }
        do {
            engine = try CHHapticEngine()
            try engine?.start()
        } catch {
            print("Haptics error: \(error.localizedDescription)")
        }
    }

    private func playHaptic() {
        guard CHHapticEngine.capabilitiesForHardware().supportsHaptics else { return }

        let intensity = CHHapticEventParameter(parameterID: .hapticIntensity, value: 1.0)
        let sharpness = CHHapticEventParameter(parameterID: .hapticSharpness, value: 1.0)
        let event = CHHapticEvent(eventType: .hapticTransient, parameters: [intensity, sharpness], relativeTime: 0)

        do {
            let pattern = try CHHapticPattern(events: [event], parameters: [])
            let player = try engine?.makePlayer(with: pattern)
            try player?.start(atTime: 0)
        } catch {
            print("Failed to play haptic: \(error.localizedDescription)")
        }
    }
}

#if DEBUG
struct LegacyMitzModeHomeView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
#endif

