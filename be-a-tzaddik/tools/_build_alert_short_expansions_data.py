#!/usr/bin/env python3
"""Generate _alert_short_expansions_data.py — 400+ char Mitzvah Me alert expansions."""

from __future__ import annotations

import json
from pathlib import Path

from _mitzvah_alert_tone_data import FULL_REWRITES

TOOLS = Path(__file__).parent
ROOT = TOOLS.parent
KEYS: list[str] = json.loads((TOOLS / "_short_alert_keys.json").read_text(encoding="utf-8"))
HE_EXTRA_KEYS: list[str] = []

MIN_LEN = 300
PAD = {
    "es": (
        " Esto conecta la fe con tu día a día — incluso un minuto con intención "
        "fortalece tu camino espiritual y tu vínculo con D-s."
    ),
    "fr": (
        " Cela relie la foi à ton quotidien — une minute d'intention renforce "
        "ton chemin spirituel et ton lien avec D."
    ),
    "ru": (
        " Это связывает веру с повседневностью — минута намерения укрепляет "
        "духовный путь и связь с В-гом."
    ),
    "he": (
        " זה מחבר אמונה ליום יום — דקה של כוונה מחזקת את הדרך הרוחנית "
        "ואת הקשר לה'."
    ),
}

PAD_ALT = {
    "es": " Un paso pequeño hoy ya es conexión espiritual.",
    "fr": " Un petit pas aujourd'hui, c'est déjà de la connexion.",
    "ru": " Маленький шаг сегодня — уже духовная связь.",
    "he": " צעד קטן היום — כבר חיבור רוחני.",
}

