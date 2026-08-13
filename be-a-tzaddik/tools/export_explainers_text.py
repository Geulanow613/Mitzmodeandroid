#!/usr/bin/env python3
"""Export every checklist mitzvah explainer into one plain-text review file."""

from __future__ import annotations

import json
import re
import sys
from io import StringIO
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(Path(__file__).resolve().parent))

from export_app_reference_text import (  # noqa: E402
    DOMAIN,
    EXPLANATION_FIELD,
    EXPLANATION_VARIANT_LABELS,
    SEASONAL_KOTLIN_FILES,
    extract_checklist_item_defs,
    extract_erev_chag_triples,
    extract_functions_named,
    is_exportable_kotlin_function,
    read,
    unescape_kotlin,
    write_item_explanations,
)

OUT = ROOT / "explainerstext.txt"

KOTLIN_EXPLAINER_FILES = [
    "SeasonalMitzvahText.kt",
    "ErevChagPrepText.kt",
    "ErevPesachPrepText.kt",
    "PublicFastDayText.kt",
    "YomTovShabbatPrepText.kt",
    "PurimMeshulashText.kt",
    "PurimBrachotText.kt",
    "OmerCountText.kt",
    "ExplainerTemplateSupport.kt",
    "ErevPesachExplainerTemplates.kt",
]

FUN_REF = re.compile(
    r"(explanation(?:Ashkenaz|Sefard|EdotHamizrach|Chabad|Female)?)\s*=\s*"
    r"(?:SeasonalMitzvahText|PublicFastDayText|OmerCountText|YomTovShabbatPrepText|"
    r"PurimMeshulashText|ErevPesachPrepText|ErevChagPrepText)\.(\w+)\("
)

CONST_TEMPLATE = re.compile(
    r"private (?:const )?val (\w+)\s*=\s*\"\"\"([\s\S]*?)\"\"\"",
    re.MULTILINE,
)

FUN_IDENT_REF = re.compile(
    r"fun\s+(\w+)\s*\([^)]*\)\s*(?::\s*String)?\s*=\s*(\w+)\s*(?:\.orEmpty\(\))?\s*$",
    re.MULTILINE,
)

FUN_IDENT_CALL = re.compile(
    r"fun\s+(\w+)\s*\([^)]*\)\s*(?::\s*String)?\s*=\s*(\w+)\([^)]*\)\s*(?:\.orEmpty\(\))?\s*$",
    re.MULTILINE,
)

FUN_INLINE_STRING = re.compile(
    r'fun\s+(\w+)\s*\([^)]*\)\s*(?::\s*String)?\s*=\s*"((?:[^"\\]|\\.)*)"\s*$',
    re.MULTILINE,
)


def fill_template(template: str, args: dict[str, str]) -> str:
    out = template
    for key, value in args.items():
        out = out.replace(f"${key}", value)
        out = out.replace(f"${{'$'}}{key}", value)  # escaped in Kotlin sources
    return out


def augment_registry(content: str, registry: dict[str, str]) -> None:
    for func_name, ident in FUN_IDENT_REF.findall(content):
        body = registry.get(ident, "")
        if body and func_name not in registry:
            registry[func_name] = body
    for func_name, callee in FUN_IDENT_CALL.findall(content):
        body = registry.get(callee, "")
        if body and func_name not in registry:
            registry[func_name] = body
    for func_name, text in FUN_INLINE_STRING.findall(content):
        body = unescape_kotlin(text)
        if body and func_name not in registry:
            registry[func_name] = body

    yaaleh_pairs = [
        ("yaalehVyavoShacharitExplanation", "YAALEH_SHACHARIT_TEMPLATE", "YAALEH_FORGOT_AMIDAH_SHACHARIT"),
        ("yaalehVyavoShacharitExplanationFemale", "YAALEH_SHACHARIT_FEMALE_TEMPLATE", "YAALEH_FORGOT_AMIDAH_SHACHARIT"),
        ("yaalehVyavoMinchaExplanation", "YAALEH_MINCHA_TEMPLATE", "YAALEH_FORGOT_AMIDAH_MINCHA"),
        ("yaalehVyavoMinchaExplanationFemale", "YAALEH_MINCHA_FEMALE_TEMPLATE", "YAALEH_FORGOT_AMIDAH_MINCHA"),
        ("yaalehVyavoMaarivExplanation", "YAALEH_MAARIV_TEMPLATE", "YAALEH_FORGOT_MAARIV"),
        ("yaalehVyavoMaarivExplanationFemale", "YAALEH_MAARIV_FEMALE_TEMPLATE", "YAALEH_FORGOT_MAARIV"),
    ]
    for func_name, tmpl_key, forgot_key in yaaleh_pairs:
        tmpl = registry.get(tmpl_key, "")
        forgot = registry.get(forgot_key, "")
        if tmpl and forgot and func_name not in registry:
            registry[func_name] = fill_template(tmpl, {"forgotBlock": forgot})

DIVIDER = "=" * 80
SUBDIVIDER = "-" * 80


def load_json_items(path: Path) -> list[dict]:
    if not path.exists():
        return []
    data = json.loads(path.read_text(encoding="utf-8"))
    return data.get("items", [])


