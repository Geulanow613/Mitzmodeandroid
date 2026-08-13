import SwiftUI
import Foundation

class MitzModeViewModel: ObservableObject {
    @Published var currentMitzvah: Mitzvah?
    @Published var showingMitzvah = false
    @Published var showingRewardVideo = false
    @Published var currentRewardVideo = 1
    @Published var showAcceptAnimation = false
    @Published var hasAccepted = false
    @AppStorage("textSize") var savedTextSize: Double = 18.0
    @AppStorage("acceptedMitzvotCount") private var acceptedMitzvotCount: Int = 0
    
    private var mitzvot: [Mitzvah] = []
    private var shuffledDeck: [Mitzvah] = []
    private var deckIndex: Int = 0
    
    init() {
        loadMitzvot()
    }
    
    func buttonPressed() {
        selectNextMitzvah()
        hasAccepted = false
    }
    
    func acceptMitzvah() {
        withAnimation(.easeInOut(duration: 0.5)) {
            showAcceptAnimation = true
            acceptedMitzvotCount += 1
            
            // Check for reward video after accepting
            if acceptedMitzvotCount % 7 == 0 {
                currentRewardVideo = Int.random(in: 1...13)
                showingRewardVideo = true
            }
            
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                self.showAcceptAnimation = false
                self.hasAccepted = true
            }
        }
    }
    
    func nextMitzvah() {
        selectNextMitzvah()
        hasAccepted = false
    }
    
    func updateTextSize(_ newSize: Double) {
        savedTextSize = newSize
    }
    
    private func loadMitzvot() {
        if let url = Bundle.main.url(forResource: "mitzvot", withExtension: "json") {
            do {
                let data = try Data(contentsOf: url)
                let decoder = JSONDecoder()
                let mitzvotData = try decoder.decode(MitzvotData.self, from: data)
                self.mitzvot = mitzvotData.mitzvot
            } catch {
                print("Error loading mitzvot: \(error)")
            }
        }
    }
    
    private func selectNextMitzvah() {
        guard !mitzvot.isEmpty else { return }

        if shuffledDeck.isEmpty || deckIndex >= shuffledDeck.count {
            shuffledDeck = mitzvot.shuffled()
            deckIndex = 0
        }

        currentMitzvah = shuffledDeck[deckIndex]
        deckIndex += 1
        showingMitzvah = true
    }
} 