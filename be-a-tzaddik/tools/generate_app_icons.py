"""Generate Android mipmaps and iOS AppIcon from appicontzaddik.png."""
from __future__ import annotations

import json
from pathlib import Path

from PIL import Image

ROOT = Path(__file__).resolve().parents[1]
SRC = ROOT / "appicontzaddik.png"
TZADEKET_SRC = ROOT / "tzadeketapp.png"
ANDROID_RES = ROOT / "androidApp" / "src" / "main" / "res"
IOS_ICONSET = ROOT / "iosApp" / "iosApp" / "Assets.xcassets" / "AppIcon.appiconset"

# Fraction of canvas reserved on each edge (artwork scaled to fit inside).
# Adaptive icons mask the outer ~33%; legacy icons need room so the circle isn't clipped.
LAUNCHER_PADDING = 0.14
ADAPTIVE_FOREGROUND_PADDING = 0.22
IOS_PADDING = 0.14

# Launcher PNG (legacy + round)
ANDROID_LAUNCHER = {
    "mipmap-mdpi": 48,
    "mipmap-hdpi": 72,
    "mipmap-xhdpi": 96,
    "mipmap-xxhdpi": 144,
    "mipmap-xxxhdpi": 192,
}

# Adaptive foreground layers (108dp @ density)
ANDROID_FOREGROUND = {
    "mipmap-mdpi": 108,
    "mipmap-hdpi": 162,
    "mipmap-xhdpi": 216,
    "mipmap-xxhdpi": 324,
    "mipmap-xxxhdpi": 432,
}

# iOS AppIcon entries: filename -> pixel size
IOS_ICONS = {
    "Icon-20@2x.png": 40,
    "Icon-20@3x.png": 60,
    "Icon-29@2x.png": 58,
    "Icon-29@3x.png": 87,
    "Icon-40@2x.png": 80,
    "Icon-40@3x.png": 120,
    "Icon-60@2x.png": 120,
    "Icon-60@3x.png": 180,
    "Icon-76@2x.png": 152,
    "Icon-83.5@2x.png": 167,
    "Icon-1024.png": 1024,
}

IOS_CONTENTS = {
    "images": [
        {"filename": "Icon-20@2x.png", "idiom": "iphone", "scale": "2x", "size": "20x20"},
        {"filename": "Icon-20@3x.png", "idiom": "iphone", "scale": "3x", "size": "20x20"},
        {"filename": "Icon-29@2x.png", "idiom": "iphone", "scale": "2x", "size": "29x29"},
        {"filename": "Icon-29@3x.png", "idiom": "iphone", "scale": "3x", "size": "29x29"},
        {"filename": "Icon-40@2x.png", "idiom": "iphone", "scale": "2x", "size": "40x40"},
        {"filename": "Icon-40@3x.png", "idiom": "iphone", "scale": "3x", "size": "40x40"},
        {"filename": "Icon-60@2x.png", "idiom": "iphone", "scale": "2x", "size": "60x60"},
        {"filename": "Icon-60@3x.png", "idiom": "iphone", "scale": "3x", "size": "60x60"},
        {"filename": "Icon-20@2x.png", "idiom": "ipad", "scale": "2x", "size": "20x20"},
        {"filename": "Icon-29@2x.png", "idiom": "ipad", "scale": "2x", "size": "29x29"},
        {"filename": "Icon-40@2x.png", "idiom": "ipad", "scale": "2x", "size": "40x40"},
        {"filename": "Icon-76@2x.png", "idiom": "ipad", "scale": "2x", "size": "76x76"},
        {"filename": "Icon-83.5@2x.png", "idiom": "ipad", "scale": "2x", "size": "83.5x83.5"},
        {"filename": "Icon-1024.png", "idiom": "ios-marketing", "scale": "1x", "size": "1024x1024"},
    ],
    "info": {"author": "xcode", "version": 1},
}


def resize_square(img: Image.Image, size: int, padding_ratio: float = 0.0) -> Image.Image:
    """Scale image to fit in size×size with optional transparent padding."""
    if padding_ratio > 0:
        inner = int(size * (1 - padding_ratio * 2))
        scaled = img.resize((inner, inner), Image.Resampling.LANCZOS)
        out = Image.new("RGBA", (size, size), (0, 0, 0, 0))
        offset = (size - inner) // 2
        out.paste(scaled, (offset, offset), scaled)
        return out
    return img.resize((size, size), Image.Resampling.LANCZOS)


def write_png(path: Path, image: Image.Image) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    image.save(path, format="PNG", optimize=True)


def generate_android_set(
    src: Image.Image,
    launcher_prefix: str,
    foreground_prefix: str,
) -> None:
    for folder, px in ANDROID_LAUNCHER.items():
        out = ANDROID_RES / folder / f"{launcher_prefix}.png"
        write_png(out, resize_square(src, px, padding_ratio=LAUNCHER_PADDING))
        round_out = ANDROID_RES / folder / f"{launcher_prefix}_round.png"
        write_png(round_out, resize_square(src, px, padding_ratio=LAUNCHER_PADDING))

    for folder, px in ANDROID_FOREGROUND.items():
        out = ANDROID_RES / folder / f"{foreground_prefix}.png"
        write_png(out, resize_square(src, px, padding_ratio=ADAPTIVE_FOREGROUND_PADDING))


def main() -> None:
    if not SRC.exists():
        raise SystemExit(f"Missing source icon: {SRC}")

    generate_android_set(
        Image.open(SRC).convert("RGBA"),
        launcher_prefix="ic_launcher",
        foreground_prefix="ic_launcher_foreground",
    )

    if TZADEKET_SRC.exists():
        generate_android_set(
            Image.open(TZADEKET_SRC).convert("RGBA"),
            launcher_prefix="ic_launcher_tzadeket",
            foreground_prefix="ic_launcher_tzadeket_foreground",
        )
        print(f"Generated Tzadeket Android icons from {TZADEKET_SRC.name}")
    else:
        print(f"Skip Tzadeket icons — missing {TZADEKET_SRC}")

    src = Image.open(SRC).convert("RGBA")

    IOS_ICONSET.mkdir(parents=True, exist_ok=True)
    for name, px in IOS_ICONS.items():
        write_png(IOS_ICONSET / name, resize_square(src, px, padding_ratio=IOS_PADDING))
    (IOS_ICONSET / "Contents.json").write_text(
        json.dumps(IOS_CONTENTS, indent=2) + "\n", encoding="utf-8"
    )

    # Asset catalog root so Xcode picks it up
    assets = IOS_ICONSET.parent.parent
    assets.mkdir(parents=True, exist_ok=True)
    contents = {
        "info": {"author": "xcode", "version": 1},
    }
    (assets / "Contents.json").write_text(
        json.dumps(contents, indent=2) + "\n", encoding="utf-8"
    )

    print(f"Generated Android icons under {ANDROID_RES}")
    print(f"Generated iOS AppIcon.appiconset under {IOS_ICONSET}")


if __name__ == "__main__":
    main()
