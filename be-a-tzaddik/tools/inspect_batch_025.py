#!/usr/bin/env python3
import json
import re
import sys
from pathlib import Path

sys.stdout.reconfigure(encoding="utf-8")
root = Path(__file__).resolve().parents[1] / "data" / "translation-catalog"
shard = json.loads((root / "shards" / "batch_025.json").read_text(encoding="utf-8"))
batch = json.loads((root / "batches" / "batch_025.json").read_text(encoding="utf-8"))["strings"]
TEMPLATE_RE = re.compile(r"(\$\{[^{}]+\})")

for key in batch:
    if "${if" not in key:
        continue
    parts = TEMPLATE_RE.split(key)
    # parts alternates: text, template, text, ...
    tpl = TEMPLATE_RE.search(key).group(1)
    before = key.split(tpl)[0]
    after = key.split(tpl)[1]
    print("KEY before end:", before[-100:])
    print("KEY after start:", after[:100])
    for lang in ("he", "es", "fr", "ru"):
        t = shard[lang][key]
        # try find translated 'after' fragment start
        for marker in ["Torah & tefillah", "Torah", "After lighting", "First night: Kiddush"]:
            if marker in after:
                # search approximate in translation
                pass
        print(f"\n{lang} snippet around meals:")
        for m in ["festive meals", "ארוחות חג", "comidas festivas", "repas festifs", "праздничн"]:
            p = t.find(m)
            if p >= 0:
                print(t[p:p+250])
                break
        else:
            print(t[400:700])
