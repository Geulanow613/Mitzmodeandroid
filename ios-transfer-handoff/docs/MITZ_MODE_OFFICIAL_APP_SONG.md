# Official App Song dialog (iOS parity)

Android source of truth: `app/src/main/java/com/beardytop/mitzmode/ui/components/MusicPlayerDialog.kt`

Opened from ⋮ menu → **Official App Song** (`MitzModeApp.kt` → `showMusicPlayer`).

## Dialog structure (top → bottom)

| Section | Notes |
|---------|--------|
| Header | “🎵 Official App Song” + close button |
| Artwork | 120dp circle — 🎵 when playing, 🎼 when idle |
| Progress | Linear bar + `0:00` / duration when loaded |
| Controls | Stop (48dp), Play/Pause (64dp) |
| Copy | “Enjoy the official Mitz Mode song!” |
| Credit | `Performed by G.E.U.L.A © 2026` |
| Divider | `HorizontalDivider` |
| Streaming links | See below |

## G.E.U.L.A streaming links

Section: rounded card (`surfaceVariant` ~35% alpha), heading **Listen to more music from G.E.U.L.A**.

**Layout:** vertical stack (`Column`, `spacedBy = 10.dp`) — **not** a single horizontal row (three official logos overflow on phone width).

| Order | Asset | Size (dp) | Label under logo | URL |
|-------|--------|-----------|------------------|-----|
| 1 | Official Apple **Listen on Apple Music** badge (`ic_apple_music.png`) | 200 × 36 | No (badge includes text) | `https://music.apple.com/us/artist/geula/1615973719` |
| 2 | Official Spotify icon (`ic_spotify.png`) | 40 × 40 | Yes — “Spotify” | `https://open.spotify.com/artist/2MSUpcrPITpkisaogXF5W9` |
| 3 | Official Amazon Music logo (`ic_amazon_music.png`) | 180 × 32 | No (logo includes text) | `https://www.amazon.com/music/player/artists/B0FCGB4RGM/g-e-u-l-a` |

Each row: full-width tappable chip, `ContentScale.Fit`, opens URL via `Context.openUrl` (network check + `ACTION_VIEW`).

## Android drawable assets (copy into iOS asset catalog)

| File | Source |
|------|--------|
| `app/src/main/res/drawable/ic_spotify.png` | Spotify brand icon |
| `app/src/main/res/drawable/ic_apple_music.png` | Apple Marketing Tools “Listen on Apple Music” badge |
| `app/src/main/res/drawable/ic_amazon_music.png` | Amazon Music horizontal logo |

Use **official** brand artwork only — do not redraw logos.

## iOS QA

- [ ] All three platforms visible without horizontal clipping.
- [ ] Apple badge appears first (Apple identity guidelines: Apple Music first in multi-service lineup).
- [ ] Credit reads `© 2026`.
- [ ] Taps open correct artist pages in Safari / native apps.
