"""Human-quality glossary fixes batch 4 — RU glued glossary + FR customs/douanes."""

from __future__ import annotations

_SEFER_TORAH_RU = (
    "Сефер Тора — свиток Торы: все пять книг Торы, написанные софером от руки на кошерном пергаменте, "
    "закатанные на двух деревянных валиках (atzei chayim). Его читают в синагоге с особой честью; "
    "обычно не учатся ежедневно из свитка, как из Хумаша. Пропущенные или лишние буквы могут "
    "сделать свиток недействительным, поэтому соферим учатся годами."
)

_MIKVEH_RU = (
    "Миква — кошерный ритуальный бассейн с природной водой, соответствующий галахическим требованиям "
    "к размеру и потоку. Используется для тахарат а-мишпача, обращения в иудаизм, тевилат кейлим "
    "и некоторых изменений статуса посуды. Погружение — мощный переход: человек или сосуд выходит "
    "духовно обновлённым. Общинные микваот сдержанны и обслуживаются обученным персоналом."
)

_PARSHA_RU = (
    "Парша (параша) — еженедельная порция Торы, читаемая в синагоге в утро Шаббата. "
    "Тора разделена на 54 порции, чтобы за год прочитать все пять книг (иногда порции объединяют). "
    "Shnayim Mikra означает изучение той же парши в течение недели до публичного чтения. "
    "Название звучит как «Паршат Берешит», «Паршат Ноах» и т.д."
)

_HALACHIC_HOUR_RU = (
    "Галахический час (ша'а zmanit) — одна двенадцатая часть светового дня от рассвета до заката, "
    "поэтому летом длиннее, а зимой короче, в отличие от 60-минутного часа. Сроки вроде "
    "последнего утреннего Шма, продажи хамеца и плаг а-минха измеряются в этих часах. "
    "Приложения и еврейские календари переводят их для вашего местоположения."
)

_KELI_RU = (
    "Кели — сосуд, принадлежность или инструмент. Обычно это любой изготовленный предмет или ёмкость, "
    "что держит, обрабатывает или содержит что-либо. Кели используется для нетилат ядаим — "
    "чашка, бутылка или кувшин для омовения рук (обычно с двумя ручками)."
)

