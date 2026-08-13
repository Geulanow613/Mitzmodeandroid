# Swift / Kotlin reference (Mitz Mode embed)

Copy or align these with the mirrored files under `be-a-tzaddik/shared/src/iosMain/`.
For the full two-track workflow (Kotlin vs Swift shell), read **`MITZI_IOS_WORKFLOW.md`**.

## Kotlin — `MainViewController.kt` (iosMain)

Expected exports:

```kotlin
fun MainViewController() = ComposeUIViewController { /* standalone App */ }

fun EmbeddedChecklistViewController(onClose: () -> Unit) = ComposeUIViewController {
    App(deps = deps, embeddedMode = true, onRequestClose = onClose)
}
```

Use the **actual** file in the mirror — do not rely on this snippet if it drifted:

`be-a-tzaddik/shared/src/iosMain/kotlin/com/beardytop/beatzaddik/MainViewController.kt`

`ChecklistEmbedBridge.kt` in the same package handles preload, mitzvot counter, and
check callbacks (parity with Android `TzaddikIntegration.kt`).

## Swift — host view (current mitzi implementation)

```swift
import SwiftUI
import shared

struct DailyChecklistHostView: UIViewControllerRepresentable {
    @ObservedObject var viewModel: DailyMitzvotViewModel
    let onClose: () -> Void

    func makeUIViewController(context: Context) -> UIViewController {
        wireBridge()
        return MainViewControllerKt.EmbeddedChecklistViewController(onClose: {
            ChecklistEmbedBridge.shared.setChecklistItemCheckedHandler(handler: nil)
            onClose()
        })
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        ChecklistEmbedBridge.shared.setMitzvotCount(count: Int32(viewModel.acceptedMitzvotCount))
    }

    private func wireBridge() {
        ChecklistEmbedBridge.shared.setMitzvotCount(count: Int32(viewModel.acceptedMitzvotCount))
        ChecklistEmbedBridge.shared.setChecklistItemCheckedHandler { _, _ in
            DispatchQueue.main.async {
                viewModel.incrementFromChecklistItem()
                ChecklistEmbedBridge.shared.setMitzvotCount(count: Int32(viewModel.acceptedMitzvotCount))
            }
        }
    }
}
```

**App launch** — call preload once in `MitzvahApp.init()`:

```swift
ChecklistEmbedBridge.shared.preloadChecklistDependencies()
```

Present full-screen from Mitz Mode when the user opens **Daily Mitzvot Checklist**;
`onClose` dismisses back to Mitz Mode. See `mitzi-shell/CHECKLIST_COUNTER_INTEGRATION.md`.

## Info.plist (if missing on host)

- `NSLocationWhenInUseUsageDescription` — zmanim / manual city

## Rules

- `MainViewController()` — standalone Be a Tzaddik app only
- `EmbeddedChecklistViewController` — Mitz Mode embed only
- Halachic / checklist **text** lives in Kotlin (`checklist-items.json`, domain Kotlin) — do not duplicate in Swift
- After replacing `shared/` sources, rebuild the KMP framework (`pod install` or `scripts/build_shared_framework.sh`)
- iOS Swift shell is backed up via `sync-mitzi-to-handoff.ps1` → `mitzi-shell/` (see `MITZI_IOS_WORKFLOW.md`)
