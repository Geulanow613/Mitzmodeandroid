"""Human-quality fixes batch 21 — ES kashrut, 100 brachot, tevilat keilim, Torah study."""

from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CATALOG = json.loads((ROOT / "data" / "translation-catalog" / "strings.json").read_text(encoding="utf-8"))[
    "strings"
]

KASHRUT_ES = (
    "Kashrut es el sistema de leyes dietéticas judías que rigen lo que comemos, cómo se prepara la comida "
    "y cómo se mantienen separados la carne y los lácteos.\n\n"
    "Qué significa kosher:\n"
    "Kosher (כָּשֵׁר) significa literalmente «apto» o «propio». La comida que cumple todos los requisitos "
    "dietéticos de la Torá se llama kosher. La que no, se llama treif (טְרֵיף — literalmente «desgarrada», "
    "refiriéndose a animales mal sacrificados).\n\n"
    "Las reglas principales:\n\n"
    "Animales permitidos y prohibidos:\n"
    "• Los animales terrestres deben tener pezuña hendida Y rumiar: res, cordero y cabra son kosher; el cerdo no\n"
    "• Los peces deben tener aletas Y escamas: salmón, atún y carpa son kosher; camarones, langosta y bagre no\n"
    "• Pollo, pavo, pato y otras aves domésticas son kosher\n\n"
    "Shejita (sacrificio kosher):\n"
    "• Toda la carne debe ser sacrificada por un shojet — un shojet especialmente entrenado y temeroso de D-s — "
    "con un corte rápido e indoloro. Esto se llama shejita (שְׁחִיטָה)\n"
    "• Tras el sacrificio, la carne se sala para quitar la sangre (melijá), pues consumir sangre está prohibido\n\n"
    "Separar carne y lácteos (basar b'jalav):\n"
    "• Carne y lácteos nunca pueden comerse juntos, cocinarse juntos ni servirse en los mismos platos\n"
    "• Tras comer carne, se espera un tiempo antes de lácteos. Seis horas es estándar en muchas comunidades, "
    "pero las costumbres varían (algunas esperan una o tres horas) — sigue a tu rabino y comunidad\n"
    "• Se mantienen juegos separados de platos, ollas y utensilios para carne y lácteos\n\n"
    "Hechsher:\n"
    "• Un hechsher (הֶכְשֵׁר) es una marca de certificación en alimentos envasados que indica supervisión rabínica. "
    "Busca un símbolo reconocido en las etiquetas.\n\n"
    "Por qué estas leyes:\n"
    "La Torá da estas leyes como mandamientos divinos. Más allá de la razón, observarlas eleva cada comida "
    "a un acto de servicio a D-s y construye disciplina y conciencia en la vida diaria.\n\n"
    "Por dónde empezar:\n"
    "Consulta a tu rabino ortodoxo local antes de organizar una cocina kosher. Muchas personas comienzan "
    "eliminando carne y mariscos no kosher y luego avanzan hacia la observancia completa."
)

