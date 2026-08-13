#!/usr/bin/env python3
"""Validate and refresh _ru_batch13_manual.json via _gen_batch13_manual_json.py."""
from __future__ import annotations

import json
import subprocess
import sys
from pathlib import Path

CAND = Path(__file__).parent / "_batch13_candidates.json"
OUT = Path(__file__).parent / "_ru_batch13_manual.json"
GEN = Path(__file__).parent / "_gen_batch13_manual_json.py"

SKIP_KEYS = {
    "Beardy Top Productions",
    "www.beardy.top",
    "https://www.beardy.top",
    "XL",
    "Rav",
    "e.g., Sarah B.",
    "Listen to more Jewish music from G.E.U.L.A",
    "Performed by G.E.U.L.A © 2026",
}


def main() -> None:
    subprocess.run([sys.executable, str(GEN)], check=True)
    manual = json.loads(OUT.read_text(encoding="utf-8"))
    candidates = json.loads(CAND.read_text(encoding="utf-8"))
    keys = [c["key"] for c in candidates if c["key"] not in SKIP_KEYS][:50]
    missing = [k for k in keys if k not in manual]
    extra = [k for k in manual if k not in keys]
    if missing:
        raise SystemExit(f"missing ({len(missing)}): {missing[0][:80]}...")
    if extra:
        raise SystemExit(f"extra ({len(extra)}): {extra[0][:80]}...")
    print(f"Validated {len(manual)} entries in {OUT.name}")


if __name__ == "__main__":
    raise SystemExit(main())
