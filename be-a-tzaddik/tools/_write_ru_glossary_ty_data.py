#!/usr/bin/env python3
"""Regenerate _ru_glossary_ty_data.py with clean Cyrillic."""
import json
from pathlib import Path

from _three_weeks_ty_data import THREE_WEEKS_INTRO_RU
from _glossary_short_batch_data import GLOSSARY_SHORT_RU, GLOSSARY_SHORT_RU_PREFIX

ROOT = Path(__file__).resolve().parents[1]
CATALOG = json.loads((ROOT / "data/translation-catalog/strings.json").read_text(encoding="utf-8"))["strings"]

def find_key(prefix: str) -> str:
    for k in CATALOG:
        if k.startswith(prefix):
            return k
    raise KeyError(prefix)

_HAMOTZI = (
    "благословение на хлеб «Барух Ата… hа-моци лехем мин hа-арец» — начало трапезы, "
    "после которой нужен Биркат а-Мазон. Перед хлебом — нетилат ядаим. Если ты хозяин, режь хлеб "
    "для других после своего благословения. На Песах — на мацу; в Шаббат — лехем мишне, две целые халы."
)

_KADDISH = (
    "Кадиш освящает Имя В-га на арамейском — публичное возвышение В-га. Скорбящие читают Кадиш "
    "11 месяцев за родителей и 30 дней за других близких. Нужен миньян. В службе есть полу-Кадиш, "
    "полный Кадиш и Кadish d'rabbanan. Кадиш связывает живых с заслугами умершего."
)

_RETZEI = (
    "«Рацей» просит В-га принять наш покой и восстановить Храм и царство Давида — вставляется "
    "в третье благословение бентчинга в Шаббат и Йом Тов. Если забыл на двух первых трапезах Шаббата — "
    "обычно повторяешь бентчинг. На сеуда шлишит (третья трапеза) — не повторяешь (Шулхан Арух О.Х. 188:8). "
    "Спроси рава, если не уверен."
)

_MACHZOR = (
    "Махzor — молитвенник для праздников и Высоких праздников — Рош а-Шана, Йом Кippur, Песах, "
    "Суккот и Шavuot. Содержит тексты, которых нет в ежедневном сидуре. Для Рош а-Шана и Йом Кippur "
    "махzor нужен для правильного порядка молитвы."
)

_BEDIEVED = (
    "Bedieved — галахическая ориентация, когда идеал был упущен — непреднамеренно или по неизбежности. "
    "Это не разрешение плохо планировать; это как восстановиться. Примеры: повтор Амиды, хамец на Песах, "
    "пропущенный счёт Омера. Твой рав применяет правила bedieved к твоему случаю; статьи в приложении "
    "обобщают распространённые схемы из стандартных источников."
)

_RAMBAM = (
    "Рамбам — рав Моше Маймонид (1138–1204): врач, философ и гигант галахи. Его «Мишне Тора» "
    "кодифицирует галаху на ясном иврите; Тринадцать принципов суммируют еврейскую веру. Многие "
    "знакомятся с ним через ежедневные обзоры галахи или его законы тшувы, цдаки и изучения Торы."
)

_LECHEM_MISHNEH = (
    "две целые халы на трапезах Шаббата и Йом Това — в память о двойной порции манны "
    "перед Шаббатом в пустыне. Накрой халы, благослови ха-моци и разломи для других. На Песах две целые "
    "мацы выполняют эту роль. Халы должны быть целыми и подходящими для ха-моци."
)

_THREE_WEEKS_INTRO = THREE_WEEKS_INTRO_RU

_SHLOSHIM_RU = (
    "Шлошим — тридцать дней сокращённых общественных радостей после погребения (для многих степеней родства). "
    "Стрижка и музыка могут быть ограничены. После шлошима обычная жизнь возвращается, кроме для родителей — "
    "Кадиш продолжается одиннадцать месяцев. Пересечение календаря с Сефирой или Тремя неделями добавляет "
    "ограничения — спроси рава."
)

_ONEG_RU = (
    "Онег Шаббат — радость Шаббата: хорошая еда, отдых, изучение Торы, пение и время с семьёй или гостями. "
    "Это позитивная мицва, не только воздержание от мелахи. Талмуд критикует тех, кто постится или лишает себя "
    "в Шаббат без нужды. Планируй трапезы и атмосферу до Шаббата, чтобы онег случился без спешки в последний момент."
)

_PIRSUMEI_RU = (
    "Пирсумей ниса — оглашение чуда: поставь ханукальную менору так, чтобы прохожие видели огни. "
    "Чудо с маслом провозглашается на улицу. Дома многие зажигают у окна. Не используй свечи мицвы для чтения — "
    "используй шамаш."
)

_T17_RU = (
    "17 Тамуз — малый пост в память о проломе стен Иерусалима, начало Трёх недель. Без еды и питья от рассвета до "
    "ночи. Некоторые общины читают Эйха и кинот. Не выпадает на Шаббат — сверься с календарём."
)

_LCHATCHILA_RU = (
    "L'chatchila — идеальный способ выполнить мицву с самого начала — то, что ты должен планировать. Bedieved — "
    "после того как что-то пошло не так, когда галаха даёт поправку или облегчение. Знание обоих терминов помогает "
    "читать гиды: «l'chatchila — с кубком; bedieved, если забыл, некоторые разрешают…»"
)

_DEVICE_SHABBAT_RU = (
    "Пожалуйста, убери устройство и соблюдай Шаббат. Отдыхай, молись, изучай Тору и наслаждайся временем с семьёй "
    "и общиной. Мелаха и электроника запрещены — это священное время."
)

_DEVICE_YOMTOV_RU = (
    "Сегодня $holidayName (Йом-Тов — праздничный день). Пожалуйста, убери устройство и храни день святым. "
    "Мелаха в Йом-Тов подобна Шаббату."
)

