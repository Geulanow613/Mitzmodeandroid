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
        # Bottom-nav tab labels: add("Today" to { Icon(...) })
        for m in re.finditer(r'add\("((?:[^"\\]|\\.)*)" to \{', text):
            add(strings, unescape_kotlin(m.group(1)))


_TRIPLE_QUOTED = re.compile(r'"""[\s\S]*?"""')
_SIMPLE_DQ_STRING = re.compile(r'"((?:[^"\\\n]|\\.)*)"')
_HEX_COLOR = re.compile(r'^#?[0-9A-Fa-f]{6,8}$')
_FORMAT_SPEC_ONLY = re.compile(r'^[%\d\s:./,\-+()\[\]]*[dsf%][%\d\s:./,\-+()\[\]]*$')
_SNAKE_CASE_ID = re.compile(r'^[a-z][a-z0-9_]*$')
_IMPORT_LIKE = re.compile(r'^[a-zA-Z_][\w.]*$')
_NON_TEXT_LINE_PREFIXES = (
    "import ", "package ", "//", "@file:",
)


def from_kotlin_ui_all_literals(strings: set[str]) -> None:
    """Catch-all pass: every plain double-quoted string literal anywhere under ui/**/*.kt
    that looks like user-facing text. This exists because AppText/GoldButton/etc. are often
    wrapped by intermediate composables (SettingLabel(label, description), ProfileToggle(label=...),
    SettingsChip(label = ...), etc.) whose call sites use parameter names or positions the
    targeted regexes above don't anticipate. Rather than keep whack-a-moling new parameter
    names, just harvest every literal and filter out the obvious non-text ones."""
    ui = SHARED / "ui"
    if not ui.exists():
        return
    for kt in ui.rglob("*.kt"):
        text = kt.read_text(encoding="utf-8")
        text_no_triple = _TRIPLE_QUOTED.sub(" ", text)
        for line in text_no_triple.splitlines():
            stripped = line.strip()
            if stripped.startswith(_NON_TEXT_LINE_PREFIXES):
                continue
            for m in _SIMPLE_DQ_STRING.finditer(line):
                block = unescape_kotlin(m.group(1)).strip()
                if len(block) < 2 or len(block) > 500:
                    continue
                if not any(c.isalpha() for c in block):
                    continue
                if block.startswith(("http://", "https://", "content://", "package:")):
                    continue
                if _HEX_COLOR.match(block):
                    continue
                if _FORMAT_SPEC_ONLY.match(block):
                    continue
                if "${" in block:
                    continue
                # Bare identifiers / testTags / enum-ish tokens: snake_case or single
                # CamelCase word with no spaces and no punctuation typical of prose.
                if " " not in block and _SNAKE_CASE_ID.match(block) and "_" in block:
                    continue
                if _IMPORT_LIKE.match(block) and block[0].islower() and "." in block:
                    continue
                add(strings, block)


_APPTEXT_CALL = re.compile(r"\bAppText\(")
_APPTEXT_ARG_BOUNDARY = re.compile(
    r',\s*\n?\s*(style|color|modifier|fontWeight|textAlign|enableTerms|maxLines|overflow|'
    r'fontSize|fontFamily|fontStyle|letterSpacing|lineHeight|softWrap|onTextLayout|textDecoration)\s*='
)
_DQ_STRING = re.compile(r'"((?:[^"\\]|\\.)*)"')


def from_apptext_conditional_args(strings: set[str]) -> None:
    """Catch string literals inside `AppText(if (...) "a" else "b", style = ...)` and
    `AppText(when (x) { A -> "a"; B -> "b" }, style = ...)`. The simple patterns in
    from_kotlin_ui only match a literal immediately after `AppText(`, so any text wrapped
    in a conditional expression was silently never extracted."""
    ui = SHARED / "ui"
    if not ui.exists():
        return
    for kt in ui.rglob("*.kt"):
        text = kt.read_text(encoding="utf-8")
        for m in _APPTEXT_CALL.finditer(text):
            start = m.end()
            window = text[start:start + 2000]
            if window.lstrip().startswith('"'):
                continue  # plain literal, already handled by from_kotlin_ui
            boundary = _APPTEXT_ARG_BOUNDARY.search(window)
            if not boundary:
                continue
            span = window[: boundary.start()]
            if "if (" not in span and "when (" not in span and "when(" not in span:
                continue
            for sm in _DQ_STRING.finditer(span):
                block = unescape_kotlin(sm.group(1)).strip()
                if len(block) < 2 or "${" in block:
                    continue
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


ZMAN_HOLIDAY_DOMAIN_FILES = [
    "ChecklistZmanEvaluator.kt",
    "UpcomingHolidayPlanner.kt",
    "UpcomingHolidayTiming.kt",
    "ZmanPeriodLabels.kt",
    "BirkatHaIlanotRules.kt",
]

