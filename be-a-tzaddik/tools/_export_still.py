import json, importlib, pkgutil
from pathlib import Path
import overlay_parts as pkg
merged = {}
for modinfo in pkgutil.iter_modules(pkg.__path__, pkg.__name__ + "."):
    if modinfo.name.endswith(".__init__"):
        continue
    mod = importlib.import_module(modinfo.name)
    for attr in ("T", "PART", "P2", "P3", "P4", "DATA"):
        b = getattr(mod, attr, None)
        if isinstance(b, dict):
            merged.update(b)
needs = json.loads(Path(r"c:\apps\hehehe\be-a-tzaddik\data\translation-catalog\_needs_007_013.json").read_text(encoding="utf-8"))
missing = [s for s in needs if s not in merged]
Path(r"c:\apps\hehehe\be-a-tzaddik\tools\_still_missing.json").write_text(json.dumps(missing, ensure_ascii=False, indent=2), encoding="utf-8")
print(len(missing))
