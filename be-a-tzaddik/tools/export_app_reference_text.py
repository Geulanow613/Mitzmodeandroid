#!/usr/bin/env python3
"""Export all user-visible app copy into one plain-text review file."""

from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SHARED = ROOT / "shared" / "src" / "commonMain" / "kotlin" / "com" / "beardytop" / "beatzaddik"
OUT = ROOT / "docs" / "APP_REFERENCE_TEXT_FOR_REVIEW.md"
SEASONAL_OUT = ROOT / "docs" / "SEASONAL_AND_HIDDEN_CHECKLIST_TEXT.md"
INFO_OUT = ROOT / "infotosearchthrough.md"
DOMAIN = SHARED / "domain"
EXTRA_SEASONAL_KOTLIN_FILES = [
    "ExplainerTemplateSupport.kt",
    "ErevPesachExplainerTemplates.kt",
    "TishaBeavTefillinRules.kt",
    "BirkatHachamahRules.kt",
    "JerusalemPurimRules.kt",
    "ZmanPeriodLabels.kt",
]

EXPLANATION_FIELD = re.compile(
    r"(explanation(?:Ashkenaz|Sefard|EdotHamizrach|Chabad|Female)?)\s*=\s*"
    r'(?:BeginnerHalachaGlossary\.withKeyTerms\(\s*[^,]+,\s*)?"""([\s\S]*?)"""',
)
SEASONAL_KOTLIN_FILES = [
    "SeasonalMitzvahText.kt",
    "ErevChagPrepText.kt",
    "ErevPesachPrepText.kt",
    "PublicFastDayText.kt",
    "YomTovShabbatPrepText.kt",
    "PurimMeshulashText.kt",
    "PurimBrachotText.kt",
    "OmerCountText.kt",
]
EXPLANATION_VARIANT_LABELS = {
    "explanation": "Default",
    "explanationAshkenaz": "Ashkenaz",
    "explanationSefard": "Sefard",
    "explanationEdotHamizrach": "Edot HaMizrach",
    "explanationChabad": "Chabad",
    "explanationFemale": "Female",
}

KOTLIN_STRING = re.compile(r'"""([\s\S]*?)"""', re.MULTILINE)
CONST_VAL = re.compile(
    r'const val \w+\s*=\s*\n?\s*"((?:[^"\\]|\\.)*)"',
    re.MULTILINE,
)
CONST_VAL_CONT = re.compile(
    r'const val \w+\s*=\s*\n?\s*"((?:[^"\\]|\\.)*)"\s*\+\s*\n\s*"((?:[^"\\]|\\.)*)"',
    re.MULTILINE,
)
LINE_ENTRY = re.compile(
    r'line\s*\(\s*\n?\s*"((?:[^"\\]|\\.)*)"',
    re.MULTILINE,
)
KOTLIN_LIST_BLOCK = re.compile(
    r"private val (\w+).*?=\s*listOf\((.*?)\)\.mapNotNull",
    re.DOTALL,
)
SHORT_DEF_MAX = 220  # chars — supplemental-style one-liners in enrichedOverrides


def parse_term_line(raw: str) -> tuple[str, str, str] | None:
    line = unescape_kotlin(raw.strip())
    if " — " not in line:
        return None
    title, _, body = line.partition(" — ")
    title = title.strip().removeprefix("•").strip()
    return title, body.strip(), line


def term_id(title: str) -> str:
    return re.sub(r"[^a-z0-9']+", "_", title.lower()).strip("_")


def merge_term_maps(*maps: dict[str, tuple[str, str, str]]) -> dict[str, tuple[str, str, str]]:
    """Merge like HalachicTermsDictionary before enrichedOverrides — longer definition wins."""
    merged: dict[str, tuple[str, str, str]] = {}
    for term_map in maps:
        for key, (title, body, full) in term_map.items():
            existing = merged.get(key)
            if existing is None or len(body) > len(existing[1]):
                merged[key] = (title, body, full)
    return merged


def extract_line_entries_from_block(block: str) -> dict[str, tuple[str, str, str]]:
    terms: dict[str, tuple[str, str, str]] = {}
    for raw in LINE_ENTRY.findall(block):
        parsed = parse_term_line(raw)
        if parsed:
            title, body, full = parsed
            key = term_id(title)
            existing = terms.get(key)
            if existing is None or len(body) > len(existing[1]):
                terms[key] = (title, body, full)
    return terms


def extract_kotlin_list_blocks(content: str) -> dict[str, str]:
    blocks: dict[str, str] = {}
    for name, body in KOTLIN_LIST_BLOCK.findall(content):
        blocks[name] = body
    return blocks


