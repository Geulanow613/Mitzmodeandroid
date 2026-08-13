#!/usr/bin/env python3
"""Assemble bundled translation JSON from index-aligned translation lists."""

from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
DATA = ROOT / "data" / "bundled-translations"
STRINGS_FILE = ROOT / "data" / "translation-strings.json"
LANGS = ("he", "es", "fr", "ru")


def load_lists() -> tuple[list[str], dict[str, list[str]]]:
    from bundled_translation_data import TRANSLATIONS  # noqa: WPS433

    all_strings: list[str] = json.loads(STRINGS_FILE.read_text(encoding="utf-8"))["strings"]
    return all_strings, TRANSLATIONS


def main() -> None:
    all_strings, by_lang = load_lists()
    DATA.mkdir(parents=True, exist_ok=True)

    for lang in LANGS:
        values = by_lang.get(lang)
        if not values:
            raise SystemExit(f"Missing TRANSLATIONS['{lang}']")
        if len(values) != len(all_strings):
            raise SystemExit(
                f"{lang}: expected {len(all_strings)} translations, got {len(values)}"
            )
        entries = {src: values[i] for i, src in enumerate(all_strings)}
        out = {"version": 1, "language": lang, "entries": entries}
        dest = DATA / f"{lang}.json"
        dest.write_text(json.dumps(out, ensure_ascii=False, indent=2), encoding="utf-8")
        print(f"Wrote {dest} ({len(entries)} entries)")

    import build_bundled_translation_assets

    build_bundled_translation_assets.main()


if __name__ == "__main__":
    main()
