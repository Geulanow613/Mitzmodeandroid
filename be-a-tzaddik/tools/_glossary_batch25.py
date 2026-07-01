"""Human-quality fixes batch 25 — RU ty glossary + Melacha/Mitzvah-me long explainers."""

from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CATALOG = json.loads((ROOT / "data" / "translation-catalog" / "strings.json").read_text(encoding="utf-8"))[
    "strings"
]

_MAASER_RU = (
    "Маасер кесафим — откладывание около десяти процентов чистого дохода на цдаку "
    "после вычета расходов. Это учит, что доход нам доверен, а не принадлежит нам абсолютно. "
    "Приоритеты: местные нуждающиеся, изучение Торы, Израиль. "
    "Если десять процентов даются с трудом, дай сколько можешь и спроси рава."
)

_MACHATZIT_RU = (
    "Махацит ха-Шекель напоминает о полушекеле, который каждый еврей давал "
    "на содержание Храма в Адаре. Сегодня многие дают цдаку перед Пурим — "
    "часто три монеты, каждая — половина местной единицы. "
    "Это распространённый обычай, не одна из четырёх мицвот Пурима, "
    "но он связывает общину с единым искуплением. "
    "Давай через свою синагогу или надёжную благотворительность."
)

_NIDDAH_RU = (
    "Нидда — состояние ритуального разделения во время и после менструации "
    "до погружения в микве. Муж и жена воздерживаются от физической близости "
    "и определённых нежных контактов. Законы пятен и циклов сложны — "
    "учительница каллы или рав помогает с реальными вопросами. "
    "Нидда — не наказание, а ритм и святость в браке."
)

_NUSACH_ARI_RU = (
    "Нусах Ари — молитвенный обряд, связанный с равом Исааком Лурией (Ари) "
    "и используемый Хабадом и некоторыми другими общинами. "
    "Он сочетает ашкеназские и сефардские элементы. "
    "Сидуры Хабада, такие как Техилат Хашем, печатают этот нусах. "
    "Если ты молишься нусах Ари, последовательно используй этот сидур "
    "для праздничных вставок."
)

_POSITIVE_SPEECH_RU = (
    "Практикуй силу позитивной речи! Возьми на себя обязательство: "
    "никакого лашон хара (ненужной негативной речи о других) "
    "в следующие 20 минут, пока общаешься с друзьями 🤐. "
    "Удивительно: каждый раз, когда ты сдерживаешься от негатива, "
    "это отдельная мицва! За 20 минут набирается много мицвот!"
)

_CHOFETZ_BODY_RU = (
    "Рав Исраэль Меир Каган (Хафец Хаим) написал основное руководство "
    "по охране речи и Мишна Берура по ежедневной галахе. "
    "Его труды учат, что сплетни разрушают общины и что обычные евреи могут "
    "освоить законы Шаббата, кашрута и молитвы. "
    "Изучение его законов речи преобразует отношения сильнее любой лекции о морали."
)

_RECHILUT_RU = (
    "Рехилут — перенос слов, которые порождают ненависть, даже если они правдивы. "
    "Пример: рассказать Реувену, что Шимон сказал о нём, чтобы разжечь конфликт. "
    "Лашон хара — говорить о ком-то плохо; рехилут — распространять слова между сторонами. "
    "Хафец Хаим посвящает обширные главы разрешённой речи для защиты "
    "или конструктивной нужды; случайные истории «а ты слышал» почти никогда не дозволены."
)

_SHEKHINAH_RU = (
    "Внутреннее присутствие Б-га, особенно связанное с Храмом, "
    "с Израилем в изгнании и со святостью брака и Шаббата. "
    "В традиции Кидуш левана говорят, что благословение луны — "
    "как приветствие Шхины. Это не отдельное существо; "
    "так мы говорим о близости Б-га."
)

