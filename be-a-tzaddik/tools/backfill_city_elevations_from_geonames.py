#!/usr/bin/env python3
"""Backfill elevationMeters from GeoNames cities1000 DEM (offline, no API)."""
import json
import math
import zipfile
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CACHE = ROOT / "tools" / ".geonames_cache" / "cities1000.zip"
PATHS = [
    ROOT / "shared" / "src" / "commonMain" / "composeResources" / "files" / "manual-cities.json",
    ROOT / "data" / "manual-cities.json",
]
MAX_MATCH_KM = 15.0


def haversine_km(lat1: float, lon1: float, lat2: float, lon2: float) -> float:
    r = 6_371.0
    p1, p2 = math.radians(lat1), math.radians(lat2)
    dlat = math.radians(lat2 - lat1)
    dlon = math.radians(lon2 - lon1)
    a = math.sin(dlat / 2) ** 2 + math.cos(p1) * math.cos(p2) * math.sin(dlon / 2) ** 2
    return r * 2 * math.atan2(math.sqrt(a), math.sqrt(1 - a))


def load_geonames_dem() -> tuple[
    dict[tuple[float, float], float],
    dict[tuple[int, int], list[tuple[float, float, float]]],
]:
    exact: dict[tuple[float, float], float] = {}
    points: list[tuple[float, float, float]] = []
    with zipfile.ZipFile(CACHE) as zf:
        with zf.open("cities1000.txt") as f:
            for raw in f:
                parts = raw.decode("utf-8").split("\t")
                if len(parts) < 17:
                    continue
                dem = int(parts[15] or "0")
                if dem <= 0:
                    continue
                lat = round(float(parts[4]), 4)
                lon = round(float(parts[5]), 4)
                exact[(lat, lon)] = float(dem)
                points.append((float(parts[4]), float(parts[5]), float(dem)))
    return exact, build_dem_grid(points)


def build_dem_grid(points: list[tuple[float, float, float]]) -> dict[tuple[int, int], list[tuple[float, float, float]]]:
    grid: dict[tuple[int, int], list[tuple[float, float, float]]] = {}
    for lat, lon, dem in points:
        key = (int(lat), int(lon))
        grid.setdefault(key, []).append((lat, lon, dem))
    return grid


def nearest_dem(
    lat: float,
    lon: float,
    grid: dict[tuple[int, int], list[tuple[float, float, float]]],
) -> float | None:
    best_dem: float | None = None
    best_km = MAX_MATCH_KM
    base_lat, base_lon = int(lat), int(lon)
    for dlat in (-1, 0, 1):
        for dlon in (-1, 0, 1):
            for plat, plon, dem in grid.get((base_lat + dlat, base_lon + dlon), ()):
                km = haversine_km(lat, lon, plat, plon)
                if km < best_km:
                    best_km = km
                    best_dem = dem
    return best_dem


def enrich(
    path: Path,
    exact: dict[tuple[float, float], float],
    grid: dict[tuple[int, int], list[tuple[float, float, float]]],
) -> None:
    data = json.loads(path.read_text(encoding="utf-8"))
    filled = 0
    for city in data["cities"]:
        if city.get("elevationMeters", 0) > 0:
            continue
        key = (round(city["latitude"], 4), round(city["longitude"], 4))
        dem = exact.get(key) or nearest_dem(city["latitude"], city["longitude"], grid)
        if dem is not None:
            city["elevationMeters"] = round(dem, 1)
            filled += 1
    path.write_text(json.dumps(data, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")
    print(f"{path.name}: filled {filled} elevations")


def main() -> None:
    if not CACHE.is_file():
        raise SystemExit(f"Missing {CACHE} — run expand_manual_cities_from_geonames.py first")
    exact, grid = load_geonames_dem()
    print(f"Loaded {len(exact)} GeoNames DEM points")
    for path in PATHS:
        enrich(path, exact, grid)
    print("Done.")


if __name__ == "__main__":
    main()
