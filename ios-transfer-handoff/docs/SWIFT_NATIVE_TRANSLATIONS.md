# Swift native translations (Mitz Mode iOS)

Use this when the **native Swift** Mitz Mode app loads JSON from the bundle (not only the embedded KMP checklist).

## Folder: `swift-native/`

Refreshed automatically when you run:

```powershell
cd c:\apps\hehehe\ios-transfer-handoff
.\sync-to-ios-handoff.ps1
```

Or whenever `be-a-tzaddik/tools/compile_full_bundled.py` finishes (unless `SKIP_IOS_HANDOFF=1`).

| Path | Purpose |
|------|---------|
| `BundledTranslations/{he,es,fr,ru}.json` | **All UI strings** — menu, buttons, rewards, brachot dialog labels, checklist explainers in catalog, mitzvah-me texts |
| `Mitzvot/mitzvotcloud.json` | Cloud mitzvot list (155) — same as Android GitHub / cache |
| `Mitzvot/mitzvotlistfull.json` | Local mitzvot list (168) — same as `app/.../assets/mitzvotlistfull.json` |
| `Checklist/checklist-items.json` | **Rebuilt daily checklist** — titles, explanations, nusach variants, links |
| `Checklist/nusach-extras.json` | Nusach-specific checklist copy |
| `Checklist/holidays-overlay.json` | Holiday metadata |
| `Checklist/manual-cities.json` | City picker names |
| `Cities/city-geography.json` | Localized city + country labels (4 langs) |
| `Explainers/explainer_template_args.json` | Args for dynamic seasonal explainers (Yaaleh, fast days, etc.) |
| `Explainers/seasonal_explainer_fragments.json` | Fragment overrides merged into bundles |
| `Rewards/reward_levels.json` | Mitzvah counter tier **display names** per language |
| `Rewards/mitzvah_level_thresholds.json` | Count → tier key (matches Android `MitzvahLevels.kt`) |
| `Catalog/strings.json` | Master list of 2752 English catalog keys |
| `MANIFEST.json` | SHA256 per file + coverage stats |

## Lookup rule (must match Android/KMP)

```swift
// Pseudocode — same as BundledTranslationsCatalog.lookup(text, languageCode)
func translate(_ englishKey: String, lang: String) -> String {
    guard let bundle = bundles[lang] else { return englishKey }
    return bundle.entries[englishKey] ?? englishKey
}
```

- **Key** = exact English source string from Kotlin/Swift (`TranslatableText("About")` → key `"About"`).
- **Bundled languages**: `he`, `es`, `fr`, `ru` (offline, no API).
- **English** (`en`): return the key unchanged.
- **Mitzvah Me**: after picking a mitzvah, translate `mitzvah.text` using the same lookup (all 323 unique cloud+local texts are in the bundles).

## What stays in Swift source code

| Content | Where |
|---------|--------|
| Hebrew brachot / Birkat Hamazon **liturgy** | Swift structs (like Android `BirkatHamazonText.kt`) — Hebrew + optional English |
| Bracha **descriptions** in menu | English keys in `BundledTranslations` (e.g. `"After using the bathroom — each time you finish..."`) |
| Menu chrome | Keys: `"Blessings"`, `"Birkat HaMazon"`, `"Language Settings"`, `"About"`, etc. |

## Reward system

1. `mitzvah_level_thresholds.json` → tier key from completed count (same thresholds as Android).
2. `translate(tierKey, lang)` using `BundledTranslations` or `Rewards/reward_levels.json`.
3. Star count: use `stars` field on the matched threshold row.

## Checklist (native or KMP)

- **Embedded KMP**: use `be-a-tzaddik/shared` — checklist JSON is loaded by `ChecklistCatalog` from compose resources (no separate Swift work).
- **Native Swift checklist**: load `Checklist/checklist-items.json` and resolve explanations through `BundledTranslations` for non-English UI.

## Verify on Mac after copy

```bash
# Optional: diff manifest hashes
python3 -c "import json; print(json.load(open('swift-native/MANIFEST.json'))['bundle_stats'])"
```

Expect ~2751/2752 translated per language; 323/323 mitzvot texts translated.

## Drift prevention

Any translation work on Windows updates handoff when:

1. `compile_full_bundled.py` completes, or
2. You run `sync-to-ios-handoff.ps1`

Then copy **`ios-transfer-handoff/`** to the Mac (USB, git, zip). Run `verify-handoff.ps1` on Windows before transfer.
