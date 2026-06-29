#!/usr/bin/env python3
"""Export keys that still match known garbage patterns."""

from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SCAN = ROOT / "data/translation-catalog/deep-quality-scan.json"
OUT = ROOT / "data/translation-catalog/fix-queue.json"


def main() -> None:
    data = json.loads(SCAN.read_text(encoding="utf-8"))
    queue: dict[str, list[str]] = {}
    for lang, groups in data.get("hits", {}).items():
        keys: list[str] = []
        for items in groups.values():
            for item in items:
                k = item["key"]
                if k not in keys:
                    keys.append(k)
        queue[lang] = keys
    OUT.write_text(json.dumps(queue, ensure_ascii=False, indent=2), encoding="utf-8")
    print("wrote", OUT)
    for lang, keys in queue.items():
        print(lang, len(keys))


if __name__ == "__main__":
    main()
