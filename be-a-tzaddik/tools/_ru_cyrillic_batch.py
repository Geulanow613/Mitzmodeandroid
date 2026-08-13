#!/usr/bin/env python3
"""Generate human-quality RU Cyrillic fixes for Latin-script / English-fallback glossary keys."""

from __future__ import annotations

import ast
import json
import re
import sys
from pathlib import Path

from _glossary_batch9 import BATCH9_RU, resolve_batch9_ru
from translation_repairs import RU_A_DOT, apply_ru_religious_translit, repair_translation

ROOT = Path(__file__).resolve().parents[1]
CATALOG_PATH = ROOT / "data/translation-catalog/strings.json"
RU_PATH = ROOT / "shared/src/commonMain/composeResources/files/translations/ru.json"
LATIN_KEYS_PATH = ROOT / "data/translation-catalog/_latin_ru_keys.txt"
OUT_PATH = ROOT / "data/translation-catalog/human/ru_cyrillic_fixes.json"
TOP50_PATH = ROOT / "data/translation-catalog/human/ru_cyrillic_top50.json"
SHARD_PATH = ROOT / "data/translation-catalog/shards/es_fr_ru_retranslate.json"
HUMAN_SHARD_PATH = ROOT / "data/translation-catalog/_ru_human_shard_queue.json"

ALLOW_IDENTITY = {
    "Rav",
    "$mitzvotCount",
    "www.beardy.top",
    "https://www.beardy.top",
    "Beardy Top Productions",
    "XL",
    "\\s*/\\s*",
    "$translatedSwitchedTo $languageName.\n$translatedPleaseNote",
}

