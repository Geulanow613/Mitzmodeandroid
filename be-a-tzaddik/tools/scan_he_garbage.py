#!/usr/bin/env python3
"""Find worst Hebrew translation garbage patterns."""
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
req = json.loads((ROOT / "data/translation-catalog/strings.json").read_text(encoding="utf-8"))["strings"]
he = json.loads(
    (ROOT / "shared/src/commonMain/composeResources/files/translations/he.json").read_text(encoding="utf-8")
)["entries"]

PATTERNS = [
    (r" - - - ", "triple-dash"),
    (r"An An An ", "triple-an"),
    (r"פסטיבל", "festival-mistranslation"),
    (r"Hurtin", "hurtin"),
    (r"אורורה", "menorah-typo"),
    (r"אולוגיז", "eulogy-garbage"),
    (r"ראש השנה \(חודש", "rosh-hashana-for-rosh-chodesh"),
    (r"יום שישי עם Hallel", "friday-for-semi-holiday"),
    (r"halachic ", "english-halachic"),
    (r"Grace לאחר", "grace-garbage"),
    (r"Vayavo A", "amalek-garbage"),
    (r"hu psak", "latin-translit"),
    (r"me'ayen", "latin-translit2"),
    (r"Enter Retzei", "english-in-he"),
    (r"Paragraph Amidah", "english-in-he2"),
    (r"שם הסרטון", "video-caption-garbage"),
    (r"בתי משפט כמו Chabad", "courts-not-chassidut"),
    (r"מניעים בציבור", "amot-garbage"),
    (r"רעידות לעתים", "shuls-garbage"),
    (r"צ'אט אפס", "chatzerot-garbage"),
]

hits: dict[str, list[str]] = {name: [] for _, name in PATTERNS}
for en, tr in he.items():
    if tr == en:
        continue
    for pat, name in PATTERNS:
        if re.search(pat, tr):
            hits[name].append(en[:80])

lines = []
for name, keys in hits.items():
    if keys:
        lines.append(f"\n=== {name} ({len(keys)}) ===")
        for k in keys[:8]:
            lines.append(f"  {k}")
        if len(keys) > 8:
            lines.append(f"  ... +{len(keys)-8} more")

# Latin-heavy garbage (not intentional transliteration keys)
latin_heavy = []
for en, tr in he.items():
    if tr == en or len(tr) < 40:
        continue
    letters = [c for c in tr if c.isalpha()]
    if not letters:
        continue
    latin = sum(1 for c in letters if c.isascii())
    ratio = latin / len(letters)
    # skip if mostly Hebrew with some terms
    if ratio > 0.35 and not en.startswith("three stars"):
        latin_heavy.append((ratio, en[:70], tr[:100]))

latin_heavy.sort(reverse=True)
lines.append(f"\n=== latin-heavy he ({len(latin_heavy)}) top 30 ===")
for r, en, tr in latin_heavy[:30]:
    lines.append(f"  [{r:.2f}] {en}")
    lines.append(f"       {tr}")

out = ROOT / "data/translation-catalog/he-garbage-scan.txt"
out.write_text("\n".join(lines), encoding="utf-8")
print(f"wrote {out}")
