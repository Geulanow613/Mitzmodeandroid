#!/usr/bin/env python3
"""Dump corrupt HE keys to JSON for translation work."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
he = json.loads(
    (ROOT / "shared/src/commonMain/composeResources/files/translations/he.json").read_text(
        encoding="utf-8"
    )
)["entries"]

KEYS = [
    "Before you open a book — remember what Torah study is:",
    "Torah learning bonds you with Hashem — the sages teach",
    "A series of short blessings said at the beginning",
    "Building a sukkah (סֻכָּה) is a mitzvah",
    "Hadlakat Nerot is lighting candles",
    "Hadlakat Nerot — Hadlakat Nerot",
    "Learn about the mitzvah of Hadlakat Nerot",
    "Lighting candles on Friday afternoon",
    "Many poskim and communities hold that women should say Birchot HaShachar",
    "Mikra Megillah (hearing the Book of Esther)",
    "Musaf is an additional Amidah",
    "The Shema is the central declaration",
    "The Torah commands: \"Be fruitful and multiply\"",
    "Why Shnayim Mikra is worth the effort",
]

out = {}
for k in he:
    for prefix in KEYS:
        if k.startswith(prefix) or prefix in k[:80]:
            out[k] = {"en_key_len": len(k), "he_val_preview": he[k][:300]}
            break

Path(__file__).parent.joinpath("_he_keys_dump.json").write_text(
    json.dumps(list(out.keys()), ensure_ascii=False, indent=2), encoding="utf-8"
)
print(f"Wrote {len(out)} keys")
