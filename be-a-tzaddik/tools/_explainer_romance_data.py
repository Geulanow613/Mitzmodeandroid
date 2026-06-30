#!/usr/bin/env python3
"""Human-quality es/fr/ru translations for checklist explainer logical keys."""
from __future__ import annotations

from typing import Callable

LANGS: tuple[str, ...] = ("es", "fr", "ru")
_SCHEDULE = "$scheduleLeadIn$scheduleBody$scheduleYomTov"


def blocks(lang: str) -> dict[str, str]:
    """Shared fragments: halacha, forgot_sh, forgot_min, forgot_maariv, etc."""
    if lang not in LANGS:
        raise ValueError(f"Unsupported language: {lang!r}")
    return _BLOCKS[lang]()


def translations(lang: str) -> dict[str, str]:
    """All logical-key translations for one language."""
    b = blocks(lang)
    t = _compose(lang, b)
    for base in ("bedikat", "mechirat", "biur", "seder"):
        t[f"{base}_sched_block"] = t[base].replace(_SCHEDULE, "$scheduleBlock")
    t["mechirat_no_sched"] = t["mechirat_urgency"]
    return t


def _blocks_es() -> dict[str, str]:
    halacha = "Shulján Aruj O.C. 422:1; Peninei Halajá 05-01-10"
    return {
        "halacha": halacha,
        "forgot_sh": (
            "Si olvidaste:\n"
            "• Todavía en Retzei (antes de concluir la bendición) — inserta Yaaleh V'yavo en su lugar y continúa ("
            + halacha + ").\n"
            "• Después de concluir Retzei — vuelve al inicio de Retzei, inserta Yaaleh V'yavo y completa las bendiciones restantes ("
            + halacha + ").\n"
            "• Terminaste toda la Amidah (después del Yihiyu L'ratzon final) — repite solo la Amidah de Shacharit (Shemoneh Esrei), nunca el servicio completo, aunque ya hayas rezado Musaf, Maariv u otra cosa después ("
            + halacha + ")."
        ),
        "forgot_min": (
            "Si olvidaste:\n"
            "• Todavía en Retzei (antes de concluir la bendición) — inserta Yaaleh V'yavo en su lugar y continúa ("
            + halacha + ").\n"
            "• Después de concluir Retzei — vuelve al inicio de Retzei, inserta Yaaleh V'yavo y completa las bendiciones restantes ("
            + halacha + ").\n"
            "• Terminaste toda la Amidah (después del Yihiyu L'ratzon final) — repite solo la Amidah de Mincha (Shemoneh Esrei), nunca el servicio completo, aunque ya hayas rezado Musaf, Maariv u otra cosa después ("
            + halacha + ")."
        ),
        "forgot_maariv": (
            "Si olvidaste en Maariv de Rosh Chodesh:\n"
            "• Todavía en Retzei antes del Nombre de Hashem al concluir — inserta Yaaleh V'yavo ahí y continúa ("
            + halacha + ").\n"
            "• Una vez que terminaste Retzei, o después de toda la Amidah — no vuelvas atrás ni repitas. Beit Din santificó el mes nuevo de día, no de noche (Berachot 30b; "
            + halacha + "). Continúa rezando."
        ),
        "days_israel": "En Israel: lulav los 7 días; el primer día es la obligación principal de la Torá para los hombres.",
        "days_diaspora": (
            "En la Diáspora: la obligación principal de la Torá para los hombres es el primer día de Sukkot (15 Tishrei); "
            "la mitzvá continúa rabínicamente el segundo día de Yom Tov y Chol HaMoed."
        ),
        "wait_ash": (
            "Se recita una vez al mes cuando la luna es visible, por lo general desde la 3.ª noche del mes hebreo "
            "(minhag asquenazí / Jabad; Peninei Halakha 05-01-18)."
        ),
        "wait_sef": (
            "Se recita una vez al mes cuando la luna es visible, por lo general desde la 7.ª noche del mes hebreo "
            "(Shulján Aruj O.C. 426:4; Peninei Halajá 05-01-18)."
        ),
        "wait_edot": (
            "Se recita una vez al mes cuando la luna es visible. La mayoría de los sefardíes esperan hasta el 7.º del mes "
            "(Shulján Aruj O.C. 426:4). Comunidades marroquíes y otras del norte de África a veces comienzan después de 3 días "
            "(Peninei Halajá 05-01-18) — sigue tu comunidad."
        ),
        "brachot_first": "Primera noche: las tres brachot, incluida Shehecheyanu.",
        "brachot_other": "Esta noche: dos brachot (sin Shehecheyanu salvo que sea la primera vez que enciendes este año).",
        "omer_ash": "Muchos asquenazíes cuentan después de Maariv.",
        "omer_sep": "Muchos sefardíes cuentan después de Maariv.",
        "omer_edot": "Muchas kehillot de Edot HaMizrach cuentan después de Maariv.",
        "omer_chabad": "Muchos en Jabad cuentan después de Maariv (Tehillat Hashem).",
        "when_south": (
            "Cuándo (hemisferio sur — tu ubicación):\n"
            "Recítala cuando los árboles frutales florezcan en tu zona — por lo general en Elul–Tishrei en el hemisferio sur."
        ),
        "when_north": (
            "Cuándo (hemisferio norte — tu ubicación):\n"
            "Recítala en Nissan, cuando los árboles frutales de tu zona empiecen a florecer (Shulján Aruj O.C. 226:1)."
        ),
        "when_unknown": (
            "Cuándo:\n"
            "En el norte — Nissan; en el sur — Elul–Tishrei. Configura tu ciudad o GPS en Ajustes."
        ),
    }


def _blocks_fr() -> dict[str, str]:
    halacha = "Shulchan Arukh O.C. 422:1; Peninei Halakha 05-01-10"
    return {
        "halacha": halacha,
        "forgot_sh": (
            "Si vous avez oublié :\n"
            "• Encore dans Retzei (avant de conclure la bénédiction) — insérez Yaaleh V'yavo à sa place et continuez ("
            + halacha + ").\n"
            "• Après avoir conclu Retzei — revenez au début de Retzei, insérez Yaaleh V'yavo et achevez les bénédictions restantes ("
            + halacha + ").\n"
            "• Vous avez terminé toute l'Amidah (après le Yihiyu L'ratzon final) — répétez uniquement l'Amidah de Shacharit (Shemoneh Esrei), jamais le service complet, même si vous avez déjà prié Moussaf, Maariv ou autre chose ensuite ("
            + halacha + ")."
        ),
        "forgot_min": (
            "Si vous avez oublié :\n"
            "• Encore dans Retzei (avant de conclure la bénédiction) — insérez Yaaleh V'yavo à sa place et continuez ("
            + halacha + ").\n"
            "• Après avoir conclu Retzei — revenez au début de Retzei, insérez Yaaleh V'yavo et achevez les bénédictions restantes ("
            + halacha + ").\n"
            "• Vous avez terminé toute l'Amidah (après le Yihiyu L'ratzon final) — répétez uniquement l'Amidah de Mincha (Shemoneh Esrei), jamais le service complet, même si vous avez déjà prié Moussaf, Maariv ou autre chose ensuite ("
            + halacha + ")."
        ),
        "forgot_maariv": (
            "Si vous avez oublié à Maariv de Rosh 'Hodesh :\n"
            "• Encore dans Retzei avant le Nom de Dieu à la conclusion — insérez Yaaleh V'yavo là et continuez ("
            + halacha + ").\n"
            "• Une fois Retzei terminé, ou après toute l'Amidah — ne revenez pas en arrière et ne répétez pas. Le Beit Din a sanctifié le nouveau mois de jour, pas de nuit (Berachot 30b; "
            + halacha + "). Continuez la prière."
        ),
        "days_israel": "En Israël : lulav les 7 jours ; le premier jour est l'obligation principale de la Torah pour les hommes.",
        "days_diaspora": (
            "En Diaspora : l'obligation principale de la Torah pour les hommes est le premier jour de Souccot (15 Tichri) ; "
            "la mitzva continue rabbiniquement le second jour de Yom Tov et 'Hol HaMoed."
        ),
        "wait_ash": (
            "Récitée une fois par mois lorsque la lune est visible, en général à partir de la 3e nuit du mois hébraïque "
            "(coutume ashkénaze / 'Habad ; Peninei Halakha 05-01-18)."
        ),
        "wait_sef": (
            "Récitée une fois par mois lorsque la lune est visible, en général à partir de la 7e nuit du mois hébraïque "
            "(Shulchan Arukh O.C. 426:4 ; Peninei Halakha 05-01-18)."
        ),
        "wait_edot": (
            "Récitée une fois par mois lorsque la lune est visible. La majorité des Séfarades attendent le 7e du mois "
            "(Shulchan Arukh O.C. 426:4). Les communautés marocaines et d'autres d'Afrique du Nord commencent parfois après 3 jours "
            "(Peninei Halakha 05-01-18) — suivez votre communauté."
        ),
        "brachot_first": "Première nuit : les trois bérakhotes, dont Shehe'heyanu.",
        "brachot_other": "Ce soir : deux bérakhotes (pas de Shehe'heyanu sauf si c'est la première fois cette année).",
        "omer_ash": "Beaucoup d'Ashkénazes comptent après Maariv.",
        "omer_sep": "Beaucoup de Séfarades comptent après Maariv.",
        "omer_edot": "De nombreuses kehillot d'Edot HaMizra'h comptent après Maariv.",
        "omer_chabad": "Beaucoup dans 'Habad comptent après Maariv (Tehillat Hashem).",
        "when_south": (
            "Quand (hémisphère sud — votre lieu) :\n"
            "Récitez-la quand les arbres fruitiers fleurissent chez vous — en général en Eloul–Tichri dans l'hémisphère sud."
        ),
        "when_north": (
            "Quand (hémisphère nord — votre lieu) :\n"
            "Récitez-la en Nissan, quand les arbres fruitiers de votre région commencent à fleurir (Shulchan Arukh O.C. 226:1)."
        ),
        "when_unknown": (
            "Quand :\n"
            "Au nord — Nissan ; au sud — Eloul–Tichri. Réglez votre ville ou le GPS dans les paramètres."
        ),
    }


