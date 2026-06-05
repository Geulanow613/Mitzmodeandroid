# Swift / Kotlin reference (Mitz Mode embed)

Copy or align these with the mirrored files under `be-a-tzaddik/shared/src/iosMain/`.

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

## Swift — host view

```swift
import SwiftUI
import shared

struct DailyChecklistHostView: UIViewControllerRepresentable {
    let onClose: () -> Void

    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.EmbeddedChecklistViewController(onClose: onClose)
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
```

Present full-screen from Mitz Mode when the user opens **Daily Mitzvot Checklist**; `onClose` dismisses back to Mitz Mode.

## Info.plist (if missing on host)

- `NSLocationWhenInUseUsageDescription` — zmanim / manual city

## Rules

- `MainViewController()` — standalone Be a Tzaddik app only
- `EmbeddedChecklistViewController` — Mitz Mode embed only
- After replacing `shared/` sources, rebuild the KMP framework the host app links to (whatever build path that project already uses)
