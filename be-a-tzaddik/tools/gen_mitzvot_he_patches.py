#!/usr/bin/env python3
"""Generate mitzvot_004_he_patches.json — full Hebrew for 10 truncated cloud mitzvot."""
from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
EXPAND = ROOT / "tools" / "_expand_mitzvot.json"
OUT = ROOT / "data" / "translation-catalog" / "human" / "mitzvot_004_he_patches.json"


def main() -> None:
    items = json.loads(EXPAND.read_text(encoding="utf-8"))
    m4 = json.loads((ROOT / "data/translation-catalog/human/mitzvot_004.json").read_text(encoding="utf-8"))
    he_block: dict[str, str] = {}
    for item in items:
        en = item["en"]
        es = m4["es"][en]
        old = m4["he"][en]
        # Expand: keep Hebrew opening through first emoji block, then add body from ES structure
        # translated to Hebrew (human-quality summaries per paragraph)
        he_block[en] = _expand(en, es, old)
    OUT.write_text(json.dumps({"he": he_block}, ensure_ascii=False, indent=2), encoding="utf-8")
    for k, v in he_block.items():
        ratio = len(v) / len(k)
        print(f"{ratio:.0%} ({len(v)} chars): {k[:55]}...")


def _expand(en: str, es: str, old: str) -> str:
    """Route to per-mitzvah expanders by prefix."""
    if en.startswith("Learn about the world's first mandatory RSVP"):
        tr = _korban_pesach()
    elif en.startswith("Learn about the machatzit hashekel"):
        tr = _machatzit()
    elif en.startswith('Learn about the incredible "sliding scale"'):
        tr = _oleh_veyored()
    elif en.startswith("Learn about the Tithe-to-Table"):
        tr = _maaser()
    elif en.startswith("Prepare for the ultimate harvest parade"):
        tr = _bikkurim()
    elif en.startswith("Learn about the mitzvah of Halvayat HaMeit"):
        tr = _halvayat()
    elif en.startswith("Learn about the prohibition of 'chukot ha'akum'"):
        tr = _chukot()
    elif en.startswith("Learn about the Rambam's specific"):
        tr = _mashiach()
    elif en.startswith("Learn the specific conditions under which speaking"):
        tr = _lashon_permitted()
    elif en.startswith("Learn about the prohibition of receiving lashon"):
        tr = _lashon_receive()
    else:
        tr = old
    return _clean_he(tr)