def _blocks_ru() -> dict[str, str]:
    halacha = "Шулхан Арух О.Х. 422:1; Пениней Халаха 05-01-10"
    return {
        "halacha": halacha,
        "forgot_sh": (
            "Если забыли:\n"
            "• Ещё в «Рацей» (до завершения благословения) — вставьте «Яале вьяво» на его место и продолжайте ("
            + halacha + ").\n"
            "• После завершения «Рацей» — вернитесь к началу «Рацей», вставьте «Яале вьяво» и завершите оставшиеся благословения ("
            + halacha + ").\n"
            "• Закончили всю Амиду (после финального «Йихию льрацон») — повторите только Амиду Шахарит (Шемоне Эсрей), "
            "никогда весь порядок молитвы, даже если уже молились Мусаф, Маарив или что-то ещё ("
            + halacha + ")."
        ),
        "forgot_min": (
            "Если забыли:\n"
            "• Ещё в «Рацей» (до завершения благословения) — вставьте «Яале вьяво» на его место и продолжайте ("
            + halacha + ").\n"
            "• После завершения «Рацей» — вернитесь к началу «Рацей», вставьте «Яале вьяво» и завершите оставшиеся благословения ("
            + halacha + ").\n"
            "• Закончили всю Амиду (после финального «Йихию льрацон») — повторите только Амиду Минха (Шемоне Эсрей), "
            "никогда весь порядок молитвы, даже если уже молились Мусаф, Маарив или что-то ещё ("
            + halacha + ")."
        ),
        "forgot_maariv": (
            "Если забыли на Маарив в Рош Ходеш:\n"
            "• Ещё в «Рацей» перед Именем Всевышнего в конце — вставьте «Яале вьяво» там и продолжайте ("
            + halacha + ").\n"
            "• После завершения «Рацей» или всей Амиды — не возвращайтесь и не повторяйте. Бейт-дин освятил новый месяц днём, "
            "не ночью (Брахот 30б; " + halacha + "). Продолжайте молитву."
        ),
        "days_israel": "В Израиле: лулав все 7 дней; первый день — основная обязанность Торы для мужчин.",
        "days_diaspora": (
            "В диаспоре: основная обязанность Торы для мужчин — первый день Суккота (15 Тишрея); "
            "мицва продолжается по раввинскому постановлению во второй день Йом-Това и Холь а-Моэд."
        ),
        "wait_ash": (
            "Произносится раз в месяц, когда видна луна, обычно с 3-й ночи еврейского месяца "
            "(ашкеназский / хабадский минхаг; Пениней Халаха 05-01-18)."
        ),
        "wait_sef": (
            "Произносится раз в месяц, когда видна луна, обычно с 7-й ночи еврейского месяца "
            "(Шулхан Арух О.Х. 426:4; Пениней Халаха 05-01-18)."
        ),
        "wait_edot": (
            "Произносится раз в месяц, когда видна луна. Большинство сефардов ждут до 7-го числа месяца "
            "(Шулхан Арух О.Х. 426:4). Марокканские и некоторые другие североафриканские общины начинают после 3 дней "
            "(Пениней Халаха 05-01-18) — следуйте своей общине."
        ),
        "brachot_first": "Первая ночь: все три брахи, включая Шехехияну.",
        "brachot_other": "Сегодня вечером: две брахи (без Шехехияну, если не первое зажигание в этом году).",
        "omer_ash": "Многие ашкеназы считают после Маарив.",
        "omer_sep": "Многие сефарды считают после Маарив.",
        "omer_edot": "Многие общины Эдот а-Мизрах считают после Маарив.",
        "omer_chabad": "Многие в Хабаде считают после Маарив (Техилат Хашем).",
        "when_south": (
            "Когда (южное полушарие — ваше местоположение):\n"
            "Произносите, когда плодовые деревья цветут у вас — обычно в Элул–Тишрей на юге."
        ),
        "when_north": (
            "Когда (северное полушарие — ваше местоположение):\n"
            "Произносите в Нисане, когда плодовые деревья в вашем регионе начинают цвести (Шулхан Арух О.Х. 226:1)."
        ),
        "when_unknown": (
            "Когда:\n"
            "На севере — Нисан; на юге — Элул–Тишрей. Укажите город или геолокацию в настройках."
        ),
    }


_BLOCKS: dict[str, Callable[[], dict[str, str]]] = {
    "es": _blocks_es,
    "fr": _blocks_fr,
    "ru": _blocks_ru,
}


def _compose(lang: str, b: dict[str, str]) -> dict[str, str]:
    return {"es": _compose_es, "fr": _compose_fr, "ru": _compose_ru}[lang](b)


