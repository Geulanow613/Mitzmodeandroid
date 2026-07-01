#!/usr/bin/env python3
"""One-shot translation quality repair — run before compile_full_bundled.py."""

from __future__ import annotations

import subprocess
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
TOOLS = ROOT / "tools"


def run(name: str) -> None:
    path = TOOLS / name
    print(f"\n=== {name} ===")
    subprocess.run([sys.executable, str(path)], cwd=ROOT, check=True)


def main() -> None:
    steps = [
        "_purge_bad_retranslate_shard.py",
        "_apply_hebrew_fixes_total.py",
        "_write_he_hybrid_purge.py",
        "_write_he_alert_tone.py",
        "_fix_fr_omer_days.py",
        "_ru_cyrillic_batch.py",
        "_write_mitzvah_alert_tone.py",
        "_write_he_glue_fixes.py",
        "_write_ru_polish_all.py",
        "_write_ru_deep_fixes.py",
        "compile_full_bundled.py",
    ]
    for step in steps:
        run(step)
    print("\n=== total_quality_repair complete ===")


if __name__ == "__main__":
    main()
