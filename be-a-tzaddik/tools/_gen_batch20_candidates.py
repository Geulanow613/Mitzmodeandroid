#!/usr/bin/env python3
"""Export batch 20: hybrid Latin-in-Cyrillic fixes and remaining polishable gaps."""
from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
GAPS_PATH = Path(__file__).parent / "_ru_latin_gaps.json"
DEEP_PATH = ROOT / "data/translation-catalog/human/ru_deep_fixes.json"
CYRILLIC_PATH = ROOT / "data/translation-catalog/human/ru_cyrillic_fixes.json"
OUT = Path(__file__).parent / "_batch20_candidates.json"
KEYS_OUT = Path(__file__).parent / "_batch20_keys.json"

SKIP_KEYS = {
    "Beardy Top Productions",
    "www.beardy.top",
    "https://www.beardy.top",
    "XL",
    "Rav",
    "e.g., Sarah B.",
    "Listen to more Jewish music from G.E.U.L.A",
    "Performed by G.E.U.L.A © 2026",
    "G.E.U.L.A",
    "\\s*/\\s*",
    "Use GPS for location",
    "Choose city now (no GPS)",
    "Current location: {place} (GPS)",
    "Current location: waiting for GPS…",
    "No location set — turn on GPS or choose a city below",
    "🇮🇱 Israel customs active — detected from your GPS location (1-day Yom Tov, Israel parsha cycle).",
    "Uses 1-day Yom Tov customs and the Israel parsha cycle. Select a city above or enable GPS for automatic detection.",
}


def main() -> None:
    gaps = json.loads(GAPS_PATH.read_text(encoding="utf-8"))

    keys: list[str] = []
    candidates: list[dict] = []
    for item in gaps["top_gaps"]:
        key = item["key"]
        if key in SKIP_KEYS:
            continue
        if key.startswith("$translated") or key.startswith("{weekday}") or key.startswith("{label}") or key.startswith("{name}"):
            continue
        if key in keys:
            continue
        keys.append(key)
        candidates.append({"key": key, "val": item["val"], "latin_chars": item["latin_chars"]})
        if len(keys) >= 50:
            break

    OUT.write_text(json.dumps(candidates, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    KEYS_OUT.write_text(json.dumps(keys, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"Wrote {len(keys)} candidates")


if __name__ == "__main__":
    main()
