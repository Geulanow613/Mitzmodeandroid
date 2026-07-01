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
        "shabbat_guide_ui.json",
        "shabbat_guide_polish.json",
        "glossary_polish.json",
        "hebrew_terms.json",
        "zman_ui.json",
        "prayers_liturgy.json",
        "seasonal_explainer_fragments.json",
        "seasonal_arg_explainers.json",
        "argos_disaster_fixes.json",
        "he_paragraph_fixes.json",
        "he_hybrid_purge.json",
        "checklist_explainers.json",
        "explainer_template_args.json",
        "mein_shalosh_ui.json",
        "he_glue_fixes.json",
        "mitzvah_alert_tone.json",
        "he_alert_tone.json",
        "mitzvah_me_ui.json",
        "ui_chrome.json",
        "ru_cyrillic_fixes.json",
        "ru_cyrillic_top50.json",
        "ru_polish_all.json",
        "public_fast_explainers.json",
        "purim_meshulash_explainers.json",
        "ru_explainer_polish.json",
        "ru_brand_ui.json",
        "es_fr_prose_polish.json",
        "he_short_learn_expansions.json",
        "alert_short_expansions.json",
        "ru_deep_fixes.json",
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
        # Proper names / technical passthrough only (Jewish terms → hebrew_terms.json)
        "Mitz Mode!", "G.E.U.L.A",
        "Normal",
        "Yom Tov — Shemini Atzeret",
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
    import subprocess
    import sys

    sync_keys = ROOT / "tools" / "_sync_explainer_catalog_keys.py"
    subprocess.run([sys.executable, str(sync_keys)], cwd=ROOT, check=True)
    write_he = ROOT / "tools" / "_write_explainer_hebrew.py"
    subprocess.run([sys.executable, str(write_he)], cwd=ROOT, check=True)
    write_romance = ROOT / "tools" / "_write_explainer_romance.py"
    subprocess.run([sys.executable, str(write_romance)], cwd=ROOT, check=True)
    write_glossary = ROOT / "tools" / "_write_glossary_es_fixes.py"
    subprocess.run([sys.executable, str(write_glossary)], cwd=ROOT, check=True)
    write_template_args = ROOT / "tools" / "_write_explainer_template_args.py"
    subprocess.run([sys.executable, str(write_template_args)], cwd=ROOT, check=True)
    write_paragraphs = ROOT / "tools" / "_write_paragraph_fixes.py"
    subprocess.run([sys.executable, str(write_paragraphs)], cwd=ROOT, check=True)
    apply_he = ROOT / "tools" / "_apply_hebrew_fixes_total.py"
    subprocess.run([sys.executable, str(apply_he)], cwd=ROOT, check=True)
    write_hybrid = ROOT / "tools" / "_write_he_hybrid_purge.py"
    subprocess.run([sys.executable, str(write_hybrid)], cwd=ROOT, check=True)
    write_alert_tone = ROOT / "tools" / "_write_mitzvah_alert_tone.py"
    subprocess.run([sys.executable, str(write_alert_tone)], cwd=ROOT, check=True)
    write_glue = ROOT / "tools" / "_write_he_glue_fixes.py"
    subprocess.run([sys.executable, str(write_glue)], cwd=ROOT, check=True)

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
            for _ in range(3):
                entries = {k: repair_translation(lang, v) for k, v in entries.items()}
        elif lang == "he":
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

    write_seasonal = ROOT / "tools" / "_write_seasonal_fragments.py"
    subprocess.run([sys.executable, str(write_seasonal)], cwd=ROOT, check=True)
    seasonal = ROOT / "data" / "translation-catalog" / "human" / "seasonal_explainer_fragments.json"
    if seasonal.exists():
        seasonal_data = json.loads(seasonal.read_text(encoding="utf-8"))
        for lang in LANGS:
            dest = COMPOSE / f"{lang}.json"
            payload = json.loads(dest.read_text(encoding="utf-8"))
            for key, val in seasonal_data.get(lang, {}).items():
                if key in required:
                    payload["entries"][key] = repair_translation(lang, val) if lang != "he" else val
            dest.write_text(json.dumps(payload, ensure_ascii=False, indent=2), encoding="utf-8")
            (LEGACY / f"{lang}.json").write_text(
                json.dumps(payload, ensure_ascii=False, indent=2), encoding="utf-8"
            )
        print("Merged seasonal explainer fragments into bundles")

    write_city_geo = ROOT / "tools" / "_write_city_geography_bundled.py"
    subprocess.run([sys.executable, str(write_city_geo)], cwd=ROOT, check=True)

    # Fail the build if shipped bundles contain corrupt glyphs.
    import subprocess
    scan = ROOT / "tools" / "_scan_bad_glyphs.py"
    result = subprocess.run([sys.executable, str(scan)], cwd=ROOT)
    if result.returncode != 0:
        raise SystemExit("Bundle compile failed: bad glyph scan found issues (see above)")

    hint_audit = ROOT / "tools" / "_audit_zman_hints.py"
    audit_result = subprocess.run([sys.executable, str(hint_audit), "--strict"], cwd=ROOT)
    if audit_result.returncode != 0:
        raise SystemExit("Bundle compile failed: domain zman hints missing from catalog (see above)")

    explainer_audit = ROOT / "tools" / "_audit_explainer_strings.py"
    subprocess.run([sys.executable, str(explainer_audit)], cwd=ROOT)

    full_audit = ROOT / "tools" / "_full_translation_audit.py"
    audit_result = subprocess.run([sys.executable, str(full_audit), "--strict"], cwd=ROOT)
    if audit_result.returncode != 0:
        raise SystemExit("Bundle compile failed: translation quality audit (see above)")


if __name__ == "__main__":
    import sys
    main()
