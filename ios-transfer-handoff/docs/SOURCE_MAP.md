# Source map — files to copy or diff

All paths are relative to **`be-a-tzaddik/`** inside this handoff folder (or the same paths in the main repo).

## Mirror layout (this handoff)

After `sync-to-ios-handoff.ps1`:

- `ios-transfer-handoff/be-a-tzaddik/shared/` — KMP module (required)
- `ios-transfer-handoff/be-a-tzaddik/iosApp/` — standalone iOS shell
- `ios-transfer-handoff/be-a-tzaddik/data/` — optional JSON duplicate

**Excluded from mirror:** `androidApp/`, `tools/`

## Shared Kotlin

| Area | Path |
|------|------|
| All UI + domain | `shared/src/commonMain/kotlin/com/beardytop/beatzaddik/` |
| Android-specific (KMP) | `shared/src/androidMain/kotlin/com/beardytop/beatzaddik/` |
| iOS calendar + entry | `shared/src/iosMain/kotlin/com/beardytop/beatzaddik/` |

## Checklist + resources (copy verbatim)

- `shared/src/commonMain/composeResources/files/checklist-items.json`
- `shared/src/commonMain/composeResources/files/manual-cities.json` (~550 cities)
- `shared/src/commonMain/composeResources/files/nusach-extras.json`
- `shared/src/commonMain/composeResources/files/holidays-overlay.json`
- `shared/src/commonMain/composeResources/drawable/` (if branding assets changed)
- `data/manual-cities.json` — optional duplicate (same as composeResources)

## High-signal Kotlin files

| Relative path | Role |
|---------------|------|
| `ui/BeATzaddikApp.kt` | Tabs, embedded mode, About |
| `ui/screens/AboutScreen.kt` | About tab |
| `ui/screens/TodayScreen.kt` | Period scroll, holy-day card |
| `ui/screens/SettingsScreen.kt` | GPS toggle, manual city list; no About card |
| `ui/screens/OnboardingScreen.kt` | 2-step onboarding + city on location page |
| `ui/CitySelector.kt` | City search + A→Z |
| `domain/AppDisclaimer.kt` | Startup + About disclaimer copy |
| `ui/components/AboutContent.kt` | About tab body (`STARTUP_BODY`) |
| `domain/ManualCities.kt` | City IDs, Israel bounds, legacy aliases |
| `data/ManualCitiesLoader.kt` | Loads `manual-cities.json` at startup |
| `data/ManualCitiesCatalog.kt` | In-memory city catalog |
| `data/ManualCitiesAssets.kt` | Resource path for JSON |
| `AppDependencies.kt` | Wires manual cities into deps |
| `ui/ChecklistPeriodScroll.kt` | Tap period → scroll |
| `domain/ChecklistZmanEvaluator.kt` | Zman windows + makeup hints |
| `domain/ZmanPeriodLogic.kt` | Day / afternoon / evening |
| `domain/HolyDayPhoneRules.kt` | Shabbat / chutz 2nd Yom Tov |
| `domain/SeasonalChecklistItems.kt` | Holiday checklist items |
| `domain/HalachicTermsDictionary.kt` | Glossary + matcher |
| `domain/BeginnerHalachaGlossary.kt` | Beginner terms merged into dictionary |
| `ui/components/HalachicTermText.kt` | Underlined terms, guide routing |
| `ui/screens/ShabbatGuideData.kt` | Guide copy, anchors, URLs |
| `ui/screens/ShabbatRestScreen.kt` | Shabbat / Yom Tov rest UX |
| `domain/ElectronicsRestEvaluator.kt` | Electronics rest periods |
| `domain/UpcomingHolidayPlanner.kt` | Upcoming holiday block on Today |
| `iosMain/.../MainViewController.kt` | Standalone + embedded VC |
| `iosMain/.../NativeJewishCalendarBackend.kt` | iOS calendar |

## iOS app shell

- `iosApp/iosApp/` — Swift sources, assets, `Info.plist`
- `iosApp/Podfile` — only if host project already uses CocoaPods for `shared`

## Mitz Mode native home (Android repo — not in KMP mirror)

| Doc | Role |
|-----|------|
| `docs/MITZ_MODE_HOME_BACKGROUND.md` | Static gradient spec + Swift snippets for iOS home |
| `docs/MITZ_MODE_HOME_LAYOUT.md` | Instruction text, Mitzvah Me button, bottom pill positions (dp) |
| `docs/GLOSSARY_AND_TERM_LINKS.md` | Term matching, Shabbat Guide taps, link audit |

| Android path | Role |
|--------------|------|
| `app/.../ui/components/LowEndDeviceBackground.kt` | Gradient composable (all devices) |
| `app/.../ui/MitzModeApp.kt` | Home layout, static gradient, bottom pills |
| `app/.../MainActivity.kt` | `enableEdgeToEdge()`, safe-area insets |

## Android embed reference (not in this mirror)

Compare behavior with repo paths:

- `app/src/main/java/com/beardytop/mitzmode/tzaddik/TzaddikIntegration.kt`
- `app/src/main/java/com/beardytop/mitzmode/ui/MitzModeApp.kt`

See `ANDROID_EMBED_REFERENCE.md`.