def _compose_es(b: dict[str, str]) -> dict[str, str]:
    t: dict[str, str] = {}
    t.update({k: b[k] for k in (
        "halacha", "brachot_first", "brachot_other", "wait_ash", "wait_sef", "wait_edot",
        "when_south", "when_north", "when_unknown", "days_israel", "days_diaspora",
        "omer_ash", "omer_sep", "omer_edot", "omer_chabad",
        "forgot_sh", "forgot_min", "forgot_maariv",
    )})
    t["omer"] = (
        "Sefirat HaOmer une Pesaj con Shavuot — contar cada día desde el Éxodo hasta la entrega de la Torá.\n\n"
        "Hoy en el Omer: $todaySummary (día $day de 49).\n\n"
        "Conteo de esta noche:\n"
        "• Noche de $tonight — cuenta $todaySummary después de la caída de la noche (tzeit)$timePart.\n"
        "$nextNightLine\n\n"
        "Cómo contar:\n"
        "• Párate y recita la berajá antes de contar si aún la dices con bendición (si faltó un día, consulta a tu rav).\n"
        '• Di: "$speechPhrase"\n'
        "• Cuenta después de la caída de la noche (tzeit); termina antes del alba. Si olvidaste de noche, cuenta al día siguiente sin berajá. "
        "Si lo haces antes de la puesta del sol, puedes seguir contando con berajá las noches siguientes. Solo pierdes la berajá de forma permanente "
        "si omites un ciclo completo de 24 horas (noche y día siguiente) — consulta a tu rav.\n\n"
        "$nusachWhen"
    )
    t["chanukah"] = (
        "Noche $day de Janucá de 8 — encendido de la menorá.\n\n"
        "Cuándo:\n"
        "• Enciende después de la caída de la noche (tzeit) — no antes de la puesta del sol. El viernes, enciende las velas de Janucá antes de las de Shabat.\n"
        "• En motzei Shabat, enciende Janucá antes o después de Havdalá según tu minhag.\n\n"
        "Cómo:\n"
        "• Pirsumei nisa — coloca la menorá junto a una ventana o puerta.\n"
        "• Coloca las velas de derecha a izquierda; enciende de izquierda a derecha.\n"
        "• Berajot (primera noche tres; las demás dos).\n\n"
        "Tefilá y comidas:\n"
        "• Añade «Al HaNissim» en cada Amidá y en Birkat Hamazon todo el día.\n\n"
        "$brachotNote\n\n"
        "Después del encendido: «HaNeiros halalu» y «Maoz Tzur» (minhag)."
    )
    t["ilanot"] = (
        "Birkat Ha'Ilanot (בִּרְכַּת הָאִילָנוֹת) se recita una vez al año al ver árboles frutales en flor.\n\n"
        "$whenBlock\n\n"
        "La bracha: Baruj Atá Hashem shelo jésar be'olamo klum u'vara vo beriyot tovot ve'ilanot tovim lehanot bahem benei adam.\n\n"
        "Cómo cumplirla: sal al aire libre a árboles frutales; recítala una vez al ver las flores."
    )
    t["hachamah"] = (
        "Birkat Hachamah (בִּרְכַּת הַחַמָּה) se recita una vez cada 28 años, cuando el sol completa el machzor gadol (מחזור גדול) — el ciclo solar de 28 años del Talmud (Berachot 59b).\n\n"
        "Este evento: $dateLabel.\n\n"
        "La bracha:\n"
        "Baruj Atá Ado-nai Eloheinu Melej ha'olam oseh ma'aseh bereishit.\n"
        "(בָּרוּךְ אַתָּה ה' אֱלֹקֵינוּ מֶלֶךְ הָעוֹלָם עוֹשֵׂה מַעֲשֵׂה בְרֵאשִׁית)\n\n"
        "Cómo cumplirla:\n"
        "• Sal al aire libre donde se vea el sol — no a través de una ventana, según la mayoría de los poskim.\n"
        "• Recítala al amanecer del día del evento, cuando puedas ver el sol.\n"
        "• Tiempo ideal: desde el amanecer hasta el final de la tercera hora halájica. Según algunas opiniones, hasta chatzos.\n"
        "• Si el cielo está completamente nublado, muchos sostienen que no se recita sin luz solar visible.\n\n"
        "Esta mitzvá llega solo una vez cada 28 años. Consulta tu sinagoga local o centro Jabad."
    )
    t["kiddush"] = (
        "Kidush Levana (Birkat HaLevanah). Los hombres están obligados; las mujeres están exentas y el minhag extendido es que no la recitan (ver Deracheha).\n\n"
        "$waitLine\n\n"
        "Plazo: el período termina en la luna llena (~14,75 días desde el molad).\n\n"
        "Cuándo: después de la caída de la noche (tzeit), al aire libre; idealmente en motzei Shabat; no en Shabat ni Yom Tov.\n\n"
        "Cómo: la luna debe ser claramente visible; usa tu sidur."
    )
    t["tw_intro"] = (
        "Las Tres Semanas (Bein HaMetzarim / בין המצרים), del 17 de Tamuz al 9 de Av, conmemoran la destrucción del "
        "Templo y las tragedias del pueblo judío.\n\n"
        "Por qué guardamos luto:\n"
        "• El 17 de Tamuz se abrieron las murallas de Jerusalén; el 9 de Av se destruyeron ambos Templos, entre otras "
        "calamidades nacionales.\n\n"
        "En Shabat durante las Tres Semanas: las prácticas de luto no aplican en Shabat mismo — observa Shabat plenamente."
    )
    t["nd_shared"] = (
        "Erev Tisha B'Av (8 de Av por la tarde): deja de estudiar Torá salvo temas de duelo; come la comida final "
        "(seudah hamafseket) antes del ayuno. Se usan talit y tefilín en Shajarit de Erev Tisha B'Av — la restricción "
        "aplica el día de Tisha B'Av mismo.\n\n"
        "Tisha B'Av (9 de Av): ayuno completo de 25 horas; kinot en Shajarit sin talit ni tefilín; póntelos en Minjá "
        "después de chatzot halájico (usa tu app de zmanim). Muchos se sientan en el suelo o en un taburete bajo hasta "
        "chatzot del 9 de Av.\n\n"
        "Shabat Jazón (el Shabat antes del 9 de Av): Shabat se observa con normalidad — carne y vino están permitidos."
    )
    t["tw_ash"] = (
        "Las Tres Semanas (Bein HaMetzarim), del 17 de Tamuz al 9 de Av.\n\n"
        "Minhag asquenazí: luto prolongado durante las Tres Semanas, más intenso en los Nueve Días.\n"
        "17 Tamuz: cortes de pelo, música y bodas prohibidos; Shehecheyanu se evita (permitido en Shabat).\n"
        "Desde 1 Av: ver el ítem de los Nueve Días."
    )
    t["tw_sep"] = (
        "Las Tres Semanas — sefardíes y Edot HaMizrach: más flexibles hasta la semana del 9 de Av.\n"
        "Cortes de pelo permitidos en la mayor parte del período; música se evita; bodas según la kehilla."
    )
    t["tw_ch"] = (
        "Las Tres Semanas en Jabad — minhag asquenazí estricto.\n"
        "Cortes de pelo, música y bodas prohibidos; más Torá y tzedaká; se intensifica en los Nueve Días desde Rosh Chodesh Av."
    )
    t["nd_ash"] = (
        "Los Nueve Días (desde 1 Av) — luto asquenazí estricto: carne, vino, lavado, baño por placer, compras.\n"
        "9 Av: ayuno; kinot.\n"
        "Después del ayuno: restricciones hasta chatzos del 10 Av."
    )
    t["nd_sep"] = (
        "Los Nueve Días y la semana del 9 de Av — sefardíes: más estrictos en shavuah she'chal bo.\n"
        "Carne, vino, lavado y baño según el minhag; consulta a tu rav."
    )
    t["nd_ch"] = (
        "Los Nueve Días en Jabad — carne y vino prohibidos desde Rosh Chodesh Av; lavado y baño por placer.\n"
        "Después del ayuno: según el psak de Jabad hasta chatzos del 10 Av."
    )
    t["arba_men"] = (
        "Arba Minim (ארבעה מינים) — los cuatro minim — se toman cada día de Sucot (excepto Shabat).\n\n"
        "Los cuatro:\n"
        "• Lulav — rama de palmera de palma cerrada (al menos 3 tefajim)\n"
        "• Etrog — cidro, hermoso y mayormente íntegro (pitom intacto si está presente)\n"
        "• Hadasim — tres ramas de mirto\n"
        "• Aravot — dos ramas de sauce\n\n"
        "Cómo cumplir (todos):\n"
        "• Arma el lulav: ata lulav, hadasim y aravot (según tu portador). La espina central del lulav debe sobresalir "
        "al menos un tefaj de las puntas del mirto y el sauce (Shulján Aruj O.C. 650:1).\n"
        "• Revisa la kashrut antes de Yom Tov — especialmente el etrog y la frescura de las hojas.\n"
        "• Cuándo: de día; muchos lo hacen antes de Shajarit en casa o en la sinagoga. No en Shabat.\n"
        "• $daysNote\n\n"
        "Si falta una especie o es pasul — consulta a tu rav.\n\n"
        "Hombres — obligación de la Torá:\n"
        "• El primer día de Sucot (15 Tishrei) es el día principal de obligación. En la Diáspora, la mitzvá continúa rabínicamente "
        "el segundo día de Yom Tov y Jol HaMoed.\n"
        "• Berajá: «Al netilat lulav» cada día (excepto Shabat). Shehejiyanu solo el primer día.\n"
        "• $menWave\n\n"
        "Regla de propiedad (lakhem — u'lekachtem lakhem):\n"
        "• En Israel: el primer día debes ser dueño del juego (matana al menat lehachzir si hace falta). En la Diáspora, lakhem aplica "
        "en los días 1 y 2; desde el día 3 (Jol HaMoed) puedes pedir prestado sin regalo (Peninei Halakha, Leyes de Sucot 13:13).\n"
        "• En Israel en Jol HaMoed: pedir prestado sin regalo está permitido."
    )
    t["arba_fem"] = (
        "Arba Minim — los cuatro minim — se toman cada día de Sucot (excepto Shabat).\n\n"
        "Los cuatro:\n"
        "• Lulav, etrog, tres hadasim, dos aravot.\n\n"
        "Cómo cumplir:\n"
        "• Ata los minim; revisa la kashrut antes de Yom Tov.\n"
        "• Cuándo: de día; no en Shabat.\n"
        "• $daysNote\n\n"
        "Mujeres — mitzvá recomendada (no obligatoria):\n"
        "• Están exentas de esta mitzvá ligada al tiempo, pero muchas participan — marcada como recomendación, no como obligación diaria estricta como para los hombres el primer día.\n\n"
        "¿Necesitan su propio juego?\n"
        "• No — la mayoría usa uno familiar o de la sinagoga.\n\n"
        "Regla de propiedad (para hombres):\n"
        "• En Israel: solo el primer día; en la Diáspora: días 1–2; desde el tercero se puede pedir prestado.\n\n"
        "Bracha:\n"
        "• $brachaLine\n\n"
        "Cómo agitar:\n"
        "• $waveLine\n"
        "• Etrog en la izquierda, lulav en la derecha — o juntos según tu sidur.\n\n"
        "Cada día de la festividad (excepto Shabat) puedes cumplir de nuevo esta mitzvá recomendada."
    )
    _mechirat_body = (
        "Mechirat chametz (מְכִירַת חָמֵץ) — vender el jametz a un no judío a través de tu rav antes de Pesaj — "
        "permite guardar productos con jametz cerrados sin ser dueño de ellos durante Pesaj (Shulján Aruj O.C. 448).\n\n"
        "Por qué:\n"
        "• En Pesaj está prohibido poseer jametz (bal yera'eh / bal yimatzei). La venta transfiere la propiedad.\n\n"
        "Cómo:\n"
        "• Firma o autoriza la venta con tu rav o comunidad (formularios en línea son comunes). La venta entra en vigor la mañana de Erev Pesaj, pero debes autorizarla con anticipación — la mayoría de los rabinos dejan de aceptar formularios la noche anterior a Erev Pesaj.\n"
        "• Marca gabinetes o habitaciones incluidos en la venta; separa el jametz vendido de lo que quemarás.\n"
        "• No comas ni uses jametz vendido después de que la venta entre en vigor.\n"
        "• Vajillas: muchos incluyen el residuo y sabor de jametz absorbido en la venta y las guardan cerradas; las vajillas físicas no se venden (Shulján Aruj Y.D. 120).\n"
        "• Guarda el contrato o confirmación; muchas comunidades venden a través de un rav central.\n\n"
        "Después de Pesaj: el jametz se recompra según los términos de la venta — sigue las instrucciones de tu rav.\n"
    )
    t["mechirat"] = _mechirat_body + "$urgency\n" + _SCHEDULE
    t["mechirat_urgency"] = _mechirat_body + "$urgency"
    t["mechirat_short"] = (
        "Mechirat chametz es vender a través del rav el jametz que quieres guardar cerrado, para que no sea tuyo en Pesaj. "
        "Autoriza la venta mucho antes de Erev Pesaj — la mayoría de los rabinos dejan de aceptar formularios la noche anterior, "
        "aunque la propiedad se transfiere la mañana de Erev Pesaj. Después de Pesaj la propiedad vuelve según el contrato."
    )
    _bedikat_body = (
        "Bedikat jametz (בְּדִיקַת חָמֵץ) — la búsqueda formal de jametz — es una mitzvá rabínica la noche **antes** "
        "del día de Erev Pesaj (después de la caída de la noche (tzeit), cuando la fecha hebrea pasa a 14 Nisan).\n\n"
        "$whenLine\n\n"
        "Cómo buscar:\n"
        "• Usa una vela (o linterna según muchos poskim), una cuchara de madera y una pluma (o bolsa) para recoger migas.\n"
        "• Busca en cada habitación donde pudo haber entrado jametz — cocina, comedor, sala, auto, bolsos.\n"
        "• Debajo de muebles, cojines, asientos del auto.\n"
        "• Diez trozos de pan (minhag opcional) — anota ubicaciones y verifica que los diez se recuperen.\n\n"
        "Después de la búsqueda:\n"
        "• Recita la berajá «Al biur jametz» y la anulación «Kol jamira» (bitul) — versión de la noche.\n"
        "• La versión nocturna anula solo el jametz que no viste — aún puedes tener jametz legalmente para el desayuno de mañana.\n"
        "• Cuando Erev Pesaj cae en Shabat: primer bitul el jueves por la noche; «Kol jamira» final el Shabat por la mañana.\n"
        "• Recoge el jametz en una bolsa para destruirlo por la mañana en biur jametz.\n"
        "• Después de la caída de la noche (tzeit): no comidas ni trabajo hasta terminar la búsqueda; desde jatzot del día siguiente — "
        "evita comida pesada antes del Seder.\n\n"
        "Si estás de visita o viajando — tu anfitrión o rav indicará qué habitaciones te corresponden."
    )
    t["bedikat"] = _bedikat_body + "\n" + _SCHEDULE
    t["bedikat_no_sched"] = _bedikat_body
    t["bedikat_short"] = (
        "Bedikat jametz — búsqueda formal la noche antes de Pesaj, después de la caída de la noche (tzeit), con luz de vela "
        "(o linterna según muchos poskim). A veces se esconden trozos de pan (minhag); aunque la casa esté limpia, la halajá exige esta noche de mitzvá."
    )
    t["biur"] = (
        "Biur jametz (בִּעוּר חָמֵץ) — destruir el jametz — completa la mitzvá de quitar la levadura antes de Pesaj.\n\n"
        "$morningNote"
        "• Quema o destruye todo el jametz encontrado anoche y el jametz restante que no vendiste.\n"
        "• Muchos queman al aire libre; migas pequeñas — consulta a tu rav.\n"
        "• $zmanNote\n"
        "• Límite de tiempo: $timelineGuardrail\n"
        "• Usa la versión matutina de «Kol jamira» — anula todo el jametz en tu posesión.\n"
        "• Recita el «Kol jamira» final inmediatamente después de la destrucción, antes del plazo$kolChamiraShabbatNote\n\n"
        "Mejirat jametz:\n"
        "• El jametz en la venta — cerrado y no comido; solo se quema el no vendido.\n\n"
        "Después del biur:\n"
        "• Solo comida kosher para Pesaj hasta que termine la festividad.\n"
        "• Primogénitos: Taanit Bechorot según el calendario; Seder $sederTiming.\n"
        + _SCHEDULE
    )
    t["seder"] = (
        "$intro\n\n"
        "$sederNights\n"
        "$secondSederHachana\n"
        "$omerTrigger\n\n"
        "Preparación antes de Yom Tov:\n"
        "• Matzá — matzá shmura para hamotsi/mitzvá de matzá (tres matzot según el minhag)\n"
        "• Maror — lechuga, rábano picante u hierbas amargas según tu minhag\n"
        "• Cuatro copas de vino por participante (jugo de uva si hace falta — consulta a tu rav)\n"
        "• Hagadá para cada persona\n"
        "• Plato del Seder: zeroa, beitzah, karpas, jaroset, maror, jazeret\n"
        "• Zeroa: idealmente asar en Erev Pesaj antes de la puesta del sol (no se come en la noche del Seder)\n"
        "• Hasheva: recostarse a la izquierda con las copas, matzá, korej y afikomán — no con maror\n"
        "• Mesa festiva; velas de Yom Tov\n\n"
        "Cocina:\n"
        "• Solo kosher para Pesaj desde ahora\n"
        "• Calentar en plata Shabat o temporizador para comidas de Yom Tov\n\n"
        "En el Seder: sigue tu Hagadá — Kidush, orden de la noche, berajot y cuatro copas. $sederNightCount\n"
        + _SCHEDULE
    )
    t["on_shabbat_body"] = (
        "\n\nEste año Erev Pesaj cae en Shabat (14 Nisan). Cuando Erev Pesaj es Shabat, todo el calendario se adelanta (Peninei Halakha cap. 14):\n\n"
        "• Taanit Bechorot: jueves (12 Nisan) — no en Shabat ni viernes.\n"
        "• Bedikat jametz: jueves por la noche (noche del 13 Nisan) después de la caída de la noche (tzeit) — no la noche habitual del 14 ni en Shabat.\n"
        "• Biur jametz: viernes por la mañana (13 Nisan) — quema; sin «Kol jamira» final en la quema.\n"
        "• Mejirat jametz: completar antes de que entre Shabat.\n"
        "• Matzá el viernes (13 Nisan): muchos evitan matzá regular también ese día.\n"
        "• Preparación del Seder: toda la cocina y preparación antes de Shabat.\n"
        "• En Shabat — plazo para comer jametz: hasta el final de la 4.ª hora halájica.\n"
        "• En Shabat — eliminar restos: tirar por el inodoro o anular antes de la 5.ª hora; no quemar en Shabat.\n"
        "• En Shabat — anulación final: «Kol jamira» antes del final de la 5.ª hora.\n"
        "• Comidas de Shabat: sefardíes — matzá ashira; asquenazíes — jalot con extremo cuidado; seudah shlishit — sin matzá regular.\n"
        "• Primer Seder: motzei Shabat con Yaknehaz.\n\n"
        "Coordina con tu rav y zmanim locales."
    )
    t["fri_shabbat_body"] = (
        "\n\nEste año Erev Pesaj es viernes (14 Nisan) y el primer día de Pesaj es Shabat (15 Nisan):\n\n"
        "• Bedikat jametz: jueves por la noche (noche del 14 Nisan) — no viernes por la noche.\n"
        "• Taanit Bechorot: viernes de día.\n"
        "• Biur jametz: viernes por la mañana hasta el final de la 5.ª hora halájica.\n"
        "• Mejirat jametz: antes de encender Shabat/Yom Tov.\n"
        "• Primer Seder: viernes por la noche.\n"
        "• Segundo Seder (solo Diáspora): motzei Shabat con Yaknehaz.\n"
        "• No preparar en Shabat para el segundo Seder — solo después de la caída de la noche (tzeit).\n\n"
        "Confirma horarios con tu sidur y rav."
    )
    t["fri_13"] = (
        "Este año Erev Pesaj es Shabat (14 Nisan). Hoy (viernes, 13 Nisan): biur jametz por la mañana (sin «Kol jamira» final en la quema), "
        "y terminar mejirat jametz antes de las velas de Shabat. Taanit Bechorot fue el jueves. Bedikat fue anoche. "
        "Mañana Shabat — termina el jametz hasta la 4.ª hora, bitul hasta la 5.ª; Seder en motzei Shabat (Yaknehaz)."
    )
    t["lead_bed_thu_tomorrow"] = (
        "Lee esto antes de bedikat jametz — mañana (jueves) por la noche después de la caída de la noche (tzeit) es la búsqueda, "
        "no la noche habitual de Erev Pesaj."
    )
    t["lead_bed_thu_tonight"] = (
        "Lee esto antes de bedikat jametz — esta noche (jueves después de la caída de la noche (tzeit)) es bedikat, no mañana."
    )
    t["lead_bed_fri_tomorrow"] = (
        "Lee esto antes de bedikat jametz — mañana (jueves) por la noche después de la caída de la noche (tzeit) es bedikat, no viernes por la noche."
    )
    t["lead_bed_fri_tonight"] = (
        "Lee esto antes de bedikat jametz — esta noche (jueves después de la caída de la noche (tzeit)) es bedikat. "
        "Mañana es Erev Pesaj (viernes): Taanit Bechorot, biur y mejirat antes de Shabat."
    )
    t["lead_shabbat"] = "Hoy es Shabat — Erev Pesaj. Los pasos de abajo ya deberían estar hechos; usa esto como lista de verificación."
    t["lead_friday"] = "Hoy es Erev Pesaj (viernes). El calendario de abajo ya debería estar en marcha."
    return t


