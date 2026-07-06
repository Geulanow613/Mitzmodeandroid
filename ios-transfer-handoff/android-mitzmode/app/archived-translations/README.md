# Archived Mitz Mode translations

Mitz Mode is **English-only**. Full translation infrastructure was moved here so it can be re-enabled later without digging through git history.

## Contents

- `mitzmode-translation/` — original Kotlin sources (LanguagePreferences, TranslationViewModel, TranslationService, language dialogs, KMP bridge, liturgy helpers).

## To re-enable translations in Mitz Mode

1. Restore files from `mitzmode-translation/` into their original packages under `app/src/main/java/com/beardytop/mitzmode/`.
2. Restore `DeviceAppLanguageTest.kt` under `app/src/test/...`.
3. In `MainActivity.kt`, wire `TranslationViewModel` + `LocalTranslationViewModel` + `ProvideAppTranslation(translationViewModel)`.
4. In `app/build.gradle`, restore `GOOGLE_TRANSLATE_API_KEY` buildConfigField and OkHttp dependency.
5. Restore language menu + dialogs in `MitzModeApp.kt` and tour copy mentioning language settings.
6. Revert `TranslatableText.kt` to the archived async translation version.

**Note:** Standalone multilingual Be a Tzaddik lives in the separate `tzaddik` repo (`be-a-tzaddik/`). Mitz Mode embeds only the checklist via `:beatzaddik-shared` with an English-only `ProvideAppTranslation` stub.
