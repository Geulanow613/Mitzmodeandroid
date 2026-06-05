# Android embed reference (parity target)

The iOS Mitz Mode app should behave like the Android integration. Source is in the **main MitzMode repo**, not in this handoff mirror.

## Files to read on Android

| Repo path | What it does |
|-----------|----------------|
| `app/src/main/java/com/beardytop/mitzmode/tzaddik/TzaddikIntegration.kt` | Loads `App(embeddedMode = true)`, permissions, return icon |
| `app/src/main/java/com/beardytop/mitzmode/ui/MitzModeApp.kt` | Home layout, static gradient, bottom pills, checklist show/dismiss |
| `app/.../ui/components/LowEndDeviceBackground.kt` | Static gradient — iOS native home must match (`docs/MITZ_MODE_HOME_BACKGROUND.md`) |
| `app/.../MainActivity.kt` | Edge-to-edge + safe areas |

**iOS docs (in this handoff):** `docs/MITZ_MODE_HOME_BACKGROUND.md`, `docs/MITZ_MODE_HOME_LAYOUT.md`

## Kotlin shared (same as iOS mirror)

| Handoff path | What to match |
|--------------|----------------|
| `be-a-tzaddik/shared/.../ui/BeATzaddikApp.kt` | `embeddedMode`, bottom nav, About tab, center logo |
| `be-a-tzaddik/shared/.../App.kt` | `embeddedMode`, `onRequestClose` parameters |

## Agent task

1. Read Android files above.
2. Ensure iOS host calls `EmbeddedChecklistViewController` with the same UX rules as `docs/PARITY_CHECKLIST.md`.
3. Diff mirrored `shared/` into the iOS project’s linked `shared` module — paths should match.
