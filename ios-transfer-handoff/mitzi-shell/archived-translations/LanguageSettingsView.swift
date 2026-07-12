import SwiftUI
import shared

struct LanguageSettingsView: View {
    @Environment(\.dismiss) private var dismiss
    @ObservedObject private var store = AppTranslationStore.shared

    @State private var enabled: Bool = false
    @State private var selectedLanguage: String = "en"
    @State private var pendingNotice: (code: String, name: String)?

    var body: some View {
        NavigationView {
            ScrollView {
                VStack(alignment: .leading, spacing: 16) {
                    VStack(spacing: 6) {
                        TranslatableText(
                            source: "Translate the app!",
                            font: .title2,
                            weight: .bold,
                            color: AppColor.androidGoldSpot,
                            alignment: .center
                        )
                        TranslatableText(
                            source: "(translations may not be perfect)",
                            font: .subheadline,
                            color: AppColor.textSecondary,
                            alignment: .center
                        )
                        TranslatableText(
                            source: AppDisclaimer.shared.TRANSLATION_LINKS_NOTE,
                            font: .caption,
                            color: AppColor.textSecondary,
                            alignment: .center
                        )
                    }
                    .frame(maxWidth: .infinity)

                    TranslatableText(
                        source: "Language Settings",
                        font: .title3,
                        weight: .semibold
                    )

                    Toggle(isOn: $enabled) {
                        TranslatableText(source: "Enable Translation", font: .body)
                    }
                    .onChange(of: enabled) { newValue in
                        store.setTranslationEnabled(newValue)
                        if !newValue {
                            store.setCurrentLanguage("en")
                            selectedLanguage = "en"
                        }
                    }

                    if enabled {
                        TranslatableText(
                            source: "Select Language",
                            font: .headline,
                            weight: .semibold
                        )
                        .padding(.top, 4)

                        LazyVStack(spacing: 0) {
                            ForEach(store.supportedLanguages(), id: \.code) { language in
                                languageRow(language)
                            }
                        }
                    }
                }
                .padding(20)
            }
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .confirmationAction) {
                    Button(action: { dismiss() }) {
                        TranslatableText(source: "Close", font: .body, weight: .semibold)
                    }
                }
            }
        }
        .onAppear {
            enabled = store.translationEnabled
            selectedLanguage = store.currentLanguage
        }
        .sheet(item: Binding(
            get: {
                pendingNotice.map { PendingNotice(code: $0.code, name: $0.name) }
            },
            set: { pending in
                pendingNotice = pending.map { ($0.code, $0.name) }
            }
        )) { notice in
            TranslationNoticeView(
                languageName: notice.name,
                targetLanguageCode: notice.code,
                onDismiss: { pendingNotice = nil },
                onUnderstood: { pendingNotice = nil }
            )
        }
    }

    private func languageRow(_ language: LanguageOption) -> some View {
        let isSelected = selectedLanguage == language.code
        return Button {
            store.setCurrentLanguage(language.code)
            selectedLanguage = language.code
            pendingNotice = (language.code, language.englishName)
        } label: {
            HStack {
                VStack(alignment: .leading, spacing: 2) {
                    Text(language.nativeName)
                        .font(.body.weight(.medium))
                        .foregroundColor(AppColor.textPrimary)
                    Text(language.englishName)
                        .font(.caption)
                        .foregroundColor(AppColor.textSecondary)
                }
                Spacer()
                if isSelected {
                    Image(systemName: "checkmark.circle.fill")
                        .foregroundColor(AppColor.androidGoldSpot)
                }
            }
            .padding(.vertical, 12)
            .padding(.horizontal, 8)
            .background(
                RoundedRectangle(cornerRadius: 10, style: .continuous)
                    .fill(isSelected ? AppColor.androidGoldSpot.opacity(0.12) : Color.clear)
            )
        }
        .buttonStyle(.plain)
    }
}

private struct PendingNotice: Identifiable {
    let id = UUID()
    let code: String
    let name: String
}

#if DEBUG
struct LanguageSettingsView_Previews: PreviewProvider {
    static var previews: some View {
        LanguageSettingsView()
    }
}
#endif
