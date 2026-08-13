#!/usr/bin/env python3
"""Write human/purim_meshulash_explainers.json."""
from __future__ import annotations

import json
from pathlib import Path

from _purim_meshulash_template_data import (
    ADVANCE_PREP,
    EREV_PREP,
    FRIDAY_MATANOT,
    FRIDAY_MEGILLAH,
    FULL_SCHEDULE,
    MEGILLAH_BLESSINGS_COMMON,
    SUNDAY_MISHLOACH,
    SUNDAY_SEUDAH,
)
from _patch_purim_he import HE as PURIM_MESHULASH_HE

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / "data" / "translation-catalog" / "human" / "purim_meshulash_explainers.json"

FIXES: dict[str, dict[str, str]] = {
    MEGILLAH_BLESSINGS_COMMON: {
        "he": """ברכות לפני הקריאה:
• על מקרא מגילה
• שעשה ניסים
• שהחיינו (בלילה הראשון; אשכנזים אומרים גם ביום)""",
        "es": """Bendiciones antes de la lectura:
• Al mikra megillah
• She'asa nissim
• Shehecheyanu (en la primera noche; los ashkenazíes también la recitan de día)""",
        "fr": """Bénédictions avant la lecture :
• Al mikra megillah
• She'asa nissim
• Sheheheyanou (la première nuit ; les Ashkénazes la récitent aussi le jour)""",
        "ru": """Брахи перед чтением:
• Ал микра мегила
• Ше-аса нисим
• Шехехияну (в первую ночь; ашкеназы читают и днём)""",
    },
    FULL_SCHEDULE: {
        "he": """פורים מְשֻׁלָּשׁ — ירושלים כשפurim שושan (ט\"ו אדר) חal בShabbat

למה לוח השנה מתפצל:
• מ observance פurim שלכם (עיר מוקפת חoma / ירושלים) בדרך כלל בט\"ו אדר, אבל מצוות פurim אינן נעשות בשבת.
• מגילה ומתנות לאביונים עוברים ליום שישי (י\"ד אדר). משloach manot והסעודה עוברים ליום ראשון (ט\"ז אדר). שבת (ט\"ו אדר): מצוות בית אישיות (מגילה, מתנות, משloach, סעודה) אינן היום, אך לשבת יש חובות פurim קהילתיים — ראו למטה.

קראו את המדריך המלא לפני הדלקת נרות שבת — הטלפון כבוי בשבת, ולא תראו את רשימת יום ראשון עד אחרי הבדלה.

ארבע המצוות — מתי הן השנה:
1. מגילה — חמישי בלילה אחרי צאת הכוכבים (תחילת שישי / י\"ד אדר) וגם שישי ביום (לפני השקיעה). שתי קריאות, כמו פurim רגיל. אשרו זמנים עם בית הכנסת; רשמו או הדפיסו.
2. מתנות לאביונים — שישי ביום בלבד (לא חמישי בלילה, לא שבת). תנו לפחות מתנה לכל אחד משני עניים שונים (מינימום שני נמענים). כסף נפוץ. הכינו מזומן, מעטפות או אנשי קשר לצדקה לפני שישי.
3. משloach manot — יום ראשון (ט\"ז אדר) לפני השקיעה. לפחות שני מאכלים מוכנים לחבר. ארזו וסמנו חבילות לפני שבת; תכננו מי מוסר ביום ראשון.
4. סעודת פurim — יום ראשון אחר הצהריים (ט\"ז אדר), לפני השקיעה. ארוחה חגיגית עם לחם, שמחה ודברי תורה — לא בשבת.

מה לסיים לפני נרות שבת (שישי):
• מגילה: חמישי בלילה ושישי בבוקר (או דעו לוח זמנים).
• מתנות: הושלמו בשישי — כסף מוכן בבוקר שישי.
• משloach: חבילות ארוזות, מתויגות, מאוחסנות; רשימת משלוחים (יום ראשון בלבד).
• סעודה: תפריט וזמנים ליום ראשון; הזמינו אורחים אם צריך.
• מחצית השקל: לפי מנהג, רבים נותנים לפני מגילה — טפלו בחמישי/שישי.

שבת (ט\"ו אדר):
• מצוות בית אישיות אינן היום, אך לשבת יש חובות פurim קהילתיים.
• בית כנסת: קריאת תורה מיוחדת (פרשת ויאבo אmalек) והפטרה לפurim.
• תפילות וארוחות: הוסיפו על הניסים בכל תפילות שבת ובברכat המזון היום. אל תאמרו על הניסים בשישי (י\"ד אדר) או ביום ראשון (ט\"ז אדר).
• אין מלאכה להכנת פurim בשבת — הכל ליום ראשון מוכן מראש.

יום ראשון (ט\"ז אדר):
• שלחו משloach manot וחגגו סעודת פurim. האפליקציה תציג פריטים אחרי שבת.

שאלו את הרב במקרי קצה (נסיעה, מחלה, מנהג).""",
        "es": """Purim Meshulash (פורים מְשֻׁלָּשׁ) — Jerusalén cuando Purim de Shushan (15 Adar) cae en Shabat

Por qué el calendario se divide:
• Su observancia de Purim (ciudad amurallada / Jerusalén) sería normalmente 15 Adar, pero las mitzvot de Purim no se hacen en Shabat.
• Megillah y matanot la'evyonim pasan al viernes (14 Adar). Mishloach manot y la seudah pasan al domingo (16 Adar). Shabat (15 Adar): las mitzvot del hogar no ocurren hoy, pero Shabat lleva obligaciones comunitarias de Purim — véase abajo.

Lea esta guía completa antes de Shabat — su teléfono estará apagado en Shabat, así que no verá la lista del domingo hasta después de Havdalah.

Las cuatro mitzvot — cuándo ocurren este año:
1. Megillah — jueves por la noche después del anochecer (inicio del viernes / 14 Adar) Y viernes de día (antes del atardecer). Dos lecturas, como Purim normal. Confirme horarios con su shul; anótelos o imprímalos.
2. Matanot la'evyonim — solo el viernes de día (no jueves por la noche, no Shabat). Dé al menos un regalo a cada uno de dos personas pobres distintas (mínimo dos destinatarios). El dinero es común. Prepare efectivo, sobres o contactos de caridad antes del viernes.
3. Mishloach manot — domingo (16 Adar) antes del atardecer. Al menos dos alimentos listos para comer a un amigo. Prepare y etiquete paquetes antes de Shabat; planifique quién entrega el domingo.
4. Seudah de Purim — domingo por la tarde (16 Adar), antes del atardecer. Comida festiva con pan, alegría y palabras de Torá — no en Shabat.

Qué terminar antes de las velas de Shabat (viernes):
• Megillah: asista jueves por la noche y viernes por la mañana (o conozca el horario).
• Matanot: complete el viernes — tenga fondos listos el viernes por la mañana.
• Mishloach: paquetes empaquetados, etiquetados y guardados; lista de entrega (solo domingo).
• Seudah: menú y horario del domingo planificados; invite invitados si hace falta.
• Machatzit haShekel: si es su costumbre, muchos dan antes de Megillah — hágalo jueves/viernes.

Shabat (15 Adar):
• Las mitzvot del hogar no ocurren hoy, pero Shabat lleva obligaciones comunitarias de Purim.
• Sinagoga: lectura especial de Torá (Parashat Vayavo Amalek) y Haftarah de Purim.
• Oración y comidas: inserte Al HaNissim en todas las oraciones de Shabat y en Birkat Hamazon hoy. No diga Al HaNissim el viernes (14 Adar) ni el domingo (16 Adar).
• Sin melajá para preparar Purim en Shabat — todo para el domingo debe estar listo.

Domingo (16 Adar):
• Envíe mishloach manot y celebre la seudah de Purim. La app mostrará los ítems después de Shabat.

Consulte a su rav sobre casos límite (viaje, enfermedad, minhag).""",
        "fr": """Pourim Meshulash (פורים מְשֻׁלָּשׁ) — Jérusalem quand Pourim de Shushan (15 Adar) tombe un Chabbat

Pourquoi le calendrier se divise :
• Votre observance de Pourim (ville fortifiée / Jérusalem) serait normalement le 15 Adar, mais les mitzvot de Pourim ne se font pas le Chabbat.
• Meguila et matanot la'evyonim passent au vendredi (14 Adar). Mishloach manot et la seoudah passent au dimanche (16 Adar). Chabbat (15 Adar) : les mitzvot domestiques n'ont pas lieu aujourd'hui, mais Chabbat porte des obligations communautaires de Pourim — voir ci-dessous.

Lisez ce guide entier avant Chabbat — votre téléphone sera éteint le Chabbat, vous ne verrez la liste du dimanche qu'après Havdalah.

Les quatre mitzvot — quand elles ont lieu cette année :
1. Meguila — jeudi soir après la tombée de la nuit (début du vendredi / 14 Adar) ET vendredi en journée (avant le coucher du soleil). Deux lectures, comme un Pourim normal. Confirmez les horaires avec votre synagogue ; notez-les ou imprimez-les.
2. Matanot la'evyonim — vendredi en journée seulement (pas jeudi soir, pas Chabbat). Donnez au moins un cadeau à chacun de deux pauvres différents (minimum deux destinataires). L'argent est courant. Préparez espèces, enveloppes ou contacts de tsedaka avant vendredi.
3. Mishloach manot — dimanche (16 Adar) avant le coucher du soleil. Au moins deux aliments prêts à manger à un ami. Préparez et étiquetez les colis avant Chabbat ; planifiez qui livre dimanche.
4. Seoudah de Pourim — dimanche après-midi (16 Adar), avant le coucher du soleil. Repas festif avec pain, joie et paroles de Torah — pas le Chabbat.

À terminer avant les bougies de Chabbat (vendredi) :
• Meguila : jeudi soir et vendredi matin (ou connaissez l'horaire).
• Matanot : complétez vendredi — fonds prêts vendredi matin.
• Mishloach : colis emballés, étiquetés, rangés ; liste de livraison (dimanche seulement).
• Seoudah : menu et horaire du dimanche planifiés ; invitez si besoin.
• Ma'ah half shekel : selon minhag, beaucoup donnent avant Meguila — faites-le jeudi/vendredi.

Chabbat (15 Adar) :
• Les mitzvot domestiques n'ont pas lieu aujourd'hui, mais Chabbat porte les obligations communautaires de Pourim.
• Synagogue : lecture spéciale de Torah (Parashat Vayavo Amalek) et Haftarah de Pourim.
• Prières et repas : insérez Al HaNissim dans toutes les prières de Chabbat et dans Birkat HaMazon aujourd'hui. Ne dites pas Al HaNissim vendredi (14 Adar) ni dimanche (16 Adar).
• Pas de melakha pour préparer Pourim le Chabbat — tout pour dimanche doit être prêt.

Dimanche (16 Adar) :
• Envoyez mishloach manot et célébrez la seoudah de Pourim. L'app affichera les items après Chabbat.

Consultez votre rav pour les cas limites (voyage, maladie, minhag).""",
        "ru": """Пурим Мешулаш (פורים מְשֻׁלָּשׁ) — Иерусалим, когда Шушан-Пурим (15 Адар) выпадает на Шаббат

Почему календарь делится:
• Ваш Пурим (окружённый город / Иерусалим) обычно 15 Адар, но мицвот Пурима не выполняют в Шаббат.
• Мегила и матанот лаэвьоним переносят на пятницу (14 Адар). Мишлоах манот и сеуда — на воскресенье (16 Адар). Шаббат (15 Адар): домашние мицвот (Мегила, матанот, мишлоах, сеуда) сегодня не делают, но у Шаббата есть общинные обязанности Пурима — см. ниже.

Прочитайте весь гид до зажжения шаббатных свечей — телефон в Шаббат выключен, список воскресенья увидите только после Хавдалы.

Четыре мицвот — когда они в этом году:
1. Мегила — четверг вечером после цейт (начало пятницы / 14 Адар) И пятница днём (до заката). Два чтения, как в обычный Пурим. Уточните время в синагоге; запишите или распечатайте.
2. Матанот лаэвьоним — только в пятницу днём (не четверг вечером, не Шаббат). Подарите хотя бы по одному дару двум разным беднякам (минимум два получателя). Часто деньгами. Подготовьте наличные, конверты или благотворительные контакты до пятницы.
3. Мишлоах манот — воскресенье (16 Адар) до заката. Как минимум два готовых к употреблению продукта одному другу. Упакуйте и подпишите до Шаббата; спланируйте, кто доставит в воскресенье.
4. Сеуда Пурима — воскресенье днём (16 Адар), до заката. Праздничная трапеза с хлебом, радостью и словами Торы — не в Шаббат.

Что завершить до шаббатных свечей (пятница):
• Мегила: четверг вечером и пятница утром (или знайте расписание).
• Матанот: завершите в пятницу — деньги готовы утром пятницы.
• Мишлоах: упаковано, подписано, сохранено; список доставки (только воскресенье).
• Сеуда: меню и время воскресенья спланированы; пригласите гостей при необходимости.
• Махацит а-шекель: по минхагу многие дают до Мегилы — сделайте в четверг/пятницу.

Шаббат (15 Адар):
• Домашние мицвот сегодня не выполняют, но у Шаббата общинные обязанности Пурима.
• Синагога: особое чтение Торы (парашат Ваяво Амалек) и хафтара Пурима.
• Молитвы и трапезы: вставляйте «Ал ха-Ниссим» во все шаббатные молитвы и в Биркат а-Мазон сегодня. Не говорите «Ал ха-Ниссим» в пятницу (14 Адар) или воскресенье (16 Адар).
• Нет мелахи для подготовки Пурима в Шаббат — всё для воскресенья готовится заранее.

Воскресенье (16 Адар):
• Отправьте мишлоах манот и отметьте сеуду Пурима. Приложение покажет пункты после Шаббата.

Спросите рава о пограничных случаях (поездка, болезнь, минхаг).""",
    },
    ADVANCE_PREP: {
        "he": """מחר ערב פurim — והשנה פurim מְשֻׁלָּשׁ בירושלים. שבת באמצע, ולכן צריך את התוכנית המלאה עכשיו (לא רק מחר).

$scheduleBlock""",
        "es": """Mañana es erev Purim — y este año es Purim Meshulash en Jerusalén. Shabat está en medio, así que necesita el plan completo ahora (no solo mañana).

$scheduleBlock""",
        "fr": """Demain, c'est erev Pourim — et cette année c'est Pourim Meshulash à Jérusalem. Chabbat est au milieu, vous avez besoin du plan complet maintenant (pas seulement demain).

$scheduleBlock""",
        "ru": """Завтра Эрев Пурим — и в этом году Пурим Мешулаш в Иерусалиме. Шаббат посередине, поэтому нужен полный план уже сейчас (не только завтра).

$scheduleBlock""",
    },
    EREV_PREP: {
        "he": """פurim מְשֻׁלָּשׁ מתחיל הלילה בירושלים. כי שבת באמצע החג, קראו ושמרו את התוכנית עכשיו — לא תוכלו להסתמך על האפליקציה בשבת למצוות יום ראשון.

$scheduleBlock

הלילה (חמישי בלילה אחרי צאת הכוכבים): קריאת מגילה ראשונה. מחר (שישי): קריאה שנייה ומתנות לאביונים. משloach והסעודה — עד יום ראשון.""",
        "es": """Purim Meshulash comienza esta noche en Jerusalén. Como Shabat cae en medio de la festividad, lea y guarde este plan ahora — no podrá usar la app en Shabat para las mitzvot del domingo.

$scheduleBlock

Esta noche (jueves por la noche después del anochecer): primera lectura de Megillah. Mañana (viernes): segunda lectura y matanot la'evyonim. Mishloach y seudah esperan hasta el domingo.""",
        "fr": """Pourim Meshulash commence ce soir à Jérusalem. Comme Chabbat tombe au milieu de la fête, lisez et conservez ce plan maintenant — vous ne pourrez pas compter sur l'app le Chabbat pour les mitzvot du dimanche.

$scheduleBlock

Ce soir (jeudi soir après la tombée de la nuit) : première lecture de Meguila. Demain (vendredi) : deuxième lecture et matanot la'evyonim. Mishloach et seoudah attendent dimanche.""",
        "ru": """Пурим Мешулаш начинается сегодня вечером в Иерусалиме. Поскольку Шаббат посередине праздника, прочитайте и сохраните этот план сейчас — в Шаббат вы не сможете полагаться на приложение для мицвот воскресенья.

$scheduleBlock

Сегодня вечером (четверг после цейт): первое чтение Мегилы. Завтра (пятница): второе чтение и матанот лаэвьоним. Мишлоах и сеуда — до воскресенья.""",
    },
    FRIDAY_MEGILLAH: {
        "he": """פurim מְשֻׁלָּשׁ — מגילה ביום שישי (י\"ד אדר)

מתי השנה:
• חמישי בלילה אחרי צאת הכוכבים — קריאה ראשונה (אמורים לשמוע אז).
• היום (שישי) — קריאה שנייה ביום (בדרך כלל אחרי שחרית, לפני השקיעה) — חובה לכולם, לא אופציונלי.

איך (אותם דינים כמו פurim רגיל):
• שמוע כל מילה ממגילה כשרה; גברים ונשים חייבים שווה.
• עמדו לברכות; מנהגים בשם המן משתנים לפי בית הכנסת.

ברכות לפני הקריאה:
• על מקרא מגילה
• שעשה ניסים
• שהחיינו (בלילה הראשון; אשכנזים גם ביום)

תזכורת: מתנות לאביונים גם היום (שישי), לא בשבת. משloach והסעודה — יום ראשון; הכינו חבילות לפני שבת אם עדיין לא.""",
        "es": """Purim Meshulash — Megillah el viernes (14 Adar)

Cuándo este año:
• Jueves por la noche después del anochecer — primera lectura (debería haberla escuchado entonces).
• Hoy (viernes) — segunda lectura de día (usualmente después de Shacharit, antes del atardecer) — obligación universal, no opcional.

Cómo (mismas leyes que Purim regular):
• Escuche cada palabra de un rollo de megillah kasher; hombres y mujeres están igualmente obligados.
• Párese para las bendiciones; las costumbres al nombre de Hamán varían según la shul.

Bendiciones antes de la lectura:
• Al mikra megillah
• She'asa nissim
• Shehecheyanu (primera noche; ashkenazíes también de día)

Recordatorio: matanot la'evyonim también hoy (viernes), no en Shabat. Mishloach manot y seudah el domingo — prepare paquetes antes de Shabat si aún no lo hizo.""",
        "fr": """Pourim Meshulash — Meguila vendredi (14 Adar)

Quand cette année :
• Jeudi soir après la tombée de la nuit — première lecture (vous auriez dû l'entendre alors).
• Aujourd'hui (vendredi) — deuxième lecture en journée (souvent après Shacharit, avant le coucher du soleil) — obligation universelle, pas optionnelle.

Comment (mêmes lois que Pourim ordinaire) :
• Entendez chaque mot d'un rouleau de megillah casher ; hommes et femmes sont également obligés.
• Tenez-vous debout pour les bénédictions ; les coutumes au nom de Haman varient selon la synagogue.

Bénédictions avant la lecture :
• Al mikra megillah
• She'asa nissim
• Sheheheyanou (première nuit ; Ashkénazes aussi le jour)

Rappel : matanot la'evyonim aussi aujourd'hui (vendredi), pas le Chabbat. Mishloach manot et seoudah dimanche — préparez les colis avant Chabbat si ce n'est pas déjà fait.""",
        "ru": """Пурим Мешулаш — Мегила в пятницу (14 Адар)

Когда в этом году:
• Четверг вечером после цейт — первое чтение (вы должны были услышать тогда).
• Сегодня (пятница) — второе чтение днём (обычно после Шахарит, до заката) — всеобщая обязанность, не по желанию.

Как (те же законы, что в обычный Пурим):
• Слушайте каждое слово из кошерного свитка Мегилы; мужчины и женщины обязаны одинаково.
• Встаньте для брах; обычаи при имени Амана различаются по синагогам.

Брахи перед чтением:
• Ал микра мегила
• Ше-аса нисим
• Шехехияну (в первую ночь; ашкеназы и днём)

Напоминание: матанот лаэвьоним тоже сегодня (пятница), не в Шаббат. Мишлоах манот и сеуда — в воскресенье; упакуйте посылки до Шаббата, если ещё не сделали.""",
    },
    FRIDAY_MATANOT: {
        "he": """פurim מְשֻׁלָּשׁ — מתנות לאביונים ביום שישי בלבד (י\"ד אדר)

היום (שישי ביום) — לא בשבת:
• תנו לפחות מתנה לכל אחד משני עניים שונים (מינימום שני נמענים).
• כל מתנה צריכה לאפשר ארוחת פurim צנועה (כסף נפוץ; סכומים לפי קהילה).
• רבים נותנים אחרי קריאת המגילה ביום.

אפשר שליח או ארגון אמין שמחלק היום. אם לא מוצאים נמענים, שאלו רב או בית כנסת בבוקר שישי.

יום ראשון — משloach והסעודה; אלה כבר צריכים להיות מוכנים לפני שבת.""",
        "es": """Purim Meshulash — matanot la'evyonim solo el viernes (14 Adar)

Hoy (viernes de día) — no en Shabat:
• Dé al menos un regalo a cada uno de dos personas pobres distintas (mínimo dos destinatarios).
• Cada regalo debe permitir una comida modesta de Purim (dinero común; montos según comunidad).
• Muchos dan después de la lectura diurna de Megillah.

Puede usar un mensajero u organización de confianza que distribuya hoy. Si no encuentra destinatarios, consulte a su rabino o shul el viernes por la mañana.

El domingo es para mishloach manot y la seudah — deben estar preparados antes de Shabat.""",
        "fr": """Pourim Meshulash — matanot la'evyonim vendredi seulement (14 Adar)

Aujourd'hui (vendredi en journée) — pas le Chabbat :
• Donnez au moins un cadeau à chacun de deux pauvres différents (minimum deux destinataires).
• Chaque cadeau doit permettre un repas modeste de Pourim (argent courant ; montants selon communauté).
• Beaucoup donnent après la lecture diurne de Meguila.

Vous pouvez utiliser un messager ou une organisation fiable qui distribue aujourd'hui. Si vous ne trouvez pas de destinataires, consultez votre rabbin ou synagogue vendredi matin.

Dimanche : mishloach manot et seoudah — doivent déjà être préparés avant Chabbat.""",
        "ru": """Пурим Мешулаш — матанот лаэвьоним только в пятницу (14 Адар)

Сегодня (пятница днём) — не в Шаббат:
• Подарите хотя бы по одному дару двум разным беднякам (минимум два получателя).
• Каждый дар должен обеспечить скромную трапезу Пурима (часто деньгами; суммы по общине).
• Многие дают после дневного чтения Мегилы.

Можно через надёжного посланника или организацию, распределяющую сегодня. Если не найдёте получателей, спросите рава или синагогу в пятницу утром.

Воскресенье — для мишлоах манот и сеуды; это должно быть подготовлено до Шаббата.""",
    },
    SUNDAY_MISHLOACH: {
        "he": """פurim מְשֻׁלָּשׁ — משloach manot ביום ראשון (ט\"ז אדר)

נדחה משבת כי מצוות פurim אינן נעשות בשבת השנה.

המצווה:
• שלחו לפחות שני מאכלים או משקאות שונים מוכנים לאכילה לחבר אחד היום — חבילה אחת.
• מסור לפני השקיעה; שליח מתקבל.
• האוכל מוכן לאכילה בלי בישול; סמנו שולח ונמען.

הייתם צריכים להכין חבילות לפני שבת. אם לא — שאלו את הרב מה מותר היום.""",
        "es": """Purim Meshulash — mishloach manot el domingo (16 Adar)

Aplazado desde Shabat porque las mitzvot de Purim no se realizan en Shabat este año.

La mitzvá:
• Envíe al menos dos alimentos o bebidas distintos listos para comer a un amigo hoy — un paquete.
• Entregue antes del atardecer; un mensajero está bien.
• La comida lista sin cocinar; etiquete remitente y destinatario.

Debería haber preparado paquetes antes de Shabat. Si no, consulte a su rav qué puede hacer hoy.""",
        "fr": """Pourim Meshulash — mishloach manot dimanche (16 Adar)

Reporté depuis Chabbat car les mitzvot de Pourim ne se font pas le Chabbat cette année.

La mitzvah :
• Envoyez au moins deux aliments ou boissons différents prêts à manger à un ami aujourd'hui — un colis.
• Livrez avant le coucher du soleil ; un messager convient.
• Nourriture prête sans cuisson ; étiquetez expéditeur et destinataire.

Vous auriez dû préparer les colis avant Chabbat. Sinon, consultez votre rav pour ce que vous pouvez encore faire aujourd'hui.""",
        "ru": """Пурим Мешулаш — мишлоах манот в воскресенье (16 Адар)

Перенесено с Шаббата, потому что мицвот Пурима в этом году не выполняют в Шаббат.

Мицва:
• Отправьте как минимум два разных готовых к употреблению продукта или напитка одному другу сегодня — одна посылка.
• Доставьте до заката; можно через посланника.
• Еда готова без готовки; подпишите отправителя и получателя.

Посылки должны были быть готовы до Шаббата. Если нет — спросите рава, что ещё можно сделать сегодня.""",
    },
    SUNDAY_SEUDAH: {
        "he": """פurim מְשֻׁלָּשׁ — סעודת פurim ביום ראשון (ט\"ז אדר)

ארוחת פurim החגיגית היום (לא שישי או שבת השנה).

מתי:
• יום ראשון ביום לפני השקיעה — רבים עושים אחר הצהריים אחרי משloach manot.

איך:
• ארוחה חגיגית עם לחם, בשר, יין ושמחה; כללו דברי תורה או הודיה לה'‎.
• שתיית יין מנהג נפוץ; חגגו באחריות.

זה משלים את ארבע מצוות פurim לפurim מְשֻׁלָּשׁ בירושלים.""",
        "es": """Purim Meshulash — seudah de Purim el domingo (16 Adar)

La comida festiva de Purim es hoy (no el viernes ni Shabat este año).

Cuándo:
• Domingo de día antes del atardecer — muchos la hacen por la tarde después de mishloach manot.

Cómo:
• Comida festiva con pan, carne, vino y alegría; incluya palabras de Torá o gratitud a Hashem.
• Beber vino es costumbre extendida; celebre con responsabilidad.

Esto completa las cuatro mitzvot de Purim para Purim Meshulash en Jerusalén.""",
        "fr": """Pourim Meshulash — seoudah de Pourim dimanche (16 Adar)

Le repas festif de Pourim est aujourd'hui (pas vendredi ni Chabbat cette année).

Quand :
• Dimanche en journée avant le coucher du soleil — beaucoup le font l'après-midi après mishloach manot.

Comment :
• Repas festif avec pain, viande, vin et joie ; incluez paroles de Torah ou gratitude envers Hachem.
• Boire du vin est une coutume répandue ; célébrez avec responsabilité.

Cela complète les quatre mitzvot de Pourim pour Pourim Meshulash à Jérusalem.""",
        "ru": """Пурим Мешулаш — сеуда Пурима в воскресенье (16 Адар)

Праздничная трапеза Пурима сегодня (не в пятницу и не в Шаббат в этом году).

Когда:
• Воскресенье днём до заката — многие устраивают после полудня, после мишлоах манот.

Как:
• Праздничная трапеза с хлебом, мясом, вином и радостью; включите слова Торы или благодарность Всевышнему.
• Вино — распространённый минхаг; празднуйте ответственно.

Это завершает четыре мицвот Пурима для Пурим Мешулаш в Иерусалиме.""",
    },
}


def main() -> None:
    by_lang: dict[str, dict[str, str]] = {lang: {} for lang in ("he", "es", "fr", "ru")}
    for en, langs in FIXES.items():
        for lang, tr in langs.items():
            if lang == "he":
                continue
            by_lang[lang][en] = tr
    by_lang["he"] = dict(PURIM_MESHULASH_HE)
    OUT.parent.mkdir(parents=True, exist_ok=True)
    OUT.write_text(json.dumps(by_lang, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"Wrote {OUT.name}: {sum(len(v) for v in by_lang.values())} entries ({len(FIXES)} keys)")


if __name__ == "__main__":
    main()
