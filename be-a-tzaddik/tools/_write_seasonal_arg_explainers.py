#!/usr/bin/env python3
"""Human translations for small template args (fast names/meanings, Chanukah brachot notes)."""
from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / "data" / "translation-catalog" / "human" / "seasonal_arg_explainers.json"

FIXES: dict[str, dict[str, str]] = {
    "commemorates the assassination of Gedaliah ben Achikam after the First Temple's destruction.": {
        "he": "לזכר רצח גדליהו בן אחיקם לאחר חורבן בית המקדש הראשון.",
        "es": "conmemora el asesinato de Gedaliah ben Ajikam tras la destrucción del Primer Templo.",
        "fr": "commémore l'assassinat de Gedalia ben Ahikam après la destruction du Premier Temple.",
        "ru": "в память об убийстве Гедалии бен Ахикама после разрушения Первого Храма.",
    },
    "marks the beginning of the siege of Jerusalem. Never postponed from Friday.": {
        "he": "מסמן את תחילת המצור על ירושלים. אינו נדחה מיום שישי.",
        "es": "marca el comienzo del sitio de Jerusalén. Nunca se pospone desde el viernes.",
        "fr": "marque le début du siège de Jérusalem. Jamais reporté depuis le vendredi.",
        "ru": "отмечает начало осады Иерусалима. Никогда не переносится с пятницы.",
    },
    "marks the breaching of Jerusalem's walls and the start of the Three Weeks mourning period.": {
        "he": "מסמן את פרצת חומות ירושלים ותחילת תקופת אבל של שלושת השבועות.",
        "es": "marca la brecha en los muros de Jerusalén y el inicio del periodo de duelo de las Tres Semanas.",
        "fr": "marque la brèche dans les murailles de Jérusalem et le début de la période de deuil des Trois Semaines.",
        "ru": "отмечает пролом стен Иерусалима и начало траурного периода Трёх Недель.",
    },
    "First night: say all three brachot including Shehecheyanu.": {
        "he": "לילה ראשון: אמרו את שלוש הברכות כולל שהחיינו.",
        "es": "Primera noche: diga las tres berajot, incluida Shehecheyanu.",
        "fr": "Première nuit : dites les trois bérakhot, y compris Sheheheyanou.",
        "ru": "Первая ночь: произнесите все три брaхи, включая Шехeheyanu.",
    },
    "Tonight: two brachot (no Shehecheyanu unless first time lighting this year).": {
        "he": "הלילה: שתי ברכות (ללא שהחיינו אלא אם זו הפעם הראשונה השנה).",
        "es": "Esta noche: dos berajot (sin Shehecheyanu salvo que sea la primera vez que enciende este año).",
        "fr": "Ce soir : deux bérakhot (pas de Sheheheyanou sauf si c'est la première allumage de l'année).",
        "ru": "Сегодня вечером: две брaхи (без Шехeheyanu, если только не зажигаете впервые в этом году).",
    },
}

SEASONAL_ARG_CATALOG_KEYS = list(FIXES.keys())


def main() -> None:
    by_lang: dict[str, dict[str, str]] = {lang: {} for lang in ("he", "es", "fr", "ru")}
    for en, langs in FIXES.items():
        for lang, tr in langs.items():
            by_lang[lang][en] = tr
    OUT.parent.mkdir(parents=True, exist_ok=True)
    OUT.write_text(json.dumps(by_lang, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"Wrote {OUT.name}: {sum(len(v) for v in by_lang.values())} entries")


if __name__ == "__main__":
    main()