def extract_beginner_terms(beginner: str) -> dict[str, tuple[str, str, str]]:
    terms: dict[str, tuple[str, str, str]] = {}
    for m in CONST_VAL.finditer(beginner):
        parsed = parse_term_line(m.group(1))
        if parsed:
            title, body, full = parsed
            terms[term_id(title)] = (title, body, full)
    for m in re.finditer(r'"([^"\\]|\\.)* — ([^"\\]|\\.)*"', beginner):
        parsed = parse_term_line(m.group(0).strip('"'))
        if parsed:
            title, body, full = parsed
            terms.setdefault(term_id(title), (title, body, full))
    return terms


def extract_checklist_key_terms() -> dict[str, tuple[str, str, str]]:
    """Key terms bullets from daily mitzvah explainers (short inline defs)."""
    checklist = ROOT / "shared" / "src" / "commonMain" / "composeResources" / "files" / "checklist-items.json"
    if not checklist.exists():
        return {}
    data = json.loads(checklist.read_text(encoding="utf-8"))
    terms: dict[str, tuple[str, str, str]] = {}
    for item in data.get("items", []):
        for field in ("explanation", "explanationAshkenaz", "explanationSefard", "explanationChabad"):
            text = item.get(field) or ""
            idx = text.find("Key terms:")
            if idx < 0:
                continue
            block = text[idx + len("Key terms:") :]
            for line in block.split("\n"):
                line = line.strip()
                if not line.startswith("•"):
                    if line and not line.startswith("•"):
                        break
                    continue
                parsed = parse_term_line(line.removeprefix("•").strip())
                if parsed:
                    title, body, full = parsed
                    key = term_id(title)
                    existing = terms.get(key)
                    if existing is None or len(body) > len(existing[1]):
                        terms[key] = (title, body, full)
    return terms


def write_term_list(f, terms: dict[str, tuple[str, str, str]], bullet: bool = True) -> None:
    for key in sorted(terms.keys(), key=lambda k: terms[k][0].lower()):
        title, body, _ = terms[key]
        if bullet:
            f.write(f"- **{title}** — {body}\n")
        else:
            f.write(f"### {title}\n\n{body}\n\n")


def build_full_glossary(dict_file: str, beginner_terms: dict) -> dict[str, tuple[str, str, str]]:
    """Simulate HalachicTermsDictionary.allTerms merge (enriched overrides win)."""
    blocks = extract_kotlin_list_blocks(dict_file)
    base = merge_term_maps(
        beginner_terms,
        extract_line_entries_from_block(blocks.get("supplemental", "")),
    )
    enriched = extract_line_entries_from_block(blocks.get("enrichedOverrides", ""))
    full = dict(base)
    for key, entry in enriched.items():
        full[key] = entry  # enriched always wins
    return full
GUIDE_TOPIC = re.compile(
    r'GuideTopic\s*\(\s*(?:id\s*=\s*"[^"]*",\s*)?title\s*=\s*"([^"]*)"(?:,\s*hebrewTitle\s*=\s*"[^"]*")?,?\s*\n?\s*(?://[^\n]*\n\s*)*body\s*=\s*"""([\s\S]*?)"""',
    re.MULTILINE,
)
GUIDE_TOPIC_MELACHA = re.compile(
    r'GuideTopic\s*\(\s*"[^"]*",\s*"([^"]*)",\s*"[^"]*",\s*\n\s*"((?:[^"\\]|\\.)*)"',
    re.MULTILINE,
)
FUN_EXPLANATION = re.compile(
    r'fun\s+(\w+(?:Explanation|Block|Prep|Schedule|Text|Basics)?)\s*\([^)]*\)\s*(?::\s*[^={]+)?\s*=\s*(?:BeginnerHalachaGlossary\.withKeyTerms\([^,]+,\s*)?(?:"""([\s\S]*?)"""|when\s*\([^)]*\)\s*\{([\s\S]*?)\n\s*\})',
    re.MULTILINE,
)
COMPARISON_ROW = re.compile(
    r'ComparisonRow\s*\(\s*\n?\s*"([^"]*)",\s*\n?\s*"([^"]*)",\s*\n?\s*"([^"]*)"\s*\)',
    re.MULTILINE,
)


def read(path: Path) -> str:
    return path.read_text(encoding="utf-8")


def unescape_kotlin(s: str) -> str:
    return s.replace("\\n", "\n").replace("\\t", "\t").replace('\\"', '"').replace("\\'", "'")


def extract_triple_strings(text: str) -> list[str]:
    return [unescape_kotlin(m.group(1).strip()) for m in KOTLIN_STRING.finditer(text)]


