# -*- coding: utf-8 -*-
import json
import re
from pathlib import Path

base = Path(r"c:\apps\hehehe\be-a-tzaddik\data\translation-catalog\human")
keys = json.load(open(r"c:\apps\hehehe\be-a-tzaddik\data\translation-catalog\_keys_local_003.json", encoding="utf-8"))

he_path = base / "local_003_he_only.json"
d = json.load(open(he_path, encoding="utf-8"))
subs = [
    ("Hakhel", "הקהל"),
    ("הHakhel", "ההקהל"),
    ("ההakhel", "ההקהל"),
    ("הakhel", "הקהל"),
    ("gathering", ""),
    ("מini", "מיני"),
    ("m ini", "mini"),
    ("chumash", "חומש"),
    ("חumash", "חומש"),
    ("yehudi", "יהודי"),
    ("yehudit", "יהודית"),
    ("יהudi", "יהודי"),
    ("\u05d9ehudi", "\u05d9\u05d4\u05d5\u05d3\u05d9"),
    ("\u05d9ehudit", "\u05d9\u05d4\u05d5\u05d3\u05d9\u05ea"),
    ("\u05d7umash", "\u05d7\u05d5\u05de\u05e9"),
    ("tanach", 'תנ"ך'),
    ("ע busy", "עסוקים"),
    ("gברים", "גברים"),
]
for i, s in enumerate(d["he"]):
    for a, b in subs:
        s = s.replace(a, b)
    s = re.sub(r"  +", " ", s)
    d["he"][i] = s
with he_path.open("w", encoding="utf-8") as f:
    json.dump(d, f, ensure_ascii=False, indent=2)
    f.write("\n")

ru_path = base / "local_003_ru_only.json"
ru = json.load(open(ru_path, encoding="utf-8"))
for i, s in enumerate(ru["ru"]):
    ru["ru"][i] = s.replace("talmid chacham", "talmid chacham")
with ru_path.open("w", encoding="utf-8") as f:
    json.dump(ru, f, ensure_ascii=False, indent=2)
    f.write("\n")

for lang in ["he", "es", "fr", "ru"]:
    p = base / f"local_003_{lang}_only.json"
    data = json.load(open(p, encoding="utf-8"))
    assert len(data[lang]) == 25, f"{lang}: {len(data[lang])}"
    assert list(data.keys()) == [lang]

print("All 4 files OK:", len(keys), "keys")
