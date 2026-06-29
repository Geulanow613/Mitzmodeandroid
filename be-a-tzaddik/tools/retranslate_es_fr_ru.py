#!/usr/bin/env python3
"""Retranslate bad es/fr/ru entries with Argos + quality repairs (skip he)."""

from __future__ import annotations

import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
TOOLS = ROOT / "tools"
CATALOG = ROOT / "data" / "translation-catalog" / "strings.json"
COMPOSE = ROOT / "shared/src/commonMain/composeResources/files/translations"
OUT = ROOT / "data/translation-catalog/shards/es_fr_ru_retranslate.json"
LANGS = ("es", "fr", "ru")

sys.path.insert(0, str(TOOLS))
from apply_quality_fixes import (  # noqa: E402
    SUBS,
    apply_subs,
    repair_placeholders,
    restore_kotlin_templates,
)
from generate_argos_translations import (  # noqa: E402
    get_translator,
    install_packages,
    translate_text,
)
from translation_repairs import repair_translation  # noqa: E402

ALLOW_IDENTITY = {
    "\\s*/\\s*",
    "$mitzvotCount",
    "www.beardy.top",
    "https://www.beardy.top",
    "$translatedSwitchedTo $languageName.\n$translatedPleaseNote",
    "(?i)(?<![A-Za-z0-9'])${Regex.escape(word)}(?![A-Za-z0-9'])",
    "Rav",
    "ברכת המזון",
    "תפילת הדרך",
}

BAD: dict[str, list[re.Pattern[str]]] = {
    "es": [
        re.compile(r"\bRápido de\b", re.I),
        re.compile(r"\bdía rápido\b", re.I),
        re.compile(r"\bAshkenaz i\b", re.I),
        re.compile(r"\bSephardi m\b", re.I),
        re.compile(r"\bMourners recite\b", re.I),
        re.compile(r"\bHigh Holiday\b", re.I),
        re.compile(r"\bRecite the\b", re.I),
        re.compile(r"\bRecite la mitad\b", re.I),
        re.compile(r"D-s 's"),
        re.compile(r"\bhalachic\b", re.I),
        re.compile(r"\bmaquillaje\b", re.I),
        re.compile(r"\bMorir significa\b", re.I),
        re.compile(r"\bdibujo cerca\b", re.I),
        re.compile(r"\bsemi-holiday\b", re.I),
        re.compile(r"t rav", re.I),
        re.compile(r"\bAshkenaz i\b", re.I),
        re.compile(r"\bSephardi m\b", re.I),
        re.compile(r"\bThe Shulchan Aruch\b"),
        re.compile(r"\bThe Three Weeks\b"),
        re.compile(r"\bThree Weeks\b"),
        re.compile(r"Исполнитель:"),
        re.compile(r"Psalm5"),
    ],
    "fr": [
        re.compile(r"\bvacances physiques\b", re.I),
        re.compile(r"\best ravissant\b", re.I),
        re.compile(r"\bt rav\b", re.I),
        re.compile(r"D\. [A-Z][a-z]+ [a-z]"),
        re.compile(r"\bHigh Holiday\b", re.I),
        re.compile(r"\bMourners récite\b", re.I),
        re.compile(r"\bUng Shabbat\b", re.I),
        re.compile(r"plupartposkim", re.I),
        re.compile(r"\bG-d\b"),
        re.compile(r"^Bat mitzvah est quand", re.I),
    ],
    "ru": [
        re.compile(r"\bminyan\b", re.I),
        re.compile(r"\bHigh Holiday\b", re.I),
        re.compile(r"\bMourners recite\b", re.I),
        re.compile(r"\bhalachic\b", re.I),
        re.compile(r"А\.\w"),
        re.compile(r"\basofer\b", re.I),
        re.compile(r"правит миром"),
        re.compile(r"Оригинальное название"),
        re.compile(r"\bTalmud\b"),
        re.compile(r"\bThe chazzan\b", re.I),
        re.compile(r"bar mitzvah Возраст", re.I),
        re.compile(r"\bG-d\b"),
        re.compile(r"\.галаха\("),
        re.compile(r"запретить стрижки"),
        re.compile(r"Три недели - Три недели"),
        re.compile(r"Uchites o\b", re.I),
        re.compile(r"Znali li\b", re.I),
        re.compile(r"The Rambam\b"),
        re.compile(r"Рамбam\b"),
        re.compile(r"Say the Shema\b", re.I),
        re.compile(r"Ты знал", re.I),
    ],
}


