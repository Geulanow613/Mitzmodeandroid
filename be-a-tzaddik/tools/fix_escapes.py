#!/usr/bin/env python3
import re
from pathlib import Path

TOOLS = Path(__file__).resolve().parent
for fn in ["write_ilanot.py", "write_sidecars.py", "generate_all.py"]:
    p = TOOLS / fn
    if not p.exists():
        continue
    t = p.read_text(encoding="utf-8")
    t = re.sub(r"(\\u05[0-9a-fA-F]{2})\\\"(\\u05)", r"\1\\u05f4\2", t)
    t = re.sub(r"(\\u05[0-9a-fA-F]{2})\\\"(\\u05[0-9a-fA-F]{2})", r"\1\\u05f4\2", t)
    t = re.sub(r"\\u05d\\\"\\u05([0-9a-fA-F]{2})", r"\\u05f4\\u05\\1", t)
    p.write_text(t, encoding="utf-8")
    print("patched", fn)
