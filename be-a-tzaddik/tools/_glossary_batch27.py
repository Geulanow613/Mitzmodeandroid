"""Human-quality fixes batch 27 — RU ty (tap hints, mitzvah-me stubs, Yom HaShoah)."""

from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CATALOG = json.loads((ROOT / "data" / "translation-catalog" / "strings.json").read_text(encoding="utf-8"))[
    "strings"
]

_YOM_HASHOAH_RU = (
    "Йом ха-Шоа ве-ха-Гевура (27 Нисан) — национальный день памяти шести миллионов евреев, "
    "убитых в Холокосте. Установлен Кнессетом в 1953 году.\n\n"
    "Это не Йом Тов — мелача полностью разрешена. Это гражданская национальная память, "
    "а не галахический пост или молитвенный день.\n\n"
    "Корректировка даты: если 27 Нисан выпадает на пятницу, день переносят на четверг (26 Нисан); "
    "если на воскресенье — на понедельник (28 Нисан), чтобы не нарушать Шаббат.\n\n"
    "Обычаи по общинам:\n\n"
    "В Израиле: в 10:00 звучит двухминутная сирена; большинство замирают в тишине. "
    "Церемонии памяти — в Яд Вашем и по всей стране.\n\n"
    "Молитвы: обычный будний давенинг — Йом ха-Шоа не добавляет и не убирает вставок в сиддуре. "
    "Это гражданский мемориал Кнессета, не раввинский литургический день; "
    "религиозные сионисты / дати леуми не опускают Тачанун специально из-за Йом ха-Шоа. "
    "(27 Нисан — в Нисане; Тачанун не говорят весь месяц Нисан по Шульхан Арух ОХ 429:2 — "
    "это радостный месяц, отдельно от этого дня.) "
    "В некоторых общинах проводят мемориальные уроки или церемонии.\n\n"
    "Многие не считают эту дату религиозным памятным днём, предпочитая 10 Тевет "
    "(Главный раббинат в 1949 году назначил его Йом Кадиш хаклали для тех, "
    "чья дата смерти неизвестна) или Тиша бе-Ав как день скорби по всем еврейским трагедиям. "
    "Это вопрос минхага и общинного руководства.\n\n"
    "Хабад: официального общинного обычая нет, хотя память о кедошим (святых мучениках) почитается."
)

_HELP_PRAYER_RU = (
    "Помогай другим через молитву! Подумай, кому нужна парнаса (пропитание), "
    "и найди время помолиться за его успех 🙏. Каждое собеседование, сделка или возможность "
    "могут зависеть от твоей заботливой молитвы. Когда мы поддерживаем друг друга так, "
    "укрепляем связи между всеми евреями."
)

_SHARE_TORAH_RU = (
    "Поделись мудростью Торы! Отправь кому-нибудь вдохновляющую мысль из Торы — "
    "вот готовая: менора в Храме зажигали днём 🕯️, учась нести свет, даже когда и так ярко "
    "(Числа 8:2). Мы всегда можем добавить света в мир, даже когда всё кажется хорошим!"
)

_HAKARAT_RU = (
    "Практикуй хакарат ха-тов (признание добра)! Отправь искреннее сообщение с благодарностью 💌. "
    "Вчера или много лет назад — это мицва, укрепляющая отношения."
)

_HEART_TO_HEART_RU = (
    "Поговори с Б-гом по душам! Потрать 5 минут и поговори с Ним, как с близким другом 💝. "
    "Поделись надеждами, заботами или просто расскажи о своём дне. "
    "Формальные молитвы не нужны — только искренний разговор! "
    "Мудрецы учат: некоторые ангелы на небесах ждут тысячи лет своей очереди воспеть хвалу Б-гу. "
    "А у нас есть потрясающий дар — говорить с Ним когда угодно! 🙏"
)

_MITZVAH_IDEA_RU = "Есть идея мицвы? Поделись с другими пользователями!"

_GUARD_SPEECH_RU = (
    "Береги речь о Б-ге! 🗣️ Рамбам в Хильхот Аводат Кохавим учит быть предельно осторожным: "
    "не говорить о Б-ге неуважительно и не отрицать Его. "
    "Запрещены даже случайные клятвы и лишние благословения. "
    "Каждое слово о Б-ге — с благоговением. "
    "Задание на сегодня: говори о святом с особой заботой и уважением."
)

_PROTECT_SPIRITUAL_RU = (
    "Защити других от духовного вреда! 🛡️ Тора возлагает важную обязанность: "
    "не подталкивай никого к идолопоклонству или ложным убеждениям. "
    "Как ты не дал бы вредного телесного совета, так осторожен будь и с духовным! "
    "Рамбам объясняет: ввести в духовное заблуждение хуже физического вреда. "
    "Задание на сегодня: поделись с другими чем-то духовно возвышающим."
)

