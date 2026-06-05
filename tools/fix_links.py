#!/usr/bin/env python3
"""Apply verified URL replacements across be-a-tzaddik checklist sources."""
from __future__ import annotations

from pathlib import Path

ROOT = Path(__file__).resolve().parents[1] / "be-a-tzaddik"
SKIP_NAMES = {"mitzvotlistfull.json", "mitzvotcloud.json"}
SUFFIXES = {".kt", ".json", ".md"}

REPLACEMENTS: list[tuple[str, str]] = [
    # Chabad — verified working article IDs
    (
        "https://www.chabad.org/library/article_cdo/aid/253326/jewish/Kiddush.htm",
        "https://www.chabad.org/library/article_cdo/aid/610626/jewish/Kiddush.htm",
    ),
    (
        "https://www.chabad.org/library/article_cdo/aid/4802/jewish/Sefirat-Haomer.htm",
        "https://www.chabad.org/library/article_cdo/aid/130631/jewish/Sefirat-HaOmer.htm",
    ),
    (
        "https://www.chabad.org/library/article_cdo/aid/2263/jewish/Sefirah.htm",
        "https://www.chabad.org/library/article_cdo/aid/130631/jewish/Sefirat-HaOmer.htm",
    ),
    # Peninei Halakha — old chapter index URLs
    ("https://ph.yhb.org.il/en/05-01-00/", "https://ph.yhb.org.il/en/05-01-01/"),
    ("https://ph.yhb.org.il/en/03-14-00/", "https://ph.yhb.org.il/en/category/zemanim/05-15/"),
    ("https://ph.yhb.org.il/en/03-16-00/", "https://ph.yhb.org.il/en/category/zemanim/05-02/"),
    ("https://ph.yhb.org.il/en/04-09-00/", "https://ph.yhb.org.il/en/12-11-01/"),
    ("https://ph.yhb.org.il/en/12-00-00/", "https://ph.yhb.org.il/en/05-12-01/"),
    ("https://ph.yhb.org.il/en/13-05-00/", "https://ph.yhb.org.il/en/13-05-01/"),
    ("https://ph.yhb.org.il/en/13-08-00/", "https://ph.yhb.org.il/en/category/13/13-07/"),
    ("https://ph.yhb.org.il/en/13-09-00/", "https://ph.yhb.org.il/en/category/13/13-07/"),
    ("https://ph.yhb.org.il/en/03-05-00/", "https://ph.yhb.org.il/en/category/15/15-03/"),
    ("https://ph.yhb.org.il/en/03-09-00/", "https://ph.yhb.org.il/en/category/15/15-06/"),
    ("https://ph.yhb.org.il/en/03-07-00/", "https://ph.yhb.org.il/en/category/12/12-13/"),
    ("https://ph.yhb.org.il/en/06-11-00/", "https://ph.yhb.org.il/tu-beshvat/"),
    (
        "https://ph.yhb.org.il/en/category/moadim/04-pesach/",
        "https://ph.yhb.org.il/en/04-03-01/",
    ),
    (
        "https://ph.yhb.org.il/en/category/moadim/13-sukkot/",
        "https://ph.yhb.org.il/en/category/13/13-01/",
    ),
    (
        "https://ph.yhb.org.il/en/category/moadim/08-the-three-weeks/",
        "https://ph.yhb.org.il/en/category/zemanim/05-08/",
    ),
    (
        "https://ph.yhb.org.il/en/category/moadim/02-days-of-awe/",
        "https://ph.yhb.org.il/en/category/15/15-01/",
    ),
    ("https://ph.yhb.org.il/en/category/moadim/", "https://ph.yhb.org.il/en/category/zemanim/"),
    # Aish
    ("https://aish.com/roshchodesh/", "https://aish.com/43-rosh-chodesh-2/"),
    ("https://aish.com/48968286/", "https://aish.com/slichot_and_the_13_attributes/"),
    ("https://aish.com/48943186/", "https://aish.com/48943916/"),
    ("https://aish.com/passover-preparation-guide/", "https://aish.com/holidays/pesach/"),
    # Ohr Somayach — yhiy paths retired
    ("https://ohr.edu/yhiy/sukkot/", "https://ohr.edu/holidays/succos/"),
    ("https://ohr.edu/yhiy/simchat-torah/", "https://ohr.edu/holidays/simchat_torah/"),
    ("https://ohr.edu/yhiy/purim/", "https://ohr.edu/1508"),
    ("https://ohr.edu/yhiy/rosh-hashana/", "https://ohr.edu/holidays/rosh_hashana/"),
    ("https://ohr.edu/yhiy/yom-kippur/", "https://ohr.edu/holidays/yom_kippur/"),
    ("https://ohr.edu/yhiy/pesach/", "https://ohr.edu/holidays/pesach/"),
    ("https://ohr.edu/yhiy/yom-tov/", "https://ohr.edu/holidays/yom_tov/"),
    (
        "https://ohr.edu/lessons/sefiras-haomer",
        "https://ohr.edu/this_week/prayer_essentials/7338",
    ),
    # Sefaria — canonical text refs
    ("https://www.sefaria.org/Megillat_Esther", "https://www.sefaria.org/Esther"),
    ("https://www.sefaria.org/Sefirat_HaOmer", "https://www.sefaria.org/Numbers.28.26"),
]


def main() -> None:
    changed_files: list[tuple[Path, int]] = []
    for path in sorted(ROOT.rglob("*")):
        if path.suffix not in SUFFIXES:
            continue
        if path.name in SKIP_NAMES:
            continue
        if "ios-transfer-handoff" in str(path):
            continue
        text = path.read_text(encoding="utf-8")
        original = text
        for old, new in REPLACEMENTS:
            text = text.replace(old, new)
        if text != original:
            path.write_text(text, encoding="utf-8", newline="\n")
            count = sum(original.count(old) for old, _ in REPLACEMENTS)
            changed_files.append((path, count))
    print(f"Updated {len(changed_files)} files")
    for path, _ in changed_files:
        rel = path.relative_to(ROOT.parent)
        print(f"  {rel}")


if __name__ == "__main__":
    main()