def extract_functions_named(content: str, filename: str) -> list[tuple[str, str]]:
    results: list[tuple[str, str]] = []
    # Match fun name(...) = """...""" or buildString blocks
    pattern = re.compile(
        r'fun\s+(\w+)\s*\([^)]*\)\s*(?::\s*[^{=]+)?\s*=\s*(?:BeginnerHalachaGlossary\.withKeyTerms\(\s*(?:BeginnerHalachaGlossary\.\w+\(\)|BeginnerHalachaGlossary\.block\([^)]*\)|[^,]+),\s*)?"""([\s\S]*?)"""',
        re.MULTILINE,
    )
    for m in pattern.finditer(content):
        name, body = m.group(1), unescape_kotlin(m.group(2).strip())
        if body and len(body) > 20:
            results.append((name, body))

    # build() and other multi-part functions — grab inner triple strings with context
    build_pattern = re.compile(
        r'fun\s+(\w+)\s*\([^)]*\)[^{]*\{([\s\S]*?)\n    \}',
        re.MULTILINE,
    )
    for m in build_pattern.finditer(content):
        name, block = m.group(1), m.group(2)
        strings = extract_triple_strings(block)
        if strings and name not in {r[0] for r in results}:
            combined = "\n\n".join(strings)
            if len(combined) > 40:
                results.append((name, combined))

    return results


def section(title: str, level: int = 2) -> str:
    return f"\n{'#' * level} {title}\n\n"


def write_glossary(f) -> None:
    f.write(section("Part 1 — Glossary", 1))

    beginner_src = read(SHARED / "domain" / "BeginnerHalachaGlossary.kt")
    dict_src = read(SHARED / "domain" / "HalachicTermsDictionary.kt")
    dict_blocks = extract_kotlin_list_blocks(dict_src)

    beginner_terms = extract_beginner_terms(beginner_src)
    supplemental_terms = extract_line_entries_from_block(dict_blocks.get("supplemental", ""))
    enriched_terms = extract_line_entries_from_block(dict_blocks.get("enrichedOverrides", ""))
    checklist_key_terms = extract_checklist_key_terms()

    short_base = merge_term_maps(beginner_terms, supplemental_terms, checklist_key_terms)
    # Include brief enriched-only entries (no short counterpart in supplemental).
    for key, entry in enriched_terms.items():
        title, body, full = entry
        if len(body) <= SHORT_DEF_MAX and key not in short_base:
            short_base[key] = entry

    full_terms = build_full_glossary(dict_src, beginner_terms)

    f.write(section("Short Definitions", 2))
    f.write(
        "One-line and brief glossary entries — from BeginnerHalachaGlossary key-term blocks, "
        "HalachicTermsDictionary supplemental entries, checklist explainer key-term bullets, "
        "and brief enriched-only terms. "
        "When a term also has a longer definition below, the app may show the longer version "
        "when you tap the gold underline.\n\n"
    )
    write_term_list(f, short_base, bullet=True)
    f.write("\n")

    f.write(section("Full Glossary Definitions (HalachicTermsDictionary — tappable in app)", 2))
    f.write(
        "Complete definitions as merged in the app (enriched entries override shorter ones). "
        "Listed alphabetically.\n\n"
    )
    write_term_list(f, full_terms, bullet=False)


def write_shabbat_guide(f) -> None:
    f.write(section("Part 2 — Shabbat & Yom Tov Guide (in-app Shabbat Guide screen)", 1))
    content = read(SHARED / "ui" / "screens" / "ShabbatGuideData.kt")

    f.write(section("Key Topics", 2))
    for m in GUIDE_TOPIC.finditer(content):
        title, body = m.group(1), unescape_kotlin(m.group(2).strip())
        f.write(f"### {title}\n\n{body}\n\n")

    f.write(section("Shabbat vs Yom Tov Comparison", 2))
    f.write("| Activity | Shabbat | Yom Tov |\n|----------|---------|--------|\n")
    for m in COMPARISON_ROW.finditer(content):
        act, sh, yt = m.group(1), m.group(2), m.group(3)
        f.write(f"| {act} | {sh} | {yt} |\n")
    f.write("\n")

    f.write(section("The 39 Melachot", 2))
    for m in GUIDE_TOPIC_MELACHA.finditer(content):
        title, body = m.group(1), unescape_kotlin(m.group(2).strip())
        f.write(f"### {title}\n\n{body}\n\n")

    f.write(section("Jewish Holidays (Guide summaries)", 2))
    holiday_pattern = re.compile(
        r'GuideTopic\s*\(\s*\n?\s*id\s*=\s*"([^"]*)",\s*\n?\s*title\s*=\s*"([^"]*)"(?:,\s*hebrewTitle\s*=\s*"[^"]*")?,?\s*\n?\s*(?://[^\n]*\n\s*)*body\s*=\s*"""([\s\S]*?)"""',
        re.MULTILINE,
    )
    for m in holiday_pattern.finditer(content):
        _id, title, body = m.group(1), m.group(2), unescape_kotlin(m.group(3).strip())
        f.write(f"### {title}\n\n{body}\n\n")


