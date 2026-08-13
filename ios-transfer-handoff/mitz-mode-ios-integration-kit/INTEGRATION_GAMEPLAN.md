# Mitz Mode iOS Integration Gameplan (Swift app)

Goal: replicate Android behavior by embedding the Be a Tzaddik checklist flow as **Daily Mitzvot Checklist** inside the existing Swift Mitz Mode iOS app.

## 1) Add shared KMP module to iOS app

- In the Swift Mitz Mode iOS project, add pod dependency to the KMP shared module.
- Example in iOS app `Podfile`:

```ruby
pod 'shared', :path => '/absolute/path/to/be-a-tzaddik/shared'
```

- Run `pod install`.

Use the mirrored path on Mac if you copied only `ios-transfer-handoff/`:

```ruby
pod 'shared', :path => '../be-a-tzaddik/shared'
```

## 2) Expose embedded entrypoint from Kotlin

`MainViewController.kt` (iosMain) includes:

- `MainViewController()` — standalone Be a Tzaddik
- `EmbeddedChecklistViewController(onClose:)` — embedded in Mitz Mode

Swift calls `MainViewControllerKt.EmbeddedChecklistViewController(onClose: ...)`.

## 3) Swift host view/controller in Mitz Mode iOS

- Create `UIViewControllerRepresentable` that loads the embedded Kotlin UIViewController.
- Present full-screen when user taps Daily Mitzvot Checklist.
- On close callback, dismiss and return to Mitz Mode main screen.
- Optional: pass custom `returnToMainIcon` composable from Kotlin if branding image is needed (Android passes Mitz Mode logo).

## 4) Mitz Mode home background (native Swift)

Android uses a **plain static gradient** on all devices (no looping video). Implement the same on iOS before polish work — full spec in **`../docs/MITZ_MODE_HOME_BACKGROUND.md`** (colors, SwiftUI/UIKit snippets, QA).

## 5) Match Android UX (current)

- Embedded title: **the Daily Mitzvot Checklist**
- Bottom nav: **Today | Timer | Mitz logo (center) | Settings | About**
- About tab: disclaimer + scope (not in Settings)
- Onboarding: Back to Mitz Mode on first step; manual city search on location step
- Settings: same city search/list as onboarding (A→Z, country labels)
- Today: tappable Current Period scrolls to prayer section; evening items grayed before sunset
- Holy days: Shabbat notice; chutz 2nd-day Yom Tov notice (see `HolyDayPhoneRules.kt`)
- Return-to-main confirms before exit

## 6) iOS permissions and settings

- `Info.plist`:
  - `NSLocationWhenInUseUsageDescription`
- Notifications if kashrut timer alerts are enabled on iOS

## 7) QA checklist on iOS

- Home screen uses static gradient only (no video background) — see `MITZ_MODE_HOME_BACKGROUND.md`
- Open checklist from Mitz Mode home
- Complete onboarding without GPS (manual city path)
- Verify city search A→Z in onboarding and Settings
- Verify 5 bottom tabs; About shows disclaimer
- Center logo returns to Mitz Mode with confirmation
- Tap “Current Period: Afternoon” → scrolls to Afternoon Prayer
- At 2pm, evening mitzvot are grayed (upcoming)
- Standalone Be a Tzaddik still launches via `MainViewController()`

See `UI_PLACEMENT_AND_BEHAVIOR_PARITY.md` for full checklist.
