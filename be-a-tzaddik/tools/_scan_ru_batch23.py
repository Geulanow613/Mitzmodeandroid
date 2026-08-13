#!/usr/bin/env python3
"""Find RU entries with Argos glue, truncation, or English leaks."""
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
cat = json.load(open(ROOT / "data/translation-catalog/strings.json", encoding="utf-8"))["strings"]
ru = json.load(open(
    ROOT / "shared/src/commonMain/composeResources/files/translations/ru.json", encoding="utf-8"
))["entries"]

PATS = [
    (r"[а-яА-Я][.!?][A-Za-z]", "cyr-punct-latin"),
    (r"[A-Za-z]{2,}[а-яА-Я]", "latin-cyr-glue"),
    (r"\bmitzvah\b", "mitzvah-en"),
    (r"\bRabbis\b", "Rabbis-en"),
    (r"\bthe other party\b", "en-leak"),
    (r"свалятся", "bad-ru-word"),
    (r"minhag [А-Я]", "minhag-glue"),
    (r"день\.Yom", "dot-glue"),
    (r"— [A-Za-z]{3,} —", "en-dash-en"),
]

hits = []
for k in cat:
    v = ru.get(k, "")
    if not v or v == k or len(v) < 40:
        continue
    for pat, tag in PATS:
        if re.search(pat, v):
            hits.append((tag, len(v), k[:55], v[:100]))
            break

print(f"RU issue hits: {len(hits)}")
for tag, ln, k, v in sorted(hits, key=lambda x: x[0])[:35]:
    print(f"  [{tag}] ({ln}) {k}...")
    print(f"    {v}...")
