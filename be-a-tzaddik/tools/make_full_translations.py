#!/usr/bin/env python3
"""Generate translations_000_006_full.py with all 560 string translations."""

from __future__ import annotations

import json
from pathlib import Path

OUT = Path(__file__).resolve().parent / "translations_000_006_full.py"
ROOT = Path(__file__).resolve().parents[1]
BATCH_DIR = ROOT / "data" / "translation-catalog" / "batches"
BUNDLED_DIR = ROOT / "data" / "bundled-translations"

# Hebrew translations for all batch 000-006 strings (keys = exact English source).
HE: dict[str, str] = {}
# Explicit es/fr/ru where bundled equals English (validator + quality).
OVR: dict[str, dict[str, str]] = {"es": {}, "fr": {}, "ru": {}}


def he(k: str, v: str) -> None:
    HE[k] = v


def ovr(k: str, es: str, fr: str, ru: str) -> None:
    OVR["es"][k] = es
    OVR["fr"][k] = fr
    OVR["ru"][k] = ru


# === BATCH 000 ===
he("No", "לא")
he("OK", "אישור")
he("Rav", "רב")
he("Amen", "אמן")
he("Back", "חזרה")
he("Chag", "חג")
he("Done", "סיום")
he("Elul", "אלול")
he("Erev", "ערב")
he("Keli", "כלי")
he("Losh", "לישה")
he("Male", "זכר")
he("Next", "הבא")
he("Skip", "דלג")
he("else", "אחר")
he("eruv", "עירוב")
he("klaf", "קלף")
he("levi", "לוי")
he("oleh", "עולה")
he("psak", "פסק")
he("shul", "בית כנסת")
he("trop", "טעמי המקרא")
he("About", "אודות")
he("Blech", "ריפוד")
he("Boneh", "בונה")
he("Borer", "בורר")
he("CLOSE", "סגור")
he("Clear", "נקה")
he("Close", "סגור")
he("Gozez", "גוזז")
he("Haman", "המן")
he("Kinot", "קינות")
he("Mavir", "מעביר")
he("Modah", "מודה")
he("Musaf", "מוסף")
he("Nerot", "נרות")
he("Purim", "פורים")
he("Rabbi", "רב")
he("Rashi", "רש״י")
he("Seder", "סeder")
he("Shema", "שמע")
he("Soter", "סותר")
he("Tanya", "תניא")
he("Torah", "תורה")
he("Tzeit", "צאת הכוכבים")
he("Zohar", "זוהר")
he("Zorea", "זורע")
he("beged", "בגד")
he("bitul", "ביטול")
he("daven", "תפילה")
he("etrog", "אתרוג")
he("hadas", "הadas")
he("kohen", "כהן")
he("lulav", "לulav")
he("maror", "מרor")
he("patur", "פטור")
he("pitom", "פיטom")
he("posek", "פosek")
he("pshat", "פשט")
he("ruach", "רוח")
he("shiva", "שבעה")
he("siyum", "סiyum")
he("sofer", "סופר")
he("treif", "טרף")
he("tumah", "טumah")
he("zeroa", "זרוע")
he("zimun", "זימון")
he("Aleinu", "עלינו")
he("Amidah", "עמידה")
he("Barchu", "ברכו")
he("Bishul", "bishul")
he("Bracha", "bracha")
he("Cancel", "ביטול")
he("Chabad", "chabad")
he("Chazal", "chazal")
he("Chitas", "chitas")
he("Emunah", "emunah")
he("Female", "נקבה")
he("Gemara", "gemara")
he("Geshem", "geshem")

ovr("Amen", "Amén", "Amen", "Аминь")
ovr("Back", "Atrás", "Retour", "Назад")
ovr("Male", "Hombre", "Homme", "Мужской")
ovr("Female", "Mujer", "Femme", "Женский")
ovr("else", "otro", "autre", "другое")
