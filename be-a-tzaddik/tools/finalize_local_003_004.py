"""Convert extracted legacy translations to *_only.json and apply polish fixes."""
from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1] / "data" / "translation-catalog"
HUMAN = ROOT / "human"


def load_extracted(batch: str) -> dict[str, list[str]]:
    return json.loads((ROOT / f"_extracted_{batch}.json").read_text(encoding="utf-8"))


# Index-based full replacements: (batch, lang) -> {index: text}
REPLACEMENTS: dict[tuple[str, str], dict[int, str]] = {
    ("003", "he"): {
        0: 'לכו בדרכי ה\'! 👣 הרמב"ם מגלה בהלכות דעות: בדיוק כפי שה\' הוא רחום, כך עליכם להיות רחום. בדיוק כפי שהוא חנון — היו חנון. בדיוק כפי שהוא קדוש — היו קדושים. הנה המפתח: מצאו את דרך האמצע בכל תכונותיכם — לא קשים מדי, לא רכים מדי. משימת היום: בחרו תכונה אלוקית אחת (חסד, קדושה או רחמים) והתמקדו באמת לפתח אותה!',
        1: 'בחרו את חבריכם בחוכמה! 🤝 הרמב"ם מלמד שיעור חיים חשוב: אדם מושפע באופן טבעי מההתנהגות ומהדעות של חבריו. לכן מצווה להתחבר עם צדיקים וללמוד מדרכיהם! הנה האתגר שלכם: בלו היום או שוחחו עם מישהו שמעורר בכם להשתפר. התכונות הטובות שלהם יחדור אליכם!',
        2: 'הכפילו את האהבה! ❤️ הרמב"ם מסביר שתי מצוות יפות: אהבת ישראל ואהבה מיוחדת לגרים. למה אהבה נוספת לגרים? הם בחרו להצטרף לעמנו ולקיים את התורה — זה מדהים! הנה משהו מעשי: הראו היום מעשה אהבה נוסף ליהודי אחר — בין אם נולד יהודי ובין אם בחר להצטרף לעמנו!',
        3: 'גלו את האות דלת (ד)! 🚪 צורת האות הזו מלמדת על ענווה — היא כפופה, כמו אדם צנוע. שמה פירושו «דלת» בעברית, ומזכיר לנו שענווה היא הדלת לכל צמיחה רוחנית. ידעתם? גימטריית הדלת היא 4, הקשורה לארבע הכיוונים — מראה כיצד אדם עניו פתוח ללמוד מכל אחד! קחu רגע לתרגל ענווה היום על ידי למידת משהו חדש מאדם אחר.',
    },
}


def polish(text: str, lang: str) -> str:
    if lang == "es":
        text = text.replace("Di-s", "D-s").replace("D-os", "D-s").replace("G-D", "D-s")
        text = text.replace("te partirán", "se te contagiarán")
        text = text.replace("¡Suéltame!", "¡שחררu grudges!" )  # fix below
    return text


def main() -> None:
    for batch in ("003", "004"):
        data = load_extracted(batch)
        for lang in ("he", "es", "fr", "ru"):
            strings = list(data[lang])
            reps = REPLACEMENTS.get((batch, lang), {})
            for i, rep in reps.items():
                strings[i] = rep
            strings = [polish(s, lang) if s else s for s in strings]
            if any(not s for s in strings):
                missing = [i for i, s in enumerate(strings) if not s]
                raise ValueError(f"{batch}/{lang} missing indices: {missing}")
            path = HUMAN / f"local_{batch}_{lang}_only.json"
            path.write_text(
                json.dumps({lang: strings}, ensure_ascii=False, indent=2) + "\n",
                encoding="utf-8",
            )
            print(path.name)

if __name__ == "__main__":
    main()
