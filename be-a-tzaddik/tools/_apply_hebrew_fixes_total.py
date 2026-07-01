#!/usr/bin/env python3
"""Apply hebrew_fixes.json + bare-key aliases + known corruption overrides into he_glue_fixes layer."""

from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CATALOG = ROOT / "data" / "translation-catalog" / "strings.json"
SHARDS = ROOT / "data" / "translation-catalog" / "shards" / "hebrew_fixes.json"
HUMAN = ROOT / "data" / "translation-catalog" / "human"
OUT = HUMAN / "he_fix_021.json"

# Keys where ui_medium_* introduced hybrid garbage — authoritative Hebrew replacements.
MANUAL_HE: dict[str, str] = {
    "intermediate days of Pesach between the festival days": "ימי חול המועד של פסח בין ימי החג",
    "synagogue officer who coordinates aliyot and services": "גבאי בית כנסת שמתאם עליות ותפילות",
    "Chol HaMoed Pesach — intermediate days of Pesach between the festival days": (
        "חול המועד פסח — ימי הביניים של פסח בין ימי החג"
    ),
    "Netilat yadayim is ritual handwashing with a cup (keli). Morning washing removes ruach ra'ah after sleep: pour water three times on each hand in alternating sequence (right, left, right, left, right, left). Before bread, pour two or three times consecutively on each hand to purify for eating.": (
        "נטילת ידיים היא שטיפת ידיים טקסית בכוס (כלי). נטילת בוקר מסירה רוח רעה לאחר השינה: "
        "שופכים מים שלוש פעמים על כל יד לסירוגין (ימין, שמאל, ימין, שמאל, ימין, שמאל). "
        "לפני לחם — שתיים או שלוש פעימות רצופות על כל יד."
    ),
    "netilat yadayim — Netilat yadayim is ritual handwashing with a cup (keli). Morning washing removes ruach ra'ah after sleep: pour water three times on each hand in alternating sequence (right, left, right, left, right, left). Before bread, pour two or three times consecutively on each hand to purify for eating.": (
        "נטילת ידיים — שטיפה טקסית בכוס; בבוקר לסירוגין (ימין, שמאל) שלוש פעמים; לפני לחם שתיים או שלוש פעימות רצופות."
    ),
    "Amen affirms a bracha someone else recited, \"so may it be.\" The Talmud praises one who answers Amen even more than the one who made the blessing. It should not be rushed or mumbled; it is a small but constant mitzvah opportunity in synagogue and at home. (Literally, it's an acronym for 'G-d, King Who is faithful' — Keil Melech Ne'eman).": (
        "אמן מאשר ברכה שאחר אמר — «כן יהי רצון». התלמוד משבח מי שעונה אמן יותר ממברך. "
        "לא למהר ולא ללחש; מצווה קטנה וקבועה בבית כנסת ובבית. "
        "(ראשי תיבות: ק-ל מלך נאמן.)"
    ),
    "Amen — Amen affirms a bracha someone else recited, \"so may it be.\" The Talmud praises one who answers Amen even more than the one who made the blessing. It should not be rushed or mumbled; it is a small but constant mitzvah opportunity in synagogue and at home. (Literally, it's an acronym for 'G-d, King Who is faithful' — Keil Melech Ne'eman).": (
        "אמן — מאשר ברכה שאחר אמר; התלמוד משבח עונה אמן; ראשי תיבות ק-ל מלך נאמן."
    ),
    "A shaliach tzibur is the prayer leader who represents the congregation before G-d — reciting the repetition of the Amidah and guiding pacing. He must be someone the community accepts and who knows the laws of prayer. Women and men have different roles per community in who may lead which parts.": (
        "שליח ציבור מנהיג התפילה ומייצג את הציבור לפני ה' — חוזר על העמידה וקובע קצב. "
        "חייב להיות מקובל בקהילה ויודע הלכות תפילה. תפקידי נשים וגברים משתנים לפי הקהילה."
    ),
    "Bar mitzvah is when a boy turns thirteen and becomes obligated in mitzvot — tefillin, fasting, full moral responsibility. The celebration marks a Jewish son's entry into adult religious life.": (
        "בר מצווה — כשבן מגיע לשלוש עשרה ומתחייב במצוות: תפילין, צום ואחריות דתית מלאה. "
        "החגיגה מסמנת כניסה לחיי מצוות של בוגר."
    ),
    "Bedieved describes halachic guidance after the ideal was missed — unintentionally and without compromise. It is not permission to plan poorly; it is how to recover. Example: if you missed the ideal time for a mitzvah, ask your rav what to do bedieved.": (
        "בדיעבד — הנחיה הלכתית לאחר שהאידיאל הוחמץ, שלא בכוונה וללא פשרות. "
        "אין זו רשות לתכנן גרוע; זו דרך להתאושש. שאלו את הרב מה לעשות בדיעבד."
    ),
    "Borer (selecting) forbids sorting a mixed pile on Shabbat by removing unwanted from wanted unless three conditions are met: take the good from the bad, by hand (not a dedicated strainer), for immediate use. Example: picking bones from your plate right before eating is OK; sorting a salad bowl for later is not.": (
        "בורר (בחירה) אוסר למיין ערימה מעורבת בשבת על ידי הפרדת לא רצוי מרצוי, "
        "אלא אם מתקיימים שלושה תנאים: לוקחים את הטוב מתוך הרע, ביד (לא במסננת ייעודית), לשימוש מיידי. "
        "דוגמה: לבחור עצמות מהצלחת ממש לפני האכילה — מותר; למיין קערת סלט למחר — אסור."
    ),
    "Borer — Borer (selecting) forbids sorting a mixed pile on Shabbat by removing unwanted from wanted unless three conditions are met: take the good from the bad, by hand (not a dedicated strainer), for immediate use. Example: picking bones from your plate right before eating is OK; sorting a salad bowl for later is not.": (
        "בורר — אוסר בחירה בשבת אלא לפי תנאי בורר: טוב מתוך רע, ביד, לשימוש מיידי."
    ),
    "Full Hallel — complete Hallel psalms on festivals/Chanukah": "הלל מלא — מזמורי הלל השלמים בחגים ובחנוכה",
    "Musaf (only on Rosh Chodesh, Festivals, and Shabbat)": "מוסף (רק בראש חודש, חגים ושבת)",
    "Put on Tefillin during morning prayers (except Shabbat/Festivals)": "הנחת תפילין בתפילת שחרית (מלבד שבת וחגים)",
}

