#!/usr/bin/env python3
"""Build and validate he_fix_007/008/009_he_only.json."""

from __future__ import annotations

import json
import re
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
        raise SystemExit(f"{label} mixed Latin/Cyrillic in Hebrew: {bad}")


def check_placeholders(keys: list[str], he: list[str], label: str) -> None:
    issues = []
    for i, (key, tr) in enumerate(zip(keys, he)):
        kp = extract_placeholders(key)
        tp = extract_placeholders(tr)
        if kp != tp:
            issues.append(f"  [{i + 1}] key={kp!r} he={tp!r}")
    if issues:
        raise SystemExit(f"PLACEHOLDER MISMATCH {label}:\n" + "\n".join(issues))
    n = sum(len(extract_placeholders(k)) for k in keys)
    print(f"PLACEHOLDERS OK {label}: {n} tokens")


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
    "• ספרו אחרי צאת הכוכבים (tzeit); סיימו לפני השחר. "
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

SELICHOT = (
    "סליחות הם תפילות כפרה הנאמרות לפני ראש השנה (אשכנזים לעיתים קרובות ממוצאי השבת "
    "שלפניו, ספרדים מאלול). הן כוללות תחנונים שיריים ושלוש עשרה מידות הרחמים. "
    "לקום מוקדם או תפילה מאוחרת לסליחות מכינה את הלב לרצינות לפני ימי האימה. "
    "הנוסח ולוח הזמנים משתנים — בדקו בבית הכנסet שלכם."
)

SEPHARDI = (
    "יהודי ספרד שורשיהם בתפוצת חצר היבשה וקהילות ים תיכוניות קשורות. התפילה בדרך כלל "
    "בנוסח ספרד (בית יוסף). ההלכה עוקבת אחר השulchan Aruch ופoskim כמו הרav אovadia "
    "יוסף זצ\"ל. אורז וkitniyot מותרים בהלכה בפesach כשאshkenazim נוהגים minhag kitniyot. "
    "אל תבלבלו עם «נusach ספרד» בסiddur chasidic (טקס אshkenazi) או עם עדot המזrach."
)

RAV = (
    "רב (מ«גדול» או «אdון») הוא תואר לlamd תורah מnוסה שpוסק בהlכah — "
    "במiוחd בקהilות אorתodokסיות ומsורתיות. הרav הוא posek לשאalות מעשיות: "
    "כשרות, שbת, נidah, עסקים וחagim. לא כל rabbi בqי מסpיק כדי להיחשב רav."
)

SHEMA_AL_HAMITAH = (
    "קריאat שma על המיטah היא שma שלפני השינה — הכרזat אמונה והפקadat הנשma לה' "
    "לפני השינה. רבים מוסיפים תhילim צ\"א ופסukim נוסfim. המapil נאמר בעת שכibה "
    "לישon בפועל. נשים חייבות בהגna זו לפי ההlכah. כמה דקות תפילah רgועה "
    "מסיימות את היom היטb."
)

BEDTIME_SHEMA = (
    "נאמר מיד לפני השינה; קריאat שma לפני השינה היא חובה נeparדת מהשma שנאמר במaariv.\n\n"
    "מה זה:\n"
    "קריאat שma על המיטah (קְרִיאַת שְׁמַע עַל הַמִּטָּה — «קריאat השma על המיטah») "
    "היא תפילat ליל קצrה שנאמרת כmaaseh הרוחni האחרון לפני השינה. התלמud מlמd "
    "שhiיא מגינה על האdם במהלך הלילה.\n\n"
    "מה לomר:\n"
    "• לפchot: הסעיף הראשון של השma («שma ישrael... ובשkbך ובqומך»)\n"
    "• מommלץ: שלושת סעיפi השma המleאim\n"
    "• כולl גם: ברכat המapil (הפריט הבא), מsפר מzmorim (כolל תhילim צ\"א — "
    "מzmor הגna), ותfilot קtsרot נוסfot\n\n"
    "למה שma שni?\n"
    "גם אם כבר אמרteם את השma כחלק מmaariv, שma לפני השינah היא מtsvaה nפרdת — "
    "zman אcher, מtרה אcherת. שma של maariv מqיים את מtsvat הערb; שma לפני השינah "
    "מyועd במיוחd להgana במהלך השina.\n\n"
    "תzמון:\n"
    "אmeru qרov כcol האpשר לzman השina בפועל. bאידיאal לפני חtsot halayla (chatzos halayla). "
    "אם hולchim לyשon מאוחr יותr — אz."
)

TRANSLATIONS = {}