_TORAH_SCHOLARS_RU = (
    "Уважение к мудрецам и старцам! 🙌 «Перед седой встань» (Ваикра 19:32) — "
    "две мицвы: к пожилым (70+) и к учёным Торы. "
    "Вставай (или жестом), когда они подходят примерно на два метра; "
    "перед гадолом — при входе в комнату, не садись, пока он не сядет. "
    "Не унижай учёного — Талмуд сравнивает это с разрушением Храма. "
    "Почтение к Торе начинается с почтения к тем, кто её несёт. "
    "Практикуй уважение уже сегодня."
)

_SPREAD_HEALING_RU = (
    "Разнеси исцеляющую энергию! Отправь искреннее сообщение тому, кто плохо себя чувствует 💌. "
    "Простуда или что-то серьёзнее — твои тёплые слова поднимут настроение! "
    "Талмуд учит: посещение больного снимает одну шестидесятую болезни."
)

_EMUNAH_RU = (
    "Практикуй эмуну (веру)! Сделай глубокий вдох и помни: всё в жизни устроено Б-гом с точностью 🎯. "
    "Каждое испытание — для роста, каждая радость — дар свыше. "
    "Укрепляя доверие к плану Б-га, находишь покой и смысл."
)

_FORGIVENESS_RU = (
    "Практикуй прощение! Отпусти обиду на другого еврея 💝. "
    "Прощая других, мы получаем прощение от Б-га! "
    "Прощение не значит забыть — это выбор мира над болью. Вот настоящая духовная сила! ✡"
)

_GRATITUDE_RU = (
    "Практикуй благодарность! Найди момент поблагодарить Б-га за что-то хорошее — "
    "большое или малое 🙌. Здоровье, семья или утренний кофе! "
    "Мудрецы учат: когда Б-г видит нашу благодарность, Он принимает и другие молитвы вместе с ней!"
)

_JUDGING_RU = (
    "Практикуй судить благосклонно! Заметил, что кто-то делает что-то сомнительное? "
    "Вот шанс стать духовным героем 🦸‍♂️! Тора учит давать другим пользу сомнения. "
    "Может, тяжёлый день, скрытая беда или просто ошибка. "
    "Когда судим других по-доброму, Б-г судит нас так же! ⚖️"
)

_REVENGE_RU = (
    "Практикуй отпускать месть! Тянет отомстить? Вместо этого сделай что-то удивительное. "
    "Тора учит: отказ от мести — не только мир, но и заповедь Б-га! "
    "Ты почувствуешь себя легче без этого груза 🪶"
)

_MINDFUL_GRATITUDE_RU = (
    "Практикуй осознанную благодарность! Оцени дар зрения 👀. "
    "Посмотри на цвета, формы и красоту мира. "
    "От улыбок близких до чтения Торы и заката — глаза дарят столько чудесного. "
    "Поблагодари Б-га за эту способность и скажи, что ценишь её! 🌈"
)

_TEKIAH_BODY_RU = (
    "Текия — длинный ровный звук шофара. Последовательность Рош ха-Шана сочетает текию "
    "с шеварим (прерывистым) и труа (дрожащим) по минхагу. "
    "Сто звуков — обычай ашкеназов. Потренируйся в шуле заранее, "
    "чтобы день Праздника не был первым разом, когда ты слышишь шофар."
)

YOM_HASHOAH_KEY = next(k for k in CATALOG if k.startswith("Yom HaShoah V'HaGevurah"))
HELP_PRAYER_KEY = next(k for k in CATALOG if k.startswith("Help others through prayer"))
SHARE_TORAH_KEY = next(k for k in CATALOG if k.startswith("Share some Torah wisdom"))
HAKARAT_KEY = next(k for k in CATALOG if k.startswith("Practice Hakarat HaTov"))
HEART_KEY = next(k for k in CATALOG if k.startswith("Have a heart-to-heart with G-d"))
MITZVAH_IDEA_KEY = next(k for k in CATALOG if k.startswith("Have a great mitzvah idea"))
GUARD_SPEECH_KEY = next(k for k in CATALOG if k.startswith("Guard your speech about G-d"))
PROTECT_SPIRITUAL_KEY = next(k for k in CATALOG if k.startswith("Protect others from spiritual harm"))
TORAH_SCHOLARS_KEY = next(k for k in CATALOG if k.startswith("Learn about the Torah's obligations of respect"))
SPREAD_HEALING_KEY = next(k for k in CATALOG if k.startswith("Spread healing energy"))
EMUNAH_KEY = next(k for k in CATALOG if k.startswith("Practice Emunah (faith)"))
FORGIVENESS_KEY = next(k for k in CATALOG if k.startswith("Practice forgiveness!"))
GRATITUDE_KEY = next(k for k in CATALOG if k.startswith("Practice gratitude!"))
JUDGING_KEY = next(k for k in CATALOG if k.startswith("Practice judging favorably"))
REVENGE_KEY = next(k for k in CATALOG if k.startswith("Practice letting go of revenge"))
MINDFUL_GRATITUDE_KEY = next(k for k in CATALOG if k.startswith("Practice mindful gratitude"))
TEKIAH_PREFIX_KEY = next(k for k in CATALOG if k.startswith("tekiah — Tekiah is"))

