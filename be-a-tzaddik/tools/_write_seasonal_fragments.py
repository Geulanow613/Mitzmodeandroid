#!/usr/bin/env python3
"""Write human/seasonal_explainer_fragments.json by slicing full catalog translations."""
from __future__ import annotations

import json
from pathlib import Path

from _sync_explainer_catalog_keys import NINE_DAYS_SHARED, THREE_WEEKS_INTRO

ROOT = Path(__file__).resolve().parents[1]
CATALOG = ROOT / "data" / "translation-catalog" / "strings.json"
FRAGMENTS_EN = ROOT / "tools" / "_seasonal_fragments_en.json"
OUT = ROOT / "data" / "translation-catalog" / "human" / "seasonal_explainer_fragments.json"
BUNDLED = ROOT / "shared" / "src" / "commonMain" / "composeResources" / "files" / "translations"
LANGS = ("he", "es", "fr", "ru")

# Sliced from full keys when shared-block split fails or shards lack the tail.
MANUAL_AFTER_FAST: dict[str, dict[str, str]] = {}
MANUAL_TW_SUFFIX: dict[str, dict[str, str]] = {}


def catalog_strings() -> list[str]:
    return json.loads(CATALOG.read_text(encoding="utf-8"))["strings"]


def pick(*, contains: str) -> str:
    matches = [s for s in catalog_strings() if contains in s]
    if not matches:
        raise KeyError(contains)
    return max(matches, key=len)


def after_shared(full_en: str) -> str:
    return full_en.split(NINE_DAYS_SHARED, 1)[1].lstrip("\n")


def before_shared(full_en: str) -> str:
    return full_en.split(NINE_DAYS_SHARED, 1)[0].rstrip("\n")


def load_bundled(lang: str) -> dict[str, str]:
    return json.loads((BUNDLED / f"{lang}.json").read_text(encoding="utf-8"))["entries"]


def tw_suffix(entries: dict[str, str], full_key: str) -> str:
    intro_tr = entries.get(THREE_WEEKS_INTRO, THREE_WEEKS_INTRO)
    full_tr = entries[full_key]
    if full_tr.startswith(intro_tr):
        return full_tr[len(intro_tr) :].lstrip("\n")
    return full_tr


def split_nine(entries: dict[str, str], full_key: str) -> tuple[str, str]:
    full_tr = entries[full_key]
    shared_tr = entries.get(NINE_DAYS_SHARED, NINE_DAYS_SHARED)
    if shared_tr in full_tr:
        before_tr, after_tr = full_tr.split(shared_tr, 1)
        return before_tr.rstrip("\n"), after_tr.lstrip("\n")
    before_en = before_shared(full_key)
    after_en = after_shared(full_key)
    return entries.get(before_en, before_en), entries.get(after_en, after_en)


