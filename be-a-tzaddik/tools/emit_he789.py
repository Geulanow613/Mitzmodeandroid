#!/usr/bin/env python3
"""Emit he_fix_007/008/009_he_only.json — run once."""

from __future__ import annotations

import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1] / "data" / "translation-catalog"
HUMAN = ROOT / "human"

MIXED_RE = re.compile(
    r"[\u0590-\u05FF]+[A-Za-z\u0400-\u04FF]+|[A-Za-z\u0400-\u04FF]+[\u0590-\u05FF]+"
)
PH_SKIP = ("HebrewCalendar", "profile", "tomorrowCal", "java.util", "java.text", "Mitz Mode")


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
        print(f"FAIL {label} mixed: {bad}", file=sys.stderr)
        sys.exit(1)


def check_ph(keys: list[str], he: list[str], label: str) -> None:
    for i, (k, t) in enumerate(zip(keys, he)):
        if extract_placeholders(k) != extract_placeholders(t):
            print(f"FAIL {label} [{i+1}] ph key={extract_placeholders(k)!r} he={extract_placeholders(t)!r}")
            sys.exit(1)
    n = sum(len(extract_placeholders(k)) for k in keys)
    print(f"PLACEHOLDERS OK {label}: {n} tokens")


ROSH = (
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

OMER = (
    "ספירת העומר מקשרת את פסח לשבועות — ספירת כל יום מיציאת מצרים לקראת קבלת התורה.\n\n"
    "היום בעומר: $todaySummary (יום $day מתוך 49).\n\n"
    "ספירת הלילה:\n"
    "• ליל $tonight — ספרו $todaySummary אחרי צאת הכוכבים$timePart.\n"
    "$nextNightLine\n\n"
    "איך לספור:\n"
    "• עמדו וברכו לפני הספירה אם עדיין אומרים ברכה "
    "(אם פספסתם יום, שאלו את הרב לפני המשך עם ברכה).\n"
    '• אמרו: "${omerCountSpeechPhrase(day)}"\n'
    "• ספרו אחרי צאת הכוכבים (tzeit); סיימו לפני השחר. "
    "אם שכחתם בלילה, ספרו ביום למחרת בלי ברכה. "
    "אם עושים זאת לפני השקיעה, אפשר להמשיך בלילות הבאים עם ברכה. "
    "מאבדים את הברכה לצמיתות רק אם מפספסים מחזור של 24 שעות שלם "
    "(גם לילה וגם היום שלאחריו) — שאלו את הרב.\n\n"
    "$nusachWhen"
)

SHEMINI = (
    'שמיני עצרת מתחיל הערב${if (profile.isInIsrael) " — בישראל זה גם שמחת תורה." else "."}\n\n'
    "הערב ומחר:\n"
    "• הדליקו נרות יום טוב.\n"
    "${shehecheyanuErevLines(HebrewCalendarEngine.SHEMINI_ATZERES, tomorrowCal, profile)}\n"
    "• אין לולב בשמיני עצרת — המצווה הסתיימה ביום השביעי של סוכות.\n"
    "${if (profile.isInIsrael)"
)

# unicode-safe rechilut (no Latin in Hebrew words)
RECHILUT = (
    "\u05E8\u05DB\u05D9\u05DC\u05D5\u05EA \u05D4\u05D9\u05D0 \u05E0\u05E9\u05D9\u05D0\u05EA \u05D3\u05D1\u05E8\u05D9\u05DD "
    "\u05E9\u05D9\u05D5\u05E6\u05E8\u05D9\u05DD \u05E9\u05E0\u05D0\u05D4 \u2014 \u05D2\u05DD \u05D0\u05DD \u05D4\u05DD \u05D0\u05DE\u05EA. "
    "\u05D3\u05D5\u05D2\u05DE\u05D4: \u05DC\u05E1\u05E4\u05E8 \u05DC\u05E8\u05D0\u05D5\u05D1\u05DF \u05DE\u05D4 \u05E9\u05DE\u05E2\u05D5\u05DF "
    "\u05D0\u05DE\u05E8 \u05E2\u05DC\u05D9\u05D5 \u05DB\u05D3\u05D9 \u05DC\u05E2\u05D5\u05E8\u05E8 \u05DE\u05D7\u05DC\u05D5\u05E7\u05EA. "
    "\u05DC\u05E9\u05D5\u05DF \u05D4\u05E8\u05E2 \u05DE\u05D3\u05D1\u05E8\u05EA \u05E8\u05E2 \u05E2\u05DC \u05DE\u05D9\u05E9\u05D4\u05D5; "
    "\u05E8\u05DB\u05D9\u05DC\u05D5\u05EA \u05DE\u05E4\u05D9\u05E6\u05D4 \u05DE\u05D9\u05DC\u05D9\u05DD \u05D1\u05D9\u05DF \u05E6\u05D3\u05D3\u05D9\u05DD. "
    "\u05D4\u05D7\u05E4\u05E5 \u05D7\u05D9\u05D9\u05DD \u05DE\u05E7\u05D3\u05D9\u05E9 \u05E4\u05E8\u05E7\u05D9\u05DD \u05E8\u05D1\u05D9\u05DD "
    "\u05DC\u05D3\u05D9\u05D1\u05D5\u05E8 \u05DE\u05D5\u05EA\u05E8 \u05DC\u05D4\u05D2\u05E0\u05D4 \u05D0\u05D5 \u05DC\u05E6\u05D5\u05E8\u05DA \u05D1\u05D5\u05E0\u05D4; "
    "\u05E1\u05D9\u05E4\u05D5\u05E8\u05D9 \u00AB\u05E9\u05DE\u05E2\u05EA\u00BB \u05DE\u05D6\u05D3\u05DE\u05E0\u05D9\u05DD "
    "\u05DB\u05DE\u05E2\u05D8 \u05D0\u05E3 \u05E4\u05E2\u05DD \u05D0\u05D9\u05E0\u05DD \u05DE\u05D5\u05EA\u05E8\u05D9\u05DD."
)

RAV = (
    "\u05E8\u05D1 (\u05DE\u00AB\u05D2\u05D3\u05D5\u05DC\u00BB \u05D0\u05D5 \u00AB\u05D0\u05D3\u05D5\u05DF\u00BB) "
    "\u05D4\u05D5\u05D0 \u05EA\u05D5\u05D0\u05E8 \u05DC\u05DC\u05DE\u05D3 \u05EA\u05D5\u05E8\u05D4 \u05DE\u05E0\u05D5\u05E1\u05D4 \u05E9\u05E4\u05D5\u05E1\u05E7 \u05D1\u05D4\u05DC\u05DB\u05D4 "
    "\u2014 \u05D1\u05DE\u05D9\u05D5\u05D7\u05D3 \u05D1\u05E7\u05D4\u05D9\u05DC\u05D5\u05EA \u05D0\u05D5\u05E8\u05EA\u05D5\u05D3\u05D5\u05E7\u05E1\u05D9\u05D5\u05EA "
    "\u05D5\u05DE\u05E1\u05D5\u05E8\u05EA\u05D9\u05D5\u05EA. \u05E8\u05D1 \u05D4\u05D5\u05D0 posek \u05DC\u05E9\u05D0\u05DC\u05D5\u05EA \u05DE\u05E2\u05E9\u05D9\u05D5\u05EA: "
    "\u05DB\u05E9\u05E8\u05D5\u05EA, \u05E9\u05D1\u05EA, \u05E0\u05D9\u05D3\u05D4, \u05E2\u05E1\u05E7\u05D9\u05DD \u05D5\u05D7\u05D2\u05D9\u05DD. "
    "\u05DC\u05D0 \u05DB\u05DC rabbi \u05D1\u05E7\u05D9 \u05DE\u05E1\u05E4\u05D9\u05E7 \u05DB\u05D3\u05D9 \u05DC\u05D4\u05D9\u05D7\u05E9\u05D1 \u05E8\u05D1."
)

SELICHOT = (
    "\u05E1\u05DC\u05D9\u05D7\u05D5\u05EA \u05D4\u05DD \u05EA\u05E4\u05D9\u05DC\u05D5\u05EA \u05DB\u05E4\u05E8\u05D4 "
    "\u05D4\u05E0\u05D0\u05DE\u05E8\u05D5\u05EA \u05DC\u05E4\u05E0\u05D9 \u05E8\u05D0\u05E9 \u05D4\u05E9\u05E0\u05D4 "
    "(\u05D0\u05E9\u05DB\u05E0\u05D6\u05D9\u05DD \u05DC\u05E2\u05D9\u05EA\u05D9\u05DD \u05E7\u05E8\u05D5\u05D1\u05D5\u05EA "
    "\u05DE\u05DE\u05D5\u05E6\u05D0\u05D9 \u05D4\u05E9\u05D1\u05EA \u05E9\u05DC\u05E4\u05E0\u05D9\u05D5, "
    "\u05E1\u05E4\u05E8\u05D3\u05D9\u05DD \u05DE\u05D0\u05DC\u05D5\u05DC). "
    "\u05D4\u05DF \u05DB\u05D5\u05DC\u05DC\u05D5\u05EA \u05EA\u05D7\u05E0\u05D5\u05E0\u05D9\u05DD \u05E9\u05D9\u05E8\u05D9\u05D9\u05DD "
    "\u05D5\u05E9\u05DC\u05D5\u05E9 \u05E2\u05E9\u05E8\u05D4 \u05DE\u05D9\u05D3\u05D5\u05EA \u05D4\u05E8\u05D7\u05DE\u05D9\u05DD. "
    "\u05DC\u05E7\u05D5\u05DD \u05DE\u05D5\u05E7\u05D3\u05DD \u05D0\u05D5 \u05EA\u05E4\u05D9\u05DC\u05D4 \u05DE\u05D0\u05D5\u05D7\u05E8\u05EA "
    "\u05DC\u05E1\u05DC\u05D9\u05D7\u05D5\u05EA \u05DE\u05DB\u05D9\u05E0\u05D4 \u05D0\u05EA \u05D4\u05DC\u05D1 \u05DC\u05E8\u05E6\u05D9\u05E0\u05D5\u05EA "
    "\u05DC\u05E4\u05E0\u05D9 \u05D9\u05DE\u05D9 \u05D4\u05D0\u05D9\u05DE\u05D4. "
    "\u05D4\u05E0\u05D5\u05E1\u05D7 \u05D5\u05DC\u05D5\u05D7 \u05D4\u05D6\u05DE\u05E0\u05D9\u05DD \u05DE\u05E9\u05EA\u05E0\u05D9\u05DD "
    "\u2014 \u05D1\u05D3\u05E7\u05D5 \u05D1\u05D1\u05D9\u05EA \u05D4\u05DB\u05E0\u05E1\u05EA \u05E9\u05DC\u05DB\u05DD."
)

SCHACH = (
    "\u05E1\u05DB\u05DA \u05D4\u05D5\u05D0 \u05DB\u05D9\u05E1\u05D5\u05D9 \u05D4\u05E6\u05DE\u05D7\u05D9\u05DD "
    "\u05E2\u05DC \u05D2\u05D2 \u05D4\u05E1\u05D5\u05DB\u05D4 \u2014 \u05D1\u05DE\u05D1\u05D5\u05E7, \u05E2\u05E0\u05E4\u05D9\u05DD "
    "\u05D0\u05D5 \u05E7\u05E0\u05D9\u05DD \u2014 \u05E2\u05D1\u05D4 \u05DE\u05E1\u05E4\u05D9\u05E7 \u05E9\u05D4\u05E6\u05DC \u05DE\u05E8\u05D5\u05D1\u05D4 "
    "\u05E2\u05DC \u05D4\u05D7\u05DE\u05D4, \u05D0\u05DA \u05D3\u05DC\u05D9\u05DC \u05DE\u05E1\u05E4\u05D9\u05E7 \u05DC\u05E8\u05D0\u05D5\u05EA "
    "\u05DB\u05D5\u05DB\u05D1\u05D9\u05DD. \u05D7\u05D9\u05D9\u05D1 \u05DC\u05D4\u05D9\u05D5\u05EA \u05DE\u05D7\u05D5\u05DE\u05E8 "
    "\u05E9\u05D2\u05D3\u05DC \u05DE\u05DF \u05D4\u05D0\u05E8\u05E5 \u05D5\u05D0\u05D9\u05E0\u05D5 \u05DE\u05-ch\u05D5\u05D1\u05E8 "
    "\u05DC\u05D0\u05E8\u05E5 \u05DB\u05E9\u05D4\u05D5\u05D0 \u05E2\u05DC \u05D4\u05E1\u05D5\u05DB\u05D4. "
    "\u05DE\u05D5\u05E6\u05E8\u05D9\u05DD \u05DE\u05D5\u05D2\u05DE\u05E8\u05D9\u05DD \u05E4\u05E1\u05D5\u05DC\u05D9\u05DD "
    "\u05DB\u05D9 \u05D4\u05DD \u05DE\u05E7\u05D1\u05DC\u05D9\u05DD \u05D8\u05D5\u05DE\u05D0\u05D4. "
    "\u05E1\u05DB\u05DA \u05E4\u05E1\u05D5\u05DC \u05DE\u05D1\u05D8\u05DC \u05D0\u05EA \u05D4\u05DE\u05E6\u05D5\u05D5\u05D4 "
    "\u2014 \u05D1\u05E0\u05D5 \u05D1\u05D4\u05D3\u05E8\u05DB\u05D4 \u05D1\u05E4\u05E2\u05DD \u05D4\u05E8\u05D0\u05E9\u05D5\u05E0\u05D4."
)

# fix typo in SCHACH
SCHACH = SCHACH.replace("\u05DE\u05-ch\u05D5\u05D1\u05E8", "\u05DE\u05-ch\u05D5\u05D1\u05E8").replace(
    "\u05DE\u05-ch\u05D5\u05D1\u05E8", "\u05DE\u05-ch\u05D5\u05D1\u05E8"
)
SCHACH = (
    "\u05E1\u05DB\u05DA \u05D4\u05D5\u05D0 \u05DB\u05D9\u05E1\u05D5\u05D9 \u05D4\u05E6\u05DE\u05D7\u05D9\u05DD "
    "\u05E2\u05DC \u05D2\u05D2 \u05D4\u05E1\u05D5\u05DB\u05D4 \u2014 \u05D1\u05DE\u05D1\u05D5\u05E7, \u05E2\u05E0\u05E4\u05D9\u05DD "
    "\u05D0\u05D5 \u05E7\u05E0\u05D9\u05DD \u2014 \u05E2\u05D1\u05D4 \u05DE\u05E1\u05E4\u05D9\u05E7 \u05E9\u05D4\u05E6\u05DC \u05DE\u05E8\u05D5\u05D1\u05D4 "
    "\u05E2\u05DC \u05D4\u05D7\u05DE\u05D4, \u05D0\u05DA \u05D3\u05DC\u05D9\u05DC \u05DE\u05E1\u05E4\u05D9\u05E7 \u05DC\u05E8\u05D0\u05D5\u05EA "
    "\u05DB\u05D5\u05DB\u05D1\u05D9\u05DD. \u05D7\u05D9\u05D9\u05D1 \u05DC\u05D4\u05D9\u05D5\u05EA \u05DE\u05D7\u05D5\u05DE\u05E8 "
    "\u05E9\u05D2\u05D3\u05DC \u05DE\u05DF \u05D4\u05D0\u05E8\u05E5 \u05D5\u05D0\u05D9\u05E0\u05D5 \u05DE\u05-ch\u05D5\u05D1\u05E8 "
    "\u05DC\u05D0\u05E8\u05E5 \u05DB\u05E9\u05D4\u05D5\u05D0 \u05E2\u05DC \u05D4\u05E1\u05D5\u05DB\u05D4. "
    "\u05DE\u05D5\u05E6\u05E8\u05D9\u05DD \u05DE\u05D5\u05D2\u05DE\u05E8\u05D9\u05DD \u05E4\u05E1\u05D5\u05DC\u05D9\u05DD "
    "\u05DB\u05D9 \u05D4\u05DD \u05DE\u05E7\u05D1\u05DC\u05D9\u05DD \u05D8\u05D5\u05DE\u05D0\u05D4. "
    "\u05E1\u05DB\u05DA \u05E4\u05E1\u05D5\u05DC \u05DE\u05D1\u05D8\u05DC \u05D0\u05EA \u05D4\u05DE\u05E6\u05D5\u05D5\u05D4 "
    "\u2014 \u05D1\u05E0\u05D5 \u05D1\u05D4\u05D3\u05E8\u05DB\u05D4 \u05D1\u05E4\u05E2\u05DD \u05D4\u05E8\u05D0\u05E9\u05D5\u05E0\u05D4."
)
