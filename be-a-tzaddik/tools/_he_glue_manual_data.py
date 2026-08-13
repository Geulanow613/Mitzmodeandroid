"""Hand-curated Hebrew (and some ES/FR) overrides for machine-garbled glossary strings."""

from __future__ import annotations

MELACHA_HE = (
    "מלאכה היא עבודה משנה אסורה בשבת — כולל בישול. ביום טוב רוב ל״ט המלאכות נשארות אסורות "
    "(כתיבה, בנייה, הדלקת אש חדשה וכו׳), אך התורה מתירה אוכל נפש — הכנת אוכל לאותו יום טוב, "
    "כולל בישול מאש קיימת (שמות י״ב:ט״ז). אסור לבשל ביום טוב ליום שלמחרת בלי עירוב תבשילין כששבת עוקבת. "
    "כשלא בטוחים — שאלו את הרב לפני המעשה."
)

# Prefix → Hebrew (matched against catalog keys via startswith).
PREFIX_HE: dict[str, str] = {
    "Chinuch is training children to do mitzvot before they are bar or bat mitzvah": (
        "חינוך הוא הכשרת ילדים לקיים מצוות לפני שהם מגיעים לבר או בת מצווה — כשהחובה הופכת אישית. "
        "הורים מלמדים בהדרגה: ברכות, ציצית, צדקה, נרות שבת (לפי מנהג) והתנהגות מכובדת בבית הכנסת. "
        "«חנוך לנער על פי דרכו» (משלי) פירושו להתאים את הקצב לאופי הילד. חינוך בונה הרגל; ההבנה מתעמקת עם הגיל."
    ),
    "chinuch — Chinuch is training children to do mitzvot before they are bar or bat mitzvah": (
        "חינוך — חינוך הוא הכשרת ילדים לקיים מצוות לפני שהם מגיעים לבר או בת מצווה — כשהחובה הופכת אישית. "
        "הורים מלמדים בהדרגה: ברכות, ציצית, צדקה, נרות שבת (לפי מנהג) והתנהגות מכובדת בבית הכנסת. "
        "«חנוך לנער על פי דרכו» (משלי) פירושו להתאים את הקצב לאופי הילד. חינוך בונה הרגל; ההבנה מתעמקת עם הגיל."
    ),
    "A Sefer Torah is a Torah scroll — the entire five books of the Torah handwritten by a sofer on kosher parchment, rolled on two wooden rollers (atzei chayim). It is read in synagogue with special honor; you usually do not study daily from the scroll the way you use a Chumash. Missing or extra letters can invalidate a scroll, so scribes train for years.": (
        "ספר תורה הוא מגילת תורה — חמישת חומשי התורה בכתב יד של סופר על קלף כשר, גלולה על שני עצי חיים. "
        "קוראים אותו בבית הכנסת בכבוד מיוחד; בדרך כלל לא לומדים ממנו יומיומית כמו מחומש. "
        "אות חסרה או מיותרת עלולה לפסול את המגילה, ולכן סופרים לומדים שנים רבות."
    ),
    "Halacha means \"the way of walking\" — the practical path of Jewish life drawn from Torah, Prophets, Writings, Mishnah, Talmud, and codes like the Shulchan Aruch. It covers prayer, Shabbat, diet, family life, business ethics, and festivals. Halacha is decided by qualified poskim; when you are unsure, the mitzvah is to ask your rav rather than guess.": (
        "הלכה פירושה «דרך ההליכה» — המסלול המעשי של החיים היהודיים הנגזר מן התורה, הנביאים, הכתובים, "
        "המשנה, התלמוד וקודים כמו השולחן ערוך. היא כוללת תפילה, שבת, כשרות, משפחה, אתיקה עסקית וחגים. "
        "הלכה נקבעת על ידי פוסקים מוסמכים; כשאינכם בטוחים — המצווה היא לשאול את הרב ולא לנחש."
    ),
    "Bal yera'eh (\"it shall not be seen\") is the Torah prohibition against chametz remaining visible in your domain on Pesach. Together with bal yimatzei (\"it shall not be found\"), it drives bedikat, biur, and mechirat chametz. Even crumbs in your control matter. Sold chametz in a closed sold area must stay inaccessible.": (
        "בל יראה («לא ייראה») הוא איסור התורה שחמץ יישאר נראה בתחומכם בפסח. יחד עם בל ימצא («לא יימצא») "
        "הוא מניע בדיקת חמץ, ביעור ומכירת חמץ. גם פירורים בשליטתכם חשובים. חמץ שנמכר באזור סגור חייב להישאר בלתי נגיש."
    ),
    "bal yera'eh — Bal yera'eh (\"it shall not be seen\") is the Torah prohibition against chametz remaining visible in your domain on Pesach. Together with bal yimatzei (\"it shall not be found\"), it drives bedikat, biur, and mechirat chametz. Even crumbs in your control matter. Sold chametz in a closed sold area must stay inaccessible.": (
        "בל יראה — בל יראה («לא ייראה») הוא איסור התורה שחמץ יישאר נראה בתחומכם בפסח. יחד עם בל ימצא "
        "(«לא יימצא») הוא מניע בדיקת חמץ, ביעור ומכירת חמץ. גם פירורים בשליטתכם חשובים. "
        "חמץ שנמכר באזור סגור חייב להישאר בלתי נגיש."
    ),
    "Chametz:\n• All chametz must be completely gone, destroyed, or sold, and the final Kol Chamira recited, before the end of the 5th halachic hour this morning (midday threshold) — NOT sunset. Stop eating chametz by the end of the 4th halachic hour. Bedikat chametz was last night; mechirat chametz should already be authorized with your rabbi — use today's biur chametz checklist item if still on your list.": (
        "חמץ:\n"
        "• כל החמץ חייב להיעלם לחלוטין, להיחרב או להימכר, ולומר קול חמירא לפני סוף השעה ההלכתית החמישית הבוקר (סף חצות היום) — לא שקיעה.\n"
        "• הפסיקו לאכול חמץ עד סוף השעה ההלכתית הרביעית.\n"
        "• בדיקת חמץ הייתה אמש; מכירת חמץ כבר אמורה להיות מאושרת אצל הרב — השתמשו בפריט ביעור חמץ של היום אם עדיין ברשימה."
    ),
    "During Sefirat HaOmer we keep customs of mourning (aveilut) because Rabbi Akiva's 24,000 students died in a plague during this period between Pesach and Shavuot (Talmud, Yevamot 62b). Their deaths ceased on Lag BaOmer — which is why many communities ease some restrictions then, while others continue until Shavuot or the morning of the 33rd day of the Omer.\n\nWhy we mourn: The Omer is the path from physical freedom (Pesach) to spiritual receiving of Torah (Shavuot). The plague cut short Torah transmission — so we temper joy with restraint until we reach Matan Torah.\n\nCommon customs (timing varies — ask your rav):\n• No live music (recordings and a cappella rules vary by posek)\n• No weddings\n• No haircuts for part or all of the Omer\n\nFollow your community's start and end dates for these practices.": (
        "במהלך ספירת העומר אנו שומרים מנהגי אבל (אבילות) כי כ״ד,אלף תלמידיו של רבי עקיבא מתו במגפה בין פסח לשבועות (תלמוד, יבמות ס״ב ב).\n"
        "מותם חדל בל״ג בעומר — ולכן קהילות רבות מקלות אז מסוימות הגבלות, ואחרות ממשיכות עד שבועות או עד בוקר היום ה-33 בעומר.\n\n"
        "מדוע מתאבלים: העומר הוא המסלול מחירות גופנית (פסח) לקבלת תורה רוחנית (שבועות). המגפה קטעה את שידור התורה — ולכן ממתינים בשמחה עד מתן תורה.\n\n"
        "מנהגים נפוצים (הזמנים משתנים — שאלו את הרב):\n"
        "• בלי מוזיקה חיה (הקלטות ואקפלה — לפי פוסק)\n"
        "• בלי חתונות\n"
        "• בלי תספורות לחלק או לכל תקופת העומר\n\n"
        "עקבו אחר תאריכי ההתחלה והסיום במנהג הקהילה שלכם."
    ),
    "Chabad (Alter Rebbe / Arizal): haircut and shaving restrictions continue the entire 49 days through Erev Shavuot — adults do not take haircuts on Lag BaOmer (the sole exception is upsherin for a 3-year-old boy). Lag BaOmer is a day of intense joy with music, bonfires, and celebration, but haircut restrictions remain until Shavuot.\n\nMusic is generally avoided through Shavuot per Chabad practice, with Lag BaOmer as a day without music restrictions. Weddings follow your Chabad rabbi's guidance.\n\nAsk your Chabad rabbi for details on your community.": (
        "חב״ד (האלטר רבי / האריז״ל): הגבלות תספורת וגילוח נמשכות כל 49 הימים עד ערב שבועות — "
        "מבוגרים אינם מתספרים בל״ג בעומר (החריג היחיד: עשירון לילד בן 3). ל״ג בעומר הוא יום שמחה עם מוזיקה, מדורות וחגיגה, "
        "אך הגבלות תספורת נמשכות עד שבועות.\n\n"
        "מוזיקה נמנעת בדרך כלל עד שבועות לפי מנהג חב״ד, ול״ג בעומר יום ללא הגבלת מוזיקה. חתונות — לפי הוראת הרב החבד״י.\n\n"
        "שאלו את הרב החבד״י שלכם לפרטי הקהילה."
    ),
    "Hagbah is lifting the open Torah scroll so the congregation can see the writing and recite \"V'zot HaTorah\" (and related verses). Ashkenazim (and most Chabad communities) perform hagbah after the reading, before gelilah (rolling and dressing the scroll). Sephardim (Nusach Edot HaMizrach) perform hagbah before the reading begins — the scroll is shown, then read. Follow your shul's order so you know when to stand and respond.": (
        "הגבהה היא הרמת מגילת התורה הפתוחה כדי שהקהל יראה את הכתב ויאמר «וזאת התורה» (ופסוקים קשורים). "
        "אשכנזים (ורוב קהילות חב״ד) עושים הגבהה אחרי הקריאה, לפני גלילה (גלילה וכיסוי המגילה). "
        "ספרדים (נוסח עדות המזרח) עושים הגבהה לפני תחילת הקריאה — מראים את המגילה ואז קוראים. "
        "עקבו אחר סדר בית הכנסת שלכם כדי לדעת מתי לעמוד ולהשיב."
    ),
    "Learn about the power of Tehillim (Psalms)! Start with Psalm 1, which is all about the joy of someone who delights in Torah 📖. Did you know? King David composed these prayers during both his highest and lowest moments, and they've been a source of comfort and protection for Jews throughout history. When you read them, you're tapping into an ancient power source of divine connection! ✡": (
        "למדו על כוחן של תהילים! התחילו בתהילים א׳ — על שמחתו של אדם שעוסק בתורה 📖. "
        "ידעתם? דוד המלך חיבר תפילות אלה ברגעי שיא ושפל, והן מקור נחמה והגנה ליהודים לאורך ההיסטוריה. "
        "כשקוראים אותן — מתחברים למקור עתיק של קשר אל הקב״ה! ✡"
    ),
    "Learn about treifot": (
        "למדו על טריפות — פגמים פנימיים שהופכים בעל חיים שנשחט כהלכה לאסור באכילה, גם אם הכל נעשה נכון! 🫁\n"
        "בעל חיים יכול להיות מין כשר, נשחט על ידי שוחט מוסמך עם סכין תקינה — ועדיין אסור אם יש פגמים גופניים מסוימים. "
        "המוקד העיקרי: הריאות — בודק (בודק) בוחן אותן אחרי כל שחיטה. ריאה נקבית היא הטריפה הנפוצה ביותר.\n"
        "קהילות רבות נוהגות «גלאט כשר» (מיידיש «חלק») — ריאות חלקות לחלוטין בלי הדבקויות. "
        "בכשר «רגיל» לעיתים מקלפים הדבקויות ובודקים; בגלאט מקבלים רק ריאות חלקות לגמרי. "
        "פגמים נוספים הפוכים לטריפה: קרע בקנה או בושט, שבר בחוליה, איבר חסר או פגום ועוד — שאלו את הרב לפרטים."
    ),
    "Melacha is transformative labor forbidden on Shabbat — including cooking. On Yom Tov most of the 39 melachot remain forbidden (writing, building, kindling a new fire, etc.), but the Torah permits ochel nefesh — preparing food for that festival day, including cooking transferred from a pre-existing flame (Exodus 12:16). You may not cook on Yom Tov for the next day without eruv tavshilin when Shabbat follows. When unsure, ask your rav before acting.": MELACHA_HE,
    "Melacha — Melacha is transformative labor forbidden on Shabbat — including cooking. On Yom Tov most of the 39 melachot remain forbidden (writing, building, kindling a new fire, etc.), but the Torah permits ochel nefesh — preparing food for that festival day, including cooking transferred from a pre-existing flame (Exodus 12:16). You may not cook on Yom Tov for the next day without eruv tavshilin when Shabbat follows. When unsure, ask your rav before acting.": (
        "מלאכה — " + MELACHA_HE
    ),
    "Ochel nefesh (\"food for the soul\") is the Torah-based allowance to perform certain food preparation tasks on Yom Tov (festival days) for consumption on that same day — cooking and baking that would otherwise be forbidden melacha. It does not apply on Chol HaMoed, where food preparation is permitted. It does not permit unnecessary cooking. To prepare for Shabbat that falls immediately after Yom Tov, you need an eruv tavshilin.": (
        "אוכל נפש («אוכל לנפש») הוא ההיתר התורני לבצע מלאכות הכנת אוכל מסוימות ביום טוב לצריכה באותו יום — "
        "בישול ואפייה שאחרת אסורים כמלאכה. לא חל בחול המועד, שבו הכנת אוכל מותרת. לא מתיר בישול מיותר. "
        "להכין לשבת שחל מיד אחרי יום טוב צריך עירוב תבשילין."
    ),
    "Parents are obligated in chinuch": (
        "הורים מחויבים בחינוך (חִנּוּךְ — הכשרת ילדים במצוות). התורה מצווה: «ולמדתם אותם את בניכם» (דברים י״א:י״ט) — "
        "כדי שילמדו תורה ויקיימו מצוות (פניני הלכה, הלכות ילדים כ״ד:א). לפני בר/בת מצווה הילד אינו חייב אישית, "
        "אך להורים חובת תורה לחנכו (דברים י״א:י״ט; רמב״ם, חינוך א׳:א).\n\n"
        "עקרונות מרכזיים:\n"
        "• «חנוך לנער על פי דרכו» (משלי כ״ב:ו) — לכל ילד קצב משלו.\n"
        "• לאמן במצווה חיובית כשהילד מבין ויכול לקיים כהלכה — כפי דעתו (פניני הלכה כ״ד:א).\n"
        "• ללמד פרטים נכונים, לא רק רעיון כללי — למשל אתרוג כשר בנטילת לולב.\n"
        "• «החיצוניות מעוררת את הפנימיות» — מעשה ורגל קודמים; הבנה מתעמקת עם הגיל.\n\n"
        "מפת דרכים קלאסית — פרקי אבות, ברכות, שבת, צדקה, כיבוד הורים; הרחיבו לפי גיל ומנהג הבית."
    ),
    "Pesach begins in about a week": (
        "פסח מתחיל בעוד כשבוע — השתמשו בזמן להכנה מעשית (לא ניקוי אביב של כל ארון, אלא הסרת חמץ היכן שחשוב).\n\n"
        "אזורי מיקוד:\n"
        "• מטבח ואוכל — שם אוכלים חמץ: משטחים, תנור, מיקרוגל, טוסטר, מקרר, מזווה.\n"
        "• מכוניות, משרדים, תיקים, כיסי מעילים — פירורים ועטיפות.\n"
        "• מכרו או סיימו חמץ שלא תשמרו; תכננו תפריטים וקניות לפסח.\n"
        "• הגדות, מצה, יין, קערת ליל הסדר — הזמינו לפני עליית מחירים.\n\n"
        "הכשרת כלים לפסח:\n"
        "• כלים שנהמיים סופגים חמץ ממאכלים חמים ושימוש ממושך — לפסח צריך כלים מוכשרים או סט נפרד.\n"
        "• שיטות נפוצות (לכל כלי דינים — שאלו רב): הגעלה, עירוי, ליבון.\n"
        "• התחילו מוקדם — הכשרת מטבח שלם לוקחת זמן."
    ),
    "Practice gratitude! Take a moment to thank G-d for something good in your life - big or small 🙌. Maybe it's your health, your family, or even just that morning coffee! Rabbis teach that When G-d sees us sending thanks, he welcomes our other prayers along with them!": (
        "תרגלו הכרת תודה! קחו רגע להודות לה׳ על משהו טוב בחייכם — גדול או קטן 🙌. "
        "אולי הבריאות, המשפחה, או אפילו הקפה של הבוקר! חז״ל מלמדים שכשה׳ רואה שאנחנו מודים, "
        "הוא מקבל את שאר תפילותינו יחד איתה!"
    ),
    "Sefirat HaOmer — Sefirat HaOmer counts forty-nine days from the second night of Pesach until Shavuot — linking freedom to receiving the Torah. Each night, preferably after nightfall, you bless and announce the day and week count. Missing a full day may affect whether you can say the blessing the remaining nights — ask your rav. During the Omer, many communities observe mourning customs (no music, weddings, haircuts) until Lag BaOmer or Shavuot, depending on minhag to commemorate the plague that killed many of Rabbi Akiva's students during this time.": (
        "ספירת העומר — סופרים ארבעים ותשעה ימים מליל שני של פסח עד שבועות — מחירות לקבלת התורה. "
        "בכל לילה, רצוי אחרי צאת הכוכבים, מברכים ומכריזים את יום ושבוע הספירה. פספוס יום מלא עלול להשפיע על הברכה בלילות הנותרים — שאלו את הרב. "
        "בתקופת העומר קהילות רבות שומרות מנהגי אבל (בלי מוזיקה, חתונות, תספורות) עד ל״ג בעומר או שבועות, לזכר מגפת תלמידי רבי עקיבא."
    ),
    "Tehillim (the Hebrew word for \"praises\") is the traditional Jewish name for the Book of Psalms, which is a biblical collection of 150 sacred poems, prayers, and hymns of gratitude compiled primarily by King David and widely recited for comfort, divine protection, and spiritual connection": (
        "תהילים (המילה העברית ל«שבחים») הוא השם היהודי המסורתי לספר תהילים — אוסף תנ״כי של 150 שירים, "
        "תפילות ומזמורי הודיה, רובם מאת דוד המלך, הנאמרים לנחמה, הגנה וקשר רוחני."
    ),
    "Tehillim — Tehillim (the Hebrew word for \"praises\") is the traditional Jewish name for the Book of Psalms, which is a biblical collection of 150 sacred poems, prayers, and hymns of gratitude compiled primarily by King David and widely recited for comfort, divine protection, and spiritual connection": (
        "תהילים — תהילים (המילה העברית ל«שבחים») הוא השם היהודי המסורתי לספר תהילים — אוסף תנ״כי של 150 שירים, "
        "תפילות ומזמורי הודיה, רובם מאת דוד המלך, הנאמרים לנחמה, הגנה וקשר רוחני."
    ),
    "Tevilat keilim immerses new food utensils manufactured by a non-Jew in a mikveh — recalling the vessels Israel immersed after the Midian war. The obligation follows the manufacturer, not the retailer. Metal and glass usually require a bracha; glazed ceramic often requires immersion without a bracha. Items manufactured by a Jew are generally exempt. Labels \"tovel before use\" remind you — immersion is quick at a keilim mikveh or community event.": (
        "טבילת כלים טובלת כלי מטבח חדשים שיוצר גוי במקווה — לזכר כלים שטבלו ישראל אחרי מלחמת מדין. "
        "החובה עוקבת אחר היצרן, לא המוכר. מתכת וזכוכית בדרך כלל דורשים ברכה; קרמיקה מזוגגת לעיתים טבילה בלי ברכה. "
        "פריטים שיוצר יהודי בדרך כלל פטורים. תוויות «לטבול לפני שימוש» מזכירות — הטבילה מהירה במקווה לכלים או באירוע קהילתי."
    ),
    "This year, today is Rosh Hashana on Friday": (
        "השנה, היום ראש השנה ביום שישי ושבת מתחילה הלילה — רצף יום טוב–שבת בעיצומו.\n\n"
        "אם עשיתם עירוב תבשילין לפני ראש השנה:\n"
        "• מותר לבשל ולהכין אוכל ביום טוב היום לארוחות שבת, במסגרת ההגבלות (אוכל מוכן בזמן לפני שבת; לא לבשל לחול או ליום טוב הקודם).\n"
        "• רבים מכינים חלת דבש, דגים, מרק ומנות שבת/ראש השנה היום.\n"
        "• בדקו שמאכלי העירוב במקום בטוח, גלוי ומסומן — קל לזרוק אותם בבלגן של הכנות יום שישי.\n\n"
        "שופר: נשמע היום (אם היום יום טוב של ראש השנה ולא שבת) לפי לוח בית הכנסת — לא בשבת עצמה.\n\n"
        "אם לא עשיתם עירוב תבשילין: שאלו את הרב מיד מה עדיין מותר להכין לשבת.\n\n"
        "שבת הלילה: כשיום טוב נגמר ביום שישי לפני שבת — הדליקו נרות שבת מאש קיימת ועקבו אחר יקנה״ז לפי המנהג."
    ),
    "Turn your snack into a spiritual feast! 🍎 Pick up some food and say a blessing (bracha) before enjoying it with complete kavannah (complete concentration) on each word of the blessing. Bonus round: If you ate about a golf-ball size (kezayit) within a few minutes, say the after-blessing too! These blessings aren't just 'thank you' notes - they actually elevate the physical food into spiritual energy. Check the app's 'Blessings' menu for the right blessing for your snack!": (
        "הפכו את החטיף לסעודה רוחנית! 🍎 קחו מזון ואמרו ברכה לפני שאוכלים בקוונה מלאה על כל מילה. "
        "בונוס: אם אכלתם ככזית (גודל כדור גולף) תוך דקות ספורות — אמרו גם ברכה אחרונה! "
        "הברכות אינן רק «תודה» — הן מעלות את המזון הפיזי לאנרגיה רוחנית. "
        "בתפריט «ברכות» באפליקציה תמצאו את הברכה המתאימה."
    ),
    "When you acquire new metal or glass dishes": (
        "כשרוכשים כלים חדשים ממתכת או זכוכית שיוצר גוי — נדרשת טבילת כלים לפני מגע באוכל. "
        "קרמיקה מזוגגת (סין, פורצלן וכד׳) לעיתים גם דורשת טבילה — בדרך כלל בלי ברכה.\n\n"
        "מי מחייב: היצרן — לא החנות. סיר מחברה לא יהודית דורש טבילה גם אם נקנה בחנות יהודית; "
        "סיר מיוצר יהודי פטור גם אם נקנה אצל גוי — שאלו רב במקרי גבול.\n\n"
        "מה זה: טבילת כלים (טְבִילַת כֵּלִים) מצווה מקורה בבמדבר ל״א:כ״ג. רוב הפוסקים דנים בה כדרבנן היום.\n\n"
        "איפה: מקווה כשר עם אזור לכלים; קהילות רבות מקיימות אירועי טבילה.\n\n"
        "ברכה: מתכת וזכוכית — «על טבילת כלי»; קרמיקה מזוגגת — לרוב בלי ברכה.\n\n"
        "פטור: פריטים מיצרן יהודי בדרך כלל פטורים."
    ),
    "Yetzer hara is the inclination toward selfishness, laziness, or sin — not a devil, but internal pull. Everyone has it; the battle is lifelong. Torah, mitzvot, and good friends strengthen yetzer hatov. The goal is not to destroy desire but to channel it — appetite for holy things, ambition for good deeds.": (
        "יצר הרע הוא הנטייה לאנוכיות, עצלות או חטא — לא שטן, אלא משיכה פנימית. לכל אחד יש אותו; המלחמה לכל החיים. "
        "תורה, מצוות וחברים טובים מחזקים את יצר הטוב. המטרה אינה להשמיד תשוקה אלא לכוון אותה — "
        "תיאבון לדברים קדושים, שאפתנות למעשים טובים."
    ),
    "The chuppah is the wedding canopy representing the couple's new home — open on all sides like Abraham's tent. The ceremony includes kiddushin (betrothal) and nisuin under the chuppah. Breaking the glass recalls the destruction of the Temple. It is a seudat mitzvah with dancing and simcha.": (
        "החופה היא סוכת החתונה המייצגת את ביתם החדש של בני הזוג — פתוחה מכל הצדדים כמו אוהלו של אברהם. "
        "הטקס כולל קידושין ונישואין תחת החופה. שבירת הכוס מזכירה את חורבן בית המקדש. "
        "זו סעודת מצווה עם ריקודים ושמחה."
    ),
    "Yom HaZikaron (4 Iyar) is Israel's national day": (
        "יום הזיכרון (ד׳ באייר) הוא יום הזיכרון הלאומי לחללי צה״ל ולנפגעי טרור שנפלו למען מדינת ישראל. "
        "הוקם בכנסת ב-1963 ותמיד חל ביום שלפני יום העצמאות.\n\n"
        "זה לא יום טוב — מלאכה מותרת לחלוטין. זהו יום אזרחי לאומי.\n\n"
        "התאמת תאריך: האפליקציה מתאימה את התאריך כשד׳ באייר קרוב לשבת, כדי לשמור על יום הזיכרון ויום העצמאות בימים עוקבים ולא סמוך לשבת.\n\n"
        "בישראל: סירנות זיכרון ב-20:00 (תחילת היום, בצאת הכוכבים) ושוב ב-11:00 בבוקר. טקסים בבתי עלמין צבאיים. דגלים ברום חצי התורן.\n\n"
        "תפילה (ציונות דתית): תחנון נאמר במלואו בשחרית — יום זיכרון לאומי חגור. תחנון חסר רק במנחה עם מעבר לחגיגות יום העצמאות (פניני הלכה ה׳:ד)."
    ),
}