BRACHOT_100_ES = (
    "Los rabinos instituyeron la práctica de decir 100 bendiciones (brachot) cada día, manteniéndonos "
    "permanentemente conscientes de los dones de D-s.\n\n"
    "Quién está obligado:\n"
    "La práctica talmúdica se enmarcó para los hombres (Menajot 43b). Muchos poskim sostienen que las mujeres "
    "también deben aspirar a 100 brachot diarios; otros lo tratan como opcional para las mujeres — consulta a tu rav.\n\n"
    "Qué es una berajá:\n"
    "Una bracha (בְּרָכָה — bendición; plural: brachot) es una fórmula breve dicha antes o después de cumplir "
    "una mitzvá o disfrutar algo del mundo. Toda berajá comienza: «Baruj Atá Ado-nai Eloheinu Melej ha'olam…» — "
    "Bendito eres Tú, Señor nuestro D-s, Rey del universo…\n\n"
    "De dónde salen las 100 en un día de semana:\n"
    "• Bendiciones matutinas (Birjot Hashajar): ~15\n"
    "• Servicio de oración matutino (Shajarit): ~40\n"
    "• Oración de la tarde (Minjá): ~20\n"
    "• Oración vespertina (Maariv): ~18\n"
    "• Bendiciones antes y después de comer: ~10\n"
    "Total: fácilmente 100 en un día de semana normal\n\n"
    "En Shabat y Yom Tov (festividades judías):\n"
    "Los servicios de oración son más cortos y la Amidá tiene menos bendiciones, así que te quedarás corto de 100. "
    "Para completar la cuenta:\n"
    "• Come bocadillos adicionales y di brachot antes y después de cada uno\n"
    "• Huele especias o frutas fragantes y di la bendición sobre fragancias (besamim — בְּשָׂמִים)\n\n"
    "Ein Kelokeinu (costumbre ashkenazí en Shabat y Yom Tov):\n"
    "Los ashkenazíes en todo el mundo recitan Ein Kelokeinu (אֵין כֵאלֹקֵינו) al final de Shajarit en Shabat y festividades — "
    "en Israel y en la Diáspora por igual. (Entre semana, los ashkenazíes lo omiten; sefardíes y Jabad lo recitan a diario.) "
    "Los ashkenazíes lo recitan en Shabat y Yom Tov especialmente porque la Amidá entre semana tiene diecinueve bendiciones "
    "mientras la Amidá de Shabat solo tiene siete, así que te quedas aún más lejos de cien esos días. Siguiendo a Rashi (Menajot 43b), "
    "algunos rabinos sostienen que las alabanzas de este himno cuentan hacia las cien diarias cuando faltan — no como brachot "
    "completas con Shem Umaljut (nombre y reino de D-s en la fórmula usual), sino como reconocimiento intencional de D-s. "
    "El Taz (OC 642) cita esto como razón de la costumbre. Muchos cuentan veinte líneas (cinco estrofas de cuatro); "
    "otros cuentan veintiuna cuando se repite la línea inicial al final, como en muchos sidurim. "
    "Un cómputo antiguo (Shibbolei Haleket) cuenta doce, paralelo a las doce bendiciones de la Amidá entre semana "
    "omitidas en Shabat. Nota sobre Yom Kipur: a diferencia de los ayunos menores, Ein Kelokeinu se canta universalmente "
    "al concluir Musaf (o Ne'ilá) en Yom Kipur en tradiciones ashkenazí, sefardí y jasidí. "
    "La Mishná Berurá prefiere completar la cuenta con brachot reales — comida, fragancias o responder Amén a aliyot — "
    "cuando puedas, en lugar de confiar solo en estas alabanzas.\n\n"
    "Tzaddik (צדיק) — un marco que algunos rabinos recomiendan:\n"
    "Algunos maestros sugieren usar las cuatro letras de la palabra Tzaddik como lista diaria junto con las 100 brachot:\n"
    "• צ (Tzadi = 90): Responde 90 Amén cada día (al escuchar bendiciones en la oración o de otros).\n"
    "• ד (Dalet = 4): Responde a las 4 Kedushot (קְדֻשּׁוֹת — oraciones de santificación) en los servicios diarios: "
    "(1) Kedushá en Yotzer Or (la bendición antes del Shemá matutino), (2) Kedushá en la repetición de la Amidá de Shajarit, "
    "(3) Kedushá D'Sidra / Uva L'Tzion (al final de Shajarit), y (4) Kedushá en la repetición de la Amidá de Minjá.\n"
    "• י (Yud = 10): Di 10 Amén a «Yehei Shemei Rabbah» (יְהֵא שְׁמֵהּ רַבָּא) durante el Kadish a lo largo del día.\n"
    "• ק (Kuf = 100): Recita 100 brachot (bendiciones) cada día — la obligación descrita arriba."
)

