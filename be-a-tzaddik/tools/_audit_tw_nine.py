#!/usr/bin/env python3
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
cat = json.load(open(ROOT / "data/translation-catalog/strings.json", encoding="utf-8"))["strings"]
fr = json.load(open(ROOT / "shared/src/commonMain/composeResources/files/translations/fr.json", encoding="utf-8"))["entries"]

for prefix in (
    "The Three Weeks (בין המצרים) from 17 Tammuz until Tisha B'Av commemorate",
    "The Nine Days (from 1 Av",
    "The Nine Days (from Rosh Chodesh",
    "The Nine Days and the week",
    "Shloshim is thirty",
    "shloshim — Shloshim",
):
    for k in cat:
        if k.startswith(prefix):
            v = fr.get(k, "?")
            bad = any(x in v for x in ["Three Weeks", "pasLes", "shloshim Н", "родителей.Kaddish", "Les Three"])
            print("BAD" if bad else "ok ", k[:75])
            if bad:
                print("   ", v[:120])
            print()
