import SwiftUI

/// In-app Grace After Meals — mirrors Android `BirkatHamazonDialog` (scroll, Hebrew/English toggle, text size, pinch zoom).
struct BirkatHamazonView: View {
    @Environment(\.dismiss) private var dismiss
    @State private var showEnglish = false
    @State private var fontScale: CGFloat = 1.0
    @GestureState private var pinchScale: CGFloat = 1.0
    @State private var expandedBirkatSectionIndices: Set<Int> = []
    @State private var selectedPrefatoryDay: PrefatoryDayKind? = nil

    private let sections = BirkatHamazonText.sections

    private var scaledFont: CGFloat {
        (16 * fontScale * pinchScale).clamped(to: 10...54)
    }

    var body: some View {
        ZStack {
            LinearGradient(
                colors: [AppColor.parchmentTop, AppColor.parchmentMid, AppColor.parchmentBase],
                startPoint: .top,
                endPoint: .bottom
            )
            .ignoresSafeArea()

            VStack(spacing: 0) {
                MenuSheetHeaderBar(title: "ברכת המזון", onClose: { dismiss() })
                MenuSheetGoldHairline()

                HStack {
                        Button(showEnglish ? "Hide English" : "Show English") {
                            showEnglish.toggle()
                        }
                        .font(.system(size: 15, weight: .medium))
                        .foregroundColor(AppColor.navyPrimary)

                        Spacer()

                        HStack(spacing: 4) {
                            Button("A−") { fontScale = max(0.5, fontScale - 0.2) }
                                .font(.system(size: 16, weight: .semibold))
                                .foregroundColor(AppColor.textPrimary.opacity(0.7))
                                .frame(width: 36, height: 36)
                            Button("A+") { fontScale = min(3.0, fontScale + 0.2) }
                                .font(.system(size: 16, weight: .semibold))
                                .foregroundColor(AppColor.textPrimary.opacity(0.7))
                                .frame(width: 36, height: 36)
                        }
                    }
                    .padding(.horizontal, 16)
                    .padding(.bottom, 8)

                    ScrollView {
                        VStack(alignment: .leading, spacing: 0) {
                            prefatoryTehillimSection
                                .padding(.bottom, 14)

                            ForEach(Array(sections.enumerated()), id: \.offset) { index, section in
                                birkatSectionBlock(index: index, section: section)
                            }
                        }
                        .padding(.horizontal, 16)
                        .padding(.bottom, 24)
                    }
                    .onAppear {
                        selectedPrefatoryDay = nil
                    }
                    .simultaneousGesture(
                        MagnificationGesture()
                            .updating($pinchScale) { value, state, _ in
                                state = value
                            }
                            .onEnded { magnification in
                                fontScale = (fontScale * magnification).clamped(to: 0.5...3.0)
                            }
                    )
            }
        }
    }

