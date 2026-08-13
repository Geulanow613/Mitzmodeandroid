# Standalone Be a Tzaddik (iOS)

## Prereqs

- macOS with Xcode 15+
- CocoaPods installed
- JDK for Gradle

## Build steps

From repo root:

1. `cd be-a-tzaddik`
2. `./gradlew :shared:embedAndSignAppleFrameworkForXcode`
3. `cd iosApp`
4. `pod install`
5. Open `iosApp.xcworkspace` in Xcode
6. Run target `iosApp` on simulator/device

## App icon note

- In Xcode, set app target icon set to `AppIcon`.
- If app icon art changed, regenerate icons from repo root:
  - `python tools/generate_app_icons.py`

## Key files to keep aligned

- `be-a-tzaddik/iosApp/iosApp/iOSApp.swift`
- `be-a-tzaddik/iosApp/iosApp/ContentView.swift`
- `be-a-tzaddik/iosApp/Podfile`
- `be-a-tzaddik/shared/src/iosMain/kotlin/com/beardytop/beatzaddik/MainViewController.kt`
- `be-a-tzaddik/shared/src/iosMain/kotlin/com/beardytop/beatzaddik/platform/`