def write_item_explanations(
    f,
    title: str,
    fields: dict[str, str],
    *,
    meta: str = "",
) -> None:
    f.write(f"### {title}\n\n")
    if meta:
        f.write(f"{meta}\n\n")
    wrote = False
    for field, label in EXPLANATION_VARIANT_LABELS.items():
        text = (fields.get(field) or "").strip()
        if not text:
            continue
        if label != "Default" or wrote:
            f.write(f"**{label}:**\n\n")
        f.write(f"{text}\n\n")
        wrote = True
    if not wrote:
        f.write("*(No static explanation text — may be assembled at runtime.)*\n\n")


def format_item_explanations(title: str, fields: dict[str, str], *, meta: str = "") -> str:
    from io import StringIO

    buf = StringIO()
    write_item_explanations(buf, title, fields, meta=meta)
    return buf.getvalue()


def extract_checklist_item_defs(kotlin_text: str) -> list[dict[str, str | dict[str, str]]]:
    items: list[dict[str, str | dict[str, str]]] = []
    pos = 0
    while True:
        start = kotlin_text.find("ChecklistItemDef(", pos)
        if start < 0:
            break
        depth = 0
        i = start + len("ChecklistItemDef")
        while i < len(kotlin_text):
            ch = kotlin_text[i]
            if ch == "(":
                depth += 1
            elif ch == ")":
                depth -= 1
                if depth == 0:
                    break
            i += 1
        block = kotlin_text[start + len("ChecklistItemDef(") : i]
        pos = i + 1

        item_id = ""
        title = ""
        section = ""
        id_m = re.search(r'id\s*=\s*"([^"]*)"', block)
        title_m = re.search(r'title\s*=\s*"([^"]*)"', block)
        section_m = re.search(r'section\s*=\s*"([^"]*)"', block)
        if id_m:
            item_id = id_m.group(1)
        if title_m:
            title = title_m.group(1)
        if section_m:
            section = section_m.group(1)
        if not title and not item_id:
            continue

        fields: dict[str, str] = {}
        for em in EXPLANATION_FIELD.finditer(block):
            fields[em.group(1)] = unescape_kotlin(em.group(2).strip())

        items.append(
            {
                "id": item_id,
                "title": title or item_id,
                "section": section,
                "fields": fields,
            }
        )
    return items


def is_exportable_kotlin_function(name: str) -> bool:
    if name.endswith("Links") or name.endswith("Link"):
        return False
    if name in {
        "build",
        "buildTitle",
        "headerLabel",
        "statusChipLabel",
        "weeksAndDays",
        "omerDaySummary",
        "omerCountSpeechPhrase",
        "displayName",
    }:
        return False
    keywords = (
        "Explanation",
        "Block",
        "Prep",
        "Text",
        "Message",
        "Basics",
        "Schedule",
        "intro",
        "Body",
        "Note",
    )
    return any(k in name for k in keywords)


def extract_holy_day_notices() -> list[tuple[str, str, str, str]]:
    holy = read(DOMAIN / "HolyDayPhoneRules.kt")
    rest = read(DOMAIN / "RestMessages.kt")
    notices: list[tuple[str, str, str, str]] = []

    sh_block = holy[holy.find("private fun shabbatNotice") : holy.find("private fun yomTovNotice")]
    sh_title = re.search(r'title = "([^"]*)"', sh_block)
    sh_footer = re.search(r'footer = "([^"]*)"', sh_block)
    sh_msg = extract_plus_strings(sh_block, "message = ")
    if sh_title and sh_footer and sh_msg:
        notices.append(("Shabbat Shalom", sh_title.group(1), sh_msg, sh_footer.group(1)))

    yt_block = holy[holy.find("private fun yomTovNotice") :]
    yt_footer = re.search(r'footer = "([^"]*)"', yt_block)
    yt_msg_base = extract_plus_strings(rest, "fun yomTovMessage(holidayName: String): String").replace(
        "$holidayName", "[holiday name]"
    )
    yt_suffix = " This app is for weekdays and erev chag preparation, not for use on Yom Tov."
    if yt_footer and yt_msg_base:
        notices.append(
            (
                "[Holiday name] (Yom Tov)",
                "[Holiday name]",
                yt_msg_base + yt_suffix,
                yt_footer.group(1),
            )
        )
    return notices