    @ViewBuilder
    private func birkatSectionBlock(index: Int, section: BirkatHamazonSection) -> some View {
        let collapsible = section.isCollapsible
        let expanded = expandedBirkatSectionIndices.contains(index)

        VStack(alignment: .leading, spacing: 0) {
            if collapsible && !expanded {
                Button(action: { expandedBirkatSectionIndices.insert(index) }) {
                    VStack(alignment: .leading, spacing: 8) {
                        HStack(alignment: .firstTextBaseline, spacing: 8) {
                            Image(systemName: "chevron.right")
                                .font(.system(size: 12, weight: .semibold))
                                .foregroundColor(AppColor.navyPrimary)
                            Text(section.title)
                                .font(.system(size: scaledFont * 0.92, weight: .semibold, design: .serif))
                                .foregroundColor(AppColor.navyPrimary)
                                .multilineTextAlignment(.leading)
                        }
                        if let hint = section.collapsedSummaryHebrew {
                            Text(hint)
                                .font(.system(size: scaledFont * 0.88, weight: .regular, design: .serif))
                                .foregroundColor(AppColor.textPrimary)
                                .multilineTextAlignment(.trailing)
                                .frame(maxWidth: .infinity, alignment: .trailing)
                        }
                        if showEnglish, let hintEn = section.collapsedSummaryEnglish {
                            Text(hintEn)
                                .font(.system(size: scaledFont * 0.82, weight: .regular, design: .serif))
                                .foregroundColor(AppColor.textMuted)
                                .multilineTextAlignment(.leading)
                                .frame(maxWidth: .infinity, alignment: .leading)
                        }
                    }
                    .padding(.vertical, 12)
                    .padding(.horizontal, 12)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .background(AppColor.goldBorder.opacity(0.07))
                    .overlay(
                        RoundedRectangle(cornerRadius: 10, style: .continuous)
                            .stroke(AppColor.goldBorder.opacity(0.4), lineWidth: 1)
                    )
                    .cornerRadius(10)
                }
                .buttonStyle(.plain)
                .padding(.vertical, 6)
            } else {
                VStack(alignment: .leading, spacing: 6) {
                    if collapsible && expanded {
                        Button(action: { expandedBirkatSectionIndices.remove(index) }) {
                            HStack {
                                Text(section.title)
                                    .font(.system(size: scaledFont * 0.95, weight: .semibold, design: .serif))
                                    .foregroundColor(AppColor.navyPrimary)
                                Spacer()
                                Image(systemName: "chevron.up")
                                    .font(.system(size: 11, weight: .semibold))
                                    .foregroundColor(AppColor.navyPrimary)
                                Text("צמצם")
                                    .font(.system(size: 13, weight: .medium, design: .serif))
                                    .foregroundColor(AppColor.navyPrimary)
                            }
                        }
                        .buttonStyle(.plain)
                        .padding(.bottom, 4)
                    }

                    if let groups = section.zimmunLineGroups {
                        if let preamble = section.zimmunPreambleEnglish {
                            Text(preamble)
                                .font(.system(size: scaledFont * 0.86, weight: .regular, design: .default))
                                .foregroundColor(AppColor.textPrimary.opacity(0.92))
                                .multilineTextAlignment(.leading)
                                .frame(maxWidth: .infinity, alignment: .leading)
                                .padding(.bottom, 12)
                        }

                        ForEach(Array(groups.enumerated()), id: \.offset) { _, group in
                            VStack(alignment: .leading, spacing: 8) {
                                Text(group.englishGuide)
                                    .font(.system(size: scaledFont * 0.84, weight: .semibold, design: .default))
                                    .foregroundColor(AppColor.textMuted)
                                    .multilineTextAlignment(.leading)
                                    .frame(maxWidth: .infinity, alignment: .leading)

                                Text(group.hebrew)
                                    .font(.system(size: scaledFont, weight: .regular, design: .serif))
                                    .foregroundColor(AppColor.textPrimary)
                                    .multilineTextAlignment(.trailing)
                                    .frame(maxWidth: .infinity, alignment: .trailing)

                                if showEnglish {
                                    Text(group.englishLine)
                                        .font(.system(size: scaledFont * 0.92, weight: .regular, design: .default))
                                        .foregroundColor(AppColor.textPrimary.opacity(0.9))
                                        .multilineTextAlignment(.leading)
                                        .frame(maxWidth: .infinity, alignment: .leading)
                                }
                            }
                            .padding(.vertical, 10)
                            .padding(.horizontal, 2)
                            .overlay(alignment: .bottom) {
                                Rectangle()
                                    .fill(AppColor.goldBorder.opacity(0.15))
                                    .frame(height: 1)
                            }
                        }
                        .padding(.bottom, 4)
                    } else {
                        if let intro = section.hebrewIntro {
                            Text(intro)
                                .font(.system(size: scaledFont * 0.92, weight: .regular, design: .serif))
                                .foregroundColor(AppColor.textPrimary)
                                .multilineTextAlignment(.trailing)
                                .frame(maxWidth: .infinity, alignment: .trailing)
                                .padding(.top, 2)
                                .padding(.bottom, 4)
                        }

                        if let enExpl = section.englishExplanatoryIntro {
                            Text(enExpl)
                                .font(.system(size: scaledFont * 0.88, weight: .regular, design: .default))
                                .foregroundColor(AppColor.textPrimary.opacity(0.88))
                                .multilineTextAlignment(.leading)
                                .frame(maxWidth: .infinity, alignment: .leading)
                                .padding(.bottom, 8)
                        }

                        Text(section.hebrew)
                            .font(.system(size: scaledFont))
                            .foregroundColor(AppColor.textPrimary)
                            .multilineTextAlignment(.trailing)
                            .frame(maxWidth: .infinity, alignment: .trailing)
                            .padding(.vertical, 6)

                        if showEnglish, let en = section.english {
                            Text(en)
                                .font(.system(size: scaledFont * 0.95))
                                .foregroundColor(AppColor.textPrimary)
                                .multilineTextAlignment(.leading)
                                .frame(maxWidth: .infinity, alignment: .leading)
                                .padding(.top, 4)
                                .padding(.bottom, 12)
                        }
                    }
                }
            }

            Rectangle()
                .fill(AppColor.goldBorder.opacity(0.28))
                .frame(height: 0.8)
                .padding(.vertical, 8)
        }
    }

