#!/usr/bin/env python3
"""Export batch 17: remaining guillemet glossary terms not in top50."""
from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
RU_PATH = ROOT / "shared/src/commonMain/composeResources/files/translations/ru.json"
TOP50_PATH = ROOT / "data/translation-catalog/human/ru_cyrillic_top50.json"
OUT = Path(__file__).parent / "_batch17_candidates.json"
KEYS_OUT = Path(__file__).parent / "_batch17_keys.json"

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
    entries: dict[str, str] = json.loads(RU_PATH.read_text(encoding="utf-8"))["entries"]
    top50 = set(json.loads(TOP50_PATH.read_text(encoding="utf-8")).get("ru", {}))

    latin: list[tuple[int, str, str]] = []
    for key, val in entries.items():
        if key in top50 or key in SKIP_KEYS:
            continue
        stripped = strip_allowed(val)
        lc = sum(1 for c in stripped if c.isascii() and c.isalpha())
        if lc:
            latin.append((lc, key, val))
    latin.sort(key=lambda x: (-x[0], x[1]))

    guil: list[str] = []
    for key, val in sorted(entries.items()):
        if key in top50 or key in SKIP_KEYS:
            continue
        if val.startswith("«") and val.endswith("»"):
            guil.append(key)

    keys: list[str] = []
    for _, k, _ in latin:
        if k not in keys:
            keys.append(k)
    for k in guil:
        if len(keys) >= 50:
            break
        if k not in keys:
            keys.append(k)
    keys = keys[:50]

    candidates = [{"key": k, "val": entries[k]} for k in keys]
    OUT.write_text(json.dumps(candidates, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    KEYS_OUT.write_text(json.dumps(keys, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"Wrote {len(candidates)} candidates ({len(latin)} latin + guillemet fill)")


if __name__ == "__main__":
    main()
