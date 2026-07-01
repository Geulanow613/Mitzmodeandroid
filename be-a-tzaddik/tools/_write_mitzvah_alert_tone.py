#!/usr/bin/env python3
"""Engaging native tone for Mitzvah Me / Did-you-know alert strings."""

from __future__ import annotations

import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CATALOG = ROOT / "data" / "translation-catalog" / "strings.json"
OUT = ROOT / "data" / "translation-catalog" / "human" / "mitzvah_alert_tone.json"
LANGS = ("he", "es", "fr", "ru")

sys.path.insert(0, str(ROOT / "tools"))
from _mitzvah_alert_tone_data import FULL_REWRITES  # noqa: E402
from compile_full_bundled import load_human, load_legacy, load_shards, repair_translation  # noqa: E402

# Order matters — longer first.
TONE_REPLACEMENTS: dict[str, list[tuple[str, str]]] = {
    "he": [
        ("Did you know?", "ידעתם?"),
        ("הידעת?", "ידעתם?"),
        ("האם ידעתם?", "ידעתם?"),
        ("Today's mission:", "משימה להיום:"),
        ("משימת היום:", "משימה להיום:"),
        ("Today's challenge:", "אתגר להיום:"),
        ("Here's your mission:", "המשימה שלכם:"),
        ("Here's your challenge:", "האתגר שלכם:"),
        ("Your mission:", "המשימה שלכם:"),
        ("Here's something incredible:", "וזה מטורף:"),
        ("Here's something amazing:", "וזה מדהים:"),
        ("Here's something fascinating:", "וזה מרתק:"),
        ("Here's something powerful:", "וזה חזק:"),
        ("Here's something practical:", "וזה מעשי:"),
        ("Here's something cool:", "וזה מגניב:"),
        ("Here's something beautiful:", "וזה יפה:"),
        ("Here's the key:", "והנה המפתח:"),
        ("Here's the amazing lesson:", "והנה השיעור המדהים:"),
        ("Here's a classic example:", "דוגמה קלאסית:"),
        ("The lesson?", "השיעור?"),
        ("Discover the letter", "גלו את האות"),
        ("גלה את המכתב", "גלו את האות"),
        ("גלה את האות", "גלו את האות"),
    ],
    "es": [
        ("Did you know?", "¿Sabías que?"),
        ("¿Lo sabías?", "¿Sabías que?"),
        ("Today's mission:", "Reto del día:"),
        ("Today's challenge:", "Reto del día:"),
        ("Here's your mission:", "Tu reto:"),
        ("Here's your challenge:", "Tu reto:"),
        ("Your mission:", "Tu reto:"),
        ("Misión de hoy:", "Reto del día:"),
        ("Misón de hoy:", "Reto del día:"),
        ("Misión:", "Reto del día:"),
        ("Here's something incredible:", "Y esto es increíble:"),
        ("Here's something amazing:", "Y esto es increíble:"),
        ("Here's something fascinating:", "Y esto es fascinante:"),
        ("Here's something powerful:", "Y esto es poderoso:"),
        ("Here's something practical:", "Algo práctico:"),
        ("Here's something cool:", "Y esto mola:"),
        ("Here's something beautiful:", "Y esto es hermoso:"),
        ("Here's the key:", "La clave:"),
        ("Here's the amazing lesson:", "La lección increíble:"),
        ("Here's a classic example:", "Un ejemplo clásico:"),
        ("The lesson?", "¿La lección?"),
        ("Descubre la carta", "Descubre la letra"),
        ("la carta ", "la letra "),
        ("Discover the letter", "Descubre la letra"),
    ],
    "fr": [
        ("Did you know?", "Tu savais ?"),
        ("Today's mission:", "Défi du jour :"),
        ("Today's challenge:", "Défi du jour :"),
        ("Here's your mission:", "Ton défi :"),
        ("Here's your challenge:", "Ton défi :"),
        ("Your mission:", "Ton défi :"),
        ("Mission d'aujourd'hui :", "Défi du jour :"),
        ("Mission du jour :", "Défi du jour :"),
        ("La mission d'aujourd'hui :", "Défi du jour :"),
        ("Here's something incredible:", "Et c'est fou :"),
        ("Here's something amazing:", "Et c'est incroyable :"),
        ("Here's something fascinating:", "Et c'est fascinant :"),
        ("Here's something powerful:", "Et c'est puissant :"),
        ("Here's something practical:", "Concrètement :"),
        ("Here's something cool:", "Et c'est cool :"),
        ("Here's something beautiful:", "Et c'est beau :"),
        ("Here's the key:", "La clé :"),
        ("Here's the amazing lesson:", "La leçon incroyable :"),
        ("Here's a classic example:", "Exemple classique :"),
        ("The lesson?", "La leçon ?"),
        ("Le saviez-vous ?", "Tu savais ?"),
        ("Tu le savais ?", "Tu savais ?"),
        ("Saviez-vous ?", "Tu savais ?"),
        ("Discover the letter", "Découvre la lettre"),
    ],
    "ru": [
        ("Did you know?", "А знаешь?"),
        ("Today's mission:", "Задание на сегодня:"),
        ("Today's challenge:", "Вызов на сегодня:"),
        ("Here's your mission:", "Твоё задание:"),
        ("Here's your challenge:", "Твой вызов:"),
        ("Your mission:", "Твоё задание:"),
        ("Миссия на сегодня:", "Задание на сегодня:"),
        ("Сегодняшняя миссия:", "Задание на сегодня:"),
        ("Here's something incredible:", "И это невероятно:"),
        ("Here's something amazing:", "И это потрясающе:"),
        ("Here's something fascinating:", "И это увлекательно:"),
        ("Here's something powerful:", "И это мощно:"),
        ("Here's something practical:", "Практично:"),
        ("Here's something cool:", "И это круто:"),
        ("Here's something beautiful:", "И это прекрасно:"),
        ("Here's the key:", "Вот ключ:"),
        ("Here's the amazing lesson:", "Урок потрясающий:"),
        ("Here's a classic example:", "Классический пример:"),
        ("The lesson?", "Урок?"),
        ("Знаете ли вы?", "А знаешь?"),
        ("Знаете ли?", "А знаешь?"),
        ("Знаешь ли?", "А знаешь?"),
        ("Откройте письмо", "Открой букву"),
        ("Откройте для себя букву", "Открой букву"),
        ("Discover the letter", "Открой букву"),
        ("( the)", ""),
        (" the)", ")"),
    ],
}


