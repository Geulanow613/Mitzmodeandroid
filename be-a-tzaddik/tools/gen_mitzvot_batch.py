#!/usr/bin/env python3
"""Generate and validate human-quality translation batch JSON.

Usage:
  python gen_mitzvot_batch.py 002          # mitzvot cloud batch
  python gen_mitzvot_batch.py ui_short_001 # UI short strings batch
"""

from __future__ import annotations

import json
import sys
from pathlib import Path

BASE = Path(__file__).resolve().parents[1]
HUMAN = BASE / "data/translation-catalog/human"
CATALOG_DIR = BASE / "data/translation-catalog"
LANGS = ("he", "es", "fr", "ru")


def batch_paths(batch: str) -> tuple[Path, Path, Path]:
    """Return (keys_path, src_path, out_path)."""
    if batch.startswith("mitzvot_"):
        num = batch.replace("mitzvot_", "")
        keys_path = CATALOG_DIR / f"_keys_{num}.json"
        src_path = HUMAN / f"{batch}_src.json"
        out_path = HUMAN / f"{batch}.json"
    else:
        keys_path = CATALOG_DIR / f"_keys_{batch}.json"
        src_path = HUMAN / f"{batch}_src.json"
        out_path = HUMAN / f"{batch}.json"
    if not keys_path.is_file():
        raise SystemExit(f"Missing {keys_path}")
    return keys_path, src_path, out_path


def batch_langs(batch: str) -> tuple[str, ...]:
    return ("he",) if batch.startswith("he_fix_") else LANGS


def load_tables(batch: str) -> dict[str, list[str]]:
    _, src_path, _ = batch_paths(batch)
    if not src_path.is_file():
        raise SystemExit(f"Missing {src_path} — merge *_only.json files first")
    raw = json.loads(src_path.read_text(encoding="utf-8"))
    return {lang: raw[lang] for lang in batch_langs(batch)}


def load_keys(batch: str) -> list[str]:
    keys_path, _, _ = batch_paths(batch)
    keys = json.loads(keys_path.read_text(encoding="utf-8"))
    if not isinstance(keys, list):
        raise SystemExit(f"{keys_path}: expected JSON array")
    return keys


def validate(keys: list[str], tables: dict[str, list[str]], batch: str) -> None:
    n = len(keys)
    for lang in batch_langs(batch):
        table = tables[lang]
        if len(table) != n:
            raise SystemExit(f"{lang}: expected {n} translations, got {len(table)}")
    for i, key in enumerate(keys):
        for lang in batch_langs(batch):
            if not tables[lang][i].strip():
                raise SystemExit(f"{lang}[{i}]: empty translation for {key[:60]!r}...")


def build(keys: list[str], tables: dict[str, list[str]]) -> dict[str, dict[str, str]]:
    return {lang: {keys[i]: tables[lang][i] for i in range(len(keys))} for lang in LANGS}


def merge_only_files(batch: str) -> Path:
    """Merge {batch}_{lang}_only.json into {batch}_src.json."""
    he_fix = batch.startswith("he_fix_")
    langs = ("he",) if he_fix else LANGS
    merged: dict[str, list[str]] = {}
    for lang in langs:
        p = HUMAN / f"{batch}_{lang}_only.json"
        if not p.is_file():
            raise SystemExit(f"Missing {p}")
        merged[lang] = json.loads(p.read_text(encoding="utf-8"))[lang]
    _, src_path, _ = batch_paths(batch)
    HUMAN.mkdir(parents=True, exist_ok=True)
    src_path.write_text(json.dumps(merged, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    return src_path


def write(batch: str) -> Path:
    keys = load_keys(batch)
    tables = load_tables(batch)
    validate(keys, tables, batch)
    langs = batch_langs(batch)
    out = {lang: {keys[i]: tables[lang][i] for i in range(len(keys))} for lang in langs}
    _, _, dest = batch_paths(batch)
    HUMAN.mkdir(parents=True, exist_ok=True)
    if dest.is_file():
        existing = json.loads(dest.read_text(encoding="utf-8"))
        for lang in langs:
            existing.setdefault(lang, {}).update(out.get(lang, {}))
        out = existing
    dest.write_text(json.dumps(out, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    return dest


def main() -> None:
    batch = sys.argv[1] if len(sys.argv) > 1 else "002"
    if len(sys.argv) > 2 and sys.argv[2] == "--merge":
        merge_only_files(batch)
        print(f"merged *_only -> {batch}_src.json")
    path = write(batch)
    keys = load_keys(batch)
    print(f"OK: wrote {path} ({path.stat().st_size:,} bytes, {len(keys)} keys)")
    for lang in batch_langs(batch):
        print(f"  {lang}: {len(json.loads(path.read_text(encoding='utf-8'))[lang])} keys")


if __name__ == "__main__":
    main()
