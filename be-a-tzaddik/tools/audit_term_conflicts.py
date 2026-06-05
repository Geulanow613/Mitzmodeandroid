#!/usr/bin/env python3
"""Audit glossary phrase conflicts and missing multi-word terms in app copy."""
from __future__ import annotations

import json
import pathlib
import re
from dataclasses import dataclass, field

ROOT = pathlib.Path(__file__).resolve().parents[1]
DICT_PATH = ROOT / (
    "shared/src/commonMain/kotlin/com/beardytop/beatzaddik/domain/HalachicTermsDictionary.kt"
)
BEGINNER_PATH = ROOT / (
    "shared/src/commonMain/kotlin/com/beardytop/beatzaddik/domain/BeginnerHalachaGlossary.kt"
)


@dataclass
class Term:
    id: str
    title: str
    labels: list[str] = field(default_factory=list)


def parse_line_terms(text: str) -> list[Term]:
    pat = re.compile(r'line\(\s*"([^"]+)"\s*(?:,\s*([^)]+))?\)', re.DOTALL)
    terms: list[Term] = []
    for m in pat.finditer(text):
        title_def = m.group(1)
        title = title_def.split(" — ", 1)[0].strip()
        aliases: list[str] = []
        if m.group(2):
            aliases = [a.strip().strip('"') for a in re.findall(r'"([^"]+)"', m.group(2))]
        labels = list(dict.fromkeys([title, *aliases]))
        tid = re.sub(r"[^a-z0-9']+", "_", title.lower()).strip("_")
        terms.append(Term(id=tid, title=title, labels=labels))
    return terms


def parse_beginner_terms(text: str) -> list[Term]:
    terms: list[Term] = []
    pat = re.compile(
        r'const val [A-Z_]+\s*=\s*"([^"]+)"',
        re.MULTILINE,
    )
    for m in pat.finditer(text):
        chunk = m.group(1)
        if " — " not in chunk:
            continue
        title = chunk.split(" — ", 1)[0].strip()
        labels = list(dict.fromkeys([title]))
        tid = re.sub(r"[^a-z0-9']+", "_", title.lower()).strip("_")
        terms.append(Term(id=tid, title=title, labels=labels))
    return terms


def merge_terms(raw: list[Term]) -> list[Term]:
    """Mirror Kotlin allTerms merge by id."""
    by_id: dict[str, Term] = {}
    for term in raw:
        existing = by_id.get(term.id)
        if existing is None:
            by_id[term.id] = Term(term.id, term.title, list(term.labels))
        else:
            labels = list(dict.fromkeys(existing.labels + term.labels))
            title = existing.title if len(existing.title) >= len(term.title) else term.title
            by_id[term.id] = Term(term.id, title, labels)
    return list(by_id.values())


def effective_labels(term: Term) -> list[str]:
    multi = [l for l in term.labels if " " in l]
    if not multi:
        return term.labels
    out = []
    for label in term.labels:
        if " " in label:
            out.append(label)
        elif label.lower() == term.title.lower():
            out.append(label)
        elif not any(
            re.search(
                rf"(?i)(?<![A-Za-z0-9']){re.escape(label)}(?![A-Za-z0-9'])",
                multi_label,
            )
            for multi_label in multi
        ):
            out.append(label)
    return out


def build_matchers(terms: list[Term]) -> list[tuple[str, Term]]:
    pairs = [(label, term) for term in terms for label in effective_labels(term)]
    pairs.sort(key=lambda x: len(x[0]), reverse=True)
    return pairs


def is_interior_char(text: str, index: int) -> bool:
    ch = text[index]
    if ch.isalnum():
        return True
    if ch == "'":
        prev = text[index - 1] if index > 0 else None
        nxt = text[index + 1] if index + 1 < len(text) else None
        return (prev is not None and prev.isalnum()) and (nxt is not None and nxt.isalnum())
    return False


def is_boundary(text: str, start: int, end: int) -> bool:
    before = start - 1
    after = end
    if before >= 0 and is_interior_char(text, before):
        return False
    if after < len(text) and is_interior_char(text, after):
        return False
    return True


def longer_phrase_ranges(text: str, multi_labels: list[str]) -> list[tuple[int, int]]:
    ranges: list[tuple[int, int]] = []
    for longer in multi_labels:
        start = 0
        while True:
            idx = text.lower().find(longer.lower(), start)
            if idx < 0:
                break
            end = idx + len(longer)
            if is_boundary(text, idx, end):
                ranges.append((idx, end))
            start = idx + 1
    return ranges


