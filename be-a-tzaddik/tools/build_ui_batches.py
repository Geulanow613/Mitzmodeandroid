#!/usr/bin/env python3
"""Build human/ UI batches from bundled translations + small overrides."""

from __future__ import annotations

import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CATALOG = ROOT / "data/translation-catalog"
HUMAN = CATALOG / "human"
LANGS = ("he", "es", "fr", "ru")

# Keys that must pass through unchanged (Kotlin templates)
PASSTHROUGH = re.compile(r"^\$[a-zA-Z_][a-zA-Z0-9_]*$")

# Human overrides for keys missing or wrong in bundled
OVERRIDES: dict[str, dict[str, str]] = {
    "$mitzvotCount": {
        "he": "$mitzvotCount",
        "es": "$mitzvotCount",
        "fr": "$mitzvotCount",
        "ru": "$mitzvotCount",
    },
    "Beardy Top Productions": {
        "he": "Beardy Top Productions",
        "es": "Beardy Top Productions",
        "fr": "Beardy Top Productions",
        "ru": "Beardy Top Productions",
    },
}


def load_bundled() -> dict[str, dict[str, str]]:
    out: dict[str, dict[str, str]] = {}
    for lang in LANGS:
        combined: dict[str, str] = {}
        bpath = ROOT / f"data/bundled-translations/{lang}.json"
        if bpath.exists():
            combined.update(json.loads(bpath.read_text(encoding="utf-8")).get("entries", {}))
        mpath = ROOT / f"data/bundled-translations/maps/{lang}.json"
        if mpath.exists():
            combined.update(json.loads(mpath.read_text(encoding="utf-8")))
        out[lang] = combined
    return out


def translate_key(key: str, lang: str, bundled: dict[str, dict[str, str]]) -> str:
    if key in OVERRIDES and lang in OVERRIDES[key]:
        return OVERRIDES[key][lang]
    if PASSTHROUGH.match(key):
        return key
    tr = bundled[lang].get(key)
    if tr and tr != key:
        return tr
    # Fallback: keep English for untranslated short labels (brand names, etc.)
    return key


def build_batch(batch: str, bundled: dict[str, dict[str, str]]) -> dict[str, dict[str, str]]:
    keys_path = CATALOG / f"_keys_{batch}.json"
    keys = json.loads(keys_path.read_text(encoding="utf-8"))
    return {lang: {k: translate_key(k, lang, bundled) for k in keys} for lang in LANGS}


def main() -> None:
    arg = sys.argv[1] if len(sys.argv) > 1 else "ui_short"
    bundled = load_bundled()
    HUMAN.mkdir(parents=True, exist_ok=True)
    prefixes = (arg,) if not arg.endswith("_") else (arg,)
    if arg.endswith("_"):
        paths = sorted(CATALOG.glob(f"_keys_{arg}*.json"))
    elif arg in ("ui_short", "ui_medium", "content_medium", "content_long"):
        paths = sorted(CATALOG.glob(f"_keys_{arg}_*.json"))
    else:
        paths = [CATALOG / f"_keys_{arg}.json"]
    paths = [p for p in paths if p.is_file()]
    total = 0
    for keys_path in paths:
        batch = keys_path.name.replace("_keys_", "").replace(".json", "")
        out = build_batch(batch, bundled)
        n = len(json.loads(keys_path.read_text(encoding="utf-8")))
        dest = HUMAN / f"{batch}.json"
        dest.write_text(json.dumps(out, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
        total += n
        print(f"  {dest.name}: {n} keys")
    print(f"OK: {len(paths)} batches, {total} keys total")


if __name__ == "__main__":
    main()
