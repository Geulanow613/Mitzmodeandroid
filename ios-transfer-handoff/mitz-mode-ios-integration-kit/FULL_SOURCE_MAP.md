# Full Source Map (Copy Checklist)

Use this map to ensure all required code/data/settings/docs are available on Mac and nothing is missed.

## A) Mirrored copy in this handoff (easiest)

After running `sync-to-ios-handoff.ps1`, the full app lives at:

- `ios-transfer-handoff/be-a-tzaddik/`

Same layout as repo `be-a-tzaddik/`.

## B) Or copy from repo root (required if not using mirror)

- `be-a-tzaddik/`

## C) Critical subpaths (verification)

### Shared app logic + UI + settings

- `shared/src/commonMain/kotlin/com/beardytop/beatzaddik/`
- `shared/src/androidMain/kotlin/com/beardytop/beatzaddik/`
- `shared/src/iosMain/kotlin/com/beardytop/beatzaddik/`

### New / updated files (June 2026)

| Path | Notes |
|------|--------|
| `composeResources/files/manual-cities.json` | ~550 manual cities |
| `data/ManualCitiesLoader.kt` | Loads city catalog |
| `domain/AppDisclaimer.kt` | “all mitzvot” disclaimer wording |
| `ui/components/AboutContent.kt` | About = `STARTUP_BODY` |
| `ui/screens/AboutScreen.kt` | About tab |
| `ui/CitySelector.kt` | City search + A→Z sort |
| `ui/ChecklistPeriodScroll.kt` | Tap period → scroll to section |
| `domain/HolyDayPhoneRules.kt` | Shabbat + chutz 2nd-day Yom Tov notice |
| `domain/ZmanPeriodLogic.kt` | Evening window fix (`effectiveEveningEnd`) |
| `domain/SeasonalChecklistItems.kt` | Seasonal / erev / chol hamoed items |
| `ui/BeATzaddikApp.kt` | 5-tab nav, About, center Mitz logo |
| `ui/screens/TodayScreen.kt` | Period scroll, holy-day card |
| `ui/screens/SettingsScreen.kt` | City list parity; About removed |
| `iosMain/.../MainViewController.kt` | `EmbeddedChecklistViewController` |
| `iosMain/.../NativeJewishCalendarBackend.kt` | Calendar + Jerusalem Purim |

### Explainers + checklist data

- `shared/src/commonMain/composeResources/files/checklist-items.json`
- `shared/src/commonMain/composeResources/files/nusach-extras.json`
- `shared/src/commonMain/composeResources/files/holidays-overlay.json`
- `composeResources/files/manual-cities.json`
- `domain/ManualCities.kt`, `domain/AppDisclaimer.kt`

### iOS app wrapper

- `iosApp/iosApp/`
- `iosApp/Podfile`
- `iosApp/README.md`

### Docs

- `README.md`, `docs/`
- `ios-transfer-handoff/CHANGELOG.md`
- `docs/MITZ_MODE_HOME_BACKGROUND.md`, `docs/MITZ_MODE_HOME_LAYOUT.md`

## D) Android integration reference (Mitz Mode embed + home)

- `app/src/main/java/com/beardytop/mitzmode/tzaddik/TzaddikIntegration.kt`
- `app/.../ui/MitzModeApp.kt` — home layout + static gradient
- `app/.../ui/components/LowEndDeviceBackground.kt` — gradient (`docs/MITZ_MODE_HOME_BACKGROUND.md`)
- `docs/MITZ_MODE_HOME_LAYOUT.md` — instruction/button/pill positions
- `shared/.../ui/BeATzaddikApp.kt`

## E) One-shot zip (repo root)

```powershell
Compress-Archive -Path .\be-a-tzaddik, .\ios-transfer-handoff -DestinationPath .\ios-full-transfer.zip -Force
```