_SHEMA_HAMITAH_RU = (
    "Шма аль ха-мита — вечерняя Шма перед сном — провозглашение веры "
    "и вручение души Б-гу перед сном. Многие добавляют Псалом 91 и другие стихи. "
    "Хамапил произносят, когда действительно ложатся спать. "
    "Женщины обязаны в этой защите по галахе. "
    "Несколько минут спокойной молитвы хорошо завершают день."
)

_SHEMA_RU = (
    "Шма («Слушай, Израиль») провозглашает единство Б-га и нашу обязанность "
    "любить Его всем сердцем, душой и силой. Её читают утром и вечером "
    "с окружающими благословениями. Криат Шма имеет определённые времена — "
    "особенно утренняя Шма до третьего часа дня по галахе. "
    "Это еврейское провозглашение веры, которое дети учат первым."
)

_SUKKOT_RU = (
    "Семь дней жизни в сукке и размахивания четырьмя видами — "
    "радость после Дней трепета. Напоминает об облаках славы в пустыне. "
    "Дождь может освобождать от сидения. Сразу следует Шмини Ацерет — "
    "отдельный праздник близости с Б-гом после публичного фестиваля."
)

_TEHILLIM_RU = (
    "Техиллим (еврейское слово «хвалы») — традиционное еврейское название "
    "Книги Псалмов — библейской коллекции из 150 священных стихов, молитв "
    "и гимнов благодарности, составленных в основном царём Давидом "
    "и широко читаемых для утешения, божественной защиты и духовной связи."
)

_TORAH_RU = (
    "Тора означает «учение» или «наставление». В узком смысле — пять книг Торы: "
    "от Берешит до Дварим на иврите, данные на Синае. "
    "В синагоге Тору читают из рукописного свитка Сефер Тора. "
    "В изучении «Тора» может также означать Мишну, Талмуд, галаху "
    "и другие труды, объясняющие, как жить по воле Б-га."
)

_YICHUD_RU = (
    "Запрет на уединение мужчины и женщины вместе в уединённом месте, "
    "если они не состоят в браке друг с другом и не являются близкими родственниками "
    "по крови (родители, дети, братья и сёстры и т. д.). "
    "Это предотвращает ситуации, которые могут привести к неподобающей близости. "
    "Исключения и практические детали (открытые двери, общие рабочие места, "
    "медицинская помощь) различаются — спроси рава для реальных случаев."
)

_BEDIKAT_RU = (
    "Бедикат хамец — формальный поиск хамеца в ночь перед Песахом, "
    "после наступления темноты, при свете свечи (или фонарика по мнению многих посеким). "
    "Хлеб часто прячут в комнатах, чтобы благословение на поиск не было напрасным "
    "(если ничего не найдено); на следующее утро его уничтожают в биур хамец. "
    "Даже если дом убирали неделями, галаха всё равно требует эту мицву в эту ночь."
)

_BITACHON_RU = (
    "Доверие Б-гу: вера в то, что Он даёт тебе необходимое "
    "и что трудности могут иметь смысл, даже когда ты его не видишь. "
    "Связано с эмуна (верой), но подчёркивает спокойную опору в ежедневных заботах. "
    "И мусар, и хасидут учат практическому битахон."
)

_CHOL_HAMOED_SHORT_RU = (
    "Холь ха-моэд — промежуточные дни между первым и последним днями Песаха или Суккота. "
    "Это всё ещё праздничное время, но правила мелахи мягче, чем в Йом Тов или Шаббат: "
    "разрешены многие виды работы и приготовление пищи, с ограничениями на коммерцию "
    "и некоторые мелахот. Бритьё и стрижка строго запрещены (О.Х. 531), "
    "кроме особых исключений — спроси рава."
)

_MERAKED_RU = (
    "Узнай о мелахе Меракед — просеивание и процеживание в Шаббат! 🫖 "
    "В Мишкане просеивали муку для лехем hа-паним и фильтровали травы. "
    "Запрещён отдельный сосуд (сито, фильтр, дуршлаг). "
    "Мешамер: запрет уровня Торы, если жидкость слишком мутная, чтобы пить без фильтрации; "
    "уже питьевой сок с мякотью — обычно разрешено. "
    "Избегай френч-пресса, просеивания мацы и дуршлага для салата в Шаббат. "
    "Принимай еду такой, какой она приготовлена заранее."
)

