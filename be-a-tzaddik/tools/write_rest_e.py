#!/usr/bin/env python3
# -*- coding: utf-8
"""Generate rest_data_e.py from overlay missing list."""

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parent
MISSING = json.loads((ROOT / "_overlay_missing.json").read_text(encoding="utf-8"))

# Import existing manual translations for E batch
from rest_e_translations import ENTRIES as E  # noqa

def main():
    missing = [s for s in MISSING if s not in E]
    if missing:
        raise SystemExit(f"Still need {len(missing)}: {missing[0]!r}")
    lines = [
        "# -*- coding: utf-8 -*-",
        "from rest_data import T",
        "",
        "ENTRIES = {",
    ]
    for key, val in E.items():
        he, es, fr, ru = val
        lines.append(f"    {json.dumps(key, ensure_ascii=False)}: T(")
        lines.append(f"        {json.dumps(he, ensure_ascii=False)},")
        lines.append(f"        {json.dumps(es, ensure_ascii=False)},")
        lines.append(f"        {json.dumps(fr, ensure_ascii=False)},")
        lines.append(f"        {json.dumps(ru, ensure_ascii=False)},")
        lines.append("    ),")
    lines.append("}")
    (ROOT / "rest_data_e.py").write_text("\n".join(lines) + "\n", encoding="utf-8")
    print(f"Wrote rest_data_e.py with {len(E)} entries")

if __name__ == "__main__":
    main()
