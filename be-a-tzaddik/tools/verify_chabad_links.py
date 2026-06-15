"""Verify all chabad.org URLs referenced in Kotlin/JSON source files."""
import json
import re
import urllib.request
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
PATTERN = re.compile(r"https://www\.chabad\.org[^\s\"'\\)>]+")


def collect_urls() -> dict[str, set[str]]:
    urls: dict[str, set[str]] = {}
    for path in ROOT.rglob("*"):
        if path.suffix not in (".kt", ".json"):
            continue
        if any(part in path.parts for part in ("build", ".gradle", "node_modules", "tools")):
            continue
        try:
            text = path.read_text(encoding="utf-8")
        except OSError:
            continue
        for match in PATTERN.finditer(text):
            url = match.group(0).rstrip(".,)")
            rel = str(path.relative_to(ROOT)).replace("\\", "/")
            urls.setdefault(url, set()).add(rel)
    return urls


def check(url: str) -> tuple[int | str, str | None]:
    req = urllib.request.Request(
        url,
        method="GET",
        headers={"User-Agent": "Mozilla/5.0 BeATzaddikLinkCheck/1.0"},
    )
    try:
        with urllib.request.urlopen(req, timeout=25) as resp:
            return resp.status, resp.geturl()
    except Exception as exc:
        code = getattr(exc, "code", None)
        return code if code is not None else str(exc)[:100], None


def main() -> None:
    urls = collect_urls()
    bad: list[tuple[str, int | str, set[str]]] = []
    for url in sorted(urls):
        status, _final = check(url)
        ok = status == 200
        print(f"{'OK' if ok else 'BAD'}\t{status}\t{url}")
        if not ok:
            bad.append((url, status, urls[url]))

    report = ROOT / "tools" / "chabad_link_audit.json"
    report.write_text(
        json.dumps(
            {
                "total": len(urls),
                "bad_count": len(bad),
                "bad": [
                    {"url": u, "status": s, "files": sorted(files)}
                    for u, s, files in bad
                ],
            },
            indent=2,
        ),
        encoding="utf-8",
    )
    print(f"\nTotal unique: {len(urls)}, bad: {len(bad)}")
    print(f"Report: {report}")


if __name__ == "__main__":
    main()
