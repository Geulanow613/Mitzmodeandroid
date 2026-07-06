#!/usr/bin/env python3
"""
Append world cities from GeoNames cities1000.zip to manual_cities.tsv.

GeoNames data is CC BY 4.0 — https://www.geonames.org/
Keeps every existing row; adds populated places not within MIN_DISTANCE_KM of a
city already in the list (population >= MIN_POPULATION).

Usage:
  python expand_manual_cities_from_geonames.py
  python gen_manual_cities.py
  python add_city_elevations.py   # optional; fetches elevation for new rows
"""
from __future__ import annotations

import math
import re
import urllib.request
import zipfile
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
TSV = ROOT / "tools" / "manual_cities.tsv"
CACHE = ROOT / "tools" / ".geonames_cache"
CITIES_URL = "https://download.geonames.org/export/dump/cities1000.zip"
COUNTRY_URL = "https://download.geonames.org/export/dump/countryInfo.txt"
ADMIN1_URL = "https://download.geonames.org/export/dump/admin1CodesASCII.txt"

MIN_POPULATION = 100_000
MIN_DISTANCE_KM = 30.0
FEATURE_CODES = frozenset({"PPL", "PPLA", "PPLA2", "PPLA3", "PPLA4", "PPLA5", "PPLC", "PPLCH", "PPLF", "PPLG", "PPLL", "PPLR", "PPLS", "PPLW", "PPLX"})


def haversine_km(lat1: float, lon1: float, lat2: float, lon2: float) -> float:
    r = 6_371.0
    p1, p2 = math.radians(lat1), math.radians(lat2)
    dlat = math.radians(lat2 - lat1)
    dlon = math.radians(lon2 - lon1)
    a = math.sin(dlat / 2) ** 2 + math.cos(p1) * math.cos(p2) * math.sin(dlon / 2) ** 2
    return r * 2 * math.atan2(math.sqrt(a), math.sqrt(1 - a))


def slugify(text: str) -> str:
    s = text.lower().strip()
    s = re.sub(r"[^a-z0-9]+", "_", s)
    s = re.sub(r"_+", "_", s).strip("_")
    return s or "city"


def download(url: str, dest: Path) -> None:
    dest.parent.mkdir(parents=True, exist_ok=True)
    if dest.exists() and dest.stat().st_size > 0:
        return
    print(f"Downloading {url} ...")
    urllib.request.urlretrieve(url, dest)


def load_country_names() -> dict[str, str]:
    path = CACHE / "countryInfo.txt"
    download(COUNTRY_URL, path)
    names: dict[str, str] = {}
    for line in path.read_text(encoding="utf-8").splitlines():
        if not line or line.startswith("#"):
            continue
        parts = line.split("\t")
        if len(parts) >= 5:
            names[parts[0]] = parts[4]
    return names


def load_admin1_names() -> dict[tuple[str, str], str]:
    path = CACHE / "admin1CodesASCII.txt"
    download(ADMIN1_URL, path)
    names: dict[tuple[str, str], str] = {}
    for line in path.read_text(encoding="utf-8").splitlines():
        if not line or line.startswith("#"):
            continue
        code, name = line.split("\t", 1)[0], line.split("\t")[1]
        country, admin1 = code.split(".", 1)
        names[(country, admin1)] = name
    return names


def parse_existing_tsv() -> tuple[list[str], list[dict]]:
    header_lines: list[str] = []
    rows: list[dict] = []
    for line in TSV.read_text(encoding="utf-8").splitlines():
        if not line.strip() or line.strip().startswith("#"):
            header_lines.append(line)
            continue
        parts = line.split("\t")
        if len(parts) != 5:
            raise ValueError(f"Bad TSV row: {line[:80]}")
        rows.append(
            {
                "id": parts[0],
                "label": parts[1],
                "latitude": float(parts[2]),
                "longitude": float(parts[3]),
                "timezoneId": parts[4],
            }
        )
    return header_lines, rows