BATCH4_RU: dict[str, str] = {
    "A Sefer Torah is a Torah scroll — the entire five books of the Torah handwritten by a sofer on kosher parchment, rolled on two wooden rollers (atzei chayim). It is read in synagogue with special honor; you usually do not study daily from the scroll the way you use a Chumash. Missing or extra letters can invalidate a scroll, so scribes train for years.": _SEFER_TORAH_RU,
    "Sefer Torah — A Sefer Torah is a Torah scroll — the entire five books of the Torah handwritten by a sofer on kosher parchment, rolled on two wooden rollers (atzei chayim). It is read in synagogue with special honor; you usually do not study daily from the scroll the way you use a Chumash. Missing or extra letters can invalidate a scroll, so scribes train for years.": f"Сефер Тора — {_SEFER_TORAH_RU}",
    "A mikveh is a kosher ritual pool of natural water meeting halachic size and flow rules. It is used for taharat hamishpacha, conversion, tevilat keilim, and some utensil and status changes. Immersion is a powerful transition — the person or vessel emerges spiritually renewed. Community mikvaot are discreet and staffed by trained attendants.": _MIKVEH_RU,
    "mikveh — A mikveh is a kosher ritual pool of natural water meeting halachic size and flow rules. It is used for taharat hamishpacha, conversion, tevilat keilim, and some utensil and status changes. Immersion is a powerful transition — the person or vessel emerges spiritually renewed. Community mikvaot are discreet and staffed by trained attendants.": f"миква — {_MIKVEH_RU}",
    'A parsha (parashah) is the weekly Torah portion read in synagogue on Shabbat morning. The Torah is divided into 54 portions so the whole five books are completed each year (with combined portions sometimes depending on the calendar). Shnayim Mikra means studying the same parsha during the week before it is read publicly. The name appears as "Parshat Bereishit," "Parshat Noach," and so on.': _PARSHA_RU,
    'parsha — A parsha (parashah) is the weekly Torah portion read in synagogue on Shabbat morning. The Torah is divided into 54 portions so the whole five books are completed each year (with combined portions sometimes depending on the calendar). Shnayim Mikra means studying the same parsha during the week before it is read publicly. The name appears as "Parshat Bereishit," "Parshat Noach," and so on.': f"парша — {_PARSHA_RU}",
    "A halachic hour (sha'ah zmanit) is one twelfth of the daylight period from dawn to dusk — so it is longer in summer and shorter in winter, unlike a 60-minute clock hour. Deadlines such as latest morning Shema, chametz sale times, and plag hamincha use these hours. Apps and Jewish calendars convert them for your location.": _HALACHIC_HOUR_RU,
    "halachic hour — A halachic hour (sha'ah zmanit) is one twelfth of the daylight period from dawn to dusk — so it is longer in summer and shorter in winter, unlike a 60-minute clock hour. Deadlines such as latest morning Shema, chametz sale times, and plag hamincha use these hours. Apps and Jewish calendars convert them for your location.": f"галахический час — {_HALACHIC_HOUR_RU}",
    "Keli — A keli is a vessel, utensil, or tool. It generally refers to any manufactured object or receptacle that holds, processes, or contains something. A keli is used for netilat yadayim — a cup, bottle, or washing cup (usually) with two handles.": _KELI_RU,
    "Afikoman — The Afikoman is matzah eaten at the end of the Seder so no other food follows — remembering the Pesach sacrifice. Children often \"steal\" it for a prize, adding joy. You need a kezayit-sized piece eaten before chatzos halachic (midpoint of the night) per many poskim. It is the last taste of matzah the Seder requires.": (
        "Афикоман — маца, съедаемая в конце Седера, чтобы после неё не было другой пищи — "
        "в память о пасхальном жертвоприношении. Дети часто «крадут» её ради приза, добавляя радости. "
        "Нужен кусок размером с кезайит, съеденный до хацот (середины ночи) по мнению многих посеким. "
        "Это последний вкус мацы, который требует Седер."
    ),
    "The Afikoman is matzah eaten at the end of the Seder so no other food follows — remembering the Pesach sacrifice. Children often \"steal\" it for a prize, adding joy. You need a kezayit-sized piece eaten before chatzos halachic (midpoint of the night) per many poskim. It is the last taste of matzah the Seder requires.": (
        "Афикоман — маца, съедаемая в конце Седера, чтобы после неё не было другой пищи — "
        "в память о пасхальном жертвоприношении. Дети часто «крадут» её ради приза. "
        "Нужен кусок размером с кезайит до хацот по мнению многих посеким — "
        "последний вкус мацы, который требует Седер."
    ),
    "Hagbah is lifting the open Torah scroll so the congregation can see the writing and recite \"V'zot HaTorah\" (and related verses). Ashkenazim (and most Chabad communities) perform hagbah after the reading, before gelilah (rolling and dressing the scroll). Sephardim (Nusach Edot HaMizrach) perform hagbah before the reading begins — the scroll is shown, then read. Follow your shul's order so you know when to stand and respond.": (
        "Хагба — поднятие открытого свитка Торы, чтобы община видела письмо и произносила "
        "«V'zot HaTorah» (и связанные стихи). Ашкеназы (и большинство общин Хабада) делают хагбу "
        "после чтения, перед гелилой (сворачиванием и одеванием свитка). Сефарды (нусах Edot HaMizrach) "
        "делают хагбу до начала чтения — свиток показывают, затем читают. Следуйте порядку вашей синагоги, "
        "чтобы знать, когда вставать и отвечать."
    ),
}

