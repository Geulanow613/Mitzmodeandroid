# -*- coding: utf-8 -*-
"""Build _he003_new_strings.json from verified translations with post-fixes."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / "data/translation-catalog/human/mitzvot_003_he_only.json"
DATA = Path(__file__).resolve().parent / "_he003_new_strings.json"

strings = json.loads(OUT.read_text(encoding="utf-8"))["he"][5:25]
assert len(strings) == 20

# String 12: keep English "fam" per style guide
strings[6] = strings[6].replace("הישארו קדושים, משפחה!", "הישארו קדושים, fam!")

# String 20: fix MT typos (עם -> אם)
strings[14] = (
    strings[14]
    .replace("קיבוד אב ועם", "כיבוד אב ואם")
    .replace("מורה אב ועם", "מורא אב ואם")
)

# Append missing מקור: lines
if "מקור:" not in strings[7]:
    strings[7] += ' מקור: תלמוד, ברכות כ"ג ע"ב; שולחן ערוך, אורח חיים כ"ז-כ"ח.'
if "מקור:" not in strings[9]:
    strings[9] += ' מקור: דברים ד:ט; רמב"ם, הלכות ספרות ר"ע; שולחן ערוך, יורה דעה ר"ע-ר"י.'
if "מקור:" not in strings[13]:
    strings[13] += " מקור: משנה, יומא ח:ט."
if "מקור:" not in strings[19]:
    strings[19] += ' מקור: רמב"ם, הלכות מתנות עניים י:ז-י"ד.'

# Normalize string 11 source citation
strings[5] = strings[5].replace("מקור: דברים 6:9; הרמב\"ם.", 'מקור: דברים ו:ט; רמב"ם, הלכות מזוזה ו:א.')

DATA.write_text(json.dumps(strings, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
print(f"Wrote {len(strings)} strings to {DATA.name}")
