#!/usr/bin/env python3
"""Expand truncated mitzvah keys in strings.json and add missing runtime-only keys."""
from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CATALOG = ROOT / "data/translation-catalog/strings.json"
FIXES = ROOT / "data/translation-catalog/mitzvot-key-fixes.json"
HUMAN = ROOT / "data/translation-catalog/human"
ALIASES = HUMAN / "mitzvot_runtime_aliases.json"

# Catalog uses wrong prefix; runtime + human shards use this key.
SUPER_TO_SPECIAL = (
    "**Super-Special Mitzvah**: Light an oil candle (such as a floating wick in a small cup of oil) for the elevation of the souls of the \"Neshamot Hagalmudot\" (הנשמות הגלמודות) -the forlorn departed souls in Heaven who need just a small merit to get into the Garden of Eden (Gan Eden). Rabbis say these poor souls are just missing a small merit to be let into the eternal pleasure of Gan Eden,  and if you light a candle in their merit, you could help them get in! Then these souls are extremely grateful, and once they're in they can pray for you for all your wishes!",
    "**Special Mitzvah**: Light an oil candle (such as a floating wick in a small cup of oil) for the elevation of the souls of the \"Neshamot Hagalmudot\" (הנשמות הגלמודות) -the forlorn departed souls in Heaven who need just a small merit to get into the Garden of Eden (Gan Eden). Rabbis say these poor souls are just missing a small merit to be let into the eternal pleasure of Gan Eden,  and if you light a candle in their merit, you could help them get in! Then these souls are extremely grateful, and once they're in they can pray for you for all your wishes!",
)


def load_human() -> dict[str, dict[str, str]]:
    merged: dict[str, dict[str, str]] = {lang: {} for lang in ("he", "es", "fr", "ru")}
    for path in sorted(HUMAN.glob("local_*.json")):
        data = json.loads(path.read_text(encoding="utf-8"))
        for lang in merged:
            merged[lang].update(data.get(lang, {}))
    for path in sorted(HUMAN.glob("mitzvot_*.json")):
        if "_only" in path.name or "_src" in path.name:
            continue
        data = json.loads(path.read_text(encoding="utf-8"))
        if not isinstance(data, dict):
            continue
        for lang in merged:
            block = data.get(lang)
            if isinstance(block, dict):
                merged[lang].update(block)
    return merged


def main() -> None:
    fixes = json.loads(FIXES.read_text(encoding="utf-8"))
    cat = json.loads(CATALOG.read_text(encoding="utf-8"))
    strings: list[str] = cat["strings"]
    index = {s: i for i, s in enumerate(strings)}

    replaced = 0
    for item in fixes["replacements"]:
        old, new = item["old"], item["new"]
        if old not in index:
            print(f"skip missing old key: {old[:60]}...")
            continue
        if new in index:
            strings.pop(index[old])
        else:
            strings[index[old]] = new
        replaced += 1
        index = {s: i for i, s in enumerate(strings)}

    old_super, new_special = SUPER_TO_SPECIAL
    if old_super in index:
        if new_special in index:
            strings.pop(index[old_super])
        else:
            strings[index[old_super]] = new_special
        index = {s: i for i, s in enumerate(strings)}
        replaced += 1

    added = 0
    for key in fixes["missing"]:
        if key in index:
            continue
        strings.append(key)
        index[key] = len(strings) - 1
        added += 1

    cat["strings"] = strings
    CATALOG.write_text(json.dumps(cat, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")

    # Alias shard: copy translations from human for any runtime key still using alternate catalog keys
    human = load_human()
    aliases: dict[str, dict[str, str]] = {lang: {} for lang in human}
    for key in fixes["missing"] + [r["new"] for r in fixes["replacements"]] + [SUPER_TO_SPECIAL[1]]:
        for lang in human:
            if key in human[lang] and human[lang][key].strip():
                aliases[lang][key] = human[lang][key]

    ALIASES.write_text(json.dumps(aliases, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"catalog: {replaced} replacements, {added} additions -> {len(strings)} strings")
    print(f"aliases: {len(aliases['he'])} he keys -> {ALIASES.name}")


if __name__ == "__main__":
    main()
