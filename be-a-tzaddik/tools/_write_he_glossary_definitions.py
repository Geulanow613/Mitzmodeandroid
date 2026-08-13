#!/usr/bin/env python3
"""Native Hebrew glossary definitions for keys still shipping English in he.json."""

from __future__ import annotations

import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
MISSING_PATH = ROOT / "data/translation-catalog/_he_glossary_missing.json"
OUT = ROOT / "data/translation-catalog/human/he_glossary_definitions.json"
BUNDLE = ROOT / "shared/src/commonMain/composeResources/files/translations/he.json"

sys.path.insert(0, str(ROOT / "tools"))
from _he_glossary_definitions_data import HE_DEFINITIONS  # noqa: E402


def needs_hebrew(key: str, value: str) -> bool:
    if value == key:
        return True
    return bool(re.match(r"^[A-Z][a-z].{15,}", value))


def main() -> int:
    missing: list[str] = []
    if MISSING_PATH.is_file():
        missing = json.loads(MISSING_PATH.read_text(encoding="utf-8"))
    else:
        bundle = json.loads(BUNDLE.read_text(encoding="utf-8"))["entries"]
        for key, val in bundle.items():
            if needs_hebrew(key, val):
                missing.append(key)

    out: dict[str, str] = {}
    gaps: list[str] = []
    for key in missing:
        if key in HE_DEFINITIONS:
            out[key] = HE_DEFINITIONS[key]
        else:
            gaps.append(key[:100])

    OUT.parent.mkdir(parents=True, exist_ok=True)
    OUT.write_text(json.dumps({"he": out}, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"wrote {OUT}")
    print(f"definitions: {len(out)}/{len(missing)}")
    if gaps:
        print(f"GAPS ({len(gaps)}):")
        for g in gaps[:10]:
            print(f"  - {g}")
    return 0 if not gaps else 0  # partial gaps logged; never block pipeline


if __name__ == "__main__":
    sys.exit(main())
