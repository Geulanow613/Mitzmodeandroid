#!/usr/bin/env python3
"""Convert he_fix_*_he_only.json into human/he_fix_*.json shard format."""

from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
HUMAN = ROOT / "data/translation-catalog/human"
CATALOG = ROOT / "data/translation-catalog"


def main() -> None:
    for only in sorted(HUMAN.glob("he_fix_*_he_only.json")):
        batch = only.name.replace("_he_only.json", "")
        keys_path = CATALOG / f"_keys_{batch}.json"
        if not keys_path.is_file():
            print(f"SKIP {batch}: no {keys_path.name}")
            continue
        keys = json.loads(keys_path.read_text(encoding="utf-8"))
        he_list = json.loads(only.read_text(encoding="utf-8"))["he"]
        if len(he_list) != len(keys):
            print(f"FAIL {batch}: {len(he_list)} translations vs {len(keys)} keys")
            continue
        out = {"he": {keys[i]: he_list[i] for i in range(len(keys))}}
        dest = HUMAN / f"{batch}.json"
        dest.write_text(json.dumps(out, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
        print(f"OK {dest.name}: {len(keys)} keys")


if __name__ == "__main__":
    main()
