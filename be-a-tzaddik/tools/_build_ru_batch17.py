#!/usr/bin/env python3
"""Build _ru_top50_batch17_data.py from _ru_batch17_manual.json."""
from __future__ import annotations

import json
import subprocess
import sys
from pathlib import Path

MANUAL_JSON = Path(__file__).parent / "_ru_batch17_manual.json"
OUT = Path(__file__).parent / "_ru_top50_batch17_data.py"
KEYS_PATH = Path(__file__).parent / "_batch17_keys.json"


def main() -> None:
    gen = Path(__file__).parent / "_gen_batch17_manual_json.py"
    if not MANUAL_JSON.exists():
        subprocess.run([sys.executable, str(gen)], check=True)
    manual: dict[str, str] = json.loads(MANUAL_JSON.read_text(encoding="utf-8"))
    keys: list[str] = json.loads(KEYS_PATH.read_text(encoding="utf-8"))
    missing = [k for k in keys if k not in manual]
    if missing:
        raise SystemExit(f"missing manual ({len(missing)}): {missing[0]!r}")
    batch = {k: manual[k] for k in keys}

    lines = [
        '"""Batch 17: final guillemet glossary n–z — explicit mappings."""',
        "",
        "from __future__ import annotations",
        "",
        "TOP50_BATCH17: dict[str, str] = {",
    ]
    for k in keys:
        lines.append(f"    {json.dumps(k, ensure_ascii=False)}: {json.dumps(batch[k], ensure_ascii=False)},")
    lines.append("}")
    lines.append("")
    OUT.write_text("\n".join(lines) + "\n", encoding="utf-8")
    print(f"Wrote {len(batch)} entries to {OUT.name}")


if __name__ == "__main__":
    main()
