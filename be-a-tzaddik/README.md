# Be a Tzaddik

Standalone Kotlin Multiplatform app (Android + iOS) — daily mitzvah checklist with GPS zmanim and multilingual UI.

## Structure

| Path | Purpose |
|------|---------|
| `shared/` | KMP module — checklist UI, domain logic, bundled `he/es/fr/ru` translations |
| `androidApp/` | Android shell + translation host (`TranslationViewModel`, Google API fallback) |
| `iosApp/` | iOS SwiftUI shell |
| `data/` | Translation catalog shards + checklist JSON sources |
| `tools/` | `compile_full_bundled.py` and translation pipeline scripts |

## Mitz Mode (separate repo)

[Mitz Mode](https://github.com/Geulanow613/Mitzmodeandroid) embeds `:shared` for the same checklist in **English only**. Its archived translation code lives in that repo under `app/archived-translations/`.

## Build (Android)

```bash
./gradlew :androidApp:assembleDebug
```

Optional: add `GOOGLE_TRANSLATE_API_KEY=...` to `local.properties` for online languages beyond bundled he/es/fr/ru.

## Translations

1. Edit human shards in `data/translation-catalog/human/`
2. Run `python tools/compile_full_bundled.py` (or incremental merge scripts)
3. Outputs land in `shared/src/commonMain/composeResources/files/translations/`
