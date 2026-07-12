import Foundation
import Combine

@MainActor
class MitzvahSubmissionViewModel: ObservableObject {
    @Published var name: String = ""
    @Published var description: String = ""
    @Published var errorMessage: String?
    @Published var isSubmitting: Bool = false
    @Published var showingConfirmation: Bool = false
    @Published var isNameValid: Bool = false
    
    var cancellables = Set<AnyCancellable>()
    private let formspreeService = FormspreeService()
    
    var canSubmit: Bool {
        isNameValid &&
        !description.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty &&
        !isSubmitting
    }
    
    func validateInput() {
        isNameValid = !name.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty
    }
    
    func submitSuggestion() async {
        guard canSubmit else { return }
        
        isSubmitting = true
        errorMessage = nil
        
        do {
            let suggestion = MitzvahSuggestion(
                name: name.trimmingCharacters(in: .whitespacesAndNewlines),
                description: description.trimmingCharacters(in: .whitespacesAndNewlines),
                timestamp: Date()
            )
            try await formspreeService.submitSuggestion(suggestion)
            showingConfirmation = true
        } catch {
            errorMessage = "Failed to submit suggestion: \(error.localizedDescription)"
        }
        
        isSubmitting = false
    }
} 