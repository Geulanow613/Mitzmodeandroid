#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""Generate educational_he_data.json — run after fixing TRANSLATIONS list."""
from __future__ import annotations

import json
import re
from pathlib import Path

TOOLS = Path(__file__).resolve().parent
KEYS_PATH = TOOLS / "_edu_keys_dump.json"
OUT_JSON = TOOLS / "educational_he_data.json"

# Import translations from the writer module once complete
from _write_edu_he import TRANSLATIONS  # noqa: E402

LATIN = re.compile(r"[A-Za-z]")


def main() -> None:
    keys: list[str] = json.loads(KEYS_PATH.read_text(encoding="utf-8"))
    if len(TRANSLATIONS) != len(keys):
        raise ValueError(f"Expected {len(keys)} translations, got {len(TRANSLATIONS)}")
    for i, tr in enumerate(TRANSLATIONS, 1):
        allow = "$bedikatLeadIn" if "$bedikatLeadIn" in tr else ""
        cleaned = tr.replace(allow, "") if allow else tr
        hits = LATIN.findall(cleaned)
        if hits:
            raise ValueError(f"Translation {i} has Latin: {set(hits)}")
    out = dict(zip(keys, TRANSLATIONS, strict=True))
    OUT_JSON.write_text(json.dumps(out, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    nlines = len(OUT_JSON.read_text(encoding="utf-8").splitlines())
    print(f"Wrote {OUT_JSON.name}: {len(out)} keys, {nlines} lines")


if __name__ == "__main__":
    main()