# Catalog-specific extensions (longer source strings than BATCH9 guesses).
EXTRA_RU: dict[str, str] = {
    "Chiyuv means obligated — the mitzvah applies to you and you are expected to fulfill it. Bar mitzvah creates chiyuv for a boy's mitzvot; other exemptions (illness, danger) can remove chiyuv temporarily. Knowing whether you are chayav or patur is why we ask a rav.": (
        "Хиюв означает обязанность — мицва применяется к вам и вы должны её выполнить. "
        "Бар-мицва создаёт хиюв для мицвот мальчика; болезнь или опасность могут временно "
        "снять хиюв. Чтобы понять, хаяв вы или патур, — спросите рава."
    ),
    "chiyuv — Chiyuv means obligated — the mitzvah applies to you and you are expected to fulfill it. Bar mitzvah creates chiyuv for a boy's mitzvot; other exemptions (illness, danger) can remove chiyuv temporarily. Knowing whether you are chayav or patur is why we ask a rav.": (
        "хиюв — Хиюв означает обязанность — мицва применяется к вам и вы должны её выполнить. "
        "Бар-мицва создаёт хиюв для мицвот мальчика; болезнь или опасность могут временно "
        "снять хиюв. Чтобы понять, хаяв вы или патур, — спросите рава."
    ),
    "Chutz la'aretz means outside the Land of Israel. Halacha differs in some areas — second day of Yom Tov, certain agricultural laws, and some prayer texts. Israelis in chutz la'aretz often follow local practice while visiting; ask your rav for long stays.": (
        "Хуц ла-арец означает вне Эрец Исраэль. Галаха различается — второй день Йом Тов, "
        "некоторые сельскохозяйственные законы и тексты молитв. Израильтяне в хуц ла-арец "
        "часто следуют местной практике во время визита; при длительном пребывании спросите рава."
    ),
    "chutz la'aretz — Chutz la'aretz means outside the Land of Israel. Halacha differs in some areas — second day of Yom Tov, certain agricultural laws, and some prayer texts. Israelis in chutz la'aretz often follow local practice while visiting; ask your rav for long stays.": (
        "хуц ла-арец — Хуц ла-арец означает вне Эрец Исраэль. Галаха различается — второй день "
        "Йом Тов, некоторые сельскохозяйственные законы и тексты молитв. Израильтяне в хуц ла-арец "
        "часто следуют местной практике во время визита; при длительном пребывании спросите рава."
    ),
    "Practice gratitude! Take a moment to thank G-d for something good in your life - big or small 🙌. Maybe it's your health, your family, or even just that morning coffee! Rabbis teach that When G-d sees us sending thanks, he welcomes our other prayers along with them!": (
        "Практикуйте благодарность! Найдите момент поблагодарить В-га за что-то хорошее "
        "в жизни — большое или малое 🙌. Может, здоровье, семья или даже утренний кофе! "
        "Мудрецы учат: когда В-г видит нашу благодарность, Он принимает и другие наши молитвы вместе с ней!"
    ),
    "Get a Chumash and open to a random page and verse. whatever verse you land on, it's supposed to be a message from G-d - Submitted by HB": (
        "Возьмите Хумаш и откройте на случайной странице и стихе. Какой бы стих ни попался — "
        "многие считают его посланием от В-га. Прочитайте медленно и подумайте, что он значит для вас сейчас."
    ),
    "Get a Chumash and open to a random page and verse — Submitted by HB. Many Jews have the cherished custom of opening the Torah to seek inspiration or guidance at important moments in life. Whether you see the verse as a personal message or simply as a spark for reflection, the act itself connects you to Torah — and our sages teach that even studying a single verse is its own mitzvah. Read what you land on slowly, think about what it might mean for your life right now, and let the words sink in.": (
        "Возьмите Хумаш и откройте на случайной странице и стихе. У многих евреев есть "
        "любимый обычай открывать Тору за вдохновением в важные моменты жизни. Видите ли вы "
        "стих как личное послание или просто повод для размышления — сам акт связывает вас с Торой. "
        "Мудрецы учат: даже один стих — это своя мицва. Прочитайте медленно, подумайте, "
        "что это значит для вас сейчас, и дайте словам осесть."
    ),
    "Psak is a halachic ruling — the answer a qualified posek gives for your real case, not a theoretical debate. Judaism has a living legal tradition; ask a rav, not only articles.": (
        "Псак — галахическое постановление — ответ квалифицированного посека на ваш реальный "
        "случай, а не теоретический спор. Иудаизм имеет живую правовую традицию; спросите рава, "
        "а не полагайтесь только на статьи."
    ),
    "psak — Psak is a halachic ruling — the answer a qualified posek gives for your real case, not a theoretical debate. Judaism has a living legal tradition; ask a rav, not only articles.": (
        "псак — Псак — галахическое постановление — ответ квалифицированного посека на ваш "
        "реальный случай, а не теоретический спор. Иудаизм имеет живую правовую традицию; "
        "спросите рава, а не полагайтесь только на статьи."
    ),
    "aggadah — Aggadah is the non-legal storytelling and teaching in the Talmud and midrash — ethics, theology, and narrative. It complements halacha (law). Shabbat table stories and many midrashim are aggadah.": (
        "агада — Агада — негалахические рассказы и учения в Талмуде и мидраше — этика, "
        "богословие и нарратив. Дополняет галаху (закон). Истории за шаббат-столом и многие "
        "мидраши — это агада."
    ),
    "Purim is tomorrow. Plan all four mitzvot: Megillah (night and morning), matanot la'evyonim, mishloach manot, and tomorrow's festive meal.": (
        "Завтра Пурим. Спланируйте все четыре мицвы: Мегилла (ночью и утром), "
        "матанот ла-эвьоним, мишлоах манот и завтрашний праздничный обед."
    ),
    "The baal koreh (master of reading) chants the Torah from the scroll with correct trop and pronunciation. Training takes months. The community follows in a Chumash. Especially vital on Shabbat and festivals.": (
        "Бааль коре читает Тору из свитка с правильным тропом и произношением. "
        "Подготовка занимает месяцы. Община следит в Хумаше. Особенно важен в Шаббат и праздники."
    ),
    "Tefilat Nedavah is a voluntary Amidah when tashlumin is impossible — with mental stipulation and a small novelty in the prayer. It does not replace obligatory prayer; ask your rav when it applies.": (
        "Тфилат Недава — добровольная Амида, когда ташлюмин невозможен — с ментальным "
        "условием и небольшой новизной в молитве. Не заменяет обязательную молитву; "
        "спросите рава, когда это применимо."
    ),
    '"guard (Shabbat)" from the Deuteronomy version of the Ten Commandments': (
        "«шамор» (Шаббат) из варианта Дварим Десяти заповедей"
    ),
    '"Rock of Ages," Chanukah hymn sung after lighting': (
        "«Маоз Цур» — ханукальный гимн, поющийся после зажигания"
    ),
    "(Nusach Ashkenaz)": "(нусах ашкеназ)",
    "Chanukah candelabra; candles are lit each night": (
        "ханукальная менора; свечи зажигаются каждую ночь"
    ),
    "HaNeiros halalu — paragraph sung after lighting Chanukah candles": (
        "hа-Неирот hалалу — параграф, который поют после зажигания свечей Хануки"
    ),
}

