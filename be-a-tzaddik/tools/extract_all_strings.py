#!/usr/bin/env python3
"""Extract every user-visible English string for bundled offline translation."""

from __future__ import annotations

import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SHARED = ROOT / "shared" / "src" / "commonMain" / "kotlin" / "com" / "beardytop" / "beatzaddik"
OUT = ROOT / "data" / "translation-catalog" / "strings.json"
MITZMODE = ROOT.parent / "app" / "src" / "main" / "java" / "com" / "beardytop" / "mitzmode"

# Reuse export_app_reference_text patterns
sys.path.insert(0, str(ROOT / "tools"))
from export_app_reference_text import (  # noqa: E402
    EXPLANATION_FIELD,
    KOTLIN_STRING,
    LINE_ENTRY,
    SEASONAL_KOTLIN_FILES,
    SHORT_DEF_MAX,
    extract_kotlin_list_blocks,
    extract_line_entries_from_block,
    extract_plus_strings,
    merge_term_maps,
    parse_term_line,
    read,
    unescape_kotlin,
)


def add(strings: set[str], value: str | None) -> None:
    if not value or not isinstance(value, str):
        return
    v = value.strip()
    if len(v) < 2:
        return
    if not any(c.isalpha() for c in v):
        return
    if "${" in v and "formatRestTime" in v:
        return
    if "${\n" in v or "IsraelDetectionSource" in v:
        return
    if v.startswith("at_least_one_prayer"):
        return
    strings.add(v)


def from_checklist(strings: set[str]) -> None:
    for name in ("checklist-items.json", "nusach-extras.json", "holidays-overlay.json"):
        path = ROOT / "data" / name
        if not path.exists():
            path = SHARED / "composeResources" / "files" / name
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
            for link in item.get("links", []) or []:
                if isinstance(link, dict):
                    add(strings, link.get("displayText"))


def from_kotlin_ui(strings: set[str]) -> None:
    ui = SHARED / "ui"
    if not ui.exists():
        return
    patterns = [
        re.compile(r'AppText\(\s*"((?:[^"\\]|\\.)*)"'),
        re.compile(r'label = \{ AppText\("((?:[^"\\]|\\.)*)"'),
        re.compile(r'GoldButton\([^)]*text = "((?:[^"\\]|\\.)*)"'),
        re.compile(r'ParchmentTextButton\([^)]*text = "((?:[^"\\]|\\.)*)"'),
        re.compile(r'title = "((?:[^"\\]|\\.)*)"'),
        re.compile(r'subtitle = "((?:[^"\\]|\\.)*)"'),
    ]
    for kt in ui.rglob("*.kt"):
        text = kt.read_text(encoding="utf-8")
        for pat in patterns:
            for m in pat.finditer(text):
                add(strings, unescape_kotlin(m.group(1)))
        for m in re.finditer(r'AppText\(\s*\n\s*"((?:[^"\\]|\\.)*)"', text):
            add(strings, unescape_kotlin(m.group(1)))
        for m in KOTLIN_STRING.finditer(text):
            block = unescape_kotlin(m.group(1))
            if 2 < len(block) < 8000 and any(c.isalpha() for c in block):
                add(strings, block)


def from_domain_kotlin(strings: set[str]) -> None:
    domain = SHARED / "domain"
    for name in SEASONAL_KOTLIN_FILES + [
        "AppDisclaimer.kt",
        "RestMessages.kt",
        "HalachicTermsDictionary.kt",
        "BeginnerHalachaGlossary.kt",
        "SeasonalChecklistItems.kt",
        "ChecklistGenderRules.kt",
    ]:
        path = domain / name
        if not path.exists():
            continue
        content = read(path)
        for m in EXPLANATION_FIELD.finditer(content):
            add(strings, m.group(2).strip())
        for m in KOTLIN_STRING.finditer(content):
            block = unescape_kotlin(m.group(1)).strip()
            if len(block) > 2:
                add(strings, block)
        for anchor in (
            "const val STARTUP_BODY",
            "fun shabbatMessage(): String",
            "fun shabbatApproachingMessage(): String",
            "fun yomTovMessage(holidayName: String): String",
            "fun yomTovApproachingMessage(holidayName: String): String",
        ):
            body = extract_plus_strings(content, anchor)
            if body:
                add(strings, body)
        for m in re.finditer(r'const val \w+ = "((?:[^"\\]|\\.)*)"', content):
            add(strings, unescape_kotlin(m.group(1)))


