# Mitz Mode — Archived Translations

Mitz Mode is **English-only**. These files are the last working translation integration before removal (July 2026).

## Archived files

| File | Role |
|------|------|
| `AppTranslationStore.swift` | SwiftUI translation state + KMP bridge |
| `DeviceLanguageBootstrap.swift` | First-launch device language detection |
| `TranslatableText.swift` | SwiftUI bundled + online translation |
| `LiturgyTranslationText.swift` | Prayer liturgy lookup |
| `LanguageSettingsView.swift` | Language picker sheet |
| `TranslationNoticeView.swift` | Disclaimer before enabling translation |

Bundled JSON lives in the KMP module: `be-a-tzaddik/data/bundled-translations/` and `shared/.../composeResources/files/translations/`.

## Re-activate translations in Mitz Mode

1. Restore archived Swift files over the English-only stubs in `Views/Components/` and `Services/`.
2. Re-add `DeviceLanguageBootstrap.applyIfNeeded()` in `MitzvahApp.swift`.
3. Re-add `.environmentObject(AppTranslationStore.shared)` and `.appTranslationLayoutDirection()`.
4. Restore Language Settings in `AndroidParityDropdownPanel` and `MenuOverlay`.
5. In KMP `MainViewController.kt`, wrap `EmbeddedChecklistViewController` with `ProvideAppTranslation(MitzModeTranslationBridge.manager)`.
6. Set `showLanguageSettings = true` for embedded checklist in `SettingsScreen` (or remove `embeddedMode` guard).
7. Rebuild shared framework: `./scripts/build_shared_framework.sh`

For full multi-language checklist + settings, use the standalone **Be a Tzaddik** app instead (`/be-a-tzaddik-app/`).
