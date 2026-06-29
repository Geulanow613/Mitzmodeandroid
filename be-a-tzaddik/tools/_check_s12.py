# -*- coding: utf-8 -*-
import json
from pathlib import Path
he = json.loads(Path(r"c:/apps/hehehe/be-a-tzaddik/data/translation-catalog/human/mitzvot_003_he_only.json").read_text(encoding="utf-8"))["he"]
s12 = he[11]
Path(r"c:/apps/hehehe/be-a-tzaddik/tools/_s12_final.txt").write_text(
    f"fam={'fam' in s12}\nends={s12[-80:]}", encoding="utf-8")
