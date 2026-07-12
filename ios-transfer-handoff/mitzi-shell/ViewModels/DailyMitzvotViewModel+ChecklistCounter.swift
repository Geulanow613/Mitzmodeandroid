import Foundation

extension DailyMitzvotViewModel {
    /// Bump the home mitzvah counter when a checklist row is checked (no reward video).
    func incrementFromChecklistItem() {
        acceptedMitzvotCount += 1
    }
}
