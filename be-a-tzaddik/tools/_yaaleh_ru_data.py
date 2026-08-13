"""Informal RU (ты) bodies for Yaaleh V'yavo checklist templates and forgot blocks."""

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

_HALACHA_RU = "Шулхан Арух О.Х. 422:1; Пениней Халаха 05-01-10"

_FORGOT_SH_RU = (
    "Если забыл:\n"
    "• Ещё в «Рацей» (до завершения благословения) — вставь «Яале вьяво» на его место и продолжай ("
    + _HALACHA_RU + ").\n"
    "• После завершения «Рацей» — вернись к началу «Рацей», вставь «Яале вьяво» и заверши оставшиеся благословения ("
    + _HALACHA_RU + ").\n"
    "• Закончил всю Амиду (после финального «Йихию льрацон») — повтори только Амиду Шахарит (Шемоне Эсрей), "
    "никогда весь порядок молитвы, даже если уже молился Мусаф, Маарив или что-то ещё после ("
    + _HALACHA_RU + ")."
)
_FORGOT_MIN_RU = _FORGOT_SH_RU.replace("Шахарит", "Минха")
_FORGOT_MAARIV_RU = (
    "Если забыл на Маарив в Рош Ходеш:\n"
    "• Ещё в «Рацей» перед Именем Всевышнего в конце — вставь «Яале вьяво» там и продолжай ("
    + _HALACHA_RU + ").\n"
    "• После завершения «Рацей» или всей Амиды — не возвращайся и не повторяй. Бейт-дин освятил новый месяц днём, "
    "не ночью (Брахот 30б; " + _HALACHA_RU + "). Продолжай молитву."
)

YAALEH_TEMPLATE_RU: dict[str, str] = {
    YAALEH_SHACHARIT_TEMPLATE: (
        "Добавь «Яале вьяво» в Амиду Шахарит в Рош Ходеш — в благословение «Рацей» (Авода).\n\n"
        "$forgotBlock\n\n"
        "Добавь также «Яале вьяво» в Биркат а-Мазон, если ешь хлеб сегодня."
    ),
    YAALEH_SHACHARIT_FEMALE_TEMPLATE: (
        "Если читаешь Амиду Шахарит в Рош Ходеш, добавь «Яале вьяво» в благословение «Рацей» (Авода).\n\n"
        "$forgotBlock\n\n"
        "Если говоришь Биркат а-Мазон, когда ешь хлеб сегодня, добавь «Яале вьяво» и там."
    ),
    YAALEH_MINCHA_TEMPLATE: (
        "Добавь «Яале вьяво» в Амиду Минха в Рош Ходеш — в благословение «Рацей» (Авода).\n\n"
        "$forgotBlock"
    ),
    YAALEH_MINCHA_FEMALE_TEMPLATE: (
        "Если читаешь Амиду Минха в Рош Ходеш, добавь «Яале вьяво» в благословение «Рацей» (Авода).\n\n"
        "$forgotBlock\n\n"
        "Если говоришь Биркат а-Мазон, когда ешь хлеб сегодня, добавь «Яале вьяво» и там."
    ),
    YAALEH_MAARIV_TEMPLATE: (
        "Добавь «Яале вьяво» в Амиду Маарив в Рош Ходеш — в благословение «Рацей» (Авода).\n\n"
        "$forgotBlock\n\n"
        "Добавь также «Яале вьяво» в Биркат а-Мазон, если ешь хлеб этой ночью."
    ),
    YAALEH_MAARIV_FEMALE_TEMPLATE: (
        "Если читаешь Амиду Маарив в Рош Ходеш, добавь «Яале вьяво» в благословение «Рацей» (Авода).\n\n"
        "$forgotBlock\n\n"
        "Если говоришь Биркат а-Мазон, когда ешь хлеб этой ночью, добавь «Яале вьяво» и там."
    ),
    YAALEH_FORGOT_SHACHARIT: _FORGOT_SH_RU,
    YAALEH_FORGOT_MINCHA: _FORGOT_MIN_RU,
    YAALEH_FORGOT_MAARIV: _FORGOT_MAARIV_RU,
}

_FORGOT_HINT_RU = {
    "Forgot Yaaleh V'yavo at Maariv on Rosh Chodesh? After Retzei or after the Amidah — do not repeat (Berachot 30b; SA O.C. 422:1). Still in Retzei before God's name — insert there.": (
        "Забыл «Яале вьяво» на Маариве в Рош Ходеш? После «Рацей» или после всей Амиды — не повторяй (Брахот 30б; "
        "Шулхан Арух О.Х. 422:1). Если ещё в «Рацей» перед Именем — вставь там."
    ),
    "Forgot Yaaleh V'yavo? In Retzei — insert there; after concluding Retzei — return to beginning of Retzei; after Yihiyu L'ratzon — repeat only Mincha Amidah (SA O.C. 422:1).": (
        "Забыл «Яале вьяво»? В «Рацей» — вставь; после «Рацей» — вернись к началу «Рацей»; "
        "после «Йихию льрацон» — повтори только Амиду Минха (Шулхан Арух О.Х. 422:1)."
    ),
    "Forgot Yaaleh V'yavo? In Retzei — insert there; after concluding Retzei — return to beginning of Retzei; after Yihiyu L'ratzon — repeat only Shacharit Amidah (SA O.C. 422:1).": (
        "Забыл «Яале вьяво»? В «Рацей» — вставь; после «Рацей» — вернись к началу «Рацей»; "
        "после «Йихию льрацон» — повтори только Амиду Шахарит (Шулхан Арух О.Х. 422:1)."
    ),
}

