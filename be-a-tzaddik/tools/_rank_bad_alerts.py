#!/usr/bin/env python3
"""Rank bad Hebrew alert keys for he_alert_tone batch authoring."""
from __future__ import annotations

import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT / "tools"))
from _write_he_alert_tone import ALLOWED_LATIN, HYBRID, MT_MARKERS, is_bad_he  # noqa: E402

HUMAN = ROOT / "data" / "translation-catalog" / "human"


def severity(v: str) -> int:
    s = 0
    if HYBRID.search(v):
        s += 50
    for w in re.findall(r"[A-Za-z]{3,}", v):
        if w not in ALLOWED_LATIN:
            s += 10
    for m in MT_MARKERS:
        if m in v:
            s += 5
    if "מלאצ" in v or "Borer" in v or "Bishul" in v or "Mishkan" in v:
        s += 40
    return s


def main() -> None:
    mitz = json.loads((HUMAN / "mitzvah_alert_tone.json").read_text(encoding="utf-8"))["he"]
    he_tone = json.loads((HUMAN / "he_alert_tone.json").read_text(encoding="utf-8"))["he"]
    need = []
    for k, v in mitz.items():
        if k in he_tone:
            continue
        if is_bad_he(v):
            need.append({"score": severity(v), "key": k, "he": v})
    need.sort(key=lambda x: (-x["score"], x["key"]))
    out = ROOT / "_worst50_keys.json"
    out.write_text(json.dumps(need[:55], ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"exported {min(55, len(need))} of {len(need)} bad keys")


if __name__ == "__main__":
    main()