TEVILAT_ES = (
    "Cuando adquieres platos, ollas o utensilios nuevos de metal o vidrio fabricados por un no judío, "
    "requieren inmersión ritual (Tevilat Keilim) antes de tocar alimentos. La cerámica esmaltada "
    "(como porcelana con esmalte) y materiales similares a menudo también requieren inmersión — "
    "generalmente sin berajá.\n\n"
    "Quién activa la obligación: el fabricante que hizo el artículo — no la tienda que lo vendió. "
    "Una olla hecha por una empresa no judía aún requiere tevilá aunque la compres en una tienda judía; "
    "una olla hecha por un judío no requiere tevilá aunque la compres en una tienda no judía. "
    "Consulta a tu rav en casos límite.\n\n"
    "Qué es:\n"
    "Tevilat keilim (טְבִילַת כֵּלִים — «inmersión de utensilios») es una mitzvá derivada de Números 31:23. "
    "La mayoría de los poskim la tratan hoy como rabínica; algunos la ven como de nivel Torá — consulta a tu rav. "
    "Así como el cuerpo humano requiere inmersión en ciertas transiciones, también nuestros utensilios "
    "al entrar al hogar judío.\n\n"
    "Qué utensilios necesitan inmersión:\n"
    "• Con berajá: utensilios de metal y vidrio usados para comida\n"
    "• Sin berajá (requerimiento rabínico): cerámica esmaltada y algunos otros materiales — consulta a tu rabino "
    "para artículos inusuales\n"
    "• Sin inmersión: plástico, artículos de madera, desechables y artículos fabricados por un judío\n\n"
    "Cómo hacerlo:\n"
    "Recita la berajá y luego sumerge completamente el utensilio. Suéltalo un momento para que el agua "
    "rodeé todo el artículo — una breve liberación asegura que nada siga tocando tu mano sobre la línea del agua. "
    "Usa un mikve kosher; si no hay mikve cerca, puedes sumergir en el océano, un lago o un río — "
    "si un cuerpo de agua en particular califica depende de varios factores, así que consulta a tu rabino. "
    "Tu mikve local o rabino puede orientarte sobre horarios y preparación.\n\n"
    "Hornos y electrodomésticos:\n"
    "En hornos y artículos similares, usualmente solo se sumergen las rejillas removibles (o partes que "
    "tocan directamente la comida) — no todo el aparato. Algunos artículos sí requieren sumergir la unidad entera; "
    "la halajá depende del tipo. Si te preocupa dañar — pantallas digitales, electrónica o equipo frágil — "
    "consulta a tu rabino; a veces puede aplicarse leniencia y no sumergir.\n\n"
    "Esta es una mitzvá situacional — márcala cuando hayas completado la inmersión de los utensilios requeridos."
)

