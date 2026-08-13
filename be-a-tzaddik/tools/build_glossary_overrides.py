#!/usr/bin/env python3
"""Append glossary + halacha HE/RU fixes to quality_overrides."""
from __future__ import annotations

import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
COMPOSE = ROOT / "shared/src/commonMain/composeResources/files/translations"
OUT = ROOT / "data/translation-catalog/human/glossary_overrides.json"

HE_FIXES = {
    "The very first act of each day:": (
        "המעשה הראשון בכל יום:\n\n"
        "מה זה:\n"
        "מודה אני (מוֹדֶה אֲנִי — \"אני מודה\") הוא תפילת הודיה קצרה שאומרים מיד עם ההקיצה, "
        "לפני שקמים מהמיטה. מודים לה' על החזרת הנשמה.\n\n"
        "הנוסח:\n"
        "\"מודה אני לפניך מלך חי וקיים, שהחזרת בי נשמתי בחמלה — רבה אמונתך.\"\n\n"
        "אחר כך: נטילת ידיים, לפי מנהג."
    ),
}

RU_FIXES = {
    "Chazal (חז\"ל) is an acronym for \"our Sages of blessed memory\"": (
        "Хазал (חז\"ל) — аббревиатура «наши мудрецы, да будет благословенна их память» — "
        "раввины Мишны, Тalmуда и midrasha, передавшие halakhu и ценности. "
        "Когда источник говорит «Хazal учат», речь о коллективной традиции, а не об одном авторе."
    ),
    "Bitachon is trust in G-d": (
        "Битахон — доверие к В-у: вера, что Он даёт необходимое и что трудности могут иметь смысл, "
        "даже когда мы этого не видим. Связано с emunah, но подчёркивает спокойствие в повседневных заботах. "
        "Мусar и Hasidut учат практическому битахону."
    ),
    "Kallah means bride": (
        "Кала означает невесту; занятия для невест учат taharat hamishpacha, домашним mitzvot и подготовке к браку."
    ),
    "Take a minute to pray for someone else's needs before your own": (
        "Уделите минуту молитве о нуждах других прежде своих. Мудрецы учат: когда молитесь за друга, "
        "ваши собственные просьбы часто исполняются первыми."
    ),
}


def main() -> None:
    he: dict[str, str] = {}
    ru: dict[str, str] = {}
    catalog = json.loads((ROOT / "data/translation-catalog/strings.json").read_text(encoding="utf-8"))[
        "strings"
    ]
    for s in catalog:
        for prefix, tr in HE_FIXES.items():
            if s.startswith(prefix) and prefix == "The very first act of each day:":
                he[s] = tr
        for prefix, tr in RU_FIXES.items():
            if s.startswith(prefix):
                ru[s] = tr if not s.startswith("Chazal —") and not s.startswith("kallah —") else (
                    f"{s.split(' — ')[0]} — {tr}" if " — " in s else tr
                )
    # prefix keys for Chazal/Bitachon/kallah variants
    for s in catalog:
        if s.startswith("Chazal —"):
            base = s.split(" — ", 1)[1]
            for k, v in list(ru.items()):
                if base.startswith(k[:40]):
                    ru[s] = f"Chazal — {v}"
        if s.startswith("kallah —"):
            ru[s] = f"kallah — {RU_FIXES['Kallah means bride']}"
        if s.startswith("Bitachon —"):
            ru[s] = f"Bitachon — {RU_FIXES['Bitachon is trust in G-d']}"

    payload = {"he": he, "ru": ru}
    OUT.write_text(json.dumps(payload, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"he={len(he)} ru={len(ru)} -> {OUT.name}")


if __name__ == "__main__":
    main()
