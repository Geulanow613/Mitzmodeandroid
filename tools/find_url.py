from pathlib import Path
import re
ROOT = Path(r"c:\apps\hehehe\be-a-tzaddik")
URL_RE = re.compile(r"https?://[^\s\"')\]>]+")
for p in ROOT.rglob("*"):
    if p.suffix not in {".kt", ".json", ".md"}:
        continue
    if "ios-transfer-handoff" in str(p):
        continue
    t = p.read_text(encoding="utf-8", errors="ignore")
    for m in URL_RE.findall(t):
        if "need_sleep" in m or "${" in m or m == "https://":
            print(p.relative_to(ROOT.parent), m)
