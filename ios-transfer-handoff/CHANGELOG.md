## 2026-07-13 (d) — Fix Hebrew mojibake + Keep Kosher explainer

- Restored proper UTF-8 Hebrew in `checklist-items.json` (was showing as `×›Ö¸…` garbage)
- Rewrote Keep Kosher explainer: meat/milk, shechita, insect checking, Israel tithes
- Strengthened `TextEncodingFixes` to recover double-encoded Hebrew at load time
# Changelog (iOS handoff sync)

What the mirrored `be-a-tzaddik/` files and `docs/` contain.

## 2026-07-13 (c) â€” Machatzit check persists Fast â†’ Purim

- Zecher LeMachatzit HaShekel uses `persistChecked` with a Hebrew-year id so a check on Fast of Esther stays checked on Purim (resets next year)

## 2026-07-13 (b) â€” Zecher LeMachatzit HaShekel custom

- New Seasonal checklist item **Zecher LeMachatzit HaShekel (custom)** on Fast of Esther (Mincha) and Purim / Meshulash Friday (with note if not given on the fast)
- Pinned near the top of Today when it appears

## 2026-07-13 â€” Tachanun Mincha erevs, glossary links, store trim (v5.6.0)

- **`TachanunRules.kt`** â€” omit Mincha Tachanun on erev Shabbat, erev chagim, Purim, Chanukah, Tisha Bâ€™Av, Rosh Chodesh, and minor festive eves; Erev RH / Erev YK still say Mincha (Shacharit-only omission)
- Glossary / mitzvah-card term linking: first-occurrence only; resource `knownLinks` no longer inlined into body
- Upcoming holidays: Fast of Esther / Purim naming + within-day sort
- Store assets: unused promo videos / song moved off release packaging (`src/debug` / UnusedMedia); version **56 / 5.6.0**
- Offline bundled translations disabled for shipping English-first build

## 2026-07-06 (b) â€” GPS timezone fix + world city catalog (~3,900 cities)

### GPS clock / zmanim timezone (Android + iOS KMP)
- **`domain/LocationTimezone.kt`** (new) â€” resolves IANA timezone from GPS lat/lng using the
  bundled city catalog (timezone id only; zmanim still use exact GPS coordinates)
- **`domain/ManualCities.kt`** â€” `nearestTimezoneId()` for catalog lookup (no distance cap)
- **`viewmodel/AppViewModel.kt`** â€” reconcile timezone on startup, GPS toggle, GPS success,
  and when GPS fix is pending but coordinates are already saved
- **`PlatformLocationService.android.kt`** â€” no longer uses emulator/device UTC as the
  primary timezone when catalog match exists
- **`androidUnitTest/.../LocationTimezoneTest.kt`** â€” Mountain View â†’ `America/Los_Angeles`,
  Jerusalem â†’ `Asia/Jerusalem`

Fixes: Android emulator GPS at Mountain View showing **2:42 PM UTC** instead of **7:42 AM Pacific**.

### City catalog expansion
- **`tools/manual_cities.tsv`** â€” 550 â†’ **3,926** cities (GeoNames, pop â‰¥ 100k)
- **`composeResources/files/manual-cities.json`** â€” same (~700 KB bundled)
- New maintainer scripts: `expand_manual_cities_from_geonames.py`,
  `backfill_city_elevations_from_geonames.py`; `add_city_elevations.py` rate-limit safe

### Mac / Xcode (Mitz Mode at `~/Dropbox/claudesucks/mitzi`)
Podfile points at `../be-a-tzaddik/shared`. After Dropbox sync from Windows:
```bash
cd ~/Dropbox/claudesucks/mitzi   # or Library/CloudStorage/Dropbox/...
pod install
# Clean build folder in Xcode (Cmd+Shift+K), then Run
```
Or: `scripts/build_shared_framework.sh` (Gradle + pod install).

## 2026-07-06 â€” Hebrew date rollover at tzeit + fast time fixes

