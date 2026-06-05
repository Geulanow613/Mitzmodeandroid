"""Remove prohibition-framed checklist rows; keep positive everyday practices."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
CHECKLIST = ROOT / "shared/src/commonMain/composeResources/files/checklist-items.json"
DATA_COPY = ROOT / "data/checklist-items.json"

REMOVE_IDS = {
    "guard_against_forbidden_relationships",
    "do_not_publicly_embarrass",
    "forgive_and_do_not_take_revenge",
    "rebuke_with_love_not_hatred",
    "believe_in_god_and_torah",
}

TITLE_UPDATES = {
    "avoid_lashon_hara": "Speak carefully about others (lashon hara)",
    "avoid_rechilut_tale_bearing": "Build peace — avoid rechilus (tale-bearing)",
    "electronics_shabbat": "Shabbat rest — phones and melacha off",
    "no_chametz_pesach": "Keep the home kosher for Passover",
}


def main() -> None:
    data = json.loads(CHECKLIST.read_text(encoding="utf-8"))
    before = len(data["items"])
    data["items"] = [it for it in data["items"] if it["id"] not in REMOVE_IDS]
    for it in data["items"]:
        if it["id"] in TITLE_UPDATES:
            it["title"] = TITLE_UPDATES[it["id"]]
    data["version"] = 5
    text = json.dumps(data, ensure_ascii=False, indent=2)
    CHECKLIST.write_text(text, encoding="utf-8")
    DATA_COPY.write_text(text, encoding="utf-8")
    print(f"Removed {before - len(data['items'])} items; now {len(data['items'])} items (v{data['version']})")


if __name__ == "__main__":
    main()
