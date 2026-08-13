#!/usr/bin/env python3
"""Audit ES/FR for Argos artifacts beyond Melacha essays."""

from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CAT = json.loads((ROOT / "data/translation-catalog/strings.json").read_text(encoding="utf-8"))["strings"]
BUNDLE = ROOT / "shared/src/commonMain/composeResources/files/translations"

BAD_PATTERNS = [
    "crea mang", "trèsdes", "être kodesh", "holy a weekday", "Corea —", "Coréeh",
    "destying", "Titulación", "cookers", "tLa unidad", "pas être kodesh",
    "Learn more!", "strictly forbidden", "highly practical", " usted declara",
    "Saturday,", " the ", " and ", " with ", " when ", " because ",
    "Baruch Atah Hashem C'est", "t rav", "g rav", "ma rav", "douanes",
    "consulteposkim", "halakhique les", "intel'essuy", "sna avec", "etrendre",
    "ainsimething", "visage qui", "forment un. Toutes", "verrière pop-up",
    "transports publics", "pains de main", "six pieds", "la Corée nous",
    "Melacha de Corée", "win-win", "blast ", "Did you know",
]

SKIP_PREFIXES = ("Learn about the Melacha of",)

for lang in ("es", "fr"):
    entries = json.loads((BUNDLE / f"{lang}.json").read_text(encoding="utf-8"))["entries"]
    hits: list[tuple[int, str, list[str], str]] = []
    for k in CAT:
        if k.startswith(SKIP_PREFIXES):
            continue
        v = entries.get(k, "")
        if not isinstance(v, str) or len(v) < 80:
            continue
        bad = [p for p in BAD_PATTERNS if p.lower() in v.lower()]
        if bad:
            hits.append((len(bad), k[:60], bad, v[:100]))
    hits.sort(reverse=True)
    print(f"\n=== {lang.upper()} non-melacha artifact hits: {len(hits)} ===")
    for n, k, bad, v in hits[:20]:
        print(f"  [{n}] {bad[:4]}")
        print(f"      {k}...")
        print(f"      => {v}...")
