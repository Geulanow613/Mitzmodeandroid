#!/usr/bin/env python3
"""Merge translation shards + legacy maps into composeResources bundled JSON."""

from __future__ import annotations

import json
from pathlib import Path

from translation_repairs import repair_translation

ROOT = Path(__file__).resolve().parents[1]
CATALOG = ROOT / "data" / "translation-catalog" / "strings.json"
SHARDS = ROOT / "data" / "translation-catalog" / "shards"
LEGACY = ROOT / "data" / "bundled-translations"
COMPOSE = ROOT / "shared" / "src" / "commonMain" / "composeResources" / "files" / "translations"
LANGS = ("he", "es", "fr", "ru")


def load_shards() -> dict[str, dict[str, str]]:
    merged: dict[str, dict[str, str]] = {lang: {} for lang in LANGS}
    if not SHARDS.exists():
        return merged
    priority = ["argos_full.json", "legacy_import.json"]
    paths = [SHARDS / name for name in priority if (SHARDS / name).exists()]
    paths += sorted(p for p in SHARDS.glob("batch_*.json"))
    paths += sorted(
        p for p in SHARDS.glob("*.json")
        if p.name not in priority and not p.name.startswith("batch_")
    )
    if (SHARDS / "manual_fixes.json").exists():
        paths.append(SHARDS / "manual_fixes.json")
    if (SHARDS / "quality_fixes.json").exists():
        paths.append(SHARDS / "quality_fixes.json")
    targeted = SHARDS / "targeted_fixes.json"
    if targeted.exists():
        paths.append(targeted)
    hebrew = SHARDS / "hebrew_fixes.json"
    if hebrew.exists():
        paths.append(hebrew)
    glossary_batch = SHARDS / "hebrew_glossary_batch.json"
    if glossary_batch.exists():
        paths.append(glossary_batch)
    es_fr_ru = SHARDS / "es_fr_ru_retranslate.json"
    if es_fr_ru.exists():
        paths.append(es_fr_ru)
    polish = SHARDS / "es_fr_ru_polish.json"
    if polish.exists():
        paths.append(polish)
    for path in paths:
        data = json.loads(path.read_text(encoding="utf-8"))
        for lang in LANGS:
            merged[lang].update(data.get(lang, {}))
    return merged


def load_legacy() -> dict[str, dict[str, str]]:
    out: dict[str, dict[str, str]] = {lang: {} for lang in LANGS}
    for lang in LANGS:
        path = LEGACY / f"{lang}.json"
        if path.exists():
            out[lang] = json.loads(path.read_text(encoding="utf-8")).get("entries", {})
    return out


def load_human() -> dict[str, dict[str, str]]:
    """Authoritative human translations — loaded last, wins over all shards."""
    merged: dict[str, dict[str, str]] = {lang: {} for lang in LANGS}
    human_dir = ROOT / "data" / "translation-catalog" / "human"
    if not human_dir.exists():
        return merged
    skip_argos = {"quality_fixes.json", "es_fr_ru_retranslate.json", "es_fr_ru_polish.json"}
    skip_work = {p.name for p in human_dir.glob("*_only.json")}
    skip_work |= {p.name for p in human_dir.glob("*_src.json")}
    skip_work |= {p.name for p in human_dir.glob("_*")}
    skip_argos_bulk = {p.name for p in human_dir.glob("content_long_*.json")}
    skip_argos_bulk |= {p.name for p in human_dir.glob("content_medium_*.json")}
    # Order matters — later entries overwrite earlier ones.
    # prayers_liturgy.json MUST be last so it wins over quality_overrides.
    priority_last_ordered = [
        "quality_overrides.json",
        "ui_templates.json",
        "prayers_liturgy.json",
    ]
    priority_last = set(priority_last_ordered)
    priority_he_fix = sorted(
        p.name for p in human_dir.glob("he_fix_*.json") if "_only" not in p.name and "_src" not in p.name
    )
    paths = sorted(
        p for p in human_dir.glob("*.json")
        if p.name not in skip_argos
        and p.name not in skip_work
        and p.name not in skip_argos_bulk
    )
    paths = [p for p in paths if p.name not in priority_last and p.name not in priority_he_fix]
    paths += [human_dir / n for n in priority_he_fix if (human_dir / n).is_file()]
    paths += [human_dir / n for n in priority_last_ordered if (human_dir / n).is_file()]
    for path in paths:
        data = json.loads(path.read_text(encoding="utf-8"))
        for lang in LANGS:
            merged[lang].update(data.get(lang, {}))
    return merged


def is_hebrew_source(text: str) -> bool:
    letters = [c for c in text if c.isalpha()]
    if not letters:
        return False
    hebrew = sum(1 for c in letters if "\u0590" <= c <= "\u05ff")
    return hebrew / len(letters) > 0.35


def is_passthrough_key(text: str) -> bool:
    if text in {
        "$mitzvotCount", "halachic_term", "www.beardy.top", "Beardy Top Productions",
        "https://www.beardy.top", "G.E.U.L.A",
        # Proper names / Hebrew terms used in all languages without translation
        "Purim", "Sofer", "Ba'al Teshuva", "Mitz Mode!", "Sefirat HaOmer",
    }:
        return True
    if text.startswith("(?i)") or text.startswith("$translated"):
        return True
    return text in {r"\s*/\s*"}


def count_missing(lang: str, required: list[str], entries: dict[str, str]) -> list[str]:
    missing: list[str] = []
    for s in required:
        tr = entries.get(s, s)
        if len(s) <= 2:
            continue
        if tr != s:
            continue
        if is_passthrough_key(s):
            continue
        if lang == "he" and is_hebrew_source(s):
            continue
        missing.append(s)
    return missing


def main() -> None:
    required = json.loads(CATALOG.read_text(encoding="utf-8"))["strings"]
    shards = load_shards()
    legacy = load_legacy()
    human = load_human()

    COMPOSE.mkdir(parents=True, exist_ok=True)
    for lang in LANGS:
        entries: dict[str, str] = {}
        entries.update(legacy.get(lang, {}))
        entries.update(shards.get(lang, {}))
        entries.update(human.get(lang, {}))
        if lang in ("es", "fr", "ru"):
            entries = {k: repair_translation(lang, v) for k, v in entries.items()}
        missing = count_missing(lang, required, entries)
        translated = len(required) - len(missing)
        payload = {
            "version": 2,
            "language": lang,
            "entries": {s: entries.get(s, s) for s in required},
        }
        dest = COMPOSE / f"{lang}.json"
        dest.write_text(json.dumps(payload, ensure_ascii=False, indent=2), encoding="utf-8")
        (LEGACY / f"{lang}.json").write_text(
            json.dumps(payload, ensure_ascii=False, indent=2), encoding="utf-8"
        )
        print(f"{lang}: {translated}/{len(required)} translated, {len(missing)} fallback English")


if __name__ == "__main__":
    main()
