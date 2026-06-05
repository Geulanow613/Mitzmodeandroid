# Mitz Mode home screen layout (iOS parity)

Android source of truth: `app/src/main/java/com/beardytop/mitzmode/ui/MitzModeApp.kt` (controls layer).

Background: **`MITZ_MODE_HOME_BACKGROUND.md`** (static gradient on all devices).

## Safe areas

- Root controls `Box`: `statusBarsPadding()` + `navigationBarsPadding()` (edge-to-edge; content inset above system bars).
- Bottom pill stack: `padding(bottom = 20.dp)` above gesture/nav bar.

## Instruction text (“Tap the Mitzvah Me button…”)

| Property | Value |
|----------|--------|
| Alignment | Top center |
| Top padding | `instructionTopPadding` = `topPadding + 8.dp + 54.dp + 28.dp` |
| `topPadding` | `32.dp` if screen height > 700dp, else `8.dp` |
| Horizontal inset | `72.dp` (clears menu + counter) |
| Style | `headlineMedium`, bold, centered |

Swift: use `safeAreaInsets.top` + same logical offsets (convert dp → pt 1:1 on @1x design scale, or scale by screen).

## Center “Mitzvah Me” button

| Property | Value |
|----------|--------|
| Alignment | Center of screen |
| Modifier chain | `padding(16.dp)` → center → `padding(bottom = 72.dp)` |
| Effect | Sits slightly above geometric center so bottom CTAs do not crowd it |

## Bottom action stack (top → bottom)

Column: `BottomCenter`, `horizontal = 20.dp`, `spacedBy = 12.dp`.

| Control | Notes |
|---------|--------|
| **Daily Mitzvot Checklist** | Full-width gold gradient pill (primary entry; **not** in ⋮ menu); `offset(y = -20.dp)` |
| **Add a Mitzvah** | Full-width outlined pill |
| **What's a Mitzvah?** | Full-width gold gradient pill |

⋮ **menu only:** About, Blessing After Meals, Traveler's Prayer, Blessings, Official App Song, Language Settings.

### Daily Mitzvot Checklist pill styling

- Shape: capsule (`RoundedCornerShape(50)`)
- Gradient stops: `#FFF3B5` → `#FFE082` → `#FFD56B` → `#E0AB2F` (horizontal)
- Border: `1.6.dp`, `#FFF8D6`
- Shadow: elevation `16.dp`, spot `#FFD56B`
- Inner padding: `horizontal 34.dp`, `vertical 14.dp`
- Text: `#1A3D72`, `titleMedium` bold

## Header chrome (same layer)

- Menu (three dots): top-start, circular gold treatment (`MenuDotsIcon.kt`)
- Mitzvah counter pill: top-end
- Header row height assumed: `54.dp` for instruction offset math

## QA

- [ ] Instruction sits below menu + counter, not under status bar.
- [ ] Mitzvah Me button clears bottom three pills and home indicator.
- [ ] Daily Mitzvot Checklist pill is ~24pt above Add a Mitzvah (visual gap).
- [ ] Static gradient fills screen (`MITZ_MODE_HOME_BACKGROUND.md`).

## Embedded checklist

Opening **Daily Mitzvot Checklist** presents KMP `EmbeddedChecklistViewController` — separate UI; this doc is **native Mitz Mode home only**.
