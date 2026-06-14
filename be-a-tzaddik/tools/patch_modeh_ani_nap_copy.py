"""Add Modeh Ani / negel vasser daytime-nap guidance to checklist explainers."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]

MODEH_OLD = (
    "How to do it:\n"
    "Say it while still in bed, before getting up. Modeh Ani is one of the only parts of davening "
    "you can say without washing your hands first — then proceed to Netilat Yadayim (see the next item)."
)
MODEH_NEW = (
    "How to do it:\n"
    "Say it while still in bed, before getting up, immediately upon waking from your primary "
    "nighttime sleep — before doing anything else. Modeh Ani is one of the only parts of davening "
    "you can say without washing your hands first — then proceed to Netilat Yadayim (negel vasser; "
    "see the next item).\n\n"
    "Nighttime sleep vs daytime naps:\n"
    "You may repeat these words anytime as personal gratitude or mindfulness, but that is not the "
    "official morning prayer and does not carry the same halachic status.\n\n"
    "Traditional practice reserves Modeh Ani for waking from major nighttime sleep. After a daytime "
    "nap, it is not required and is typically not recited — simply wash your hands (negel vasser) "
    "per the rules in the next item."
)

NETILAT_OLD = "After naps:\n• Nap under ~30 minutes:"
NETILAT_NEW = (
    "After naps (Modeh Ani is not recited):\n"
    "When waking from a daytime nap, Modeh Ani is typically omitted — wash your hands (negel vasser) "
    "per the rules below:\n"
    "• Nap under ~30 minutes:"
)

PATHS = [
    ROOT / "data" / "checklist-items.json",
    ROOT / "shared" / "src" / "commonMain" / "composeResources" / "files" / "checklist-items.json",
    ROOT.parent / "ios-transfer-handoff" / "be-a-tzaddik" / "data" / "checklist-items.json",
    ROOT.parent
    / "ios-transfer-handoff"
    / "be-a-tzaddik"
    / "shared"
    / "src"
    / "commonMain"
    / "composeResources"
    / "files"
    / "checklist-items.json",
]

NETILAT_KEYS = (
    "explanation",
    "explanationAshkenaz",
    "explanationSefard",
    "explanationChabad",
    "explanationEdotHamizrach",
)


def patch_file(path: Path) -> list[str]:
    data = json.loads(path.read_text(encoding="utf-8"))
    changed: list[str] = []
    for item in data["items"]:
        if item.get("id") == "modeh_ani_upon_waking":
            exp = item.get("explanation", "")
            if MODEH_OLD in exp:
                item["explanation"] = exp.replace(MODEH_OLD, MODEH_NEW)
                changed.append("modeh_ani")
        if item.get("id") == "ritual_hand_washing":
            for key in NETILAT_KEYS:
                exp = item.get(key, "")
                if exp and NETILAT_OLD in exp:
                    item[key] = exp.replace(NETILAT_OLD, NETILAT_NEW)
                    changed.append(f"netilat:{key}")
    path.write_text(json.dumps(data, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    return changed


def main() -> None:
    for path in PATHS:
        if not path.exists():
            print("skip", path)
            continue
        print(path.name, patch_file(path))


if __name__ == "__main__":
    main()
