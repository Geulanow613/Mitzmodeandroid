import Foundation

public struct MitzvahSuggestion: Codable {
    public let name: String
    public let description: String
    public let timestamp: Date
    
    public init(name: String, description: String, timestamp: Date) {
        self.name = name
        self.description = description
        self.timestamp = timestamp
    }
} 