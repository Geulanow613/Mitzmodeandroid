"""Informal ES/FR bodies for Yaaleh V'yavo checklist templates and forgot blocks."""

from __future__ import annotations

from _yaaleh_template_data import (
    FORGOT_MAARIV as YAALEH_FORGOT_MAARIV,
    YAALEH_FORGOT_MINCHA,
    YAALEH_FORGOT_SHACHARIT,
    YAALEH_FULL_EXPLAINERS,
    YAALEH_MINCHA_FEMALE_TEMPLATE,
    YAALEH_MINCHA_TEMPLATE,
    YAALEH_MAARIV_FEMALE_TEMPLATE,
    YAALEH_MAARIV_TEMPLATE,
    YAALEH_SHACHARIT_FEMALE_TEMPLATE,
    YAALEH_SHACHARIT_TEMPLATE,
)

_HALACHA_ES = "Shuljan Aruj O.C. 422:1; Peninei Halakha 05-01-10"
_HALACHA_FR = "Choulhan 'Aroukh O.C. 422:1; Peninei Halakha 05-01-10"

_FORGOT_SH_ES = (
    "Si olvidaste:\n"
    "• Aún en Retzei (antes de concluir la berajá) — inserta Yaale ve-Yavo en su lugar y continúa ("
    + _HALACHA_ES + ").\n"
    "• Tras concluir Retzei — vuelve al inicio de Retzei, inserta Yaale ve-Yavo y completa las berajot restantes ("
    + _HALACHA_ES + ").\n"
    "• Terminaste toda la Amidá (tras el Yihiyu le-ratzon final) — repite solo la Amidá de Shajarit (Shemoneh Esrei), "
    "nunca el servicio completo, aunque ya hayas rezado Musaf, Arvit u otra cosa después ("
    + _HALACHA_ES + ")."
)
_FORGOT_MIN_ES = _FORGOT_SH_ES.replace("Shajarit", "Minjá")
_FORGOT_MAARIV_ES = (
    "Si olvidaste en Arvit de Rosh Jodesh:\n"
    "• Aún en Retzei antes del Nombre al concluir — inserta Yaale ve-Yavo ahí y continúa ("
    + _HALACHA_ES + ").\n"
    "• Una vez terminaste Retzei, o tras toda la Amidá — no vuelvas atrás ni repitas. "
    "El Beit Din santificó el mes nuevo de día, no de noche (Berajot 30b; " + _HALACHA_ES + "). Sigue rezando."
)

_FORGOT_SH_FR = (
    "Si tu as oublié :\n"
    "• Encore dans Retzei (avant de conclure la bénédiction) — insère Yaale ve-Yavo à sa place et continue ("
    + _HALACHA_FR + ").\n"
    "• Après avoir conclu Retzei — reviens au début de Retzei, insère Yaale ve-Yavo et achève les bénédictions restantes ("
    + _HALACHA_FR + ").\n"
    "• Tu as terminé toute l'Amida (après le Yihiyou le-ratson final) — répète seulement l'Amida de Shaharit (Shemoneh Esrei), "
    "jamais tout le service, même si tu as déjà prié Moussaf, Arvit ou autre chose après ("
    + _HALACHA_FR + ")."
)
_FORGOT_MIN_FR = _FORGOT_SH_FR.replace("Shaharit", "Min'ha")
_FORGOT_MAARIV_FR = (
    "Si tu as oublié à Arvit de Rosh 'Hodesh :\n"
    "• Encore dans Retzei avant le Nom à la conclusion — insère Yaale ve-Yavo là et continue ("
    + _HALACHA_FR + ").\n"
    "• Une fois Retzei terminé, ou après toute l'Amida — ne reviens pas en arrière et ne répète pas. "
    "Le Beit Din a sanctifié le mois nouveau de jour, pas de nuit (Berakhot 30b; " + _HALACHA_FR + "). Continue à prier."
)

