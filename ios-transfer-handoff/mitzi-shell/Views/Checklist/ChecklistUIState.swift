import SwiftUI

class ChecklistUIState: ObservableObject {
    @Published var selectedGender: Gender = .male
    @Published var showingChecklist: Bool = false
    @Published var textSize: CGFloat = 14
    
    init(selectedGender: Gender = .male, showingChecklist: Bool = false) {
        self.selectedGender = selectedGender
        self.showingChecklist = showingChecklist
    }
} 