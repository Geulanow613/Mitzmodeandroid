#!/usr/bin/env python3
"""Deep scan of compiled bundled translations for remaining quality issues."""

from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CATALOG = ROOT / "data/translation-catalog/strings.json"
COMPOSE = ROOT / "shared/src/commonMain/composeResources/files/translations"
OUT = ROOT / "data/translation-catalog/deep-scan.json"
LANGS = ("he", "es", "fr", "ru")

ALLOW_IDENTITY = {
    "\\s*/\\s*",
    "(?i)(?<![A-Za-z0-9'])${Regex.escape(word)}(?![A-Za-z0-9'])",
    "$mitzvotCount",
    "www.beardy.top",
    "https://www.beardy.top",
    "$translatedSwitchedTo $languageName.\n$translatedPleaseNote",
    "Rav",
}

DISASTER = {
    "es": [
        (r"\bvacaciones?\b", "vacation-for-holiday"),
        (r"\bmaquillaje\b", "makeup-cosmetics"),
        (r"\bservicios móviles\b", "mobile-services"),
        (r"\b(rápido|rápida)\b", "rapid-fast"),
        (r"\bdibujo\b", "drawing"),
        (r"\bG-d\b", "g-d"),
        (r"\bGod\b", "god"),
        (r"\bOWN\b", "own-leak"),
        (r"\bBELOW\b", "below-leak"),
        (r"\bHigh Holidays?\b", "high-holiday"),
        (r"\bholiday\b", "holiday-word"),
        (r"\bmakeup\b", "makeup"),
    ],
    "fr": [
        (r"\bvacances?\b", "vacation-for-holiday"),
        (r"\bmaquillage\b", "makeup-cosmetics"),
        (r"\bdessin\b", "drawing"),
        (r"\bG-d\b", "g-d"),
        (r"\bGod\b", "god"),
        (r"\bOWN\b", "own-leak"),
        (r"\bBELOW\b", "below-leak"),
        (r"\bHigh Holidays?\b", "high-holiday"),
        (r"\bmakeup\b", "makeup"),
    ],
    "ru": [
        (r"\bотпуск\b", "vacation-for-holiday"),
        (r"\bG-d\b", "g-d"),
        (r"\bGod\b", "god"),
        (r"\bOWN\b", "own-leak"),
        (r"\bBELOW\b", "below-leak"),
        (r"\bHigh Holidays?\b", "high-holiday"),
        (r"\bmakeup\b", "makeup"),
    ],
    "he": [
        (r"\bG-d\b", "g-d"),
        (r"\bGod\b", "god"),
    ],
}


def is_hebrew_source(text: str) -> bool:
    return any("\u0590" <= c <= "\u05ff" for c in text) and not any(
        c.isascii() and c.isalpha() for c in text[:20]
    )


def english_ratio(text: str) -> float:
    letters = [c for c in text if c.isalpha()]
    if not letters:
        return 0.0
    ascii_letters = sum(1 for c in letters if c.isascii())
    return ascii_letters / len(letters)


def main() -> None:
    required = json.loads(CATALOG.read_text(encoding="utf-8"))["strings"]
    entries = {
        lang: json.loads((COMPOSE / f"{lang}.json").read_text(encoding="utf-8"))["entries"]
        for lang in LANGS
    }

    issues: list[dict] = []
    english_fallbacks: dict[str, list[str]] = {lang: [] for lang in LANGS}
    short_untranslated: dict[str, list[str]] = {lang: [] for lang in LANGS}

    for en in required:
        for lang in LANGS:
            tr = entries[lang].get(en, en)

            if tr == en and en not in ALLOW_IDENTITY:
                if len(en) > 40 or (lang == "he" and not is_hebrew_source(en)):
                    english_fallbacks[lang].append(en)
                elif len(en) > 3 and lang != "he":
                    short_untranslated[lang].append(en)

            if lang == "he" and is_hebrew_source(en):
                continue

            for pat, reason in DISASTER.get(lang, []):
                if not re.search(pat, tr, re.I):
                    continue
                if reason == "vacation-for-holiday":
                    if not re.search(
                        r"holiday|Yom Tov|chag|festival|Pesach|Sukkot|Shavuot|Chanukah|Purim|Rosh|Kippur|Shabbat|fête|fiesta",
                        en,
                        re.I,
                    ):
                        continue
                    if re.search(r"physical vacation|vacaciones físicas|vacances physiques", tr, re.I):
                        continue
                if reason == "rapid-fast":
                    if not re.search(r"\bfast\b|ayuno|ayunar|צום|пост", en + tr, re.I):
                        continue
                if reason == "holiday-word":
                    if not re.search(r"holiday|Yom Tov|chag", en, re.I):
                        continue
                    if re.search(r"physical vacation", en, re.I):
                        continue
                if reason == "drawing":
                    if not re.search(r"drawing close|draw.*close", en, re.I):
                        continue
                issues.append({
                    "lang": lang,
                    "reason": reason,
                    "key": en[:120],
                    "key_full": en,
                    "snippet": tr[:200],
                })

            # Suspicious: long translation still mostly English (es/fr/ru)
            if lang != "he" and len(en) > 80 and tr != en and english_ratio(tr) > 0.55:
                if not is_hebrew_source(en):
                    issues.append({
                        "lang": lang,
                        "reason": "mostly_english",
                        "key": en[:120],
                        "key_full": en,
                        "snippet": tr[:200],
                        "ratio": round(english_ratio(tr), 2),
                    })

    report = {
        "english_fallbacks_long": {k: len(v) for k, v in english_fallbacks.items()},
        "english_fallback_keys": english_fallbacks,
        "short_untranslated": {k: len(v) for k, v in short_untranslated.items()},
        "short_untranslated_keys": short_untranslated,
        "pattern_issues": issues,
        "pattern_issue_count": len(issues),
    }
    OUT.write_text(json.dumps(report, ensure_ascii=False, indent=2), encoding="utf-8")
    print("wrote", OUT)
    print("english fallbacks (long):", report["english_fallbacks_long"])
    print("short untranslated:", report["short_untranslated"])
    print("pattern issues:", report["pattern_issue_count"])
    for lang in LANGS:
        keys = english_fallbacks[lang]
        if keys:
            print(f"\n=== {lang} english fallbacks ({len(keys)}) ===")
            for k in keys[:30]:
                print(" -", k[:100])


if __name__ == "__main__":
    main()
