# Be a Tzaddik — embed integration (Android + iOS)

Single source of truth: **`be-a-tzaddik/shared`** in this monorepo. Do **not** maintain a long-lived fork on Mac/iOS — point the host app at this path (or sync the handoff mirror and verify).

## Android (Mitz Mode) — already wired

`settings.gradle` at repo root:

```gradle
include ':beatzaddik-shared'
project(':beatzaddik-shared').projectDir = file('be-a-tzaddik/shared')
```

The `:app` module depends on `:beatzaddik-shared`. Zmanim use **KosherJava** on `androidMain` only — do not change that path for production Android.

## iOS (Mitz Mode or standalone)

### Recommended: path reference (like Android)

In the Mac/Xcode workspace, add the KMP module from the **same** `be-a-tzaddik/shared` directory (git submodule, sibling folder, or CI checkout). CocoaPods `Podfile` can use:

```ruby
pod 'shared', :path => '../be-a-tzaddik/shared'
```

Adjust the relative path to match your repo layout. Rebuild the `shared` framework after pulling `main`.

### Fallback: file bundle mirror

If you cannot path-reference yet, copy from **`ios-transfer-handoff/be-a-tzaddik/`** after running `sync-to-ios-handoff.ps1` on Windows, then run **`verify-handoff.ps1`** to confirm the mirror matches source.

## Entry points

| Target | Kotlin entry |
|--------|----------------|
| Standalone iOS app | `MainViewController()` |
| Mitz Mode embed | `EmbeddedChecklistViewController(onClose:)` |

Both live in `shared/src/iosMain/kotlin/com/beardytop/beatzaddik/MainViewController.kt`.

## Zmanim parity (iOS)

| Platform | Implementation |
|----------|----------------|
| Android production | `JewishCalendarBackend.android.kt` + KosherJava |
| iOS | `NativeJewishCalendarBackend.kt` + `SharedZmanimBuilder` (commonMain solar math) |
| Tests | `SharedZmanimParityTest` (androidUnitTest) vs KosherJava, ~90s tolerance |

Shared module: `domain/zmanim/SolarZmanim.kt`, `SharedZmanimBuilder.kt`.

Run on Windows before handoff:

```powershell
cd c:\apps\hehehe
.\gradlew :beatzaddik-shared:testDebugUnitTest :beatzaddik-shared:compileDebugKotlinAndroid
```

## Checklist JSON workflow

1. Edit `be-a-tzaddik/data/*.json`
2. `ios-transfer-handoff/sync-to-ios-handoff.ps1` (copies `data/` → `composeResources/files/`, then mirrors)
3. `ios-transfer-handoff/verify-handoff.ps1` (fails if mirror drifted)

## What not to do

- Do not swap Android to `SharedZmanimBuilder` — keep KosherJava on `androidMain`.
- Do not hand-edit only `ios-transfer-handoff/` without syncing back to `be-a-tzaddik/`.
- Do not treat `HeuristicZmanim` (fixed 6:30) as production iOS zmanim — iOS uses `SharedZmanimBuilder` when lat/lon are set.

## Agent start

1. `ios-transfer-handoff/AGENTS.md`
2. `ios-transfer-handoff/docs/PARITY_CHECKLIST.md`
3. This file (`EMBED.md`)
