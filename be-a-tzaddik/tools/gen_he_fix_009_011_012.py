#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""Generate he_fix_009/011/012_he_only.json — pure Hebrew translations."""
from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1] / "data" / "translation-catalog"
HUMAN = ROOT / "human"

MIXED_RE = re.compile(
    r"[\u0590-\u05FF]+[A-Za-z\u0400-\u04FF]+|[A-Za-z\u0400-\u04FF]+[\u0590-\u05FF]+"
)


def hb(*codes: int) -> str:
    return "".join(chr(c) for c in codes)


LL = hb(0x05DC, 0x05D5, 0x05DC, 0x05D1)
ET = hb(0x05D0, 0x05EA, 0x05E8, 0x05D5, 0x05D2)
HD = hb(0x05D4, 0x05D3, 0x05E1, 0x05D9, 0x05DD)
AR = hb(0x05E2, 0x05E8, 0x05D1, 0x05D5, 0x05EA)
RV = hb(0x05D4, 0x05E8, 0x05B8, 0x05D1)
SY = hb(0x05E1, 0x05D9, 0x05D5, 0x05DD)
GM = hb(0x05D2, 0x05DE, 0x05E8, 0x05D0)
CZ = hb(0x05D7, 0x05DB, 0x05DE, 0x05D9, 0x05DF)
HL = hb(0x05D4, 0x05DC, 0x05DB, 0x05D4)
AG = hb(0x05D0, 0x05D2, 0x05D3, 0x05D4)
TH = hb(0x05EA, 0x05D4, 0x05D9, 0x05DC, 0x05D9, 0x05DD)
HGD = hb(0x05D4, 0x05D2, 0x05D3, 0x05D4)
SKN = hb(0x05D4, 0x05E9, 0x05DB, 0x05D9, 0x05E0, 0x05D4)
CHZ = hb(0x05D7, 0x05D6, 0x05DF)
SHMA = hb(0x05E9, 0x05DE, 0x05E2)
HMPIL = hb(0x05D4, 0x05DE, 0x05D0, 0x05E4, 0x05D9, 0x05DC)
RBI_SHIMON = hb(
    0x05E8, 0x05D1, 0x05D9, 0x0020, 0x05E9, 0x05DE, 0x05E2, 0x05D5, 0x05DF,
    0x0020, 0x05D1, 0x05E8, 0x0020, 0x05D9, 0x05D5, 0x05D7, 0x05D0, 0x05D9,
)
YECHAVEH = hb(0x05D9, 0x05D7, 0x05D5, 0x05D5, 0x05D4, 0x0020, 0x05D3, 0x05E2, 0x05EA)
SHUAR = hb(0x05E9, 0x05D5, 0x05DC, 0x05D7, 0x05DF, 0x0020, 0x05E2, 0x05E8, 0x05D5, 0x05DA)
REMA = hb(0x05D4, 0x05E8, 0x05DE, 0x05D4)
KIDUSH_LVN = hb(0x05E7, 0x05D9, 0x05D3, 0x05D5, 0x05E9, 0x0020, 0x05DC, 0x05D1, 0x05E0, 0x05D4)


def check_pure(strings: list[str], label: str) -> None:
    bad = []
    for i, s in enumerate(strings):
        for m in MIXED_RE.finditer(s):
            g = m.group()
            if any(x in g for x in ("HebrewCalendar", "profile", "tomorrowCal", "fullSchedule", "alotTomorrow")):
                continue
            bad.append((i + 1, g))
    if bad:
        raise SystemExit(f"{label} mixed Latin/Cyrillic in Hebrew: {bad}")


def write_batch(name: str, strings: list[str]) -> None:
    check_pure(strings, name)
    keys = json.loads((ROOT / f"_keys_{name}.json").read_text(encoding="utf-8"))
    if len(strings) != len(keys):
        raise SystemExit(f"{name}: {len(strings)} strings != {len(keys)} keys")
    path = HUMAN / f"{name}_he_only.json"
    path.write_text(
        json.dumps({"he": strings}, ensure_ascii=False, indent=2) + "\n",
        encoding="utf-8",
    )
    print(f"OK {path.name}: {len(strings)} strings")


TALMUD = (
    f"\u05ea\u05dc\u05de\u05d5\u05d3 \u2014 \u05d4\u05ea\u05dc\u05de\u05d5\u05d3 ({GM} + "
    f"\u05de\u05e9\u05e0\u05d4) \u05d4\u05d5\u05d0 \u05d3\u05d9\u05d5\u05df {CZ} "
    f"\u05d4\u05de\u05e8\u05db\u05d6\u05d9 \u05d1{HL} \u05d5\u05d1{AG} \u2014 "
    "\u05d4\u05ea\u05e4\u05ea\u05d7 \u05d1\u05d1\u05d1\u05dc \u05d5\u05d1\u05d0\u05e8\u05e5 "
    "\u05d9\u05e9\u05e8\u05d0\u05dc. \u05dc\u05d5\u05de\u05d3\u05d9 \u05d3\u05e3 \u05d9\u05d5\u05de\u05d9 "
    "\u05de\u05e1\u05d9\u05d9\u05de\u05d9\u05dd \u05d0\u05ea \u05db\u05dc \u05d4\u05ea\u05dc\u05de\u05d5\u05d3 "
    "\u05d1\u05db\u05e9\u05d1\u05e2 \u05e9\u05e0\u05d9\u05dd. \u05d0\u05e4\u05e9\u05e8 \u05dc\u05d4\u05ea\u05d7\u05d9\u05dc "
    "\u05d1\u05e7\u05d8\u05df; \u05d0\u05e4\u05d9\u05dc\u05d5 \u05e9\u05d5\u05e8\u05d4 \u05d0\u_ch\u05ea \u05e2\u05dd "
    f"\u05e4\u05d9\u05e8\u05d5\u05e9 \u05d1\u05d5\u05e0\u05d4 \u05d0\u05d5\u05e8\u05d9\u05d9\u05e0\u05d5\u05ea \u05d9\u05d4\u05d5\u05d3\u05d9\u05ea. "
    f"\u05d4\u05d5\u05d0 \u05e2\u05de\u05d5\u05d3 \u05d4\u05e9\u05d3\u05e8\u05d4 \u05e9\u05dc \u05d4\u05ea\u05e4\u05ea\u05d7\u05d5\u05ea \u05d4{HL}."
)
