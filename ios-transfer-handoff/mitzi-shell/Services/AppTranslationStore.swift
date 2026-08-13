import Foundation
import Combine

/// English-only stub for Mitz Mode. Full implementation archived in `archived-translations/`.
@MainActor
final class AppTranslationStore: ObservableObject {
    static let shared = AppTranslationStore()

    @Published private(set) var currentLanguage: String = "en"
    @Published private(set) var translationEnabled: Bool = false
    @Published private(set) var catalogGeneration: Int = 0

    var isRtl: Bool { false }
    var isActive: Bool { false }

    func bundledText(_ text: String) -> String { text }

    func translate(_ text: String) async -> String { text }

    func translateToLanguage(_ text: String, languageCode: String) async -> String { text }

    func liturgyTranslation(_ text: String, languageCode: String) -> String { text }
}
