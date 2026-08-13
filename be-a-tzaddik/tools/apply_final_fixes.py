#!/usr/bin/env python3
"""Apply hand-tuned translations for keys Argos cannot handle."""

from __future__ import annotations

import json
import subprocess
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
TOOLS = ROOT / "tools"
SHARDS = ROOT / "data" / "translation-catalog" / "shards"
OUT = SHARDS / "manual_fixes.json"

# English template strings → translations
EN_FIXES: dict[str, dict[str, str]] = {
    "Rav": {"he": "רב", "es": "Rav", "fr": "Rav", "ru": "Рав"},
    "www.beardy.top": {"he": "www.beardy.top", "es": "www.beardy.top", "fr": "www.beardy.top", "ru": "www.beardy.top"},
    "https://www.beardy.top": {
        "he": "https://www.beardy.top",
        "es": "https://www.beardy.top",
        "fr": "https://www.beardy.top",
        "ru": "https://www.beardy.top",
    },
    "$mitzvotCount": {"he": "$mitzvotCount", "es": "$mitzvotCount", "fr": "$mitzvotCount", "ru": "$mitzvotCount"},
    "\\s*/\\s*": {"he": "\\s*/\\s*", "es": "\\s*/\\s*", "fr": "\\s*/\\s*", "ru": "\\s*/\\s*"},
    "(?i)(?<![A-Za-z0-9'])${Regex.escape(word)}(?![A-Za-z0-9'])": {
        "he": "(?i)(?<![A-Za-z0-9'])${Regex.escape(word)}(?![A-Za-z0-9'])",
        "es": "(?i)(?<![A-Za-z0-9'])${Regex.escape(word)}(?![A-Za-z0-9'])",
        "fr": "(?i)(?<![A-Za-z0-9'])${Regex.escape(word)}(?![A-Za-z0-9'])",
        "ru": "(?i)(?<![A-Za-z0-9'])${Regex.escape(word)}(?![A-Za-z0-9'])",
    },
    "$translatedSwitchedTo $languageName.\n$translatedPleaseNote": {
        "he": "$translatedSwitchedTo $languageName.\n$translatedPleaseNote",
        "es": "$translatedSwitchedTo $languageName.\n$translatedPleaseNote",
        "fr": "$translatedSwitchedTo $languageName.\n$translatedPleaseNote",
        "ru": "$translatedSwitchedTo $languageName.\n$translatedPleaseNote",
    },
    "Pesach meets Shabbat this year — some steps happen today or tonight (before tomorrow's erev Pesach checklist):\n\n$body": {
        "he": "השנה פסח פוגש את שבת — חלק מההכנות מתבצעות היום או הלילה (לפני רשימת ערב פסח של מחר):\n\n$body",
        "es": "Este año Pesaj coincide con Shabat: algunos pasos son hoy o esta noche (antes de la lista de víspera de Pesaj de mañana):\n\n$body",
        "fr": "Cette année, Pessa'h coïncide avec Chabbat — certaines étapes ont lieu aujourd'hui ou ce soir (avant la liste de veille de Pessa'h de demain) :\n\n$body",
        "ru": "В этом году Песах совпадает с Шаббатом — часть подготовки выполняется сегодня или сегодня вечером (до списка накануне Песаха на завтра):\n\n$body",
    },
    "This year, Shabbat is Erev $chagName — $chagName begins tonight at nightfall (Motzei Shabbat). This doesn't happen every year.": {
        "he": "השנה שבת היא ערב $chagName — $chagName מתחיל הלילה עם צאת השבת. זה לא קורה בכל שנה.",
        "es": "Este año, Shabat es víspera de $chagName — $chagName comienza esta noche al anochecer (Motzei Shabat). No ocurre todos los años.",
        "fr": "Cette année, Chabbat est la veille de $chagName — $chagName commence ce soir à la sortie de Chabbat (Motzei Chabbat). Cela n'arrive pas chaque année.",
        "ru": "В этом году Шаббат — это канун $chagName; $chagName начинается сегодня вечером после выхода Шаббата (Моце Шабат). Так бывает не каждый год.",
    },
    "This year, Shabbat is Erev Rosh Hashana — Rosh Hashana begins tonight at nightfall (Motzei Shabbat). This doesn't happen every year.": {
        "he": "השנה שבת היא ערב ראש השנה — ראש השנה מתחיל הלילה עם צאת השבת. זה לא קורה בכל שנה.",
        "es": "Este año, Shabat es víspera de Rosh Hashaná — Rosh Hashaná comienza esta noche al anochecer (Motzei Shabat). No ocurre todos los años.",
        "fr": "Cette année, Chabbat est la veille de Roch Hachana — Roch Hachana commence ce soir à la sortie de Chabbat. Cela n'arrive pas chaque année.",
        "ru": "В этом году Шаббат — канун Рош а-Шана; Рош а-Шана начинается сегодня вечером после выхода Шаббата. Так бывает не каждый год.",
    },
    "This year, tomorrow (Shabbat) is Erev $chagName — $chagName begins tomorrow night at nightfall (Motzei Shabbat). This doesn't happen every year.": {
        "he": "השנה מחר (שבת) הוא ערב $chagName — $chagName מתחיל מחר בלילה עם צאת השבת. זה לא קורה בכל שנה.",
        "es": "Este año, mañana (Shabat) es víspera de $chagName — $chagName comienza mañana por la noche al anochecer (Motzei Shabat). No ocurre todos los años.",
        "fr": "Cette année, demain (Chabbat) est la veille de $chagName — $chagName commence demain soir à la sortie de Chabbat. Cela n'arrive pas chaque année.",
        "ru": "В этом году завтра (Шаббат) — канун $chagName; $chagName начинается завтра вечером после выхода Шаббата. Так бывает не каждый год.",
    },
    "This year, tomorrow (Shabbat) is Erev Rosh Hashana — Rosh Hashana begins tomorrow night at nightfall (Motzei Shabbat). This doesn't happen every year.": {
        "he": "השנה מחר (שבת) הוא ערב ראש השנה — ראש השנה מתחיל מחר בלילה עם צאת השבת. זה לא קורה בכל שנה.",
        "es": "Este año, mañana (Shabat) es víspera de Rosh Hashaná — Rosh Hashaná comienza mañana por la noche al anochecer (Motzei Shabat). No ocurre todos los años.",
        "fr": "Cette année, demain (Chabbat) est la veille de Roch Hachana — Roch Hachana commence demain soir à la sortie de Chabbat. Cela n'arrive pas chaque année.",
        "ru": "В этом году завтра (Шаббат) — канун Рош а-Шана; Рош а-Шана начинается завтра вечером после выхода Шаббата. Так бывает не каждый год.",
    },
    "Add Yaaleh V'yavo in the Mincha Amidah on Rosh Chodesh — in the blessing Retzei (Avodah).\n\n${yaalehVyavoForgotShacharitOrMincha(\"Mincha\")}": {
        "he": "הוסיפו יעלה ויבוא בעמידת מנחה בראש חודש — בברכת רצה (עבודה).\n\n${yaalehVyavoForgotShacharitOrMincha(\"Mincha\")}",
        "es": "Añade Yaaleh V'yavo en la Amidá de Minjá en Rosh Jodesh — en la bendición Retzei (Avodá).\n\n${yaalehVyavoForgotShacharitOrMincha(\"Mincha\")}",
        "fr": "Ajoutez Yaaleh V'yavo dans l'Amidah de Min'ha à Roch Hodech — dans la bénédiction Retzei (Avodah).\n\n${yaalehVyavoForgotShacharitOrMincha(\"Mincha\")}",
        "ru": "Добавьте Яале ве-яво в амиду Минхи в Рош Ходеш — в благословение Рице (Авода).\n\n${yaalehVyavoForgotShacharitOrMincha(\"Mincha\")}",
    },
    "${yaalehVyavoFemaleAmidahLead(\"Maariv\")}\n\n${yaalehVyavoForgotMaarivRoshChodesh()}\n\nIf you say Birkat Hamazon when you eat bread tonight, add Yaaleh V'yavo there too.": {
        "he": "${yaalehVyavoFemaleAmidahLead(\"Maariv\")}\n\n${yaalehVyavoForgotMaarivRoshChodesh()}\n\nאם אומרים ברכת המזון כשאוכלים לחם הלילה, יש להוסיף גם שם יעלה ויבוא.",
        "es": "${yaalehVyavoFemaleAmidahLead(\"Maariv\")}\n\n${yaalehVyavoForgotMaarivRoshChodesh()}\n\nSi recitas Birkat Hamazón al comer pan esta noche, añade también Yaaleh V'yavo allí.",
        "fr": "${yaalehVyavoFemaleAmidahLead(\"Maariv\")}\n\n${yaalehVyavoForgotMaarivRoshChodesh()}\n\nSi vous dites Birkat Hamazon en mangeant du pain ce soir, ajoutez aussi Yaaleh V'yavo.",
        "ru": "${yaalehVyavoFemaleAmidahLead(\"Maariv\")}\n\n${yaalehVyavoForgotMaarivRoshChodesh()}\n\nЕсли сегодня вечером вы говорите Биркат а-Мазон при еде хлеба, добавьте туда также Яале ве-яво.",
    },
    "${yaalehVyavoFemaleAmidahLead(\"Mincha\")}\n\n${yaalehVyavoForgotShacharitOrMincha(\"Mincha\")}\n\nIf you say Birkat Hamazon when you eat bread today, add Yaaleh V'yavo there too.": {
        "he": "${yaalehVyavoFemaleAmidahLead(\"Mincha\")}\n\n${yaalehVyavoForgotShacharitOrMincha(\"Mincha\")}\n\nאם אומרים ברכת המזון כשאוכלים לחם היום, יש להוסיף גם שם יעלה ויבוא.",
        "es": "${yaalehVyavoFemaleAmidahLead(\"Mincha\")}\n\n${yaalehVyavoForgotShacharitOrMincha(\"Mincha\")}\n\nSi recitas Birkat Hamazón al comer pan hoy, añade también Yaaleh V'yavo allí.",
        "fr": "${yaalehVyavoFemaleAmidahLead(\"Mincha\")}\n\n${yaalehVyavoForgotShacharitOrMincha(\"Mincha\")}\n\nSi vous dites Birkat Hamazon en mangeant du pain aujourd'hui, ajoutez aussi Yaaleh V'yavo.",
        "ru": "${yaalehVyavoFemaleAmidahLead(\"Mincha\")}\n\n${yaalehVyavoForgotShacharitOrMincha(\"Mincha\")}\n\nЕсли сегодня вы говорите Биркат а-Мазон при еде хлеба, добавьте туда также Яале ве-яво.",
    },
    "${yaalehVyavoFemaleAmidahLead(\"Shacharit\")}\n\n${yaalehVyavoForgotShacharitOrMincha(\"Shacharit\")}\n\nIf you say Birkat Hamazon when you eat bread today, add Yaaleh V'yavo there too.": {
        "he": "${yaalehVyavoFemaleAmidahLead(\"Shacharit\")}\n\n${yaalehVyavoForgotShacharitOrMincha(\"Shacharit\")}\n\nאם אומרים ברכת המזון כשאוכלים לחם היום, יש להוסיף גם שם יעלה ויבוא.",
        "es": "${yaalehVyavoFemaleAmidahLead(\"Shacharit\")}\n\n${yaalehVyavoForgotShacharitOrMincha(\"Shacharit\")}\n\nSi recitas Birkat Hamazón al comer pan hoy, añade también Yaaleh V'yavo allí.",
        "fr": "${yaalehVyavoFemaleAmidahLead(\"Shacharit\")}\n\n${yaalehVyavoForgotShacharitOrMincha(\"Shacharit\")}\n\nSi vous dites Birkat Hamazon en mangeant du pain aujourd'hui, ajoutez aussi Yaaleh V'yavo.",
        "ru": "${yaalehVyavoFemaleAmidahLead(\"Shacharit\")}\n\n${yaalehVyavoForgotShacharitOrMincha(\"Shacharit\")}\n\nЕсли сегодня вы говорите Биркат а-Мазон при еде хлеба, добавьте туда также Яале ве-яво.",
    },
}


