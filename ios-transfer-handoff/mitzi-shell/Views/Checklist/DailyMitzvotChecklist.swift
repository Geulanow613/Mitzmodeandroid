import SwiftUI
import AVKit

struct DailyMitzvotChecklist: View {
    @ObservedObject var viewModel: DailyMitzvotViewModel
    @StateObject private var uiState = ChecklistUIState()
    @Binding var isPresented: Bool
    @State private var showingTzaddikVideo = false
    @State private var player: AVPlayer?
    @State private var showHelp = false

    private let clearAllRed = Color(red: 0.753, green: 0.224, blue: 0.169)

    var body: some View {
        ZStack {
            AppColor.scrim
                .ignoresSafeArea()
                .onTapGesture { isPresented = false }

            VStack(spacing: 0) {
                checklistHeader
                goldHairlineDivider
                ScrollView {
                    VStack(spacing: 16) {
                        introBox
                        genderRow
                        clearAllRow
                        checklistContent
                    }
                    .padding(.horizontal, 16)
                    .padding(.vertical, 12)
                }
            }
            .background(
                LinearGradient(
                    colors: [AppColor.parchmentTop, AppColor.parchmentMid, AppColor.parchmentBase],
                    startPoint: .top,
                    endPoint: .bottom
                )
            )
            .clipShape(RoundedRectangle(cornerRadius: 20, style: .continuous))
            .overlay(
                RoundedRectangle(cornerRadius: 20, style: .continuous)
                    .stroke(AppColor.goldBorder.opacity(0.55), lineWidth: 1.4)
            )
            .shadow(color: .black.opacity(0.22), radius: 18, y: 8)
            .padding(.horizontal, 10)
            .padding(.vertical, 12)
            .frame(maxWidth: UIScreen.main.bounds.width * 0.96)
            .frame(maxHeight: UIScreen.main.bounds.height * 0.92)

            if viewModel.showingBlessedPopup {
                VStack {
                    Spacer()
                    Text("BLESSED!!!")
                        .font(.system(size: 30, weight: .bold, design: .serif))
                        .foregroundColor(AppColor.goldBright)
                        .padding()
                        .background(
                            LinearGradient(
                                colors: [AppColor.navyPrimary, AppColor.navyDeep],
                                startPoint: .topLeading,
                                endPoint: .bottomTrailing
                            )
                        )
                        .cornerRadius(16)
                        .overlay(RoundedRectangle(cornerRadius: 16).stroke(AppColor.goldBorder.opacity(0.6), lineWidth: 1))
                        .shadow(color: .black.opacity(0.35), radius: 12, y: 6)
                        .transition(.scale.combined(with: .opacity))
                }
                .padding(.bottom, 50)
            }
        }
        .environment(\.checklistTextSize, uiState.textSize)
        .sheet(isPresented: $showHelp) {
            BeginnerInfoDialog(isPresented: $showHelp)
        }
        .fullScreenCover(isPresented: $viewModel.showingTzaddikVideo) {
            ZStack {
                if let player = player {
                    VideoPlayer(player: player)
                        .edgesIgnoringSafeArea(.all)
                }

                if viewModel.showingBlessedPopup {
                    VStack {
                        Spacer()
                        Text("BLESSED!!!")
                            .font(.system(size: 44, weight: .bold, design: .serif))
                            .foregroundColor(AppColor.goldBright)
                            .shadow(color: .black, radius: 3, x: 1, y: 2)
                            .padding()
                            .background(AppColor.navyDeep.opacity(0.72))
                            .cornerRadius(18)
                            .transition(.scale.combined(with: .opacity))
                            .padding(.bottom, 50)
                    }
                }
            }
            .onAppear {
                if let url = Bundle.main.url(forResource: "tzaddik", withExtension: "mp4") {
                    player = AVPlayer(url: url)
                    player?.play()

                    NotificationCenter.default.addObserver(
                        forName: .AVPlayerItemDidPlayToEndTime,
                        object: player?.currentItem,
                        queue: .main
                    ) { _ in
                        viewModel.showingTzaddikVideo = false
                        viewModel.showingBlessedPopup = false
                        player = nil
                    }
                }
            }
            .onDisappear {
                player?.pause()
                player = nil
            }
        }
    }