def extract_erev_chag_triples(content: str) -> list[tuple[str, str]]:
    results: list[tuple[str, str]] = []
    pattern = re.compile(
        r'Triple\s*\(\s*\n?\s*"([^"]*)",\s*\n?\s*BeginnerHalachaGlossary\.withKeyTerms\(\s*[^,]+,\s*"""([\s\S]*?)"""',
        re.MULTILINE,
    )
    for m in pattern.finditer(content):
        results.append((m.group(1), unescape_kotlin(m.group(2).strip())))
    return results


def collect_seasonal_content() -> list[str]:
    """Build markdown sections for seasonal/hidden checklist copy."""
    parts: list[str] = []

    parts.append(
        "# User-visible copy — seasonal & hidden checklist\n\n"
        "Titles and explanation text as shown in the app. Dynamic parts "
        "(Omer day number, sunset/tzeit times, which Yom Tov–Shabbat blocks appear, "
        "holiday name, parsha name) depend on your calendar, location, and profile.\n\n"
        "*Auto-generated from Kotlin sources by `tools/export_app_reference_text.py`.*\n"
    )

    parts.append(section("When the checklist is hidden", 2).rstrip())
    for card_title, title, message, footer in extract_holy_day_notices():
        parts.append(f"### {card_title}\n\n**Title:** {title}\n\n{message}\n\n{footer}\n")

    parts.append(section("Seasonal checklist items (SeasonalChecklistItems.kt)", 2).rstrip())
    seasonal_items = extract_checklist_item_defs(read(DOMAIN / "SeasonalChecklistItems.kt"))
    seasonal_items.sort(key=lambda i: (str(i.get("section", "")), str(i.get("title", ""))))
    for item in seasonal_items:
        meta = ""
        if item.get("section"):
            meta += f"*Section: {item['section']}*"
        if item.get("id"):
            meta += f" · ID: `{item['id']}`"
        if meta:
            meta += "\n"
        parts.append(format_item_explanations(str(item["title"]), item["fields"], meta=meta))

    parts.append(section("Erev Yom Tov holiday-specific blocks (ErevChagPrepText.kt)", 2).rstrip())
    erev = read(DOMAIN / "ErevChagPrepText.kt")
    for title, body in extract_erev_chag_triples(erev):
        parts.append(f"### {title}\n\n{body}\n\n")
    for name, body in extract_functions_named(erev, "ErevChagPrepText.kt"):
        if is_exportable_kotlin_function(name) and body:
            parts.append(f"### {name}\n\n{body}\n\n")

    parts.append(section("Seasonal text modules (Kotlin)", 2).rstrip())
    for filename in SEASONAL_KOTLIN_FILES:
        path = DOMAIN / filename
        if not path.exists() or filename == "ErevChagPrepText.kt":
            continue
        content = read(path)
        fns = [(n, b) for n, b in extract_functions_named(content, filename) if is_exportable_kotlin_function(n)]
        if not fns:
            continue
        parts.append(section(filename, 3).rstrip())
        for name, body in sorted(fns, key=lambda x: x[0]):
            parts.append(f"#### {name}\n\n{body}\n\n")

    return parts


def write_seasonal_from_kotlin(f) -> None:
    sections = collect_seasonal_content()
    seasonal_md = "\n".join(sections).strip() + "\n"
    SEASONAL_OUT.parent.mkdir(parents=True, exist_ok=True)
    SEASONAL_OUT.write_text(seasonal_md, encoding="utf-8")

    f.write(section("Part 3 — Seasonal, Hidden & Festival Checklist Explainers", 1))
    f.write(
        "All seasonal and situational checklist copy: hidden Shabbat/Yom Tov screens, "
        "Erev Shabbat prep (see Part 4), Musaf, Omer, holidays, Pesach prep, mourning periods, etc. "
        "Extracted live from Kotlin sources.\n\n"
    )
    # Skip the duplicate H1 from seasonal_md
    body = seasonal_md
    if body.startswith("# "):
        body = body.split("\n", 1)[1].lstrip()
    f.write(body)
    f.write("\n")


def write_seasonal_doc(f) -> None:
    write_seasonal_from_kotlin(f)


