import json
from pathlib import Path

BASE = Path(r"c:\apps\hehehe\be-a-tzaddik\data\translation-catalog\human")

files = {
    "local_006_es_only.json": ("es", json.load(open(BASE / "local_006_es_only.json", encoding="utf-8"))["es"] if False else None),
}

# Load es/fr/ru from the draft module
exec(open(r"c:\apps\hehehe\be-a-tzaddik\tools\_write_translations.py", encoding="utf-8").read().split("files =")[0])

he = [
    local_006_he[0],
    local_006_he[1],
    local_006_he[2],
    local_006_he[3],
    "ראיתם לאחרונה חפץ אבוד? 🔍 אל תתעלמו! חזרו ונסו להחזיר אותו לבעליו (אם אפשר). זו מצוות השבת אבידה (Hashavat Aveidah). התורה מלמדת שאם אתם רואים את בהמתו או חפציו של חברכם הולכים ותועים — אסור להתעלם; עליכם להשתדל להחזירם. חachamenu הקדישu דיונים שלמים למצווה זו: כמה חייבים להשתדל, מה מותר לשמור כשכר על הטרחה, ולמה החזרat רכוש הוא גם החזרat כבוד.",
]
