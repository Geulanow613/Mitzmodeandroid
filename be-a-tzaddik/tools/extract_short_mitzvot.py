#!/usr/bin/env python3
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
m4 = json.loads((ROOT / "data/translation-catalog/human/mitzvot_004.json").read_text(encoding="utf-8"))
prefixes = [
    "Learn about the Tithe-to-Table",
    "Prepare for the ultimate harvest parade",
    "Learn about the prohibition of 'chukot ha'akum'",
    "Learn about the Rambam's specific",
    "Learn the specific conditions under which speaking negatively",
    "Learn about the prohibition of receiving lashon hara",
]
out = {}
for k, v in m4["he"].items():
    for p in prefixes:
        if k.startswith(p):
            out[k] = {"en_len": len(k), "he_len": len(v), "he": v}
            break
print(json.dumps(out, ensure_ascii=False, indent=2))
