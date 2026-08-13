#!/usr/bin/env python3
"""Human translations for Kiddush Levana waitLine + Omer explainer template args."""
from __future__ import annotations

import json
import re
from pathlib import Path

from _kiddush_levana_wait_data import (
    KIDDUSH_LEVANA_WAIT_ASHKENAZ_CHABAD,
    KIDDUSH_LEVANA_WAIT_EDOT,
    KIDDUSH_LEVANA_WAIT_SEFARD,
)
from _omer_explainer_arg_data import (
    NUSACH_WHEN,
    OMER_NEXT_NIGHT_LINE,
    OMER_SPEECH_PHRASES,
    OMER_TIME_PART,
    OMER_TODAY_SUMMARIES,
    omer_day_summary_he,
)
from _taanit_template_data import TAANIT_BECHOR_BASE, TAANIT_BECHOR_TEMPLATE
from _taanit_bechor_ty_data import TAANIT_BECHOR_FR, TAANIT_BECHOR_RU
from _yaaleh_es_fr_data import YAALEH_TEMPLATE_ES, YAALEH_TEMPLATE_FR
from _yaaleh_ru_data import YAALEH_TEMPLATE_RU
from _yaaleh_template_data import (
    YAALEH_FORGOT_MINCHA,
    YAALEH_FORGOT_SHACHARIT,
    FORGOT_MAARIV as YAALEH_FORGOT_MAARIV,
    YAALEH_FULL_EXPLAINERS,
    YAALEH_MINCHA_FEMALE_TEMPLATE,
    YAALEH_MINCHA_TEMPLATE,
    YAALEH_MAARIV_FEMALE_TEMPLATE,
    YAALEH_MAARIV_TEMPLATE,
    YAALEH_SHACHARIT_FEMALE_TEMPLATE,
    YAALEH_SHACHARIT_TEMPLATE,
)

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / "data" / "translation-catalog" / "human" / "explainer_template_args.json"
BUNDLED = ROOT / "data" / "bundled-translations"

COUNT_PREFIX = {
    "es": "Contar el Omer — ",
    "fr": "Compter l'Omer — ",
    "ru": "Счёт омера — ",
}

KIDDUSH_FIXES: dict[str, dict[str, str]] = {
    KIDDUSH_LEVANA_WAIT_SEFARD: {
        "he": "אומרים פעם בחודש כשהירח נראה, בדרך כלל מהלילה השביעי של החודש העברי (שו\"ע או\"ח תרכ\"ו:ד; פניני הלכה 05-01-18).",
        "es": "Se recita una vez al mes cuando la luna es visible, por lo general desde la 7.ª noche del mes hebreo (Shuljan Aruj O.C. 426:4; Peninei Halakha 05-01-18).",
        "fr": "Récitée une fois par mois lorsque la lune est visible, en général à partir de la 7e nuit du mois hébraïque (Shoulhan 'Aroukh O.C. 426:4 ; Peninei Halakha 05-01-18).",
        "ru": "Произносится раз в месяц, когда луна видна, обычно с 7-й ночи еврейского месяца (Shulchan Arukh O.C. 426:4; Peninei Halakha 05-01-18).",
    },
    KIDDUSH_LEVANA_WAIT_EDOT: {
        "he": "אומרים פעם בחודש כשהירח נראה. רוב הספרדים ממתינים עד יום ז' בחודש (שו\"ע או\"ח תרכ\"ו:ד). קהילות מרוקאיות וצפון-אפריקאיות אחרות מתחילות אחרי 3 ימים (פניני הלכה 05-01-18) — לפי הקהילה.",
        "es": "Se recita una vez al mes cuando la luna es visible. La mayoría de sefardíes esperan hasta el día 7 del mes (Shuljan Aruj O.C. 426:4). Comunidades marroquíes y otras del norte de África comienzan tras 3 días (Peninei Halakha 05-01-18) — siga su comunidad.",
        "fr": "Récitée une fois par mois lorsque la lune est visible. La majorité des Séfarades attendent le 7e du mois (Shoulhan 'Aroukh O.C. 426:4). Les kehillot marocaines et d'Afrique du Nord commencent parfois après 3 jours (Peninei Halakha 05-01-18) — suivez votre communauté.",
        "ru": "Произносится раз в месяц, когда луна видна. Большинство сефардов ждут до 7-го числа месяца (Shulchan Arukh O.C. 426:4). Марокканские и некоторые другие сефардские общины начинают после 3 дней (Peninei Halakha 05-01-18) — следуйте своей kehilla.",
    },
    KIDDUSH_LEVANA_WAIT_ASHKENAZ_CHABAD: {
        "he": "אומרים פעם בחודש כשהירח נראה, בדרך כלל מהלילה השלישי של החודש העברי (מנהג אשכנז / חב\"ד; פניני הלכה 05-01-18).",
        "es": "Se recita una vez al mes cuando la luna es visible, por lo general desde la 3.ª noche del mes hebreo (costumbre ashkenazí / Jabad; Peninei Halakha 05-01-18).",
        "fr": "Récitée une fois par mois lorsque la lune est visible, en général à partir de la 3e nuit du mois hébraïque (minhag ashkénaze / 'Habad ; Peninei Halakha 05-01-18).",
        "ru": "Произносится раз в месяц, когда луна видна, обычно с 3-й ночи еврейского месяца (ашкеназский / хabad minhag; Peninei Halakha 05-01-18).",
    },
}

