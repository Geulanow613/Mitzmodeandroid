# Mitz Mode Android app (handoff mirror)

Slim copy of the repo root Android project (`app/` + Gradle wrapper). **Videos and other large media are omitted** — see `EXCLUDED_MEDIA.md`.

## Layout in handoff

```
ios-transfer-handoff/
  be-a-tzaddik/shared/     ← KMP module (settings.gradle points here)
  android-mitzmode/
    app/                   ← Mitz Mode UI + embed
    settings.gradle
    gradle/
    ...
```

## Open in Android Studio (Mac)

1. Run `extract-to-dropbox.command` (or `extract-to-dropbox.sh`) from the handoff folder.
2. Open **`~/Dropbox/claudesucks/android-mitzmode`** (or your extracted path).
3. Let Gradle sync. Module `:beatzaddik-shared` resolves to `../be-a-tzaddik/shared`.

## Home screen background

Static vertical gradient only (no looping `background.mp4`). Spec: `../docs/MITZ_MODE_HOME_BACKGROUND.md`  
Source: `app/src/main/java/com/beardytop/mitzmode/ui/components/LowEndDeviceBackground.kt`

## Refresh from Windows

```powershell
cd c:\apps\hehehe\ios-transfer-handoff
.\sync-to-ios-handoff.ps1
```