_HASHEM_NAMES_RU = (
    "Узнай о высшей политике «обращаться с осторожностью» с именами Хашем! ✍️ "
    "Тора: «Не делай так Хашему, Б-гу твоему» — строгий запрет стирать или уничтожать Его святое Имя. "
    "Галаха выделяет семь особых Имён, которые нельзя стирать. "
    "Ошибка в свитке Торы — весь фрагмент в генизу, не ластик. "
    "Избегай писать Имена на том, что могут выбросить — поэтому пишут «Б-г» с тире "
    "или сокращения вроде «יי». Даже «святые прозвища» требуют достоинства. "
    "Практика осознанности: буквы на странице несут искру Бесконечного!"
)

_MITZVAH_WORD_RU = (
    "Слово «мицва» (מִצְוָה) буквально означает «заповедь». В иудаизме мицвот — 613 заповедей, "
    "данных Б-гом через Тору, плюс дополнительные мицвот, которые мудрецы добавили "
    "по божественному вдохновению.\n\n"
    "Но «мицва» — не только заповедь, а и «связь». Выполняя мицву, ты исполняешь волю Б-га "
    "и соединяешься с Ним. Это как следовать указаниям близкого человека — так ты укрепляешь связь.\n\n"
    "Б-г постоянно ниспосылает чистый небесный свет, который поддерживает мир. "
    "Если ты идёшь против Его воли — это как зонт между тобой и этим светом. "
    "Но через мицвот ты можешь соединиться с небесным добром — самим Б-гом — "
    "и почувствовать небо на земле, в состоянии, которое можно назвать «Mitz Mode». "
    "Может, не сразу — сделай несколько мицвот и посмотри, как себя чувствуешь. Это только начало..."
)

MAASER_KEY = next(k for k in CATALOG if k.startswith("Maaser kesafim is setting"))
MACHATZIT_KEY = next(k for k in CATALOG if k.startswith("Machatzit HaShekel recalls"))
MACHATZIT_PREFIX_KEY = next(k for k in CATALOG if k.startswith("Machatzit HaShekel — Machatzit"))
NIDDAH_KEY = next(k for k in CATALOG if k.startswith("Niddah is the state"))
NIDDAH_PREFIX_KEY = next(k for k in CATALOG if k.startswith("niddah — Niddah is"))
NUSACH_ARI_KEY = next(k for k in CATALOG if k.startswith("Nusach Ari is the prayer"))
NUSACH_ARI_PREFIX_KEY = next(k for k in CATALOG if k.startswith("Nusach Ari — Nusach Ari"))
POSITIVE_SPEECH_KEY = next(k for k in CATALOG if k.startswith("Practice the power of positive speech"))
CHOFETZ_BODY_KEY = next(k for k in CATALOG if k.startswith("Rabbi Yisrael Meir Kagan (Chofetz Chaim) wrote"))
RECHILUT_KEY = next(k for k in CATALOG if k.startswith("Rechilut is carrying tales"))
SHEKHINAH_PREFIX_KEY = next(k for k in CATALOG if k.startswith("Shekhinah — The Shekhinah"))
SHEMA_HAMITAH_KEY = next(k for k in CATALOG if k.startswith("Shema al HaMitah is the bedtime"))
SHEMA_HAMITAH_PREFIX_KEY = next(k for k in CATALOG if k.startswith("Shema al HaMitah — Shema al HaMitah"))
SHEMA_PREFIX_KEY = next(k for k in CATALOG if k.startswith("Shema — The Shema"))
SUKKOT_KEY = next(k for k in CATALOG if k.startswith("Sukkot is seven days"))
SUKKOT_PREFIX_KEY = next(k for k in CATALOG if k.startswith("Sukkot — Sukkot is seven"))
TEHILLIM_KEY = next(k for k in CATALOG if k.startswith("Tehillim (the Hebrew word"))
TORAH_KEY = next(k for k in CATALOG if k.startswith('Torah means "teaching"'))
TORAH_PREFIX_KEY = next(k for k in CATALOG if k.startswith("Torah — Torah means"))
YICHUD_KEY = next(k for k in CATALOG if k.startswith("Yichud is the prohibition"))
YICHUD_PREFIX_KEY = next(k for k in CATALOG if k.startswith("yichud — Yichud is"))
BEDIKAT_PREFIX_KEY = next(k for k in CATALOG if k.startswith("bedikat chametz — Bedikat chametz"))
BITACHON_PREFIX_KEY = next(k for k in CATALOG if k.startswith("bitachon — Bitachon is trust"))
CHOL_HAMOED_SHORT_KEY = next(k for k in CATALOG if k.startswith("Chol HaMoed are the intermediate"))
CHOL_HAMOED_PREFIX_KEY = next(k for k in CATALOG if k.startswith("Chol HaMoed — Chol HaMoed are"))
MERAKED_KEY = next(k for k in CATALOG if k.startswith("Learn about Meraked"))
HASHEM_NAMES_KEY = next(k for k in CATALOG if k.startswith('Learn about the ultimate "handle with care"'))
MITZVAH_WORD_KEY = next(k for k in CATALOG if k.startswith("The word mitzvah"))

