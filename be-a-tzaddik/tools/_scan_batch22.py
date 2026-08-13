#!/usr/bin/env python3
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
cat = json.load(open(ROOT / "data/translation-catalog/strings.json", encoding="utf-8"))["strings"]
needles = [
    "After using the bathroom",
    "Before eating or drinking",
    "Chol HaMoed (",
    "Married Jewish women",
    'The Torah commands: "Be fruitful',
    "revi'it —",
    "Kashrut is the system",
    "Why the sages urge",
    "Guard your tongue",
    "Learn about seeing the good",
    "ona'ah —",
    "misheyakir —",
    "Arba Minim (",
    "**Special Mitzvah**",
    "The rabbis instituted a practice",
    "When you acquire new metal",
]

for lang in ("ru", "es", "fr"):
    b = json.load(open(ROOT / f"shared/src/commonMain/composeResources/files/translations/{lang}.json", encoding="utf-8"))["entries"]
    print(f"\n=== {lang} ===")
    for n in needles:
        matches = [x for x in cat if x.startswith(n) or n in x[:100]]
        if not matches:
            continue
        k = max(matches, key=len)
        v = b.get(k, "")
        issues = []
        if re.search(r"\(-\)|\(=0=1=\)|\\cHFF", v):
            issues.append("glyph")
        if re.search(r"י{4,}", v):
            issues.append("he-gibberish")
        if re.search(r"\*\*Special Mitzvah\*\*", v):
            issues.append("en-header")
        if re.search(r"Overcharging or underpaying when|when EL other", v):
            issues.append("en-leak")
        if v == k and len(k) > 40:
            issues.append("fallback")
        if issues:
            print(f"  [{','.join(issues)}] {n[:40]}")
            print(f"    {v[:120]}")
