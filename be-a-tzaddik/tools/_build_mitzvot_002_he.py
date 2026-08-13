# -*- coding: utf-8 -*-
import json
import re
from pathlib import Path

ROOT = Path(r"c:\apps\hehehe\be-a-tzaddik")
OUT = ROOT / "data/translation-catalog/human/mitzvot_002_he_only.json"
SRC = ROOT / "tools/_write_mitzvot_002_he.py"
TAIL_JSON = ROOT / "tools/_tail_he.json"

text = SRC.read_text(encoding="utf-8")
start = text.index("he = [")
end = text.index("\n]\n\nprint")
he = eval(text[start:end + 2].split("=", 1)[1].strip())[:18]

e19 = json.loads(TAIL_JSON.read_text(encoding="utf-8"))[0]

e20 = (
    "היו שופטים לזכות: למדו על דן לכף זכות (שיפוט לכף זכות)! ⚖️ "
    "התורה מצווה לשpט את רעcha בצedek (ויקרא י\"ט:ט\"ו). "
    "הרמב\"ם קובע שזו חובה חיובית, לא רק רעיון נחמד! "
    "הנוסחה הקלאסית מהתלמוד (שבת קכ\"ז ע\"ב) היא: 'עם כל אדם, הטה את הכף לזכות.' "
    "פרקי אבות (ב:ד) מוסיפים: 'אל תשpט את רעcha עד שתגיע למקומo.' "
    "איך זה נראה בפועל? אם אדם שאתם בדרך כלל יודעים שהוא צadik עושה משהu שנראה חשud, "
    "הניחu שחסר לכם הקשר. אם מישהu באופi ממוצע עושה משהu שאפשר לפרש בשתי דרכים, "
    "תנu לו במפורש את כaf הזכut. רק אם למישהu יש דפוס ברור של עוול, "
    "כדאי להניח את הפרשנות הגרועה. החפץ חיים מרחיב, ומלמד שגם היווצרות *מחשבה* שלילית "
    "על מישהu בלי סיבה מספקת יכולה להיות עבירה! התלמוד מספר סיפור עוצמתי על פועל "
    "שסמך על מעסיקo למרות עיכוב בתשלום, ומדגים איך שיפוט לזכut מביא שכר אלוקי עצום."
)

print('latin in e20', re.findall(r'[A-Za-z]+', e20))
