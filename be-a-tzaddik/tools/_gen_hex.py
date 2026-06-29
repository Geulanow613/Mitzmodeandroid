#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""Generate base64-encoded Hebrew strings for mitzvot_003 build script."""
import base64
import json
from pathlib import Path

# Helper: build string from UTF-8 hex (avoids source encoding issues)
def hx(*hex_chunks: str) -> str:
    return bytes.fromhex("".join(hex_chunks)).decode("utf-8")

# --- String 7: Amen ---
S7 = (
    hx("d7a9d7a8d7d0")  # למד
    + hx("d795")  # ו
    + " "
    + hx("d7a2d7a8")  # על
    + " "
    + hx("d7a4d7a1d7a7")  # פסק
    + " "
    + hx("d7deftd7a4d799d7a2")  # מפתיע
    + ": "
    + hx("d7a9d7a2d7a0d795d7aa")  # לענות
    + " "
    + hx("d7d0d795d7df")  # אמן
    + " "
    + hx("d799d7dbd795d7a1")  # יכול
    + " "
    + hx("d7dcd799d7aa")  # להיות
    + " *"
    + hx("d7d2d791d7d5d7dc")  # גדול
    + " "
    + hx("d799d795d7aad7a8")  # יותר
    + "* "
    + hx("d7de794d7a8d799d7da")  # מלברך
    + " "
    + hx("d7d0d7aa")  # את
    + " "
    + hx("d7d4d791d7a8d7db")  # הברכה
    + " "
    + hx("d7e2d7a6d7de7")  # עצמה
    + "! \U0001f64f "
    + hx("d7d4d7aad7dcd7de7d5d7")  # התלמוד
    + " ("
    + hx("d791d7a8d7db")  # ברכ
    + hx("d795d7aa")  # ות
    + " "
    + hx("d7a0")  # נ
    + "\""
    + hx("d792")  # ג
    + " "
    + hx("d7a2")  # ע
    + "\""
    + hx("d791")  # ב
    + ") "
    + hx("d7de7a2d7a1d799d7a8")  # מעביר
    + " "
    + hx("d795d799d7dbd795d7a5")  # ויכוח
    + ": "
    + hx("d7de7d99")  # מי
    + " "
    + hx("d7d2d791d7d5d7dc")  # גדול
    + " "
    + hx("d799d795d7aad7a8")  # יותר
    + " \u2014 "
    + hx("d7d4d7de794d7a8d799d7da")  # המברך
    + " "
    + hx("d7d0d795")  # או
    + " "
    + hx("d7d4d7a2d795d7a0d795")  # העונה
    + " "
    + hx("d7d0d795d7df")  # אמן
    + "? "
    + hx("d7a8d791d799")  # רבי
    + " "
    + hx("d799d795d7a1d799")  # יוסי
    + " "
    + hx("d7a4d7a1d7a7")  # פסק
    + ': "'
    + hx("d7d4d7a2d795d7a0d795")  # העונה
    + " "
    + hx("d7d0d795d7df")  # אמן
    + " "
    + hx("d7d2d791d7d5d7dc")  # גדול
    + " "
    + hx("d7de7df")  # מן
    + " "
    + hx("d7d4d7de794d7a8d799d7da")  # המברך
    + '!\" '
    + hx("d7dbd799")  # כי
    + " "
    + hx("d7d0d795d7df")  # אמן
    + " "
    + hx("d7d4d795d790")  # הוא
    + " "
    + hx("d7d0d799d7a9d795d7a8")  # אישור
    + " \u2014 "
    + hx("d7d0d7aad7dd")  # אתם
    + " "
    + hx("d7de6d794d799d799d799d7dd")  # מצהירים
    + " "
    + hx("d7e9d7de7d94")  # שמה
    + " "
    + hx("d7a9d7a0d790d7de7")  # שנאמר
    + " "
    + hx("d7d4d795d790")  # הוא
    + " "
    + hx("d7d0d7de7aa")  # אמת
    + " "
    + hx("d7dcd7d2d7de7a8d799")  # לגמרי
    + ". "
    + hx("d7d9d7a9d7d0d7aad7dd")  # כשאתם
    + " "
    + hx("d7d0d795d7d3d799d7dd")  # אומרים
    + " "
    + hx("d7d0d795d7df")  # אמן
    + " "
    + hx("d7dc")  # ל
    + hx("d791d7a8d7db")  # ברכ
    + hx("d794")  # ה
    + ", "
    + hx("d7d0d7aad7dd")  # אתם
    + " "
    + hx("d7de7d0d7a9d799d7a8d799d7dd")  # מאשרים
    + " "
    + hx("d7d0d795d7aa")  # אותה
    + " "
    + hx("d7d5d7d7d7dcd799d7d9d7dd")  # וחולקים
    + " "
    + hx("d7d1")  # ב
    + hx("d6d7dbd7aa")  # זכות
    + hx("d794")  # ה
    + ". "
    + hx("d7d4d7aad7dcd7de7d5d7")  # התלמוד
    + " "
    + hx("d7de7e9d795d7d4")  # משווה
    + " "
    + hx("d7dc")  # ל
    + hx("d7a9d7a0d799")  # שני
    + " "
    + hx("d7a4d795d7a2d7dcd799d7dd")  # פועלים
    + ": "
    + hx("d7d4d7de795d7d1d799d/")  # wrong
)

print("test")
