#!/usr/bin/env python3
import json
from collections import Counter
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
data = json.loads((ROOT / "shared/src/commonMain/composeResources/files/manual-cities.json").read_text(encoding="utf-8"))
CANADA = {"ON", "QC", "BC", "AB", "MB", "SK", "NS", "NB", "NL", "PE"}


def display_label(city: dict) -> str:
    label = city["label"]
    suffix = label.rsplit(", ", 1)[1].strip() if ", " in label else ""
    if len(suffix) == 2 and suffix.isupper():
        country = "Canada" if suffix in CANADA else "USA"
    elif suffix:
        country = suffix
    elif city["timezoneId"] == "Asia/Jerusalem":
        country = "Israel"
    elif city["timezoneId"] == "Asia/Singapore":
        country = "Singapore"
    elif city["timezoneId"] == "Asia/Hong_Kong":
        country = "Hong Kong"
    elif city["timezoneId"].startswith("Australia/"):
        country = "Australia"
    elif city["timezoneId"] == "Pacific/Auckland":
        country = "New Zealand"
    else:
        country = "Unknown"
    if label.endswith(", " + country):
        return label
    return f"{label}, {country}"


def parse_parts(city: dict) -> tuple[str, str | None, str]:
    label = city["label"]
    d = display_label(city)
    country = d.rsplit(", ", 1)[-1]
    rest = d[: -len(country) - 2]
    if ", " in rest:
        city_name, region = rest.rsplit(", ", 1)
        return city_name.strip(), region.strip(), country
    return rest.strip(), None, country


cities = data["cities"]
labels = {c["id"]: display_label(c) for c in cities}
parts = [parse_parts(c) for c in cities]
city_names = Counter(p[0] for p in parts)
regions = Counter(p[1] for p in parts if p[1])
countries = Counter(p[2] for p in parts)
print("cities", len(cities))
print("unique display labels", len(set(labels.values())))
print("unique city names", len(city_names))
print("unique regions", len(regions))
print("unique countries", len(countries))
print("countries:", sorted(countries.keys()))
print("regions sample:", sorted(regions.keys())[:40])
