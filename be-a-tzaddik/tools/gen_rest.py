#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""Generate overlay_parts/rest.py with all remaining translations."""

from __future__ import annotations

import json
from pathlib import Path

MISSING = json.loads(Path(__file__).resolve().parent.joinpath("_still_missing.json").read_text(encoding="utf-8"))

# Human-quality translations for all 327 remaining strings
# Format: english_key -> (he, es, fr, ru)
TRANSLATIONS: dict[str, tuple[str, str, str, str]] = {}

def T(he: str, es: str, fr: str, ru: str) -> tuple[str, str, str, str]:
    return (he, es, fr, ru)

# Populate TRANSLATIONS dict - see rest_data module
from rest_data import ENTRIES  # noqa: E402

TRANSLATIONS.update(ENTRIES)

def main() -> None:
    out = Path(__file__).resolve().parent / "overlay_parts" / "rest.py"
    lines = ["# -*- coding: utf-8 -*-", '"""All remaining overlay translations."""', "", "DATA: dict[str, dict[str, str]] = {"]
    for key in MISSING:
        if key not in TRANSLATIONS:
            raise SystemExit(f"Missing translation for: {key!r}")
        he, es, fr, ru = TRANSLATIONS[key]
        lines.append(f"    {json.dumps(key, ensure_ascii=False)}: {{")
        lines.append(f'        "he": {json.dumps(he, ensure_ascii=False)},')
        lines.append(f'        "es": {json.dumps(es, ensure_ascii=False)},')
        lines.append(f'        "fr": {json.dumps(fr, ensure_ascii=False)},')
        lines.append(f'        "ru": {json.dumps(ru, ensure_ascii=False)},')
        lines.append("    },")
    lines.append("}")
    lines.append("")
    out.write_text("\n".join(lines), encoding="utf-8")
    print(f"Wrote {out} with {len(MISSING)} entries")


if __name__ == "__main__":
    main()
