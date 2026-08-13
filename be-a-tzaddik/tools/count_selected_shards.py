#!/usr/bin/env python3
import json
import sys
from pathlib import Path

sys.stdout.reconfigure(encoding="utf-8")
root = Path(__file__).resolve().parents[1] / "data" / "translation-catalog"
LANGS = ("he", "es", "fr", "ru")
total = 0
for n in (18, 19, 23, 24, 25):
    batch = json.loads((root / "batches" / f"batch_{n:03d}.json").read_text(encoding="utf-8"))["strings"]
    shard = json.loads((root / "shards" / f"batch_{n:03d}.json").read_text(encoding="utf-8"))
    ok = all(len(shard[l]) == len(batch) for l in LANGS)
    print(f"batch_{n:03d}: {len(batch)} keys, langs ok={ok}, shard langs={ {l: len(shard[l]) for l in LANGS} }")
    total += len(batch)
print(f"Grand total: {total} strings, {total*4} entries")
