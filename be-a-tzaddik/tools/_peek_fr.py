#!/usr/bin/env python3
import json
from pathlib import Path
ROOT = Path(__file__).resolve().parents[1]
fr = json.load(open(ROOT / "shared/src/commonMain/composeResources/files/translations/fr.json", encoding="utf-8"))["entries"]
for prefix in ["Learn about the gid", "Learn about the rabbinic boundary of Muktzeh", "Yom HaZikaron (4 Iyar)"]:
    for k,v in fr.items():
        if k.startswith(prefix):
            print("KEY:", k[:80])
            print("START:", v[:120])
            print("KosherJava" in v, "pasLes" in v, "Kli SheMelakhto" in v)
            print()