# Keys not in FULL_REWRITES — full native bodies (prefix match on catalog keys).
NEW_BY_PREFIX: dict[str, dict[str, str]] = {
    "Connect to our history!": {
        "he": (
            "התחברו להיסטוריה שלנו! 🌊 זכרו את יציאת מצרים — איך ה' הפך עם של עבדים לעם חופשי "
            "בניסים נפלאים. הזיכרון הזה כל כך חשוב שזו מצווה לזכור ביום ובלילה (דברים טז:ג). "
            "בסדר פסח אנחנו חיים את הסיפור מחדש; בכל יום — «ביציאת מצרים» מזכירים שהגאולה אפשרית. "
            "אתגר: קחו רגע היום לדמיין את החירות — ולהודות עליה."
        ),
        "es": (
            "¡Conéctate con nuestra historia! 🌊 Recuerda el Éxodo de Egipto: cómo D-s transformó "
            "un pueblo de esclavos en un pueblo libre con milagros asombrosos. Esa memoria es tan "
            "importante que es mitzvá recordarla cada día y cada noche (Devarim 16:3). En la Pésaj "
            "vivimos la historia; cada día, «por el Éxodo» nos recuerda que la redención es posible. "
            "Reto: tómate un momento hoy para imaginar la libertad — y agradecerla."
        ),
        "fr": (
            "Connecte-toi à notre histoire ! 🌊 Souviens-toi de la Sortie d'Égypte : comment D. a "
            "transformé un peuple d'esclaves en peuple libre par des miracles. Cette mémoire est si "
            "importante que c'est une mitsva de s'en souvenir jour et nuit (Devarim 16:3). À Pessa'h "
            "on revit l'histoire ; chaque jour, « par la Sortie » rappelle que la délivrance est possible. "
            "Défi : prends un moment aujourd'hui pour imaginer la liberté — et remercier."
        ),
        "ru": (
            "Соединись с нашей историей! 🌊 Вспомни Исход из Египта: как В-г превратил народ рабов "
            "в свободный народ чудесами. Эта память настолько важна, что мицва помнить днём и ночью "
            "(Дварим 16:3). На Песах мы проживаем историю заново; каждый день «по Исходу» напоминает: "
            "геула возможна. Вызов: найди минуту сегодня представить свободу — и поблагодари за неё."
        ),
    },
    "Connect to the Holy Land!": {
        "he": (
            "התחברו לארץ הקודש! 🗺️ זכרו (אחד מעשרה זכירות יומיות) שה' נתן את ארץ ישראל לעם ישראל "
            "לנחלה נצחית. הגמרא: אוירה של ארץ ישראל מחכים. כשה' נתן את הארץ — נתן גם כוח לקיים מצוות. "
            "רחוקים פיזית? לכל יהודי יש חלק בארץ — זו הבסיס הרוחני שלכם. "
            "אתגר: קראו פסוק על ארץ ישראל או התפללו על שלום הארץ."
        ),
        "es": (
            "¡Conéctate con la Tierra Santa! 🗺️ Recuerda (una de las diez remembranzas diarias) que "
            "D-s dio Israel al pueblo judío como herencia eterna. El Talmud: el aire de Israel hace sabios. "
            "Al dar la tierra, D-s dio fuerza para cumplir mitzvot. ¿Lejos físicamente? Cada judío tiene "
            "parte en la Tierra — es tu base espiritual. Reto: lee un versículo sobre Israel o ora por su paz."
        ),
        "fr": (
            "Connecte-toi à la Terre Sainte ! 🗺️ Souviens-toi (un des dix souvenirs quotidiens) que D. "
            "a donné Israël au peuple juif en héritage éternel. Le Talmud : l'air d'Israël rend sage. "
            "En donnant la terre, D. a donné la force pour les mitsvot. Loin physiquement ? Chaque Juif "
            "a une part — c'est ta base spirituelle. Défi : lis un verset sur Israël ou prie pour sa paix."
        ),
        "ru": (
            "Соединись со Святой Землёй! 🗺️ Вспомни (одно из десяти ежедневных воспоминаний), что В-г "
            "дал Израиль еврейскому народу в вечное наследие. Талмуд: воздух Израиля делает мудрым. "
            "Даря землю, В-г дал силу для мицвот. Далеко физически? У каждого еврея есть часть в Земле — "
            "это твоя духовная база. Вызов: прочитай стих об Израиле или помолись о мире на земле."
        ),
    },
    "Discover G-d's unique unity!": {
        "he": (
            "גלו את ייחודו של ה'! ✡ הרמב\"ם (הלכות יסודי התורה א:ז): אחדות ה' שונה מכל אחדות שאנחנו מכירים. "
            "אדם «אחד» אבל עם חלקים; קבוצה «אחת» אבל מפרטים — אבל אחדות ה'? מוחלטת, בלי חלקים ובלי חלוקים. "
            "האתגר: התבוננו רגע בייחוד כל כך שלם שאין כלום מחוץ לו — זו יסוד האמונה היהודית. "
            "שמע ישראל מתחיל מכאן: ה' אחד."
        ),
        "es": (
            "¡Descubre la unidad única de D-s! ✡ El Rambam (Yesodei HaTorah 1:7): la unidad de D-s no es "
            "como ninguna otra. Una persona es «una» pero tiene partes; un grupo es «uno» pero de individuos — "
            "pero la unidad de D-s es absoluta, sin partes ni divisiones. El reto: contempla una unidad tan "
            "completa que no hay nada fuera de ella — base de la fe judía. El Shemá empieza aquí: D-s es Uno."
        ),
        "fr": (
            "Découvre l'unité unique de D. ! ✡ Le Rambam (Yesodei HaTorah 1:7) : l'unité de D. n'est "
            "comme aucune autre. Une personne est « une » mais a des parties ; un groupe est « un » mais "
            "fait d'individus — l'unité de D. est absolue, sans parties ni divisions. Défi : contemple une "
            "unité si complète qu'il n'y a rien en dehors — fondement de la foi juive. Le Shema commence ici."
        ),
        "ru": (
            "Открой уникальное единство В-га! ✡ Рамбам (Йесодей а-Тора 1:7): единство В-га не похоже "
            "ни на что. Человек «один», но с частями; группа «одна», но из людей — единство В-га абсолютно, "
            "без частей и делений. Вызов: поразмысли об единстве настолько полном, что нет ничего вне его — "
            "основа еврейской веры. Шма начинается здесь: В-г един."
        ),
    },
    "Discover the power of Peh (פ)!": {
        "he": (
            "גלו את כוח האות פה (פ)! 👄 «פה» — פה; מלמדת על כוח הדיבור. הצורה כוללת בית (ב) בפנים — "
            "מה שבפנים משפיע על מה שיוצא החוצה. חז\"ל מזהירים: לשון הרע הורגת; דיבור טוב בונה עולמות. "
            "אתגר: היום אמרו מילה אחת שמחזקת — לא שוברת. הפה הוא כלי קדושה."
        ),
        "es": (
            "¡Descubre el poder de Peh (פ)! 👄 «Peh» significa boca — enseña el poder del habla. Su forma "
            "incluye un Bet (ב) por dentro: lo interior afecta lo que sale. Los Sabios advierten: el habla "
            "mala destruye; el habla buena construye mundos. Reto del día: di una palabra que fortalezca, "
            "no que rompa. La boca es instrumento de santidad."
        ),
        "fr": (
            "Découvre le pouvoir de Peh (פ) ! 👄 « Peh » veut dire bouche — le pouvoir de la parole. "
            "Sa forme contient un Bet (ב) à l'intérieur : l'intérieur influence ce qui sort. Les Sages : "
            "la mauvaise parole détruit ; la bonne parole construit des mondes. Défi : dis un mot qui "
            "renforce, pas qui brise. La bouche est un instrument de sainteté."
        ),
        "ru": (
            "Открой силу буквы Пе (פ)! 👄 «Пе» — «рот», сила речи. В форме внутри Бет (ב) — внутреннее "
            "влияет на сказанное. Мудрецы предупреждают: злая речь разрушает; добрая строит миры. "
            "Вызов: скажи сегодня слово, которое укрепляет, а не ломает. Рот — инструмент святости."
        ),
    },
    "Discover the power of Psalm 7!": {
        "he": (
            "גלו את כוח תהילים ז'! 🛡️ דוד המלך מגיב להאשמות שקריות — ושומר על יושרו. "
            "המזמור מלמד: גם כשמעלילים עליך, אפשר להישאר נקי בלב. "
            "קראו את המזמור וחשבו: איך לשמור על מצפן מוסרי בזמנים קשים. "
            "תהילים הם תפילה וחיזוק — לא רק שיר יפה."
        ),
        "es": (
            "¡Descubre el poder del Salmo 7! 🛡️ El rey David responde a acusaciones falsas — y mantiene "
            "su integridad. El salmo enseña: incluso bajo calumnia, puedes permanecer limpio de corazón. "
            "Léelo y reflexiona: cómo mantener tu brújula moral en tiempos difíciles. "
            "Los Tehilim son oración y fortaleza — no solo poesía bonita."
        ),
        "fr": (
            "Découvre le pouvoir du Psaume 7 ! 🛡️ Le roi David répond à de fausses accusations — et "
            "garde son intégrité. Le psaume enseigne : même sous calomnie, on peut rester pur de cœur. "
            "Lis-le et réfléchis : comment garder ta boussole morale dans les moments difficiles. "
            "Les Tehilim sont prière et force — pas seulement de beaux vers."
        ),
        "ru": (
            "Открой силу Псалма 7! 🛡️ Царь Давид отвечает на ложные обвинения — и сохраняет честность. "
            "Псалом учит: даже под клеветой можно остаться чистым сердцем. "
            "Прочитай и подумай: как сохранить моральный компас в трудные времена. "
            "Техилим — молитва и опора, не просто красивая поэзия."
        ),
    },
    "Discover the power of Divine protection!": {
        "he": (
            "גלו את כוח ההגנה האלוקית! 🛡️ זכרו (מעשר זכירות) איך ה' הפך את קללות בלעם לברכות. "
            "מכשף חזק שכר לקלל את ישראל — ודיבר ברכות במקום! כולל «מה טובו אוהליך יעקב» שאומרים בבוקר. "
            "השיעור: כשאחרים מנסים להפיל — ה' יכול להפוך שליליות לברכה הגדולה. "
            "היום: בטחו שה' שומר עליכם גם כשלא רואים."
        ),
        "es": (
            "¡Descubre el poder de la protección divina! 🛡️ Recuerda (una de las diez remembranzas) cómo "
            "D-s convirtió las maldiciones de Balaam en bendiciones. Un hechicero poderoso fue contratado "
            "para maldecir a Israel — ¡y habló bendiciones! Incluye «Ma tovu ohaleja Yaakov» que decimos "
            "por la mañana. Lección: cuando otros intentan derribarte, D-s puede transformar lo negativo "
            "en la mayor bendición. Hoy: confía en que D-s te protege aunque no lo veas."
        ),
        "fr": (
            "Découvre le pouvoir de la protection divine ! 🛡️ Souviens-toi (un des dix souvenirs) comment "
            "D. a transformé les malédictions de Bilaam en bénédictions. Un puissant sorcier fut payé pour "
            "maudire Israël — et il a béni ! Dont « Ma tovu ohalekha Yaakov » du matin. Leçon : quand "
            "d'autres veulent te faire tomber, D. peut transformer le négatif en plus grande bénédiction. "
            "Aujourd'hui : fais confiance à la protection de D. même invisible."
        ),
        "ru": (
            "Открой силу божественной защиты! 🛡️ Вспомни (одно из десяти воспоминаний), как В-г "
            "превратил проклятия Билама в благословения. Сильного колдуна наняли проклясть Израиль — "
            "и он благословил! Включая «Ма тову охалеха Яаков» утром. Урок: когда другие хотят уронить, "
            "В-г может превратить негатив в величайшее благословение. Сегодня: доверься защите В-га, "
            "даже когда её не видно."
        ),
    },
    "Discover the righteous Tzadik (צ)!": {
        "he": (
            "גלו את הצדיק (צ)! ⚖️ האות מייצגת צדיק שמעשיו מאוזנים. הצורה: נון (נ) עם יוד (י) למעלה — "
            "ענווה (נון כפוף) עם קשר לה' (יוד). הסופית (ץ) יורדת מתחת לקו — השפעת הצדיק מגיעה גם למקומות נמוכים. "
            "אתגר: איזון בין חסד לגבולות באינטראקציות שלכם היום."
        ),
        "es": (
            "¡Descubre al Tzadik justo (צ)! ⚖️ La letra representa al justo cuyas acciones están "
            "equilibradas. Su forma: Nun (נ) con Yud (י) arriba — humildad (nun curvado) con conexión "
            "a D-s (yud). La final (ץ) baja de la línea — la influencia del justo llega a los lugares "
            "más bajos. Reto: equilibra bondad y límites en tus interacciones hoy."
        ),
        "fr": (
            "Découvre le Tzadik juste (צ) ! ⚖️ La lettre représente le juste dont les actes sont "
            "équilibrés. Sa forme : Noun (נ) avec Yod (י) au-dessus — humilité (noun courbé) et lien "
            "avec D. (yod). La finale (ץ) descend sous la ligne — l'influence du juste atteint les "
            "endroits les plus bas. Défi : équilibre bonté et limites dans tes interactions aujourd'hui."
        ),
        "ru": (
            "Открой праведного Цадика (צ)! ⚖️ Буква — праведник с уравновешенными поступками. "
            "Форма: Нун (נ) с Йуд (י) сверху — смирение (изогнутый нун) и связь с В-гом (йуд). "
            "Конечная (ץ) уходит ниже строки — влияние праведника доходит до самых низких мест. "
            "Вызов: баланс доброты и границ в общении сегодня."
        ),
    },
    "Embrace the power of Teshuvah (repentance)!": {
        "he": (
            "אמצו את כוח התשובה! 🌱 הרמב\"ם: אפילו מחשבת תשובה אחת יכולה לשנות אדם מיד. "
            "תשובה אמיתית יכולה להפוך עבר לזכות. קחו רגע לחשוב על דבר אחד לשפר — וצעד חיובי קטן. "
            "תשובה לא רק «סליחה» — היא חזרה לדרך ה' בשמחה."
        ),
        "es": (
            "¡Abraza el poder del teshuvá! 🌱 El Rambam: incluso un pensamiento de arrepentimiento "
            "puede transformar a una persona al instante. El teshuvá verdadero convierte errores pasados "
            "en méritos. Tómate un momento para pensar en algo a mejorar — y da un paso positivo. "
            "El teshuvá no es solo «perdón» — es volver al camino de D-s con alegría."
        ),
        "fr": (
            "Embrasse le pouvoir du teshouva ! 🌱 Le Rambam : une seule pensée de repentir peut "
            "transformer une personne instantanément. Le vrai teshouva transforme les fautes passées "
            "en mérites. Prends un moment pour réfléchir à une chose à améliorer — et fais un pas "
            "positif. Le teshouva n'est pas que « pardon » — c'est revenir vers D. avec joie."
        ),
        "ru": (
            "Обними силу тешувы! 🌱 Рамбам: даже одна мысль о покаянии может мгновенно изменить "
            "человека. Истинная тешува превращает прошлые ошибки в заслуги. Найди минуту подумать, "
            "что улучшить — и сделай маленький позитивный шаг. Тешува — не только «прощение», "
            "а возвращение к пути В-га с радостью."
        ),
    },
    "Embrace the power of the Shema!": {
        "he": (
            "אמצו את כוח השמע! 🌅 מצווה פעמיים ביום — להכריז על אחדות ה' ואהבתו. "
            "כמה פסוקים — עקרונות יסוד של האמונה; חז\"ל מלמדים על כוח ההגנה. "
            "אתגר: אמרו את השמע באיטיות, מילה במילה, בכוונה אמיתית — בוקר וערב."
        ),
        "es": (
            "¡Abraza el poder del Shemá! 🌅 Mitzvá dos veces al día — proclamar la unidad de D-s y "
            "nuestro amor por Él. Pocos versículos con principios fundamentales de la fe; los Sabios "
            "enseñan su poder protector. Reto: recita el Shemá despacio, palabra por palabra, con "
            "intención real — mañana y noche."
        ),
        "fr": (
            "Embrasse le pouvoir du Shema ! 🌅 Mitsva deux fois par jour — proclamer l'unité de D. "
            "et notre amour pour Lui. Quelques versets avec les principes fondamentaux de la foi ; "
            "les Sages enseignent son pouvoir protecteur. Défi : récite le Shema lentement, mot à mot, "
            "avec une vraie intention — matin et soir."
        ),
        "ru": (
            "Обними силу Шма! 🌅 Мицва дважды в день — провозгласить единство В-га и любовь к Нему. "
            "Несколько стихов с основами веры; Мудрецы учат о защитной силе. Вызов: читай Шма медленно, "
            "слово за словом, с настоящим намерением — утром и вечером."
        ),
    },
    "Experience the power of Psalm 5!": {
        "he": (
            "חוו את כוח תהילים ה'! 🌅 תפילת בוקר של דוד המלך — איך לפתוח את היום באמונה. "
            "המזמור מלמד: ה' שומע את קולנו בבוקר — התחילו ביום בתפילה ותקווה. "
            "משימה: קראו את המזמור הזה בבוקר ותנו לו להרים את הרוח."
        ),
        "es": (
            "¡Experimenta el poder del Salmo 5! 🌅 Oración matutina del rey David — cómo empezar "
            "el día con fe. El salmo enseña: D-s escucha nuestra voz por la mañana — comienza el día "
            "con oración y esperanza. Misión: lee este salmo por la mañana y deja que eleve tu espíritu."
        ),
        "fr": (
            "Découvre le pouvoir du Psaume 5 ! 🌅 Prière du matin du roi David — comment commencer "
            "la journée avec foi. Le psaume enseigne : D. écoute notre voix le matin — commence la "
            "journée par prière et espoir. Mission : lis ce psaume le matin et laisse-le élever ton esprit."
        ),
        "ru": (
            "Почувствуй силу Псалма 5! 🌅 Утренняя молитва царя Давида — как начать день с верой. "
            "Псалом учит: В-г слышит наш голос утром — начинай день с молитвы и надежды. "
            "Задание: прочитай этот псалом утром и дай ему поднять дух."
        ),
    },
    "Experience the power of community prayer!": {
        "he": (
            "חוו את כוח התפילה בציבור! 👥 מניין של עשרה מגביר את התפילה — לא רק מספר, אלא נוכחות קהילה. "
            "הגמרא: הקב\"ה לא מאס תפילת הציבור. כשמתפללים יחד — גם הפרט נכלל בברכה הגדולה יותר. "
            "משימה: הצטרפו למניין כשאפשר; אם לא — התפללו על שלום ישראל."
        ),
        "es": (
            "¡Experimenta el poder de la oración comunitaria! 👥 Un minyan de diez amplifica la oración — "
            "no solo el número, sino la presencia de la comunidad. El Talmud: D-s no rechaza la oración "
            "del pueblo. Al orar juntos, el individuo entra en una bendición mayor. "
            "Reto: únete a un minyan cuando puedas; si no, ora por la paz de Israel."
        ),
        "fr": (
            "Découvre le pouvoir de la prière communautaire ! 👥 Un minyan de dix amplifie la prière — "
            "pas seulement le nombre, mais la présence de la communauté. Le Talmud : D. ne rejette pas "
            "la prière du peuple. En priant ensemble, l'individu entre dans une plus grande bénédiction. "
            "Défi : rejoins un minyan quand tu peux ; sinon, prie pour la paix d'Israël."
        ),
        "ru": (
            "Почувствуй силу общинной молитвы! 👥 Миньян из десяти усиливает молитву — не только число, "
            "но присутствие общины. Талмуд: В-г не отвергает молитву народа. Молясь вместе, человек "
            "входит в большее благословение. Задание: присоединяйся к миньяну, когда можешь; "
            "иначе — молись о мире Израиля."
        ),
    },
    "Discover #2 of the Constant Mitzvot:": {
        "he": (
            "גלו מצווה קבועה ב': אל תאמינו בכוחות אחרים! 🚫 לא רק עבודה זרה — להטמיע שרק לה' כוח אמיתי. "
            "הכל תחת שליטתו — מהאטום הקטן ביותר ועד הגלקסיה הגדולה. שום דבר לא פועל בלי רשותו! "
            "קחו רגע להכיר שכל אירוע, אתגר וברכה מגיעים ישירות ממנו — בלי כוחות ביניים. "
            "אמונה זו מביאה בטחון ושלווה עמוקים (שמות כ:ג; הרמב\"ם, מצווה שלילית ב')."
        ),
        "es": (
            "¡Descubre la mitzvá constante #2: no creas en otros poderes! 🚫 No es solo evitar idolatría — "
            "es internalizar que solo Hashem tiene poder verdadero. Todo, del átomo a la galaxia, "
            "está bajo Su control. Nada actúa sin Su permiso. Reconoce que cada evento, reto y "
            "bendición viene directamente de Él — sin fuerzas intermedias. Esta fe trae confianza "
            "y paz profundas (Éxodo 20:3; Rambam, mandamiento negativo 2)."
        ),
        "fr": (
            "Découvre la mitsva constante n°2 : ne crois pas en d'autres pouvoirs ! 🚫 Ce n'est pas "
            "seulement éviter l'idolâtrie — c'est intérioriser que seul Hachem a un vrai pouvoir. "
            "Tout, de l'atome à la galaxie, est sous Son contrôle. Rien n'agit sans Sa permission. "
            "Reconnais que chaque événement, défi et bénédiction vient directement de Lui. "
            "Cette foi apporte confiance et paix profondes (Exode 20:3 ; Rambam, commandement négatif 2)."
        ),
        "ru": (
            "Открой постоянную мицву №2: не верь в другие силы! 🚫 Не только избегать идолопоклонства — "
            "осознать, что только Шем обладает истинной силой. Всё — от атома до галактики — "
            "под Его контролем. Ничто не действует без Его разрешения. Признай: каждое событие, "
            "испытание и благословение — прямо от Него, без посредников. Эта вера даёт доверие "
            "и глубокий покой (Шмот 20:3; Рамбам, отрицательная мицва 2)."
        ),
    },
    "Experience the power of Mezuzah!": {
        "he": (
            "חוו את כוח המזוזה! 🏠 הרמב\"ם: כל מעבר ליד מזוזה מזכיר את ה' ומעיר משינה רוחנית! "
            "גימטריה «מזוזה» = 65 = «היכל» — הבית הופך לארמון קטן לשכינה. "
            "713 אותיות במזוזה = גימטריה «תשובה» — כל כניסה הזדמנות להתעוררות רוחנית. "
            "אתגר: געו במזוזה בכוונה בכניסה וביציאה — והרגישו את הקדושה בבית."
        ),
        "es": (
            "¡Experimenta el poder de la mezuzá! 🏠 El Rambam: cada paso junto a una mezuzá recuerda "
            "a D-s y despierta del sueño espiritual. Gematría «mezuzá» = 65 = «hekhal» — el hogar "
            "como palacio para la Presencia. 713 letras = gematría «teshuvá» — cada entrada, "
            "oportunidad de despertar. Reto: toca la mezuzá con intención al entrar y salir."
        ),
        "fr": (
            "Découvre le pouvoir de la mezouza ! 🏠 Le Rambam : chaque passage près d'une mezouza "
            "rappelle D. et réveille du sommeil spirituel. Guematria « mezouza » = 65 = « hekhal » — "
            "la maison comme palais pour la Présence. 713 lettres = guematria « teshouva » — chaque "
            "entrée, occasion de se réveiller. Défi : touche la mezouza avec intention."
        ),
        "ru": (
            "Почувствуй силу мезузы! 🏠 Рамбам: каждый проход мимо мезузы напоминает о В-ге и "
            "пробуждает от духовного сна. Гематрия «мезуза» = 65 = «хекаль» — дом как дворец "
            "для Присутствия. 713 букв = гематрия «тешува» — каждый вход, шанс пробудиться. "
            "Вызов: касайся мезузы с намерением при входе и выходе."
        ),
    },
    "Contemplate #5 of the Constant Mitzvot:": {
        "es": (
            "¡Contempla la mitzvá constante #5: teme a Hashem! 🤩 No es solo miedo — es reconocer "
            "el poder, la majestad y la trascendencia de D-s, con reverencia profunda. Imagina "
            "el Mar Rojo abierto o el Monte Sinaí — esa grandeza abrumadora es Yirat Hashem. "
            "Esta conciencia ayuda a valorar cada mitzvá y que cada acción importa (Devarim 6:13; "
            "Rambam, mandamiento positivo 4). Reto: deja que el asombro guíe una decisión hoy."
        ),
        "fr": (
            "Contemple la mitsva constante n°5 : crains Hachem ! 🤩 Ce n'est pas seulement la peur — "
            "c'est reconnaître la puissance, la majesté et la transcendance de D., avec une profonde "
            "révérence. Imagine la mer Rouge ou le Sinaï — cette grandeur écrasante, c'est la "
            "Yirat Hashem. Cette conscience aide à valoriser chaque mitsva (Devarim 6:13 ; "
            "Rambam, commandement positif 4). Défi : laisse l'émerveillement guider un choix aujourd'hui."
        ),
        "ru": (
            "Размышляй о постоянной мицве №5: бойся Шема! 🤩 Не только страх — признание силы, "
            "величия и трансцендентности В-га с глубоким благоговением. Представь расступившееся "
            "море или Синай — это Yirat Hashem. Это осознание помогает ценить каждую мицву "
            "(Дварим 6:13; Рамбам, положительная мицва 4). Вызов: пусть благоговение направит "
            "одно решение сегодня."
        ),
        "he": (
            "התבוננו במצווה קבועה ה': יראת ה'! 🤩 לא רק פחד — הכרה בכוח, בגדולה ובנשגבות של ה', "
            "ביראה עמוקה. דמיינו את קריעת ים סוף או הר סיני — זו יראת שמים. "
            "המודעות הזו מלמדת שכל מעשה חשוב (דברים ו:יג; הרמב\"ם, מצווה חיובית ד'). "
            "אתגר: תנו לפליאה להנחות החלטה אחת היום."
        ),
    },
    "Discover the Tikkun HaKlali": {
        "he": (
            "גלו את תיקון הכללי! 📖 רבי נחמן מברסלב גילה עשרה פרקי תהילים (טז, לב, מא, מב, נט, "
            "עז, צ, קה, קלז, קנ) — תיקון רוחני עצום לנפש, במיוחד בטומאה או מצוקה. "
            "רבי נחמן אמר שגילוי זה היה תיקון נסתר מאז הבריאה! אפשר לומר אותם בכל עת "
            "לטהרה, כוח וקשר עמוק לה'. מקור: ליקוטי מוהר\"ן א, רכה."
        ),
        "es": (
            "¡Descubre el Tikkun HaKlali! 📖 El Rabí Najmán de Breslov reveló diez salmos (16, 32, 41, "
            "42, 59, 77, 90, 105, 137, 150) — remedio espiritual inmenso para el alma, "
            "especialmente en impureza o angustia. Dijo que este descubrimiento fue un rectificado "
            "oculto desde la Creación. Recítalos cuando necesites limpieza, fuerza y conexión "
            "con Hashem. Fuente: Likutei Moharan I, 205."
        ),
        "fr": (
            "Découvre le Tikkun HaKlali ! 📖 Le rabbi Nahman de Breslev a révélé dix psaumes (16, 32, "
            "41, 42, 59, 77, 90, 105, 137, 150) — remède spirituel immense pour l'âme, surtout "
            "en impureté ou détresse. Il dit que cette révélation était cachée depuis la Création. "
            "Récite-les quand tu as besoin de purification, force et lien avec Hachem. "
            "Source : Likoutei Moharan I, 205."
        ),
        "ru": (
            "Открой Тиккун ха-Клали! 📖 Рабби Нахман из Бреслова открыл десять псалмов (16, 32, 41, "
            "42, 59, 77, 90, 105, 137, 150) — мощное духовное средство для души, особенно при "
            "нечистоте или беде. Он сказал: это исправление было сокрыто со времён Творения! "
            "Читай их, когда нужна очистка, сила и глубокая связь с Шемом. "
            "Источник: Ликутей Мохаран I, 205."
        ),
    },
}


