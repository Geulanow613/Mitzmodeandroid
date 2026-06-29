#!/usr/bin/env python3
"""Audit bundled translations for Argos/MT quality issues."""

from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CATALOG = ROOT / "data" / "translation-catalog" / "strings.json"
COMPOSE = ROOT / "shared" / "src" / "commonMain" / "composeResources" / "files" / "translations"
OUT = ROOT / "data" / "translation-catalog" / "quality-report.json"
LANGS = ("he", "es", "fr", "ru")

# Intentional identity keys (not translation failures)
ALLOW_IDENTITY = {
    "\\s*/\\s*",
    "(?i)(?<![A-Za-z0-9'])${Regex.escape(word)}(?![A-Za-z0-9'])",
    "$mitzvotCount",
    "www.beardy.top",
    "https://www.beardy.top",
    "$translatedSwitchedTo $languageName.\n$translatedPleaseNote",
    "Rav",
}

PLACEHOLDER_PATTERNS = [
    re.compile(r"\$\{[^}]+\}"),
    re.compile(r"\$[A-Za-z_][A-Za-z0-9_]*"),
]

# Obvious UI leaks (avoid THE/AND/FAST/HOLIDAY false positives in halacha text)
ENGLISH_LEAK = re.compile(
    r"\b(OWN|BELOW|MAKEUP)\b",
    re.I,
)
HIGH_HOLIDAY_LEAK = re.compile(r"\bHigh Holidays?\b", re.I)

# Known Argos disaster substrings in es/fr/ru
DISASTER_PATTERNS: list[tuple[str, re.Pattern[str], str]] = [
    ("vacation_for_holiday_es", re.compile(r"\bvacaciones?\b", re.I), "Jewish holiday mistranslated as vacation"),
    ("vacation_for_holiday_fr", re.compile(r"\bvacances?\b", re.I), "Jewish holiday mistranslated as vacation"),
    ("vacation_for_holiday_ru", re.compile(r"\bотпуск\b", re.I), "Jewish holiday mistranslated as vacation"),
    ("makeup_prayer_es", re.compile(r"\bmaquillaje\b", re.I), "makeup prayer → cosmetics"),
    ("makeup_prayer_fr", re.compile(r"\bmaquillage\b", re.I), "makeup prayer → cosmetics"),
    ("tashlumin_es", re.compile(r"\bmaquillaje\b.*tashlumin|tashlumin.*maquillaje", re.I), "tashlumin mistranslated"),
    ("mobile_services_es", re.compile(r"\bservicios móviles\b", re.I), "moving services mistranslation"),
    ("fast_rapid_es", re.compile(r"\b(rápido|rápida)\b.*\b(ayuno|ayunar)\b|\b(ayuno|ayunar)\b.*\b(rápido|rápida)\b", re.I), "fast day → rápido"),
    ("fast_rapid_es_title", re.compile(r"\bRápido de\b", re.I), "Fast of → Rápido de"),
    ("fast_minor_es", re.compile(r"\b(rápido|rápida)\s+menor\b", re.I), "minor fast → rápido menor"),
    ("fast_full_es", re.compile(r"\b(rápido|rápida)\s+lleno\b", re.I), "full fast → rápido lleno"),
    ("not_fast_day_es", re.compile(r"\bno un día rápido\b", re.I), "not a fast day → día rápido"),
    ("dwelling_morir_es", re.compile(r"\bMorir significa\b", re.I), "Dwelling → Morir"),
    ("semi_holiday_en_es", re.compile(r"\bsemi-holiday\b", re.I), "semi-holiday untranslated"),
    ("drawing_close_es", re.compile(r"\bdibujo cerca\b", re.I), "drawing close → dibujo"),
    ("dibujo_fr", re.compile(r"\bdessin\b", re.I), "drawing close literalism (FR)"),
]

