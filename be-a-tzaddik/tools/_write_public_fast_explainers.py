#!/usr/bin/env python3
"""Write human/public_fast_explainers.json from _public_fast_template_data."""
from __future__ import annotations

import json
from pathlib import Path

from _mourning_period_translations import (
    TISHA_FAST_EN,
    TISHA_FAST_ES,
    TISHA_FAST_FR,
    TISHA_FAST_HE,
    TISHA_FAST_RU,
)

from _public_fast_template_data import (
    ALOT_LINE_FALLBACK,
    ALOT_LINE_WITH_TIME,
    APPROX_TIME_SUFFIX,
    COMMON_FAST_LAWS,
    EREV_MINOR_FAST,
    EREV_TISHA_BEAV,
    EREV_YOM_KIPPUR,
    ESTHER_FAST_DAY,
    FAST_DAY_SUBTITLE_TEMPLATE,
    FAST_TIMING_DAWN,
    FAST_TIMING_SUNSET,
    FRIDAY_NOTE_10_TEVET,
    FRIDAY_NOTE_ESTHER,
    MINOR_FAST_DAY,
    MOTZEI_YOM_KIPPUR_MEAL,
    PREP_TITLE,
    FAST_DAY_TITLE,
    SHABBAT_TISHA_NOTE,
    TISHA_FAST_DAY,
    TZET_LINE,
    TZET_TOMORROW_FALLBACK,
    TZET_TOMORROW_TIME,
    YOM_KIPPUR_FAST_DAY,
)

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / "data" / "translation-catalog" / "human" / "public_fast_explainers.json"

