#!/usr/bin/env python3
"""Validate shards 007-013."""

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1] / "data" / "translation-catalog"
LANGS = ("he", "es", "fr", "ru")
errors: list[str] = []

for i in range(7, 14):
    batch = json.loads((ROOT / "batches" / f"batch_{i:03d}.json").read_text(encoding="utf-8"))
    shard = json.loads((ROOT / "shards" / f"batch_{i:03d}.json").read_text(encoding="utf-8"))
    n = len(batch["strings"])
    for s in batch["strings"]:
        for lang in LANGS:
            if s not in shard[lang]:
                errors.append(f"batch_{i:03d} missing {lang}: {s[:50]!r}")
            elif not shard[lang][s].strip():
                errors.append(f"batch_{i:03d} empty {lang}: {s[:50]!r}")
    print(f"batch_{i:03d}: {n} strings, all langs present: {all(s in shard[l] for s in batch['strings'] for l in LANGS)}")

print(f"Total errors: {len(errors)}")
for e in errors[:10]:
    print(" ", e)
