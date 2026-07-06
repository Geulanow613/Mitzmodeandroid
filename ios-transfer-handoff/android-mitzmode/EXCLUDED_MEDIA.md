# Media excluded from handoff (copy separately if needed)

These files are **not** copied to keep the handoff bundle small. Code still references them; add locally before testing reward videos.

| Asset | Typical path | Purpose |
|-------|----------------|--------|
| `background.mp4` | `app/src/main/assets/` | Old looping home video — **not used** |
| `mitzmodenew1.mp4` … `mitzmodenew13.mp4` | `app/src/main/assets/` | Level-up reward clips |
| `finalreward.mp4` | `app/src/main/assets/` | Mitz Mode! tier reward |
| `tzaddik.mp4` | `app/src/main/assets/` | Blessed animation |
| `starbg.png` | `app/src/main/assets/` | Legacy star layer — home uses gradient only |

Home screen uses **`LowEndDeviceBackground`** (static gradient) on all devices.
