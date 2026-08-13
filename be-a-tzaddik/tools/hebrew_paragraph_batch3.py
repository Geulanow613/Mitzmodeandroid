"""Third batch — Hebrew fixes for Shema, Melacha, Tisha B'Av, etc."""

from __future__ import annotations

FIXES: dict[str, str] = {
    "The Shema (\"Hear O Israel\") declares G-d's oneness and our duty to love Him with all our heart, soul, and might. It is recited morning and evening with surrounding blessings. Krias Shema has specific times — especially morning Shema before the third hour of the day halachically. It is the Jewish declaration of faith children learn first.": (
        "שמע ישראל מכריז על אחדות ה' וחובתנו לאהוב אותו בכל ליבנו ונפשנו. נאמר בבוקר ובערב. קריאת שמע בזמנים — שמע של שחרית עד סוף השעה השלישית. הכרזת האמונה הראשונה של ילדים."
    ),
    "Shema — The Shema (\"Hear O Israel\") declares G-d's oneness and our duty to love Him with all our heart, soul, and might. It is recited morning and evening with surrounding blessings. Krias Shema has specific times — especially morning Shema before the third hour of the day halachically. It is the Jewish declaration of faith children learn first.": (
        "שמע — אחדות ה' ואהבה; קריאת שמע בזמנים; שמע של שחרית עד השעה השלישית."
    ),
    "Melacha is transformative labor forbidden on Shabbat — including cooking. On Yom Tov most of the 39 melachot remain forbidden (writing, building, kindling a new flame, etc.), but the Torah permits ochel nefesh — preparing food for that festival day, including cooking transferred from a pre-existing flame (Exodus 12:16). You may not cook on Yom Tov for the next day without eruv tavshilin when Shabbat follows. When unsure, ask your rav.": (
        "מלאכה — עבודה אסורה בשבת, כולל בישול. ביום טוב רוב המלאכות אסורות, אך התורה מתירה אוכל נפש — הכנת אוכל לאותו יום מלהבה קיימת. לא לבשל ליום למחרת בלי עירוב תבשילין כששבת אחריו. בספק — שאלו רב."
    ),
    "Melacha — Melacha is transformative labor forbidden on Shabbat — including cooking. On Yom Tov most of the 39 melachot remain forbidden (writing, building, kindling a new flame, etc.), but the Torah permits ochel nefesh — preparing food for that festival day, including cooking transferred from a pre-existing flame (Exodus 12:16). You may not cook on Yom Tov for the next day without eruv tavshilin when Shabbat follows. When unsure, ask your rav.": (
        "מלאכה — אסורה בשבת; ביום טוב אוכל נפש מלהבה קיימת; עירוב תבשילין; שאלו רב."
    ),
    "Erev Tisha B'Av (8 Av afternoon): stop learning Torah except sad topics; eat the final meal (seudah hamafseket) before the fast. Tallit and tefillin are worn at Shacharit on Erev Tisha B'Av — the restriction applies on Tisha B'Av day itself.\n\nTisha B'Av (9 Av): full 25-hour fast; kinot at Shacharit without tallit and tefillin; don them at Mincha after halachic chatzos (use your zmanim app). Sit on low stools until chatzos on 9 Av.\n\nShabbat Chazon (the Shabbat before 9 Av): Shabbat is observed normally — meat and wine are permitted.": (
        "ערב תשעה באב: להפסיק לימוד תורה מלבד נושאים עצובים; סעודה מפסקת לפני הצום. טלית ותפילין בשחרית בערב תשעה באב — האיסור ביום תשעה באב.\n\nתשעה באב: צום 25 שעות; קינות בשחרית בלי טלית ותפילין; חולצים במנחה אחרי חצות. ישיבה על קרקע עד חצות.\n\nשבת חזון: שבת כרגיל — בשר ויין מותרים."
    ),
    "Sefirat HaOmer counts forty-nine days from the second night of Pesach until Shavuot — linking freedom to receiving the Torah. Each night, preferably after nightfall, you bless and announce the day and week count. Missing a full day may affect whether you can say the blessing the remaining nights — ask your rav. During the Omer, many communities observe mourning customs (no music, weddings, haircuts) until Lag BaOmer or Shavuot, depending on minhag to commemorate the plague that killed many of Rabbi Akiva's students during this time.": (
        "ספירת העומר — 49 ימים מליל שני של פסח עד שבועות. כל לילה אחרי צאת הכוכבים — ברכה וספירה. פספוס יום מלא — שאלו רב. בתקופה — מנהגי אבל (ללא מוזיקה, חתונות, תספורות) עד ל\"ג בעומר או שבועות."
    ),
    "Tachanun at Shacharit (weekdays): After the morning Amidah — longer on Mondays and Thursdays (Vehu Rachum and the full penitential section), shorter on other weekdays. Omitted on Rosh Chodesh, festivals, Chanukah, and other days your siddur lists.\n\nTachanun at Mincha: Some Ashkenaz communities especially emphasize saying Tachanun at Mincha if it was skipped at Shacharit that day. On a normal weekday, Tachanun at Mincha is already part of the standard service (often the shorter form). Omitted on the same festive days — Mincha itself is still said.": (
        "תחנון בשחרית (ימי חול): אחר עמידת שחרית — ארוך בשני וחמישי, קצר באחרים. חסר בר\"ח, חגים וחנוכה.\n\nתחנון במנחה: קהילות אשכנז מדגישות תחנון במנחה אם דולג בשחרית. ביום רגיל — חלק מהשירות. חסר בימי שמחה — המנחה נאמרת."
    ),
    "Kiddush b'Makom Seudah means sanctifying Shabbat or Yom Tov, typically with blessings over wine, in the same place you eat a meal afterwards. You would not fulfill Kiddush if you recited it and left without eating there. On Shabbat night, the meal should include bread (hamotzi) per most poskim. On Yom Tov or daytime Kiddush, other foods may qualify in some cases; ask your rav. If you make Kiddush in one room, the meal should follow there and not in a different space.": (
        "קידוש במקום סעודה — מקדשים שבת או יום טוב ביין באותו מקום שאוכלים. בלי סעודה שם — לא יצאתם. בליל שבת — לחם (המוציא) לרוב הפוסקים. ביום טוב או קידוש יום — לפעמים מזונות אחרים; שאלו רב. הסעודה באותו חדר."
    ),
    "Kashering makes utensils fit for kosher or Pesach use. The core principle is kebolo kach polto — as a vessel absorbed forbidden taste, so it expels it. Items used in boiling water often need hag'alah (immersion in rolling boiling water); items used directly on fire may need libun (intense heat until they turn red hot). Plastic and glass have highly conflicting rulings between Ashkenazim and Sephardim — and it's less lenient on Pesach. Ask your rav. Ceramic cannot be kashered. Many families keep a separate Pesach set.": (
        "הגעלה — להכשיר כלים לכשרות או פסח. כבולע כך פולט. מים רותחים — הגעלה; על האש — ליבון. פלסטיק וזכוכית — חלוקי דעות; בפסח מחמירים. שאלו רב. קרמיקה לא מוגעלת. סט פסח נפרד."
    ),
    "Shmoneh Esrei — the nineteen blessings of the standing Amidah prayer": (
        "שמונה עשרה — תשע עשרה ברכות של תפילת העמידה."
    ),
    "neutral foods (neither meat nor dairy) such as fish, eggs, and produce": (
        "פרווה — מזונות ניטרליים (לא בשר ולא חלב) כמו דג, ביצים וירק."
    ),
    "garment; tzitzit apply to four-cornered beged": (
        "בגד — בגד; ציצית חלים על בגד בעל ארבע כנפות."
    ),
    "Aseret Yemei Teshuvah — Ten Days of Repentance from Rosh Hashana to Yom Kippur": (
        "עשרת ימי תשובה — מראש השנה עד יום כיפור."
    ),
    "Matan Torah — receiving the Torah at Mount Sinai, celebrated on Shavuot": (
        "מתן תורה — קבלת התורה בהר סיני, בחג שבועות."
    ),
    "receiving the Torah at Mount Sinai, celebrated on Shavuot": (
        "קבלת התורה בהר סיני, בחג שבועות."
    ),
    "bitul — nullification (e.g. Kol Chamira nullifying chametz)": (
        "ביטול — ביטול (למשל כל חמירא המבטל חמץ)."
    ),
    "Kibud Av V'Eim - mitzvah to honor/respect and care for parents": (
        "כיבוד אב ואם — מצווה לכבד ולטפל בהורים."
    ),
    "Havdalah in Kiddush — Yaknehaz order when Shabbat leads into Yom Tov": (
        "הבדלה בקידוש — סדר יקנה\"ז כששבת נכנסת ליום טוב."
    ),
    "14 Adar (15 in walled cities); Megillah, gifts to friends/poor, and feast": (
        "י\"ד באדר (ט\"ו בערים מוקפות חומה); מגילה, מתנות לאביונים וסעודה."
    ),
}