def write_daily_checklist(f) -> None:
    f.write(section("Part 4 — Daily Mitzvot Checklist Explainers", 1))
    f.write(
        "Full explanation text for every item in the daily checklist (`checklist-items.json`). "
        "Includes default text plus Ashkenaz, Sefard, Edot HaMizrach, Chabad, and female variants where present. "
        "Shnayim Mikra prepends \"This week's parsha: Parshat [name]\" at runtime.\n\n"
    )
    checklist = ROOT / "shared" / "src" / "commonMain" / "composeResources" / "files" / "checklist-items.json"
    if not checklist.exists():
        f.write("*Checklist file not found.*\n\n")
        return
    data = json.loads(checklist.read_text(encoding="utf-8"))
    items = sorted(data.get("items", []), key=lambda i: (i.get("section", ""), i.get("sortOrder", 0)))
    for item in items:
        title = item.get("title", item.get("id", "Unknown"))
        item_id = item.get("id", "")
        section_name = item.get("section", "")
        f.write(f"### {title}\n\n")
        if section_name:
            f.write(f"*Section: {section_name} · ID: `{item_id}`*\n\n")
        variants = [
            ("Default", "explanation"),
            ("Ashkenaz", "explanationAshkenaz"),
            ("Sefard", "explanationSefard"),
            ("Edot HaMizrach", "explanationEdotHamizrach"),
            ("Chabad", "explanationChabad"),
            ("Female", "explanationFemale"),
        ]
        wrote = False
        for label, field in variants:
            text = (item.get(field) or "").strip()
            if not text:
                continue
            if label != "Default" or wrote:
                f.write(f"**{label}:**\n\n")
            f.write(f"{text}\n\n")
            wrote = True
        if not wrote:
            f.write("*(No explanation text.)*\n\n")


def write_omer_template(f) -> None:
    f.write(section("Omer Count Text Template (OmerCountText.kt)", 2))
    omer_src = read(DOMAIN / "OmerCountText.kt")
    for name, body in extract_functions_named(omer_src, "OmerCountText.kt"):
        if name == "buildExplanation":
            f.write(
                "**buildExplanation** (runtime fills day number, weekday names, local tzeit, nusach):\n\n"
            )
            f.write(f"{body}\n\n")
            return
    f.write("*(Could not extract OmerCountText.buildExplanation.)*\n\n")


def extract_welcome_intros(disclaimer: str) -> tuple[str, str]:
    embedded = re.search(
        r'if \(embeddedMode\) \{\s*\n\s*"((?:[^"\\]|\\.)*)"',
        disclaimer,
    )
    standalone = re.search(
        r'\} else \{\s*\n\s*"((?:[^"\\]|\\.)*)"',
        disclaimer,
    )
    return (
        unescape_kotlin(embedded.group(1)) if embedded else "",
        unescape_kotlin(standalone.group(1)) if standalone else "",
    )


def extract_plus_strings(text: str, anchor: str) -> str:
    """Extract a Kotlin block of "..." + "..." concatenation after anchor."""
    pos = text.find(anchor)
    if pos < 0:
        return ""
    pos = text.find("=", pos)
    if pos < 0:
        return ""
    rest = text[pos + 1 :]
    parts: list[str] = []
    while True:
        sm = re.match(r'\s*"((?:[^"\\]|\\.)*)"', rest)
        if not sm:
            break
        parts.append(unescape_kotlin(sm.group(1)))
        rest = rest[sm.end() :]
        if not re.match(r"\s*\+", rest):
            break
        rest = re.sub(r"^\s*\+\s*", "", rest, count=1)
    return "".join(parts)


