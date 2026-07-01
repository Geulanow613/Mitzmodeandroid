#!/usr/bin/env python3
"""Write human/he_glue_fixes.json — wins over quality_overrides.json at compile time."""

from __future__ import annotations

import json
import re
from pathlib import Path

from _he_glue_manual_data import MANUAL_ES, MANUAL_FR, MANUAL_RU, PREFIX_HE
from _he_paragraph_batch29_data import PREFIX_HE_BATCH29

ROOT = Path(__file__).resolve().parents[1]
CATALOG = ROOT / "data/translation-catalog/strings.json"
HUMAN = ROOT / "data/translation-catalog/human"
BUNDLE_HE = ROOT / "shared/src/commonMain/composeResources/files/translations/he.json"
OUT = HUMAN / "he_glue_fixes.json"

ARBA_COMMON = """ארבעת המינים — לולב, אתרוג, הדסים וערבות — ניטלים בכל יום של סוכות (מלבד שבת).

הארבעה:
• לולב — ענף גמרא סגור (לפחות 3 טפחים)
• אתרוג — יפה ושלם ברובו (פיטום — אם קיים — צריך להישאר שלם)
• הדסים — שלושה ענפי הדס
• ערבות — שני ענפי ערבה

איך לקיים (כולם):
• להרכיב את הלולב: קושרים לולב, הדסים וערבות יחד (במחזיק לולב / קויסן). שדרת הלולב תבליט לפחות טפח אחד מעל ראשי ההדסים והערבות (שולחן ערוך או״ח תר״נ:א).
• לבדוק כשרות לפני יום טוב — במיוחד אתרוג ורעננות העלים.
• מתי: ביום; רבים עושים לפני שחרית בבית או בבית הכנסת. לא בשבת.
• $daysNote

אם חסר מין או שאינו כשר — שאלו את הרב; יש תחליפים מוגבלים במקרים לחוצים."""

ARBA_MEN = ARBA_COMMON + """

גברים — חובה מהתורה:
• היום הראשון של סוכות בכל העולם (ט״ו בתשרי) הוא יום החובה מהתורה לגברים. בחו״ל המצווה נמשכת מדרבנן ביום השני של יום טוב ובחול המועד.
• ברכה: גברים אומרים «על נטילת לולב» בכל יום (מלבד שבת). שהחיינו — רק ביום הראשון.
• $menWave

כלל הבעלות (לכם — ולקחתם לכם):
• בארץ ישראל: דרישת הבעלות המלאה על הסט חלה ביום הראשון בלבד (שו״ע או״ח תרנ״ח:ג). אם אין סט בבעלות — בקשו «מתנה על מנת להחזיר» לפני הנענוע.
• בחו״ל: בגלל ספק דאורייתא ביום שני — אותה דרישת לכם ביום 1 ויום 2; לא מספיק לשאול סט מבית הכנסת בלי מתנה.
• בחו״ל מיום שלישי (חול המועד הראשון): מותר לשאול סט משותף בלי מתנה רשמית.
• בארץ ישראל בחול המועד: מותר לשאול בלי מתנה — הבעלות נדרשה רק ביום הראשון."""

ARBA_WOMEN = ARBA_COMMON + """

נשים — מצווה מומלצת (לא חובה):
• נשים פטורות ממצווה זו הקשורה לזמן, אך השתתפות נפוצה מאוד באשכנז ובספרד. הרשימה מסמנת מומלץ — לא חובה יומית כמו לגברים ביום הראשון.

האם נשים צריכות סט משלהן?
• לא. רוב הנשים משתמשות בסט משפחתי משותף או בסט בית הכנסת.

כלל הבעלות (לגברים):
• בארץ ישראל: ביום הראשון בלבד גבר חייב בבעלות (מתנה על מנת להחזיר במידת הצורך). בחו״ל — לכם ביום 1 ו-2; מיום חול המועד מותר לשאול.

ברכה:
• $brachaLine

איך לנענע:
• $waveLine
• אתרוג בשמאל, לולב (עם הדסים וערבות) בימין — או יחד לפי הסידור.

בכל יום של סוכות (מלבד שבת) אפשר לקיים שוב מצווה מומלצת זו."""