_AVEILUT_FR = (
    "L'aveilut est la pratique du deuil après la perte d'un proche parent. Les coutumes comprennent "
    "la shiva (sept jours), le shloshim (trente jours) et, pour les parents, onze mois. Pendant l'Omer, "
    "les coutumes d'aveilut se chevauchent avec le deuil national pour les étudiants de Rabbi Akiva. "
    "Pendant les trois semaines du 17 Tamouz au 9 Av, nous pleurons la destruction du Temple."
)

_DAYS_OF_AWE_FR = (
    "Les jours de la crainte (Aseret Yemei Teshuvah) s'étendent de Roch Hachana à Yom Kippour — "
    "temps de jugement, prière et teshouva. Les coutumes s'intensifient : Selichot, charité et "
    "demande de pardon. Même les Juifs moins observants toute l'année assistent souvent aux offices. "
    "Le ton est solennel mais plein d'espoir — le repentir peut changer un décret sévère."
)

_THREE_WEEKS_FR = (
    "Les trois semaines du 17 Tamouz au 9 Av (Tisha BeAv) pleurent la destruction du Temple. "
    "Les ashkénazes et le Habad interdisent coupes de cheveux, mariages et musique instrumentale "
    "pendant toute la période ; les séfarades et Edot HaMizrach sont en général plus indulgents "
    "jusqu'à la semaine de Tisha BeAv. Les restrictions s'intensifient pendant les neuf jours — "
    "les coutumes diffèrent selon le nusah."
)

_ROSH_CHODESH_MEAL_FR = (
    "• C'est une mitzvah d'enrichir votre repas à Roch 'Hodesh — au minimum un plat supplémentaire "
    "ou un aliment spécial en l'honneur du jour (Choulhan Aroukh O.C. 419:1).\n"
    "• Prenez le repas de jour. Les poskim écrivent que cela commémore le festin que le Sanhédrin "
    "tenait à Beit Ya'zek pour les témoins venus attester avoir vu la nouvelle lune "
    "(Mishna Roch Hachana 2:5 ; Orhos 'Haïm et Kol Bo, cités sur O.C. 419).\n"
    "• L'argent dépensé pour les repas de Roch 'Hodesh — comme pour Chabbat et Yom Tov — "
    "n'est pas déduit du revenu qui vous est alloué à Roch Hachana ; si vous dépensez plus pour "
    "ces mitzvot, le Ciel ajoute à votre part (Pessikta de-Rav Kahana, citée dans le Tour O.C. 419 "
    "et Maguen Avraham 419:1)."
)

_ROSH_CHODESH_OTHER_FR = (
    "Autres coutumes :\n"
    "• Le jeûne et les éloges funèbres ne se font généralement pas à Roch 'Hodesh\n"
    "• Coutume répandue : les femmes mariées s'abstiennent de certaines melakhot (couture, lessive, etc.) "
    "en signe d'honneur supplémentaire — demandez les détails à votre rav\n\n"
    "Quand Roch 'Hodesh dure deux jours (30e du mois précédent et 1er), les observances s'appliquent aux deux jours."
)

_RC_INTRO_FR = (
    "Roch 'Hodesh (ראש חודש) — le nouveau mois — est un demi-fête avec prières et coutumes supplémentaires.\n\n"
    "Repas festif (mitzvah) :\n"
)

_RC_INTRO_SHORT_FR = (
    "Roch 'Hodesh commence le mois hébraïque — demi-fête avec Hallel (demi), Moussaf, Yaaleh V'yavo, "
    "et souvent travail réduit pour les femmes selon la coutume. Retirez le téfiline avant Moussaf — "
    "porter le téfiline pendant le Moussaf de Roch 'Hodesh est interdit. Autrefois la nouvelle lune "
    "était proclamée sur témoignage ; aujourd'hui le calendrier est fixé. C'est une remise à zéro mensuelle — "
    "planifiez objectifs de Torah et charité."
)