ALERT_OPENER = re.compile(
    r"Did you know|Today's mission|Today's challenge|Your mission:|"
    r"Here's something|Here's your mission|Here's your challenge|Here's the key|"
    r"Discover the letter|Discover the (power|secret|righteous|deeper)|"
    r"Walk in G-d|Be an Amen|Celebrate the gift|Connect through|Crown yourself|"
    r"Gather for inspiration|Guard your|Learn about the|Rise above|Embrace the|"
    r"Honor Torah|Experience the|Explore the|Dive into|Set your Torah|"
    r"Spread the joy|Practice gratitude|Get a Chumash|Be a judge|Relive the most|"
    r"Appreciate life|Beware the fire|Ever wonder|Learn about [A-Z]|"
    r"Be a |Become a |Try to |Take a moment|Share the |Help someone|"
    r"Make someone's|Strengthen your|Deepen your|Unlock the|Master the|"
    r"Challenge yourself|Reflect on|Remember when|Think about",
    re.I,
)


def is_alert_key(key: str) -> bool:
    if len(key) < 40 or key.startswith("Tap the"):
        return False
    if ALERT_OPENER.search(key):
        return True
    if "Did you know" in key or "Today's mission" in key:
        return True
    if "Today's challenge:" in key or "Your mission:" in key:
        return True
    for phrase in (
        "Here's something amazing",
        "Here's something cool",
        "Here's something powerful",
        "Here's something beautiful",
        "Here's something incredible",
        "Here's the amazing lesson",
    ):
        if phrase in key:
            return True
    if re.match(r"^Discover the letter", key):
        return True
    if re.match(r"^Discover the (power|secret|righteous|deeper)", key):
        return True
    if re.match(r"^Dive into ", key) and (
        "Did you know" in key or "Today's mission" in key or "Here's something" in key
    ):
        return True
    return False


def apply_tone(text: str, lang: str) -> str:
    out = text
    for old, new in TONE_REPLACEMENTS.get(lang, []):
        out = out.replace(old, new)
    if lang == "he":
        out = re.sub(r"\bD-s\b", "ה'", out)
        out = re.sub(r"\bG-d\b", "ה'", out)
    elif lang == "es":
        out = re.sub(r"\bG-d\b", "D-s", out)
    elif lang == "fr":
        out = re.sub(r"\bG-d\b", "D.", out)
        out = re.sub(r"\bD-s\b", "D.", out)
    elif lang == "ru":
        out = re.sub(r"\bG-d\b", "В-г", out)
        out = re.sub(r"\bD-s\b", "В-г", out)
    return out