TORAH_MEANS_ES = (
    "La Torá significa «enseñanza» o «instrucción». En sentido estricto son los cinco libros de la Torá — "
    "de Génesis a Deuteronomio (Bereishit a Devarim en hebreo) — dados en el Sinaí. "
    "En la sinagoga se lee de un rollo de Sefer Torá escrito a mano. "
    "En el estudio también puede incluir Mishná, Talmud, halajá y otras obras que explican cómo vivir según la voluntad de D-s."
)

HALACHA_MEANS_ES = (
    "La halajá significa «el camino de caminar» — la vía práctica de la vida judía extraída de la Torá, "
    "los Profetas, los Escritos, la Mishná, el Talmud y códigos como el Shulján Aruj. "
    "Abarca oración, Shabat, dieta, vida familiar, ética de negocios y festivales. "
    "La halajá la determinan poskim calificados; cuando no esté seguro, la mitzvá es preguntar a su rav y no adivinar."
)

TORAH_MEANS_FR = (
    "La Torah signifie « enseignement » ou « instruction ». Au sens strict, ce sont les cinq livres de la Torah — "
    "de Bereishit à Devarim — donnés au Sinaï. À la synagogue, on lit la Torah depuis un rouleau de Sefer Torah manuscrit. "
    "Dans l'étude, « Torah » peut aussi désigner la Michna, le Talmud, la halakha et d'autres ouvrages "
    "qui expliquent comment vivre selon la volonté de D."
)