    private var checklistHeader: some View {
        HStack(spacing: 8) {
            HStack(spacing: 2) {
                Button(action: { uiState.textSize = max(12, uiState.textSize - 1) }) {
                    Text("a")
                        .font(.system(size: 13, weight: .medium, design: .serif))
                        .foregroundColor(AppColor.goldBright.opacity(0.85))
                        .frame(width: 28, height: 28)
                }
                .buttonStyle(.plain)
                Button(action: { uiState.textSize = min(20, uiState.textSize + 1) }) {
                    Text("A")
                        .font(.system(size: 15, weight: .semibold, design: .serif))
                        .foregroundColor(AppColor.goldBright.opacity(0.85))
                        .frame(width: 28, height: 28)
                }
                .buttonStyle(.plain)
            }

            Spacer(minLength: 4)

            Text("Daily Mitzvot Guide")
                .font(.system(size: uiState.textSize + 3, weight: .semibold, design: .serif))
                .foregroundColor(AppColor.goldBright)
                .lineLimit(1)
                .minimumScaleFactor(0.65)

            Spacer(minLength: 4)

            Button(action: { showHelp = true }) {
                Image(systemName: "questionmark.circle.fill")
                    .font(.system(size: 20))
                    .foregroundColor(AppColor.goldBright.opacity(0.88))
            }
            .buttonStyle(.plain)
            .accessibilityLabel("Help")

            Button(action: { isPresented = false }) {
                Image(systemName: "xmark.circle.fill")
                    .font(.system(size: 20))
                    .foregroundColor(AppColor.goldBright.opacity(0.88))
            }
            .buttonStyle(.plain)
            .accessibilityLabel("Close checklist")
        }
        .padding(.horizontal, 12)
        .padding(.vertical, 10)
        .background(
            LinearGradient(
                colors: [AppColor.navyDeep, AppColor.navyMid],
                startPoint: .top,
                endPoint: .bottom
            )
        )
    }

    private var goldHairlineDivider: some View {
        Rectangle()
            .fill(
                LinearGradient(
                    colors: [.clear, AppColor.goldBorder.opacity(0.6), .clear],
                    startPoint: .leading,
                    endPoint: .trailing
                )
            )
            .frame(height: 1)
    }

    private var introBox: some View {
        Text("This guide is for beginners to understand the basic requirements of Torah-observant Jewish life. Take it one step at a time and always consult with a rabbi for proper guidance.")
            .font(.system(size: uiState.textSize, weight: .regular, design: .serif))
            .foregroundColor(AppColor.textMuted)
            .padding(.horizontal, 14)
            .padding(.vertical, 10)
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(
                RoundedRectangle(cornerRadius: 12, style: .continuous)
                    .fill(AppColor.goldBorder.opacity(0.07))
            )
            .overlay(
                RoundedRectangle(cornerRadius: 12, style: .continuous)
                    .stroke(AppColor.goldBorder.opacity(0.28), lineWidth: 0.8)
            )
    }

    private var genderRow: some View {
        HStack(spacing: 8) {
            genderCapsule(title: "Male", isSelected: uiState.selectedGender == .male) {
                uiState.selectedGender = .male
            }
            genderCapsule(title: "Female", isSelected: uiState.selectedGender == .female) {
                uiState.selectedGender = .female
            }
        }
    }

    private func genderCapsule(title: String, isSelected: Bool, action: @escaping () -> Void) -> some View {
        Button(action: action) {
            Text(title)
                .font(.system(size: 15, weight: .semibold, design: .serif))
                .foregroundColor(isSelected ? Color(red: 1.0, green: 0.965, blue: 0.847) : AppColor.textMuted)
                .frame(maxWidth: .infinity)
                .padding(.vertical, 11)
                .background(
                    Group {
                        if isSelected {
                            LinearGradient(
                                colors: [
                                    Color(red: 43 / 255, green: 91 / 255, blue: 168 / 255),
                                    AppColor.navyDeep
                                ],
                                startPoint: .top,
                                endPoint: .bottom
                            )
                        } else {
                            LinearGradient(
                                colors: [AppColor.parchmentMid, AppColor.parchmentBase],
                                startPoint: .top,
                                endPoint: .bottom
                            )
                        }
                    }
                )
                .clipShape(Capsule())
                .overlay(
                    Capsule()
                        .stroke(AppColor.goldBorder.opacity(isSelected ? 0.85 : 0.35), lineWidth: 1)
                )
        }
        .buttonStyle(.plain)
    }

    private var clearAllRow: some View {
        HStack {
            Spacer()
            Button(action: { viewModel.clearChecklist() }) {
                Text("Clear All")
                    .font(.system(size: 14, weight: .medium, design: .serif))
                    .foregroundColor(clearAllRed.opacity(0.88))
                    .padding(.horizontal, 14)
                    .padding(.vertical, 6)
                    .background(
                        Capsule()
                            .stroke(clearAllRed.opacity(0.55), lineWidth: 0.8)
                    )
            }
            .buttonStyle(.plain)
        }
        .padding(.bottom, 4)
    }

    private var checklistContent: some View {
        Group {
            if uiState.selectedGender == Gender.male {
                MaleChecklistSection(viewModel: viewModel)
            } else {
                FemaleChecklistSection(viewModel: viewModel)
            }
        }
    }
}
