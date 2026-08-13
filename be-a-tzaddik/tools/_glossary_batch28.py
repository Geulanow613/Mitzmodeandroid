"""Human-quality fixes batch 28 — RU prefix keys, mitzvah essays, Boneh melacha."""

from __future__ import annotations

import json
from pathlib import Path

from _glossary_batch27 import BAT_MITZVAH_RU, CHAROSET_RU, LCHATCHILA_RU
from _melacha_ru_boneh import BONEH_RU

ROOT = Path(__file__).resolve().parents[1]
CATALOG = json.loads((ROOT / "data" / "translation-catalog" / "strings.json").read_text(encoding="utf-8"))[
    "strings"
]

_MACHLOKET_RU = (
    "Махлокет — уважительное расхождение между Torah authorities — "
    "часто зафиксированное в Талмуде. Не каждая махлокет дозволена; "
    "обе стороны могут быть правомерны. Мицва — следовать псаку своего рава "
    "на практике, уважая, что другая община может следовать другому "
    "правомерному мнению. Махлокет ле-шэм шамайим — ради истины; "
    "спор из эго разрушителен."
)

BAT_MITZVAH_PREFIX_KEY = next(k for k in CATALOG if k.startswith("bat mitzvah — Bat mitzvah"))
CHAROSET_PREFIX_KEY = next(k for k in CATALOG if k.startswith("charoset — Charoset"))
LCHATCHILA_PREFIX_KEY = next(k for k in CATALOG if k.startswith("l'chatchila — L'chatchila"))
MACHLOKET_KEY = next(k for k in CATALOG if k.startswith("Machloket is respectful"))
MACHLOKET_PREFIX_KEY = next(k for k in CATALOG if k.startswith("machloket — Machloket"))
TZITZIT_EXP_KEY = next(k for k in CATALOG if k.startswith("Experience the mitzvah of tzitzit"))
TZITZIT_WRAP_KEY = next(k for k in CATALOG if k.startswith("Get wrapped up in the mitzvah"))
CONSTANT_6_KEY = next(k for k in CATALOG if k.startswith("Master #6 of the Constant Mitzvot"))
EAT_CHAMETZ_KEY = next(k for k in CATALOG if k.startswith("Eat some chametz"))
PEACEMAKER_KEY = next(k for k in CATALOG if k.startswith("Be a peacemaker"))
AMALEK_KEY = next(k for k in CATALOG if k.startswith("Learn about the mitzvah to remember what Amalek"))
TZEDAKAH_KEY = next(k for k in CATALOG if k.startswith("Make a difference: Give Tzedakah"))
BONEH_KEY = next(k for k in CATALOG if k.startswith("Learn about the Melacha of Boneh"))

BATCH28_RU: dict[str, str] = {
    BAT_MITZVAH_PREFIX_KEY: f"bat mitzvah — {BAT_MITZVAH_RU}",
    CHAROSET_PREFIX_KEY: f"charoset — {CHAROSET_RU}",
    LCHATCHILA_PREFIX_KEY: f"l'chatchila — {LCHATCHILA_RU}",
    MACHLOKET_KEY: _MACHLOKET_RU,
    MACHLOKET_PREFIX_KEY: f"machloket — {_MACHLOKET_RU}",
    TZITZIT_EXP_KEY: (
        "Переживи мицву цицит! Мужчинам: возьми цицит для ношения "
        "или новый комплект к праздникам! Женщинам: подумай о цицит "
        "в подарок близким мужчинам или мальчикам 🎁. "
        "Знаешь? Числовое значение «цицит» — 600, а с 8 нитями и 5 узлами "
        "получается 613 — общее число мицвот!"
    ),
    TZITZIT_WRAP_KEY: (
        "Окунись в мицву цицит! 🧵 Знаешь? Эти нити — не просто "
        "оригинальное еврейское модное заявление — они напоминают о всех 613 мицвот! "
        "Каждый раз, когда ты их носишь, ты буквально «обёрнут» в святость!"
    ),
    CONSTANT_6_KEY: (
        "Освой постоянную мицву №6: не поддавайся глазам и сердцу! 👀❤️ "
        "Эта мицва (Бамидбар 15:39) учит не следовать за глазами и сердцем "
        "в сторону греха. Глаза и сердце — ворота души; охраняй их! "
        "Источник: Бамидбар 15:39; Рамбам, Сефер ха-Мицвот, негативная заповедь 265."
    ),
    EAT_CHAMETZ_KEY: (
        "Съешь немного хамеца (кислого хлеба) с намерением подготовить дом к Песаху! 🍞 "
        "Это последний шанс до запрета — и акт подготовки. "
        "Сделай это осознанно и с благодарностью."
    ),
    PEACEMAKER_KEY: (
        "Будь миротворцем: установи мир между людьми! 🕊️ "
        "Мицва родеф шалом — одна из величайших! "
        "Талмуд (Санхедрин 6б) хвалит тех, кто мирит; Хиллель учил быть учениками Аарона — "
        "любить мир и гнаться за ним. Даже одно примирение может изменить жизни. "
        "Позвони, напиши или помоги двум сторонам найти общий язык!"
    ),
    AMALEK_KEY: (
        "Узнай о мицве помнить, что Амалек сделал с нами! 🗡️ "
        "Найди момент поразмыслить об этом важном уроке истории — "
        "ненависть к слабым и нападение на уязвимых. "
        "Память об Амалеке учит бороться со злом и защищать уязвимых."
    ),
    TZEDAKAH_KEY: (
        "Измени мир к лучшему: дай цдаку — даже маленькую монету! 💸 "
        "Тора учит: каждый акт цдаки открывает каналы благословения. "
        "Даже копейка, дана с намерением, имеет силу. "
        "Сегодня: положи монету в копилку цдаки или переведи символическую сумму!"
    ),
    BONEH_KEY: BONEH_RU,
}
