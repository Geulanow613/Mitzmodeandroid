#!/usr/bin/env python3
"""Restore embedded Hebrew parentheticals from English keys in shard translations."""

from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SHARDS = ROOT / "data" / "translation-catalog" / "shards"
BATCHES = ROOT / "data" / "translation-catalog" / "batches"
LANGS = ("he", "es", "fr", "ru")

HEBREW_PAREN = re.compile(r"\([^)]*[\u0590-\u05ff][^)]*\)")


def fix_translation(en: str, tr: str) -> str:
    out = tr
    for match in HEBREW_PAREN.finditer(en):
        correct = match.group(0)
        if correct in out:
            continue
        # Replace the next parenthetical that contains any Hebrew or Latin corruption after Hebrew marker.
        corrupt = re.search(r"\([^)]*[\u0590-\u05ff\u0080-\u024fA-Za-z][^)]*\)", out)
        if corrupt:
            out = out[: corrupt.start()] + correct + out[corrupt.end() :]
    return out


def fix_shard(batch_num: int) -> None:
    strings = json.loads((BATCHES / f"batch_{batch_num:03d}.json").read_text(encoding="utf-8"))["strings"]
    path = SHARDS / f"batch_{batch_num:03d}.json"
    shard = json.loads(path.read_text(encoding="utf-8"))
    fixed = 0
    for s in strings:
        if not HEBREW_PAREN.search(s):
            continue
        for lang in LANGS:
            before = shard[lang][s]
            after = fix_translation(s, before)
            if after != before:
                shard[lang][s] = after
                fixed += 1
    path.write_text(json.dumps(shard, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"batch_{batch_num:03d}: fixed {fixed} entries")


def main() -> None:
    for n in (24, 25):
        fix_shard(n)


if __name__ == "__main__":
    main()
