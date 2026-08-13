# -*- coding: utf-8
"""Apply translation_repairs and fix known Hebrew corruption in local batch files."""
from __future__ import annotations

import json
import re
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
from translation_repairs import repair_translation

HUMAN = Path(__file__).resolve().parents[1] / "data" / "translation-catalog" / "human"

FILES = [
    "local_003_es_only.json",
    "local_003_fr_only.json",
    "local_003_ru_only.json",
    "local_004_he_only.json",
    "local_004_es_only.json",
    "local_004_fr_only.json",
    "local_004_ru_only.json",
    "local_005_es_only.json",
    "local_005_fr_only.json",
    "local_005_ru_only.json",
    "local_006_he_only.json",
    "local_006_es_only.json",
    "local_006_fr_only.json",
    "local_006_ru_only.json",
]

HE_FIXES = [
    (r"посвятил", "\u05d4\u05e7\u05d3\u05d9\u05e9"),
    (r"\u05e4esach", "\u05e4\u05e1\u05d7"),
    (r"\u05e4esach", "\u05e4\u05e1\u05d7"),
    (r"\u05dbohanim", "\u05d4\u05db\u05d4\u05e0\u05d9\u05dd"),
    (r"\u05dbohen", "\u05db\u05d4\u05df"),
    (r"\u05d1\u05d1\u05d9\u05ea \u05d4\u05db\u05e0\u05e1et", "\u05d1\u05d1\u05d9\u05ea \u05d4\u05db\u05e0\u05e1\u05ea"),
    (r"\u05dc\u05d9\u05e9\u05e8\u05d0el", "\u05dc\u05d9\u05e9\u05e8\u05d0\u05dc"),
    (r"\u05e9idduch", "shidduch"),
    (r"\u05deordechai", "Mordechai"),
    (r"\u05d7\u05db\u05de\u05d9\u05e0u", "\u05d7\u05db\u05de\u05d9\u05e0\u05d5"),
    (r"\u05d0\u05e0\u05d7\u05e0u", "\u05d0\u05e0\u05d7\u05e0\u05d5"),
    (r"\u05de\u05e6\u05d5at", "\u05de\u05e6\u05d5\u05ea"),
    (r"\u05de\u05e6\u05d5at", "\u05de\u05e6\u05d5\u05ea"),
    (r"\u05d1\u05e7\u05e8u", "\u05d1\u05e7\u05e8\u05d5"),
    (r"\u05d7\u05d6\u05e8u", "\u05d7\u05d6\u05e8\u05d5"),
    (r"\u05e2\u05e6\u05e8u", "\u05e2\u05e6\u05e8\u05d5"),
    (r"\u05d6\u05db\u05e8u", "\u05d6\u05db\u05e8\u05d5"),
    (r"\u05ea\u05e0u", "\u05ea\u05e0\u05d5"),
    (r"\u05d0\u05de\u05d9\u05e8at \u05e9ma", "\u05d0\u05de\u05d9\u05e8\u05ea \u05e9\u05de\u05e2"),
    (r"\u05dc\u05e2\u05d6\u05d5\u05e8 \u05dc\u05d4\u05ea\u05e4\u05d9\u05d9\u05d9\u05e9", "\u05dc\u05e2\u05d6\u05d5\u05e8 \u05dc\u05d4\u05ea\u05e4\u05d9\u05d9\u05e9"),
    (r"\u05de\u05d9\u05e9\u05d4u", "\u05de\u05d9\u05e9\u05d4\u05d5"),
    (r"\u05d0\u05e4\u05d9\u05dco", "\u05d0\u05e4\u05d9\u05dc\u05d5"),
    (r"\u05d4\u05ea\u05e4\u05dc\u05dcu", "\u05d4\u05ea\u05e4\u05dc\u05dc\u05d5"),
    (r"\u05e7\u05d7u", "\u05e7\u05d7\u05d5"),
    (r"\u05e0\u05e1u", "\u05e0\u05e1\u05d5"),
    (r"\u05ea\u05e0u", "\u05ea\u05e0\u05d5"),
    (r"\u05d1\u05e8\u05dbu", "\u05d1\u05e8\u05db\u05d5"),
    (r"\u05d4\u05ea\u05e7\u05e9\u05e8u", "\u05d4\u05ea\u05e7\u05e9\u05e8\u05d5"),
    (r"\u05e9losh", "\u05e9\u05dc\u05d5\u05e9"),
    (r"\u05d1\u05d9\u05d0at", "\u05d1\u05d9\u05d0\u05ea"),
    (r"\u05d3\u05e2at", "\u05d3\u05e2\u05ea"),
    (r"\u05d4\u05d7ag", "\u05d4\u05d7\u05d2"),
    (r"\u05dc\u05db\u05d1\u05d5\u05d3 \u05d4\u05d7ag", "\u05dc\u05db\u05d1\u05d5\u05d3 \u05d4\u05d7\u05d2"),
    (r"\u05d4\u05e6\u05d1at", "\u05d4\u05e6\u05d1\u05ea"),
    (r"\u05e0\u05e1u", "\u05e0\u05e1\u05d5"),
    (r"\u05d4illel", "Hillel"),
    (r"\u05eaalmidei", "\u05ea\u05dc\u05de\u05d9\u05d3\u05d9"),
    (r"\u05e8odefei", "\u05e8\u05d5\u05d3\u05e4\u05d9"),
    (r"\u05e7\u05d9\u05d1\u05dcu", "\u05e7\u05d9\u05d1\u05dc\u05d5"),
    (r"\u05e7\u05e8\u05d1u", "\u05e7\u05e8\u05d1\u05d5"),
    (r"\u05d4\u05ea\u05e4\u05d9is", "\u05dc\u05d4\u05ea\u05e4\u05d9\u05d9\u05d9\u05e9"),
    (r"\u05de\u05d0\u05d6\u05d9\u05e8\u05d9\u05dd", "\u05de\u05d0\u05d6\u05d9\u05e8\u05d9\u05dd"),
    (r"\u05d4\u05e6ibur", "\u05d4\u05e6\u05d9\u05d1\u05d5\u05e8"),
    (r"\u05d0\u05d4\u05d1at", "\u05d0\u05d4\u05d1\u05ea"),
    (r"\u05d9\u05e9\u05e8\u05d0el", "\u05d9\u05e9\u05e8\u05d0\u05dc"),
    (r"\u05d9am Suf", "\u05d9\u05dd \u05e1\u05d5\u05e3"),
    (r"\u05e0\u05d9\u05e9\u05d5\u05d0in", "\u05e0\u05d9\u05e9\u05d5\u05d0\u05d9\u05df"),
    (r"\u05dcshidduch", "\u05dc\u05e9\u05d9\u05d3\u05d5\u05da"),
    (r"\u05de\u05ea\u05e2 wandering", "\u05de\u05ea\u05e2\u05d5\u05ea\u05d9\u05dd"),
    (r"\u05d4\u05e9\u05d1at", "\u05d4\u05e9\u05d1\u05ea"),
    (r"\u05d4\u05e7\u05d3\u05d9\u05e9u", "\u05d4\u05e7\u05d3\u05d9\u05e9\u05d5"),
    (r"\u05d4\u05d7\u05d6\u05e8at", "\u05d4\u05d7\u05d6\u05e8\u05ea"),
    (r"\u05d4\u05d7\u05d6\u05e8at", "\u05d4\u05d7\u05d6\u05e8\u05ea"),
]


ES_FIXES = [
    (r"\u27e6T0", "Yesodei HaTorah:"),
    (r"\u27e6T1:?", ":"),
    (r"\u27e6T1", ""),
    (r"\u27e7T0", ""),
]


def fix_es(text: str) -> str:
    for pat, repl in ES_FIXES:
        text = re.sub(pat, repl, text)
    return repair_translation("es", text)


def fix_he(text: str) -> str:
    for pat, repl in HE_FIXES:
        text = re.sub(pat, repl, text)
    return repair_translation("he", text)


def main() -> None:
    for name in FILES:
        path = HUMAN / name
        data = json.loads(path.read_text(encoding="utf-8"))
        lang = next(iter(data))
        data[lang] = [
            fix_he(s)
            if lang == "he"
            else fix_es(s)
            if lang == "es"
            else repair_translation(lang, s)
            for s in data[lang]
        ]
        path.write_text(json.dumps(data, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
        print(f"repaired {name} ({len(data[lang])} strings)")


if __name__ == "__main__":
    main()
