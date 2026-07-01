"""Human-quality fixes batch 22 — Special Mitzvah header, Help those in need RU, praying RU."""

from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CATALOG = json.loads((ROOT / "data" / "translation-catalog" / "strings.json").read_text(encoding="utf-8"))[
    "strings"
]

SPECIAL_MITZVAH_ES = (
    "**Mitzvá especial**: Enciende una vela de aceite (como una mecha flotante en una taza pequeña de aceite) "
    "para la elevación de las almas de las «Neshamot Hagalmudot» (הנשמות הגלמודות) — las almas desamparadas "
    "en el Cielo que solo necesitan un pequeño mérito para entrar al Gan Eden. Los rabinos dicen que a estas almas "
    "les falta solo un pequeño mérito para disfrutar del placer eterno de Gan Eden; si enciendes una vela "
    "en su mérito, ¡puedes ayudarlas a entrar! Entonces estas almas estarán profundamente agradecidas y, "
    "una vez dentro, podrán orar por ti por todo lo que desees."
)

SPECIAL_MITZVAH_FR = (
    "**Mitzvah spéciale** : Allume une bougie à huile (comme une mèche flottante dans une petite tasse d'huile) "
    "pour l'élévation des âmes des « Neshamot Hagalmudot » (הנשמות הגלמודות) — les âmes délaissées au Ciel "
    "qui n'ont besoin que d'un petit mérite pour entrer au Gan Eden. Les rabbins disent que ces âmes manquent "
    "seulement d'un petit mérite pour accéder au plaisir éternel du Gan Eden ; si tu allumes une bougie "
    "en leur mérite, tu peux les aider à entrer ! Ces âmes seront alors profondément reconnaissantes et, "
    "une fois à l'intérieur, pourront prier pour toi pour tout ce que tu souhaites."
)

SPECIAL_MITZVAH_RU = (
    "**Особая мицва**: Зажги масляную свечу (например, с плавающим фитилём в маленькой чашке масла) "
    "для вознесения душ «Neshamot Hagalmudot» (הנשמות הגלמודות) — заброшенных душ на Небесах, "
    "которым нужна лишь небольшая заслуга, чтобы попасть в Gan Eden. Мудрецы говорят, что этим душам "
    "не хватает лишь малой заслуги для вечного блаженства Gan Eden, и если ты зажжёшь свечу в их заслугу, "
    "ты можешь помочь им войти! Тогда эти души будут безмерно благодарны и, оказавшись внутри, "
    "смогут молиться за тебя обо всём, о чём ты просишь."
)

SPECIAL_MITZVAH_HE = (
    "**מצווה מיוחדת**: הדליקו נר שמן (כמו פתיל צף בכוס שמן קטנה) לעילוי נשמות "
    "«Neshamot Hagalmudot» (הנשמות הגלמודות) — הנשמות העצובות בשמים שחסר להן רק מעט זכות "
    "כדי להיכנס ל-Gan Eden. חכמים אומרים שהנשמות האלה חסרות רק מעט זכות כדי להיכנס לעונג הנצחי "
    "של Gan Eden, ואם תדליקו נר לזכותן, אתם יכולים לעזור להן להיכנס! אז הנשמות האלה אסירות תודה במיוחד, "
    "וברגע שהן בפנים הן יכולות להתפלל עבורכם על כל משאלותיכם."
)

HELP_NEED_ES = (
    "¡Ayuda a quienes lo necesitan! ¿Tienes ropa que hace tiempo que no usas? "
    "¡Es hora de una transformación mitzvá! 👕 Separa esas prendas que pueden alegrar a alguien más. "
    "¿Lo increíble? Cuando ayudas a vestir a otros, imitas a D-s, que vistió a Adán y Eva en el Jardín del Edén. "
    "Además, tu armario queda organizado — ¡ganar-ganar!"
)

HELP_NEED_FR = (
    "Aide ceux qui en ont besoin ! Tu n'as pas porté certains vêtements depuis longtemps ? "
    "Il est temps pour une mitzvah de renouveau ! 👕 Mets de côté les articles qui pourraient "
    "apporter de la joie à quelqu'un d'autre. C'est incroyable : en aidant à habiller les autres, "
    "tu imites D., qui a vêtu Adam et Ève dans le jardin d'Éden. De plus, ton placard "
    "est organisé — gagnant-gagnant !"
)

