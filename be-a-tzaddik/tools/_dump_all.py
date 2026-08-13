# -*- coding: utf-8 -*-
import json
from pathlib import Path

path = Path(__file__).resolve().parents[1] / "data/translation-catalog/human/mitzvot_003_he_only.json"
he = json.loads(path.read_text(encoding="utf-8"))["he"]
out = Path(__file__).resolve().parent / "_all_new_strings.txt"
parts = []
for i in range(5, 25):
    parts.append(f"=== {i+1} ===")
    parts.append(he[i])
    parts.append("")
out.write_text("\n".join(parts), encoding="utf-8")