HYBRID_TERM_FIXES: list[tuple[re.Pattern[str], str]] = [
    (re.compile(r"פesach", re.I), "פסח"),
    (re.compile(r"פEsach", re.I), "פסח"),
    (re.compile(r"גabbaי", re.I), "גבאי"),
    (re.compile(r"גabba", re.I), "גבאי"),
    (re.compile(r"אשכנaz", re.I), "אשכנז"),
    (re.compile(r"שacharit", re.I), "שחרית"),
    (re.compile(r"Erev פesach", re.I), "ערב פסח"),
    (re.compile(r"Eturn פesach", re.I), "ערב פסח"),
    (re.compile(r"Ereturn פesach", re.I), "ערב פסח"),
    (re.compile(r"ימין, ימין, ימין, ימין, ימין, ימין"), "ימין, שמאל, ימין, שמאל, ימין, שמאל"),
    (re.compile(r"ימין, ימין, ימין, ימין, ימין"), "ימין, שמאל, ימין, שמאל, ימין"),
]


def strip_hybrid(text: str) -> str:
    out = text
    for pat, repl in HYBRID_TERM_FIXES:
        out = pat.sub(repl, out)
    return out


def bare_aliases(fixes: dict[str, str], strings: list[str]) -> dict[str, str]:
    """Map bare catalog keys from 'term — definition' hebrew_fixes entries."""
    out: dict[str, str] = {}
    string_set = set(strings)
    for key, val in fixes.items():
        if " — " not in key:
            continue
        term, rest = key.split(" — ", 1)
        term = term.strip()
        rest = rest.strip()
        val_body = val.split(" — ", 1)[-1].strip() if " — " in val else val.strip()
        val_term = val.split(" — ", 1)[0].strip() if " — " in val else term
        if rest in string_set and rest not in fixes:
            out[rest] = val_body
        if term in string_set and term not in fixes:
            out[term] = val_term
    return out


def main() -> None:
    strings = json.loads(CATALOG.read_text(encoding="utf-8"))["strings"]
    fixes = json.loads(SHARDS.read_text(encoding="utf-8")).get("he", {})
    he: dict[str, str] = {}

    for k, v in fixes.items():
        he[k] = strip_hybrid(v)

    he.update(bare_aliases(fixes, strings))

    for k, v in MANUAL_HE.items():
        he[k] = strip_hybrid(v)

    for k in list(he.keys()):
        he[k] = strip_hybrid(he[k])

    OUT.write_text(json.dumps({"he": he}, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"wrote {OUT} — {len(he)} Hebrew overrides from hebrew_fixes + manual")


if __name__ == "__main__":
    main()
