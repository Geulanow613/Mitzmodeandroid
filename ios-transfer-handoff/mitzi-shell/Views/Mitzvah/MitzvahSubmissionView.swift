import SwiftUI
import Combine
import Network

struct MitzvahSubmissionView: View {
    @Binding var isPresented: Bool
    @StateObject private var viewModel = MitzvahSubmissionViewModel()
    @StateObject private var networkMonitor = NetworkMonitor()
    @State private var keyboardHeight: CGFloat = 0

    var body: some View {
        ZStack {
            LinearGradient(
                colors: [AppColor.parchmentTop, AppColor.parchmentMid, AppColor.parchmentBase],
                startPoint: .top,
                endPoint: .bottom
            )
            .ignoresSafeArea()

            VStack(spacing: 0) {
                MenuSheetHeaderBar(title: "Submit a Mitzvah", onClose: { isPresented = false })
                MenuSheetGoldHairline()

                if !networkMonitor.isOnline {
                    offlineView
                } else {
                    ScrollView {
                        VStack(spacing: 16) {
                            explanationText
                            nameInput
                            suggestionInput
                            submitButton
                            Spacer(minLength: keyboardHeight + 20)
                        }
                        .padding(.horizontal, 8)
                        .padding(.vertical, 12)
                    }
                    .simultaneousGesture(DragGesture().onChanged { _ in
                        UIApplication.shared.sendAction(
                            #selector(UIResponder.resignFirstResponder),
                            to: nil,
                            from: nil,
                            for: nil
                        )
                    })
                }
            }
        }
        .alert("Thank You!", isPresented: $viewModel.showingConfirmation) {
            Button("OK") { isPresented = false }
        } message: {
            Text("Your mitzvah suggestion has been received. We appreciate your contribution to the community!")
        }
        .onAppear {
            networkMonitor.startMonitoring()
            setupKeyboardNotifications()
        }
        .onDisappear {
            networkMonitor.stopMonitoring()
            removeKeyboardNotifications()
        }
    }

    private var offlineView: some View {
        VStack(spacing: 16) {
            Image(systemName: "wifi.slash")
                .font(.system(size: 48))
                .foregroundColor(AppColor.goldBorder.opacity(0.85))
                .padding(.top, 20)

            TranslatableText(
                source: "Internet connection required",
                font: .system(size: 18, weight: .semibold, design: .serif),
                weight: .semibold,
                color: AppColor.textPrimary,
                alignment: .center
            )

            TranslatableText(
                source: "Please check your connection and try again",
                font: .system(size: 15, weight: .regular, design: .serif),
                color: AppColor.textMuted,
                alignment: .center
            )
                .padding(.horizontal, 20)

            Button(action: { isPresented = false }) {
                TranslatableText(
                    source: "Close",
                    font: .system(size: 16, weight: .semibold, design: .serif),
                    weight: .semibold,
                    color: AppColor.textOnDark,
                    alignment: .center
                )
                    .padding(.vertical, 10)
                    .padding(.horizontal, 28)
                    .background(
                        Capsule()
                            .fill(
                                LinearGradient(
                                    colors: [AppColor.navyPrimary, AppColor.navyDeep],
                                    startPoint: .topLeading,
                                    endPoint: .bottomTrailing
                                )
                            )
                            .overlay(Capsule().stroke(AppColor.goldBorder.opacity(0.65), lineWidth: 1))
                    )
            }
            .buttonStyle(.plain)
            .padding(.bottom, 24)
        }
        .frame(maxWidth: .infinity)
    }

    private var explanationText: some View {
        VStack(spacing: 8) {
            TranslatableText(
                source: "Have a great mitzvah idea? Share it with other users!",
                font: .system(size: 16, weight: .regular, design: .serif),
                color: AppColor.textPrimary,
                alignment: .center
            )
            .padding(.horizontal, 8)
        }
    }

    private var nameInput: some View {
        VStack(alignment: .leading, spacing: 8) {
            TranslatableText(
                source: "Your Name/Initials (Optional)",
                font: .system(size: 15, weight: .semibold, design: .serif),
                weight: .semibold,
                color: AppColor.goldBorder,
                alignment: .leading
            )
            TextField("Enter your name", text: $viewModel.name)
                .font(.system(size: 16, weight: .regular, design: .serif))
                .foregroundColor(AppColor.textPrimary)
                .padding(12)
                .background(
                    RoundedRectangle(cornerRadius: 10, style: .continuous)
                        .fill(Color.white.opacity(0.38))
                )
                .overlay(
                    RoundedRectangle(cornerRadius: 10, style: .continuous)
                        .stroke(AppColor.goldBorder.opacity(0.42), lineWidth: 1)
                )
                .disableAutocorrection(true)
                .autocapitalization(.words)
                .submitLabel(.next)
                .onChange(of: viewModel.name) { _ in
                    viewModel.validateInput()
                }
        }
        .padding(.horizontal, 12)
    }

    private var suggestionInput: some View {
        VStack(alignment: .leading, spacing: 8) {
            TranslatableText(
                source: "Mitzvah Description",
                font: .system(size: 15, weight: .semibold, design: .serif),
                weight: .semibold,
                color: AppColor.goldBorder,
                alignment: .leading
            )
            TextEditor(text: $viewModel.description)
                .font(.system(size: 16, weight: .regular, design: .serif))
                .foregroundColor(AppColor.textPrimary)
                .frame(minHeight: 120)
                .padding(10)
                .background(
                    RoundedRectangle(cornerRadius: 10, style: .continuous)
                        .fill(Color.white.opacity(0.38))
                )
                .overlay(
                    RoundedRectangle(cornerRadius: 10, style: .continuous)
                        .stroke(AppColor.goldBorder.opacity(0.42), lineWidth: 1)
                )

            if let error = viewModel.errorMessage {
                Text(error)
                    .font(.system(size: 14, weight: .medium, design: .serif))
                    .foregroundColor(Color(red: 0.65, green: 0.12, blue: 0.12))
            }
        }
        .padding(.horizontal, 12)
    }

    private var submitButton: some View {
        ZStack {
            TranslatableHeirloomButton(source: "Submit", enabled: viewModel.canSubmit && !viewModel.isSubmitting) {
                Task { await viewModel.submitSuggestion() }
            }
            if viewModel.isSubmitting {
                ProgressView()
                    .progressViewStyle(CircularProgressViewStyle(tint: AppColor.textOnDark))
                    .allowsHitTesting(false)
            }
        }
        .padding(.horizontal, 12)
    }

    private func setupKeyboardNotifications() {
        NotificationCenter.default.addObserver(forName: UIResponder.keyboardWillShowNotification, object: nil, queue: .main) { notification in
            if let keyboardFrame = notification.userInfo?[UIResponder.keyboardFrameEndUserInfoKey] as? CGRect {
                keyboardHeight = keyboardFrame.height
            }
        }

        NotificationCenter.default.addObserver(forName: UIResponder.keyboardWillHideNotification, object: nil, queue: .main) { _ in
            keyboardHeight = 0
        }
    }

    private func removeKeyboardNotifications() {
        NotificationCenter.default.removeObserver(self, name: UIResponder.keyboardWillShowNotification, object: nil)
        NotificationCenter.default.removeObserver(self, name: UIResponder.keyboardWillHideNotification, object: nil)
    }
}
