# Mitz Mode home screen background (iOS parity)

Android **no longer** plays the looping `background.mp4` video on any device. All phones use the same **static vertical gradient** as the former “low memory” path.

iOS native Mitz Mode home UI must match this — do not gate on RAM, device tier, or a user preference for video.

## Android reference (source of truth)

| Repo path | Role |
|-----------|------|
| `app/.../ui/components/LowEndDeviceBackground.kt` | Gradient composable |
| `app/.../ui/MitzModeApp.kt` | Home screen always calls `LowEndDeviceBackground()` |
| `app/.../util/DeviceCapabilityChecker.kt` | `canHandleVideoBackground()` always `false` (no looping bg) |

**Not used for home background:** `VideoBackground.kt`, `GradientBackground.kt` (animated), `background.mp4`.

**Still allowed on both platforms:** short **reward** videos (level-up, blessed animation, etc.) — separate players, not the home loop.

## Gradient specification

Vertical gradient, top → bottom:

| Stop | Hex | Description |
|------|-----|-------------|
| 0% | `#050B1F` | Deep midnight navy |
| 45% | `#0E1B47` | Mid blue |
| 85% | `#1A0B3D` | Plum indigo |
| 100% | `#0A0420` | Dark base |

No animation, stars, shimmer, or video layer on the home screen.

## SwiftUI example

```swift
import SwiftUI

struct MitzModeStaticBackground: View {
    var body: some View {
        LinearGradient(
            stops: [
                .init(color: Color(red: 5/255, green: 11/255, blue: 31/255), location: 0),
                .init(color: Color(red: 14/255, green: 27/255, blue: 71/255), location: 0.45),
                .init(color: Color(red: 26/255, green: 11/255, blue: 61/255), location: 0.85),
                .init(color: Color(red: 10/255, green: 4/255, blue: 32/255), location: 1),
            ],
            startPoint: .top,
            endPoint: .bottom
        )
        .ignoresSafeArea()
    }
}

struct MitzModeHomeView: View {
    var body: some View {
        ZStack {
            MitzModeStaticBackground()
            // menu, mitzvah button, counters, etc.
        }
    }
}
```

## UIKit example

Use `CAGradientLayer` with the same four colors and locations `[0, 0.45, 0.85, 1.0]`, `startPoint = (0.5, 0)`, `endPoint = (0.5, 1)`.

## QA

- [ ] Home screen shows only the static gradient on a high-end iPhone (no video, no animated star field).
- [ ] Completed-mitzvot / list screens that mirrored Android also use this gradient (Android: `CompletedMitzvotScreen.kt`).
- [ ] No `background.mp4` asset loaded on app launch for the home screen.

## Embedded checklist note

The **Daily Mitzvot Checklist** (KMP `BeATzaddikApp`) uses its own parchment/navy UI (`HolyLightBackground`, etc.) — this doc applies to the **native Mitz Mode generator home**, not the embedded checklist interior.
