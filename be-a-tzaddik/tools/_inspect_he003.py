# -*- coding: utf-8 -*-
import json
import re
from pathlib import Path

CORR = re.compile(
    r"(?<=[\u0590-\u05FF])[a-zA-Z]{2,}"
    r"|(?<=[a-zA-Z])[\u0590-\u05FF]{1,3}(?=[a-zA-Z])"
    r"|(?<=[\u0590-\u05FF])[a-z](?=[\u0590-\u05FF])"
)

path = Path(__file__).resolve().parents[1] / "data/translation-catalog/human/mitzvot_003_he_only.json"
he = json.loads(path.read_text(encoding="utf-8"))["he"]
lines = [f"total={len(he)}"]
for i, s in enumerate(he):
    bad = CORR.findall(s)
    fam = "fam" in s
    mk = "מקור:" in s
    lines.append(f"{i+1}: len={len(s)} bad={bad!r} fam={fam} source={mk}")
    if bad:
        lines.append(f"  start: {s[:100]!r}")

out = Path(__file__).resolve().parent / "_inspect_report.txt"
out.write_text("\n".join(lines), encoding="utf-8")
print(out)