def _compose_fr(b: dict[str, str]) -> dict[str, str]:
    t: dict[str, str] = {}
    t.update({k: b[k] for k in (
        "halacha", "brachot_first", "brachot_other", "wait_ash", "wait_sef", "wait_edot",
        "when_south", "when_north", "when_unknown", "days_israel", "days_diaspora",
        "omer_ash", "omer_sep", "omer_edot", "omer_chabad",
        "forgot_sh", "forgot_min", "forgot_maariv",
    )})
    t["omer"] = (
        "Sefirat HaOmer relie Pessa'h à Chavouot — compter chaque jour depuis la Sortie d'Égypte jusqu'à la réception de la Torah.\n\n"
        "Aujourd'hui dans l'Omer : $todaySummary (jour $day sur 49).\n\n"
        "Compte de ce soir :\n"
        "• Nuit de $tonight — comptez $todaySummary après la tombée de la nuit (tzeit)$timePart.\n"
        "$nextNightLine\n\n"
        "Comment compter :\n"
        "• Tenez-vous debout et récitez la bérakhah avant de compter si vous la dites encore avec bénédiction "
        "(si vous avez manqué un jour, consultez votre rav).\n"
        '• Dites : "$speechPhrase"\n'
        "• Comptez après la tombée de la nuit (tzeit) ; terminez avant l'aube. Si vous avez oublié la nuit, comptez le lendemain "
        "de jour sans bérakhah. Si vous le faites avant le coucher du soleil, vous pouvez continuer avec bérakhah les nuits suivantes. "
        "Vous ne perdez la bérakhah définitivement que si vous manquez un cycle complet de 24 heures — consultez votre rav.\n\n"
        "$nusachWhen"
    )
    t["chanukah"] = (
        "Nuit $day de 'Hanoukka sur 8 — allumage de la menorah.\n\n"
        "Quand :\n"
        "• Allumez après la tombée de la nuit (tzeit) — pas avant le coucher du soleil. Le vendredi, allumez les bougies de 'Hanoukka avant celles de Chabbat.\n"
        "• À motzei Chabbat, allumez 'Hanoukka avant ou après Havdalah selon votre minhag.\n\n"
        "Comment :\n"
        "• Pirsoumei nissa — placez la menorah près d'une fenêtre ou d'une porte.\n"
        "• Placez les bougies de droite à gauche ; allumez de gauche à droite.\n"
        "• Bérakhotes (première nuit trois ; les autres deux).\n\n"
        "Prière et repas :\n"
        "• Ajoutez « Al HaNissim » dans chaque Amida et dans Birkat Hamazon toute la journée.\n\n"
        "$brachotNote\n\n"
        "Après l'allumage : « HaNeiros halalu » et « Maoz Tzur » (minhag)."
    )
    t["ilanot"] = (
        "Birkat Ha'Ilanot (בִּרְכַּת הָאִילָנוֹת) se récite une fois par an en voyant des arbres fruitiers en fleur.\n\n"
        "$whenBlock\n\n"
        "La bénédiction : Baroukh Atah Hashem shelo 'hissar be'olamo kloum ou'vara vo beriyot tovot ve'ilanot tovim lehanot bahem benei adam.\n\n"
        "Comment l'accomplir : sortez vers des arbres fruitiers ; récitez une fois en voyant les fleurs."
    )
    t["hachamah"] = (
        "Birkat Hachamah (בִּרְכַּת הַחַמָּה) se récite une fois tous les 28 ans, quand le soleil achève le machzor gadol (מחזור גדול) — le cycle solaire de 28 ans du Talmud (Berachot 59b).\n\n"
        "Cet événement : $dateLabel.\n\n"
        "La bénédiction :\n"
        "Baroukh Atah Ado-nai Eloheinou Melekh ha'olam oseh ma'aseh bereishit.\n"
        "(בָּרוּךְ אַתָּה ה' אֱלֹקֵינוּ מֶלֶךְ הָעוֹלָם עוֹשֵׂה מַעֲשֵׂה בְרֵאשִׁית)\n\n"
        "Comment l'accomplir :\n"
        "• Sortez où le soleil est visible — pas à travers une fenêtre, selon la plupart des poskim.\n"
        "• Récitez à l'aube du jour de l'événement, quand vous voyez le soleil.\n"
        "• Temps idéal : de l'aube à la fin de la troisième heure halakhique. Selon certaines opinions, jusqu'à 'hatzot.\n"
        "• Si le ciel est entièrement couvert, beaucoup estiment qu'on ne récite pas sans lumière solaire visible.\n\n"
        "Cette mitzva n'arrive qu'une fois tous les 28 ans. Consultez votre synagogue locale ou centre 'Habad."
    )
    t["kiddush"] = (
        "Kidouch Levana (Birkat HaLevanah). Les hommes sont obligés ; les femmes en sont exemptées et la coutume répandue est qu'elles ne la récitent pas (voir Deracheha).\n\n"
        "$waitLine\n\n"
        "Délai : la fenêtre se ferme à la pleine lune (~14,75 jours depuis le molad).\n\n"
        "Quand : après la tombée de la nuit (tzeit), dehors ; idéalement à motzei Chabbat ; pas le Chabbat ni Yom Tov.\n\n"
        "Comment : la lune doit être clairement visible ; utilisez votre siddour."
    )
    t["tw_intro"] = (
        "Les Trois Semaines (Bein HaMetzarim / בין המצרים), du 17 Tamouz au 9 Av, commémorent la destruction du Temple "
        "et les tragédies du peuple juif.\n\n"
        "Pourquoi nous pleurons :\n"
        "• Le 17 Tamouz, les murailles de Jérusalem ont été percées ; le 9 Av, les deux Temples ont été détruits, "
        "ainsi que d'autres calamités nationales.\n\n"
        "Chabbat pendant les Trois Semaines : les pratiques de deuil ne s'appliquent pas à Chabbat lui-même — "
        "observez Chabbat pleinement."
    )
    t["nd_shared"] = (
        "Erev Tisha B'Av (8 Av l'après-midi) : cessez d'étudier la Torah sauf sujets de deuil ; prenez le repas final "
        "(seudah hamafseket) avant le jeûne. Tallit et tefilin sont portés à Shaharit d'Erev Tisha B'Av — la restriction "
        "s'applique le jour de Tisha B'Av lui-même.\n\n"
        "Tisha B'Av (9 Av) : jeûne complet de 25 heures ; kinot à Shaharit sans tallit ni tefilin ; enfilez-les à Min'ha "
        "après 'hatzot halakhique (utilisez votre application de zmanim). Beaucoup s'assoient sur le sol ou un tabouret "
        "bas jusqu'à 'hatzot du 9 Av.\n\n"
        "Chabbat 'Hazon (le Chabbat avant le 9 Av) : Chabbat s'observe normalement — viande et vin sont permis."
    )
    t["tw_ash"] = (
        "Les Trois Semaines (Bein HaMetzarim), du 17 Tamouz au 9 Av.\n\n"
        "Minhag ashkénaze : deuil prolongé pendant les Trois Semaines, plus intense pendant les Neuf Jours.\n"
        "17 Tamouz : coupes de cheveux, musique et mariages interdits ; Shehe'heyanu évité (permis le Shabbat).\n"
        "À partir du 1 Av : voir l'élément des Neuf Jours."
    )
    t["tw_sep"] = (
        "Les Trois Semaines — Séfarades et Edot HaMizra'h : plus souples jusqu'à la semaine du 9 Av.\n"
        "Coupes de cheveux permises la plupart du temps ; musique évitée ; mariages selon la kehilla."
    )
    t["tw_ch"] = (
        "Les Trois Semaines dans 'Habad — minhag ashkénaze strict.\n"
        "Coupes de cheveux, musique et mariages interdits ; plus de Torah et de tsedaka ; s'intensifie aux Neuf Jours depuis Rosh 'Hodesh Av."
    )
    t["nd_ash"] = (
        "Les Neuf Jours (du 1 Av) — deuil ashkénaze strict : viande, vin, lessive, bain de plaisir, achats.\n"
        "9 Av : jeûne ; kinot.\n"
        "Après le jeûne : restrictions jusqu'à 'hatzot du 10 Av."
    )
    t["nd_sep"] = (
        "Les Neuf Jours et la semaine du 9 Av — Séfarades : plus stricts dans le shavouah she'hal bo.\n"
        "Viande, vin, lessive et bain selon le minhag ; consultez votre rav."
    )
    t["nd_ch"] = (
        "Les Neuf Jours dans 'Habad — viande et vin interdits depuis Rosh 'Hodesh Av ; lessive et bain de plaisir.\n"
        "Après le jeûne : selon le psak 'Habad jusqu'à 'hatzot du 10 Av."
    )
    t["arba_men"] = (
        "Arba Minim (ארבעה מינים) — les quatre espèces — sont prises chaque jour de Souccot (sauf Chabbat).\n\n"
        "Les quatre :\n"
        "• Loulav — branche de palmier à feuilles fermées (au moins 3 tefahim)\n"
        "• Etrog — cédrat, beau et surtout intact (pitom intact s'il est présent)\n"
        "• Hadasim — trois branches de myrte\n"
        "• Aravot — deux branches de saule\n\n"
        "Comment accomplir (tous) :\n"
        "• Assemblez le loulav : liez loulav, hadasim et aravot (selon votre porteur). La tige centrale du loulav doit dépasser "
        "d'au moins un tefah le myrte et le saule (Choulhan Aroukh O.H. 650:1).\n"
        "• Vérifiez la cachrout avant Yom Tov — surtout l'etrog et la fraîcheur des feuilles.\n"
        "• Quand : de jour ; beaucoup le font avant Shaharit à la maison ou à la synagogue. Pas le Chabbat.\n"
        "• $daysNote\n\n"
        "Si une espèce manque ou est pasoul — consultez votre rav.\n\n"
        "Hommes — obligation de la Torah :\n"
        "• Le premier jour de Souccot (15 Tichri) est le jour principal d'obligation. En Diaspora, la mitzva continue rabbiniquement "
        "le second jour de Yom Tov et 'Hol HaMoed.\n"
        "• Bérakhah : « Al netilat loulav » chaque jour (sauf Chabbat). Shehe'heyanou le premier jour seulement.\n"
        "• $menWave\n\n"
        "Règle de propriété (lakhem — u'lekachtem lakhem) :\n"
        "• En Israël : le premier jour vous devez posséder le jeu (matana al menat lehachzir si besoin). En Diaspora, lakhem s'applique "
        "aux jours 1 et 2 ; dès le jour 3 ('Hol HaMoed) vous pouvez emprunter sans cadeau (Peninei Halakha, Lois de Souccot 13:13).\n"
        "• En Israël pendant 'Hol HaMoed : emprunter sans cadeau est permis."
    )
    t["arba_fem"] = (
        "Arba Minim — les quatre espèces — sont prises chaque jour de Souccot (sauf Chabbat).\n\n"
        "Les quatre :\n"
        "• Loulav, etrog, trois hadasim, deux aravot.\n\n"
        "Comment accomplir :\n"
        "• Liez les minim ; vérifiez la cachrout avant Yom Tov.\n"
        "• Quand : de jour ; pas le Chabbat.\n"
        "• $daysNote\n\n"
        "Femmes — mitzva recommandée (non obligatoire) :\n"
        "• Elles sont exemptées de cette mitzva liée au temps, mais beaucoup participent — marquée comme recommandation, pas comme obligation quotidienne stricte comme pour les hommes le premier jour.\n\n"
        "Ont-elles besoin de leur propre jeu ?\n"
        "• Non — la plupart utilisent un jeu familial ou de synagogue.\n\n"
        "Règle de propriété (pour les hommes) :\n"
        "• En Israël : premier jour seulement ; en Diaspora : jours 1–2 ; dès le troisième on peut emprunter.\n\n"
        "Bérakhah :\n"
        "• $brachaLine\n\n"
        "Comment agiter :\n"
        "• $waveLine\n"
        "• Etrog à gauche, loulav à droite — ou ensemble selon votre siddour.\n\n"
        "Chaque jour de la fête (sauf Chabbat) vous pouvez accomplir à nouveau cette mitzva recommandée."
    )
    _mechirat_body = (
        "Mechirat chametz (מְכִירַת חָמֵץ) — vendre le 'hamets à un non-Juif par l'intermédiaire de votre rav avant Pessa'h — "
        "permet de garder des produits au 'hamets fermés sans en être propriétaire pendant Pessa'h (Choulhan Aroukh O.H. 448).\n\n"
        "Pourquoi :\n"
        "• À Pessa'h, posséder du 'hamets est interdit (bal yera'eh / bal yimatzei). La vente transfère la propriété.\n\n"
        "Comment :\n"
        "• Signez ou autorisez la vente avec votre rav ou communauté (formulaires en ligne courants). La vente prend effet le matin d'Erev Pessa'h, mais vous devez l'autoriser longtemps à l'avance — la plupart des rabbins cessent d'accepter les formulaires la veille d'Erev Pessa'h.\n"
        "• Marquez les armoires ou pièces incluses ; séparez le 'hamets vendu de ce que vous brûlerez.\n"
        "• Ne mangez ni n'utilisez le 'hamets vendu après l'entrée en vigueur de la vente.\n"
        "• Vaisselle : beaucoup incluent le résidu et le goût de 'hamets absorbés dans la vente et les ferment ; la vaisselle elle-même n'est pas vendue (Choulhan Aroukh Y.D. 120).\n"
        "• Conservez le contrat ou la confirmation ; beaucoup de communautés vendent par un rav central.\n\n"
        "Après Pessa'h : le 'hamets est racheté selon les termes de la vente — suivez les instructions de votre rav.\n"
    )
    t["mechirat"] = _mechirat_body + "$urgency\n" + _SCHEDULE
    t["mechirat_urgency"] = _mechirat_body + "$urgency"
    t["mechirat_short"] = (
        "Mechirat chametz consiste à vendre par le rav le 'hamets que vous voulez garder fermé, pour qu'il ne soit pas à vous à Pessa'h. "
        "Autorisez la vente bien avant Erev Pessa'h — la plupart des rabbins cessent d'accepter les formulaires la veille, "
        "même si la propriété se transfère le matin d'Erev Pessa'h. Après Pessa'h la propriété revient selon le contrat."
    )
    _bedikat_body = (
        "Bedikat chametz (בְּדִיקַת חָמֵץ) — la recherche formelle du 'hamets — est une mitzva rabbinique la nuit **avant** le jour d'Erev Pessa'h "
        "(après la tombée de la nuit (tzeit), quand la date hébraïque devient 14 Nisan).\n\n"
        "$whenLine\n\n"
        "Comment chercher :\n"
        "• Utilisez une bougie (ou lampe selon beaucoup de poskim), une cuillère en bois et une plume (ou sac) pour ramasser les miettes.\n"
        "• Cherchez dans chaque pièce où du 'hamets a pu entrer — cuisine, salle à manger, salon, voiture, sacs.\n"
        "• Sous les meubles, coussins, sièges de voiture.\n"
        "• Dix morceaux de pain (minhag optionnel) — notez les emplacements et vérifiez que les dix sont retrouvés.\n\n"
        "Après la recherche :\n"
        "• Récitez la bérakhah « Al bi'ur chametz » et l'annulation « Kol chamira » (bitul) — version de nuit.\n"
        "• La version de nuit annule seulement le 'hamets que vous n'avez pas vu — vous pouvez encore posséder légalement du 'hamets pour le petit-déjeuner de demain.\n"
        "• Quand Erev Pessa'h tombe un Chabbat : premier bitul jeudi soir ; « Kol chamira » final le Chabbat matin.\n"
        "• Rassemblez le 'hamets dans un sac pour le détruire le matin au biur chametz.\n"
        "• Après la tombée de la nuit (tzeit) : pas de repas ni de travail jusqu'à la fin de la recherche ; à partir de 'hatzot le lendemain — évitez un repas lourd avant le Seder.\n\n"
        "Si vous êtes en visite ou en voyage — votre hôte ou rav indiquera les pièces dont vous êtes responsable."
    )
    t["bedikat"] = _bedikat_body + "\n" + _SCHEDULE
    t["bedikat_no_sched"] = _bedikat_body
    t["bedikat_short"] = (
        "Bedikat chametz — recherche formelle la veille de Pessa'h, après la tombée de la nuit (tzeit), à la lumière d'une bougie "
        "(ou lampe selon beaucoup de poskim). On cache parfois des morceaux de pain (minhag) ; même si la maison est propre, la halakha exige cette nuit de mitzva."
    )
    t["biur"] = (
        "Biur chametz (בִּעוּר חָמֵץ) — détruire le 'hamets — achève la mitzva d'éliminer le levain avant Pessa'h.\n\n"
        "$morningNote"
        "• Brûlez ou détruisez tout le 'hamets trouvé la nuit dernière et le 'hamets restant non vendu.\n"
        "• Beaucoup brûlent dehors ; petites miettes — consultez votre rav.\n"
        "• $zmanNote\n"
        "• Limite de temps : $timelineGuardrail\n"
        "• Utilisez la version matinale de « Kol Chamira » — elle annule tout le 'hamets en votre possession.\n"
        "• Récitez le « Kol chamira » final immédiatement après la destruction, avant le délai$kolChamiraShabbatNote\n\n"
        "Mechirat chametz :\n"
        "• Le 'hamets dans la vente — fermé et non mangé ; seul le non vendu est brûlé.\n\n"
        "Après le biur :\n"
        "• Seulement de la nourriture cachère pour Pessa'h jusqu'à la fin de la fête.\n"
        "• Premiers-nés : Taanit Bechorot selon le calendrier ; Seder $sederTiming.\n"
        + _SCHEDULE
    )
    t["seder"] = (
        "$intro\n\n"
        "$sederNights\n"
        "$secondSederHachana\n"
        "$omerTrigger\n\n"
        "Préparation avant Yom Tov :\n"
        "• Matsa — matsa shmura pour motzi/mitzva de matsa (trois matsot selon le minhag)\n"
        "• Maror — laitue, raifort ou herbes amères selon votre minhag\n"
        "• Quatre coupes de vin par participant (jus de raisin si besoin — consultez votre rav)\n"
        "• Hagada pour chaque personne\n"
        "• Plat du Seder : zeroa, beitzah, karpas, 'harosset, maror, 'hazeret\n"
        "• Zeroa : idéalement rôtir le jour d'Erev Pessa'h avant le coucher du soleil (non mangé la nuit du Seder)\n"
        "• Hasebha : s'allonger à gauche pour les coupes, matsa, kore'h et afikoman — pas pour maror\n"
        "• Table festive ; bougies de Yom Tov\n\n"
        "Cuisine :\n"
        "• Seulement cachère pour Pessa'h à partir de maintenant\n"
        "• Réchauffer sur plata ou minuterie pour les repas de Yom Tov\n\n"
        "Au Seder : suivez votre Hagada — Kiddouch, ordre de la nuit, bérakhotes et quatre coupes. $sederNightCount\n"
        + _SCHEDULE
    )
    t["on_shabbat_body"] = (
        "\n\nCette année Erev Pessa'h tombe un Chabbat (14 Nisan). Quand Erev Pessa'h est Chabbat, tout le calendrier est avancé (Peninei Halakha ch. 14) :\n\n"
        "• Taanit Bechorot : jeudi (12 Nisan) — pas le Chabbat ni le vendredi.\n"
        "• Bedikat chametz : jeudi soir (nuit du 13 Nisan) après la tombée de la nuit (tzeit) — pas la nuit habituelle du 14 ni le Chabbat.\n"
        "• Biur chametz : vendredi matin (13 Nisan) — brûlage ; pas de « Kol chamira » final au brûlage.\n"
        "• Mechirat chametz : terminer avant l'entrée du Chabbat.\n"
        "• Matsa le vendredi (13 Nisan) : beaucoup évitent aussi la matsa ordinaire ce jour-là.\n"
        "• Préparation du Seder : toute la cuisine et préparation avant le Chabbat.\n"
        "• Chabbat — délai pour manger du 'hamets : jusqu'à la fin de la 4e heure halakhique.\n"
        "• Chabbat — élimination des restes : jeter aux toilettes ou annuler avant la 5e heure ; ne pas brûler le Chabbat.\n"
        "• Chabbat — annulation finale : « Kol chamira » avant la fin de la 5e heure.\n"
        "• Repas de Chabbat : Séfarades — matsa ashira ; Ashkénazes — 'hallot avec extrême prudence ; seoudah shlishit — pas de matsa ordinaire.\n"
        "• Premier Seder : motzei Chabbat avec Yaknehaz.\n\n"
        "Coordonnez avec votre rav et zmanim locaux."
    )
    t["fri_shabbat_body"] = (
        "\n\nCette année Erev Pessa'h est vendredi (14 Nisan) et le premier jour de Pessa'h est Chabbat (15 Nisan) :\n\n"
        "• Bedikat chametz : jeudi soir (nuit du 14 Nisan) — pas vendredi soir.\n"
        "• Taanit Bechorot : vendredi de jour.\n"
        "• Biur chametz : vendredi matin jusqu'à la fin de la 5e heure halakhique.\n"
        "• Mechirat chametz : avant l'allumage de Chabbat/Yom Tov.\n"
        "• Premier Seder : vendredi soir.\n"
        "• Second Seder (Diaspora seulement) : motzei Chabbat avec Yaknehaz.\n"
        "• Pas de préparation le Chabbat pour le second Seder — seulement après la tombée de la nuit (tzeit).\n\n"
        "Confirmez les horaires avec votre siddour et rav."
    )
    t["fri_13"] = (
        "Cette année Erev Pessa'h est Chabbat (14 Nisan). Aujourd'hui (vendredi, 13 Nisan) : biur chametz ce matin (sans « Kol chamira » final au brûlage), "
        "et terminer mechirat chametz avant les bougies de Chabbat. Taanit Bechorot était jeudi. Bedikat était hier soir. "
        "Demain Chabbat — finir le 'hamets jusqu'à la 4e heure, bitul jusqu'à la 5e ; Seder à motzei Chabbat (Yaknehaz)."
    )
    t["lead_shabbat"] = "Aujourd'hui c'est Shabbat — Erev Pessa'h. Les étapes ci-dessous devraient déjà être faites ; utilisez ceci comme liste de contrôle."
    t["lead_friday"] = "Aujourd'hui c'est Erev Pessa'h (vendredi). Le calendrier ci-dessous devrait déjà être en cours."
    t["lead_bed_thu_tomorrow"] = (
        "Lisez ceci avant bedikat chametz — demain (jeudi) soir après la tombée de la nuit (tzeit) c'est la recherche, "
        "pas la nuit habituelle d'Erev Pessa'h."
    )
    t["lead_bed_thu_tonight"] = (
        "Lisez ceci avant bedikat chametz — ce soir (jeudi après la tombée de la nuit (tzeit)) c'est bedikat, pas demain."
    )
    t["lead_bed_fri_tomorrow"] = (
        "Lisez ceci avant bedikat chametz — demain (jeudi) soir après la tombée de la nuit (tzeit) c'est bedikat, pas vendredi soir."
    )
    t["lead_bed_fri_tonight"] = (
        "Lisez ceci avant bedikat chametz — ce soir (jeudi après la tombée de la nuit (tzeit)) c'est bedikat. "
        "Demain c'est Erev Pessa'h (vendredi) : Taanit Bechorot, biur et mechirat avant Chabbat."
    )
    return t


