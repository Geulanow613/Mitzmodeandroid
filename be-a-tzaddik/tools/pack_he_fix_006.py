#!/usr/bin/env python3
"""Pack he_fix_006 strings from delimited UTF-8 source into JSON."""

from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1] / "data" / "translation-catalog"
SRC = Path(__file__).resolve().parent / "he_fix_006_strings.txt"
HUMAN = ROOT / "human"
MIXED_RE = re.compile(r"[\u0590-\u05FF]+[A-Za-z]+|[A-Za-z]+[\u0590-\u05FF]+")


def main() -> None:
    text = SRC.read_text(encoding="utf-8")
    strings = [s.strip() for s in text.split("\n---\n") if s.strip()]
    keys = json.loads((ROOT / "_keys_he_fix_006.json").read_text(encoding="utf-8"))
    if len(strings) != len(keys):
        raise SystemExit(f"got {len(strings)} strings, need {len(keys)}")
    bad = []
    for i, s in enumerate(strings):
        for m in MIXED_RE.finditer(s):
            bad.append((i + 1, m.group()))
    if bad:
        raise SystemExit(f"mixed script: {bad}")
    out = HUMAN / "he_fix_006_he_only.json"
    out.write_text(json.dumps({"he": strings}, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"OK {out.name}: {len(strings)} strings")


if __name__ == "__main__":
    main()
