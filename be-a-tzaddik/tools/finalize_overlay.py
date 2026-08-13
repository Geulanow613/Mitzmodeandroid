#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""Write overlay_007_013.json merging all translation parts."""

from __future__ import annotations

import importlib
import json
import pkgutil
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
NEEDS = ROOT / "data" / "translation-catalog" / "_needs_007_013.json"
OUT = ROOT / "data" / "translation-catalog" / "shards" / "overlay_007_013.json"
LANGS = ("he", "es", "fr", "ru")


def load_merged() -> dict[str, dict[str, str]]:
    import overlay_parts as pkg

    merged: dict[str, dict[str, str]] = {}
    for modinfo in pkgutil.iter_modules(pkg.__path__, pkg.__name__ + "."):
        if modinfo.name.endswith(".__init__"):
            continue
        mod = importlib.import_module(modinfo.name)
        for attr in ("T", "PART", "P2", "P3", "P4", "DATA", "REST"):
            block = getattr(mod, attr, None)
            if isinstance(block, dict):
                merged.update(block)
    return merged


def main() -> None:
    needs: list[str] = json.loads(NEEDS.read_text(encoding="utf-8"))
    translations = load_merged()
    missing = [s for s in needs if s not in translations]
    if missing:
        # Write partial report
        report = ROOT / "tools" / "_overlay_missing.json"
        report.write_text(json.dumps(missing, ensure_ascii=False, indent=2), encoding="utf-8")
        raise SystemExit(f"Missing {len(missing)} translations (saved to {report.name})")
    overlay = {lang: {s: translations[s][lang] for s in needs} for lang in LANGS}
    OUT.parent.mkdir(parents=True, exist_ok=True)
    OUT.write_text(json.dumps(overlay, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"Wrote {OUT}: {len(needs)} strings x 4 langs")


if __name__ == "__main__":
    main()
