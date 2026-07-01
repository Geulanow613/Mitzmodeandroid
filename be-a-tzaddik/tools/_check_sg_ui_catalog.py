#!/usr/bin/env python3
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
kt = (ROOT / "shared/src/commonMain/kotlin/com/beardytop/beatzaddik/ui/screens/ShabbatGuideScreen.kt").read_text(
    encoding="utf-8"
)
catalog = json.loads((ROOT / "data/translation-catalog/strings.json").read_text(encoding="utf-8"))
s = set(catalog["strings"])
patterns = [
    r'AppText\(\s*"([^"]+)"',
    r'HubSectionLabel\("([^"]+)"\)',
    r'HubCard\(\s*title = "([^"]+)"',
    r'subtitle = "([^"]+)"',
    r'SubHeading\("([^"]+)"\)',
    r'BodyCard\(\s*"([^"]+)"',
    r'BodyCard\(\s*\n\s*"([^"]+)"',
]
found: set[str] = set()
for p in patterns:
    found.update(re.findall(p, kt))
missing = [x for x in sorted(found) if x not in s]
print("UI strings in screen:", len(found))
print("missing:", len(missing))
for m in missing:
    print(repr(m))
