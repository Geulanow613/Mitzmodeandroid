"""Verify Sefaria and Chabad URLs for each parsha in ParshaData.kt."""
import re
import urllib.parse
import urllib.request
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
PARSHA_KT = ROOT / "shared/src/commonMain/kotlin/com/beardytop/beatzaddik/domain/ParshaData.kt"

# Sefaria canonical parsha refs (from Sefaria index)
SEFARIA_REFS: dict[str, str] = {
    "BERESHIS": "Bereishit",
    "NOACH": "Noach",
    "LECH_LECHA": "Lech Lecha",
    "VAYERA": "Vayera",
    "CHAYEI_SARA": "Chayei Sara",
    "TOLDOS": "Toldot",
    "VAYETZEI": "Vayetze",
    "VAYISHLACH": "Vayishlach",
    "VAYESHEV": "Vayeshev",
    "MIKETZ": "Miketz",
    "VAYIGASH": "Vayigash",
    "VAYECHI": "Vayechi",
    "SHEMOS": "Shemot",
    "VAERA": "Vaera",
    "BO": "Bo",
    "BESHALACH": "Beshalach",
    "YISRO": "Yitro",
    "MISHPATIM": "Mishpatim",
    "TERUMAH": "Terumah",
    "TETZAVEH": "Tetzaveh",
    "KI_SISA": "Ki Tisa",
    "VAYAKHEL": "Vayakhel",
    "PEKUDEI": "Pekudei",
    "VAYAKHEL_PEKUDEI": "Vayakhel-Pekudei",
    "VAYIKRA": "Vayikra",
    "TZAV": "Tzav",
    "SHMINI": "Shemini",
    "TAZRIA": "Tazria",
    "METZORA": "Metzora",
    "TAZRIA_METZORA": "Tazria-Metzora",
    "ACHREI_MOS": "Achrei Mot",
    "KEDOSHIM": "Kedoshim",
    "ACHREI_MOS_KEDOSHIM": "Achrei Mot-Kedoshim",
    "EMOR": "Emor",
    "BEHAR": "Behar",
    "BECHUKOSAI": "Bechukotai",
    "BEHAR_BECHUKOSAI": "Behar-Bechukotai",
    "BAMIDBAR": "Bamidbar",
    "NASSO": "Nasso",
    "BEHAALOSCHA": "Beha'alotcha",
    "SHLACH": "Shelach",
    "KORACH": "Korach",
    "CHUKAS": "Chukat",
    "BALAK": "Balak",
    "CHUKAS_BALAK": "Chukat-Balak",
    "PINCHAS": "Pinchas",
    "MATOS": "Matot",
    "MASEI": "Masei",
    "MATOS_MASEI": "Matot-Masei",
    "DEVARIM": "Devarim",
    "VAESCHANAN": "Vaetchanan",
    "EIKEV": "Eikev",
    "REEH": "Re'eh",
    "SHOFTIM": "Shoftim",
    "KI_SEITZEI": "Ki Teitzei",
    "KI_SAVO": "Ki Tavo",
    "NITZAVIM": "Nitzavim",
    "VAYEILECH": "Vayelech",
    "NITZAVIM_VAYEILECH": "Nitzavim-Vayelech",
    "HAAZINU": "Ha'azinu",
    "VZOS_HABERACHA": "V'Zot Habracha",
}

