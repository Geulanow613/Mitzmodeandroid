#!/usr/bin/env python3
"""Add elevationMeters to manual-cities.json via Open-Meteo (free, no API key)."""
import json
import time
import urllib.error
import urllib.parse
import urllib.request
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
PATHS = [
    ROOT / "shared" / "src" / "commonMain" / "composeResources" / "files" / "manual-cities.json",
    ROOT / "data" / "manual-cities.json",
]
BATCH = 50
SLEEP_SEC = 2.0
MAX_RETRIES = 5


def fetch_elevations(lats: list[float], lons: list[float]) -> list[float]:
    params = urllib.parse.urlencode(
        [
            ("latitude", ",".join(f"{lat:.6f}" for lat in lats)),
            ("longitude", ",".join(f"{lon:.6f}" for lon in lons)),
        ]
    )
    url = f"https://api.open-meteo.com/v1/elevation?{params}"
    for attempt in range(MAX_RETRIES):
        try:
            with urllib.request.urlopen(url, timeout=60) as resp:
                payload = json.load(resp)
            return [max(0.0, float(v)) for v in payload["elevation"]]
        except urllib.error.HTTPError as e:
            if e.code == 429 and attempt + 1 < MAX_RETRIES:
                wait = SLEEP_SEC * (2**attempt)
                print(f"  rate limited, sleeping {wait:.0f}s ...")
                time.sleep(wait)
                continue
            raise
    raise RuntimeError("unreachable")


def enrich(path: Path) -> None:
    data = json.loads(path.read_text(encoding="utf-8"))
    cities = data["cities"]
    pending = [c for c in cities if not c.get("elevationMeters")]
    print(f"{path.name}: {len(pending)} cities need elevation ({len(cities)} total)")
    for start in range(0, len(pending), BATCH):
        batch = pending[start : start + BATCH]
        elevs = fetch_elevations(
            [c["latitude"] for c in batch],
            [c["longitude"] for c in batch],
        )
        for city, elev in zip(batch, elevs):
            city["elevationMeters"] = round(elev, 1)
        print(f"{path.name}: {min(start + len(batch), len(pending))}/{len(pending)}")
        path.write_text(json.dumps(data, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")
        if start + BATCH < len(pending):
            time.sleep(SLEEP_SEC)


def main() -> None:
    for path in PATHS:
        enrich(path)
    print("Done.")


if __name__ == "__main__":
    main()
