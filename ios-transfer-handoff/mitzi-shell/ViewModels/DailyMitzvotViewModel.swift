import Foundation
import SwiftUI
import Combine

class DailyMitzvotViewModel: ObservableObject {
    @Published var selectedGender: Gender = .male
    @Published var showingMitzvah = false
    @Published var currentMitzvah: Mitzvah?
    @Published var acceptedMitzvotCount: Int {
        didSet {
            UserDefaults.standard.set(acceptedMitzvotCount, forKey: "AcceptedMitzvotCount")
        }
    }
    @Published var textSize: CGFloat = 14
    @Published var showingRewardVideo = false
    @Published var currentRewardVideo = 1
    @Published var showAcceptAnimation = false
    @Published var hasAccepted = false
    @Published var checklistStates: [String: Bool] = [:] {
        didSet {
            saveChecklistStates()
        }
    }
    @Published var showingTzaddikVideo = false
    @Published var showingBlessedPopup = false
    /// Rainbow parchment toast on level-up (not the certificate dialog).
    @Published var showingLevelAchieved = false
    @Published var achievedLevel: String = ""

    private var lastRewardDate: Date? {
        get { UserDefaults.standard.object(forKey: "LastRewardDate") as? Date }
        set { UserDefaults.standard.set(newValue, forKey: "LastRewardDate") }
    }

    private var localMitzvot: [Mitzvah] = []
    @Published private(set) var cloudMitzvot: [Mitzvah] = []
    /// Full pool in random order; advance `deckIndex` until empty, then reshuffle.
    private var shuffledDeck: [Mitzvah] = []
    private var deckIndex: Int = 0
    /// `Set` of ids the current `shuffledDeck` was built from; rebuild if the merged pool changes.
    private var deckPoolIdSet: Set<String> = []
    private var cancellables = Set<AnyCancellable>()

    private var allMitzvot: [Mitzvah] {
        (localMitzvot + cloudMitzvot).mergedDistinctFirstWins()
    }

    init() {
        self.acceptedMitzvotCount = UserDefaults.standard.integer(forKey: "AcceptedMitzvotCount")
        loadLocalMitzvot()
        cloudMitzvot = readCachedCloudMitzvotSync()
        loadChecklistStates()

        NotificationCenter.default.publisher(for: .mitzvotCloudDidUpdate)
            .receive(on: DispatchQueue.main)
            .sink { [weak self] _ in
                self?.cloudMitzvot = readCachedCloudMitzvotSync()
            }
            .store(in: &cancellables)

        Task(priority: .userInitiated) {
            let fresh = await MitzvotCloudRepository.shared.runBackgroundSyncIfOnline()
            await MainActor.run {
                if let fresh {
                    self.cloudMitzvot = fresh
                }
            }
        }
    }

    private func loadLocalMitzvot() {
        if let url = Bundle.main.url(forResource: "mitzvotlistfull", withExtension: "json") {
            do {
                let data = try Data(contentsOf: url)
                let mitzvotData = try JSONDecoder().decode(MitzvotData.self, from: data)
                self.localMitzvot = mitzvotData.mitzvot
            } catch {
                print("Error loading local mitzvot: \(error)")
            }
        }
    }

    func clearChecklist() {
        checklistStates.removeAll()
        saveChecklistStates()
    }

    func toggleItem(_ id: String) {
        checklistStates[id] = !(checklistStates[id] ?? false)

        let checkedCount = checklistStates.values.filter { $0 }.count
        if checkedCount >= 5 {
            let today = Calendar.current.startOfDay(for: Date())
            if lastRewardDate == nil || !Calendar.current.isDate(lastRewardDate!, inSameDayAs: today) {
                showingTzaddikVideo = true
                showingBlessedPopup = true
                lastRewardDate = today

                DispatchQueue.main.asyncAfter(deadline: .now() + 3) {
                    withAnimation {
                        self.showingBlessedPopup = false
                    }
                }
            }
        }
    }

    func getItemState(_ id: String) -> Bool {
        checklistStates[id] ?? false
    }

    private func loadChecklistStates() {
        if let data = UserDefaults.standard.data(forKey: "ChecklistStates"),
           let states = try? JSONDecoder().decode([String: Bool].self, from: data) {
            checklistStates = states
        }
    }

    private func saveChecklistStates() {
        if let data = try? JSONEncoder().encode(checklistStates) {
            UserDefaults.standard.set(data, forKey: "ChecklistStates")
        }
    }

    func buttonPressed() {
        selectNextMitzvah()
        hasAccepted = false
    }

    func getMitzvotCountText() -> String {
        if acceptedMitzvotCount == 1 {
            return "1 Mitzvah"
        } else {
            return "\(acceptedMitzvotCount) Mitzvot"
        }
    }

    func acceptMitzvah() {
        guard !hasAccepted else { return }

        withAnimation(.easeInOut(duration: 0.5)) {
            showAcceptAnimation = true
            acceptedMitzvotCount += 1
            hasAccepted = true

            let newCount = acceptedMitzvotCount
            if let clipId = RewardVideoCatalog.clipId(forTotalAcceptedCount: newCount) {
                currentRewardVideo = clipId
                showingRewardVideo = true

                if clipId != RewardVideoCatalog.finalRewardSentinel {
                    achievedLevel = MitzvotLevel.getLevel(for: newCount)
                    showingLevelAchieved = true
                    DispatchQueue.main.asyncAfter(deadline: .now() + 5) {
                        withAnimation {
                            self.showingLevelAchieved = false
                        }
                    }
                }
            }

            DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                self.showAcceptAnimation = false
            }
        }
    }

    /// Dismiss certificate first, then play `finalreward.mp4` (same as Android `requestFinalRewardVideoReplay`).
    func requestFinalRewardReplay() {
        showingLevelAchieved = false
        currentRewardVideo = RewardVideoCatalog.finalRewardSentinel
        showingRewardVideo = true
    }

    func nextMitzvah() {
        selectNextMitzvah()
        hasAccepted = false
    }

    private func selectNextMitzvah() {
        let pool = allMitzvot
        guard !pool.isEmpty else { return }

        let idSet = Set(pool.map(\.id))
        if shuffledDeck.isEmpty || deckIndex >= shuffledDeck.count || idSet != deckPoolIdSet {
            shuffledDeck = pool.shuffled()
            deckIndex = 0
            deckPoolIdSet = idSet
        }

        currentMitzvah = shuffledDeck[deckIndex]
        deckIndex += 1
        showingMitzvah = true
    }
}
