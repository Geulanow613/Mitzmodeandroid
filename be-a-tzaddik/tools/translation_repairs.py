#!/usr/bin/env python3
"""Post-process translations: mechanical fixes only (no nonsense replacements)."""

from __future__ import annotations

import re

# Russian: Argos turned English article "A" into Cyrillic "А."
RU_A_DOT: dict[str, str] = {
    "blech": "Плита (blech)",
    "Chumash": "Хумаш",
    "kezayit": "кезайит",
    "siddur": "Сидур",
    "shaliach tzibur": "Шлиах цибур",
    "sofer": "Софер",
    "sukkah": "Сукка",
    "halachic": "Галахический",
    "mezuzah": "Мезуза",
    "mikveh": "Миква",
    "minyan": "Миньян",
    "machzor": "Махзор",
    "rav": "Рав",
}

FR_TRAV: list[tuple[str, str]] = [
    ("t rav aillent", "travaillent"),
    ("t rav ailleurs", "travailleurs"),
    ("t rav ailler", "travailler"),
    ("t rav ailliez", "travailliez"),
    ("t rav aillant", "travaillant"),
    ("t rav ailleur", "travailleur"),
    ("t rav aille", "travaille"),
    ("t rav aux", "travaux"),
    ("t rav ers", "travers"),
    ("t rav ail", "travail"),
    ("g rav es", "graves"),
    ("g rav e", "grave"),
    ("Ashkenaz i m", "ashkénazes"),
    ("Ashkenaz i", "ashkénazes"),
    ("Sephardi m", "séfarades"),
]

ES_TRAV: list[tuple[str, str]] = [
    ("a t ravés", "a través"),
    ("t ravés", "través"),
    ("g rav es", "graves"),
    ("g rav e", "grave"),
    ("ma rav illa", "maravilla"),
    ("Ashkenaz i m", "los ashkenazim"),
    ("Ashkenaz i", "ashkenazim"),
    ("Sephardi m", "sefardíes"),
    ("Sephardi", "sefardíes"),
]


def _fix_ru_a_dot(text: str) -> str:
    for word, repl in RU_A_DOT.items():
        text = text.replace(f"А.{word}", repl)
    text = re.sub(r"А\.([a-zA-Z])", r"\1", text)
    return text


def _normalize_spacing(text: str) -> str:
    text = re.sub(r"  +", " ", text)
    text = re.sub(r" +\.\.\.", "...", text)
    text = re.sub(r"\.\.\. +", "... ", text)
    text = re.sub(r" +\n", "\n", text)
    text = re.sub(r"\n +", "\n", text)
    return text


def repair_translation(lang: str, text: str) -> str:
    """Apply language-specific mechanical cleanup."""
    if lang == "es":
        for old, new in ES_TRAV:
            text = text.replace(old, new)
        text = re.sub(r"\bhalachic\b", "halájico", text, flags=re.IGNORECASE)
        text = re.sub(r"\bhalacha\b", "halajá", text, flags=re.IGNORECASE)
        text = _normalize_spacing(text)
    elif lang == "fr":
        for old, new in FR_TRAV:
            text = text.replace(old, new)
        text = re.sub(r"\bhalachic\b", "halakhique", text, flags=re.IGNORECASE)
        text = re.sub(r"\bhalacha\b", "halakha", text, flags=re.IGNORECASE)
        text = _normalize_spacing(text)
    elif lang == "ru":
        text = _fix_ru_a_dot(text)
        for old, new in [
            ("Ashkenaz i m", "ашкеназим"),
            ("Ashkenaz i", "ашкеназим"),
            ("Sephardi m", "сефарды"),
            ("Sephardi", "сефарды"),
            ("The Shulchan Aruch", "Шулхан Арух"),
            ("Shulchan Aruch", "Шулхан Арух"),
            ("The Rambam", "Рамбам"),
        ]:
            text = text.replace(old, new)
        text = _normalize_spacing(text)
    return text
