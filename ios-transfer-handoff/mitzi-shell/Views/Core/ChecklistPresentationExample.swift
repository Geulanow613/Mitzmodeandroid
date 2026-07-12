import SwiftUI

/// Example wiring for `ContentView` — present the embedded checklist with counter bridge.
///
/// Replace your existing `.fullScreenCover` checklist presentation with:
///
/// ```swift
/// .fullScreenCover(isPresented: $showingChecklist) {
///     DailyChecklistHostView(viewModel: viewModel) {
///         showingChecklist = false
///     }
/// }
/// ```
struct ChecklistPresentationExample: View {
    @StateObject private var viewModel = DailyMitzvotViewModel()
    @State private var showingChecklist = false

    var body: some View {
        Text("See ChecklistPresentationExample.swift for fullScreenCover wiring")
            .fullScreenCover(isPresented: $showingChecklist) {
                DailyChecklistHostView(viewModel: viewModel) {
                    showingChecklist = false
                }
            }
    }
}
