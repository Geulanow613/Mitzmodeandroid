#!/usr/bin/env python3
"""Scan FR Melacha essays for Argos artifacts."""

from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CAT = json.loads((ROOT / "data/translation-catalog/strings.json").read_text(encoding="utf-8"))["strings"]
FR = json.loads(
    (ROOT / "shared/src/commonMain/composeResources/files/translations/fr.json").read_text(encoding="utf-8")
)["entries"]

# Known bad substrings from prior batches + common Argos FR glue
BAD_FRAGMENTS = [
    "crea mangé",
    "trèsdes",
    "visage qui",
    "Melacha de Corée",
    "s'adapter aux bases",
    " ceasing ",
    " carrying ",
    " the ",
    " and ",
    " with ",
    "Saturday",
    "Learn more!",
    "En savoir plus!",
    "pop-up",
    "DIY",
    "six pieds",
    "balle de golf",
    "pain de main",
    "pains de main",
    "coudées",
    "vice versa",
    "pop-up",
    "canopie",
    "tent/canopie",
    "verrière pop-up",
    "Toutes les unités sont des problèmes",
    "forment un. Toutes",
    "Moché",
    "Hachem",
    "Chabbat !",
    "Chabbat.",
    "Shabbat",
    "melakha",
    "melacha",
    "poskim",
    "poskim",
    "halakhique",
    "halakha",
    "bracha",
    "brachot",
    "minhag",
    "muktzeh",
    "eiruv",
    "karmelit",
    "reshut",
    "Ohel",
    "Koraya",
    "Koreah",
    "Boneh",
    "Hotza'ah",
    "Mechateich",
    "Mav'ir",
    "Me'abeid",
    "Molei'ach",
    "Memachek",
    "Menapetz",
    "Makeh B'patish",
    "Matir",
    "Koshair",
    "Mechabeh",
    "Mafshit",
    "Koteiv",
    "Melaben",
    "Gozez",
]

# English leak detector
EN = re.compile(
    r"\b(the|and|with|when|that|this|you|your|before|after|means|Saturday|Sunday|Monday|people|because|however|while|where|which|their|them|these|those|into|from|over|under|between|through|during|without|within|against|among|around|behind|below|above|across|afterward|already|always|another|anyone|anything|anywhere|became|become|becomes|became|being|both|could|didn't|doesn't|don't|every|everyone|everything|everywhere|first|found|getting|going|hadn't|hasn't|haven't|having|he's|here's|herself|himself|however|i'm|i've|isn't|it's|itself|just|keep|know|later|let's|might|must|myself|never|nothing|ourselves|perhaps|rather|really|said|says|seems|she's|should|since|some|someone|something|sometimes|somewhere|still|such|than|that|that's|their|theirs|them|themselves|then|there|there's|these|they|they're|they've|thing|things|think|this|those|though|through|today|together|told|too|took|try|trying|under|until|upon|used|using|very|want|wanted|wasn't|we're|we've|were|weren't|what|what's|when|when's|where|where's|which|while|who|who's|whom|why|why's|will|with|within|without|won't|would|wouldn't|you're|you've|your|yours|yourself|yourselves)\b",
    re.I,
)

keys = [k for k in CAT if k.startswith("Learn about the Melacha of")]
issues: list[tuple[str, list[str], str]] = []

for k in keys:
    v = FR.get(k, "")
    hits: list[str] = []
    for frag in BAD_FRAGMENTS:
        if frag in v:
            hits.append(frag)
    en = len(EN.findall(v))
    # glued Latin inside French words
    if re.search(r"[a-zàâäéèêëïîôùûü]{2,}[A-Z][a-z]", v):
        hits.append("camelGlue")
    if re.search(r"[a-z][.!?][A-Z]", v):
        hits.append("dotGlue")
    if en >= 5:
        hits.append(f"enWords={en}")
    if hits:
        short = k.replace("Learn about the Melacha of ", "")[:40]
        issues.append((short, hits, v))

print(f"Melacha keys: {len(keys)}, with issues: {len(issues)}\n")
for short, hits, v in sorted(issues, key=lambda x: -len(x[1])):
    print(f"=== {short} ===")
    print(f"  hits: {hits[:8]}")
    # show suspicious snippets
    for m in re.finditer(r".{0,30}(the |and |crea |trèsdes|visage|Toutes les unités|pop-up|Saturday|six pieds|pain de main|\.[A-Z]| ceasing | carrying ).{0,40}", v, re.I):
        print(f"  ...{m.group()}...")
    print()
