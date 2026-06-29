import json
from pathlib import Path

base = Path(r"c:\apps\hehehe\be-a-tzaddik\data\translation-catalog\human")
checks = [
    ("local_005_es_only.json", "es", 25),
    ("local_005_fr_only.json", "fr", 25),
    ("local_005_ru_only.json", "ru", 25),
    ("local_006_he_only.json", "he", 18),
    ("local_006_es_only.json", "es", 18),
    ("local_006_fr_only.json", "fr", 18),
    ("local_006_ru_only.json", "ru", 18),
]
all_ok = True
for fname, key, expected in checks:
    n = len(json.loads((base / fname).read_text(encoding="utf-8"))[key])
    ok = n == expected
    all_ok = all_ok and ok
    print(f"{fname}: {n}/{expected} {'OK' if ok else 'FAIL'}")
print("ALL OK" if all_ok else "ISSUES")
