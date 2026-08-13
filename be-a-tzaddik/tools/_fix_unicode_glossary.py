#!/usr/bin/env python3
"""Fix invalid \\u05X escape typos in he_glossary_part2_data.py."""

from __future__ import annotations

import re
from pathlib import Path

PART2 = Path(__file__).resolve().parent / "he_glossary_part2_data.py"
GEN = Path(__file__).resolve().parent / "gen_he_glossary.py"

# Latin letter mistakenly used after \\u05 instead of hex digits.
FIXES = {
    "l": "05dc",  # lamed
    "t": "05ea",  # tav
    "m": "05dd",  # final mem
    "s": "05e1",  # samekh
    "i": "05d9",  # yod
}


def fix_content(text: str) -> str:
    text = text.replace("\\u05it", "\\u05d9\\u05ea")
    text = text.replace("\\u05–", "\\u05d6\\u05de\\u05e0\\u05d9")
    text = text.replace('"\\u39 ', '"\\u05dc\\u05f9\\u05d8 ')
    text = text.replace(" \\u39 ", " \\u05dc\\u05f9\\u05d8 ")
    text = text.replace("\\u05i\\u05ea", "\\u05d9\\u05ea")
    text = re.sub(r"\\u39(?![0-9a-fA-F])", r"\\u05dc\\u05f9\\u05d8", text)

    def repl(m: re.Match[str]) -> str:
        ch = m.group(1)
        if ch in FIXES:
            return "\\u" + FIXES[ch]
        return m.group(0)

    return re.sub(r"\\u05([^0-9a-fA-F])", repl, text)


def main() -> None:
    for path in (PART2, GEN):
        if not path.exists():
            continue
        text = path.read_text(encoding="utf-8")
        fixed = fix_content(text)
        if path == GEN:
            fixed = re.sub(
                r'"Yaknehaz": "[^"]*"',
                '"Yaknehaz": "YAKNEHAZ_PLACEHOLDER"',
                fixed,
                count=1,
            )
            fixed = fixed.replace(
                '"Yaknehaz": "YAKNEHAZ_PLACEHOLDER"',
                '"Yaknehaz": "\\u05d9\\u05e7\\u05e0\\u05d4\\u05f4\\u05d6"',
            )
        compile(fixed, str(path), "exec")
        if fixed != text:
            path.write_text(fixed, encoding="utf-8")
            print(f"fixed {path.name}")
        else:
            print(f"unchanged {path.name}")


if __name__ == "__main__":
    main()
