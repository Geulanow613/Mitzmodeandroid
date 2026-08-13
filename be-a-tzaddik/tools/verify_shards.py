#!/usr/bin/env python3
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1] / "data" / "translation-catalog"
LANGS = ("he", "es", "fr", "ru")
BAD = re.compile(r"ZZZP|⟦|mithhhh")

grand = 0
for i in range(14, 20):
    p = ROOT / "shards" / f"batch_{i:03d}.json"
    d = json.loads(p.read_text(encoding="utf-8"))
    n = len(d["he"])
    grand += n
    broken = sum(1 for lang in LANGS for v in d[lang].values() if BAD.search(v))
    var_keys = [k for k in d["he"] if "$" in k or "${" in k]
    var_ok = all(
        all(x in d[lang][k] for x in re.findall(r"\$\{[^}]+\}|\$[A-Za-z_][A-Za-z0-9_]*", k))
        for k in var_keys for lang in LANGS
    )
    print(f"batch_{i:03d}: {n} keys/lang | broken={broken} | vars_ok={var_ok}")
print(f"TOTAL strings: {grand}")
print(f"TOTAL translations: {grand * len(LANGS)}")
