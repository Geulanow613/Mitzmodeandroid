#!/usr/bin/env python3
"""Extract unique English user-visible strings for bundled translation."""

from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SHARED = ROOT / "shared" / "src" / "commonMain" / "kotlin" / "com" / "beardytop" / "beatzaddik"
OUT = ROOT / "data" / "translation-strings.json"
MITZMODE = ROOT.parent / "app" / "src" / "main" / "java" / "com" / "beardytop" / "mitzmode"


def unescape_kotlin(s: str) -> str:
    return s.replace("\\n", "\n").replace('\\"', '"').replace("\\\\", "\\")


def add(strings: set[str], value: str | None) -> None:
    if not value or not isinstance(value, str):
        return
    v = value.strip()
    if len(v) < 2:
        return
    if not any(c.isalpha() for c in v):
        return
    if "${" in v or "if (" in v or "IsraelDetectionSource" in v:
        return
    strings.add(v)


def from_checklist(strings: set[str]) -> None:
    for name in ("checklist-items.json", "nusach-extras.json"):
        path = ROOT / "data" / name
        if not path.exists():
            continue
        data = json.loads(path.read_text(encoding="utf-8"))
        for item in data.get("items", []):
            add(strings, item.get("title"))
            add(strings, item.get("section"))
            add(strings, item.get("explanation"))
            for key, val in item.items():
                if key.startswith("explanation") and isinstance(val, str):
                    add(strings, val)


def from_kotlin_ui(strings: set[str]) -> None:
    ui_dir = SHARED / "ui"
    if not ui_dir.exists():
        return
    # Skip Kotlin code fragments accidentally captured from UI source.
    patterns = [
        re.compile(r'AppText\(\s*"((?:[^"\\]|\\.)*)"'),
        re.compile(r'label = \{ AppText\("((?:[^"\\]|\\.)*)"'),
        re.compile(r'GoldButton\([^)]*text = "((?:[^"\\]|\\.)*)"'),
        re.compile(r'ParchmentTextButton\([^)]*text = "((?:[^"\\]|\\.)*)"'),
    ]
    for kt in ui_dir.rglob("*.kt"):
        text = kt.read_text(encoding="utf-8")
        for pat in patterns:
            for m in pat.finditer(text):
                add(strings, unescape_kotlin(m.group(1)))
        for m in re.finditer(r'AppText\(\s*\n\s*"((?:[^"\\]|\\.)*)"', text):
            add(strings, unescape_kotlin(m.group(1)))


def from_domain(strings: set[str]) -> None:
    domain = SHARED / "domain"
    for kt in domain.glob("*.kt"):
        text = kt.read_text(encoding="utf-8")
        for m in re.finditer(r'const val \w+ = "((?:[^"\\]|\\.)*)"', text):
            add(strings, unescape_kotlin(m.group(1)))


def from_mitzmode(strings: set[str]) -> None:
    if not MITZMODE.exists():
        return
    pat = re.compile(r'TranslatableText\(\s*\n?\s*text = "((?:[^"\\]|\\.)*)"')
    for kt in MITZMODE.rglob("*.kt"):
        text = kt.read_text(encoding="utf-8")
        for m in pat.finditer(text):
            add(strings, unescape_kotlin(m.group(1)))


def main() -> None:
    strings: set[str] = set()
    from_checklist(strings)
    from_kotlin_ui(strings)
    from_domain(strings)
    from_mitzmode(strings)
    ordered = sorted(strings, key=lambda s: (len(s), s))
    OUT.parent.mkdir(parents=True, exist_ok=True)
    OUT.write_text(
        json.dumps({"version": 1, "count": len(ordered), "strings": ordered}, ensure_ascii=False, indent=2),
        encoding="utf-8",
    )
    short = sum(1 for s in ordered if len(s) < 120)
    med = sum(1 for s in ordered if 120 <= len(s) < 1000)
    long = sum(1 for s in ordered if len(s) >= 1000)
    print(f"Wrote {len(ordered)} strings to {OUT}")
    print(f"short={short} med={med} long={long} total_chars={sum(len(s) for s in ordered)}")


if __name__ == "__main__":
    main()
