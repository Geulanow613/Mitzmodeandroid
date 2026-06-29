#!/usr/bin/env python3
"""Hand-reviewed Hebrew overrides for Argos glossary/paragraph disasters."""

from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / "data" / "translation-catalog" / "shards" / "hebrew_fixes.json"

# English key -> Hebrew translation
FIXES: dict[str, str] = {
    "Rosh Chodesh begins the Hebrew month — semi-holiday with Hallel (half), Musaf, Yaaleh V'yavo, and often reduced work for women per custom. Remove tefillin before Musaf — wearing tefillin during Rosh Chodesh Musaf is forbidden. In ancient times the new moon was proclaimed from testimony. Today the calendar is fixed. It is a monthly reset — plan Torah goals and charity.": (
        "ראש חודש פותח את החודש העברי — חצי חג עם הלל (חצי), מוסף, יעלה ויבוא, ולעתים קרובות הפחתת עבודה לנשים לפי מנהג. להסיר תפילין לפני מוסף — אסור ללבוש תפילין במוסף של ראש חודש. בימי קדם הוכרז הירח החדש מעדות; היום לוח השנה קבוע. זהו איפוס חודשי — תכננו יעדי תורה וצדקה."
    ),
    "Rosh Chodesh — Rosh Chodesh begins the Hebrew month — semi-holiday with Hallel (half), Musaf, Yaaleh V'yavo, and often reduced work for women per custom. Remove tefillin before Musaf — wearing tefillin during Rosh Chodesh Musaf is forbidden. In ancient times the new moon was proclaimed from testimony. Today the calendar is fixed. It is a monthly reset — plan Torah goals and charity.": (
        "ראש חודש — ראש חודש פותח את החודש העברי — חצי חג עם הלל (חצי), מוסף, יעלה ויבוא. להסיר תפילין לפני מוסף — אסור במוסף ראש חודש. איפוס חודשי — תכננו יעדי תורה וצדקה."
    ),
    "Rosh Chodesh (the New Month) is the first day of each Hebrew month, and in some months the 30th day of the previous month as well.\n\nSpecial observances include: Yaaleh V'Yavo in prayers and Grace After Meals, half Hallel, a Musaf (additional) prayer, and a special Torah reading.\n\nRosh Chodesh is a semi-holiday — certain fasting and eulogizing are restricted. There is a widespread custom for women to refrain from certain types of work as an extra mark of honor for the day.": (
        "ראש חודש הוא היום הראשון של כל חודש עברי, ובחודשים מסוימים גם היום ה-30 של החודש הקודם.\n\nמצוות מיוחדות: יעלה וייבו בתפילות ובברכת המזון, חצי הלל, תפילת מוסף וקריאה מיוחדת בתורה.\n\nראש חודש הוא חצי חג — מוגבלים צומות והספדים. מנהג נפוץ לנשים להימנע מסוגי עבודה מסוימים לכבוד היום."
    ),
    "A machzor is a prayer book for the Jewish festivals and High Holidays — Rosh Hashana, Yom Kippur, Pesach, Sukkot, and Shavuot. It includes texts and prayers not found in the daily siddur. For Rosh Hashana and Yom Kippur, a machzor is required to perform the proper prayers.": (
        "מחזור הוא ספר תפילה לחגים ולימים נוראים — ראש השנה, יום כיפור, פסח, סוכות ושבועות. כולל טקסטים ותפילות שאינם בסידור היומי. לראש השנה ויום כיפור נדרש מחזור לתפילה כהלכתה."
    ),
    "machzor — A machzor is a prayer book for the Jewish festivals and High Holidays — Rosh Hashana, Yom Kippur, Pesach, Sukkot, and Shavuot. It includes texts and prayers not found in the daily siddur. For Rosh Hashana and Yom Kippur, a machzor is required to perform the proper prayers.": (
        "מחזור — ספר תפילה לחגים ולימים נוראים; לראש השנה ויום כיפור נדרש מחזור."
    ),
    "A siddur (from seder, \"order\") is the Jewish prayer book with the fixed texts for daily and Shabbat services — blessings, Shema, Amidah, Birkat Hamazon, and more. Editions follow nusach (Ashkenaz, Sefard, Chabad, etc.), so words and order differ slightly. Your siddur is your map for davening.": (
        "סידור (מ«סדר») הוא ספר התפילה היהודי עם הטקסטים הקבועים לתפילות יומיות ושבת — ברכות, שמע, עמידה, ברכת המזון ועוד. מהדורות לפי נוסח (אשכנז, ספרד, חב\"ד וכו'), כך שהמילים והסדר שונים במקצת. הסידור הוא המפה שלכם לתפילה."
    ),
    "siddur — A siddur (from seder, \"order\") is the Jewish prayer book with the fixed texts for daily and Shabbat services — blessings, Shema, Amidah, Birkat Hamazon, and more. Editions follow nusach (Ashkenaz, Sefard, Chabad, etc.), so words and order differ slightly. Your siddur is your map for davening.": (
        "סידור — ספר התפילה היהודי ליום ולשבת; לפי נוסח (אשכנז, ספרד, חב\"ד). המפה שלכם לתפילה."
    ),
    "A minyan is ten Jewish men above bar mitzvah age (13) forming a quorum for public prayer. Certain prayers — including Kaddish, Barchu, and repetition of the Amidah — require a minyan. The Talmud stresses that communal prayer is especially useful for getting prayers accepted by Heaven; joining a minyan is considered the ideal way of prayer.": (
        "מניין הוא עשרה יהודים מעל גיל בר מצווה (13) המרכיבים מניין לתפילה ציבורית. תפילות מסוימות — כולל קדיש, ברכו וחזרת העמידה — דורשות מניין. התלמוד מדגיש שתפילה ציבורית מועילה במיוחד לקבלת תפילות; הצטרפות למניין היא הדרך האידיאלית."
    ),
    "minyan — A minyan is ten Jewish men above bar mitzvah age (13) forming a quorum for public prayer. Certain prayers — including Kaddish, Barchu, and repetition of the Amidah — require a minyan. The Talmud stresses that communal prayer is especially useful for getting prayers accepted by Heaven; joining a minyan is considered the ideal way of prayer.": (
        "מניין — עשרה יהודים מעל גיל בר מצווה למניין תפילה ציבורית; קדיש, ברכו וחזרת העמידה דורשים מניין."
    ),
    "A posek is a rabbi qualified to decide difficult halachic questions and write authoritative rulings. Your rav may consult poskim on novel cases. The term differs from a maggid or teacher who inspires but does not pasken. Major poskim include figures behind the Mishnah Berurah, Igrot Moshe, and contemporary halachic works your community follows.": (
        "פוסק הוא רב המוסמך להכריע בשאלות הלכתיות קשות ולכתוב פסקים מחייבים. הרב שלכם עשוי להתייעץ עם פוסקים במקרים חדשים. המונח שונה ממגיד או מורה שמעורר השראה אך אינו פוסק. פוסקים גדולים כוללים את מחברי משנה ברורה, אגרות משה וספרי הלכה עכשוויים שהקהילה שלכם נוהגת לפיהם."
    ),
    "posek — A posek is a rabbi qualified to decide difficult halachic questions and write authoritative rulings. Your rav may consult poskim on novel cases. The term differs from a maggid or teacher who inspires but does not pasken. Major poskim include figures behind the Mishnah Berurah, Igrot Moshe, and contemporary halachic works your community follows.": (
        "פוסק — רב המוסמך להכריע בשאלות הלכה ולכתוב פסקים; שונה ממגיד שאינו פוסק."
    ),
    "A mezuzah is the klaf — handwritten parchment with Shema and V'ahavta passages — affixed to doorposts of rooms used for dwelling. The case is protection for the scroll, not the mitzvah itself. Check scrolls by a sofer every few years. Touching the case and kissing the mezuzah when entering reminds you that G-d watches over the home.": (
        "מזוזה היא הקלף — קלף בכתב יד עם קטעי שמע וואהבתה — המוצמד למזוזות של חדרי מגורים. הבית הוא הגנה על המגילה, לא המצווה עצמה. בדקו קלפים אצל סופר כל כמה שנים. נגיעה בבית והנשיקה בכניסה מזכירות שה' שומר על הבית."
    ),
    "mezuzah — A mezuzah is the klaf — handwritten parchment with Shema and V'ahavta passages — affixed to doorposts of rooms used for dwelling. The case is protection for the scroll, not the mitzvah itself. Check scrolls by a sofer every few years. Touching the case and kissing the mezuzah when entering reminds you that G-d watches over the home.": (
        "מזוזה — הקלף בכתב יד עם שמע וואהבתה על מזוזות; בדקו אצל סופר כל כמה שנים."
    ),
    "A bracha is a structured blessing, usually opening with \"Baruch Atah Hashem…\" It is how we acknowledge G-d as the Source of all blessing before eating, before mitzvot, and at holy moments. Different foods and mitzvot have specific brachot; saying the wrong one or none when required is a halachic issue. Brachot also train gratitude — pausing to recognize that what we enjoy comes from Above.": (
        "ברכה היא נוסח מובנה, בדרך כלל פותח ב«ברוך אתה ה'…» — הכרה בה' כמקור כל ברכה לפני אכילה, מצוות ורגעים קדושים. למזונות ומצוות שונות ברכות מיוחדות; ברכה שגויה או היעדר ברכה כשחייבים — בעיה הלכתית. ברכות מאמנות הכרת תודה."
    ),
    "Bracha — A bracha is a structured blessing, usually opening with \"Baruch Atah Hashem…\" It is how we acknowledge G-d as the Source of all blessing before eating, before mitzvot, and at holy moments. Different foods and mitzvot have specific brachot; saying the wrong one or none when required is a halachic issue. Brachot also train gratitude — pausing to recognize that what we enjoy comes from Above.": (
        "ברכה — נוסח מובנה המתחיל ב«ברוך אתה ה'…»; למזונות ומצוות ברכות מיוחדות."
    ),
    "A minhag is a binding community or family custom rooted in Jewish life — not merely \"what people happen to do.\" Minhag can shape prayer text (nusach), mourning practices, Pesach stringencies, and holiday joy. When a minhag is established with rabbinic approval in a community, it often carries nearly the weight of law for that community. If you are new or between communities, ask your rav which minhagim to follow.": (
        "מנהג הוא מנהג קהילתי או משפחתי מחייב בשורש חיי יהדות — לא רק «מה שאנשים עושים». מנהג יכול לעצב נוסח תפילה, אבלות, חומרות פסח ושמחת חג. מנהג מבוסס ברבנות נושא לעתים כמעט משקל של חוק בקהילה. חדשים או בין קהילות — שאלו את הרב אילו מנהגים לנהוג."
    ),
    "Minhag — A minhag is a binding community or family custom rooted in Jewish life — not merely \"what people happen to do.\" Minhag can shape prayer text (nusach), mourning practices, Pesach stringencies, and holiday joy. When a minhag is established with rabbinic approval in a community, it often carries nearly the weight of law for that community. If you are new or between communities, ask your rav which minhagim to follow.": (
        "מנהג — מנהג קהילתי או משפחתי מחייב; שאלו את הרב אילו מנהגים לנהוג."
    ),
    "Bishul is cooking on Shabbat — heating food until yad soledet bo (hand recoils). Fully cooked dry food may be reheated in some cases; liquids are stricter. A blech or slow cooker set before Shabbat keeps food warm. Yom Tov allows cooking for that day — different rules entirely.": (
        "בישול הוא בישול בשבת — חימום מזון עד יד סולדת בו. מזון יבש מבושל לגמרי עשוי להתחמם במקרים מסוימים; נוזלים מחמירים יותר. פלטה או בישול איטי מוקדם לפני שבת שומרים על חום. ביום טוב מותר לבשל לאותו יום — חוקים שונים לגמרי."
    ),
    "Bishul — Bishul is cooking on Shabbat — heating food until yad soledet bo (hand recoils). Fully cooked dry food may be reheated in some cases; liquids are stricter. A blech or slow cooker set before Shabbat keeps food warm. Yom Tov allows cooking for that day — different rules entirely.": (
        "בישול — בישול בשבת עד יד סולדת בו; פלטה לפני שבת. ביום טוב חוקים שונים."
    ),
    "Maror is bitter herb at the Seder — horseradish root or romaine — reminding the bitterness of slavery. A kezayit is required at the Seder. Charoset's sweetness is dipped with maror in Korech (Hillel's sandwich). Prepare enough fresh maror; grated horseradish loses strength quickly.": (
        "מרור הוא ירק מר בסדר — שורש חזרת או חסה — מזכיר מרירות השעבוד. כזית נדרש בסדר. מתיקות החרוסת נטבלת עם מרור בכורך (כריך הלל). הכינו מרור טרי; חזרת מגורדת מאבדת חוזק במהירות."
    ),
    "maror — Maror is bitter herb at the Seder — horseradish root or romaine — reminding the bitterness of slavery. A kezayit is required at the Seder. Charoset's sweetness is dipped with maror in Korech (Hillel's sandwich). Prepare enough fresh maror; grated horseradish loses strength quickly.": (
        "מרור — ירק מר בסדר; כזית נדרש; עם חרוסת בכורך."
    ),
    "Sukkot is seven days dwelling in the sukkah and waving the Four Species — joy after the Days of Awe. It recalls desert clouds of glory. Rain may exempt from sitting. Shemini Atzeret follows immediately — a separate holiday of intimacy with G-d after the public festival.": (
        "סוכות שבעה ימים בישיבה בסוכה ונענוע ארבעת המינים — שמחה אחרי ימי ה'. מזכיר ענני הכבוד במדבר. גשם עשוי לפטור מישיבה. שמיני עצרת מיד אחריו — חג נפרד של קרבה לה' אחרי החג הציבורי."
    ),
    "Sukkot — Sukkot is seven days dwelling in the sukkah and waving the Four Species — joy after the Days of Awe. It recalls desert clouds of glory. Rain may exempt from sitting. Shemini Atzeret follows immediately — a separate holiday of intimacy with G-d after the public festival.": (
        "סוכות — שבעה ימים בסוכה וארבעת המינים; שמיני עצרת מיד אחריו."
    ),
    "Shavuot marks Matan Torah — receiving the Torah at Sinai, seven weeks after Pesach. Unlike other festivals, candles and Kiddush may not begin until full nightfall (tzeit). Dairy meals, all-night learning, and reading Ruth are customs. It is one day in Israel, two in the Diaspora.": (
        "שבועות מציין מתן תורה בסיני, שבעה שבועות אחרי פסח. בניגוד לחגים אחרים, נרות וקידוש לא מתחילים עד צאת הכוכבים המלאה. ארוחות חלב, לימוד כל הלילה וקריאת רות — מנהגים. יום אחד בישראל, שניים בגולה."
    ),
    "Shavuot — Shavuot marks Matan Torah — receiving the Torah at Sinai, seven weeks after Pesach. Unlike other festivals, candles and Kiddush may not begin until full nightfall (tzeit). Dairy meals, all-night learning, and reading Ruth are customs. It is one day in Israel, two in the Diaspora.": (
        "שבועות — מתן תורה; נרות וקידוש רק אחרי צאת הכוכבים; יום אחד בישראל, שניים בגולה."
    ),
    "Cholent (chamin, hamin) is Shabbat stew left on a blech or slow cooker from before Shabbat — hot food without cooking on Shabbat. Every community has recipes: beans, meat, potatoes. It makes oneg Shabbat practical. Start it early Friday so it has time to cook partway before Shabbat.": (
        "צ'ולנט (chamin, hamin) הוא תבשיל שבת על פלטה או בישול איטי מלפני שבת — אוכל חם בלי בישול בשבת. לכל קהילה מתכונים: שעועית, בשר, תפוחי אדמה. מקל על עונג שבת. התחילו מוקדם ביום שישי כדי שיבשל חלקית לפני שבת."
    ),
    "cholent — Cholent (chamin, hamin) is Shabbat stew left on a blech or slow cooker from before Shabbat — hot food without cooking on Shabbat. Every community has recipes: beans, meat, potatoes. It makes oneg Shabbat practical. Start it early Friday so it has time to cook partway before Shabbat.": (
        "צ'ולנט — תבשיל שבת על פלטה מלפני שבת; התחילו מוקדם ביום שישי."
    ),
    "Hotza'ah is carrying from private to public domain (or four amot in public) on Shabbat. Without an eruv, keys in a pin-on pouch, strollers in some areas, and wheeling objects outdoors may be issues. An eruv symbolically encloses a community — check if yours is up this week.": (
        "הוצאה היא הוצאה מרשות היחיד לרשות הרבים (או ארבע אמות ברשות הרבים) בשבת. בלי עירוב, מפתחות בנרתיק ננעץ, עגלות באזורים מסוימים והגלגת חפצים בחוץ — בעיות. עירוב מקיף סמלית קהילה — בדקו אם העירוב שלכם תקין השבוע."
    ),
    "Hotza'ah — Hotza'ah is carrying from private to public domain (or four amot in public) on Shabbat. Without an eruv, keys in a pin-on pouch, strollers in some areas, and wheeling objects outdoors may be issues. An eruv symbolically encloses a community — check if yours is up this week.": (
        "הוצאה — הוצאה מרשות היחיד לרשות הרבים בשבת; בדקו עירוב."
    ),
    "An eruv (especially eruv chatzerot or city eruv) is a halachic enclosure letting Jews carry within a defined area on Shabbat. It requires kosher boundaries and community upkeep. When the eruv is down, carrying keys and pushing strollers outdoors may be forbidden — shuls often text status Friday. It does not permit driving or phones.": (
        "עירוב (במיוחד עירוב חצרות או עירוב עיר) הוא מחיצה הלכתית המאפשרת לשאת באזור מוגדר בשבת. דורש גבולות כשרים ותחזוקה קהילתית. כשהעירוב פגום, נשיאת מפתחות ודחיפת עגלות בחוץ עשויה להיות אסורה — בתי כנסת מודיעים לעתים קרובות בסטטוס ביום שישי. לא מאפשר נהיגה או טלפונים."
    ),
    "eruv — An eruv (especially eruv chatzerot or city eruv) is a halachic enclosure letting Jews carry within a defined area on Shabbat. It requires kosher boundaries and community upkeep. When the eruv is down, carrying keys and pushing strollers outdoors may be forbidden — shuls often text status Friday. It does not permit driving or phones.": (
        "עירוב — מחיצה הלכתית לשאת בשבת; בדקו סטטוס ביום שישי."
    ),
    "Mincha is the afternoon service, ideally before sunset. On weekdays it is short; on Shabbat and Yom Tov it includes Torah reading (often) and Musaf on festivals. Friday Mincha is special — it enters Shabbat. Missing Mincha requires tashlumin at Maariv when possible. Working people often pray Mincha at shul or a quiet office corner.": (
        "מנחה היא תפילת אחר הצהריים, באידיאל לפני השקיעה. בימי חול קצרה; בשבת ויום טוב כוללת קריאת תורה (לעתים) ומוסף בחגים. מנחת שישי מיוחדת — נכנסת לשבת. היעדר מנחה דורש תשלומין במעריב כשאפשר. רבים מתפללים מנחה בבית כנסת או בפינה שקטה במשרד."
    ),
    "Mincha — Mincha is the afternoon service, ideally before sunset. On weekdays it is short; on Shabbat and Yom Tov it includes Torah reading (often) and Musaf on festivals. Friday Mincha is special — it enters Shabbat. Missing Mincha requires tashlumin at Maariv when possible. Working people often pray Mincha at shul or a quiet office corner.": (
        "מנחה — תפילת אחר הצהריים; מנחת שישי נכנסת לשבת."
    ),
    "Al HaNissim (\"for the miracles\") is added to the Amidah and Birkat Hamazon on Chanukah and Purim. It summarizes the salvation — the Maccabees or Esther/Mordecai — and thanks G-d. On Purim Meshulash (Jerusalem when 15 Adar is Shabbat), Al HaNissim is recited only on Shabbat — not on Friday or Sunday when other Purim mitzvot occur.": (
        "על הניסים («על הנסים») נוסף לעמידה ולברכת המזון בחנוכה ופורים. מסכם את הישועה — המכבים או אסתר ומרדכי — ומודה לה'. בפורים משולש (ירושלים כשט\"ו באדר בשבת), על הניסים נאמר רק בשבת — לא ביום שישי או ראשון כשמצוות פורים אחרות."
    ),
    "Al HaNissim — Al HaNissim (\"for the miracles\") is added to the Amidah and Birkat Hamazon on Chanukah and Purim. It summarizes the salvation — the Maccabees or Esther/Mordecai — and thanks G-d. On Purim Meshulash (Jerusalem when 15 Adar is Shabbat), Al HaNissim is recited only on Shabbat — not on Friday or Sunday when other Purim mitzvot occur.": (
        "על הניסים — נוסף לעמידה וברכת המזון בחנוכה ופורים; בפורים משולש רק בשבת."
    ),
    "Purim Meshulash is the rare Jerusalem schedule when Shushan Purim (15 Adar) falls on Shabbat. Megillah and matanot l'evyonim move to Friday; mishloach manot and the seudah move to Sunday. Shabbat carries communal Purim obligations — special Torah reading (Vayavo Amalek), Haftarah, and Al HaNissim in Amidah and bentching (not on Friday or Sunday).": (
        "פורים משולש הוא לוח הזמנים הנדיר בירושלים כששושן פורים (ט\"ו באדר) נופל בשבת. מגילה ומתנות לאביונים עוברים ליום שישי; משלוח מנות והסעודה ליום ראשון. שבת נושאת חובות פורים ציבוריות — קריאת תורה מיוחדת (ויבוא עמלק), הפטרה ועל הניסים בעמידה ובברכת המזון (לא ביום שישי או ראשון)."
    ),
    "Purim Meshulash — Purim Meshulash is the rare Jerusalem schedule when Shushan Purim (15 Adar) falls on Shabbat. Megillah and matanot l'evyonim move to Friday; mishloach manot and the seudah move to Sunday. Shabbat carries communal Purim obligations — special Torah reading (Vayavo Amalek), Haftarah, and Al HaNissim in Amidah and bentching (not on Friday or Sunday).": (
        "פורים משולש — ירושלים כשט\"ו באדר בשבת; מגילה ומתנות ביום שישי, משלוח וסעודה בראשון."
    ),
    "Shushan Purim is 15 Adar in walled cities that had walls in Joshua's time (Jerusalem is primary today). When 15 Adar is Shabbat, Jerusalem observes Purim Meshulash — Megillah and matanot Friday, mishloach and seudah Sunday. Elsewhere Purim is 14 Adar. Know your city's calendar.": (
        "שושן פורים ט\"ו באדר בערים מוקפות חומה מימי יהושע (ירושלים העיקרית היום). כשט\"ו באדר בשבת, ירושלים שומרת פורים משולש — מגילה ומתנות ביום שישי, משלוח וסעודה בראשון. במקומות אחרים פורים י\"ד באדר. הכירו את לוח העיר שלכם."
    ),
    "Shushan Purim — Shushan Purim is 15 Adar in walled cities that had walls in Joshua's time (Jerusalem is primary today). When 15 Adar is Shabbat, Jerusalem observes Purim Meshulash — Megillah and matanot Friday, mishloach and seudah Sunday. Elsewhere Purim is 14 Adar. Know your city's calendar.": (
        "שושן פורים — ט\"ו באדר בערים מוקפות; פורים משולש כשחל בשבת."
    ),
    "Chanukah is eight nights lighting menorah for the oil miracle and victory over Greek oppression. Al HaNissim is added in prayer. On Friday, light before Shabbat candles using long candles or extra oil — standard small Chanukah candles burn out before nightfall and invalidate the mitzvah. Gifts are American custom, not core mitzvah. Place menorah for pirsumei nisa. Work is permitted; the focus is light and thanks.": (
        "חנוכה שמונה לילות הדלקת מנורה לנס השמן ולניצחון על הדיכוי היווני. על הניסים מוסיפים בתפילה. ביום שישי, הדליקו לפני נרות שבת בנרות ארוכים או שמן נוסף — נרות חנוכה קטנים נשרפים לפני החשיכה ופוסלים את המצווה. מתנות מנהג אמריקאי, לא מצווה מרכזית. הניחו מנורה לפרסומי ניסא. עבודה מותרת; המיקוד באור והודיה."
    ),
    "Besamim are fragrant spices smelled during Havdalah Saturday night — comforting the soul as the extra neshama yeteira departs back into weekday life. The blessing is Borei minei besamim. Cloves, myrtle, or sweet spices are common. Yaknehaz omits besamim when Shabbat flows into Yom Tov — the joy of the festival itself comforts the soul, so spices are halachically unnecessary.": (
        "בשמים הם תבלינים ריחניים בהבדלה בליל שבת — מנחמים את הנפש כשהנשמה יתירה חוזרת לחיי יום חול. הברכה: בורא מיני בשמים. ציפורן, הדס או תבלינים מתוקים נפוצים. יקנה\"ז מדלג על בשמים כששבת זורמת ליום טוב — שמחת החג עצמה מנחמת, ולכן בשמים מיותרים הלכתית."
    ),
    "besamim — Besamim are fragrant spices smelled during Havdalah Saturday night — comforting the soul as the extra neshama yeteira departs back into weekday life. The blessing is Borei minei besamim. Cloves, myrtle, or sweet spices are common. Yaknehaz omits besamim when Shabbat flows into Yom Tov — the joy of the festival itself comforts the soul, so spices are halachically unnecessary.": (
        "בשמים — תבלינים ריחניים בהבדלה; יקנה\"ז מדלג כששבת זורמת ליום טוב."
    ),
    "Chamar medina (\"drink of the land\") is a prestigious local beverage — such as coffee, tea, or kosher liquor — that some authorities allow for daytime Kiddush or Havdalah when wine or grape juice is unavailable or difficult. It can never be used for Friday night Shabbat Kiddush, which strictly requires wine, grape juice, or bread (Shulchan Arukh O.C. 272).": (
        "חמר מדינה («משקה הארץ») הוא משקה מקומי יוקרתי — קפה, תה או משקאות כשרים — שחלק מהרשויות מתירים לקידוש יום או הבדלה ביום כשיין או מיץ ענבים אינם זמינים. לעולם לא לקידוש שבת ליל שישי, שדורש יין, מיץ ענבים או לחם (שולחן ערוך או\"ח רעב)."
    ),
    "chamar medina — Chamar medina (\"drink of the land\") is a prestigious local beverage — such as coffee, tea, or kosher liquor — that some authorities allow for daytime Kiddush or Havdalah when wine or grape juice is unavailable or difficult. It can never be used for Friday night Shabbat Kiddush, which strictly requires wine, grape juice, or bread (Shulchan Arukh O.C. 272).": (
        "חמר מדינה — משקה מקומי יוקרתי; לא לקידוש ליל שבת."
    ),
    "Hallel is a set of psalms (113–118, with variations) praising G-d for salvation. Full Hallel is recited on major festivals; Half Hallel (Partial Hallel) on Rosh Chodesh and the last 6 days of Pesach. Hallel is not recited on Rosh Hashana or Yom Kippur (Arachin 10b). It is said standing, with joy.": (
        "הלל הוא קבוצת מזמורים (קי\"ג–קי\"ח, עם וריאציות) המשבחים את ה' על הישועה. הלל מלא בחגים גדולים; חצי הלל בראש חודש וששת הימים האחרונים של פסח. לא אומרים הלל בראש השנה או יום כיפור (ערכין י' ב). אומרים מעומד, בשמחה."
    ),
    "Rambam is Rabbi Moses Maimonides (1138–1204) — physician, philosopher, and halachic giant. His Mishneh Torah codifies halacha in clear Hebrew; his Thirteen Principles summarize Jewish faith. Many beginners meet him through daily halacha summaries or his laws of teshuvah, charity, and Torah study.": (
        "הרמב\"ם, רבי משה בן מימון (ה'תשצ\"ח–ה'תשס\"ד) — רופא, פילוסוף וענק הלכה. משנה תורה שלו מקודדת הלכה בעברית ברורה; שלוש עשרה העקרונות מסכמים את האמונה. מתחילים רבים פוגשים אותו בסיכומי הלכה יומיים או בחוקי תשובה, צדקה ולימוד תורה."
    ),
    "Rambam — Rambam is Rabbi Moses Maimonides (1138–1204) — physician, philosopher, and halachic giant. His Mishneh Torah codifies halacha in clear Hebrew; his Thirteen Principles summarize Jewish faith. Many beginners meet him through daily halacha summaries or his laws of teshuvah, charity, and Torah study.": (
        "הרמב\"ם — רבי משה בן מימון; משנה תורה ושלוש עשרה העקרונות."
    ),
    "Chassidut — Chassidut (Chasidic philosophy) teaches serving G-d with joy, sincerity, and awareness that divine sparks fill all of life. Founded by the Baal Shem Tov, it spread through courts like Chabad, Breslov, and Satmar. Tanya is a central Chassidut text for daily study.": (
        "חסידות — פילוסופיה חסידית של עבודת ה' בשמחה, כנות ומודעות שניצוצות אלוקיים ממלאים את החיים. נוסדה על ידי הבעל שם טוב והתפשטה בחסידויות כמו חב\"ד, ברסלב וסאטמר. התניא טקסט מרכזי ללימוד יומי."
    ),
    "Kitzur Shulchan Arukh — The Kitzur Shulchan Arukh is a condensed summary of daily halacha — prayer, Shabbat, kashrut, festivals, and family life — written by Rabbi Shlomo Ganzfried. It is a practical first halacha book for beginners and a quick reference for experienced Jews.": (
        "קיצור שולחן ערוך — סיכום הלכה יומית: תפילה, שבת, כשרות, חגים וחיי משפחה, מאת רבי שלמה גנזפריד. ספר הלכה מעשי ראשון למתחילים ועזר מהיר ליהודים מנוסים."
    ),
    "Dayeinu — Dayeinu (\"it would have been enough\") is the Seder song listing gifts from the Exodus — each step alone would have justified gratitude. Singing it trains children and adults to notice cumulative kindness from G-d. It appears after the Maggid narrative and before Hallel.": (
        "דיינו («די היה לנו») הוא שיר הסדר המונה מתנות מיציאת מצרים — כל שלב לבד היה מצדיק הכרת תודה. שירה מאמנת ילדים ומבוגרים להבחין בחסד מצטבר מה'. מופיע אחרי מגיד ולפני הלל."
    ),
    "Mussar — Mussar is the Jewish discipline of ethical and character development — refining anger, arrogance, laziness, and speech through study and practice. Classic works include Mesillat Yesharim and Ale Shur. It complements halacha (what to do) with work on who you are becoming.": (
        "מוסר — משמעת יהודית של התפתחות אתית ואופי — שיפור כעס, יהירות, עצלות ודיבור בלימוד ומעשה. יצירות קלאסיות: מסילת ישרים ועלי שור. משלים הלכה (מה לעשות) בעבודה על מי אתם הופכים."
    ),
    "Gan Eden — Gan Eden (Garden of Eden) in Jewish thought often means the soul's reward after death — closeness to G-d, not a physical vacation. It pairs with Olam HaBa. The original Garden in Bereishit is the starting point of human story; tradition teaches souls return to a heavenly Gan Eden.": (
        "גן עדן — במחשבה יהודית לעתים פרס הנשמה לאחר המוות — קרבה לה', לא חופשה פיזית. מזווג עם עולם הבא. הגן המקורי בבראשית הוא נקודת המוצא; המסורת מלמדת שנשמות שבות לגן עדן שמימי."
    ),
    "Hoshanot — Hoshanot are prayers of salvation recited while holding the Four Species and circling the synagogue sanctuary platform (Bimah) during Sukkot (except Shabbat). Hoshana Raba (seventh day) has extra circuits. It is the last push of repentance after Yom Kippur — joy and urgency together.": (
        "הושענות — תפילות ישועה בנענוע ארבעת המינים והקפות סביב הבימה בסוכות (מלבד שבת). הושענא רבה (יום שביעי) עם הקפות נוספות. הדחיפה האחרונה של תשובה אחרי יום כיפור — שמחה ודחיפות יחד."
    ),
    "Purim — Purim celebrates salvation in Shushan — Megillah, matanot la'evyonim, mishloach manot, and seudah. Costumes and joy are mitzvah, not frivolity alone. Hearing every word of the Megillah is essential. Drunkenness per \"until you cannot tell\" is bounded by safety and halacha — ask your rav.": (
        "פורים חוגג ישועה בשושן — מגילה, מתנות לאביונים, משלוח מנות וסעודה. תחפושות ושמחה מצווה, לא שטות בלבד. לשמוע כל מילה במגילה חיוני. שיכרות «עד שלא יודע» מוגבל בבטיחות ובהלכה — שאלו את הרב."
    ),
    "Purim celebrates salvation in Shushan — Megillah, matanot la'evyonim, mishloach manot, and seudah. Costumes and joy are mitzvah, not frivolity alone. Hearing every word of the Megillah is essential. Drunkenness per \"until you cannot tell\" is bounded by safety and halacha — ask your rav.": (
        "פורים חוגג ישועה בשושן — מגילה, מתנות לאביונים, משלוח מנות וסעודה. תחפושות ושמחה מצווה. לשמוע כל מילה במגילה חיוני."
    ),
    "Paragraph in Amidah and bentching on Rosh Chodesh and festivals. Forgot in Amidah: insert in Retzei if still there; after concluding Retzei return to the beginning of Retzei; after final Yihiyu L'ratzon repeat only that Amidah. Rosh Chodesh Maariv only: no repeat after Retzei (SA O.C. 422:1).": (
        "פסקה בעמידה ובברכת המזון בראש חודש ובחגים. נשכח בעמידה: הכניסו בִּרְצֵה אם עדיין שם; אחרי סיום רצה חזרו לתחילת רצה; אחרי יהיו לרצון חזרו רק על אותה עמידה. מעריב ראש חודש בלבד: לא חוזרים אחרי רצה (שו\"ע או\"ח תכב:א)."
    ),
    "Yaaleh V'yavo — Paragraph in Amidah and bentching on Rosh Chodesh and festivals. Forgot in Amidah: insert in Retzei if still there; after concluding Retzei return to the beginning of Retzei; after final Yihiyu L'ratzon repeat only that Amidah. Rosh Chodesh Maariv only: no repeat after Retzei (SA O.C. 422:1).": (
        "יעלה וייבו — פסקה בעמידה ובברכת המזון בראש חודש ובחגים; כללי תיקון לפי שו\"ע או\"ח תכב."
    ),
    "Psak is a halachic ruling — the answer a qualified posek gives for your real case, not a theoretical debate. Judaism has legitimate range between authorities; your job is to follow your rav's psak consistently rather than shop around for leniencies.": (
        "פסק הוא הכרעה הלכתית — התשובה שפוסק מוסמך נותן למקרה האמיתי שלכם, לא לוויכוח תיאורטי. ביהדות יש טווח לגיטימי בין רשויות; תפקידכם לנהוג לפי פסק הרב בעקביות ולא לחפש קולות."
    ),
    "psak — Psak is a halachic ruling — the answer a qualified posek gives for your real case, not a theoretical debate. Judaism has legitimate range between authorities; your job is to follow your rav's psak consistently rather than shop around for leniencies.": (
        "פסק — הכרעה הלכתית של פוסק מוסמך למקרה האמיתי; נהגו לפי הרב בעקביות."
    ),
    "Chatzos is halachic midnight or midday — the midpoint of the night or day. Chatzos halayla matters for saying Modeh Ani, starting morning blessings, and some fast practices. Chatzos hayom is the midpoint of daylight. It moves with sunrise and sunset, not with 12:00 on the clock.": (
        "חצות היא חצות הלכתית לילה או יום — אמצע הלילה או היום. חצות הלילה רלוונטית למודה אני, ברכות בוקר ומנהגי צום. חצות היום — אמצע אור היום. זז עם זריחה ושקיעה, לא עם 12:00 על השעון."
    ),
    "halachic chatzos — Chatzos is halachic midnight or midday — the midpoint of the night or day. Chatzos halayla matters for saying Modeh Ani, starting morning blessings, and some fast practices. Chatzos hayom is the midpoint of daylight. It moves with sunrise and sunset, not with 12:00 on the clock.": (
        "חצות הלכתית — אמצע הלילה או היום; חצות הלילה למודה אני וברכות בוקר."
    ),
    "Yom Yerushalayim marks the reunification of Jerusalem in 1967, observed on 28 Iyar.\n\nMany communities recite Hallel and hold festive gatherings. As with Yom Ha'atzmaut, customs and halachic rulings vary — consult your rav.": (
        "יום ירושלים מציין את איחוד ירושלים ב-1967, בכ\"ח באייר.\n\nקהילות רבות אומרות הלל ומקיימות אסיפות חגיגיות. כמו יום העצמאות, מנהגים ופסקים משתנים — התייעצו עם הרב."
    ),
    "Nefesh is the 'animal' part of the soul closest to physical life — appetite, hunger, bodily drives. Neshama is the higher breath of life G-d blew into Adam. Ruach is the emotional/spiritual middle layer. Kabbalah maps how they interact in divine service.": (
        "נפש — החלק «החייתי» של הנשמה הקרוב לחיים גשמיים — תיאבון, רעב ודחפים גופניים. נשמה — נשימת החיים העליונה. רוח — השכבה הרגשית/רוחנית. הקבלה ממפה את האינטראקציה בעבודת ה'."
    ),
    "nefesh — Nefesh is the 'animal' part of the soul closest to physical life — appetite, hunger, bodily drives. Neshama is the higher breath of life G-d blew into Adam. Ruach is the emotional/spiritual middle layer. Kabbalah maps how they interact in divine service.": (
        "נפש — החלק הקרוב לחיים גשמיים; נשמה ורוח — שכבות גבוהות יותר."
    ),
    "A keli is a vessel, utensil, or tool. It generally refers to any manufactured object used for a purpose — pots, cups, knives. In halacha, kelim have statuses (meat, dairy, pareve) and may require tevilah when bought from a non-Jew. Broken kelim may lose their status.": (
        "כלי הוא כלי, כלי מטבח או כלי עבודה — כל חפץ מיוצר לשימוש: סירים, כוסות, סכינים. בהלכה לכלים מעמד (בשרי, חלבי, פרווה) ועשויה להידרש טבילה בקנייה מגוי. כלים שבורים עשויים לאבד מעמד."
    ),
    "Keli — A keli is a vessel, utensil, or tool. It generally refers to any manufactured object used for a purpose — pots, cups, knives. In halacha, kelim have statuses (meat, dairy, pareve) and may require tevilah when bought from a non-Jew. Broken kelim may lose their status.": (
        "כלי — כלי מטבח או עבודה; בהלכה מעמד בשרי/חלבי/פרווה וטבילה."
    ),
    "zimun — Zimun is inviting others to bentch together when three or more men (per common minhag) ate bread as a group. The leader says \"let us bless\" and others answer. It turns private thanks into communal praise. Women's zimun follows separate customs — ask your rav.": (
        "זימון — הזמנת אחרים לברך יחד בברכת המזון כששלושה או יותר (לפי מנהג) אכלו לחם כקבוצה. המנהיג אומר «נברך» והאחרים עונים. הופך הודיה פרטית לשבח ציבורי. זימון נשים לפי מנהגים נפרדים — שאלו את הרב."
    ),
    "treif — Treif means non-kosher — originally torn flesh of improperly slaughtered animals, today any non-kosher food.": (
        "טרף — לא כשר; במקור נבלה של בהמה שלא נשחטה כהלכה, היום כל מזון לא כשר."
    ),
    "shul — Shul (synagogue) is the community house of prayer, Torah, and chesed. Beyond services, it hosts classes, youth programs, and lifecycle events.": (
        "בית כנסת — בית התפילה, התורה והחסד של הקהילה; מעבר לתפילות — שיעורים, נוער ואירועי חיים."
    ),
    "pshat — Pshat is the plain, straightforward meaning of a Torah verse — what the text says on the surface before deeper layers.": (
        "פשט — המשמעות הפשוטה והישירה של פסוק בתורה — מה הכתוב אומר בשטח לפני שכבות עומק."
    ),
    "kohen — priest descended from Aaron. They perform the service in the Temple when it stands.": (
        "כהן — יורש אהרן; משרתים במקדש כשהוא עומד."
    ),
    "pareve — neutral foods (neither meat nor dairy) such as fish, eggs, and produce.": (
        "פרווה — מזונות ניטרליים (לא בשר ולא חלב) כמו דג, ביצים וירקות."
    ),
    "Half Hallel — shortened Hallel psalms on Rosh Chodesh and after the first Yom Tov of Pesach.": (
        "חצי הלל — מזמורי הלל מקוצרים בראש חודש ואחרי יום טוב ראשון של פסח."
    ),
    "seudat mitzvah — festive meal tied to a mitzvah (e.g. brit, siyum, wedding).": (
        "סעודת מצווה — ארוחה חגיגית הקשורה למצווה (ברית, סיום, חתונה וכו')."
    ),
    "lulav — palm branch waved with the etrog, willows, and myrtle branches on Sukkot.": (
        "לולב — ענף דקל שננענע עם אתרוג, ערבות והדסים בסוכות."
    ),
    "leining — Leining is chanting the Torah portion from the scroll with precise trope.": (
        "ליינינג — קריאת פרשת השבוע מספר התורה בטעמים מדויקים."
    ),
    "tallit gadol — large prayer shawl with tzitzit worn during Shacharit/Musaf and all day on Yom Kippur.": (
        "טלית גדול — טלית עם ציצית הנילבש בשחרית/מוסף וכל היום ביום כיפור."
    ),
    "tallit katan — small four-cornered garment with tzitzit, worn generally over or under clothing.": (
        "טלית קטן — בגד קטן בארבע כנפות עם ציצית, נילבש מעל או מתחת לבגדים."
    ),
    "siyum — completion of a Torah text such as a tractate of the Talmud, often with a festive meal.": (
        "סיום — השלמת קטע בתורה כמו מסכת בתלמוד, לעתים עם סעודה חגיגית."
    ),
    "simanim — Simanim are symbolic Rosh Hashana foods — apple in honey, pomegranate, fish head, dates — each with a pun blessing.": (
        "סימנים — מאכלי סימן לראש השנה — תפוח בדבש, רימון, ראש דג, תמרים — כל אחד עם ברכת משחק מילים."
    ),
    "yirat Shamayim — fearing of Heaven; living with awareness that G-d is present.": (
        "יראת שמים — חיים במודעות להיות ה' נוכח."
    ),
    "walk in G-d's ways — imitating divine kindness (clothing the needy, visiting the sick).": (
        "ללכת בדרכיו — לחקות חסד אלוקי (הלבשת עניים, ביקור חולים)."
    ),
    "tzedakah — Tzedakah is usually translated \"charity,\" but the root means justice — sharing what is rightfully due to others.": (
        "צדקה — מתורגמת «צדקה», אך השורש משמע צדק — לחלק את מה שהגיע לאחרים."
    ),
    "tzedakah box — charity box; collecting coins for those in need is itself a mitzvah.": (
        "קופת צדקה — איסוף מטבעות לנזקקים הוא בפני עצמו מצווה."
    ),
    "this too is for the good; trust that hardship can be for the greater good.": (
        "גם זו לטובה — בטחון שקושי יכול להיות לטובה הכללית."
    ),
    "this too is for the good; trust that hardship can be for the greater good": (
        "גם זו לטובה — בטחון שקושי יכול להיות לטובה הכללית."
    ),
    "yirat Shamayim — fearing of Heaven; living with awareness that G-d is present": (
        "יראת שמים — חיים במודעות להיות ה' נוכח."
    ),
    "walk in G-d's ways — imitating divine kindness (clothing the needy, visiting the sick)": (
        "ללכת בדרכיו — לחקות חסד אלוקי (הלבשת עניים, ביקור חולים)."
    ),
    "tzedakah — Tzedakah is usually translated \"charity,\" but the root means justice — sharing what is rightfully due to others.": (
        "צדקה — מתורגמת «צדקה», אך השורש משמע צדק — לחלק את מה שהגיע לאחרים."
    ),
    "tzedakah box — charity box; collecting coins for those in need is itself a mitzvah.": (
        "קופת צדקה — איסוף מטבעות לנזקקים הוא בפני עצמו מצווה."
    ),
    "treif — Treif means non-kosher — originally torn flesh of improperly slaughtered animals, now any forbidden food. Mixing treif into a kosher kitchen can require kashering. \"Kosher style\" restaurants without supervision are not kosher.": (
        "טרף — לא כשר; במקור נבלה שלא נשחטה כהלכה, היום כל מאכל אסור. ערבוב טרף במטבח כשר עשוי לדרוש הכשרה. מסעדות «כשר סטייל» בלי השגחה — לא כשרות."
    ),
    "pareve — neutral foods (neither meat nor dairy) such as fish, eggs, and produce": (
        "פרווה — מזונות ניטרליים (לא בשר ולא חלב) כמו דג, ביצים וירקות."
    ),
    "shortened Hallel psalms on Rosh Chodesh and after the first Yom Tov of Pesach": (
        "חצי הלל — מזמורי הלל מקוצרים בראש חודש ואחרי יום טוב ראשון של פסח."
    ),
    "Half Hallel — shortened Hallel psalms on Rosh Chodesh and after the first Yom Tov of Pesach": (
        "חצי הלל — הלל קצר בראש חודש ואחרי יום טוב ראשון של פסח."
    ),
    "seudat mitzvah — festive meal tied to a mitzvah (e.g. brit, siyum, wedding, etc.)": (
        "סעודת מצווה — ארוחה חגיגית הקשורה למצווה (ברית, סיום, חתונה וכו')."
    ),
    "lulav — palm branch waved with the etrog, willows, and myrtle branches on Sukkot": (
        "לולב — ענף דקל שננענע עם אתרוג, ערבות והדסים בסוכות."
    ),
    "tallit gadol — large prayer shawl with tzitzit worn during Shacharit/Musaf and all day on Yom Kippur": (
        "טלית גדול — טלית עם ציצית הנילבש בשחרית/מוסף וכל היום ביום כיפור."
    ),
    "tallit katan — small four-cornered garment with tzitzit, worn generally over or under a shirt per community custom": (
        "טלית קטן — בגד קטן בארבע כנפות עם ציצית, נילבש מעל או מתחת לחולצה לפי מנהג."
    ),
    "small four-cornered garment with tzitzit, worn generally over or under a shirt per community custom": (
        "טלית קטן — בגד קטן בארבע כנפות עם ציצית, לפי מנהג הקהילה."
    ),
    "large prayer shawl with tzitzit worn during Shacharit/Musaf and all day on Yom Kippur": (
        "טלית גדול — טלית עם ציצית לשחרית/מוסף וכל היום ביום כיפור."
    ),
    "siyum — completion of a Torah text such as a tractate of the Talmud, often with a festive meal": (
        "סיום — השלמת קטע בתורה כמו מסכת בתלמוד, לעתים עם סעודה חגיגית."
    ),
    "completion of a Torah text such as a tractate of the Talmud, often with a festive meal": (
        "סיום — השלמת מסכת או קטע בתורה, לעתים עם סעודה חגיגית."
    ),
    "kohen — priest descended from Aaron. They perform the service in the Temple when it's standing.": (
        "כהן — יורש אהרן; משרתים במקדש כשהוא עומד."
    ),
    "priest descended from Aaron. They perform the service in the Temple when it's standing.": (
        "כהן — יורש אהרן; משרתים במקדש כשהוא עומד."
    ),
    "shul — Shul (synagogue) is the community house of prayer, Torah, and chesed. Beyond services, it hosts classes, youth programs, and lifecycle events.": (
        "בית כנסת — בית התפילה, התורה והחסד של הקהילה; מעבר לתפילות — שיעורים, נוער ואירועי חיים."
    ),
    "fleishig — meat status; dairy may not be eaten for a waiting period afterward, usually 6 hours": (
        "בשרי — מעמד בשרי; חלב אסור לאכול עד זמן המתנה, לרוב 6 שעות."
    ),
    "meat status; dairy may not be eaten for a waiting period afterward, usually 6 hours": (
        "בשרי — אחרי בשר חלב אסור עד המתנה, לרוב 6 שעות."
    ),
    "hechsher — in kashrut, a certification that food was produced under reliable kosher supervision": (
        "הכשר — בכשרות, תעודה שמזון יוצר בהשגחה כשרה אמינה."
    ),
    "in kashrut, a certification that food was produced under reliable kosher supervision": (
        "הכשר — תעודת כשרות מהימנה על ייצור המזון."
    ),
    "Blech — A blech is a metal cover placed on the stove to cover flames and heat food on Shabbat.": (
        "פלטה — מכסה מתכת על הכיריים לכסות להבות ולחמם אוכל בשבת."
    ),
    "A blech is a metal cover placed on the stove to cover flames and heat food on Shabbat.": (
        "פלטה — מכסה מתכת על הכיריים לחימום אוכל בשבת בלי בישול."
    ),
    "Hakafot — dancing while circling around the synagogue with Torah scrolls on Simchat Torah": (
        "הקפות — ריקודים סביב בית הכנסת עם ספרי תורה בשמחת תורה."
    ),
    "dancing while circling around the synagogue with Torah scrolls on Simchat Torah": (
        "הקפות — הקפות עם ספרי תורה בשמחת תורה."
    ),
    "Gam zu l'tovah — this too is for the good; trust that hardship can be for the greater good": (
        "גם זו לטובה — בטחון שקושי יכול להיות לטובה הכללית."
    ),
    "Davening — praying; any prayer, often refers to the formal daily prayer services": (
        "תפילה — כל תפילה; לעתים מתייחס לתפילות היומיות הקבועות."
    ),
    "Hamapil — blessing entrusting the soul to G-d before sleep, part of bedtime Shema": (
        "המפיל — ברכה המפקידה את הנפש לה' לפני שינה, חלק משמע על המיטה."
    ),
    "Eruv chatzerot — symbolic merging of courtyards allowing carrying on Shabbat": (
        "עירוב חצרות — מיזוג סמלי של חצרות המאפשר הוצאה בשבת."
    ),
    "borei me'orei ha'eish — blessing over fire in Yaknehaz and regular Havdalah": (
        "בורא מאורי האש — ברכה על האש ביקנה\"ז ובהבדלה רגילה."
    ),
    "nusach Sefard — Ashkenazi-Chasidic prayer rite (not the same as Sephardic Nusach Edot HaMizrach)": (
        "נוסח ספרד — נוסח תפילה אשכנזי-חסידי (לא נוסח ספרד עדות המזרח)."
    ),
    "Ashkenazi-Chasidic prayer rite (not the same as Sephardic Nusach Edot HaMizrach)": (
        "נוסח ספרד — נוסח אשכנזי-חסידי, שונה מנוסח עדות המזרח."
    ),
    "chametz she'avar — chametz that was in your possession over Pesach (forbidden to benefit from after)": (
        "חמץ שעבר — חמץ שהיה ברשותכם בפסח (אסור להנות ממנו אחר כך)."
    ),
    "chametz that was in your possession over Pesach (forbidden to benefit from after)": (
        "חמץ שעבר — חמץ ברשות בפסח; אסור להנות ממנו לאחר מכן."
    ),
    "The Afikoman is matzah eaten at the end of the Seder so no other food follows — remembering the Pesach sacrifice. Children often \"steal\" it for a prize, adding joy. You need a kezayit-sized piece eaten before chatzos halachic (midpoint of the night) per many poskim. It is the last taste of matzah the Seder requires.": (
        "האפיקומן הוא מצה הנאכלת בסוף הסדר כדי שלא יבוא אחריו מאכל — לזכור קורבן הפסח. ילדים לעתים «גונבים» אותו לפרס. נדרש כזית לפני חצות הלילה לפי רבים מן הפוסקים. זהו טעם המצה האחרון שהסדר דורש."
    ),
    "Afikoman — The Afikoman is matzah eaten at the end of the Seder so no other food follows — remembering the Pesach sacrifice. Children often \"steal\" it for a prize, adding joy. You need a kezayit-sized piece eaten before chatzos halachic (midpoint of the night) per many poskim. It is the last taste of matzah the Seder requires.": (
        "אפיקומן — מצה בסוף הסדר; כזית לפני חצות הלילה לפי רבים מן הפוסקים."
    ),
    "Tumah is ritual impurity — a halachic state affecting what you may touch or eat (Temple-era laws, niddah, corpse contact, etc.). It is not moral guilt and not the same as being \"dirty.\" Many tumah laws apply today in taharat hamishpacha and kohanim rules; others await the Temple. Morning tumah on hands is removed by washing, not by soap alone.": (
        "טומאה היא טומאה טקסית — מצב הלכתי המשפיע על מה מותר לגעת או לאכול (חוקי בית המקדש, נידה, מגע במת וכו'). זו לא אשמה מוסרית ולא «לכלוך». חוקי טומאה רבים חלים היום בטהרת המשפחה ובכללי כהנים; אחרים מחכים למקדש. טומאת ידיים בבוקר נסלקת בנטילה, לא בסבון בלבד."
    ),
    "tumah — Tumah is ritual impurity — a halachic state affecting what you may touch or eat (Temple-era laws, niddah, corpse contact, etc.). It is not moral guilt and not the same as being \"dirty.\" Many tumah laws apply today in taharat hamishpacha and kohanim rules; others await the Temple. Morning tumah on hands is removed by washing, not by soap alone.": (
        "טומאה — מצב הלכתי; לא אשמה מוסרית. נטילת ידיים בבוקר."
    ),
    "Shomer negiah is the halachic guarding of touch between men and women (and related laws). It preserves the specialness of physical closeness for marriage and reduces situations that lead to serious sin. Details vary by circumstance; a rav can guide real-life situations like family, healthcare, etc.": (
        "שומר נגיעה הוא שמירה הלכתית על מגע בין גברים ונשים (וחוקים קשורים). שומר על ייחוד הקרבה הפיזית לנישואין ומפחית מצבים מובילים לחטא. פרטים משתנים לפי נסיבות; הרב יכול להנחות במשפחה, בריאות וכו'."
    ),
    "shomer negiah — Shomer negiah is the halachic guarding of touch between men and women (and related laws). It preserves the specialness of physical closeness for marriage and reduces situations that lead to serious sin. Details vary by circumstance; a rav can guide real-life situations like family, healthcare, etc.": (
        "שומר נגיעה — שמירה על מגע בין גברים ונשים; התייעצו עם הרב."
    ),
    "L'chatchila means the ideal way to perform a mitzvah from the outset — what you should plan to do. Bedieved is after the fact, when something went wrong and halacha may offer a corrective or leniency. Learning both terms helps you read halachic guides: \"l'chatchila use a cup; bedieved if you forgot, some allow…\"": (
        "לכתחילה — הדרך האידיאלית לבצע מצווה מההתחלה. בדיעבד — אחרי המעשה, כשמשהו השתבש וההלכה עשויה להציע תיקון או קולה. שני המונחים עוזרים בקריאת מדריכי הלכה."
    ),
    "l'chatchila — L'chatchila means the ideal way to perform a mitzvah from the outset — what you should plan to do. Bedieved is after the fact, when something went wrong and halacha may offer a corrective or leniency. Learning both terms helps you read halachic guides: \"l'chatchila use a cup; bedieved if you forgot, some allow…\"": (
        "לכתחילה — הדרך האידיאלית; בדיעבד — אחרי המעשה."
    ),
    "Zmanim are halachic times derived from sunrise, sunset, and seasonal hours — not always clock times. Examples: latest morning Shema, Mincha, Shabbat entry, chatzos, plag hamincha. A \"halachic hour\" divides daylight into twelve parts, so it lengthens in summer. Jewish calendars and apps translate zmanim for your location.": (
        "זמנים — זמנים הלכתיים משחר, שקיעה ושעות עונתיות — לא תמיד שעון קיר. דוגמאות: סוף זמן קריאת שמע, מנחה, כניסת שבת, חצות, פלג המנחה. שעה הלכתית מחלקת אור היום לשתים עשרה חלקים. לוחות שנה ואפליקציות ממירים לפי מיקומכם."
    ),
    "zmanim — Zmanim are halachic times derived from sunrise, sunset, and seasonal hours — not always clock times. Examples: latest morning Shema, Mincha, Shabbat entry, chatzos, plag hamincha. A \"halachic hour\" divides daylight into twelve parts, so it lengthens in summer. Jewish calendars and apps translate zmanim for your location.": (
        "זמנים — זמנים הלכתיים משחר ושקיעה; שעה הלכתית מחלקת אור היום."
    ),
    "nefesh — Nefesh is the 'animal' part of the soul closest to physical life — appetite, hunger, physical desires, etc. Torah and mitzvot attempt to refine the nefesh toward holiness instead of animal instincts.": (
        "נפש — החלק «החייתי» של הנשמה הקרוב לחיים גשמיים — תיאבון, רעב ודחפים. תורה ומצוות מעלים את הנפש לקדושה."
    ),
    "Nefesh is the 'animal' part of the soul closest to physical life — appetite, hunger, physical desires, etc. Torah and mitzvot attempt to refine the nefesh toward holiness instead of animal instincts.": (
        "נפש — החלק הקרוב לחיים גשמיים; תורה ומצוות מעלים לקדושה."
    ),
    "A keli is a vessel, utensil, or tool. It generally refers to any manufactured object used for a purpose — pots, cups, knives. In halacha, kelim have statuses (meat, dairy, pareve) and may require tevilah when bought from a non-Jew. Broken kelim may lose their status.": (
        "כלי — כלי, כלי מטבח או כלי עבודה; בהלכה מעמד בשרי/חלבי/פרווה ועשויה טבילה בקנייה מגוי."
    ),
    "Keli — A keli is a vessel, utensil, or tool. It generally refers to any manufactured object used for a purpose — pots, cups, knives. In halacha, kelim have statuses (meat, dairy, pareve) and may require tevilah when bought from a non-Jew. Broken kelim may lose their status.": (
        "כלי — כלי מטבח או עבודה; מעמד וטבילה לפי ההלכה."
    ),
    "Musaf is an additional Amidah prayer added to the morning service on Shabbat, Rosh Chodesh, and festivals — remembering the Temple sacrifices. On Rosh Chodesh, remove tefillin before Musaf. On Yom Tov, Musaf includes Yaaleh V'yavo.": (
        "מוסף — תפילת עמידה נוספת בשחרית בשבת, ראש חודש וחגים — לזכר קורבנות המקדש. בראש חודש מסירים תפילין לפני מוסף. ביום טוב כולל יעלה וייבו."
    ),
    "colloquial marker for halachic nightfall (tzeit); actual timing varies slightly by community and location": (
        "שלושה כוכבים — סימן עממי לצאת הכוכבים ההלכתי; הזמן המדויק משתנה בין קהילות ומיקומים."
    ),
    "three stars — colloquial marker for halachic nightfall (tzeit); actual timing varies slightly by community and location": (
        "שלושה כוכבים — סימן עממי לצאת הכוכבים; הזמן משתנה לפי קהילה ומיקום."
    ),
    "Aleinu — Aleinu is a closing prayer declaring G-d's sovereignty and our hope for universal recognition of His kingship. It ends daily Shacharit and Maariv and appears near the end of High Holiday services. The bow at \"we bend the knee\" is a moment of submission and awe.": (
        "עלינו — תפילת סיום על מלכות ה' ותקווה להכרה עולמית; בסוף ימים נוראים. כריעה ב«ונכרע» — כניעה ויראה."
    ),
    "Tachanun (Ashkenaz): At Shacharit on weekdays — longer on Mondays and Thursdays (Vehu Rachum), shorter on other days. At Mincha, often the shorter form (sometimes with nefilat apayim). Omitted on Rosh Chodesh, Yom Tov, Chanukah, and other days of celebration — Mincha itself is still said.": (
        "תחנון (אשכנז): בשחרית בימי חול — ארוך בימי שני וחמישי (והוא רחום), קצר בימים אחרים. במנחה לעתים קרובות הצורה הקצרה (לפעמים עם נפילת apayim). חסר בראש חודש, יום טוב, חנוכה וימי שמחה — המנחה עצמה נאמרת."
    ),
    "Nusach Ari is the prayer rite associated with Rabbi Isaac Luria (the Ari) and used by Chabad and some other communities. It blends Ashkenazi and Sephardi elements. Chabad siddurim such as Tehillat Hashem print this nusach. If you daven Nusach Ari, use that siddur consistently for festival inserts.": (
        "נוסח האר\"י הוא נוסח התפילה הקשור לרבי יצחק לוריה (האר\"י), בשימוש חב\"ד וקהילות אחרות. משלב אשכנז וספרד. סידורי חב\"ד כמו תהילת ה' מדפיסים נוסח זה. אם מתפללים נוסח האר\"י — השתמשו באותו סידור לחגים."
    ),
    "Nusach Ari — Nusach Ari is the prayer rite associated with Rabbi Isaac Luria (the Ari) and used by Chabad and some other communities. It blends Ashkenazi and Sephardi elements. Chabad siddurim such as Tehillat Hashem print this nusach. If you daven Nusach Ari, use that siddur consistently for festival inserts.": (
        "נוסח האר\"י — נוסח תפילה של האר\"י; סידור תהילת ה' לחב\"ד; השתמשו בעקביות לחגים."
    ),
    "Double your love! ❤️ The Rambam explains two beautiful mitzvot: loving fellow Jews and showing special love to converts. Why the extra love for converts? They chose to join our people and follow Torah - that's amazing! Here's something practical: Make an extra effort today to show love to another Jew through action - whether they were born Jewish or chose to join our people!": (
        "הכפילו את האהבה! ❤️ הרמב\"ם מסביר שתי מצוות יפות: אהבת ישראל ואהבה מיוחדת לגרים. למה אהבה נוספת לגרים? הם בחרו להצטרף לעמנו וללכת בתורה — מדהים! היום — מאמץ נוסף להפגין אהבה ליהודי אחר במעשה, נולד יהודי או בחר להצטרף."
    ),
    "Plag HaMincha — Plag hamincha is one and a quarter halachic hours before nightfall — used for early Mincha, early Shabbat entry in some communities, and certain Pesach and Chanukah times. It is also the earliest time you may light Shabbat candles; lighting before plag invalidates the mitzvah and makes the blessing unnecessary. It is not identical to sunset; check your calendar.": (
        "פלג המנחה — שעה ורבע הלכתיות לפני הלילה; למנחה מוקדמת, כניסת שבת מוקדמת, וזמני פסח/חנוכה. גם הזמן המוקדם ביותר להדלקת נרות שבת; הדלקה לפני פלג פוסלת את המצווה. לא זהה לשקיעה — בדקו לוח."
    ),
    "Ashkenaz custom: mourning from after Pesach until Lag BaOmer (33rd day of the Omer, 18 Iyar) or until the morning of Lag BaOmer (per your shul). Some continue haircuts/music restrictions until Shavuot or the Three Weeks.\n\nNo weddings, no live music, and no haircuts during your community's Sefirah period. Lag BaOmer is a break for many Ashkenazim; ask your rabbi about music and haircuts after that date.": (
        "מנהג אשכנז: אבל מאחרי פסח עד ל\"ג בעומר (יום ל\"ג בעומר, י\"ח באייר) או עד בוקר ל\"ג בעומר (לפי בית הכנסת). חלק ממשיכים הגבלות תספורת/מוזיקה עד שבועות או שלושת השבועות.\n\nאין חתונות, מוזיקה חיה ותספורות בתקופת הספירה. ל\"ג בעומר הפסקה לרבים — שאלו את הרב לגבי מוזיקה ותספורות אחרי התאריך."
    ),
    "Guard your speech about G-d! 🗣️ The Rambam teaches us in Hilchot Avodat Kochavim that we must be extremely careful never to speak disrespectfully about G-d or deny His existence. Even casual oaths or unnecessary blessings are forbidden. Here's something powerful: Every word we speak about G-d should be with reverence and awe. Today's mission: Practice speaking about holy matters with extra care and respect.": (
        "שמרו על דיבורכם על ה'! 🗣️ הרמב\"ם בהלכות עבודה זרה: להיזהר מאוד לא לדבר בזלזול על ה' או להכחיש את קיומו. שבועות מזדמנים או ברכות מיותרות אסורות. כל מילה על ה' — ביראה וכבוד. משימה: דברו על עניינים קדושים בזהירות."
    ),
    "Tu B'Shvat (15 Shevat) — New Year for Trees — is a day to appreciate Hashem's fruit and Land of Israel.\n\nLiturgical note:\n• Tachanun is completely omitted from standard weekday prayers today — as well as during yesterday afternoon's Mincha service (14 Shevat).\n\nCustoms:\n• Eat fruit — especially the seven species of Eretz Yisrael: wheat, barley, grapes, figs, pomegranates, olives, dates.\n• Say brachot and after-brachot carefully.\n• Shehecheyanu on fruit: You may say Shehecheyanu only on a seasonal fruit that is genuinely new to the market this season (like fresh pomegranates or figs). Do not say this blessing on fruits grown and sold year-round (like bananas, pineapples, or apples), even if you personally have not eaten one recently.\n• Some hold a Tu B'Shvat Seder with four cups of wine (white to red) and themed fruit — follow a guide if hosting.\n• No fasting; work is permitted; it is not Yom Tov.\n\nSpiritual focus: gratitude for creation, connection to Eretz Yisrael, and growth (trees blossom in Israel around this season).": (
        "ט\"ו בשבט — ראש השנה לאילנות — יום להעריך פירות ה' וארץ ישראל.\n\nהערה ליטורגית: לא תחנון בתפילות היום וגם לא במנחת אתמול (י\"ד בשבט).\n\nמנהגים: אכילת פירות — שבעת המינים; ברכות וברכות אחרונות בזהירות; שהחיינו רק על פירות עונתיים חדשים; סדר ט\"ו בשבט אצל חלק; אין צום; מותר בעבודה.\n\nמיקוד: הכרת תודה, קשר לארץ ישראל וצמיחה."
    ),
}