def _compose_ru(b: dict[str, str]) -> dict[str, str]:
    t: dict[str, str] = {}
    t.update({k: b[k] for k in (
        "halacha", "brachot_first", "brachot_other", "wait_ash", "wait_sef", "wait_edot",
        "when_south", "when_north", "when_unknown", "days_israel", "days_diaspora",
        "omer_ash", "omer_sep", "omer_edot", "omer_chabad",
        "forgot_sh", "forgot_min", "forgot_maariv",
    )})
    t["omer"] = (
        "Сефират ха-Омер связывает Песах со Шавуот — счёт каждого дня от Исхода до принятия Торы.\n\n"
        "Сегодня в Омере: $todaySummary (день $day из 49).\n\n"
        "Счёт этой ночи:\n"
        "• Ночь $tonight — считайте $todaySummary после цейт$timePart.\n"
        "$nextNightLine\n\n"
        "Как считать:\n"
        "• Встаньте и произнесите браху перед счётом, если ещё говорите её с благословением "
        "(если пропустили день — спросите рава).\n"
        '• Скажите: «$speechPhrase»\n'
        "• Считайте после цейт; завершите до рассвета. Если забыли ночью — считайте днём без брахи. "
        "Если успели до заката, в следующие ночи можно снова с брахой. Браху теряют навсегда только "
        "при пропуске полного 24-часового цикла (ночь и следующий день) — спросите рава.\n\n"
        "$nusachWhen"
    )
    t["chanukah"] = (
        "Ночь $day Хануки из 8 — зажигание меноры.\n\n"
        "Когда:\n"
        "• Зажигайте после цейт — не до заката. В пятницу зажигайте свечи Хануки до свечей Шаббата.\n"
        "• В моцаеи Шаббата зажигайте Хануку до или после Хавдалы по минхагу.\n\n"
        "Как:\n"
        "• Пирсумей ниса — поставьте менору у окна или у двери.\n"
        "• Ставьте свечи справа налево; зажигайте слева направо.\n"
        "• Брахот (в первую ночь три; в остальные две).\n\n"
        "Молитва и трапезы:\n"
        "• Добавляйте «Ал ха-Ниссим» в каждую Амиду и в Биркат а-Мазон весь день.\n\n"
        "$brachotNote\n\n"
        "После зажигания: «Ха-Неирот халалу» и «Маоз Цур» (минхаг)."
    )
    t["ilanot"] = (
        "Биркат ха-Иланот (בִּרְכַּת הָאִילָנוֹת) произносится раз в год при виде плодовых деревьев в цвету — "
        "благодарность Всевышнему за красоту и обновление творения.\n\n"
        "$whenBlock\n\n"
        "Браха (один раз в год) — текст в сидуре на иврите.\n\n"
        "Как выполнить: выйдите к плодовым деревьям (лучше два и более); произнесите один раз при виде цветов."
    )
    t["hachamah"] = (
        "Биркат ха-Хамах (בִּרְכַּת הַחַמָּה) произносится раз в 28 лет, когда солнце завершает махзор гадоль "
        "(מחזור גדול) — 28-летний солнечный цикл Талмуда (Брахот 59б).\n\n"
        "Это событие: $dateLabel.\n\n"
        "Браха — текст в сидуре на иврите.\n\n"
        "Как выполнить:\n"
        "• Выйдите туда, где видно солнце — не через окно, по мнению большинства посеким.\n"
        "• Произнесите на рассвете в день события, когда видите солнце.\n"
        "• Идеальное время: от рассвета до конца третьей галахической часа. По некоторым мнениям — до хацот.\n"
        "• Если небо полностью затянуто, многие считают, что без видимого солнечного света браху не произносят.\n\n"
        "Эта мицва бывает раз в 28 лет. Уточните в местной синагоге или центре Хабад."
    )
    t["kiddush"] = (
        "Кидуш левана (Биркат ха-Левана). Мужчины обязаны; женщины освобождены, и распространённый минхаг — "
        "не произносить (см. Deracheha).\n\n$waitLine\n\n"
        "Срок: окно закрывается в полнолуние (~14,75 дней от молада).\n\n"
        "Когда: после цейт, на улице под открытым небом; желательно в моцаеи Шаббата; не в Шаббат и не в Йом-Тов.\n\n"
        "Как: луна должна быть хорошо видна; используйте сидур."
    )
    t["tw_intro"] = (
        "Три недели (Бейн а-Мецарим / בין המצרים) — с 17 Тамуза до 9 Ава — в память о разрушении Храма "
        "и еврейских трагедиях.\n\n"
        "Почему мы скорбим:\n"
        "• 17 Тамуза прорваны стены Иерусалима; 9 Ава разрушены оба Храма, среди прочих национальных бедствий.\n\n"
        "Шаббат в Три недели: обычаи траура не применяются в сам Шаббат — полностью соблюдайте Шаббат."
    )
    t["nd_shared"] = (
        "Эрев Тиша б'Ав (8 Ава днём): прекратите изучение Торы, кроме скорбных тем; съешьте последнюю трапезу "
        "(сеуда амафсекет) перед постом. Талит и тефилин на Шахарите в эрев Тиша б'Ав — ограничение действует "
        "в сам день Тиша б'Ав.\n\n"
        "Тиша б'Ав (9 Ава): полный 25-часовой пост; кинот на Шахарите без талита и тефилин; наденьте их на Минхе "
        "после галахического хацот (используйте приложение зманим). Многие сидят на полу или низком табурете "
        "до хацот 9 Ава.\n\n"
        "Шаббат Хазон (Шаббат перед 9 Ава): Шаббат соблюдается как обычно — мясо и вино разрешены."
    )
    t["tw_ash"] = (
        "Три недели (Бейн а-Мецарим), с 17 Тамуза до 9 Ава.\n\n"
        "Ашкеназский минхаг: продлённый траур на протяжении Трёх недель, усиливается в Девять дней.\n"
        "17 Тамуза: стрижка, музыка и свадьбы запрещены; Шехехияну избегают (разрешено в Шаббат).\n"
        "С 1 Ава: см. пункт о Девяти днях."
    )
    t["tw_sep"] = (
        "Три недели — сефарды и Эдот а-Мизрах: более мягко до недели 9 Ава.\n"
        "Стрижка разрешена большую часть периода; музыку избегают; свадьбы — по общине."
    )
    t["tw_ch"] = (
        "Три недели в Хабаде — строгий ашкеназский минхаг.\n"
        "Стрижка, музыка и свадьбы запрещены; больше Торы и цдаки; усиливается в Девять дней с Рош Ходеш Ав."
    )
    t["nd_ash"] = (
        "Девять дней (с 1 Ава) — строгий ашкеназский траур: мясо, вино, стирка, купание для удовольствия, покупки.\n"
        "9 Ава: пост; кинот.\n"
        "После поста: ограничения до хацот 10 Ава."
    )
    t["nd_sep"] = (
        "Девять дней и неделя 9 Ава — сефарды: строже в шавуа ше-халь бо.\n"
        "Мясо, вино, стирка и купание по минхагу; спросите рава."
    )
    t["nd_ch"] = (
        "Девять дней в Хабаде — мясо и вино запрещены с Рош Ходеш Ав; стирка и купание для удовольствия.\n"
        "После поста: по псаку Хабада до хацот 10 Ава."
    )
    t["arba_men"] = (
        "Арба миним (ארבעה מינים) — четыре вида — берут каждый день Суккота (кроме Шаббата).\n\n"
        "Четыре вида:\n"
        "• Лулав — ветвь пальмы с закрытой ладонью (не менее 3 тефахим)\n"
        "• Этрог — цитрон, красивый и в основном целый (питом цел, если есть)\n"
        "• Адасим — три ветви мирта\n"
        "• Аровот — две ветви ивы\n\n"
        "Как выполнить (все):\n"
        "• Соберите лулав: свяжите лулав, адасим и аровот (по вашему держателю). Центральный стебель лулава "
        "должен выступать минимум на один тефах над миртом и ивой (Шулхан Арух О.Х. 650:1).\n"
        "• Проверьте кашрут до Йом Това — особенно этрог и свежесть листьев.\n"
        "• Когда: днём; многие делают до Шахарит дома или в синагоге. Не в Шаббат.\n"
        "• $daysNote\n\n"
        "Если вида нет или он пасуль — спросите рава.\n\n"
        "Мужчины — обязанность Торы:\n"
        "• Первый день Суккота (15 Тишрея) — основной день обязанности. В диаспоре мицва продолжается "
        "по раввинскому постановлению во второй день Йом Това и Холь а-Моэд.\n"
        "• Браха: «Аль нетилат лулав» каждый день (кроме Шаббата). Шехехияну только в первый день.\n"
        "• $menWave\n\n"
        "Правило владения (лахем — у-лекахтем лахем):\n"
        "• В Израиле: в первый день нужно владеть набором (матана аль менат ле-хахзир при необходимости). "
        "В диаспоре лахем в дни 1 и 2; с 3-го дня (Холь а-Моэд) можно одолжить без подарка "
        "(Пенинеи халаха, законы Суккота 13:13).\n"
        "• В Израиле в Холь а-Моэд: одолжить без подарка разрешено."
    )
    t["arba_fem"] = (
        "Арба миним — четыре вида — берут каждый день Суккота (кроме Шаббата).\n\n"
        "Четыре вида:\n"
        "• Лулав, этрог, три адасима, две аровот.\n\n"
        "Как выполнить:\n"
        "• Свяжите миним; проверьте кашрут до Йом Това.\n"
        "• Когда: днём; не в Шаббат.\n"
        "• $daysNote\n\n"
        "Женщины — рекомендуемая мицва (не обязательная):\n"
        "• Освобождены от этой мицвы, привязанной ко времени, но многие участвуют — отмечено как рекомендация, "
        "не как строгая ежедневная обязанность, как для мужчин в первый день.\n\n"
        "Нужен ли свой набор?\n"
        "• Нет — большинство использует семейный или синагогальный.\n\n"
        "Правило владения (для мужчин):\n"
        "• В Израиле: только первый день; в диаспоре: дни 1–2; с третьего можно одолжить.\n\n"
        "Браха:\n"
        "• $brachaLine\n\n"
        "Как махать:\n"
        "• $waveLine\n"
        "• Этрог в левой, лулав в правой — или вместе по сидуру.\n\n"
        "Каждый день праздника (кроме Шаббата) можно снова выполнить эту рекомендуемую мицву."
    )
    _mechirat_body = (
        "Мехира хамеца (מְכִירַת חָמֵץ) — продажа хамеца нееврею через рава перед Песахом — "
        "позволяет хранить продукты с хамецом запертыми, не владея ими в Песах (Шулхан Арух О.Х. 448).\n\n"
        "Зачем:\n"
        "• В Песах запрещено владеть хамецом (бал йераэ / бал йиматцеи). Продажа передаёт владение.\n\n"
        "Как:\n"
        "• Подпишите или уполномочьте продажу у рава или общины (онлайн-формы распространены). "
        "Продажа вступает в силу утром Эрева Песаха, но нужно уполномочить заранее — большинство раввинов "
        "перестают принимать формы накануне Эрева Песаха.\n"
        "• Отметьте шкафы или комнаты, включённые в продажу; отделите проданный хамец от того, что сожжёте.\n"
        "• Не ешьте и не используйте проданный хамец после вступления продажи в силу.\n"
        "• Посуда: многие включают в продажу остатки и вкус хамеца в посуде и запирают её; "
        "сама посуда не продаётся (Шулхан Арух Й.Д. 120).\n"
        "• Сохраните договор или подтверждение; многие общины продают через центрального рава.\n\n"
        "После Песаха: хамец выкупается по условиям продажи — следуйте указаниям рава.\n"
    )
    t["mechirat"] = _mechirat_body + "$urgency\n" + _SCHEDULE
    t["mechirat_urgency"] = _mechirat_body + "$urgency"
    t["mechirat_short"] = (
        "Мехира хамеца — продажа через рава хамеца, который хотите оставить запертым, чтобы он не был вашим в Песах. "
        "Уполномочьте продажу задолго до Эрева Песаха — большинство раввинов перестают принимать формы накануне, "
        "хотя владение переходит утром Эрева Песаха. После Песаха владение возвращается по договору."
    )
    _bedikat_body = (
        "Бедика хамеца (בְּדִיקַת חָמֵץ) — формальный поиск хамеца — раввинская мицва в ночь **перед** "
        "днём Эрева Песаха (после цейт, когда еврейская дата становится 14 Нисана).\n\n"
        "$whenLine\n\n"
        "Как искать:\n"
        "• Свеча (или фонарь по мнению многих посеким), деревянная ложка и перо (или пакет) для сбора крошек.\n"
        "• Ищите в каждой комнате, куда мог попасть хамец — кухня, столовая, гостиная, машина, сумки.\n"
        "• Под мебелью, подушками, сиденьями машины.\n"
        "• Десять кусочков хлеба (опциональный минхаг) — запишите места и убедитесь, что все десять найдены.\n\n"
        "После поиска:\n"
        "• Произнесите браху «Аль биур хамец» и аннулирование «Коль хамира» (битуль) — ночная версия.\n"
        "• Ночная версия аннулирует только хамец, который вы не видели — завтра утром хамец ещё можно иметь на завтрак.\n"
        "• Когда Эрев Песаха в Шаббат: первый битуль в четверг вечером; финальный «Коль хамира» в Шаббат утром.\n"
        "• Соберите хамец в пакет для уничтожения утром в биур хамеца.\n"
        "• После цейт: нельзя есть и работать до окончания поиска; с хацот следующего дня — избегайте тяжёлой еды перед Седером.\n\n"
        "Если вы в гостях или в пути — хозяин или рав укажет, какие комнаты на вас."
    )
    t["bedikat"] = _bedikat_body + "\n" + _SCHEDULE
    t["bedikat_no_sched"] = _bedikat_body
    t["bedikat_short"] = (
        "Бедика хамеца — формальный поиск хамеца в ночь перед Песахом, после цейт, при свече "
        "(или фонаре по мнению многих посеким). Иногда прячут кусочки хлеба (минхаг); "
        "даже если дом чист, галаха требует эту ночную мицву."
    )
    t["biur"] = (
        "Биур хамеца (בִּעוּר חָמֵץ) — уничтожение хамеца — завершает мицву удаления квасного до Песаха.\n\n"
        "$morningNote"
        "• Сожгите или уничтожьте весь хамец, найденный прошлой ночью, и оставшийся непроданный хамец.\n"
        "• Многие жгут на улице; мелкие крошки — спросите рава.\n"
        "• $zmanNote\n"
        "• Временной предел: $timelineGuardrail\n"
        "• Используйте утреннюю версию «Коль хамира» — она аннулирует весь хамец в вашем владении.\n"
        "• Произнесите финальный «Коль хамира» сразу после уничтожения, до срока$kolChamiraShabbatNote\n\n"
        "Мехира хамеца:\n"
        "• Хамец в продаже — заперт и не едят; сжигают только непроданный.\n\n"
        "После биура:\n"
        "• Только кошерное для Песаха до конца праздника.\n"
        "• Первородные: Таанит бехорот по расписанию; Седер $sederTiming.\n"
        + _SCHEDULE
    )
    t["seder"] = (
        "$intro\n\n"
        "$sederNights\n"
        "$secondSederHachana\n"
        "$omerTrigger\n\n"
        "Подготовка до Йом Това:\n"
        "• Маца — шмура для моци/мицвы мацы (три мацы на тарелке по минхагу)\n"
        "• Марор — салат, хрен или горькие травы по минхагу\n"
        "• Четыре чаши вина на участника (виноградный сок при необходимости — спросите рава)\n"
        "• Агада для каждого\n"
        "• Тарелка Седера: цеа, бейца, карпас, харосет, марор, хазерет\n"
        "• Цеа: желательно жарить в Эрев Песаха до заката (не едят в ночь Седера)\n"
        "• Хасева: возлежать налево при чашах, маце, корех и афикомане — не при мароре\n"
        "• Праздничный стол; свечи Йом Това\n\n"
        "Кухня:\n"
        "• Только кошерное для Песаха с этого момента\n"
        "• Подогрев на плите Шаббата или по таймеру для трапез Йом Това\n\n"
        "На Седере: следуйте Агаде — Кидуш, порядок ночи, брахот и четыре чаши. $sederNightCount\n"
        + _SCHEDULE
    )
    t["on_shabbat_body"] = (
        "\n\nВ этом году Эрев Песаха в Шаббат (14 Нисана). Когда Эрев Песаха в Шаббат, весь календарь сдвигается раньше "
        "(Пенинеи халаха гл. 14):\n\n"
        "• Таанит бехорот: четверг (12 Нисана) — не в Шаббат и не в пятницу.\n"
        "• Бедика хамеца: четверг вечером (ночь 13 Нисана) после цейт — не обычная ночь 14-го и не в Шаббат.\n"
        "• Биур хамеца: пятница утром (13 Нисана) — сжигание; без финального «Коль хамира» при сжигании.\n"
        "• Мехира хамеца: завершить до входа Шаббата.\n"
        "• Маца в пятницу (13 Нисана): многие избегают обычной мацы и в этот день.\n"
        "• Подготовка Седера: вся готовка до Шаббата.\n"
        "• В Шаббат — срок есть хамец: до конца 4-й галахической часа.\n"
        "• В Шаббат — утилизация остатков: смыть или аннулировать до 5-й часа; не жечь в Шаббат.\n"
        "• В Шаббат — финальное аннулирование: «Коль хамира» до конца 5-й часа.\n"
        "• Трапезы Шаббата: сефарды — маца ашира; ашкеназы — халы с крайней осторожностью; "
        "сеуда шлишит — без обычной мацы.\n"
        "• Первый Седер: моцаеи Шаббата с Якнеаз.\n\n"
        "Согласуйте с равом и местными зманим."
    )
    t["fri_shabbat_body"] = (
        "\n\nВ этом году Эрев Песаха в пятницу (14 Нисана), а первый день Песаха в Шаббат (15 Нисана):\n\n"
        "• Бедика хамеца: четверг вечером (ночь 14 Нисана) — не пятница вечером.\n"
        "• Таанит бехорот: пятница днём.\n"
        "• Биур хамеца: пятница утром до конца 5-й галахической часа.\n"
        "• Мехира хамеца: до зажигания Шаббата/Йом Това.\n"
        "• Первый Седер: пятница вечером.\n"
        "• Второй Седер (только диаспора): моцаеи Шаббата с Якнеаз.\n"
        "• Не готовить в Шаббат ко второму Седеру — только после цейт.\n\n"
        "Подтвердите время по сидуру и с равом."
    )
    t["fri_13"] = (
        "В этом году Эрев Песаха в Шаббат (14 Нисана). Сегодня (пятница, 13 Нисана): биур хамеца утром "
        "(без финального «Коль хамира» при сжигании), и завершить мехиру хамеца до свечей Шаббата. "
        "Таанит бехорот был в четверг. Бедика была вчера вечером. "
        "Завтра Шаббат — закончите хамец до 4-й часа, битуль до 5-й; Седер в моцаеи Шаббата (Якнеаз)."
    )
    t["lead_shabbat"] = "Сегодня Шаббат — Эрев Песаха. Шаги ниже уже должны быть выполнены; используйте это как контрольный список."
    t["lead_friday"] = "Сегодня Эрев Песаха (пятница). Календарь ниже уже должен быть в действии."
    t["lead_bed_thu_tomorrow"] = (
        "Прочитайте перед бедикой хамеца — завтра (четверг) вечером после цейт поиск, а не обычная ночь Эрева Песаха."
    )
    t["lead_bed_thu_tonight"] = (
        "Прочитайте перед бедикой хамеца — сегодня вечером (четверг после цейт) бедика, не завтра."
    )
    t["lead_bed_fri_tomorrow"] = (
        "Прочитайте перед бедикой хамеца — завтра (четверг) вечером после цейт бедика, не в пятницу вечером."
    )
    t["lead_bed_fri_tonight"] = (
        "Прочитайте перед бедикой хамеца — сегодня вечером (четверг после цейт) бедика. "
        "Завтра Эрев Песаха (пятница): Таанит бехорот, биур и мехира до Шаббата."
    )
    return t


def _verify() -> None:
    from _explainer_keys import resolve_keys

    expected = set(resolve_keys()) | {
        "bedikat_sched_block",
        "mechirat_sched_block",
        "biur_sched_block",
        "seder_sched_block",
        "mechirat_no_sched",
    }
    for lang in LANGS:
        got = set(translations(lang))
        missing = expected - got
        extra = got - expected
        if missing:
            raise SystemExit(f"{lang} missing keys: {sorted(missing)}")
        if extra:
            raise SystemExit(f"{lang} extra keys: {sorted(extra)}")
        if translations(lang)["mechirat_no_sched"] != translations(lang)["mechirat_urgency"]:
            raise SystemExit(f"{lang}: mechirat_no_sched != mechirat_urgency")


if __name__ == "__main__":
    _verify()
    for lang in LANGS:
        print(f"{lang}: {len(translations(lang))} keys")
