# -*- coding: utf-8 -*-
"""Rebuild mitzvot_003_he_only.json after accidental wipe."""
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / "data/translation-catalog/human/mitzvot_003_he_only.json"
KEPT = Path(__file__).resolve().parent / "_mitzvot_003_he_kept_0_4.json"
PART1 = Path(__file__).resolve().parent / "_mitzvot_003_he_new_entries.json"
POLISH = Path(__file__).resolve().parent / "_polish_003_he.json"

LUBAVITCH = "\u05de\u05dc\u05d9\u05d5\u05d1\u05d0\u05d5\u05d5\u05d9\u05d8\u05e9"
MAMRIM = "\u05d4\u05dc\u05db\u05d5\u05ea \u05de\u05de\u05e8\u05d9\u05dd"
SECHIRUT = "\u05d4\u05dc\u05db\u05d5\u05ea \u05e9\u05db\u05d9\u05e8\u05d5\u05ea"
DEUT_VERSE = (
    "\u05d1\u05d9\u05d5\u05dd \u05d0\u05ea\u05d5 \u05d9\u05e6\u05d0 \u05d1\u05d9\u05d5\u05de\u05d5, "
    "\u05ea\u05df \u05dc\u05d5 \u05d0\u05ea \u05e9\u05db\u05e8\u05d5, "
    "\u05dc\u05d0 \u05d9\u05d1\u05d5\u05d0 \u05e2\u05dc\u05d9\u05d5 \u05d4\u05e9\u05de\u05e9"
)

def fix_entry(s: str) -> str:
    s = s.replace("מליובavitch", LUBAVITCH)
    s = re.sub(r"הלכות מ[a-z]*rim", MAMRIM, s)
    s = s.replace("הלכות שכirut", SECHIRUT)
    if "ביוo" in s or "תiten" in s or "שkיעat" in s:
        s = re.sub(
            r'"[^"]*"\s*\(דברים כ"ד:ט"ו\)',
            f'"{DEUT_VERSE}" (דברים כ"ד:ט"ו)',
            s,
            count=1,
        )
    return s

def main():
    kept = json.loads(KEPT.read_text(encoding="utf-8"))
    part1 = json.loads(PART1.read_text(encoding="utf-8"))
    polish = json.loads(POLISH.read_text(encoding="utf-8"))

    he = kept + part1
    if len(he) < 25:
        he.extend([""] * (25 - len(he)))
    for i, entry in enumerate(polish["entries"], start=10):
        he[i] = fix_entry(entry)

    he[2] = he[2].replace('"מצ מוד!"', "'Mitz Mode!'")
    for old in ("אנחנו מטbיעים מצוות, משפחה!", "אנחנו מטביעים מצוות, משפחה!"):
        he[4] = he[4].replace(old, "We're mintin' mitzvot, fam!")
    if "mintin" not in he[4] and "fam!" in he[4]:
        pass
    elif "mintin" not in he[4]:
        he[4] = he[4].replace("Talk about an after-dinner mint!", "")
        if "We're mintin'" not in he[4]:
            he[4] = he[4].replace(
                "ההלכה אומרת לשבת ולהתמקד במילים.",
                "ההלכה אומרת לשבת ולהתמקד במילים. Talk about an after-dinner mint! We're mintin' mitzvot, fam!",
            )

    assert len(he) == 25, len(he)
    OUT.write_text(
        json.dumps({"he": he}, ensure_ascii=False, indent=2) + "\n",
        encoding="utf-8",
    )
    print("wrote", OUT, "entries", len(he))

if __name__ == "__main__":
    main()
