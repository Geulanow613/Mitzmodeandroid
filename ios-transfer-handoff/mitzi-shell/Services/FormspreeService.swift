import Foundation

class FormspreeService {
    private let formspreeEndpoint = "https://formspree.io/f/movjwkbe"
    
    func submitSuggestion(_ suggestion: MitzvahSuggestion) async throws {
        var request = URLRequest(url: URL(string: formspreeEndpoint)!)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        let body = [
            "name": suggestion.name,
            "message": suggestion.description,
            "timestamp": ISO8601DateFormatter().string(from: suggestion.timestamp)
        ]
        
        request.httpBody = try JSONSerialization.data(withJSONObject: body)
        
        let (_, response) = try await URLSession.shared.data(for: request)
        guard let httpResponse = response as? HTTPURLResponse,
              httpResponse.statusCode == 200 else {
            throw URLError(.badServerResponse)
        }
    }
} 