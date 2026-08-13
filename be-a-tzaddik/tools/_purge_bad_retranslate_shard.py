#!/usr/bin/env python3
"""Remove corrupt entries from es_fr_ru_retranslate.json (placeholder disasters)."""

from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
PATH = ROOT / "data" / "translation-catalog" / "shards" / "es_fr_ru_retranslate.json"

BAD = re.compile(
    r"the\d|s\d+s|\)\.\d+\)|\(\s*the\d|less\d|\$\{if\s*\(|Grace After Meals —|\)\.0\)",
    re.I,
)


def main() -> None:
    data = json.loads(PATH.read_text(encoding="utf-8"))
    removed = 0
    for lang in ("ru", "fr", "es"):
        section = data.get(lang, {})
        for k in list(section.keys()):
            v = section[k]
            if BAD.search(v):
                del section[k]
                removed += 1
        data[lang] = section
    PATH.write_text(json.dumps(data, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"purged {removed} corrupt entries from {PATH.name}")


if __name__ == "__main__":
    main()