WHY_SAGES_ES = (
    "¿Por qué los sabios nos exhortan a aprender y por qué vale la pena comenzar hoy?\n\n"
    "El estudio de la Torá no es solo una obligación. Es la forma cotidiana más profunda en que un judío "
    "se vincula con Hashem. La Mishná enseña que Talmud Torá k'neged kulam — el estudio de la Torá "
    "supera a cualquier otra mitzvá combinada (Peah 1:1). Cuando aprendes, te aferras al D-s que nos dio "
    "la vida — no escapando del mundo, sino llenándolo de significado.\n\n"
    "Qué dicen nuestros rabinos que ocurre cuando aprendes:\n\n"
    "• Hashem se acerca — Pirkei Avot enseña que cuando incluso dos personas se sientan juntas "
    "y pasan palabras de Torá entre ellas, la Shejiná (Presencia Divina) mora entre ellas (Avot 3:2–3). "
    "Solo en tu mesa de cocina con un sidur o Jumash, sigues en esa compañía.\n\n"
    "• Protección en este mundo y en el otro — El Talmud incluye el estudio de Torá entre las tres cosas "
    "que guardan la vida de una persona (junto con temor al pecado y actos de bondad; Sotah 21a). "
    "La tradición describe palabras de Torá ascendiendo y convirtiéndose en ángeles que abogan por el estudiante. "
    "Como entiendas esa imagen, los sabios son claros: la Torá rodea tu hogar, tus hijos y tus decisiones con mérito.\n\n"
    "• Dulzura, no faena — Birkat HaTorah pide a Hashem que haga la Torá «dulce en nuestra boca». "
    "La Torá se llama etz jaim, árbol de vida (Mishlei 3:18). Cuanto más vuelves a ella, "
    "más las mitzvot se sienten conectadas en lugar de dispersas.\n\n"
    "• Ayudas a sostener el mundo — El Talmud enseña que el mundo perdura por el aliento de niños "
    "que estudian Torá, y que Jerusalén se perdió en parte cuando se descuidó el estudio de Torá (Shabat 119b). "
    "Tu aprendizaje inclina la balanza hacia la bendición para el pueblo judío.\n\n"
    "No necesitas ser erudito. Unos minutos concentrados cuentan. La recompensa es real incluso antes de sentirla.\n\n"
    "Qué es el estudio de Torá:\n"
    "Talmud Torá (תַּלְמוּד תּוֹרָה — estudio de la Torá) significa involucrarse con cualquier parte "
    "de la tradición textual judía: los cinco libros de la Torá, Profetas y Escritos (Tanaj), Mishná, Talmud, "
    "obras halájicas, Kabbalá, Jasidut, Musar o cualquier libro auténtico de Torá.\n\n"
    "La obligación:\n"
    "Todo judío debe aprender al menos un poco de Torá durante el día. No hay límite superior — "
    "aprende tanto como puedas. Reserva horarios fijos (keviyut — קְבִיעוּת) cuando sea posible; "
    "incluso una cantidad pequeña hecha con constancia y propósito es enormemente valiosa.\n\n"
    "Este ítem del checklist es para estudio diurno. Hay un ítem aparte para estudio de Torá de noche.\n\n"
    "Buenos puntos de partida para principiantes:\n"
    "• Halajá diaria: [DailyHalacha.com](https://www.dailyhalacha.com/) y [Halachipedia](https://www.halachipedia.com/) "
    "para consultar leyes\n"
    "• [Chabad.org](https://www.chabad.org/) o [Sefaria.org](https://www.sefaria.org/) para textos estructurados con traducción\n"
    "• Una clase semanal de Parashá (en persona o en [TorahAnytime.com](https://www.torahanytime.com/))\n"
    "• [Daf Yomi](https://www.sefaria.org/daf-yomi) — una página de Talmud al día (completa todo el Talmud en 7,5 años)\n"
    "• El [Kitzur Shulján Aruj](https://www.sefaria.org/Kitzur_Shulchan_Arukh) — resumen conciso de halajá práctica para la vida diaria"
)

KASHRUT_KEY = next(k for k in CATALOG if k.startswith("Kashrut is the system"))
BRACHOT_100_KEY = next(k for k in CATALOG if k.startswith("The rabbis instituted a practice"))
TEVILAT_KEY = next(k for k in CATALOG if k.startswith("When you acquire new metal"))
WHY_SAGES_KEY = next(k for k in CATALOG if k.startswith("Why the sages urge us to learn"))
ROSH_CHODESH_SHORT_KEY = next(
    k for k in CATALOG if k.startswith("Rosh Chodesh begins the Hebrew month")
)
ROSH_CHODESH_COMPOSITE_KEY = next(
    k for k in CATALOG if k.startswith("Rosh Chodesh — Rosh Chodesh begins the Hebrew month")
)

