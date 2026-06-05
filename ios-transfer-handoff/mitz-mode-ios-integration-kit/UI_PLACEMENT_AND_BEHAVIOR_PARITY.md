# UI Placement and Behavior Parity (iOS target)

Use this as a strict acceptance checklist so iOS matches your current integrated experience.

## Branding and flow

- Embedded flow title uses: `the Daily Mitzvot Checklist`
- Standalone app remains independently launchable
- Embedded flow has a clear return path to Mitz Mode (center bottom-nav logo)

## Bottom navigation (embedded)

Order left → right:

1. **Today**
2. **Timer**
3. **Mitz Mode logo** (center) — confirms before return to generator
4. **Settings**
5. **About** (right) — disclaimer + producer info (not in Settings)

Standalone app: Today | Timer | Settings | About (no center logo).

## About screen / disclaimer

- Dedicated **About** tab with `AboutContent` (`AppDisclaimer.STARTUP_BODY` — same as startup dialog)
- Wording: checklist “does not contain **all** the mitzvot in the entire Torah”
- Settings no longer contains an About card

## Onboarding placement/behavior

- Onboarding cards fill screen area appropriately (not compact)
- First onboarding page has Back control in same placement/style as later pages
- Onboarding location page includes manual city selector directly on the page
- Manual city list is scrollable to reach all entries
- Manual city list sorted alphabetically (`CitySelector.kt` / `filterManualCities`)
- City labels include country (city + country)

## Settings location (parity with onboarding)

- **~550 cities** from bundled `manual-cities.json`
- Search field + scrollable full city list (A→Z)
- Optional “More cities” dialog uses same sorted/filtered list
- GPS toggle separate from city list; `ManualCityListRow` selection UI

## GPS/location behavior

- GPS toggle can trigger permission dialog if denied
- Permission dialog provides settings guidance only
- Manual city selection does not require GPS permission
- `UserProfile.isInIsrael` from GPS bounds, Jerusalem/manual city, or toggle

## Today screen / zmanim

- **Current Period** label in header and above checklist is **tappable** — scrolls to:
  - Day → Upon waking
  - Afternoon → Afternoon Prayer
  - Night → Evening Prayer
- Section auto-expands when scrolling to period
- **Evening mitzvot** show as upcoming (grayed) between dawn and sunset — not active at 2pm (`ZmanPeriodLogic` evening window fix)
- Prayer rows use `ChecklistZmanEvaluator` when zmanim available

## Holy days / Shabbat

- **Shabbat (Saturday):** holy-day notice, empty checklist
- **Chutz la’aretz 2nd day Yom Tov:** festival notice, empty checklist (`HolyDayPhoneRules.kt`)
- **Israel Yom Tov / chutz day 1:** checklist still available (prep on erev only for day-of mitzvot)
- Seasonal sections: erev chag/purim/chanukah, Chol HaMoed, Purim (Jerusalem 15 Adar / meshulash), etc.

## Navigation and safe areas

- Bottom controls are above iOS home indicator / safe area
- Embedded checklist bottom nav is fully visible and tappable
- Return-to-main action confirms before exit

## Mitz Mode home screen (native Swift)

- **Static gradient only** on every device — match Android `LowEndDeviceBackground` (see **`../docs/MITZ_MODE_HOME_BACKGROUND.md`**)
- **Do not** use looping `background.mp4` or device-tier video background on home
- **Control positions** — match Android `MitzModeApp.kt` (see **`../docs/MITZ_MODE_HOME_LAYOUT.md`**): instruction below header (`+28dp` under 54dp row), Mitzvah Me `bottom = 72dp` from center, Daily Mitzvot pill `offset(y = -24dp)`, bottom stack `20dp` above nav bar
- Safe areas: status + navigation bar padding on controls layer
- Reward videos (level-up, etc.) are separate and still allowed

## Performance

- Home uses the lightweight static gradient (no video decode on launch)
- Ensure no tap targets are blocked by overlays
- Validate smooth menu/button responsiveness under load

## Data/settings parity

- Nusach selection parity
- Gender/married/children profile switches parity
- Zmanim location behavior parity (GPS/manual city)
- Explainer/disclaimer text parity from shared data/resources
- Kashrut timer + scroll-to-settings from timer screen

## iOS-specific notes

- Calendar: `NativeJewishCalendarBackend.kt` (iosMain) — Hebrew date, seasons, Jerusalem Purim
- Yom Tov full-screen rest on iOS is still heuristic-limited vs Android zmanim; parity improvements ongoing
- Use `EmbeddedChecklistViewController(onClose:)` for Mitz Mode embed
