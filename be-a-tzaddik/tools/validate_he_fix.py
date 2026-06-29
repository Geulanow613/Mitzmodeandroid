#!/usr/bin/env python3
import json
import re
from pathlib import Path

BASE = Path(__file__).resolve().parent.parent / "data" / "translation-catalog"
HUMAN = BASE / "human"
MIXED_RE = re.compile(r"[\u0590-\u05FF]+[A-Za-z]+|[A-Za-z]+[\u0590-\u05FF]+")

for batch in ("he_fix_001", "he_fix_002", "he_fix_003"):
    keys = json.loads((BASE / f"_keys_{batch}.json").read_text(encoding="utf-8"))
    data = json.loads((HUMAN / f"{batch}_he_only.json").read_text(encoding="utf-8"))["he"]
    assert len(data) == len(keys), f"{batch}: {len(data)} != {len(keys)}"
    bad = []
    for i, s in enumerate(data):
        for m in MIXED_RE.finditer(s):
            g = m.group()
            if "HebrewCalendar" in g:
                continue
            bad.append((i + 1, g))
    status = "PURE" if not bad else f"MIXED {bad[:5]}"
    print(f"{batch}: {len(data)} strings — {status}")

# Placeholder checks
d3 = json.loads((HUMAN / "he_fix_003_he_only.json").read_text(encoding="utf-8"))["he"]
assert "${if (day == 1)" in d3[1], "Chanukah placeholder missing"
assert "$hoshanaRabaBlock" in d3[9], "Chol HaMoed placeholder missing"
assert "$hallelBlock" in d3[9], "hallelBlock missing"
assert "$festivalLines" in d3[9], "festivalLines missing"
print("Placeholders OK")
