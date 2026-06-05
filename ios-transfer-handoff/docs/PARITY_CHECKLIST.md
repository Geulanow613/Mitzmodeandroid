# UI placement and behavior parity (iOS target)

Strict acceptance checklist — iOS must match the Android / embedded Mitz Mode experience. Verify using files in `../be-a-tzaddik/`.

## Branding and flow

- Embedded flow title: **the Daily Mitzvot Checklist**
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

- Dedicated **About** tab with `AboutContent` (uses `AppDisclaimer.STARTUP_BODY` — same as first-launch dialog)
- Disclaimer includes: “does not contain **all** the mitzvot in the entire Torah”
- Settings no longer contains an About card

## Onboarding

- Cards fill screen appropriately
- First page has Back control consistent with later pages
- Location page includes manual city selector on the page
- City list scrollable, A→Z (`CitySelector.kt` / `filterManualCities`), labels include country

## Settings / location

- **~550 manual cities** from `manual-cities.json` (search + full A→Z list, same as onboarding)
- GPS toggle + permission flow; manual city does not require GPS
- `ManualCityListRow` with checkmark for selected city; legacy IDs (`jlm`, `tlv`, `nyc`, …) still resolve
- `UserProfile.isInIsrael` from GPS bounds, manual city, or toggle

## Today / zmanim

- **Current Period** tappable — scrolls to Upon waking / Afternoon Prayer / Evening Prayer; section expands
- Evening mitzvot **upcoming (grayed)** between dawn and sunset (not active at 2pm)
- Prayer rows use `ChecklistZmanEvaluator` when zmanim available
- Mincha window shown until **nightfall** where zmanim provide tzeit

## Holy days / Shabbat

- **Shabbat:** holy-day notice, empty checklist
- **Chutz la’aretz 2nd day Yom Tov:** festival notice, empty checklist
- **Israel / chutz day 1 Yom Tov:** checklist available
- Seasonal: erev prep, Chol HaMoed, Jerusalem Purim, etc.

## Mitz Mode home screen (native Swift shell)

- **Static gradient only** on all devices — same as Android `LowEndDeviceBackground` (see **`MITZ_MODE_HOME_BACKGROUND.md`**)
- **No** looping `background.mp4`, no RAM-based video toggle, no animated `GradientBackground` on home
- **Layout parity** — instruction text, Mitzvah Me button offset, bottom pills including Daily Mitzvot Checklist `−24dp` offset (see **`MITZ_MODE_HOME_LAYOUT.md`**)
- Edge-to-edge with safe-area padding on controls (status + home indicator)
- Reward/celebration videos may still play on events (separate from home background)

## Navigation / performance

- Bottom nav above safe area; all targets tappable
- Return-to-main confirms before exit
- No blocked tap targets from overlays

## Data / settings parity

- Nusach, gender/married/children, zmanim location, explainer text from shared JSON
- Kashrut timer + scroll-to-settings from timer screen

## iOS-specific

- Calendar: `NativeJewishCalendarBackend.kt` (iosMain)
- Embed: `EmbeddedChecklistViewController(onClose:)`
- Yom Tov full-screen device rest may differ from Android until wired to same rules as `HolyDayPhoneRules.kt`