def build_function_registry() -> dict[str, str]:
    registry: dict[str, str] = {}
    for filename in KOTLIN_EXPLAINER_FILES:
        path = DOMAIN / filename
        if not path.exists():
            continue
        content = read(path)
        for name, body in extract_functions_named(content, filename):
            if body and name not in registry:
                registry[name] = body
        for name, body in CONST_TEMPLATE.findall(content):
            if body.strip() and name not in registry:
                registry[name] = unescape_kotlin(body.strip())
        # Also pick up `const val FOO = """..."""` blocks
        for m in re.finditer(r'const val (\w+)\s*=\s*"""([\s\S]*?)"""', content):
            if m.group(1) not in registry:
                registry[m.group(1)] = unescape_kotlin(m.group(2).strip())

    for filename in KOTLIN_EXPLAINER_FILES:
        path = DOMAIN / filename
        if not path.exists():
            continue
        augment_registry(read(path), registry)

    return registry


def json_item_fields(item: dict) -> dict[str, str]:
    fields: dict[str, str] = {}
    for field, label in EXPLANATION_VARIANT_LABELS.items():
        text = (item.get(field) or "").strip()
        if text:
            fields[field] = text
    return fields


def resolve_block_explanations(block: str, registry: dict[str, str]) -> dict[str, str]:
    fields: dict[str, str] = {}
    for em in EXPLANATION_FIELD.finditer(block):
        fields[em.group(1)] = unescape_kotlin(em.group(2).strip())
    for field, func in FUN_REF.findall(block):
        body = registry.get(func, "")
        if body and field not in fields:
            fields[field] = body
        elif func and field not in fields:
            fields[field] = (
                f"[Runtime text from {func}() — not extracted as static text; "
                f"see ADDITIONAL RUNTIME section below]"
            )
    return fields


def extract_kotlin_checklist_defs(path: Path, registry: dict[str, str]) -> list[dict]:
    content = read(path)
    items: list[dict] = []
    pos = 0
    while True:
        start = content.find("ChecklistItemDef(", pos)
        if start < 0:
            break
        depth = 0
        i = start + len("ChecklistItemDef")
        while i < len(content):
            ch = content[i]
            if ch == "(":
                depth += 1
            elif ch == ")":
                depth -= 1
                if depth == 0:
                    break
            i += 1
        block = content[start + len("ChecklistItemDef(") : i]
        pos = i + 1

        id_m = re.search(r'id\s*=\s*"([^"]*)"', block)
        title_m = re.search(r'title\s*=\s*"([^"]*)"', block)
        section_m = re.search(r'section\s*=\s*"([^"]*)"', block)
        if not id_m and not title_m:
            continue

        item_id = id_m.group(1) if id_m else ""
        title = title_m.group(1) if title_m else item_id
        section = section_m.group(1) if section_m else ""

        fields = resolve_block_explanations(block, registry)
        if not fields:
            continue

        items.append(
            {
                "id": item_id,
                "title": title,
                "section": section,
                "fields": fields,
            }
        )
    return items


def write_mitzvah(
    out: StringIO,
    *,
    title: str,
    item_id: str = "",
    section: str = "",
    fields: dict[str, str],
    note: str = "",
) -> None:
    out.write(f"\n{DIVIDER}\n")
    out.write(f"MITZVAH: {title}\n")
    if item_id:
        out.write(f"ID: {item_id}\n")
    if section:
        out.write(f"SECTION: {section}\n")
    if note:
        out.write(f"NOTE: {note}\n")
    out.write(f"{SUBDIVIDER}\n\n")

    wrote = False
    for field, label in EXPLANATION_VARIANT_LABELS.items():
        text = (fields.get(field) or "").strip()
        if not text:
            continue
        if label != "Default" or wrote:
            out.write(f"[{label}]\n\n")
        out.write(f"{text}\n\n")
        wrote = True
    if not wrote:
        out.write("(No explanation text.)\n\n")


def write_json_section(out: StringIO, heading: str, items: list[dict]) -> None:
    out.write(f"\n{DIVIDER}\n")
    out.write(f"{heading}\n")
    out.write(f"{DIVIDER}\n")
    items = sorted(items, key=lambda i: (i.get("section", ""), i.get("sortOrder", 0), i.get("title", "")))
    for item in items:
        write_mitzvah(
            out,
            title=item.get("title", item.get("id", "Unknown")),
            item_id=item.get("id", ""),
            section=item.get("section", ""),
            fields=json_item_fields(item),
        )


