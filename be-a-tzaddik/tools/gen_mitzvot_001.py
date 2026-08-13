#!/usr/bin/env python3
"""Generate and validate human-quality mitzvot_001.json."""

from __future__ import annotations

import json
from pathlib import Path

from mitzvot_es import ES
from mitzvot_fr import FR
from mitzvot_he import HE
from mitzvot_ru import RU

BASE = Path(__file__).resolve().parents[1]
KEYS_PATH = BASE / "data/translation-catalog/_keys_001.json"
SRC_DIR = BASE / "data/translation-catalog/human"
SRC_JSON = SRC_DIR / "mitzvot_001_src.json"
OUT = SRC_DIR / "mitzvot_001.json"
LANGS = ("he", "es", "fr", "ru")


def load_tables() -> dict[str, list[str]]:
    if SRC_JSON.is_file():
        raw = json.loads(SRC_JSON.read_text(encoding="utf-8"))
        return {lang: raw[lang] for lang in LANGS}
    return {"he": HE, "es": ES, "fr": FR, "ru": RU}


TABLES: dict[str, list[str]] = load_tables()


def load_keys() -> list[str]:
    keys = json.loads(KEYS_PATH.read_text(encoding="utf-8"))
    if not isinstance(keys, list):
        raise SystemExit(f"{KEYS_PATH}: expected JSON array")
    return keys


def validate(keys: list[str]) -> None:
    if len(keys) != 25:
        raise SystemExit(f"Expected 25 keys in {KEYS_PATH.name}, got {len(keys)}")
    for lang in LANGS:
        table = TABLES[lang]
        if len(table) != 25:
            raise SystemExit(f"{lang}: expected 25 translations, got {len(table)}")
    for i, key in enumerate(keys):
        for lang in LANGS:
            if not TABLES[lang][i].strip():
                raise SystemExit(f"{lang}[{i}]: empty translation")


def build(keys: list[str]) -> dict[str, dict[str, str]]:
    return {lang: {keys[i]: TABLES[lang][i] for i in range(len(keys))} for lang in LANGS}


def check_output(data: dict[str, dict[str, str]], keys: list[str]) -> None:
    for lang in LANGS:
        block = data[lang]
        if len(block) != 25:
            raise SystemExit(f"Output {lang}: expected 25 keys, got {len(block)}")
        missing = [k for k in keys if k not in block]
        extra = [k for k in block if k not in keys]
        if missing:
            raise SystemExit(f"Output {lang} missing {len(missing)} keys; first: {missing[0][:60]!r}...")
        if extra:
            raise SystemExit(f"Output {lang} has {len(extra)} extra keys; first: {extra[0][:60]!r}...")


def write(keys: list[str]) -> Path:
    validate(keys)
    out = build(keys)
    check_output(out, keys)
    OUT.parent.mkdir(parents=True, exist_ok=True)
    OUT.write_text(json.dumps(out, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    return OUT


def main() -> None:
    keys = load_keys()
    path = write(keys)
    size = path.stat().st_size
    print(f"OK: wrote {path} ({size:,} bytes)")
    for lang in LANGS:
        print(f"  {lang}: {len(json.loads(path.read_text(encoding='utf-8'))[lang])} keys")
    # Note trailing space on cloud4 (index 3)
    if keys[3].endswith(" "):
        print("  note: key[3] (cloud4) has trailing space — preserved in output")


if __name__ == "__main__":
    main()
