"""Apply halachic copy fixes: Ein Kelokeinu/Yom Kippur, Melave Malka heading."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]

EIN_OLD = (
    "It is not recited on Yom Kippur (Maharil), when the long liturgy already supplies abundant praise."
)
EIN_NEW = (
    "Note on Yom Kippur: Unlike the minor fasts, Ein Kelokeinu is universally chanted at the "
    "conclusion of the Musaf service (or Ne'ilah) on Yom Kippur across mainstream Ashkenazic, "
    "Sephardic, and Chassidic traditions."
)

MELAVE_OLD_PREFIX = 'Melave Malka ('
MELAVE_NEW_PREFIX = 'Melave Malka:\n\nמְלַוֶּה מַלְכָּה — "escorting the queen" — is a mitzvah'
MELAVE_OLD_SUFFIX = ') is a mitzvah'


def patch_checklist(path: Path) -> list[str]:
    data = json.loads(path.read_text(encoding="utf-8"))
    changed: list[str] = []
    for item in data["items"]:
        exp = item.get("explanation", "")
        if EIN_OLD in exp:
            item["explanation"] = exp.replace(EIN_OLD, EIN_NEW)
            changed.append("ein_kelokeinu")
            exp = item["explanation"]
        if item.get("id") == "melave_malkah" and MELAVE_OLD_PREFIX in exp and MELAVE_OLD_SUFFIX in exp:
            start = exp.index(MELAVE_OLD_PREFIX)
            end = exp.index(MELAVE_OLD_SUFFIX, start) + len(MELAVE_OLD_SUFFIX)
            item["explanation"] = exp[:start] + MELAVE_NEW_PREFIX + exp[end:]
            changed.append("melave_malka")
    path.write_text(json.dumps(data, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    return changed


def main() -> None:
    paths = [
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
    for path in paths:
        if not path.exists():
            print("skip missing", path)
            continue
        print(path, patch_checklist(path))


if __name__ == "__main__":
    main()
