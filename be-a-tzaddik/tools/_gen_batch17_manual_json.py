#!/usr/bin/env python3
"""Generate _ru_batch17_manual.json — explicit key→RU for batch 17 (final guillemet sweep)."""
from __future__ import annotations

import json
from pathlib import Path

KEYS_PATH = Path(__file__).parent / "_batch17_keys.json"
OUT = Path(__file__).parent / "_ru_batch17_manual.json"

MANUAL: dict[str, str] = {
    "neshama": "нешама",
    "neshama yeteira": "нешама йетейра",
    "netilat yadayim": "нетилат ядаим",
    "niddah": "нида",
    "ona'ah": "онаа",
    "ona'at devarim": "онаат деварим",
    "parsha": "парша",
    "patur": "патур",
    "pitom": "питом",
    "posek": "поск",
    "psak": "псак",
    "pshat": "пшат",
    "rechilut": "рехилут",
    "refuah shleimah": "рефуа шлема",
    "revi'it": "ревиит",
    "shinui": "шинуи",
    "shloshim": "шлошим",
    "shmirat halashon": "шмират hалашон",
    "shmurah matzah": "шмура маца",
    "shochet": "шохет",
    "shul": "шул",
    "siddur": "сидур",
    "siyum": "сиум",
    "sofer": "софер",
    "tallit gadol": "талит гадоль",
    "tallit katan": "талит катан",
    "tefillah": "тфилла",
    "teshuvah": "тешува",
    "treif": "трейф",
    "trop": "троп",
    "tumah": "тума",
    "tzedakah": "цдака",
    "tzitzit": "цицит",
    "yad soledet bo": "яд соледет бо",
    "zeroa": "цроа",
    "zmanim": "зманим",
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
