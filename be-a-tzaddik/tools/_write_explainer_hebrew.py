#!/usr/bin/env python3
"""Merge quality Hebrew explainer translations into human/checklist_explainers.json."""
from __future__ import annotations

import json
from pathlib import Path

from _mourning_period_translations import (
    ND_ASH_HE,
    NINE_DAYS_SHARED_EN,
    NINE_DAYS_SHARED_HE,
    THREE_WEEKS_INTRO_EN,
    THREE_WEEKS_INTRO_HE,
)

ROOT = Path(__file__).resolve().parents[1]
EXPLAINERS = ROOT / "data" / "translation-catalog" / "human" / "checklist_explainers.json"
CATALOG = ROOT / "data" / "translation-catalog" / "strings.json"

# Keys pulled from catalog after _sync_explainer_catalog_keys.py
HE: dict[str, str] = {}

def load_keys() -> None:
    strings = json.loads(CATALOG.read_text(encoding="utf-8"))["strings"]
    global HE
    HE = {}
    omer = next(s for s in strings if s.startswith("Sefirat HaOmer links"))
    chanukah = next(s for s in strings if s.startswith("Chanukah night $day"))
    ilanot = next(s for s in strings if s.startswith("Birkat Ha'Ilanot (ב"))
    kiddush = next(s for s in strings if s.startswith("Kiddush Levana (Sanctification"))
    tw_intro = next(s for s in strings if s == THREE_WEEKS_INTRO_EN or (
        s.startswith("The Three Weeks (בין המצרים)") and "Three stages of mourning" in s
    ))
    tw_ash = next(s for s in strings if "Ashkenazi custom observes a longer" in s)
    tw_sep = next(s for s in strings if "Sephardic and Edot HaMizrach" in s)
    tw_ch = next(s for s in strings if "Chabad follows strict Ashkenazi mourning" in s)
    tw_ash_full = next(
        s for s in strings
        if s.startswith("The Three Weeks") and "Ashkenazi custom observes a longer" in s
    )
    tw_sep_full = next(
        s for s in strings
        if s.startswith("The Three Weeks") and "Sephardic and Edot HaMizrach" in s
    )
    tw_ch_full = next(
        s for s in strings
        if s.startswith("The Three Weeks") and "Chabad follows strict Ashkenazi mourning" in s
    )
    nd_ash = next(s for s in strings if s.startswith("The Nine Days (from 1 Av"))
    nd_sep = next(s for s in strings if "shavuah she'chal bo" in s and "Sephardic communities" in s)
    nd_ch = next(s for s in strings if "Chabad practice" in s and "Nine Days" in s)

    when_south = """When (Southern Hemisphere — your location):
The Shulchan Arukh places this blessing in Nissan, when spring arrives in Israel. Mainstream poskim rule that the blessing follows the natural blossoming of fruit trees in your locale — not the calendar month alone (Yalkut Yosef; Peninei Halakha). In Australia, South America, southern Africa, and similar regions, local spring falls in Elul and Tishrei (typically September–October). Say it once during that window, as soon as you see the first blossoms."""

    when_north = """When (Northern Hemisphere — your location):
Say it during Nissan, when fruit trees in your area begin to blossom — ideally as early in the month as you first see flowers (Shulchan Arukh O.C. 226:1)."""

    when_unknown = """When:
In the Northern Hemisphere, say it during Nissan when fruit trees blossom (Shulchan Arukh O.C. 226:1). In the Southern Hemisphere, say it when local fruit trees blossom in Elul–Tishrei. Set your city or GPS in Settings so the app shows this item in the correct season for you."""

    wait_ash = "Recited once a month when the moon is visible, usually beginning on the 3rd night of the Hebrew month (Ashkenaz / Chabad custom; Peninei Halakha 05-01-18)."
    wait_sef = "Recited once a month when the moon is visible, usually beginning on the 7th night of the Hebrew month (Shulchan Arukh O.C. 426:4; Peninei Halakha 05-01-18)."
    wait_edot = "Recited once a month when the moon is visible. The majority of Sefardim wait until the 7th of the month (Shulchan Arukh O.C. 426:4). Moroccan and some other North African kehillot begin after 3 days (Peninei Halakha 05-01-18) — follow your community."

    brachot_first = "First night: say all three brachot including Shehecheyanu."
    brachot_other = "Tonight: two brachot (no Shehecheyanu unless first time lighting this year)."

    nusach_omer = {
        "Many Ashkenazim count after Maariv.": "רבים מאשכנזים סופרים אחרי מעריב.",
        "Many Sephardim count after Maariv.": "רבים מספרדים סופרים אחרי מעריב.",
        "Many Edot HaMizrach kehillot count after Maariv.": "קהילות רבות מעדות המזרח סופרות אחרי מעריב.",
        "Many in Chabad count after Maariv (Tehillat Hashem).": "רבים בחב״ד סופרים אחרי מעריב (תהילת ה׳).",
    }

    HE.update({
        omer: (
            "ספירת העומר מקשרת בין פסח לשבועות — ספירת כל יום מהיציאה ממצרים עד קבלת התורה.\n\n"
            "היום בעומר: $todaySummary (יום $day מתוך 49).\n\n"
            "ספירת הלילה:\n"
            "• ליל $tonight — ספרו $todaySummary אחרי צאת הכוכבים$timePart.\n"
            "$nextNightLine\n\n"
            "איך לספור:\n"
            "• עמדו ואמרו את הברכה לפני הספירה אם אתם עדיין אומרים אותה בברכה (אם פספסתם יום, שאלו את הרב לפני שממשיכים בברכה).\n"
            '• אמרו: "$speechPhrase"\n'
            "• ספרו אחרי צאת הכוכבים (צאת); סיימו לפני עלות השחר. אם שכחתם בלילה, ספרו ביום למחרת בלי ברכה. אם עשיתם זאת לפני שקיעה, אפשר להמשיך בלילות הבאים בברכה. מאבדים את הברכה לצמיתות רק אם פספסתם מחזור שלם של 24 שעות (גם לילה וגם היום שלאחריו) — שאלו את הרב.\n\n"
            "$nusachWhen"
        ),
        chanukah: (
            "ליל חנוכה $day מתוך 8 — הדלקת המנורה.\n\n"
            "מתי:\n"
            "• הדליקו אחרי צאת הכוכבים — לא לפני השקיעה. ביום שישי, הדליקו נרות חנוכה לפני נרות שבת.\n"
            "• במוצאי שבת, הדליקו חנוכה לפני או אחרי הבדלה לפי המנהג.\n\n"
            "איך:\n"
            "• פרסומי ניסא — הניחו את המנורה ליד חלון או פתח.\n"
            "• הכניסה מימין לשמאל; הדלקה משמאל לימין.\n"
            "• ברכות (בלילה הראשון שלוש; בשאר הלילות שתיים).\n\n"
            "תפילה וסעודות:\n"
            "• הוסיפו על הניסים בכל עמידה ובברכת המזון כל היום.\n\n"
            "$brachotNote\n\n"
            "אחרי ההדלקה: נהרות הללו ומעוז צור (מנהג)."
        ),
        ilanot: (
            "ברכת האילנות (בִּרְכַּת הָאִילָנוֹת) נאמרת פעם בשנה על ראיית אילני פרי בפריחה.\n\n"
            "$whenBlock\n\n"
            "הברכה: ברוך אתה ה׳ שלא חסר בעולמו כלום וברא בו בריות טובות ואילנות טובים להנות בהם בני אדם.\n\n"
            "איך למלא: צאו לחוץ לאילני פרי; אמרו פעם אחת בראיית הפרחים."
        ),
        when_south: (
            "מתי (חצי הכדור הדרומי — המיקום שלכם):\n"
            "אמרו כשאילנות פורחים באזורכם — בדרך כלל באלול–תשרי בחצי הכדור הדרומי."
        ),
        when_north: (
            "מתי (חצי הכדור הצפוני — המיקום שלכם):\n"
            "אמרו בניסן, כשאילני הפרי באזורכם מתחילים לפרוח (שו״ע או״ח רכ״ו:א׳)."
        ),
        when_unknown: (
            "מתי:\n"
            "בצפון — ניסן; בדרום — אלול–תשרי. הגדירו מיקום בהגדרות."
        ),
        kiddush: (
            "קידוש לבנה (ברכת הלבנה). גברים חייבים; נשים פטורות והמנהג שאינן אומרות (ראו דרכה).\n\n"
            "$waitLine\n\n"
            "מועד אחרון: סוף החלון במילוא הלבנה (~14.75 ימים מהמולד).\n\n"
            "מתי: אחרי צאת, בחוץ; עדיף במוצאי שבת; לא בשבת או יום טוב.\n\n"
            "איך: הירח חייב להיות נראה; השתמשו בסידור."
        ),
        wait_ash: "נאמרת פעם בחודש, בדרך כלל מהלילה השלישית (אשכנז / חב״ד).",
        wait_sef: "נאמרת פעם בחודש, בדרך כלל מהלילה השביעית (שו״ע או״ח תכ״ו:ד).",
        wait_edot: "נאמרת פעם בחודש; רוב הספרדים מהיום השביעי; מרוקאים לעיתים אחרי 3 ימים.",
        brachot_first: "לילה ראשון: שלוש ברכות כולל שהחיינו.",
        brachot_other: "הלילה: שתי ברכות (בלי שהחיינו אלא אם זו הפעם הראשונה השנה).",
        tw_intro: THREE_WEEKS_INTRO_HE,
        tw_ash: (
            "מנהג אשכנז: אבל ממושך ומחמיר לאורך שלושת השבועות, מתחזק בתשעת הימים.\n\n"
            "מי״ז בתמוז (שלושת השבועות בכלל):\n"
            "• תספורת וגילוח: אסורים לכל תקופת שלושת השבועות.\n"
            "• מוזיקה: אין להאזין למוזיקה כלי לכל התקופה.\n"
            "• חתונות: לא מתקיימות.\n"
            "• שהחיינו: בדרך כלל לא על בגדים או פירות חדשים; מותר בשבת.\n\n"
            "מא׳ באב (תשעת הימים): ההגבלות מתחזקות — ראו פריט תשעת הימים לבשר, יין, כביסה, רחצה ומנהגי בית."
        ),
        tw_sep: (
            "קהילות ספרדיות ועדות המזרח, לפי שולחן ערוך, מקילות יותר מאשכנזים בתחילת שלושת השבועות.\n\n"
            "מי״ז בתמוז (שלושת השבועות בכלל):\n"
            "• תספורת וגילוח: מותרים ברוב שלושת השבועות; גילוח אסור בדרך כלל רק בשבוע שחל בו ט׳ באב.\n"
            "• מוזיקה: נמנעים ממוזיקה חיה או מוקלטת.\n"
            "• חתונות: חלק מהקהילות נמנעות מי״ז בתמוז; אחרות רק מר״ח אב — לפי הקהילה.\n"
            "• שהחיינו: נמנע על פריטים חדשים לכל התקופה.\n\n"
            "מר״ח אב או שבוע ט׳ באב: הגבלות נוספות — ראו פריט תשעת הימים."
        ),
        tw_ch: (
            "חב״ד עוקב אחר מנהגי אבל אשכנזיים מחמירים, עם דגש הרבי על צמיחה רוחנית בתקופה זו.\n\n"
            "מי״ז בתמוז (שלושת השבועות בכלל):\n"
            "• תספורת, מוזיקה וחתונות: אסורים לכל שלושת השבועות.\n"
            "• שהחיינו: נמנע לחלוטין, חוץ בשבת או למצווה (למשל ברית מילה).\n"
            "• תורה וצדקה: מרבים לימוד תורה — במיוחד על המקדש — וצדקה.\n\n"
            "מר״ח אב (תשעת הימים): ההגבלות מתחזקות — ראו פריט תשעת הימים."
        ),
        nd_ash: ND_ASH_HE,
        nd_sep: (
            "תשעת הימים ושבוע תשעה באב — ספרדים: מחמירים בשבוע שחל בו תשעה באב.\n"
            "בשר, יין, כביסה ורחצה לפי המנהג; שאלו את הרב."
        ),
        nd_ch: (
            "תשעת הימים בחב״ד — בשר ויין אסורים מר״ח אב; כביסה ורחצה לתענוג.\n"
            "אחרי הצום: לפי פסק חב״ד עד חצות י׳ באב."
        ),
        **nusach_omer,
    })
    HE[tw_ash_full] = THREE_WEEKS_INTRO_HE + "\n\n" + HE[tw_ash]
    HE[tw_sep_full] = THREE_WEEKS_INTRO_HE + "\n\n" + HE[tw_sep]
    HE[tw_ch_full] = THREE_WEEKS_INTRO_HE + "\n\n" + HE[tw_ch]
    HE[NINE_DAYS_SHARED_EN if NINE_DAYS_SHARED_EN in strings else next(
        s for s in strings if "At the seudah hamafseket, do not bentch" in s
    )] = NINE_DAYS_SHARED_HE
    load_template_keys(strings)


