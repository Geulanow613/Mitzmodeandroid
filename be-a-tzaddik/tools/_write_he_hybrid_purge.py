#!/usr/bin/env python3
"""Authoritative Hebrew overrides for hybrid-glyph corruption — wins before he_glue_fixes."""

from __future__ import annotations

import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CATALOG = ROOT / "data" / "translation-catalog" / "strings.json"
SHARDS = ROOT / "data" / "translation-catalog" / "shards" / "hebrew_fixes.json"
OUT = ROOT / "data" / "translation-catalog" / "human" / "he_hybrid_purge.json"

HYBRID = re.compile(r"[\u05d0-\u05ea][a-zA-Z]{2,}|[a-zA-Z]{2,}[\u05d0-\u05ea]")

HYBRID_TERM_FIXES: list[tuple[re.Pattern[str], str]] = [
    (re.compile(r"פesach", re.I), "פסח"),
    (re.compile(r"גabbaי", re.I), "גבאי"),
    (re.compile(r"אשכנaz", re.I), "אשכנז"),
    (re.compile(r"יום כיפur", re.I), "יום כיפור"),
    (re.compile(r"אתrog", re.I), "אתרוג"),
    (re.compile(r"בחagim", re.I), "בחגים"),
    (re.compile(r"חagim", re.I), "חגים"),
    (re.compile(r"מוסaf", re.I), "מוסף"),
    (re.compile(r"מidian", re.I), "מדין"),
]


def strip_hybrid(text: str) -> str:
    out = text
    for pat, repl in HYBRID_TERM_FIXES:
        out = pat.sub(repl, out)
    return out


# Long explainers — condensed quality Hebrew (full blocks live in checklist_explainers / he_fix batches).
LONG_PURGE_PREFIXES: dict[str, str] = {
    "Tefillin are two small black leather boxes": (
        "תפילין הן שתי קופסאות עור שחורות המכילות פרשיות כתובות בכתב יד, הנילבשות בתפילת שחרית. "
        "זו אחת המצוות היומיות החשובות לגברים יהודים.\n\n"
        "מה הן:\nתפילין (תְּפִלִּין) — שתי קופסאות עור, כל אחת עם ארבע פרשיות על קלף.\n"
        "• של יד — על הזרוע השמאלית (ימניים: ימין), ליד הלב, עם רצועה סביב הזרוע והיד\n"
        "• של ראש — במרכז הראש, מעל קו השיער\n\n"
        "מקור התורה:\n«וקשרתם לאות על ידך והיו לטוטפת בין עיניך» (דברים ו:ח).\n\n"
        "למה:\nתפילין מקשרות מחשבה (ראש), לב (יד) ומעשה (רצועות) — מצוות יומית של קדושה."
    ),
    "Yom Kippur — Day of Atonement. Full 25-hour fast": (
        "יום כיפור — יום הכיפורים. צום מלא של כ-25 שעות; חמשה עינויים משקיעה הערב עד צאת הכוכבים מחר.\n\n"
        "היום לפני הצום:\n"
        "• מצוות אכילה: יש חובה הלכתית לאכול ולשתות ביום ערב יום כיפור (ברכות ח:ב)\n"
        "• סעודה מפסקת חגיגית לפני השקיעה\n"
        "• הדלקת נרות לפני כל הנרות לפני השקיעה\n"
        "• אחרי הדלקת הנרות וברכתה — יום כיפור התחיל עבורכם; אין נסיעה ברכב\n\n"
        "במהלך הצום: אין אכילה, שתייה, רחצה, סיכה, נעילת סנדל ותשמיש חדר. תפילות כל היום."
    ),
    "Psalms and poetic passages recited before the Shema": (
        "מזמורי תהילים וקטעים לפני שמע ועמידה להכנת הלב והנפש לתפילה.\n\n"
        "מה הם:\nפסוקי דזמרה (פְּסוּקֵי דְזִמְרָא) — אוסף תהילים ופסוקים לפני התפילה המרכזית בבוקר.\n\n"
        "למה:\nהתלמוד משווה כניסה ישירה לעמידה לכניסה לפני מלך בלי בקשת רשות.\n\n"
        "מינימום בזמן קצר:\n• ברוך שאמר\n• אשרי (תהילים קמ״ה)\n• ישתבח"
    ),
    "Misheyakir (מִשֶּׁיַּכִּיר) is when": (
        "משיכיר (מִשֶּׁיַּכִּיר) — כשיש מספיק אור טבעי לזהות מכר מזדמן מכארבע אמות. "
        "זה הזמן המוקדם לברכת טלית ותפילין — לרוב כ-35–50 דקות אחרי עלות השחר (משתנה לפי עונה ומקום). "
        "לפני משיכיר אין קיום המצווה גם אחרי השחר. משנה ברורה ממליצה להמתין עד משיכיר, ללבוש טלית ותפילין ולקרוא שמע ועמידה. "
        "הגמרא (ברכות יד:) משווה קריאת שמע בלי טלית ותפילין לעדות שקר. "
        "בצורך גדול: איגרות משה (או״ח ד:ו) מתיר ללבוש אחרי עלות השחר בלי ברכה, לתפלל, ואז להזיז מעט ולברך אחרי משיכיר."
    ),
    "misheyakir — Misheyakir": (
        "משיכיר — זמן מספיק אור לזיהוי מכר; הזמן המוקדם לטלית ותפילין ביום חול."
    ),
}


