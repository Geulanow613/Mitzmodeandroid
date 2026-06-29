import json, re
from pathlib import Path
p = Path(r'c:\apps\hehehe\be-a-tzaddik\tools\_he_data_010_012.json')
he = json.loads(p.read_text(encoding='utf-8'))['010']
MIXED = re.compile(r'[\u0590-\u05FF]+[A-Za-z\u0400-\u04FF]+|[A-Za-z\u0400-\u04FF]+[\u0590-\u05FF]+')
for i,s in enumerate(he):
    bad=[m.group() for m in MIXED.finditer(s) if 'HebrewCalendar' not in m.group() and 'profile' not in m.group()]
    print(i+1, 'BAD' if bad else 'OK', bad[:1])
Path(r'c:\apps\hehehe\be-a-tzaddik\tools\_check_result.txt').write_text('done', encoding='utf-8')