def _init_manual_after_fast(frags_en: list[str]) -> None:
    k_ash = frags_en[4]
    k_sep = frags_en[6]
    k_ch = frags_en[8]
    MANUAL_AFTER_FAST["fr"] = {
        k_ash: (
            "Après le jeûne (10 Av) : le minhag ashkénaze prolonge l'interdit de viande, vin, musique, "
            "lessive et bain de plaisir jusqu'à 'hatzot (milieu halakhique du jour) du 10 Av.\n\n"
            "Havdalah des Neuf Jours : le motsei Chabbat pendant les Neuf Jours, utilisez du vin ou du "
            "jus de raisin pour la Havdalah. Minhag ashkénaze (Rema O.C. 551:10) : idéalement un enfant "
            "(6–9 ans) boit la coupe ; sinon celui qui récite la Havdalah — la mitzva de Havdalah l'emporte "
            "sur la retenue."
        ),
        k_sep: (
            "Après le jeûne : beaucoup de Séfarades observent des restrictions similaires jusqu'à 'hatzot "
            "du 10 Av — confirmez avec votre rav avant de reprendre viande et vin."
        ),
        k_ch: (
            "Après le jeûne (10 Av) : suivez le psak 'Habad accepté sur viande, vin, musique, lessive et "
            "bain jusqu'à 'hatzot du 10 Av — demandez à votre rav en cas de doute.\n\n"
            "Havdalah des Neuf Jours : vin ou jus de raisin ; minhag ashkénaze donne souvent la coupe à "
            "un enfant (6–9 ans) lorsque c'est possible."
        ),
    }
    MANUAL_AFTER_FAST["es"] = {
        k_ash: (
            "Después del ayuno (10 Av): la costumbre asquenazí mantiene prohibidos carne, vino, música, "
            "lavado y baño por placer hasta chatzot (mediodía halájico) del 10 Av.\n\n"
            "Havdalah de los Nueve Días: en motzei Shabat durante los Nueve Días, use vino o jugo de uva "
            "para la Havdalah. Costumbre asquenazí (Rema O.C. 551:10): idealmente un niño (6–9 años) "
            "bebe la copa; si no hay, quien recita la Havdalah — la mitzvá de Havdalah prevalece sobre "
            "la restricción."
        ),
        k_sep: (
            "Después del ayuno: muchos sefardíes siguen restricciones similares hasta chatzot del 10 Av "
            "— confirme con su rav antes de reanudar carne y vino."
        ),
        k_ch: (
            "Después del ayuno (10 Av): siga el psak de Jabad aceptado sobre carne, vino, música, lavado "
            "y baño hasta chatzot del 10 Av — pregunte a su rav si tiene dudas.\n\n"
            "Havdalah de los Nueve Días: vino o jugo de uva; la costumbre asquenazí suele dar la copa a "
            "un niño (6–9 años) cuando es posible."
        ),
    }
    MANUAL_AFTER_FAST["ru"] = {
        k_ash: (
            "После поста (10 Ава): ашкеназский обычай продлевает запрет на мясо, вино, музыку, стирку и "
            "купание для удовольствия до хацот (галахического полудня) 10 Ава.\n\n"
            "Хавдала в Девять дней: в моцаеи Шаббата в Девять дней используйте вино или виноградный сок "
            "для хавдала. Ашкеназский обычай (Rema O.C. 551:10): в идеале чашу выпивает ребёнок "
            "(6–9 лет); если его нет, пьёт тот, кто читает хавдалу — мицва хавдала важнее "
            "обычая сдержанности."
        ),
        k_sep: (
            "После поста: многие сефарды соблюдают похожие ограничения до хацот 10 Ава — уточните у "
            "рава, прежде чем возобновлять мясо и вино."
        ),
        k_ch: (
            "После поста (10 Ава): следуйте принятому псаку Хабада о мясе, вине, музыке, стирке и купании "
            "до хацот 10 Ава — спросите рава при сомнении.\n\n"
            "Хавдала в Девять дней: вино или сок; ашкеназский обычай часто даёт чашу ребёнку (6–9 лет)."
        ),
    }
    MANUAL_AFTER_FAST["he"] = {
        k_ash: (
            "אחרי הצום (י׳ באב): מנהג אשכנז ממשיך איסור בשר, יין, מוזיקה, כביסה ורחצה לתענוג עד חצות "
            "(חצות היום ההלכתי) של י׳ באב.\n\n"
            "הבדלה בתשעת הימים: במוצ\"ש בתשעת הימים — יין או מיץ ענבים. מנהג אשכנז (רמ\"א או\"ח תקנ\"א:י): "
            "בדרך כלל ילד (גיל 6–9) שותה; אם לא — המברך — מצוות ההבדלה גוברת על ההגבלה."
        ),
        k_sep: (
            "אחרי הצום: ספרדים רבים ממשיכים הגבלות דומות עד חצות של י׳ באב — אשרו עם הרב לפני חזרה "
            "לבשר ויין."
        ),
        k_ch: (
            "אחרי הצום (י׳ באב): לפי פסק חב\"ד המקובל על בשר, יין, מוזיקה, כביסה ורחצה עד חצות של "
            "י׳ באב — שאלו את הרב אם אינכם בטוחים.\n\n"
            "הבדלה בתשעת הימים: יין או מיץ; מנהג אשכנזי נותן לעיתים את הכוס לילד (6–9) כשאפשר."
        ),
    }