WEEK_SHAVUOT = """השבוע שלפני שבועות מיועד להכנה מעשית — שבועות חוגג את מתן תורה בסיני.

אוכל ובית:
• חלב — מנהג אהוב (עוגות, בלינצ'ס, פסטה, גלידה); לתכנן ולקנות מוקדם. ארוחת בשר חגיגית עם יין נשארת מנהג עיקרי לשמחת יום טוב (שו״ע או״ח תקכ״ט:ב); משפחות רבות — קידוש חלבי ואחר כך ארוחת בשר.
• קישוט בפרחים וירוק (מנהג גן סביב סיני).
• יין, מיץ ענבים ומצרכי יום טוב${if (!profile.isInIsrael) " — בחו״ל, הכנה לשני ימי יום טוב" else ""}.

תורה ותפילה:
• לוודא זמני מעריב, שחרית ומוסף; לימוד ליל שבועות (תיקון ליל שבועות) — למצוא תוכנית או לימוד ביתי.
• לבחור טקסטים: רות (נקרא בשבועות), מגילת רות, פרקי אבות או נושא משפחתי.
• לעיין במנהגי אקדמות / יזכור ביום השני (חו״ל).

שמחה ומשפחה:
• ארוחות בשר חגיגיות עם יין — לצד מנהג החלב.
• מתנות לאישה ולילדים לפי יכולת — לשייך את היום לשמחה.

מעשי:
• ערב שבועות: הכנות ביום עד צאת הכוכבים; בישול ללילה אפשרי בחג מאש קיימת אחרי tzeit.
• לכבות מכשירים לפני יום טוב — האפליקציה להכנה, לא לשימוש בחג."""


def is_bad_he(text: str) -> bool:
    if not text or len(text) < 20:
        return False
    stripped = re.sub(r"\$\{[^}]*\}", "", text)
    if re.search(r"[\u0590-\u05ff]\$", stripped) or re.search(r"\$[a-zA-Z]+[\u0590-\u05ff]", stripped):
        return True
    if re.search(r"[\u0590-\u05ff][A-Za-z]{3,}", stripped):
        return True
    bad_markers = (
        "מינצ'ה", "Savereva", "Chofetz Life", "צ'רצ", "צנצ'וט", "Orchos Life",
        "avak lashon hara", "תחנun", "שacharit", "ק א ַת", "ראש העיר צ'וש",
        "האימידה", "The עמידה", "The Femoneh",
    )
    return any(marker in stripped for marker in bad_markers)


def load_pooled_good_he() -> dict[str, str]:
    pooled: dict[str, str] = {}
    sources = sorted(HUMAN.glob("he_fix_*.json"))
    sources += [
        HUMAN / "educational_he_overrides.json",
        HUMAN / "he_fix_012.json",
        HUMAN / "glossary_polish.json",
    ]
    for path in sources:
        if not path.is_file():
            continue
        for k, v in json.loads(path.read_text(encoding="utf-8")).get("he", {}).items():
            if v and not is_bad_he(v):
                pooled[k] = v
    return pooled


def load_seasonal_keys() -> dict[str, str]:
    """Week-before explainers live in quality_overrides / he_fix_012, not always in strings.json."""
    out: dict[str, str] = {}
    for path in (HUMAN / "he_fix_012.json", HUMAN / "quality_overrides.json"):
        if not path.is_file():
            continue
        for k, v in json.loads(path.read_text(encoding="utf-8")).get("he", {}).items():
            if k.startswith("The week before Sukkot") or k.startswith("The week before Shavuot"):
                out[k] = v
    return out


def apply_prefix_he(he: dict[str, str], keys: list[str]) -> None:
    """Apply PREFIX_HE — longest prefix wins per catalog key."""
    merged_prefix = {**PREFIX_HE, **PREFIX_HE_BATCH29}
    prefixes = sorted(merged_prefix.items(), key=lambda kv: len(kv[0]), reverse=True)
    for k in keys:
        for prefix, translation in prefixes:
            if k == prefix or k.startswith(prefix):
                he[k] = translation
                break


