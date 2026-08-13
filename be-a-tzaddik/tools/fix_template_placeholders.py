#!/usr/bin/env python3
import json
import re
import sys
from pathlib import Path

sys.stdout.reconfigure(encoding="utf-8")
root = Path(__file__).resolve().parents[1] / "data" / "translation-catalog"
LANGS = ("he", "es", "fr", "ru")
TEMPLATE_RE = re.compile(r"\$\{[^{}]+\}")
PH_RE = re.compile(r"__?PH\d+__?|\s+PH\d+\s+")


def fix_shard(n: int) -> int:
    batch_path = root / "batches" / f"batch_{n:03d}.json"
    shard_path = root / "shards" / f"batch_{n:03d}.json"
    batch = json.loads(batch_path.read_text(encoding="utf-8"))["strings"]
    shard = json.loads(shard_path.read_text(encoding="utf-8"))
    fixes = 0

    for key in batch:
        templates = TEMPLATE_RE.findall(key)
        if not templates:
            continue
        for lang in LANGS:
            val = shard[lang][key]
            for tpl in templates:
                if tpl in val:
                    continue
                # restore from broken placeholder tokens
                new_val = val
                for pat in (r"__PH\d+__", r"\s*PH\d+\s*", r"PH\d+"):
                    if re.search(pat, new_val):
                        new_val = re.sub(pat, tpl, new_val, count=1)
                        break
                if new_val != val:
                    shard[lang][key] = new_val
                    fixes += 1
                    print(f"fixed batch_{n:03d} {lang}: inserted template")
                elif tpl not in shard[lang][key]:
                    print(f"WARN batch_{n:03d} {lang}: still missing template")

    if fixes:
        shard_path.write_text(
            json.dumps(shard, ensure_ascii=False, indent=2) + "\n",
            encoding="utf-8",
        )
    return fixes


total = sum(fix_shard(n) for n in (18, 19, 23, 24, 25))
print(f"Total fixes: {total}")
