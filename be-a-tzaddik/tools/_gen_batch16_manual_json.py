#!/usr/bin/env python3
"""Generate _ru_batch16_manual.json — explicit key→RU for batch 16."""
from __future__ import annotations

import json
from pathlib import Path

KEYS_PATH = Path(__file__).parent / "_batch16_keys.json"
OUT = Path(__file__).parent / "_ru_batch16_manual.json"

MANUAL: dict[str, str] = {
    "birchat hamapil": "биркат hамапиль",
    "bitachon": "битахон",
    "bitul": "битуль",
    "borei nefashot": "борей нефашот",
    "bracha acharona": "браха ахарона",
    "charoset": "харосет",
    "chatzos": "хацот",
    "chazeret": "хазерет",
    "chesed": "хесед",
    "cheshbon hanefesh": "хешбон hанефеш",
    "chiyuv": "хиюв",
    "chutz la'aretz": "хуц ла-арец",
    "eruv": "эрув",
    "etrog": "этрог",
    "fleishig": "фляйшиг",
    "gabbai": "габбай",
    "gemach": "гемах",
    "gemilut chasadim": "гемилут хасадим",
    "geneivat da'at": "гневат даат",
    "hadas": "хадас",
    "hakarat hatov": "hакарат hатов",
    "halachot": "hалахот",
    "hechsher": "hехшер",
    "karmelit": "кармелит",
    "karpas": "карпас",
    "kehilla": "кехилла",
    "kezayit": "кезаит",
    "kitniyot": "китнийот",
    "kneidlach": "кнейдлах",
    "l'chatchila": "ле-хатхила",
    "levi": "леви",
    "lulav": "лулав",
    "maaser kesafim": "маасер кесафим",
    "machloket": "махлокет",
    "machzor": "махзор",
    "maror": "марор",
    "matanot la'evyonim": "матанот ла-эвьоним",
    "mayim achronim": "маим ахроним",
    "mechirat chametz": "мехират хамец",
    "mechitza": "мехица",
    "melachot": "мелахот",
    "melicha": "мелиха",
    "menorah": "менора",
    "mesorah": "месора",
    "mezuzah": "мезуза",
    "mikveh": "микве",
    "milchig": "мильхиг",
    "minyan": "миньян",
    "misheyakir": "мишейакир",
    "nefesh": "нефеш",
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