KASHRUT_FR = (
    "Le Kashrout est le système de lois alimentaires juives : ce qu'on mange, comment on prépare, "
    "et comment on sépare viande et lait.\n\n"
    "Ce que signifie casher :\n"
    "Kasher (כָּשֵׁר) signifie littéralement « apte » ou « convenable ». L'aliment conforme à la Torah est casher ; "
    "sinon c'est treif (טְרֵיף — « déchiré »).\n\n"
    "Les règles principales :\n\n"
    "Animaux permis et interdits :\n"
    "• Terrestres : sabot fendu ET rumination — bœuf, agneau, chèvre ; pas de porc\n"
    "• Poisson : nageoires ET écailles — saumon, thon, carpe ; pas crevettes, homard, poisson-chat\n"
    "• Volaille domestique — casher\n\n"
    "Shehita :\n"
    "• Toute viande doit être abattue par un shohet — abatteur formé et craignant D. — "
    "d'un coup rapide et indolore (שְׁחִיטָה)\n"
    "• Ensuite salage (meli'ha) pour retirer le sang — le sang est interdit\n\n"
    "Séparer viande et lait (basar b'chalav) :\n"
    "• Jamais manger, cuisiner ou servir viande et lait ensemble ni sur la même vaisselle\n"
    "• Après la viande, attends avant le lait — six heures est courant ; certains attendent une ou trois heures — "
    "suis ton rav et ta communauté\n"
    "• Vaisselle, casseroles et ustensiles séparés pour viande et lait\n\n"
    "He'hsher :\n"
    "• Un he'hsher (הֶכְשֵׁר) est un symbole de certification sur les emballages. Cherche un sigle reconnu.\n\n"
    "Pourquoi ces lois :\n"
    "La Torah les donne comme commandements divins. Les observer élève chaque repas en service de D. "
    "et construit discipline et conscience au quotidien.\n\n"
    "Par où commencer :\n"
    "Consulte ton rav orthodoxe local avant d'organiser une cuisine casher. Beaucoup commencent "
    "par éliminer viande et fruits de mer non casher, puis avancent vers l'observance complète."
)

_RC_BODY_ES = (
    "comienza el mes hebreo — día semi-festivo con Hallel (medio), Musaf, Yaaleh V'yavo; "
    "muchas mujeres reducen cierto trabajo según minhag. "
    "Quítate el tefilín antes de Musaf — está prohibido usar tefilín durante Musaf de Rosh Jodesh. "
    "Antiguamente la luna nueva se proclamaba por testimonio; hoy el calendario es fijo. "
    "Reinicio mensual — planifica metas de Torá y caridad."
)
_RC_BODY_FR = (
    "commence le mois hébraïque — demi-fête avec Hallel (demi), Moussaf, Yaaleh V'yavo ; "
    "souvent travail réduit pour les femmes selon le minhag. "
    "Retire le téfiline avant Moussaf — interdit pendant le Moussaf de Roch 'Hodesh. "
    "Autrefois la nouvelle lune se proclamait sur témoignage ; aujourd'hui le calendrier est fixé. "
    "Remise à zéro mensuelle — planifie objectifs de Torah et charité."
)
_RC_BODY_RU = (
    "начало еврейского месяца — полупраздник с полу-Алелем, Мусафом, Яале ве-яво; "
    "многие замужние женщины воздерживаются от части работ по минхагу. "
    "Сними тфилин до Мусафа — нельзя надевать тфилин во время Мусаф Рош Ходеш. "
    "Раньше новолуние провозглашали по свидетельству; сейчас календарь фиксирован. "
    "Ежемесячный перезапуск — планируй цели Торы и тредаку."
)