# Fields that hold user-facing text (as opposed to internal ids, map keys like "label"/"time",
# or KDoc example strings). Mirrors tools/_audit_zman_hints.py plus a few extra field names
# found in these files (label=, name=, hint local vals, etc).
_ZMAN_FIELD_PATTERNS = [
    re.compile(r'\bhint\s*=\s*"((?:[^"\\]|\\.)*)"'),
    re.compile(r'\bmakeupNote\s*=\s*"((?:[^"\\]|\\.)*)"'),
    re.compile(r'\bactiveHint\s*=\s*"((?:[^"\\]|\\.)*)"'),
    re.compile(r'\bupcomingTemplate\s*=\s*"((?:[^"\\]|\\.)*)"'),
    re.compile(r'\bexpiredTemplate\s*=\s*"((?:[^"\\]|\\.)*)"'),
    re.compile(r'\bactiveHintTemplate\s*=\s*"((?:[^"\\]|\\.)*)"'),
    re.compile(r'\bmakeupTemplate\s*=\s*"((?:[^"\\]|\\.)*)"'),
    re.compile(r'\bhintTemplate\s*=\s*"((?:[^"\\]|\\.)*)"'),
    re.compile(r'\bupcoming\s*=\s*"((?:[^"\\]|\\.)*)"'),
    re.compile(r'\bexpired\s*=\s*"((?:[^"\\]|\\.)*)"'),
    re.compile(r'\bmakeup\s*=\s*"((?:[^"\\]|\\.)*)"'),
    re.compile(r'\blabel\s*=\s*"((?:[^"\\]|\\.)*)"'),
    re.compile(r'\bname\s*=\s*"((?:[^"\\]|\\.)*)"'),
    # bare literal branches: `val lateTemplate = if (...) "..." else null`
    re.compile(r'"((?:[^"\\]|\\.){15,})"\s*\n\s*\}?\s*else\s*(?:null|"")'),
    # bare literal branches inside `when { cond -> "..." ... }`
    re.compile(r'->\s*\n?\s*"((?:[^"\\]|\\.){15,})"\s*\n\s*\w+ !?=|->\s*\n?\s*"((?:[^"\\]|\\.){15,})"\s*\n\s*else ->'),
    # tuple form: `"..." to mapOf(` and `"Name" to "hint text"`
    re.compile(r'"((?:[^"\\]|\\.){4,})"\s*\n?\s*to\s*\n?\s*mapOf\('),
    re.compile(r'->\s*\n?\s*"((?:[^"\\]|\\.){4,})"\s*to\s*"((?:[^"\\]|\\.){4,})"'),
]


def from_zman_holiday_kotlin(strings: set[str]) -> None:
    domain = SHARED / "domain"
    for name in ZMAN_HOLIDAY_DOMAIN_FILES:
        path = domain / name
        if not path.exists():
            continue
        content = read(path)
        # Strip comments so KDoc example strings (e.g. "Candles 7:35pm Fri") aren't extracted.
        no_comments = re.sub(r"/\*[\s\S]*?\*/", "", content)
        no_comments = re.sub(r"//[^\n]*", "", no_comments)
        for pat in _ZMAN_FIELD_PATTERNS:
            for m in pat.finditer(no_comments):
                for group in m.groups():
                    if not group:
                        continue
                    block = unescape_kotlin(group).strip()
                    if len(block) < 2 or "${" in block:
                        continue
                    add(strings, block)


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
    # Melachot list uses positional GuideTopic("id", "Title", "hebrew", "body", url) —
    # not named title=/body= params, so it needs its own pattern.
    for m in re.finditer(
        r'GuideTopic\(\s*"[^"]*",\s*"((?:[^"\\]|\\.)*)",\s*"(?:[^"\\]|\\.)*",\s*\n?\s*"((?:[^"\\]|\\.)*)"',
        content,
    ):
        add(strings, unescape_kotlin(m.group(1)))
        add(strings, unescape_kotlin(m.group(2)))
    for m in re.finditer(r'ComparisonRow\(\s*\n?\s*"((?:[^"\\]|\\.)*)",\s*\n?\s*"((?:[^"\\]|\\.)*)",\s*\n?\s*"((?:[^"\\]|\\.)*)"', content):
        add(strings, unescape_kotlin(m.group(1)))
        add(strings, unescape_kotlin(m.group(2)))
        add(strings, unescape_kotlin(m.group(3)))


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
    from_apptext_conditional_args(strings)
    from_kotlin_ui_all_literals(strings)
    from_domain_kotlin(strings)
    from_zman_holiday_kotlin(strings)
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
