"""Human-quality glossary fixes batch 3 — FR/RU glossary + Hebrew holiday texts."""

from __future__ import annotations

BATCH3_FR: dict[str, str] = {
    "Kitzur Shulchan Arukh — The Kitzur Shulchan Arukh is a condensed summary of daily halacha — prayer, Shabbat, kashrut, festivals, and family life — written by Rabbi Shlomo Ganzfried. It is a practical first halacha book for beginners and a quick reference for experienced Jews.": (
        "Kitzur Shulchan Arukh — Le Kitzur Choulhan Aroukh est un résumé condensé de la halakha quotidienne — "
        "prière, Chabbat, cacherout, fêtes et vie de famille — rédigé par le rabbin Shlomo Ganzfried. "
        "C'est un premier livre de halakha pratique pour les débutants et une référence rapide pour les juifs expérimentés."
    ),
    "The Kitzur Shulchan Arukh is a condensed summary of daily halacha — prayer, Shabbat, kashrut, festivals, and family life — written by Rabbi Shlomo Ganzfried. It is a practical first halacha book for beginners and a quick reference for experienced Jews.": (
        "Le Kitzur Choulhan Aroukh est un résumé condensé de la halakha quotidienne — prière, Chabbat, cacherout, "
        "fêtes et vie de famille — rédigé par le rabbin Shlomo Ganzfried. C'est un premier livre de halakha pratique "
        "pour les débutants et une référence rapide pour les juifs expérimentés."
    ),
    "Kosher means fit for use per halacha — especially food, but also valid scrolls and acceptable conduct (e.g. kosher money for mitzvot). Kosher is not a health label; it is spiritual fitness. When in doubt on a product, call the hechsher company or your rav.": (
        "Kasher signifie « apte » selon la halakha — surtout pour la nourriture, mais aussi pour des rouleaux valides "
        "et une conduite acceptable (par ex. argent kasher pour les mitzvot). Kasher n'est pas un label sanitaire ; "
        "c'est une aptitude spirituelle. En cas de doute sur un produit, appelez l'organisme de hekhsher ou votre rav."
    ),
}

