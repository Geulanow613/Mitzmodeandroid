"""Split Sephardi vs Edot HaMizrach nusach copy in checklist JSON (sourced text only)."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
PATHS = [
    ROOT / "data/checklist-items.json",
    ROOT / "shared/src/commonMain/composeResources/files/checklist-items.json",
    ROOT.parent / "ios-transfer-handoff/be-a-tzaddik/data/checklist-items.json",
    ROOT.parent / "ios-transfer-handoff/be-a-tzaddik/shared/src/commonMain/composeResources/files/checklist-items.json",
]

NUSACH_EXTRAS_PATHS = [
    ROOT / "data/nusach-extras.json",
    ROOT / "shared/src/commonMain/composeResources/files/nusach-extras.json",
    ROOT.parent / "ios-transfer-handoff/be-a-tzaddik/data/nusach-extras.json",
    ROOT.parent / "ios-transfer-handoff/be-a-tzaddik/shared/src/commonMain/composeResources/files/nusach-extras.json",
]

TACHANUN_SHACHARIT_SEFARD = (
    "Tachanun (Sephardi): On weekdays after Shacharit Amidah — Vidui, the Thirteen Attributes of Mercy, "
    "then Psalm 25 (LeDavid). Sephardic poskim including Rav Ovadia Yosef (Yechaveh Daat 6:7) and the "
    "Ben Ish Chai rule against physical nefilat apayim — recite sitting upright. Longer form on Mondays "
    "and Thursdays; shorter on other weekdays. Omitted on Rosh Chodesh, festivals, Chanukah, and other "
    "days in your siddur (Peninei Halakha, Prayer 03-17-05)."
)

TACHANUN_SHACHARIT_EDOT = (
    "Tachanun (Edot HaMizrach): On weekdays after Shacharit — Vidui and the Thirteen Attributes per "
    "Sephardic order (Peninei Halakha, Prayer 03-17-05). Posture and exact wording vary by kehilla "
    "(Iraqi, Syrian, Moroccan, etc.) — follow your siddur. Many communities, following kabbalistic "
    "guidance cited by the Ben Ish Chai and Rav Ovadia Yosef (Yechaveh Daat 6:7), refrain from physical "
    "nefilat apayim and recite Psalm 25 sitting upright."
)

TACHANUN_MINCHA_SEFARD = (
    "Tachanun at Mincha (Sephardi): Recited on weekdays after Mincha, including Vidui and the Thirteen "
    "Attributes where applicable. Rav Ovadia Yosef (Yechaveh Daat 6:7) and the Ben Ish Chai rule against "
    "physical nefilat apayim — recite sitting upright, including Psalm 25 (LeDavid). Omitted on Rosh Chodesh, "
    "holidays, and standard festive days."
)

TACHANUN_MINCHA_EDOT = (
    "Tachanun at Mincha (Edot HaMizrach): Recited on weekdays after Mincha, including Vidui (Ashamnu) and "
    "the Thirteen Attributes. Following the Arizal (per Kaf HaChaim and Yalkut Yosef), do not perform "
    "nefilat apayim at Mincha — recite the entire prayer sitting upright, including Psalm 25 (LeDavid Alecha). "
    "Omitted on Rosh Chodesh, holidays, and standard festive days."
)

EDOT_NETILAT = """

Edot HaMizrach minhag — when to say Al Netilat Yadayim:
• Many kehillot follow Shulchan Aruch: say the blessing after washing, before drying (Maran Rav Ovadia Yosef, Halichot Olam vol. 1; halachayomit.co.il).
• If you have an urgent need to use the bathroom, go first, then wash and recite the blessing (Shulchan Aruch O.C. 4).
• Baladi Yemenite and some kehillot wash after the bathroom before reciting the blessing — follow your community's psak (Shulchan Aruch O.C. 4 and your rav)."""


def patch_checklist(path: Path) -> None:
    data = json.loads(path.read_text(encoding="utf-8"))
    for item in data["items"]:
        if item["id"] == "mincha_shemoneh_esrei_tachanun":
            item["explanationSefard"] = TACHANUN_MINCHA_SEFARD
            item["explanationEdotHamizrach"] = TACHANUN_MINCHA_EDOT
        elif item["id"] == "shemoneh_esrei_tachanun":
            item["explanationSefard"] = TACHANUN_SHACHARIT_SEFARD
            item["explanationEdotHamizrach"] = TACHANUN_SHACHARIT_EDOT
        elif item["id"] == "ritual_hand_washing":
            sef = item.get("explanationSefard", "")
            if "Sefard minhag" in sef and EDOT_NETILAT.strip() not in sef:
                item["explanationEdotHamizrach"] = sef.replace(
                    "Sefard minhag", "Edot HaMizrach minhag (many kehillot)"
                ).replace(
                    "Different minhagim disagree whether morning washing is mainly to remove ruach ra'ah upon waking or mainly as preparation for prayer — so when you say Al Netilat Yadayim differs between Ashkenaz, Sefard, and Chabad.",
                    "Different minhagim disagree whether morning washing is mainly to remove ruach ra'ah upon waking or mainly as preparation for prayer — so when you say Al Netilat Yadayim differs between Ashkenaz, Sephardi, Edot HaMizrach, and Chabad.",
                ) + EDOT_NETILAT
            overview = item.get("explanation", "")
            if "Sefard:" in overview:
                item["explanation"] = overview.replace(
                    "Ashkenaz, Sefard, and Chabad",
                    "Ashkenaz, Sephardi, Edot HaMizrach, and Chabad",
                ).replace(
                    "• Sefard: blessing ideally on first washing; bathroom first only if urgent.",
                    "• Sephardi / many Edot HaMizrach: blessing after washing, before drying (Shulchan Aruch); bathroom first only if urgent.\n• Some Edot HaMizrach kehillot (e.g. Baladi Yemenite): wash after bathroom — follow your rav.",
                )
    path.write_text(json.dumps(data, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"patched checklist {path}")


def patch_nusach_extras(path: Path) -> None:
    data = json.loads(path.read_text(encoding="utf-8"))
    for item in data["items"]:
        if item["id"] == "sefard_birkat_kohanim":
            item["title"] = "Birkat Kohanim in Shacharit (Sephardi / Edot HaMizrach)"
            item["nusach"] = ["sefard", "edot_hamizrach"]
            item["explanation"] = (
                "Sephardi and Edot HaMizrach communities in the Diaspora commonly recite Birkat Kohanim "
                "daily during Shacharit (customs vary — follow your kehilla and Shulchan Aruch O.C. 128)."
            )
    path.write_text(json.dumps(data, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"patched nusach-extras {path}")


def main() -> None:
    for path in PATHS:
        if path.exists():
            patch_checklist(path)
        else:
            print(f"skip missing {path}")
    for path in NUSACH_EXTRAS_PATHS:
        if path.exists():
            patch_nusach_extras(path)
        else:
            print(f"skip missing {path}")


if __name__ == "__main__":
    main()
