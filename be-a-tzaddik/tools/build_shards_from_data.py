#!/usr/bin/env python3
"""Build translation shards from _data/t_NNN.py modules (index-ordered tuples)."""

from __future__ import annotations

import importlib.util
import json
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
BATCHES = ROOT / "data" / "translation-catalog" / "batches"
SHARDS = ROOT / "data" / "translation-catalog" / "shards"
DATA_DIR = ROOT / "data" / "translation-catalog" / "shards" / "_data"
LANGS = ("he", "es", "fr", "ru")


def is_hebrew_source(text: str) -> bool:
    hebrew = sum(1 for c in text if "\u0590" <= c <= "\u05ff")
    latin = sum(1 for c in text if c.isascii() and c.isalpha())
    return hebrew > 30 and latin < max(20, hebrew // 4)


def load_t(batch_num: int) -> list[tuple[str, str, str, str]]:
    path = DATA_DIR / f"t_{batch_num:03d}.py"
    if not path.exists():
        raise FileNotFoundError(path)
    spec = importlib.util.spec_from_file_location(f"t_{batch_num:03d}", path)
    mod = importlib.util.module_from_spec(spec)
    assert spec.loader is not None
    spec.loader.exec_module(mod)
    return mod.T  # type: ignore[attr-defined]


def build_shard(batch_num: int) -> dict[str, dict[str, str]]:
    batch_path = BATCHES / f"batch_{batch_num:03d}.json"
    strings = json.loads(batch_path.read_text(encoding="utf-8"))["strings"]
    rows = load_t(batch_num)
    if len(rows) != len(strings):
        raise SystemExit(
            f"batch_{batch_num:03d}: expected {len(strings)} translations, got {len(rows)}"
        )
    shard: dict[str, dict[str, str]] = {lang: {} for lang in LANGS}
    for s, (he, es, fr, ru) in zip(strings, rows):
        if is_hebrew_source(s):
            he = s
        shard["he"][s] = he
        shard["es"][s] = es
        shard["fr"][s] = fr
        shard["ru"][s] = ru
    return shard


def main() -> None:
    batches = [int(a) for a in sys.argv[1:]] if len(sys.argv) > 1 else []
    if not batches:
        print("Usage: build_shards_from_data.py 18 19 23 ...", file=sys.stderr)
        sys.exit(1)
    SHARDS.mkdir(parents=True, exist_ok=True)
    total = 0
    for n in batches:
        shard = build_shard(n)
        out = SHARDS / f"batch_{n:03d}.json"
        out.write_text(json.dumps(shard, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
        count = len(json.loads((BATCHES / f"batch_{n:03d}.json").read_text(encoding="utf-8"))["strings"])
        total += count
        print(f"Wrote {out.name}: {count} keys x 4 langs")
    print(f"Grand total strings: {total} ({total * 4} translation entries)")


if __name__ == "__main__":
    main()
