#!/usr/bin/env python3
"""Remove redundant supplemental glossary entries; merge aliases into enriched overrides."""
from __future__ import annotations

import re
import sys
from collections import defaultdict
from dataclasses import dataclass, field
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
DICT_PATH = ROOT / "shared/src/commonMain/kotlin/com/beardytop/beatzaddik/domain/HalachicTermsDictionary.kt"


@dataclass
class ParsedLine:
    raw: str
    title: str
    definition: str
    aliases: list[str] = field(default_factory=list)
    is_multiline: bool = False

    @property
    def term_id(self) -> str:
        return re.sub(r"[^a-z0-9']+", "_", self.title.lower()).strip("_")

    @property
    def labels(self) -> list[str]:
        parts = [p.strip() for p in re.split(r"\s*/\s*", self.title)]
        out: list[str] = []
        seen: set[str] = set()
        for label in [*parts, *self.aliases]:
            key = label.lower()
            if label and key not in seen:
                seen.add(key)
                out.append(label)
        return out


def parse_line_call(text: str) -> ParsedLine | None:
    if " — " not in text:
        return None
    strings = re.findall(r'"((?:[^"\\]|\\.)*)"', text, re.DOTALL)
    if not strings or " — " not in strings[0]:
        return None
    title, definition = strings[0].split(" — ", 1)
    aliases = strings[1:]
    return ParsedLine(
        raw=text,
        title=title.strip(),
        definition=definition.strip(),
        aliases=aliases,
        is_multiline="\n" in text,
    )


def split_line_calls(block: str) -> list[tuple[str, ParsedLine | None]]:
    """Split block into individual line(...) chunks preserving order."""
    chunks: list[tuple[str, ParsedLine | None]] = []
    i = 0
    n = len(block)
    while i < n:
        start = block.find("line(", i)
        if start < 0:
            break
        depth = 0
        j = start
        in_string = False
        escape = False
        while j < n:
            ch = block[j]
            if in_string:
                if escape:
                    escape = False
                elif ch == "\\":
                    escape = True
                elif ch == '"':
                    in_string = False
            else:
                if ch == '"':
                    in_string = True
                elif ch == "(":
                    depth += 1
                elif ch == ")":
                    depth -= 1
                    if depth == 0:
                        j += 1
                        # Include trailing comma after line(...) if present
                        while j < n and block[j] in " \t":
                            j += 1
                        if j < n and block[j] == ",":
                            j += 1
                        break
            j += 1
        chunk = block[start:j]
        chunks.append((chunk, parse_line_call(chunk)))
        i = j
    return chunks


def format_single_line(entry: ParsedLine) -> str:
    alias_part = ""
    if entry.aliases:
        alias_part = ", " + ", ".join(f'"{a}"' for a in entry.aliases)
    return f'        line("{entry.title} — {entry.definition}"{alias_part}),'


def format_multiline(entry: ParsedLine) -> str:
    lines = ["        line(", f'            "{entry.title} — {entry.definition}",']
    for alias in entry.aliases:
        lines.append(f'            "{alias}",')
    lines.append("        ),")
    return "\n".join(lines)


