#!/usr/bin/env python3
"""Audit HTTP links in be-a-tzaddik checklist sources (excludes mitzvot JSON)."""
from __future__ import annotations

import re
import ssl
import urllib.error
import urllib.parse
import urllib.request
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1] / "be-a-tzaddik"
SKIP_NAMES = {"mitzvotlistfull.json", "mitzvotcloud.json"}
URL_RE = re.compile(r"https?://[^\s\"')\]>]+")
HEADERS = {"User-Agent": "Mozilla/5.0 (compatible; MitzModeLinkAudit/1.0)"}
CTX = ssl.create_default_context()


SKIP_DIR_PARTS = {
    "build",
    ".kotlin",
    ".gradle",
    "ios-transfer-handoff",
}


def collect_urls() -> set[str]:
    urls: set[str] = set()
    for path in ROOT.rglob("*"):
        if path.suffix not in {".kt", ".json", ".md"}:
            continue
        if path.name in SKIP_NAMES:
            continue
        if any(part in SKIP_DIR_PARTS for part in path.parts):
            continue
        text = path.read_text(encoding="utf-8", errors="ignore")
        for match in URL_RE.findall(text):
            url = match.rstrip(".,;")
            if "${" in url or url.endswith("…") or url == "https://":
                continue
            urls.add(url)
    return urls


def is_soft_ok(url: str, code: int | None) -> bool:
    """Sites that block bots but work in browsers."""
    if code in {403, 405, 429}:
        host = urllib.parse.urlparse(url).netloc.lower()
        if any(
            token in host
            for token in (
                "chabad.org",
                "sefaria.org",
                "halachipedia.com",
                "aish.com",
            )
        ):
            return True
    return False


def check_url(url: str) -> tuple[int | None, str]:
    for method in ("HEAD", "GET"):
        req = urllib.request.Request(url, headers=HEADERS, method=method)
        try:
            with urllib.request.urlopen(req, timeout=20, context=CTX) as resp:
                return resp.status, "ok"
        except urllib.error.HTTPError as exc:
            if is_soft_ok(url, exc.code):
                return 200, "soft-ok"
            if exc.code in {403, 405, 429} and method == "HEAD":
                continue
            return exc.code, str(exc)
        except Exception as exc:  # noqa: BLE001
            detail = str(exc)[:160]
            if "CERTIFICATE_VERIFY_FAILED" in detail:
                return 200, "ssl-soft-ok"
            if method == "HEAD":
                continue
            return None, detail
    return None, "unreachable"


def main() -> None:
    urls = sorted(collect_urls())
    print(f"TOTAL {len(urls)}")
    broken: list[tuple[str, str]] = []
    for url in urls:
        code, detail = check_url(url)
        if code is None or code >= 400:
            broken.append((url, f"{code} {detail}"))
    print(f"BROKEN {len(broken)}")
    for url, err in broken:
        print(f"{err}\t{url}")


if __name__ == "__main__":
    main()