### Halachic day boundary (both platforms, KMP)
- **`domain/HalachicDayRollover.kt`** (new) â€” Hebrew day advances at **tzeit**, not civil midnight
- **`JewishCalendarBackend.android.kt`** + **`iosMain/.../NativeJewishCalendarBackend.kt`** â€” after tonight's tzeit,
  `dayInfoAt` returns the next Hebrew day (date label, fast/holiday flags, seasons, zmanim);
  civil label keeps the real Gregorian date; new `DayInfo.startedTonightAtTzeit` flag
- Fixes: "still 17 Tammuz at 10 PM after the fast ended"; fast now shows
  "begins at dawn (~4:10 AM)" on the eve after nightfall
- **`ChecklistZmanEvaluator`** â€” Yom Kippur / Tisha B'Av night portion is ACTIVE from the previous sunset
- **`OmerCountText`** â€” tonight's count is rollover-aware (no off-by-one at night)
- **`PublicFastDayText`** â€” erev prep uses **tomorrow's** dawn for the fast start time

### iOS build fixes (Kotlin/Native)
- **`SolarZmanim.kt`**, **`LocationElevation.kt`** â€” removed JVM-only `Math.toDegrees`/`Math.toRadians`
  (broke the iOS framework compile; likely cause of stale zmanim behavior on device)

### Tests (androidUnitTest)
- **`HebrewDateTzeitRolloverTest`** â€” 17â†’18 Tammuz at tzeit, erev-fast night, Shabbat entry/exit, no-location fallback
- **`zmanim/FastDayZmanimSanityTest`** â€” Jerusalem 17 Tammuz 5786: fast starts 3â€“5 AM, ends in the evening

## 2026-07-02 â€” Full app mirror + Mac Dropbox extract

### New
- **`android-mitzmode/`** â€” Android Studio project (`app/` + Gradle); reward `.mp4` files excluded (`EXCLUDED_MEDIA.md`)
- **`extract-to-dropbox.command`** + **`extract-to-dropbox.sh`** â€” Mac extract to `~/Dropbox/claudesucks` (or CloudStorage Dropbox path)
- **`SYNC_MANIFEST.json`** â€” written on each sync (timestamp, file counts)

### swift-native explainers (expanded)
- **`Explainers/checklist_explainers.json`**, **`public_fast_explainers.json`**, **`erev_chag_explainers.json`**, **`zman_and_upcoming_strings.json`**

### Mourning / fast updates (KMP)
- Three Weeks / Nine Days active day+night through chatzos 10 Av
- Tisha B'Av fast zman window + greeting wording fixes
- All bundled translations (he/es/fr/ru) synced

### Docs
- **`MAC_QUICKSTART.md`** â€” Dropbox extract workflow
- **`docs/MITZ_MODE_HOME_BACKGROUND.md`** â€” static gradient spec (`#050B1F` â†’ `#0A0420`)

## 2026-06-30 â€” Full translation + swift-native export

### swift-native/ (new)
- **`BundledTranslations/{he,es,fr,ru}.json`** â€” complete shipped bundles (~2752 keys)
- **`Mitzvot/mitzvotcloud.json`** + **`mitzvotlistfull.json`** â€” Mitzvah Me lists (323 texts, all translated)
- **`Checklist/*`** â€” rebuilt checklist-items, nusach-extras, holidays-overlay, manual-cities
- **`Cities/city-geography.json`** â€” localized city/country names
- **`Explainers/*`** â€” template args + seasonal fragments for dynamic copy
- **`Rewards/*`** â€” tier thresholds + reward level display names
- **`MANIFEST.json`** â€” file hashes + coverage stats

### Automation
- **`be-a-tzaddik/tools/_export_ios_handoff.py`** â€” builds swift-native; syncs data/ â†’ composeResources
- **`compile_full_bundled.py`** â€” auto-runs `sync-to-ios-handoff.ps1` (set `SKIP_IOS_HANDOFF=1` to disable)
- **`sync-to-ios-handoff.ps1`** â€” calls export script before robocopy
- **`verify-handoff.ps1`** â€” also checks swift-native translation parity
- **`MAC_QUICKSTART.md`**, **`docs/SWIFT_NATIVE_TRANSLATIONS.md`**

## 2026-06-16 (b) â€” Shared zmanim, embed docs, verify script