AFTER_SUNSET_TAP_KEY = "Available after sunset · tap to read"
AT_NIGHT_TAP_KEY = "Available at night · tap to read"
DURING_DAY_TAP_KEY = "Available during the day · tap to read"
FROM_MIDDAY_TAP_KEY = "Available from midday on · tap to read"
COMING_UP_TAP_KEY = "Coming up later · tap to read"
ONLY_AFTER_SUNSET_TAP_KEY = "Only available after sunset · tap to read"
ONLY_AT_NIGHT_TAP_KEY = "Only available at night · tap to read"
ONLY_DURING_DAY_TAP_KEY = "Only available during the day · tap to read"
ONLY_FROM_MIDDAY_TAP_KEY = "Only available from midday on · tap to read"
TAP_TO_READ_KEY = "tap to read"
KASHRUT_TIMER_KEY = "Kashrut timer active — tap to view"

BATCH27_RU: dict[str, str] = {
    YOM_HASHOAH_KEY: _YOM_HASHOAH_RU,
    HELP_PRAYER_KEY: _HELP_PRAYER_RU,
    SHARE_TORAH_KEY: _SHARE_TORAH_RU,
    HAKARAT_KEY: _HAKARAT_RU,
    HEART_KEY: _HEART_TO_HEART_RU,
    MITZVAH_IDEA_KEY: _MITZVAH_IDEA_RU,
    GUARD_SPEECH_KEY: _GUARD_SPEECH_RU,
    PROTECT_SPIRITUAL_KEY: _PROTECT_SPIRITUAL_RU,
    TORAH_SCHOLARS_KEY: _TORAH_SCHOLARS_RU,
    SPREAD_HEALING_KEY: _SPREAD_HEALING_RU,
    EMUNAH_KEY: _EMUNAH_RU,
    FORGIVENESS_KEY: _FORGIVENESS_RU,
    GRATITUDE_KEY: _GRATITUDE_RU,
    JUDGING_KEY: _JUDGING_RU,
    REVENGE_KEY: _REVENGE_RU,
    MINDFUL_GRATITUDE_KEY: _MINDFUL_GRATITUDE_RU,
    TEKIAH_PREFIX_KEY: f"tekiah — {_TEKIAH_BODY_RU}",
    AFTER_SUNSET_TAP_KEY: "Доступно после заката · нажми",
    AT_NIGHT_TAP_KEY: "Доступно ночью · нажми",
    DURING_DAY_TAP_KEY: "Доступно днём · нажми для подробностей",
    FROM_MIDDAY_TAP_KEY: "Доступно с полудня · нажми",
    COMING_UP_TAP_KEY: "Скоро · нажми",
    ONLY_AFTER_SUNSET_TAP_KEY: "Только после заката · нажми",
    ONLY_AT_NIGHT_TAP_KEY: "Только ночью · нажми",
    ONLY_DURING_DAY_TAP_KEY: "Только днём · нажми для подробностей",
    ONLY_FROM_MIDDAY_TAP_KEY: "Только с полудня · нажми",
    TAP_TO_READ_KEY: "нажми для чтения",
    KASHRUT_TIMER_KEY: "Таймер кашрута активен · нажми для просмотра",
}

# Shared glossary bodies (imported by batch 28 / 30).
BAT_MITZVAH_RU = (
    "когда девочке исполняется двенадцать лет и один день, она становится обязанной в мицвот, "
    "не зависящих от времени Храма — стандартный возраст обязанности в традиционной галахе. "
    "Обычаи празднования различаются — речи, учебные проекты, семейная трапеза. "
    "Мицвот женщин включают шаббатные свечи, кашрут, цдаку и изучение Торы — "
    "детали по семье и раву."
)

CHAROSET_RU = (
    "сладкая паста (яблоки, вино, орехи и т.д.), напоминающая раствор между кирпичами. "
    "Её макают с марором для кореха. Рецепты различаются по семьям — ашкеназский, "
    "сефардский и персидский харосет выполняют мицву при правильном намерении. "
    "На тарелке Седера это контраст горечи и сладости."
)

LCHATCHILA_RU = (
    "L'chatchila — идеальный способ выполнить мицву с самого начала — то, что ты должен планировать. "
    "Bedieved — после того как что-то пошло не так, когда галаха даёт поправку или облегчение. "
    "Знание обоих терминов помогает читать гиды: «l'chatchila — с кубком; bedieved, если забыл, "
    "некоторые разрешают…»"
)
