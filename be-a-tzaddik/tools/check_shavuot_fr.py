import json
from pathlib import Path

fr = json.loads(
    (Path(__file__).parents[1] / "shared/src/commonMain/composeResources/files/translations/fr.json").read_text(encoding="utf-8")
)["entries"]
for k, v in fr.items():
    if k.startswith("The week before Shavuot"):
        i = v.lower().find("vacan")
        print("idx", i)
        print(v[max(0, i - 80) : i + 120])
        j = v.find("${")
        print("kotlin at", j)
        if j >= 0:
            print(v[j : j + 300])
