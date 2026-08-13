#!/usr/bin/env python3
"""Build _ru_top50_batch13_data.py from _ru_batch13_manual.json."""
from __future__ import annotations

import json
import subprocess
import sys
from pathlib import Path

CAND = Path(__file__).parent / "_batch13_candidates.json"
MANUAL_JSON = Path(__file__).parent / "_ru_batch13_manual.json"
OUT = Path(__file__).parent / "_ru_top50_batch13_data.py"

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
    gen = Path(__file__).parent / "_gen_batch13_manual_json.py"
    if not MANUAL_JSON.exists():
        subprocess.run([sys.executable, str(gen)], check=True)
    if not CAND.exists():
        subprocess.run([sys.executable, str(Path(__file__).parent / "_gen_batch13_candidates.py")], check=True)

    manual: dict[str, str] = json.loads(MANUAL_JSON.read_text(encoding="utf-8"))
    candidates = json.loads(CAND.read_text(encoding="utf-8"))
    keys = [c["key"] for c in candidates if c["key"] not in SKIP_KEYS][:50]
    missing = [k for k in keys if k not in manual]
    if missing:
        raise SystemExit(f"missing manual ({len(missing)}): {missing[0][:80]}...")
    batch = {k: manual[k] for k in keys}
    if len(batch) != 50:
        raise SystemExit(f"expected 50 keys, got {len(batch)}")

    lines = [
        '"""Batch 13: next 50 Latin-gap keys — explicit mappings."""',
        "",
        "from __future__ import annotations",
        "",
        "TOP50_BATCH13: dict[str, str] = {",
    ]
    for k in keys:
        lines.append(f"    {json.dumps(k, ensure_ascii=False)}: {json.dumps(batch[k], ensure_ascii=False)},")
    lines.append("}")
    lines.append("")
    OUT.write_text("\n".join(lines) + "\n", encoding="utf-8")
    print(f"Wrote {len(batch)} entries to {OUT.name}")


if __name__ == "__main__":
    main()
