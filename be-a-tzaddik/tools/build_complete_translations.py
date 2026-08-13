#!/usr/bin/env python3
"""Build complete_translations.json — final overrides for remaining translation gaps."""
from __future__ import annotations

import importlib.util
import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CATALOG_PATH = ROOT / "data" / "translation-catalog" / "strings.json"
OUT = ROOT / "data" / "translation-catalog" / "human" / "complete_translations.json"
TOOLS = Path(__file__).resolve().parent

PLACEHOLDER_RE = [
    re.compile(r"\$\{[^}]+\}"),
    re.compile(r"\$[A-Za-z_][A-Za-z0-9_]*"),
]


def _load_module(name: str, path: Path):
    spec = importlib.util.spec_from_file_location(name, path)
    mod = importlib.util.module_from_spec(spec)
    assert spec.loader
    spec.loader.exec_module(mod)
    return mod


def placeholders(text: str) -> list[str]:
    found: list[str] = []
    for pat in PLACEHOLDER_RE:
        found.extend(pat.findall(text))
    return found


def assert_placeholders(en: str, tr: str, label: str = "") -> None:
    en_p = placeholders(en)
    tr_p = placeholders(tr)
    if en_p != tr_p:
        raise ValueError(f"Placeholder mismatch {label}: en={en_p} tr={tr_p}")


def apply_prefix(out: dict[str, str], catalog: list[str], prefix: str, tr: str) -> None:
    for s in catalog:
        if s.startswith(prefix):
            out[s] = tr


