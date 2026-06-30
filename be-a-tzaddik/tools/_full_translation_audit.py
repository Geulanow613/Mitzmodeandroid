#!/usr/bin/env python3
"""Actionable full-app translation audit — real leaks and Argos disasters, not false positives."""
from __future__ import annotations

import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CATALOG = ROOT / "data" / "translation-catalog" / "strings.json"
COMPOSE = ROOT / "shared" / "src" / "commonMain" / "composeResources" / "files" / "translations"
OUT = ROOT / "data" / "translation-catalog" / "full-audit.json"
LANGS = ("he", "es", "fr", "ru")

ALLOW_IDENTITY = {
    "\\s*/\\s*",
    "(?i)(?<![A-Za-z0-9'])${Regex.escape(word)}(?![A-Za-z0-9'])",
    "$mitzvotCount",
    "www.beardy.top",
    "https://www.beardy.top",
    "$translatedSwitchedTo $languageName.\n$translatedPleaseNote",
    "Rav",
    "Normal",
    "Yom Tov — Shemini Atzeret",
    "Beardy Top Productions",
    "Mitz Mode!",
}

# (lang, pattern, reason) — only flag when English source context matches
DISASTERS: list[tuple[str | None, re.Pattern[str], str]] = [
    (None, re.compile(r"⟦"), "placeholder_corruption"),
    ("es", re.compile(r"\bmapa para la aventura\b", re.I), "davening_to_adventure"),
    ("es", re.compile(r"\bpicaz[oó]n\b", re.I), "bentching_to_itching"),
    ("es", re.compile(r"\bmaquillaje\b", re.I), "makeup_cosmetics"),
    ("es", re.compile(r"\bRápido de\b", re.I), "fast_day_rapid"),
    ("es", re.compile(r"\bMorir significa\b", re.I), "dwelling_to_die"),
    ("es", re.compile(r"\bdibujo cerca\b", re.I), "drawing_close_literal"),
    ("es", re.compile(r"\bsemi-holiday\b", re.I), "semi_holiday_english"),
    ("fr", re.compile(r"\bmaquillage\b", re.I), "makeup_cosmetics"),
    ("fr", re.compile(r"\bjeûne rapide\b", re.I), "fast_day_rapid"),
    ("es", re.compile(r"\bvacaciones\b", re.I), "vacation_for_holiday"),
    ("fr", re.compile(r"\bvacances\b", re.I), "vacation_for_holiday"),
    ("ru", re.compile(r"\bотпуск\b", re.I), "vacation_for_holiday"),
    ("es", re.compile(r"\bma\s+rav\s+ill", re.I), "rav_word_split"),
    ("es", re.compile(r"\bRopa desnuda\b"), "clothing_naked_literal"),
    ("es", re.compile(r"\bnuevas noticias\b", re.I), "bentcher_news"),
    ("es", re.compile(r"\bantes Dios\b"), "missing_de_before_dios"),
    ("es", re.compile(r"\bpacing\b"), "untranslated_pacing"),
    ("es", re.compile(r"âTM"), "unicode_corruption"),
    ("es", re.compile(r"Dios Es una"), "god_is_a_corruption"),
    (None, re.compile(r"h{20,}"), "h_corruption"),
    ("he", re.compile(r"^The [A-Z].{40,}"), "english_sentence_start_he"),
    ("ru", re.compile(r"^The [A-Z].{40,}"), "english_sentence_start_ru"),
    (None, re.compile(r"\bG-d\b"), "g_d_leak"),
    (None, re.compile(r"\bGod\b"), "god_leak"),
]


def is_hebrew_source(text: str) -> bool:
    letters = [c for c in text if c.isalpha()]
    if not letters:
        return False
    hebrew = sum(1 for c in letters if "\u0590" <= c <= "\u05ff")
    return hebrew / len(letters) > 0.35


def disaster_applies(reason: str, en: str, tr: str) -> bool:
    if reason == "vacation_for_holiday":
        if not re.search(r"holiday|Yom Tov|chag|festival|Pesach|Sukkot|Shavuot|Chanukah|Purim|Rosh|Kippur|Shabbat|fête|fiesta", en, re.I):
            return False
        if re.search(r"physical vacation|vacaciones físicas|vacances physiques", tr, re.I):
            return False
    if reason == "drawing_close_literal":
        if not re.search(r"drawing close|draw.*close", en, re.I):
            return False
    if reason == "god_leak" and re.search(r"\bGod\b.*\b(bless|thank|praise|faith|Creator)\b", en, re.I):
        return False  # English source mentions God
    return True


def main() -> None:
    strict = "--strict" in sys.argv
    required = json.loads(CATALOG.read_text(encoding="utf-8"))["strings"]
    entries = {
        lang: json.loads((COMPOSE / f"{lang}.json").read_text(encoding="utf-8"))["entries"]
        for lang in LANGS
    }

    fallbacks: dict[str, list[str]] = {lang: [] for lang in LANGS}
    disasters: list[dict] = []

    for en in required:
        for lang in LANGS:
            tr = entries[lang].get(en, en)
            if tr == en and en not in ALLOW_IDENTITY:
                if lang == "he" and is_hebrew_source(en):
                    continue
                if len(en) > 40:
                    fallbacks[lang].append(en)

            if lang == "he" and is_hebrew_source(en):
                continue

            for lang_filter, pat, reason in DISASTERS:
                if lang_filter is not None and lang_filter != lang:
                    continue
                if not pat.search(tr):
                    continue
                if not disaster_applies(reason, en, tr):
                    continue
                disasters.append({
                    "lang": lang,
                    "reason": reason,
                    "key": en[:120],
                    "snippet": tr[:160],
                })

    report = {
        "fallbacks_long": {k: len(v) for k, v in fallbacks.items()},
        "disaster_count": len(disasters),
        "disasters": disasters[:200],
        "disaster_by_reason": {},
    }
    for d in disasters:
        key = f"{d['lang']}:{d['reason']}"
        report["disaster_by_reason"][key] = report["disaster_by_reason"].get(key, 0) + 1

    OUT.write_text(json.dumps(report, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"Wrote {OUT.name}")
    print("Long English fallbacks:", report["fallbacks_long"])
    print("Disaster patterns:", report["disaster_count"])
    if disasters:
        print("\n=== Samples ===")
        for d in disasters[:25]:
            print(f"  [{d['lang']}] {d['reason']}: {d['key'][:70]}")
            print(f"    {d['snippet'][:100]}")

    if strict and disasters:
        raise SystemExit(f"Translation audit failed: {len(disasters)} disaster pattern(s)")

    if strict and any(report["fallbacks_long"].get(lang, 0) > 0 for lang in ("es", "fr", "ru")):
        raise SystemExit("Translation audit failed: es/fr/ru have untranslated long strings")


if __name__ == "__main__":
    main()
