#!/usr/bin/env python3
"""Fix French Omer count strings: 'days de l'Omer' -> 'jours de l'Omer'."""

from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
PATH = ROOT / "data" / "translation-catalog" / "human" / "explainer_template_args.json"

DAYS_FIX = re.compile(r"(\d+)\s+days de l'Omer", re.I)
DAYS_FIX2 = re.compile(r"et (\d+) days de l'Omer", re.I)


def fix_fr_value(text: str) -> str:
    text = DAYS_FIX2.sub(r"et \1 jours de l'Omer", text)
    text = DAYS_FIX.sub(r"\1 jours de l'Omer", text)
    text = re.sub(r"\b(\d+) days\b", r"\1 jours", text)
    return text


def main() -> None:
    data = json.loads(PATH.read_text(encoding="utf-8"))
    fr = data.get("fr", {})
    changed = 0
    for k, v in list(fr.items()):
        fixed = fix_fr_value(v)
        if fixed != v:
            fr[k] = fixed
            changed += 1
    data["fr"] = fr
    PATH.write_text(json.dumps(data, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"fixed {changed} French Omer/day strings in {PATH.name}")


if __name__ == "__main__":
    main()