def write_static_blocks(f) -> None:
    f.write(section("Part 5 — App UI Copy (Disclaimer, About, Rest Screens)", 1))

    disclaimer = read(SHARED / "domain" / "AppDisclaimer.kt")
    headline = re.search(r'const val WELCOME_HEADLINE = "([^"]*)"', disclaimer)
    if headline:
        f.write(f"### Welcome Headline\n\n{headline.group(1)}\n\n")

    embedded_intro, standalone_intro = extract_welcome_intros(disclaimer)
    if standalone_intro:
        f.write(f"### Welcome Intro (standalone app)\n\n{standalone_intro}\n\n")
    if embedded_intro:
        f.write(f"### Welcome Intro (embedded in MitzMode)\n\n{embedded_intro}\n\n")

    f.write("### Disclaimer / About Body\n\n")
    f.write(extract_plus_strings(disclaimer, "const val STARTUP_BODY") + "\n\n")

    producer_intro = re.search(r'const val PRODUCER_INTRO = "([^"]*)"', disclaimer)
    producer_name = re.search(r'const val PRODUCER_NAME = "([^"]*)"', disclaimer)
    website = re.search(r'const val WEBSITE_DISPLAY = "([^"]*)"', disclaimer)
    if producer_intro and producer_name:
        f.write(f"**{producer_intro.group(1)}** {producer_name.group(1)}\n\n")
    if website:
        f.write(f"Website: {website.group(1)}\n\n")

    rest = read(SHARED / "domain" / "RestMessages.kt")
    shabbat_title = re.search(r'const val SHABBAT_REST_TITLE = "([^"]*)"', rest)
    if shabbat_title:
        f.write(f"### Rest Screen Title (Shabbat active)\n\n{shabbat_title.group(1)}\n\n")
    f.write("### Rest Screen Title (Shabbat approaching)\n\nShabbat is about to begin\n\n")
    f.write("### Rest Screen Title (Yom Tov active)\n\n[Holiday name] (Yom Tov)\n\n")
    f.write("### Rest Screen Title (Yom Tov approaching)\n\n[Holiday name] is about to begin\n\n")

    f.write("### Rest Screen Messages\n\n")
    rest_blocks = [
        ("Shabbat (active)", "fun shabbatMessage(): String"),
        ("Shabbat (approaching)", "fun shabbatApproachingMessage(): String"),
        ("Yom Tov (active)", "fun yomTovMessage(holidayName: String): String"),
        ("Yom Tov (approaching)", "fun yomTovApproachingMessage(holidayName: String): String"),
    ]
    for label, anchor in rest_blocks:
        body = extract_plus_strings(rest, anchor).replace("$holidayName", "[holiday name]")
        if body:
            f.write(f"**{label}:**\n\n{body}\n\n")

    write_omer_template(f)


def write_nusach_extras(f) -> None:
    f.write(section("Part 6 — Nusach-Only Checklist Items (nusach-extras.json)", 1))
    path = ROOT / "shared" / "src" / "commonMain" / "composeResources" / "files" / "nusach-extras.json"
    if not path.exists():
        path = ROOT / "data" / "nusach-extras.json"
    if not path.exists():
        f.write("*nusach-extras.json not found.*\n\n")
        return
    data = json.loads(path.read_text(encoding="utf-8"))
    items = sorted(data.get("items", []), key=lambda i: (i.get("section", ""), i.get("sortOrder", 0)))
    for item in items:
        title = item.get("title", item.get("id", "Unknown"))
        section_name = item.get("section", "")
        item_id = item.get("id", "")
        f.write(f"### {title}\n\n")
        if section_name or item_id:
            f.write(f"*Section: {section_name} · ID: `{item_id}`*\n\n")
        for field in (
            "explanation",
            "explanationAshkenaz",
            "explanationSefard",
            "explanationEdotHamizrach",
            "explanationChabad",
            "explanationFemale",
        ):
            text = (item.get(field) or "").strip()
            if text:
                label = EXPLANATION_VARIANT_LABELS.get(field, field)
                if label != "Default":
                    f.write(f"**{label}:**\n\n")
                f.write(f"{text}\n\n")


def write_extra_kotlin_modules(f) -> None:
    f.write(section("Part 7 — Dynamic Templates & Zman / Holiday Hints (Kotlin)", 1))
    for filename in EXTRA_SEASONAL_KOTLIN_FILES:
        path = DOMAIN / filename
        if not path.exists():
            continue
        content = read(path)
        fns = [(n, b) for n, b in extract_functions_named(content, filename) if is_exportable_kotlin_function(n)]
        const_blocks = []
        for m in CONST_VAL.finditer(content):
            val = unescape_kotlin(m.group(1))
            if len(val) > 30 and any(c.isalpha() for c in val):
                const_blocks.append(val)
        for m in KOTLIN_STRING.finditer(content):
            body = unescape_kotlin(m.group(1).strip())
            if len(body) > 40 and "fun " not in body[:20]:
                const_blocks.append(body)
        if not fns and not const_blocks:
            continue
        f.write(section(filename, 2))
        for name, body in sorted(fns, key=lambda x: x[0]):
            f.write(f"### {name}\n\n{body}\n\n")
        seen: set[str] = set()
        for block in const_blocks:
            if block in seen:
                continue
            seen.add(block)
            f.write(f"{block}\n\n")


