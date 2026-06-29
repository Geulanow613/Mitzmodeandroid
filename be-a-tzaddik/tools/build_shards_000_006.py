#!/usr/bin/env python3
"""Build shards 000-006 from bundled translations + Hebrew + UI overrides."""

from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
BATCH_DIR = ROOT / "data" / "translation-catalog" / "batches"
SHARD_DIR = ROOT / "data" / "translation-catalog" / "shards"
BUNDLED_DIR = ROOT / "data" / "bundled-translations"
LANGS = ("he", "es", "fr", "ru")

# Hebrew for every batch 000-006 string (exact English keys).
from he_fixes_000_006 import HE as HE_FIXES  # noqa: E402
from he_glossary_000_006 import HE_GLOSSARY  # noqa: E402
from ui_ovr_000_006 import UI  # noqa: E402

OVERLAY_PATH = SHARD_DIR / "overlay_000_006.json"


def build_he(extra: dict[str, str], bundled_he: dict[str, str], all_strings: list[str]) -> dict[str, str]:
    he = {}
    he.update(extra)
    he.update(HE_GLOSSARY)
    he.update(HE_FIXES)
    for s in all_strings:
        if s in he:
            continue
        if any("\u0590" <= c <= "\u05ff" for c in s):
            he[s] = s
            continue
        if s in bundled_he and bundled_he[s] != s:
            he[s] = bundled_he[s]
    he["halachic_term"] = "\u05de\u05d5\u05e0\u05d7 \u05d4\u05dc\u05db\u05ea\u05d9"
    he["Beardy Top Productions"] = "\u05d4\u05e4\u05e7\u05d5\u05ea Beardy Top"
    return he


def load_overlay() -> dict[str, dict[str, str]]:
    if not OVERLAY_PATH.exists():
        return {lang: {} for lang in LANGS}
    data = json.loads(OVERLAY_PATH.read_text(encoding="utf-8"))
    return {lang: data.get(lang, {}) for lang in LANGS}


def load_bundled() -> dict[str, dict[str, str]]:
    out: dict[str, dict[str, str]] = {}
    for lang in LANGS:
        out[lang] = json.loads((BUNDLED_DIR / f"{lang}.json").read_text(encoding="utf-8"))["entries"]
    return out


def load_extra() -> dict[str, dict[str, str]]:
    out = {lang: {} for lang in LANGS}
    for lang in LANGS:
        mp = BUNDLED_DIR / "maps" / f"{lang}.json"
        if mp.exists():
            out[lang].update(json.loads(mp.read_text(encoding="utf-8")))
    leg = SHARD_DIR / "legacy_import.json"
    if leg.exists():
        data = json.loads(leg.read_text(encoding="utf-8"))
        for lang in LANGS:
            out[lang].update(data.get(lang, {}))
    return out


def resolve(string: str, lang: str, bundled: dict, extra: dict, overlay: dict, he_map: dict[str, str]) -> str:
    if lang == "he":
        if string in he_map:
            return he_map[string]
        raise KeyError(f"Missing Hebrew: {string[:80]!r}")

    if string in overlay[lang]:
        return overlay[lang][string]
    if string in UI and lang in UI[string]:
        return UI[string][lang]
    if string in extra[lang] and extra[lang][string] != string:
        return extra[lang][string]
    if string in bundled[lang] and bundled[lang][string] != string:
        return bundled[lang][string]
    raise KeyError(f"Missing {lang} translation for: {string[:80]!r}")


def main() -> None:
    bundled = load_bundled()
    extra = load_extra()
    overlay = load_overlay()
    all_strings: list[str] = []
    for batch_idx in range(7):
        batch = json.loads((BATCH_DIR / f"batch_{batch_idx:03d}.json").read_text(encoding="utf-8"))
        all_strings.extend(batch["strings"])
    he_map = build_he(extra["he"], bundled["he"], all_strings)
    missing_he = [s for s in all_strings if s not in he_map]
    if missing_he:
        raise SystemExit(f"Missing {len(missing_he)} Hebrew entries, first: {missing_he[0]!r}")
    SHARD_DIR.mkdir(parents=True, exist_ok=True)

    for batch_idx in range(7):
        batch = json.loads((BATCH_DIR / f"batch_{batch_idx:03d}.json").read_text(encoding="utf-8"))
        shard = {lang: {} for lang in LANGS}
        for s in batch["strings"]:
            for lang in LANGS:
                shard[lang][s] = resolve(s, lang, bundled, extra, overlay, he_map)
        out = SHARD_DIR / f"batch_{batch_idx:03d}.json"
        out.write_text(json.dumps(shard, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
        print(f"batch_{batch_idx:03d}.json: {len(batch['strings'])} strings")


if __name__ == "__main__":
    main()