def build_hebrew_liturgy_argos() -> dict[str, dict[str, str]]:
    """Translate Hebrew-only liturgy keys to es/fr/ru via Argos he->target if available."""
    missing_path = ROOT / "data" / "translation-catalog" / "still-missing.json"
    if not missing_path.exists():
        return {"he": {}, "es": {}, "fr": {}, "ru": {}}
    still = json.loads(missing_path.read_text(encoding="utf-8"))
    hebrew_keys = [k for k in still.get("he", []) if any("\u0590" <= c <= "\u05ff" for c in k)]
    hebrew_keys += [k for k in still.get("es", []) if any("\u0590" <= c <= "\u05ff" for c in k)]
    hebrew_keys = list(dict.fromkeys(hebrew_keys))
    if not hebrew_keys:
        return {"he": {}, "es": {}, "fr": {}, "ru": {}}

    import argostranslate.package
    import argostranslate.translate

    argostranslate.package.update_package_index()
    available = argostranslate.package.get_available_packages()
    installed = argostranslate.package.get_installed_packages()

    def ensure(from_code: str, to_code: str) -> None:
        if any(p.from_code == from_code and p.to_code == to_code for p in installed):
            return
        pkg = next((p for p in available if p.from_code == from_code and p.to_code == to_code), None)
        if pkg:
            argostranslate.package.install_from_path(pkg.download())

    for to_code in ("es", "fr", "ru"):
        ensure("he", to_code)

    langs = {l.code: l for l in argostranslate.translate.get_installed_languages()}
    he_lang = langs.get("he")
    out: dict[str, dict[str, str]] = {"he": {}, "es": {}, "fr": {}, "ru": {}}
    for key in hebrew_keys:
        out["he"][key] = key
        for to_code in ("es", "fr", "ru"):
            tgt = langs.get(to_code)
            if he_lang and tgt:
                tr = he_lang.get_translation(tgt)
                out[to_code][key] = tr.translate(key) if tr else key
            else:
                out[to_code][key] = key
    return out


def main() -> None:
    shard: dict[str, dict[str, str]] = {"he": {}, "es": {}, "fr": {}, "ru": {}}
    for en, mapping in EN_FIXES.items():
        for lang, val in mapping.items():
            shard[lang][en] = val

    liturgy = build_hebrew_liturgy_argos()
    for lang in shard:
        shard[lang].update(liturgy.get(lang, {}))

    OUT.write_text(json.dumps(shard, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"wrote {OUT} with {sum(len(v) for v in shard.values())} entries")
    subprocess.check_call([sys.executable, str(TOOLS / "compile_full_bundled.py")])


if __name__ == "__main__":
    main()
