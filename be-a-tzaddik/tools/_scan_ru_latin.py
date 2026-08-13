#!/usr/bin/env python3
"""Scan ru.json values for Latin letters (excluding $vars and {placeholders})."""

from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
RU_PATH = ROOT / "shared/src/commonMain/composeResources/files/translations/ru.json"

VAR_PATTERN = re.compile(r"\$[a-zA-Z_][a-zA-Z0-9_]*|\{[a-zA-Z_]+\}|\$\{[^}]+\}")
URL_PATTERN = re.compile(r"https?://[^\s]+|www\.[^\s]+")
ALLOWED_VALUES = {
    "XL",
    "Rav",
    "www.beardy.top",
    "https://www.beardy.top",
    "Beardy Top Productions",
    "$mitzvotCount",
}


def strip_allowed(text: str) -> str:
    t = text
    for v in URL_PATTERN.findall(t):
        t = t.replace(v, "")
    for v in VAR_PATTERN.findall(t):
        t = t.replace(v, "")
    return t.replace("\\n", "")


def main() -> int:
    entries = json.loads(RU_PATH.read_text(encoding="utf-8"))["entries"]
    hits: list[tuple[int, int, list[str], str, str]] = []

    for key, val in entries.items():
        if val in ALLOWED_VALUES:
            continue
        stripped = strip_allowed(val)
        latin_words = re.findall(r"[a-zA-Z]+", stripped)
        if not latin_words:
            continue
        latin_chars = sum(1 for c in stripped if c.isascii() and c.isalpha())
        hits.append(
            (
                latin_chars,
                len(latin_words),
                sorted(set(latin_words), key=len, reverse=True)[:20],
                key,
                val,
            )
        )

    hits.sort(reverse=True)
    print(f"Total entries: {len(entries)}")
    print(f"Entries with Latin: {len(hits)}")
    print()
    print("TOP 20 worst by latin char count:")
    for i, (lc, wc, words, k, v) in enumerate(hits[:20]):
        print(f"{i + 1}. latin_chars={lc} words={wc}")
        print(f"   key: {k[:120]}")
        print(f"   val: {v[:180]}")
        print(f"   latin: {words}")
        print()
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
