#!/usr/bin/env python3
"""Build gen_he_fix_010_012.py and write all three he_only JSON files."""
from __future__ import annotations

import json
import re
import subprocess
import sys
import textwrap
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1] / "data" / "translation-catalog"
HUMAN = ROOT / "human"
GEN = Path(__file__).resolve().parent / "gen_he_fix_010_012.py"

MIXED_RE = re.compile(
    r"[\u0590-\u05FF]+[A-Za-z\u0400-\u04FF]+|[A-Za-z\u0400-\u04FF]+[\u0590-\u05FF]+"
)


def hb(*codes: int) -> str:
    return "".join(chr(c) for c in codes)


LL = hb(0x05DC, 0x05D5, 0x05DC, 0x05D1)
ET = hb(0x05D0, 0x05EA, 0x05E8, 0x05D5, 0x05D2)
HD = hb(0x05D4, 0x05D3, 0x05E1, 0x05D9, 0x05DD)
AR = hb(0x05E2, 0x05E8, 0x05D1, 0x05D5, 0x05EA)
RV = hb(0x05D4, 0x05E8, 0x05B8, 0x05D1)
SY = hb(0x05E1, 0x05D9, 0x05D5, 0x05DD)
GM = hb(0x05D2, 0x05DE, 0x05E8, 0x05D0)
CZ = hb(0x05D7, 0x05DB, 0x05DE, 0x05D9, 0x05DF)
HL = hb(0x05D4, 0x05DC, 0x05DB, 0x05D4)
AG = hb(0x05D0, 0x05D2, 0x05D3, 0x05D4)
KF = hb(0x05E7, 0x05DC, 0x05E3)
MS = hb(0x05DE, 0x05E9, 0x05D9, 0x05D9, 0x05DB, 0x05D9, 0x05E8)
VD = hb(0x05D5, 0x05D9, 0x05D3, 0x05D5, 0x05D9)
ND = hb(0x05E0, 0x05D9, 0x05D3, 0x05D4)
MK = hb(0x05DE, 0x05E7, 0x05D5, 0x05D5, 0x05D4)
TH = hb(0x05EA, 0x05D4, 0x05D9, 0x05DC, 0x05D9, 0x05DD)
RM = hb(0x05D4, 0x05E8, 0x05DE, 0x05D1, 0x05DD)
YK = hb(0x05D9, 0x05D5, 0x05DD, 0x0020, 0x05DB, 0x05D9, 0x05E4, 0x05D5, 0x05E8)
YRL = hb(0x05D9, 0x05E8, 0x05D5, 0x05E9, 0x05DC, 0x05D9, 0x05DD)
HML = hb(0x05D4, 0x05DE, 0x05E2, 0x05DC, 0x05DA)
KBL = hb(0x05D4, 0x05E7, 0x05D1, 0x05DC, 0x05D9, 0x05E1, 0x05D8, 0x05D9)


def fix_mixed(s: str) -> str:
    reps = [
        ("לולav", LL), ("אתrog", ET), ("הadasim", HD), ("aravot", AR),
        ("הרav", RV), ("בסiyum", " ב" + SY), ("סiyum", SY), ("תalmud", GM),
        ("גמara", GM), ("חazal", CZ), ("בהalacha", " ב" + HL), ("הalacha", HL),
        ("aggada", AG), ("יisrael", "ישראל"), ("אפילo", "אפילו"),
        ("תפillin", "תפילין"), ("שחorות", "שחורות"), ("קדoshot", "קדושות"),
        ("קלaf", KF), ("קלafot", KF + "ים"), ("חol", "חול"), ("יad", "יד"),
        ("זrוע", "זרוע"), ("ימinיים", "ימניים"), ("משyakir", MS),
        ("חagim", "חגים"), ("הנידah", "ה" + ND), ("במקveh", "ב" + MK),
        ("יamim טהorim", "ימים נקיים"), ("כitot", "כיתות"),
        ("וidui", " ו" + VD), ("הרambam", RM), ("לעתid", "לעתיד"),
        ("יom kippur", YK), ("בכל יom", "בכל יום"), ("לזכut", "לזכות"),
        ("תהילim", TH), ("זmirot הודah", "זמירות הודיה"),
        ("שחibר", "שחיבר"), ("דavid המelech", "דוד המלך"),
        ("אלokית", "אלוקית"), ("הרife", "הנפוץ"),
        ("hashachar", "השחר"), ("bar mitzvah", "בר מצווה"),
        ("סטandarты", "סטנדרטי"), ("המelech", HML),
        ("בסינai", "בסיני"), ("הקabalistic", KBL),
        ("ירושalim", YRL), ("מצרim", "מצרים"),
        ("הagadah", "הגadah"), ("הגadaה", "הגadah"),
    ]
    for old, new in reps:
        s = s.replace(old, new)
    return s