NUSACH_WHEN_FIXES: dict[str, dict[str, str]] = {
    "Many in Chabad count after Maariv (Tehillat Hashem).": {
        "he": "רבים בחב\"ד סופרים אחרי מעריב (Tehillat Hashem).",
        "es": "Muchos en Jabad cuentan después de Maariv (Tehillat Hashem).",
        "fr": "Beaucoup dans 'Habad comptent après Maariv (Tehillat Hashem).",
        "ru": "Многие в Хабаде считают после Maariv (Tehillat Hashem).",
    },
    "Many Sephardim count after Maariv.": {
        "he": "ספרדים רבים סופרים אחרי מעריב.",
        "es": "Muchos sefardíes cuentan después de Maariv.",
        "fr": "Beaucoup de Séfarades comptent après Maariv.",
        "ru": "Многие сефарды считают после Maariv.",
    },
    "Many Edot HaMizrach kehillot count after Maariv.": {
        "he": "קהילות עדות המזרח רבות סופרות אחרי מעריב.",
        "es": "Muchas kehillot de Edot HaMizrach cuentan después de Maariv.",
        "fr": "De nombreuses kehillot des Edot HaMizra'h comptent après Maariv.",
        "ru": "Многие kehillot Эдот а-Мизрах считают после Maariv.",
    },
    "Many Ashkenazim count after Maariv.": {
        "he": "אשכנזים רבים סופרים אחרי מעריב.",
        "es": "Muchos ashkenazíes cuentan después de Maariv.",
        "fr": "Beaucoup d'Ashkénazes comptent après Maariv.",
        "ru": "Многие ашкеназы считают после Maariv.",
    },
}

OMER_NEXT_NIGHT_FIXES = {
    "he": "\n• בליל $tomorrowNight תספרו $nextDaySummary.",
    "es": "\n• El $tomorrowNight por la noche contará $nextDaySummary.",
    "fr": "\n• $tomorrowNight soir, tu compteras $nextDaySummary.",
    "ru": "\n• $tomorrowNight вечером ты будешь считать $nextDaySummary.",
}

OMER_TIME_PART_FIXES = {
    "he": " ב-$time",
    "es": " a las $time",
    "fr": " à $time",
    "ru": " в $time",
}

TAANIT_BECHOR_KEY = TAANIT_BECHOR_BASE

