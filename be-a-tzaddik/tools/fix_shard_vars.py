#!/usr/bin/env python3
"""Restore exact template variables from English keys into shard translations."""
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1] / "data" / "translation-catalog"
LANGS = ("he", "es", "fr", "ru")
VAR_RE = re.compile(r"\$\{[^}]+\}|\$[A-Za-z_][A-Za-z0-9_]*")

REPLACEMENTS = [
    (r"\$mitzvah Text", "$mitzvahText"),
    (r"\$\{diasporaFinal Pesach AdvanceNote", "${diasporaFinalPesachAdvanceNote"),
    (r"\$\{si \(profil est en Israël\)", "${if (profile.isInIsrael)"),
    (r"תגית:is inIsrael", "${if (profile.isInIsrael)"),
]


def spaced_camel(s: str) -> str:
    """Insert spaces at camelCase boundaries inside a template token."""
    inner = s
    if inner.startswith("${") and inner.endswith("}"):
        return "${" + re.sub(r"(?<=[a-z])(?=[A-Z])", " ", inner[2:-1]) + "}"
    return re.sub(r"(?<=[a-z])(?=[A-Z])", " ", s)


def restore_vars(key: str, text: str) -> str:
    for pattern, repl in REPLACEMENTS:
        text = re.sub(pattern, repl, text)

    for var in VAR_RE.findall(key):
        if var in text:
            continue
        corrupt = spaced_camel(var)
        if corrupt != var and corrupt in text:
            text = text.replace(corrupt, var)
    return text


def main() -> None:
    fixed_count = 0
    for batch_num in range(14, 20):
        path = ROOT / "shards" / f"batch_{batch_num:03d}.json"
        shard = json.loads(path.read_text(encoding="utf-8"))
        for key in list(shard["he"]):
            if "$" not in key:
                continue
            for lang in LANGS:
                old = shard[lang][key]
                new = restore_vars(key, old)
                if new != old:
                    shard[lang][key] = new
                    fixed_count += 1
        path.write_text(json.dumps(shard, ensure_ascii=False, indent=2), encoding="utf-8")
        print(f"processed batch_{batch_num:03d}.json")
    print(f"fixed {fixed_count} entries")


if __name__ == "__main__":
    main()
