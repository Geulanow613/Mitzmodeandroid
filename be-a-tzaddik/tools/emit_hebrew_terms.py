#!/usr/bin/env python3
"""Emit hebrew_terms.json — Jewish vocabulary in Hebrew script; localize civil UI."""

from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CATALOG = ROOT / "data" / "translation-catalog" / "strings.json"
HE_BUNDLE = (
    ROOT
    / "shared"
    / "src"
    / "commonMain"
    / "composeResources"
    / "files"
    / "translations"
    / "he.json"
)
OUT = ROOT / "data" / "translation-catalog" / "human" / "hebrew_terms.json"

# Standalone labels: use Hebrew bundle value in es/fr/ru (not English).
HEBREW_SCRIPT_KEYS = {
    "Av",
    "Adar",
    "Adar II",
    "Elul",
    "Iyar",
    "Sivan",
    "Teves",
    "Kislev",
    "Nissan",
    "Shevat",
    "Tammuz",
    "Cheshvan",
    "Tishrei",
    "Purim",
    "Sofer",
    "Ba'al Teshuva",
    "Kiddush",
    "Yom Tov",
    "Sefirat HaOmer",
    "Ha'etz",
    "Hagafen",
    "Ha'adama",
    "Shehakol",
    "Asher Yatzar",
    "Shehecheyanu",
    "Borei Nefashot",
    "Birkat Ha'Ilanot",
    "Oseh Ma'aseh Bereishit",
    "Friday",
    "Monday",
    "Tuesday",
    "Wednesday",
    "Thursday",
    "Saturday",
    "Sunday",
    "Fri",
    "Mon",
    "Tue",
    "Wed",
    "Thu",
    "Sat",
    "Sun",
}

LOCALIZED: dict[str, dict[str, str]] = {
    "No": {"es": "No", "fr": "Non", "ru": "Нет"},
    "OK": {"es": "Aceptar", "fr": "OK", "ru": "ОК"},
    "Normal": {"es": "Normal", "fr": "Normal", "ru": "Обычный"},
    "XL": {"es": "XL", "fr": "XL", "ru": "XL"},
    "30 min": {"es": "30 minutos", "fr": "30 minutes", "ru": "30 мин"},
    "{day} {month} {year}": {
        "fr": "le {day} {month} {year}",
        "ru": "{day} {month} {year} г.",
    },
    "January": {"es": "Enero", "fr": "Janvier", "ru": "Январь"},
    "February": {"es": "Febrero", "fr": "Février", "ru": "Февраль"},
    "March": {"es": "Marzo", "fr": "Mars", "ru": "Март"},
    "April": {"es": "Abril", "fr": "Avril", "ru": "Апрель"},
    "May": {"es": "Mayo", "fr": "Mai", "ru": "Май"},
    "June": {"es": "Junio", "fr": "Juin", "ru": "Июнь"},
    "July": {"es": "Julio", "fr": "Juillet", "ru": "Июль"},
    "August": {"es": "Agosto", "fr": "Août", "ru": "Август"},
    "September": {"es": "Septiembre", "fr": "Septembre", "ru": "Сентябрь"},
    "October": {"es": "Octubre", "fr": "Octobre", "ru": "Октябрь"},
    "November": {"es": "Noviembre", "fr": "Novembre", "ru": "Ноябрь"},
    "December": {"es": "Diciembre", "fr": "Décembre", "ru": "Декабрь"},
    "Beardy Top Productions": {
        "es": "Beardy Top Productions",
        "fr": "Beardy Top Productions",
        "ru": "Beardy Top Productions",
    },
}


def main() -> None:
    required = set(json.loads(CATALOG.read_text(encoding="utf-8"))["strings"])
    he_entries = json.loads(HE_BUNDLE.read_text(encoding="utf-8"))["entries"]

    out: dict[str, dict[str, str]] = {"he": {}, "es": {}, "fr": {}, "ru": {}}

    for key in sorted(HEBREW_SCRIPT_KEYS):
        if key not in required:
            continue
        he_val = he_entries.get(key, key)
        for lang in ("es", "fr", "ru"):
            out[lang][key] = he_val

    for key, langs in LOCALIZED.items():
        if key not in required:
            continue
        for lang, val in langs.items():
            out[lang][key] = val

    OUT.write_text(json.dumps(out, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    counts = {lang: len(out[lang]) for lang in out}
    print(f"wrote {OUT}: {counts}")


if __name__ == "__main__":
    main()