def load_geonames_cities() -> list[dict]:
    zip_path = CACHE / "cities1000.zip"
    download(CITIES_URL, zip_path)
    txt_name = "cities1000.txt"
    with zipfile.ZipFile(zip_path) as zf:
        with zf.open(txt_name) as f:
            text = f.read().decode("utf-8")
    cities: list[dict] = []
    for line in text.splitlines():
        parts = line.split("\t")
        if len(parts) < 19:
            continue
        feature = parts[7]
        if feature not in FEATURE_CODES:
            continue
        population = int(parts[14] or "0")
        if population < MIN_POPULATION:
            continue
        tz = parts[17].strip()
        if not tz or "/" not in tz:
            continue
        cities.append(
            {
                "name": parts[1] or parts[2],
                "asciiname": parts[2] or parts[1],
                "latitude": float(parts[4]),
                "longitude": float(parts[5]),
                "country": parts[8],
                "admin1": parts[10],
                "population": population,
                "timezoneId": tz,
                "geonameid": parts[0],
            }
        )
    cities.sort(key=lambda c: (-c["population"], c["asciiname"]))
    return cities


def format_label(city: dict, country_names: dict[str, str], admin1_names: dict[tuple[str, str], str]) -> str:
    name = city["asciiname"]
    cc = city["country"]
    admin1 = city["admin1"]
    country = country_names.get(cc, cc)
    if cc in {"US", "CA", "AU"} and admin1:
        region = admin1 if cc == "US" else admin1_names.get((cc, admin1), admin1)
        return f"{name}, {region}"
    return f"{name}, {country}"


def make_id(city: dict, used: set[str]) -> str:
    base = slugify(city["asciiname"])
    if city["country"] != "US":
        base = f"{base}_{city['country'].lower()}"
    else:
        base = f"{base}_{city['admin1'].lower()}" if city["admin1"] else f"{base}_us"
    candidate = base
    n = 2
    while candidate in used:
        candidate = f"{base}_{n}"
        n += 1
    return candidate


def too_close(lat: float, lon: float, kept: list[dict]) -> bool:
    for other in kept:
        if haversine_km(lat, lon, other["latitude"], other["longitude"]) < MIN_DISTANCE_KM:
            return True
    return False


def main() -> None:
    header_lines, existing = parse_existing_tsv()
    country_names = load_country_names()
    admin1_names = load_admin1_names()
    candidates = load_geonames_cities()

    kept = list(existing)
    used_ids = {r["id"] for r in existing}
    added: list[dict] = []

    for city in candidates:
        if too_close(city["latitude"], city["longitude"], kept):
            continue
        row = {
            "id": make_id(city, used_ids),
            "label": format_label(city, country_names, admin1_names),
            "latitude": round(city["latitude"], 4),
            "longitude": round(city["longitude"], 4),
            "timezoneId": city["timezoneId"],
        }
        used_ids.add(row["id"])
        kept.append(row)
        added.append(row)

    lines = list(header_lines)
    for row in existing:
        lines.append(
            f"{row['id']}\t{row['label']}\t{row['latitude']}\t{row['longitude']}\t{row['timezoneId']}"
        )
    if added:
        if lines and lines[-1].strip():
            lines.append("")
        lines.append(f"# GeoNames expansion (pop >= {MIN_POPULATION}, min {MIN_DISTANCE_KM} km apart)")
        lines.append("# Data (c) GeoNames.org, CC BY 4.0")
        for row in added:
            lines.append(
                f"{row['id']}\t{row['label']}\t{row['latitude']}\t{row['longitude']}\t{row['timezoneId']}"
            )

    TSV.write_text("\n".join(lines) + "\n", encoding="utf-8")
    print(f"Existing: {len(existing)}")
    print(f"Added:    {len(added)}")
    print(f"Total:    {len(kept)}")
    print(f"Wrote {TSV}")
    print("Next: python gen_manual_cities.py && python add_city_elevations.py")


if __name__ == "__main__":
    main()