PURGE_PREFIXES: list[tuple[str, str]] = [
    ("A revi'it (רביעית)", "רביעית (רביעית) היא יחידת נפח נוזלית הלכתית, רבע לוג — בערך נפח ביצה וחצי."),
    ("revi'it — A revi'it", "רביעית — יחידת נפח נוזלית הלכתית, רבע לוג."),
    ("Ashkenaz Selichot begin", (
        "סליחות אשכנז מתחילות במוצאי שבת שלפני ראש השנה, לפחות ארבעה ימי סליחות לפני החג (פניני הלכה טו:ב-ה; הרמ\"א)."
    )),
    ("Chabad follows the standard Ashkenazi Selichot", (
        "חב\"ד עוקבת אחר לוח הסליחות האשכנזי — מתחיל במוצאי שבת שלפני ראש השנה."
    )),
    ("Do the powerful mitzvah of praying for others", (
        "קיימו את מצוות התפילה בעד אחרים! חשבו על מי שמחפש שידוך והתפללו בעדו בכנות 💑."
    )),
    ("K'fi daato means", (
        "כפי דעתו — חינוך ילד לפי מה שהוא מסוגל להבין ולקיים באמינות, לא לדרוש שלמות של מבוגר."
    )),
    ("k'fi daato — K'fi daato", "כפי דעתו — חינוך ילד לפי יכולתו."),
    ("Learn about shechitah", (
        "למדו על שחיטה כשרה וחמשת הדרישות שחייבות להתקיים — אחרת הבשר אסור!"
    )),
    ("Learn about the Melacha of Mocheik", (
        "למדו על מלאכת מוחק בשבת — מחיקה מכוונת של סימנים משמעותיים; נגזרת מכתיבה."
    )),
    ("Learn and grow! Watch any Torah class", (
        "למדו וגדלו! צפו בשיעור תורה שמעניין אתכם 📚 — פרשה, היסטוריה או הלכה מעשית."
    )),
    ("Shema al HaMitah is the bedtime Shema", (
        "קריאת שמע על המיטה — הצהרת אמונה והפקדת הנפש לה' לפני השינה; רבים מוסיפים תהילים צ\"א והמפיל."
    )),
    ("Shema al HaMitah — Shema al HaMitah", (
        "קריאת שמע על המיטה — הצהרת אמונה לפני השינה; תהילים צ\"א והמפיל."
    )),
    ("Taanit Bechorot (תַּעֲנִית בְּכוֹרוֹת)", (
        "תענית בכורות — צום הבכורים בערב פסח; רבים משתתפים בסיום כדי להיפטר."
    )),
    ("The Talmud (Gemara + Mishnah)", (
        "התלמוד (גמרא ומשנה) — הדיון הרבני המרכזי בהלכה ובאגדה; התפתח בבבל ובארץ ישראל. דף יומי מסיים את כל התלמוד בכשבע שנים."
    )),
    ("The Torah commands us to write G-d's words", (
        "התורה מצווה לכתוב את דברי ה' על מזוזות ביתך ושעריך (דברים ו:ט) — בהצמדת מזוזה לכל פתח."
    )),
    ("Yom Kippur — Yom Kippur is the Day of Atonement", (
        "יום כיפור — יום הכיפורים; צום, וידוי ותפילה; איסורי מלאכה כמו בשבת."
    )),
    ("lashon hara — Lashon hara", (
        "לשון הרע — דיבור שלילי על יהודי אחר גם כשהמידע נכון, אלא אם יש תכלית הלכתית בונה."
    )),
    ("mechirat chametz — Mechirat chametz", (
        "מכירת חמץ — מכירת חמץ לגוי דרך הרב לפני פסח; יש לאשר מוקדם ולאחסן את החמץ הנמכר."
    )),
    ("tevilat keilim — Tevilat keilim", (
        "טבילת כלים — טבילת כלי מזון חדשים מיוצר גוי במקווה, כזכר לכלי ישראל אחר מלחמת מidian."
    )),
    ("Borer (selecting) forbids", (
        "בורר (בחירה) אוסר למיין ערימה מעורבת בשבת על ידי הפרדת לא רצוי מרצוי, "
        "אלא אם מתקיימים שלושה תנאים: לוקחים את הטוב מתוך הרע, ביד (לא במסננת ייעודית), לשימוש מיידי. "
        "דוגמה: לבחור עצמות מהצלחת ממש לפני האכילה — מותר; למיין קערת סלט למחר — אסור."
    )),
    ("Borer — Borer", "בורר — אוסר בחירה בשבת אלא לפי תנאי בורר."),
    ("Full Hallel — complete", "הלל מלא — מזמורי הלל השלמים בחגים ובחנוכה"),
    ("Misheyakir (מִשֶּׁיַּכִּיר)", (
        "משיכיר (מִשֶּׁיַּכִּיר) — כשיש מספיק אור טבעי לזהות מכר מזדמן מכארבע אמות. "
        "זה הזמן המוקדם לברכת טלית ותפילין — לרוב כ-35–50 דקות אחרי עלות השחר (משתנה לפי עונה ומקום). "
        "לפני משיכיר אין קיום המצווה גם אחרי השחר. משנה ברורה ממליצה להמתין עד משיכיר. "
        "הגמרא (ברכות יד:) משווה קריאת שמע בלי טלית ותפילין לעדות שקר — להימנע אם אפשר. "
        "בצורך גדול: איגרות משה (או״ח ד:ו) מתיר ללבוש אחרי עלות השחר בלי ברכה, לתפלל, ואז להזיז מעט ולברך אחרי משיכיר."
    )),
    ("Musaf (only on Rosh Chodesh", "מוסף (רק בראש חודש, חגים ושבת)"),
    ("Put on Tefillin during morning", "הנחת תפילין בתפילת שחרית (מלבד שבת וחגים)"),
    ("Tefillin are two small black leather", LONG_PURGE_PREFIXES["Tefillin are two small black leather boxes"]),
    ("Yom Kippur — Day of Atonement. Full 25-hour", LONG_PURGE_PREFIXES["Yom Kippur — Day of Atonement. Full 25-hour fast"]),
]