KASHRUT_RU = (
    "Кашрут — система диетических законов: что едим, как готовим, как разделяем мясо и молочное.\n\n"
    "Что значит кошер:\n"
    "Кошер (כָּשֵׁר) — «подходящий», «достойный». Еда по Торе — кошер. Некошерная — трейф (טְרֵיף).\n\n"
    "Основные правила:\n\n"
    "Разрешённые и запрещённые животные:\n"
    "• Сухопутные: расщеплённые копыта и жвачка — говядина, баранина, козлятина; свинина — нет\n"
    "• Рыба: плавники и чешуя — лосось, тунец, карп; креветки, омары, сом — нет\n"
    "• Домашняя птица — кошерна\n\n"
    "Шехита:\n"
    "• Мясо только после забоя шохетом — обученным бойцом — быстрым безболезненным разрезом (שְׁחִיטָה)\n"
    "• После забоя мясо солят (мелиха) для удаления крови\n\n"
    "Басар бхалав:\n"
    "• Не есть, не готовить и не подавать мясо с молочным на одной посуде\n"
    "• После мяса ждёшь перед молочным — часто 6 часов; минхаги: 1–3 часа — следуй раву\n"
    "• Отдельные наборы посуды для мяса и молочного\n\n"
    "Хешер:\n"
    "• Хешер (הֶכְשֵׁר) — знак раввинского надзора на упаковке\n\n"
    "Зачем:\n"
    "Тора дала эти законы как заповеди. Соблюдение превращает трапезу в служение В-гу.\n\n"
    "С чего начать:\n"
    "Проконсультируйся с ортодоксальным равом. Начни с отказа от некошерного мяса и морепродуктов."
)

BRACHOT_100_FR = (
    "Les Sages ont institué la pratique de dire 100 bénédictions (birkhot) chaque jour — "
    "conscience permanente des dons de D.\n\n"
    "Qui est concerné :\n"
    "La pratique talmudique vise les hommes (Menahot 43b). Beaucoup de poskim estiment que les femmes "
    "devraient aussi viser 100 birkhot ; d'autres le traitent comme facultatif — demande à ton rav.\n\n"
    "Qu'est-ce qu'une birkha :\n"
    "Birkha (בְּרָכָה) — formule courte avant ou après une mitsva ou un plaisir du monde. "
    "Début : « Baroukh Ata Ado-nai Eloheinou Melekh ha'olam… »\n\n"
    "D'où viennent les 100 en semaine :\n"
    "• Birkhot HaShakhar : ~15\n• Shaharit : ~40\n• Min'ha : ~20\n• Maariv : ~18\n"
    "• Avant/après manger : ~10 — facilement 100\n\n"
    "Chabbat et Yom Tov :\n"
    "Services plus courts — tu manques de 100. Complète avec collations et birkhot, "
    "ou parfums (besamim). Ein Kelokeinu (Ashkénazes) : certaines lignes comptent selon le Taz — "
    "la Michna Beroura préfère de vraies birkhot quand possible.\n\n"
    "Tzaddik (צדיק) — cadre de certains maîtres :\n"
    "צ=90 Amen ; ד=4 Kedushot ; י=10 « Yehei Shemei Rabbah » ; ק=100 birkhot."
)

BRACHOT_100_RU = (
    "Мудрецы установили практику произносить 100 брахот в день — постоянная благодарность В-гу.\n\n"
    "Кому:\n"
    "Талмудическая практика для мужчин (Менахот 43б). Многие посеким считают, что женщинам тоже стоит стремиться к 100; "
    "другие — по желанию. Спроси рава.\n\n"
    "Браха (בְּרָכָה) — короткая формула до или после мицвы или наслаждения миром. "
    "Начало: «Барух Ата Адо-най Элохейну Мелех ха-олам…»\n\n"
    "Откуда 100 в будний день:\n"
    "• Биркот ха-Шахар ~15 • Шахарит ~40 • Минха ~20 • Маарив ~18 • Еда ~10\n\n"
    "В Шаббат и Йом Тов:\n"
    "Службы короче — не хватает. Добавь перекусы с брахот, бесамим (ароматы). "
    "Эйн Келокейну (ашкеназы): по Тазу часть строк может считаться — Мишна Берура предпочитает настоящие брахот.\n\n"
    "Цадик (צדיק) — рамка некоторых учителей:\n"
    "צ=90 «амен» ; ד=4 кедушот ; י=10 «Йехи шемей рабба» ; ק=100 брахот."
)

