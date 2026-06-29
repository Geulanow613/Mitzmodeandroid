import json
from pathlib import Path
import importlib, pkgutil

ROOT = Path(r"c:\apps\hehehe\be-a-tzaddik")
needs = json.loads((ROOT / "data/translation-catalog/_needs_007_013.json").read_text(encoding="utf-8"))
import overlay_parts as pkg
merged = {}
for modinfo in pkgutil.iter_modules(pkg.__path__, "overlay_parts."):
    if modinfo.name.endswith(".__init__"): continue
    mod = importlib.import_module(modinfo.name)
    for attr in ("T","PART","P2","P3","P4","DATA","REST"):
        b = getattr(mod, attr, None)
        if isinstance(b, dict): merged.update(b)
missing = [s for s in needs if s not in merged]
bundled = {}
for lang in ("he","es","fr","ru"):
    bundled[lang] = json.loads((ROOT / f"data/bundled-translations/{lang}.json").read_text(encoding="utf-8"))["entries"]
can_use = 0
for s in missing:
    ok = all(s in bundled[l] and bundled[l][s] != s for l in ("he","es","fr","ru"))
    if ok: can_use += 1
print("missing", len(missing), "bundled usable", can_use)
