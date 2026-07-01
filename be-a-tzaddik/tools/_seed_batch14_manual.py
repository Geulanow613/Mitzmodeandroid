#!/usr/bin/env python3
"""Validate _ru_batch14_manual.json."""
from __future__ import annotations

import json
import subprocess
import sys
from pathlib import Path

GEN = Path(__file__).parent / "_gen_batch14_manual_json.py"
OUT = Path(__file__).parent / "_ru_batch14_manual.json"
KEYS_PATH = Path(__file__).parent / "_batch14_keys.json"


def main() -> None:
    subprocess.run([sys.executable, str(GEN)], check=True)
    manual = json.loads(OUT.read_text(encoding="utf-8"))
    keys = json.loads(KEYS_PATH.read_text(encoding="utf-8"))
    assert set(manual) == set(keys), "manual/keys mismatch"
    print(f"Validated {len(manual)} entries")


if __name__ == "__main__":
    raise SystemExit(main())
