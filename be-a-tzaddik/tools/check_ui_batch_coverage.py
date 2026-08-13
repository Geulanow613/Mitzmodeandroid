#!/usr/bin/env python3
"""Build human UI batch from bundled maps where quality is OK, flag gaps."""
from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
LANGS = ("he", "es", "fr", "ru")


def load_maps() -> dict[str, dict[str, str]]:
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


def main() -> None:
    import sys

    batch = sys.argv[1] if len(sys.argv) > 1 else "ui_short_001"
    keys_path = ROOT / f"data/translation-catalog/_keys_{batch}.json"
    keys = json.loads(keys_path.read_text(encoding="utf-8"))
    maps = load_maps()
    for lang in LANGS:
        miss = [k for k in keys if not maps[lang].get(k) or maps[lang][k] == k]
        print(f"{lang}: {len(keys) - len(miss)}/{len(keys)} from bundled, {len(miss)} need human")


if __name__ == "__main__":
    main()
