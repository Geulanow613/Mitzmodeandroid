import SwiftUI

struct InfoDialog: View {
    let text: String
    let explanation: String
    let link: ExternalLink?
    @Environment(\.dismiss) private var dismiss

    var body: some View {
        NavigationView {
            ZStack {
                LinearGradient(
                    colors: [AppColor.parchmentTop, AppColor.parchmentMid, AppColor.parchmentBase],
                    startPoint: .top,
                    endPoint: .bottom
                )
                .ignoresSafeArea()

                ScrollView {
                    VStack(alignment: .leading, spacing: 16) {
                        Text(text)
                            .font(.system(size: 18, weight: .semibold, design: .serif))
                            .foregroundColor(AppColor.goldBorder)
                            .padding(.bottom, 4)

                        Text(explanation)
                            .font(.system(size: 16, weight: .regular, design: .serif))
                            .foregroundColor(AppColor.textPrimary)
                            .lineSpacing(3)

                        if let link = link {
                            Divider()
                                .background(AppColor.goldBorder.opacity(0.35))
                                .padding(.vertical, 4)

                            Text("Useful Resources:")
                                .font(.headline)
                                .foregroundColor(AppColor.textPrimary)
                                .padding(.bottom, 4)

                            AppLinkText(
                                displayText: link.displayText,
                                urlString: link.url,
                                fontSize: 17
                            )
                            .frame(maxWidth: .infinity, alignment: .leading)
                        }
                    }
                    .padding()
                }
            }
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button("Close") { dismiss() }
                        .foregroundColor(AppColor.navyPrimary)
                }
            }
        }
    }
}
