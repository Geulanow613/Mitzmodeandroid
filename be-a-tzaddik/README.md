# Be a Tzaddik

Standalone Kotlin Multiplatform + Compose Multiplatform app (Android + iOS targets).

## Features (v1)

- Daily / night mitzvah checklist (ported from MitzMode Daily Mitzvot)
- Male / female, married, children, nusach (Ashkenaz, Sefard, Chabad, I'm not sure → Nusach Ari, Other)
- Optional GPS location for zmanim (Android)
- Meat / dairy waiting timer
- Holy-light checkbox animation (check and uncheck)
- Custom mitzvot you type in

## Run Android

### Android Studio

Open the **`be-a-tzaddik`** folder as the project root — not `hehehe` (MitzMode) and not `be-a-tzaddik/androidApp` alone.

1. **File → Open** → select `c:\apps\hehehe\be-a-tzaddik`
2. Wait for **Gradle sync** to finish (needs `local.properties` with `sdk.dir`; create from Android SDK path if missing)
3. Run configuration: **androidApp** (module `:androidApp`)
4. Click **Run** on a device or emulator

If you see `Unable to find Gradle tasks to build: []`, you opened the wrong directory — close and reopen **`be-a-tzaddik`**.

### Command line

```bash
cd be-a-tzaddik
.\gradlew.bat :androidApp:assembleDebug
```

Open `androidApp/build/outputs/apk/debug/androidApp-debug.apk` on a device or emulator.

## iOS

Requires macOS with Xcode. See [iosApp/README.md](iosApp/README.md) — SwiftUI hosts `MainViewController()` from the `:shared` framework.

## Data

- `data/checklist-items.json` — migrated checklist (regenerate with `tools/gen_checklist.py`)
- `data/holidays-overlay.json` — upcoming holiday hints
- Bundled copies live under `shared/src/commonMain/composeResources/files/`

## Disclaimer

Educational tool only — consult your rabbi for halachic guidance. See [docs/HALACHIC_DISCLAIMER.md](docs/HALACHIC_DISCLAIMER.md).
