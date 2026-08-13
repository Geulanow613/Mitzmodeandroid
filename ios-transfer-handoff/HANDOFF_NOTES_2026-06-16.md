# iOS / Mitz Mode handoff — June 16, 2026

This bundle is ready to copy to the Mac iOS / Mitz Mode workspace. It mirrors **`c:\apps\hehehe\be-a-tzaddik\`** (KMP shared module + iosApp shell). Android embed behavior is documented in `docs/ANDROID_EMBED_REFERENCE.md`.

**Last sync:** run `sync-to-ios-handoff.ps1` on Windows before transfer (copies `data/*.json` → `composeResources/files/`, then mirrors all of `be-a-tzaddik/` except `androidApp/` and `tools/`).

---

## What to transfer

Copy the **entire folder**:

```
c:\apps\hehehe\ios-transfer-handoff\
```

Into the iOS repo (or open it directly on the Mac). The KMP module lives at:

```
ios-transfer-handoff/be-a-tzaddik/shared/
```

Replace the host project’s existing `be-a-tzaddik/shared/` (same relative paths).

**Do not copy:** `be-a-tzaddik/androidApp/`, `be-a-tzaddik/tools/` (Android-only / maintainer scripts).

---

## Integration target: Mitz Mode iOS (embedded checklist)

Same pattern as Android `TzaddikIntegration.kt`:

| Step | Doc / file |
|------|------------|
| 1 | Read `AGENTS.md` (start here) |
| 2 | Read `docs/PARITY_CHECKLIST.md` — acceptance tests |
| 3 | Read `docs/SWIFT_KOTLIN_REFERENCE.md` — Swift host + `EmbeddedChecklistViewController` |
| 4 | Wire Swift host to `MainViewController.kt` → `EmbeddedChecklistViewController(onClose:)` |
| 5 | Rebuild KMP `shared` framework in Xcode / Gradle (whatever the host already uses) |

**Kotlin entry (iosMain):**

`be-a-tzaddik/shared/src/iosMain/kotlin/com/beardytop/beatzaddik/MainViewController.kt`

**Embedded UX (commonMain):**

`be-a-tzaddik/shared/src/commonMain/kotlin/com/beardytop/beatzaddik/ui/BeATzaddikApp.kt` — `embeddedMode = true`, title **the Daily Mitzvot Checklist**, bottom nav: Today | Timer | Mitz logo | Settings | About.

---

## Changes in this batch (shared KMP — applies to iOS + Android)

All of these are in **`commonMain`** unless noted.

### Upcoming & seasonal timing
| File | What |
|------|------|
| `domain/CandleLightingRules.kt` | **New** — 18 min default, 40 min Jerusalem |
| `domain/CivilWeek.kt` | **New** — show zman subtitle only in current Sun–Sat week |
| `domain/UpcomingHolidayTiming.kt` | **New** — inline labels (`Candles 7:13pm Fri`, `4:10am Thu` for fasts, tzeit for Purim/RC, etc.) |
| `domain/UpcomingHolidayPlanner.kt` | Wires timing hints |
| `domain/PublicFastDayText.kt` | Yom Kippur erev/day copy; fast start times |
| `domain/ZmanimFormatter.kt` | `formatTimeInline`, `formatWeekdayShort`, `formatMonthDayTime` |
| `domain/Models.kt` | `UpcomingHoliday.timingHint` |
| `ui/screens/TodayScreen.kt` | Single-line upcoming rows (baseline-aligned); timing inline |

### Shabbat / holy-day rest
| File | What |
|------|------|
| `domain/ElectronicsRestEvaluator.kt` | Shabbat ends at **tzeit**; Yom Tov sequences |
| `ui/screens/ShabbatRestScreen.kt` | End time uses **profile timezone** (not device); says “after tzeit” |
| `ui/BeATzaddikApp.kt` | Passes `timezoneId` to rest screen; onboarding flash fix (`prefsLoaded`) |
| `viewmodel/AppViewModel.kt` | `prefsLoaded` exposed; tzeit-day check storage |

### Parsha header
| File | What |
|------|------|
| `domain/ChecklistEngine.kt` | Header uses `upcomingShabbatParsha`; Motzei Shabbat → next week |
| `domain/ParshaData.kt` | `displayLabel()` |

### Onboarding
| File | What |
|------|------|
| `viewmodel/AppViewModel.kt` | `prefsLoaded` — no flash of onboarding when already complete |
| `ui/BeATzaddikApp.kt` | Hold parchment background until prefs loaded |

### Women’s daily prayer
| File | What |
|------|------|
| `domain/TzeitDay.kt` | **New** — halachic day key (tzeit → tzeit) |
| `domain/ChecklistZmanEvaluator.kt` | Available all day/night (not Shacharit window) |
| `domain/ChecklistEngine.kt` | Checkbox resets on tzeit day boundary |
| `data/AppRepository.kt` | `tzeitCheckedDays` / `setTzeitDayChecked` |
| `iosMain/.../JsonFileAppRepository.kt` | iOS persistence for tzeit-day checks |
| `viewmodel/AppViewModel.kt` | `setTzeitDayChecked()` |
| `ui/screens/TodayScreen.kt` | Routes check to tzeit-day storage |

### Copy / seasonal / debug
| File | What |
|------|------|
| `domain/ErevChagPrepText.kt`, `SeasonalMitzvahText.kt` | Shavuot copy (no Omer/temimot wording) |
| `domain/SeasonalMitzvahText.kt` | Birkat Ha'Ilanot melav'lave |
| `domain/BirkatHachamahRules.kt`, `ChecklistDebugScenarios.kt` | Debug menu dates beyond spiral limit |
| `domain/BeginnerHalachaGlossary.kt`, `HalachicTermsDictionary.kt` | Edot HaMizrach, term updates |
| `domain/SeasonalChecklistItems.kt`, `ChecklistGenderRules.kt`, etc. | Edot HaMizrach nusach wiring |

### Bundled JSON (ship with app)
| File | What |
|------|------|
| `shared/src/commonMain/composeResources/files/checklist-items.json` | **Source of truth** for mitzvah copy |
| `data/checklist-items.json` | Editable duplicate (sync script copies → composeResources) |
| `composeResources/files/nusach-extras.json` | Nusach-specific items |
| `composeResources/files/manual-cities.json` | ~550 cities |
| `composeResources/files/holidays-overlay.json` | Upcoming overlay |

---

## Platform-specific notes

### Android (already integrated in Mitz Mode)
- Zmanim: **KosherJava** (`JewishCalendarBackend.android.kt`) — precise location-based times.
- Embed: `app/.../tzaddik/TzaddikIntegration.kt`

### iOS (this handoff)
- **All UI, checklist logic, copy, and most behavior** come from the same **`commonMain`** code as Android — no separate Swift port needed for the checklist itself.
- **Calendar / zmanim backend:** `iosMain/.../NativeJewishCalendarBackend.kt` uses `HeuristicZmanim` (lat/lon estimation), not KosherJava. Times are approximate but the **same rules** (tzeit Shabbat end, tzeit-day prayer reset, upcoming timing labels) apply once zmanim exist.
- **Persistence:** `iosMain/.../JsonFileAppRepository.kt` — includes new tzeit-day check keys (same API as Android DataStore).
- **Standalone vs embed:** Use `EmbeddedChecklistViewController` for Mitz Mode; `MainViewController()` for standalone Be a Tzaddik app.
- **Native Mitz Mode home** (gradient, pills, Official App Song): **not** in this KMP module — see `docs/MITZ_MODE_HOME_BACKGROUND.md`, `MITZ_MODE_HOME_LAYOUT.md`, `MITZ_MODE_OFFICIAL_APP_SONG.md` for Swift shell parity with Android.

### Known iOS parity gap (pre-existing)
`docs/PARITY_CHECKLIST.md` notes iOS `NativeJewishCalendarBackend` parsha on `DayInfo.parsha` is only populated on Shabbat; the **header fix** uses `upcomingShabbatParsha` in `ChecklistEngine` (commonMain), so the Today header should still show the correct parsha all week.

---

## After copying to Mac

1. Replace `be-a-tzaddik/shared/` in the iOS repo with this mirror’s `shared/`.
2. Rebuild the KMP framework (Gradle task the project already uses, e.g. `./gradlew :shared:embedAndSignAppleFrameworkForXcode` or CocoaPods sync).
3. Run through `docs/PARITY_CHECKLIST.md` on device/simulator with a manual city (e.g. Jerusalem, San Diego).
4. Test embedded flow: open Daily Mitzvot Checklist from Mitz Mode → return via center logo.
5. Test Shabbat rest screen times match **settings location timezone**, not device travel timezone.

---

## Maintainer: refresh before next handoff

```powershell
cd c:\apps\hehehe\ios-transfer-handoff
.\sync-to-ios-handoff.ps1
```

Then copy `ios-transfer-handoff/` to the Mac or commit the folder.

---

## Doc index (in this folder)

| Path | Purpose |
|------|---------|
| `AGENTS.md` | Agent start instructions |
| `CHANGELOG.md` | Historical sync notes (update after each batch) |
| `docs/SOURCE_MAP.md` | File map |
| `docs/PARITY_CHECKLIST.md` | QA checklist |
| `docs/SWIFT_KOTLIN_REFERENCE.md` | Mitz Mode embed snippets |
| `docs/ANDROID_EMBED_REFERENCE.md` | Android parity reference (main repo) |
| `mitz-mode-ios-integration-kit/` | Extra integration templates |