def load_create_he_map() -> dict[str, str]:
    tools = ROOT / "tools"
    sys.path.insert(0, str(tools))
    try:
        from create_he_map import HE  # type: ignore
        return dict(HE)
    except Exception:
        return {}


def purge_for_key(key: str, pooled: dict[str, str]) -> str | None:
    if key in pooled and not HYBRID.search(pooled[key]):
        return pooled[key]
    for prefix, val in PURGE_PREFIXES:
        if key.startswith(prefix):
            return val
    for prefix, val in LONG_PURGE_PREFIXES.items():
        if key.startswith(prefix):
            return val
    return None


def apply_authoritative_purges(keys: list[str]) -> dict[str, str]:
    """Always emit fixes for known corrupt keys — do not depend on bundle scan."""
    he: dict[str, str] = {}
    prefixes = sorted(PURGE_PREFIXES, key=lambda kv: len(kv[0]), reverse=True)
    long_prefixes = sorted(LONG_PURGE_PREFIXES.items(), key=lambda kv: len(kv[0]), reverse=True)
    for k in keys:
        for prefix, val in prefixes:
            if k.startswith(prefix):
                cleaned = strip_hybrid(val)
                if not HYBRID.search(cleaned):
                    he[k] = cleaned
                break
        else:
            for prefix, val in long_prefixes:
                if k.startswith(prefix):
                    cleaned = strip_hybrid(val)
                    if not HYBRID.search(cleaned):
                        he[k] = cleaned
                    break
    return he


def main() -> None:
    strings = json.loads(CATALOG.read_text(encoding="utf-8"))["strings"]
    hebrew_fixes = json.loads(SHARDS.read_text(encoding="utf-8")).get("he", {})
    bundle = {}
    if (ROOT / "shared/src/commonMain/composeResources/files/translations/he.json").is_file():
        bundle = json.loads(
            (ROOT / "shared/src/commonMain/composeResources/files/translations/he.json").read_text(encoding="utf-8")
        ).get("entries", {})

    pooled: dict[str, str] = {}
    pooled.update(load_create_he_map())
    pooled.update(hebrew_fixes)

    he = apply_authoritative_purges(strings)

    for k in strings:
        if k in he:
            continue
        current = bundle.get(k, k)
        if HYBRID.search(current):
            fixed = purge_for_key(k, pooled)
            if fixed and not HYBRID.search(fixed):
                he[k] = strip_hybrid(fixed)
        elif k in pooled and not HYBRID.search(pooled[k]):
            he[k] = pooled[k]

    OUT.write_text(json.dumps({"he": he}, ensure_ascii=False, indent=2), encoding="utf-8")
    forced = sum(1 for k in he if k in strings and any(k.startswith(p) for p, _ in PURGE_PREFIXES))
    print(
        f"wrote {OUT} — {len(he)} purge entries "
        f"(authoritative: {forced}, hybrid fixes: {sum(1 for k in he if HYBRID.search(bundle.get(k,'')))})"
    )


if __name__ == "__main__":
    main()
