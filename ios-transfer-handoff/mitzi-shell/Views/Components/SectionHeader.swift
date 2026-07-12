import SwiftUI

/// Section title styled like Android `DailyMitzvotChecklist` `ChecklistSection`.
struct SectionHeader: View {
    let title: String
    @Environment(\.checklistTextSize) private var textSize

    var body: some View {
        VStack(alignment: .leading, spacing: 6) {
            HStack(alignment: .center, spacing: 8) {
                RoundedRectangle(cornerRadius: 2, style: .continuous)
                    .fill(
                        LinearGradient(
                            colors: [AppColor.goldBright, AppColor.goldBorder],
                            startPoint: .top,
                            endPoint: .bottom
                        )
                    )
                    .frame(width: 3, height: 20)

                Text(title)
                    .font(.system(size: textSize + 2, weight: .bold, design: .serif))
                    .foregroundColor(AppColor.navyDeep)
            }
            .padding(.bottom, 2)

            Rectangle()
                .fill(
                    LinearGradient(
                        colors: [AppColor.goldBorder.opacity(0.55), .clear],
                        startPoint: .leading,
                        endPoint: .trailing
                    )
                )
                .frame(height: 0.8)

            Spacer().frame(height: 4)
        }
        .padding(.top, 8)
    }
}
