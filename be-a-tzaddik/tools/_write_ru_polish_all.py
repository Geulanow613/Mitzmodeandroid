#!/usr/bin/env python3
"""Emit human/ru_polish_all.json — best-effort Cyrillic polish for every Latin-heavy RU value."""

from __future__ import annotations

import json
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT / "tools"))

from _ru_cyrillic_batch import (  # noqa: E402
    auto_polish_latin_entries,
    count_latin_chars,
    load_catalog,
    load_ru_entries,
)
from translation_repairs import repair_translation  # noqa: E402

OUT = ROOT / "data" / "translation-catalog" / "human" / "ru_polish_all.json"


def main() -> int:
    catalog = load_catalog()
    ru_entries = load_ru_entries()
    auto = auto_polish_latin_entries(catalog, ru_entries)

    # Second pass on already-polished values.
    merged = dict(ru_entries)
    merged.update(auto)
    auto2 = auto_polish_latin_entries(catalog, merged)
    for k, v in auto2.items():
        cur = auto.get(k, ru_entries.get(k, ""))
        if count_latin_chars(v) < count_latin_chars(cur):
            auto[k] = v

    # Force repair on any remaining Latin (even if count unchanged).
    fixes: dict[str, str] = {}
    for key in catalog:
        val = merged.get(key, key)
        if val == key or count_latin_chars(val) == 0:
            continue
        polished = val
        for _ in range(5):
            polished = repair_translation("ru", polished)
        if count_latin_chars(polished) <= count_latin_chars(val):
            fixes[key] = polished

    fixes.update(auto)
    OUT.parent.mkdir(parents=True, exist_ok=True)
    OUT.write_text(json.dumps({"ru": fixes}, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"wrote {len(fixes)} keys to {OUT.name}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
