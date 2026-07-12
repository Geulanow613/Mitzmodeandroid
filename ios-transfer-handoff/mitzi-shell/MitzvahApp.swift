import SwiftUI
import Sentry
import Network
import shared

@main
struct MitzvahApp: App {
    @State private var isOnline = false
    private let monitor = NWPathMonitor()
    private let monitorQueue = DispatchQueue(label: "NetworkMonitor")
    
    init() {
        ChecklistEmbedBridge.shared.preloadChecklistDependencies()
        startMonitoring()
    }
    
    var body: some Scene {
        WindowGroup {
            ZStack {
                Color.white.ignoresSafeArea()
                // Same artwork as `LaunchScreen` — white backdrop matches launch storyboard until video paints.
                Image("LaunchLogo")
                    .renderingMode(.original)
                    .resizable()
                    .interpolation(.high)
                    .scaledToFit()
                    .frame(maxWidth: 300, maxHeight: 300)
                    .allowsHitTesting(false)
                    .accessibilityHidden(true)
                ContentView()
                ToastOverlay(toast: ToastCenter.shared)
            }
        }
    }
    
    private func startMonitoring() {
        monitor.pathUpdateHandler = { path in
            DispatchQueue.main.async {
                self.isOnline = path.status == .satisfied
                if self.isOnline {
                    self.initializeSentry()
                }
            }
        }
        monitor.start(queue: monitorQueue)
    }
    
    private func initializeSentry() {
        #if DEBUG
        SentrySDK.start { options in
            options.dsn = "https://206b82c733d49c409111f53d7a132e1a@o4508615756414976.ingest.us.sentry.io/4508635470626816"
            options.debug = true
            options.enableAutoSessionTracking = true
            options.environment = "development"
            
            // Set tracing options
            options.tracesSampleRate = 1.0
            
            // Enable crash reporting
            options.attachScreenshot = true
        }
        
        // Set tags after initialization
        let appVersion = Bundle.main.infoDictionary?["CFBundleShortVersionString"] as? String ?? "unknown"
        let buildNumber = Bundle.main.infoDictionary?["CFBundleVersion"] as? String ?? "unknown"
        SentrySDK.configureScope { scope in
            scope.setTag(value: appVersion, key: "app_version")
            scope.setTag(value: buildNumber, key: "build_number")
        }
        #else
        SentrySDK.start { options in
            options.dsn = "https://206b82c733d49c409111f53d7a132e1a@o4508615756414976.ingest.us.sentry.io/4508635470626816"
            options.debug = false
            options.enableAutoSessionTracking = true
            options.environment = "production"
            
            // Set tracing options
            options.tracesSampleRate = 0.2
            
            // Enable crash reporting
            options.attachScreenshot = true
        }
        
        // Set tags after initialization
        let appVersion = Bundle.main.infoDictionary?["CFBundleShortVersionString"] as? String ?? "unknown"
        let buildNumber = Bundle.main.infoDictionary?["CFBundleVersion"] as? String ?? "unknown"
        SentrySDK.configureScope { scope in
            scope.setTag(value: appVersion, key: "app_version")
            scope.setTag(value: buildNumber, key: "build_number")
        }
        #endif
    }
} 
