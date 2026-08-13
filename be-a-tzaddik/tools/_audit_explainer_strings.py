#!/usr/bin/env python3
"""Audit checklist explainer strings for translation gaps and dynamic interpolation."""
from __future__ import annotations

import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CATALOG = ROOT / "data" / "translation-catalog" / "strings.json"
HE = ROOT / "data" / "bundled-translations" / "he.json"
CHECKLIST_JSON = ROOT / "shared" / "src" / "commonMain" / "composeResources" / "files" / "checklist_items.json"

DOMAIN_EXPLAINER_FILES = [
    ROOT / "shared/src/commonMain/kotlin/com/beardytop/beatzaddik/domain/SeasonalMitzvahText.kt",
    ROOT / "shared/src/commonMain/kotlin/com/beardytop/beatzaddik/domain/OmerCountText.kt",
    ROOT / "shared/src/commonMain/kotlin/com/beardytop/beatzaddik/domain/ErevPesachPrepText.kt",
    ROOT / "shared/src/commonMain/kotlin/com/beardytop/beatzaddik/domain/PurimMeshulashText.kt",
    ROOT / "shared/src/commonMain/kotlin/com/beardytop/beatzaddik/domain/PublicFastDayText.kt",
    ROOT / "shared/src/commonMain/kotlin/com/beardytop/beatzaddik/ui/screens/ShabbatGuideData.kt",
]

# Triple-quoted explainer bodies and explanation = "..." assignments.
TRIPLE_QUOTE = re.compile(r'"""(.*?)"""', re.DOTALL)
EXPLANATION_ASSIGN = re.compile(
    r'\b(?:explanation|body)\s*=\s*"""([^"]*(?:"[^"]|"[^"])*?)"""',
    re.DOTALL,
)
# Kotlin string templates that break catalog lookup (not $dateLabel catalog placeholders).
DYNAMIC_INTERP = re.compile(r'\$\{(?!dateLabel\})[^}]+\}|\$[a-zA-Z_][a-zA-Z0-9_]*')


def normalize_explainer(text: str) -> str:
    return text.strip().replace("\r\n", "\n")


def extract_from_kt(path: Path) -> tuple[set[str], list[tuple[str, str]]]:
    static: set[str] = set()
    dynamic: list[tuple[str, str]] = []
    if not path.exists():
        return static, dynamic
    text = path.read_text(encoding="utf-8")
    for m in TRIPLE_QUOTE.finditer(text):
        body = normalize_explainer(m.group(1))
        if len(body) < 40:
            continue
        if "$dateLabel" in body and DYNAMIC_INTERP.search(body.replace("$dateLabel", "")):
            dynamic.append((path.name, body[:120] + "…"))
        elif DYNAMIC_INTERP.search(body):
            dynamic.append((path.name, body[:120] + "…"))
        else:
            static.add(body)
    return static, dynamic


def extract_from_checklist_json() -> set[str]:
    found: set[str] = set()
    if not CHECKLIST_JSON.exists():
        return found
    data = json.loads(CHECKLIST_JSON.read_text(encoding="utf-8"))
    for item in data.get("items", []):
        for key in (
            "explanation",
            "explanationFemale",
            "explanationAshkenaz",
            "explanationSefard",
            "explanationEdotHamizrach",
            "explanationChabad",
        ):
            val = item.get(key, "")
            if isinstance(val, str) and len(val.strip()) >= 40:
                found.add(normalize_explainer(val))
    return found


def main() -> None:
    strict = "--strict" in sys.argv
    catalog = set(json.loads(CATALOG.read_text(encoding="utf-8"))["strings"])
    he_entries = json.loads(HE.read_text(encoding="utf-8"))["entries"]

    static_strings: set[str] = set()
    dynamic_blocks: list[tuple[str, str]] = []
    for path in DOMAIN_EXPLAINER_FILES:
        s, d = extract_from_kt(path)
        static_strings |= s
        dynamic_blocks.extend(d)
    static_strings |= extract_from_checklist_json()

    # Template explainers keyed with $dateLabel are OK when UI uses rememberAppTranslatedTemplate.
    template_keys = {s for s in catalog if "$dateLabel" in s or "{dateLabel}" in s}

    missing_catalog = sorted(s for s in static_strings if s not in catalog and s not in template_keys)
    missing_he = sorted(
        s
        for s in static_strings
        if s in catalog and (s not in he_entries or he_entries[s] == s)
    )
    bad_he = sorted(
        s
        for s in static_strings
        if s in he_entries
        and he_entries[s] != s
        and any(tok in he_entries[s] for tok in ("Hachamah", "Wednesday", "The cycle:", "How to fulfill"))
    )

    print("=== STATIC EXPLAINERS NOT IN strings.json ===")
    for s in missing_catalog[:40]:
        print(s[:100].replace("\n", " ") + ("…" if len(s) > 100 else ""))
    if len(missing_catalog) > 40:
        print(f"… and {len(missing_catalog) - 40} more")
    print(f"\nTotal: {len(missing_catalog)}")

    print("\n=== IN catalog but missing Hebrew bundle ===")
    for s in missing_he[:30]:
        print(s[:100].replace("\n", " ") + ("…" if len(s) > 100 else ""))
    if len(missing_he) > 30:
        print(f"… and {len(missing_he) - 30} more")
    print(f"\nTotal: {len(missing_he)}")

    print("\n=== DYNAMIC EXPLAINERS (need template + args, not plain lookup) ===")
    for src, preview in dynamic_blocks[:25]:
        print(f"[{src}] {preview}")
    print(f"\nTotal: {len(dynamic_blocks)}")

    print("\n=== Hebrew bundle still looks English (sample) ===")
    for s in bad_he[:15]:
        print(s[:80].replace("\n", " ") + "…")
    print(f"\nTotal: {len(bad_he)}")

    if strict and (missing_catalog or missing_he or dynamic_blocks):
        sys.exit(1)


if __name__ == "__main__":
    main()
