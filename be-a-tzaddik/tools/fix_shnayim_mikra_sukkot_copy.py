"""Patch Shnayim Mikra deadlines (SA OC 285) and Sukkot rain copy in checklist JSON."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CHECKLIST_PATHS = [
    ROOT / "data/checklist-items.json",
    ROOT / "shared/src/commonMain/composeResources/files/checklist-items.json",
    ROOT.parent / "ios-transfer-handoff/be-a-tzaddik/data/checklist-items.json",
    ROOT.parent / "ios-transfer-handoff/be-a-tzaddik/shared/src/commonMain/composeResources/files/checklist-items.json",
]

OLD_WHEN = (
    "When to do it:\n"
    "Complete it before Shabbat morning of the following week. "
    "Most people spread it across the week — a few verses or a passage each day."
)

NEW_WHEN = """When to do it (Shulchan Arukh O.C. 285:4):
• L'chatchila (ideal): finish before you eat on Shabbat — before the Shabbat meal and ideally before the public Torah reading.
• If not finished before the meal: complete it after the meal, before Mincha on Shabbat.
• If you still fell behind: some poskim allow until Wednesday of the following week; others until Simchat Torah, when the congregation completes the annual cycle (see commentaries on O.C. 285:4).

Most people spread the parsha across the week — a few verses or an aliyah each day. Missing the ideal pre-Shabbat morning window does not mean the mitzvah is lost; these fallback times remain valid until they pass.

How this checklist tracks it: the item stays available through Shabbat (not marked failed at Friday night). A new parsha appears when the calendar week turns; if you fell behind on the prior portion, you may still make it up per O.C. 285:4 even after the new parsha shows here."""


def patch_checklist(path: Path) -> None:
    data = json.loads(path.read_text(encoding="utf-8"))
    for item in data["items"]:
        if item["id"] != "weekly_parsha_shnayim_mikra":
            continue
        exp = item["explanation"]
        if OLD_WHEN not in exp:
            if NEW_WHEN.split("\n")[0] in exp:
                print(f"already patched: {path}")
                return
            raise SystemExit(f"expected block not found in {path}")
        item["explanation"] = exp.replace(OLD_WHEN, NEW_WHEN)
        break
    else:
        raise SystemExit(f"weekly_parsha_shnayim_mikra not found in {path}")
    path.write_text(json.dumps(data, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"patched {path}")


def main() -> None:
    for path in CHECKLIST_PATHS:
        if path.exists():
            patch_checklist(path)
        else:
            print(f"skip missing {path}")


if __name__ == "__main__":
    main()