### iOS zmanim parity (Android unchanged)
- **`domain/zmanim/SolarZmanim.kt`** (new) â€” KosherJava-compatible NOAA port in `commonMain`
- **`domain/zmanim/SharedZmanimBuilder.kt`** (new) â€” location zmanim for iOS; heuristic fallback without GPS
- **`iosMain/.../NativeJewishCalendarBackend.kt`** â€” uses `SharedZmanimBuilder`; Shabbat/Yom Tov rest ends at real **tzeit**
- **`SharedZmanimParityTest`** (androidUnitTest) â€” golden check vs KosherJava (~90s tolerance)
- Android production still uses **`JewishCalendarBackend.android.kt` + KosherJava** â€” not switched

### Handoff tooling
- **`EMBED.md`** â€” path-based integration (like Android `settings.gradle`)
- **`verify-handoff.ps1`** â€” fails if mirror â‰  `be-a-tzaddik/`
- **`AGENTS.md`** â€” updated workflow (embed first, verify after sync)

---

## 2026-06-16 â€” Upcoming timing, Shabbat rest, parsha, onboarding, womenâ€™s prayer

See **`HANDOFF_NOTES_2026-06-16.md`** for full file list and transfer steps.

### Upcoming & seasonal (Today screen)
- **`CandleLightingRules.kt`**, **`CivilWeek.kt`**, **`UpcomingHolidayTiming.kt`** (new)
- **`UpcomingHolidayPlanner.kt`**, **`ZmanimFormatter.kt`**, **`Models.kt`**
- **`TodayScreen.kt`** â€” inline timing on same line; baseline-aligned smaller text
- **`PublicFastDayText.kt`** â€” fast/YK timing copy

### Shabbat / electronics rest
- **`ShabbatRestScreen.kt`** â€” end time in **location timezone**; â€œafter tzeit (nightfall)â€
- **`BeATzaddikApp.kt`**, **`AppViewModel.kt`** â€” onboarding no longer flashes when complete (`prefsLoaded`)

### Parsha header
- **`ChecklistEngine.kt`**, **`ParshaData.kt`** â€” upcoming Shabbat parsha; Motzei â†’ next week

### Womenâ€™s daily prayer (tzeit â†’ tzeit)
- **`TzeitDay.kt`** (new), **`ChecklistZmanEvaluator.kt`**, **`ChecklistEngine.kt`**
- **`AppRepository.kt`**, **`JsonFileAppRepository.kt`**, **`DataStoreAppRepository.kt`**
- **`AppViewModel.kt`**, **`TodayScreen.kt`**

### Other copy / seasonal
- **`ErevChagPrepText.kt`**, **`SeasonalMitzvahText.kt`** â€” Shavuot wording
- **`BirkatHachamahRules.kt`**, **`ChecklistDebugScenarios.kt`** â€” debug dates
- **`checklist-items.json`** (+ composeResources copy)

---

## Official App Song dialog (Android â€” iOS native must match)

See **`docs/MITZ_MODE_OFFICIAL_APP_SONG.md`**.

- **`MusicPlayerDialog.kt`:** G.E.U.L.A streaming section below player controls
- Heading: **Listen to more music from G.E.U.L.A**
- Official logos: Apple Music badge, Spotify icon, Amazon Music logo â€” **vertical stack** (avoids clipping on narrow screens)
- Credit: `Performed by G.E.U.L.A Â© 2026`
- Assets: `app/src/main/res/drawable/ic_apple_music.png`, `ic_spotify.png`, `ic_amazon_music.png`
- Artist URLs: Apple Music, Spotify, Amazon Music (G.E.U.L.A artist pages)

## Mitz Mode home (Android â€” iOS native must match)

- **Background:** Static vertical gradient on all devices â€” no looping `background.mp4` (`docs/MITZ_MODE_HOME_BACKGROUND.md`)
- **Layout:** Instruction text, Mitzvah Me button, bottom pills â€” `docs/MITZ_MODE_HOME_LAYOUT.md` (dp values from `MitzModeApp.kt`)
- **Bottom pills:** Daily Mitzvot Checklist stays **full width**; **Add a Mitzvah** and **What's a Mitzvah?** are **content-width**, centered (`wrapContentWidth` / no `fillMaxWidth`)
- **Edge-to-edge:** Controls use status + navigation bar padding (`MainActivity.kt` / `MitzModeApp.kt`)

