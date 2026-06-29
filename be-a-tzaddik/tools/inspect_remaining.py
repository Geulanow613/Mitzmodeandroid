#!/usr/bin/env python3
"""Inspect remaining CRITICAL issues."""
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
COMPILED = ROOT / "shared/src/commonMain/composeResources/files/translations"
STRINGS = json.loads((ROOT / "data/translation-catalog/strings.json").read_text(encoding="utf-8"))["strings"]
STRINGS_SET = set(STRINGS)

def kotlin_blocks(s: str) -> list[str]:
    return re.findall(r"\$\{[^}]*\}", s)

def bare_vars(s: str) -> list[str]:
    return re.findall(r"(?<!\$)\$[a-zA-Z_][a-zA-Z0-9_]*", s)

for lang in ("he", "es"):
    data = json.loads((COMPILED / f"{lang}.json").read_text(encoding="utf-8"))["entries"]
    for k in data:
        if k.startswith("Today is Erev Yom Kippur"):
            en = k
            tr = data[k]
            print(f"=== {lang} Erev YK ===")
            print("en blocks", len(kotlin_blocks(en)), "vars", bare_vars(en))
            print("tr blocks", len(kotlin_blocks(tr)), "vars", bare_vars(tr))
            print("en block lens", [len(b) for b in kotlin_blocks(en)])
            print("tr block lens", [len(b) for b in kotlin_blocks(tr)])
            (ROOT / "data/translation-catalog").joinpath(f"debug_erev_{lang}.txt").write_text(tr, encoding="utf-8")
        if k.startswith("The week before Shavuot"):
            print(f"=== es Shavuot (in {lang}) ===")
            m = re.search(r"vacaciones?", data[k], re.I)
            if m:
                print(data[k][max(0, m.start() - 40) : m.end() + 40])
        if k.startswith('The Torah commands: "Honor your father'):
            m = re.search(r"vacaciones?", data[k], re.I)
            if m:
                print(f"=== honor ({lang}) ===")
                print(data[k][max(0, m.start() - 40) : m.end() + 40])
