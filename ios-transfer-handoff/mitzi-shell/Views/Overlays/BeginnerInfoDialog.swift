import SwiftUI

struct BeginnerInfoDialog: View {
    @Binding var isPresented: Bool

    var body: some View {
        ZStack(alignment: .topTrailing) {
            LinearGradient(
                colors: [AppColor.parchmentTop, AppColor.parchmentMid, AppColor.parchmentBase],
                startPoint: .top,
                endPoint: .bottom
            )
            .ignoresSafeArea()

            ScrollView {
                VStack(alignment: .leading, spacing: 16) {
                    Text("Understanding Daily Jewish Observance")
                        .font(.system(size: 20, weight: .semibold, design: .serif))
                        .foregroundColor(AppColor.goldBorder)
                        .frame(maxWidth: .infinity, alignment: .center)
                        .padding(.top, 36)

                    Text(
                        """
                        This checklist shows the basic daily requirements for Torah-observant Jews. If you're just starting your journey:

                        • Take it one mitzvah at a time
                        • Don't feel overwhelmed - growth is gradual
                        • Focus on understanding each mitzvah properly
                        • Consult with a rabbi for proper guidance
                        • Join a supportive Jewish community

                        This list is meant as an educational tool to understand what daily Jewish life entails, not as a source of pressure. The goal is steady growth in observance while maintaining joy in serving Hashem.
                        """
                    )
                    .font(.system(size: 16, weight: .regular, design: .serif))
                    .foregroundColor(AppColor.textPrimary)
                    .lineSpacing(4)
                }
                .padding(.horizontal, 22)
                .padding(.bottom, 24)
            }

            Button(action: { isPresented = false }) {
                Image(systemName: "xmark.circle.fill")
                    .font(.system(size: 26))
                    .foregroundColor(AppColor.goldBorder)
            }
            .padding(.top, 12)
            .padding(.trailing, 14)
            .accessibilityLabel("Close")
        }
    }
}
