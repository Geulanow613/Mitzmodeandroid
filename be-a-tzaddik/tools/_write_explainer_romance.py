#!/usr/bin/env python3
"""Merge human-reviewed es/fr/ru explainer translations into human/checklist_explainers.json."""
from __future__ import annotations

import json
from pathlib import Path

from _explainer_keys import resolve_keys
from _explainer_romance_data import translations

ROOT = Path(__file__).resolve().parents[1]
EXPLAINERS = ROOT / "data" / "translation-catalog" / "human" / "checklist_explainers.json"

LANGS = ("es", "fr", "ru")
_SCHEDULE = "$scheduleLeadIn$scheduleBody$scheduleYomTov"

MANUAL: dict[str, dict[str, str]] = {
    "Normal": {"es": "Normal", "fr": "Ordinaire", "ru": "Обычный"},
    "Yom Tov — Shemini Atzeret": {
        "es": "Yom Tov — Sheminí Atzeret",
        "fr": "Yom Tov — Chemini Atseret",
        "ru": "Йом Тов — Шмини Ацерет",
    },
}


def catalog_key_map() -> dict[str, str]:
    """Map logical names to exact English catalog keys."""
    keys = resolve_keys()
    out = dict(keys)
    out["mechirat_no_sched"] = keys["mechirat_urgency"]
    for base in ("bedikat", "mechirat", "biur", "seder"):
        out[f"{base}_sched_block"] = keys[base].replace(_SCHEDULE, "$scheduleBlock")
    return out


_FULL_TW = (
    ("tw_ash_full", "tw_ash"),
    ("tw_sep_full", "tw_sep"),
    ("tw_ch_full", "tw_ch"),
)


def build_lang_section(lang: str, key_map: dict[str, str]) -> dict[str, str]:
    tr = translations(lang)
    section: dict[str, str] = {}
    for logical, en_key in key_map.items():
        if logical == "halacha":
            continue
        if logical.endswith("_full"):
            continue
        if logical not in tr:
            raise KeyError(f"Missing translation for logical key {logical!r} ({lang})")
        section[en_key] = tr[logical]
    intro = tr["tw_intro"]
    for full_logical, frag_logical in _FULL_TW:
        section[key_map[full_logical]] = intro + "\n\n" + tr[frag_logical]
    for en_key, val in MANUAL.items():
        section[en_key] = val[lang]
    return section


def main() -> None:
    key_map = catalog_key_map()
    existing = (
        json.loads(EXPLAINERS.read_text(encoding="utf-8"))
        if EXPLAINERS.exists()
        else {}
    )
    existing.setdefault("he", {})

    counts: dict[str, int] = {}
    for lang in LANGS:
        existing[lang] = build_lang_section(lang, key_map)
        counts[lang] = len(existing[lang])

    EXPLAINERS.write_text(json.dumps(existing, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"Wrote {EXPLAINERS.name}")
    for lang in LANGS:
        print(f"  {lang}: {counts[lang]} keys")


if __name__ == "__main__":
    main()
