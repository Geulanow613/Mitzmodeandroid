"""Prepare-for-Shabbat section on erev; remove in-app Shabbat-day checklist rows."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CHECKLIST = ROOT / "shared/src/commonMain/composeResources/files/checklist-items.json"
DATA = ROOT / "data/checklist-items.json"

REMOVE_IDS = {"electronics_shabbat"}


def main() -> None:
    data = json.loads(CHECKLIST.read_text(encoding="utf-8"))
    data["items"] = [it for it in data["items"] if it["id"] not in REMOVE_IDS]
    for it in data["items"]:
        if it["id"] == "prepare_for_and_observe_shabbat_and_festivals":
            it["section"] = "Prepare for Shabbat"
            it["title"] = "Prepare for Shabbat (before candle lighting)"
            it["shabbatEveOnly"] = True
            it["hideOnShabbat"] = False
            it["sortOrder"] = 10
            it["explanation"] = (
                "Use the app before Shabbat begins to review what to prepare:\n\n"
                "• Finish cooking and set the table\n"
                "• Warm food and set timers before candle lighting\n"
                "• Lay out clothes, books, and anything you need until after Shabbat\n"
                "• Light Shabbat candles before sunset (see the next item)\n\n"
                "Once Shabbat starts, put away your phone — the app will pause until Havdalah."
            )
        if it["id"] == "shabbat_candles":
            it["section"] = "Prepare for Shabbat"
            it["sortOrder"] = 20
    data["version"] = 8
    text = json.dumps(data, ensure_ascii=False, indent=2)
    CHECKLIST.write_text(text, encoding="utf-8")
    DATA.write_text(text, encoding="utf-8")
    print(f"checklist v{data['version']}, {len(data['items'])} items")


if __name__ == "__main__":
    main()
