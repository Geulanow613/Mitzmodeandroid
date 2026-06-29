#!/usr/bin/env python3
import json
import sys
from pathlib import Path

sys.stdout.reconfigure(encoding="utf-8")
ROOT = Path(__file__).resolve().parents[1]
data = json.loads((ROOT / "data/translation-catalog/human/mitzvot_001.json").read_text(encoding="utf-8"))
keys = json.loads((ROOT / "data/translation-catalog/_keys_001.json").read_text(encoding="utf-8"))

for i, key in enumerate(keys, 1):
    title = key.split("!")[0].split(".")[0][:70]
    print("\n" + "=" * 80)
    print(f"#{i} (cloud{i}) — {title}...")
    print("=" * 80)
    print("EN (source):")
    print(key if len(key) <= 600 else key[:600] + "...")
    for lang in ("he", "es", "fr", "ru"):
        tr = data[lang].get(key, "MISSING")
        print(f"\n{lang.upper()}:")
        print(tr if len(tr) <= 900 else tr[:900] + "...")
