# -*- coding: utf-8 -*-
"""Polish extracted legacy translations and write local_003/004 *_only.json files."""
from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1] / "data" / "translation-catalog"
HUMAN = ROOT / "human"


def load_batch(batch: str) -> dict[str, list[str]]:
    return json.loads((ROOT / f"_extracted_{batch}.json").read_text(encoding="utf-8"))


def apply_replacements(text: str, pairs: list[tuple[str, str]]) -> str:
    for old, new in pairs:
        text = text.replace(old, new)
    return text


# Full index overrides for empty or heavily corrupted strings
FULL: dict[tuple[str, str, int], str] = {}

def init_full() -> None:
    global FULL
    FULL.update({
        ("003", "he", 6): (
            'גלו את האות המיסטית ה\' (ה)! ✡ האות העוצמתית הזו מייצגת את נוכחות ה\' בעולמנו! '
            'צורתה מלמדת משהו מדהים: יש לה פתח בתחתית — מראה שגם אם נופלים, תמיד אפשר לחזור לה\'. '
            'ידעתם? האות ה\' מופיעה פעמיים בשם הקדוש של ה\' — מלמדת שה\' נוכח גם בעולם הרוחני וגם בגשמי. '
            'קchu רגע להבחין בנוכחות ה\' בחייכם היום.'
        ),
        ("003", "he", 14): (
            'חוו את כוח המזוזה! 🏠 הרמב"ם מסביר שבכל פעם שעוברים ליד מזוזה, היא מזכירה את נוכחות ה\' '
            'ומעוררת משינה רוחנית! למילה «מזוזה» גימטריה 65 — כמו «היכל», חדר או ארמון — '
            'ומראה שהמזוזה הופכת בית ל«ארמון» קטן למקום שכינה. בנוסף, טקסט המזוזה מכיל 713 אותיות — '
            'גימטריה של «תשובה».'
        ),
        ("004", "he", 11): (
            'למדו את הלכות לשון הרע מהחפץ חיים. — הוגש על ידי משה. חכמינו משווים דיבור מזיק '
            'לעבירות החמורות ביותר — ומלמדים שרכילות הרגלית יכולה להרוס מערכות יחסים, קהילות '
            'והמצב הרוחני של האדם. החפץ חיים посвятил חייו להראות כמה התורה מזהירה על דיבור שוב '
            'ושוב, כי מילים יכולות לבנות עולמות או לקרוע אותם. אפילו יום אחד של שמירה על הלשון '
            'יכול לשנות את הדרך שבה אתם רואים אחרים — ואיך הם רואים אתכם.'
        ),
    })


COMMON_ES = [
    ("Di-s", "D-s"), ("D-os", "D-s"), ("G-D", "D-s"), ("G es", "D-s"),
    ("te partirán", "se te contagiarán"),
    ("¡Suéltame!", "¡Suelta rencores!"),
    ("Maestro el", "Domina el"),
    ("Mostrar más", "¡Muestra más"),
    ("Abrazar el", "¡Abraza el"),
    ("¡Honor Torá", "¡Honra a los maestros de Torá"),
    ("¡Ponganse", "¡Corónate"),
    ("pergaminos TWO", "DOS pergaminos de"),
    ("713 cartas", "713 letras"),
    ("Tetsay", "Tet (ט)"),
    ("(יייייייייייייייייייייריא)", "(דיבור) tiene el mismo valor numérico que la palabra para abeja (דבורה)"),
    ("(su texto)", "(שנאה)"), ("(sujeto)", "(שינה)"),
    ("Zayin ()", "Zayin (ז)"), ("Vav ()", "Vav (ו)"),
    ("Kaf (aquí)", "Kaf (כ)"), ("(correcto)", "(ך)"),
]

COMMON_FR = [
    ("Leurs bonnes qualités vont vous déprimer", "Leurs bonnes qualités finiront par vous inspirer"),
    ("Double ton amour", "Doublez votre amour"),
    ("Maître l'art", "Maîtrisez l'art"),
    ("Montrez des", "Montrez un"),
    ("Lève-toi", "Élevez-vous"),
    ("Lâchez les", "Lâchez vos"),
    ("Embrassez le", "Embrassez le"),
    ("Honorez les professeurs de Torah ! C'est-à-dire", "Honorez les professeurs de Torá ! 👨‍🏫"),
    ("Couronne-toi", "Couronnez-vous"),
    ("Rassemblez-vous", "Rassemblez-vous"),
    ("Zayin (-)", "Zayin (ז)"), ("Tet (-)", "Tet (ט)"), ("Kaf !\n", "Kaf (כ) ! 👑 "),
    ("T0.", "D."), ("Kaf (-)", "Kaf final (ך)"),
    ("Marchez dans les voies de D.!Le", "Marchez dans les voies de D. ! 👣 Le"),
]

COMMON_RU = [
    ("свалятся на вас", "передадутся вам"),
    ("Откройте письмо", "Откройте букву"),
    ("( the)", "(ד)"), ("(ה)", "(ה)"), ("(ו)", "(ו)"),
    ("(.)", "(כ)"), ("Т0.", "Б-га."),
    (" the️", " 🛡️"), ("Сделайте ваши", "Пусть ваши слова будут"),
    ("Он добрый", "Это добро"),
]

HE003 = [
    ("ללכת בדרכיו של הקב\"ה, הרמב\"ם מגלה ב\"הילכוט דהו\": בדיוק כפי שהקב\"ה הוא חסד, אתה צריך להיות רחום. בדיוק כפי שהוא אדיב, אתה צריך להיות אדיב. בדיוק כפי שהוא קדוש, אתה צריך להיות קדוש.",
     "לכו בדרכי ה'! 👣 הרמב\"ם מגלה בהלכות דעות: בדיוק כפי שה' הוא רחום — היו רחum. בדיוק כפי שהוא חנון — היו חנון. בדיוק כפי שהוא קדוש — היו קדושים."),
]

# Fix the typo I keep making - use replace after
HE_POST = [("רחum", "רחום"), ("קchu", "קחו")]


def polish_strings(batch: str, lang: str, strings: list[str]) -> list[str]:
    out = list(strings)
    for i, s in enumerate(out):
        key = (batch, lang, i)
        if key in FULL:
            out[i] = FULL[key]
    if lang == "es":
        out = [apply_replacements(s, COMMON_ES) for s in out]
    elif lang == "fr":
        out = [apply_replacements(s, COMMON_FR) for s in out]
    elif lang == "ru":
        out = [apply_replacements(s, COMMON_RU) for s in out]
    elif lang == "he" and batch == "003":
        out = [apply_replacements(s, HE003) for s in out]
        out = [apply_replacements(s, HE_POST) for s in out]
    return out


def main() -> None:
    init_full()
    for batch in ("003", "004"):
        data = load_batch(batch)
        for lang in ("he", "es", "fr", "ru"):
            strings = polish_strings(batch, lang, data[lang])
            if any(not s.strip() for s in strings):
                bad = [i for i, s in enumerate(strings) if not s.strip()]
                raise SystemExit(f"{batch}/{lang} empty at {bad}")
            path = HUMAN / f"local_{batch}_{lang}_only.json"
            path.write_text(json.dumps({lang: strings}, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
            print(f"wrote {path.name} ({len(strings)} strings)")


if __name__ == "__main__":
    main()
