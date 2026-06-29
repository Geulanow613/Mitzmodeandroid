import json, re
from pathlib import Path
src = Path(r"c:\apps\hehehe\be-a-tzaddik\tools\gen_overlay_007_013.py").read_text(encoding="utf-8")
m = re.search(r"T: dict\[str, dict\[str, str\]\] = \{(.*)\}\n# fmt: on", src, re.S)
block = m.group(1)
keys = set(re.findall(r'^"((?:[^"\\]|\\.)*)":', block, re.M))
needs = json.loads(Path(r"c:\apps\hehehe\be-a-tzaddik\data\translation-catalog\_needs_007_013.json").read_text(encoding="utf-8"))
missing = [s for s in needs if s not in keys]
print("current keys:", len(keys))
print("missing:", len(missing))
for s in missing[:5]:
    print(" -", repr(s)[:80])