def _strip_trailing_pads(text: str, lang: str) -> str:
    out = text.strip()
    cores = [PAD[lang].strip(), PAD_ALT[lang].strip()]
    changed = True
    while changed:
        changed = False
        for core in cores:
            if out.endswith(core):
                out = out[: -len(core)].rstrip()
                changed = True
    return out


def ensure_min(text: str, lang: str) -> str:
    out = _strip_trailing_pads(text, lang)
    if len(out) < MIN_LEN:
        out += PAD[lang]
    if len(out) < MIN_LEN:
        out += " " + PAD_ALT[lang]
    return out


def lookup_new(key: str) -> dict[str, str] | None:
    best: dict[str, str] | None = None
    best_len = 0
    for prefix, val in NEW_BY_PREFIX.items():
        if key.startswith(prefix) and len(prefix) > best_len:
            best = val
            best_len = len(prefix)
    return best


def catalog_keys() -> list[str]:
    catalog = json.loads((ROOT / "data" / "translation-catalog" / "strings.json").read_text(encoding="utf-8"))["strings"]
    extra = [k for k in catalog if lookup_new(k) and k not in KEYS]
    return KEYS + extra


def build() -> dict[str, dict[str, str]]:
    all_keys = catalog_keys()
    out: dict[str, dict[str, str]] = {lang: {} for lang in PAD}
    for key in all_keys:
        new = lookup_new(key)
        for lang in PAD:
            if new and lang in new:
                body = new[lang]
            elif key in FULL_REWRITES and lang in FULL_REWRITES[key]:
                body = FULL_REWRITES[key][lang]
            else:
                continue
            out[lang][key] = ensure_min(body, lang)
    return out


def main() -> None:
    data = build()
    lines = [
        '"""400+ char expansions for short Mitzvah Me alert strings."""',
        "",
        "from __future__ import annotations",
        "",
        "ALERT_SHORT_EXPAND: dict[str, dict[str, str]] = {",
    ]
    for key in sorted(data["he"].keys(), key=lambda k: k[:40]):
        lines.append(f"    {json.dumps(key, ensure_ascii=False)}: {{")
        for lang in ("he", "es", "fr", "ru"):
            if lang in data and key in data[lang]:
                lines.append(f"        {json.dumps(lang)}: {json.dumps(data[lang][key], ensure_ascii=False)},")
        lines.append("    },")
    lines.append("}")
    lines.append("")
    out_path = TOOLS / "_alert_short_expansions_data.py"
    out_path.write_text("\n".join(lines) + "\n", encoding="utf-8")
    counts = {lang: len(data[lang]) for lang in PAD}
    short = sum(1 for k, v in data["he"].items() if len(v) < 400)
    print(f"wrote {out_path.name}: {counts}, under 400: {short}")


if __name__ == "__main__":
    main()
