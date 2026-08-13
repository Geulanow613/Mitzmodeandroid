import json
from pathlib import Path

src = Path(r"c:\apps\hehehe\be-a-tzaddik\tools\gen_overlay_007_013.py").read_text(encoding="utf-8")
ns: dict = {"__file__": str(Path(r"c:\apps\hehehe\be-a-tzaddik\tools\gen_overlay_007_013.py"))}
exec(src.split("def main")[0], ns)
T = ns["T"]
needs = json.loads(
    Path(r"c:\apps\hehehe\be-a-tzaddik\data\translation-catalog\_needs_007_013.json").read_text(encoding="utf-8")
)
missing = [s for s in needs if s not in T]
Path(r"c:\apps\hehehe\be-a-tzaddik\tools\_missing.json").write_text(
    json.dumps(missing, ensure_ascii=False, indent=2), encoding="utf-8"
)
print(len(missing))