TAANIT_BECHOR_ES = (
    "Taanit Bechorot (תַּעֲנִית בְּכוֹרוֹת) — ayuno de los primogénitos — se observa el día de Erev Pesaj "
    "por varones primogénitos (y en algunas comunidades también mujeres primogénitas — consulta a tu rav).\n\n"
    "Por qué:\n• Conmemora la plaga de los primogénitos en Egipto, cuando los primogénitos judíos fueron perdonados.\n\n"
    "El ayuno:\n• Cuando Erev Pesaj cae en Shabat (14 Nisan), Taanit Bechorot se traslada al jueves "
    "(12 Nisan) según el Rama y Peninei Halakha — no el viernes ni Shabat. Asiste a un siyum ese día si es tu minhag.\n"
    "• Muchos primogénitos evitan el ayuno asistiendo a un siyum (finalización de un tratado del Talmud u obra similar) "
    "seguido de una seudat mitzvá (comida festiva). Para quedar exento no basta escuchar el siyum: hay que participar "
    "en la seudat mitzvá comiendo al menos un kezayit (aprox. 28 g) de pan o pastel. Irse después del siyum sin comer "
    "no exime — aún debes ayunar.\n• Si ayunas: desde el alba (alot hashachar) hasta la noche completa (tzeit) — "
    "sin comer ni beber entre medias.\n\n"
    "Padre de un hijo primogénito antes del bar mitzvá:\n• Si eres varón con un hijo primogénito menor de bar mitzvá, "
    "el minhag extendido es que tú ayunas o asistes al siyum en su nombre — el niño no observa el ayuno.\n\n"
    "Planifica con anticipación: localiza un siyum comunitario si es tu minhag, o confirma las reglas de ayuno con tu rav."
)


def load_count_omer_tail(lang: str) -> dict[str, str]:
    entries = json.loads((BUNDLED / f"{lang}.json").read_text(encoding="utf-8"))["entries"]
    prefix = "Count the Omer — "
    tail_prefix = COUNT_PREFIX.get(lang, "")
    out: dict[str, str] = {}
    for key, val in entries.items():
        if not key.startswith(prefix):
            continue
        summary_en = key[len(prefix) :]
        if tail_prefix and val.startswith(tail_prefix):
            out[summary_en] = val[len(tail_prefix) :]
        elif lang == "he" and val.startswith("ספירת העומר — "):
            day_m = re.match(r"Count the Omer — (\d+)", key)
            if day_m:
                day = int(day_m.group(1))
                out[summary_en] = omer_day_summary_he(day, "לעומר")
    return out


def ru_days(n: int) -> str:
    if n % 10 == 1 and n % 100 != 11:
        return f"{n} день"
    if 2 <= n % 10 <= 4 and (n % 100 < 10 or n % 100 >= 20):
        return f"{n} дня"
    return f"{n} дней"


def ru_weeks(n: int) -> str:
    if n == 1:
        return "1 неделя"
    if 2 <= n <= 4:
        return f"{n} недели"
    return f"{n} недель"


def translate_omer_summary(summary_en: str, lang: str) -> str:
    m = re.fullmatch(r"(\d+) day of the Omer", summary_en)
    if m:
        n = int(m.group(1))
        if lang == "es":
            return f"{n} día del Omer" if n == 1 else f"{n} días del Omer"
        if lang == "fr":
            return f"{n} jour de l'Omer" if n == 1 else f"{n} jours de l'Omer"
        if lang == "ru":
            return f"{ru_days(n)} омера"
        return summary_en
    m = re.fullmatch(r"(\d+) days of the Omer", summary_en)
    if m:
        n = int(m.group(1))
        if lang == "es":
            return f"{n} días del Omer"
        if lang == "fr":
            return f"{n} jours de l'Omer"
        if lang == "ru":
            return f"{ru_days(n)} омера"
        return summary_en
    m = re.fullmatch(r"(\d+) days, which is (.+) of the Omer", summary_en)
    if not m:
        return summary_en
    total = int(m.group(1))
    wd = m.group(2)
    if lang == "es":
        wd_es = (
            wd.replace("1 week and 1 day", "1 semana y 1 día")
            .replace("1 week and", "1 semana y")
            .replace("1 week", "1 semana")
            .replace(" weeks and 1 day", " semanas y 1 día")
            .replace(" weeks and ", " semanas y ")
            .replace(" weeks", " semanas")
        )
        wd_es = re.sub(r"\b(\d+) days\b", r"\1 días", wd_es)
        return f"{total} días, es decir {wd_es} del Omer"
    if lang == "fr":
        wd_fr = (
            wd.replace("1 week and 1 day", "1 semaine et 1 jour")
            .replace("1 week and", "1 semaine et")
            .replace("1 week", "1 semaine")
            .replace(" weeks and 1 day", " semaines et 1 jour")
            .replace(" weeks and ", " semaines et ")
            .replace(" weeks", " semaines")
        )
        wd_fr = re.sub(r"\b(\d+) days\b", r"\1 jours", wd_fr)
        return f"{total} jours, soit {wd_fr} de l'Omer"
    if lang == "ru":
        wd_ru = wd
        wm = re.fullmatch(r"(\d+) weeks and (\d+) days", wd)
        if wm:
            w, d = int(wm.group(1)), int(wm.group(2))
            wd_ru = f"{ru_weeks(w)} и {ru_days(d)}"
        elif wd == "1 week":
            wd_ru = "1 неделя"
        elif wd.endswith(" weeks"):
            w = int(wd.split()[0])
            wd_ru = ru_weeks(w)
        elif wd == "1 week and 1 day":
            wd_ru = "1 неделя и 1 день"
        elif wd.startswith("1 week and "):
            dm = re.search(r"(\d+) days?$", wd)
            d = int(dm.group(1)) if dm else 0
            wd_ru = f"1 неделя и {ru_days(d)}"
        return f"{ru_days(total)} омера, то есть {wd_ru} омера"
    return summary_en


