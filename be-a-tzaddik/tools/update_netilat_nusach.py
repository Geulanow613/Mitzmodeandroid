"""Patch ritual_hand_washing nusach explanations in checklist JSON copies."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
PATHS = [
    ROOT / "shared/src/commonMain/composeResources/files/checklist-items.json",
    ROOT / "data/checklist-items.json",
    ROOT.parent / "ios-transfer-handoff/be-a-tzaddik/shared/src/commonMain/composeResources/files/checklist-items.json",
    ROOT.parent / "ios-transfer-handoff/be-a-tzaddik/data/checklist-items.json",
]

CORE = """Immediately after saying Modeh Ani, wash your hands in a specific ritual way before doing anything else.

What it is:
Netilat Yadayim (נְטִילַת יָדַיִם — literally "lifting of the hands") is a ritual washing — not just hygiene, but a spiritual act. During deep sleep, the soul partially ascends and a spiritual impurity called Ruach Ra'ah (evil spirit) settles on the fingers and nails. This washing removes it and prepares us for prayer, Torah, and the day ahead.

Different minhagim disagree whether morning washing is mainly to remove ruach ra'ah upon waking or mainly as preparation for prayer — so when you say Al Netilat Yadayim differs between Ashkenaz, Sefard, and Chabad.

Key terms:
• Ruach Ra'ah — the spirit of impurity that rests on the hands during sleep
• Tumah — ritual impurity; a spiritual state, not physical dirtiness
• Keli — a vessel (water must be poured from a cup, bottle, or washcup — filling a cup from the tap and then pouring is fine; do not wash under a running tap without a vessel)

How to pour (all traditions):
1. Ideally have a cup, bottle, or washcup filled with water and a basin ready at your bedside before you sleep
2. Pour over your right hand, then left, alternating three times: right, left, right, left, right, left
3. Dry your hands when your minhag calls for it (see blessing timing below)"""

ASHKENAZ = """

Ashkenaz minhag — when to say Al Netilat Yadayim:
• Routine: wash upon waking (often pouring into a basin by the bed) without a blessing first.
• Blessing: because most people need the bathroom shortly after waking — and one should not recite blessings while needing to relieve oneself — the custom is to say Al Netilat Yadayim only after the second washing: once you have used the bathroom, gotten dressed, washed again, and are about to dry your hands; then say Asher Yatzar as well.
• If you woke with no urge to use the bathroom: say the blessing on the first morning washing instead."""

SEFARD = """

Sefard minhag — when to say Al Netilat Yadayim:
• Following Shulchan Aruch and the Rashba: morning washing primarily removes ruach ra'ah from sleep.
• Say the blessing right after the first morning washing when you can — ideally immediately, even if you will use the bathroom afterward.
• If you have an urgent, pressing need to use the bathroom, go first, then wash and recite the blessing."""

CHABAD = """

Chabad minhag — when to say Al Netilat Yadayim (Alter Rebbe's Shulchan Aruch):
• First washing: upon waking, before touching things or letting your feet touch the floor without washing — three alternating pours, no blessing.
• Then: restroom, rinse your mouth, and personal grooming.
• Second washing: at the sink — now recite Al Netilat Yadayim, with Asher Yatzar and Elokai Neshama, in a clean state of body and mind."""

FOOTER = """

The blessing text:
"Baruch Atah Ado-nai Eloheinu Melech ha'olam, asher kid'shanu b'mitzvotav v'tzivanu al netilat yadayim"

The 4 amot rule:
Before washing, do not walk 4 amot (about 6–8 feet) continuously without stopping — this is considered a full "journey" that allows the impurity to expand. If the sink is farther than 4 amot away:
• Strict / Kabbalistic approach (Zohar, Shulchan Aruch HaRav): walk in segments shorter than 4 amot, pause fully between each, and repeat until you reach the sink
• Mainstream approach (Mishnah Berurah): walking directly to the sink to remove the impurity quickly is also permitted, though the stop-and-go method is praiseworthy
• Lenient approach (Aruch HaShulchan): the entire modern house is considered one domain; walk normally to the sink
Follow your rabbi's guidance. In all opinions, washing normally at the sink still fully removes the impurity even if you walked further first.

If you need to say a blessing before you can wash (e.g. urgent bathroom need and no water nearby):
Rub your hands firmly on a clean, dry surface — a wooden headboard, wall, or clothing. This is called kuach and permits saying holy words, but does NOT remove the spiritual impurity. You must still wash with water as soon as possible (Shulchan Aruch OC 4:22, Mishnah Berurah 4:61).

After naps:
• Nap under ~30 minutes: the severe Ruach Ra'ah does not apply; wash for cleanliness before prayer but no bracha is required and the 4-amot rule does not apply (Mishnah Berurah 4:34)
• Long daytime nap: mainstream opinion — wash three times alternating but no bracha; strict opinion — treat as morning washing. Ask your rabbi

Other important notes:
• Do not touch your eyes, mouth, or face before washing
• Do not touch food before washing
• The same alternating triple-pour without a blessing is done after: using the bathroom, leaving a cemetery, and before eating bread
• Once you have said Al Netilat Yadayim in the morning, all traditions agree you do not repeat the blessing when you wash again at shul before davening"""

OVERVIEW = """

Blessing timing overview (set nusach in Settings for your full text):
• Ashkenaz: usually no blessing at bedside; blessing after second washing (bathroom + dress), unless you had no bathroom need.
• Sefard: blessing ideally on first washing; bathroom first only if urgent.
• Chabad: no blessing on first washing; blessing only after second washing at the sink."""

PATCH = {
    "explanation": CORE + OVERVIEW + FOOTER,
    "explanationAshkenaz": CORE + ASHKENAZ + FOOTER,
    "explanationSefard": CORE + SEFARD + FOOTER,
    "explanationChabad": CORE + CHABAD + FOOTER,
}


def main() -> None:
    for path in PATHS:
        if not path.exists():
            print(f"skip missing: {path}")
            continue
        data = json.loads(path.read_text(encoding="utf-8"))
        for item in data["items"]:
            if item["id"] == "ritual_hand_washing":
                item.update(PATCH)
                break
        else:
            raise SystemExit(f"ritual_hand_washing not found in {path}")
        path.write_text(json.dumps(data, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
        print(f"updated {path}")


if __name__ == "__main__":
    main()
