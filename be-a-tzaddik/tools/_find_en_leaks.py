#!/usr/bin/env python3
"""Find long entries still mostly English in non-en bundles."""
from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CAT = json.loads((ROOT / "data" / "translation-catalog" / "strings.json").read_text(encoding="utf-8"))["strings"]
BUNDLE = ROOT / "shared" / "src" / "commonMain" / "composeResources" / "files" / "translations"

EN_WORDS = re.compile(
    r"\b(the|and|with|when|that|this|you|your|before|after|means|learn about|people say)\b",
    re.I,
)


def score_en(s: str) -> int:
    return len(EN_WORDS.findall(s))


for lang in ("es", "fr", "ru"):
    entries = json.loads((BUNDLE / f"{lang}.json").read_text(encoding="utf-8"))["entries"]
    leaks = []
    for k in CAT:
        v = entries.get(k, k)
        if not isinstance(v, str) or len(v) < 80:
            continue
        if v == k and len(k) > 80:
            leaks.append((999, k[:70], v[:100]))
            continue
        en = score_en(v)
        if en >= 8:
            leaks.append((en, k[:70], v[:120]))
    leaks.sort(reverse=True)
    print(f"\n=== {lang} long English-heavy ({len(leaks)}) ===")
    for en, k, v in leaks[:20]:
        print(f"  [{en}] {k}...")
        print(f"       {v}...")