HALACHA_TERMS = [
    "Shacharit", "Amidah", "minhag", "mitzvah", "Yom Tov", "tashlumin", "Shabbat", "Shulchan Aruch",
    "Birkat Hamazon", "Shema", "Maariv", "Mincha", "Kiddush", "Havdalah", "melacha", "poskim",
]

G_D_PATTERNS = {
    "he": re.compile(r"\bG-d\b|\bGod\b", re.I),
    "es": re.compile(r"\bG-d\b"),
    "fr": re.compile(r"\bG-d\b"),
    "ru": re.compile(r"\bG-d\b"),
}


def extract_placeholders(text: str) -> list[str]:
    found: list[str] = []
    for block in kotlin_template_blocks(text):
        found.append(block)
    for pat in PLACEHOLDER_PATTERNS:
        for m in pat.finditer(text):
            ph = m.group(0)
            if ph.startswith("${"):
                continue
            if not any(ph in block for block in found):
                found.append(ph)
    return found


def kotlin_template_blocks(text: str) -> list[str]:
    blocks: list[str] = []
    i = 0
    while i < len(text):
        if text.startswith("${", i):
            depth = 1
            j = i + 2
            while j < len(text) and depth > 0:
                if text[j] == "{":
                    depth += 1
                elif text[j] == "}":
                    depth -= 1
                j += 1
            blocks.append(text[i:j])
            i = j
        else:
            i += 1
    return blocks


def placeholders_ok(en: str, tr: str) -> bool:
    en_vars = sorted(set(re.findall(r"\$[A-Za-z_][A-Za-z0-9_]*", en)))
    tr_vars = sorted(set(re.findall(r"\$[A-Za-z_][A-Za-z0-9_]*", tr)))
    if en_vars != tr_vars:
        return False
    return len(kotlin_template_blocks(en)) == len(kotlin_template_blocks(tr))


def is_hebrew_source(text: str) -> bool:
    return any("\u0590" <= c <= "\u05ff" for c in text) and not any(c.isascii() and c.isalpha() for c in text[:20])


def load_entries() -> dict[str, dict[str, str]]:
    out: dict[str, dict[str, str]] = {}
    for lang in LANGS:
        path = COMPOSE / f"{lang}.json"
        out[lang] = json.loads(path.read_text(encoding="utf-8"))["entries"]
    return out


