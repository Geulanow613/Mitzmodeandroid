#!/usr/bin/env python3
import json
import re
import sys
from pathlib import Path

sys.stdout.reconfigure(encoding="utf-8")
root = Path(__file__).resolve().parents[1] / "data" / "translation-catalog"
LANGS = ("he", "es", "fr", "ru")
errors: list[str] = []

for n in (18, 19, 23, 24, 25):
    batch = json.loads((root / "batches" / f"batch_{n:03d}.json").read_text(encoding="utf-8"))["strings"]
    shard = json.loads((root / "shards" / f"batch_{n:03d}.json").read_text(encoding="utf-8"))
    for lang in LANGS:
        for s in batch:
            if s not in shard[lang]:
                errors.append(f"batch_{n:03d} {lang}: missing key {s[:50]!r}")
            elif shard[lang][s] == s and len(s) > 20:
                hebrew = sum(1 for c in s if "\u0590" <= c <= "\u05ff")
                if hebrew < 30:
                    errors.append(f"batch_{n:03d} {lang}: untranslated {s[:50]!r}")
    for s in batch:
        if "${" in s:
            for lang in LANGS:
                t = shard[lang][s]
                for part in re.findall(r"\$\{[^{}]+\}", s):
                    if part not in t:
                        errors.append(f"batch_{n:03d} {lang}: lost template {part}")

print(f"errors: {len(errors)}")
for e in errors[:20]:
    print("-", e)

# samples
for n in (18, 23, 25):
    batch = json.loads((root / "batches" / f"batch_{n:03d}.json").read_text(encoding="utf-8"))["strings"]
    shard = json.loads((root / "shards" / f"batch_{n:03d}.json").read_text(encoding="utf-8"))
    s = batch[1]
    print(f"\n=== batch_{n:03d} sample ===")
    for lang in LANGS:
        print(f"{lang}: {shard[lang][s][:140]}...")
