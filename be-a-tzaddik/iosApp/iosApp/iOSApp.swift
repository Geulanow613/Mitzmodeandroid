import SwiftUI
import shared

@main
struct iOSApp: App {
    init() {
        // Register the notification delegate before any cold-start tap is delivered.
        MainViewControllerKt.warmUpKashrutNotifications()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