HELP_NEED_RU = (
    "Помоги нуждающимся! Есть одежда, которую ты давно не носил? "
    "Пора для «переодевания мицвы»! 👕 Отложи в сторону вещи, которые могут принести "
    "радость кому-то другому. Удивительно, но когда ты помогаешь одевать других, "
    "ты подражаешь В-гу, который одел Адама и Еву в Эдемском саду. "
    "Кроме того, твой шкаф становится организованнее — беспроигрышный вариант!"
)

HELP_NEED_HE = (
    "עזרו לנזקקים! יש לכם בגדים שלא לבשתם זמן מה? "
    "הגיע הזמן ל«שדרוג מצווה»! 👕 הפרישו בצד פריטים שיכולים לשמח מישהו אחר. "
    "מה המדהים? כשאתם עוזרים להלביש אחרים, אתם מחקים את ה', "
    "שהלביש את אדם וחוה בגן עדן. בנוסף, הארון שלכם מסודר — כולם מרוויחים!"
)

PRAYING_RU = (
    "Узнай о невероятной силе молитвы за всех! 🙏 Когда ты молишься (daven) В-гу о благословениях, "
    "здоровье и parnassa (пропитании) или о чём угодно хорошем для всего народа Израиля (Am Yisrael) — "
    "не только о себе — мудрецы учат, что такая молитва с большей вероятностью будет услышана! "
    "Это духовный беспроигрышный вариант: помогая всем, ты помогаешь себе. "
    "Кто не мечтает жить в мире, где никто не в стрессе? "
    "В следующий раз, когда тебе нужны благословения, расширь круг молитвы и молись за всё сообщество!"
)

HADLAKAT_LONG_RU = (
    "Узнай о мицве хадлакат нерот — зажигании шаббатных свечей — и об увлекательной галахической головоломке! 🕯️ "
    "Мудрецы установили их для шалом байит (мира в доме), а не только ради атмосферы: нужно реально пользоваться "
    "светом. А что, если ты принимаешь Шаббат рано, пока снаружи ещё ярко светит солнце? Спор вокруг пословицы "
    "«зачем лампа в полдень?» (Хулин 60б). Маген Авраам (О.Х. 263:11): если зажёг и ушёл — сядь и получи краткую "
    "пользу от пламени. Шулхан Арух ha-Рав и Арух ha-Шулхан не согласны: дневной свет свечей бесполезен — "
    "используй длинные свечи, чтобы к твоему возвращению в темноте они ещё горели. Современные авторитеты вроде "
    "«Шемират Шаббат ке-хильхата» (43:17) предлагают таймеры на свет в столовой с явной каваной, чтобы браха "
    "покрывала и свечи, и электричество (Мишна Берура 263:41) — мицва выполняется, когда наступает темнота!"
)

PRAYING_HE = (
    "גלו את כוח התפילה לכולם! 🙏 כשאתם מתפללים לה' על ברכות, בריאות, פרנסה, "
    "או על כל דבר טוב עבור כל עם ישראל — ולא רק עבור עצמכם — החכמים מלמדים "
    "שהתפילה שלכם סביר יותר שתיענה! זה כמו «כולם מרוויחים» רוחנית: אתם עוזרים לעצמכם "
    "בזמן שאתם עוזרים לכולם. ומי לא היה רוצה לחיות בעולם שבו אף אחד לא לחוץ? "
    "אז בפעם הבאה שאתם צריכים ברכות, הרחיבו את אופק התפילה ותתפללו על כל הקהילה!"
)

SPECIAL_KEY = next(k for k in CATALOG if k.startswith("**Special Mitzvah**"))
HELP_KEY = next(k for k in CATALOG if k.startswith("Help those in need"))
PRAYING_KEY = next(k for k in CATALOG if k.startswith("Learn about the incredible power of praying"))
HADLAKAT_LONG_KEY = next(k for k in CATALOG if k.startswith("Learn about the mitzvah of Hadlakat Nerot—lighting"))

HONOR_SHABBAT_KEY = next(k for k in CATALOG if k.startswith("Honor Shabbat! Pick out"))
LEVEL_UP_KEY = next(k for k in CATALOG if k.startswith("Level up your day"))
CHANUKAH_LIGHT_KEY = next(k for k in CATALOG if k.startswith("Light up the night"))
HONOR_GOD_KEY = next(k for k in CATALOG if k.startswith("Honor G-d's Name"))
HONOR_BOOKS_KEY = next(k for k in CATALOG if k.startswith("Honor holy books"))

