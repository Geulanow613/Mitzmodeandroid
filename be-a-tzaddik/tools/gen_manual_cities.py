#!/usr/bin/env python3
"""Build manual-cities.json from tools/manual_cities.tsv."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
TSV = ROOT / "tools" / "manual_cities.tsv"
OUT = ROOT / "shared" / "src" / "commonMain" / "composeResources" / "files" / "manual-cities.json"
DATA_COPY = ROOT / "data" / "manual-cities.json"


def main() -> None:
    cities = []
    seen: set[str] = set()
    for line in TSV.read_text(encoding="utf-8").splitlines():
        line = line.strip()
        if not line or line.startswith("#"):
            continue
        parts = line.split("\t")
        if len(parts) != 5:
            raise ValueError(f"Expected 5 columns, got {len(parts)}: {line[:80]}")
        city_id, label, lat_s, lon_s, tz = parts
        if city_id in seen:
            raise ValueError(f"Duplicate id: {city_id}")
        seen.add(city_id)
        cities.append(
            {
                "id": city_id,
                "label": label,
                "latitude": float(lat_s),
                "longitude": float(lon_s),
                "timezoneId": tz,
            }
        )
    cities.sort(key=lambda c: c["label"].lower())
    payload = {"version": 1, "cities": cities}
    text = json.dumps(payload, indent=2, ensure_ascii=False) + "\n"
    OUT.parent.mkdir(parents=True, exist_ok=True)
    OUT.write_text(text, encoding="utf-8")
    DATA_COPY.parent.mkdir(parents=True, exist_ok=True)
    DATA_COPY.write_text(text, encoding="utf-8")
    print(f"Wrote {len(cities)} cities to {OUT}")


if __name__ == "__main__":
    main()
