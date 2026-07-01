#!/usr/bin/env python3
"""Find catalog entries still mostly English in non-English bundles."""
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
cat = json.load(open(ROOT / "data/translation-catalog/strings.json", encoding="utf-8"))["strings"]
cat_set = set(cat)

def mostly_english(text: str) -> bool:
    if len(text) < 80:
        return False
    # crude: starts with common English patterns
    if re.match(r"^(Learn about|The |This is |In Israel|How to |What to )", text):
        return True
    words = re.findall(r"[A-Za-z]{4,}", text[:200])
    return len(words) >= 8

for lang in ("es", "fr", "ru"):
    entries = json.load(open(
        ROOT / f"shared/src/commonMain/composeResources/files/translations/{lang}.json",
        encoding="utf-8",
    ))["entries"]
    hits = [k[:75] for k, v in entries.items() if k in cat_set and mostly_english(v)]
    print(f"\n{lang}: {len(hits)} mostly-English long entries")
    for h in hits[:25]:
        print(" ", h)
    if len(hits) > 25:
        print(f"  ... +{len(hits)-25} more")
