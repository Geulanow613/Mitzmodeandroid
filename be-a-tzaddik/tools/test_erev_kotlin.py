import json
from pathlib import Path

strings = json.loads(Path("data/translation-catalog/strings.json").read_text(encoding="utf-8"))["strings"]
batch = json.loads(Path("data/translation-catalog/shards/batch_025.json").read_text(encoding="utf-8"))
en = [s for s in strings if s.startswith("Today is Erev Yom Kippur")][0]
tr = batch["es"][en]


def kotlin_blocks(text: str) -> list[str]:
    blocks, i = [], 0
    while i < len(text):
        if text.startswith("${", i):
            depth, j = 1, i + 2
            while j < len(text) and depth > 0:
                if text[j] == "{":
                    depth += 1
                elif text[j] == "}":
                    depth -= 1
                j += 1
            blocks.append(text[i:j])
            i = j
        else:
            i += 1
    return blocks


def force_english_kotlin(en: str, tr: str) -> str:
    for eb in kotlin_blocks(en):
        for tb in kotlin_blocks(tr):
            if tb != eb:
                tr = tr.replace(tb, eb, 1)
    return tr


def repair_corrupted_kotlin(en: str, tr: str, lang: str = "") -> str:
    en_blocks = kotlin_blocks(en)
    tr_blocks = kotlin_blocks(tr)
    if len(en_blocks) == len(tr_blocks):
        if any(len(tb) > len(eb) * 2 for eb, tb in zip(en_blocks, tr_blocks)):
            pass
        else:
            return force_english_kotlin(en, tr)
    for eb in en_blocks:
        if eb in tr:
            continue
        idx = tr.find("${")
        if idx < 0:
            break
        depth, j = 1, idx + 2
        while j < len(tr) and depth > 0:
            if tr[j] == "{":
                depth += 1
            elif tr[j] == "}":
                depth -= 1
            j += 1
        giant = tr[idx:j]
        prefix = tr[:idx]
        print("j", j, "len tr", len(tr), "giant", len(giant), "eb", len(eb))
        if j >= len(tr) and len(giant) > len(eb) + 100:
            cut = giant.find("Tosefet")
            suffix = giant[cut:] if cut > 0 else ""
            mid = " hasta la noche de mañana ($tzeitTomorrow).\n"
            if suffix and not suffix.startswith("•"):
                suffix = "• " + suffix.lstrip()
            tr = prefix + eb + mid + suffix
            print("repaired mega", len(tr))
        else:
            tr = prefix + eb + tr[j:]
    return tr


tr2 = repair_corrupted_kotlin(en, tr, "es")
print("final", len(tr2), "tzeitTomorrow" in tr2)
