import SwiftUI

struct MitzvahDefinitionView: View {
    @Binding var isPresented: Bool
    @State private var textSize: CGFloat = 18

    private let definitionText = """
The word mitzvah (מִצְוָה) literally means "commandment." In Judaism, mitzvot are the 613 commandments given by G-d through the Torah, and also some extra mitzvot which our rabbis, through Divine inspiration, added on. 

While mitzvah means commandment, it also carries the deeper meaning of "connection." By performing a mitzvah, we fulfill G-d's will and connect with the Divine. It's like following instructions from a loved one; by doing so, you strengthen your bond with them. 

G-d is constantly sending down pure Heavenly Light which sustains the world and everything in it. If you go against His will, it's like putting an umbrella between yourself and this Light. But by performing mitzvot you can connect with the Heavenly Goodness that is G-d Himself, and experience Heaven on Earth- in a state you might like to call "Mitz Mode." It might not happen right away, but do a few mitzvot and see how you feel. This is just the beginning...
"""

    var body: some View {
        ZStack {
            AppColor.scrim
                .ignoresSafeArea()
                .onTapGesture { isPresented = false }

            VStack(spacing: 0) {
                MenuSheetHeaderBar(title: "What's a Mitzvah?", onClose: { isPresented = false })
                MenuSheetGoldHairline()

                ScrollView {
                    TranslatableText(
                        source: definitionText,
                        font: .system(size: textSize, weight: .regular, design: .serif),
                        color: AppColor.textPrimary,
                        alignment: .center
                    )
                    .padding(.horizontal, 18)
                    .padding(.vertical, 16)
                }
                .frame(maxHeight: UIScreen.main.bounds.height * 0.52)

                HStack(spacing: 20) {
                    Button(action: { textSize = max(12, textSize - 2) }) {
                        Text("A−")
                            .font(.system(size: 16, weight: .semibold, design: .serif))
                            .foregroundColor(AppColor.navyPrimary)
                            .frame(width: 44, height: 40)
                    }
                    .buttonStyle(.plain)
                    Button(action: { textSize = min(32, textSize + 2) }) {
                        Text("A+")
                            .font(.system(size: 16, weight: .semibold, design: .serif))
                            .foregroundColor(AppColor.navyPrimary)
                            .frame(width: 44, height: 40)
                    }
                    .buttonStyle(.plain)
                }
                .padding(.vertical, 10)

                TranslatableHeirloomButton(source: "Close", enabled: true) {
                    isPresented = false
                }
                .frame(maxWidth: .infinity)
                .padding(.horizontal, 28)
                .padding(.bottom, 16)
            }
            .background(
                RoundedRectangle(cornerRadius: AppRadius.card, style: .continuous)
                    .fill(
                        LinearGradient(
                            colors: [AppColor.parchmentTop, AppColor.parchmentMid, AppColor.parchmentBase],
                            startPoint: .top,
                            endPoint: .bottom
                        )
                    )
            )
            .clipShape(RoundedRectangle(cornerRadius: AppRadius.card, style: .continuous))
            .overlay(
                RoundedRectangle(cornerRadius: AppRadius.card, style: .continuous)
                    .stroke(AppColor.goldBorder.opacity(0.55), lineWidth: 1.2)
            )
            .shadow(color: .black.opacity(0.22), radius: 14, y: 6)
            .padding(.horizontal, 20)
        }
    }
}

