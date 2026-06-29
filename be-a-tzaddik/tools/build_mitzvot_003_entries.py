#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""Build _mitzvot_003_he_entries.json — strings 6–25 (cloud51–cloud75)."""

from __future__ import annotations

import json
from pathlib import Path

ENTRIES: list[str] = []

# 6 — Brachot on food
ENTRIES.append(
    'למדו על "היתר החוקי" האולטימטיבי ליהנות מהעולם! 🍎 התלמוד אומר שהכל על הארץ שייך לה\', '
    'ולכן לנשוך בלי ברכה זה כמו "לגנוב" מארמון המלך. אמירת ברכה היא בעצם בקשת רשות '
    'והפיכת חטיף פיזי לרגע רוחני! 🏛️ המערכת מדויקת להפליא: אם זה גדל על עץ — "העץ". '
    'אם זה מהאדמה — "האדמה". ללחם — "המוציא" הכבד, ולכל השאר — בשר, גבינה, שוקולד — "שהכל". '
    '🕊️ ואז יש את ה-VIP של עולם המשקאות: יין! כי יין כל כך מיוחד ומשמש לרגעים קדושים כמו קידוש, '
    'יש לו ברכה ייחודית משלו: "בורא פרי הגפן". 🍷 כל כך עוצמתי שאם שותים יין במהלך סעודה, '
    'הוא לעיתים קרובות מכסה את כל שאר המשקאות על השולחן! 🥂 יש אפילו "סדר פעולות" מיוחד '
    'שנקרא מגע אש, שעוזר להחליט איזה מאכל מקבל את הברכה קודם כשיש צלחת גדולה. 🍇 '
    'זו כמו אימון יומי במודעות — לעצור ולהכיר בדיוק מאין האוכל שלכם בא ואת כל היצירות, '
    'הטעamים והניחוחות המדהימים שה\' הניח בעולם ליהנות מהם. 🎊🥯 '
    'מקור: תלמוד, ברכות ל"ה ע"א; שולחן ערוך, אורח חיים רי"א-רכ"א.'
)

# 7 — Answering Amen
ENTRIES.append(
    'למדו על פסק מפתיע: לענות אמן יכול להיות *גדול יותר* מלברך את הברכה עצמה! 🙏 '
    'התלמוד (ברכות נ"ג ע"ב) מעביר ויכוח: מי גדול יותר — המברך או העונה אמן? '
    'רבי יוסי פסק: "העונה אמן גדול מן המברך!" כי אמן הוא אישור — אתם מצהירים '
    'שמה שנאמר הוא אמת לגמרי. כשאתם אומרים אמן לברכה, אתם מאשרים אותה וחולקים בזכותה. '
    'התלמוד משווה לשני פועלים: המוביל (המברך) נכנס ראשון לקרב, אבל המגיב (העונה אמן) '
    'הוא הלוחם החזק שמחזק את הניצחון. הזוהר מלמד ש"אמן" הוא ראשי תיבות של '
    '"א-ל מלך נאמן" — אל, מלך, נאמן. '
    'אז כשאתם עונים אמן, אתם לא רק אומרים "כן" — אתם מכריזים על ה\' כמלך נאמן. '
    'נסו לתת לאמן הבא שלכם קצת יותר כוח! '
    'מקור: תלמוד, ברכות נ"ג ע"ב; זוהר, פרשת ויקרא.'
)

# 8 — Shemoneh Esrei / Amidah
ENTRIES.append(
    'קבעו פגישה לקהל פרטי עם מלך המלכים! 👑 שmonעh esrei — הידוע גam בשם העamidah — '
    'הוא לb כl תפילah יhudית. חachamenu chivru otah be-ruach ha-kodesh, klomar kol milah hi '
    'mafte\'ach meduyak sheno\'ad lifto\'ach she\'arim shamayimim vlehorid brachot ruchaniot '
    'u-gashmiot! 🔓 Ki zot sichah yeshirah im Hashem, le-halakhah yesh killei "פרotokol '
    'melkhuti" lishmor al rikuz. Kodem — la\'amod bereglayim tsmudot, be-hakay hamlakhim '
    'she-yesh lahem "regel achat", lehar\'ot mesirut muzhlatet (vesha\'atem lo holchim leshum '
    'makom bezman she-medabrim im ha-melekh!). 😇 Gam fonim le-Yerushalayim — eifo she\'atem '
    'ba\'olam — kedei lekaven et ha-lev le\'ever mikom Beit HaMikdash! 🏛️ Ha-tefilah mechuleket '
    'le-shlosha chlakim: shlosha brachot shevach, shlosh esre bakashot lidvarim kmo chochmah, '
    'bri\'ut veshgashut, ve-shlosha brachot siyum shel hodayah. Ba-shabat u-v-chagim, tzurat '
    'ha-brachot ha\'emtzaiot meshaneh — vadu she\'atem kor\'im et ha-amidah ha-nechonah min '
    'ha-siddur! 📜 Ba\'od she-kavvanah tovah le\'orekh kol ha-tefillah, ha-chachamim omrim '
    'she-hi kritit beyoter ba-brachah ha-rishonah "Avot" — im ibadetem sham et ha-rikuz, '
    'pispastem et lev ha-mifgash (Ashkenazim lo chozrim lechaazor, Sephardim ken)! 🕊️ '
    'Hichnasu le-mamad acher vehoridu et kol ha-brachot she-Hashem rotzeh latet lachem! '
    'Limdu od! 🎊🙏 Mekor: Talmud, Brachot lamed dalet; Shulchan Aruch, Orach Chaim tzadi heh-tzadi zayin.'
)

if __name__ == "__main__":
    out = Path(__file__).with_name("_mitzvot_003_he_entries.json")
    out.write_text(json.dumps(ENTRIES, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"Wrote {len(ENTRIES)} entries")
