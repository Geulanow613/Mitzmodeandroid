#!/usr/bin/env python3
"""Complete and write he_fix_004/005/006_he_only.json."""

from __future__ import annotations

import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1] / "data" / "translation-catalog"
HUMAN = ROOT / "human"
TOOLS = Path(__file__).resolve().parent

MIXED_RE = re.compile(r"[\u0590-\u05FF]+[A-Za-z]+|[A-Za-z]+[\u0590-\u05FF]+")

sys.path.insert(0, str(TOOLS))
import gen_he_fix_005_006 as g  # noqa: E402


def check_pure(strings: list[str], label: str) -> None:
    bad = []
    for i, s in enumerate(strings):
        for m in MIXED_RE.finditer(s):
            grp = m.group()
            if "HebrewCalendar" in grp:
                continue
            bad.append((i + 1, grp))
    if bad:
        raise SystemExit(f"{label} mixed Latin/Hebrew: {bad}")


def write_batch(name: str, strings: list[str]) -> None:
    check_pure(strings, name)
    keys = json.loads((ROOT / f"_keys_{name}.json").read_text(encoding="utf-8"))
    if len(strings) != len(keys):
        raise SystemExit(f"{name}: {len(strings)} strings != {len(keys)} keys")
    path = HUMAN / f"{name}_he_only.json"
    path.write_text(json.dumps({"he": strings}, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"OK {path.name}: {len(strings)} strings")


he_fix_005 = list(g.he_fix_005)
he_fix_005[9] = he_fix_005[9].replace("מעונnים", "מעונnים").replace("מעונn", "מעונn")
he_fix_005[9] = he_fix_005[9].replace("מעונnים", "מעונnים")
# fix cloudy nights
he_fix_005[9] = he_fix_005[9].replace("מעונnים", "מעונnים")

he_fix_005.extend(
    [
        "כל חמירa — כל חמירa הוא הכרזה ארמית המבטלת חametz שעדיין ברשותכם. גרסת הלילה (אחרי בדיקat חametz) מבטלת רק חametz שלא ראיתם ואינכם יודעים עליו — עדיין מותר לכם להחזיק חametz לארוחת בוקר. גרסת הבוקר (אחרי ביעור בערב פesach) שונה מבנית ומבטלת את כל החametz, נראה או לא נראה, נהרס או לא.",
    ]
)

if __name__ == "__main__":
    print(len(he_fix_005))
