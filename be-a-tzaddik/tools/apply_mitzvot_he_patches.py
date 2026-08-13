#!/usr/bin/env python3
"""Apply full Hebrew translations to truncated mitzvot_004 entries."""
from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
MITZVOT = ROOT / "data" / "translation-catalog" / "human" / "mitzvot_004.json"
PATCHES = ROOT / "data" / "translation-catalog" / "human" / "mitzvot_004_he_patches.json"


def main() -> None:
    patches = json.loads(PATCHES.read_text(encoding="utf-8"))
    data = json.loads(MITZVOT.read_text(encoding="utf-8"))
    he_patches = patches.get("he", {})
    updated = 0
    for key, tr in he_patches.items():
        if key in data.get("he", {}):
            old = len(data["he"][key])
            data["he"][key] = tr
            updated += 1
            print(f"  {old} -> {len(tr)}: {key[:60]}...")
    MITZVOT.write_text(json.dumps(data, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"Updated {updated} entries")


if __name__ == "__main__":
    main()