    private enum PrefatoryDayKind: Hashable {
        case withTachanun
        case withoutTachanun
    }

    /// Prefatory Tehillim (cut from `BirkatHamazonText.sections`): full text only after day type is chosen.
    private var prefatoryTehillimSection: some View {
        VStack(alignment: .leading, spacing: 14) {
            Text("Prefatory Tehillim")
                .font(.system(size: scaledFont * 0.88, weight: .semibold, design: .serif))
                .foregroundColor(AppColor.navyPrimary)

            Text("תהלים לפני ברכת המזון / אחריה — לפי מנהג אשכנזי נפוץ")
                .font(.system(size: scaledFont * 0.78, weight: .regular, design: .serif))
                .foregroundColor(AppColor.textPrimary.opacity(0.85))
                .fixedSize(horizontal: false, vertical: true)

            if showEnglish {
                Text("Psalms before or after Grace After Meals — common Ashkenazi usage.")
                    .font(.system(size: scaledFont * 0.76, weight: .regular, design: .default))
                    .foregroundColor(AppColor.textMuted)
                    .fixedSize(horizontal: false, vertical: true)
            }

            Text("הקשו על סוג היום המתאים — לאחר מכן יוצג כאן פרק התהלים המלא בלבד.")
                .font(.system(size: scaledFont * 0.78, weight: .medium, design: .serif))
                .foregroundColor(AppColor.textPrimary)
                .fixedSize(horizontal: false, vertical: true)

            if showEnglish {
                Text("Tap the row that matches today. The full psalm text appears only after you choose.")
                    .font(.system(size: scaledFont * 0.74, weight: .regular, design: .default))
                    .foregroundColor(AppColor.textMuted)
                    .fixedSize(horizontal: false, vertical: true)
            }

            VStack(spacing: 10) {
                prefatoryDayChoiceRow(
                    kind: .withTachanun,
                    titleEnglish: "Days we recite Tachanun",
                    titleHebrew: "ימים שאומרים בהם תחנון",
                    subtitleHebrew: nil,
                    subtitleEnglish: nil
                )
                prefatoryDayChoiceRow(
                    kind: .withoutTachanun,
                    titleEnglish: "Days we do not recite Tachanun",
                    titleHebrew: "ימים שאין אומרים בהם תחנון",
                    subtitleHebrew: "למשל ימים חגיגיים, ראש חודש וכו׳",
                    subtitleEnglish: "e.g. festive days, Rosh Chodesh, etc."
                )
            }

            if let day = selectedPrefatoryDay {
                Rectangle()
                    .fill(AppColor.goldBorder.opacity(0.35))
                    .frame(height: 1)
                    .padding(.vertical, 4)

                prefatoryPsalmBody(for: day)
                    .id(day)

                Button {
                    withAnimation(.easeInOut(duration: 0.2)) { selectedPrefatoryDay = nil }
                } label: {
                    Text("בחר שוב / Choose again")
                        .font(.system(size: scaledFont * 0.78, weight: .semibold, design: .serif))
                        .foregroundColor(AppColor.navyPrimary)
                        .frame(maxWidth: .infinity)
                        .padding(.vertical, 10)
                }
                .buttonStyle(.plain)
                .background(AppColor.goldBorder.opacity(0.08))
                .overlay(
                    RoundedRectangle(cornerRadius: 8, style: .continuous)
                        .stroke(AppColor.goldBorder.opacity(0.35), lineWidth: 1)
                )
                .clipShape(RoundedRectangle(cornerRadius: 8, style: .continuous))
            }
        }
        .padding(.vertical, 12)
        .padding(.horizontal, 12)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(AppColor.goldBorder.opacity(0.06))
        .overlay(
            RoundedRectangle(cornerRadius: 10, style: .continuous)
                .stroke(AppColor.goldBorder.opacity(0.35), lineWidth: 1)
        )
        .clipShape(RoundedRectangle(cornerRadius: 10, style: .continuous))
    }