BATCH25_RU: dict[str, str] = {
    MAASER_KEY: _MAASER_RU,
    MACHATZIT_KEY: _MACHATZIT_RU,
    MACHATZIT_PREFIX_KEY: f"Machatzit HaShekel — {_MACHATZIT_RU}",
    NIDDAH_KEY: _NIDDAH_RU,
    NIDDAH_PREFIX_KEY: f"niddah — {_NIDDAH_RU}",
    NUSACH_ARI_KEY: _NUSACH_ARI_RU,
    NUSACH_ARI_PREFIX_KEY: f"Nusach Ari — {_NUSACH_ARI_RU}",
    POSITIVE_SPEECH_KEY: _POSITIVE_SPEECH_RU,
    CHOFETZ_BODY_KEY: _CHOFETZ_BODY_RU,
    RECHILUT_KEY: _RECHILUT_RU,
    SHEKHINAH_PREFIX_KEY: f"Shekhinah — {_SHEKHINAH_RU}",
    SHEMA_HAMITAH_KEY: _SHEMA_HAMITAH_RU,
    SHEMA_HAMITAH_PREFIX_KEY: f"Shema al HaMitah — {_SHEMA_HAMITAH_RU}",
    SHEMA_PREFIX_KEY: f"Shema — {_SHEMA_RU}",
    SUKKOT_KEY: _SUKKOT_RU,
    SUKKOT_PREFIX_KEY: f"Sukkot — {_SUKKOT_RU}",
    TEHILLIM_KEY: _TEHILLIM_RU,
    TORAH_KEY: _TORAH_RU,
    TORAH_PREFIX_KEY: f"Torah — {_TORAH_RU}",
    YICHUD_KEY: _YICHUD_RU,
    YICHUD_PREFIX_KEY: f"yichud — {_YICHUD_RU}",
    BEDIKAT_PREFIX_KEY: f"bedikat chametz — {_BEDIKAT_RU}",
    BITACHON_PREFIX_KEY: f"bitachon — {_BITACHON_RU}",
    CHOL_HAMOED_SHORT_KEY: _CHOL_HAMOED_SHORT_RU,
    CHOL_HAMOED_PREFIX_KEY: f"Chol HaMoed — {_CHOL_HAMOED_SHORT_RU}",
    MERAKED_KEY: _MERAKED_RU,
    HASHEM_NAMES_KEY: _HASHEM_NAMES_RU,
    MITZVAH_WORD_KEY: _MITZVAH_WORD_RU,
}
