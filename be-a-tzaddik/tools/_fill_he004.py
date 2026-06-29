#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""Fill he_004_data.txt strings 6-25 and build final JSON."""
import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
DATA = Path(__file__).resolve().parent / "he_004_data.txt"
OUT = ROOT / "data/translation-catalog/human/mitzvot_004_he_only.json"

REST = [
    # 6 cloud81
    (
        'למדו את התהליך של הבאת קורban — ולמה היפוך אפילו של שלב אחד פוסל את הכל! 🐑 '
        'הבאת קורban הייתה תהליך מדויק עם ארבעה מעשים חיוניים, בסדר מסוים: '
        '(1) שחיטה — השחיטה, שבוצעה על ידי כל ישראל (לא רק כהן!). כל גבר יehudi בוגר יכול היה לשחot את החיה. '
        '(2) קבלה — הכohן תofes את dם החיה בכli qodesh מיד. שלb זeh חyיב להיעשות על ידי kohen. '
        '(3) הולכה — הkohen נושא את הכli המלא dם אל המזbach. '
        '(4) זריקה — הkohen zorek את הdם על המזbach. '
        'אם השלבים נעשu lo be-seder (למשal, lo-kohen asah qabalah, o ha-dam husham lifnei zerikah) — kol ha-qorban hayah pasul. '
        'כלל mafli: ha-kavanah chashuvah le-khol ha-tahalich. '
        'im ha-kohen she-oseh et ha-avodah chashav le-ekhol et ha-basar le-achar chalon ha-zman ha-mutar (yadu\'a ke-pigul) — '
        'ha-pesul ha-mental hazeh yachol le-fasel et ha-qorban. '
        'ha-torah hit\'lelah be-mo\'ach ha-kohen ke-chelk min ha-ma\'aseh ha-kadosh. '
        'lmedu od al eich makrivim qorbanot be-hilchot ma\'aseh ha-qorbanot shel ha-rambam!'
    ),
]

def validate(strings):
    if len(strings) != 25:
        raise SystemExit(f"Expected 25 strings, got {len(strings)}")
    for i, s in enumerate(strings):
        bad = [c for c in s if c.isascii() and c.isalpha()]
        if bad:
            raise SystemExit(f"String {i+1} has Latin: {sorted(set(bad))}")

def main():
    text = DATA.read_text(encoding="utf-8")
    parts = [p.strip() for p in text.split("\n---\n")]
    if len(REST) != 20:
        raise SystemExit(f"REST must have 20 strings, got {len(REST)}")
    for idx, s in enumerate(REST, start=6):
        parts[idx - 1] = s
    validate(parts)
    DATA.write_text("\n---\n".join(parts) + "\n", encoding="utf-8")
    OUT.parent.mkdir(parents=True, exist_ok=True)
    OUT.write_text(json.dumps({"he": parts}, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"OK: wrote {len(parts)} strings to {OUT}")

if __name__ == "__main__":
    main()
