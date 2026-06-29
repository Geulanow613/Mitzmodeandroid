# iOS handoff — agent instructions (start here)

This folder is a **file bundle for handoff**, not an installation guide. **Preferred integration:** point the iOS/Mac project at the same `be-a-tzaddik/shared` path as Android (see **`EMBED.md`**). Use this mirror when you must copy files; run **`verify-handoff.ps1`** after sync to catch drift.

## What to do (in order)

1. Read **`EMBED.md`** — path-based embed (Android model), zmanim split, test commands.
2. Read **`docs/PARITY_CHECKLIST.md`** — acceptance criteria (must match Android / embedded Mitz Mode).
2. Read **`docs/SOURCE_MAP.md`** — which files matter and where they live in this mirror.
3. Apply code from **`be-a-tzaddik/`** into the Mac iOS workspace (same relative paths under `be-a-tzaddik/` or your repo’s `be-a-tzaddik/`).
4. For **Mitz Mode embed**, read **`docs/SWIFT_KOTLIN_REFERENCE.md`** — `EmbeddedChecklistViewController`, Swift host, `Info.plist` keys.
5. For **Mitz Mode native home UI**, read **`docs/MITZ_MODE_HOME_BACKGROUND.md`** (gradient) and **`docs/MITZ_MODE_HOME_LAYOUT.md`** (button/text positions).
6. For **glossary / Shabbat Guide term taps**, read **`docs/GLOSSARY_AND_TERM_LINKS.md`**.
7. For **Official App Song + G.E.U.L.A streaming links**, read **`docs/MITZ_MODE_OFFICIAL_APP_SONG.md`**.
8. For **Android behavior reference** (outside this folder), see **`docs/ANDROID_EMBED_REFERENCE.md`**.
9. Check **`CHANGELOG.md`** for the latest copy/zman/halacha deltas since last handoff.

## Folder layout

| Path | Purpose |
|------|---------|
| **`be-a-tzaddik/shared/`** | KMP shared module — all UI, domain, checklist JSON, iosMain calendar |
| **`be-a-tzaddik/iosApp/`** | Standalone iOS app shell (Swift + Podfile) |
| **`be-a-tzaddik/data/`** | Optional duplicate of checklist JSON (source of truth is `shared/.../composeResources/files/`) |
| **`docs/`** | Parity checklist, source map, Swift/Kotlin snippets, Mitz home background, Android embed pointer |
| **`CHANGELOG.md`** | What changed in the last sync |
| **`EMBED.md`** | Path-based integration (preferred), zmanim parity, test workflow |
| **`sync-to-ios-handoff.ps1`** | Refreshes this mirror from repo `be-a-tzaddik/` (Windows) |
| **`verify-handoff.ps1`** | Fails if mirror ≠ source (run after sync) |

**Not included on purpose:** `androidApp/`, `tools/`, build caches — Android-only or maintainer scripts.

## Two integration targets

### A) Standalone “Be a Tzaddik” iOS app

- Use files under `be-a-tzaddik/iosApp/` + `be-a-tzaddik/shared/`.
- Entry: `shared/src/iosMain/.../MainViewController.kt` → `MainViewController()`.

### B) Embedded in Mitz Mode iOS (Daily Mitzvot Checklist)

- Same `shared/` module; host app presents Kotlin UI full-screen.
- Entry: `EmbeddedChecklistViewController(onClose:)` in the same `MainViewController.kt`.
- UX: title **the Daily Mitzvot Checklist**, bottom nav **Today | Timer | Mitz logo | Settings | About** — see parity doc.

## High-signal files (recent work)

| File | Why |
|------|-----|
| `shared/.../composeResources/files/manual-cities.json` | ~550 manual cities for Settings/Onboarding |
| `shared/.../domain/AppDisclaimer.kt` | Startup + About disclaimer (“all mitzvot” wording) |
| `shared/.../ui/components/AboutContent.kt` | About tab body |
| `shared/.../data/ManualCitiesLoader.kt` | Loads city catalog at app start |
| `shared/.../composeResources/files/checklist-items.json` | Mitzvah copy, women’s `explanationFemale`, halacha wording |
| `shared/.../domain/ChecklistZmanEvaluator.kt` | Tashlumin hints, Mincha until nightfall |
| `shared/.../domain/SeasonalChecklistItems.kt` | Holidays, Chol HaMoed, Arba Minim notes |
| `shared/.../domain/HalachicTermsDictionary.kt` | Glossary matcher, phrase overlap, apostrophe rules |
| `shared/.../ui/components/HalachicTermText.kt` | Term popups, `LocalOpenShabbatGuide` routing |
| `shared/.../ui/screens/ShabbatGuideData.kt` | Guide text, anchors, `glossaryOnlyTermIds`, learn-more URLs |
| `shared/.../ui/screens/TodayScreen.kt` | Today + Shabbat guide entry, upcoming holidays |
| `shared/.../ui/screens/ShabbatRestScreen.kt` | Holy-day / electronics rest screen |
| `shared/.../domain/ElectronicsRestEvaluator.kt` | When device rest applies |
| `shared/.../ui/BeATzaddikApp.kt` | Tabs, embedded mode, About |
| `shared/.../ui/screens/AboutScreen.kt` | About tab |
| `shared/.../domain/HolyDayPhoneRules.kt` | Shabbat / chutz 2nd-day Yom Tov |
| `shared/.../domain/ZmanPeriodLogic.kt` | Evening period boundaries |
| `shared/iosMain/.../NativeJewishCalendarBackend.kt` | iOS calendar / seasons / zmanim (`SharedZmanimBuilder`) |
| `shared/.../domain/zmanim/SharedZmanimBuilder.kt` | KMP solar zmanim (iOS + tests; Android still KosherJava) |

## Checklist text on iOS

All mitzvah titles and explanations ship from the **shared KMP module** (not separate Swift strings):

| Source | What it contains |
|--------|------------------|
| `shared/.../composeResources/files/checklist-items.json` | Main checklist: `explanation`, `explanationFemale`, `explanationAshkenaz` / `Sefard` / `Chabad`, `links` |
| `shared/.../composeResources/files/nusach-extras.json` | Extra nusach-specific copy |
| `shared/.../composeResources/files/holidays-overlay.json` | Upcoming holiday metadata |
| `shared/.../composeResources/files/manual-cities.json` | City names (settings/onboarding) |
| `shared/.../domain/SeasonalChecklistItems.kt` | Holiday / seasonal items and explanations (Kotlin) |
| `shared/.../ui/screens/ShabbatGuideData.kt` | Shabbat & Yom Tov guide text (Kotlin) |
| `shared/.../domain/AppDisclaimer.kt` | First-launch disclaimer (Kotlin) |

Edit `be-a-tzaddik/data/checklist-items.json` (and other `data/*.json`), then run **`sync-to-ios-handoff.ps1`** — it copies `data/` into `composeResources/files/` before mirroring to this folder.

## Refresh this bundle (maintainer, Windows)

```powershell
cd c:\apps\hehehe\ios-transfer-handoff
.\sync-to-ios-handoff.ps1
.\verify-handoff.ps1
```

Before handoff, run zmanim parity tests from repo root:

```powershell
cd c:\apps\hehehe
.\gradlew :beatzaddik-shared:testDebugUnitTest
```

Then re-copy `ios-transfer-handoff/` to the Mac or re-open it in the agent session.

## Success criteria

All items in **`docs/PARITY_CHECKLIST.md`** pass on iOS, using the **files in this folder** as the expected source — no drift from an old `be-a-tzaddik` copy on the Mac.