_RC_DAVENING_DETAIL_FR = (
    "Prière aujourd'hui (voir vos sections Matin, Après-midi et Soir) :\n"
    "• Si vous récitez Shaharit, Min'ha ou Maariv Amida — ajoutez Yaaleh V'yavo dans Retzei. "
    "Shaharit/Min'ha : corrigez selon le moment (insérez dans Retzei, revenez au début de Retzei "
    "si déjà conclu, ou répétez seulement cette Amida si terminée). Maariv de Roch 'Hodesh seulement : "
    "ne répétez pas si oublié après Retzei (Berakhot 30b ; SA O.C. 422:1)\n"
    "• Si vous dites Birkat Hamazon en mangeant du pain — ajoutez Yaaleh V'yavo aussi\n"
    "• Coutume ashkénaze — la plupart des autorités obligent l'Amida de Shaharit et Min'ha ces jours-là\n"
    "• Coutume séfarade — beaucoup de femmes s'acquittent de l'obligation quotidienne avec une seule prière ; "
    "si vous priez une Amida supplémentaire et oubliez Yaaleh V'yavo, demandez à votre rav une stipulation "
    "de nedavah (offrande volontaire)\n"
    "• Demi-Hallel à Shaharit si vous dites Hallel (facultatif — suivez votre minhag ; Hallel complet "
    "si Roch 'Hodesh tombe pendant Hanoucca)\n"
    "• Tahanoun est omis toute la journée\n\n"
)

_RC_DAVENING_SUMMARY_FR = (
    "Prière aujourd'hui (voir vos sections Matin, Après-midi et Soir) :\n"
    "• Yaaleh V'yavo dans l'Amida de Shaharit, Min'ha et Maariv — et au bentching quand vous mangez du pain\n"
    "• Demi-Hallel à Shaharit (Hallel complet si Roch 'Hodesh tombe pendant Hanoucca ; pas de Hallel "
    "à Roch 'Hodesh Tichri / Roch Hachana)\n"
    "• Moussaf après Shaharit — retirez le téfiline avant Moussaf\n"
    "• Tahanoun est omis toute la journée\n\n"
)

_KOTEIV_FR = (
    "Apprenez la melakha de Koteiv — l'interdiction fondamentale d'écrire — et découvrez comment "
    "cette limite créative façonne notre conduite le Chabbat ! Cette catégorie centrale de travail "
    "créatif remonte à la construction du Michkan, où des artisans qualifiés inscrivaient des lettres "
    "ou symboles distincts sur les poutres en bois pour marquer les paires correspondantes, afin que "
    "l'édifice puisse être démonté et remonté avec précision. La définition centrale de Koteiv est la "
    "formation de tout symbole, caractère ou lettre porteur de sens sur une surface. Pour transgresser "
    "l'interdiction toranique, il faut écrire au moins deux lettres ou symboles distincts avec un "
    "support permanent sur une surface durable. Les Sages ont étendu cette limite pour protéger le "
    "jour de repos, interdisant rabbiniquement l'écriture temporaire et la formation de lettres ou "
    "images qui ne préexistent pas. De même, recoller une page déchirée pour que les lettres s'alignent "
    "peut violer Koteiv. Même les livres dont les tranches portent des mots ou motifs estampés "
    "directement sur les bords extérieurs des pages posent une question particulière : ouvrir et fermer "
    "le livre sépare et réunit le texte, ce qui pousse beaucoup d'autorités à préférer des livres "
    "sans impression sur les tranches le jour saint. Cette limite guide aussi les jeux comme le Scrabble : "
    "poser de simples lettres plates côte à côte est en général permis, mais les versions de voyage où "
    "les lettres s'enclenchent dans un cadre rigide sont discutées, car fixer fermement des lettres "
    "ressemble à écrire. Pour les scores, les joueurs soucieux de la halakha peuvent suivre les points "
    "en feuilletant les pages d'un livre (non saint) plutôt qu'en écrivant !"
)

