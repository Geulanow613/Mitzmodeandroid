#!/usr/bin/env python3
"""Translate checklist explainer template keys to es/fr/ru (Argos + quality repairs)."""
from __future__ import annotations

import json
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
TOOLS = ROOT / "tools"
EXPLAINERS = ROOT / "data" / "translation-catalog" / "human" / "checklist_explainers.json"
CATALOG = ROOT / "data" / "translation-catalog" / "strings.json"
LANGS = ("es", "fr", "ru")

sys.path.insert(0, str(TOOLS))
from apply_quality_fixes import apply_subs, repair_placeholders, restore_kotlin_templates  # noqa: E402
from compile_full_bundled import (  # noqa: E402
    count_missing,
    load_human,
    load_legacy,
    load_shards,
    repair_translation,
)
from generate_argos_translations import get_translator, install_packages, translate_text  # noqa: E402
from _write_explainer_hebrew import load_keys, HE  # noqa: E402


def polish(lang: str, en: str, tr: str) -> str:
    tr = apply_subs(lang, tr)
    tr = restore_kotlin_templates(en, tr)
    tr = repair_placeholders(en, tr)
    return repair_translation(lang, tr)


def keys_to_translate() -> list[str]:
    """English catalog keys for explainers that need es/fr/ru."""
    load_keys()
    catalog = json.loads(CATALOG.read_text(encoding="utf-8"))["strings"]
    wanted: set[str] = set(HE.keys())
    for lang in LANGS:
        entries: dict[str, str] = {}
        entries.update(load_legacy().get(lang, {}))
        entries.update(load_shards().get(lang, {}))
        entries.update(load_human().get(lang, {}))
        entries = {k: repair_translation(lang, v) for k, v in entries.items()}
        wanted.update(count_missing(lang, catalog, entries))
    return sorted(wanted)


def main() -> None:
    install_packages()
    translators = {lang: get_translator(lang) for lang in LANGS}
    keys = keys_to_translate()
    existing = (
        json.loads(EXPLAINERS.read_text(encoding="utf-8"))
        if EXPLAINERS.exists()
        else {"he": {}}
    )
    counts = {lang: 0 for lang in LANGS}

    # Short UI labels that Argos mishandles — translate manually.
    manual: dict[str, dict[str, str]] = {
        "Normal": {"es": "Normal", "fr": "Normal", "ru": "Обычный"},
        "Yom Tov — Shemini Atzeret": {
            "es": "Yom Tov — Shemini Atzeret",
            "fr": "Yom Tov — Chemini Atseret",
            "ru": "Йом Тов — Шмини Ацерет",
        },
    }

    for lang in LANGS:
        lang_entries = dict(existing.get(lang, {}))
        for en in keys:
            existing_tr = lang_entries.get(en)
            if existing_tr and existing_tr != en:
                repaired = polish(lang, en, existing_tr)
                if repaired != en:
                    continue
            raw = translate_text(translators[lang], en)
            lang_entries[en] = polish(lang, en, raw)
            counts[lang] += 1
        existing[lang] = lang_entries

    for en, by_lang in manual.items():
        for lang in LANGS:
            existing.setdefault(lang, {})[en] = by_lang[lang]

    EXPLAINERS.write_text(json.dumps(existing, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"Wrote explainer translations to {EXPLAINERS.name}")
    for lang in LANGS:
        print(f"  {lang}: {counts[lang]} new/upgraded entries ({len(existing[lang])} total)")


if __name__ == "__main__":
    main()