def is_english_key(text: str) -> bool:
    if any("\u0590" <= c <= "\u05ff" for c in text):
        latin = sum(1 for c in text if c.isascii() and c.isalpha())
        return latin > len(text) * 0.15
    return bool(re.search(r"[A-Za-z]", text))


def ru_has_translit_garbage(tr: str) -> bool:
    if len(tr) < 40:
        return False
    latin = sum(1 for c in tr if c.isascii() and c.isalpha())
    cyrillic = sum(1 for c in tr if "\u0400" <= c <= "\u04ff")
    if cyrillic < 20 and latin < 15:
        return False
    if latin >= 8 and latin / max(latin + cyrillic, 1) > 0.05:
        return True
    if re.search(r"\b(Otkroyte|Uchites|Smotrite|Znali|Delайte|Ty znal|Kabb[aа]listy|govoryat|tsennoye)\b", tr, re.I):
        return True
    if re.search(r"[а-яА-ЯёЁ][A-Za-z]{3,}|[A-Za-z]{3,}[а-яА-ЯёЁ]", tr):
        return True
    if re.search(r"(с|в|к|у|о|и|на|от|до|из|по|за|при|для|без|над|под|перед|после|между|через|против|среди|вокруг|внутри|вне|вместо|кроме|согласно|благодаря|вопреки|навстречу|наподобие|вследствие|ввиду|вроде|включая|исключая|согласно|несмотря|относительно|применительно|соответственно|параллельно|одновременно|последовательно|постепенно|немедленно|срочно|внезапно|внезапно|внезапно)[А-ЯA-Z]", tr):
        return True
    glued = re.findall(r"[\u0400-\u04ff][A-Za-z]|[A-Za-z][\u0400-\u04ff]", tr)
    if len(glued) >= 2:
        return True
    return False


def is_bad(lang: str, en: str, tr: str) -> bool:
    if en in ALLOW_IDENTITY:
        return False
    if not is_english_key(en):
        return False
    if tr == en and len(en) > 2:
        return True
    for pat in BAD.get(lang, []):
        if pat.search(tr):
            return True
    if lang == "ru" and ru_has_translit_garbage(tr):
        return True
    if lang == "fr" and re.search(r"^Bat mitzvah est quand", tr):
        return True
    if lang == "ru" and re.search(r"\.галаха\(|запретить стрижки|Три недели - Три недели", tr):
        return True
    return False


def polish(lang: str, en: str, tr: str) -> str:
    tr = apply_subs(lang, tr)
    tr = restore_kotlin_templates(en, tr)
    tr = repair_placeholders(en, tr)
    tr = repair_translation(lang, tr)
    return tr


def main() -> None:
    install_packages()
    translators = {lang: get_translator(lang) for lang in LANGS}
    required = json.loads(CATALOG.read_text(encoding="utf-8"))["strings"]
    current = {
        lang: json.loads((COMPOSE / f"{lang}.json").read_text(encoding="utf-8"))["entries"]
        for lang in LANGS
    }

    out: dict[str, dict[str, str]] = {lang: {} for lang in LANGS}
    counts = {lang: 0 for lang in LANGS}

    for en in required:
        for lang in LANGS:
            tr = current[lang].get(en, en)
            if not is_bad(lang, en, tr):
                continue
            fresh = translate_text(translators[lang], en)
            fixed = polish(lang, en, fresh)
            if fixed != tr:
                out[lang][en] = fixed
                counts[lang] += 1

    OUT.write_text(json.dumps(out, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"wrote {OUT}")
    for lang in LANGS:
        print(f"  {lang}: {counts[lang]} retranslations")


if __name__ == "__main__":
    main()
