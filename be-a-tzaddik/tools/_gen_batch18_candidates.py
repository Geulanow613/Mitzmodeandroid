#!/usr/bin/env python3
"""Export batch 18: top RU Latin gaps not yet in ru_deep_fixes."""
from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
GAPS_PATH = Path(__file__).parent / "_ru_latin_gaps.json"
DEEP_PATH = ROOT / "data/translation-catalog/human/ru_deep_fixes.json"
CYRILLIC_PATH = ROOT / "data/translation-catalog/human/ru_cyrillic_fixes.json"
OUT = Path(__file__).parent / "_batch18_candidates.json"
KEYS_OUT = Path(__file__).parent / "_batch18_keys.json"

SKIP_KEYS = {
    "Beardy Top Productions",
    "www.beardy.top",
    "https://www.beardy.top",
    "XL",
    "Rav",
    "e.g., Sarah B.",
    "Listen to more Jewish music from G.E.U.L.A",
    "Performed by G.E.U.L.A © 2026",
}


def main() -> None:
    gaps = json.loads(GAPS_PATH.read_text(encoding="utf-8"))
    deep = json.loads(DEEP_PATH.read_text(encoding="utf-8")).get("ru", {})
    cyrillic = json.loads(CYRILLIC_PATH.read_text(encoding="utf-8")).get("ru", {})

    keys: list[str] = []
    candidates: list[dict[str, str]] = []
    for item in gaps["top_gaps"]:
        key = item["key"]
        if key in SKIP_KEYS:
            continue
        if key.startswith("$translated") or key.startswith("{weekday}") or key.startswith("{label}") or key.startswith("{name}"):
            continue
        if key in deep or key in cyrillic:
            continue
        if key in keys:
            continue
        keys.append(key)
        candidates.append({"key": key, "val": item["val"], "latin_chars": item["latin_chars"]})
        if len(keys) >= 50:
            break

    OUT.write_text(json.dumps(candidates, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    KEYS_OUT.write_text(json.dumps(keys, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"Wrote {len(keys)} candidates (skipped keys already in deep/cyrillic fixes)")


if __name__ == "__main__":
    main()