def _clean_he(s: str) -> str:
    """Strip accidental Latin characters from Hebrew strings."""
    fixes = {
        "תalmid חacham": "תalmid חacham",
        "קורban ": "קורban ",
        "קרban ": "קרban ",
        "צara'at": "צara'at",
        "אפילo": "אפילo",
        "tevel": "tevel",
        "גדolah": "גדolah",
        "היסטoria": "היסטoria",
        "זהb": "זהb",
        "נ cherished": "נ cherished",
        "אחdut": "אחdut",
        "לrecipient": "לrecipient",
        "יehudi": "יehudi",
        "למדu": "למדu",
        "תלכu": "תלכu",
        "חוקot": "חוקot",
        "הגoy": "הגoy",
        "שulchan Aruch": "שulchan Aruch",
        "גoyim": "גoyim",
        "יehudit": "יehudit",
        "יemot HaMashiach": "יemot HaMashiach",
        "מלachim": "מלachim",
        "מלכut": "מלכut",
        "דavid": "דavid",
        "נiddchי": "נiddchי",
        "דaתך": "דaתך",
        "מנהagו": "מנהagו",
        "זeב": "זeב",
        "מטaphora": "מטaphora",
        "ימot HaMashiach": "ימot HaMashiach",
        "תזrח": "תזrח",
        "חfree": "חfree",
        "עbדות": "עbדות",
        "הסchah": "הסchah",
        "חofshi": "חofshi",
        "ללimud": "ללimud",
        "נקmמה": "נקmמה",
        "לעודd": "לעודd",
        "השomע": "השomע",
        "תפisה": "תפisה",
        "בpraktika": "בpraktika",
        "נסu": "נסu",
        "להפnות": "להפnות",
        "תalmud": "תalmud",
        "נiddah": "נiddah",
        "nuanced": "מורכבת",
        "toelet": "תועלת",
    }
    # proper unicode replacements
    proper = {
        "תalmid חacham": "\u05ea\u05dc\u05de\u05d9\u05d3 \u05d7\u05db\u05dd",
        "קורban ": "\u05e7\u05d5\u05e8\u05d1\u05df ",
        "קרban ": "\u05e7\u05e8\u05d1\u05df ",
        "צara'at": "\u05e6\u05e8\u05e2\u05ea",
        "אפילo": "\u05d0\u05e4\u05d9\u05dc\u05d5",
        "tevel": "\u05ea\u05d1\u05dc",
        "גדolah": "\u05d2\u05d3\u05d5\u05dc\u05d4",
        "היסטoria": "\u05d4\u05d9\u05e1\u05d8\u05d5\u05e8\u05d9\u05d4",
        "זהb": "\u05d6\u05d4\u05d1",
        "נ cherished": "\u05e0\u05d7\u05de\u05d3\u05d9\u05dd",
        "אחdut": "\u05d0\u05d7\u05d3\u05d5\u05ea",
        "לrecipient": "\u05dc\u05de\u05e7\u05d1\u05dc",
        "יehudi": "\u05d9\u05d4\u05d5\u05d3\u05d9",
        "למדu": "\u05dc\u05de\u05d3\u05d5",
        "תלכu": "\u05ea\u05dc\u05db\u05d5",
        "חוקot": "\u05d7\u05d5\u05e7\u05d5\u05ea",
        "הגoy": "\u05d2\u05d5\u05d9",
        "שulchan Aruch": "\u05e9\u05d5\u05dc\u05d7\u05df \u05e2\u05e8\u05d5\u05da",
        "גoyim": "\u05d2\u05d5\u05d9\u05d9\u05dd",
        "יehudit": "\u05d9\u05d4\u05d5\u05d3\u05d9\u05ea",
        "יemot HaMashiach": "\u05d9\u05de\u05d5\u05ea \u05d4\u05de\u05e9\u05d9\u05d7",
        "מלachim": "\u05d4\u05dc\u05db\u05d5\u05ea \u05de\u05dc\u05db\u05d9\u05dd",
        "מלכut": "\u05de\u05dc\u05db\u05d5\u05ea",
        "דavid": "\u05d3\u05d5\u05d3",
        "נiddchי": "\u05e0\u05d3\u05d7\u05d9",
        "דaתך": "\u05d3\u05e2\u05ea\u05da",
        "מנהagו": "\u05de\u05e0\u05d4\u05d2\u05d5",
        "זeב": "\u05d6\u05d0\u05d1",
        "מטaphora": "\u05de\u05d8\u05d0\u05e4\u05d5\u05e8\u05d4",
        "ימot HaMashiach": "\u05d9\u05de\u05d5\u05ea \u05d4\u05de\u05e9\u05d9\u05d7",
        "תזrח": "\u05ea\u05d6\u05e8\u05d7",
        "חfree": "\u05d7\u05e4\u05e9\u05d9",
        "עbדות": "\u05e2\u05d1\u05d3\u05d5\u05ea",
        "הסchah": "\u05d4\u05e1\u05d7\u05d4",
        "חofshi": "\u05d7\u05e4\u05e9\u05d9",
        "ללimud": "\u05dc\u05dc\u05d9\u05de\u05d5\u05d3",
        "נקmמה": "\u05e0\u05e7\u05de\u05d4",
        "לעודd": "\u05dc\u05e2\u05d5\u05d3\u05d3",
        "השomע": "\u05d4\u05e9\u05d5\u05de\u05e2",
        "תפisה": "\u05ea\u05e4\u05d9\u05e1\u05d4",
        "בpraktika": "\u05d1\u05e4\u05e8\u05e7\u05d8\u05d9\u05e7\u05d4",
        "נסu": "\u05e0\u05e1\u05d5",
        "להפnות": "\u05dc\u05d4\u05e4\u05e0\u05d5\u05ea",
        "תalmud": "\u05ea\u05dc\u05de\u05d5\u05d3",
        "נiddah": "\u05e0\u05d3\u05d4",
    }
    for bad, good in proper.items():
        s = s.replace(bad, good)
    return s


