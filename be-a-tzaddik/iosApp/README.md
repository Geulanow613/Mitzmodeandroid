# iOS app (Be a Tzaddik)

Build on macOS with Xcode 15+:

1. `cd be-a-tzaddik && ./gradlew :shared:embedAndSignAppleFrameworkForXcode`
2. `cd iosApp && pod install`
3. Open `iosApp.xcworkspace` (generate via Xcode or `xcodegen` if added) and run on simulator/device.
4. In Xcode, set the app target **App Icon** to `AppIcon` (`iosApp/Assets.xcassets` is included).

**Regenerate icons** after updating `appicontzaddik.png` at the project root:

```bash
python tools/generate_app_icons.py
```

The `shared` static framework exports `MainViewController()` for SwiftUI hosting.
