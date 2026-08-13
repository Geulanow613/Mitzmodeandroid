import SwiftUI
import shared

struct TranslationNoticeView: View {
    let languageName: String
    let targetLanguageCode: String
    let onDismiss: () -> Void
    let onUnderstood: () -> Void

    @State private var title = AppDisclaimer.shared.TRANSLATION_NOTICE_TITLE
    @State private var switchedTo = "You've switched to"
    @State private var disclaimer = AppDisclaimer.shared.TRANSLATION_NOTICE_DISCLAIMER
    @State private var englishLabel = "English:"
    @State private var gotIt = "Got it"
    @State private var closeLabel = "Close"
    @State private var linksNote = AppDisclaimer.shared.TRANSLATION_LINKS_NOTE

    var body: some View {
        NavigationView {
            ScrollView {
                VStack(spacing: 16) {
                    Image(systemName: "info.circle.fill")
                        .font(.system(size: 44))
                        .foregroundColor(AppColor.androidGoldSpot)

                    Text(title)
                        .font(.title3.weight(.bold))
                        .multilineTextAlignment(.center)

                    Text("\(switchedTo) \(languageName).")
                        .font(.body)
                        .multilineTextAlignment(.center)

                    Text(disclaimer)
                        .font(.body)
                        .multilineTextAlignment(.center)

                    Text(linksNote)
                        .font(.caption)
                        .foregroundColor(AppColor.textSecondary)
                        .multilineTextAlignment(.center)

                    Link(AppDisclaimer.shared.FEEDBACK_EMAIL, destination: URL(string: AppDisclaimer.shared.FEEDBACK_EMAIL_MAILTO)!)
                        .font(.body.weight(.semibold))

                    if targetLanguageCode != "en" {
                        Divider()
                        Text(englishLabel)
                            .font(.subheadline.weight(.semibold))
                            .foregroundColor(AppColor.textSecondary)
                        Text(AppDisclaimer.shared.TRANSLATION_NOTICE_DISCLAIMER)
                            .font(.subheadline)
                            .foregroundColor(AppColor.textSecondary)
                            .multilineTextAlignment(.center)
                        Text(AppDisclaimer.shared.TRANSLATION_LINKS_NOTE)
                            .font(.caption)
                            .foregroundColor(AppColor.textSecondary)
                            .multilineTextAlignment(.center)
                        Link(AppDisclaimer.shared.FEEDBACK_EMAIL, destination: URL(string: AppDisclaimer.shared.FEEDBACK_EMAIL_MAILTO)!)
                            .font(.subheadline.weight(.semibold))
                    }

                    Button(action: onUnderstood) {
                        Text(gotIt)
                            .font(.body.weight(.semibold))
                            .frame(maxWidth: .infinity)
                            .padding(.vertical, 12)
                            .background(AppColor.androidGoldSpot)
                            .foregroundColor(Color(red: 26 / 255, green: 26 / 255, blue: 26 / 255))
                            .clipShape(RoundedRectangle(cornerRadius: 12, style: .continuous))
                    }
                    .buttonStyle(.plain)
                }
                .padding(24)
            }
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button(action: onDismiss) {
                        Text(closeLabel)
                    }
                }
            }
        }
        .task(id: targetLanguageCode) {
            guard targetLanguageCode != "en" else { return }
            let store = AppTranslationStore.shared
            title = await store.translateToLanguage(AppDisclaimer.shared.TRANSLATION_NOTICE_TITLE, languageCode: targetLanguageCode)
            switchedTo = await store.translateToLanguage("You've switched to", languageCode: targetLanguageCode)
            disclaimer = await store.translateToLanguage(AppDisclaimer.shared.TRANSLATION_NOTICE_DISCLAIMER, languageCode: targetLanguageCode)
            englishLabel = await store.translateToLanguage("English:", languageCode: targetLanguageCode)
            gotIt = await store.translateToLanguage("Got it", languageCode: targetLanguageCode)
            closeLabel = await store.translateToLanguage("Close", languageCode: targetLanguageCode)
            linksNote = await store.translateToLanguage(AppDisclaimer.shared.TRANSLATION_LINKS_NOTE, languageCode: targetLanguageCode)
        }
    }
}

#if DEBUG
struct TranslationNoticeView_Previews: PreviewProvider {
    static var previews: some View {
        TranslationNoticeView(
            languageName: "Hebrew",
            targetLanguageCode: "he",
            onDismiss: {},
            onUnderstood: {}
        )
    }
}
#endif
