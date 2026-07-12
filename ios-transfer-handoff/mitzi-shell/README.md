# Mitz Mode App

## Project Structure
```
Mitz Mode/
├── MitzvahApp.swift        # Main app entry point
├── ContentView.swift       # Root view of the app
├── MitzModeViewModel.swift # Main view model
├── Assets.xcassets/       # Images and colors
├── Resources/            # Video and other resources
│   ├── Videos/          # All video content
│   │   ├── Background/  # Background videos
│   │   └── Rewards/     # Reward videos
│   └── Data/           # JSON and other data files
├── Models/              # Data models
│   └── Mitzvah.swift
└── Views/               # All SwiftUI views
    ├── Core/           # Main app views
    │   ├── MainView.swift
    │   └── HomeView.swift
    ├── Features/       # Feature-specific views
    │   ├── Checklist/
    │   ├── Prayer/
    │   └── Rewards/
    └── Components/     # Reusable components
```

## Setting up in Xcode
1. Create a new Xcode project:
   - Choose iOS App template
   - Product Name: "Mitz Mode"
   - Interface: SwiftUI
   - Language: Swift
   - Minimum Deployment: iOS 15.0

2. Delete the template files created by Xcode:
   - ContentView.swift
   - YourAppName.swift
   - Assets.xcassets

3. Before dragging files:
   - Create the following groups in Xcode:
     - Views
     - Models
     - Resources
     - Assets.xcassets

4. Drag and drop files:
   - All .swift files from root Mitz Mode directory to root group
   - Views directory contents to Views group
   - Models directory to Models group
   - Resources directory contents to Resources group
   - Assets.xcassets contents to Assets.xcassets

5. In Xcode's target settings:
   - Set minimum iOS version to 15.0
   - Add required capabilities:
     - Background video playback
     - Background audio (if needed)
   - Update the Bundle Identifier
   - Add Privacy Usage Descriptions if needed

6. Sentry Setup:
   - Add your actual Sentry DSN in MitzvahApp.swift
   - Debug mode will be automatically enabled for development
   - Production mode will be used for releases

## Dependencies
- Sentry SDK for crash reporting

## Development Notes
- The app uses SwiftUI for all views
- Follows MVVM architecture
- Uses native haptics and video playback
- Includes optimized video background with fallback for older devices
- Debug crash reporting (hidden feature)

## Video Optimization
Background video has been optimized:
- Resolution: 768x1280 (portrait)
- Framerate: 24fps
- Bitrate: 2Mbps
- Format: H.264
- Size: ~2.3MB 