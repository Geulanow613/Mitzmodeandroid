import SwiftUI

struct AboutView: View {
    @Environment(\.dismiss) private var dismiss

    private let privacyPolicyURL = "https://geulanow613.github.io/mitzmodeprivacypolicy/"
    private let websiteURL = "https://www.beardy.top"
    private let currentYear = Calendar.current.component(.year, from: Date())

    var body: some View {
        ZStack {
            LinearGradient(
                colors: [AppColor.parchmentTop, AppColor.parchmentMid, AppColor.parchmentBase],
                startPoint: .top,
                endPoint: .bottom
            )
            .ignoresSafeArea()

            VStack(spacing: 0) {
                MenuSheetHeaderBar(title: "About", onClose: { dismiss() })
                MenuSheetGoldHairline()

                ScrollView {
                    VStack(spacing: 18) {
                        TranslatableText(
                            source: "This app is brought to you by",
                            font: .system(size: 15, weight: .medium, design: .serif),
                            color: AppColor.textMuted,
                            alignment: .center
                        )

                        TranslatableText(
                            source: "Beardy Top Productions",
                            font: .system(size: 16, weight: .semibold, design: .serif),
                            weight: .semibold,
                            color: AppColor.textPrimary,
                            alignment: .center
                        )

                        AppLinkText(displayText: "www.beardy.top", urlString: websiteURL, fontSize: 16)
                            .accessibilityHint("Opens Beardy Top website in browser")

                        Rectangle()
                            .fill(AppColor.goldBorder.opacity(0.35))
                            .frame(height: 0.8)
                            .padding(.vertical, 6)

                        AppLinkText(displayText: "Privacy Policy", urlString: privacyPolicyURL, fontSize: 16)
                            .accessibilityHint("Opens privacy policy in browser")

                        Text(verbatim: "© \(currentYear) Beardy Top Productions")
                            .font(.system(size: 14, weight: .medium, design: .serif))
                            .foregroundColor(AppColor.textMuted)

                        StarOfDavid()
                            .stroke(AppColor.goldBorder.opacity(0.55), lineWidth: 1.2)
                            .frame(width: 28, height: 28)
                            .frame(maxWidth: .infinity)
                            .padding(.vertical, 6)
                            .accessibilityHidden(true)

                        Spacer(minLength: 8)

                        Button(action: { dismiss() }) {
                            TranslatableText(
                                source: "Close",
                                font: .system(size: 16, weight: .semibold, design: .serif),
                                weight: .semibold,
                                color: AppColor.textOnDark,
                                alignment: .center
                            )
                                .padding(.vertical, 11)
                                .padding(.horizontal, 28)
                                .background(
                                    Capsule()
                                        .fill(
                                            LinearGradient(
                                                colors: [AppColor.navyPrimary, AppColor.navyDeep],
                                                startPoint: .top,
                                                endPoint: .bottom
                                            )
                                        )
                                        .overlay(Capsule().stroke(AppColor.goldBorder.opacity(0.65), lineWidth: 1))
                                )
                        }
                        .padding(.top, 8)
                    }
                    .padding(24)
                    .multilineTextAlignment(.center)
                }
            }
        }
        .modifier(AboutViewModifier())
    }
}

struct AboutViewModifier: ViewModifier {
    func body(content: Content) -> some View {
        if #available(iOS 16.0, *) {
            content.presentationDetents([.height(400)])
        } else {
            content
        }
    }
}