# Always merged into ru_cyrillic_fixes.json (even when not in latin-key queue).
MANUAL_RU_FIXES: dict[str, str] = {
    k: EXTRA_RU[k]
    for k in (
        '"guard (Shabbat)" from the Deuteronomy version of the Ten Commandments',
        '"Rock of Ages," Chanukah hymn sung after lighting',
        "(Nusach Ashkenaz)",
        "Chanukah candelabra; candles are lit each night",
        "HaNeiros halalu — paragraph sung after lighting Chanukah candles",
    )
}

MOJIBAKE: list[tuple[str, str]] = [
    ("ΓÇö", "—"),
    ("ΓÇó", "•"),
    ("ΓÇª", "…"),
    ("≡ƒÖî", "🙌"),
]

PLACEHOLDER_CORRUPT = re.compile(
    r"the0|\)\.0\)|\)\.1\)|s0s|⟦",
    re.IGNORECASE,
)
URL_PATTERN = re.compile(r"https?://[^\s]+|www\.[^\s]+")
VAR_PATTERN = re.compile(r"\$[a-zA-Z_][a-zA-Z0-9_]*|\{[a-zA-Z_]+\}|\$\{[^}]+\}")


def strip_for_latin_scan(text: str) -> str:
    t = text
    for u in URL_PATTERN.findall(t):
        t = t.replace(u, "")
    for v in VAR_PATTERN.findall(t):
        t = t.replace(v, "")
    return t


def count_latin_chars(text: str) -> int:
    return sum(1 for c in strip_for_latin_scan(text) if c.isascii() and c.isalpha())


def _normalize_mojibake(text: str) -> str:
    for bad, good in MOJIBAKE:
        text = text.replace(bad, good)
    return text


def is_placeholder_corrupt(text: str) -> bool:
    return bool(PLACEHOLDER_CORRUPT.search(text))


def cyrillic_ratio(text: str) -> float:
    letters = [c for c in text if c.isalpha()]
    if not letters:
        return 0.0
    return sum(1 for c in letters if "\u0400" <= c <= "\u04ff") / len(letters)


def latin_ratio(text: str) -> float:
    letters = [c for c in text if c.isalpha()]
    if not letters:
        return 0.0
    return sum(1 for c in letters if c.isascii()) / len(letters)


def polish_ru(text: str) -> str:
    """Apply RU_A_DOT fixes and religious-term Cyrillic transliteration."""
    for word, repl in RU_A_DOT.items():
        text = text.replace(f"А.{word}", repl)
    text = re.sub(r"А\.([a-zA-Z])", r"\1", text)
    return apply_ru_religious_translit(text)


def load_human_shard(path: Path) -> dict[str, str]:
    if not path.exists():
        return {}
    return json.loads(path.read_text(encoding="utf-8")).get("ru", {})


def collect_manual_fixes() -> dict[str, str]:
    """Merge top-50 shard, EXTRA_RU, and explicit MANUAL_RU_FIXES (later wins)."""
    merged: dict[str, str] = {}
    merged.update(load_human_shard(TOP50_PATH))
    merged.update(EXTRA_RU)
    merged.update(MANUAL_RU_FIXES)
    return merged