def apply_glossary(
    out_es: dict[str, str], out_fr: dict[str, str], out_ru: dict[str, str], catalog: list[str], p2
) -> None:
    kab_es = p2.KAB_ES
    kab_fr = p2.KAB_FR
    kab_ru = p2.KAB_RU
    pshat_fr = p2.PSHAT_FR
    zohar_fr = p2.ZOHAR_FR

    for s in catalog:
        if s == "Kabbalah":
            out_es[s] = "Cábala"
            out_fr[s] = "Tradition mystique"
        elif s.startswith("Kabbalah is the Jewish mystical tradition"):
            out_es[s] = kab_es
            out_fr[s] = kab_fr
            out_ru[s] = kab_ru
        elif s.startswith("Kabbalah — Kabbalah is the Jewish mystical tradition"):
            out_es[s] = f"Cábala — {kab_es}"
            out_fr[s] = f"Tradition mystique — {kab_fr}"
            out_ru[s] = f"Каббала — {kab_ru}"
        elif s.startswith("Dive into Kabbalah's depths!"):
            out_es[s] = (
                "¡Sumérgete en las profundidades de la Cábala! 🌊 Los grandes rabinos enfatizaron que aprender "
                "Cábala requiere madurez en Torá y temor a D-s — no es entretenimiento ni superstición."
            )
            out_fr[s] = (
                "Plongez dans les profondeurs de la tradition mystique ! 🌊 Les grands rabbins ont souligné "
                "qu'apprendre cette tradition exige une maturité en Torah et la crainte de D. — "
                "ce n'est ni divertissement ni superstition."
            )
        elif s.startswith("Learn the step-by-step process of bringing a korban"):
            out_es[s] = (
                "Aprenda el proceso paso a paso de traer un korban (ofrenda) — y por qué invertir incluso un solo paso "
                "demuestra que el servicio en el Templo no era superstición sino avodah (servicio) con intención."
            )
            out_fr[s] = (
                "Apprenez le processus étape par étape pour apporter un korban (offrande) — et pourquoi inverser "
                "même une seule étape montre que le service au Temple n'était pas de la superstition mais une avodah (service) avec intention."
            )
        elif s.startswith("Ushpizin are mystical"):
            out_es[s] = (
                "Los Ushpizin son «invitados» místicos — Abraham, Isaac, Jacob y otros — recibidos en la sukkah "
                "cada noche de Sucot según la costumbre mística. Algunos recitan una invitación antes de la comida. "
                "La idea es que la verdadera hospitalidad y el estudio de Torá traen presencia santa al tabernáculo. "
                "Incluso sin el texto místico completo, invitar huéspedes físicos cumple la mitzvá de alegría en la sukkah."
            )
            out_fr[s] = (
                "Les Ouchpizine sont des «invités» mystiques — Abraham, Isaac, Jacob et d'autres — accueillis dans la soukka "
                "chaque nuit de Souccot selon la coutume mystique. Certains récitent une invitation avant le repas. "
                "L'idée est que l'hospitalité véritable et l'étude de Torah apportent une présence sainte dans la cabane. "
                "Même sans le texte mystique complet, inviter des hôtes réels accomplit la mitzvah de joie dans la soukka."
            )
        elif s.startswith("Ushpizin — Ushpizin are mystical"):
            base_es = out_es.get(s.split(" — ", 1)[1], "")
            base_fr = out_fr.get(s.split(" — ", 1)[1], "")
            if base_es:
                out_es[s] = f"Ushpizin — {base_es}"
            if base_fr:
                out_fr[s] = f"Ushpizin — {base_fr}"
        elif s.startswith("K'fi daato means"):
            out_es[s] = (
                "K'fi daato significa educar a un niño según lo que puede entender y hacer de forma fiable — "
                "no exigir perfección de adulto. El chinuch desarrolla mitzvot paso a paso: berajot antes de la Amidá completa, "
                "asistencia breve a Shabat antes de servicios extensos. La presión sin da'at fracasa; la alegría en mitzvot dura más."
            )
            out_fr[s] = (
                "K'fi daato signifie éduquer un enfant selon ce qu'il peut comprendre et faire de façon fiable — "
                "sans exiger la perfection d'un adulte. Le 'hinoukh développe les mitzvot pas à pas : bérakhot avant l'Amidah complète, "
                "courte présence à Chabbat avant de longs offices. La pression sans da'at échoue ; la joie dans les mitzvot dure plus longtemps."
            )
        elif s.startswith("k'fi daato — K'fi daato means"):
            out_es[s] = (
                "k'fi daato — K'fi daato significa educar a un niño según lo que puede entender y hacer de forma fiable — "
                "no exigir perfección de adulto. El chinuch crece mitzvot paso a paso."
            )
            out_fr[s] = (
                "k'fi daato — K'fi daato signifie éduquer un enfant selon ce qu'il peut comprendre et faire de façon fiable — "
                "sans exiger la perfection d'un adulte. Le 'hinoukh développe les mitzvot pas à pas."
            )
        elif s.startswith("Tachanun (Ashkenaz): On most weekdays Tachanun"):
            out_es[s] = (
                "Tachanun (Ashkenaz): En la mayoría de los días de semana, Tachanun sigue a la Amidá de Shajarit — "
                "generalmente sentado con nefilat apayim. Lunes y jueves: Tachanun más largo (Vehu Rachum y la sección penitencial completa). "
                "Otros días laborables: forma más corta. Omitido en Rosh Jodesh, festivos, Janucá y otros días listados en su sidur."
            )
            out_fr[s] = (
                "Tachanun (Ashkenaz) : La plupart des jours de semaine, Tachanun suit l'Amidah de Shacharit — "
                "généralement assis avec nefilat apayim. Lundis et jeudis : Tachanun plus long (Vehu Rachum et section pénitentielle complète). "
                "Autres jours ouvrables : forme plus courte. Omis à Rosh 'Hodesh, fêtes, 'Hanoucca et autres jours indiqués dans votre siddour."
            )
        elif s.startswith("Ashkenaz Selichot begin on Motzei Shabbat"):
            out_es[s] = (
                "Las Selichot ashkenazíes comienzan en Motzei Shabat antes de Rosh Hashaná, con al menos cuatro días "
                "de preparación antes de Yamim Noraim. Siga el horario de su comunidad."
            )
            out_fr[s] = (
                "Les Selichot ashkenazes commencent à Motzei Chabbat avant Roch Hachana, avec au moins quatre jours "
                "de préparation avant les Yamim Noraim. Suivez le calendrier de votre communauté."
            )
        elif s.startswith("Shemini Atzeret / Simchat Torah (22 Tishrei in Israel)"):
            out_es[s] = (
                "Shemini Atzeret / Simjat Torá (22 Tishrei en Israel) — un día que combina ambos.\n\n"
                "Yom Tov:\n• Amidá completa de Yom Tov con Yaaleh V'yavo\n• Hallel completo y Musaf\n• En Israel: hakafot y alegría con la Torá\n\n"
                "Costumbres: muchos comen en la sukkah el día antes según minhag; en Israel este día es Simjat Torá."
            )
            out_fr[s] = (
                "Shemini Atzeret / Sim'hat Torah (22 Tishri en Israël) — un jour combinant les deux.\n\n"
                "Yom Tov :\n• Amidah complète de Yom Tov avec Yaaleh V'yavo\n• Hallel complet et Moussaf\n• En Israël : hakafot et joie avec la Torah\n\n"
                "Coutumes : beaucoup mangent dans la soukka la veille selon minhag ; en Israël ce jour est Sim'hat Torah."
            )
        elif s.startswith("Learn about the little known melacha of Maisach"):
            out_es[s] = (
                "Aprenda sobre la poco conocida melajá de Maisach — el arte de urdir y tejer en el servicio del Templo. "
                "Aunque no practicamos el Templo hoy, estudiar estas melajot profundiza la apreciación del Shabat."
            )
            out_fr[s] = (
                "Découvrez la melakha peu connue de Maisach — l'art du métier à tisser au service du Temple. "
                "Bien que nous ne pratiquions plus le Temple aujourd'hui, étudier ces melakhot approfondit l'appréciation de Chabbat."
            )
        elif s.startswith("Hamapil is the very last thing said"):
            out_es[s] = (
                "Hamapil es lo último que se dice antes de cerrar los ojos para dormir.\n\n"
                "Qué es:\n"
                "Hamapil (הַמַּפִּיל — «quien hace caer») es una bendición que agradece a D-s el don del sueño y pide protección durante la noche. "
                "Es única porque se dice antes de la acción que va a realizar (dormir).\n\n"
                "La bendición pide que:\n• Nuestros ojos puedan dormir\n• Despertemos por la mañana en paz\n• No seamos perturbados por malos sueños\n• El sueño nos refresque y sane\n\n"
                "Por qué el sueño requiere una bendición:\n"
                "El Talmud enseña que el sueño es «una sesenta parte de la muerte» — una partida nocturna parcial del alma. "
                "Decir Hamapil reconoce esta transición y expresa confianza total en D-s."
            )
            out_fr[s] = (
                "Hamapil est la toute dernière chose dite avant de fermer les yeux pour dormir.\n\n"
                "Qu'est-ce que c'est :\n"
                "Hamapil (הַמַּפִּיל — «qui fait tomber») est une bénédiction remerciant D. pour le don du sommeil et demandant protection la nuit. "
                "Elle est unique car dite avant l'action à venir (dormir).\n\n"
                "La bénédiction demande que :\n• Nos yeux puissent dormir\n• Nous nous réveillions le matin en paix\n• Nous ne soyons pas troublés par de mauvais rêves\n• Le sommeil nous rafraîchisse et guérisse\n\n"
                "Pourquoi le sommeil requiert une bénédiction :\n"
                "Le Talmud enseigne que le sommeil est « un soixantième de la mort » — un départ nocturne partiel de l'âme. "
                "Dire Hamapil reconnaît cette transition et exprime une confiance totale en D."
            )
        elif s.startswith("Pshat is the plain, straightforward meaning"):
            out_fr[s] = pshat_fr
        elif s.startswith("pshat — Pshat is the plain"):
            out_fr[s] = f"pshat — {pshat_fr}"
        elif s.startswith("The Zohar is the classic work of Kabbalah"):
            out_fr[s] = zohar_fr
        elif s.startswith("Zohar — The Zohar is the classic work"):
            out_fr[s] = f"Zohar — {zohar_fr}"
        elif s.startswith("Why the sages urge us to learn"):
            out_fr[s] = (
                "Pourquoi les sages nous exhortent à apprendre — et pourquoi cela vaut la peine de commencer aujourd'hui :\n\n"
                "L'étude de la Torah n'est pas seulement une obligation. C'est la façon la plus profonde pour un Juif "
                "de se lier à D. La Michnah enseigne que Talmud Torah k'neged kulam — l'étude de la Torah l'emporte "
                "sur toute autre mitzvah réunie (Peah 1:1). Quand vous apprenez, vous vous attachez au D. qui nous a donné la vie."
            )

    apply_prefix(out_es, catalog, "Immediately after saying Modeh Ani, wash your hands", p2.MODEH_NETILAT_ES)
    apply_prefix(out_fr, catalog, "Immediately after saying Modeh Ani, wash your hands", p2.MODEH_NETILAT_FR)
    apply_prefix(out_es, catalog, "Speech is one of the most powerful forces", p2.LASHON_ES)
    apply_prefix(out_fr, catalog, "Speech is one of the most powerful forces", p2.LASHON_FR)

    for key, tr in p2.ES_GLOSSARY.items():
        if key in catalog:
            out_es[key] = tr
    for key, tr in p2.FR_GLOSSARY.items():
        if key in catalog:
            out_fr[key] = tr