def _korban_pesach() -> str:
    return (
        "למדו על אירוע העולם הראשון עם אישור הגעה חובה — קורבן הפסח! 🐑 "
        "זו לא היתה רק מצווה, אלא נס לוגיסטי. דמיינו מאות אלפי אנשים שיורדים לירושלים "
        "במטרה אחת: לצלות מספיק בשר להאכיל את כל העם! 🌃 "
        "אבל לא יכולתם פשוט להיכנס ולקנות כמה צלעות טלה. הייתם חייבים להירשם על בהמת "
        "קבוצה מסוימת עוד לפני שקיעת החמה. כל אחד היה חייב להיות חלק מחבורה — "
        "אם לא הייתם ברשימה, לא אכלתם! 📝 "
        "בצהריים של י\"ד בניסן כמעט כל יהודי כשיר במדינה הגיע למקדש בבת אחת. "
        "כדי להתמודד עם שעת השיא הקדושה, הכהנים חילקו את העם לשלוש משמרות ענק. "
        "ברגע שהקבוצה הראשונה מילאה את העזרה, הדלתות הכבדות נסגרו והעבודה החלה! "
        "כאן זה נהיה קולנועי: הכהנים עמדו בשורות מסודרות עד המזבח — שורה עם קערות זהב "
        "ושורה עם כסף. כשהצליחו כבשים או עזים, הדם עבר מיד ליד כמו מרוץ שליחים! "
        "אחרי שהבהמה שלכם נשחטה ודמה נזרק על המזבח, קיבלתם אותה חזרה. "
        "כל קבוצה הלכה למקום לינה, צלתה את הבשר שלם על אש פתוחה וערכה סדר פסח! "
        "ושכחו מסכין — אסור היה לשבור אפילו עצם אחד! 🦴 "
        "הלחץ היה לסיים לפני חצות, בעוד כל העיר שרה הלל. ירושלים היתה מלאה עד הגגות "
        "וריח הצלוי מילא את האוויר — חגיגה עצומה של חירות! "
        "תזכו לחוות זאת בעצמכם במהרה בימינו, עם בניין המקדש! למדו עוד! 🎊🍷"
    )


def _machatzit() -> str:
    return (
        "למדו על מחצית השקל — ועל הקשר המפתיע לסיפור פורים! 🪙 "
        "מדי שנה, החל מראש חודש אדר, הקהילות נזכרו: הגיע הזמן לתת מחצית שקל! "
        "כל יהודי בוגר נתן בדיוק חצי שקל למימון קורבנות הציבור במקדש. "
        "עשיר או עני, תalmid חacham או פועל — אותה כמות, בלי יוצא מן הכלל. "
        "לא שקל שלם, כי אין תרומה שלמה בלי כולם. "
        "הקשר לפורים מרתק: המן הציע לאחשורוש 10,000 ככרי כסף להשמיד את היהודים. "
        "המדרש מלמד: שקליכם קדמו לשקלי המן — זכות דורות של מצווה זו הגנה על עם ישראל. "
        "לכן נתפתחה המנהג לתת שלוש מחציות שקל לפני פורים — נגד כסף המן. "
        "היום אשכנזים וספרדים נוהגים לתת שלוש מחציות (או שווי מקביל)."
    )


def _oleh_veyored() -> str:
    return (
        "למדו על מערכת הסולם הנידח שמבטיחה שאף אחד לא ייגרע מסיכוי לפתיחה חדשה! 🕊️ "
        "קורban עולה ויורד מוכיח שוב שה' דואג לכל יהודי! "
        "לפי מה שהיה בכיס, מחיר הקרban עלה או ירד. 📉📈 "
        "המערכת חלה על רגעים כמו כניסה בטעות למקדש, אכילת קודש בטומאה, "
        "שבועה שנשכחה, שתיקה כשצריך להעיד, ריפוי מצara'at, או קרban יולדת. "
        "עסקים טובים — הבאתם בהמה; קשה — ציפורים; לפעמים אפילo קמח! 🌾 "
        "עשיר או דל — הדרך חזרה לקדושה תמיד פתוחה. לא עלות המתנה אלא הלב! "
        "חשבו איך ה' שונה ממנהיגים — הוא באמת רוצה קשר, לא רק כסף!"
    )


def _maaser() -> str:
    return (
        "למדו על מערכת מהמעשר לשולחן שהופכת כל חטיף ישראלי למשימה קדושה! 🧺 "
        "בארץ ישראל, פירות וירקות הם חלק ממחזור רוחני. לפני נשיכה אחת מפרידים "
        "תרומה, מעשר ראשון, מעשר שני או מעשר עני ממצב tevel. 🏛️ "
        "תחילה תרומה גדolah לכהנים (קדושה עליונה — היום לא נאכל). 🕊️ "
        "אחר כך מעשר ראשון ללויים; מעשר שני בשנים 1,2,4,5 — עשרה אחוז לירושלים; "
        "בשנים 3,6 — מעשר עני לעניים. 🍎🤝 "
        "גם בשוק ישראלי מפרידים סמלית — מס רוחני שמזכיר: הארץ שייכת לה'!"
    )


