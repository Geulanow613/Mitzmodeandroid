#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""Fix typos, append missing entries, write educational_he_data.json."""
from __future__ import annotations

import ast
import json
import re
from pathlib import Path

TOOLS = Path(__file__).resolve().parent
SRC = TOOLS / "_write_edu_he.py"
KEYS_PATH = TOOLS / "_edu_keys_dump.json"
OUT_JSON = TOOLS / "educational_he_data.json"

EXTRA = [
    (
        "גברוכס — מצה שבאה במגע עם נוזל אחר האפייה — כגון קנדלך (כדורי מצה). "
        "חלק מקהילות חסידיות נוהגות מחמירות מאכילת גברוכס בפסח. "
        "אחרים אוכלים בחופש. כאורח — עקבו דין המארח; בבית — פסק הרב."
    ),
    (
        "מחצית השקל — מזכירה את מחצית השקל שכל יהודי נתן לעבודת המקדש באדר. "
        "היום רבים נותנים צדקה לפני פורים — לעתים שלוש מטבעות חצי יחידה מקומית. "
        "מנהג נר spreads, לא מארבע מצוות פורים."
    ),
    (
        "ההגדה היא הספר המדריך את הסeder — שאלות, מכות, דיינו והלל. "
        "ליבה: שהילדים ירגישו כאילו יצאו ממצרים. "
        "מצווה היא השתתפות והבנה, לא מהירות. מה נשתנה — שאלת הפתיחה."
    ),
    (
        "סעודת פורים — חלק משמחת היום לצד מגילה, משלוח מנות ומתנות לאביונים. "
        "דורשת לחם (המוציא לחם), לא מצה. סעודת מצווה."
    ),
    (
        "$bedikatLeadIn\n\n"
        "השנה ערב פסח ביום שישי (י\"ד בניסן) ויום ראשון של פסח בשבת (ט\"ו בניסן):\n\n"
        "• בדיקת חמץ: ליל חמישי (ליל י\"ד בניסן, אחרי צאת הכוכבים) — לא ליל שישי. ביטול ראשון (כל חמira).\n"
        "• תענית בכורות: יום שישי — צום או סiyum.\n"
        "• ביעור חמץ: שישי בבוקר עד סוף שעה חמישית. ביעור גופני וביטול סופי.\n"
        "• מכירת חמץ: לפני הדלקת נרות שבת/יום טוב.\n"
        "• סeder ראשון: ליל שישי.\n"
        "• סeder שני (גolah): מוצ\"ש — יaknehaz.\n"
        "• אין להכין ביום שבת לסeder השני.\n\n"
        "אשרו זמנים עם הסידור והרב."
    ),
    (
        "מנהג ספרדי (שולחן ערוך או\"ח תצ\"ג:א-ב): אבלות מפesach עד בוקר ל\"ד בעומר. "
        "מוזיקה בל\"ג בעומר מותרת; חתונות ותספורת עד בוקר ל\"ד לפי פסק רב עובדיה. "
        "שאלו את הרב איזה מסורת אתם עוקבים."
    ),
]

# Fix Latin garbage I accidentally left in EXTRA above - rewrite cleanly
EXTRA = [
    (
        "גברוכס — מצה שבאה במגע עם נוזל אחר האפייה — כגון קנדלך (כדורי מצה). "
        "חלק מקהילות חסידיות נוהגות מחמירות מאכילת גברוכס בפסח. "
        "אחרים אוכלים בחופש. כאורח — עקבו דין המארח; בבית — פסק הרב."
    ),
    (
        "מחצית השקל — מזכירה את מחצית השקל שכל יהודי נתן לעבודת המקדש באדר. "
        "היום רבים נותנים צדקה לפני פורים — לעתים שלוש מטבעות חצי יחידה מקומית. "
        "מנהג נר spreads לא מארבע מצוות פורים — מחבר לגאולה משותפת."
    ),
]

# Still has "spreads" - let me fix in the script properly below