## Glossary, term matching, and links (KMP)

See **`docs/GLOSSARY_AND_TERM_LINKS.md`**.

- **`HalachicTermsDictionary.kt`:** `isInsideLongerPhrase`, apostrophe boundary fix, many new terms/aliases (Kiddush Levana, Korbanot, yad soledet bo, etc.)
- **`BeginnerHalachaGlossary.kt`:** Rabbi vs rav split; Arba Minim copy no longer aliases lulav/etrog as slash phrase
- **`HalachicTermText.kt`:** `LocalOpenShabbatGuide`; guide routing via `anchorForTerm`
- **`ShabbatGuideData.kt`:** `glossaryOnlyTermIds`, stricter `anchorForTerm`, fixed broken learn-more URLs
- **`TodayScreen.kt`:** Provides `LocalOpenShabbatGuide`; Shabbat guide â€º uses `enableTerms = false`
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

## Disclaimer & About (KMP â€” copy into iOS shared module)

- **`AppDisclaimer.kt`:** â€œdoes not contain **all** the mitzvot in the entire Torahâ€
- **`AboutContent.kt`:** About tab uses `STARTUP_BODY` (same as first-launch dialog); no duplicate About block in Settings
- **`AboutScreen.kt`:** Dedicated About tab (embedded + standalone nav)

## Manual cities (~550)

- **`composeResources/files/manual-cities.json`** â€” bundled catalog (source of truth)
- **`data/manual-cities.json`** â€” duplicate for tooling/diff
- **`ManualCities.kt`**, **`ManualCitiesCatalog.kt`**, **`ManualCitiesLoader.kt`**, **`ManualCitiesAssets.kt`**
- **`CitySelector.kt`**, **`SettingsScreen.kt`**, **`OnboardingScreen.kt`:** GPS toggle, searchable Aâ†’Z list, `ManualCityListRow`, legacy ID aliases (`jlm`â†’Jerusalem, `nyc`â†’`new_york`, etc.)
- Regenerate JSON from `be-a-tzaddik/tools/manual-cities.tsv` via `gen_manual_cities.py` (tools folder not mirrored)

## Netilat Yadayim (nusach)

- **`ritual_hand_washing`** â€” separate `explanationAshkenaz` / `explanationSefard` / `explanationChabad` for when to say Al Netilat Yadayim (bedside vs second washing, Sefard first-washing, Chabad two-step sequence); consensus: no repeat at shul

## Halachic copy & zman hints

- **Tashlumin** â€” Maariv makeup: only one missed Amidah at Maariv (`ChecklistZmanEvaluator.kt`)
- **Mincha window** â€” active until nightfall (tzeit), not sunset
- **checklist-items.json** â€” tap/ntila, tzitzit 613 drash, Kiddush Levana window, Maarivâ†’siddur, women Hamapil/Birchot HaShachar
- **SeasonalChecklistItems.kt** â€” Chol HaMoed wine optional; Arba Minim nusach notes
- **ShabbatGuideData.kt** â€” Sukkot / Shemini Atzeret / Hoshana Raba / Yizkor nusach differences

## Navigation & screens

- **About tab** â€” 5th bottom-nav item; removed from Settings
- **Embedded nav:** Today | Timer | **Mitz logo** | Settings | About
- **Standalone nav:** Today | Timer | Settings | About

## Today / zmanim / calendar

- Evening mitzvot graying fix (`ZmanPeriodLogic`)
- Holy-day phone rules (`HolyDayPhoneRules.kt`)
- Clickable Current Period â†’ scroll (`ChecklistPeriodScroll.kt`)
- Seasonal checklist items + `NativeJewishCalendarBackend.kt`

## Settings / location

- City selector Aâ†’Z parity (`CitySelector.kt`)
- `UserProfile.isInIsrael` from GPS, manual city, or toggle

## iOS entry

- `MainViewController()` standalone
- `EmbeddedChecklistViewController(onClose)` for Mitz Mode

