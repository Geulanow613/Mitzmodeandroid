#!/usr/bin/env python3
import json
import re
from pathlib import Path

fr = json.loads(
    Path("../shared/src/commonMain/composeResources/files/translations/fr.json").read_text(
        encoding="utf-8"
    )
)["entries"]


def strip_quotes(s: str) -> str:
    return re.sub(r"«[^»]*»", "", s)


snips: set[str] = set()
for v in fr.values():
    sq = strip_quotes(str(v))
    for m in re.finditer(r".{0,30}\bvous\b.{0,30}", sq, re.I):
        s = m.group(0).strip()
        if "rendez-vous" in s.lower():
            continue
        snips.add(s)
    for m in re.finditer(r".{0,20}-vous\b.{0,20}", sq, re.I):
        snips.add(m.group(0).strip())

print("=== vous snippets ===")
for s in sorted(snips):
    print(s)
print("count", len(snips))

bad_pat = re.compile(
    r"Tu (n'avez|en avez|êtes-vous|éloignent|devriez|contentez|n'avez pas)",
    re.I,
)
bad = [k for k, v in fr.items() if bad_pat.search(v)]
print("broken tu:", len(bad))
for b in bad[:20]:
    print(" ", b[:70])
