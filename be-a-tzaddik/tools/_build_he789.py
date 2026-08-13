#!/usr/bin/env python3
"""One-shot builder for he_fix_007/008/009 — merges into gen_he_fix_007_009.py output."""

from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1] / "data" / "translation-catalog"
HUMAN = ROOT / "human"

MIXED_RE = re.compile(
    r"[\u0590-\u05FF]+[A-Za-z\u0400-\u04FF]+|[A-Za-z\u0400-\u04FF]+[\u0590-\u05FF]+"
)
PH_SKIP = ("HebrewCalendar", "profile", "tomorrowCal", "java.util", "java.text")


def extract_placeholders(s: str) -> list[str]:
    out: list[str] = []
    i = 0
    while i < len(s):
        if s[i] != "$":
            i += 1
            continue
        if i + 1 < len(s) and s[i + 1] == "{":
            depth = 0
            j = i + 1
            while j < len(s):
                if s[j] == "{":
                    depth += 1
                elif s[j] == "}":
                    depth -= 1
                    if depth == 0:
                        out.append(s[i : j + 1])
                        i = j + 1
                        break
                j += 1
            else:
                i += 1
        else:
            j = i + 1
            while j < len(s) and (s[j].isalnum() or s[j] in "_.$"):
                j += 1
            if j < len(s) and s[j] == "(":
                depth = 1
                j += 1
                while j < len(s) and depth:
                    if s[j] == "(":
                        depth += 1
                    elif s[j] == ")":
                        depth -= 1
                    j += 1
            out.append(s[i:j])
            i = j
    return out


def check_pure(strings: list[str], label: str) -> None:
    bad = []
    for idx, s in enumerate(strings):
        for m in MIXED_RE.finditer(s):
            g = m.group()
            if any(skip in g for skip in PH_SKIP):
                continue
            bad.append((idx + 1, g))
    if bad:
        raise SystemExit(f"{label} mixed Latin/Cyrillic in Hebrew: {bad}")


def check_placeholders(keys: list[str], he: list[str], label: str) -> list[str]:
    issues = []
    for i, (key, tr) in enumerate(zip(keys, he)):
        kp = extract_placeholders(key)
        tp = extract_placeholders(tr)
        if kp != tp:
            issues.append(f"  [{i + 1}] key={kp!r} he={tp!r}")
    if issues:
        print(f"PLACEHOLDER MISMATCH {label}:")
        print("\n".join(issues))
    else:
        print(f"PLACEHOLDERS OK {label}: {len([p for k in keys for p in extract_placeholders(k)])} tokens")
    return issues


ROSH_HASHANA = (
    "ראש השנה — ראש השנה ויום הדין. אין מלאכה מהערב.\n\n"
    "אם ראש השנה פוגש שבת — סעיף מפורט (עירוב תבשילין, יקנה\"ז וכו') "
    "רק בשנים הנדרשות. השתמשו במחזור לנוסח המדויק.\n\n"
    "הערב ומחר:\n"
    "• הדליקו נרות יום טוב לפני השקיעה.\n"
    "${shehecheyanuErevLines(HebrewCalendarEngine.ROSH_HASHANA, tomorrowCal, profile)}\n"
    "• ארוחות חגיגיות עם קידוש, חלה בדבש ומאכלי סימן.\n"
    "• שמעו שופר בשירותי היום (לא הלילה).\n"
    "• יעלה וייבו בעמידה ובברכת המזון; לא תחנון.\n\n"
    "מנהגים:\n"
    "• «לשנה טובה».\n"
    "• רבים נמנעים מאגוזים, חומץ וחריף (minhag).\n"
    "• תשליך בצהריים הראשון כשאינו שבת; אם יום א' בשבת — נדחה ליום ראשון.\n\n"
    '${diasporaSecondDayNote(profile, "Rosh Hashana")}'
)

SEFIRAT_OMER = (
    "ספירת העומר מקשרת את פסח לשבועות — ספירת כל יום מיציאת מצרים לקראת קבלת התורה.\n\n"
    "היום בעומר: $todaySummary (יום $day מתוך 49).\n\n"
    "ספירת הלילה:\n"
    "• ליל $tonight — ספרו $todaySummary אחרי צאת הכוכבים$timePart.\n"
    "$nextNightLine\n\n"
    "איך לספור:\n"
    "• עמדו וברכו לפני הספירה אם עדיין אומרים ברכה "
    "(אם פספסתם יום, שאלו את הרב לפני המשך עם ברכה).\n"
    '• אמרו: "${omerCountSpeechPhrase(day)}"\n'
    "• ספרו אfter צאת הכוכבים (tzeit); סיימו לפני השחר. "
    "אם שכחתם בלילה, ספרו ביום למחרת בלי ברכה. "
    "אם עושים זאת לפני השקיעה, אפשר להמשיך בלילות הבאים עם ברכה. "
    "מאבדים את הברכה לצמיתות רק אם מפספסים מחזור של 24 שעות שלם "
    "(גם לילה וגם היום שלאחריו) — שאלו את הרב.\n\n"
    "$nusachWhen"
)

SHEMINI_BEGINS = (
    'שמיני עצרת מתחיל הערב${if (profile.isInIsrael) " — בישראל זה גם שמחת תורה." else "."}\n\n'
    "הערב ומחר:\n"
    "• הדליקו נרות יום טוב.\n"
    "${shehecheyanuErevLines(HebrewCalendarEngine.SHEMINI_ATZERES, tomorrowCal, profile)}\n"
    "• אין לולב בשמיני עצרת — המצווה הסתיימה ביום השביעי של סוכות.\n"
    "${if (profile.isInIsrael)"
)

# Fix typo in SEFIRAT_OMER
SEFIRAT_OMER = SEFIRAT_OMER.replace("אfter", "אחרי")

TRANSLATIONS: dict[str, list[str]] = {}
