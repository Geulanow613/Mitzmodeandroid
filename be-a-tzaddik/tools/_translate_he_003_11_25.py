#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""Translate mitzvot_003 entries 11-25 (ES->HE) with post-processing fixes."""

from __future__ import annotations

import json
import re
import time
from pathlib import Path

from deep_translator import GoogleTranslator

ROOT = Path(__file__).resolve().parents[1]
ES_FILE = ROOT / "data" / "translation-catalog" / "human" / "mitzvot_003_es_only.json"
EN_KEYS = ROOT / "data" / "translation-catalog" / "_keys_003.json"
OUT = Path(__file__).resolve().parent / "_mitzvot_003_he_entries_11_25.json"

# Preserve loanwords / brand terms after MT.
REPLACEMENTS = [
    (r"\bVIP\b", "VIP"),
    (r"Star Trek", "Star Trek"),
    (r"\bfam\b", "משפחה"),
    (r"Mitz Mode", "מצ מוד"),
    (r"Live Long and Prosper", "Live Long and Prosper"),
    (r"Leonard Nimoy", "Leonard Nimoy"),
    (r"high-voltage", "high-voltage"),
    (r"handle with care", "handle with care"),
    (r"G-d", "G-d"),
    (r"Genizah", "גניזה"),
    (r"genizah", "גניזה"),
    (r"Kiddush Hashem", "קידוש השם"),
    (r"Chillul Hashem", "חילול השם"),
    (r"KIDDUSH HASHEM", "קידוש השם"),
    (r"CHILLUL HASHEM", "חילול השם"),
    (r"Rambam", "הרמב\"ם"),
    (r"Shulchan Aruch", "שולחן ערוך"),
    (r"Talmud", "תלמוד"),
    (r"Torah", "תורה"),
    (r"Hashem", "ה'"),
    (r"HaShem", "ה'"),
    (r"teshuvah", "תשובה"),
    (r"teshuvá", "תשובה"),
    (r"vidui", "וידוי"),
    (r"Yom Kippur", "יום כיפור"),
    (r"Kol Nidrei", "כל נדרי"),
    (r"kibbud", "כיבוד"),
    (r"mora", "מורא"),
    (r"KIBBUD AV VA'EM", "כיבוד אב ואם"),
    (r"MORA AV VA'EM", "מורא אב ואם"),
    (r"ona'ah", "אונאה"),
    (r"lo talin", "לא תלין"),
    (r"ribbis", "ריבית"),
    (r"ribit", "ריבית"),
    (r"Heter Iska", "היתר עיסקא"),
    (r"tzedakah", "צדקה"),
    (r"tzedaká", "צדקה"),
    (r"tefillin", "תפillin"),
    (r"tefilín", "תפillin"),
    (r"mezuzot", "מזוזot"),
    (r"mezuzá", "מזוזה"),
    (r"sofer", "סופר"),
    (r"duchan", "דוכן"),
    (r"Kohen", "כהן"),
    (r"Kohanim", "כohanim"),
    (r"Sephardim", "ספרדים"),
    (r"Ashkenazim", "אשכנזים"),
    (r"Sephardíes", "ספרדים"),
    (r"Asquenazí", "אשכנזים"),
    (r"Ruaj HaKodesh", "רוח הקודש"),
    (r"Ruach HaKodesh", "רוח הקודש"),
    (r"kavaná", "כוונה"),
    (r"kavanah", "כוונה"),
    (r"siddur", "סידור"),
    (r"sidur", "סידור"),
    (r"Mag'a Esh", "מגע אש"),
    (r"Mag\\'a Esh", "מגע אש"),
]

# Fix common MT Hebrew issues
HEbrew_FIXES = [
    ("תפillin", "תפillin"),
    ("מזוזot", "מזוזot"),
    ("כohanim", "כohanim"),
    ("למד ", "למדו "),
    ("למדוu", "למדו"),
    ("ה' '", "ה'"),
    ("  ", " "),
]

EMOJI_RE = re.compile(
    "["
    "\U0001F300-\U0001FAFF"
    "\u2600-\u27BF"
    "]+",
    flags=re.UNICODE,
)


def extract_emojis(text: str) -> str:
    found = EMOJI_RE.findall(text)
    return "".join(found)


def apply_replacements(text: str) -> str:
    for pattern, repl in REPLACEMENTS:
        text = re.sub(pattern, repl, text, flags=re.IGNORECASE)
    for old, new in HEbrew_FIXES:
        text = text.replace(old, new)
    return text


def translate_chunk(text: str) -> str:
    # Google Translate limit ~5000 chars; our entries are shorter.
    return GoogleTranslator(source="es", target="iw").translate(text)


def add_source_if_missing(he: str, en: str) -> str:
    if "מקור:" in he:
        return he
    # Minimal source extraction from English when obvious
    sources = []
    if "Leviticus" in en or "Vayikra" in en:
        m = re.search(r"\((Leviticus|Vayikra) ([^)]+)\)", en)
        if m:
            sources.append(f"ויקרא {m.group(2)}")
    if "Deuteronomy" in en:
        m = re.search(r"\((Deuteronomy|Devarim) ([^)]+)\)", en)
        if m:
            sources.append(f"דברים {m.group(2)}")
    if "Exodus" in en:
        m = re.search(r"\((Exodus|Shemot) ([^)]+)\)", en)
        if m:
            sources.append(f"שmot {m.group(2)}")
    if "Numbers" in en:
        m = re.search(r"\((Numbers|Bamidbar) ([^)]+)\)", en)
        if m:
            sources.append(f"במדbar {m.group(2)}")
    if "Rambam" in en or "Hilchot" in en:
        sources.append("הרמב\"ם")
    if "Shulchan Aruch" in en:
        sources.append("שולחן ערוך")
    if "Talmud" in en and "מקור" not in he:
        sources.append("תלמוד")
    if sources:
        he = he.rstrip() + " מקור: " + "; ".join(dict.fromkeys(sources)) + "."
    return he


def main() -> None:
    es_data = json.loads(ES_FILE.read_text(encoding="utf-8"))
    en_keys = json.loads(EN_KEYS.read_text(encoding="utf-8"))
    es_entries = es_data["es"][10:25]  # entries 11-25
    en_entries = en_keys[10:25]

    results: list[str] = []
    for i, (es, en) in enumerate(zip(es_entries, en_entries, strict=True)):
        print(f"Translating entry {i + 11}...")
        he = translate_chunk(es)
        he = apply_replacements(he)
        # Ensure opening matches style
        if not he.startswith("למד"):
            he = "למדו " + he.lstrip("¡! ")
        emojis = extract_emojis(en)
        if emojis and emojis not in he:
            # Append missing emojis near start after first sentence
            parts = he.split("! ", 1)
            if len(parts) == 2:
                he = parts[0] + "! " + emojis[0] + " " + parts[1] if len(emojis) == 1 else parts[0] + "! " + emojis + " " + parts[1]
            else:
                he = he + " " + emojis
        he = add_source_if_missing(he, en)
        results.append(he)
        time.sleep(0.5)

    OUT.write_text(json.dumps(results, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"Wrote {len(results)} entries to {OUT}")


if __name__ == "__main__":
    main()
