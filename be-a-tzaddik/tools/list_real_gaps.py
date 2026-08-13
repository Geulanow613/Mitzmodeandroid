#!/usr/bin/env python3
"""List real translation gaps (exclude technical passthrough keys)."""
import json
import sys
from pathlib import Path

sys.stdout.reconfigure(encoding="utf-8")
ROOT = Path(__file__).resolve().parents[1]
req = json.loads((ROOT / "data/translation-catalog/strings.json").read_text(encoding="utf-8"))["strings"]
TECHNICAL = {
    r"\s*/\s*",
    "$mitzvotCount",
    "halachic_term",
    "www.beardy.top",
    "Beardy Top Productions",
    "https://www.beardy.top",
}
for lang in ("es", "fr", "ru"):
    ent = json.loads(
        (ROOT / f"shared/src/commonMain/composeResources/files/translations/{lang}.json").read_text(
            encoding="utf-8"
        )
    )["entries"]
    miss = [
        s
        for s in req
        if ent.get(s, s) == s and len(s) > 2 and s not in TECHNICAL and not s.startswith("(?i)")
        and not s.startswith("$translated")
    ]
    print(f"{lang}: {len(miss)} real misses")
    for s in miss:
        print(f"  [{len(s)}] {s[:120]}")
