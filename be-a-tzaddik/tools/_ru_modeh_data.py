"""Authoritative RU rewrites for Modeh Ani glossary and explainers."""

from __future__ import annotations

MODEH_RU_EXACT: dict[str, str] = {
    "Modeh Ani": "Модэ ани",
    "Modeh Ani (Thank G-d upon waking)": "Модэ ани (благодарность В-гу при пробуждении)",
    "Modah — feminine form of Modeh (grateful) in Modeh Ani": (
        "Мода — женская форма «моде» (благодарная) в «Модэ ани»"
    ),
    "feminine form of Modeh (grateful) in Modeh Ani": (
        "женская форма «моде» (благодарная) в «Модэ ани»"
    ),
    "Neshama is the soul — the divine breath that makes you alive. Modeh Ani thanks G-d for returning it after sleep. Yizkor and Kaddish relate to the soul's journey after death. Jewish thought distinguishes levels of soul (nefesh, ruach, neshama) in mysticism; practically, nurturing the soul means Torah, mitzvot, and character.": (
        "Нешама — душа, божественное дыхание. «Модэ ани» после сна; йизкор и кадиш — путь души. "
        "Практически — Тора, мицвот, характер."
    ),
    "neshama — Neshama is the soul — the divine breath that makes you alive. Modeh Ani thanks G-d for returning it after sleep. Yizkor and Kaddish relate to the soul's journey after death. Jewish thought distinguishes levels of soul (nefesh, ruach, neshama) in mysticism; practically, nurturing the soul means Torah, mitzvot, and character.": (
        "нешама — душа, божественное дыхание жизни. «Модэ ани» после сна; йизкор и кадиш — путь души. "
        "Практически — Тора, мицвот, характер."
    ),
    "Chatzos is halachic midnight or midday — the midpoint of the night or day. Chatzos halayla matters for saying Modeh Ani, starting morning blessings, and some fast practices. Chatzos hayom is the midpoint of daylight. It moves with sunrise and sunset, not with 12:00 on the clock.": (
        "Хацот — галахическая полночь или полдень. Хацот ха-лайла — для «Модэ ани» и утренних брахот. "
        "Хацот ха-йом — середина дня; не совпадает с часами на часах."
    ),
    "halachic chatzos — Chatzos is halachic midnight or midday — the midpoint of the night or day. Chatzos halayla matters for saying Modeh Ani, starting morning blessings, and some fast practices. Chatzos hayom is the midpoint of daylight. It moves with sunrise and sunset, not with 12:00 on the clock.": (
        "галахический хацот — середина ночи или дня. Хацот ха-лайла — для «Модэ ани», утренних брахот и постов. "
        "Хацот ха-йом — середина дневного света; движется с восходом и закатом, не с 12:00."
    ),
}

_MODEH_SHORT = (
    "«Модэ ани» — первые слова, которые многие евреи произносят, просыпаясь от ночного сна: "
    "благодарность живому и вечному Царю за возвращение души с состраданием. "
    "Произнеси это в постели, прежде чем вставать, не умываясь. "
    "Обычно не читают после дневного сна — тогда вымой руки (негел вассер). "
    "Можешь повторять слова как личную благодарность, но это не официальная утренняя молитва. "
    "Мужчины: «Моде»; женщины часто: «Мода»."
)

