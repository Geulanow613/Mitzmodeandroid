import Foundation
import os.log

/// Mirrors Android `MitzvotLoader` + `MitzvotRepository` cloud cache behavior (ETag, version, merge by id).
enum MitzvotCloudConfig {
    static let githubURL = URL(string: "https://raw.githubusercontent.com/Geulanow613/mitzmode/main/mitzvotcloud.json")!
    static let cacheFileName = "mitzvotcloud_cache.json"
    static let userAgent = "MitzMode-iOS-App"
    /// Must match Android `REQUIRED_MIN_CLOUD_VERSION`.
    static let requiredMinCloudVersion = 2
    static let etagKey = "cloud_etag"
    static let cloudVersionKey = "cloud_version"
    static let prefsSuiteName = "mitzvot_cache"
}

private let mitzvotCloudLog = Logger(
    subsystem: Bundle.main.bundleIdentifier ?? "MitzMode",
    category: "MitzvotCloud"
)

/// Synchronous cache read for cache-first startup (same rules as Android `readCloudCache`).
func readCachedCloudMitzvotSync() -> [Mitzvah] {
    let prefs = UserDefaults(suiteName: MitzvotCloudConfig.prefsSuiteName) ?? .standard
    let url = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)[0]
        .appendingPathComponent(MitzvotCloudConfig.cacheFileName, isDirectory: false)
    guard FileManager.default.fileExists(atPath: url.path) else { return [] }
    do {
        let data = try Data(contentsOf: url)
        let envelope = try JSONDecoder().decode(MitzvotListEnvelope.self, from: data)
        if envelope.version < MitzvotCloudConfig.requiredMinCloudVersion {
            try? FileManager.default.removeItem(at: url)
            prefs.removeObject(forKey: MitzvotCloudConfig.etagKey)
            prefs.removeObject(forKey: MitzvotCloudConfig.cloudVersionKey)
            return []
        }
        return envelope.mitzvot
    } catch {
        mitzvotCloudLog.error("readCachedCloudMitzvotSync decode failed: \(error.localizedDescription)")
        try? FileManager.default.removeItem(at: url)
        return []
    }
}