_GLOSSARY_RU = {
    "Paragraph in Amidah and bentching on Rosh Chodesh and festivals. Forgot in Amidah: insert in Retzei if still there; after concluding Retzei return to the beginning of Retzei; after final Yihiyu L'ratzon repeat only that Amidah. Rosh Chodesh Maariv only: no repeat after Retzei (SA O.C. 422:1).": (
        "«Яале вьяво» в Амиде и Биркат а-Мазон в Рош Ходеш и праздники. Забыл: в «Рацей» — вставь; "
        "после «Рацей» — к началу «Рацей»; после «Йихию ле-рацон» — повтори только эту Амиду. "
        "Маарив Рош Ходеш: после «Рацей» — не повторяй (О.Х. 422:1)."
    ),
    "Yaaleh V'yavo — Paragraph in Amidah and bentching on Rosh Chodesh and festivals. Forgot in Amidah: insert in Retzei if still there; after concluding Retzei return to the beginning of Retzei; after final Yihiyu L'ratzon repeat only that Amidah. Rosh Chodesh Maariv only: no repeat after Retzei (SA O.C. 422:1).": (
        "«Яале вьяво» — в Амиде и Биркат а-Мазон в Рош Ходеш и праздники. Забыл: в «Рацей» — вставь; "
        "после «Рацей» — к началу; после «Йихию ле-рацон» — повтори только Амиду. "
        "Маарив Рош Ходеш: не повторяй (О.Х. 422:1)."
    ),
}

_YAALEH_OVERVIEW_RU = (
    "«Яале вьяво» — особый абзац в Амиде и в Биркат а-Мазон в Рош Ходеш, Йом Тов и холь а-моэд.\n\n"
    "Текст просит В-га помнить нас, отцов, Иерусалим, династию Давида и весь народ Израиля "
    "к добру — к жизни и миру — в этот день.\n\n"
    "Если забыл в Шахарит или Минхе (также холь а-моэд / Йом Тов в любой Амиде, включая Маарив):\n"
    "• Ещё в «Рацей» — вставь «Яале вьяво» и продолжай.\n"
    "• После завершения «Рацей» — вернись к началу «Рацей», вставь и заверши оставшиеся благословения.\n"
    "• После финального «Йихию льрацон» — повтори только эту Амиду (Шемоне Эсрей), не весь порядок.\n\n"
    "Только Маарив Рош Ходеш: если забыл после «Рацей» или после всей Амиды — не повторяй (Брахот 30б; "
    "Шулхан Арух О.Х. 422:1). Если ещё в «Рацей» перед Именем — вставь там."
)


def _expand(template: str, forgot: str) -> str:
    return template.replace("$forgotBlock", forgot)


_FULL_SPECS: tuple[tuple[str, str, str], ...] = (
    ("shacharit", YAALEH_SHACHARIT_TEMPLATE, _FORGOT_SH_RU),
    ("shacharit_female", YAALEH_SHACHARIT_FEMALE_TEMPLATE, _FORGOT_SH_RU),
    ("mincha", YAALEH_MINCHA_TEMPLATE, _FORGOT_MIN_RU),
    ("mincha_female", YAALEH_MINCHA_FEMALE_TEMPLATE, _FORGOT_MIN_RU),
    ("maariv", YAALEH_MAARIV_TEMPLATE, _FORGOT_MAARIV_RU),
    ("maariv_female", YAALEH_MAARIV_FEMALE_TEMPLATE, _FORGOT_MAARIV_RU),
)

for _name, _tmpl, _forgot in _FULL_SPECS:
    YAALEH_TEMPLATE_RU[YAALEH_FULL_EXPLAINERS[_name]] = _expand(YAALEH_TEMPLATE_RU[_tmpl], _forgot)

YAALEH_TEMPLATE_RU.update(_FORGOT_HINT_RU)
YAALEH_TEMPLATE_RU.update(_GLOSSARY_RU)


def yaaleh_ru_for_key(key: str) -> str | None:
    """Return ty Yaaleh RU body when [key] is in the catalog."""
    return YAALEH_TEMPLATE_RU.get(key)


def yaaleh_overview_ru_for_key(key: str) -> str | None:
    if key.startswith("Yaaleh V'Yavo is a special prayer paragraph"):
        return _YAALEH_OVERVIEW_RU
    return None