YAALEH_TEMPLATE_ES: dict[str, str] = {
    YAALEH_SHACHARIT_TEMPLATE: (
        "Añade Yaale ve-Yavo en la Amidá de Shajarit en Rosh Jodesh — en la berajá Retzei (Avodá).\n\n"
        "$forgotBlock\n\n"
        "Añade también Yaale ve-Yavo en Birkat HaMazón si comes pan hoy."
    ),
    YAALEH_SHACHARIT_FEMALE_TEMPLATE: (
        "Si recitas la Amidá de Shajarit en Rosh Jodesh, añade Yaale ve-Yavo en la berajá Retzei (Avodá).\n\n"
        "$forgotBlock\n\n"
        "Si dices Birkat HaMazón cuando comes pan hoy, añade Yaale ve-Yavo ahí también."
    ),
    YAALEH_MINCHA_TEMPLATE: (
        "Añade Yaale ve-Yavo en la Amidá de Minjá en Rosh Jodesh — en la berajá Retzei (Avodá).\n\n"
        "$forgotBlock"
    ),
    YAALEH_MINCHA_FEMALE_TEMPLATE: (
        "Si recitas la Amidá de Minjá en Rosh Jodesh, añade Yaale ve-Yavo en la berajá Retzei (Avodá).\n\n"
        "$forgotBlock\n\n"
        "Si dices Birkat HaMazón cuando comes pan hoy, añade Yaale ve-Yavo ahí también."
    ),
    YAALEH_MAARIV_TEMPLATE: (
        "Añade Yaale ve-Yavo en la Amidá de Arvit en Rosh Jodesh — en la berajá Retzei (Avodá).\n\n"
        "$forgotBlock\n\n"
        "Añade también Yaale ve-Yavo en Birkat HaMazón si comes pan esta noche."
    ),
    YAALEH_MAARIV_FEMALE_TEMPLATE: (
        "Si recitas la Amidá de Arvit en Rosh Jodesh, añade Yaale ve-Yavo en la berajá Retzei (Avodá).\n\n"
        "$forgotBlock\n\n"
        "Si dices Birkat HaMazón cuando comes pan esta noche, añade Yaale ve-Yavo ahí también."
    ),
    YAALEH_FORGOT_SHACHARIT: _FORGOT_SH_ES,
    YAALEH_FORGOT_MINCHA: _FORGOT_MIN_ES,
    YAALEH_FORGOT_MAARIV: _FORGOT_MAARIV_ES,
}

YAALEH_TEMPLATE_FR: dict[str, str] = {
    YAALEH_SHACHARIT_TEMPLATE: (
        "Ajoute Yaale ve-Yavo dans l'Amida de Shaharit à Rosh 'Hodesh — dans la bénédiction Retzei (Avodah).\n\n"
        "$forgotBlock\n\n"
        "Ajoute aussi Yaale ve-Yavo dans Birkat HaMazon si tu manges du pain aujourd'hui."
    ),
    YAALEH_SHACHARIT_FEMALE_TEMPLATE: (
        "Si tu récites l'Amida de Shaharit à Rosh 'Hodesh, ajoute Yaale ve-Yavo dans la bénédiction Retzei (Avodah).\n\n"
        "$forgotBlock\n\n"
        "Si tu dis Birkat HaMazon en mangeant du pain aujourd'hui, ajoute Yaale ve-Yavo là aussi."
    ),
    YAALEH_MINCHA_TEMPLATE: (
        "Ajoute Yaale ve-Yavo dans l'Amida de Min'ha à Rosh 'Hodesh — dans la bénédiction Retzei (Avodah).\n\n"
        "$forgotBlock"
    ),
    YAALEH_MINCHA_FEMALE_TEMPLATE: (
        "Si tu récites l'Amida de Min'ha à Rosh 'Hodesh, ajoute Yaale ve-Yavo dans la bénédiction Retzei (Avodah).\n\n"
        "$forgotBlock\n\n"
        "Si tu dis Birkat HaMazon en mangeant du pain aujourd'hui, ajoute Yaale ve-Yavo là aussi."
    ),
    YAALEH_MAARIV_TEMPLATE: (
        "Ajoute Yaale ve-Yavo dans l'Amida de Maariv à Rosh 'Hodesh — dans la bénédiction Retzei (Avodah).\n\n"
        "$forgotBlock\n\n"
        "Ajoute aussi Yaale ve-Yavo dans Birkat HaMazon si tu manges du pain ce soir."
    ),
    YAALEH_MAARIV_FEMALE_TEMPLATE: (
        "Si tu récites l'Amida de Maariv à Rosh 'Hodesh, ajoute Yaale ve-Yavo dans la bénédiction Retzei (Avodah).\n\n"
        "$forgotBlock\n\n"
        "Si tu dis Birkat HaMazon en mangeant du pain ce soir, ajoute Yaale ve-Yavo là aussi."
    ),
    YAALEH_FORGOT_SHACHARIT: _FORGOT_SH_FR,
    YAALEH_FORGOT_MINCHA: _FORGOT_MIN_FR,
    YAALEH_FORGOT_MAARIV: _FORGOT_MAARIV_FR,
}