_MODEH_LONG = (
    "Первое действие каждого дня:\n\n"
    "Что это:\n"
    "«Модэ ани» (מוֹדֶה אֲנִי — буквально «благодарю») — двухстрочная молитва в момент, когда открываешь глаза. "
    "Самое простое выражение еврейской благодарности — поблагодарить В-га за возвращение души после сна.\n\n"
    "Традиция:\n"
    "Сон — ночной опыт, близкий к смерти: душа каждую ночь частично покидает тело и возвращается утром. "
    "«Модэ ани» признаёт, что В-г дал тебе ещё один день.\n\n"
    "Иврит и перевод:\n"
    "«Моде ани лефанеха, Мелех хай ве-кайам, shehechezarta bi nishmati be-khemla — raba emunatekha.»\n"
    "«Благодарю Тебя, живой и вечный Царь, что вернул мне душу с состраданием — насколько велика Твоя верность.»\n\n"
    "Как делать:\n"
    "Произнеси, ещё лёжа в постели, перед тем как встать, сразу после основного ночного сна — прежде чем делать что-либо. "
    "«Модэ ани» — одна из немногих частей даавенинга, которую можно сказать без предварительного омовения рук — "
    "затем переходи к нетилат ядаим (негел вассер; см. следующий пункт).\n\n"
    "Ночной сон и дневной:\n"
    "Можешь повторять слова как личную благодарность, но это не официальная утренняя молитва. "
    "После дневного сна обычно не читают — просто вымой руки по правилам следующего пункта."
)

MODEH_RU_BY_PREFIX: dict[str, str] = {
    "Modeh Ani is the first words many Jews": _MODEH_SHORT,
    "Modeh Ani — Modeh Ani is the first words": f"Модэ ани — {_MODEH_SHORT}",
    "The very first act of each day": _MODEH_LONG,
    "While men are obligated in three structured prayer times": (
        "Мужчины обязаны в трёх структурированных временах молитвы; женщины — молиться хотя бы раз в день.\n\n"
        "Тефила (תְּפִלָּה) — от корня «судить себя». Молитва — мицва и прямой канал связи с В-гом. "
        "Из «служить Б-гу всем сердцем» (Дварим 11:13) выводят молитву — служение сердца.\n\n"
        "Минимум для женщин: хвала, просьбы и благодарность. Многие читают полную Амиду; "
        "другие — искреннюю личную молитву на любом языке.\n\n"
        "Новичкам: начни с того, что можешь выдерживать. Несколько искренних фраз утром — уже исполнение. "
        "Со временем учись больше по сидуру."
    ),
    "Women are obligated in Kriat Shema al HaMitah": (
        "Женщины обязаны в Криат Шема аль ha-митa (שְׁמַע עַל הַמִּטָּה — Шema на сон), "
        "хотя освобождены от большинства временных мицвот асе.\n\n"
        "Шмира (שְׁמִירָה): слова охраняют во сне, когда душа частично отходит — женщинам это нужно так же, как мужчинам.\n\n"
        "Хамапиль: благословение перед сном — по ашкеназскому минхагу при укладывании (Мишна Берура 239:6). "
        "При сомнении, заснёшь ли скоро — можно без заключительного «Бaruch Ata…» или как медитация без Имени.\n\n"
        "Минимум: первый абзац Шемы; рекомендуется полный порядок с Псалмом 91 и Хамапилом. "
        "Произнеси перед сном на ночь, не перед дневным сном. Уточни у рава или реббецин."
    ),
    "Make an appointment for a private audience with the King of Kings": (
        "Запишись на частную аудиенцию у Царя царей! 👑 Шмоне Эсрей (Амида) — сердце каждой молитвы, "
        "составлена с руах ha-kodesh — каждое слово ключ к небесным вратам 🔓. "
        "Протокол: стой, ноги вместе (как у ангелов), лицом к Иерусалиму 🏛️. "
        "Три части — хвала, просьбы, благодарность. В Шаббат и праздники читай нужную Амиду из сидура 📜. "
        "Кавана особенно в «Авот» 🕊️. Погрузись — и получи благословения, которые В-г хочет тебе дать! "
        "Узнай больше 🎊🙏"
    ),
}


def modeh_ru_for_key(key: str) -> str | None:
    if key in MODEH_RU_EXACT:
        return MODEH_RU_EXACT[key]
    best: str | None = None
    best_len = 0
    for prefix, val in MODEH_RU_BY_PREFIX.items():
        if key.startswith(prefix) and len(prefix) > best_len:
            best = val
            best_len = len(prefix)
    return best
