#!/usr/bin/env python3
import json
from pathlib import Path
ROOT = Path(__file__).resolve().parents[1]
fr = json.load(open(ROOT / "shared/src/commonMain/composeResources/files/translations/fr.json", encoding="utf-8"))["entries"]
for prefix in ["Learn about the gid", "Learn about the rabbinic boundary of Muktzeh", "Pesach begins in about a week"]:
    for k,v in fr.items():
        if k.startswith(prefix):
            for frag in ["pasLes", "Kli SheMelakhto", "haPesach)", "KosherJava"]:
                if frag in v:
                    i = v.index(frag)
                    print(f"=== {frag} in {k[:60]}")
                    print(v[max(0,i-80):i+len(frag)+80])
                    print()