BATCH3_RU: dict[str, str] = {
    "Neshama is the soul — the divine breath that makes you alive. Modeh Ani thanks G-d for returning it after sleep. Yizkor and Kaddish relate to the soul's journey after death. Jewish thought distinguishes levels of soul (nefesh, ruach, neshama) in mysticism; practically, nurturing the soul means Torah, mitzvot, and character.": (
        "Нешама — это душа, божественное дыхание, которое делает вас живым. Modeh Ani благодарит В-го за то, "
        "что Он возвращает её после сна. Yizkor и Kaddish связаны с путешествием души после смерти. "
        "Еврейская мысль различает уровни души (нефеш, руах, нешама) в мистицизме; на практике "
        "воспитание души означает Тору, мицвот и характер."
    ),
    "neshama — Neshama is the soul — the divine breath that makes you alive. Modeh Ani thanks G-d for returning it after sleep. Yizkor and Kaddish relate to the soul's journey after death. Jewish thought distinguishes levels of soul (nefesh, ruach, neshama) in mysticism; practically, nurturing the soul means Torah, mitzvot, and character.": (
        "нешама — Нешама — это душа, божественное дыхание, которое делает вас живым. Modeh Ani благодарит В-го "
        "за возвращение души после сна. Yizkor и Kaddish связаны с путешествием души после смерти. "
        "На практике воспитание души означает Тору, мицвот и характер."
    ),
    "shamash — On Chanukah, the shamash is the helper candle used to light the others; its light is not counted among the mitzvah candles, so you may use it for utility light. Do not use the actual mitzvah flames for reading or work — they are holy and forbidden for personal benefit (bizuy mitzvah). In a shul, the shamash often means the caretaker who maintains the building and/or assists the rabbi/services.": (
        "шамаш — На Хануке шамаш — вспомогательная свеча, которой зажигают остальные; её свет не считается "
        "среди свечей мицвы, поэтому ею можно пользоваться как бытовым освещением. "
        "Не используйте пламя мицвы для чтения или работы — это свято и запрещено для личной выгоды "
        "(бизуй мицвы). В синагоге shamash часто означает смотрителя здания и/или помощника раввина."
    ),
    "A Sefer Torah is a Torah scroll — the entire five books written by a sofer on kosher parchment, used for public reading. Treating it with respect — standing when it moves, not touching the text directly — reflects its holiness.": (
        "Сефер Тора — это свиток Торы: все пять книг, написанные софером на кошерном пергаменте, "
        "для публичного чтения. Относиться к нему с уважением — вставать, когда он проходит, "
        "не касаться текста напрямую — отражает его святость."
    ),
    "A bentcher (birkon) is a small booklet with Birkat Hamazon and sometimes Shabbat songs and benching additions. Keeping one at the table makes bentching after bread meals easy. Many are bilingual; some include zimun instructions and festival inserts.": (
        "Бентчер (биркон) — небольшая книжица с Биркат а-Мазон и иногда шабатными песнями и дополнениями "
        "к бентчингу. Держать её на столе облегчает бентчинг после трапезы с хлебом. "
        "Многие двуязычные; некоторые включают указания по зиммуну и вставки для праздников."
    ),
    "A keli is a vessel, utensil, or tool. It generally refers to any manufactured object or receptacle that holds, processes, or contains something. A keli is used for netilat yadayim — a cup, bottle, or washing cup (usually) with two handles.": (
        "Кели — сосуд, принадлежность или инструмент. Обычно это любой изготовленный предмет или ёмкость, "
        "что-то вмещающая или содержащая. Кели используется для нетилат ядаим — чашка, бутылка "
        "или кувшин для омовения рук (обычно с двумя ручками)."
    ),
    "A posek is a rabbi qualified to decide difficult halachic questions and write authoritative rulings. Your rav may consult poskim on novel cases. The term differs from a maggid or teacher who inspires but does not pasken. Major poskim include figures behind the Mishnah Berurah, Igrot Moshe, and contemporary halachic works your community follows.": (
        "Посек — раввин, способный решать сложные галахические вопросы и писать авторитетные постановления. "
        "Ваш рав может консультироваться с посеким в новых случаях. Термин отличается от маггида или учителя, "
        "который вдохновляет, но не паскенит. Крупные посеким — авторы Мишна Берура, Игрот Моше "
        "и современных галахических трудов, которым следует ваша община."
    ),
    "A sofer (scribe) is trained to write STaM — Torah scrolls, tefillin, mezuzot, and megillot — by hand with special ink on parchment. Letters must be formed exactly; mistakes can invalidate the scroll. Soferim also check existing klafim. Never buy tefillin or mezuzot without reliable certification.": (
        "Софер (письмоводитель) обучен писать STaM — свитки Торы, тфилин, мезузот и мегилот — "
        "вручную специальными чернилами на пергаменте. Буквы должны быть сформированы точно; "
        "ошибка может сделать свиток недействительным. Соферим также проверяют существующий клаф. "
        "Никогда не покупайте тфилин или мезузот без надёжной сертификации."
    ),
    "A sukkah is a temporary booth with schach roof where we eat (and some sleep) on Sukkot — recalling desert clouds of glory. Walls must stand; schach must shade more than sun. Decorate for joy; invite guests and ushpizin. Rain may exempt you from eating in the sukkah — follow halacha for your situation.": (
        "Сукка — временная хижина с крышей из схах, где мы едим (и некоторые спят) на Суккот — "
        "в память о пустынных облаках славы. Стены должны стоять; схах должен давать больше тени, чем солнца. "
        "Украшайте для радости; приглашайте гостей и ушпизин. Дождь может освобождать от еды в сукке — "
        "следуйте галахе для вашей ситуации."
    ),
    "Amen affirms a bracha someone else recited, \"so be it.\" The Talmud praises one who answers Amen; responding Amen is considered greater than making the blessing oneself. Answer Amen clearly when you hear a bracha.": (
        "Амен подтверждает браху, которую произнёс другой — «да будет так». Талмуд хвалит отвечающего «Амен»; "
        "ответ «Амен» считается важнее самого благословения. Отвечайте «Амен» чётко, когда слышите браху."
    ),
    "Amen — Amen affirms a bracha someone else recited, \"so be it.\" The Talmud praises one who answers Amen; responding Amen is considered greater than making the blessing oneself. Answer Amen clearly when you hear a bracha.": (
        "Амен — «Амен» подтверждает браху другого — «да будет так». Талмуд хвалит отвечающего «Амен»; "
        "ответ «Амен» важнее самого благословения."
    ),
}

