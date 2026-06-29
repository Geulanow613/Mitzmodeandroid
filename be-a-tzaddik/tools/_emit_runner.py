#!/usr/bin/env python3
"""One-shot runner: build pure HE strings and write he_fix_009/011/012 JSON."""
from __future__ import annotations

import json
import re
import sys
from pathlib import Path

_TOOLS = Path(__file__).resolve().parent
sys.path.insert(0, str(_TOOLS))

from build_he_fix_all import (  # noqa: E402
    AG,
    AR,
    CHZ,
    CZ,
    ET,
    GM,
    HGD,
    HL,
    HD,
    LL,
    RV,
    SKN,
    TH,
    TORAH,
    USHPIZIN,
    YK,
    build_he_011 as _build_he_011_raw,
    hb,
)

ROOT = _TOOLS.parent / "data" / "translation-catalog"
HUMAN = ROOT / "human"
BUNDLED = _TOOLS.parent / "data" / "bundled-translations" / "he.json"

MIXED = re.compile(
    r"[\u0590-\u05FF]+[A-Za-z\u0400-\u04FF]+|[A-Za-z\u0400-\u04FF]+[\u0590-\u05FF]+"
)
SKIP = (
    "HebrewCalendar",
    "profile",
    "tomorrowCal",
    "fullSchedule",
    "alotTomorrow",
    "diasporaSecond",
)

SHUAR = hb(0x05E9, 0x05D5, 0x05DC, 0x05D7, 0x05DF, 0x0020, 0x05E2, 0x05E8, 0x05D5, 0x05DA)
REMA = hb(0x05D4, 0x05E8, 0x05DE, 0x05D4)
RBI_SHIMON = hb(
    0x05E8, 0x05D1, 0x05D9, 0x0020, 0x05E9, 0x05DE, 0x05E2, 0x05D5, 0x05DF,
    0x0020, 0x05D1, 0x05E8, 0x0020, 0x05D9, 0x05D5, 0x05D7, 0x05D0, 0x05D9,
)
YECHAVEH = hb(0x05D9, 0x05D7, 0x05D5, 0x05D5, 0x05D4, 0x0020, 0x05D3, 0x05E2, 0x05EA)
RBI_OVADIA = hb(
    0x05D4, 0x05E8, 0x05D1, 0x0020, 0x05E2, 0x05D5, 0x05D1, 0x05D3, 0x05D9, 0x05D4,
    0x0020, 0x05D9, 0x05D5, 0x05E1, 0x05E3,
)
MHRH = hb(
    0x05DE, 0x05E9, 0x05D9, 0x05D1, 0x0020, 0x05D4, 0x05E8, 0x05D5, 0x05D7, 0x0020, 0x05D5, 0x05DE, 0x05D5, 0x05E8, 0x05D9, 0x05D3, 0x0020, 0x05D4, 0x05D2, 0x05E9, 0x05DD,
)
TFG = hb(0x05EA, 0x05E4, 0x05D9, 0x05DC, 0x05EA, 0x0020, 0x05D2, 0x05E9, 0x05DD)
OMR = hb(0x05D4, 0x05E2, 0x05D5, 0x05DE, 0x05E8)
SYVN = hb(0x05E1, 0x05D9, 0x05D5, 0x05DF)
TKN = hb(
    0x05EA, 0x05D9, 0x05E7, 0x05D5, 0x05DF, 0x0020, 0x05DC, 0x05D9, 0x05DC, 0x0020, 0x05E9, 0x05D1, 0x05D5, 0x05E2, 0x05D5, 0x05EA,
)
SPHR = hb(0x05E1, 0x05E4, 0x05D9, 0x05E8, 0x05AA)
HOSHANA_RABA = hb(0x05D4, 0x05D5, 0x05E9, 0x05E2, 0x05E0, 0x05D0, 0x0020, 0x05E8, 0x05D1, 0x05D4)
ERUV = hb(0x05E2, 0x05D9, 0x05E8, 0x05D5, 0x05D1, 0x0020, 0x05EA, 0x05D1, 0x05E9, 0x05D9, 0x05DC, 0x05D9, 0x05DF)
PURIM = hb(0x05E4, 0x05D5, 0x05E8, 0x05D9, 0x05DD)
MESHULASH = hb(0x05DE, 0x05E9, 0x05D5, 0x05DC, 0x05E9)
YRL = hb(0x05D9, 0x05E8, 0x05D5, 0x05E9, 0x05DC, 0x05D9, 0x05DD)
TAMIM = hb(0x05D8, 0x05E2, 0x05DE, 0x05D9, 0x05DD)
TAAMEI = hb(0x05D8, 0x05E2, 0x05DE, 0x05D9, 0x0020, 0x05D4, 0x05DE, 0x05E7, 0x05E8, 0x05D0)
CHUMASH = hb(0x05D7, 0x05D5, 0x05DE, 0x05E9)
TARGUM = hb(0x05EA, 0x05E8, 0x05D2, 0x05D5, 0x05DD)
HMITAH = hb(0x05D4, 0x05DE, 0x05D9, 0x05D8, 0x05D4)
KR_SHMA = hb(0x05E7, 0x05E8, 0x05D9, 0x05D0, 0x05EA, 0x0020, 0x05E9, 0x05DE, 0x05E2)
HMPIL = hb(0x05D4, 0x05DE, 0x05D0, 0x05E4, 0x05D9, 0x05DC)
BIRKAT_KOHANIM = hb(
    0x05D1, 0x05E8, 0x05DB, 0x05AA, 0x0020, 0x05DB, 0x05D4, 0x05E0, 0x05D9, 0x05DD,
)
LEISHEV = hb(
    0x05DC, 0x05D9, 0x05E9, 0x05D1, 0x0020, 0x05D1, 0x05E1, 0x05D5, 0x05DB, 0x05D4,
)
SAFek = hb(0x05E1, 0x05E4, 0x05E7, 0x0020, 0x05D3, 0x05D9, 0x05D5, 0x05DE, 0x05D0)
TANAI = hb(0x05EA, 0x05E0, 0x05D0, 0x05D9)
ZMAN = hb(0x05D6, 0x05DE, 0x05DF)
BAT_MZ = hb(0x05D1, 0x05EA, 0x0020, 0x05DE, 0x05E6, 0x05D5, 0x05D4)
HOSHANOT = hb(0x05D4, 0x05D5, 0x05E9, 0x05E2, 0x05E0, 0x05D5, 0x05EA)