def main() -> None:
    from generate_hebrew_glossary_batch import BATCH as GLOSS_BATCH
    from hebrew_paragraph_batch2 import FIXES as HE_BATCH2
    from hebrew_paragraph_batch3 import FIXES as HE_BATCH3
    from hebrew_paragraph_batch4 import FIXES as HE_BATCH4
    from kibbud_paragraph import HE as KIBBUD_HE, KEY as KIBBUD_KEY
    from shabbat_sukkot_he import FIXES as SHABBAT_SUKKOT_HE

    merged = dict(FIXES)
    merged.update(SHABBAT_SUKKOT_HE)
    merged.update(HE_BATCH2)
    merged.update(HE_BATCH3)
    merged.update(HE_BATCH4)
    merged.update(GLOSS_BATCH)
    merged[KIBBUD_KEY] = KIBBUD_HE
    batch_path = ROOT / "data" / "translation-catalog" / "shards" / "hebrew_glossary_batch.json"
    if batch_path.exists():
        merged.update(json.loads(batch_path.read_text(encoding="utf-8")).get("he", {}))
    OUT.write_text(
        json.dumps({"he": merged}, ensure_ascii=False, indent=2),
        encoding="utf-8",
    )
    print(f"wrote {OUT} ({len(merged)} Hebrew entries)")


if __name__ == "__main__":
    main()
