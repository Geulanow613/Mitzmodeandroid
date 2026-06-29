#!/usr/bin/env python3
"""Generate all shard files 014-019 from embedded translation tables."""
from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1] / "data" / "translation-catalog"
LANGS = ("he", "es", "fr", "ru")


def _load_tables() -> dict[str, dict[str, str]]:
    from tr_data import TABLES  # noqa: WPS433
    merged: dict[str, dict[str, str]] = {}
    for table in TABLES:
        merged.update(table)
    return merged


def main() -> None:
    tables = _load_tables()
    out_dir = ROOT / "shards"
    out_dir.mkdir(parents=True, exist_ok=True)
    grand = 0
    for batch_num in range(14, 20):
        batch = json.loads((ROOT / "batches" / f"batch_{batch_num:03d}.json").read_text(encoding="utf-8"))
        shard = {lang: {} for lang in LANGS}
        missing = []
        for s in batch["strings"]:
            if s not in tables:
                missing.append(s)
                continue
            for lang in LANGS:
                shard[lang][s] = tables[s][lang]
        if missing:
            raise SystemExit(f"batch_{batch_num:03d}: missing {len(missing)} keys, e.g. {missing[0][:100]!r}")
        path = out_dir / f"batch_{batch_num:03d}.json"
        path.write_text(json.dumps(shard, ensure_ascii=False, indent=2), encoding="utf-8")
        n = len(shard["he"])
        grand += n
        print(f"batch_{batch_num:03d}.json: {n} keys x 4 langs")
    print(f"TOTAL: {grand} strings (expected 480)")


if __name__ == "__main__":
    main()