def speech_phrase(summary_en: str, summary_tr: str, lang: str) -> str:
    if lang == "he":
        return f"היום {summary_tr}."
    if lang == "ru":
        return f"Сегодня — {summary_tr}."
    if lang == "es":
        return f"Hoy: {summary_tr}."
    if lang == "fr":
        return f"Aujourd'hui : {summary_tr}."
    return f"Today is {summary_en}."


def load_bundled_entries(lang: str) -> dict[str, str]:
    return json.loads((BUNDLED / f"{lang}.json").read_text(encoding="utf-8"))["entries"]


def yaaleh_template_translation(
    entries: dict[str, str],
    template_en: str,
    forgot_en: str,
    full_en: str,
    existing: dict[str, str] | None = None,
) -> str:
    if existing and template_en in existing and "$forgotBlock" in existing[template_en]:
        return existing[template_en]
    bundled_template = entries.get(template_en, "")
    if bundled_template and "$forgotBlock" in bundled_template:
        return bundled_template
    full_tr = entries.get(full_en, "")
    forgot_tr = entries.get(forgot_en, "")
    if full_tr and forgot_tr and forgot_tr in full_tr:
        return full_tr.replace(forgot_tr, "$forgotBlock", 1)
    if "$forgotBlock" in template_en:
        return template_en
    return bundled_template or template_en


YAALEH_TEMPLATE_PAIRS = [
    (YAALEH_SHACHARIT_TEMPLATE, YAALEH_FORGOT_SHACHARIT, YAALEH_FULL_EXPLAINERS["shacharit"]),
    (YAALEH_SHACHARIT_FEMALE_TEMPLATE, YAALEH_FORGOT_SHACHARIT, YAALEH_FULL_EXPLAINERS["shacharit_female"]),
    (YAALEH_MINCHA_TEMPLATE, YAALEH_FORGOT_MINCHA, YAALEH_FULL_EXPLAINERS["mincha"]),
    (YAALEH_MINCHA_FEMALE_TEMPLATE, YAALEH_FORGOT_MINCHA, YAALEH_FULL_EXPLAINERS["mincha_female"]),
    (YAALEH_MAARIV_TEMPLATE, YAALEH_FORGOT_MAARIV, YAALEH_FULL_EXPLAINERS["maariv"]),
    (YAALEH_MAARIV_FEMALE_TEMPLATE, YAALEH_FORGOT_MAARIV, YAALEH_FULL_EXPLAINERS["maariv_female"]),
]

