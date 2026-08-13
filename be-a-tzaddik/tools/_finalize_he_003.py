# -*- coding: utf-8 -*-
"""Finalize mitzvot_003 Hebrew translations (cloud51–cloud75)."""
import json
import re
import subprocess
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / "data/translation-catalog/human/mitzvot_003_he_only.json"
DATA = Path(__file__).resolve().parent / "_he003_new_strings.json"

CORRUPTION_RE = re.compile(
    r"(?<=[\u0590-\u05FF])[a-zA-Z]{2,}"
    r"|(?<=[a-zA-Z])[\u0590-\u05FF]{1,3}(?=[a-zA-Z])"
    r"|(?<=[\u0590-\u05FF])[a-z](?=[\u0590-\u05FF])"
)


def ensure_data() -> None:
    if not DATA.exists():
        prep = Path(__file__).resolve().parent / "_prepare_he003_data.py"
        subprocess.check_call([sys.executable, str(prep)])


def load_new_strings() -> list[str]:
    ensure_data()
    strings = json.loads(DATA.read_text(encoding="utf-8"))
    if len(strings) != 20:
        raise ValueError(f"Expected 20 new strings, got {len(strings)}")
    return strings


def main() -> None:
    existing = json.loads(OUT.read_text(encoding="utf-8"))
    first_five = existing["he"][:5]
    if len(first_five) != 5:
        raise ValueError(f"Expected 5 existing strings, got {len(first_five)}")

    new_strings = load_new_strings()
    he = first_five + new_strings

    if len(he) != 25:
        raise ValueError(f"Expected 25 total strings, got {len(he)}")

    for i, s in enumerate(he, start=1):
        bad = CORRUPTION_RE.findall(s)
        if bad:
            raise ValueError(f"String {i} has Latin/Hebrew corruption: {bad!r}")
        if i >= 6 and "מקור:" not in s:
            raise ValueError(f"String {i} missing מקור: citation")

    OUT.write_text(
        json.dumps({"he": he}, ensure_ascii=False, indent=2) + "\n",
        encoding="utf-8",
    )
    print(f"Wrote {len(he)} Hebrew strings to {OUT.relative_to(ROOT)}")


if __name__ == "__main__":
    main()
