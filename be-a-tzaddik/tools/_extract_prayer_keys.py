#!/usr/bin/env python3
"""Extract prayer/liturgy translation keys from mitzmode data files."""
from __future__ import annotations

import json
import re
from pathlib import Path

BASE = Path(__file__).resolve().parents[1].parent / "app/src/main/java/com/beardytop/mitzmode/data"
OUT = Path(__file__).resolve().parents[1] / "data/translation-catalog/_keys_prayers_liturgy.json"


def unescape(s: str) -> str:
    return s.replace("\\n", "\n").replace('\\"', '"').replace("\\\\", "\\")


def extract_field(text: str, field: str) -> list[str]:
    out: list[str] = []
    for m in re.finditer(rf'{field}\s*=\s*"""([\s\S]*?)"""', text):
        out.append(unescape(m.group(1)))
    for m in re.finditer(rf'{field}\s*=\s*"((?:[^"\\]|\\.)*)"', text):
        out.append(unescape(m.group(1)))
    return out


def main() -> None:
    keys: list[str] = []
    seen: set[str] = set()
    for fname in ("BirkatHamazonText.kt", "TefilatHaderechData.kt", "BrachotData.kt"):
        text = (BASE / fname).read_text(encoding="utf-8")
        for field in (
            "title",
            "hebrew",
            "english",
            "collapsibleSummary",
            "collapsibleSummaryEnglish",
        ):
            for val in extract_field(text, field):
                if val and val not in seen:
                    seen.add(val)
                    keys.append(val)
    for extra in ("ברכת המזון", "תפילת הדרך"):
        if extra not in seen:
            seen.add(extra)
            keys.append(extra)
    OUT.write_text(json.dumps(keys, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"Wrote {len(keys)} keys to {OUT.name}")


if __name__ == "__main__":
    main()
