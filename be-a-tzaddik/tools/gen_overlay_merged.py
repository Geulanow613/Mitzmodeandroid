#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""Generate overlay_007_013.json from translation parts."""

from __future__ import annotations

import json
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
NEEDS = ROOT / "data" / "translation-catalog" / "_needs_007_013.json"
OUT = ROOT / "data" / "translation-catalog" / "shards" / "overlay_007_013.json"
LANGS = ("he", "es", "fr", "ru")

sys.path.insert(0, str(Path(__file__).resolve().parent))
from gen_overlay_007_013 import T  # noqa: E402
from overlay_parts.remaining import PART  # noqa: E402


def main() -> None:
    needs: list[str] = json.loads(NEEDS.read_text(encoding="utf-8"))
    merged = dict(T)
    merged.update(PART)
    missing = [s for s in needs if s not in merged]
    if missing:
        raise SystemExit(f"Missing {len(missing)} translations. First: {missing[0]!r}")
    overlay = {lang: {s: merged[s][lang] for s in needs} for lang in LANGS}
    OUT.parent.mkdir(parents=True, exist_ok=True)
    OUT.write_text(json.dumps(overlay, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"Wrote {OUT} with {len(needs)} strings x {len(LANGS)} langs")


if __name__ == "__main__":
    main()