actor MitzvotCloudRepository {
    static let shared = MitzvotCloudRepository()

    private var prefs: UserDefaults {
        UserDefaults(suiteName: MitzvotCloudConfig.prefsSuiteName) ?? .standard
    }

    private var cacheFileURL: URL {
        FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)[0]
            .appendingPathComponent(MitzvotCloudConfig.cacheFileName, isDirectory: false)
    }

    private func saveCloudCache(mitzvot: [Mitzvah], etag: String?, version: Int) throws {
        let envelope = MitzvotListEnvelope(mitzvot: mitzvot, version: version)
        let data = try JSONEncoder().encode(envelope)
        try data.write(to: cacheFileURL, options: .atomic)
        if let etag {
            prefs.set(etag, forKey: MitzvotCloudConfig.etagKey)
        }
        prefs.set(version, forKey: MitzvotCloudConfig.cloudVersionKey)
    }

    private func storedETag() -> String? {
        prefs.string(forKey: MitzvotCloudConfig.etagKey)
    }

    /// `storedEtag == nil` skips `If-None-Match` (force refresh).
    func fetchCloudConditional(storedEtag: String?) async throws -> CloudFetchResult {
        var request = URLRequest(url: MitzvotCloudConfig.githubURL)
        request.httpMethod = "GET"
        request.timeoutInterval = 25
        request.setValue(MitzvotCloudConfig.userAgent, forHTTPHeaderField: "User-Agent")
        request.setValue("application/json", forHTTPHeaderField: "Accept")
        request.setValue("close", forHTTPHeaderField: "Connection")
        if let storedEtag {
            request.setValue(storedEtag, forHTTPHeaderField: "If-None-Match")
        }

        let (data, response) = try await URLSession.shared.data(for: request)
        guard let http = response as? HTTPURLResponse else {
            throw URLError(.badServerResponse)
        }

        if http.statusCode == 304 {
            mitzvotCloudLog.debug("GET mitzvotcloud.json → 304 (cached copy still valid)")
            return CloudFetchResult(mitzvot: [], etag: storedEtag, wasModified: false, version: prefs.integer(forKey: MitzvotCloudConfig.cloudVersionKey))
        }

        guard http.statusCode == 200 else {
            mitzvotCloudLog.error("GET mitzvotcloud.json HTTP \(http.statusCode)")
            throw URLError(.badServerResponse)
        }

        let newEtag = http.value(forHTTPHeaderField: "ETag")
        let envelope = try JSONDecoder().decode(MitzvotListEnvelope.self, from: data)
        mitzvotCloudLog.debug("GET mitzvotcloud.json → 200, \(envelope.mitzvot.count) items, version \(envelope.version)")
        return CloudFetchResult(mitzvot: envelope.mitzvot, etag: newEtag, wasModified: true, version: envelope.version)
    }

    /// After a successful 200 fetch, persist cloud-only payload + metadata.
    func persistCloudResult(_ result: CloudFetchResult) throws {
        guard result.wasModified, !result.mitzvot.isEmpty else { return }
        guard result.version >= MitzvotCloudConfig.requiredMinCloudVersion else {
            mitzvotCloudLog.error("Rejecting cloud envelope: version \(result.version) < required \(MitzvotCloudConfig.requiredMinCloudVersion)")
            return
        }
        try saveCloudCache(mitzvot: result.mitzvot, etag: result.etag, version: result.version)
    }

    func currentStoredETag() -> String? {
        storedETag()
    }

    /// Fetches when online; returns **fresh cloud list** after a successful 200 + persist (so UI does not depend on a second disk read).
    /// - Returns: `nil` if unchanged (304), offline/error, empty payload, or version too low.
    func runBackgroundSyncIfOnline() async -> [Mitzvah]? {
        let etag = storedETag()
        do {
            let result = try await fetchCloudConditional(storedEtag: etag)
            if !result.wasModified {
                return nil
            }
            guard !result.mitzvot.isEmpty else {
                mitzvotCloudLog.error("Cloud response marked modified but mitzvot array empty")
                return nil
            }
            guard result.version >= MitzvotCloudConfig.requiredMinCloudVersion else {
                mitzvotCloudLog.error("Cloud version \(result.version) below minimum \(MitzvotCloudConfig.requiredMinCloudVersion)")
                return nil
            }
            try persistCloudResult(result)
            mitzvotCloudLog.info("Cloud mitzvot cache updated: \(result.mitzvot.count) mitzvot, version \(result.version)")
            await MainActor.run {
                NotificationCenter.default.post(name: .mitzvotCloudDidUpdate, object: nil)
            }
            return result.mitzvot
        } catch {
            mitzvotCloudLog.error("Cloud mitzvot sync failed: \(error.localizedDescription)")
            return nil
        }
    }

    /// Force refresh: no If-None-Match.
    func forceRefreshFromNetwork() async throws -> CloudFetchResult {
        let result = try await fetchCloudConditional(storedEtag: nil)
        if result.wasModified, !result.mitzvot.isEmpty {
            try persistCloudResult(result)
            await MainActor.run {
                NotificationCenter.default.post(name: .mitzvotCloudDidUpdate, object: nil)
            }
        }
        return result
    }
}

struct CloudFetchResult: Sendable {
    let mitzvot: [Mitzvah]
    let etag: String?
    let wasModified: Bool
    let version: Int
}

extension Notification.Name {
    static let mitzvotCloudDidUpdate = Notification.Name("mitzvotCloudDidUpdate")
}

extension Array where Element == Mitzvah {
    /// First occurrence wins (local + cloud order), same as Kotlin `distinctBy { it.id }` on concatenated lists.
    func mergedDistinctFirstWins() -> [Mitzvah] {
        var seen = Set<String>()
        var out: [Mitzvah] = []
        for m in self {
            if seen.insert(m.id).inserted { out.append(m) }
        }
        return out
    }
}
