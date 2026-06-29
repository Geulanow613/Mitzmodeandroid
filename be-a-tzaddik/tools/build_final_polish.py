#!/usr/bin/env python3
"""Generate final Hebrew polish overrides and rebuild local shard files."""
from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CATALOG = ROOT / "data" / "translation-catalog"
HUMAN = CATALOG / "human"
LANGS = ("he", "es", "fr", "ru")

MITZVOT_004_OUT = HUMAN / "mitzvot_004_he_expansions.json"
EDUCATIONAL_OUT = HUMAN / "educational_he_overrides.json"
LOCAL_001_OUT = HUMAN / "local_001.json"
LOCAL_007_OUT = HUMAN / "local_007.json"


def _load_json(path: Path) -> dict | list:
    return json.loads(path.read_text(encoding="utf-8"))


def _write_shard(path: Path, he_map: dict[str, str]) -> None:
    path.write_text(
        json.dumps({"he": he_map}, ensure_ascii=False, indent=2) + "\n",
        encoding="utf-8",
    )


def _resolve_audit_he_keys() -> list[str]:
    audit = _load_json(CATALOG / "deep-mitzvot-audit.json")
    catalog = _load_json(CATALOG / "strings.json")["strings"]
    prefixes: set[str] = set()
    for item in audit.get("he_latin_heavy", []):
        prefixes.add(item["key"])
    for item in audit.get("garbage_hits", []):
        if item.get("lang") == "he":
            prefixes.add(item["key"])
    resolved: list[str] = []
    seen: set[str] = set()
    for prefix in sorted(prefixes, key=len):
        matches = [s for s in catalog if s.startswith(prefix)]
        if not matches:
            raise KeyError(f"No catalog match for audit prefix: {prefix[:80]!r}")
        key = matches[0]
        if key not in seen:
            seen.add(key)
            resolved.append(key)
    return resolved


def _mitzvot_004_he_texts() -> list[str]:
    from _final_polish_he_data import MITZVOT_004_HE  # noqa: PLC0415

    return MITZVOT_004_HE


def _educational_he_texts() -> dict[str, str]:
    path = Path(__file__).resolve().parent / "educational_he_data.json"
    if path.exists():
        return json.loads(path.read_text(encoding="utf-8"))
    from _final_polish_he_data import EDUCATIONAL_HE  # noqa: PLC0415

    return EDUCATIONAL_HE


def build_mitzvot_004_he_expansions() -> dict[str, str]:
    keys_path = CATALOG / "_six_keys.json"
    if keys_path.exists():
        keys: list[str] = _load_json(keys_path)
    elif MITZVOT_004_OUT.exists():
        keys = list(_load_json(MITZVOT_004_OUT)["he"].keys())
    else:
        raise FileNotFoundError(f"Need {keys_path.name} or existing {MITZVOT_004_OUT.name}")
    texts = _mitzvot_004_he_texts()
    if len(keys) != 6 or len(texts) != 6:
        raise ValueError(f"Expected 6 keys/texts, got {len(keys)}/{len(texts)}")
    return dict(zip(keys, texts, strict=True))


def build_educational_he_overrides() -> dict[str, str]:
    translations = _educational_he_texts()
    audit_keys = set(_resolve_audit_he_keys())
    out: dict[str, str] = {}
    missing_audit: list[str] = []
    for key in audit_keys:
        if key in translations:
            out[key] = translations[key]
        else:
            missing_audit.append(key[:80])
    if missing_audit:
        raise KeyError(f"Missing educational HE for {len(missing_audit)} audit keys: {missing_audit[:5]}")
    # Extra fixes present in data but not in current audit (e.g. quality_fixes garbage)
    for key, tr in translations.items():
        if key not in out:
            out[key] = tr
    return out


def rebuild_local_001() -> dict[str, dict[str, str]]:
    if LOCAL_001_OUT.exists():
        return _load_json(LOCAL_001_OUT)
    keys: list[str] = _load_json(CATALOG / "_keys_local_001.json")
    if len(keys) != 25:
        raise ValueError(f"_keys_local_001.json must have 25 keys, got {len(keys)}")
    out: dict[str, dict[str, str]] = {lang: {} for lang in LANGS}
    for lang in LANGS:
        path = HUMAN / f"local_001_{lang}_only.json"
        if not path.exists():
            raise FileNotFoundError(f"Missing {path.name} and no existing {LOCAL_001_OUT.name}")
        block = _load_json(path)[lang]
        if len(block) != 25:
            raise ValueError(f"{path.name} must have 25 entries, got {len(block)}")
        if len(set(block)) != 25:
            raise ValueError(f"{path.name} has duplicate translations")
        for key, tr in zip(keys, block, strict=True):
            out[lang][key] = tr
    LOCAL_001_OUT.write_text(json.dumps(out, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    return out


def rebuild_local_007() -> dict[str, dict[str, str]]:
    import build_local_007 as b7  # noqa: PLC0415

    keys: list[str] = _load_json(CATALOG / "_keys_local_007.json")
    if len(keys) != 18:
        raise ValueError(f"_keys_local_007.json must have 18 keys, got {len(keys)}")
    current = _load_json(LOCAL_007_OUT) if LOCAL_007_OUT.exists() else {lang: {} for lang in LANGS}
    out: dict[str, dict[str, str]] = {lang: {} for lang in LANGS}
    for key in keys:
        for lang in LANGS:
            if key in b7.NEW:
                out[lang][key] = b7.NEW[key][lang]
            else:
                out[lang][key] = current.get(lang, {}).get(key, key)
    LOCAL_007_OUT.write_text(json.dumps(out, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    return out


def main() -> None:
    m004 = build_mitzvot_004_he_expansions()
    _write_shard(MITZVOT_004_OUT, m004)

    edu = build_educational_he_overrides()
    _write_shard(EDUCATIONAL_OUT, edu)

    l001 = rebuild_local_001()
    l007 = rebuild_local_007()

    print(f"Wrote {MITZVOT_004_OUT.name}: {len(m004)} he keys")
    print(f"Wrote {EDUCATIONAL_OUT.name}: {len(edu)} he keys")
    print(f"Wrote {LOCAL_001_OUT.name}: {len(l001['he'])} keys x {len(LANGS)} langs")
    print(f"Wrote {LOCAL_007_OUT.name}: {len(l007['he'])} keys x {len(LANGS)} langs")


if __name__ == "__main__":
    main()
