#!/usr/bin/env python3
"""Scan compiled Hebrew bundle for Latin-mixed garbage."""
import json
import sys
from pathlib import Path

sys.stdout.reconfigure(encoding="utf-8")
ROOT = Path(__file__).resolve().parents[1]
he = json.loads(
    (ROOT / "shared/src/commonMain/composeResources/files/translations/he.json").read_text(
        encoding="utf-8"
    )
)["entries"]
req = json.loads((ROOT / "data/translation-catalog/strings.json").read_text(encoding="utf-8"))[
    "strings"
]


def latin_ratio(t: str) -> float:
    letters = [c for c in t if c.isalpha()]
    if not letters:
        return 0.0
    return sum(1 for c in letters if ord(c) < 128) / len(letters)


bad = []
for s in req:
    tr = he.get(s, s)
    if tr == s or len(tr) < 40:
        continue
    r = latin_ratio(tr)
    if r > 0.15:
        bad.append({"ratio": round(r, 3), "key": s})

bad.sort(key=lambda x: -x["ratio"])
out = ROOT / "data/translation-catalog/he-compiled-bad.json"
out.write_text(json.dumps(bad, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
print(f"{len(bad)} bad -> {out}")
for row in bad[:20]:
    print(row["ratio"], row["key"][:70])
