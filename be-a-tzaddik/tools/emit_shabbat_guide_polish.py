# -*- coding: utf-8 -*-
"""Emit human-quality Shabbat Guide polish for he, es, fr, ru."""
from __future__ import annotations

import json
from pathlib import Path

from shabbat_guide_es_fr_ru_polish_data import ES_POLISH, FR_POLISH, RU_POLISH
from shabbat_guide_he_polish_data import HE_POLISH

ROOT = Path(__file__).resolve().parents[1]
GUIDE = ROOT / "data" / "translation-catalog" / "human" / "shabbat_guide.json"
OUT = ROOT / "data" / "translation-catalog" / "human" / "shabbat_guide_polish.json"


def main() -> None:
    guide = json.loads(GUIDE.read_text(encoding="utf-8"))
    ordered_keys = list(guide["he"].keys())

    polishes = {"he": HE_POLISH, "es": ES_POLISH, "fr": FR_POLISH, "ru": RU_POLISH}
    for lang, polish in polishes.items():
        missing = [k for k in ordered_keys if k not in polish]
        if missing:
            print(f"ERROR: {len(missing)} keys missing from {lang.upper()}_POLISH:")
            for k in missing:
                print(f"  - {k[:80]}...")
            raise SystemExit(1)
        extra = set(polish.keys()) - set(ordered_keys)
        if extra:
            print(f"WARNING: {len(extra)} extra keys in {lang.upper()}_POLISH")

    payload = {lang: {k: polishes[lang][k] for k in ordered_keys} for lang in polishes}
    OUT.write_text(json.dumps(payload, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"OK: wrote {len(ordered_keys)} entries x 4 langs -> {OUT}")


if __name__ == "__main__":
    main()
