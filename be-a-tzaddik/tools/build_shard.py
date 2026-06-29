#!/usr/bin/env python3
"""Build one shard from a translations sidecar JSON."""
import json
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1] / "data" / "translation-catalog"
LANGS = ("he", "es", "fr", "ru")


def main(batch_num: int, sidecar: Path) -> None:
    batch = json.loads((ROOT / "batches" / f"batch_{batch_num:03d}.json").read_text(encoding="utf-8"))
    data = json.loads(sidecar.read_text(encoding="utf-8"))
    shard = {lang: {} for lang in LANGS}
    missing = []
    for s in batch["strings"]:
        if s not in data:
            missing.append(s)
            continue
        for lang in LANGS:
            shard[lang][s] = data[s][lang]
    if missing:
        print(f"Missing {len(missing)}:", missing[0][:100], file=sys.stderr)
        sys.exit(1)
    out = ROOT / "shards" / f"batch_{batch_num:03d}.json"
    out.parent.mkdir(parents=True, exist_ok=True)
    out.write_text(json.dumps(shard, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"Wrote {out.name}: {len(shard['he'])} keys x {len(LANGS)} langs")

if __name__ == "__main__":
    main(int(sys.argv[1]), Path(sys.argv[2]))
