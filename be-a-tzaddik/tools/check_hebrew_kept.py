import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
for n in range(20, 26):
    batch = json.loads((ROOT / f"data/translation-catalog/batches/batch_{n:03d}.json").read_text(encoding="utf-8"))["strings"]
    shard = json.loads((ROOT / f"data/translation-catalog/shards/batch_{n:03d}.json").read_text(encoding="utf-8"))
    for i, s in enumerate(batch):
        hc = sum(1 for c in s if "\u0590" <= c <= "\u05ff")
        if hc > 30:
            same = shard["he"][s] == s
            print(f"batch_{n:03d}[{i}]: he_same={same} hebrew_chars={hc}")