def find_matches(text: str, matchers: list[tuple[str, Term]]) -> list[dict]:
    if not text.strip():
        return []
    multi_labels = [label for label, _ in matchers if " " in label]
    longer_ranges = longer_phrase_ranges(text, multi_labels)
    candidates: list[dict] = []

    for label, term in matchers:
        start = 0
        while True:
            idx = text.lower().find(label.lower(), start)
            if idx < 0:
                break
            end = idx + len(label)
            if not is_boundary(text, idx, end):
                start = idx + 1
                continue
            inside = False
            if " " not in label:
                for lstart, lend in longer_ranges:
                    if idx >= lstart and end <= lend:
                        inside = True
                        break
            if not inside:
                candidates.append(
                    {
                        "start": idx,
                        "end": end,
                        "label": label,
                        "term_id": term.id,
                        "term_title": term.title,
                        "matched": text[idx:end],
                    }
                )
            start = idx + 1

    candidates.sort(key=lambda c: (-(c["end"] - c["start"]), c["start"]))
    picked: list[dict] = []
    for cand in candidates:
        cr = range(cand["start"], cand["end"])
        if any(
            cr.start < range(p["start"], p["end"]).stop
            and range(p["start"], p["end"]).start < cr.stop
            for p in picked
        ):
            continue
        picked.append(cand)
    picked.sort(key=lambda c: c["start"])
    return picked


def extract_kt_strings(path: pathlib.Path) -> list[tuple[str, str]]:
    if not path.exists():
        return []
    kt = path.read_text(encoding="utf-8")
    chunks: list[tuple[str, str]] = []
    for m in re.finditer(r'"""([\s\S]*?)"""', kt):
        chunks.append((f"{path.name}:triple", m.group(1)))
    for m in re.finditer(r'"(?:[^"\\]|\\.)*"', kt):
        s = m.group(0)[1:-1]
        if len(s) < 12 or " — " not in s and " " not in s:
            continue
        if any(x in s for x in ("http", "package ", "import ", "id =")):
            continue
        chunks.append((f"{path.name}:str", s))
    return chunks


def collect_corpus() -> list[tuple[str, str]]:
    chunks: list[tuple[str, str]] = []

    checklist = ROOT / "data/checklist-items.json"
    if checklist.exists():
        data = json.loads(checklist.read_text(encoding="utf-8"))
        for item in data.get("items", []):
            iid = item.get("id", "?")
            for field in (
                "title", "explanation", "hint",
                "explanationAshkenaz", "explanationSefard", "explanationChabad",
            ):
                v = item.get(field)
                if v:
                    chunks.append((f"checklist:{iid}:{field}", v))
            for link in item.get("links", []) or []:
                if link.get("displayText"):
                    chunks.append((f"checklist:{iid}:link", link["displayText"]))

    for rel in [
        "shared/src/commonMain/kotlin/com/beardytop/beatzaddik/domain/ChecklistZmanEvaluator.kt",
        "shared/src/commonMain/kotlin/com/beardytop/beatzaddik/domain/YomTovShabbatPrepText.kt",
        "shared/src/commonMain/kotlin/com/beardytop/beatzaddik/domain/ErevPesachPrepText.kt",
        "shared/src/commonMain/kotlin/com/beardytop/beatzaddik/domain/ErevChagPrepText.kt",
        "shared/src/commonMain/kotlin/com/beardytop/beatzaddik/domain/SeasonalChecklistItems.kt",
        "shared/src/commonMain/kotlin/com/beardytop/beatzaddik/domain/SeasonalMitzvahText.kt",
        "shared/src/commonMain/kotlin/com/beardytop/beatzaddik/domain/OmerCountText.kt",
        "shared/src/commonMain/kotlin/com/beardytop/beatzaddik/domain/PurimMeshulashText.kt",
        "shared/src/commonMain/kotlin/com/beardytop/beatzaddik/ui/screens/ShabbatGuideData.kt",
    ]:
        chunks.extend(extract_kt_strings(ROOT / rel))

    seasonal = ROOT / "docs/SEASONAL_AND_HIDDEN_CHECKLIST_TEXT.md"
    if seasonal.exists():
        chunks.append(("SEASONAL_AND_HIDDEN_CHECKLIST_TEXT.md", seasonal.read_text(encoding="utf-8")))

    return chunks


