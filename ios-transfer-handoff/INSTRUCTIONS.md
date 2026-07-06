# Instructions — agent start here

**Read this file first.** It explains what Be a Tzaddik is, how it fits inside Mitz Mode,
where code lives on Windows vs Mac, and how to sync changes. For parity acceptance
criteria see `docs/PARITY_CHECKLIST.md`; for file locations see `docs/SOURCE_MAP.md`
and `AGENTS.md`.

---

## What this project is

**Be a Tzaddik** is the *Daily Mitzvot Checklist*: a **Kotlin Multiplatform (KMP)**
Compose app (`be-a-tzaddik/shared`) with shared UI, halacha logic, zmanim, and
bundled JSON (checklist copy, cities, translations).

It is **not** a separate product fork — it is **one module** used in two shipping modes:

| Mode | Purpose | Android | iOS |
|------|---------|---------|-----|
| **Embedded in Mitz Mode** (primary) | Checklist inside the main Mitz Mode app | Repo root `app/` → `TzaddikIntegration.kt` → `App(embeddedMode=true)` via Gradle `:beatzaddik-shared` | `~/Dropbox/claudesucks/mitzi` → CocoaPods `pod 'shared', :path => '../be-a-tzaddik/shared'` → `EmbeddedChecklistViewController(onClose:)` |
| **Standalone app** (optional) | Separate “Be a Tzaddik” store listing later | `be-a-tzaddik/androidApp/` | `be-a-tzaddik/iosApp/` → `MainViewController()` |

Both entry points are in `shared/src/iosMain/.../MainViewController.kt`:
- **Standalone:** `MainViewController()`
- **Mitz Mode embed:** `EmbeddedChecklistViewController(onClose:)` — same `App()`, with `embeddedMode = true` and a close callback

**Android embed host:** `app/src/main/java/com/beardytop/mitzmode/tzaddik/TzaddikIntegration.kt`

### Translations (important)

- **Mitz Mode (native Swift shell):** English only for now. Translation UI was removed
  from the main app; archived under `mitzi/archived-translations/`.
- **Be a Tzaddik KMP checklist:** Fully translatable (he/es/fr/ru bundles in
  `shared/.../composeResources/files/translations/`). Used when running standalone
  or if translations are re-enabled in the embed.