def sync_shard_ru(fixes: dict[str, str]) -> int:
    """Sync es_fr_ru_retranslate.json ru entries; drop corrupt placeholders."""
    if not SHARD_PATH.exists():
        return 0
    data = json.loads(SHARD_PATH.read_text(encoding="utf-8"))
    ru = data.setdefault("ru", {})
    updated = 0
    for key in list(ru.keys()):
        val = ru[key]
        if is_placeholder_corrupt(key) or is_placeholder_corrupt(val):
            del ru[key]
            updated += 1
            continue
        if key in fixes:
            ru[key] = polish_ru(fixes[key])
            updated += 1
        else:
            cleaned = repair_translation("ru", val)
            if cleaned != val:
                ru[key] = cleaned
                updated += 1
    for key, val in fixes.items():
        if key not in ru and not is_placeholder_corrupt(key) and not is_placeholder_corrupt(val):
            ru[key] = polish_ru(val)
            updated += 1
    SHARD_PATH.write_text(
        json.dumps(data, ensure_ascii=False, indent=2) + "\n",
        encoding="utf-8",
    )
    return updated


def load_catalog() -> list[str]:
    return json.loads(CATALOG_PATH.read_text(encoding="utf-8"))["strings"]


def load_ru_entries() -> dict[str, str]:
    return json.loads(RU_PATH.read_text(encoding="utf-8"))["entries"]


def read_latin_keys_file(path: Path) -> list[str]:
    if not path.exists():
        return []
    raw = path.read_bytes()
    for enc in ("utf-16", "utf-8-sig", "utf-8"):
        try:
            text = raw.decode(enc)
            break
        except UnicodeDecodeError:
            continue
    else:
        text = raw.decode("utf-8", errors="replace")

    prefixes: list[str] = []
    for line in text.splitlines()[1:]:
        line = line.strip()
        if not line:
            continue
        try:
            prefixes.append(ast.literal_eval(line))
        except (SyntaxError, ValueError):
            prefixes.append(_normalize_mojibake(line.strip("'\"")))
    return [_normalize_mojibake(p) for p in prefixes]


def resolve_prefix_to_catalog(prefix: str, catalog: list[str]) -> str | None:
    prefix = _normalize_mojibake(prefix)
    if prefix in catalog:
        return prefix
    for n in (len(prefix), 80, 60, 45, 30):
        hits = [k for k in catalog if k.startswith(prefix[:n])]
        if len(hits) == 1:
            return hits[0]
    return None


def scan_ru_targets(catalog: list[str], ru_entries: dict[str, str]) -> list[str]:
    """Keys where RU is English fallback or Latin-heavy."""
    targets: list[str] = []
    for en in catalog:
        if en in ALLOW_IDENTITY:
            continue
        if is_placeholder_corrupt(en):
            continue
        tr = ru_entries.get(en, en)
        if is_placeholder_corrupt(tr):
            continue
        if tr == en and len(en) > 40:
            targets.append(en)
        elif len(en) > 40 and cyrillic_ratio(tr) < 0.35 and latin_ratio(tr) > 0.35:
            targets.append(en)
    return targets


def collect_target_keys(catalog: list[str], ru_entries: dict[str, str]) -> tuple[list[str], list[str]]:
    """Return (target keys, keys deferred to human shard)."""
    human_shard: list[str] = []
    targets: set[str] = set()

    if LATIN_KEYS_PATH.exists():
        for prefix in read_latin_keys_file(LATIN_KEYS_PATH):
            if is_placeholder_corrupt(prefix):
                human_shard.append(prefix[:120])
                continue
            key = resolve_prefix_to_catalog(prefix, catalog)
            if key:
                if is_placeholder_corrupt(key) or is_placeholder_corrupt(ru_entries.get(key, "")):
                    human_shard.append(key[:120])
                else:
                    targets.add(key)
    else:
        for key in scan_ru_targets(catalog, ru_entries):
            targets.add(key)

    # BATCH9 covers the ~83 English-fallback glossary keys.
    batch9 = resolve_batch9_ru(catalog)
    for key in batch9:
        if is_placeholder_corrupt(key) or is_placeholder_corrupt(batch9[key]):
            human_shard.append(key[:120])
            continue
        targets.add(key)

    return sorted(targets), human_shard