def main() -> None:
    existing_all: dict[str, dict[str, str]] = {}
    if OUT.exists():
        existing_all = json.loads(OUT.read_text(encoding="utf-8"))
    by_lang: dict[str, dict[str, str]] = {lang: {} for lang in ("he", "es", "fr", "ru")}

    for en, langs in KIDDUSH_FIXES.items():
        for lang, tr in langs.items():
            by_lang[lang][en] = tr

    for en, langs in NUSACH_WHEN_FIXES.items():
        for lang, tr in langs.items():
            by_lang[lang][en] = tr

    by_lang["he"][OMER_NEXT_NIGHT_LINE] = OMER_NEXT_NIGHT_FIXES["he"]
    by_lang["es"][OMER_NEXT_NIGHT_LINE] = OMER_NEXT_NIGHT_FIXES["es"]
    by_lang["fr"][OMER_NEXT_NIGHT_LINE] = OMER_NEXT_NIGHT_FIXES["fr"]
    by_lang["ru"][OMER_NEXT_NIGHT_LINE] = OMER_NEXT_NIGHT_FIXES["ru"]

    by_lang["he"][OMER_TIME_PART] = OMER_TIME_PART_FIXES["he"]
    by_lang["es"][OMER_TIME_PART] = OMER_TIME_PART_FIXES["es"]
    by_lang["fr"][OMER_TIME_PART] = OMER_TIME_PART_FIXES["fr"]
    by_lang["ru"][OMER_TIME_PART] = OMER_TIME_PART_FIXES["ru"]

    tails = {lang: load_count_omer_tail(lang) for lang in ("he", "es", "fr", "ru")}
    for summary_en in OMER_TODAY_SUMMARIES:
        for lang in ("he", "es", "fr", "ru"):
            summary_tr = tails[lang].get(summary_en) or translate_omer_summary(summary_en, lang)
            if lang == "he" and summary_en not in tails[lang]:
                day_m = re.match(r"(\d+)", summary_en)
                if day_m:
                    summary_tr = omer_day_summary_he(int(day_m.group(1)), "לעומר")
            by_lang[lang][summary_en] = summary_tr
            speech_en = f"Today is {summary_en}."
            by_lang[lang][speech_en] = speech_phrase(summary_en, summary_tr, lang)

    by_lang["ru"][TAANIT_BECHOR_KEY] = TAANIT_BECHOR_RU
    by_lang["fr"][TAANIT_BECHOR_KEY] = TAANIT_BECHOR_FR
    by_lang["es"][TAANIT_BECHOR_KEY] = TAANIT_BECHOR_ES

    schedule_suffix = "$scheduleLeadIn$scheduleBody$scheduleYomTov"
    for lang in ("he", "es", "fr", "ru"):
        entries = load_bundled_entries(lang)
        if lang == "ru":
            base_tr = TAANIT_BECHOR_RU
        elif lang == "fr":
            base_tr = TAANIT_BECHOR_FR
        elif lang == "es":
            base_tr = TAANIT_BECHOR_ES
        else:
            base_tr = entries.get(TAANIT_BECHOR_KEY, TAANIT_BECHOR_KEY)
        by_lang[lang][TAANIT_BECHOR_TEMPLATE] = base_tr + schedule_suffix
        for template_en, forgot_en, full_en in YAALEH_TEMPLATE_PAIRS:
            if lang == "ru" and template_en in YAALEH_TEMPLATE_RU:
                by_lang[lang][template_en] = YAALEH_TEMPLATE_RU[template_en]
            elif lang == "es" and template_en in YAALEH_TEMPLATE_ES:
                by_lang[lang][template_en] = YAALEH_TEMPLATE_ES[template_en]
            elif lang == "fr" and template_en in YAALEH_TEMPLATE_FR:
                by_lang[lang][template_en] = YAALEH_TEMPLATE_FR[template_en]
            else:
                by_lang[lang][template_en] = yaaleh_template_translation(
                    entries, template_en, forgot_en, full_en, existing_all.get(lang),
                )
        if lang == "ru":
            for key, val in YAALEH_TEMPLATE_RU.items():
                if key not in {t[0] for t in YAALEH_TEMPLATE_PAIRS}:
                    by_lang[lang][key] = val
        elif lang == "es":
            for key, val in YAALEH_TEMPLATE_ES.items():
                if key not in {t[0] for t in YAALEH_TEMPLATE_PAIRS}:
                    by_lang[lang][key] = val
        elif lang == "fr":
            for key, val in YAALEH_TEMPLATE_FR.items():
                if key not in {t[0] for t in YAALEH_TEMPLATE_PAIRS}:
                    by_lang[lang][key] = val

    OUT.parent.mkdir(parents=True, exist_ok=True)
    OUT.write_text(json.dumps(by_lang, ensure_ascii=False, indent=2), encoding="utf-8")
    total = sum(len(v) for v in by_lang.values())
    print(f"Wrote {OUT.name}: {total} entries ({len(by_lang['ru'])} keys per lang avg)")


if __name__ == "__main__":
    main()