- **swift-native/** in this handoff folder = flat JSON for native Swift Mitz Mode
  (mitzvot list, etc.) — separate from the KMP checklist translations.

---

## Source of truth & folder layout

### Windows (development machine)

| Path | Role |
|------|------|
| `c:\apps\hehehe\be-a-tzaddik\shared` | **KMP source of truth** — edit here |
| `c:\apps\hehehe\be-a-tzaddik\data\` | Checklist JSON sources; synced into `composeResources/files/` |
| `c:\apps\hehehe\be-a-tzaddik\tools\` | Python bundlers, city catalog generators (not mirrored to handoff) |
| `c:\apps\hehehe\app\` | Mitz Mode Android app (embeds `:beatzaddik-shared`) |
| `c:\apps\hehehe\ios-transfer-handoff\` | Mirror bundle for Mac + docs |
| `c:\apps\hehehe\Dropbox\claudesucks\be-a-tzaddik\` | **Mac iOS pod source** (synced from Windows) |
| `c:\apps\hehehe\Dropbox\claudesucks\mitzi\` | Mitz Mode iOS Xcode project |
| `c:\apps\hehehe\Dropbox\ios-transfer-handoff\` | Handoff docs + mirror (Dropbox copy) |

Gradle wiring (repo root `settings.gradle`):

```gradle
include ':beatzaddik-shared'
project(':beatzaddik-shared').projectDir = file('be-a-tzaddik/shared')
```

### Mac (via Dropbox)

| Path | Role |
|------|------|
| `~/Dropbox/claudesucks/be-a-tzaddik/shared` | Sibling of `mitzi/` — what the Podfile points at |
| `~/Dropbox/claudesucks/mitzi` | Mitz Mode iOS — open `MitzModeTest.xcworkspace` |
| `~/Dropbox/ios-transfer-handoff` | Docs + full handoff mirror |

The iOS `Podfile` uses `pod 'shared', :path => '../be-a-tzaddik/shared'`.  
**Do not** maintain a long-lived fork on Mac — sync from Windows, then `pod install`.

---

## Location, timezone & zmanim (read before debugging “wrong times”)

### Manual city vs GPS

Everything (clock, zmanim labels, Hebrew date rollover) reads **`UserProfile.timezoneId`**
plus **`latitude` / `longitude`** for solar math.

| Mode | Coordinates | Timezone |
|------|-------------|----------|
| **Manual city** | From `manual-cities.json` row | Row's `timezoneId` (e.g. `Asia/Jerusalem`) |
| **GPS** | Device/emulator lat/lng | Resolved by **`LocationTimezone.kt`** — **not** the device clock |

### GPS timezone bug (fixed 2026-07-06)

**Symptom:** Android emulator at Mountain View shows **2:42 PM** when it's **7:42 AM**
Pacific — because the emulator timezone is **UTC** while GPS coords are California.

**Fix:** `domain/LocationTimezone.kt` + `ManualCities.nearestTimezoneId()`:
- Finds the **nearest catalog city's IANA timezone** from GPS coordinates.
- Uses that timezone **only for display and zmanim epoch conversion**.
- **Zmanim solar math still uses the actual GPS lat/lng** — we do **not** move the
  user to the catalog city's coordinates.

**Also:**
- `AppViewModel.refreshGps()` — resolves timezone on GPS success.
- `runStartupMaintenance()` — reconciles timezone on app launch for GPS profiles.
- `PlatformLocationService.android.kt` — calls `LocationTimezone.resolve()` at GPS fix.
- **iOS:** `PlatformLocationService.ios.kt` `getCurrentLocation()` is still a **stub**
  (`Unavailable`); GPS mode on iOS relies on profile coords + catalog timezone until
  CoreLocation is wired up.

### City catalog (`manual-cities.json`)

- **~3,926 cities** (pop ≥ 100k from GeoNames + original curated list).
- Used for: Settings/Onboarding picker, GPS timezone fallback, elevation fallback.
- Source TSV: `be-a-tzaddik/tools/manual_cities.tsv`
- Regenerate: `python expand_manual_cities_from_geonames.py` → `python gen_manual_cities.py`
- **Adding more cities improves timezone fallback coverage; it does not change zmanim
  coordinates on GPS** (those always come from the GPS fix).

### Zmanim implementation split

| Platform | Backend | Notes |
|----------|---------|-------|
| **Android (production)** | `JewishCalendarBackend.android.kt` + **KosherJava** | Do **not** swap to `SharedZmanimBuilder` on Android |
| **iOS** | `NativeJewishCalendarBackend.kt` + **`SharedZmanimBuilder`** / `SolarZmanim.kt` | KMP-safe math (no `java.lang.Math`) |
| **Tests** | `SharedZmanimParityTest`, `FastDayZmanimSanityTest`, `LocationTimezoneTest` | Run on Windows before handoff |

---

## Halachic day boundary & fast times (2026-07-06)

### Hebrew date advances at tzeit, not civil midnight

- `domain/HalachicDayRollover.kt` — after tonight's tzeit, `dayInfoAt` returns the
  **next Hebrew day** (date, fast/holiday flags, seasons) while the **Gregorian
  civil label** stays on the real calendar day.
- `DayInfo.startedTonightAtTzeit` marks this state.
- **Fixes:** “still 17 Tammuz at 10 PM after the fast ended”; Hebrew date stuck on
  Thursday night when halachically already Friday.

### Fast time display

- `PublicFastDayText` — erev-fast uses **tomorrow's dawn** (`nightObligationsEndMillis`).
- `ChecklistZmanEvaluator` — Yom Kippur / Tisha B'Av ACTIVE from previous sunset.
- `OmerCountText` — rollover-aware after tzeit.
- **Fixes:** “fast starts at 2:12 PM” (wrong TZ); should be ~4:10 AM dawn in Jerusalem summer.

### Jerusalem / Shushan Purim / Purim Meshulash

Rules live in `domain/JerusalemPurimRules.kt` and related files. Jerusalem uses
**15 Adar** (Shushan Purim), not 14 Adar. Purim Meshulash (when 15 Adar is Shabbat)
has a three-day schedule. See `CHANGELOG.md` and debug scenarios in
`ChecklistDebugScenarios.kt` — do not use legacy `upcomingHolidays` paths that key
only on 14 Adar for Jerusalem.

---

## Windows → Mac sync (do this after every shared-module change)

```powershell
cd c:\apps\hehehe\ios-transfer-handoff
.\sync-to-ios-handoff.ps1          # mirrors be-a-tzaddik into handoff; runs swift-native export
.\verify-handoff.ps1               # fails if mirror drifted

# Push to Dropbox (both required for Mitz Mode iOS embed):
robocopy c:\apps\hehehe\be-a-tzaddik c:\apps\hehehe\Dropbox\claudesucks\be-a-tzaddik /E /XD build .gradle .kotlin .idea node_modules Pods DerivedData .cxx captures androidApp tools .geonames_cache
robocopy c:\apps\hehehe\ios-transfer-handoff c:\apps\hehehe\Dropbox\ios-transfer-handoff /E /XD .git build .gradle .kotlin
```

**Robocopy exit codes 0–7 = success** (partial copy counts as success).

### Tests on Windows (before handoff)

```powershell
cd c:\apps\hehehe
.\gradlew :beatzaddik-shared:testDebugUnitTest
```

Focused regression tests:

```powershell
.\gradlew :beatzaddik-shared:testDebugUnitTest --tests "*HebrewDateTzeitRolloverTest" --tests "*FastDayZmanimSanityTest" --tests "*LocationTimezoneTest"
```

---

## Mac build steps (after Dropbox sync)

1. Wait for Dropbox to finish syncing `claudesucks/be-a-tzaddik` and `mitzi`.
2. Reinstall pods and rebuild the KMP framework:

   ```bash
   cd ~/Dropbox/claudesucks/mitzi    # or ~/Library/CloudStorage/Dropbox/claudesucks/mitzi
   pod install
   ```

   Or use the helper (Gradle + pod install):

   ```bash
   ~/Dropbox/claudesucks/mitzi/scripts/build_shared_framework.sh
   ```

3. Open **`MitzModeTest.xcworkspace`** (not `.xcodeproj`).
4. **Product → Clean Build Folder** (Cmd+Shift+K), then Run.

### Xcode / CocoaPods notes (already in `mitzi/Podfile`)

- Requires **Java 17+** to compile the `shared` Kotlin framework (`brew install openjdk@17`).
- `post_install` patches the KMP build script with `JAVA_HOME` for Xcode GUI builds.
- `ENABLE_USER_SCRIPT_SANDBOXING = NO` — required for CocoaPods script phases.

Alternative: run `extract-to-dropbox.sh` from `ios-transfer-handoff/` to refresh
the whole `~/Dropbox/claudesucks` tree (overwrites local Mac copies of handoff files).

---

## What to verify after rebuilding

| Check | Expected |
|-------|----------|
| **Hebrew date after tzeit** | Next Hebrew day (e.g. 18 Tammuz at 10 PM) while civil date still Thursday |
| **Fast start (erev)** | Tomorrow's dawn (~4:10 AM Jerusalem summer), not afternoon |
| **Fast end** | Clears after tzeit; date advances |
| **GPS timezone (emulator)** | Mountain View → Pacific time, not UTC |
| **Manual Jerusalem** | `Asia/Jerusalem`, correct local clock |
| **Jerusalem Purim** | Shushan Purim on 15 Adar; meshulash three-day schedule when applicable |

---

## Known limitations

| Issue | Status |
|-------|--------|
| iOS GPS stub | `PlatformLocationService.ios.kt` returns `Unavailable` — no CoreLocation yet |
| iOS simulator | No real GPS; set **Features → Location → Custom Location** in Xcode |
| Emulator UTC clock | Fixed for GPS mode via `LocationTimezone`; manual city always worked |
| Android zmanim | KosherJava only — parity with iOS via unit tests, not shared runtime code |

---

## Checklist JSON workflow

1. Edit `be-a-tzaddik/data/*.json` (or human shards compiled by Python tools).
2. Run `be-a-tzaddik/tools/compile_full_bundled.py` (full rebuild + optional handoff sync), **or**
   `ios-transfer-handoff/sync-to-ios-handoff.ps1` only.
3. `verify-handoff.ps1` — catches drift between repo and mirror.

---

## Local Windows environment notes

**`local.properties`** may point at a throwaway SDK (`c:\apps\hehehe\.android-sdk`) on
some accounts. Main dev machine should use:

```
sdk.dir=C\:\\Users\\chill\\AppData\\Local\\Android\\Sdk
```

---

## Related docs (read as needed)

| File | Contents |
|------|----------|
| `AGENTS.md` | Handoff folder map, high-signal files, maintainer sync commands |
| `EMBED.md` | Path-based embed model, zmanim split, what not to do |
| `CHANGELOG.md` | Dated deltas since last sync |
| `docs/PARITY_CHECKLIST.md` | iOS must match Android acceptance tests |
| `docs/SWIFT_KOTLIN_REFERENCE.md` | Embed wiring, `Info.plist`, Swift host |
| `MAC_QUICKSTART.md` | Short Mac onboarding |

---

## Quick reference — key Kotlin files

| File | Why |
|------|-----|
| `domain/LocationTimezone.kt` | GPS timezone from catalog |
| `domain/ManualCities.kt` | City picker + nearest timezone |
| `domain/HalachicDayRollover.kt` | Hebrew day at tzeit |
| `viewmodel/AppViewModel.kt` | GPS refresh, profile, startup reconcile |
| `domain/zmanim/SharedZmanimBuilder.kt` | iOS solar zmanim |
| `iosMain/.../NativeJewishCalendarBackend.kt` | iOS calendar backend |
| `androidMain/.../JewishCalendarBackend.android.kt` | Android KosherJava backend |
| `iosMain/.../MainViewController.kt` | Standalone + embed entry points |
| `composeResources/files/manual-cities.json` | City catalog (~3.9k) |
| `composeResources/files/checklist-items.json` | Mitzvah copy |
