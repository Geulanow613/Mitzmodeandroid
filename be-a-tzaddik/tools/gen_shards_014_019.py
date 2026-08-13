#!/usr/bin/env python3
"""Generate translation shards for batches 014-019."""
from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1] / "data" / "translation-catalog"
LANGS = ("he", "es", "fr", "ru")
T: dict[str, dict[str, str]] = {}


def tr(key: str, he: str, es: str, fr: str, ru: str) -> None:
    T[key] = {"he": he, "es": es, "fr": fr, "ru": ru}


def load_pairs() -> None:
    from shard_translations import ALL  # noqa: WPS433

    for key, he, es, fr, ru in ALL:
        tr(key, he, es, fr, ru)


def build_shard(batch_index: int) -> dict:
    batch = json.loads((ROOT / "batches" / f"batch_{batch_index:03d}.json").read_text(encoding="utf-8"))
    shard = {lang: {} for lang in LANGS}
    missing = []
    for s in batch["strings"]:
        if s not in T:
            missing.append(s)
            continue
        for lang in LANGS:
            shard[lang][s] = T[s][lang]
    if missing:
        raise SystemExit(f"batch_{batch_index:03d}: missing {len(missing)}: {missing[0][:120]!r}")
    return shard


def main() -> None:
    load_pairs()
    out_dir = ROOT / "shards"
    out_dir.mkdir(parents=True, exist_ok=True)
    total = 0
    for i in range(14, 20):
        shard = build_shard(i)
        path = out_dir / f"batch_{i:03d}.json"
        path.write_text(json.dumps(shard, ensure_ascii=False, indent=2), encoding="utf-8")
        n = len(shard["he"])
        total += n
        print(f"batch_{i:03d}.json: {n} keys x 4 langs")
    print(f"TOTAL: {total} strings across 6 batches")


if __name__ == "__main__":
    main()
