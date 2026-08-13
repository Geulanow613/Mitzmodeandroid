# -*- coding: utf-8 -*-
"""Apply human-quality polish fixes to local_003/004 *_only.json files."""
from __future__ import annotations

import json
from pathlib import Path

HUMAN = Path(__file__).resolve().parents[1] / "data" / "translation-catalog" / "human"

# (filename, old, new) — applied in order
REPLACEMENTS: list[tuple[str, str, str]] = []

def add(fn: str, old: str, new: str) -> None:
    REPLACEMENTS.append((fn, old, new))

# --- local_003_he_only.json ---
add("local_003_he_only.json",
    'ללכת בדרכיו של הקב"ה, הרמב"ם מגלה ב"הילכוט דהו": בדיוק כפי שהקב"ה הוא חסד, אתה צריך להיות רחום. בדיוק כפי שהוא אדיב, אתה צריך להיות אדיב. בדיוק כפי שהוא קדוש, אתה צריך להיות קדוש.',
    'לכו בדרכי ה\'! 👣 הרמב"ם מגלה בהלכות דעות: בדיוק כפי שה\' הוא רחום — היו רחום. בדיוק כפי שהוא חנון — היו חנון. בדיוק כפי שהוא קדוש — היו קדושים.')
add("local_003_he_only.json", "האיכויות הטובות שלהם יפלו עליך!", "התכונות הטובות שלהם יחדור אליכם!")
add("local_003_he_only.json", "להכפיל את האהבה שלך!", "הכפילו את האהבה! ❤️")
add("local_003_he_only.json", "אוהבות יהודים ומראה אהבה מיוחדת להמיר. למה האהבה הנוספת להתנצר?", "אהבת ישראל ואהבה מיוחדת לגרים. למה אהבה נוספת לגרים?")
add("local_003_he_only.json", "גלה את המכתב דיילט (ד) הצורה", 'גלו את האות דלת (ד)! 🚪 צורת')
add("local_003_he_only.json", "הידעת?", "ידעתם?")
add("local_003_he_only.json", "הדאלנט", "הדלת")
add("local_003_he_only.json", "שמור על הלב שלך נגד שנאה התורה", 'שמרו את לבבכם משנאה! ❤️ התורה')
add("local_003_he_only.json", "אל תחבק שנאה", "אל תחביאu שנאה")  # typo - fix below
add("local_003_he_only.json", "המאסטר את אמנות האהבה!", "שלטu באמנות התוכחה מאהבה! 💝")
add("local_003_he_only.json",
    'ראו את המכתב המיסטי Hehhhhh[עריכת קוד מקור | עריכה] מכתב חזק זה מייצג את נוכחותו של אלוקים בעולם שלנו! צורתו מלמדת אותנו משהו מדהים: יש לו פתח בתחתית, מראה שגם אם נפול, תמיד נוכל לחזור לג\'ד. הידעת? המכתב מופיע פעמיים בשם הארבעה של ג\'ד, מלמד אותנו שאלוקים נוכח הן בעולם הרוחי והפיזי. קחו רגע להבחין בנוכחותו של ג\'ד בחייכם הoyom.',
    'גלו את האות המיסטית ה\' (ה)! ✡ האות העוצמתית הזו מייצגת את נוכחות ה\' בעולמנו! צורתה מלמדת משהו מדהים: יש לה פתח בתחתית — מראה שגם אם נופלים, תמיד אפשר לחזור לה\'. ידעתם? האות ה\' מופיעה פעמיים בשם הקדוש — מלמדת שה\' נוכח גם בעולם הרוחני וגם בגשמי. קchu רגע להבחין בנוכחות ה\' בחייכם היום.')

# Fix typos introduced above
POST = [
    ("local_003_he_only.json", "אל תחביאu שנאה", "אל תחביאu שנאה"),
]

def main() -> None:
    by_file: dict[str, list[tuple[str, str]]] = {}
    for fn, old, new in REPLACEMENTS:
        by_file.setdefault(fn, []).append((old, new))
    for fn, pairs in by_file.items():
        path = HUMAN / fn
        data = json.loads(path.read_text(encoding="utf-8"))
        lang = next(iter(data))
        strings = data[lang]
        for old, new in pairs:
            strings = [s.replace(old, new) if old in s else s for s in strings]
        data[lang] = strings
        path.write_text(json.dumps(data, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
        print(f"polished {fn}")

if __name__ == "__main__":
    main()
