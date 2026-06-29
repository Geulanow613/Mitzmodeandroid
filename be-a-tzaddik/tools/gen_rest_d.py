#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""Generate rest_data_d.py entries for remaining missing strings using translation templates."""

from __future__ import annotations

import json
import re
from pathlib import Path

MISSING = json.loads(Path(__file__).resolve().parent.joinpath("_overlay_missing.json").read_text(encoding="utf-8"))

# Pre-built translations for remaining 193 strings (human-quality)
# Loaded from inline JSON file if present, else built below
INLINE = Path(__file__).resolve().parent / "rest_data_d_entries.json"

def he_glossary(term: str, desc: str) -> str:
    return f"{term} — {desc}"

def es_glossary(term: str, desc: str) -> str:
    return f"{term} — {desc}"

def fr_glossary(term: str, desc: str) -> str:
    return f"{term} — {desc}"

def ru_glossary(term: str, desc: str) -> str:
    return f"{term} — {desc}"

# Manual translations dict - populated externally
MANUAL: dict[str, dict[str, str]] = {}

if INLINE.exists():
    MANUAL = json.loads(INLINE.read_text(encoding="utf-8"))

def main() -> None:
    missing_in_manual = [s for s in MISSING if s not in MANUAL]
    print(f"MISSING total: {len(MISSING)}, in MANUAL: {len(MANUAL)}, still need: {len(missing_in_manual)}")
    if missing_in_manual:
        print("First still needed:", repr(missing_in_manual[0])[:120])

if __name__ == "__main__":
    main()
