#!/usr/bin/env python3
"""Build remaining he_fix_006 strings from UTF-8 hex blobs."""

from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1] / "data" / "translation-catalog"
TOOLS = Path(__file__).resolve().parent
HUMAN = ROOT / "human"
MIXED_RE = re.compile(r"[\u0590-\u05FF]+[A-Za-z]+|[A-Za-z]+[\u0590-\u05FF]+")


BLOBS = Path(__file__).resolve().parent / "he_fix_006_blobs.json"

REST = json.loads(BLOBS.read_text(encoding="utf-8"))

he_fix_006 = [
    s.strip()
    for s in (TOOLS / "he_fix_006_strings.txt").read_text(encoding="utf-8").split("\n---\n")
    if s.strip()
]
he_fix_006.extend(REST)


def main() -> None:
    keys = json.loads((ROOT / "_keys_he_fix_006.json").read_text(encoding="utf-8"))
    if len(he_fix_006) != len(keys):
        raise SystemExit(f"{len(he_fix_006)} != {len(keys)}")
    bad = [(i + 1, m.group()) for i, s in enumerate(he_fix_006) for m in MIXED_RE.finditer(s)]
    if bad:
        raise SystemExit(f"mixed: {bad}")
    out = HUMAN / "he_fix_006_he_only.json"
    out.write_text(json.dumps({"he": he_fix_006}, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"OK {out.name}: {len(he_fix_006)}")


if __name__ == "__main__":
    main()