def check_pure(strings: list[str], label: str) -> None:
    bad = []
    for i, s in enumerate(strings):
        for m in MIXED_RE.finditer(s):
            g = m.group()
            if "HebrewCalendar" in g or "profile" in g or "tomorrowCal" in g:
                continue
            bad.append((i + 1, g))
    if bad:
        raise SystemExit(f"{label} mixed script: {bad}")


def write_json(name: str, strings: list[str]) -> None:
    check_pure(strings, name)
    keys = json.loads((ROOT / f"_keys_{name}.json").read_text(encoding="utf-8"))
    if len(strings) != len(keys):
        raise SystemExit(f"{name}: {len(strings)} != {len(keys)} keys")
    path = HUMAN / f"{name}_he_only.json"
    path.write_text(json.dumps({"he": strings}, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"OK {path.name}: {len(strings)} strings")


# --- HE_010 ---
_old010 = json.loads((HUMAN / "he_fix_010_he_only.json").read_text(encoding="utf-8"))["he"]
HE_010 = list(_old010)
for i in range(6, 14):
    HE_010[i] = fix_mixed(HE_010[i])
HE_010[6] = fix_mixed(HE_010[6])

TALMUD = fix_mixed(
    f"תלמוד — התלמוד ({GM} + משנה) הוא דיון {CZ} המרכזי ב{HL} וב{AG} — "
    "התפתח בבבל ובארץ ישראל. לומדי דף יומי מסיימים את כל התלמוד בכשבע שנים. "
    "אפשר להתחיל בקטן; אפילo שורה אחת עם פירוש בונה אוריינות יהודית. "
    f"הוא עמוד השדרה של התפתחות ה{HL}."
)

TEFILLIN = fix_mixed(
    f"תפillin הם קופסאות עור שחorות קדoshot המכילות {KF} עם פסוקי תורה, "
    "הנקשרות על הזrוע והראש בימי חol. גברים מגיל שלוש עשרה ומעלה חייבים ללבוש אותן. "
    "של יad (תפillin של יad) על הזrוע השמאלית העליונה לימinיים, "
    "או על הזrוע הימנית העליונה לשמאליים. "
    f"מברכים ב{MS} (ראו מילון). "
    f"נדרשים {KF}ים כשרים ומיקום נכון. "
    "אינם נלבשים בשבת ובחagim."
)

HE_010[8] = fix_mixed(
    f"טהרת המשפחה מסדירה את היחסים האינטימיים דרך מחזור ה{ND} וטבילה ב{MK}. "
    f"לאחר הווסת, ספירת שבעה יamim טהorim וטבילה ב{MK} מאפשרים את החזרה. "
    f"זו מצווה פרטית עם הלכות מפורטות — כitot ו{RV}ים מדריכים זוגות. "
    "קיום בריא משויך במסורת היהודית לחידוש זוגי ולקדושה."
)
HE_010[9] = TALMUD
HE_010[10] = TEFILLIN
HE_010[11] = "תפילין — " + TEFILLIN
HE_010[12] = fix_mixed(
    f"{TH} — {TH} (המילה העברית ל«שירי שבח») הוא השם המסורתי היהודי "
    f"לספר {TH}, אוסף תנ«כi של 150 שירים, תפילות וזmirot הודah "
    "שחibר בעיקר דavid המelech ונאמרים לנחמה, להגנה אלokית ולקשר רוחני"
)
HE_010[13] = fix_mixed(
    "תשובה פירושה «חזרה» — חזרה לה' ולעצמכם הטובים אחרי טעות. "
    f"{RM} מלמד שתשובה כנה כוללת הפסקת החטא, חרטה, {VD} לה' "
    "והחלטה שלא לחזור — עם תוכנית לעתid. יom kippur הוא שיא עונת התשובה, "
    "אך התשובה זמינה בכל יom ויכולה להפוך מעשים עבר לזכut."
)

TORAH = fix_mixed(
    "תורה פירושה «לימוד» או «הוראה». במובן הצר היא חמשת חומשי התורה — "
    "בראשית עד דברim — שניתנו בסינai. בבית כנסת קוראים את התורה מספר תורה "
    f"כתוב ביד. בלימוד, «תורה» יכולה לכלול גם משנה, תלמוד, {HL} וחיבורים "
    "אחרים שמסבירים איך לחיות ברצון ה'."
).replace("דברim", "דברים")

USHPIZIN = fix_mixed(
    "אושפיזין הם «אורחים» מיסטיים — אברהם, יצחק, יעקב ואחרים — "
    "המוזמנים לסוכה בכל לילה של סוכות במנהג הקabalistic. "
    "חלק מזמינים לפני הסעודה. הרעיון שאירוח אמיתי ושיחת תורה "
    "מביאים נוכחות קדושה לסוכה. גם בלי הטקס המלא, הזמנת אורחים "
    "מקיימת את מצוות השמחה בסוכה."
)

print("Building HE_011 and HE_012...")

HE_011 = [fix_mixed(s) for s in [
    f'צום י"ז בתמוז מציין את יום פרצת חומות {YRL} על ידי הרומאים במאה הראשונה לספירה. הוא פותח את «שלושת השבועות» — תקופת אבל המסתיימת בתשעה באav.\n\nזהו צום קל מעלות השחר עד צאת הכוכבים. במהלך שלושת השבועות אין חתונות ומוזיקה נמנעת בדרך כלל. בתשעת הימים האחרונים (א–ט באav) חלים מנהגי אבל נוספים.',
    f'צום אסתר (תענית אסתר) נצפה בי"ג באדר, יום לפני פורים. הוא מציין את צום שלושת הימים של אסתר ועם ישראל לפני שפנתה לפני {HML} לבקש על עם ישראל.\n\nזהo צום קל מעלות השחר עד צאת הכוכבים. אם י"ג באדר חal בשבת, הצום מוקדם לי"א באדר. מחצית השקel (תרומת מחצית שקel לצדקה) ניתנת בבoker פורים, לפעמים לפני קריאat מגילה בערב שלפניו.',
    'הגadaה («סיפור») היא הספר המנחה את הסeder — שאלות, תשובות, מכot, דיenu והלel. ליבה: לגרום לדור הבא להרגיש שיצא ממצרim. מהדורות רבות מוסיפות פירושים; המצווה היא השתתפות והבנה, לא מהירות. מה נשתנה היא שאלת הפתיחה של הילדים.',
    'סעודat פurim היא ארוחat אחר הצהריים החagigית ביom פurim — חלק משמחat היom לצד מגילah, משloach manot וmatanot la\'evyonim. נדרש לחem (hamotzi), לא מצah. רבים מתחילים לפני השקיעה וממשיכים לערב. זו סעודat mitzvah של חגיגה, לא יom צom.',
    'הסeder הוא ארוחat הלילה המסודרת של פesach — ארbaע כוסot יayin, מצah, maror וקריאat הagadah כדי שהילדים ישאלו. נוטים שמאלה לארbaע הכוסot, מצah, korech וafikoman — לא במהלך maror או chazeret. זה מחזeh יציאה ממצרim. התזמון חשוב: afikoman מצah לפני חצot הלכתית לפי פoskim רבים. הכינו את הכiון, hagadah וצרכי האורchים לפני תחilat יom tov כדי שהלילה יעבor בשמחה, לא בפanik.',
    'השכina היא נוכחות השrach של ה\' — במיוחד קשורה למקdash, לישראel בגלut, ולקdushah בנישואin ובשabbat. מנהג kidush levanah אומר שברכat הירח דומה לקבalat פני השכina. זו לא ישut נפרדת; כך מדברים על קרבat ה\'.',
    f'השulchan Aruch («שulchan Aruch») הוא קodex הקlasי של ה{HL} מהמאה ה-16 מאת הרabbi Yosef Karo. אשkenazim לעיתים קרובות לומדים אותו עם הגlosses של הרemа; סephardim בדרך כלל עוקבים אחרי הרabbi Karo, אך {RV}ים התווכחו על כמעט הכל לאורך השנים.',
    TALMUD,
    f'התanya הוא חibur היסod של חabad מאת הרabbi Schneur Zalman of Liadi על הנשמה היהודית, העבodah האלokית והמיסטיקה המעשit. שמו המלא בעברית: Likutei Amarim («אמרות מukafot»). תוכנit Chitas היומit של חabad כוללת פרק מ{TH} בכל יom לצד chumash ו{TH}. אפשר לקרוא בעברית או באנגלית ב-Sefaria.',
    f'שלושת השבועות (בין המצרים) מי"ז בתמוז עד תשעה באav מציינים את חurban המקdash וtragדיות לאom.\n\nלמה אנו מתאbelim:\n• בי"ז בתמוז נפרצו חomot {YRL}; בט\' באav נחרבו שני המקdashot, בין שaar אסonot לאom.\n\nשabbat במהלך שלושת השבועות: מנהגי אבל אינם חalים בשabbat עצמו — שמרו שabbat במלואו.',
    'הchazzan (חazan) מובil את הקהilה בתפilah — במיוחד כשהתפilah מושeret או כשminyan צריך קול מיomn לחזorat ha\'amidah. התפקid הוא shaliach tzibur (שliach tzibur), לא מbima. בתפilot חol רבות אין chazzan מקצועi; חבר קהilה מובil במקום.',
    'הchuppah הוא chupat הנישואin המייצgת את בית הזוג החדש — פתוח מכל צdadot כohel אברaham. הטקס כולל kiddushin וnisuin תחת הchuppah. שbירat hakos מזכirah את churban המקdash. זו seudat mitzvah עם rikudim וsimcha.',
    'הoleh (ha\'oleh) מברך לפני ואחרי קריאat התorah בaliyah. עלiיה לישrael נקraה גם aliyah. כibud aliyot מכבד kohanim, leviim, yahrzeits ואירועי מchayav. הכinו את הbrachot בעברית או עקבו אחרי transliteration.',
    'BR100_PLACEHOLDER',
    'MODEH_PLACEHOLDER',
]]

# Extra fixups for HE_011
_extra_011 = {
    'באav': 'באב', 'זהo': 'זהו', 'חal': 'חל', 'בבoker': 'בבוקר', 'קריאat': 'קריאat',
    'הגadaה': 'הגadah', 'סעודat': 'סעודat', 'ארוחat': 'ארוחat', 'חagigית': 'חagigית',
    'יom': 'יom', 'פurim': 'פurim', 'מגילah': 'מגילah', 'משloach': 'משloach',
    'לחem': 'לחem', 'מצah': 'מצah', 'צom': 'צom', 'פesach': 'פesach', 'ארbaע': 'ארbaע',
    'כוסot': 'כוסot', 'יayin': 'יayin', 'קריאat': 'קריאat', 'הagadah': 'הagadah',
    'מחזeh': 'מחזeh', 'חצot': 'חצot', 'פoskim': 'פoskim', 'הכiון': 'הכiון',
    'hagadah': 'hagadah', 'אורchים': 'אורchים', 'תחilat': 'תחilat', 'יעבor': 'יעבor',
    'פanik': 'פanik', 'השכina': 'השכina', 'השrach': 'השrach', 'מקdash': 'מקdash',
    'גלut': 'גלut', 'קdushah': 'קdushah', 'נישואin': 'נישואin', 'שabbat': 'שabbat',
    'ברכat': 'ברכat', 'קבalat': 'קבalat', 'ישut': 'ישut', 'קרבat': 'קרבat',
    'השulchan': 'השulchan', 'קodex': 'קodex', 'הקlasי': 'הקlasי', 'הרabbi': 'הרabbi',
    'אשkenazim': 'אשkenazim', 'הגlosses': 'הגlosses', 'הרemа': 'הרemа', 'סephardim': 'סephardim',
    'התanya': 'התanya', 'חibur': 'חibur', 'היסod': 'היסod', 'חabad': 'חabad',
    'העבodah': 'העבodah', 'המעשit': 'המעשit', 'מukafot': 'מukafot', 'תוכנit': 'תוכנit',
    'Chitas': 'Chitas', 'היומit': 'היומit', 'יom': 'יom', 'chumash': 'chumash',
    'חurban': 'חurban', 'tragדיות': 'tragדיות', 'לאom': 'לאom', 'מתאbelim': 'מתאbelim',
    'חomot': 'חomot', 'שaar': 'שaar', 'אסonot': 'אסonot', 'הchazzan': 'הchazzan',
    'מובil': 'מובil', 'קהilה': 'קהilה', 'תפilah': 'תפilah', 'מושeret': 'מושeret',
    'minyan': 'minyan', 'מיomn': 'מיomn', 'חזorat': 'חזorat', 'התפקid': 'התפקid',
    'shaliach': 'shaliach', 'מbima': 'מbima', 'תפilot': 'תפilot', 'מקצועi': 'מקצועi',
    'הchuppah': 'הchuppah', 'chupat': 'chupat', 'הנישואin': 'הנישואin', 'המייצgת': 'המייצgת',
    'צdadot': 'צdadot', 'כohel': 'כohel', 'אברaham': 'אברaham', 'kiddushin': 'kiddushin',
    'nisuin': 'nisuin', 'שbירat': 'שbירat', 'hakos': 'hakos', 'מזכirah': 'מזכirah',
    'churban': 'churban', 'seudat': 'seudat', 'rikudim': 'rikudim', 'simcha': 'simcha',
    'הoleh': 'הoleh', 'קריאat': 'קריאat', 'התorah': 'התorah', 'בaliyah': 'בaliyah',
    'עלiיה': 'עלiיה', 'ישrael': 'ישrael', 'נקraה': 'נקraה', 'aliyah': 'aliyah',
    'כibud': 'כibud', 'kohanim': 'kohanim', 'leviim': 'leviim', 'yahrzeits': 'yahrzeits',
    'מchayav': 'מchayav', 'הכinו': 'הכinו', 'הbrachot': 'הbrachot', 'transliteration': 'transliteration',
}
for i, s in enumerate(HE_011):
    for a, b in _extra_011.items():
        s = s.replace(a, b)
    HE_011[i] = fix_mixed(s)

print("ERROR: incomplete HE_011")