_FORGOT_HINT_ES = {
    "Forgot Yaaleh V'yavo at Maariv on Rosh Chodesh? After Retzei or after the Amidah — do not repeat (Berachot 30b; SA O.C. 422:1). Still in Retzei before God's name — insert there.": (
        "¿Olvidaste Yaale ve-Yavo en Arvit de Rosh Jodesh? Tras Retzei o tras toda la Amidá — no repitas (Berajot 30b; Shuljan Aruj O.C. 422:1). "
        "Si aún estás en Retzei antes del Nombre — insértalo ahí."
    ),
    "Forgot Yaaleh V'yavo? In Retzei — insert there; after concluding Retzei — return to beginning of Retzei; after Yihiyu L'ratzon — repeat only Mincha Amidah (SA O.C. 422:1).": (
        "¿Olvidaste Yaale ve-Yavo? En Retzei — insértalo; tras concluir Retzei — vuelve al inicio de Retzei; "
        "tras Yihiyu le-ratzon — repite solo la Amidá de Minjá (Shuljan Aruj O.C. 422:1)."
    ),
    "Forgot Yaaleh V'yavo? In Retzei — insert there; after concluding Retzei — return to beginning of Retzei; after Yihiyu L'ratzon — repeat only Shacharit Amidah (SA O.C. 422:1).": (
        "¿Olvidaste Yaale ve-Yavo? En Retzei — insértalo; tras concluir Retzei — vuelve al inicio de Retzei; "
        "tras Yihiyu le-ratzon — repite solo la Amidá de Shajarit (Shuljan Aruj O.C. 422:1)."
    ),
}
_FORGOT_HINT_FR = {
    "Forgot Yaaleh V'yavo at Maariv on Rosh Chodesh? After Retzei or after the Amidah — do not repeat (Berachot 30b; SA O.C. 422:1). Still in Retzei before God's name — insert there.": (
        "Oublié Yaale ve-Yavo à Maariv de Rosh 'Hodesh ? Après Retzei ou après toute l'Amida — ne répète pas (Berakhot 30b ; "
        "Choulhan 'Aroukh O.C. 422:1). Encore dans Retzei avant le Nom — insère-le là."
    ),
    "Forgot Yaaleh V'yavo? In Retzei — insert there; after concluding Retzei — return to beginning of Retzei; after Yihiyu L'ratzon — repeat only Mincha Amidah (SA O.C. 422:1).": (
        "Oublié Yaale ve-Yavo ? Dans Retzei — insère-le ; après Retzei — reviens au début de Retzei ; "
        "après Yihiyou le-ratson — répète seulement l'Amida de Min'ha (Choulhan 'Aroukh O.C. 422:1)."
    ),
    "Forgot Yaaleh V'yavo? In Retzei — insert there; after concluding Retzei — return to beginning of Retzei; after Yihiyu L'ratzon — repeat only Shacharit Amidah (SA O.C. 422:1).": (
        "Oublié Yaale ve-Yavo ? Dans Retzei — insère-le ; après Retzei — reviens au début de Retzei ; "
        "après Yihiyou le-ratson — répète seulement l'Amida de Shaharit (Choulhan 'Aroukh O.C. 422:1)."
    ),
}


def _expand(template: str, forgot: str) -> str:
    return template.replace("$forgotBlock", forgot)


_FULL_SPECS: tuple[tuple[str, str, str, str], ...] = (
    ("shacharit", YAALEH_SHACHARIT_TEMPLATE, _FORGOT_SH_ES, _FORGOT_SH_FR),
    ("shacharit_female", YAALEH_SHACHARIT_FEMALE_TEMPLATE, _FORGOT_SH_ES, _FORGOT_SH_FR),
    ("mincha", YAALEH_MINCHA_TEMPLATE, _FORGOT_MIN_ES, _FORGOT_MIN_FR),
    ("mincha_female", YAALEH_MINCHA_FEMALE_TEMPLATE, _FORGOT_MIN_ES, _FORGOT_MIN_FR),
    ("maariv", YAALEH_MAARIV_TEMPLATE, _FORGOT_MAARIV_ES, _FORGOT_MAARIV_FR),
    ("maariv_female", YAALEH_MAARIV_FEMALE_TEMPLATE, _FORGOT_MAARIV_ES, _FORGOT_MAARIV_FR),
)

for _name, _tmpl, _f_es, _f_fr in _FULL_SPECS:
    _full_en = YAALEH_FULL_EXPLAINERS[_name]
    YAALEH_TEMPLATE_ES[_full_en] = _expand(YAALEH_TEMPLATE_ES[_tmpl], _f_es)
    YAALEH_TEMPLATE_FR[_full_en] = _expand(YAALEH_TEMPLATE_FR[_tmpl], _f_fr)

YAALEH_TEMPLATE_ES.update(_FORGOT_HINT_ES)
YAALEH_TEMPLATE_FR.update(_FORGOT_HINT_FR)
