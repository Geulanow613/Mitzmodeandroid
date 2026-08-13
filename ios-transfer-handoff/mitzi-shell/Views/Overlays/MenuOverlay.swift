import SwiftUI

/// Android `DropdownMenu`: parchment panel under the menu chip + dim scrim (tap outside closes).
struct MenuOverlay: View {
    @Binding var isPresented: Bool
    @Binding var showingAbout: Bool
    @Binding var showingBirkatHamazon: Bool
    @Binding var showingTefilatHaderech: Bool
    @Binding var showingBrachot: Bool
    @Binding var showingMediaPlayer: Bool

    var body: some View {
        ZStack(alignment: .topLeading) {
            AppColor.scrim
                .ignoresSafeArea()
                .onTapGesture {
                    withAnimation { isPresented = false }
                }

            AndroidParityDropdownPanel(
                isOpen: $isPresented,
                onAbout: { showingAbout = true },
                onBirkatHamazon: { showingBirkatHamazon = true },
                onTefilat: { showingTefilatHaderech = true },
                onBrachot: { showingBrachot = true },
                onSong: { showingMediaPlayer = true }
            )
            .padding(.top, AppChromeLayout.menuDropdownTopY)
            .padding(.leading, AppChromeLayout.menuLeading)
        }
    }
}

#if DEBUG
struct MenuOverlay_Previews: PreviewProvider {
    static var previews: some View {
        MenuOverlay(
            isPresented: .constant(true),
            showingAbout: .constant(false),
            showingBirkatHamazon: .constant(false),
            showingTefilatHaderech: .constant(false),
            showingBrachot: .constant(false),
            showingMediaPlayer: .constant(false)
        )
    }
}
#endif
