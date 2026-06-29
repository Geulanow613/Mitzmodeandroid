#!/usr/bin/env python3
"""Merge hand-translated long strings into long-multi.json."""

from __future__ import annotations

import json
from pathlib import Path

DATA = Path(__file__).resolve().parents[1] / "data" / "bundled-translations"


def main() -> None:
    from generate_long_multi import build_table  # noqa: WPS433

    en = json.loads((DATA / "long-en-all.json").read_text(encoding="utf-8"))
    payload = build_table(en)

    overlay_path = DATA / "long-hand-overlay.json"
    if overlay_path.exists():
        overlay = json.loads(overlay_path.read_text(encoding="utf-8"))
        for lang in ("he", "es", "fr", "ru"):
            for i, text in overlay.get(lang, {}).items():
                payload[lang][int(i)] = text

    (DATA / "long-multi.json").write_text(json.dumps(payload, ensure_ascii=False, indent=2), encoding="utf-8")
    print("Updated long-multi.json")


if __name__ == "__main__":
    main()
