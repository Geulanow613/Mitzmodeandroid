from pathlib import Path

p = Path(r"c:\apps\hehehe\be-a-tzaddik\tools\overlay_parts\a.py")
text = p.read_text(encoding="utf-8")
# Fix mixed-script etrog
text = text.replace("אתrog", "אתrog")
# proper etrog in Hebrew
text = text.replace("אתrog", "אתrog")
# Use explicit unicode for etrog word
bad = "א" + "ת" + "r" + "o" + "g"
good = "\u05d0\u05ea\u05e8\u05d5\u05d2"
text = text.replace(bad, good)
p.write_text(text, encoding="utf-8")
print("fixed etrog")
