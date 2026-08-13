#!/usr/bin/env python3
"""Scan Hebrew bundle for machine-translation disasters."""
from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
BUNDLE = ROOT / "shared/src/commonMain/composeResources/files/translations/he.json"
CATALOG = ROOT / "data/translation-catalog/strings.json"

PATTERNS: list[tuple[str, re.Pattern[str]]] = [
    ("chofetz_life", re.compile(r"Chofetz Life|צ'רצ", re.I)),
    ("english_avak", re.compile(r"avak lashon hara", re.I)),
    ("mincha_translit", re.compile(r"מינצ'ה|Savereva")),
    ("tsaddik_sandwich", re.compile(r"צנצ'וט")),
    ("spaced_nikud", re.compile(r"[א-ת]\s+[א-ת]\s+[ְ-ֹ]")),
    ("english_isaac", re.compile(r"\(Isaac\)")),
    ("english_menachot", re.compile(r"Menachot 43b")),
    ("tachanun_garbage", re.compile(r"תחנun|שacharit")),
    ("english_clause", re.compile(r"\bThe Talmud\b")),
    ("g_d_leak", re.compile(r"\bG-d\b")),
    ("latin_shacharit", re.compile(r"\bShacharit\b")),
    ("english_start", re.compile(r"^The [A-Z].{30,}")),
    ("high_latin_ratio", None),  # handled separately
    ("mincha_translit", re.compile(r"מינצ'ה|Savereva")),
    ("chofetz_life", re.compile(r"Chofetz Life|צ'רצ")),
    ("tsaddik_sandwich", re.compile(r"צנצ'וט")),
]


def latin_ratio(text: str) -> float:
    letters = [c for c in text if c.isalpha()]
    if not letters:
        return 0.0
    return sum(1 for c in letters if c.isascii()) / len(letters)


def main() -> None:
    required = json.loads(CATALOG.read_text(encoding="utf-8"))["strings"]
    entries = json.loads(BUNDLE.read_text(encoding="utf-8"))["entries"]
    by_pat: dict[str, list[str]] = {}
    mostly_en: list[str] = []
    for key in required:
        tr = entries.get(key, key)
        if tr == key:
            continue
        for name, pat in PATTERNS:
            if pat is None:
                continue
            if pat.search(tr):
                by_pat.setdefault(name, []).append(key[:100])
        if len(key) > 200 and latin_ratio(tr) > 0.22:
            mostly_en.append(key[:100])
    print("Pattern hits:")
    for name, keys in sorted(by_pat.items(), key=lambda x: -len(x[1])):
        print(f"  {name}: {len(keys)}")
        for k in keys[:2]:
            print(f"    - {k}...")
    print(f"High Latin ratio (>0.22, len>200): {len(mostly_en)}")
    for k in mostly_en[:5]:
        print(f"  - {k}...")


if __name__ == "__main__":
    main()
