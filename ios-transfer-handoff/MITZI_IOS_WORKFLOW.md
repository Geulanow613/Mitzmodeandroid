# Mitz Mode iOS — two-track sync workflow

Mitz Mode on iOS is **half Swift, half Kotlin**. They must stay in sync without
overwriting each other.

| Track | What it is | Source of truth | Git |
|-------|------------|-----------------|-----|
| **Kotlin (checklist engine)** | Compose UI, halacha text, zmanim, checklist JSON | `c:\apps\hehehe\be-a-tzaddik\` | Yes — main repo |
| **Swift (Mitz Mode shell)** | Home screen, mitzvah cards, videos, prayers, Xcode project | `Dropbox/claudesucks/mitzi/` | Via `ios-transfer-handoff/mitzi-shell/` mirror |

**Never** copy Android `app/` into `mitzi`. **Never** robocopy `be-a-tzaddik` into
`mitzi` (except the sibling junction `../be-a-tzaddik` that CocoaPods already uses).

---

## Architecture

```
Dropbox/claudesucks/
├── be-a-tzaddik/          ← junction → c:\apps\hehehe\be-a-tzaddik (KMP source)
└── mitzi/                 ← Swift Xcode project (Mitz Mode)
         │
         │  Podfile: pod 'shared', :path => '../be-a-tzaddik/shared'
         ▼
    EmbeddedChecklistViewController  (Kotlin Compose checklist)
         ▲
         │  ChecklistEmbedBridge (counter, check callbacks)
         │
    DailyChecklistHostView.swift   (Swift host — iOS-only wiring)
```

Halachic copy lives in **Kotlin only** (`checklist-items.json`, `SeasonalMitzvahText.kt`,
etc.). Swift does not duplicate checklist explainers.

---

## When you change halacha / checklist logic (Windows)

1. Edit `be-a-tzaddik/` (same as Android).
2. Regenerate strings if needed:
   ```powershell
   cd c:\apps\hehehe\be-a-tzaddik
   python tools/extract_all_strings.py
   ```
3. Refresh handoff mirror:
   ```powershell
   cd c:\apps\hehehe\ios-transfer-handoff
   .\sync-to-ios-handoff.ps1
   ```
4. **Do not touch `mitzi/`** — the junction `Dropbox/claudesucks/be-a-tzaddik` already
   points at the updated source on this machine.
5. Commit + push the main repo (`be-a-tzaddik`, `ios-transfer-handoff` if you mirror it).
6. On Mac (after Dropbox sync):
   ```bash
   cd ~/Dropbox/claudesucks/mitzi
   pod install
   # or: scripts/build_shared_framework.sh
   ```
   Open `MitzModeTest.xcworkspace`, Clean Build Folder, Run.

---

## When you change iOS Swift / Xcode (Mac or Dropbox mitzi)

Edit directly in `Dropbox/claudesucks/mitzi/`. Typical iOS-only files:

- `Views/`, `ViewModels/`, `Models/`, `Services/`, `Utilities/`
- `MitzvahApp.swift`, `Podfile`, `project.yml`
- `Views/Checklist/DailyChecklistHostView.swift` — Swift ↔ Kotlin bridge
- `ViewModels/DailyMitzvotViewModel+ChecklistCounter.swift`

**Not** in Swift: checklist explainer text (that's Kotlin).

After Swift edits, back up to git (from Windows once Dropbox syncs):

```powershell
cd c:\apps\hehehe\ios-transfer-handoff
.\sync-mitzi-to-handoff.ps1
```

Then commit `ios-transfer-handoff/mitzi-shell/` in the main repo.

---

## Kotlin bridge contract (do not break)

Swift must keep calling these when embedding the checklist:

| Swift | Kotlin |
|-------|--------|
| `ChecklistEmbedBridge.shared.preloadChecklistDependencies()` | App launch warmup (`MitzvahApp.swift`) |
| `MainViewControllerKt.EmbeddedChecklistViewController(onClose:)` | Compose embed entry |
| `setMitzvotCount(count:)` | Home counter → gold pill in checklist |
| `setChecklistItemCheckedHandler { … }` | Checklist check → bump Swift counter |
| `setChecklistItemCheckedHandler(nil)` on close | Avoid stale callbacks |

See `mitzi-shell/Views/Checklist/DailyChecklistHostView.swift` and
`docs/SWIFT_KOTLIN_REFERENCE.md` for the current template.

---

## What sync scripts do

| Script | Direction | Touches |
|--------|-----------|---------|
| `sync-to-ios-handoff.ps1` | `be-a-tzaddik` → `ios-transfer-handoff/be-a-tzaddik` | Kotlin only |
| `sync-mitzi-to-handoff.ps1` | `Dropbox/.../mitzi` → `ios-transfer-handoff/mitzi-shell` | Swift shell only |
| `verify-handoff.ps1` | Checks KMP mirror hash | Kotlin only |

**No script** overwrites live `Dropbox/claudesucks/mitzi` automatically.

---

## Common pitfalls

1. **`mitzi/be-a-tzaddik/`** — orphan `.git` folder only; **not** the pod source. Pod uses
   `../be-a-tzaddik/shared`.
2. **Stale KMP framework** — source updated but iOS still shows old text until
   `pod install` + rebuild on Mac.
3. **Handoff docs lag** — trust `mitzi-shell/` and `CHECKLIST_COUNTER_INTEGRATION.md`
   over older templates in `mitz-mode-ios-integration-kit/`.
4. **Legacy native checklist** — `DailyMitzvotChecklist.swift` etc. are excluded in
   `project.yml`; do not re-enable unless intentionally reverting from KMP embed.
5. **Android `app/` ≠ mitzi** — Android embed is `TzaddikIntegration.kt`; iOS embed is
   `DailyChecklistHostView` + `ChecklistEmbedBridge`. Same behavior, different glue code.

---

## Restore mitzi-shell → Dropbox (optional, careful)

Only when setting up a new Mac or recovering lost Swift files:

```powershell
cd c:\apps\hehehe\ios-transfer-handoff
.\sync-mitzi-to-handoff.ps1 -PushToDropbox
```

This copies `mitzi-shell/` → `Dropbox/claudesucks/mitzi` but **skips** `Pods/`,
`be-a-tzaddik/`, and large video assets. Always run `pod install` afterward.
