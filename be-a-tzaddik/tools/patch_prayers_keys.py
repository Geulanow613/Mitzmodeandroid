#!/usr/bin/env python3
"""Patch exact-catalog prayer keys missing from prayers_liturgy.json."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
req = json.loads((ROOT / "data/translation-catalog/strings.json").read_text(encoding="utf-8"))[
    "strings"
]
path = ROOT / "data/translation-catalog/human/prayers_liturgy.json"
pr = json.loads(path.read_text(encoding="utf-8"))

yaaleh = next(s for s in req if "ביום ראש החדש" in s)
if yaaleh not in pr["es"]:
    pr["he"][yaaleh] = yaaleh
    pr["es"][yaaleh] = (
        "En este día de Rosh Jodesh\n"
        "            En este día de Jagué HaMatzot\n"
        "            En este día de Jagué HaSucot"
    )
    pr["fr"][yaaleh] = (
        "En ce jour de Roch Hodech\n"
        "            En ce jour de Hag HaMatzot\n"
        "            En ce jour de Hag HaSoukot"
    )
    pr["ru"][yaaleh] = (
        "В этот день Рош Ходеш\n"
        "            В этот день Хаг а-Мацот\n"
        "            В этот день Хаг а-Суккот"
    )
    print("patched yaaleh")

had = next((s for s in req if "יְהִי רָצוֹן" in s), None)
if had and had not in pr["es"]:
    pr["he"][had] = had
    pr["es"][had] = (
        "Que sea Tu voluntad ante Ti, Hashem nuestro Dios y Dios de nuestros padres, "
        "que nos conduzcas en paz y nos hagas caminar en paz, y nos hagas llegar en paz "
        "al lugar al que deseamos ir, y nos devuelvas en paz a nuestra casa, y rescatesnos "
        "de mano de todo enemigo y emboscada en el camino."
    )
    pr["fr"][had] = (
        "Qu'il Te plaise, Éternel notre Dieu et Dieu de nos pères, de nous conduire en paix "
        "et de nous faire marcher en paix, et de nous faire arriver en paix au lieu où nous "
        "désirons aller, et de nous ramener en paix chez nous, et de nous sauver de la main "
        "de tout ennemi et embuscade sur la route."
    )
    pr["ru"][had] = (
        "Да будет угодно пред Тобой, Г-споди наш Б-г и Б-г отцов наших, вести нас в мире "
        "и заставить идти нас в мире, и доставить нас в мире к месту, куда мы желаем идти, "
        "и возвратить нас в мире домой, и спасти нас от руки всякого врага и засады на пути."
    )
    print("patched haderech")

path.write_text(json.dumps(pr, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
print("done")
