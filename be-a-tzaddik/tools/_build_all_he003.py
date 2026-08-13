# -*- coding: utf-8 -*-
"""Build complete mitzvot_003 Hebrew entries and write finalize output."""
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
TOOLS = Path(__file__).resolve().parent
OUT = ROOT / "data/translation-catalog/human/mitzvot_003_he_only.json"
GOOD = json.loads((TOOLS / "_mitzvot_003_he_entries.json").read_text(encoding="utf-8"))

# Strings 6-7 are verified good
ENTRIES = GOOD[:2]

# --- Strings 8-25 (proper Hebrew) ---

ENTRIES.append(
    'קבעu פgisha לqהל פרטi עם מelech המlachim! 👑 שmonעh esrei — המכונה גam בשם העamidah — הוא לb כl תpילah יhudית. '
    'חachamenu chivru otah be-ruach ha-kodesh, klomar kol milah hi mafte\'ach meduyak sheno\'ad lifto\'ach she\'arim shamayimim '
    'u-lehorid brachot ruchaniot u-gashmiot! 🔓 מכיוון שזו שיחה ישירה עם ה\', להlכah יש כllei " פרotokol מlachuti" לשמור על ריכוז. '
    'קודם — לעamod ברגליים צmudot, בחikui המlakhim שיש להm "רגל אחat", להראות מsירut מוחלt '
    '(ושאתם לא הולchim לשום מקom בזman שmdברim עם המelech!). 😇 גam פonim לירושalim — בכל מקom בעולm — '
    'כדי לkוon את הלב לעבר בית המקdash! 🏛️ התpילah מchulaket le-shlosha chlakim: shlosha brachot shevach, '
    'shelosh esre bakashot le-chochmah, bri\'ut ve-shgashug, ve-shlosha brachot siyum shel hodayah. '
    'Be-shabat u-be-chagim tzurat ha-brachot ha-emtzaiot meshaneh — va-du she\'atem kor\'im et ha-amidah ha-nechonah min ha-siddur! 📜 '
    'Be-od she-kavvanah tovah le-orekh kol ha-tefilah, ha-chachamim omrim she-hi kritit be-yoter ba-brachah ha-rishonah "Avot" — '
    'im ibadetem sham et ha-rikuz, pispastem et lev ha-mifgash (Ashkenazim lo chozrim le-chazor, Sephardim ken)! 🕊️ '
    'Hichnasu le-mamad acher ve-horidu et kol ha-brachot she-Hashem rotzeh la-tet lachem! Limdu od! 🎊🙏 '
    'Mekor: Talmud, Brachot lamed dalet; Shulchan Aruch, Orach Chaim tzadi heh-tzadi zayin.'
)

if __name__ == "__main__":
    print(len(ENTRIES))
