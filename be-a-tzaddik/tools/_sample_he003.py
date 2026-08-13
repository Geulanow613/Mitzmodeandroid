# -*- coding: utf-8 -*-
import json
from pathlib import Path

path = Path(__file__).resolve().parents[1] / "data/translation-catalog/human/mitzvot_003_he_only.json"
he = json.loads(path.read_text(encoding="utf-8"))["he"]
out = Path(__file__).resolve().parent / "_sample_strings.txt"
parts = []
for i in [11, 12, 14, 18, 24]:
    parts.append(f"=== STRING {i+1} ===")
    parts.append(he[i])
    parts.append("")
out.write_text("\n".join(parts), encoding="utf-8")