def main() -> None:
    gen = _load_module("gen_ru_critical", TOOLS / "_gen_complete_translations.py")
    p2 = _load_module("part2", TOOLS / "_complete_translations_part2.py")

    catalog: list[str] = json.loads(CATALOG_PATH.read_text(encoding="utf-8"))["strings"]
    cat_set = set(catalog)

    out: dict[str, dict[str, str]] = {"he": {}, "es": {}, "fr": {}, "ru": {}}

    for key, tr in gen.RU_CRITICAL.items():
        if key not in cat_set:
            print(f"WARN: RU critical key not in catalog ({len(key)} chars)", file=sys.stderr)
        tr = gen.force_kotlin_from_en(key, tr)
        assert_placeholders(key, tr, "RU critical")
        out["ru"][key] = tr

    for key, tr in p2.RU_IMPORTANT.items():
        if key not in cat_set:
            print(f"WARN: RU important key not in catalog", file=sys.stderr)
        out["ru"][key] = tr

    for lang, block in p2.LITERALS.items():
        for key, tr in block.items():
            if key in cat_set:
                out[lang][key] = tr

    apply_glossary(out["es"], out["fr"], out["ru"], catalog, p2)

    OUT.parent.mkdir(parents=True, exist_ok=True)
    OUT.write_text(json.dumps(out, ensure_ascii=False, indent=2), encoding="utf-8")
    counts = {lang: len(out[lang]) for lang in out}
    print(f"Wrote {OUT.name}: {counts}")


if __name__ == "__main__":
    main()