def badness_score(lang: str, en: str, tr: str) -> int:
    if not tr or tr == en:
        return 1000
    score = 0
    literals = {
        "he": ["Did you know", "Today's mission", "הידעת?", "משימת היום", "גלה את המכתב"],
        "es": ["Did you know", "Today's mission", "Misión de hoy", "la carta Dalet"],
        "fr": ["Did you know", "Today's mission", "Mission d'", "Saviez-vous"],
        "ru": ["Did you know", "Today's mission", "Знаете ли", "Миссия на сегодня", "( the)", " the)"],
    }
    for m in literals.get(lang, []):
        if m in tr:
            score += 40
    if lang == "ru" and re.search(r"\bthe\b", tr, re.I):
        score += 30
    return score


def load_base_entries() -> dict[str, dict[str, str]]:
    """Bundled translations without mitzvah_alert_tone (human loader may include it)."""
    entries: dict[str, dict[str, str]] = {lang: {} for lang in LANGS}
    entries["he"].update(load_legacy().get("he", {}))
    entries["he"].update(load_shards().get("he", {}))
    human = load_human()
    for lang in LANGS:
        entries[lang].update(load_legacy().get(lang, {}))
        entries[lang].update(load_shards().get(lang, {}))
        for k, v in human.get(lang, {}).items():
            if k not in entries[lang]:
                entries[lang][k] = v
    compose = ROOT / "shared/src/commonMain/composeResources/files/translations"
    for lang in LANGS:
        p = compose / f"{lang}.json"
        if p.is_file():
            entries[lang].update(json.loads(p.read_text(encoding="utf-8")).get("entries", {}))
    hebrew_fixes = ROOT / "data/translation-catalog/shards/hebrew_fixes.json"
    if hebrew_fixes.is_file():
        entries["he"].update(json.loads(hebrew_fixes.read_text(encoding="utf-8")).get("he", {}))
    return entries


def main() -> None:
    strings = json.loads(CATALOG.read_text(encoding="utf-8"))["strings"]
    base = load_base_entries()
    alert_keys = sorted(k for k in strings if is_alert_key(k))

    out: dict[str, dict[str, str]] = {lang: {} for lang in LANGS}
    full_rewrite_counts = {lang: 0 for lang in LANGS}
    template_counts = {lang: 0 for lang in LANGS}

    # Score worst keys per language for reporting
    worst_per_lang: dict[str, list[tuple[int, str]]] = {lang: [] for lang in LANGS}

    for key in alert_keys:
        for lang in LANGS:
            if key in FULL_REWRITES and lang in FULL_REWRITES[key]:
                out[lang][key] = FULL_REWRITES[key][lang]
                full_rewrite_counts[lang] += 1
                continue
            candidate = base.get(lang, {}).get(key, "")
            if not candidate or candidate == key:
                continue
            if lang in ("es", "fr", "ru"):
                candidate = repair_translation(lang, candidate)
            toned = apply_tone(candidate, lang)
            if toned != candidate:
                template_counts[lang] += 1
            out[lang][key] = toned
            worst_per_lang[lang].append((badness_score(lang, key, candidate), key))

    OUT.parent.mkdir(parents=True, exist_ok=True)
    OUT.write_text(json.dumps(out, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")

    total = {lang: len(out[lang]) for lang in LANGS}
    print(f"wrote {OUT}")
    print(f"alert keys scanned: {len(alert_keys)}")
    print(f"entries per language: {total}")
    print(f"full rewrites per language: {full_rewrite_counts}")
    print(f"template fixes per language: {template_counts}")

    # Sample before/after
    samples: list[tuple[str, str, str, str]] = []
    for lang in LANGS:
        scored = sorted(worst_per_lang[lang], reverse=True)
        for _, key in scored[:2]:
            before = base.get(lang, {}).get(key, key)[:120]
            after = out[lang].get(key, "")[:120]
            if before != after:
                samples.append((lang, key[:60] + "...", before, after))
    print("\n--- samples before/after ---")
    for lang, key_short, before, after in samples[:8]:
        print(f"\n[{lang}] {key_short}")
        print(f"  before: {before}...")
        print(f"  after:  {after}...")


if __name__ == "__main__":
    main()
