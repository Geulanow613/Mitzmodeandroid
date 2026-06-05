# Changelog (iOS handoff sync)

What the mirrored `be-a-tzaddik/` files and `docs/` contain. Last refresh: **2026-06-04** (`sync-to-ios-handoff.ps1`).

## Mitz Mode home (Android — iOS native must match)

- **Background:** Static vertical gradient on all devices — no looping `background.mp4` (`docs/MITZ_MODE_HOME_BACKGROUND.md`)
- **Layout:** Instruction text, Mitzvah Me button, bottom pills — `docs/MITZ_MODE_HOME_LAYOUT.md` (dp values from `MitzModeApp.kt`)
- **Edge-to-edge:** Controls use status + navigation bar padding (`MainActivity.kt` / `MitzModeApp.kt`)

## Disclaimer & About (KMP — copy into iOS shared module)

- **`AppDisclaimer.kt`:** “does not contain **all** the mitzvot in the entire Torah”
- **`AboutContent.kt`:** About tab uses `STARTUP_BODY` (same as first-launch dialog); no duplicate About block in Settings
- **`AboutScreen.kt`:** Dedicated About tab (embedded + standalone nav)

## Manual cities (~550)

- **`composeResources/files/manual-cities.json`** — bundled catalog (source of truth)
- **`data/manual-cities.json`** — duplicate for tooling/diff
- **`ManualCities.kt`**, **`ManualCitiesCatalog.kt`**, **`ManualCitiesLoader.kt`**, **`ManualCitiesAssets.kt`**
- **`CitySelector.kt`**, **`SettingsScreen.kt`**, **`OnboardingScreen.kt`:** GPS toggle, searchable A→Z list, `ManualCityListRow`, legacy ID aliases (`jlm`→Jerusalem, `nyc`→`new_york`, etc.)
- Regenerate JSON from `be-a-tzaddik/tools/manual-cities.tsv` via `gen_manual_cities.py` (tools folder not mirrored)

## Netilat Yadayim (nusach)

- **`ritual_hand_washing`** — separate `explanationAshkenaz` / `explanationSefard` / `explanationChabad` for when to say Al Netilat Yadayim (bedside vs second washing, Sefard first-washing, Chabad two-step sequence); consensus: no repeat at shul

## Halachic copy & zman hints

- **Tashlumin** — Maariv makeup: only one missed Amidah at Maariv (`ChecklistZmanEvaluator.kt`)
- **Mincha window** — active until nightfall (tzeit), not sunset
- **checklist-items.json** — tap/ntila, tzitzit 613 drash, Kiddush Levana window, Maariv→siddur, women Hamapil/Birchot HaShachar
- **SeasonalChecklistItems.kt** — Chol HaMoed wine optional; Arba Minim nusach notes
- **ShabbatGuideData.kt** — Sukkot / Shemini Atzeret / Hoshana Raba / Yizkor nusach differences

## Navigation & screens

- **About tab** — 5th bottom-nav item; removed from Settings
- **Embedded nav:** Today | Timer | **Mitz logo** | Settings | About
- **Standalone nav:** Today | Timer | Settings | About

## Today / zmanim / calendar

- Evening mitzvot graying fix (`ZmanPeriodLogic`)
- Holy-day phone rules (`HolyDayPhoneRules.kt`)
- Clickable Current Period → scroll (`ChecklistPeriodScroll.kt`)
- Seasonal checklist items + `NativeJewishCalendarBackend.kt`

## Settings / location

- City selector A→Z parity (`CitySelector.kt`)
- `UserProfile.isInIsrael` from GPS, manual city, or toggle

## iOS entry

- `MainViewController()` standalone
- `EmbeddedChecklistViewController(onClose)` for Mitz Mode
