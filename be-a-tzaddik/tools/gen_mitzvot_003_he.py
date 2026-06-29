# -*- coding: utf-8 -*-
import json
from pathlib import Path

OUT = Path(__file__).resolve().parents[1] / "data/translation-catalog/human/mitzvot_003_he_only.json"

# Strings 1-5 already validated in file; strings 6-25 below
he = json.loads((Path(__file__).parent / "_he_003_part1.json").read_text(encoding="utf-8"))["he"]
he.extend(json.loads((Path(__file__).parent / "_he_003_part2.json").read_text(encoding="utf-8"))["he"])

assert len(he) == 25, f"Expected 25 strings, got {len(he)}"
OUT.write_text(json.dumps({"he": he}, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
print(f"Wrote {len(he)} strings to {OUT}")
