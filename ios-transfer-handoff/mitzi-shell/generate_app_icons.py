import os
import sys
from PIL import Image

def generate_app_icons(source_image_path, output_dir="Assets.xcassets/AppIcon.appiconset"):
    # Create output directory if it doesn't exist
    if not os.path.exists(output_dir):
        os.makedirs(output_dir)
    
    # Define required sizes for iOS app icons
    icon_sizes = {
        # iPhone
        "Icon-20@2x.png": (40, 40),    # 20pt@2x
        "Icon-20@3x.png": (60, 60),    # 20pt@3x
        "Icon-29@2x.png": (58, 58),    # 29pt@2x
        "Icon-29@3x.png": (87, 87),    # 29pt@3x
        "Icon-40@2x.png": (80, 80),    # 40pt@2x
        "Icon-40@3x.png": (120, 120),  # 40pt@3x
        "Icon-60@2x.png": (120, 120),  # 60pt@2x
        "Icon-60@3x.png": (180, 180),  # 60pt@3x
        
        # iPad
        "Icon-20.png": (20, 20),       # 20pt
        "Icon-29.png": (29, 29),       # 29pt
        "Icon-40.png": (40, 40),       # 40pt
        "Icon-76.png": (76, 76),       # 76pt
        "Icon-76@2x.png": (152, 152),  # 76pt@2x
        "Icon-83.5@2x.png": (167, 167),# 83.5pt@2x
        
        # App Store
        "Icon-1024.png": (1024, 1024)  # App Store
    }
    
    try:
        print(f"Opening source image: {source_image_path}")
        # Open source image
        with Image.open(source_image_path) as img:
            # Convert to RGBA if necessary
            if img.mode != 'RGBA':
                img = img.convert('RGBA')
            
            # Generate each icon size
            for filename, size in icon_sizes.items():
                # Resize image
                resized = img.resize(size, Image.Resampling.LANCZOS)
                
                # Save resized image
                output_path = os.path.join(output_dir, filename)
                resized.save(output_path, 'PNG', quality=95)
                print(f"Generated {filename} ({size[0]}x{size[1]})")
        
        print("\nApp icons generated successfully!")
        print(f"Output directory: {os.path.abspath(output_dir)}")
        
    except Exception as e:
        print(f"Error generating icons: {str(e)}")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Please provide the path to the source image as an argument")
        sys.exit(1)
    source_image = sys.argv[1]
    generate_app_icons(source_image) 