def audit() -> dict:
    required = json.loads(CATALOG.read_text(encoding="utf-8"))["strings"]
    entries = load_entries()
    issues: list[dict] = []

    for en in required:
        for lang in LANGS:
            tr = entries[lang].get(en, en)
            if en in ALLOW_IDENTITY:
                continue

            # CRITICAL: broken placeholders
            if extract_placeholders(en) and not placeholders_ok(en, tr):
                issues.append({
                    "severity": "CRITICAL",
                    "lang": lang,
                    "key": en[:200] + ("..." if len(en) > 200 else ""),
                    "key_full": en,
                    "reason": "placeholder_mismatch",
                    "detail": f"en={extract_placeholders(en)} tr={extract_placeholders(tr)}",
                    "current": tr[:300],
                })

            # CRITICAL: regex / template corruption
            if en.startswith("(?i)") and tr != en:
                issues.append({
                    "severity": "CRITICAL",
                    "lang": lang,
                    "key": en[:120],
                    "key_full": en,
                    "reason": "regex_template_changed",
                    "current": tr[:200],
                })

            if lang == "he" and is_hebrew_source(en):
                continue  # Hebrew liturgy identity OK for he

            # CRITICAL: identical to English on long strings
            if tr == en and len(en) > 40 and en not in ALLOW_IDENTITY:
                if is_hebrew_source(en) and lang in ("es", "fr", "ru"):
                    pass  # Hebrew liturgy keys stay Hebrew in all bundles
                elif lang != "he" or not is_hebrew_source(en):
                    issues.append({
                        "severity": "CRITICAL",
                        "lang": lang,
                        "key": en[:200] + ("..." if len(en) > 200 else ""),
                        "key_full": en,
                        "reason": "untranslated_long",
                        "current": tr[:200],
                    })

            if lang == "he":
                continue

            # CRITICAL: English leaks (only for non-hebrew source in es/fr/ru)
            if not is_hebrew_source(en):
                for m in ENGLISH_LEAK.finditer(tr):
                    issues.append({
                        "severity": "CRITICAL",
                        "lang": lang,
                        "key": en[:120],
                        "key_full": en,
                        "reason": "english_leak",
                        "detail": m.group(0),
                        "current": tr[:300],
                    })
                if HIGH_HOLIDAY_LEAK.search(tr):
                    issues.append({
                        "severity": "CRITICAL",
                        "lang": lang,
                        "key": en[:120],
                        "key_full": en,
                        "reason": "english_leak",
                        "detail": "High Holiday",
                        "current": tr[:300],
                    })

                for name, pat, msg in DISASTER_PATTERNS:
                    lang_suffix = name.rsplit("_", 1)[-1]
                    if lang_suffix not in ("es", "fr", "ru") or lang != lang_suffix:
                        continue
                    if not pat.search(tr):
                        continue
                    # Context filter: vacaciones near Jewish holiday words
                    if "vacation" in name or "vacances" in name or "отпуск" in msg:
                        if not re.search(
                            r"holiday|Yom Tov|chag|festival|Pesach|Sukkot|Shavuot|Chanukah|Purim|Rosh|Kippur|Shabbat|Shabat|Chabbat|fête|fiesta",
                            en,
                            re.I,
                        ):
                            continue
                        if re.search(r"physical vacation|vacaciones físicas|vacances physiques", tr, re.I):
                            continue
                    if name in ("fast_rapid_es", "fast_rapid_es_title", "fast_minor_es", "fast_full_es", "not_fast_day_es"):
                        if re.search(r"\bquick\b", en, re.I) and not re.search(r"\bfast\b", en, re.I):
                            continue
                    if name == "drawing_close_es" and not re.search(r"drawing close", en, re.I):
                        continue
                    issues.append({
                        "severity": "CRITICAL",
                        "lang": lang,
                        "key": en[:120],
                        "key_full": en,
                        "reason": name,
                        "detail": msg,
                        "current": tr[:300],
                    })

            # IMPORTANT: G-d convention
            if G_D_PATTERNS[lang].search(tr) and lang != "he":
                issues.append({
                    "severity": "IMPORTANT",
                    "lang": lang,
                    "key": en[:120],
                    "key_full": en,
                    "reason": "g_d_not_localized",
                    "suggested_fix": {"es": "Dios", "fr": "D.", "ru": "Б-г"}.get(lang, ""),
                    "current": tr[:200],
                })

    # Dedupe
    seen = set()
    deduped = []
    for item in issues:
        k = (item["severity"], item["lang"], item.get("key_full", item["key"]), item["reason"])
        if k in seen:
            continue
        seen.add(k)
        deduped.append(item)

    by_sev = {"CRITICAL": 0, "IMPORTANT": 0, "LOW": 0}
    for i in deduped:
        by_sev[i["severity"]] = by_sev.get(i["severity"], 0) + 1

    report = {
        "total_strings": len(required),
        "issues": deduped,
        "summary": by_sev,
        "coverage": {
            lang: sum(
                1 for s in required
                if entries[lang].get(s, s) != s or s in ALLOW_IDENTITY or (lang == "he" and is_hebrew_source(s))
            )
            for lang in LANGS
        },
    }
    return report


def main() -> None:
    report = audit()
    OUT.write_text(json.dumps(report, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"wrote {OUT}")
    print("summary:", report["summary"])
    print("coverage:", report["coverage"])
    crit = [i for i in report["issues"] if i["severity"] == "CRITICAL"]
    print(f"CRITICAL samples ({min(10, len(crit))}/{len(crit)}):")
    for i in crit[:10]:
        print(f"  [{i['lang']}] {i['reason']}: {i['key'][:80]}")


if __name__ == "__main__":
    main()
