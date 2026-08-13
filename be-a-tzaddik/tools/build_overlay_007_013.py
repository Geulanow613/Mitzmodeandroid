#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""Build complete overlay_007_013.json from translation modules."""

from __future__ import annotations

import importlib
import json
import pkgutil
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
NEEDS = ROOT / "data" / "translation-catalog" / "_needs_007_013.json"
OUT = ROOT / "data" / "translation-catalog" / "shards" / "overlay_007_013.json"
LANGS = ("he", "es", "fr", "ru")


def load_all_translations() -> dict[str, dict[str, str]]:
    merged: dict[str, dict[str, str]] = {}
    import overlay_parts as pkg

    for modinfo in pkgutil.iter_modules(pkg.__path__, pkg.__name__ + "."):
        if modinfo.name.endswith(".__init__"):
            continue
        mod = importlib.import_module(modinfo.name)
        for attr in ("T", "PART", "P2", "P3", "P4", "DATA"):
            block = getattr(mod, attr, None)
            if isinstance(block, dict):
                merged.update(block)
    return merged


def main() -> None:
    needs: list[str] = json.loads(NEEDS.read_text(encoding="utf-8"))
    translations = load_all_translations()
    missing = [s for s in needs if s not in translations]
    if missing:
        raise SystemExit(f"Missing {len(missing)} translations. First: {missing[0]!r}")
    overlay = {lang: {s: translations[s][lang] for s in needs} for lang in LANGS}
    OUT.parent.mkdir(parents=True, exist_ok=True)
    OUT.write_text(json.dumps(overlay, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"Wrote {OUT}: {len(needs)} strings")


if __name__ == "__main__":
    main()
