#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""Convert UTF-8 Hebrew lines to unicode-escape Python literals."""
import json
import sys
from pathlib import Path

src = Path(__file__).with_name("hebrew_raw.txt")
if not src.exists():
    sys.exit("Create hebrew_raw.txt with strings 6-25 separated by ---")

parts = [p.strip() for p in src.read_text(encoding="utf-8").split("\n---\n") if p.strip()]
for i, s in enumerate(parts, start=6):
    bad = [c for c in s if c.isascii() and c.isalpha()]
    if bad:
        print(f"# String {i} BAD Latin: {sorted(set(bad))}", file=sys.stderr)
    esc = json.dumps(s, ensure_ascii=True)
    print(f"S{i} = {esc}")
