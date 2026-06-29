#!/usr/bin/env python3
"""Scan human shards for quality issues in mitzvot translations."""
from __future__ import annotations

import json
import re
import sys
from pathlib import Path

sys.stdout.reconfigure(encoding="utf-8")

ROOT = Path(__file__).resolve().parents[1]
HUMAN = ROOT / "data/translation-catalog/human"
LANGS = ("he", "es", "fr", "ru")

BAD = [
    re.compile(r"⟦\d"),
    re.compile(r"\u27e6|\u27e7"),
    re.compile(r"איריירי"),  # argos corruption in es
    re.compile(r"Kabb[aаА]l"),
    re.compile(r"\bKayin\b|\bHevel\b"),  # in he should be קין/הבל often ok in es
]


def main() -> None:
    issues = []
    for path in sorted(HUMAN.glob("*.json")):
        if any(x in path.name for x in ("_only", "_src", "_keys")) or path.name.startswith("_"):
            continue
        data = json.loads(path.read_text(encoding="utf-8"))
        if not isinstance(data, dict):
            continue
        for lang in LANGS:
            block = data.get(lang)
            if not isinstance(block, dict):
                continue
            for key, tr in block.items():
                if not isinstance(tr, str) or tr == key:
                    continue
                for pat in BAD:
                    if pat.search(tr):
                        issues.append(
                            {
                                "file": path.name,
                                "lang": lang,
                                "pattern": pat.pattern,
                                "key": key[:80],
                                "tr": tr[:100],
                            }
                        )
                        break
                # he: excessive english in long mitzvot
                if lang == "he" and len(key) > 150:
                    letters = [c for c in re.sub(r"\$\{[^}]*\}|\$[a-zA-Z_][a-zA-Z0-9_]*", "", tr) if c.isalpha()]
                    if letters:
                        lr = sum(1 for c in letters if c.isascii()) / len(letters)
                        if lr > 0.12:
                            issues.append(
                                {
                                    "file": path.name,
                                    "lang": lang,
                                    "pattern": f"latin_ratio={lr:.2f}",
                                    "key": key[:80],
                                    "tr": tr[:100],
                                }
                            )

    out = ROOT / "data/translation-catalog/human-shard-audit.json"
    out.write_text(json.dumps(issues, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"{len(issues)} issues -> {out}")
    for i in issues[:25]:
        print(f"  [{i['file']}] {i['lang']} {i['pattern']}: {i['key'][:50]}...")


if __name__ == "__main__":
    main()
