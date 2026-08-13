#!/usr/bin/env python3
"""Merge t_batch_*.py modules into shard JSON files."""
import importlib.util
import json
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1] / "data" / "translation-catalog"
TOOLS = Path(__file__).resolve().parent
LANGS = ("he", "es", "fr", "ru")


def load_module(name: str):
    path = TOOLS / f"{name}.py"
    spec = importlib.util.spec_from_file_location(name, path)
    mod = importlib.util.module_from_spec(spec)
    spec.loader.exec_module(mod)
    return mod.TRANSLATIONS


def write_shard(batch_num: int, translations: dict) -> int:
    batch = json.loads((ROOT / "batches" / f"batch_{batch_num:03d}.json").read_text(encoding="utf-8"))
    shard = {lang: {} for lang in LANGS}
    missing = []
    for s in batch["strings"]:
        if s not in translations:
            missing.append(s)
            continue
        for lang in LANGS:
            shard[lang][s] = translations[s][lang]
    if missing:
        print(f"batch_{batch_num:03d}: MISSING {len(missing)}", file=sys.stderr)
        print(missing[0][:120], file=sys.stderr)
        return 0
    out = ROOT / "shards" / f"batch_{batch_num:03d}.json"
    out.parent.mkdir(parents=True, exist_ok=True)
    out.write_text(json.dumps(shard, ensure_ascii=False, indent=2), encoding="utf-8")
    return len(shard["he"])


def main() -> None:
    total = 0
    for n in range(14, 20):
        mod = load_module(f"t_batch_{n:03d}")
        c = write_shard(n, mod)
        print(f"batch_{n:03d}.json: {c} keys")
        total += c
    print(f"TOTAL: {total}")


if __name__ == "__main__":
    main()