def strip_title(text: str) -> str:
    return text.split(" — ", 1)[1] if " — " in text else text


def check(strings: list[str], label: str) -> None:
    bad = []
    for i, s in enumerate(strings):
        for m in MIXED.finditer(s):
            g = m.group()
            if any(x in g for x in SKIP):
                continue
            bad.append((i + 1, g))
    if bad:
        raise SystemExit(f"{label}: {bad}")


def write(name: str, strings: list[str]) -> None:
    check(strings, name)
    keys = json.loads((ROOT / f"_keys_{name}.json").read_text(encoding="utf-8"))
    assert len(strings) == len(keys)
    p = HUMAN / f"{name}_he_only.json"
    p.write_text(json.dumps({"he": strings}, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"OK {p.name}")


def he_011() -> list[str]:
    s = _build_he_011_raw()
    s[6] = s[6].replace("קודекс", "ספר")
    return s


def he_009() -> list[str]:
    he016 = json.loads((HUMAN / "he_fix_016_he_only.json").read_text(encoding="utf-8"))["he"]
    he017 = json.loads((HUMAN / "he_fix_017_he_only.json").read_text(encoding="utf-8"))["he"]
    entries = json.loads(BUNDLED.read_text(encoding="utf-8"))["entries"]
    keys = json.loads((ROOT / "_keys_he_fix_009.json").read_text(encoding="utf-8"))
    shemini_begins = entries[keys[12]]

    sephardi = (
        "יהודים ספרדים שורשיהם בגלות ספרד והקהילות הים-תיכוניות הקשורות. "
        "התפילה בדרך כלל נוסח ספרד (בית יוסף). "
        f"ה{HL} עוקבת בדרך כלל אחר {SHUAR} ופוסקים כמו {RBI_OVADIA} זצ\"ל. "
        "אורז וקטניות מותרים בהלכה בפסח, בעוד אשכנזים נוהגים איסור קטניות. "
        "אל תבלבלו עם «נוסח ספרד» בסידור חסידי (נוסח אשכנזי) "
        "או עם עדות המזרח (נוסח מזרחי / צפון-אפריקאי)."
    )

    shema = (
        f"שמע על {HMITAH} הוא {KR_SHMA} לפני השינה — "
        "הכרזת אמונה והפקדת הנשמה בידי ה' לפני שינה. "
        f"רבים מוסיפים {TH} צ\"א ופסוקים נוספים. "
        f"«{HMPIL}» נאמר כששוכבים בפועל לישון. "
        "נשים חייבות בהגנה זו לפי ההלכה. "
        "כמה דקות תפילה שקטה מסיימות את היום יפה."
    )

    return [
        (
            "סליחות — סליחות הן תפילות כפרה הנאמרות לפני ראש השנה "
            "(אשכנזים לעיתים קרובות ממוצאי שבת שלפני, ספרדים מאלול). "
            "הן כוללות פיוטים של תחינות ושלוש עשרה מידות הרחמים. "
            "קימה מוקדמת או השהייה מאוחרת לסליחות מגדירה רצינות לפני ימים נוראים. "
            "הנוסח והלוח משתנים — בדקו בבית הכנסת שלכם."
        ),
        sephardi,
        (
            f"מנהג ספרדי (שו\"ע או\"ח תצ\"ג:א–ב; פניני {HL} 05-03-03): "
            f"אבלות מפסח עד בוקר יום ל\"ד ב{OMR} (ל\"ד). "
            f"מוזיקה בל\"ג ב{OMR} לכבוד {RBI_SHIMON} מותרת, "
            "אך חתונות ותספורות נשארות אסורות עד בוקר ל\"ד "
            f"לפי פסק ספרדי נפוץ ({RBI_OVADIA}, {YECHAVEH} 3:31). "
            f"קהילות מסוימות (כגון טורקיה ומצרים) מסיימות אבל בל\"ג ב{OMR} — "
            "עקבו אחר הקהילה.\n\n"
            f"שאלו את {RV} איזו מסורה אתם עוקבים ומתי ההגבלות מתחילות ומסתיימות."
        ),
        f"ספרדי — {sephardi}",
        strip_title(he016[14]),
        (
            f"שבועות נצפה ב-ו' ב{SYVN} (יומיים בגולה). "
            "הוא חוגג מתן תורה בהר סיני, שבועיים אחרי יציאת מצרים.\n\n"
            "מנהגים:\n"
            f"• לימוד תורה כל הלילה ({TKN})\n"
            "• אכילת מאכלי חלב — סיבות שונות ניתנו, ביניהן "
            "שעם ישראל עדיין לא קיבל את דיני הכשרות בשר\n"
            "• קריאת מגילת רות, שעניני מסירות נפש לתורה משקפים את קבלת התורה\n"
            "• שמיעת עשרת הדיברות בבית הכנסת\n\n"
            f"שבועות חל בדיוק חמישים יום אחרי ליל שני של פסח, בסוף {SPHR} ה{OMR}. "
            "יום טוב מלא."
        ),
        strip_title(he017[0]),
        (
            f"{SKN} — {SKN} היא נוכחות ה' השורה בעולם — במיוחד קשורה לבית המקדש, "
            "לישראל בגלות, ולקדושה בנישואין ובשבת. במסורת קידוש לבנה אומרים "
            f"שברכת הירח דומה לקבלת פני ה{SKN}. "
            "זו לא ישות נפרדת; כך אנו מדברים על קרבת ה'."
        ),
        shema,
        f"שמע על המיטה — {shema}",
        (
            "שמיני עצרet (כ\"ב בתשרי בגולה) — יom שמינi של סוכot.\n\n"
            "יom טוב:\n"
            "• יom טוב מלא — אין מלאכה; קידush וארוחות חagigיות.\n"
            f"• סוכah בגולה: בשל {SAFek} ({HL} איזה יom הוא איזה), "
            f"אשכנזים בגולה חייבים לאכol את כol הארוחot העיקרiot בסוכah "
            f"בשmini עצרet, אף שברכat «{LEISHEV}» נשמטet לgמרi. "
            f"מנהגי ספרד וחב\"ד משתנים — אשרu עם {RV}.\n\n"
            "תפילה:\n"
            f"• מעבר ליטורgi: במוסף היom, העolam היהudi עober לסeder תפילat "
            f"החורf, ומוסif «{MHRH}» בברכah השנiיה של העmidah. "
            f"{TFG} נאmer במוסף.\n"
            "• יizkor — קהilot אשכנaz מתפלlot תpilot zikaron.\n"
            "• הלel מלא ומוסף; עמida של יom טוב.\n\n"
            "שמחat תורה מחר (כ\"ג בתשרi) בגולה — ראu פריט רשimat היom להקpות "
            "ושmaחat תורה.\n\n"
            f"הערb: הדliqו nerot יom טוב; אין {LL} בשmini עצרet."
        ),
        "PLACEHOLDER",
        shemini_begins,
        "PLACEHOLDER2",
        strip_title(he017[3]),
    ]


if __name__ == "__main__":
    write("he_fix_011", he_011())
    try:
        write("he_fix_009", he_009())
    except SystemExit as e:
        print("009 failed:", e)