def lookup_translation(key: str, batch9: dict[str, str], manual: dict[str, str]) -> str | None:
    if key in manual:
        return manual[key]
    if key in EXTRA_RU:
        return EXTRA_RU[key]
    if key in batch9:
        return batch9[key]
    return None


def auto_polish_latin_entries(
    catalog: list[str],
    ru_entries: dict[str, str],
    existing: dict[str, str] | None = None,
) -> dict[str, str]:
    """Apply compile-time RU repairs to every Latin-heavy shipped value."""
    auto: dict[str, str] = {}
    baseline = existing or {}
    for key in catalog:
        val = ru_entries.get(key, key)
        if val == key or is_placeholder_corrupt(val):
            continue
        if count_latin_chars(val) == 0:
            continue
        polished = val
        best = val
        best_lc = count_latin_chars(val)
        for _ in range(5):
            polished = polish_ru(repair_translation("ru", polished))
            lc = count_latin_chars(polished)
            if lc < best_lc:
                best = polished
                best_lc = lc
            if best_lc == 0:
                break
        if best_lc >= count_latin_chars(val):
            continue
        cur = baseline.get(key)
        if cur is not None and count_latin_chars(best) >= count_latin_chars(cur):
            continue
        prev = auto.get(key)
        if prev is None or count_latin_chars(best) < count_latin_chars(prev):
            auto[key] = best
    return auto


def main() -> int:
    catalog = load_catalog()
    ru_entries = load_ru_entries()
    batch9 = resolve_batch9_ru(catalog)
    manual_fixes = collect_manual_fixes()

    target_keys, human_shard = collect_target_keys(catalog, ru_entries)

    fixes: dict[str, str] = {}
    skipped_no_translation: list[str] = []

    for key in target_keys:
        if is_placeholder_corrupt(key):
            human_shard.append(key[:120])
            continue
        tr = lookup_translation(key, batch9, manual_fixes)
        if not tr:
            skipped_no_translation.append(key[:120])
            continue
        if is_placeholder_corrupt(tr):
            human_shard.append(key[:120])
            continue
        fixes[key] = polish_ru(tr)

    for key, val in manual_fixes.items():
        fixes[key] = polish_ru(val)

    auto_fixes = auto_polish_latin_entries(catalog, ru_entries, fixes)
    for key, val in auto_fixes.items():
        cur = fixes.get(key)
        polished = polish_ru(val)
        if cur is None or count_latin_chars(polished) < count_latin_chars(cur):
            fixes[key] = polished

    OUT_PATH.parent.mkdir(parents=True, exist_ok=True)
    OUT_PATH.write_text(
        json.dumps({"ru": fixes}, ensure_ascii=False, indent=2) + "\n",
        encoding="utf-8",
    )

    shard_updates = sync_shard_ru(fixes)
    if shard_updates:
        print(f"synced {shard_updates} ru entries in {SHARD_PATH.name}")

    if human_shard or skipped_no_translation:
        HUMAN_SHARD_PATH.write_text(
            json.dumps(
                {
                    "placeholder_corruption": sorted(set(human_shard)),
                    "no_translation": sorted(set(skipped_no_translation)),
                },
                ensure_ascii=False,
                indent=2,
            )
            + "\n",
            encoding="utf-8",
        )

    print(f"wrote {OUT_PATH}")
    print(f"keys written: {len(fixes)} (auto-polish: {len(auto_fixes)})")
    if human_shard:
        print(f"deferred to human shard (placeholder corruption): {len(set(human_shard))}")
    if skipped_no_translation:
        print(f"skipped (no translation): {len(skipped_no_translation)}")

    samples = list(fixes.items())[:5]
    if samples:
        print("\nSample fixes:")
        for k, v in samples:
            print(f"  KEY: {k[:55]}...")
            try:
                print(f"    RU: {v[:90]}...")
            except UnicodeEncodeError:
                print(f"    RU: {v[:90].encode('ascii', 'backslashreplace').decode()}...")

    return 0


if __name__ == "__main__":
    sys.exit(main())
