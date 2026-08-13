#!/usr/bin/env python3
"""Replace English 'Shabbat' with 'Chabbat' in human shard French values."""

from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
HUMAN = ROOT / "data" / "translation-catalog" / "human"

SHABBAT = re.compile(r"\bShabbat\b")


def fix_text(text: str) -> str:
    return SHABBAT.sub("Chabbat", text)


def main() -> None:
    total = 0
    for path in sorted(HUMAN.glob("*.json")):
        if path.name.startswith("_"):
            continue
        data = json.loads(path.read_text(encoding="utf-8"))
        fr = data.get("fr")
        if not fr:
            continue
        changed = 0
        for k, v in list(fr.items()):
            fixed = fix_text(v)
            if fixed != v:
                fr[k] = fixed
                changed += 1
        if changed:
            data["fr"] = fr
            path.write_text(json.dumps(data, ensure_ascii=False, indent=2), encoding="utf-8")
            print(f"{path.name}: fixed {changed}")
            total += changed
    print(f"total: {total} Shabbat -> Chabbat in human fr values")


if __name__ == "__main__":
    main()