# Chabad slug spellings for parshah pages (from chabad.org URLs)
CHABAD_SLUGS: dict[str, str] = {
    "BERESHIS": "Bereishit",
    "NOACH": "Noach",
    "LECH_LECHA": "Lech-Lecha",
    "VAYERA": "Vayera",
    "CHAYEI_SARA": "Chayei-Sara",
    "TOLDOS": "Toldot",
    "VAYETZEI": "Vayetze",
    "VAYISHLACH": "Vayishlach",
    "VAYESHEV": "Vayeshev",
    "MIKETZ": "Miketz",
    "VAYIGASH": "Vayigash",
    "VAYECHI": "Vayechi",
    "SHEMOS": "Shemot",
    "VAERA": "Vaera",
    "BO": "Bo",
    "BESHALACH": "Beshalach",
    "YISRO": "Yitro",
    "MISHPATIM": "Mishpatim",
    "TERUMAH": "Terumah",
    "TETZAVEH": "Tetzaveh",
    "KI_SISA": "Ki-Tisa",
    "VAYAKHEL": "Vayakhel",
    "PEKUDEI": "Pekudei",
    "VAYAKHEL_PEKUDEI": "Vayakhel-Pekudei",
    "VAYIKRA": "Vayikra",
    "TZAV": "Tzav",
    "SHMINI": "Shemini",
    "TAZRIA": "Tazria",
    "METZORA": "Metzora",
    "TAZRIA_METZORA": "Tazria-Metzora",
    "ACHREI_MOS": "Acharei",
    "KEDOSHIM": "Kedoshim",
    "ACHREI_MOS_KEDOSHIM": "Acharei-Kedoshim",
    "EMOR": "Emor",
    "BEHAR": "Behar",
    "BECHUKOSAI": "Bechukotai",
    "BEHAR_BECHUKOSAI": "Behar-Bechukotai",
    "BAMIDBAR": "Bamidbar",
    "NASSO": "Nasso",
    "BEHAALOSCHA": "Behaalotecha",
    "SHLACH": "Shelach",
    "KORACH": "Korach",
    "CHUKAS": "Chukat",
    "BALAK": "Balak",
    "CHUKAS_BALAK": "Chukat-Balak",
    "PINCHAS": "Pinchas",
    "MATOS": "Matot",
    "MASEI": "Masei",
    "MATOS_MASEI": "Matot-Masei",
    "DEVARIM": "Devarim",
    "VAESCHANAN": "Vaetchanan",
    "EIKEV": "Eikev",
    "REEH": "Reeh",
    "SHOFTIM": "Shoftim",
    "KI_SEITZEI": "Ki-Teitzei",
    "KI_SAVO": "Ki-Tavo",
    "NITZAVIM": "Nitzavim",
    "VAYEILECH": "Vayelech",
    "NITZAVIM_VAYEILECH": "Nitzavim-Vayelech",
    "HAAZINU": "Haazinu",
    "VZOS_HABERACHA": "Vezot-Habracha",
}


def check(url: str) -> tuple[bool, str]:
    req = urllib.request.Request(url, method="GET", headers={"User-Agent": "Mozilla/5.0"})
    try:
        with urllib.request.urlopen(req, timeout=20) as resp:
            return True, resp.geturl()
    except Exception as e:
        code = getattr(e, "code", str(e))
        return False, str(code)


def main() -> None:
    bad_sefaria = []
    bad_chabad = []
    for key, ref in SEFARIA_REFS.items():
        url = f"https://www.sefaria.org/{urllib.parse.quote(ref)}"
        ok, final = check(url)
        status = "OK" if ok else "BAD"
        print(f"Sefaria {status} {key}: {url} -> {final}")
        if not ok:
            bad_sefaria.append(key)

    print()
    for key, slug in CHABAD_SLUGS.items():
        # Try redirect-style parshah text URL with slug search via default parshah page
        url = f"https://www.chabad.org/parshah/default_cdo/jewish/{slug}.htm"
        ok, final = check(url)
        if not ok:
            url2 = f"https://www.chabad.org/parshah/article_cdo/aid/3290/jewish/{slug}.htm"
            ok2, final2 = check(url2)
            status = "OK" if ok2 else "BAD"
            print(f"Chabad {status} {key}: {url2} -> {final2}")
            if not ok2:
                bad_chabad.append(key)
        else:
            print(f"Chabad OK {key}: {url} -> {final}")

    print(f"\nBad Sefaria: {len(bad_sefaria)}")
    print(f"Bad Chabad: {len(bad_chabad)}")


if __name__ == "__main__":
    main()
