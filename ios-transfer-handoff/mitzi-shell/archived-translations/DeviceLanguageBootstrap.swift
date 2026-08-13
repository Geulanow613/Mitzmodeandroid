import Foundation
import shared

/// First launch: pick he/es/fr/ru from the device language list (Android parity).
enum DeviceLanguageBootstrap {
  static func applyIfNeeded() {
    _ = MitzModeTranslationBridge.shared
    MitzModeTranslationBridge.shared.ensureInitialLanguageFromDevice()
  }
}
