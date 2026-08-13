#!/usr/bin/env python3
"""Merge *_only.json language files and build final human batch JSON."""

from __future__ import annotations

import subprocess
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent
GEN = ROOT / "gen_mitzvot_batch.py"


def batches_with_only_files() -> list[str]:
    human = ROOT.parent / "data/translation-catalog/human"
    names: set[str] = set()
    for p in human.glob("*_he_only.json"):
        names.add(p.name.replace("_he_only.json", ""))
    return sorted(names)


def main() -> None:
    if len(sys.argv) > 1:
        batches = sys.argv[1:]
    else:
        batches = batches_with_only_files()
    if not batches:
        print("No *_he_only.json files to merge")
        return
    for batch in batches:
        subprocess.run([sys.executable, str(GEN), batch, "--merge"], check=True)
        subprocess.run([sys.executable, str(GEN), batch], check=True)


if __name__ == "__main__":
    main()
