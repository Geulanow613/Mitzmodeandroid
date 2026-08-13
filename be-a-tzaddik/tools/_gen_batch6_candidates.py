#!/usr/bin/env python3
"""Export top 55 RU Latin gaps not yet in ru_cyrillic_top50.json."""
from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
RU_PATH = ROOT / "shared/src/commonMain/composeResources/files/translations/ru.json"
TOP50_PATH = ROOT / "data/translation-catalog/human/ru_cyrillic_top50.json"
OUT = Path(__file__).parent / "_batch6_candidates.json"

VAR_PATTERN = re.compile(r"\$[a-zA-Z_][a-zA-Z0-9_]*|\{[a-zA-Z_]+\}|\$\{[^}]+\}")
URL_PATTERN = re.compile(r"https?://[^\s]+|www\.[^\s]+")


def strip_allowed(text: str) -> str:
    t = text
    for v in URL_PATTERN.findall(t):
        t = t.replace(v, "")
    for v in VAR_PATTERN.findall(t):
        t = t.replace(v, "")
    return t.replace("\\n", "")


def main() -> None:
    entries = json.loads(RU_PATH.read_text(encoding="utf-8"))["entries"]
    top50 = set(json.loads(TOP50_PATH.read_text(encoding="utf-8")).get("ru", {}))
    hits: list[tuple[int, str, str]] = []
    for key, val in entries.items():
        stripped = strip_allowed(val)
        latin_chars = sum(1 for c in stripped if c.isascii() and c.isalpha())
        if latin_chars:
            hits.append((latin_chars, key, val))
    hits.sort(key=lambda x: (-x[0], x[1]))
    candidates = [
        {"latin_chars": lc, "key": k, "val": v}
        for lc, k, v in hits
        if k not in top50
    ][:55]
    OUT.write_text(json.dumps(candidates, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"Wrote {len(candidates)} candidates to {OUT.name}")


if __name__ == "__main__":
    main()
