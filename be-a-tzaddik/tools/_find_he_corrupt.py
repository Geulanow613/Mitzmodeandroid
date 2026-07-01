#!/usr/bin/env python3
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
he = json.loads(
    (ROOT / "shared/src/commonMain/composeResources/files/translations/he.json").read_text(
        encoding="utf-8"
    )
)["entries"]
patterns = [
    "EmbKat",
    "Birkt",
    "Nart",
    "שולכן אראך",
    "צ'אטוז",
    "Hadlakat Nart",
    "What it is:",
    "What they are:",
    "G-d for",
    "the sages teach",
]
bad = []
for k, v in he.items():
    if not isinstance(v, str):
        continue
    for p in patterns:
        if p in v:
            bad.append((p, k, v))
            break
print(f"Found {len(bad)}")
for p, k, v in bad:
    print(f"\n[{p}] key len={len(k)} val len={len(v)}")
    print(f"KEY: {k[:100]}...")
    print(f"VAL: {v[:200]}...")
