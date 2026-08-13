#!/usr/bin/env python3
"""Batch halachic copy fixes: tallit katan, gebrochts, kiddush levana, shnayim mikra."""

from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]

REPLACEMENTS = [
    (
        "a small rectangular garment with tzitzit at its four corners, typically worn under the shirt (it can also be worn over the shirt)",
        "a small rectangular garment with tzitzit at its four corners, worn generally over or under the shirt, depending on community custom",
    ),
    (
        "• Once in Aramaic translation (Targum Onkelos, a word-for-word Aramaic translation of the Torah written by Onkelos, a convert to Judaism in the 1st century CE)",
        "• Once in Aramaic translation (Targum Onkelos) or Rashi's commentary, which many use as a substitute for the translation to better understand the plain meaning (Shulchan Arukh and Mishnah Berurah O.C. 285:2)",
    ),
]

CHECKLIST_PATHS = [
    ROOT / "data/checklist-items.json",
    ROOT / "shared/src/commonMain/composeResources/files/checklist-items.json",
    ROOT.parent / "ios-transfer-handoff/be-a-tzaddik/data/checklist-items.json",
    ROOT.parent / "ios-transfer-handoff/be-a-tzaddik/shared/src/commonMain/composeResources/files/checklist-items.json",
]


def fix_checklist(path: Path) -> int:
    data = json.loads(path.read_text(encoding="utf-8"))
    total = 0
    for item in data.get("items", []):
        for field in (
            "explanation",
            "explanationAshkenaz",
            "explanationSefard",
            "explanationChabad",
            "explanationFemale",
            "explanationEdotHamizrach",
        ):
            val = item.get(field, "")
            if not val:
                continue
            for old, new in REPLACEMENTS:
                if old in val:
                    item[field] = val.replace(old, new)
                    val = item[field]
                    total += 1
    path.write_text(json.dumps(data, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    return total


def main() -> None:
    total = sum(fix_checklist(p) for p in CHECKLIST_PATHS if p.exists())
    print(f"Checklist replacements: {total}")


if __name__ == "__main__":
    main()
