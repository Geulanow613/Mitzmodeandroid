from pathlib import Path

p = Path(__file__).resolve().parent / "make_blobs.py"
t = p.read_text(encoding="utf-8")
t = t.replace(
    '"\\u05dc\\u05e2\\u05it\\u05D9\\u05DD \\u05DB\\u05D6\\u05D4 35\\u201350 \\u05D3\\u05E7\\u05D5\\u05EA \\u05D0\\u05Ch\\u05D9\\u05E8 \\u05E2\\u05DC\\u05D5\\u05EA \\u05D4\\u05E9\\u05D7\\u05E8. "',
    '"\\u05dc\\u05e2\\u05d9\\u05ea\\u05d9\\u05dd \\u05db\\u05d6\\u05d4 35\\u201350 \\u05d3\\u05e7\\u05d5\\u05ea \\u05d0\\u05d7\\u05e8\\u05d9 \\u05e2\\u05dc\\u05d5\\u05ea \\u05d4\\u05e9\\u05d7\\u05e8. "',
)
for a, b in [("\\u05E", "\\u05e"), ("\\u05D", "\\u05d"), ("\\u05C", "\\u05c"), ("\\u05B", "\\u05b")]:
    t = t.replace(a, b)
p.write_text(t, encoding="utf-8")
print("fixed")
