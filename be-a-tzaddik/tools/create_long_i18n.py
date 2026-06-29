#!/usr/bin/env python3
"""Build long-i18n.json from per-index translation table + English sources."""

from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
DATA = ROOT / "data" / "bundled-translations"


def load_en() -> list[str]:
    files = sorted(DATA.glob("long-en-*.txt"))
    return [p.read_text(encoding="utf-8") for p in files]


def main() -> None:
    from long_translation_table import ROWS  # noqa: WPS433

    en = load_en()
    if len(ROWS) != len(en):
        raise SystemExit(f"ROWS {len(ROWS)} != english {len(en)}")

    out = {lang: [] for lang in ("he", "es", "fr", "ru")}
    for i, row in enumerate(ROWS):
        for lang in out:
            if lang not in row:
                raise SystemExit(f"row {i} missing {lang}")
            out[lang].append(row[lang])

    (DATA / "long-i18n.json").write_text(json.dumps(out, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"Wrote long-i18n.json ({len(en)} entries x 4 langs)")


if __name__ == "__main__":
    main()