BATCH3_HE: dict[str, str] = {
    "Shemini Atzeret (22 Tishrei in the Diaspora) — eighth day of Sukkot.\n\nYom Tov:\n• Full Yom Tov — no melacha; Kiddush and festive meals.\n• Sukkah in the Diaspora: Due to safek dyoma (halachic doubt which day is which), Diaspora Ashkenazim are required to eat all major meals in the sukkah on Shemini Atzeret, though leishev basukkah is omitted entirely. Sephardic and Chabad customs vary — confirm with your rav.\n\nDavening:\n• Liturgical shift: During Musaf today, the entire Jewish world officially transitions to the winter prayer cycle, universally inserting \"Mashiv HaRuach U'Morid HaGeshem\" into the second blessing of the Amidah. Tefillat Geshem is recited in Musaf.\n• Yizkor — Ashkenaz communities recite memorial prayers.\n• Full Hallel and Musaf; Yom Tov Amidah.\n\nSimchat Torah is tomorrow (23 Tishrei) in the Diaspora — see that day's checklist item for hakafot and Torah joy.\n\nTonight: light Yom Tov candles; no lulav on Shemini Atzeret.": (
        "שמיני עצרת (כ״ב בתשרי בחו״ל) — היום השמיני של סוכות.\n\nיום טוב:\n"
        "• יום טוב מלא — אין מלאכה; קידוש וסעודות חג.\n"
        "• סוכה בחו״ל: בשל ספק דיומא, אשכנזי חו״ל נוהגים לאכול את כל הסעודות העיקריות בסוכה "
        "בשמיני עצרת, אך אין אומרים «לישב בסוכה» כלל. מנהגי ספרדים וחב״ד משתנים — שאלו את הרב.\n\n"
        "תפילה:\n"
        "• מעבר ליטורגי: במוסף היום העולם היהודי עובר רשמית למחזור חורף — מוסיפים «משיב הרוח "
        "ומוריד הגשם» בברכה השנייה של העמידה. תפילת גשם במוסף.\n"
        "• יזכור — בקהילות אשכנז.\n"
        "• הלל מלא ומוסף; עמידת יום טוב.\n\n"
        "שמחת תורה מחר (כ״ג בתשרי) בחו״ל — ראו פריט רשימת אותו יום להקפות ושמחת תורה.\n\n"
        "הערב: הדלקת נרות יום טוב; אין לולב בשמיני עצרת."
    ),
    "Shemini Atzeret / Simchat Torah (22 Tishrei in Israel) — one day combining both.\n\nYom Tov:\n• Full Yom Tov — no melacha; festive meals with Kiddush and Shehecheyanu (if not said already).\n• Sukkot has ended — in Israel, do not eat or sleep in the sukkah today; festive meals are indoors (not in the sukkah).\n\nDavening highlights:\n• Liturgical shift: During Musaf today, the entire Jewish world officially transitions to the winter prayer cycle, universally inserting \"Mashiv HaRuach U'Morid HaGeshem\" into the second blessing of the Amidah. Tefillat Geshem (the formal prayer for rain) is recited in Musaf.\n• Yizkor — memorial prayer in many Ashkenaz communities.\n• Hakafot — dancing with Torah scrolls; finish the annual Torah reading and begin Bereshit again.\n• Synagogue note: Because drinking often occurs during daytime hakafot, many synagogues move the Priestly Blessing (Birkat Kohanim) up to the early morning Shacharit service instead of keeping it in Musaf, so Kohanim are completely sober for the blessing.\n• Full Hallel and Musaf; Yom Tov Amidah.\n\nSimchat Torah joy:\n• Everyone receives an aliyah in many shuls; Kol HaNearim (children's aliyah) with flags — an adult (or a boy over bar mitzvah) stands with the group and recites the Torah blessing aloud so the aliya is halachically valid.\n• Singing, dancing — honor the day with Torah celebration.\n\nEvening: Candle lighting and Yom Tov; morning services are long — plan accordingly.": (
        "שמיני עצרת / שמחת תורה (כ״ב בתשרי בישראל) — יום אחד המשלב את שניהם.\n\n"
        "יום טוב:\n"
        "• יום טוב מלא — אין מלאכה; סעודות חג עם קידוש ושהחיינו (אם לא נאמר כבר).\n"
        "• הסוכות הסתיימה — בישראל אין אוכלים ולא ישנים בסוכה היום; הסעודות בפנים.\n\n"
        "תפילה:\n"
        "• מעבר ליטורגי: במוסף היום מוסיפים «משיב הרוח ומוריד הגשם» בברכה השנייה; תפילת גשם במוסף.\n"
        "• יזכור — בקהילות אשכנז רבות.\n"
        "• הקפות — ריקודים עם ספרי תורה; סיום קריאת השנה והתחלה מבראשית.\n"
        "• הערה: בגלל שתייה בהקפות יום, בתי כנסת רבים מעבירים ברכת כהנים לשחרית "
        "במקום מוסף, כדי שהכהנים יהיו צלולים.\n"
        "• הלל מלא ומוסף; עמידת יום טוב.\n\n"
        "שמחת תורה:\n"
        "• בבתי כנסת רבים כולם מקבלים עלייה; קול הנערים עם דגלים — מבוגר (או בן מצווה) "
        "עומד עם הקבוצה ואומר את ברכת התורה בקול.\n"
        "• שירה וריקוד — כבוד היום בחגיגת התורה.\n\n"
        "ערב: הדלקת נרות ויום טוב; שירותי הבוקר ארוכים — תכננו בהתאם."
    ),
    "Yom Ha'atzmaut (5 Iyar) commemorates Israeli independence in 1948.\n\nCustoms vary significantly by community:\n\nReligious Zionist / Modern Orthodox: Hallel is recited (instituted by the Chief Rabbinate of Israel). Whether Hallel is said with a bracha is disputed — the Chief Rabbinate instructed with a bracha; many Ashkenazi poskim outside Israel say without a bracha. Tachanun is omitted. Some communities add special festive prayers (Hallel u'Maariv).\n\nSephardic (Rav Ovadia Yosef / Yalkut Yosef): Rav Ovadia Yosef ruled that Hallel should not be recited (concern of bracha levatala since the day was not established by Chazal). Tachanun omission is also disputed in these communities.\n\nChabad: The Rebbe did not institute any special observance. Most Chabad communities do not say Hallel and recite Tachanun as usual.\n\nCharedi communities (Agudah, Litvish): Generally do not observe the day as a religious holiday. Tachanun is said as usual.\n\nThe Omer continues to be counted normally. There is no Al HaNissim addition to davening.\n\nAsk your rav which custom your community follows.": (
        "יום העצמאות (ה׳ באייר) מנציח את עצמאות ישראל ב-1948. זה לא יום טוב תנ״כי או רבני — מלאכה מותרת לחלוטין.\n\n"
        "המנהגים משתנים לפי קהילה:\n\n"
        "ציונות דתית / אורתודוכסיה מודרנית: אומרים הלל (על פי הרבנות הראשית). "
        "האם עם ברכה — שנוי במחלוקת; הרבנות הראשית הנחתה עם ברכה, רבים מחוץ לישראל בלי ברכה. "
        "תחנון נדחה. יש קהילות עם תפילות חגיגיות מיוחדות.\n\n"
        "ספרדים (הרב עובדיה יוסף / ילקוט יוסף): הרב עובדיה פסק שלא לומר הלל "
        "(חשש ברכה לבטלה — היום לא הוקם על ידי חז״ל). הדחיית תחנון גם שנויה במחלוקת.\n\n"
        "חב״ד: הרבי לא קבע מנהג מיוחד. רוב קהילות חב״ד לא אומרות הלל ואומרות תחנון כרגיל.\n\n"
        "חרדים: בדרך כלל לא רואים ביום חג דתי. תחנון כרגיל.\n\n"
        "ספירת העומר ממשיכה כרגיל. אין תוספת אל הניסים בתפילה.\n\n"
        "שאלו את הרב לפי מנהג הקהילה."
    ),
    "Yom Yerushalayim (28 Iyar) marks the reunification of Jerusalem during the Six-Day War in 1967 (5727).\n\nCustoms vary by community:\n\nReligious Zionist / Dati Leumi: Hallel is recited (with or without a bracha, depending on the posek and community). Tachanun is omitted. Some communities recite special tefillot.\n\nSephardic (Rav Ovadia Yosef): Hallel is not recited for the same reason as Yom Ha'atzmaut — not established by Chazal. Practice varies.\n\nChabad and Charedi communities: Generally no special observance. Tachanun is said as usual.\n\nYom Yerushalayim is observed by fewer communities than Yom Ha'atzmaut, and there is no universally accepted halachic obligation. Ask your rav about your community's custom.": (
        "יום ירושלים (כ״ח באייר) מסמן את איחוד ירושלים במלחמת ששת הימים ב-1967 (תשכ״ז). "
        "זה לא יום טוב — מלאכה מותרת לחלוטין.\n\n"
        "המנהגים משתנים לפי קהילה:\n\n"
        "ציונות דתית / דתי לאומי: אומרים הלל (עם או בלי ברכה — לפי הפוסק והקהילה). תחנון נדחה. "
        "יש קהילות עם תפילות מיוחדות.\n\n"
        "ספרדים (הרב עובדיה יוסף): לא אומרים הלל מאותה סיבה כמו ביום העצמאות — לא הוקם על ידי חז״ל.\n\n"
        "חב״ד וחרדים: בדרך כלל אין מנהג מיוחד. תחנון כרגיל.\n\n"
        "יום ירושלים נצפה בפחות קהילות מיום העצמאות, ואין חובה הלכתית מקובלת. שאלו את הרב."
    ),
}

