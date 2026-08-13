#!/usr/bin/env python3
"""One-shot generator for _fr_tu_explainers_data.py — run from tools/."""
from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
FR_BUNDLE = ROOT / "shared/src/commonMain/composeResources/files/translations/fr.json"
OUT = Path(__file__).resolve().parent / "_fr_tu_explainers_data.py"

# Learn-about explainers that were vous-heavy; keep stable prefixes across regenerations.
FIXED_PREFIXES = [
    "Learn about Oneg Shabbat!",
    "Learn about honoring the Kohanim!",
    "Learn about kavod sefarim",
    "Learn about ona'ah",
    "Learn about one of the main vessels of the Beit HaMikdash",
    "Learn about saying the Shema!",
    "Learn about seeing the good in everything!",
    "Learn about thanking Hashem for sustaining you!",
    "Learn about the Rambam's specific, practical description",
    "Learn about the Tithe-to-Table system",
    "Learn about the Torah obligation to TEACH",
    "Learn about the Torah's obligations of respect for Torah scholars",
    "Learn about the divine command to have the best time",
    "Learn about the fascinating laws of Nedarim",
    "Learn about the gid ha-nasheh",
    "Learn about the incredible \"sliding scale\" system",
    "Learn about the incredible power of praying for everyone!",
    "Learn about the joy of mitzvot! Take a moment",
    "Learn about the mikveh — a bath with requirements",
    "Learn about the mitzvah of Hadlakat Nerot",
    "Learn about the obligation to check your mezuzot and tefillin",
    "Learn about the power of forgiveness! Take a moment",
    "Learn about the power of proper speech!",
    "Learn about the prohibition of 'chukot ha'akum'",
    "Learn about the prohibition of receiving lashon hara",
    "Learn about the quality-control laws for offerings",
    "Learn about the ultimate \"legal permit\" to enjoy the world",
    "Learn about the ultimate 'Heavenly delivery service'",
    'Learn about the ultimate spiritual "high-voltage" connection!',
    "Learn about the world's first mandatory RSVP event - the Korban Pesach",
    "Learn about tochachah",
    "Learn about tumat meit",
    "Learn about tzedakah priorities",
]


from _fr_tu_convert import apply_fr_tu as to_tu
def main() -> None:
    fr = json.loads(FR_BUNDLE.read_text(encoding="utf-8"))["entries"]
    prefixes: dict[str, str] = {}
    for prefix in FIXED_PREFIXES:
        matches = [k for k in fr if k.startswith(prefix) and "$" not in fr[k]]
        if not matches:
            raise SystemExit(f"no catalog key for prefix: {prefix!r}")
        prefixes[prefix] = to_tu(fr[matches[0]])

    still = []
    for p, v in prefixes.items():
        stripped = re.sub(r"«[^»]*»", "", v)
        if re.search(r"\bvous\b|\bvotre\b|\bvos\b", stripped, re.I):
            still.append(p)
    if still:
        raise SystemExit(f"still formal after conversion: {still[:5]}")

    lines = [
        '"""FR tu rewrites for Learn-about explainers still using vous."""',
        "",
        "PREFIX_FR_TU_EXPLAINERS: dict[str, str] = {",
    ]
    for p, v in sorted(prefixes.items()):
        lines.append(f"    {p!r}: (")
        lines.append(f"        {v!r}")
        lines.append("    ),")
    lines.append("}")
    OUT.write_text("\n".join(lines) + "\n", encoding="utf-8")
    print(f"wrote {len(prefixes)} entries to {OUT.name}")


if __name__ == "__main__":
    main()