def main() -> None:
    raw = parse_line_terms(DICT_PATH.read_text(encoding="utf-8"))
    if BEGINNER_PATH.exists():
        raw.extend(parse_beginner_terms(BEGINNER_PATH.read_text(encoding="utf-8")))
    terms = merge_terms(raw)
    matchers = build_matchers(terms)
    multi_labels = sorted({label for label, _ in matchers if " " in label}, key=len, reverse=True)

    print(f"Merged {len(terms)} terms, {len(matchers)} effective matchers")
    print()

    corpus = collect_corpus()
    print(f"Corpus: {len(corpus)} text chunks")
    print()

    # UNMATCHED multi-word glossary labels present in copy
    print("=== Phrases in copy that should match but don't ===")
    unmatched = []
    label_set = {label.lower() for label, _ in matchers}
    for source, body in corpus:
        matches = find_matches(body, matchers)
        body_lower = body.lower()
        for ml in multi_labels:
            idx = body_lower.find(ml.lower())
            if idx < 0:
                continue
            end = idx + len(ml)
            hit = any(m["start"] == idx and m["end"] == end for m in matches)
            if not hit:
                covered = any(m["start"] <= idx and m["end"] >= end for m in matches)
                if not covered:
                    unmatched.append((ml, source, body[max(0, idx - 20): idx + len(ml) + 20]))

    for ml, source, ctx in sorted(set(unmatched))[:40]:
        print(f'  "{ml}" in {source}')
        print(f"    ...{ctx.replace(chr(10), ' ')}...")

    print()
    print("=== Partial matches inside longer phrases (bad) ===")
    bad = []
    for source, body in corpus:
        matches = find_matches(body, matchers)
        for ml in multi_labels:
            if body.lower().find(ml.lower()) < 0:
                continue
            idx = body.lower().find(ml.lower())
            end = idx + len(ml)
            for m in matches:
                if m["start"] >= idx and m["end"] <= end and " " not in m["label"]:
                    if m["label"].lower() != ml.lower():
                        bad.append((m["matched"], m["term_title"], ml, source))

    for item in sorted(set(bad))[:35]:
        print(f'  "{item[0]}" -> {item[1]} inside "{item[2]}" in {item[3]}')

    print()
    print("=== Duplicate single-word matchers (different term ids) ===")
    label_to_terms: dict[str, set[str]] = {}
    for label, term in matchers:
        if " " in label:
            continue
        label_to_terms.setdefault(label.lower(), set()).add(f"{term.id}|{term.title}")
    for label, ids in sorted(label_to_terms.items()):
        if len(ids) > 1:
            print(f"  {label}: {sorted(ids)}")

    print()
    print("=== High-frequency copy phrases with no glossary label ===")
    phrase_pat = re.compile(r"\b([A-Z][A-Za-z0-9']+(?:\s+[A-Za-z0-9']+){1,3})\b")
    all_labels_lower = {label.lower() for label, _ in matchers}
    missing: dict[str, int] = {}
    skip = {
        "what it is", "key terms", "learn more", "in israel", "this year",
        "how to do", "when in doubt", "the blessing", "friday night",
        "jewish holidays", "daily torah", "holy temple", "ten commandments",
    }
    for _, body in corpus:
        for m in phrase_pat.finditer(body):
            phrase = m.group(1)
            pl = phrase.lower()
            if pl in all_labels_lower or pl in skip or len(phrase) < 10:
                continue
            if any(pl in lab for lab in all_labels_lower):
                continue
            missing[phrase] = missing.get(phrase, 0) + 1

    halachic_hints = [
        "yom tov", "rosh chodesh", "yaknehaz", "bedikat", "biur", "mechirat",
        "netilat", "birkat", "krias", "kriat", "musaf", "hallel", "havdalah",
        "kiddush", "shabbat", "pesach", "sukkot", "purim", "chanukah", "omer",
        "tefillin", "mezuzah", "mikveh", "tzitzit", "chametz", "matzah", "seder",
    ]
    for phrase, count in sorted(missing.items(), key=lambda x: -x[1])[:50]:
        if any(h in phrase.lower() for h in halachic_hints):
            print(f'  "{phrase}" ({count}x)')


if __name__ == "__main__":
    main()