TEVILAT_FR = (
    "Quand tu acquiers de nouveaux ustensiles en métal ou verre fabriqués par un non-Juif, "
    "ils nécessitent une immersion rituelle (Tevilat Keilim) avant de toucher la nourriture. "
    "La céramique émaillée (porcelaine) et matériaux similaires requièrent souvent tevila — "
    "en général sans birkha.\n\n"
    "Qui déclenche l'obligation : le fabricant — pas le magasin. "
    "Casserole d'une entreprise non juive : tevila même achetée chez un Juif ; "
    "fabriquée par un Juif : pas de tevila même chez un non-Juif. Demande à ton rav aux cas limites.\n\n"
    "Tevilat keilim (טְבִילַת כֵּלִים) dérive de Bemidbar 31:23. "
    "La plupart des poskim : rabbinique aujourd'hui ; certains : niveau Torah.\n\n"
    "Ustensiles concernés :\n"
    "• Avec birkha : métal et verre pour la nourriture\n"
    "• Sans birkha : céramique émaillée et autres — demande à ton rav\n"
    "• Pas d'immersion : plastique, bois, jetables, fabriqués par un Juif\n\n"
    "Comment :\n"
    "Récite la birkha puis immerge complètement ; lâche un instant pour que l'eau entoure tout. "
    "Mikvé casher ; mer, lac ou rivière selon ton rav.\n\n"
    "Fours et gros appareils :\n"
    "Souvent seules les grilles amovibles ; parfois l'unité entière. "
    "Écrans ou électronique fragile — demande à ton rav.\n\n"
    "Mitsva situationnelle — coche quand l'immersion requise est faite."
)

TEVILAT_RU = (
    "Новая металлическая или стеклянная посуда от нееврейского производителя требует тевилат келейм "
    "перед контактом с едой. Глазурованная керамика (фарфор) часто тоже — обычно без брахи.\n\n"
    "Обязанность возникает от производителя, не от магазина. "
    "Кастрюля нееврейской фирмы — тевила даже в еврейском магазине; еврейского производства — нет тевилы. "
    "Спроси рава в пограничных случаях.\n\n"
    "Тевилат келейм (טְבִילַת כֵּלִים) из Бемидбар 31:23. Большинство посеким: рабинически; некоторые: уровень Торы.\n\n"
    "Что нужно:\n"
    "• С брахой: металл и стекло для еды\n"
    "• Без брахи: глазурованная керамика — спроси рава\n"
    "• Без тевилы: пластик, дерево, одноразовое, от еврейского производителя\n\n"
    "Как:\n"
    "Браха, полное погружение; отпусти на миг, чтобы вода окружила предмет. Микве; море/озеро/река — по раву.\n\n"
    "Духовки:\n"
    "Часто только съёмные решётки. Хрупкая электроника — спроси рава.\n\n"
    "Ситуативная мицва — отметь, когда выполнил."
)

WHY_SAGES_FR = (
    "Pourquoi les Sages nous exhortent à apprendre — et pourquoi commencer aujourd'hui :\n\n"
    "L'étude de la Torah n'est pas seulement une obligation : c'est la façon quotidienne la plus profonde "
    "de se lier à Hachem. La Michna : Talmud Torah k'neged kulam (Peah 1:1). "
    "Quand tu apprends, tu t'attaches au D. qui nous a donné la vie.\n\n"
    "Ce que disent nos Sages :\n\n"
    "• Hachem se rapproche — Avot 3:2–3 : deux personnes qui échangent des paroles de Torah, "
    "la Chekhina habite entre elles — même seul à ta table avec un sidour.\n\n"
    "• Protection — Sotah 21a : l'étude parmi les trois choses qui gardent la vie.\n\n"
    "• Douceur — Birkat HaTorah demande que la Torah soit « douce dans notre bouche » (Mishlei 3:18).\n\n"
    "• Soutenir le monde — Shabat 119b : le monde subsiste grâce à l'étude.\n\n"
    "Pas besoin d'être érudit. Quelques minutes concentrées comptent.\n\n"
    "Talmud Torah : Torah écrite, Nevi'im, Ketouvim, Michna, Talmud, halakha, Hassidout, Moussar.\n\n"
    "Obligation : apprendre un peu chaque jour ; pas de plafond. Keviyut (constance) même en petit.\n\n"
    "Ce point du checklist = étude de jour ; étude de nuit = entrée séparée.\n\n"
    "Pour débuter : DailyHalacha.com, Halachipedia, Chabad.org, Sefaria, cours de Paracha, Daf Yomi, Kitzur Choulhan Aroukh."
)

