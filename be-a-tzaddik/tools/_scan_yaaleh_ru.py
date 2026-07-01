#!/usr/bin/env python3
"""Scan RU bundle for formal вы-style Yaaleh copy."""
from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
RU = ROOT / "shared/src/commonMain/composeResources/files/translations/ru.json"

FORMAL = (
    "Добавьте",
    "вставьте",
    "продолжайте",
    "вернитесь",
    "повторите",
    "произносите",
    "добавьте",
    "забыли",
    "Закончили",
    "Продолжайте",
    "не возвращайтесь",
)


def main() -> None:
    data = json.loads(RU.read_text(encoding="utf-8"))
    ru = data.get("entries", data)
    yaaleh_keys = [
        k
        for k in ru
        if "Yaaleh" in k
        or "yaaleh" in k.lower()
        or (k.startswith("If you forgot") and "Retzei" in k)
        or k.startswith("Add Yaaleh")
        or (k.startswith("If you recite") and "Yaaleh" in k)
    ]
    hits = []
    for k in yaaleh_keys:
        v = ru[k]
        bad = [f for f in FORMAL if f in v]
        if bad:
            hits.append((k[:70], bad, len(v)))
    print(f"yaaleh-related keys: {len(yaaleh_keys)}")
    print(f"formal hits: {len(hits)}")
    for h in hits:
        print(f"  {h[0]}... {h[1]} len={h[2]}")
    for k, v in ru.items():
        if k.startswith("Add Yaaleh") and "Shacharit" in k:
            print(f"\nShacharit full: len={len(v)}")
            print(f"  formal: {[f for f in FORMAL if f in v]}")
            print(f"  start: {v[:180]}...")
            break
    for k, v in ru.items():
        if k.startswith("Yaaleh V'Yavo is a special"):
            print(f"\nOverview: len={len(v)}")
            print(f"  formal: {[f for f in FORMAL if f in v]}")
            break


if __name__ == "__main__":
    main()
