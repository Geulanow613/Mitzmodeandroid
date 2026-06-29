#!/usr/bin/env python3
"""Assemble bundled translation assets from maps + long files."""

from __future__ import annotations

import json
import subprocess
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
TOOLS = ROOT / "tools"
DATA = ROOT / "data" / "bundled-translations"
CATALOG = ROOT / "data" / "translation-catalog"


def write_map(lang: str, values: list[str]) -> None:
    if len(values) != len(KEYS):
        raise SystemExit(f"{lang}: expected {len(KEYS)} values, got {len(values)}")
    mapping = dict(zip(KEYS, values))
    (DATA / "maps" / f"{lang}.json").write_text(
        json.dumps(mapping, ensure_ascii=False, indent=2),
        encoding="utf-8",
    )
    print(f"Wrote {lang} map ({len(mapping)} entries)")


def write_long_from_i18n() -> None:
    i18n = json.loads((DATA / "long-i18n.json").read_text(encoding="utf-8"))
    long_en = sorted(DATA.glob("long-en-*.txt"))
    for lang in ("he", "es", "fr", "ru"):
        texts = i18n[lang]
        if len(texts) != len(long_en):
            raise SystemExit(f"long {lang}: {len(texts)} != {len(long_en)}")
        folder = DATA / "long" / lang
        folder.mkdir(parents=True, exist_ok=True)
        for i, text in enumerate(texts):
            (folder / f"{i:02d}.txt").write_text(text, encoding="utf-8")


def main() -> None:
    subprocess.check_call([sys.executable, str(TOOLS / "extract_all_strings.py")])
    subprocess.check_call([sys.executable, str(TOOLS / "import_legacy_translations.py")])
    if not (CATALOG / "shards" / "argos_full.json").exists():
        subprocess.check_call([sys.executable, str(TOOLS / "generate_argos_translations.py")])
    subprocess.check_call([sys.executable, str(TOOLS / "compile_full_bundled.py")])


if __name__ == "__main__":
    main()
