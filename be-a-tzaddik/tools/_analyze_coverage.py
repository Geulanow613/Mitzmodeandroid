#!/usr/bin/env python3
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
LANGS = ("he", "es", "fr", "ru")
SKIP = {"\\s*/\\s*", "halachic_term", "$mitzvotCount"}


def load_merged():
    merged = {l: {} for l in LANGS}
    for lang in LANGS:
        entries = json.loads((ROOT / f"data/bundled-translations/{lang}.json").read_text(encoding="utf-8"))["entries"]
        merged[lang].update(entries)
        mp = ROOT / f"data/bundled-translations/maps/{lang}.json"
        if mp.exists():
            merged[lang].update(json.loads(mp.read_text(encoding="utf-8")))
    leg = json.loads((ROOT / "data/translation-catalog/shards/legacy_import.json").read_text(encoding="utf-8"))
    for lang in LANGS:
        merged[lang].update(leg.get(lang, {}))
    return merged


def main():
    merged = load_merged()
    all_strings = []
    for i in range(7):
        all_strings.extend(
            json.loads((ROOT / f"data/translation-catalog/batches/batch_{i:03d}.json").read_text(encoding="utf-8"))["strings"]
        )
    for lang in LANGS:
        need = []
        for s in all_strings:
            t = merged[lang].get(s, s)
            if s in SKIP or s.startswith("http") or any("\u0590" <= c <= "\u05ff" for c in s):
                continue
            if t == s and len(s) > 3:
                need.append(s)
        print(lang, len(need), "need fix")
        for x in need[:5]:
            print(" ", x)


if __name__ == "__main__":
    main()