HALACHA_MEANS_FR = (
    "La halakha signifie « la voie de la marche » — le chemin pratique de la vie juive tiré de la Torah, "
    "des Prophètes, des Écrits, de la Michna, du Talmud et de codes comme le Choulhan Aroukh. "
    "Elle couvre la prière, Chabbat, la cacherout, la vie de famille, l'éthique des affaires et les fêtes. "
    "La halakha est décidée par des poskim qualifiés ; en cas de doute, la mitzva est de demander à votre rav plutôt que de deviner."
)

MANUAL_ES: dict[str, str] = {
    "Chilul Hashem is desecrating G-d's Name — behavior that makes Torah or Jews look contemptible, especially in public. Fraud, rage, or hypocrisy by a visibly Jewish person harms the whole people. The opposite is kiddush Hashem — sanctifying G-d's Name through integrity. Non-Jews judging Judaism by your conduct is a serious responsibility.": (
        "Jilul Hashem es la profanación del Nombre de D-s — conducta que deshonra la Torá o a los judíos, "
        "sobre todo en público. Fraude, ira o hipocresía de alguien visiblemente judío daña a todo el pueblo. "
        "Lo opuesto es kidush Hashem — santificar el Nombre de D-s con integridad. "
        "Que los no judíos juzguen el judaísmo por su conducta es una responsabilidad seria."
    ),
    "Normal": "Estándar",
    "Torah means \"teaching\" or \"instruction.\" In the narrow sense it is the five books of the Torah — Genesis through Deuteronomy in English, Bereishit through Devarim in Hebrew — given at Sinai. In shul, the Torah is read from a handwritten Sefer Torah scroll. In study, it can also mean Mishnah, Talmud, halacha, and other works that explain how to live by G-d's will.": TORAH_MEANS_ES,
    "Torah — Torah means \"teaching\" or \"instruction.\" In the narrow sense it is the five books of the Torah — Genesis through Deuteronomy in English, Bereishit through Devarim in Hebrew — given at Sinai. In shul, the Torah is read from a handwritten Sefer Torah scroll. In study, it can also mean Mishnah, Talmud, halacha, and other works that explain how to live by G-d's will.": (
        "Torá — " + TORAH_MEANS_ES
    ),
    "Halacha means \"the way of walking\" — the practical path of Jewish life drawn from Torah, Prophets, Writings, Mishnah, Talmud, and codes like the Shulchan Aruch. It covers prayer, Shabbat, diet, family life, business ethics, and festivals. Halacha is decided by qualified poskim; when you are unsure, the mitzvah is to ask your rav rather than guess.": HALACHA_MEANS_ES,
    "halacha — Halacha means \"the way of walking\" — the practical path of Jewish life drawn from Torah, Prophets, Writings, Mishnah, Talmud, and codes like the Shulchan Aruch. It covers prayer, Shabbat, diet, family life, business ethics, and festivals. Halacha is decided by qualified poskim; when you are unsure, the mitzvah is to ask your rav rather than guess.": (
        "halajá — " + HALACHA_MEANS_ES
    ),
}