BATCH4_FR: dict[str, str] = {
    "Aveilut is mourning practice after the loss of a close relative. Customs include shiva (seven days), shloshim (thirty days), and for parents, eleven months. During the Omer, aveilut customs overlap with national mourning for Rabbi Akiva's students. During the 3 weeks from 17 Tammuz to 9 Av, we mourn the Temple's destruction.": _AVEILUT_FR,
    "aveilut — Aveilut is mourning practice after the loss of a close relative. Customs include shiva (seven days), shloshim (thirty days), and for parents, eleven months. During the Omer, aveilut customs overlap with national mourning for Rabbi Akiva's students. During the 3 weeks from 17 Tammuz to 9 Av, we mourn the Temple's destruction.": f"aveilut — {_AVEILUT_FR}",
    "The Days of Awe (Aseret Yemei Teshuvah) span Rosh Hashana through Yom Kippur — a time of judgment, prayer, and teshuvah. Customs intensify: Selichot, charity, and asking forgiveness. Even Jews who are less observant year-round often attend services. The tone is solemn but hopeful — repentance can change a harsh decree.": _DAYS_OF_AWE_FR,
    "Days of Awe — The Days of Awe (Aseret Yemei Teshuvah) span Rosh Hashana through Yom Kippur — a time of judgment, prayer, and teshuvah. Customs intensify: Selichot, charity, and asking forgiveness. Even Jews who are less observant year-round often attend services. The tone is solemn but hopeful — repentance can change a harsh decree.": f"Jours de la crainte — {_DAYS_OF_AWE_FR}",
    "The Three Weeks from 17 Tammuz to Tisha B'Av mourn the Temple's destruction. Ashkenazim and Chabad prohibit haircuts, weddings, and instrumental music for the full period; Sephardim and Edot HaMizrach are generally more lenient until the week of Tisha B'Av. Restrictions intensify in the Nine Days — customs differ by nusach.": _THREE_WEEKS_FR,
    "Three Weeks — The Three Weeks from 17 Tammuz to Tisha B'Av mourn the Temple's destruction. Ashkenazim and Chabad prohibit haircuts, weddings, and instrumental music for the full period; Sephardim and Edot HaMizrach are generally more lenient until the week of Tisha B'Av. Restrictions intensify in the Nine Days — customs differ by nusach.": f"Three Weeks — {_THREE_WEEKS_FR}",
    "Rosh Chodesh begins the Hebrew month — semi-holiday with Hallel (half), Musaf, Yaaleh V'yavo, and often reduced work for women per custom. Remove tefillin before Musaf — wearing tefillin during Rosh Chodesh Musaf is forbidden. In ancient times the new moon was proclaimed from testimony. Today the calendar is fixed. It is a monthly reset — plan Torah goals and charity.": _RC_INTRO_SHORT_FR,
    "Rosh Chodesh — Rosh Chodesh begins the Hebrew month — semi-holiday with Hallel (half), Musaf, Yaaleh V'yavo, and often reduced work for women per custom. Remove tefillin before Musaf — wearing tefillin during Rosh Chodesh Musaf is forbidden. In ancient times the new moon was proclaimed from testimony. Today the calendar is fixed. It is a monthly reset — plan Torah goals and charity.": f"Roch 'Hodesh — {_RC_INTRO_SHORT_FR}",
}


def _fr_rc_text(*, detail: bool) -> str:
    body = _RC_DAVENING_DETAIL_FR if detail else _RC_DAVENING_SUMMARY_FR
    return _RC_INTRO_FR + _ROSH_CHODESH_MEAL_FR + "\n\n" + body + _ROSH_CHODESH_OTHER_FR


def _register_long_keys() -> None:
    import json
    from pathlib import Path

    catalog = Path(__file__).resolve().parents[1] / "data" / "translation-catalog" / "strings.json"
    for key in json.loads(catalog.read_text(encoding="utf-8"))["strings"]:
        if key.startswith("Rosh Chodesh (") and len(key) > 500:
            BATCH4_FR[key] = _fr_rc_text(detail="If you recite" in key)
        elif key.startswith("Learn about the Melacha of Koteiv"):
            BATCH4_FR[key] = _KOTEIV_FR


_register_long_keys()