BATCH22_ES: dict[str, str] = {
    SPECIAL_KEY: SPECIAL_MITZVAH_ES,
    HELP_KEY: HELP_NEED_ES,
}

BATCH22_FR: dict[str, str] = {
    SPECIAL_KEY: SPECIAL_MITZVAH_FR,
    HELP_KEY: HELP_NEED_FR,
}

BATCH22_RU: dict[str, str] = {
    SPECIAL_KEY: SPECIAL_MITZVAH_RU,
    HELP_KEY: HELP_NEED_RU,
    PRAYING_KEY: PRAYING_RU,
    HADLAKAT_LONG_KEY: HADLAKAT_LONG_RU,
    "Choose city now (no GPS)": "Выбери город (без геолокации)",
    "Select Language": "Выбери язык",
    "Choose city for zmanim": "Выбери город для зманим",
    "Use GPS for location": "Используй геолокацию",
    "No location set — turn on GPS or choose a city below": (
        "Местоположение не задано — включи геолокацию или выбери город ниже"
    ),
    (
        "Uses 1-day Yom Tov customs and the Israel parsha cycle. "
        "Select a city above or enable GPS for automatic detection."
    ): "Однодневный Йом Тов и израильский цикл паршот. Выбери город или включи геолокацию.",
    HONOR_SHABBAT_KEY: (
        "Почитай Шаббат! Выбери особое вино (или виноградный сок) для Киддуш 🍷. Киддуш произносится над полной "
        "чашей вина — или сока при необходимости — чтобы освятить святой день с самого начала. Мудрецы учат: "
        "почитание Шаббата — наслаждаться хорошей едой и питьём, и Хашем хранит особое сокровище, из которого "
        "вознаграждает тех, кто старается почтить день. Выбор чаши Киддуш заранее — маленький акт подготовки, "
        "задающий тон целому дню покоя и радости."
    ),
    LEVEL_UP_KEY: (
        "Подними день: изучай одну галаху (еврейский закон) в день! 📚 Мудрецы учат, что постоянное изучение Торы — "
        "даже одна практическая галаха в день — мощный способ оставаться связанным с Б-гом и Его мудростью всю жизнь. "
        "Это как ежедневная духовная витаминка! Выбери тему, которая тебя заинтересует: законы Шаббата, благословения "
        "на еду, деловая этика, кашрут или уважение к другим. Будь то Мишна, Гемара, Шулхан Арух или современный "
        "сефер — небольшое постоянное обучение строит несокрушимую основу для содержательной еврейской жизни. "
        "А знаешь? Ежедневное изучение Торы, особенно галаха, считается одним из деяний, у которых «нет меры» — "
        "их духовная награда поистине бесконечна!"
    ),
    CHANUKAH_LIGHT_KEY: (
        "Зажги ночь: свечи Хануки и пирсумей ниса! 🕎 Цель — публично провозгласить чудо с маслом. "
        "РАЗМЕЩЕНИЕ: ханукия у двери слева (мезуза справа); на верхних этажах — у окна на улицу; "
        "в опасности — на столе внутри. ВРЕМЯ: зажигание около цейт ха-кохавим; свечи горят минимум полчаса "
        "после наступления ночи. Благословения: первую ночь — три, остальные — две; добавляй свечу каждый вечер "
        "по хасидской или общей традиции. Масло предпочтительнее воску по некоторым мнениям — уточни у рава. "
        "Пусть свет Хануки напомнит: малое может победить многое."
    ),
    HONOR_GOD_KEY: (
        "Почитай Имя В-га! 👑 Рамбам учит: Киддуш Хашем — не только слова, но и жизнь! Когда тальмид хахам честен "
        "в делах, добр и говорит мягко, люди говорят: «Как прекрасны пути Торы!» Это Киддуш Хашем! Миссия сегодня: "
        "сделай что-то, что заставит других высоко думать о В-ге и Его Торе. Ты — посланник В-га в этом мире! ✡"
    ),
    HONOR_BOOKS_KEY: (
        "Почитай святые книги! Проверь, стоят ли твои сефарим (святые книги) с уважением 📚. Убедись, что они "
        "перевёрнуты правильно и на них не лежит ничего светского. А знаешь? Уважение к святым книгам показывает "
        "нашу признательность мудрости в них и Тому, Кто её дал!"
    ),
}

BATCH22_HE: dict[str, str] = {
    SPECIAL_KEY: SPECIAL_MITZVAH_HE,
    HELP_KEY: HELP_NEED_HE,
    PRAYING_KEY: PRAYING_HE,
}
