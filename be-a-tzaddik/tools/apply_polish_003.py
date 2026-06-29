# -*- coding: utf-8 -*-
import json
import re
from pathlib import Path

ROOT = Path(r"c:\apps\hehehe\be-a-tzaddik")
target = ROOT / "data/translation-catalog/human/mitzvot_003_he_only.json"
polish_path = ROOT / "tools/_polish_003_he.json"

MAMRIM = "\u05d4\u05dc\u05db\u05d5\u05ea \u05de\u05de\u05e8\u05d9\u05dd"
LUBAVITCH = "\u05de\u05dc\u05d9\u05d5\u05d1\u05d0\u05d5\u05d5\u05d9\u05d8\u05e9"
SECHIRUT = "\u05d4\u05dc\u05db\u05d5\u05ea \u05e9\u05db\u05d9\u05e8\u05d5\u05ea"
DEUT_VERSE = (
    "\u05d1\u05d9\u05d5\u05dd \u05d0\u05ea\u05d5 \u05d9\u05e6\u05d0 \u05d1\u05d9\u05d5\u05de\u05d5, "
    "\u05ea\u05df \u05dc\u05d5 \u05d0\u05ea \u05e9\u05db\u05e8\u05d5, "
    "\u05dc\u05d0 \u05d9\u05d1\u05d5\u05d0 \u05e2\u05dc\u05d9\u05d5 \u05d4\u05e9\u05de\u05e9"
)

data = json.loads(target.read_text(encoding="utf-8"))
he = data["he"]
polish = json.loads(polish_path.read_text(encoding="utf-8"))

he[2] = he[2].replace('"מצ מוד!"', "'Mitz Mode!'")
for old in ("אנחנו מטbיעים מצוות, משפחה!", "אנחנו מטביעים מצוות, משפחה!"):
    he[4] = he[4].replace(old, "We're mintin' mitzvot, fam!")

entries = polish["entries"]
indices = polish["indices"]

for idx, entry in zip(indices, entries):
    entry = entry.replace("מליובavitch", LUBAVITCH)
    entry = re.sub(r"הלכות מ[a-z]*rim", MAMRIM, entry)
    entry = entry.replace(
        '"ביוo יoצא ביוo, לפני שקיעat השמש, תiten lo את שכרו"',
        f'"{DEUT_VERSE}"',
    )
    entry = entry.replace("הלכות שכirut", SECHIRUT)
    he[idx] = entry

# Validate
assert len(he) == 25, len(he)
bad = re.compile(r"[a-z]{2,}", re.I)
issues = []
for i, s in enumerate(he):
    for m in bad.finditer(s):
        w = m.group()
        if w.lower() in {
            "fam", "mitz", "mode", "mintin", "mitzvot", "re", "we",
            "vip", "star", "trek", "live", "long", "and", "prosper",
            "vulcan", "leonard", "nimoy", "amen", "kol", "nidrei",
            "stay", "holy", "shel", "yad", "rosh", "ha", "etz",
            "adamah", "motzi", "shehakol", "gafen", "mag", "esh",
            "omer", "duchan", "shacharit", "yom", "tov", "kiddush",
            "chillul", "hashem", "torah", "talmud", "rambam", "shulchan",
            "aruch", "hilchot", "berakhot", "deuteronomy", "leviticus",
            "exodus", "numbers", "nach", "gemara", "chumash", "genizah",
            "vidui", "charatah", "teshuvah", "ashkenazim", "sephardim",
            "kavanah", "avot", "siddur", "ruach", "hakodesh", "shemoneh",
            "esrei", "amidah", "shabbat", "challah", "midrash", "moshiach",
            "hamotzi", "yoshiyahu", "kohen", "kohanim", "levites",
            "tallits", "mezuzah", "sofer", "batim", "kazayit", "zimun",
            "hazen", "haaretz", "betar", "bracha", "brachot", "berakhot",
            "tochachah", "talmid", "chacham", "gadol", "olam", "haba",
            "shema", "birkat", "hamazon", "haetz", "haadamah", "borei",
            "peri", "hagafen", "tech", "star", "trek", "or", "if",
            "the", "not", "for", "and", "you", "your", "with", "from",
        }:
            continue
        if w in {"lo", "et", "al", "o", "y", "at", "ten", "bo", "av"}:
            continue
        issues.append((i, w, s[max(0, m.start()-20):m.end()+20]))

report = ROOT / "tools/_polish_report.txt"
report.write_text(
    f"count={len(he)}\nissues={len(issues)}\n" + "\n".join(str(x) for x in issues[:30]),
    encoding="utf-8",
)

data["he"] = he
target.write_text(json.dumps(data, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
print("done", len(he), "issues", len(issues))
