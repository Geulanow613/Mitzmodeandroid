#!/usr/bin/env python3
"""Remove translation staging artifacts after polish merge."""
from __future__ import annotations

from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CATALOG = ROOT / "data" / "translation-catalog"
HUMAN = CATALOG / "human"

GLOB_PATTERNS = (
    "*_only.json",
    "*_src.json",
    "content_long_*.json",
    "content_medium_*.json",
)

TEMP_FILES = (
    CATALOG / "_short_mitzvot_dump.json",
    CATALOG / "_six_keys.json",
)


def main() -> None:
    removed: list[Path] = []
    for pattern in GLOB_PATTERNS:
        for path in sorted(HUMAN.glob(pattern)):
            path.unlink(missing_ok=True)
            removed.append(path)
    for path in TEMP_FILES:
        if path.exists():
            path.unlink()
            removed.append(path)
    print(f"Removed {len(removed)} files")
    for path in removed:
        print(f"  {path}")


if __name__ == "__main__":
    main()
