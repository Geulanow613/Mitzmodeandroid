#!/usr/bin/env python3
"""Write human/he_paragraph_fixes.json — late-win overrides for Argos-corrupted long paragraphs."""

from __future__ import annotations

import json
from pathlib import Path

from _he_paragraph_fixes import build_he
from _paragraph_fixes_data import ES, FR, RU

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / "data" / "translation-catalog" / "human" / "he_paragraph_fixes.json"
CATALOG = ROOT / "data" / "translation-catalog" / "strings.json"


def resolve_by_prefix(src: dict[str, str], strings: list[str]) -> dict[str, str]:
    out: dict[str, str] = {}
    for key, val in src.items():
        if key in strings:
            out[key] = val
            continue
        matches = [s for s in strings if s.startswith(key[:60]) or key.startswith(s[:60])]
        if len(matches) == 1:
            out[matches[0]] = val
        elif key in strings:
            out[key] = val
        else:
            # exact prefix match on catalog
            pref = key[:80]
            hit = next((s for s in strings if s == key or s.startswith(pref)), None)
            if hit:
                out[hit] = val
            else:
                out[key] = val
    return out


def main() -> None:
    strings: list[str] = json.loads(CATALOG.read_text(encoding="utf-8"))["strings"]
    payload = {
        "he": build_he(),
        "ru": resolve_by_prefix(RU, strings),
        "es": resolve_by_prefix(ES, strings),
        "fr": resolve_by_prefix(FR, strings),
    }
    OUT.write_text(json.dumps(payload, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    for lang in payload:
        print(f"he_paragraph_fixes.json: {lang}={len(payload[lang])} entries")


if __name__ == "__main__":
    main()
