# -*- coding: utf-8 -*-
import json
import re
from pathlib import Path

OUT = Path(r"c:\apps\hehehe\be-a-tzaddik\data\translation-catalog\human\mitzvot_002_he_only.json")
SRC = Path(r"c:\apps\hehehe\be-a-tzaddik\tools\_write_mitzvot_002_he.py")

text = SRC.read_text(encoding="utf-8")
start = text.index("he = [")
end = text.index("\n]\n\nprint")
block = text[start:end + 2]
he = eval(block.split("=", 1)[1].strip())[:18]

tail = json.loads(Path(__file__).with_name("_tail_he.json").read_text(encoding="utf-8"))
he.extend(tail)

assert len(he) == 25, len(he)
for i, s in enumerate(he):
    assert s.strip(), f"empty at {i+1}"

latin = re.compile(r"[A-Za-z]")
allowed = re.compile(
    r'(?:רמב"ם|הלכות|שולחן ערוך|אורח חיים|פרקי אבות|תלמוד|גמרא|משנה|זוהר|ספר החינוך|החפץ חיים|חמר מדינה|נגלה|נסתר|שבת|פסחים|ערכין|עירובין|סנהדרין|נדרים|פאה|מגיד|קידוש|הבדלה|בשמים|חנוכייה|פרסומי ניסא|משלוח מנות|מתנות לאביונים|עונג שבת|ביקור חולים|בל תשחית|לשון הרע|רודף שלום|הכנסת אורחים|צאת הכוכבים|כל חמירא|ביטול|שריפת חמץ|Am Yisrael|Hashem|Shabbat|Shalom|melo lugmav|HaMotzi|Borei Pri HaGafen|Kiddush|Havdalah|Besamim|neshama yeteira|Hamavdil|Baruch Hamavdil|Kodesh|Melachah|Oneg Shabbat|Pirsumei Nisa|matanot la.evyonim|Mishloach Manot|Bedikat Chametz|Kol Chamira|Bitul|Biur Chametz|Maggid|Dan L.Kaf Zechut|V.Halachta B.Drachav|Nekamah|Netirah|Hilchot|Orach Chaim|Peah|Nedarim|Arachin|Eruvin|Sanhedrin|Shabbat 118a|Deuteronomy|Exodus|Leviticus|Mishnah|Pesachim|Chinuch|Chofetz Chaim|Rashi|Ramban|Rambam|Maimonides|Hilchot De.ot|Hilchot Aveilut|Hilchot Matanot Aniyim|Hilchot Shabbat|Hilchot Chametz U.Matzah|brit milah|Adar|Nissan|Tzet HaKochavim|chamor medina|B.tzedek Tishpot Amitecha|Borei pri hagafen|Borei minei vesamim|Borei m.orei ha.eish|Hamavdil bein kodesh l.chol|win-win)'
)

bad = []
for i, s in enumerate(he, 1):
    for m in latin.finditer(s):
        ctx = s[max(0, m.start() - 20) : m.end() + 20]
        if not allowed.search(ctx):
            bad.append((i, m.group(), ctx))

if bad:
    raise SystemExit(f"Latin corruption found: {bad[:5]}")

OUT.write_text(
    json.dumps({"he": he}, ensure_ascii=False, indent=2) + "\n",
    encoding="utf-8",
)
print(f"Wrote {OUT}")
print(f"count={len(he)}")
