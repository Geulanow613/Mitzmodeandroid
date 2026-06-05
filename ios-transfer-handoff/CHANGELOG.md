# Changelog (iOS handoff sync)

What the mirrored `be-a-tzaddik/` files and `docs/` contain. Last refresh: **2026-06-05** (`sync-to-ios-handoff.ps1`).

## Mitz Mode home (Android — iOS native must match)

- **Background:** Static vertical gradient on all devices — no looping `background.mp4` (`docs/MITZ_MODE_HOME_BACKGROUND.md`)
- **Layout:** Instruction text, Mitzvah Me button, bottom pills — `docs/MITZ_MODE_HOME_LAYOUT.md` (dp values from `MitzModeApp.kt`)
- **Bottom pills:** Daily Mitzvot Checklist stays **full width**; **Add a Mitzvah** and **What's a Mitzvah?** are **content-width**, centered (`wrapContentWidth` / no `fillMaxWidth`)
- **Edge-to-edge:** Controls use status + navigation bar padding (`MainActivity.kt` / `MitzModeApp.kt`)

## Glossary, term matching, and links (KMP)

See **`docs/GLOSSARY_AND_TERM_LINKS.md`**.

- **`HalachicTermsDictionary.kt`:** `isInsideLongerPhrase`, apostrophe boundary fix, many new terms/aliases (Kiddush Levana, Korbanot, yad soledet bo, etc.)
- **`BeginnerHalachaGlossary.kt`:** Rabbi vs rav split; Arba Minim copy no longer aliases lulav/etrog as slash phrase
- **`HalachicTermText.kt`:** `LocalOpenShabbatGuide`; guide routing via `anchorForTerm`
- **`ShabbatGuideData.kt`:** `glossaryOnlyTermIds`, stricter `anchorForTerm`, fixed broken learn-more URLs
- **`TodayScreen.kt`:** Provides `LocalOpenShabbatGuide`; Shabbat guide › uses `enableTerms = false`
- Link audit: 240 URLs in source, 0 broken (excludes mitzvot cloud/list JSON)

## Shabbat rest & upcoming holidays

- **`ShabbatRestScreen.kt`:** Refined holy-day / electronics rest UX
- **`ElectronicsRestEvaluator.kt`**, **`ElectronicsRestPeriod.kt`:** Period evaluation updates
- **`RestMessages.kt`:** Additional rest copy
- **`UpcomingHolidayPlanner.kt`:** Holiday countdown on Today
- **`JewishCalendarService.kt`**, **`SeasonalChecklistItems.kt`**, **`SeasonalMitzvahText.kt`:** Seasonal copy tweaks
- Prep text: **`ErevChagPrepText.kt`**, **`ErevPesachPrepText.kt`**, **`YomTovShabbatPrepText.kt`**

## Embedded Mitz Mode / navigation

- **`BeATzaddikApp.kt`:** Embedded mode, Shabbat guide from Today, return-to-Mitz-Mode flow
- **`AppViewModel.kt`:** Minor state wiring

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
