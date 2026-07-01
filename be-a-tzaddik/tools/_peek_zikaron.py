#!/usr/bin/env python3
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
key = [
    x
    for x in json.load(open(ROOT / "data/translation-catalog/strings.json", encoding="utf-8"))["strings"]
    if x.startswith("Yom HaZikaron (4 Iyar) is Israel")
][0]
for lang in ("es", "fr", "ru", "he"):
    v = json.load(
        open(ROOT / f"shared/src/commonMain/composeResources/files/translations/{lang}.json", encoding="utf-8")
    )["entries"][key]
    i = v.find("KosherJava")
    if i >= 0:
        print(lang, v[max(0, i - 40) : i + 120])
    else:
        print(lang, "no KosherJava")