def load_template_keys(strings: list[str]) -> None:
    """Hebrew for catalog template keys added by _sync_explainer_catalog_keys.py."""
    halacha = "שו״ע או״ח תכ״ב:א׳; פניני הלכה 05-01-10"

    forgot_sh = next(
        s for s in strings
        if s.startswith("If you forgot:\n") and "repeat only the Shacharit Amidah" in s
    )
    forgot_min = next(
        s for s in strings
        if s.startswith("If you forgot:\n") and "repeat only the Mincha Amidah" in s
    )
    forgot_maariv = next(s for s in strings if s.startswith("If you forgot at Maariv on Rosh Chodesh:"))

    arba_men = next(s for s in strings if s.startswith("Arba Minim") and "Men — Torah obligation" in s)
    arba_fem = next(s for s in strings if s.startswith("Arba Minim") and "Women — recommended mitzvah" in s)
    mechirat = next(s for s in strings if s.startswith("Mechirat chametz") and "$scheduleLeadIn" in s)
    mechirat_urgency_only = next(
        s for s in strings
        if s.startswith("Mechirat chametz (מְכִירַת") and s.endswith("$urgency") and "$schedule" not in s
    )
    mechirat_short = next(
        s for s in strings
        if s.startswith("Mechirat chametz is selling")
    )
    seder = next(s for s in strings if s.startswith("$intro\n\n$sederNights"))
    bedikat = next(s for s in strings if s.startswith("Bedikat chametz (בְּדִיקַת") and "$scheduleLeadIn" in s)
    bedikat_no_sched = next(
        s for s in strings
        if s.startswith("Bedikat chametz (בְּדִיקַת") and s.endswith("to search.")
    )
    bedikat_short = next(
        s for s in strings
        if s.startswith("Bedikat chametz is the formal search")
    )
    biur = next(s for s in strings if s.startswith("Biur chametz") and "$morningNote" in s)

    HE.update({
        forgot_sh: (
            "אם שכחתם:\n"
            "• עדיין ב״רצה״ (לפני סיום הברכה) — הוסיפו יעלה ויבוא במקומה והמשיכו (" + halacha + ").\n"
            "• אחרי שסיימתם ״רצה״ — חזרו לתחילת ״רצה״, הוסיפו יעלה ויבוא, והשלימו את שאר הברכות (" + halacha + ").\n"
            "• סיימתם את כל העמידה (אחרי ״יהיו לרצון״) — חזרו על עמידת שחרית בלבד, לא על כל התפילה (" + halacha + ")."
        ),
        forgot_min: (
            "אם שכחתם:\n"
            "• עדיין ב״רצה״ (לפני סיום הברכה) — הוסיפו יעלה ויבוא במקומה והמשיכו (" + halacha + ").\n"
            "• אחרי שסיימתם ״רצה״ — חזרו לתחילת ״רצה״, הוסיפו יעלה ויבוא, והשלימו את שאר הברכות (" + halacha + ").\n"
            "• סיימתם את כל העמידה — חזרו על עמידת מנחה בלבד (" + halacha + ")."
        ),
        forgot_maariv: (
            "אם שכחתם במעריב של ראש חודש:\n"
            "• עדיין ב״רצה״ לפני שם ה׳ בסיום — הוסיפו יעלה ויבוא שם והמשיכו (" + halacha + ").\n"
            "• אחרי שסיימתם ״רצה״, או אחרי כל העמידה — אין לחזור ואין לחזור על העמידה. בית דין מקדש את החודש ביום, לא בלילה (ברכות ל׳ ב; " + halacha + "). המשיכו בתפילה."
        ),
    })

    HE.update({
        arba_men: (
            "ארבעת המינים (ארבעה מינים) — נטילה בכל יום חג הסוכות (חוץ משבת).\n\n"
            "ארבעת המינים:\n"
            "• לולב — כפה סגורה (לפחות 3 טפחים)\n"
            "• אתרוג — יפה ושלם (פיטם שלם אם קיים)\n"
            "• הדסים — שלושה ענפי הדס\n"
            "• ערבות — שני ענפי ערבה\n\n"
            "איך לקיים (כולם):\n"
            "• קשרו לולב, הדס וערבה (בכיסן / קוישן). שדרת הלולב גבוהה לפחות טפח מראשי ההדס והערבה (שו״ע או״ח תר״נ:א׳).\n"
            "• בדקו כשרות לפני יום טוב — בעיקר אתרוג ורעננות העלים.\n"
            "• מתי: ביום; רבים לפני שחרית בבית או בבית כנסת. לא בשבת.\n"
            "• $daysNote\n\n"
            "אם חסר מין או פסול — שאלו את הרב.\n\n"
            "גברים — חובה מהתורה:\n"
            "• יום ראשון של סוכות (ט״ו תשרי) הוא יום החובה העיקרי. בחו״ל המצווה נמשכת מדרבנן ביום ב׳ וחול המועד.\n"
            "• ברכה: ״על נטילת לולב״ בכל יום (חוץ משבת). שהחיינו ביום הראשון בלבד.\n"
            "• $menWave\n\n"
            "כלל הבעלות (לכם — ולקחתם לכם):\n"
            "• בארץ: ביום ראשון חובה לבעלות במערך (שו״ע או״ח תרנ״ח:ג׳). אם אין — בקשו מתנה על מנת להחזיר.\n"
            "• בחו״ל: ביום א׳ ויום ב׳ — אותו כלל; מיום ג׳ (חול המועד) אפשר לשאול מבית כנסת בלי מתנה (פניני הלכה, סוכה י״ג:י״ג).\n"
            "• בארץ בחול המועד: שאילה בלי מתנה מותרת."
        ),
        arba_fem: (
            "ארבעת המינים — נטילה בכל יום חג הסוכות (חוץ משבת).\n\n"
            "ארבעת המינים:\n"
            "• לולב, אתרוג, שלושה הדסים, שתי ערבות.\n\n"
            "איך לקיים:\n"
            "• קשרו את המינים; בדקו כשרות לפני יום טוב.\n"
            "• מתי: ביום; לא בשבת.\n"
            "• $daysNote\n\n"
            "נשים — מצווה מומלצת (לא חובה):\n"
            "• פטורות ממצווה שהזמנה גרמה, אך רבות נוטלות — מסומן כהמלצה, לא כחובה יומית כמו לגברים ביום הראשון.\n\n"
            "האם נשים צריכות סט משלהן?\n"
            "• לא — רוב הנשים משתמשות במשפחתי או בבית כנסת.\n\n"
            "כלל הבעלות (לגברים):\n"
            "• בארץ: ביום ראשון בלבד; בחו״ל: יום א׳–ב׳; משלישי אפשר לשאול.\n\n"
            "ברכה:\n"
            "• $brachaLine\n\n"
            "איך לנענע:\n"
            "• $waveLine\n"
            "• אתרוג בשמאל, לולב בימין — או יחד לפי הסידור.\n\n"
            "בכל יום חג (חוץ משבת) אפשר לקיים שוב את המצווה המומלצת."
        ),
        mechirat: (
            "מכירת חמץ — מכירת החמץ לגוי דרך הרב לפני פסח — מאפשרת להשאיר חמץ נעול בבית בלי בעלות עליו בפסח (שו״ע או״ח תמ״ח).\n\n"
            "למה:\n"
            "• בפסח אסורה בעלות חמץ. המכירה מעבירה בעלות.\n\n"
            "איך:\n"
            "• חתמו או אישרו מכירה אצל הרב (גם אונליין). המכירה נכנסת לתוקף בבוקר ערב פסח, אבל יש לאשר מראש — רוב הרבנים מפסיקים לקבל בליל שלפני ערב פסח.\n"
            "• סמנו ארונות/חדרים במכירה; הפרידו חמץ נמכר ממה שתשרפו.\n"
            "• אחרי תחילת המכירה — אל תאכלו חמץ נמכר.\n"
            "• כלים: רבים כוללים טעם חמץ בכלים במכירה ונועלים אותם; הכלים עצמם לא נמכרים (שו״ע יו״ד ק״כ).\n"
            "• שמרו אישור; רבים מוכרים דרך רב מרכזי.\n\n"
            "אחרי פסח: החמץ נרכש חזרה לפי תנאי המכירה — לפי הוראות הרב.\n"
            "$urgency\n"
            "$scheduleLeadIn$scheduleBody$scheduleYomTov"
        ),
        mechirat_urgency_only: (
            "מכירת חמץ — מכירת החמץ לגוי דרך הרב לפני פסח — מאפשרת להשאיר חמץ נעול בבית בלי בעלות עליו בפסח (שו״ע או״ח תמ״ח).\n\n"
            "למה:\n"
            "• בפסח אסורה בעלות חמץ. המכירה מעבירה בעלות.\n\n"
            "איך:\n"
            "• חתמו או אישרו מכירה אצל הרב (גם אונליין). המכירה נכנסת לתוקף בבוקר ערב פסח, אבל יש לאשר מראש — רוב הרבנים מפסיקים לקבל בליל שלפני ערב פסח.\n"
            "• סמנו ארונות/חדרים במכירה; הפרידו חמץ נמכר ממה שתשרפו.\n"
            "• אחרי תחילת המכירה — אל תאכלו חמץ נמכר.\n"
            "• כלים: רבים כוללים טעם חמץ בכלים במכירה ונועלים אותם; הכלים עצמם לא נמכרים (שו״ע יו״ד ק״כ).\n"
            "• שמרו אישור; רבים מוכרים דרך רב מרכזי.\n\n"
            "אחרי פסח: החמץ נרכש חזרה לפי תנאי המכירה — לפי הוראות הרב.\n"
            "$urgency"
        ),
        mechirat_short: (
            "מכירת חמץ — מוכרים דרך הרב חמץ שרוצים להשאיר נעול, כדי שלא יהיה שלכם בפסח. "
            "יש לאשר את המכירה זמן רב לפני ערב פסח — רוב הרבנים מפסיקים לקבל טפסים בליל שלפני, "
            "למרות שהמכירה נכנסת לתוקף בבוקר ערב פסח. אחרי פסח הבעלות חוזרת לפי החוזה."
        ),
        seder: (
            "$intro\n\n"
            "$sederNights\n"
            "$secondSederHachana\n"
            "$omerTrigger\n\n"
            "הכנה לפני יום טוב:\n"
            "• מצה שמורה למוציא ומצוות מצה (שלוש מצות לפי המנהג)\n"
            "• מרור — חסה, חרדל וכו׳ לפי המנהג\n"
            "• ארבעה כוסות יין (או מיץ ענבים לפי הרב)\n"
            "• הגדה לכל אחד\n"
            "• קערה: זרוע, ביצה, כרפס, חרוסת, מרור, חזרת\n"
            "• זרוע: לרוב צולים בערב פסח לפני השקיעה (לא אוכלים אותה בלילה)\n"
            "• הסיבה (הסבה): השתכבו שמאלה בכוסות, מצה, כורך ואפיקומן — לא במרור\n"
            "• שולחן חג; נרות ליום טוב\n\n"
            "מטבח:\n"
            "• רק כשר לפסח מעתה\n"
            "• חימום על פלטה או טיימר ליום טוב\n\n"
            "בסדר: לפי ההגדה — קידוש, סדר הלילה, ברכות וארבעה כוסות. $sederNightCount ליל(ות) סדר השנה.\n"
            "$scheduleLeadIn$scheduleBody$scheduleYomTov"
        ),
        bedikat: (
            "בדיקת חמץ — מצווה מדרבנן בליל שלפני ערב פסח (אחרי צאת, כשהתאריך העברי הופך לי״ד בניסן).\n\n"
            "$whenLine\n\n"
            "איך לבדוק:\n"
            "• נר (או פנס), כף עץ ונוצה (או שקית) לאיסוף פירורים.\n"
            "• חפשו בכל חדר שיכול היה להיכנס חמץ — מטבח, סלון, רכב, תיקים.\n"
            "• תחת רהיטים, כריות, מושבי רכב.\n"
            "• עשרה פירות לחם (אופציונלי במנהג) — רשמו מיקומים וודאו שכל עשרה נמצאו.\n\n"
            "אחרי הבדיקה:\n"
            "• ברכה ״על ביעור חמץ״ וקול חמירא (ביטול) — גרסת הלילה.\n"
            "• גרסת הלילה מבטלת רק חמץ שלא ראיתם — עדיין מותר חמץ לבוקר.\n"
            "• כשערב פסח בשבת: ביטול ראשון בחמישי בלילה; קול חמירא סופי בשבת בבוקר.\n"
            "• אספו חמץ לשקית לביעור בבוקר.\n"
            "• אחרי צאת: לא סעודה ולא עבודה עד סיום הבדיקה; מחצות למחרת — הימנעו מארוחה כבדה לפני הסדר.\n\n"
            "אם אתם בארח או בדרך — שאלו מארח או רב אילו חדרים עליכם.\n"
            "$scheduleLeadIn$scheduleBody$scheduleYomTov"
        ),
        bedikat_no_sched: (
            "בדיקת חמץ — מצווה מדרבנן בליל שלפני ערב פסח (אחרי צאת, כשהתאריך העברי הופך לי״ד בניסן).\n\n"
            "$whenLine\n\n"
            "איך לבדוק:\n"
            "• נר (או פנס), כף עץ ונוצה (או שקית) לאיסוף פירורים.\n"
            "• חפשו בכל חדר שיכול היה להיכנס חמץ — מטבח, סלון, רכב, תיקים.\n"
            "• תחת רהיטים, כריות, מושבי רכב.\n"
            "• עשרה פירות לחם (אופציונלי במנהג) — רשמו מיקומים וודאו שכל עשרה נמצאו.\n\n"
            "אחרי הבדיקה:\n"
            "• ברכה ״על ביעור חמץ״ וקול חמירא (ביטול) — גרסת הלילה.\n"
            "• גרסת הלילה מבטלת רק חמץ שלא ראיתם — עדיין מותר חמץ לבוקר.\n"
            "• כשערב פסח בשבת: ביטול ראשון בחמישי בלילה; קול חמירא סופי בשבת בבוקר.\n"
            "• אספו חמץ לשקית לביעור בבוקר.\n"
            "• אחרי צאת: לא סעודה ולא עבודה עד סיום הבדיקה; מחצות למחרת — הימנעו מארוחה כבדה לפני הסדר.\n\n"
            "אם אתם בארח או בדרך — שאלו מארח או רב אילו חדרים עליכם."
        ),
        bedikat_short: (
            "בדיקת חמץ — חיפוש פורמלי בליל שלפני פסח, אחרי צאת, בנר (או פנס לפי פוסקים). "
            "לעיתים מניחים פירות לחם בחדרים (מנהג); גם בבית נקי — הלכה מחייבת את ליל הבדיקה."
        ),
        biur: (
            "ביעור חמץ — השלמת מצוות הסרת החמץ לפני פסח.\n\n"
            "$morningNote\n"
            "• שרפו או השמידו חמץ מבדיקת הלילה וכל חמץ שלא נמכר.\n"
            "• רבים שורפים בחוץ; פירורים קטנים — שאלו את הרב (למשל ביוב).\n"
            "• $zmanNote\n"
            "• מגבלת זמן: $timelineGuardrail\n"
            "• גרסת הבוקר של קול חמירא שונה — מבטלת את כל החמץ בבעלותכם.\n"
            "• אמרו קול חמירא סופי מיד אחרי הביעור לפני המועד$kolChamiraShabbatNote\n\n"
            "מכירת חמץ:\n"
            "• חמץ במכירה — נעול ולא נאכל; רק חמץ שלא נמכר נשרף.\n\n"
            "אחרי ביעור:\n"
            "• רק כשר לפסח עד סוף החג.\n"
            "• בכורות: תענית בכורות לפי לוח; סדר $sederTiming.\n"
            "$scheduleLeadIn$scheduleBody$scheduleYomTov"
        ),
    })

    HE.update({
        "In Israel: lulav all 7 days; first day is the primary Torah obligation for men.":
            "בארץ: לולב כל 7 ימי החג; יום ראשון הוא חובת התורה העיקרית לגברים.",
        "In the Diaspora: men's primary Torah obligation is the first day of Sukkot (15 Tishrei); the mitzvah continues rabbinically on the second day of Yom Tov and Chol HaMoed.":
            "בחו״ל: חובת התורה העיקרית לגברים ביום ראשון (ט״ו תשרי); המצווה נמשכת מדרבנן ביום ב׳ וחול המועד.",
    })

    on_shabbat_body = next(s for s in strings if "When Erev Pesach falls on Shabbat, the entire preparatory timeline" in s)
    fri_shabbat_body = next(s for s in strings if "first day of Pesach is Shabbat (15 Nisan)" in s and "Bedikat chametz: Thursday night" in s)
    fri_13 = next(s for s in strings if "Today (Friday, 13 Nisan): biur chametz" in s)

    HE.update({
        on_shabbat_body: (
            "\n\nהשנה ערב פסח בשבת (י״ד בניסן). כשערב פסח חל בשבת, לוח הזמנים נדחה מוקדם (פניני הלכה פ״י):\n\n"
            "• תענית בכורות: חמישי (י״ב בניסן) — לא בשבת או בערב שבת.\n"
            "• בדיקת חמץ: חמישי בלילה (ליל י״ג) אחרי צאת — לא בליל י״ד הרגיל ולא בשבת.\n"
            "• ביעור חמץ: שישי בבוקר (י״ג) — שריפה; בלי קול חמירא סופי בביעור.\n"
            "• מכירת חמץ: לסיים לפני כניסת שבת.\n"
            "• מצה ביום ו׳: רבים נמנעים ממצה רגילה גם ביום ו׳.\n"
            "• הכנת סדר: כל הבישול וההכנה לפני שבת.\n"
            "• בשבת — סיום אכילת חמץ: עד סוף שעה ד׳.\n"
            "• בשבת — השמדת שאריות: שפכו או בטלו לפני שעה ה׳; לא שורפים בשבת.\n"
            "• בשבת — ביטול סופי: קול חמירא לפני סוף שעה ה׳.\n"
            "• סעודות שבת: ספרדים — מצה עשירה; אשכנזים — חלות בזהירות; סעודה שלישית — בלי מצה רגילה.\n"
            "• סדר ראשון: מוצאי שבת עם יקנה״ז.\n\n"
            "תאמו עם הרב וזמנים מקומיים."
        ),
        fri_shabbat_body: (
            "\n\nהשנה ערב פסח ביום ו׳ (י״ד בניסן) ויום טוב ראשון של פסח בשבת (ט״ו בניסן):\n\n"
            "• בדיקה: חמישי בלילה (ליל י״ד) — לא בליל שישי.\n"
            "• תענית בכורות: שישי ביום.\n"
            "• ביעור: שישי בבוקר עד סוף שעה ה׳.\n"
            "• מכירת חמץ: לפני הדלקת שבת/יום טוב.\n"
            "• סדר ראשון: ליל שישי.\n"
            "• סדר שני (חו״ל): מוצאי שבת עם יקנה״ז.\n"
            "• אין הכנה בשבת לסדר השני — רק אחרי צאת.\n\n"
            "אשרו זמנים עם סידור ורב."
        ),
        fri_13: (
            "השנה ערב פסח בשבת (י״ד בניסן). היום (שישי, י״ג): ביעור בבוקר (בלי קול חמירא בשריפה), מכירה לפני נרות שבת. "
            "תענית בכורות הייתה בחמישי. בדיקה הייתה אמש. מחר שבת — גמרו חמץ עד שעה ד׳, ביטול עד ה׳; סדר במוצאי שבת (יקנה״ז)."
        ),
        "Today is Shabbat — Erev Pesach. The steps below should already be done; use this as a checklist.":
            "היום שבת — ערב פסח. השלבים למטה אמורים כבר להיות מבוצעים; השתמשו ברשימה לבדיקה.",
        "Today is Erev Pesach (Friday). The calendar below should already be in motion.":
            "היום ערב פסח (שישי). לוח הזמנים למטה אמור כבר להיות בתנועה.",
        "Read this before bedikat chametz — tomorrow (Thursday) night after tzeit is the search, not the usual Erev Pesach night.":
            "קראו לפני בדיקת חמץ — מחר (חמישי) בלילה אחרי צאת היא הבדיקה, לא בליל ערב פסח הרגיל.",
        "Read this before bedikat chametz — tonight (Thursday night after tzeit) is bedikat, not tomorrow.":
            "קראו לפני בדיקה — הלילה (חמישי אחרי צאת) היא הבדיקה, לא מחר.",
        "Read this before bedikat chametz — tomorrow (Thursday) night after tzeit is bedikat, not Friday night.":
            "קראו לפני בדיקה — מחר (חמישי) בלילה אחרי צאת היא הבדיקה, לא בליל שישי.",
        "Read this before bedikat chametz — tonight (Thursday night after tzeit) is bedikat. Tomorrow is Erev Pesach (Friday): Taanit Bechorot, biur, and mechirat before Shabbat.":
            "קראו לפני בדיקה — הלילה (חמישי אחרי צאת) בדיקה. מחר ערב פסח (שישי): תענית בכורות, ביעור ומכירה לפני שבת.",
    })


def main() -> None:
    load_keys()
    existing = json.loads(EXPLAINERS.read_text(encoding="utf-8")) if EXPLAINERS.exists() else {"he": {}}
    existing.setdefault("he", {}).update(HE)
    EXPLAINERS.write_text(json.dumps(existing, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"Wrote {len(HE)} Hebrew explainer entries to {EXPLAINERS.name}")


if __name__ == "__main__":
    main()
