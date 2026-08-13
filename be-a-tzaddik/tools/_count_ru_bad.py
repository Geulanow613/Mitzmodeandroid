#!/usr/bin/env python3
import json
import re
from pathlib import Path

ru = json.loads(
    (Path(__file__).resolve().parents[1] / "shared/src/commonMain/composeResources/files/translations/ru.json").read_text(
        encoding="utf-8"
    )
)["entries"]
pat = re.compile(r"[a-zA-Z]{4,}[а-яА-ЯёЁ]|[а-яА-ЯёЁ][a-zA-Z]{5,}")
hits = [(len(k), k, v) for k, v in ru.items() if pat.search(v)]
hits.sort(key=lambda x: -x[0])
print(f"total={len(hits)}")
for ln, k, v in hits:
    m = pat.search(v)
    frag = v[max(0, m.start() - 15) : m.end() + 40] if m else v[:80]
    print(f"\n[{ln}] {k[:90]}")
    print(f"  ...{frag}...")
