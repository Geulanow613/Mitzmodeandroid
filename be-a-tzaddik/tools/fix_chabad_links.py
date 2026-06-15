"""Replace broken or legacy Chabad.org URLs across Kotlin and JSON sources."""
from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]

# Verified working library/article URLs (aid-based; stable on Chabad.org).
REPLACEMENTS: dict[str, str] = {
    # ── Confirmed HTTP 404 ───────────────────────────────────────────────
    "https://www.chabad.org/holidays/10tevet/default_cdo/jewish/Asarah-BeTevet.htm": (
        "https://www.chabad.org/library/article_cdo/aid/3170662/jewish/What-Is-Asarah-BTevet-Tevet-10.htm"
    ),
    "https://www.chabad.org/holidays/17tammuz/default_cdo/jewish/17-Tammuz.htm": (
        "https://www.chabad.org/library/article_cdo/aid/479885/jewish/The-17th-of-Tammuz.htm"
    ),
    "https://www.chabad.org/holidays/YomKippur/default_cdo/jewish/Yom-Kippur.htm": (
        "https://www.chabad.org/library/article_cdo/aid/4688/jewish/What-Is-Yom-Kippur.htm"
    ),
    "https://www.chabad.org/holidays/fastofgedaliah/default_cdo/jewish/Fast-of-Gedaliah.htm": (
        "https://www.chabad.org/library/article_cdo/aid/2316462/jewish/Tzom-Gedaliah-Fast-Day.htm"
    ),
    "https://www.chabad.org/holidays/lagbaomer/default_cdo/jewish/Lag-BaOmer.htm": (
        "https://www.chabad.org/library/article_cdo/aid/679300/jewish/What-Is-Lag-BaOmer.htm"
    ),
    "https://www.chabad.org/holidays/sukkos/article_cdo/aid/1695/jewish/Shemini-Atzeret-Simchat-Torah.htm": (
        "https://www.chabad.org/library/article_cdo/aid/4464/jewish/What-Is-Shemini-Atzeret-Simchat-Torah.htm"
    ),
    "https://www.chabad.org/holidays/sukkos/article_cdo/aid/2474/jewish/Hoshana-Rabbah.htm": (
        "https://www.chabad.org/library/article_cdo/aid/757453/jewish/Hoshana-Rabbah.htm"
    ),
    "https://www.chabad.org/holidays/sukkot/article_cdo/aid/1772/jewish/Simchat-Torah.htm": (
        "https://www.chabad.org/library/article_cdo/aid/4464/jewish/What-Is-Shemini-Atzeret-Simchat-Torah.htm"
    ),
    "https://www.chabad.org/holidays/sukkot/article_cdo/aid/1771/jewish/Shemini-Atzeret.htm": (
        "https://www.chabad.org/library/article_cdo/aid/4464/jewish/What-Is-Shemini-Atzeret-Simchat-Torah.htm"
    ),
    "https://www.chabad.org/holidays/tisha_bav/default_cdo/jewish/Tisha-BAv.htm": (
        "https://www.chabad.org/library/article_cdo/aid/144575/jewish/What-Is-Tisha-BAv.htm"
    ),
    # ── Legacy holiday hub paths (often 404 or wrong slug) ───────────────
    "https://www.chabad.org/holidays/JewishNewYear/default_cdo/jewish/Rosh-Hashanah.htm": (
        "https://www.chabad.org/library/article_cdo/aid/4762/jewish/What-Is-Rosh-Hashanah.htm"
    ),
    "https://www.chabad.org/holidays/jewishnewyear/default_cdo/jewish/Jewish-New-Year.htm": (
        "https://www.chabad.org/library/article_cdo/aid/4762/jewish/What-Is-Rosh-Hashanah.htm"
    ),
    "https://www.chabad.org/holidays/yomkippur/default_cdo/jewish/Yom-Kippur.htm": (
        "https://www.chabad.org/library/article_cdo/aid/4688/jewish/What-Is-Yom-Kippur.htm"
    ),
    "https://www.chabad.org/holidays/sukkos/default_cdo/jewish/Sukkot.htm": (
        "https://www.chabad.org/library/article_cdo/aid/4784/jewish/What-Is-Sukkot.htm"
    ),
    "https://www.chabad.org/holidays/sukkot/default_cdo/jewish/Sukkot.htm": (
        "https://www.chabad.org/library/article_cdo/aid/4784/jewish/What-Is-Sukkot.htm"
    ),
    "https://www.chabad.org/holidays/tubishvat/default_cdo/jewish/Tu-BShvat.htm": (
        "https://www.chabad.org/library/article_cdo/aid/468738/jewish/Tu-BiShvat-What-and-How.htm"
    ),
    "https://www.chabad.org/holidays/PesachSheni/default_cdo/jewish/Pesach-Sheni.htm": (
        "https://www.chabad.org/library/article_cdo/aid/4377624/jewish/What-Is-Pesach-Sheni-the-Second-Passover.htm"
    ),
    "https://www.chabad.org/holidays/tubeav/default_cdo/jewish/Tu-BAv.htm": (
        "https://www.chabad.org/library/article_cdo/aid/717167/jewish/7-Joyous-Events-That-Happened-on-the-15th-of-Av.htm"
    ),
    # ── Known dead / wrong links from prior audits ───────────────────────
    "https://www.chabad.org/shabbat": (
        "https://www.chabad.org/library/article_cdo/aid/4687/jewish/Shabbat.htm"
    ),
    "https://www.chabad.org/holidays/purim/article_cdo/aid/1362/jewish/The-Megillah.htm": (
        "https://www.chabad.org/holidays/purim/article_cdo/aid/644322/jewish/Laws-and-Customs.htm"
    ),
    "https://www.chabad.org/library/article_cdo/aid/1363/jewish/Gifts-to-the-Poor.htm": (
        "https://www.chabad.org/holidays/purim/article_cdo/aid/5846239/jewish/Matanot-Laevyonim-FAQs.htm"
    ),
    "https://www.chabad.org/library/article_cdo/aid/1364/jewish/Food-Gifts.htm": (
        "https://www.chabad.org/holidays/purim/article_cdo/aid/261101/jewish/Mishloach-Manot-Sending-Food-Gifts-on-Purim.htm"
    ),
    "https://www.chabad.org/library/article_cdo/aid/5280/jewish/Drinking-on-Chol-Hamoed.htm": (
        "https://www.chabad.org/library/article_cdo/aid/5279/jewish/Chol-Hamoed.htm"
    ),
    "https://www.chabad.org/library/article_cdo/aid/144568/jewish/The-Three-Weeks.htm": (
        "https://www.chabad.org/library/article_cdo/aid/144558/jewish/Tisha-BAv-and-the-3-Weeks.htm"
    ),
}

