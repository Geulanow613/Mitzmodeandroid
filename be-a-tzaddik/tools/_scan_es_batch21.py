#!/usr/bin/env python3
import json,re
from pathlib import Path
ROOT = Path(__file__).resolve().parents[1]
cat = json.load(open('data/translation-catalog/strings.json', encoding='utf-8'))['strings']
es = json.load(open('shared/src/commonMain/composeResources/files/translations/es.json', encoding='utf-8'))['entries']
subs = [
    'Kashrut is the system',
    'The rabbis instituted a practice',
    'When you acquire new metal',
    'Why the sages urge us to learn',
]
for sub in subs:
    k = next(x for x in cat if x.startswith(sub))
    v = es[k]
    print('='*60, sub)
    for pat in [r'.{0,50}י{3,}.{0,50}', r'treif \([^)]+\)', r'Incluso', r'tronco', r'fit\"']:
        for m in re.finditer(pat, v):
            print(' MATCH:', m.group(0).replace('\n',' '))
    print('LEN', len(v))
