#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""Finalize mitzvot_003 Hebrew translations: keep entries 1-5, replace 6-25."""

from __future__ import annotations

import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
INPUT = ROOT / "data" / "translation-catalog" / "human" / "mitzvot_003_he_only.json"
PART1 = Path(__file__).resolve().parent / "_mitzvot_003_he_new_entries.json"
PART2 = Path(__file__).resolve().parent / "_mitzvot_003_he_entries_11_25.json"

CORRUPTION_RE = re.compile(
    r"(?<=[\u0590-\u05FF])[a-zA-Z]{2,}"
    r"|(?<=[a-zA-Z])[\u0590-\u05FF]{1,3}(?=[a-zA-Z])"
    r"|(?<=[\u0590-\u05FF])[a-z](?=[\u0590-\u05FF])"
    r"|\bha-|\bbe-|\blo-|\bke-|\bve-|\bla-|\bmi-|\bli-|\bsh-|\bch-|\bte-|\bpo-|\bno-"
)

POST_FIXES: list[tuple[str, str]] = [
    ("שmot", "שמות"),
    ("במדbar", "במדבר"),
    ("שמות האשם", "שמות ה'"),
    ("mindfulness", "מודעות"),
    ("מיינדפולנס", "מודעות"),
    ("גורם לצליל השם", "גורם לחילול השם"),
    ("אזיבת החית", "עזיבת החטא"),
    ("קיבוץ (כבוד)", "כיבוד"),
    ("מה הקיבוץ דורש", "מה הכיבוד דורש"),
    ("הקיבוץ עוסק", "הכיבוד עוסק"),
    ("ניתן לשלם על הקיבוץ", "ניתן לשלם על הכיבוד"),
    ("על תלין", "על לא תלין"),
    ("ריביט", "ריבית"),
    ("הטר יסקה", "היתר עיסקא"),
    ("הטר יסכה", "היתר עיסקא"),
    ("למידע נוסף!", "למדו עוד!"),
    ("למידע נוסף.", "למדו עוד."),
    ("⚡📜💻🌐🦾 ⚡", "⚡"),
    ("✍🏛📜📧🕊 ✍️", "✍️"),
    ("👨👩👧👦 👨‍👩‍👧‍👦", "👨‍👩‍👧‍👦"),
    ("הדו-ראשי", "שריר הזרוע"),
    ("(דברים ו, ​​ט)", '(דברים ו:ט)'),
    ("doorposts", "מזוזות"),
    ("Azivat HaChait", "עזיבת החטא"),
    ("KIDDUSH HASHEM", "קידוש השם"),
    ("CHILLUL HASHEM", "חילול השם"),
]


def load_new_entries() -> list[str]:
    part1 = json.loads(PART1.read_text(encoding="utf-8"))
    part2 = json.loads(PART2.read_text(encoding="utf-8"))
    if len(part1) != 5 or len(part2) != 15:
        raise ValueError(f"Expected 5+15 new entries, got {len(part1)}+{len(part2)}")
    entries = part1 + part2
    fixed: list[str] = []
    for text in entries:
        for old, new in POST_FIXES:
            text = text.replace(old, new)
        fixed.append(text)
    return fixed


ALLOWED_LATIN = re.compile(
    r"\b(VIP|Star Trek|Live Long and Prosper|Leonard Nimoy|G-d|fam|Mitz Mode)\b"
)


def assert_clean(entries: list[str]) -> None:
    for i, entry in enumerate(entries, start=6):
        stripped = ALLOWED_LATIN.sub("", entry)
        if CORRUPTION_RE.search(stripped):
            m = CORRUPTION_RE.search(stripped)
            raise ValueError(
                f"Entry {i} still has corruption near: {m.group()!r}" if m else f"Entry {i} corrupt"
            )


def main() -> int:
    data = json.loads(INPUT.read_text(encoding="utf-8"))
    kept = data["he"][:5]
    if len(kept) != 5:
        raise ValueError(f"Expected 5 kept entries, got {len(kept)}")

    new_entries = load_new_entries()
    assert len(new_entries) == 20

    merged = kept + new_entries
    assert len(merged) == 25

    assert_clean(merged[5:])

    data["he"] = merged
    INPUT.write_text(json.dumps(data, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"Wrote {INPUT}")
    print(f"Total entries: {len(data['he'])}")
    return 0


if __name__ == "__main__":
    sys.exit(main())