AFTER_FR = (
    "Après avoir mangé ou bu, nous remercions D. avec une bénédiction postérieure (berakha aḥrona). "
    "Laquelle dépend de ce que vous avez consommé — et si la quantité et le délai requis sont atteints.\n\n"
    "Les bénédictions postérieures :\n\n"
    "Birkat Hamazon (בִּרְכַּת הַמָּזוֹן — Grâce après les repas) :\n"
    "• Après le pain (Hamotzi)\n"
    "• Prière de remerciement complète ; commandement au niveau de la Torah (Deutéronome 8:10)\n"
    "• Trois hommes juifs adultes ou plus : Zimmun (זִמּוּן) avant Birkat Hamazon\n"
    "• Un bentcher est un petit livret avec le texte — courant sur les tables de Chabbat\n"
    "• Après un repas avec pain : un kezayit dans k'dei achilat pras — Birkat Hamazon couvre "
    "tout le reste de ce repas (vin, viande, fruits, dessert, etc.)\n\n"
    "Al HaMichya (עַל הַמִּחְיָה) :\n"
    "• Après Mezonot — aliments de graine qui ne sont pas du pain\n"
    "• Bénédiction condensée « une comme trois », dérivée de Birkat Hamazon\n\n"
    "Al HaGafen (עַל הַגָּפֶן וְעַל פְּרִי הַגָּפֶן) :\n"
    "• Après vin ou jus de raisin — au moins un revi'it dans k'dei shtiyat revi'it\n\n"
    "Al HaEtz (עַל הָעֵץ וְעַל פְּרִי הָעֵץ) :\n"
    "• Après fruits des shivat ha-minim (dattes, figues, grenades, olives, raisins frais)\n\n"
    "Borei Nefashot (בּוֹרֵא נְפָשׁוֹת) :\n"
    "• Après la plupart des autres aliments et boissons (sauf vin)\n\n"
    "Quantité — k'dei achilat pras : environ un kezayit (30 g) en 4–9 minutes.\n"
    "Boisson — k'dei shtiyat revi'it : un revi'it en quelques secondes continues.\n\n"
    "Texte exact : ouvrez votre siddour ou bentcher, section berakha aḥrona. "
    "Dans Mitz Mode : menu (⋮) → **Bénédictions**."
)

