#!/usr/bin/env python3
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
from build_shards_000_006 import build_he, load_bundled, load_extra, BATCH_DIR

bundled = load_bundled()
extra = load_extra()
all_strings = []
for i in range(7):
    all_strings.extend(json.loads((BATCH_DIR / f"batch_{i:03d}.json").read_text(encoding="utf-8"))["strings"])
he_map = build_he(extra["he"], bundled["he"], all_strings)
missing = [s for s in all_strings if s not in he_map]
print(len(missing))
for s in missing:
    print(repr(s))
