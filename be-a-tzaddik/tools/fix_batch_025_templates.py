#!/usr/bin/env python3
"""Fix batch_025 shard strings that lost Kotlin template placeholders."""
import json
import re
import sys
from pathlib import Path

sys.stdout.reconfigure(encoding="utf-8")
root = Path(__file__).resolve().parents[1] / "data" / "translation-catalog"
shard_path = root / "shards" / "batch_025.json"
shard = json.loads(shard_path.read_text(encoding="utf-8"))
batch = json.loads((root / "batches" / "batch_025.json").read_text(encoding="utf-8"))["strings"]

TEMPLATE_RE = re.compile(r"\$\{[^{}]+\}")


def fix_translation(english: str, translated: str) -> str:
    """Restore any ${...} templates from English key into translation."""
    templates = TEMPLATE_RE.findall(english)
    if not templates:
        return translated
    result = translated
    for tpl in templates:
        if tpl not in result:
            # Argos often leaves a mangled fragment; rebuild by splicing template
            # into the position matching the English surrounding context.
            pass
    return result


# Manual fixes: translate body text, keep exact English keys and ${...} blocks.
FIXES: dict[str, dict[str, str]] = {}

# Find keys with templates
template_keys = [s for s in batch if "${if" in s]
print(f"Found {len(template_keys)} keys with if-templates")

for key in template_keys:
    print(f"\nKey preview: {key[:120]}...")
    for lang in ("he", "es", "fr", "ru"):
        t = shard[lang][key]
        for tpl in TEMPLATE_RE.findall(key):
            if tpl not in t:
                print(f"  {lang}: MISSING {tpl[:60]}...")
