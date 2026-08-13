# Mac quickstart — iOS / Mitz Mode handoff

Copy this entire **`ios-transfer-handoff/`** folder to your Mac (AirDrop, USB, git clone, or Dropbox), then extract to your Dropbox workspace.

## Extract to Dropbox (recommended)

1. Copy `ios-transfer-handoff/` to your Mac (anywhere).
2. **Double-click `extract-to-dropbox.command`** (or run `./extract-to-dropbox.sh` in Terminal).
3. Files land in:
   - `~/Dropbox/claudesucks/` **or**
   - `~/Library/CloudStorage/Dropbox/claudesucks/` (newer macOS Dropbox)

Everything below assumes that extracted path as **`~/Dropbox/claudesucks`**.

## What’s in the bundle

| Path | Contents |
|------|-----------|
| `be-a-tzaddik/` | Full KMP shared module + `data/` (checklist, translations, human shards) |
| `swift-native/` | Flat JSON for native Swift (translations, mitzvot, checklist, explainers) |
| `android-mitzmode/` | Android Studio project (`app/` + Gradle); videos omitted |
| `docs/` | Parity checklist, gradient spec, Swift integration |
| `SYNC_MANIFEST.json` | Last sync timestamp + file counts |

## Two integration paths

### A) Embedded KMP checklist (recommended — same as Android embed)

1. Point Xcode/CocoaPods at `be-a-tzaddik/shared` (see `EMBED.md`).
2. Entry: `EmbeddedChecklistViewController(onClose:)` in `MainViewController.kt`.
3. Translations + checklist load from compose resources inside the framework.

### B) Native Swift Mitz Mode home + JSON assets

1. Add **`swift-native/`** to your Xcode target.
2. Implement lookup per `docs/SWIFT_NATIVE_TRANSLATIONS.md`.
3. Home background gradient: **`docs/MITZ_MODE_HOME_BACKGROUND.md`** (matches `LowEndDeviceBackground.kt`).

### C) Android reference app (Mac with Android Studio)

1. Open **`android-mitzmode/`** in Android Studio.
2. `:beatzaddik-shared` resolves to `../be-a-tzaddik/shared`.
3. See `android-mitzmode/EXCLUDED_MEDIA.md` for reward videos not included in handoff.

## Home screen gradient (iOS native)

Vertical gradient, top → bottom — **no video, no animation**:

| Stop | Hex |
|------|-----|
| 0% | `#050B1F` |
| 45% | `#0E1B47` |
| 85% | `#1A0B3D` |
| 100% | `#0A0420` |

Full Swift/UIKit snippets: `docs/MITZ_MODE_HOME_BACKGROUND.md`

## On Mac after extract

```bash
cd ~/Dropbox/claudesucks/be-a-tzaddik
# If using CocoaPods:
pod install
```

Read **`AGENTS.md`** → **`docs/PARITY_CHECKLIST.md`** for acceptance tests.

## Refresh from Windows (maintainer)

```powershell
cd c:\apps\hehehe\ios-transfer-handoff
.\sync-to-ios-handoff.ps1
.\verify-handoff.ps1
```

Then re-copy `ios-transfer-handoff/` to the Mac and run `extract-to-dropbox.command` again.
