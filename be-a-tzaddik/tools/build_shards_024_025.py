#!/usr/bin/env python3
"""Offline Argos translation for shards batch_024 and batch_025 only."""

from __future__ import annotations

import json
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
from auto_translate_shards import LANGS, get_translator, translate_text

ROOT = Path(__file__).resolve().parents[1]
BATCHES = ROOT / "data" / "translation-catalog" / "batches"
SHARDS = ROOT / "data" / "translation-catalog" / "shards"
TARGET = (24, 25)


def build_shard(batch_num: int, translators: dict) -> dict[str, dict[str, str]]:
    strings = json.loads((BATCHES / f"batch_{batch_num:03d}.json").read_text(encoding="utf-8"))["strings"]
    shard: dict[str, dict[str, str]] = {lang: {} for lang in LANGS}
    for i, s in enumerate(strings):
        print(f"  batch_{batch_num:03d} [{i + 1}/{len(strings)}]", file=sys.stderr)
        for lang in LANGS:
            shard[lang][s] = translate_text(s, lang, translators[lang])
    return shard


def main() -> None:
    SHARDS.mkdir(parents=True, exist_ok=True)
    translators = {lang: get_translator(lang) for lang in LANGS}
    for n in TARGET:
        print(f"Translating batch_{n:03d}...", file=sys.stderr)
        shard = build_shard(n, translators)
        out = SHARDS / f"batch_{n:03d}.json"
        out.write_text(json.dumps(shard, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
        print(f"Wrote {out.name}: {len(shard['he'])} keys x 4 langs")


if __name__ == "__main__":
    main()
