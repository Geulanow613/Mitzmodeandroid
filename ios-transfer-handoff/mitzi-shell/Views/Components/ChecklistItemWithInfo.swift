import SwiftUI
import UIKit

struct CustomCheckboxToggleStyle: ToggleStyle {
    func makeBody(configuration: Configuration) -> some View {
        Button(action: {
            configuration.isOn.toggle()
        }) {
            Image(systemName: configuration.isOn ? "checkmark.square.fill" : "square")
                .foregroundColor(
                    configuration.isOn ? AppColor.navyPrimary : AppColor.goldBorder.opacity(0.62)
                )
                .symbolRenderingMode(.monochrome)
                .imageScale(.large)
        }
    }
}

struct ChecklistItemWithInfo: View {
    let text: String
    let explanation: String
    let link: ExternalLink?
    let isChecked: Bool
    let onToggle: (Bool) -> Void
    @State private var showingInfo = false
    @Environment(\.checklistTextSize) private var textSize

    var body: some View {
        HStack(alignment: .center, spacing: 6) {
            Toggle("", isOn: Binding(
                get: { isChecked },
                set: {
                    HapticFeedback.checkboxToggle()
                    onToggle($0)
                }
            ))
            .toggleStyle(CustomCheckboxToggleStyle())
            .labelsHidden()

            Text(text)
                .font(.system(size: textSize, weight: .regular, design: .serif))
                .foregroundColor(isChecked ? AppColor.textMuted : AppColor.textPrimary)
                .fixedSize(horizontal: false, vertical: true)

            Spacer(minLength: 4)

            Button(action: { showingInfo = true }) {
                Image(systemName: "info.circle.fill")
                    .font(.system(size: 18))
                    .foregroundColor(AppColor.goldBorder.opacity(0.72))
            }
            .buttonStyle(.plain)
            .accessibilityLabel("More information")
        }
        .padding(.vertical, 6)
        .padding(.horizontal, 2)
        .sheet(isPresented: $showingInfo) {
            InfoDialog(text: text, explanation: explanation, link: link)
        }
    }
} 