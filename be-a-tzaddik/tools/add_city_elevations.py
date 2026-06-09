#!/usr/bin/env python3
"""Add elevationMeters to manual-cities.json via Open-Meteo (free, no API key)."""
import json
import urllib.parse
import urllib.request
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
PATHS = [
    ROOT / "shared" / "src" / "commonMain" / "composeResources" / "files" / "manual-cities.json",
    ROOT / "data" / "manual-cities.json",
]
BATCH = 100


def fetch_elevations(lats: list[float], lons: list[float]) -> list[float]:
    params = urllib.parse.urlencode(
        [
            ("latitude", ",".join(f"{lat:.6f}" for lat in lats)),
            ("longitude", ",".join(f"{lon:.6f}" for lon in lons)),
        ]
    )
    url = f"https://api.open-meteo.com/v1/elevation?{params}"
    with urllib.request.urlopen(url, timeout=60) as resp:
        payload = json.load(resp)
    return [max(0.0, float(v)) for v in payload["elevation"]]


def enrich(path: Path) -> None:
    data = json.loads(path.read_text(encoding="utf-8"))
    cities = data["cities"]
    for start in range(0, len(cities), BATCH):
        batch = cities[start : start + BATCH]
        elevs = fetch_elevations(
            [c["latitude"] for c in batch],
            [c["longitude"] for c in batch],
        )
        for city, elev in zip(batch, elevs):
            city["elevationMeters"] = round(elev, 1)
        print(f"{path.name}: {start + len(batch)}/{len(cities)}")
    path.write_text(json.dumps(data, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")


def main() -> None:
    for path in PATHS:
        enrich(path)
    print("Done.")


if __name__ == "__main__":
    main()
