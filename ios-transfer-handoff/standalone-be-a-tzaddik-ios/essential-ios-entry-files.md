# Essential iOS Entry Files

Use these as quick-reference copies of the current working standalone iOS entry setup.

## `iOSApp.swift`

```swift
import SwiftUI
import shared

@main
struct iOSApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
```

## `ContentView.swift`

```swift
import SwiftUI
import shared

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea(.all, edges: .bottom)
    }
}

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
```

## `Podfile`

```ruby
platform :ios, '15.0'
use_frameworks!

target 'iosApp' do
  pod 'shared', :path => '../shared'
end
```

## `MainViewController.kt`

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

/** For Mitz Mode iOS embed — same as Android embedded checklist. */
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

## Swift embed (Mitz Mode)

```swift
MainViewControllerKt.EmbeddedChecklistViewController(onClose: {
    // dismiss embedded checklist
})
```

See `mitz-mode-ios-integration-kit/swift-and-kotlin-templates.md`.
```
