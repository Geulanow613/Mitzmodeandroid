# -*- coding: utf-8 -*-
"""Emit human-quality Hebrew polish for shabbat_guide.json."""
from __future__ import annotations

import json
from pathlib import Path

from shabbat_guide_he_polish_data import HE_POLISH

ROOT = Path(__file__).resolve().parents[1]
GUIDE = ROOT / "data" / "translation-catalog" / "human" / "shabbat_guide.json"
OUT = ROOT / "data" / "translation-catalog" / "human" / "shabbat_guide_he_polish.json"


def main() -> None:
    guide = json.loads(GUIDE.read_text(encoding="utf-8"))
    ordered_keys = list(guide["he"].keys())

    missing = [k for k in ordered_keys if k not in HE_POLISH]

    if missing:
        print(f"ERROR: {len(missing)} keys missing from HE_POLISH:")
        for k in missing:
            print(f"  - {k[:80]}...")
        raise SystemExit(1)

    extra = set(HE_POLISH.keys()) - set(ordered_keys)
    if extra:
        print(f"WARNING: {len(extra)} extra keys in HE_POLISH not in shabbat_guide.json")

    payload = {"he": {k: HE_POLISH[k] for k in ordered_keys}}
    OUT.write_text(json.dumps(payload, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"OK: wrote {len(payload['he'])} Hebrew entries -> {OUT}")


if __name__ == "__main__":
    main()
