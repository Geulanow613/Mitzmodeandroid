#!/usr/bin/env python3
import json
from pathlib import Path
ROOT = Path(__file__).resolve().parents[1]
cat = json.load(open('data/translation-catalog/strings.json', encoding='utf-8'))['strings']
subs = [
    'Kashrut is the system',
    'The rabbis instituted a practice',
    'When you acquire new metal',
    'Why the sages urge us to learn',
]
for i, sub in enumerate(subs):
    k = next(x for x in cat if x.startswith(sub))
    (ROOT / 'tools' / f'_batch21_en_{i}.txt').write_text(k, encoding='utf-8')
    print(i, sub, len(k))