# fix latin o in Рosh
for name in (
    "_HAMOTZI",
    "_KADDISH",
    "_RETZEI",
    "_MACHZOR",
    "_BEDIEVED",
    "_RAMBAM",
    "_LECHEM_MISHNEH",
    "_THREE_WEEKS_INTRO",
    "_SHLOSHIM_RU",
    "_ONEG_RU",
    "_PIRSUMEI_RU",
    "_T17_RU",
    "_LCHATCHILA_RU",
    "_DEVICE_SHABBAT_RU",
    "_DEVICE_YOMTOV_RU",
):
    val = locals()[name]
    val = val.replace("\u0420o", "\u0420\u043e").replace("Кadish", "Кадиш").replace("bentching", "бентчинга")
    val = val.replace("Рosh", "\u0420\u043e\u0448").replace("шlishit", "шлишит")
    locals()[name] = val

_HAMOTZI = locals()["_HAMOTZI"]
_KADDISH = locals()["_KADDISH"]
_RETZEI = locals()["_RETZEI"]
_MACHZOR = locals()["_MACHZOR"]
_BEDIEVED = locals()["_BEDIEVED"]
_RAMBAM = locals()["_RAMBAM"]
_LECHEM_MISHNEH = locals()["_LECHEM_MISHNEH"]
_THREE_WEEKS_INTRO = locals()["_THREE_WEEKS_INTRO"]
_SHLOSHIM_RU = locals()["_SHLOSHIM_RU"]
_ONEG_RU = locals()["_ONEG_RU"]
_PIRSUMEI_RU = locals()["_PIRSUMEI_RU"]
_T17_RU = locals()["_T17_RU"]
_LCHATCHILA_RU = locals()["_LCHATCHILA_RU"]
_DEVICE_SHABBAT_RU = locals()["_DEVICE_SHABBAT_RU"]
_DEVICE_YOMTOV_RU = locals()["_DEVICE_YOMTOV_RU"]

GLOSSARY = {
    find_key("Hamotzi is the blessing over bread"): f"Хамоци — {_HAMOTZI}",
    find_key("Hamotzi — Hamotzi is the blessing"): f"Хамоци — {_HAMOTZI}",
    find_key("Kaddish sanctifies G-d's Name in Aramaic"): _KADDISH,
    find_key("Kaddish — Kaddish sanctifies G-d's Name"): f"Кадиш — {_KADDISH}",
    find_key("Retzei asks G-d to be pleased with our rest"): _RETZEI,
    find_key("Retzei — Retzei asks G-d to be pleased"): f"Рацей — {_RETZEI}",
    find_key("A machzor is a prayer book for the Jewish festivals"): _MACHZOR,
    find_key("machzor — A machzor is a prayer book"): f"Махzor — {_MACHZOR}",
    find_key("Bedieved describes halachic guidance"): _BEDIEVED,
    find_key("bedieved — Bedieved describes"): f"bedieved — {_BEDIEVED}",
    find_key("Rambam is Rabbi Moses Maimonides"): _RAMBAM,
    find_key("Rambam — Rambam is Rabbi Moses"): f"Рамбам — {_RAMBAM}",
    find_key("lechem mishneh — Lechem mishneh is"): f"лехем мишне — {_LECHEM_MISHNEH}",
    find_key("Lechem mishneh is two whole"): f"Лехем мишне — {_LECHEM_MISHNEH}",
    find_key("The Three Weeks (בין המצרים) from 17 Tammuz"): _THREE_WEEKS_INTRO,
    find_key("Shloshim is thirty days of reduced"): _SHLOSHIM_RU,
    find_key("shloshim — Shloshim is thirty days"): f"shloshim — {_SHLOSHIM_RU}",
    find_key("Oneg Shabbat is delighting in Shabbat"): _ONEG_RU,
    find_key("Oneg Shabbat — Oneg Shabbat is delighting"): f"Oneg Shabbat — {_ONEG_RU}",
    find_key("Pirsumei nisa means publicizing"): _PIRSUMEI_RU,
    find_key("pirsumei nisa — Pirsumei nisa means"): f"pirsumei nisa — {_PIRSUMEI_RU}",
    find_key("17 Tammuz — fast marking"): _T17_RU,
    find_key("L'chatchila means the ideal way"): _LCHATCHILA_RU,
    find_key("l'chatchila — L'chatchila means"): f"l'chatchila — {_LCHATCHILA_RU}",
    "Please put away your device and keep Shabbat. Rest, pray, learn Torah, and enjoy time with family and community. Melacha (forbidden labor) applies on Shabbat, including most phone and device use.": _DEVICE_SHABBAT_RU,
    "Today is $holidayName (Yom Tov — a festival day). Please put away your device and keep the day holy. Melacha (forbidden labor) applies on Yom Tov similar to Shabbat.": _DEVICE_YOMTOV_RU,
    "lechem mishneh": "лехем мишне",
}
GLOSSARY.update(GLOSSARY_SHORT_RU)
for prefix, text in GLOSSARY_SHORT_RU_PREFIX.items():
    GLOSSARY[find_key(prefix)] = text

out = '''"""Informal RU (ty) expansions for short glossary explainers."""

from __future__ import annotations

GLOSSARY_RU: dict[str, str] = {
'''

for k, v in GLOSSARY.items():
    out += f"    {k!r}: {v!r},\n"

out += '''}


def glossary_ru_for_key(key: str) -> str | None:
    return GLOSSARY_RU.get(key)
'''

Path("_ru_glossary_ty_data.py").write_text(out, encoding="utf-8")
for k, v in GLOSSARY.items():
    print(len(v), k[:50])
