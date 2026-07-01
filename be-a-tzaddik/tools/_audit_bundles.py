#!/usr/bin/env python3
"""Audit compiled bundles for corruption artifacts."""
import json, re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
COMPOSE = ROOT / "shared/src/commonMain/composeResources/files/translations"
CATALOG = ROOT / "data/translation-catalog/strings.json"

required = json.loads(CATALOG.read_text(encoding="utf-8"))["strings"]

# Patterns that indicate machine-translation corruption
ARTIFACT_PATTERNS = [
    (r"PH\d+", "placeholder PH0/PH1/…"),
    (r"⟦\d+⟧", "argos bracket placeholder ⟦N⟧"),
    (r"\[\[TRANS:", "TRANS: marker"),
    (r"TRANSLATEME", "TRANSLATEME marker"),
    (r"&#\d+;", "HTML numeric entity"),
    (r"&amp;|&lt;|&gt;|&quot;", "HTML named entity"),
    (r"\bNaN\b", "NaN literal"),
    (r"\bundefined\b", "undefined literal"),
    (r"\bnull\b", "null literal"),
    (r"<[A-Z][A-Z0-9]*>", "XML/HTML tag"),
]

# English words that strongly indicate a non-English translation is corrupted
# (only flag if they appear surrounded by non-ASCII text)
ENGLISH_LEAK_WORDS = [
    "mirth", "tormentors", "captors", "praiseworthy", "wept",
    "spacious", "lovingkindness", "endureth", "feedest",
]

def is_hebrew_dominant(text: str) -> bool:
    he = sum(1 for c in text if '\u0590' <= c <= '\u05ff')
    letters = sum(1 for c in text if c.isalpha())
    return letters > 0 and he / letters > 0.3

def check_too_short(en: str, tr: str, lang: str) -> bool:
    """Translation suspiciously short vs source (possible truncation)."""
    if len(en) < 40:
        return False
    # Hebrew/Russian compress naturally; still flag extreme cases
    ratio = len(tr) / max(len(en), 1)
    if lang in ("he", "ru") and ratio < 0.25:
        return True
    if lang in ("es", "fr") and ratio < 0.35:
        return True
    return False

def check_same_as_english(en: str, tr: str) -> bool:
    """Translation is identical to English source (untranslated)."""
    if len(en) < 10:
        return False
    return en.strip() == tr.strip()

issues: dict[str, list[tuple[str, str, str]]] = {lang: [] for lang in ("he", "es", "fr", "ru")}

for lang in ("he", "es", "fr", "ru"):
    path = COMPOSE / f"{lang}.json"
    entries = json.loads(path.read_text(encoding="utf-8"))["entries"]

    for en_key in required:
        tr = entries.get(en_key, "")
        if not tr or tr == en_key:
            # untranslated fallback — only flag non-trivial strings
            if len(en_key) > 8:
                issues[lang].append((en_key[:60], tr[:60], "UNTRANSLATED fallback to English"))
            continue

        # Artifact pattern checks
        for pattern, label in ARTIFACT_PATTERNS:
            m = re.search(pattern, tr)
            if m:
                issues[lang].append((en_key[:70], tr[:80], f"artifact: {label} ({m.group()!r})"))
                break

        # English word leak in non-English translation
        if lang != "en":
            for word in ENGLISH_LEAK_WORDS:
                if re.search(r'\b' + word + r'\b', tr, re.IGNORECASE):
                    # Only flag if there's substantial non-ASCII content (so it's clearly a translation)
                    non_ascii = sum(1 for c in tr if ord(c) > 127)
                    if non_ascii > 10:
                        issues[lang].append((en_key[:70], tr[:100], f"English word leak: {word!r}"))
                        break

        # Truncation check
        if check_too_short(en_key, tr, lang):
            issues[lang].append((en_key[:70], tr[:80], f"suspiciously short ({len(en_key)} → {len(tr)} chars)"))

for lang, lang_issues in issues.items():
    # Separate untranslated from real corruption
    real = [(k, v, r) for k, v, r in lang_issues if not r.startswith("UNTRANSLATED")]
    untranslated = [(k, v, r) for k, v, r in lang_issues if r.startswith("UNTRANSLATED")]
    print(f"\n{'='*60}")
    print(f"  {lang.upper()} — {len(real)} corruption issues, {len(untranslated)} untranslated strings")
    print(f"{'='*60}")
    if real:
        print("  CORRUPTION:")
        for k, v, reason in real:
            print(f"    [{reason}]")
            print(f"      EN: {k}")
            print(f"      TR: {v}")
    if untranslated:
        print(f"  UNTRANSLATED ({len(untranslated)} — first 10):")
        for k, _, _ in untranslated[:10]:
            print(f"    {k!r}")
        if len(untranslated) > 10:
            print(f"    ... and {len(untranslated)-10} more")
