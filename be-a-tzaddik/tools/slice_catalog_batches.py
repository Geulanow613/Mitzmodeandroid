#!/usr/bin/env python3
"""Slice translation catalog into human-batch key files."""

from __future__ import annotations

import json
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CATALOG = ROOT / "data/translation-catalog/strings.json"
HUMAN = ROOT / "data/translation-catalog/human"
CLOUD = ROOT / "data/mitzvotcloud.json"
LOCAL = ROOT.parent / "app/src/main/assets/mitzvotlistfull.json"
OUT = ROOT / "data/translation-catalog"


def load_human_keys() -> set[str]:
    covered: set[str] = set()
    skip = {"_only", "_src"}
    for p in HUMAN.glob("*.json"):
        if any(x in p.name for x in skip) or p.name.startswith("_"):
            continue
        data = json.loads(p.read_text(encoding="utf-8"))
        for lang in ("he", "es", "fr", "ru"):
            covered.update(data.get(lang, {}).keys())
    return covered


def mitzvah_texts() -> set[str]:
    texts: set[str] = set()
    texts.update(m["text"] for m in json.loads(CLOUD.read_text(encoding="utf-8"))["mitzvot"])
    texts.update(m["text"] for m in json.loads(LOCAL.read_text(encoding="utf-8"))["mitzvot"])
    return texts


def slice_keys(name: str, keys: list[str], batch_size: int) -> list[Path]:
    OUT.mkdir(parents=True, exist_ok=True)
    paths: list[Path] = []
    for i in range(0, len(keys), batch_size):
        batch = keys[i : i + batch_size]
        num = i // batch_size + 1
        path = OUT / f"_keys_{name}_{num:03d}.json"
        path.write_text(json.dumps(batch, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
        paths.append(path)
        print(f"  {path.name}: {len(batch)} keys")
    return paths


def main() -> None:
    mode = sys.argv[1] if len(sys.argv) > 1 else "status"
    strings = json.loads(CATALOG.read_text(encoding="utf-8"))["strings"]
    covered = load_human_keys()
    mitzvot = mitzvah_texts()

    if mode == "status":
        buckets = {"<=30": [], "<=80": [], "<=300": [], ">300": []}
        for s in strings:
            if s in covered:
                continue
            n = len(s)
            if n <= 30:
                buckets["<=30"].append(s)
            elif n <= 80:
                buckets["<=80"].append(s)
            elif n <= 300:
                buckets["<=300"].append(s)
            else:
                buckets[">300"].append(s)
        print(f"covered: {len(covered)} / {len(strings)}")
        print(f"mitzvot in catalog: {len(mitzvot & set(strings))}")
        for k, v in buckets.items():
            print(f"  remaining {k}: {len(v)}")
        return

    if mode == "ui_short":
        batch_size = int(sys.argv[2]) if len(sys.argv) > 2 else 50
        keys = sorted(s for s in strings if s not in covered and s not in mitzvot and len(s) <= 30)
        print(f"ui_short: {len(keys)} keys -> batches of {batch_size}")
        slice_keys("ui_short", keys, batch_size)
        return

    if mode == "ui_medium":
        batch_size = int(sys.argv[2]) if len(sys.argv) > 2 else 40
        keys = sorted(
            s for s in strings if s not in covered and s not in mitzvot and 30 < len(s) <= 80
        )
        print(f"ui_medium: {len(keys)} keys -> batches of {batch_size}")
        slice_keys("ui_medium", keys, batch_size)
        return

    if mode == "local":
        local_data = json.loads(LOCAL.read_text(encoding="utf-8"))["mitzvot"]
        keys = [m["text"] for m in local_data if m["text"] not in covered]
        batch_size = int(sys.argv[2]) if len(sys.argv) > 2 else 25
        print(f"local mitzvot: {len(keys)} keys -> batches of {batch_size}")
        slice_keys("local", keys, batch_size)
        return

    if mode == "content_medium":
        batch_size = int(sys.argv[2]) if len(sys.argv) > 2 else 20
        keys = sorted(
            s for s in strings if s not in covered and s not in mitzvot and 80 < len(s) <= 300
        )
        print(f"content_medium: {len(keys)} keys -> batches of {batch_size}")
        slice_keys("content_medium", keys, batch_size)
        return

    if mode == "content_long":
        batch_size = int(sys.argv[2]) if len(sys.argv) > 2 else 10
        keys = sorted(s for s in strings if s not in covered and s not in mitzvot and len(s) > 300)
        print(f"content_long: {len(keys)} keys -> batches of {batch_size}")
        slice_keys("content_long", keys, batch_size)
        return

    raise SystemExit(f"unknown mode: {mode}")


if __name__ == "__main__":
    main()
