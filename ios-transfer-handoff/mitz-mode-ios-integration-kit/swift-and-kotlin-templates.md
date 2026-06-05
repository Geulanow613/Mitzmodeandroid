# Swift and Kotlin Templates for Mitz Mode iOS

Use these templates while integrating into the Swift Mitz Mode iOS app.

## Kotlin template (shared iosMain)

Create/update `MainViewController.kt` in shared iosMain to expose both standalone + embedded variants:

```kotlin
package com.beardytop.beatzaddik

import androidx.compose.ui.window.ComposeUIViewController
import com.beardytop.beatzaddik.platform.PlatformLocationService
import kotlinx.coroutines.runBlocking

fun MainViewController() = ComposeUIViewController {
    val deps = runBlocking {
        AppDependencies.create(platformContext = null, locationService = PlatformLocationService())
    }
    App(deps)
}

fun EmbeddedChecklistViewController(onClose: () -> Unit) = ComposeUIViewController {
    val deps = runBlocking {
        AppDependencies.create(platformContext = null, locationService = PlatformLocationService())
    }
    App(
        deps = deps,
        embeddedMode = true,
        onRequestClose = onClose
    )
}
```

## Swift template (host in Mitz Mode iOS)

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

## Swift usage example

Use the **static home gradient** on all devices (no looping video). See `../docs/MITZ_MODE_HOME_BACKGROUND.md`. Control positions: `../docs/MITZ_MODE_HOME_LAYOUT.md`.

```swift
struct MitzModeHomeView: View {
    @State private var showChecklist = false

    var body: some View {
        ZStack {
            MitzModeStaticBackground() // from MITZ_MODE_HOME_BACKGROUND.md
            VStack {
                Button("Daily Mitzvot Checklist") {
                    showChecklist = true
                }
            }
        }
        .fullScreenCover(isPresented: $showChecklist) {
            DailyChecklistHostView {
                showChecklist = false
            }
        }
    }
}
```

## Notes

- Keep standalone `MainViewController()` unchanged for Be a Tzaddik app.
- Use `EmbeddedChecklistViewController` only for Mitz Mode iOS embedding.
- Re-run pod install/build after Kotlin shared updates.