def _bikkurim() -> str:
    return (
        "הכינו למצעד הקציר הגדול! 🍇 "
        "בכורים — אחת הטקסים המרגשים ביותר במקדש. "
        "כשהפירות הראשונים משבע המינים הבשילו, קשרו קנה לסימון קדושה. 🎀 "
        "המסע לירושלים היה חג ארצי — עולי רגל בקבוצות, אומנים ומנהיגים קמים לקבלם! 🏛️ "
        "רחובות מלאים בחלילי הלויים עד הר הבית. "
        "החקלאי מחזיק את הסל ומספר את ההיסטoria מהיציאת מצרים. 📜 "
        "עשירים — סלי זהb וכסף; עניים — נצרים — כולם נ cherished. "
        "טקס של הכרת הטוב ואחdut — תצפו לחג שוב במקדש השלישי! 🎊🍎"
    )


def _halvayat() -> str:
    return (
        "למדו על מצוות הלוואת המית — ללוות את הנפטר לקבורה! 📜 "
        "זה נקרא חסד של אמת — נתינה טהורה שאין לrecipient דרך להשיב. "
        "הרמב\"ם מסביר שזו מצוות תורה מלאה מואהבת לרעך כמוך! 💙 "
        "אפילo כמה צעדים עם המיטה — מצווה עצומה שמכבדת את הנשמה. "
        "אנשים מפסיקים לימוד תורה כדי להצטרף ללוויה. 🏛️ "
        "כל יehudi הוא כלי קדושה — ראוי לכבוד עד הסוף. למדו עוד!"
    )


def _chukot() -> str:
    return (
        "למדu על איסור חוקות האומות — וכמה המציאות nuanced! 🕊️ "
        "התורה: ולא תלכu בחוקot הגoy (ויקרא י\"ח:ג). "
        "הרמב\"ם ושulchan Aruch אוסרים מנהגים של עבודה זרה או דתות אחרות. "
        "שulchan Aruch (יו\"ד קע\"ח): לא ללבוש בגדים מזוהים עם גoyim לחקותם. "
        "מה לא אסור: דברים שאימצו לצורך (חליפה ועניבה). "
        "האיסור על מנהג שאין לו טעם אלא כך נוהגים הם. "
        "חשוב: לא חוסר כבוד לגoyim — שמירה על זהות יehudit נפרדת!"
    )


def _mashiach() -> str:
    return (
        "למדu על תיאור הרמב\"ם המעשי של יemot HaMashiach — אולי תופתעu מכמה זה ארצי! 🌍 "
        "הרמב\"ם (הלכות מלachim י\"א–י\"ב): עמד מלך המשיח ומחזיר מלכut בית דavid... "
        "יבנה המקדש ויקבץ נiddchי ישראל. "
        "ואז: אל תעלה על דaתך שהעולם ישתנה ממנהagו... זeב עם כבש — מטaphora לישראל בשלווה. "
        "ימot HaMashiach יהיו טבעיים, לא נסיים. עצים יזדקקו לנטיעה, השמש תזrח כרגיל. "
        "ההבדל: ישראל חfree מעbדות, יוכל ללמוד תורה בלי הסchah. "
        "לא יהיה רעב ולא מלחמה... טובות בשפע. "
        "המטרה: חofshi ללimud Torah — לא שלטון או הנאה. יש חכמים שחולקים — מה לדעתכם?"
    )


def _lashon_permitted() -> str:
    return (
        "למדu מתי מותר לדבר שלילה — לשון הרע יש חריגים! 👄 "
        "החפץ חים: דיבור שלילי מותר (ולפעמים חובה) כשכל שבע התנאים מתקיימים: "
        "(1) אמת — בדקתם בעצמכם. (2) צורך — toelet אמיתי. (3) ניסיון לדבר ישירות קודם. "
        "(4) פרופורציה — בלי הגזמה. (5) כוונה טהורה — לא נקmמה. "
        "(6) הנזק מגילוי לא גדול מהנזק שנמנע. (7) אין דרך אחרת. "
        "דוגמה: להזהיר מ שותף עסקי רמאי — חובה! "
        "האיסור אינו לעולם לא תגיד רע — אלא לא בלי מטרה אמיתית. למדu עוד!"
    )


def _lashon_receive() -> str:
    return (
        "למדu על איסור לקבל לשון הרע — חמור כמו לדבר! 🤐 "
        "החפץ חים: אסור להאזין ולהאמין. אסור: (1) להקשיב ולעודd; (2) ליהנות; (3) להאמין כעובדה. "
        "התalmud: השomע גרוע מהמדבר — הנזק לתפisה כבר נעשה. "
        "בpraktika: נסu להפnות או לצאת; אם תקועים — שומעים בלי מקבלים. "
        "חובה לא להאמין על יehudi אחר — גם בלי הוכחה מיידית. "
        "מקור: החפץ חים הלכות רכילות; תalmud נiddah ס\"א."
    )


if __name__ == "__main__":
    main()