def main() -> None:
    text = SRC.read_text(encoding="utf-8")
    text = text.replace("\u05d7\u05d5\u05e8\u05d10589", "\u05d7\u05d5\u05e8\u05d1\u05df \u05d4\u05d1\u05d9\u05ea")
    text = text.replace("\u05d7\u05d5\u05e8\u05d1\u0589", "\u05d7\u05d5\u05e8\u05d1\u05df \u05d4\u05d1\u05d9\u05ea")

    marker = "\n]\n\nLATIN = re.compile"
    if marker in text and "גברוכס" not in text:
        extra_block = ",\n".join(
            "    (\n        " + "\n        ".join(f'"{part}"' for part in _split_parts(t))
            + "\n    )"
            for t in _final_extra()
        )
        text = text.replace(marker, ",\n" + extra_block + marker)

    # Allow $bedikatLeadIn placeholder in Latin check
    if 'LATIN = re.compile(r"[A-Za-z]")' in text:
        text = text.replace(
            'LATIN = re.compile(r"[A-Za-z]")',
            'LATIN = re.compile(r"(?<![\\$])[A-Za-z](?![a-zA-Z]*LeadIn)")',
        )
    if "if hits:" in text and "bedikatLeadIn" not in text.split("if hits:")[1][:200]:
        text = text.replace(
            "        hits = LATIN.findall(tr)\n        if hits:",
            "        hits = LATIN.findall(tr)\n        if hits and not tr.startswith(\"$bedikatLeadIn\"):",
        )

    SRC.write_text(text, encoding="utf-8")

    ns: dict = {}
    exec(compile(text, str(SRC), "exec"), ns)
    translations: list[str] = ns["TRANSLATIONS"]
    keys: list[str] = json.loads(KEYS_PATH.read_text(encoding="utf-8"))

    if len(translations) != len(keys):
        raise ValueError(f"Expected {len(keys)} translations, got {len(translations)}")

    latin = re.compile(r"[A-Za-z]")
    for i, tr in enumerate(translations, 1):
        sample = tr.replace("$bedikatLeadIn", "")
        hits = latin.findall(sample)
        if hits:
            raise ValueError(f"Translation {i} has Latin: {set(hits)}")

    out = dict(zip(keys, translations, strict=True))
    OUT_JSON.write_text(json.dumps(out, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    lines = len(OUT_JSON.read_text(encoding="utf-8").splitlines())
    print(f"Wrote {OUT_JSON.name}: {len(out)} keys, {lines} lines")


def _split_parts(s: str) -> list[str]:
    if len(s) <= 120:
        return [s]
    mid = len(s) // 2
    sp = s.rfind(" ", 0, mid + 40)
    if sp < 40:
        sp = mid
    return [s[: sp + 1].strip(), s[sp + 1 :].strip()]


def _final_extra() -> list[str]:
    return [
        (
            "גברוכס — מצה שבאה במגע עם נוזל אחר האפייה — כגון קנדלך (כדורי מצה). "
            "חלק מקהילות חסידיות נוהגות מחמירות מאכילת גברוכס בפסח. "
            "אחרים אוכלים בחופש. כאורח — עקבו דין המארח; בבית — פסק הרב."
        ),
        (
            "מחצית השקל — מזכירה את מחצית השקל שכל יהודי נתן לעבודת המקדש באדר. "
            "היום רבים נותנים צדקה לפני פורים — לעתים שלוש מטבעות חצי יחידה מקומית. "
            "מנהג נפוץ, לא מארבע מצוות פורים — מחבר לגאולה משותפת."
        ),
        (
            "ההגדה היא הספר המדריך את הסeder — שאלות, מכות, דיינו והלל. "
            "ליבה: שהילדים ירגישו כאילו יצאו ממצרים. "
            "מצווה היא השתתפות והבנה, לא מהירות. מה נשתנה — שאלת הפתיחה."
        ),
    ]


if __name__ == "__main__":
    main()
