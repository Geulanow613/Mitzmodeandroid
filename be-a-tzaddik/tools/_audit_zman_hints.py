#!/usr/bin/env python3
"""Find checklist zman hint strings missing from catalog or Hebrew bundle."""
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CATALOG = ROOT / "data" / "translation-catalog" / "strings.json"
HE = ROOT / "data" / "bundled-translations" / "he.json"

DOMAIN_FILES = [
    ROOT / "shared/src/commonMain/kotlin/com/beardytop/beatzaddik/domain/BirkatHaIlanotRules.kt",
    ROOT / "shared/src/commonMain/kotlin/com/beardytop/beatzaddik/domain/ChecklistZmanEvaluator.kt",
    ROOT / "shared/src/commonMain/kotlin/com/beardytop/beatzaddik/domain/UpcomingHolidayPlanner.kt",
    ROOT / "shared/src/commonMain/kotlin/com/beardytop/beatzaddik/domain/UpcomingHolidayTiming.kt",
    ROOT / "shared/src/commonMain/kotlin/com/beardytop/beatzaddik/domain/ZmanPeriodLabels.kt",
]

HINT_PATTERNS = [
    re.compile(r"\bhint\s*=\s*\"([^\"]+)\""),
    re.compile(r"\bmakeupNote\s*=\s*\"([^\"]+)\""),
    re.compile(r"\bactiveHint\s*=\s*\"([^\"]+)\""),
    re.compile(r"\bupcomingTemplate\s*=\s*\"([^\"]+)\""),
    re.compile(r"\bexpiredTemplate\s*=\s*\"([^\"]+)\""),
    re.compile(r"\bactiveHintTemplate\s*=\s*\"([^\"]+)\""),
    re.compile(r"\bmakeupTemplate\s*=\s*\"([^\"]+)\""),
    re.compile(r"\bupcoming\s*=\s*\"([^\"]+)\""),
    re.compile(r"\bexpired\s*=\s*\"([^\"]+)\""),
    re.compile(r"\bmakeup\s*=\s*\"([^\"]+)\""),
]

BIRKAT_ILANOT_PATTERNS = [
    re.compile(r"->\s*\"([^\"]{30,})\""),
    re.compile(r"else\s*->\s*\"([^\"]{30,})\""),
]


def extract_hint_strings(text: str, path: Path) -> set[str]:
    found: set[str] = set()
    patterns = list(HINT_PATTERNS)
    if path.name == "BirkatHaIlanotRules.kt":
        patterns.extend(BIRKAT_ILANOT_PATTERNS)
    for pat in patterns:
        for m in pat.finditer(text):
            s = m.group(1).strip()
            if not s or "$" in s:
                continue
            found.add(s)
    return found


def main() -> None:
    import sys

    strict = "--strict" in sys.argv
    catalog_data = json.loads(CATALOG.read_text(encoding="utf-8"))
    catalog = set(catalog_data["strings"] if isinstance(catalog_data, dict) else catalog_data)
    he_entries = json.loads(HE.read_text(encoding="utf-8"))["entries"]

    all_strings: set[str] = set()
    for path in DOMAIN_FILES:
        if path.exists():
            all_strings |= extract_hint_strings(path.read_text(encoding="utf-8"), path)

    missing_catalog = sorted(s for s in all_strings if s not in catalog)
    missing_he = sorted(
        s
        for s in all_strings
        if s in catalog and (s not in he_entries or he_entries[s] == s)
    )

    print("=== HINT STRINGS NOT IN strings.json ===")
    for s in missing_catalog:
        print(s)
    print(f"\nTotal: {len(missing_catalog)}")

    print("\n=== IN catalog but missing Hebrew bundle ===")
    for s in missing_he:
        print(s)
    print(f"\nTotal: {len(missing_he)}")

    if strict and (missing_catalog or missing_he):
        raise SystemExit(1)


if __name__ == "__main__":
    main()