def from_glossary(strings: set[str]) -> None:
    path = SHARED / "domain" / "HalachicTermsDictionary.kt"
    if not path.exists():
        return
    content = read(path)
    blocks = extract_kotlin_list_blocks(content)
    merged = merge_term_maps(*(extract_line_entries_from_block(b) for b in blocks.values()))
    for title, body, full in merged.values():
        add(strings, title)
        add(strings, body)
        add(strings, full)
        if len(body) <= SHORT_DEF_MAX:
            add(strings, f"{title} — {body}")


def from_shabbat_guide(strings: set[str]) -> None:
    path = SHARED / "ui" / "screens" / "ShabbatGuideData.kt"
    if not path.exists():
        return
    content = read(path)
    for m in KOTLIN_STRING.finditer(content):
        add(strings, unescape_kotlin(m.group(1)))
    for m in re.finditer(r'title\s*=\s*"((?:[^"\\]|\\.)*)"', content):
        add(strings, unescape_kotlin(m.group(1)))
    for m in re.finditer(r'body\s*=\s*"((?:[^"\\]|\\.)*)"', content):
        add(strings, unescape_kotlin(m.group(1)))


def from_mitzmode(strings: set[str]) -> None:
    if not MITZMODE.exists():
        return
    patterns = [
        re.compile(r'TranslatableText\(\s*\n?\s*text = "((?:[^"\\]|\\.)*)"'),
        re.compile(r'TranslatableText\(\s*"((?:[^"\\]|\\.)*)"'),
        re.compile(r'ParchmentDialog\([^)]*title = "((?:[^"\\]|\\.)*)"'),
        re.compile(r'GoldButton\([^)]*text = "((?:[^"\\]|\\.)*)"'),
        re.compile(r'text = "((?:[^"\\]|\\.)*)"'),
    ]
    for kt in MITZMODE.rglob("*.kt"):
        text = kt.read_text(encoding="utf-8")
        for pat in patterns:
            for m in pat.finditer(text):
                s = unescape_kotlin(m.group(1))
                if len(s) < 500 or "mitzvah" in kt.name.lower() or "dialog" in kt.name.lower():
                    add(strings, s)
        for m in KOTLIN_STRING.finditer(text):
            block = unescape_kotlin(m.group(1))
            if 10 < len(block) < 5000:
                add(strings, block)


def from_mitzvot_cloud(strings: set[str]) -> None:
    path = ROOT / "data" / "mitzvotcloud.json"
    if not path.exists():
        return
    data = json.loads(path.read_text(encoding="utf-8"))
    for item in data.get("mitzvot", []):
        add(strings, item.get("text"))


def from_local_mitzvot(strings: set[str]) -> None:
    candidates = [
        ROOT.parent / "mitzmode" / "data" / "mitzvot.json",
        MITZMODE.parent / "assets" / "mitzvot.json",
    ]
    for path in candidates:
        if not path.exists():
            continue
        data = json.loads(path.read_text(encoding="utf-8"))
        for item in data.get("mitzvot", []):
            add(strings, item.get("text"))


def main() -> None:
    strings: set[str] = set()
    from_checklist(strings)
    from_kotlin_ui(strings)
    from_domain_kotlin(strings)
    from_glossary(strings)
    from_shabbat_guide(strings)
    from_mitzmode(strings)
    from_mitzvot_cloud(strings)
    from_local_mitzvot(strings)

    ordered = sorted(strings, key=lambda s: (len(s), s))
    OUT.parent.mkdir(parents=True, exist_ok=True)
    OUT.write_text(
        json.dumps({"version": 2, "count": len(ordered), "strings": ordered}, ensure_ascii=False, indent=2),
        encoding="utf-8",
    )
    short = sum(1 for s in ordered if len(s) < 120)
    med = sum(1 for s in ordered if 120 <= len(s) < 1000)
    long = sum(1 for s in ordered if len(s) >= 1000)
    print(f"Wrote {len(ordered)} strings to {OUT}")
    print(f"short={short} med={med} long={long} total_chars={sum(len(s) for s in ordered)}")


if __name__ == "__main__":
    main()