TARGET_DIRS = [
    ROOT / "shared" / "src" / "commonMain" / "kotlin",
    ROOT / "shared" / "src" / "commonMain" / "composeResources" / "files",
    ROOT / "data",
]
IOS = ROOT.parent / "ios-transfer-handoff" / "be-a-tzaddik"
if IOS.exists():
    TARGET_DIRS.extend(
        [
            IOS / "shared" / "src" / "commonMain" / "kotlin",
            IOS / "shared" / "src" / "commonMain" / "composeResources" / "files",
            IOS / "data",
        ]
    )


def patch_file(path: Path) -> int:
    text = path.read_text(encoding="utf-8")
    original = text
    for old, new in REPLACEMENTS.items():
        text = text.replace(old, new)
    if text != original:
        path.write_text(text, encoding="utf-8")
        return sum(original.count(old) for old in REPLACEMENTS)
    return 0


def main() -> None:
    total = 0
    touched: list[str] = []
    for base in TARGET_DIRS:
        if not base.exists():
            continue
        for path in base.rglob("*"):
            if path.suffix not in (".kt", ".json"):
                continue
            if "build" in path.parts:
                continue
            n = patch_file(path)
            if n:
                total += n
                touched.append(str(path.relative_to(ROOT if str(path).startswith(str(ROOT)) else IOS.parent)))
    print(f"Replaced {total} URL occurrence(s) in {len(touched)} file(s):")
    for t in sorted(set(touched)):
        print(f"  {t}")


if __name__ == "__main__":
    main()