def main() -> None:
    strings = json.loads(CATALOG.read_text(encoding="utf-8"))["strings"]
    bundle = {}
    if BUNDLE_HE.is_file():
        bundle = json.loads(BUNDLE_HE.read_text(encoding="utf-8")).get("entries", {})

    pooled = load_pooled_good_he()
    he: dict[str, str] = {}

    apply_prefix_he(he, strings)

    for k, v in pooled.items():
        bundle_val = bundle.get(k, "")
        if k not in he and (is_bad_he(bundle_val) or bundle_val == k):
            he[k] = v
        elif is_bad_he(bundle.get(k, he.get(k, ""))):
            he[k] = v

    for k in strings:
        if not k.startswith("Arba Minim (ארבעה מינים) — the Four Species"):
            continue
        if "Women — recommended" in k:
            he[k] = ARBA_WOMEN
        elif "Men — Torah obligation" in k:
            he[k] = ARBA_MEN
        elif "If a species is missing" in k and "Men —" not in k and "Women —" not in k:
            he[k] = ARBA_COMMON

    for k in strings:
        if k.startswith("Read this today (Friday) before Shabbat candles"):
            he[k] = (
                "קראו את זה היום (יום שישי) לפני הדלקת נרות שבת — האפליקציה אינה לשימוש בשבת.\n\n"
                "מחר שבת וערב $tomorrowChag. $tomorrowChag מתחיל מחר בלילה בצאת הכוכבים (מוצאי שבת), לא הערב. "
                "סיימו הכנת יקנה״ז, נרות יום טוב מאש קיימת, יין ואוכל חגיגי לפני סיום השבת."
            )
        elif k.startswith("$holidayName is about to begin"):
            he[k] = (
                "$holidayName עומד להתחיל. סיימו את מה שאתם עושים, כבו את הטלפון והתכוננו בברכה. "
                "שמחת יום טוב! חכמינו מלמדים ששמחת יום טוב היא מצווה בפני עצמה — "
                "אולי המצוות שלכם מביאות ברכה לכם ולביתכם."
            )
        elif k.startswith("$translatedSwitchedTo $languageName"):
            he[k] = "התרגום הוחלף ל-$languageName.\n$translatedPleaseNote"

    he["{label} is available from dawn ({startTime}). Ideal at sunrise ({sunriseTime}) or later."] = (
        "{label} זמין משחר ({startTime}). מומלץ בזריחה ({sunriseTime}) או אחר כך."
    )
    he["{label} is available from dawn ({startTime}). Ideal from sunrise or later."] = (
        "{label} זמין משחר ({startTime}). מומלץ מזריחה ואילך."
    )

    seasonal = load_seasonal_keys()
    for k in seasonal:
        if k.startswith("The week before Sukkot"):
            he[k] = seasonal[k] if not is_bad_he(seasonal[k]) else pooled.get(k, seasonal[k])
        elif k.startswith("The week before Shavuot"):
            he[k] = WEEK_SHAVUOT

    fix021 = HUMAN / "he_fix_021.json"
    hybrid = HUMAN / "he_hybrid_purge.json"
    for path in (fix021, hybrid):
        if not path.is_file():
            continue
        for k, v in json.loads(path.read_text(encoding="utf-8")).get("he", {}).items():
            if not v:
                continue
            if path == hybrid or not is_bad_he(v):
                he[k] = v

    for k in list(he.keys()):
        if is_bad_he(he[k]):
            print(f"WARN still bad: {k[:60]}")

    out = {"he": he, "es": dict(MANUAL_ES), "fr": dict(MANUAL_FR), "ru": dict(MANUAL_RU)}
    OUT.write_text(json.dumps(out, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"wrote {OUT} — he:{len(he)} es:{len(out['es'])} fr:{len(out['fr'])} ru:{len(out['ru'])}")


if __name__ == "__main__":
    main()
