#!/usr/bin/env python3
"""Find duplicate glossary term ids in HalachicTermsDictionary source."""
from __future__ import annotations

import re
from collections import defaultdict
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
DICT_PATH = ROOT / "shared/src/commonMain/kotlin/com/beardytop/beatzaddik/domain/HalachicTermsDictionary.kt"
BEGINNER_PATH = ROOT / "shared/src/commonMain/kotlin/com/beardytop/beatzaddik/domain/BeginnerHalachaGlossary.kt"


def term_id(title: str) -> str:
    return re.sub(r"[^a-z0-9']+", "_", title.lower()).strip("_")


def main() -> None:
    import sys
    sys.stdout.reconfigure(encoding="utf-8")
    text = DICT_PATH.read_text(encoding="utf-8")
    lines = text.splitlines()
    entries: list[tuple[str, str, str, int, str]] = []

    enriched_start = next(
        (i for i, ln in enumerate(lines, 1) if "private val enrichedOverrides" in ln),
        999999,
    )

    # line("title — def") possibly split across lines inside enrichedOverrides
    line_pat = re.compile(
        r'line\(\s*"([^"]+ — [^"]+)"',
        re.MULTILINE,
    )
    for m in line_pat.finditer(text):
        chunk = m.group(1)
        title, defn = chunk.split(" — ", 1)
        title = title.strip()
        pos = text[: m.start()].count("\n") + 1
        section = "enrichedOverrides" if pos >= enriched_start else "supplemental"
        entries.append((term_id(title), title, section, pos, defn[:70]))

    for i, line in enumerate(BEGINNER_PATH.read_text(encoding="utf-8").splitlines(), 1):
        m = re.search(r'const val [A-Z_]+ = "([^"]+)"', line)
        if not m or " — " not in m.group(1):
            continue
        chunk = m.group(1)
        title, defn = chunk.split(" — ", 1)
        title = title.strip()
        entries.append((term_id(title), title, "BeginnerHalachaGlossary", i, defn[:70]))

    by_id: dict[str, list[tuple[str, str, str, int, str]]] = defaultdict(list)
    for entry in entries:
        by_id[entry[0]].append(entry)

    dups = {k: v for k, v in by_id.items() if len(v) > 1}
    print(f"Total raw entries: {len(entries)}")
    print(f"Unique ids: {len(by_id)}")
    print(f"Duplicate ids: {len(dups)}")
    print()

    verbose = "--verbose" in sys.argv

    safe_to_remove: list[tuple[str, int, str, str]] = []
    beginner_supplemental: list[tuple[str, int, str]] = []
    supplemental_only: list[tuple[str, list[tuple]]] = []

    for tid_key, items in sorted(dups.items(), key=lambda x: (-len(x[1]), x[0])):
        has_enriched = any(s == "enrichedOverrides" for _, _, s, _, _ in items)
        has_beginner = any(s == "BeginnerHalachaGlossary" for _, _, s, _, _ in items)
        supp = [it for it in items if it[2] == "supplemental"]
        if len(supp) > 1 and not has_enriched and not has_beginner:
            supplemental_only.append((tid_key, supp))

        if verbose:
            print(f"=== {tid_key} ({len(items)}x) enriched={has_enriched} beginner={has_beginner} ===")
        for _, title, src, ln, defn in items:
            if has_enriched and src == "supplemental":
                safe_to_remove.append((tid_key, ln, title, "enriched"))
            elif has_beginner and src == "supplemental" and not has_enriched:
                beginner_supplemental.append((tid_key, ln, title))
            elif has_beginner and src == "supplemental" and has_enriched:
                safe_to_remove.append((tid_key, ln, title, "both"))
            if verbose:
                marker = ""
                if has_enriched and src == "supplemental":
                    marker = " [REMOVABLE: enriched wins]"
                elif has_beginner and src == "supplemental":
                    marker = " [REMOVABLE: beginner/enriched wins]"
                print(f"  L{ln} [{src}] {title} — {defn}...{marker}")
        if verbose:
            print()

    print(f"=== Supplemental dups removable (enriched exists): {len(safe_to_remove)} ===")
    for tid_key, ln, title, kind in sorted(safe_to_remove, key=lambda x: x[1]):
        print(f"  L{ln} {tid_key} ({title}) [{kind}]")

    print()
    print(f"=== Supplemental dups removable (beginner only, no enriched): {len(beginner_supplemental)} ===")
    for tid_key, ln, title in sorted(beginner_supplemental, key=lambda x: x[1]):
        print(f"  L{ln} {tid_key} ({title})")

    print()
    print(f"=== Supplemental-only duplicate groups (consolidate to 1): {len(supplemental_only)} ===")
    extra_lines = sum(len(s) - 1 for _, s in supplemental_only)
    print(f"    ({extra_lines} extra line() calls could be removed)")
    for tid_key, supp in supplemental_only[:25]:
        lines_str = ", ".join(f"L{ln}" for _, _, _, ln, _ in supp)
        print(f"  {tid_key} ({len(supp)}x): {lines_str}")
    if len(supplemental_only) > 25:
        print(f"  ... and {len(supplemental_only) - 25} more groups")


if __name__ == "__main__":
    main()
