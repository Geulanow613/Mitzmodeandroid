#!/usr/bin/env python3
"""Generate _ru_batch15_manual.json — explicit key→RU for batch 15."""
from __future__ import annotations

import json
from pathlib import Path

KEYS_PATH = Path(__file__).parent / "_batch15_keys.json"
OUT = Path(__file__).parent / "_ru_batch15_manual.json"

MANUAL: dict[str, str] = {
    "Mezonot": "мезонот",
    "Mishloach Manot": "мишлоах манот",
    "Mishnah Berurah": "мишна брура",
    "Modah": "мода",
    "Muktzeh": "мукце",
    "Musaf": "мусаф",
    "Mussar": "мусар",
    "Nachum Ish Gamzu": "нахум иш гамзу",
    "Nevi'im": "невиим",
    "Peninei Halakha": "пениней hалаха",
    "Purim Meshulash": "пурим мешугаш",
    "Rabbi": "раввин",
    "Rashi": "раши",
    "Retzei": "рецеи",
    "Seder": "седер",
    "Sefardim": "сефарды",
    "Sefirah": "сефира",
    "Selichot": "селихот",
    "Seudah": "сеуда",
    "Shamayim": "шамаим",
    "Shavuot": "шавуот",
    "Shemini Atzeret": "шемини ацерет",
    "Shemoneh Esrei": "шмоне эре",
    "Shemoneh Esrei/Tachanun": "шмоне эре/таханун",
    "Shir HaMaalot": "шир hа-маалот",
    "Shulchan Aruch": "шулхан арух",
    "Simchat Torah": "симхат тора",
    "Simchat Yom Tov": "симхат йом тов",
    "Sukkah": "сукка",
    "Taanit Bechorot": "таанит бехорот",
    "Tachanun": "таханун",
    "Tanach": "танах",
    "Tanya": "тания",
    "Tefilat Nedavah": "тфилат недава",
    "Vatodi'enu": "ватодиену",
    "Yaknehaz": "якнеhаз",
    "Yom Yerushalayim": "йом йерушалаим",
    "Zachor": "захор",
    "Zohar": "зоар",
    "aggadah": "агада",
    "alot hashachar": "алот hашахар",
    "aravah": "арава",
    "assur bemelacha": "асур бе-мелаха",
    "bedieved": "бедиевед",
    "bedikat chametz": "бедикат хамец",
    "beged": "бегед",
    "bein kodesh l'kodesh": "бейн кодеш ле-кодеш",
    "beitzah": "бейца",
    "bentcher": "бентчер",
    "besamim": "бесамим",
}


def main() -> None:
    keys: list[str] = json.loads(KEYS_PATH.read_text(encoding="utf-8"))
    missing = [k for k in keys if k not in MANUAL]
    extra = [k for k in MANUAL if k not in keys]
    if missing:
        raise SystemExit(f"missing ({len(missing)}): {missing[0]!r}")
    if extra:
        raise SystemExit(f"extra ({len(extra)}): {extra[0]!r}")
    manual = {k: MANUAL[k] for k in keys}
    OUT.write_text(json.dumps(manual, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"Wrote {len(manual)} entries to {OUT.name}")


if __name__ == "__main__":
    main()
