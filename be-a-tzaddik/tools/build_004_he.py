# -*- coding: utf-8 -*-
import json
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
DATA = Path(__file__).resolve().parent / "he_004_data.txt"
OUT = ROOT / "data/translation-catalog/human/mitzvot_004_he_only.json"

text = DATA.read_text(encoding="utf-8")
strings = [s.strip() for s in text.split("\n---\n") if s.strip()]

for i, s in enumerate(strings):
    bad = [c for c in s if c.isascii() and c.isalpha()]
    if bad:
        print(f"String {i+1} has Latin: {set(bad)}", file=sys.stderr)
        sys.exit(1)

if len(strings) != 25:
    print(f"Expected 25 strings, got {len(strings)}", file=sys.stderr)
    sys.exit(1)

OUT.write_text(json.dumps({"he": strings}, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
print(f"OK: wrote {len(strings)} strings")
