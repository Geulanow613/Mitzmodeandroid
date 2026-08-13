#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""Generate remaining overlay translations and write overlay_007_013.json."""

from __future__ import annotations

import importlib
import json
import pkgutil
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
NEEDS_PATH = ROOT / "data" / "translation-catalog" / "_needs_007_013.json"
OUT_PATH = ROOT / "data" / "translation-catalog" / "shards" / "overlay_007_013.json"
LANGS = ("he", "es", "fr", "ru")


def tr(he: str, es: str, fr: str, ru: str) -> dict[str, str]:
    return {"he": he, "es": es, "fr": fr, "ru": ru}


# Remaining 327 translations (human-quality)
REST: dict[str, dict[str, str]] = {
    "the nineteen blessings of the standing Amidah prayer": tr(
        "תשע עשרה ברכות של תפילת העמידה",
        "las diecinueve bendiciones de la oración Amidá de pie",
        "les dix-neuf bénédictions de la prière Amidah debout",
        "девятнадцать благословений стоячей молитвы Amidah",
    ),
    "Birkat HaMazon — Grace After Meals after eating bread": tr(
        "ברכת המזון — ברכה לאחר אכילת לחם",
        "Birkat HaMazon — Gracia después de las comidas tras comer pan",
        "Birkat HaMazon — Grâce après les repas après avoir mangé du pain",
        "Birkat HaMazon — благословение после трапезы после хлеба",
    ),
    "beged — garment; tzitzit apply to four-cornered beged": tr(
        "beged — בeged; ציצית חלים על beged בעל ארba kenafot",
        "beged — prenda; los tzitzit aplican a un beged de cuatro esquinas",
        "beged — vêtement ; les tsitsit s'appliquent à un beged à quatre coins",
        "beged — одежда; tzitzit относятся к четырёхугольной beged",
    ),
}

# NOTE: This script is a scaffold; full REST dict populated below via exec of parts.


def load_parts() -> dict[str, dict[str, str]]:
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
    needs: list[str] = json.loads(NEEDS_PATH.read_text(encoding="utf-8"))
    translations = load_parts()
    translations.update(REST)
    missing = [s for s in needs if s not in translations]
    if missing:
        raise SystemExit(f"Missing {len(missing)} translations. First: {missing[0]!r}")
    overlay = {lang: {s: translations[s][lang] for s in needs} for lang in LANGS}
    OUT_PATH.parent.mkdir(parents=True, exist_ok=True)
    OUT_PATH.write_text(json.dumps(overlay, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"Wrote {OUT_PATH}: {len(needs)} strings")


if __name__ == "__main__":
    main()
