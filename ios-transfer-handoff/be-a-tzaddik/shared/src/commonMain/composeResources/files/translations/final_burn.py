import json
import re

TERM_MAP = {
    "sha'ah zmanit": "שעה זמנית",
    "sha'a zmanit": "шаа зманит",
    "sofer": "סופר",
    "Neshamot Hagalmudot": "נשמות הגלמודות",
    "Gan Eden": "גן עדן",
    "Star-K": "סטאר-קיי",
    "chatzos halayla": "חצות הלילה",
    "Yahrzeit": "יארצייט",
    "yahrzeit": "יארצייט",
    "Mourner": "אבל",
    "Kaddish": "קדיש",
    "Amidá": "עמידה",
    "Shemoneh Esrei": "שמונה עשרה",
    "Shajarit": "שחרית",
    "Minjá": "מנחה",
    "Maariv": "מעריב",
    "tashlumin": "תשלומין",
    "Tefilat Nedavah": "תפילת נדבה",
    "minyan": "מניין",
    "Kiddush": "קידוש",
    "lessרוכּ": "ברכו",
    "Bless Dios": "ברכו את ה'",
}

WHITELIST = ['geula', 'mm', 'ev', 'amy', 'hs', 'dm', 'mb', 'hb', 'beardy', 'top', 'www', 'https', 'it', 'n', 'r', 'regex', 'escape', 'word']

def final_clean(text, lang):
    if not text: return text

    # Replace known terms
    for k, v in TERM_MAP.items():
        text = re.sub(r'(?i)\b' + re.escape(k) + r'\b', v, text)
        if re.search(r'[א-ת]', k) or re.search(r'[а-яА-Я]', k):
             text = text.replace(k, v)

    # Aggressive Latin removal for Hebrew and Russian
    if lang in ['he', 'ru']:
        def sub_fn(match):
            w = match.group(0)
            if w.startswith('$') or w.lower() in WHITELIST: return w
            return ""

        text = re.sub(r'\$?[a-zA-Z\u0027]+', sub_fn, text)

    # Fix parentheses left empty or with just spaces
    text = re.sub(r'\(\s*\)', '', text)
    text = re.sub(r'\s+', ' ', text)
    return text.strip()

files = ['he.json', 'ru.json', 'es.json']
for f_name in files:
    lang = f_name[:2]
    with open(f_name, 'r', encoding='utf-8') as f:
        data = json.load(f)
    entries = data['entries']
    for k in entries:
        entries[k] = final_clean(entries[k], lang)
    with open(f_name, 'w', encoding='utf-8') as f:
        json.dump(data, f, ensure_ascii=False, indent=4)

print("Final burn complete.")
