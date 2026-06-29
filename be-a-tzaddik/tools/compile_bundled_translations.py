#!/usr/bin/env python3
"""Compile bundled translation JSON from per-language maps + long-text files."""

from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
STRINGS = ROOT / "data" / "translation-strings.json"
OUT_DIR = ROOT / "data" / "bundled-translations"
MAP_DIR = OUT_DIR / "maps"
COMPOSE = ROOT / "shared" / "src" / "commonMain" / "composeResources" / "files" / "translations"
LANGS = ("he", "es", "fr", "ru")


def load_long(lang: str, count: int) -> dict[str, str]:
    folder = OUT_DIR / "long" / lang
    mapping: dict[str, str] = {}
    for i in range(count):
        en_path = OUT_DIR / f"long-en-{i:02d}.txt"
        tr_path = folder / f"{i:02d}.txt"
        if not en_path.exists() or not tr_path.exists():
            raise FileNotFoundError(f"Missing long pair {en_path.name} / {tr_path}")
        mapping[en_path.read_text(encoding="utf-8")] = tr_path.read_text(encoding="utf-8")
    return mapping


def main() -> None:
    required = json.loads(STRINGS.read_text(encoding="utf-8"))["strings"]
    long_count = sum(1 for s in required if len(s) >= 1000)

    for lang in LANGS:
        map_path = MAP_DIR / f"{lang}.json"
        if not map_path.exists():
            raise SystemExit(f"Missing map {map_path}")
        entries = json.loads(map_path.read_text(encoding="utf-8"))
        entries.update(load_long(lang, long_count))
        missing = [s for s in required if s not in entries]
        if missing:
            raise SystemExit(f"{lang}: missing {len(missing)} entries, first: {missing[0][:80]!r}")
        payload = {
            "version": 1,
            "language": lang,
            "entries": {s: entries[s] for s in required},
        }
        dest = OUT_DIR / f"{lang}.json"
        dest.write_text(json.dumps(payload, ensure_ascii=False, indent=2), encoding="utf-8")
        COMPOSE.mkdir(parents=True, exist_ok=True)
        (COMPOSE / f"{lang}.json").write_text(
            json.dumps(payload, ensure_ascii=False, indent=2), encoding="utf-8"
        )
        print(f"{lang}: {len(required)} entries -> {dest}")


if __name__ == "__main__":
    main()
