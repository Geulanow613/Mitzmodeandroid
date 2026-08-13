# -*- coding: utf-8 -*-
import json
import re
import sys
from pathlib import Path

ROOT = Path(r"c:\apps\hehehe\be-a-tzaddik")
OUT = ROOT / "data/translation-catalog/human/mitzvot_002_he_only.json"
SRC = ROOT / "tools/_write_mitzvot_002_he.py"
TAIL19 = ROOT / "tools/_tail_he.json"

sys.path.insert(0, str(ROOT / "tools"))
from emit_tail import TAIL  # noqa: E402

LATIN = re.compile(r"[A-Za-z]{2,}")

text = SRC.read_text(encoding="utf-8")
start = text.index("he = [")
end = text.index("\n]\n\nprint")
he = eval(text[start:end + 2].split("=", 1)[1].strip())[:18]
e19 = json.loads(TAIL19.read_text(encoding="utf-8"))[0]

all_he = he + [e19] + TAIL
assert len(all_he) == 25, len(all_he)

for i, s in enumerate(all_he):
    assert s.strip(), f"empty at {i}"
    hits = LATIN.findall(s)
    assert not hits, f"latin at {i}: {hits}"

OUT.write_text(
    json.dumps({"he": all_he}, ensure_ascii=False, indent=2) + "\n",
    encoding="utf-8",
)
print(f"Wrote {OUT} with {len(all_he)} entries")
