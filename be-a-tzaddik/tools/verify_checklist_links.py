"""Verify HTTP status for all checklist link URLs. Run after editing checklist-items.json."""
import json
import urllib.request
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CHECKLIST = ROOT / "shared/src/commonMain/composeResources/files/checklist-items.json"
EXTRAS = ROOT / "shared/src/commonMain/composeResources/files/nusach-extras.json"


def check(url: str) -> int | str:
    req = urllib.request.Request(url, method="GET", headers={"User-Agent": "BeATzaddikLinkCheck/1.0"})
    try:
        with urllib.request.urlopen(req, timeout=20) as resp:
            return resp.status
    except Exception as e:
        if hasattr(e, "code"):
            return e.code
        return str(e)


def collect_urls(path: Path) -> list[tuple[str, str, str]]:
    data = json.loads(path.read_text(encoding="utf-8"))
    out = []
    for item in data.get("items", []):
        for link in item.get("links", []):
            out.append((item.get("id", "?"), link.get("displayText", ""), link.get("url", "")))
    return out


def main() -> None:
    seen: set[str] = set()
    bad = []
    for path in (CHECKLIST, EXTRAS):
        if not path.exists():
            continue
        for iid, label, url in collect_urls(path):
            if not url or url in seen:
                continue
            seen.add(url)
            status = check(url)
            ok = status == 200
            print(f"{'OK' if ok else 'BAD'} {status} {url}")
            if not ok:
                bad.append((iid, url, status))
    if bad:
        print(f"\n{len(bad)} URLs need attention.")
    else:
        print("\nAll unique URLs returned 200.")


if __name__ == "__main__":
    main()
