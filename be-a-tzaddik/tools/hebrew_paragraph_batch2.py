"""Second batch of hand-reviewed Hebrew paragraph fixes."""

from __future__ import annotations

FIXES: dict[str, str] = {
    "Recite Half Hallel after Shacharit on Rosh Chodesh if you say Hallel — a cherished custom, not obligatory for women (Peninei Halakha 05-01-12). Many Ashkenazi women omit Hallel; many Sephardi women recite it. Follow your community.\n\nTachanun is omitted on Rosh Chodesh.": (
        "אמרו חצי הלל אחר שחרית בראש חודש אם אומרים הלל — מנהג אהוב, לא חובה לנשים. רבות אשכנזיות מדלגות; רבות ספרדיות אומרות. לפי הקהילה.\n\nלא תחנון בראש חודש."
    ),
    "Pesukei d'Zimra (\"verses of song\") open Shacharit — psalms and praises warming the heart before Shema. Baruch She'amar and Yishtabach frame the section. Skipping it to \"save time\" loses the service's emotional arc. On Shabbat, Nishmat and expanded psalms add length and joy.": (
        "פסוקי דזמרה פותחים את שחרית — תהילים ושבחים לפני שמע. ברוך שאמר וישתבח מסגרים את הקטע. לדלג — מאבדים את קשת הרגש. בשבת — נשמת ומזמורים מורחבים."
    ),
    "Pesukei d'Zimra — Pesukei d'Zimra (\"verses of song\") open Shacharit — psalms and praises warming the heart before Shema. Baruch She'amar and Yishtabach frame the section. Skipping it to \"save time\" loses the service's emotional arc. On Shabbat, Nishmat and expanded psalms add length and joy.": (
        "פסוקי דזמרה — פותחים שחרית; ברוך שאמר וישתבח; בשבת נשמת."
    ),
    "Rosh Chodesh falls during Chanukah — recite Full Hallel at Shacharit (the Chanukah obligation), not the usual Half Hallel of Rosh Chodesh (Peninei Halakha 12-02-07).\n\nBefore Musaf:\nRemove tefillin before Musaf — do not wear tefillin during the Musaf Amidah.\n\nTachanun is omitted.": (
        "ראש חודש בחנוכה — הלל מלא בשחרית (חובת חנוכה), לא חצי הלל של ראש חודש.\n\nלפני מוסף: הסירו תפילין.\n\nלא תחנון."
    ),
    "Shacharit is the morning service — Pesukei d'Zimra, Shema and its blessings, the Amidah, and on Shabbat and Yom Tov Torah reading and Musaf. The Shema must be recited before the end of the third halachic hour; the Amidah ideally before the end of the fourth. Tefillin are worn on weekdays. Daily Shacharit anchors the day in prayer before the world's noise.": (
        "שחרית — תפילת הבוקר: פסוקי דזמרה, שמע וברכותיו, עמידה; בשבת ויום טוב קריאת תורה ומוסף. שמע עד סוף השעה השלישית; עמידה עד הרביעית. תפילין בימי חול. שחרית יומית מעגנת את היום בתפילה."
    ),
    "Shacharit — Shacharit is the morning service — Pesukei d'Zimra, Shema and its blessings, the Amidah, and on Shabbat and Yom Tov Torah reading and Musaf. The Shema must be recited before the end of the third halachic hour; the Amidah ideally before the end of the fourth. Tefillin are worn on weekdays. Daily Shacharit anchors the day in prayer before the world's noise.": (
        "שחרית — תפילת הבוקר; שמע עד השעה השלישית; תפילין בחול."
    ),
    "Tachanun (Ashkenaz): On most weekdays Tachanun follows Shacharit Amidah — usually sitting with nefilat apayim. Mondays and Thursdays: longer Tachanun (Vehu Rachum and the full penitential section). Other weekdays: shorter form. Omitted on Rosh Chodesh, festivals, Chanukah, and other days listed in your siddur.": (
        "תחנון (אשכנז): בימי חול אחר עמידת שחרית — לרוב בישיבה עם נפילת apayim. שני וחמישי: ארוך (והוא רחום); אחרים: קצר. חסר בר\"ח, חגים וחנוכה."
    ),
    "Tachanun (Sephardi): On weekdays after Shacharit Amidah — Vidui, the Thirteen Attributes of Mercy, then Psalm 25 (LeDavid). Sephardic poskim including Rav Ovadia Yosef (Yechaveh Daat 6:7) and the Ben Ish Chai rule against physical nefilat apayim — recite sitting upright. Longer form on Mondays and Thursdays; shorter on other weekdays. Omitted on Rosh Chodesh, festivals, Chanukah, and other days in your siddur (Peninei Halakha, Prayer 03-17-05).": (
        "תחנון (ספרדי): אחר עמידת שחרית — וידוי, י\"ג מידות, תהילים כ\"ה. פוסקים ספרדיים כולל הרב עובדיה יוסף — בלי נפילת apayim פיזית; ישיבה זקופה. ארוך בשני וחמישי. חסר בר\"ח, חגים וחנוכה."
    ),
    "Tzitzit are fringes on four-cornered garments reminding us of all 613 mitzvot. The tallit katan is worn daily — generally over or under the shirt, depending on community custom (many Chassidim, Sephardim, and followers of the Arizal wear it over the shirt, under a vest or jacket); the tallit gadol at Shacharit. Strings must be kosher and tied correctly. Looking at tzitzit during Shema fulfills \"you shall see them.\" Women in some communities wear tzitzit; ask your rav.": (
        "ציצית על בגד ארבע כנפות — זוכרים 613 מצוות. טלית קטן יומי — מעל או מתחת לחולצה לפי מנהג; טלית גדול בשחרית. חוטים כשרים וקשורים נכון. מביטים בציצית בשמע. נשים בקהילות מסוימות — שאלו רב."
    ),
    "tzitzit — Tzitzit are fringes on four-cornered garments reminding us of all 613 mitzvot. The tallit katan is worn daily — generally over or under the shirt, depending on community custom (many Chassidim, Sephardim, and followers of the Arizal wear it over the shirt, under a vest or jacket); the tallit gadol at Shacharit. Strings must be kosher and tied correctly. Looking at tzitzit during Shema fulfills \"you shall see them.\" Women in some communities wear tzitzit; ask your rav.": (
        "ציצית — טלית קטן יומי; טלית גדול בשחרית; שאלו רב."
    ),
}
