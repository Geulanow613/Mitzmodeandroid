#!/usr/bin/env python3
"""Export all user-visible app copy into one plain-text review file."""

from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SHARED = ROOT / "shared" / "src" / "commonMain" / "kotlin" / "com" / "beardytop" / "beatzaddik"
OUT = ROOT / "docs" / "APP_REFERENCE_TEXT_FOR_REVIEW.md"

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


def write_seasonal_doc(f) -> None:
    seasonal_path = ROOT / "docs" / "SEASONAL_AND_HIDDEN_CHECKLIST_TEXT.md"
    if not seasonal_path.exists():
        return
    f.write(section("Part 3 — Seasonal, Hidden & Festival Checklist Explainers", 1))
    f.write(
        "All seasonal and situational checklist copy: hidden Shabbat/Yom Tov screens, "
        "Erev Shabbat prep, Musaf, Omer, holidays, Pesach prep, mourning periods, etc.\n\n"
    )
    content = seasonal_path.read_text(encoding="utf-8")
    start = content.find("## When the checklist is hidden")
    if start < 0:
        start = content.find("---", 50)
        start = content.find("\n", start) + 1
    f.write(content[start:].strip())
    f.write("\n\n")


def write_daily_checklist(f) -> None:
    f.write(section("Part 4 — Daily Mitzvot Checklist Explainers", 1))
    f.write(
        "Full explanation text for every item in the daily checklist (`checklist-items.json`). "
        "Includes default text plus Ashkenaz, Sefard, Chabad, and female variants where present.\n\n"
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
    beginner = read(SHARED / "domain" / "BeginnerHalachaGlossary.kt")
    omer_key_terms = ""
    m = re.search(r"fun omerBasics\(\): String = block\(([\s\S]*?)\)", beginner)
    if m:
        lines = re.findall(r"(\w+)", m.group(1))
        for const in lines:
            cm = re.search(rf"const val {const}\s*=\s*\n?\s*\"((?:[^\"\\]|\\.)*)\"", beginner)
            if cm:
                omer_key_terms += f"• {unescape_kotlin(cm.group(1))}\n"
    f.write("**Key terms block (prepended in app):**\n\n")
    f.write(omer_key_terms + "\n")
    f.write(
        "**Body template** (runtime fills day number, weekday names, local tzeit time, nusach line):\n\n"
        "Sefirat HaOmer links Pesach to Shavuot — counting each day from the Exodus toward receiving the Torah.\n\n"
        "Today in the Omer: day [N] of 49 — [weeks and days phrase].\n\n"
        "Tonight's count:\n"
        "• [Weekday] night — count day [N] ([weeks phrase]) after nightfall [at local tzeit time].\n"
        "• [Next weekday] night: you will count day [N+1] ([weeks phrase]). *(Omitted on day 49.)*\n\n"
        "How to count:\n"
        "• Stand and recite the blessing before counting if you are still saying it with a blessing "
        "(if you missed a day, ask your rabbi before continuing with a bracha).\n"
        "• Say: Days 1–6: \"Today is N day(s) of the Omer.\" only — do not mention weeks. "
        "From day 7 onward: add \"which is [weeks and days phrase]\".\n"
        "• Count after nightfall (tzeit); complete before dawn. If you forgot at night, count the next day "
        "during the daytime without a bracha. If you do this before sunset, you can continue counting on "
        "subsequent nights with a bracha. You only lose the blessing permanently if you miss an entire "
        "24-hour cycle (both night and the following day) — ask your rav.\n\n"
        "One of these lines appears by nusach:\n"
        "• Many in Chabad count after Maariv (Tehillat Hashem).\n"
        "• Many Sefardim count after Maariv.\n"
        "• Many Ashkenazim count after Maariv.\n\n"
    )


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


def main() -> None:
    OUT.parent.mkdir(parents=True, exist_ok=True)
    with OUT.open("w", encoding="utf-8") as f:
        f.write("# Be a Tzaddik — Complete App Text for Halachic Review\n\n")
        f.write(
            "Plain-English export of **all user-visible halachic and educational copy** in the app.\n\n"
            "**Part 1:** Short + full glossary · **Part 2:** Shabbat Guide · "
            "**Part 3:** Seasonal/hidden/festival checklist · **Part 4:** Daily mitzvot checklist · "
            "**Part 5:** Disclaimer, About, rest screens\n\n"
            "Dynamic placeholders (local times, Hebrew dates, Omer day number, Chanukah night) "
            "appear as bracketed notes where the app fills them at runtime.\n\n"
            "---\n"
        )

        write_glossary(f)
        write_shabbat_guide(f)
        write_seasonal_doc(f)
        write_daily_checklist(f)
        write_static_blocks(f)

        f.write("\n---\n\n")
        f.write(
            "*End of export. Re-run `python tools/export_app_reference_text.py` to regenerate from source.*\n"
        )

    print(f"Wrote {OUT} ({OUT.stat().st_size // 1024} KB)")


if __name__ == "__main__":
    main()
