#!/usr/bin/env python3
"""Fix PH0/PH1/PH2 placeholders and HTML entities in human translation shards."""
import json, re, html
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
HUMAN = ROOT / "data/translation-catalog/human"
CATALOG = ROOT / "data/translation-catalog/strings.json"
LANGS = ("he", "es", "fr", "ru")

all_strings: list[str] = json.loads(CATALOG.read_text(encoding="utf-8"))["strings"]
en_links_cache: dict[str, list[tuple[str, str]]] = {}

def en_links(en_key: str) -> list[tuple[str, str]]:
    if en_key not in en_links_cache:
        en_links_cache[en_key] = re.findall(r'\[([^\]]+)\]\(([^)]+)\)', en_key)
    return en_links_cache[en_key]

def fix_ph_placeholders(en_key: str, tr: str) -> str:
    """Replace PHn with the n-th markdown link label from the English source."""
    links = en_links(en_key)
    if not links:
        # No links in source — just strip stray PHn tokens
        tr = re.sub(r'\bPH\d+\b', '???', tr)
        return tr
    def replace_ph(m: re.Match) -> str:
        n = int(m.group(1))
        return links[n][0] if n < len(links) else m.group(0)
    # Pattern: PH0 or PH1 etc. as a word boundary
    tr = re.sub(r'\bPH(\d+)\b', replace_ph, tr)
    return tr

def fix_html_entities(tr: str) -> str:
    return html.unescape(tr)

def needs_fixing(tr: str) -> bool:
    return bool(re.search(r'\bPH\d+\b', tr)) or bool(re.search(r'&(?:amp|quot|lt|gt|apos);', tr))

total_fixed = 0

for path in sorted(HUMAN.glob("*.json")):
    if path.name.startswith("_") or "_src" in path.name or "_only" in path.name:
        continue
    raw = path.read_text(encoding="utf-8")
    data: dict = json.loads(raw)
    changed = False
    for lang in LANGS:
        if lang not in data:
            continue
        for en_key, tr in list(data[lang].items()):
            if not needs_fixing(tr):
                continue
            fixed = fix_html_entities(tr)
            fixed = fix_ph_placeholders(en_key, fixed)
            if fixed != tr:
                data[lang][en_key] = fixed
                changed = True
                total_fixed += 1
                print(f"  [{path.name}:{lang}] fixed: {tr[:60]!r}")
                print(f"    → {fixed[:60]!r}")
    if changed:
        path.write_text(json.dumps(data, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
        print(f"Saved {path.name}")

print(f"\nTotal fixes: {total_fixed}")
