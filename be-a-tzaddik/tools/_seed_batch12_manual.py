#!/usr/bin/env python3
"""One-shot: write _ru_batch12_manual.json with explicit key→RU mappings for batch 12."""
from __future__ import annotations

import json
from pathlib import Path

CAND = Path(__file__).parent / "_batch12_candidates.json"
OUT = Path(__file__).parent / "_ru_batch12_manual.json"

SKIP_KEYS = {
    "Beardy Top Productions",
    "www.beardy.top",
    "https://www.beardy.top",
    "XL",
    "Rav",
    "e.g., Sarah B.",
    "Listen to more Jewish music from G.E.U.L.A",
    "Performed by G.E.U.L.A © 2026",
}

MANUAL: dict[str, str] = {
    "alot hashachar — Alot hashachar is halachic dawn — when the sky begins to lighten. Many morning laws start here, such as the beginning of public fast days. It is before sunrise (netz). It is not the normal time for the bracha on tallit and tefillin — see misheyakir. In great need, Igros Moshe (O.C. 4:6) allows putting them on after alot hashachar without a bracha and reciting the brachos after misheyakir — ask your rav.": (
        "алот hа-шахар — галахический рассвет, когда небо начинает светлеть; начало общественных постов; до netz. "
        "Браха на талит и тфилин — после misheyakir; в нужде — Игрот Моше О.Х. 4:6, спросите рава."
    ),
    "bal yera'eh": "баль йераэ",
    "geneivat da'at — Geneivat da'at (\"stealing the mind\") is deception — advertising falsely, hiding defects, or misleading about credentials. It applies to customers, employees, and friends. The Torah demands \"honest scales\" in spirit and letter. Trust lost through geneivat da'at is hard to rebuild.": (
        "генейват даат («воровство ума») — обман: ложная реклама, сокрытие недостатков, введение в заблуждение. "
        "Тора требует «честных весов» в духе и букве. Потерянное доверие трудно вернуть."
    ),
    "honoring your parents — Kibud Av V'Eim - mitzvah to honor/respect and care for parents": (
        "почитание родителей — кибуд ав ва-эйм: мицва чтить, уважать и заботиться о родителях"
    ),
    "kisui rosh": "кисуй роsh",
    "klaf": "клаф",
    "oleh": "олех",
    "paragraph sung after lighting Chanukah candles": "отрывок, поющийся после зажигания свечей Хануки",
    "seudat mitzvah — festive meal tied to a mitzvah (e.g. brit, siyum, wedding, etc.)": (
        "сеудат мицва — праздничная трапеза, связанная с мицвой (брит, сиум, свадьба и т.д.)"
    ),
    "shofar": "шофар",
    "sofer — A sofer (scribe) is trained to write STaM — Torah scrolls, tefillin, mezuzot, and megillot — by hand with special ink on parchment. Letters must be formed exactly; mistakes can invalidate the scroll. Soferim also check existing klafim. Never buy tefillin or mezuzot without reliable certification.": (
        "софер обучен писать ШТаМ — свитки Торы, тфилин, мезузот и мегилот — вручную на пергаменте. "
        "Ошибка может аннулировать свиток. Покупайте только с надёжным хешером."
    ),
    "Al hamichya (Al HaMichya) is the after-blessing on mezonot, grain foods that are not bread — cake, crackers, pasta, cereal, etc. — when you ate a kezayit (approx. golf ball size of food) within k'dei achilat pras (4-9 mins). If you had a bread meal, Birkat Hamazon covers all foods. You may also need to say Birkat Hamazon if you ate a lot of mezonot- depending on the type of food. Ask your rav.": (
        "Ал hа-михья — послеблагословение на мезонот (выпечка, макароны, крупы и т.д.), когда съели кезайит "
        "в пределах кдей ахилат прас (4–9 мин). После хлебной трапезы — Биркат а-Мазон покрывает всё. Спросите рава."
    ),
    "Birkat Ha'Ilanot (בִּרְכַּת הָאִילָנוֹת — Blessing of the Trees) is recited once each Jewish year upon seeing fruit trees in bloom — thanking Hashem for the beauty and renewal of creation.\n\nThis checklist appears during the likely season for your hemisphere — a reminder to go look for blossoms, not an obligation to recite before you see them.\n\n$whenBlock\n\nThe blessing (said once per year):\nBaruch Atah Ado-nai Eloheinu Melech ha'olam shelo chiser be'olamo kelum u'vara vo beriyot tovot v'ilanot tovim lehanot bahem benei adam.\n(בָּרוּךְ אַתָּה ה' אֱלֹקֵינוּ מֶלֶךְ הָעוֹלָם שֶׁלֹּא חִסֵּר בְּעוֹלָמוֹ כְּלוּם וּבָרָא בוֹ בְּרִיּוֹת טוֹבוֹת וְאִילָנוֹת טוֹבִים לֵהָנוֹת בָּהֶם בְּנֵי אָדָם)\n\"Blessed are You, L-rd our G-d, King of the universe, Who has withheld nothing from His world and created in it good creatures and good trees for people to enjoy.\"\n\nHow to fulfill it:\n• Go outdoors to fruit-bearing trees — at least one tree; preferably two or more of the same or different species.\n• Recite the blessing once while viewing the blossoms. Many add Tehillim 104 or other verses; follow your siddur or community custom.\n• Once a year: if you said it in your local spring, you do not repeat it when traveling to another hemisphere later in the same Jewish year.\n• Many communities avoid saying it on Shabbat — go during the week when you first see suitable blossoms.\n\nWhat stage must the tree be in (melav'lave — מְלַבְלְבֵי)?\nThe rabbis require the tree to be actively flowering — not merely sprouting leaves. Shulchan Arukh O.C. 226:1 defines the Talmudic stage of melav'lave as when trees \"put forth flowers\" (shemotzi'in perach): you need open, visible blossoms that you can recognize as flowers. The blessing praises Hashem for creating \"good trees for people to enjoy\" — that response is meant to come from seeing beautiful, open blooms.\n\nL'chatchilah (ideal): the tree has moved past the leaf-only stage and shows open flowers, but the petals have not yet fallen and fruit has not begun to form.\n\nWhat does not work:\n• Green leaf buds alone — invalid; do not recite the blessing on leaves without open flowers.\n• After petals have dropped and fruit is forming — too late for this year's blessing.\n\nBedieved (after the fact): some contemporary poskim allow reciting on swollen, tightly closed flower buds that are clearly about to open when no properly blooming trees are available — ask your rav if that is your only option.\n\nSpiritual note:\nNissan is the month of redemption; blossoming trees recall that creation itself waits to praise Hashem. Even a brief walk among flowering orchards can be a mitzvah.": (
        "Биркат hа-Иланот (בִּרְכַּת הָאִילָנוֹת) — раз в еврейский год при виде цветущих плодовых деревьев.\n\n"
        "$whenBlock\n\n"
        "Благословение (раз в год): «Барух Ата Адонай… шело хисер бе-оламо клум увара во бериот товот в-иланот товим…»\n\n"
        "Выйдите к плодовым деревьям (лучше два и более), произнесите один раз, глядя на цветение. "
        "Раз в год; многие избегают в Шаббат.\n\n"
        "Стадия мелавлеве (מְלַבְלְבֵי): нужны открытые цветы, не только листья (О.Х. 226:1). "
        "Листья без цветов — не годится; после опадания лепестков — поздно. Спросите рава при сомнении."
    ),
    "Bracha": "браха",
    "Call someone who is lonely.\n\nSubmitted by: Amy": "Позвоните кому-то одинокому.\n\nОт Amy",
    "Call someone who is lonely. Submitted by: Amy. Our sages warn never to separate yourself from the community — because a Jew needs connection to thrive. Visiting the sick removes one-sixtieth of their suffering, and loneliness can be its own kind of illness. A phone call, a message, or a visit tells someone they matter — and loving your fellow Jew is one of the foundations of everything we stand for.": (
        "Позвоните одинокому человеку. От Amy. Мудрецы предупреждают: не отделяйтесь от общины — еврею нужна связь. "
        "Посещение больного снимает одну шестидесятую страдания; одиночество — своя болезнь. "
        "Звонок или визит говорит человеку, что он важен."
    ),
    "Chofetz Chaim": "Хафец Хаим",
    "Choose city now (no GPS)": "Выбрать город (без GPS)",
    "Current location: waiting for GPS…": "Текущее местоположение: ожидание GPS…",
    "Current location: {place} (GPS)": "Текущее местоположение: {place} (GPS)",
    "Erev Tisha B'Av (8 Av afternoon): stop learning Torah except sad topics; eat the final meal (seudah hamafseket) before the fast. Tallit and tefillin are worn at Shacharit on Erev Tisha B'Av — the restriction applies on Tisha B'Av day itself.\n\nTisha B'Av (9 Av): full 25-hour fast; kinot at Shacharit without tallit and tefillin; don them at Mincha after halachic chatzos (use your zmanim app). Sit on low stools until chatzos on 9 Av.\n\nShabbat Chazon (the Shabbat before 9 Av): Shabbat is observed normally — meat and wine are permitted.": (
        "Эрев Тиша бе-Ав (8 ав, после полудня): прекратите изучение Торы, кроме печальных тем; сеуда hа-мафсекет перед постом. "
        "Талит и тфилин — на Шахарите эрева; запрет — в сам Тиша бе-Ав.\n\n"
        "Тиша бе-Ав (9 ав): полный 25-часовой пост; кинот без талита и тфилин; надевают на Минхе после хацот. "
        "Низкие табуреты до хацот.\n\n"
        "Шаббат Хазон: Шаббат как обычно — мясо и вино разрешены."
    ),
    "Explore the mystical letter Heh (ה)! ✡ This powerful letter represents G-d's presence in our world! Its shape teaches us something amazing: It has an opening at the bottom, showing that even if we fall, we can always return to G-d. Did you know? The letter ה appears twice in G-d's four-letter name, teaching us that G-d is present in both the spiritual and physical worlds! Take a moment to notice G-d's presence in your life today.": (
        "Открой букву Хей (ה)! ✡ Она символизирует присутствие В-га в мире: отверстие внизу — даже упав, можно вернуться. "
        "ה дважды в Четырёхбуквенном Имени — В-г в духовном и физическом. Задание: заметь Его присутствие сегодня."
    ),
    "Havdalah in Kiddush — Yaknehaz order when Shabbat leads into Yom Tov": (
        "Хавдала в Киддуше — порядок Якнеаз, когда Шаббат переходит в Йом Тов"
    ),
    "Learn about the ultimate \"legal permit\" to enjoy the world! 🍎 The Talmud says that everything on earth belongs to Hashem, so taking a bite without a blessing is like \"stealing\" from the King’s palace. Saying a bracha is essentially asking for permission and turning a physical snack into a spiritual moment! 🏛️ The system is incredibly precise: if it grows on a tree, you say \"Ha'etz.\" If it comes from the ground, it's \"Ha'adamah.\" For bread, you have the heavyweight \"Ha'motzi,\" and for everything else—like meat, cheese, or chocolate—you say \"Shehakol.\" 🕊️ But then there’s the VIP of the drink world: wine! Because wine is so special and used for holy moments like Kiddush, it gets its very own unique blessing: \"Borei Peri HaGafen.\" 🍷 It’s so powerful that if you drink wine during a meal, it often covers all other drinks on the table! 🥂 There is even a specific \"Order of Operations\" called Mag'a Esh to help you decide which food gets the blessing first if you have a big plate. 🍇 It’s like a daily training session in mindfulness—stopping to recognize exactly where your food came from and all the amazing creations, tastes and flavors that Hashem put in the world for us to enjoy 🎊🥯.": (
        "Узнай о «разрешении» наслаждаться миром! 🍎 Талмуд: всё принадлежит В-гу — кусок без брахи как «кража» из дворца Царя. "
        "Дерево — ха-Эц; земля — ха-Адама; хлеб — ха-Моци; остальное — шехаколь. Вино — борей при hа-гафен. "
        "Ежедневная тренировка осознанности 🎊🥯."
    ),
    "Mezonot (grain)": "мезонот (зерновые)",
    "Musaf (only on Rosh Chodesh, Festivals, and Shabbat)": "мусаф (только в Рош Ходеш, на праздники и в Шаббат)",
    "No location set — turn on GPS or choose a city below": "Местоположение не задано — включите GPS или выберите город ниже",
    "Purim Meshulash starts tonight in Jerusalem. Because Shabbat falls in the middle of the festival, read and save this plan now — you will not be able to rely on the app on Shabbat for Sunday's mitzvot.\n\n$scheduleBlock\n\nTonight (Thursday night after tzeit): first Megillah reading. Tomorrow (Friday): second Megillah reading and matanot la'evyonim. Mishloach and seudah wait until Sunday.": (
        "Пурим Мешугаш начинается сегодня вечером в Иерусалиме. Шаббат посередине праздника — прочитайте и сохраните план сейчас; "
        "в Шаббат приложение не поможет с воскресными мицвот.\n\n$scheduleBlock\n\n"
        "Сегодня вечером (четверг после цейт): первое чтение Мегиллы. Завтра (пятница): второе чтение и матанот ла-эвьоним. "
        "Мишлоах и сеуда — в воскресенье."
    ),
    "Purim Meshulash — Purim seudah on Sunday (16 Adar)\n\nThe festive Purim meal is today (not Friday or Shabbat this year).\n\nWhen:\n• Sunday daytime before sunset — many hold the meal in the afternoon after mishloach manot.\n\nHow:\n• Festive meal with bread, meat, wine, and joy; include words of Torah or thanks to Hashem.\n• Drinking wine is a widespread custom; celebrate responsibly.\n\nThis completes the four Purim mitzvot for Purim Meshulash in Jerusalem.": (
        "Пурим Мешугаш — сеуда Пурима в воскресенье (16 Адар).\n\n"
        "Когда: днём до заката; многие — после полудня, после мишлоах манот.\n"
        "Как: праздничная трапеза с хлебом, мясом, вином и радостью; слова Торы или благодарность В-гу. "
        "Вино — распространённый обычай; празднуйте ответственно.\n\n"
        "Завершает четыре мицвот Пурима в Иерусалиме."
    ),
    "Purim Meshulash — matanot la'evyonim on Friday only (14 Adar)\n\nToday (Friday daytime) — not on Shabbat:\n• Give at least one gift to each of two different poor people (minimum of two recipients total).\n• Each gift should enable a modest Purim meal (money is common; amounts vary by community).\n• Many give after the daytime Megillah reading.\n\nYou may use a trustworthy messenger or organization that distributes today. If you cannot find recipients, ask your rabbi or shul Friday morning.\n\nSunday is for mishloach manot and the seudah — those should already be prepared before Shabbat.": (
        "Пурим Мешугаш — матанот ла-эвьоним только в пятницу (14 Адар).\n\n"
        "Сегодня (пятница днём), не в Шаббат:\n"
        "• Дар двум разным беднякам (минимум два получателя).\n"
        "• Дар на скромную трапезу Пурима; многие дают после дневной Мегиллы.\n\n"
        "Можно через посланника или организацию. Воскресенье — мишлоах манот и сеуда; готовьте до Шаббата."
    ),
    "Rosh Chodesh (ראש חודש) — the New Month — is a semi-holiday with extra prayer and customs.\n\nFestive meal (mitzvah):\n• It is a mitzvah to increase your meal on Rosh Chodesh — at minimum add an extra dish or special food in honor of the day (Shulchan Arukh O.C. 419:1).\n• Have the meal during the day. Poskim write this commemorates the feast the Sanhedrin held at Beit Ya'zek for witnesses who came to testify they saw the new moon (Mishnah Rosh Hashanah 2:5; Orchos Chaim and Kol Bo, cited on O.C. 419).\n• Money spent on Rosh Chodesh meals — like Shabbat and Yom Tov — is not deducted from the income allotted to you on Rosh Hashanah; if you spend more for these mitzvos, Heaven adds to your allotment (Pesikta de-Rav Kahana, cited in Tur O.C. 419 and Magen Avraham 419:1).\n\nDavening today (listed in your Morning, Afternoon, and Evening Prayer sections):\n• If you recite Shacharit, Mincha, or Maariv Amidah — add Yaaleh V'yavo in Retzei. Shacharit/Mincha: correct per timing (insert in Retzei, return to the beginning of Retzei if already concluded, or repeat only that Amidah if finished). Maariv on Rosh Chodesh only: do not repeat if forgotten after Retzei (Berachot 30b; SA O.C. 422:1)\n• If you say Birkat Hamazon when you eat bread — add Yaaleh V'yavo there too\n• Ashkenazi custom — most authorities obligate Shacharit and Mincha Amidah on these days\n• Sephardic custom — many women fulfill the daily obligation with one prayer; if you daven an extra Amidah and forget Yaaleh V'yavo, ask your rabbi about a voluntary (nedavah) stipulation\n• Half Hallel at Shacharit if you say Hallel (optional — follow your minhag; Full Hallel if Rosh Chodesh falls during Chanukah)\n• Tachanun is omitted all day\n\nOther customs:\n• Fasting and eulogies are generally not done on Rosh Chodesh\n• Widespread custom: married women refrain from certain melacha (needlework, laundry, etc.) as an extra mark of honor — ask your rav for details\n\nWhen Rosh Chodesh spans two days (30th of the previous month and 1st), observances apply to both days.": (
        "Рош Ходеш (ראש חודש) — полупраздник с дополнительными молитвами.\n\n"
        "Праздничная трапеза: добавьте блюдо в честь дня (О.Х. 419:1); днём.\n\n"
        "Молитва: «Яале вьяво» в «Рецей» и в Биркат а-Мазон; полу-Галель по минхагу; без Тахануна. "
        "Маарив: если забыли после «Рецей» — не повторяйте (Берахот 30b). Спросите рава о деталях."
    ),
    "Sephardi Jews trace roots to the Iberian diaspora and related Mediterranean communities. Prayer is usually Nusach Sefard (Bet Yosef). Halacha generally follows the Shulchan Aruch and poskim such as Rav Ovadia Yosef zt\"l. Rice and kitniyot are halachically permitted on Pesach where Ashkenazim follow the kitniyot custom. Do not confuse with \"Nusach Sefard\" on a Chasidic siddur (an Ashkenazi rite) or with Edot HaMizrach (Middle Eastern / North African nusach).": (
        "Сефарды — иберийская и средиземноморская диаспора. Молитва: нусах Сефард (Бет Йосеф). "
        "Галаха: Шулхан Арух и посеким вроде Рав Овадьи Йосефа. Рис и китнийот на Песах разрешены, где ашкеназы держат обычай китнийот. "
        "Не путать с «нусах Сефард» в хасидском сиддуре (ашкеназский обряд) и с Эдот hа-Мизрах."
    ),
    "Sephardi — Sephardi Jews trace roots to the Iberian diaspora and related Mediterranean communities. Prayer is usually Nusach Sefard (Bet Yosef). Halacha generally follows the Shulchan Aruch and poskim such as Rav Ovadia Yosef zt\"l. Rice and kitniyot are halachically permitted on Pesach where Ashkenazim follow the kitniyot custom. Do not confuse with \"Nusach Sefard\" on a Chasidic siddur (an Ashkenazi rite) or with Edot HaMizrach (Middle Eastern / North African nusach).": (
        "сефарды — иберийская и средиземноморская диаспора; нусах Сефард (Бет Йосеф); Шулхан Арух и Рав Овадья Йосеф. "
        "Рис и китнийот на Песах разрешены. Не путать с хасидским «нусах Сефард» и с Эдот hа-Мизрах."
    ),
    "Shulchan Aruch — The Shulchan Aruch (\"Set Table\") is the classic 16th-century code of halacha (Jewish Law) by Rabbi Yosef Karo. Ashkenazim often study it with the Rema's glosses; Sephardim generally follow Rabbi Karo, but rabbis have debated about almost everything throughout the years.": (
        "Шулхан Арух — классический кодекс галахи XVI века раввина Йосефа Каро. Ашкеназы — с глоссами Рема; сефарды — обычно Каро."
    ),
    "Ten Days of Repentance from Rosh Hashana to Yom Kippur": "Десять дней покаяния от Рош hа-Шана до Йом Кипур",
    "The Shulchan Aruch (\"Set Table\") is the classic 16th-century code of halacha (Jewish Law) by Rabbi Yosef Karo. Ashkenazim often study it with the Rema's glosses; Sephardim generally follow Rabbi Karo, but rabbis have debated about almost everything throughout the years.": (
        "Шулхан Арух («Покрытый стол») — классический кодекс галахи XVI века раввина Йосефа Каро. "
        "Ашкеназы изучают с Рема; сефарды обычно следуют Каро."
    ),
    "This app is a learning companion, not a rabbi — it does not give halachic rulings.\n\nThis checklist does not contain all the mitzvot in the entire Torah. It covers standard daily mitzvot that observant Jews commonly practice — waking, prayer, blessings, meals, Torah study, Shabbat preparation, and similar foundations.\n\nWith your permission, the app uses GPS or a city you choose to calculate Jewish calendar times and when you can fulfill different mitzvot throughout the day (for example morning, afternoon, and evening prayer windows, candle lighting, and Shabbat-related times). Location is kept on your device only for zmanim and the calendar.\n\nIf you are new to Judaism, take it slow and do what you can. Build steady habits without overwhelm, and always ask an Orthodox rabbi you trust when something is unclear or when your situation needs personal guidance.": (
        "Это приложение — спутник обучения, не раввин; оно не выносит галахических решений.\n\n"
        "Список не охватывает все мицвот Торы — только ежедневные основы: пробуждение, молитва, благословения, еда, Тора, подготовка к Шаббату.\n\n"
        "С вашего разрешения приложение использует GPS или выбранный город для зманим и календаря; данные остаются на устройстве.\n\n"
        "Новичкам — не спешите; спрашивайте доверенного ортодоксального рава, когда нужна личная консультация."
    ),
    "Tu B'Shvat (the 15th of Shvat) is the New Year for Trees — one of the four \"new years\" mentioned in the Mishnah. It marks the date used for calculating the age of fruit trees for tithing purposes.\n\nTu B'Shvat is not a fast day, and work is permitted. It is customary to eat fruits of Israel, especially the seven species: wheat, barley, grapes, figs, pomegranates, olives, and dates. Many communities hold a \"Tu B'Shvat seder\" with fruits and wine.\n\nIn Kabbalistic tradition (particularly from the 16th-century Safed mystics), Tu B'Shvat became associated with the spiritual rectification of the world through eating fruits with intention.": (
        "Ту бе-Шват (15 Шват) — Новый год деревьев. Не пост; работа разрешена. "
        "Едят плоды Земли Израиля, семь видов; многие — седер Ту бе-Шват. В каббале — духовное исправление через плоды с намерением."
    ),
    "Use GPS for location": "Использовать GPS для местоположения",
    "Uses 1-day Yom Tov customs and the Israel parsha cycle. Select a city above or enable GPS for automatic detection.": (
        "Однодневный Йом Тов и израильский цикл паршот. Выберите город или включите GPS."
    ),
    "When:\nIn the Northern Hemisphere, say it during Nissan when fruit trees blossom (Shulchan Arukh O.C. 226:1). In the Southern Hemisphere, say it when local fruit trees blossom in Elul–Tishrei. Set your city or GPS in Settings so the app shows this item in the correct season for you.": (
        "Когда:\nСеверное полушарие — Нисан при цветении (О.Х. 226:1). Южное — Элул–Тишрей. Укажите город или GPS в настройках."
    ),
    "Yaaleh V'Yavo is a special prayer paragraph inserted into the Amidah (the standing silent prayer) and into Birkat HaMazon (Grace After Meals) on Rosh Chodesh (the new month), Yom Tov (festivals), and Chol HaMoed (intermediate festival days).\n\nThe text asks G-d to remember us, our fathers, Jerusalem, the Davidic dynasty, and the entire Jewish people for good — for life and peace — on the day being observed.\n\nIf forgotten in Shacharit or Mincha (also Chol HaMoed / Yom Tov at any Amidah including Maariv):\n• Still in Retzei — insert Yaaleh V'yavo in its place and continue.\n• After concluding Retzei — return to the beginning of Retzei, insert Yaaleh V'yavo, and complete the remaining blessings.\n• After the final Yihiyu L'ratzon — repeat only that Amidah (Shemoneh Esrei), never the full service.\n\nRosh Chodesh Maariv only: if forgotten after finishing Retzei, or after the entire Amidah — do not repeat (Berachot 30b; Shulchan Arukh O.C. 422:1). If still in Retzei before God's name at the conclusion, insert it there.": (
        "«Яале вьяво» — в Амиду и Биркат а-Мазон в Рош Ходеш, Йом Тов и Холь hа-Моэд.\n\n"
        "Если забыли в Шахарит/Минхе: в «Рецей» — вставьте; после «Рецей» — вернитесь к началу; после «Йихию ле-рацон» — повторите только эту Амиду.\n\n"
        "Маарив Рош Ходеш: после «Рецей» или всей Амиды — не повторяйте (Берахот 30b; О.Х. 422:1)."
    ),
    "Yaknehaz order when Shabbat leads into Yom Tov": "порядок Якнеаз, когда Шаббат переходит в Йом Тов",
    "bein kodesh l'chol — Bein kodesh l'chol means \"between holy and mundane\" — the wording in regular Saturday-night Havdalah when Shabbat ends and weekday begins. People say 'Baruch hamavdil bein kodesh l'chol' before doing melacha if they have not yet heard full Havdalah or recited the maariv amidah with the Havdalah insert. When Shabbat flows into Yom Tov instead, the wording is bein kodesh l'kodesh (holy to holy), not l'chol.": (
        "бейн кодеш ле-холь — «между святым и мирским» в субботней Хавдале. "
        "Говорят «Барух hамавдил бейн кодеш ле-холь» перед мелахой, если ещё не слышали полную Хавдалу. "
        "Шаббат в Йом Тов — бейн кодеш ле-кодеш."
    ),
    "challah": "халла",
    "forbidden to do melacha on Shabbat or Yom Tov": "запрещено делать мелаху в Шаббат или Йом Тов",
    "matana al menat lehachzir": "матана аль менат леhахзир — дар с условием возврата",
    "mechirat chametz — Mechirat chametz is selling chametz you want to keep to a non-Jew through your rabbi before Pesach so it is not yours during the holiday. Authorize the sale well before Erev Pesach — most rabbis stop accepting forms by the night before, even though ownership transfers on Erev Pesach morning. It must be an absolute sale, not a conditional gift (matana al menat lehachzir). Chametz sold should be stored away. When including year-round dishes, rabbis typically sell only the absorbed chametz flavor in the vessels — not the physical dishes themselves — to avoid requiring tevilat kelim after Pesach. Shortly after Pesach, ownership reverts per the contract your rabbi uses.": (
        "мехират хамец — продажа хамеца нееврею через рава до Песаха. Оформите заранее; "
        "абсолютная продажа, не матана аль менат леhахзир. Проданный хамец — убрать. После Песаха — по договору рава."
    ),
    "symbolic merging of courtyards allowing carrying on Shabbat": (
        "символическое объединение дворов (эрув хацерот), разрешающее перенос в Шаббат"
    ),
    "the 39 categories of transformative labor forbidden on Shabbat": (
        "39 категорий преобразующего труда, запрещённого в Шаббат"
    ),
}


def main() -> None:
    candidates = json.loads(CAND.read_text(encoding="utf-8"))
    keys = [c["key"] for c in candidates if c["key"] not in SKIP_KEYS][:50]
    missing = [k for k in keys if k not in MANUAL]
    extra = [k for k in MANUAL if k not in keys]
    if missing:
        raise SystemExit(f"missing MANUAL ({len(missing)}): {missing[0][:80]}...")
    if extra:
        raise SystemExit(f"extra MANUAL keys not in batch: {extra[0][:80]}...")
    batch = {k: MANUAL[k] for k in keys}
    OUT.write_text(json.dumps(batch, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"Wrote {len(batch)} entries to {OUT.name}")


if __name__ == "__main__":
    main()
