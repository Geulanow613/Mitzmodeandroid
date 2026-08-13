#!/usr/bin/env python3
"""Apply FR tu / RU ty converters to human translation shard source files."""
from __future__ import annotations

import json
from pathlib import Path

from _fr_tu_convert import apply_fr_tu
from _ru_ty_convert import apply_ru_ty

HUMAN = Path(__file__).resolve().parents[1] / "data" / "translation-catalog" / "human"
SKIP = {
    "es_fr_prose_polish.json",
    "ru_deep_fixes.json",
    "explainer_template_args.json",
    "seasonal_explainer_fragments.json",
    "checklist_explainers.json",
}
LANGS = ("es", "fr", "ru", "he")


def patch_lang_map(data: dict, lang: str, apply) -> int:
    block = data.get(lang)
    if not isinstance(block, dict):
        return 0
    n = 0
    for key, val in list(block.items()):
        if not isinstance(val, str):
            continue
        new = apply(val)
        if new != val:
            block[key] = new
            n += 1
    return n


def main() -> None:
    totals: dict[str, int] = {"fr": 0, "ru": 0}
    for path in sorted(HUMAN.glob("*.json")):
        if path.name.endswith("_only.json") or path.name.endswith("_src.json"):
            continue
        if path.name in SKIP:
            continue
        try:
            data = json.loads(path.read_text(encoding="utf-8"))
        except json.JSONDecodeError:
            continue
        if not isinstance(data, dict):
            continue
        fr_n = patch_lang_map(data, "fr", apply_fr_tu)
        ru_n = patch_lang_map(data, "ru", apply_ru_ty)
        if fr_n or ru_n:
            path.write_text(json.dumps(data, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
            print(f"{path.name}: fr={fr_n} ru={ru_n}")
            totals["fr"] += fr_n
            totals["ru"] += ru_n
    print(f"total fr={totals['fr']} ru={totals['ru']}")


if __name__ == "__main__":
    main()