# English key -> {lang: translation}  (placeholders must match English keys exactly)
FIXES: dict[str, dict[str, str]] = {
    COMMON_FAST_LAWS: {
        "he": """מי חייב בצום:
מבוגרים יהודים מגיל בר/בת מצווה שבריאים מספיק לצום. ילדים מחונכים בהדרגה — שאלו את הרב. נשים בהריון, מניקות, לאחר לידה, וכל חולה — שאלו פוסק; רבים אוכלים בשיעורים או פטורים לגמרי.

אם אכלתם בטעות:
אם שכחתם ואכלתם או שתיתם שלא במתכוון, אפשר להמשיך לצום כשזוכרים — הצום תקף (שולחן ערוך או\"ח תרס\"ח:א).

אם אינכם יכולים לצום:
אל תסכנו את בריאותכם. שאלו רב על שיעורים, דחיית הצום או פטור. פיקוח נפש גובר על הצום.""",
        "es": """Quién debe ayunar:
Adultos judíos desde la edad bar/bat mitzvá con salud suficiente para ayunar. Los niños se educan gradualmente — consulte a su rav. Mujeres embarazadas, en lactancia o recién paridas, y quien esté enfermo, deben consultar a un posek — muchos comen en cantidades medidas (shiurim) o están totalmente exentos.

Si comió por error:
Si olvidó y comió o bebió sin intención, puede continuar ayunando al recordarlo — el ayuno sigue siendo válido (Shuljan Aruj O.J. 568:1).

Si no puede ayunar:
No ponga en riesgo su salud. Consulte a su rav sobre shiurim, posponer el ayuno o exención. Pikuaj nefesh prevalece sobre el ayuno.""",
        "fr": """Qui doit jeûner :
Adultes juifs à partir de l'âge bar/bat mitzvah en assez bonne santé pour jeûner. Les enfants sont formés progressivement — consultez votre rav. Femmes enceintes, allaitantes ou récemment accouchées, et toute personne malade, doivent consulter un posek — beaucoup mangent en quantités mesurées (shiourim) ou sont entièrement exemptés.

Si vous avez mangé par erreur :
Si vous avez oublié et mangé ou bu involontairement, vous pouvez continuer à jeûner une fois que vous vous en souvenez — le jeûne reste valide (Choulhan Aroukh O.H. 568:1).

Si vous ne pouvez pas jeûner :
Ne mettez pas votre santé en danger. Consultez votre rav pour les shiourim, reporter le jeûne ou une exemption. Pikuah nefesh prime sur le jeûne.""",
        "ru": """Кто обязан поститься:
Взрослые евреи с возраста бар/бат мицва, достаточно здоровые для поста. Детей приучают постепенно — спросите раввина. Беременным, кормящим, недавно родившим и больным — к позеку; многие едят в мерных количествах (шиурим) или полностью освобождены.

Если съели по ошибке:
Если забыли и ели или пили ненамеренно, можно продолжать пост, когда вспомнили — пост остаётся действительным (Шулхан Арух О.Х. 568:1).

Если не можете поститься:
Не рискуйте здоровьем. Спросите раввина о шиурах, переносе поста или освобождении. Пикуах нефеш важнее поста.""",
    },
    MINOR_FAST_DAY: {
        "es": """Hoy es $name — $meaning

Reglas de ayuno menor:
• No comer ni beber desde el amanecer hasta el anochecer.
• Lavado, música y ducha están generalmente permitidos (a diferencia de Tisha B'Av y Yom Kippur).
• Pueden recitarse selichot y oraciones especiales en la sinagoga — consulte el horario de su comunidad.

$common""",
        "fr": """Aujourd'hui, c'est $name — $meaning

Règles de jeûne mineur :
• Ne pas manger ni boire de l'aube jusqu'à la tombée de la nuit.
• Le lavage, la musique et la douche sont généralement permis (contrairement à Tisha B'Av et Yom Kippour).
• Des selichot et prières spéciales peuvent être récitées à la synagogue — vérifiez le calendrier de votre communauté.

$common""",
        "ru": """Сегодня $name — $meaning

Правила малого поста:
• Не есть и не пить с рассвета до выхода звёзд (цейт).
• Мытьё, музыка и душ, как правило, разрешены (в отличие от Тиша б'Ав и Йом Кипура).
• В синагоге могут читаться особые селихот и молитвы — проверьте расписание вашей общины.

$common""",
        "he": """היום $name — $meaning

כללי צום קל:
• אין אכילה ושתייה משחר עד צאת הכוכבים.
• רחצה, מוזיקה ומקלחת מותרים בדרך כלל (בניגוד לט' באב ויום הכיפורים).
• ייתכנו סליחות ותפילות מיוחדות בבית הכנסת — בדקו לוח זמנים בקהילה.

$common""",
    },
    ESTHER_FAST_DAY: {
        "es": """Hoy es el Ayuno de Ester (Taanit Ester) — conmemora el ayuno que Ester convocó antes de acercarse al rey (Ester 4:16).

Reglas de ayuno menor con contexto de Purim:
• No comer ni beber desde el amanecer hasta el anochecer.
• Música, ducha por placer y zapatos de cuero están permitidos — es un ayuno menor, no como Tisha B'Av.
• Las mitzvot de Purim comienzan esta noche/mañana según su calendario (14 Adar, o 15 en ciudades amuralladas). Este ayuno es la preparación espiritual antes del gozo de Purim.

$common""",
        "fr": """Aujourd'hui, c'est le jeûne d'Esther (Taanit Esther) — commémorant le jeûne qu'Esther convoqua avant de s'approcher du roi (Esther 4:16).

Règles de jeûne mineur dans le contexte de Pourim :
• Ne pas manger ni boire de l'aube à la tombée de la nuit.
• Musique, douche pour le plaisir et chaussures en cuir sont permis — c'est un jeûne mineur, pas comme Tisha B'Av.
• Les mitzvot de Pourim commencent ce soir/demain selon votre calendrier (14 Adar, ou 15 dans les villes fortifiées). Ce jeûne est la préparation spirituelle avant la joie de Pourim.

$common""",
        "ru": """Сегодня Пост Эстер (Таанит Эстер) — в память о посте, который Эстер объявила перед тем, как приблизиться к царю (Эстер 4:16).

Правила малого поста в контексте Пурима:
• Не есть и не пить с рассвета до выхода звёзд (цейт).
• Музыка, душ для удовольствия и кожаная обувь разрешены — это малый пост, не как Тиша б'Ав.
• Мицвот Пурима начинаются сегодня вечером/завтра по вашему календарю (14 Адар или 15 в городах со стенами). Этот пост — духовная подготовка к радости Пурима.

$common""",
        "he": """היום תענית אסתר — לזכר הצום שאסתר קראה לה לפני הגשתה לפני המלך (אסתר ד:טז).

כללי צום קל בהקשר פורים:
• אין אכילה ושתייה משחר עד צאת הכוכבים.
• מוזיקה, מקלחת להנאה ונעלי עור מותרים — זה צום קל, לא כמו ט' באב.
• מצוות פורים מתחילות הלילה/מחר לפי לוח השנה (י\"ד אדר, או ט\"ו בערים מוקפות חומה). הצום הזה הוא ההכנה הרוחנית לפני שמחת פורים.

$common""",
    },
    YOM_KIPPUR_FAST_DAY: {
        "es": """Hoy es Yom Kippur — el Día de la Expiación.

El ayuno:
• Comenzó al atardecer anoche (muchas comunidades lo aceptaron antes con tosefet — añadiendo tiempo del erev a Yom Kippur) y termina al anochecer esta noche$tzeitSuffix.
• Cinco aflicciones: sin comer/beber, sin zapatos de cuero, sin baño por placer, sin ungir, sin relaciones conyugales.
• Muchos visten de blanco; hombres casados que usan kittel lo hacen; se usa talit todo el día.
• El día se dedica a la oración — Kol Nidrei fue anoche antes del atardecer (hatarat nedarim no puede hacerse en Yom Kippur mismo), luego Shacharit, Musaf, Minjá, Neilá, y después Maariv y Havdalah al anochecer.

Después del anochecer: coma la comida de Motzei Yom Kippur (vea su lista). La tradición jabad conecta esta comida con el sustento del año.

$common""",
        "fr": """Aujourd'hui, c'est Yom Kippour — le Jour de l'Expiation.

Le jeûne :
• A commencé au coucher du soleil hier soir (de nombreuses communautés l'ont accepté plus tôt avec tosefet — ajout de temps de l'erev à Yom Kippour) et se termine à la tombée de la nuit ce soir$tzeitSuffix.
• Cinq afflictions : pas de nourriture/boisson, pas de chaussures en cuir, pas de bain pour le plaisir, pas d'onction, pas de relations conjugales.
• Beaucoup portent du blanc ; les hommes mariés qui portent un kittel le font ; le tallit est porté toute la journée.
• La journée est consacrée à la prière — Kol Nidrei était hier soir avant le coucher du soleil (hatarat nedarim ne peut pas se faire le jour de Yom Kippour lui-même), puis Shacharit, Moussaf, Min'ha, Neïla, puis Maariv et Havdalah après la tombée de la nuit.

Après la tombée de la nuit : prenez le repas de Motzei Yom Kippour (voir votre liste). La tradition Habad relie ce repas à la subsistance de l'année.

$common""",
        "ru": """Сегодня Йом Кипур — День Искупления.

Пост:
• Начался на закате вчера вечером (многие общины приняли его раньше с тосефет — добавлением времени от Эрева к Йом Кипуру) и заканчивается после выхода звёзд (цейт) сегодня$tzeitSuffix.
• Пять ограничений: нет еды/питья, нет кожаной обуви, нет купания для удовольствия, нет помазания, нет супружеских отношений.
• Многие носят белое; женатые мужчины в кителе делают это; талит носят весь день.
• День посвящён молитве — Кол Нидре был вчера до заката (хатарат недарим нельзя в сам Йом Кипур), затем Шахарит, Мусаф, Минха, Неила, потом Маарив и Хавдала после выхода звёзд (цейт).

После выхода звёзд: съешьте трапезу Моцэи Йом Кипур (см. ваш список). Традиция Хабада связывает эту трапезу с пропитанием на год.

$common""",
        "he": """היום יום הכיפורים — יום הכיפורים.

הצום:
• התחיל בשקיעה אמש (קהילות רבות קיבלו אותו מוקדם יותר עם תוספת — הוספת זמן מערב ליום הכיפורים) ומסתיים בצאת הכוכבים הלילה$tzeitSuffix.
• חמש עינויים: אין אכילה/שתייה, אין נעלי עור, אין רחצה להנאה, אין סיכה, אין יחסי אישות.
• רבים לובשים לבן; גברים נשואים שלובשים כתונת לבן עושים כן; טלית נלבשת כל היום.
• היום מוקדש לתפילה — כל נדרי היה אמש לפני השקיעה (התרת נדרים אינה אפשרית ביום הכיפורים עצמו), אחר כך שחרית, מוסף, מנחה, נעילה, ואז מעריב והבדלה אחרי צאת הכוכבים.

אחרי צאת הכוכבים: אכלו את ארוחת מוצאי יום הכיפורים (ראו את רשימת הבדיקה). מסורת חב\"ד מקשרת ארוחה זו לפרנסה לשנה.

$common""",
    },
    TISHA_FAST_DAY: {
        "he": TISHA_FAST_HE,
        "es": TISHA_FAST_ES,
        "fr": TISHA_FAST_FR,
        "ru": TISHA_FAST_RU,
    },
    EREV_MINOR_FAST: {
        "he": """מחר $fastName — צום ציבורי משחר (עלות השחר) עד צאת הכוכבים (צאת).

אם מתכננים לאכול לפני תחילת הצום:
• קבעו תנאי בלילה שלפניו: \"אם אתעורר רעב לפני השחר, אוכל.\" בלי תנאי זה, התעוררות מוקדמת ואכילה עלולה לאסור אכילה נוספת עד תחילת הצום בשחר (שולחן ערוך או\"ח תרס\"ד:א).
• אם מתעוררים לפני השחר ורוצים לאכול, מותר לשתות מים ולאכול מאכלים שלא מבושלים בדרך כלל לארוחה — למשל עוגה, פירות או דגנים. ארוחה חמה מלאה שנויה במחלוקת; רבים נמנעים מארוחה מבושלת רשמית לאחר שהחליטו לצום (משנה ברורה תרס\"ד:ח-ט).
• הפסיקו אכילה ושתייה בעלות השחר$alotLine.

הכנה מעשית הלילה:
• שתו היטב ואכלו ארוחת ערב מאוזנת.
• תכננו בוקר קל יותר אם לא תאכלו לפני השחר.
• בדקו את לוח הזמנים בבית הכנסת אם מתכננים להגיע לתפילות מיוחדות.

מי חייב בצום: מבוגרים יהודים (מגיל בר/בת מצווה ומעלה) בבריאות טובה. ילדים מתחת לגיל בר/בת מצווה אינם חייבים — חנכו בהדרגה לפי הרב.$fridayNote""",
        "es": """Mañana es $fastName — un ayuno público desde el alba halájica (alot hashachar) hasta la caída de la noche (tzeit).

Si planea comer antes de que comience el ayuno:
• Establezca una condición mental (tanai) la noche anterior: «Si despierto con hambre antes del alba, comeré.» Sin esta condición, despertar temprano y comer puede prohibirle comer de nuevo hasta que el ayuno comience oficialmente al alba (Shulján Aruj O.J. 564:1).
• Si despierta antes del alba y quiere comer, puede beber agua y comer alimentos que normalmente no se cocinan para una comida — p. ej. un trozo de pastel, fruta o cereal. Una comida caliente completa es disputada; muchos evitan una comida cocida formal una vez decidido ayunar (Mishná Berurá 564:8–9).
• Deje de comer y beber en alot hashachar$alotLine.

Preparación práctica esta noche:
• Hidrátese bien y coma una cena equilibrada.
• Planee una mañana más ligera si no comerá antes del alba.
• Conozca el horario de su sinagoga si planea asistir a oraciones especiales.

Quién debe ayunar: adultos judíos (edad bar/bat mitzvá o mayor) en buena salud. Los niños menores no están obligados — entrénelos gradualmente según su rav.$fridayNote""",
        "fr": """Demain, c'est $fastName — un jeûne public de l'aube halakhique (alot hashachar) à la tombée de la nuit (tzeit).

Si vous prévoyez de manger avant le jeûne :
• Fixez une condition mentale (tanai) la veille : « Si je me réveille affamé avant l'aube, je mangerai. » Sans cette condition, se réveiller tôt et manger peut vous interdire de manger à nouveau jusqu'au début officiel du jeûne à l'aube (Choulhan Aroukh O.H. 564:1).
• Si vous vous réveillez avant l'aube et voulez manger, vous pouvez boire de l'eau et manger des aliments normalement non cuits pour un repas — p. ex. un morceau de gâteau, des fruits ou des céréales. Un repas chaud complet est disputé ; beaucoup évitent un repas cuit formel une fois le jeûne décidé (Mishna Beroura 564:8–9).
• Arrêtez toute nourriture et boisson à alot hashachar$alotLine.

Préparation pratique ce soir :
• Hydratez-vous bien et prenez un dîner équilibré.
• Prévoyez un matin plus léger si vous ne mangerez pas avant l'aube.
• Connaissez l'horaire de votre synagogue si vous prévoyez des prières spéciales.

Qui doit jeûner : adultes juifs (âge bar/bat mitzvah et plus) en bonne santé. Les enfants plus jeunes ne sont pas tenus — formez-les progressivement selon votre rav.$fridayNote""",
        "ru": """Завтра $fastName — общественный пост с рассвета (алот ха-шахар) до выхода звёзд (цейт).

Если планируете есть до начала поста:
• Установите условие (танаи) накануне: «Если проснусь голодным до рассвета, буду есть.» Без этого раннее пробуждение и еда могут запретить есть снова до официального начала поста на рассвете (Шулхан Арух О.Х. 564:1).
• Если проснулись до рассвета и хотите есть, можно пить воду и есть продукты, обычно не готовящиеся для трапезы — напр. кусок торта, фрукты или хлопья. Полноценная горячая трапеза спорна; многие избегают формальной горячей трапезы после решения поститься (Мишна Берура 564:8–9).
• Прекратите есть и пить в алот ха-шахар$alotLine.

Практическая подготовка сегодня вечером:
• Хорошо пейте и съешьте сбалансированный ужин.
• Планируйте более лёгкое утро, если не будете есть до рассвета.
• Узнайте расписание синагоги, если планируете особые молитвы.

Кто обязан поститься: взрослые евреи (возраст бар/бат мицва и старше) в добром здоровье. Дети младше не обязаны — приучайте постепенно по указанию раввина.$fridayNote""",
    },
    EREV_YOM_KIPPUR: {
        "he": """היום ערב יום הכיפורים — יום ייחודי של אכילה לפני הצום הקדוש ביותר של השנה.

מצווה לאכול היום:
• מצווה לאכול ולשתות בנדיבות כל היום ערב יום הכיפורים (שולחן ערוך או\"ח תר\"ד:א; משנה ברורה).
• ארוחה חגיגית בסגנון יום טוב — רבים מתחילים בהדלקת נרות וקידוש כמו ביום טוב (שאלו את הרב למנהגכם).
• התלמוד משווה אכילה בערב יום הכיפורים לצום ביום הכיפורים עצמו לזכות (ראש השנה ט א).

לפני תחילת הצום:
• זמן הצום ההלכתי: יום הכיפורים נמשך משקיעה הערב$sunsetLine עד צאת הכוכבים מחר ($tzeitTomorrow).
• תוספת (הוספה מחול על הקודש): קהילות רבות מתחילות להימנע ממזון ושתייה מעט לפני השקיעה — קבלת הצום מוקדם, כפי ש\"מוסיפים מחול על היום הקדוש\" (יומא פא ב; שולחן ערוך או\"ח תר\"ח:א). כמה דקות מוקדם משתנה לפי הקהילה; שאלו את הרב. זמן השקיעה באפליקציה הוא כשהצום בהחלט בתוקף, לא בהכרח כשבית הכנסת מתחיל כל נדרי.
• כל נדרי לפני השקיעה: כל נדרי הוא התרת נדרים לפני יום הכיפורים. בית דין אינו יכול להתכנס ביום הכיפורים עצמו, ולכן כל נדרי חייב להסתיים לפני השקיעה בעודו חול (ראש השנה ט ב; שולחן ערוך או\"ח תרי\"ט:א). לכן התפילות מתחילות הרבה לפני השקיעה — כדי שהקהילה תסיים כל נדרי ותתחיל את הצום עם תוספת בעוד זה עדיין ערב.
• בקשו סליחה ממי שאולי פגעתם; תנו צדקה; טבלו במקווה אם זה מנהגכם.
• הדליקו נרות יום הכיפורים לפני השקיעה (נשים נשואות מדליקות במסורת; אחרים לפי הקהילה — שאלו את הרב).

ארוחת מוצאי יום הכיפורים:
• לאחר סיום הצום מחר בלילה, מצווה לאכול ארוחה נכונה — לא רק חטיף. לרבים יש ארוחת שבירת צום חגיגית.
• מסורת חב\"ד (בעל התניא) מלמדת שפרנסת השנה קשורה במיוחד לארוחה זו לאחר יום הכיפורים — אכלו בשמחה ובכוונה אחרי התפילה והבדלה.

מי צם מחר: מבוגרים יהודים בריאים מגיל בר/בת מצווה. חולים, נשים בהריון, מניקות או לאחר לידה — שאלו את הרב; לעתים אוכלים בכמויות קטנות (שיעורים) או פטורים.""",
        "es": """Hoy es Erev Yom Kippur — un día único de comida antes del ayuno más sagrado del año.

Mitzvá de comer hoy:
• Es mitzvá comer y beber generosamente todo el día de Erev Yom Kippur (Shuljan Aruj O.J. 604:1; Mishná Berurá).
• Tenga una comida festiva al estilo Yom Tov — muchos comienzan con encendido de velas y Kidush como en Yom Tov (consulte a su rav su minhag).
• El Talmud compara comer en Erev Yom Kippur con ayunar en Yom Kippur mismo en mérito (Rosh Hashaná 9a).

Antes de que comience el ayuno:
• Tiempo halájico del ayuno: Yom Kippur va del atardecer esta noche$sunsetLine hasta el anochecer mañana ($tzeitTomorrow).
• Tosefet (añadir del día profano): muchas comunidades dejan de comer y beber un poco antes del atardecer — aceptando el ayuno temprano, como «añadimos del día profano al día santo» (Yoma 81b; Shuljan Aruj O.J. 608:1). Cuántos minutos antes varía; consulte a su rav. La hora del atardecer en esta app es cuando el ayuno está definitivamente en vigor, no necesariamente cuando su sinagoga comienza Kol Nidrei.
• Kol Nidrei antes del atardecer: Kol Nidrei es la anulación de votos (hatarat nedarim) antes del Día de la Expiación. Un beit din no puede reunirse en Yom Kippur mismo, por lo que Kol Nidrei debe completarse antes del atardecer mientras aún es día profano (Rosh Hashaná 9b; Shuljan Aruj O.J. 619:1). Por eso los servicios suelen comenzar mucho antes del atardecer.
• Pida perdón a quienes haya lastimado; dé tzedaká; moje en mikve si es su costumbre.
• Encienda velas de Yom Kippur antes del atardecer (las mujeres casadas suelen encenderlas; otros siguen la comunidad — consulte a su rav).

Comida de Motzei Yom Kippur:
• Después de que termine el ayuno mañana por la noche, es mitzvá comer una comida adecuada — no solo un bocado. Muchos tienen un desayuno festivo tras el ayuno.
• La tradición jabad (Baal HaTanya) enseña que el sustento (parnasa) del año está especialmente ligado a esta comida — coma con alegría e intención después de davening y Havdalah.

Quién ayuna mañana: adultos judíos sanos desde la edad bar/bat mitzvá. Enfermos, embarazadas, lactantes o recién paridas deben consultar a un rav — a menudo comen en cantidades menores (shiurim) o están exentos.""",
        "fr": """Aujourd'hui, c'est Erev Yom Kippour — une journée unique de repas avant le jeûne le plus saint de l'année.

Mitzvah de manger aujourd'hui :
• Il est mitzvah de manger et boire généreusement toute la journée d'Erev Yom Kippour (Choulhan Aroukh O.H. 604:1 ; Mishna Beroura).
• Prenez un repas festif de style Yom Tov — beaucoup commencent par l'allumage des bougies et le Kiddouch comme pour Yom Tov (demandez à votre rav pour votre minhag).
• Le Talmud compare manger à Erev Yom Kippour à jeûner le jour de Yom Kippour lui-même pour le mérite (Rosh Hashana 9a).

Avant le début du jeûne :
• Temps halakhique du jeûne : Yom Kippour va du coucher du soleil ce soir$sunsetLine à la tombée de la nuit demain ($tzeitTomorrow).
• Tosefet (ajout du jour profane) : de nombreuses communautés s'abstiennent de nourriture et boisson un peu avant le coucher du soleil — acceptant le jeûne tôt, comme nous « ajoutons du jour profane au jour saint » (Yoma 81b ; Choulhan Aroukh O.H. 608:1). Le nombre de minutes varie ; consultez votre rav. L'heure du coucher du soleil dans cette app est celle où le jeûne est définitivement en vigueur, pas nécessairement quand votre synagogue commence Kol Nidrei.
• Kol Nidrei avant le coucher du soleil : Kol Nidrei est l'annulation des vœux (hatarat nedarim) avant le Jour de l'Expiation. Un beit din ne peut pas se réunir le jour de Yom Kippour lui-même, donc Kol Nidrei doit être achevé avant le coucher du soleil tant que c'est encore un jour profane (Rosh Hashana 9b ; Choulhan Aroukh O.H. 619:1). C'est pourquoi les offices commencent souvent bien avant le coucher du soleil.
• Demandez pardon à ceux que vous avez pu blesser ; donnez tsedaka ; immergez-vous au mikvé si c'est votre coutume.
• Allumez les bougies de Yom Kippour avant le coucher du soleil (les femmes mariées allument traditionnellement ; les autres suivent la communauté — consultez votre rav).

Repas de Motzei Yom Kippour :
• Après la fin du jeûne demain soir, c'est une mitzvah de prendre un bon repas — pas seulement une bouchée. Beaucoup ont un repas festif de rupture du jeûne.
• La tradition Habad (Baal HaTanya) enseigne que la subsistance (parnassa) de l'année est particulièrement liée à ce repas — mangez avec joie et intention après la tefillah et Havdalah.

Qui jeûne demain : adultes juifs en bonne santé à partir de l'âge bar/bat mitzvah. Malades, femmes enceintes, allaitantes ou récemment accouchées doivent consulter un rav — souvent ils mangent en petites quantités (shiourim) ou sont exemptés.""",
        "ru": """Сегодня Эрев Йом Кипур — особый день еды перед самым святым постом года.

Мицва есть сегодня:
• Мицва щедро есть и пить весь день Эрева Йом Кипура (Шулхан Арух О.Х. 604:1; Мишна Берура).
• Праздничная трапеза в духе Йом Това — многие начинают с зажжения свечей и Кидуша, как в Йом Тов (уточните минхаг у раввина).
• Талмуд сравнивает еду в Эрев Йом Кипур с постом в сам Йом Кипур по заслуге (Рош ха-Шана 9а).

Перед началом поста:
• Галахическое время поста: Йом Кипур длится от заката сегодня вечером$sunsetLine до выхода звёзд (цейт) завтра ($tzeitTomorrow).
• Тосефет (добавление от будней): многие общины воздерживаются от еды и питья немного до заката — принимая пост раньше, «добавляя от будней к святому дню» (Йома 81б; Шулхан Арух О.Х. 608:1). Сколько минут раньше — по общине; спросите раввина. Время заката в приложении — когда пост точно действует, не обязательно когда синагога начинает Кол Нидре.
• Кол Нидре до заката: Кол Нидре — расторжение обетов (хатарат недарим) перед Днём Искупления. Бейт дин не может собраться в сам Йом Кипур, поэтому Кол Нидре должен завершиться до заката, пока ещё будний день (Рош ха-Шана 9б; Шулхан Арух О.Х. 619:1). Поэтому службы часто начинаются задолго до заката.
• Просите прощения у тех, кого могли обидеть; давайте цдаку; погружайтесь в микву, если это ваш обычай.
• Зажгите свечи Йом Кипура до заката (замужние женщины традиционно зажигают; другие — по общине — спросите раввина).

Трапеза Моцэи Йом Кипур:
• После окончания поста завтра вечером — мицва съесть полноценную трапезу, а не только перекус. У многих праздничная трапеза после поста.
• Традиция Хабада (Бааль а-Танья) учит, что пропитание (парнаса) на год особенно связано с этой трапезой — ешьте с радостью и намерением после молитвы и Хавдалы.

Кто постится завтра: здоровые взрослые евреи с возраста бар/бат мицва. Больным, беременным, кормящим или недавно родившим — к раввину; часто едят малыми порциями (шиурим) или освобождены.""",
    },
    EREV_TISHA_BEAV: {
        "he": """ערב ט' באב מכין אותנו לאבל על חורבן בית המקדש.

מתי מתחיל הצום:
• ט' באב מתחיל בשקיעה הערב$sunsetLine ונמשך עד צאת הכוכבים מחר בלילה — לא משחר עד לילה כמו צומות קלים.

הגבלות מחצות היום (חצות הלכתי) היום:
• מחצות ואילך: אל תאכלו בשר ואל תשתו יין (חלק מרחיבים הגבלות — לפי המנהג).
• הפחיתו פעילויות הנאה; הימנעו מכביסה, תספורת ושחייה לפי מנהגי האבל.

סעודה המפסקת — הארוחה האחרונה לפני הצום:
• אכלו ארוחה פשוטה ביחידות, יושבים על הרצפה או כיסא נמוך, עם מנה מבושלת אחת בלבד (למשל ביצה קשה טבולה באפר, או לחם עם מים קרים).
• אל תאכלו שתי מנות מבושלות יחד; אל תסעדו בשכיבה; אל תברכו אחרים ב\"שלום\".
• סיימו לפני השקיעה; לאחר מכן מותר רק מים עד תחילת הצום.
• רבים אומרים ברכת המזון ואז מחליפים לנעליים שאינן מעור.

משקיעה הערב עד צאת הכוכבים מחר:
• חמש עינויים (כמו ביום הכיפורים): אין אכילה/שתייה, אין נעלי עור, אין רחצה להנאה, אין סיכה, אין יחסי אישות.
• בנוסף: אין לימוד תורה למעט נושאי אבל (איכה, איוב, הלכות אבל); רבים יושבים על הרצפה עד חצות מחר; קינות נאמרות.$shabbatNote

שאלו את הרב לפרטים אם אתם חולים, בהריון או מניקים.""",
        "es": """Erev Tisha B'Av nos prepara para el duelo por la destrucción del Templo.

Cuándo comienza el ayuno:
• Tisha B'Av comienza al atardecer esta noche$sunsetLine y continúa hasta el anochecer mañana por la noche — no de amanecer a atardecer como los ayunos menores.

Restricciones desde chatzot (mediodía halájico) hoy:
• Desde chatzot en adelante: no comer carne ni beber vino (algunos extienden otras restricciones — siga su minhag).
• Reduzca actividades placenteras; evite lavandería, cortes de pelo y natación según costumbres de duelo.

Seudah hamafseket — la comida final antes del ayuno:
• Coma una comida sencilla solo, sentado en el suelo o en un taburete bajo, con solo un plato cocido (p. ej. huevo duro en ceniza, o pan con agua fría).
• No coma dos platos cocidos juntos; no se recline; no salude con \"hola\".
• Termine antes del atardecer; después solo se permite agua hasta que comience el ayuno.
• Muchos recitan Birkat HaMazon y luego cambian a zapatos sin cuero antes del atardecer.

Después del atardecer esta noche hasta el anochecer mañana:
• Cinco aflicciones (como Yom Kippur): sin comer/beber, sin zapatos de cuero, sin baño por placer, sin ungir, sin relaciones conyugales.
• Además: sin estudio de Torá excepto temas tristes (Eijá, Iyov, leyes de duelo); muchos se sientan en el suelo hasta chatzot mañana; se recitan kinot.$shabbatNote

Consulte a su rav si está enfermo, embarazada o amamantando.""",
        "fr": """Erev Tisha B'Av nous prépare au deuil de la destruction du Temple.

Quand le jeûne commence :
• Tisha B'Av commence au coucher du soleil ce soir$sunsetLine et continue jusqu'à la tombée de la nuit demain soir — pas de l'aube au crépuscule comme les jeûnes mineurs.

Restrictions à partir de hatzot (midi halakhique) aujourd'hui :
• À partir de hatzot : ne pas manger de viande ni boire de vin (certains étendent d'autres restrictions — suivez votre minhag).
• Réduisez les activités plaisantes ; évitez lessive, coupes de cheveux et natation selon les coutumes de deuil.

Seudah hamafseket — le repas final avant le jeûne :
• Prenez un repas simple seul, assis sur le sol ou un tabouret bas, avec un seul plat cuit (p. ex. œuf dur trempé dans la cendre, ou pain avec eau froide).
• Ne mangez pas deux plats cuits ensemble ; ne vous allongez pas ; ne saluez pas avec « bonjour ».
• Terminez avant le coucher du soleil ; ensuite seule l'eau est permise jusqu'au début du jeûne.
• Beaucoup récitent Birkat HaMazon puis passent à des chaussures non en cuir avant le coucher du soleil.

Après le coucher du soleil ce soir jusqu'à la tombée de la nuit demain :
• Cinq afflictions (comme Yom Kippour) : pas de nourriture/boisson, pas de chaussures en cuir, pas de bain pour le plaisir, pas d'onction, pas de relations conjugales.
• De plus : pas d'étude de Torah sauf sujets tristes (Eikha, Iyov, lois du deuil) ; beaucoup s'assoient sur le sol jusqu'à hatzot demain ; kinot sont récitées.$shabbatNote

Consultez votre rav si vous êtes malade, enceinte ou allaitez.""",
        "ru": """Эрев Тиша б'Ав готовит нас к скорби о разрушении Храма.

Когда начинается пост:
• Тиша б'Ав начинается на закате сегодня вечером$sunsetLine и продолжается до выхода звёзд (цейт) завтра вечером — не от рассвета до заката, как в малых постах.

Ограничения с хацот (галахический полдень) сегодня:
• С хацот: не есть мясо и не пить вино (некоторые расширяют ограничения — следуйте минхагу).
• Сократите приятные занятия; избегайте стирки, стрижки и плавания по обычаям траура.

Сеуда амафсекет — последняя трапеза перед постом:
• Ешьте простую трапезу в одиночестве, сидя на полу или низком табурете, только с одним приготовленным блюдом (напр. варёное яйцо в пепле или хлеб с холодной водой).
• Не ешьте два приготовленных блюда вместе; не возлежите; не приветствуйте «привет».
• Закончите до заката; после разрешена только вода до начала поста.
• Многие читают Биркат а-Мазон и переобуваются в немеховую обувь до заката.

После заката сегодня до выхода звёзд (цейт) завтра:
• Пять ограничений (как в Йом Кипур): нет еды/питья, нет кожаной обуви, нет купания для удовольствия, нет помазания, нет супружеских отношений.
• Кроме того: нет изучения Торы, кроме скорбных тем (Эйха, Иов, законы траура); многие сидят на полу до хацот завтра; читают кинот.$shabbatNote

Спросите раввина, если вы больны, беременны или кормите грудью.""",
    },
    MOTZEI_YOM_KIPPUR_MEAL: {
        "he": """לאחר שיום הכיפורים מסתיים בצאת הכוכבים$tzeitLine, מצווה לאכול ארוחה נכונה — לא רק ביס מהיר.

מה לעשות:
• השלימו מעריב והבדלה (הבדלה כוללת נר שהדליקו לאורך יום הכיפורים, יין וללא בשמים).
• אכלו ארוחת שבירת צום חגיגית — רבים מכינים מזון מראש כי איסורי בישול ביום הכיפורים מסתיימים בצאת הכוכבים.
• מסורת חב\"ד (בעל התניא) מלמדת שפרנסת השנה קשורה במיוחד לארוחה זו — אכלו בשמחה ובכוונה.

פריט זה זמין בצאת הכוכבים לאחר סיום הצום.""",
        "es": """Después de que Yom Kippur termine al anochecer$tzeitLine, es una mitzvá comer una comida adecuada — no solo un bocado rápido.

Qué hacer:
• Complete Maariv y Havdalah (Havdalah incluye una vela encendida durante todo Yom Kippur, vino y sin especias).
• Coma un desayuno festivo tras el ayuno — muchos preparan comida con anticipación porque las restricciones de cocina en Yom Kippur terminan al anochecer.
• La tradición jabad (Baal HaTanya) enseña que el sustento (parnasa) del año está especialmente ligado a esta comida — coma con alegría e intención espiritual.

Este elemento estará disponible al anochecer después de que termine el ayuno.""",
        "fr": """Après la fin de Yom Kippour à la tombée de la nuit$tzeitLine, c'est une mitzvah de prendre un bon repas — pas seulement une bouchée rapide.

Que faire :
• Terminez Maariv et Havdalah (Havdalah comprend une bougie allumée tout au long de Yom Kippour, du vin et pas d'épices).
• Prenez un repas festif de rupture du jeûne — beaucoup préparent la nourriture à l'avance car les restrictions de cuisson de Yom Kippour se terminent à la tombée de la nuit.
• La tradition Habad (Baal HaTanya) enseigne que la subsistance (parnassa) de l'année est particulièrement liée à ce repas — mangez avec joie et intention spirituelle.

Cet élément devient disponible à la tombée de la nuit après la fin du jeûne.""",
        "ru": """После окончания Йом Кипура после выхода звёзд (цейт)$tzeitLine, мицва — съесть полноценную трапезу, а не только быстрый перекус.

Что делать:
• Завершите Маарив и Хавдалу (Хавдала включает свечу, горевшую весь Йом Кипур, вино и без благовоний).
• Съешьте праздничную трапезу после поста — многие готовят еду заранее, потому что запрет на готовку в Йом Кипур заканчивается после выхода звёзд (цейт).
• Традиция Хабада (Бааль а-Танья) учит, что пропитание (парнаса) на год особенно связано с этой трапезой — ешьте с радостью и духовным намерением.

Этот пункт станет доступен после выхода звёзд (цейт) по окончании поста.""",
    },
    PREP_TITLE: {
        "he": "הכנה לצום מחר — $fastName",
        "es": "Prepararse para el ayuno de mañana — $fastName",
        "fr": "Se préparer pour le jeûne de demain — $fastName",
        "ru": "Подготовиться к завтрашнему посту — $fastName",
    },
    FAST_DAY_TITLE: {
        "he": "צום — $name",
        "es": "Ayuno — $name",
        "fr": "Jeûne — $name",
        "ru": "Пост — $name",
    },
    ALOT_LINE_FALLBACK: {
        "he": " — הפעילו מיקום בהגדרות לזמן השחר",
        "es": " — active la ubicación en Ajustes para su hora del amanecer",
        "fr": " — activez la localisation dans Réglages pour l'heure de l'aube",
        "ru": " — включите геолокацию в Настройках для времени рассвета",
    },
    FRIDAY_NOTE_10_TEVET: {
        "he": """

עשרה בטבת ביום שישי: זהו הצום הציבורי היחיד שאינו נדחה לעולם. אם חל ביום שישי, עדיין צמים עד צאת הכוכבים, ואז שוברים את הצום בקידוש שבת וארוחת ליל שישי (שולחן ערוך או\"ח רמ\"ט:ד; משנה ברורה).""",
        "es": """

10 Tevet en viernes: es el único ayuno público que nunca se pospone. Si cae en viernes, aún se ayuna hasta el anochecer, luego se rompe el ayuno con Kidush de Shabat y la cena del viernes por la noche (Shuljan Aruj O.J. 249:4; Mishná Berurá).""",
        "fr": """

10 Tevet un vendredi : c'est le seul jeûne public jamais reporté. S'il tombe un vendredi, on jeûne quand même jusqu'à la tombée de la nuit, puis on rompt le jeûne avec le Kiddouch de Chabbat et le repas du vendredi soir (Choulhan Aroukh O.H. 249:4 ; Mishna Beroura).""",
        "ru": """

10 Тевета в пятницу: это единственный общественный пост, который никогда не переносят. Если он выпадает на пятницу, всё равно постятся до выхода звёзд (цейт), затем прерывают пост Кидушем Шаббата и пятничной трапезой (Шулхан Арух О.Х. 249:4; Мишна Берура).""",
    },
    FRIDAY_NOTE_ESTHER: {
        "he": """

תענית אסתר ביום שישי: קהילות רבות עדיין צמות עד זמן קצר לפני השבת; שברו בזמן להכנות לשבת — שאלו את הרב למנהג המקומי.""",
        "es": """

Ayuno de Ester en viernes: muchas comunidades aún ayunan hasta poco antes de Shabat; rompan a tiempo para las preparaciones de Shabat — consulte a su rav la práctica local.""",
        "fr": """

Jeûne d'Esther un vendredi : de nombreuses communautés jeûnent encore jusqu'à peu avant Chabbat ; rompez à temps pour les préparatifs de Chabbat — consultez votre rav pour la pratique locale.""",
        "ru": """

Пост Эстер в пятницу: многие общины всё ещё постятся почти до Шаббата; прервите пост вовремя для подготовки к Шаббату — уточните местный обычай у раввина.""",
    },
    SHABBAT_TISHA_NOTE: {
        "he": """

שבת ערב ט' באב (כשט' באב בשבת והצום נדחה ליום ראשון):
• שבת נחגגת במלואה עד השקיעה — אין הגבלות אבל בשבת עצמה.
• לאחר סיום השבת (הבדלה), החליפו לנעליים שאינן מעור והתחילו את הצום ומנהגי האבל.
• סעודה המפסקת אינה נאכלת בשבת — נצפת אחרי סיום השבת, לפני תחילת הצום.""",
        "es": """

Shabat Erev Tisha B'Av (cuando 9 Av cae en Shabat y el ayuno se traslada al domingo):
• Shabat se celebra plenamente hasta el atardecer — sin restricciones de duelo en Shabat mismo.
• Después de que termine Shabat (Havdalah), cámbiese a zapatos sin cuero y comience el ayuno y las prácticas de duelo.
• La seudah hamafseket no se come en Shabat — se observa después de Shabat, antes de que comience el ayuno.""",
        "fr": """

Chabbat Erev Tisha B'Av (quand le 9 Av est Chabbat et le jeûne est reporté au dimanche) :
• Chabbat est célébré pleinement jusqu'au coucher du soleil — pas de restrictions de deuil pendant Chabbat lui-même.
• Après la fin de Chabbat (Havdalah), passez à des chaussures non en cuir et commencez le jeûne et les pratiques de deuil.
• La seudah hamafseket n'est pas prise pendant Chabbat — elle est observée après Chabbat, avant le début du jeûne.""",
        "ru": """

Шаббат Эрев Тиша б'Ав (когда 9 Ав — Шаббат и пост переносят на воскресенье):
• Шаббат отмечают полностью до заката — без траурных ограничений в сам Шаббат.
• После окончания Шаббата (Хавдала) переобуйтесь в немеховую обувь и начните пост и траурные практики.
• Сеуда амафсекет не едят в Шаббат — её соблюдают после Шаббата, до начала поста.""",
    },
    APPROX_TIME_SUFFIX: {
        "he": " ב-$time",
        "es": " (aprox. $time)",
        "fr": " (env. $time)",
        "ru": " (примерно $time)",
    },
    ALOT_LINE_WITH_TIME: {
        "he": " (בערך $time — הפעילו מיקום לזמן המדויק)",
        "es": " (aprox. $time — active la ubicación para su zman exacto)",
        "fr": " (env. $time — activez la localisation pour votre zman exact)",
        "ru": " (примерно $time — включите геолокацию для точного змана)",
    },
    TZET_TOMORROW_TIME: {
        "he": "בערך $time מחר בלילה",
        "es": "aprox. $time mañana por la noche",
        "fr": "env. $time demain soir",
        "ru": "примерно $time завтра вечером",
    },
    TZET_TOMORROW_FALLBACK: {
        "he": "צאת הכוכבים מחר בלילה",
        "es": "anochecer mañana por la noche",
        "fr": "tombée de la nuit demain soir",
        "ru": "выход звёзд завтра вечером",
    },
    TZET_LINE: {
        "he": " (בערך $time הלילה)",
        "es": " (aprox. $time esta noche)",
        "fr": " (env. $time ce soir)",
        "ru": " (примерно $time сегодня вечером)",
    },
    FAST_DAY_SUBTITLE_TEMPLATE: {
        "he": "{name} · {timing}{endSuffix}",
        "es": "{name} · {timing}{endSuffix}",
        "fr": "{name} · {timing}{endSuffix}",
        "ru": "{name} · {timing}{endSuffix}",
    },
    FAST_TIMING_SUNSET: {
        "he": "משקיעה עד צאת הכוכבים למחרת",
        "es": "desde el atardecer hasta el anochecer de la noche siguiente",
        "fr": "du coucher du soleil à la tombée de la nuit le lendemain",
        "ru": "с заката до выхода звёзд (цейт) на следующий день",
    },
    FAST_TIMING_DAWN: {
        "he": "משחר (עלות השחר) עד צאת הכוכבים (צאת)",
        "es": "desde el amanecer (alot hashachar) hasta el anochecer (tzeit)",
        "fr": "de l'aube (alot hashachar) à la tombée de la nuit (tzeit)",
        "ru": "с рассвета (алот ха-шахар) до выхода звёзд (цейт)",
    },
}


def main() -> None:
    by_lang: dict[str, dict[str, str]] = {lang: {} for lang in ("he", "es", "fr", "ru")}
    for en, langs in FIXES.items():
        for lang, tr in langs.items():
            by_lang[lang][en] = tr
    # Legacy catalog keys without trailing $common (older string-id form).
    common_suffix = "\n\n$common"
    for en, langs in list(FIXES.items()):
        if not en.endswith(common_suffix):
            continue
        legacy_en = en[: -len(common_suffix)]
        for lang, tr in langs.items():
            legacy_tr = tr[: -len(common_suffix)] if tr.endswith(common_suffix) else tr
            by_lang[lang][legacy_en] = legacy_tr
    OUT.parent.mkdir(parents=True, exist_ok=True)
    OUT.write_text(json.dumps(by_lang, ensure_ascii=False, indent=2), encoding="utf-8")
    total = sum(len(v) for v in by_lang.values())
    print(f"Wrote {OUT.name}: {total} entries ({len(FIXES)} keys × langs)")


if __name__ == "__main__":
    main()
