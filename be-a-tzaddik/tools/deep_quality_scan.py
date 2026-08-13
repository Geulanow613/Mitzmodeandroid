#!/usr/bin/env python3
"""Find remaining translation garbage in bundled JSON."""

from __future__ import annotations

import json
import re
from collections import defaultdict
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CATALOG = ROOT / "data" / "translation-catalog/strings.json"
COMPOSE = ROOT / "shared/src/commonMain/composeResources/files/translations"
OUT = ROOT / "data/translation-catalog/deep-quality-scan.json"
LANGS = ("he", "es", "fr", "ru")

PATTERNS: dict[str, list[tuple[str, re.Pattern[str]]]] = {
    "all": [
        ("triple_dash", re.compile(r" - - - ")),
        ("double_space_ellipsis", re.compile(r"\.\.\.")),
    ],
    "es": [
        ("t_rav", re.compile(r"t rav(?!iss)", re.I)),
        ("g_rav", re.compile(r"g rav(?!iss)", re.I)),
        ("d_s_possessive", re.compile(r"D-s 's")),
        ("english_mourners", re.compile(r"\bMourners recite\b")),
        ("halachic_untranslated", re.compile(r"\bhalachic\b", re.I)),
        ("open_english", re.compile(r"\bOpen Pirkei\b")),
    ],
    "fr": [
        ("t_rav", re.compile(r"t rav(?!iss)", re.I)),
        ("g_rav", re.compile(r"g rav(?!iss)", re.I)),
        ("d_dot_space", re.compile(r"D\. [A-Z]")),
        ("ung_shabbat", re.compile(r"\bUng Shabbat\b")),
        ("poskim_glued", re.compile(r"plupartposkim")),
    ],
    "ru": [
        ("a_dot_cyrillic", re.compile(r"А\.\w")),
        ("a_minyan", re.compile(r"\ba minyan\b", re.I)),
        ("asofer", re.compile(r"\basofer\b", re.I)),
        ("english_mourners", re.compile(r"\bMourners recite\b", re.I)),
        ("halachic_untranslated", re.compile(r"\bhalachic\b", re.I)),
        ("rules_world", re.compile(r"правит миром")),
    ],
    "he": [
        ("tag_garbage", re.compile(r"תגית:")),
        ("latin_shacharit", re.compile(r"\bShacharit\b")),
        ("g_d_leak", re.compile(r"\bG-d\b")),
        ("english_clause", re.compile(r"\bThe Talmud\b")),
    ],
}


def english_ratio(text: str) -> float:
    letters = [c for c in text if c.isalpha()]
    if not letters:
        return 0.0
    return sum(1 for c in letters if c.isascii()) / len(letters)


def main() -> None:
    required = json.loads(CATALOG.read_text(encoding="utf-8"))["strings"]
    entries = {
        lang: json.loads((COMPOSE / f"{lang}.json").read_text(encoding="utf-8"))["entries"]
        for lang in LANGS
    }

    hits: dict[str, dict[str, list[dict]]] = defaultdict(lambda: defaultdict(list))
    mostly_english: dict[str, list[dict]] = defaultdict(list)

    for en in required:
        for lang in LANGS:
            tr = entries[lang].get(en, en)
            if tr == en:
                continue
            pats = PATTERNS.get("all", []) + PATTERNS.get(lang, [])
            for name, pat in pats:
                if pat.search(tr):
                    hits[lang][name].append({"key": en, "snippet": tr[:240]})
            if lang != "he" and len(en) > 120 and english_ratio(tr) > 0.5:
                mostly_english[lang].append(
                    {"key": en, "ratio": round(english_ratio(tr), 2), "snippet": tr[:240]}
                )

    summary = {
        lang: {name: len(items) for name, items in sorted(hits[lang].items())}
        for lang in LANGS
    }
    report = {
        "summary": summary,
        "hits": {lang: dict(hits[lang]) for lang in LANGS},
        "mostly_english": {lang: mostly_english[lang][:80] for lang in LANGS},
        "mostly_english_counts": {lang: len(mostly_english[lang]) for lang in LANGS},
    }
    OUT.write_text(json.dumps(report, ensure_ascii=False, indent=2), encoding="utf-8")
    print("wrote", OUT)
    for lang in LANGS:
        print(lang, summary.get(lang, {}), "mostly_en", report["mostly_english_counts"][lang])


if __name__ == "__main__":
    main()
