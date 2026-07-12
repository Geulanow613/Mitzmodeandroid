import Foundation

enum GoogleTranslateService {
    static func translate(text: String, targetLanguage: String) async -> String? {
        let apiKey = Secrets.googleTranslateApiKey.trimmingCharacters(in: .whitespacesAndNewlines)
        guard !apiKey.isEmpty, apiKey != "YOUR_GOOGLE_TRANSLATE_API_KEY_HERE" else {
            return nil
        }
        guard !text.isEmpty else { return text }

        var components = URLComponents(string: "https://translation.googleapis.com/language/translate/v2")!
        components.queryItems = [URLQueryItem(name: "key", value: apiKey)]
        guard let url = components.url else { return nil }

        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/json; charset=utf-8", forHTTPHeaderField: "Content-Type")

        let body: [String: Any] = [
            "q": [text],
            "target": targetLanguage,
            "format": "text",
        ]
        guard let bodyData = try? JSONSerialization.data(withJSONObject: body) else { return nil }
        request.httpBody = bodyData

        do {
            let (data, response) = try await URLSession.shared.data(for: request)
            guard let http = response as? HTTPURLResponse, (200...299).contains(http.statusCode) else {
                return nil
            }
            let json = try JSONSerialization.jsonObject(with: data) as? [String: Any]
            let translations = (json?["data"] as? [String: Any])?["translations"] as? [[String: Any]]
            return translations?.first?["translatedText"] as? String
        } catch {
            return nil
        }
    }
}