def main() -> None:
    registry = build_function_registry()
    out = StringIO()

    out.write("CHECKLIST MITZVAH EXPLAINERS\n")
    out.write("Plain-text export for review. Auto-generated from checklist-items.json,\n")
    out.write("nusach-extras.json, and Kotlin seasonal/prep modules.\n")
    out.write(f"Generated from sources under: {ROOT.name}/\n")

    checklist_path = (
        ROOT / "shared" / "src" / "commonMain" / "composeResources" / "files" / "checklist-items.json"
    )
    if not checklist_path.exists():
        checklist_path = ROOT / "data" / "checklist-items.json"

    nusach_path = (
        ROOT / "shared" / "src" / "commonMain" / "composeResources" / "files" / "nusach-extras.json"
    )
    if not nusach_path.exists():
        nusach_path = ROOT / "data" / "nusach-extras.json"

    write_json_section(out, "DAILY CHECKLIST (checklist-items.json)", load_json_items(checklist_path))
    write_json_section(out, "NUSACH-ONLY CHECKLIST ITEMS (nusach-extras.json)", load_json_items(nusach_path))

    # Seasonal + Pesach prep items from Kotlin
    seasonal_items = extract_kotlin_checklist_defs(DOMAIN / "SeasonalChecklistItems.kt", registry)
    seasonal_items.extend(extract_kotlin_checklist_defs(DOMAIN / "ErevPesachPrepText.kt", registry))

    # Also use parser that catches inline triple-quoted only (backup merge)
    for item in extract_checklist_item_defs(read(DOMAIN / "SeasonalChecklistItems.kt")):
        existing = next((x for x in seasonal_items if x["id"] == item["id"]), None)
        if existing:
            existing["fields"].update({k: v for k, v in item["fields"].items() if v})
        else:
            seasonal_items.append(item)

    out.write(f"\n{DIVIDER}\n")
    out.write("SEASONAL & SITUATIONAL CHECKLIST (Kotlin)\n")
    out.write(f"{DIVIDER}\n")

    seasonal_items.sort(key=lambda i: (str(i.get("section", "")), str(i.get("title", ""))))
    seen_ids: set[str] = set()
    for item in seasonal_items:
        item_id = str(item.get("id", ""))
        display_id = item_id
        note = ""
        if "$day" in item_id:
            display_id = item_id.replace("$day", "N")
            note = "Template item — day number filled at runtime."
        if item_id in seen_ids:
            continue
        seen_ids.add(item_id)
        write_mitzvah(
            out,
            title=str(item.get("title", item_id)),
            item_id=display_id,
            section=str(item.get("section", "")),
            fields=item.get("fields", {}),
            note=note,
        )

    # Erev Yom Tov holiday-specific blocks (erev_chag_prep content variants)
    erev_content = read(DOMAIN / "ErevChagPrepText.kt")
    erev_triples = extract_erev_chag_triples(erev_content)
    if erev_triples:
        out.write(f"\n{DIVIDER}\n")
        out.write("EREV YOM TOV PREP VARIANTS (shown under erev_chag_prep by holiday)\n")
        out.write(f"{DIVIDER}\n")
        for title, body in erev_triples:
            write_mitzvah(
                out,
                title=title,
                item_id="erev_chag_prep",
                section="Seasonal",
                fields={"explanation": body},
                note="One of several holiday-specific texts for the erev_chag_prep checklist item.",
            )

    # Dynamic / when-based explainers not fully captured above
    out.write(f"\n{DIVIDER}\n")
    out.write("ADDITIONAL RUNTIME EXPLAINER FUNCTIONS (referenced by checklist items)\n")
    out.write(f"{DIVIDER}\n")
    out.write(
        "These functions assemble text at runtime (profile, calendar, nusach). "
        "Included so nothing is missing from review.\n"
    )

    referenced: set[str] = set()
    for path in [DOMAIN / "SeasonalChecklistItems.kt", DOMAIN / "ErevPesachPrepText.kt", DOMAIN / "ExplainerTemplateResolver.kt"]:
        if path.exists():
            for _, func in FUN_REF.findall(read(path)):
                referenced.add(func)

    for name in sorted(referenced):
        if name in registry and registry[name].strip():
            body = registry[name]
            if any(body.strip() == (f.get("fields", {}).get("explanation") or "").strip() for f in seasonal_items):
                continue
            write_mitzvah(
                out,
                title=f"[Function] {name}",
                item_id=name,
                section="(referenced explainer)",
                fields={"explanation": body},
                note="Kotlin explainer function — may map to one or more checklist items.",
            )

    for filename in SEASONAL_KOTLIN_FILES + ["ErevPesachPrepText.kt", "ExplainerTemplateSupport.kt"]:
        path = DOMAIN / filename
        if not path.exists():
            continue
        fns = [
            (n, b)
            for n, b in extract_functions_named(read(path), filename)
            if is_exportable_kotlin_function(n) or n in referenced
        ]
        for name, body in sorted(fns, key=lambda x: x[0]):
            if not body.strip():
                body = registry.get(name, "")
            if not body.strip():
                continue
            # Skip if already emitted as a checklist item field body
            if any(body.strip() == (f.get("fields", {}).get("explanation") or "").strip() for f in seasonal_items):
                continue
            write_mitzvah(
                out,
                title=f"[Function] {name}",
                item_id=name,
                section=filename,
                fields={"explanation": body},
                note="Kotlin explainer function — may map to one or more checklist items.",
            )

    OUT.write_text(out.getvalue().strip() + "\n", encoding="utf-8")
    print(f"Wrote {OUT} ({OUT.stat().st_size:,} bytes)")


if __name__ == "__main__":
    main()
