# Glossary, term matching, and link parity

Android/iOS shared KMP behavior — copy these files verbatim from `be-a-tzaddik/shared/`.

## Core files

| Path | Role |
|------|------|
| `domain/HalachicTermsDictionary.kt` | Term catalog, matcher, overlap logic, apostrophe boundaries |
| `domain/BeginnerHalachaGlossary.kt` | Beginner overlay terms merged into dictionary |
| `ui/components/HalachicTermText.kt` | Underlined terms, popups, Shabbat Guide routing |
| `ui/screens/ShabbatGuideData.kt` | Guide anchors, `glossaryOnlyTermIds`, learn-more URLs |
| `ui/screens/TodayScreen.kt` | Provides `LocalOpenShabbatGuide` to checklist copy |

## Term matching rules (must match on iOS)

1. **Longest phrase wins** — `selectNonOverlapping()` keeps the longest match when ranges overlap (e.g. *Kiddush Levana* beats *Kiddush*).
2. **`isInsideLongerPhrase`** — single-word matches are suppressed when they fall inside any longer glossary phrase in the same text (not prefix-only). Covers *Kiddush* inside *Kiddush Levana*, *Shabbat* inside *Motzei Shabbat*, *Hallel* inside *Half Hallel*, etc.
3. **`effectiveMatchLabels()`** — single-word aliases inside a multi-word term title are suppressed (e.g. *challah* inside *hafrashat challah*).
4. **Apostrophe boundaries** — `'` is word-interior only when **between two letters** (`b'Makom`, `ha'eish`). Quoted phrases like `'yad soledet bo'` in Shabbat Guide text must match normally.

## Shabbat Guide navigation

- **`LocalOpenShabbatGuide`** — composition local in `HalachicTermText.kt`; `TodayScreen` provides `onOpenShabbatGuide`.
- Tapping an underlined term:
  - If `ShabbatGuideData.anchorForTerm(term)` returns an anchor → open **Shabbat Guide** scrolled to that section.
  - Else → glossary popup.
- **`glossaryOnlyTermIds`** — terms that share a word with a guide topic but are a different concept (e.g. `kiddush_levana`, `motzei_shabbat`, `shabbat_candles`). These never route to the guide via substring match.
- **`anchorForTerm`** — exact guide-key match first; single-word terms may use `anchorForLabel`; multi-word terms do not fall through to loose substring matching.
- **"Shabbat guide ›"** link on Today uses `enableTerms = false` so the whole phrase is one tap target (not per-word glossary).
- Underlined **Shabbat** in prep/seasonal copy opens the guide; brief glossary-only terms stay in popup.

## Rabbi vs rav

Separate glossary entries — do not alias `"rabbi"` / `"Rabbi"` on the `rav` term. Each has its own definition.

## Links audit (maintainer)

All learn-more / external URLs in checklist, glossary, guide, and prep text were audited (excludes `mitzvotlistfull.json` and `mitzvotcloud.json`).

- Broken Kiddush learn-more in `ShabbatGuideData.kt` fixed.
- Repo scripts: `tools/audit_links.py`, `tools/fix_links.py` (Android repo root; not mirrored).
- Term conflict audit: `be-a-tzaddik/tools/audit_term_conflicts.py` — scans checklist, prep text, guide, seasonal docs; **0 bad partial matches** after apostrophe fix.

## iOS QA

- [ ] *Kiddush Levana* underlines full phrase; tap shows moon blessing (not wine Kiddush).
- [ ] *Motzei Shabbat*, *Shabbat candles*, *Half Hallel* match as phrases.
- [ ] Quoted `'yad soledet bo'` in guide text is tappable.
- [ ] *rabbi* and *rav* show different definitions.
- [ ] *Shabbat guide ›* opens full guide; underlined *Shabbat* in prep cards opens guide.
- [ ] Learn-more links in guide/glossary open valid URLs.
