#!/usr/bin/env python3
"""Write translation shards 000-006 from translations_000_006_data.T."""

from __future__ import annotations

import json
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
BATCH_DIR = ROOT / "data" / "translation-catalog" / "batches"
SHARD_DIR = ROOT / "data" / "translation-catalog" / "shards"
LANGS = ("he", "es", "fr", "ru")

sys.path.insert(0, str(Path(__file__).resolve().parent))
from translations_000_006_data import T  # noqa: E402


def main() -> None:
    SHARD_DIR.mkdir(parents=True, exist_ok=True)
    for batch_idx in range(7):
        batch = json.loads((BATCH_DIR / f"batch_{batch_idx:03d}.json").read_text(encoding="utf-8"))
        missing = [s for s in batch["strings"] if s not in T]
        if missing:
            raise SystemExit(f"batch_{batch_idx:03d}: missing {len(missing)} in T, first={missing[0]!r}")
        shard = {lang: {s: T[s][lang] for s in batch["strings"]} for lang in LANGS}
        out = SHARD_DIR / f"batch_{batch_idx:03d}.json"
        out.write_text(json.dumps(shard, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
        print(f"Wrote {out.name}: {len(batch['strings'])} strings")


if __name__ == "__main__":
    main()
