# Checklist → home counter (iOS)

When a user checks a mitzvah in the embedded checklist, the gold mitzvah-count pill appears briefly in the top-right (same as Android) and the home counter increments.

## Files to add/update in your Xcode project

1. `Views/Checklist/DailyChecklistHostView.swift` — wires `ChecklistEmbedBridge` + `EmbeddedChecklistViewController`
2. `ViewModels/DailyMitzvotViewModel+ChecklistCounter.swift` — `incrementFromChecklistItem()`
3. `Views/Core/ContentView.swift` — use `DailyChecklistHostView(viewModel:onClose:)` in your `.fullScreenCover`

## After pulling Kotlin changes

```bash
cd mitzi && pod install
```

Rebuild the shared framework from `../be-a-tzaddik/shared` if needed.

## ContentView wiring

```swift
.fullScreenCover(isPresented: $showingChecklist) {
    DailyChecklistHostView(viewModel: viewModel) {
        showingChecklist = false
    }
}
```

Counter updates persist via existing `acceptedMitzvotCount` / `UserDefaults`. Checklist checks do **not** trigger reward videos.