WHY_SAGES_RU = (
    "Почему мудрецы призывают учиться — и почему стоит начать сегодня:\n\n"
    "Изучение Торы — глубочайшая ежедневная связь с В-гом. Мишна: Талмуд Тора кнегед кулам (Пеа 1:1).\n\n"
    "Что говорят мудрецы:\n\n"
    "• В-г ближе — Авот 3:2–3: когда двое обмениваются словами Торы, Шехина среди них — даже один за столом с сидуром.\n\n"
    "• Защита — Сота 21а: Тора среди трёх вещей, охраняющих жизнь.\n\n"
    "• Сладость — Биркат а-Тора просит, чтобы Тора была «сладкой во устах» (Мишлей 3:18).\n\n"
    "• Мир стоит на Торе — Шабат 119б.\n\n"
    "Не нужно быть учёным. Несколько сосредоточенных минут имеют значение.\n\n"
    "Талмуд Тора: письменная Тора, Невиим, Ктувим, Мишна, Талмуд, галаха, хасидут, мусар.\n\n"
    "Обязанность: хотя бы немного каждый день; верхнего предела нет. Постоянство важнее объёма.\n\n"
    "Этот пункт чеклиста — дневное учение; ночное — отдельно.\n\n"
    "С чего начать: DailyHalacha.com, Halachipedia, Chabad.org, Sefaria, урок парши, Даф Йоми, Кицур Шулхан Арух."
)

BATCH21_ES: dict[str, str] = {
    KASHRUT_KEY: KASHRUT_ES,
    BRACHOT_100_KEY: BRACHOT_100_ES,
    TEVILAT_KEY: TEVILAT_ES,
    WHY_SAGES_KEY: WHY_SAGES_ES,
    ROSH_CHODESH_SHORT_KEY: f"Rosh Jodesh {_RC_BODY_ES}",
    ROSH_CHODESH_COMPOSITE_KEY: f"Rosh Jodesh — {_RC_BODY_ES}",
}

BATCH21_FR: dict[str, str] = {
    KASHRUT_KEY: KASHRUT_FR,
    BRACHOT_100_KEY: BRACHOT_100_FR,
    TEVILAT_KEY: TEVILAT_FR,
    WHY_SAGES_KEY: WHY_SAGES_FR,
    ROSH_CHODESH_SHORT_KEY: f"Roch 'Hodesh {_RC_BODY_FR}",
    ROSH_CHODESH_COMPOSITE_KEY: f"Roch 'Hodesh — {_RC_BODY_FR}",
}

BATCH21_RU: dict[str, str] = {
    KASHRUT_KEY: KASHRUT_RU,
    BRACHOT_100_KEY: BRACHOT_100_RU,
    TEVILAT_KEY: TEVILAT_RU,
    WHY_SAGES_KEY: WHY_SAGES_RU,
    ROSH_CHODESH_SHORT_KEY: f"Рош Ходеш — {_RC_BODY_RU}",
    ROSH_CHODESH_COMPOSITE_KEY: f"Рош Ходеш — {_RC_BODY_RU}",
}
