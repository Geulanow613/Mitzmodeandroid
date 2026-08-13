# iOS transfer handoff

**For agents:** open **`AGENTS.md`** first. This directory is only source files and task docs — not an install guide.

## Contents

| Item | Purpose |
|------|---------|
| **`AGENTS.md`** | What to do, folder layout, success criteria |
| **`be-a-tzaddik/`** | Mirrored KMP + iOS app sources (refreshed by sync script) |
| **`docs/`** | Parity checklist, source map, Swift/Kotlin reference, **Mitz home static gradient**, Android embed pointer |
| **`CHANGELOG.md`** | Latest deltas in the mirror |
| **`sync-to-ios-handoff.ps1`** | Refresh mirror from repo (Windows) |
| **`docs/MITZ_MODE_HOME_LAYOUT.md`** | Native home control positions (dp) |
| **`CHANGELOG.md`** | Latest sync: cities, disclaimer, background, layout |

Legacy paths `mitz-mode-ios-integration-kit/` and `standalone-be-a-tzaddik-ios/` point here — use **`docs/`** instead.

## Refresh (maintainer)

```powershell
.\ios-transfer-handoff\sync-to-ios-handoff.ps1
```

## Hand off to another agent

Copy the whole `ios-transfer-handoff/` folder (or zip it) and say:

> Read `ios-transfer-handoff/AGENTS.md` and bring the iOS app to parity using the files in that folder.
