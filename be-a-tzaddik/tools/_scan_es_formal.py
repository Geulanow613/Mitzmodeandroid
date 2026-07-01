#!/usr/bin/env python3
"""Scan ES bundle for remaining formal usted patterns."""
from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
BUNDLE = ROOT / "shared" / "src" / "commonMain" / "composeResources/files/translations/es.json"
STRINGS = ROOT / "data" / "translation-catalog/strings.json"

PATTERNS = [
    re.compile(r"\busted\b", re.I),
    re.compile(r"\b(pregunte|consulte|confirme) a su\b", re.I),
    re.compile(r"\b(use|utilice|siga) (el|la|los|las|su)\b", re.I),
    re.compile(r"\b(su rav|su rabino|su sidur|su comunidad|su kehil)\b", re.I),
    re.compile(r"^(Use|Siga|Pregunte|Consulte|Confirme|Utilice)\b"),
]


def main() -> None:
    strings = json.loads(STRINGS.read_text(encoding="utf-8"))["strings"]
    entries = json.loads(BUNDLE.read_text(encoding="utf-8"))["entries"]
    hits: list[tuple[int, str, str]] = []
    for key in strings:
        val = entries.get(key, key)
        if val == key or len(val) < 40:
            continue
        reasons = [p.pattern for p in PATTERNS if p.search(val)]
        if reasons:
            hits.append((len(val), key[:70], val[:120].replace("\n", " | ")))
    hits.sort()
    print(f"Formal ES hits: {len(hits)}")
    for h in hits[:40]:
        print(f"  [{h[0]}] {h[1]}")
        print(f"    {h[2]}")


if __name__ == "__main__":
    main()