MANUAL_FR: dict[str, str] = {
    "Torah means \"teaching\" or \"instruction.\" In the narrow sense it is the five books of the Torah — Genesis through Deuteronomy in English, Bereishit through Devarim in Hebrew — given at Sinai. In shul, the Torah is read from a handwritten Sefer Torah scroll. In study, it can also mean Mishnah, Talmud, halacha, and other works that explain how to live by G-d's will.": TORAH_MEANS_FR,
    "Torah — Torah means \"teaching\" or \"instruction.\" In the narrow sense it is the five books of the Torah — Genesis through Deuteronomy in English, Bereishit through Devarim in Hebrew — given at Sinai. In shul, the Torah is read from a handwritten Sefer Torah scroll. In study, it can also mean Mishnah, Talmud, halacha, and other works that explain how to live by G-d's will.": (
        "Torah — " + TORAH_MEANS_FR
    ),
    "Halacha means \"the way of walking\" — the practical path of Jewish life drawn from Torah, Prophets, Writings, Mishnah, Talmud, and codes like the Shulchan Aruch. It covers prayer, Shabbat, diet, family life, business ethics, and festivals. Halacha is decided by qualified poskim; when you are unsure, the mitzvah is to ask your rav rather than guess.": HALACHA_MEANS_FR,
    "halacha — Halacha means \"the way of walking\" — the practical path of Jewish life drawn from Torah, Prophets, Writings, Mishnah, Talmud, and codes like the Shulchan Aruch. It covers prayer, Shabbat, diet, family life, business ethics, and festivals. Halacha is decided by qualified poskim; when you are unsure, the mitzvah is to ask your rav rather than guess.": (
        "halakha — " + HALACHA_MEANS_FR
    ),
}

MANUAL_RU: dict[str, str] = {
    "Mitz Mode!": "Митц-Мод!",
}
