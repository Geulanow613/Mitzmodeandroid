"""Human-quality fixes batch 29 — shechitah, Purim/Oneg/Plag glossary, mitzvah UI strings."""

from __future__ import annotations

import json
from pathlib import Path

from _glossary_batch28 import _MACHLOKET_RU
from _melacha_ru_boneh import BONEH_RU

ROOT = Path(__file__).resolve().parents[1]
CATALOG = json.loads((ROOT / "data" / "translation-catalog" / "strings.json").read_text(encoding="utf-8"))[
    "strings"
]

_MACHLOKET_BODY_RU = _MACHLOKET_RU.removeprefix("Machloket — ")

_SHECHITAH_RU = (
    "Узнай о шехите — кошерной бойне — и пяти требованиях, которые должны быть "
    "выполнены, иначе мясо запрещено! 🔪 Пять факторов, делающих бойню недействительной, "
    "запоминают по акрониму ShaCHaT NaDaH: (1) Shehiyah — пауза в середине разреза. "
    "Рез должен быть одним непрерывным плавным движением. Остановка посередине, "
    "даже краткая, может сделать мясо treif. (2) Chaladah — «зарывание». "
    "Нож не может заходить под трахею в горло; разрез должен идти чисто по поверхности, "
    "нож полностью виден. Нельзя вводить его под кожу или между трахеей и пищеводом. "
    "(3) Drasha — слишком сильное нажатие. Нож должен скользить, а не давить. "
    "Если shochet толкает, а не ведёт лезвие, — недействительно. "
    "(4) Hagramah — разрез не в том месте. Есть точная область горла, "
    "где разрез действителен — слишком высоко или низко недопустимо. "
    "(5) Ikur — вырывание. Трахея или пищевод нельзя вырывать до завершения разреза. "
    "А сам нож? Он должен быть бритвенно острым с идеально гладким лезвием — без зазубрин. "
    "Настолько острым, что посрамит поварской нож или хирургический скальпель! "
    "У тех часто есть микроскопическая «пиллность» — она помогает кухонному ножу "
    "«цепляться» за кожу томата. Для chalaf эта шероховатость строго запрещена. "
    "Лезвие должно быть отполировано до зеркальной, без трения поверхности. "
    "Даже крошечная заусенца или неровность сорвёт ткань, а не разрежет чисто — "
    "мясо моментально станет не-кошерным. Кошерное мясо — серьёзный «steaks»-бизнес! 😂"
)

_ONEG_RU = (
    "Радость Шаббата — хорошая еда, отдых, изучение Торы, пение "
    "и время с семьёй или гостями. Это позитивная мицва, не только воздержание от melаха. "
    "Talmud критикует постящихся или лишающих себя на Шаббате без нужды. "
    "Планирование трапез и атмосферы до Шаббата помогает oneg без стресса в последний момент."
)

_PLAG_RU = (
    "Плаг ха-минха — полтора галахических часа (один с четвертью) до наступления темноты — "
    "используется для ранней минхи, раннего входа в Шаббат в некоторых общинах "
    "и для определённых времён Песаха и Хануки. "
    "Это также самое раннее время зажигания шаббатных свечей; "
    "зажигание до плаг аннулирует мицву и делает благословение напрасным. "
    "Это не то же самое, что закат; сверься с календарём."
)

_PURIM_RU = (
    "Purim отмечает спасение в Шушане — Megillah, matanot la'evyonim, "
    "mishloach manot и seudah. Костюмы и радость — часть мицвот дня. "
    "В диаспоре Purim — 14 Adar, в Иерусалим — Shushan Purim 15 Adar."
)

SHECHITAH_KEY = next(k for k in CATALOG if k.startswith("Learn about shechitah"))
ONEG_KEY = next(k for k in CATALOG if k.startswith("Oneg Shabbat is delighting"))
ONEG_PREFIX_KEY = next(k for k in CATALOG if k.startswith("Oneg Shabbat — Oneg Shabbat"))
PLAG_KEY = next(k for k in CATALOG if k.startswith("Plag hamincha is one and a quarter"))
PLAG_PREFIX_KEY = next(k for k in CATALOG if k.startswith("Plag HaMincha — Plag hamincha"))
PURIM_KEY = next(k for k in CATALOG if k.startswith("Purim celebrates salvation"))
PURIM_PREFIX_KEY = next(k for k in CATALOG if k.startswith("Purim — Purim celebrates"))
HOLIDAY_KEY = next(k for k in CATALOG if k.startswith("Today is $holidayName (Yom Tov"))
MITZVAH_ME_1 = next(k for k in CATALOG if k == 'Tap the\n"Mitzvah Me" button for a mitzvah!')
MITZVAH_ME_2 = next(k for k in CATALOG if k == 'Tap the "Mitzvah Me"\nbutton for a mitzvah!')
MITZVAH_ME_3 = next(k for k in CATALOG if k == "Tap the Mitzvah Me button for a mitzvah!")
HONOR_PARENTS_KEY = next(k for k in CATALOG if k.startswith("honoring your parents — Kibud"))
SEUDAT_KEY = next(k for k in CATALOG if k.startswith("seudat mitzvah — festive"))
TZEDAKAH_KEY = next(k for k in CATALOG if k.startswith("Tzedakah is usually translated"))
MACHLOKET_KEY = next(k for k in CATALOG if k.startswith("Machloket is respectful"))
MACHLOKET_PREFIX_KEY = next(k for k in CATALOG if k.startswith("machloket — Machloket"))
BONEH_KEY = next(k for k in CATALOG if k.startswith("Learn about the Melacha of Boneh"))
SPREAD_SMILES_KEY = next(k for k in CATALOG if k.startswith("Spread some smiles"))
CHUMASH_RANDOM_KEY = next(k for k in CATALOG if k.startswith("Get a Chumash and open"))
SHOFAR_KEY = next(k for k in CATALOG if k.startswith("Get ready for a wake-up call"))

