#!/usr/bin/env python3
"""Write long/{lang}/*.txt from long-multi.json."""

from __future__ import annotations

import json
from pathlib import Path

DATA = Path(__file__).resolve().parents[1] / "data" / "bundled-translations"


def main() -> None:
    multi = json.loads((DATA / "long-multi.json").read_text(encoding="utf-8"))
    for lang, texts in multi.items():
        folder = DATA / "long" / lang
        folder.mkdir(parents=True, exist_ok=True)
        for i, text in enumerate(texts):
            (folder / f"{i:02d}.txt").write_text(text, encoding="utf-8")
        print(f"Wrote {len(texts)} long files for {lang}")


if __name__ == "__main__":
    main()