    private func prefatoryDayChoiceRow(
        kind: PrefatoryDayKind,
        titleEnglish: String,
        titleHebrew: String,
        subtitleHebrew: String?,
        subtitleEnglish: String?
    ) -> some View {
        let selected = selectedPrefatoryDay == kind
        return Button {
            withAnimation(.easeInOut(duration: 0.22)) { selectedPrefatoryDay = kind }
        } label: {
            VStack(alignment: .leading, spacing: 6) {
                HStack(alignment: .firstTextBaseline, spacing: 8) {
                    Image(systemName: selected ? "checkmark.circle.fill" : "circle")
                        .font(.system(size: 16, weight: .semibold))
                        .foregroundColor(selected ? AppColor.navyPrimary : AppColor.textPrimary.opacity(0.35))
                    Text(titleEnglish)
                        .font(.system(size: scaledFont * 0.84, weight: .semibold, design: .default))
                        .foregroundColor(AppColor.navyPrimary)
                        .multilineTextAlignment(.leading)
                }
                Text(titleHebrew)
                    .font(.system(size: scaledFont * 0.82, weight: .semibold, design: .serif))
                    .foregroundColor(AppColor.textPrimary)
                    .multilineTextAlignment(.trailing)
                    .frame(maxWidth: .infinity, alignment: .trailing)
                if let she = subtitleHebrew {
                    Text(she)
                        .font(.system(size: scaledFont * 0.76, weight: .regular, design: .serif))
                        .foregroundColor(AppColor.textPrimary.opacity(0.88))
                        .multilineTextAlignment(.trailing)
                        .frame(maxWidth: .infinity, alignment: .trailing)
                }
                if showEnglish, let sen = subtitleEnglish {
                    Text(sen)
                        .font(.system(size: scaledFont * 0.72, weight: .regular, design: .default))
                        .foregroundColor(AppColor.textMuted)
                        .multilineTextAlignment(.leading)
                }
            }
            .padding(.vertical, 12)
            .padding(.horizontal, 12)
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(AppColor.goldBorder.opacity(selected ? 0.14 : 0.07))
            .overlay(
                RoundedRectangle(cornerRadius: 10, style: .continuous)
                    .stroke(AppColor.goldBorder.opacity(selected ? 0.55 : 0.38), lineWidth: selected ? 2 : 1)
            )
            .clipShape(RoundedRectangle(cornerRadius: 10, style: .continuous))
        }
        .buttonStyle(.plain)
    }

    @ViewBuilder
    private func prefatoryPsalmBody(for day: PrefatoryDayKind) -> some View {
        let section: BirkatHamazonSection = {
            switch day {
            case .withTachanun:
                return BirkatHamazonText.prefatoryAlNaharotBavel
            case .withoutTachanun:
                return BirkatHamazonText.prefatoryShirHamaalosBeshuv
            }
        }()

        VStack(alignment: .leading, spacing: 10) {
            Text(section.title)
                .font(.system(size: scaledFont * 0.9, weight: .bold, design: .serif))
                .foregroundColor(AppColor.navyPrimary)
                .frame(maxWidth: .infinity, alignment: .trailing)

            Text(section.hebrew)
                .font(.system(size: scaledFont * 0.92, weight: .regular, design: .serif))
                .foregroundColor(AppColor.textPrimary)
                .multilineTextAlignment(.trailing)
                .frame(maxWidth: .infinity, alignment: .trailing)
                .fixedSize(horizontal: false, vertical: true)

            if showEnglish, let en = section.english {
                Text(en)
                    .font(.system(size: scaledFont * 0.88, weight: .regular, design: .serif))
                    .foregroundColor(AppColor.textPrimary)
                    .multilineTextAlignment(.leading)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .fixedSize(horizontal: false, vertical: true)
                    .padding(.top, 6)
            }
        }
        .padding(.vertical, 10)
        .padding(.horizontal, 10)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(AppColor.goldBorder.opacity(0.05))
        .overlay(
            RoundedRectangle(cornerRadius: 10, style: .continuous)
                .stroke(AppColor.goldBorder.opacity(0.32), lineWidth: 1)
        )
        .clipShape(RoundedRectangle(cornerRadius: 10, style: .continuous))
    }
}

private extension CGFloat {
    func clamped(to range: ClosedRange<CGFloat>) -> CGFloat {
        Swift.min(Swift.max(self, range.lowerBound), range.upperBound)
    }
}