_MITZVAH_ME_RU = "Нажми кнопку «Mitzvah Me» для мицвы!"

BONEH_RU_FIXED = BONEH_RU.replace("мелахе", "мелахе").replace("tent", "навеса")

BATCH29_RU: dict[str, str] = {
    SHECHITAH_KEY: _SHECHITAH_RU,
    ONEG_KEY: _ONEG_RU,
    ONEG_PREFIX_KEY: f"Oneg Shabbat — {_ONEG_RU}",
    PLAG_KEY: _PLAG_RU,
    PLAG_PREFIX_KEY: f"Plag HaMincha — {_PLAG_RU}",
    PURIM_KEY: _PURIM_RU,
    PURIM_PREFIX_KEY: f"Purim — {_PURIM_RU}",
    HOLIDAY_KEY: (
        "Сегодня $holidayName (Йом-Тов — праздничный день). "
        "Пожалуйста, убери устройство и храни день святым."
    ),
    MITZVAH_ME_1: _MITZVAH_ME_RU,
    MITZVAH_ME_2: _MITZVAH_ME_RU,
    MITZVAH_ME_3: _MITZVAH_ME_RU,
    HONOR_PARENTS_KEY: (
        "Почитание родителей — Kibud Av V'Eim — мицва почитать, уважать и заботиться о родителях."
    ),
    SEUDAT_KEY: (
        "seudat mitzvah — праздничная трапеза, связанная с мицвой "
        "(например, brit, siyum, свадьба)."
    ),
    TZEDAKAH_KEY: (
        "Слово «цдака» часто переводят как «благотворительность», "
        "но корень означает справедливость — делиться тем, что Б-г доверил тебе, "
        "с нуждающимися. Мудрецы считают её одной из опор, на которых стоит мир; "
        "даже маленькая монета, данная от всего сердца, — великая мицва."
    ),
    MACHLOKET_KEY: _MACHLOKET_BODY_RU,
    MACHLOKET_PREFIX_KEY: f"machloket — {_MACHLOKET_BODY_RU}",
    BONEH_KEY: BONEH_RU_FIXED,
    SPREAD_SMILES_KEY: (
        "Разнеси улыбки: скажи доброе слово! 🗣️👂 "
        "Мицва тёплого приветствия и доброты (хахнасат орхим — приём гостей "
        "и общие акты хесед) — ежедневная возможность осветлить чей-то день! "
        "Искренняя улыбка, дружелюбное «шалом» или ободряющее слово могут изменить "
        "чужой день. Мудрецы учат: даже маленький акт доброты имеет глубокую духовную ценность. "
        "Кому в твоей жизни сегодня нужна поддержка? Сделай своей миссией поделиться позитивом!"
    ),
    CHUMASH_RANDOM_KEY: (
        "Возьмите Chumash и откройте на случайной странице и стихе — предложено HB. "
        "У многих евреев есть дорогая традиция открывать Тору для вдохновения "
        "или направления в важные моменты жизни. "
        "Видите ли вы в стихе личное послание или просто искру для размышления — "
        "сам акт связывает вас с Торой, и мудрецы учат, что даже изучение одного стиха — "
        "отдельная мицва. Прочитайте медленно, подумайте, что это может значить для вас сейчас, "
        "и дайте словам осесть."
    ),
    SHOFAR_KEY: (
        "Приготовьтесь к пробуждению! 🎺 Исследуйте потрясающую мицву трубления "
        "в shofar в Rosh Hashanah! Речь не просто о шуме — это мощный духовный "
        "будильник для души, призывающий к самоанализу и teshuvah (покаянию). "
        "Звуки shofara глубоко значимы: Tekiah — длинный непрерывный звук, "
        "символ ясности, уверенности и единства с Б-гом. "
        "Shevarim — три коротких прерывистых звука, как вздох или плач, "
        "наше сокрушение и искреннее сожаление. "
        "Teruah — серия из минимум девяти быстрых отрывистых звуков, как рыдания, "
        "отчаянная мольба о милости и готовность к духовному пробуждению. "
        "Последовательность звуков (Tekiah-Shevarim-Teruah-Tekiah и т. д.) важна. "
        "Представьте эти древние звуки в День Суда, пробуждающие душу вернуться к Творцу. "
        "Источник: Bamidbar 29:1; Talmud, Rosh Hashanah 33b; Shulchan Aruch, Orach Chaim 590."
    ),
}
