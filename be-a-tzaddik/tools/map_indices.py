import json
from pathlib import Path
p = Path(r"c:\apps\hehehe\be-a-tzaddik\data\translation-catalog\human\he_fix_013_he_only.json")
d = json.load(open(p, encoding="utf-8"))
out = Path(r"c:\apps\hehehe\be-a-tzaddik\tools\_idx.txt")
out.write_text("\n".join(f"{i} {s[:60]}" for i, s in enumerate(d["he"])), encoding="utf-8")
