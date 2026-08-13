# -*- coding: utf-8 -*-
import importlib.util
import json
from pathlib import Path

spec = importlib.util.spec_from_file_location(
    "w", Path(__file__).parent / "write_local_003_translations.py"
)
mod = importlib.util.module_from_spec(spec)
spec.loader.exec_module(mod)

HE, ES, FR, RU = mod.HE, mod.ES, mod.FR, mod.RU
OUT = mod.OUT

# Override broken HE entries from source array
HE[14] = (
    'חוו את כוח המזוזה! 🏠 הרמב"ם מסביר: בכל מעבר ליד מזוזה, היא מזכירה את נוכחות ה\' '
    'ומעירה אותנו משינה רוחנית! המילה "מזוזה" שווה 65 בגימטריה — כמו "היכל", חדר/ארמון. '
    'הקשר מרמז שהמזוזה הופכת בית למקדש מיני — מקום לשכינה. בנוסף, טקסט המזוזה מכיל 713 '
    'אותיות — גימטריית "תשובה".'
)
HE[15] = (
    'למדו על מצוות כתיבת ספר תורה! ✍️ לכל גבר יהודי יש מצווה לכתוב (או לפי דעות, '
    'לשתף בכתיבת) ספר תורה! יש אומרים שאפשר לקיים זאת היום בקניית ספרים כמו חומש, '
    'תנ"ך וספרים קדושים אחרים. כל אות בתורה מתאימה לנשמה יהודית — אות אחת חסרה '
    'פוסלת את כל הס"ת, ומראה כמה כל יehudi חשוב!'
)
HE[16] = (
    'עטרו את עצמכם בתורה! 👑 הרמב"ם מלמד: מלך יehudi חייב לכתוב שני ספרי תורה — '
    'אחד לאוצר ואחד שלא יעזוב אותו! הלקח: לא משנה כמה אתם עסוקים או חזקים — '
    'התורה צריכה להיות בן זוג קבוע! ידעתם? המלך קרא מס"תו בכל יום — מלמד לקבוע '
    'זמן לימוד יומי! המשימה שלכם: קבעו זמן קבוע ללימוד, גם אם רק כמה דקות.'
)
HE[17] = (
    'התאספו להשראה! 👥 למדו על מצוות ההקהל המיוחדת! כל שבע שנים התאסף כל העם — '
    'גברים, נשים וילדים — לשמוע את המלך קורא בתורה! משהו מדהים: הביאו גם תינוקות, '
    'וההורים זכו בשכר מיוחד! הלקח? לפעמים רק הנוכחות בקהילה קדושה מעלה את הנשמה! '
    'המשימה של היום: הצטרפו לשיעור תורה, גם אם באינטרנט.'
)
HE[24] = (
    'חשפו את מסתורי הכ"ף (כ)! 👑 במיסטיקה יehudit, הכ"ף מייצגת כתר — לא כתר של גאווה! '
    'ידעתם? צורת הכ"ף הכפופה (כ) מייצגת כריעה והכנעה, וגימטרייתה 20 — גיל שבו מתחיל '
    'מרדף החכמה. הכ"ף הסופית (ך) מלמדת שהישג אמיתי בא דרך ענווה.'
)

for i, s in enumerate(HE):
    HE[i] = s.replace("yehudi", "יהודי").replace("yehudit", "יהודית")

FR[23] = FR[23].replace("yet c'est", "c'est")
RU[12] = RU[12].replace("не talmid chacham", "не является talmid chacham")
RU[18] = RU[18].replace("Шabbat", "Shabbat").replace("intent", "намерением")
RU[19] = RU[19].replace("idol worship", "идолопоклонства")

for lang, strings in [("he", HE), ("es", ES), ("fr", FR), ("ru", RU)]:
    assert len(strings) == 25
    path = OUT / f"local_003_{lang}_only.json"
    with path.open("w", encoding="utf-8") as f:
        json.dump({lang: strings}, f, ensure_ascii=False, indent=2)
        f.write("\n")
    print("OK", path.name)
