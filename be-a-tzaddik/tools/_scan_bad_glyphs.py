#!/usr/bin/env python3
"""Scan shipped translation bundles for corrupt Unicode and known Hebrew bugs.

Designed to catch what actually reaches the app — not every draft shard.
Run automatically at end of compile_full_bundled.py; exits non-zero on hits.
"""
from __future__ import annotations

import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]

SHIPPED = [
    ROOT / "shared/src/commonMain/composeResources/files/translations",
    ROOT / "data/bundled-translations",
]

FORBIDDEN_CHARS: list[tuple[str, str]] = [
    ("\u05f7", "HEBREW PUNCTUATION QAMATS (U+05F7) — corrupt gershayim"),
    ("\ufffd", "REPLACEMENT CHARACTER"),
    ("\u25a1", "WHITE SQUARE / tofu"),
    ("\ufffc", "OBJECT REPLACEMENT"),
]

BAD_NUMERAL = re.compile(r"[\u05d0-\u05ea]\u05f7[\u05d0-\u05ea]")
HYBRID_HE_LATIN = re.compile(r"[\u05d0-\u05ea][a-zA-Z]{2,}|[a-zA-Z]{2,}[\u05d0-\u05ea]")

# Garbage that has appeared in Hebrew UI / holiday titles (not valid Hebrew).
HEBREW_GARBAGE = [
    (r"בתבֵת", "corrupt Tevet spelling"),
    (r"(?<=[\u05d0-\u05ea])תממוז", "double-mem Tammuz typo"),
    (r"טאבט", "Latinized Tevet in Hebrew"),
    (r"שיווה אסאר", "garbled Shiva Asar"),
    (r"מהרו של", "garbled Fast of"),
]

GENERATOR_SKIP = {"_scan_bad_glyphs.py", "translation_repairs.py"}


def scan_hebrew_value(key: str, value: str, rel: str) -> list[tuple[str, str, str]]:
    hits: list[tuple[str, str, str]] = []
    for ch, desc in FORBIDDEN_CHARS:
        if ch in value:
            hits.append((rel, desc, f'key="{key[:60]}…" value="{value[:100]}"'))
    if BAD_NUMERAL.search(value):
        hits.append((rel, "corrupt Hebrew numeral (U+05F7)", f'key="{key[:60]}" value="{value[:100]}"'))
    for pattern, desc in HEBREW_GARBAGE:
        if re.search(pattern, value):
            hits.append((rel, desc, f'key="{key[:60]}" value="{value[:120]}"'))
    if HYBRID_HE_LATIN.search(value):
        hits.append((rel, "Hebrew/Latin hybrid glyph (e.g. פesach)", f'key="{key[:60]}" value="{value[:120]}"'))
    return hits


def scan_shipped_hebrew() -> list[tuple[str, str, str]]:
    hits: list[tuple[str, str, str]] = []
    for base in SHIPPED:
        he_path = base / "he.json"
        if not he_path.exists():
            continue
        rel = str(he_path.relative_to(ROOT))
        data = json.loads(he_path.read_text(encoding="utf-8"))
        for key, value in data.get("entries", {}).items():
            if not isinstance(value, str):
                continue
            hits.extend(scan_hebrew_value(key, value, rel))
    return hits


def scan_generators() -> list[tuple[str, str, str]]:
    hits: list[tuple[str, str, str]] = []
    tools = ROOT / "tools"
    if not tools.exists():
        return hits
    for path in sorted(tools.glob("*.py")):
        if path.name in GENERATOR_SKIP:
            continue
        text = path.read_text(encoding="utf-8")
        if "\u05f7" not in text and r"\u05f7" not in text:
            continue
        for i, line in enumerate(text.splitlines(), 1):
            if "\u05f7" in line or r"\u05f7" in line:
                hits.append((
                    str(path.relative_to(ROOT)),
                    "generator embeds U+05F7",
                    f"line {i}: {line.strip()[:120]}",
                ))
    return hits


def main() -> None:
    hits = scan_shipped_hebrew() + scan_generators()
    print("=== SHIPPED HEBREW GLYPH SCAN ===")
    for rel, desc, detail in hits:
        print(f"{rel} [{desc}]")
        print(f"  {detail}")
    print(f"\nTotal hits: {len(hits)}")
    if hits:
        sys.exit(1)


if __name__ == "__main__":
    main()
