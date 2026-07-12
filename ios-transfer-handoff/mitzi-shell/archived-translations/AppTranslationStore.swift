import Foundation
import Combine
import shared

@MainActor
final class AppTranslationStore: ObservableObject {
    static let shared = AppTranslationStore()

    @Published private(set) var currentLanguage: String = "en"
    @Published private(set) var translationEnabled: Bool = false
    @Published private(set) var catalogGeneration: Int = 0

    private var removeListener: (() -> Void)?

    var isRtl: Bool {
        currentLanguage == "he" || currentLanguage == "yi"
    }

    var isActive: Bool {
        translationEnabled && currentLanguage != "en"
    }

    private init() {
        refreshFromBridge()
        removeListener = MitzModeTranslationBridge.shared.addStateListener { [weak self] in
            Task { @MainActor in
                self?.refreshFromBridge()
            }
        }
    }

    func refreshFromBridge() {
        currentLanguage = MitzModeTranslationBridge.shared.currentLanguage()
        translationEnabled = MitzModeTranslationBridge.shared.translationEnabled()
        catalogGeneration = Int(MitzModeTranslationBridge.shared.catalogGeneration())
    }

    func setCurrentLanguage(_ code: String) {
        MitzModeTranslationBridge.shared.setCurrentLanguage(languageCode: code)
        refreshFromBridge()
    }

    func setTranslationEnabled(_ enabled: Bool) {
        MitzModeTranslationBridge.shared.setTranslationEnabled(enabled: enabled)
        refreshFromBridge()
    }

    func supportedLanguages() -> [LanguageOption] {
        MitzModeTranslationBridge.shared.supportedLanguages()
    }

    func bundledText(_ text: String) -> String {
        guard isActive else { return text }
        return RuntimeZmanLocalizationKt.resolveBundledTranslationSync(text: text, languageCode: currentLanguage)
    }

    func translate(_ text: String) async -> String {
        guard isActive else { return text }
        return await withCheckedContinuation { continuation in
            MitzModeTranslationBridge.shared.translate(text: text) { result in
                continuation.resume(returning: result)
            }
        }
    }

    func translateToLanguage(_ text: String, languageCode: String) async -> String {
        liturgyTranslation(text, languageCode: languageCode)
    }

    /// Prayer liturgy lookup — independent of the global translation toggle.
    func liturgyTranslation(_ text: String, languageCode: String) -> String {
        guard languageCode != "en", !text.isEmpty else { return text }
        return RuntimeZmanLocalizationKt.resolveBundledTranslationSync(text: text, languageCode: languageCode)
    }
}