def _init_manual_tw_suffix(frags_en: list[str]) -> None:
    """When tw_suffix() fails (empty prefix match), use explicit RU/FR/ES tails."""
    MANUAL_TW_SUFFIX["ru"] = {
        frags_en[0]: (
            "Ашкеназский минхаг: продлённый траур на протяжении Трёх недель, усиливается в Девять дней.\n"
            "17 Тамуза: стрижка, музыка и свадьбы запрещены; Шехехияну избегают (разрешено в Шаббат).\n"
            "С 1 Ава: см. пункт о Девяти днях."
        ),
        frags_en[1]: (
            "Сефарды и Эдот а-Мизрах: более мягко до недели 9 Ава.\n"
            "Стрижка разрешена большую часть периода; музыку избегают; свадьбы — по общине."
        ),
        frags_en[2]: (
            "Хабад — строгий ашкеназский минхаг.\n"
            "Стрижка, музыка и свадьбы запрещены; больше Торы и цдаки; усиливается в Девять дней с Рош Ходеш Ав."
        ),
    }
    MANUAL_TW_SUFFIX["es"] = {
        frags_en[0]: (
            "Minhag asquenazí: luto prolongado durante las Tres Semanas, más intenso en los Nueve Días.\n"
            "17 Tamuz: cortes de pelo, música y bodas prohibidos; Shehecheyanu se evita (permitido en Shabat).\n"
            "Desde 1 Av: ver el ítem de los Nueve Días."
        ),
        frags_en[1]: (
            "Sefardíes y Edot HaMizrach: más flexibles hasta la semana del 9 de Av.\n"
            "Cortes de pelo permitidos en la mayor parte del período; música se evita; bodas según la kehilla."
        ),
        frags_en[2]: (
            "Jabad — minhag asquenazí estricto.\n"
            "Cortes de pelo, música y bodas prohibidos; más Torá y tzedaká; se intensifica en los Nueve Días desde Rosh Chodesh Av."
        ),
    }
    MANUAL_TW_SUFFIX["fr"] = {
        frags_en[0]: (
            "Minhag ashkénaze : deuil prolongé pendant les Trois Semaines, plus intense pendant les Neuf Jours.\n"
            "17 Tamouz : coupes de cheveux, musique et mariages interdits ; Shehe'heyanu évité (permis le Chabbat).\n"
            "À partir du 1 Av : voir l'élément des Neuf Jours."
        ),
        frags_en[1]: (
            "Séfarades et Edot HaMizra'h : plus souples jusqu'à la semaine du 9 Av.\n"
            "Coupes de cheveux permises la plupart du temps ; musique évitée ; mariages selon la kehilla."
        ),
        frags_en[2]: (
            "Habad — minhag ashkénaze strict.\n"
            "Coupes de cheveux, musique et mariages interdits ; plus de Torah et de tsedaka ; s'intensifie aux Neuf Jours depuis Rosh 'Hodesh Av."
        ),
    }


def main() -> None:
    frags_en: list[str] = json.loads(FRAGMENTS_EN.read_text(encoding="utf-8"))
    if not MANUAL_AFTER_FAST:
        _init_manual_after_fast(frags_en)
    if not MANUAL_TW_SUFFIX:
        _init_manual_tw_suffix(frags_en)

    tw_ash = pick(contains="Ashkenazi custom observes")
    tw_sep = pick(contains="Sephardic and Edot HaMizrach")
    tw_chabad = pick(contains="Chabad follows strict Ashkenazi mourning")
    nine_ash = pick(contains="The Nine Days (from 1 Av until")
    nine_chabad = pick(contains="The Nine Days (from Rosh Chodesh Av until")
    nine_sep = pick(contains="The Nine Days and the week of Tisha B'Av")

    out: dict[str, dict[str, str]] = {}
    for lang in LANGS:
        entries = load_bundled(lang)
        ash_before, ash_after = split_nine(entries, nine_ash)
        sep_before, sep_after = split_nine(entries, nine_sep)
        ch_before, ch_after = split_nine(entries, nine_chabad)
        out[lang] = {
            frags_en[0]: tw_suffix(entries, tw_ash),
            frags_en[1]: tw_suffix(entries, tw_sep),
            frags_en[2]: tw_suffix(entries, tw_chabad),
            frags_en[3]: ash_before,
            frags_en[4]: ash_after,
            frags_en[5]: sep_before,
            frags_en[6]: sep_after,
            frags_en[7]: ch_before,
            frags_en[8]: ch_after,
        }
        for key, val in MANUAL_AFTER_FAST.get(lang, {}).items():
            if key in out[lang]:
                out[lang][key] = val
        for key, val in MANUAL_TW_SUFFIX.get(lang, {}).items():
            if key in out[lang] and not out[lang][key].strip():
                out[lang][key] = val

    OUT.write_text(json.dumps(out, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"Wrote {OUT.name}")
    for lang in LANGS:
        print(f"  {lang}: {len(out[lang])} fragments")


if __name__ == "__main__":
    main()