AFTER_RU = (
    "После еды или питья мы благодарим В-га благословением ахарон (браха ахрона). "
    "Какое именно — зависит от того, что вы ели или пили, и достаточно ли вы съели или выпили "
    "в требуемое время.\n\n"
    "Благословения после еды:\n\n"
    "Биркат а-Мазон (בִּרְכַּת הַמָּזוֹן — благодарение после трапезы):\n"
    "• После хлеба (Хамоци)\n"
    "• Полная молитва благодарения; заповедь уровня Торы (Второзаконие 8:10)\n"
    "• Когда три или более взрослых мужчин едят хлеб вместе — Зиммун (זִמּוּן) перед Биркат а-Мазон\n"
    "• Бентчер — небольшая книжица с текстом, обычно на шабатных столах\n"
    "• После трапезы с хлебом: если съели хотя бы кезайит в пределах k'dei achilat pras, "
    "Биркат а-Мазон покрывает всё остальное за той же трапезой\n\n"
    "Ал hа-Михья (עַל הַמִּחְיָה):\n"
    "• После мезонот — зерновые продукты, не являющиеся хлебом\n"
    "• Сжатое «одно как три», производное от Биркат а-Мазон\n\n"
    "Ал hа-Гафен (עַל הַגָּפֶן וְעַל פְּרִי הַגָּפֶן):\n"
    "• После вина или виноградного сока — revi'it в k'dei shtiyat revi'it\n\n"
    "Ал hа-Эц (עַל הָעֵץ וְעַל פְּרִי הָעֵץ):\n"
    "• После плодов шиват hа-миним (финики, инжир, гранаты, оливки, свежий виноград)\n\n"
    "Борей Нефашот (בּוֹרֵא נְפָשׁוֹת):\n"
    "• После большинства других продуктов и напитков (кроме вина)\n\n"
    "Количество — k'dei achilat pras: примерно кезайит (~30 г) за 4–9 минут.\n"
    "Питьё — k'dei shtiyat revi'it: revi'it за несколько непрерывных секунд.\n\n"
    "Точный текст: откройте сидур или бентчер, раздел браха ахрона. "
    "В Mitz Mode: меню (⋮) → **Благословения**."
)
