#!/usr/bin/env python3
"""Split translation catalog into batches for offline translation workers."""

from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CATALOG = ROOT / "data" / "translation-catalog" / "strings.json"
OUT = ROOT / "data" / "translation-catalog" / "batches"
BATCH_SIZE = 80


def main() -> None:
    strings = json.loads(CATALOG.read_text(encoding="utf-8"))["strings"]
    OUT.mkdir(parents=True, exist_ok=True)
    for i in range(0, len(strings), BATCH_SIZE):
        batch = strings[i : i + BATCH_SIZE]
        idx = i // BATCH_SIZE
        (OUT / f"batch_{idx:03d}.json").write_text(
            json.dumps({"index": idx, "start": i, "strings": batch}, ensure_ascii=False, indent=2),
            encoding="utf-8",
        )
    print(f"Wrote {(len(strings) + BATCH_SIZE - 1) // BATCH_SIZE} batches ({len(strings)} strings)")


if __name__ == "__main__":
    main()
