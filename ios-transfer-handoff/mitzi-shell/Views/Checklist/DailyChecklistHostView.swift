import SwiftUI
import shared

/// Hosts the unified Mitz Mode + checklist shell (Kotlin Compose).
/// Checklist opens first; center tab pops a mitzvah (shared `mitzvahbutton` drawable);
/// Blessings / Status are bottom-nav tabs. Rebuild the shared pod on Mac after KMP changes.
struct DailyChecklistHostView: UIViewControllerRepresentable {
    var onClose: (() -> Void)? = nil

    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.EmbeddedChecklistViewController(onClose: {
            onClose?()
        })
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}