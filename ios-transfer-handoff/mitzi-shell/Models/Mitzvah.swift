import Foundation

/// Bundled local JSON (`mitzvotlistfull.json`) — may omit `version`.
struct MitzvotData: Codable {
    let mitzvot: [Mitzvah]
    var version: Int?

    init(mitzvot: [Mitzvah], version: Int? = nil) {
        self.mitzvot = mitzvot
        self.version = version
    }
}

/// Cloud envelope + on-disk cache file (matches Android `MitzvotList`).
struct MitzvotListEnvelope: Codable {
    let mitzvot: [Mitzvah]
    let version: Int

    enum CodingKeys: String, CodingKey {
        case mitzvot
        case version
    }

    init(mitzvot: [Mitzvah], version: Int) {
        self.mitzvot = mitzvot
        self.version = version
    }

    init(from decoder: Decoder) throws {
        let c = try decoder.container(keyedBy: CodingKeys.self)
        mitzvot = try c.decode([Mitzvah].self, forKey: .mitzvot)
        version = try c.decodeIfPresent(Int.self, forKey: .version) ?? 1
    }

    func encode(to encoder: Encoder) throws {
        var c = encoder.container(keyedBy: CodingKeys.self)
        try c.encode(mitzvot, forKey: .mitzvot)
        try c.encode(version, forKey: .version)
    }
}

struct Mitzvah: Codable, Identifiable {
    let id: String
    let text: String
    let links: [ExternalLink]
    
    init(id: String, text: String, links: [ExternalLink] = []) {
        self.id = id
        self.text = text
        self.links = links
    }
} 