def write_mitzvot_cloud(f) -> None:
    f.write(section("Part 8 — Mitzvah Cloud Educational Blurbs (mitzvotcloud.json)", 1))
    path = ROOT / "data" / "mitzvotcloud.json"
    if not path.exists():
        f.write("*mitzvotcloud.json not found.*\n\n")
        return
    data = json.loads(path.read_text(encoding="utf-8"))
    for entry in data.get("mitzvot", []):
        title = entry.get("title") or entry.get("id") or "Untitled"
        text = (entry.get("text") or "").strip()
        entry_id = entry.get("id", "")
        if not text:
            continue
        f.write(f"### {title}\n\n")
        if entry_id:
            f.write(f"*ID: `{entry_id}`*\n\n")
        f.write(f"{text}\n\n")


def write_halachic_disclaimer_doc(f) -> None:
    f.write(section("Part 9 — Standalone Halachic Disclaimer (docs/HALACHIC_DISCLAIMER.md)", 1))
    path = ROOT / "docs" / "HALACHIC_DISCLAIMER.md"
    if path.exists():
        f.write(path.read_text(encoding="utf-8").strip() + "\n\n")
    else:
        f.write("*(HALACHIC_DISCLAIMER.md not found.)*\n\n")


def write_ui_strings_appendix(f) -> None:
    f.write(section("Part 10 — All Other UI Labels & Prompts (strings.json)", 1))
    f.write(
        "Deduped English strings from UI screens, zman hints, settings, onboarding, etc. "
        "Many overlap with Parts 1–5; included for completeness when searching this file.\n\n"
    )
    path = ROOT / "data" / "translation-catalog" / "strings.json"
    if not path.exists():
        f.write("*strings.json not found — run `python tools/extract_all_strings.py` first.*\n\n")
        return
    data = json.loads(path.read_text(encoding="utf-8"))
    strings = sorted(data.get("strings", data) if isinstance(data, dict) else data)
    if isinstance(strings, dict):
        strings = sorted(strings.keys())
    for s in strings:
        if not isinstance(s, str) or len(s.strip()) < 3:
            continue
        f.write(f"- {s}\n")
    f.write("\n")


def write_export_header(f, *, title: str, include_extra_parts: bool) -> None:
    f.write(f"# {title}\n\n")
    f.write(
        "Plain-English export of **all user-visible halachic and educational copy** "
        "in Be a Tzaddik / Holy Light Checklist.\n\n"
        "**Part 1:** Short + full glossary · **Part 2:** Shabbat Guide (incl. 39 melachot) · "
        "**Part 3:** Seasonal/hidden/festival checklist · **Part 4:** Daily mitzvot checklist · "
        "**Part 5:** Disclaimer, About, rest screens"
    )
    if include_extra_parts:
        f.write(
            " · **Part 6:** Nusach extras · **Part 7:** Dynamic templates & zman hints · "
            "**Part 8:** Mitzvah cloud blurbs · **Part 9:** Halachic disclaimer doc · "
            "**Part 10:** Other UI strings"
        )
    f.write(
        "\n\nDynamic placeholders (local times, Hebrew dates, Omer day number, Chanukah night) "
        "appear as bracketed notes where the app fills them at runtime.\n\n"
        "---\n"
    )


def write_all_content(f, *, include_extra_parts: bool = False) -> None:
    write_glossary(f)
    write_shabbat_guide(f)
    write_seasonal_doc(f)
    write_daily_checklist(f)
    write_static_blocks(f)
    if include_extra_parts:
        write_nusach_extras(f)
        write_extra_kotlin_modules(f)
        write_mitzvot_cloud(f)
        write_halachic_disclaimer_doc(f)
        write_ui_strings_appendix(f)


def main() -> None:
    OUT.parent.mkdir(parents=True, exist_ok=True)
    with OUT.open("w", encoding="utf-8") as f:
        write_export_header(f, title="Be a Tzaddik — Complete App Text for Halachic Review", include_extra_parts=False)
        write_all_content(f, include_extra_parts=False)
        f.write("\n---\n\n")
        f.write(
            "*End of export. Re-run `python tools/export_app_reference_text.py` to regenerate from source.*\n"
        )

    with INFO_OUT.open("w", encoding="utf-8") as f:
        write_export_header(f, title="infotosearchthrough", include_extra_parts=True)
        write_all_content(f, include_extra_parts=True)
        f.write("\n---\n\n")
        f.write(
            "*End of infotosearchthrough. Re-run `python tools/export_app_reference_text.py` to regenerate from source.*\n"
        )

    print(f"Wrote {OUT} ({OUT.stat().st_size // 1024} KB)")
    print(f"Wrote {INFO_OUT} ({INFO_OUT.stat().st_size // 1024} KB)")
    if SEASONAL_OUT.exists():
        print(f"Wrote {SEASONAL_OUT} ({SEASONAL_OUT.stat().st_size // 1024} KB)")


if __name__ == "__main__":
    main()
