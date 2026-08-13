#!/usr/bin/env python3
"""Find Hebrew human shards with high Latin ratio (Argos garbage)."""

from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
HUMAN = ROOT / "data/translation-catalog/human"
OUT = ROOT / "data/translation-catalog/he-garbage-batches.json"

SKIP = ("_only", "_src")
PASSTHROUGH = re.compile(r"^\$[a-zA-Z_]")


def latin_ratio(text: str) -> float:
    letters = [c for c in text if c.isalpha()]
    if not letters:
        return 0.0
    latin = sum(1 for c in letters if ord(c) < 128)
    return latin / len(letters)


def main() -> None:
    bad: list[dict] = []
    for path in sorted(HUMAN.glob("*.json")):
        if any(s in path.name for s in SKIP) or path.name.startswith("_"):
            continue
        data = json.loads(path.read_text(encoding="utf-8"))
        he = data.get("he", {})
        for key, tr in he.items():
            if tr == key or PASSTHROUGH.match(key):
                continue
            if len(tr) < 40:
                continue
            r = latin_ratio(tr)
            if r > 0.12:
                bad.append(
                    {
                        "batch": path.stem,
                        "ratio": round(r, 3),
                        "key": key,
                        "snippet": tr[:200],
                    }
                )
    bad.sort(key=lambda x: (-x["ratio"], x["batch"]))
    # group by batch
    by_batch: dict[str, list[str]] = {}
    for row in bad:
        by_batch.setdefault(row["batch"], []).append(row["key"])
    OUT.write_text(json.dumps({"count": len(bad), "by_batch": by_batch, "rows": bad[:200]}, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"{len(bad)} suspicious Hebrew entries -> {OUT}")
    for batch, keys in sorted(by_batch.items(), key=lambda x: -len(x[1]))[:15]:
        print(f"  {batch}: {len(keys)}")


if __name__ == "__main__":
    main()
