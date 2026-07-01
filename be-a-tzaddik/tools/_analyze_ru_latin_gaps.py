#!/usr/bin/env python3
"""Dump top RU Latin hits not yet in ru_cyrillic_fixes.json."""

from __future__ import annotations

import json
import re
from collections import Counter
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
RU_PATH = ROOT / "shared/src/commonMain/composeResources/files/translations/ru.json"
FIXES_PATH = ROOT / "data/translation-catalog/human/ru_cyrillic_fixes.json"
OUT_PATH = ROOT / "tools/_ru_latin_gaps.json"

VAR_PATTERN = re.compile(r"\$[a-zA-Z_][a-zA-Z0-9_]*|\{[a-zA-Z_]+\}|\$\{[^}]+\}")
URL_PATTERN = re.compile(r"https?://[^\s]+|www\.[^\s]+")
ALLOWED = {
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
    fixes = json.loads(FIXES_PATH.read_text(encoding="utf-8")).get("ru", {})
    hits: list[tuple] = []
    word_counter: Counter[str] = Counter()

    for key, val in entries.items():
        if val in ALLOWED:
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
                sorted(set(latin_words), key=len, reverse=True),
                key,
                val,
            )
        )
        for w in latin_words:
            word_counter[w.lower()] += 1

    hits.sort(reverse=True)
    gaps = [h for h in hits if h[3] not in fixes]

    report = {
        "total_latin": len(hits),
        "in_fixes": len(hits) - len(gaps),
        "gaps": len(gaps),
        "top_words": word_counter.most_common(50),
        "top_gaps": [
            {
                "latin_chars": lc,
                "latin_words": wc,
                "words": words[:20],
                "key": k,
                "val": v,
            }
            for lc, wc, words, k, v in gaps[:80]
        ],
    }
    OUT_PATH.write_text(json.dumps(report, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"total_latin={len(hits)} gaps={len(gaps)} wrote {OUT_PATH}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