def main() -> None:
    sys.stdout.reconfigure(encoding="utf-8")
    text = DICT_PATH.read_text(encoding="utf-8")

    sup_start = text.index("private val supplemental")
    sup_list_start = text.index("listOf(", sup_start)
    sup_close = text.index("    ).mapNotNull { it }", sup_list_start)
    enriched_marker = "    /** Richer definitions that always win over shorter duplicate entries. */"
    enriched_start = text.index("private val enrichedOverrides")
    enriched_list_start = text.index("listOf(", enriched_start)
    enriched_close = text.index("    ).mapNotNull { it }", enriched_list_start)

    supplemental_block = text[sup_list_start + len("listOf(") : sup_close]
    enriched_block = text[enriched_list_start + len("listOf(") : enriched_close]

    sup_chunks = split_line_calls(supplemental_block)
    enr_chunks = split_line_calls(enriched_block)

    enriched_by_id: dict[str, ParsedLine] = {}
    enriched_aliases: dict[str, set[str]] = defaultdict(set)
    for _, parsed in enr_chunks:
        if not parsed:
            continue
        enriched_by_id[parsed.term_id] = parsed
        for label in parsed.labels:
            enriched_aliases[parsed.term_id].add(label)

    sup_by_id: dict[str, list[ParsedLine]] = defaultdict(list)
    for _, parsed in sup_chunks:
        if parsed:
            sup_by_id[parsed.term_id].append(parsed)

    kept_supplemental: list[ParsedLine] = []
    removed_enriched_dup = 0
    removed_sup_dup = 0

    for tid, entries in sup_by_id.items():
        all_labels: set[str] = set()
        for e in entries:
            all_labels.update(e.labels)

        if tid in enriched_by_id:
            removed_enriched_dup += len(entries)
            before = len(enriched_aliases[tid])
            enriched_aliases[tid].update(all_labels)
            after = len(enriched_aliases[tid])
            if after > before:
                pass  # aliases merged into enriched
            continue

        # Keep one supplemental entry: longest definition, all aliases merged
        best = max(entries, key=lambda e: len(e.definition))
        merged_aliases: list[str] = []
        seen: set[str] = {best.title.lower()}
        for label in best.labels:
            if label.lower() not in seen:
                seen.add(label.lower())
                if label != best.title.split("/")[0].strip():
                    merged_aliases.append(label)
        for e in entries:
            for label in e.labels:
                if label.lower() not in seen:
                    seen.add(label.lower())
                    merged_aliases.append(label)
        removed_sup_dup += len(entries) - 1
        kept_supplemental.append(
            ParsedLine(
                raw="",
                title=best.title,
                definition=best.definition,
                aliases=merged_aliases,
            )
        )

    # Rebuild enriched with merged aliases
    new_enr_lines: list[str] = []
    for chunk, parsed in enr_chunks:
        if not parsed:
            # Preserve unparsed chunks but ensure indentation and trailing comma
            fixed = chunk.strip()
            if not fixed.startswith("line("):
                fixed = "        " + fixed
            if not fixed.rstrip().endswith(","):
                fixed = fixed.rstrip().rstrip(",") + ","
            new_enr_lines.append(fixed)
            continue
        tid = parsed.term_id
        want = enriched_aliases[tid]
        have = set(l.lower() for l in parsed.labels)
        extra = [l for l in sorted(want, key=str.lower) if l.lower() not in have]
        # Don't duplicate title in aliases
        title_keys = {p.strip().lower() for p in re.split(r"\s*/\s*", parsed.title)}
        extra = [l for l in extra if l.lower() not in title_keys]
        new_entry = ParsedLine(
            raw="",
            title=parsed.title,
            definition=parsed.definition,
            aliases=list(dict.fromkeys([*parsed.aliases, *extra])),
            is_multiline=True,
        )
        new_enr_lines.append(format_multiline(new_entry))

    new_sup_body = "\n".join(format_single_line(e) for e in kept_supplemental)

    new_text = (
        text[: sup_list_start + len("listOf(")]
        + "\n"
        + new_sup_body
        + "\n"
        + text[sup_close : enriched_list_start + len("listOf(")]
        + "\n"
        + "\n".join(new_enr_lines)
        + "\n"
        + text[enriched_close:]
    )

    DICT_PATH.write_text(new_text, encoding="utf-8")

    print(f"Supplemental before: {len(sup_chunks)} entries")
    print(f"Supplemental after:  {len(kept_supplemental)} entries")
    print(f"Removed (enriched dup): {removed_enriched_dup}")
    print(f"Removed (supplemental-only dup): {removed_sup_dup}")
    print(f"Enriched entries: {len(enr_chunks)}")


if __name__ == "__main__":
    main()
