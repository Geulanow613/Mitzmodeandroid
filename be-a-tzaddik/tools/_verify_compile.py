#!/usr/bin/env python3
"""Quick post-compile sanity check."""
import json
from pathlib import Path
ROOT = Path(__file__).resolve().parents[1]
COMPOSE = ROOT / "shared/src/commonMain/composeResources/files/translations"

psalm_key_fragment = "rivers of Babylon"
for lang in ("he", "es", "fr", "ru"):
    b = json.loads((COMPOSE / f"{lang}.json").read_text(encoding="utf-8"))
    entries = b["entries"]
    key = next((k for k in entries if psalm_key_fragment in k), None)
    if key:
        val = entries[key]
        ph0 = "PH0" in val
        mirth = "mirth" in val
        print(f"{lang} psalm137: PH0={ph0} mirth={mirth} | {val[:100]}")
    else:
        print(f"{lang}: psalm key NOT FOUND")
    show = entries.get("Show translation", "MISSING")
    hide = entries.get("Hide translation", "MISSING")
    print(f"  Show translation={show!r}")
    print(f"  Hide translation={hide!r}")
    print()
